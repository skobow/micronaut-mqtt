/*
 * Copyright 2017-2023 original authors
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

import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public interface TrustManagerFactoryProvider {
    default TrustManagerFactory getTrustManagerFactory(final MqttCertificateConfiguration certConfiguration) throws TrustManagerFactoryCreationException {
        try {
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            final Certificate certificate = CertificateReader.readCertificate(certConfiguration.getCertificateAuthority());

            keyStore.load(null);
            keyStore.setCertificateEntry("ca-certificate", certificate);

            tmf.init(keyStore);

            return tmf;
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException e) {
            throw new TrustManagerFactoryCreationException(e.getMessage(), e);
        }
    }
}
