package com.clearcapital.oss.solrj;

import static junit.framework.TestCase.assertNull;

import java.net.URISyntaxException;

import javax.net.ssl.SSLContext;

import org.junit.Test;

import com.clearcapital.oss.solrj.configuration.SolrEncryptionConfiguration;
import com.google.common.io.Resources;

public class SSLContextFactoryTest {

    @Test
    public void testValidSettings() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra");
        config.setUseEncryption(true);
        final SSLContext sslContext = new SSLContextFactory(config).create();

        sslContext.createSSLEngine();
    }

    @Test
    public void testEncryptionOff() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra");
        config.setUseEncryption(false);
        assertNull(new SSLContextFactory(config).create());
    }

    @Test
    public void testBadKeyStorePassword() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra!");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra");
        config.setUseEncryption(true);
        final SSLContext sslContext = new SSLContextFactory(config).create();
        assertNull(sslContext);
    }
    @Test
    public void testBadTrustStorePassword() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra!");
        config.setUseEncryption(true);
        final SSLContext sslContext = new SSLContextFactory(config).create();
        assertNull(sslContext);
    }

    @Test
    public void testBadKeyStorePath() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath("/does/not/exist");
        config.setKeyStorePassword("cassandra");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra!");
        config.setUseEncryption(true);
        final SSLContext sslContext = new SSLContextFactory(config).create();
        assertNull(sslContext);
    }

    @Test
    public void testBadTrustStorePath() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra");
        config.setTrustStorePath("/does/not/exist");
        config.setTrustStorePassword("cassandra!");
        config.setUseEncryption(true);
        final SSLContext sslContext = new SSLContextFactory(config).create();
        assertNull(sslContext);
    }




}
