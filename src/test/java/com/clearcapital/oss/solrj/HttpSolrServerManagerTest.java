package com.clearcapital.oss.solrj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HttpSolrServerManagerTest {

    @Test
    public void testBaseUrl() {
        final HttpSolrServerManager httpSolrServerManager = new HttpSolrServerManager("http://foobar:8983");
        assertEquals("http://foobar:8983/keyspace.core", httpSolrServerManager.get("keyspace.core").getBaseURL());
        assertEquals("http://foobar:8983/keyspace2.core2", httpSolrServerManager.get("keyspace2.core2").getBaseURL());
        assertEquals("http://foobar:8983/keyspace.properties", httpSolrServerManager.get("keyspace.properties").getBaseURL());
        assertEquals("http://foobar:8983/keyspace2.core2", httpSolrServerManager.get("keyspace2.core2").getBaseURL());
    }

}
