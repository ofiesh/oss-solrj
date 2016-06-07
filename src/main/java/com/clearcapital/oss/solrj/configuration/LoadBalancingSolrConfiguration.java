package com.clearcapital.oss.solrj.configuration;

import java.util.Collection;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * The {@link LoadBalancingSolrConfiguration} provides a list of hosts to load balance against and general solr
 * configuration settings
 */
public class LoadBalancingSolrConfiguration {

    /**
     * List of hosts with in the form of protocol://fqdn:port/solr
     *
     * i.e. https://solrserver:8983:solr
     */
    private Collection<String> hosts;
    private SolrConfiguration solrConfiguration;

    public Collection<String> getHosts() {
        return hosts;
    }

    public void setHosts(final Collection<String> hosts) {
        this.hosts = hosts;
    }

    public SolrConfiguration getSolrConfiguration() {
        return solrConfiguration;
    }

    public void setSolrConfiguration(final SolrConfiguration solrConfiguration) {
        this.solrConfiguration = solrConfiguration;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("hosts", hosts).add("solrConfiguration", solrConfiguration)
                .toString();
    }
}
