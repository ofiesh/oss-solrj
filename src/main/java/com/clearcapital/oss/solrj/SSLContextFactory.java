package com.clearcapital.oss.solrj;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearcapital.oss.solrj.configuration.SolrEncryptionConfiguration;

public class SSLContextFactory {

    private final Logger logger = LoggerFactory.getLogger(SSLContextFactory.class);
    private final SolrEncryptionConfiguration config;

    public SSLContextFactory(final SolrEncryptionConfiguration config) {
        this.config = config;
    }

    public SSLContext create() {
        if (config.isUseEncryption() && config.getKeyStorePath() != null && config.getKeyStorePassword() != null
                && config.getTrustStorePath() != null && config.getTrustStorePassword() != null) {
            logger.debug("Creating SSLContext with config: " + config);
            try {
                FileInputStream trustStoreFis = new FileInputStream(config.getTrustStorePath());
                FileInputStream keyStoreFis = new FileInputStream(config.getKeyStorePath());
                SSLContext ctx = SSLContext.getInstance("SSL");

                KeyStore trustStore = KeyStore.getInstance("JKS");
                trustStore.load(trustStoreFis, config.getTrustStorePassword().toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                        .getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);

                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(keyStoreFis, config.getKeyStorePassword().toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory
                        .getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, config.getKeyStorePassword().toCharArray());

                ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom
                        ());

                return ctx;
            } catch (Exception e) {
                logger.error("Unable to setup SSLContext", e);
            }
        } else {
            logger.debug("Skipping SSL context, config not set up for encryption: " + config);
        }
        return null;
    }

}
