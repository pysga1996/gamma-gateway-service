package com.gamma.config.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @author thanhvt
 * @created 25/04/2021 - 12:16:33 SA
 * @project vengeance
 * @description
 * @since 1.0
 **/
@Log4j2
@Configuration
public class SSLConfig {

    @Value("classpath:vengeance.jks")
    private Resource trustStore;

    @Value("${custom.trust-store-password}")
    private String trustStorePassword;

    @Value("${custom.trust-store-type}")
    private String trustStoreType;
    //    static {
//        //for localhost testing only
////        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
////                (hostname, sslSession) -> hostname.equals("localhost");
//        System.setProperty("https.protocols", "TLSv1.2");
//        ClassPathResource resource = new ClassPathResource("vengeance.jks");
//        try {
//            System.setProperty("javax.net.ssl.trustStore", Paths.get(resource.getURI()).toString());
//            System.setProperty("javax.net.ssl.trustStorePassword", "1381996");
//            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
//        } catch (IOException e) {
//            log.error(e);
//            System.exit(1);
//        }
//    }

//    @PostConstruct
//    private void configureSSL() throws IOException {
////        System.setProperty("javax.net.debug", "ssl");
//        System.setProperty("https.protocols", "TLSv1.2");
//        System.setProperty("javax.net.ssl.trustStore", Paths.get(trustStore.getURI()).toString());
//        System.setProperty("javax.net.ssl.trustStorePassword", Objects.requireNonNull(trustStorePassword));
//        System.setProperty("javax.net.ssl.trustStoreType", Objects.requireNonNull(trustStoreType));
//    }

    @PostConstruct
    public SSLContext customSSL() {
        try {
            HttpsURLConnection
                .setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
            //Gets the inputstream of a a trust store file under ssl/rpgrenadesClient.jks
            //This path refers to the ssl folder in the jar file, in a jar file in the same directory
            //as this jar file, or a different directory in the same directory as the jar file
            InputStream stream = this.trustStore.getInputStream();
            //Both trustStores and keyStores are represented by the KeyStore object
            KeyStore trustStore = KeyStore.getInstance(this.trustStoreType.toLowerCase());
            //The password for the trustStore
            //This loads the trust store into the object
            trustStore.load(stream, this.trustStorePassword.toCharArray());
            //This is defining the SSLContext so the trust store will be used
            //Getting default SSLContext to edit.
            SSLContext context = SSLContext.getInstance("SSL");
            //TrustMangers hold trust stores, more than one can be added
            TrustManagerFactory factory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //Adds the truststore to the factory
            factory.init(trustStore);
            //This is passed to the SSLContext init method
            TrustManager[] managers = factory.getTrustManagers();
            context.init(null, managers, null);
            //Sets our new SSLContext to be used.
            SSLContext.setDefault(context);
            return context;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException
            | CertificateException | KeyManagementException ex) {
            //Handle error
            log.error(ex);
            return null;
        }
    }
}
