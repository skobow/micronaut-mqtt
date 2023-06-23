/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.mqtt.ssl;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Readable;

/**
 * MQTT client SSL configuration.
 *
 * @author James Kleeh
 * @since 1.0.0
 */
@ConfigurationProperties("mqtt.client.ssl")
public class MqttSslConfiguration {

    private boolean enabled = false;
    private Readable certificateAuthority;
    private Readable certificate;
    private Readable privateKey;
    private char[] password;

    /**
     * @return True if SSL should be used for securing connections.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled If connections should be secured with SSL.
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return The certificate authority
     */
    public Readable getCertificateAuthority() {
        return certificateAuthority;
    }

    /**
     * @param certificateAuthority The certificate authority location
     */
    public void setCertificateAuthority(@Nullable final Readable certificateAuthority) {
        this.certificateAuthority = certificateAuthority;
    }

    /**
     * @return The certificate
     */
    public Readable getCertificate() {
        return certificate;
    }

    /**
     * @param certificate The client certificate location
     */
    public void setCertificate(@Nullable final Readable certificate) {
        this.certificate = certificate;
    }

    /**
     * @return The client key
     */
    public Readable getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey The client private key location
     */
    public void setPrivateKey(@Nullable final Readable privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return The key password
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * @param password The key password
     */
    public void setPassword(@Nullable final char[] password) {
        this.password = password;
    }
}
