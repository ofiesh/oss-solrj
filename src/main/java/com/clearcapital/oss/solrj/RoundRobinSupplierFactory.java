package com.clearcapital.oss.solrj;

import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearcapital.oss.solrj.configuration.LoadBalancingSolrConfiguration;

/**
 * The {@link RoundRobinSupplierFactory} creates a {@link RoundRobinSupplier<HttpSolrServer>} for a given
 * {@link LoadBalancingSolrConfiguration}.
 */
public class RoundRobinSupplierFactory {

    private final Logger logger = LoggerFactory.getLogger(RoundRobinSupplier.class);

    /**
     *
     * @param config The {@link LoadBalancingSolrConfiguration} to configure the {@link RoundRobinSupplier} against.
     * @return Returns a configured {@link RoundRobinSupplier<HttpSolrServer>}.
     */
    public RoundRobinSupplier<HttpSolrServer> create(LoadBalancingSolrConfiguration config) {
        logger.debug("Creating RoundRobbinSupplier with config " + config);

        Set<HttpSolrServerManager> managers = new HashSet<>();
        if (config != null && config.getHosts() != null) {
            for (final String host : config.getHosts()) {
                managers.add(new HttpSolrServerManager(host, new HttpSolrServerFactory(config.getSolrConfiguration())));
            }
        } else {
            logger.warn("No hosts in LoadBalancingSolrConfiguration!");
        }

        return new RoundRobinSupplier<>(managers);
    }
}
