package com.clearcapital.oss.solrj;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 * The {@link HttpSolrServerManagerRemovePredicate} is a {@link RemovePredicate} to remove a
 * {@link HttpSolrServerManager} from a {@link RoundRobinSupplier<HttpSolrServerManager>} given a {@link String} url.
 */
public class HttpSolrServerManagerRemovePredicate implements RemovePredicate<HttpSolrServer> {

    private final String baseUrl;

    public HttpSolrServerManagerRemovePredicate(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean matches(final Supplier<HttpSolrServer> t) {
        return t instanceof HttpSolrServerManager && ((HttpSolrServerManager) t).getBaseUrl().equalsIgnoreCase(baseUrl);
    }
}
