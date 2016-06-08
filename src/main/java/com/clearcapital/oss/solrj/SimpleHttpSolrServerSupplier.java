package com.clearcapital.oss.solrj;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SimpleHttpSolrServerSupplier implements Supplier<HttpSolrServer> {

    private final String baseUrl;

    public SimpleHttpSolrServerSupplier(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HttpSolrServer get(String core) {
        return new HttpSolrServer(baseUrl + "/" + core);
    }
}
