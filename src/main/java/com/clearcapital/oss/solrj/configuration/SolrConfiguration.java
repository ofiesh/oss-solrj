package com.clearcapital.oss.solrj.configuration;

/**
 * The {@link SolrConfiguration} class provides solr client configuration options and encryption configuration
 */
public class SolrConfiguration {

    private boolean followRedirects;
    private SolrEncryptionConfiguration solrEncryptionConfiguration;

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public SolrEncryptionConfiguration getSolrEncryptionConfiguration() {
        return solrEncryptionConfiguration;
    }

    public void setSolrEncryptionConfiguration(final SolrEncryptionConfiguration solrEncryptionConfiguration) {
        this.solrEncryptionConfiguration = solrEncryptionConfiguration;
    }
}
