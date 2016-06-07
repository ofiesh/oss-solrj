package com.clearcapital.oss.solrj;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.clearcapital.oss.solrj.configuration.SolrConfiguration;

/**
 * The {@link HttpSolrServerFactory} creates a {@link HttpSolrServer} from the optionally supplied configuration.
 */
public class HttpSolrServerFactory {

    private final SolrConfiguration solrConfiguration;
    private final SSLContextFactory sslContextFactory;

    /**
     * Creates the HttpSolrServerFactory with no configuration.
     */
    public HttpSolrServerFactory() {
        this(null);
    }

    /**
     * Creates the HttpSolrServerFactory with the {@link SolrConfiguration} settings.
     * @param solrConfiguration solr client configuration settings
     */
    public HttpSolrServerFactory(final SolrConfiguration solrConfiguration) {
        this.solrConfiguration = solrConfiguration;
        if (solrConfiguration != null) {
            sslContextFactory = new SSLContextFactory(solrConfiguration.getSolrEncryptionConfiguration());
        } else {
            sslContextFactory = null;
        }
    }

    /**
     * Creates a {@link HttpSolrServer} with the supplied base url.
     * @param baseUrl The base url for all solr requests through the created solr client.
     * @return Returns a {@link HttpSolrServer} with the given baseUrl configured against the supplied configuration.
     */
    public HttpSolrServer create(String baseUrl) {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("maxConnections", 128);
        params.set("maxConnectionsPerHost", 32);
        if (solrConfiguration != null) {
            params.set("followRedirects", solrConfiguration.isFollowRedirects());
        }
        SystemDefaultHttpClient httpClient = new SystemDefaultHttpClient();
        HttpClientUtil.configureClient(httpClient, params);
        SSLContext sslContext;
        if (sslContextFactory != null && (sslContext = sslContextFactory.create()) != null) {

            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext, new AllowAllHostnameVerifier());
            Scheme httpsScheme = new Scheme("https", 443, sslSocketFactory);
            httpClient.getConnectionManager().getSchemeRegistry().register(httpsScheme);
        }
        return new HttpSolrServer(baseUrl, httpClient, new BinaryResponseParser());
    }
}
