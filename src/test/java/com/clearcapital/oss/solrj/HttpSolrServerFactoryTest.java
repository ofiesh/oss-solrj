package com.clearcapital.oss.solrj;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.params.HttpParams;
import org.junit.Test;

import com.clearcapital.oss.solrj.configuration.SolrConfiguration;
import com.clearcapital.oss.solrj.configuration.SolrEncryptionConfiguration;
import com.google.common.io.Resources;

public class HttpSolrServerFactoryTest {

    @Test
    public void testHttpClientSocketFactory() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra");
        config.setUseEncryption(true);
        SolrConfiguration solrConfiguration = new SolrConfiguration();
        solrConfiguration.setSolrEncryptionConfiguration(config);
        HttpSolrServerFactory factory = new HttpSolrServerFactory(solrConfiguration);
        final Scheme scheme = factory.create("foo").getHttpClient().getConnectionManager()
                .getSchemeRegistry().getScheme("https");
        assertNotNull(scheme.getSchemeSocketFactory());
    }

    @Test
    public void testHttpClientBadSSLConfig() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra!");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra!");
        config.setUseEncryption(true);
        SolrConfiguration solrConfiguration = new SolrConfiguration();
        solrConfiguration.setSolrEncryptionConfiguration(config);
        HttpSolrServerFactory factory = new HttpSolrServerFactory(solrConfiguration);
        final Scheme scheme = factory.create("foo").getHttpClient().getConnectionManager()
                .getSchemeRegistry().getScheme("https");
        assertNotNull(scheme.getSchemeSocketFactory());
    }

    @Test
    public void testFollowRedirects() throws URISyntaxException {
        SolrEncryptionConfiguration config = new SolrEncryptionConfiguration();
        config.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        config.setKeyStorePassword("cassandra!");
        config.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        config.setTrustStorePassword("cassandra!");
        config.setUseEncryption(true);
        SolrConfiguration solrConfiguration = new SolrConfiguration();
        solrConfiguration.setFollowRedirects(true);
        solrConfiguration.setSolrEncryptionConfiguration(config);
        HttpSolrServerFactory factory= new HttpSolrServerFactory(solrConfiguration);
        final HttpParams params = factory.create("foo").getHttpClient().getParams();
        assertTrue((Boolean) params.getParameter("http.protocol.handle-redirects"));
    }

}
