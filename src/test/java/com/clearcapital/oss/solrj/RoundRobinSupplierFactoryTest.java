package com.clearcapital.oss.solrj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Test;

import com.clearcapital.oss.solrj.configuration.LoadBalancingSolrConfiguration;
import com.clearcapital.oss.solrj.configuration.SolrConfiguration;
import com.clearcapital.oss.solrj.configuration.SolrEncryptionConfiguration;
import com.google.common.io.Resources;

public class RoundRobinSupplierFactoryTest {

    @Test
    public void testNullConfig() {
        new RoundRobinSupplierFactory().create(null);

    }

    @Test
    public void testNoHostsConfig() {
        LoadBalancingSolrConfiguration config = new LoadBalancingSolrConfiguration();
        new RoundRobinSupplierFactory().create(config);
    }

    @Test
    public void testHostsConfig() {
        LoadBalancingSolrConfiguration config = new LoadBalancingSolrConfiguration();
        config.setHosts(Arrays.asList("db1", "db2"));
        final RoundRobinSupplier<HttpSolrServer> supplier = new RoundRobinSupplierFactory().create(config);
        String first = supplier.get("core").getBaseURL();
        String second = supplier.get("core").getBaseURL();
        assertNotEquals(first, second);
        assertEquals(first, supplier.get("core").getBaseURL());
        assertEquals(second, supplier.get("core").getBaseURL());
    }

    @Test
    public void testSolrConfig() throws URISyntaxException {
        LoadBalancingSolrConfiguration config = new LoadBalancingSolrConfiguration();
        config.setHosts(Arrays.asList("db1", "db2"));
        SolrConfiguration solrConfiguration = new SolrConfiguration();
        solrConfiguration.setFollowRedirects(true);
        config.setSolrConfiguration(solrConfiguration);
        SolrEncryptionConfiguration encryptionConfiguration = new SolrEncryptionConfiguration();
        encryptionConfiguration.setKeyStorePath(Resources.getResource("certs/keystore").toURI().getPath());
        encryptionConfiguration.setKeyStorePassword("cassandra");
        encryptionConfiguration.setTrustStorePath(Resources.getResource("certs/truststore").toURI().getPath());
        encryptionConfiguration.setTrustStorePassword("cassandra");
        encryptionConfiguration.setUseEncryption(true);
        solrConfiguration.setSolrEncryptionConfiguration(encryptionConfiguration);
        final RoundRobinSupplier<HttpSolrServer> supplier = new RoundRobinSupplierFactory().create(config);
        String first = supplier.get("core").getBaseURL();
        String second = supplier.get("core").getBaseURL();
        assertNotEquals(first, second);
        assertEquals(first, supplier.get("core").getBaseURL());
        assertEquals(second, supplier.get("core").getBaseURL());
    }
}
