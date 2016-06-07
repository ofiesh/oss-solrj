package com.clearcapital.oss.solrj;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 * The {@link HttpSolrServerManager} manages multiple {@link HttpSolrServer} clients to one baseUrl but different solr
 * cores.
 */
public class HttpSolrServerManager implements Supplier<HttpSolrServer> {

    private final String baseUrl;
    private final HttpSolrServerFactory httpSolrServerFactory;
    private Map<String, HttpSolrServer> httpSolrServerMap = new HashMap<>();

    public HttpSolrServerManager(final String baseUrl) {
        this(baseUrl, new HttpSolrServerFactory());
    }

    public HttpSolrServerManager(final String baseUrl, HttpSolrServerFactory httpSolrServerFactory) {
        this.baseUrl = baseUrl;
        this.httpSolrServerFactory = httpSolrServerFactory;
    }

    /**
     * The {@link HttpSolrServerManager} supplies a configured {@link HttpSolrServer} for a given solr core.
     * @param core The solr core to configure the {@link HttpSolrServer} against
     * @return
     */
    @Override
    public synchronized HttpSolrServer get(String core) {
        HttpSolrServer httpSolrServer = httpSolrServerMap.get(core);
        if (httpSolrServer == null) {
            httpSolrServer = httpSolrServerFactory.create(baseUrl + "/" + core);
            httpSolrServerMap.put(core, httpSolrServer);
        }
        return httpSolrServer;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
