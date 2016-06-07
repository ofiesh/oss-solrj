package com.clearcapital.oss.solrj.configuration;

import com.google.common.base.MoreObjects;

/**
 * The {@link SolrEncryptionConfiguration} provides settings to configure the http client to use an SSL socket to connect.
 *
 * {@link SolrEncryptionConfiguration#keyStorePath}, {@link SolrEncryptionConfiguration@keyStorePassword},
 * {@link SolrEncryptionConfiguration#trustStorePath} and {@link SolrEncryptionConfiguration@trustStorePassword} must
 * not be null, and {@link SolrEncryptionConfiguration@useEncription} mut be set to true to enable encryption.
 */
public class SolrEncryptionConfiguration {

    /**
     * flag to turn encryption configuration on.
     */
    private boolean useEncryption = false;
    /**
     * file path to the keyStore file
     */
    private String keyStorePath;
    /**
     * password to the keyStore file
     */
    private String keyStorePassword;
    /**
     * file path to the trustStore file
     */
    private String trustStorePath;
    /**
     * password to the trustStore file
     */
    private String trustStorePassword;

    public boolean isUseEncryption() {
        return useEncryption;
    }

    public void setUseEncryption(final boolean useEncryption) {
        this.useEncryption = useEncryption;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(final String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(final String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(final String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(final String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("useEncryption", useEncryption).add("keyStorePath", keyStorePath)
                .add("keyStorePassword", keyStorePassword).add("trustStorePath", trustStorePath).add(
                        "trustStorePassword", trustStorePassword).toString();
    }
}
