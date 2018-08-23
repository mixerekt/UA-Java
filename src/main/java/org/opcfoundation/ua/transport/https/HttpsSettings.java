/* Copyright (c) 1996-2015, OPC Foundation. All rights reserved.
   The source code in this file is covered under a dual-license scenario:
     - RCL: for OPC Foundation members in good-standing
     - GPL V2: everybody else
   RCL license terms accompanied with this source code. See http://opcfoundation.org/License/RCL/1.00/
   GNU General Public License as published by the Free Software Foundation;
   version 2 of the License are accompanied with this source code. See http://opcfoundation.org/License/GPLv2
   This source code is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
package org.opcfoundation.ua.transport.https;

import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.params.*;
import org.opcfoundation.ua.common.*;
import org.opcfoundation.ua.transport.security.*;
import org.opcfoundation.ua.transport.security.KeyPair;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.KeyStore.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.util.*;

@NoArgsConstructor
@Slf4j
public class HttpsSettings implements Cloneable {

    /**
     * Key Managers
     * <p>
     * key manager for a https application.
     */
    @Getter
    @Setter
    private X509KeyManager keyManager;

    /**
     * Trust managers
     * Set the trust manager for a https application.
     * Trust manager validates peer's certificates and certificate issuers.
     */
    @Getter
    @Setter
    private TrustManager trustManager;

    /**
     * Verifies whether the target hostname matches the names stored inside
     * the server's X.509 certificate, once the connection has been established.
     * This verification can provide additional guarantees of authenticity of
     * the server trust material.
     */
    @Getter
    @Setter
    private X509HostnameVerifier hostnameVerifier;

    /**
     * Authentication info
     */
    @Setter
    @Getter
    private String username;

    @Setter
    @Getter
    private String password;

    /**
     * http params
     */
    @Getter
    @Setter
    private HttpParams httpParams;

    @Getter
    private HttpsSecurityPolicy[] httpsSecurityPolicies;

    @SneakyThrows
    public static HttpsSettings newInstanceFrom(HttpsSettings source) {
        Objects.requireNonNull(source);

        return (HttpsSettings) source.clone();
    }

    public HttpsSettings(KeyPair keypair, CertificateValidator certValidator, X509HostnameVerifier hostnameVerifier) {

        setKeyPair(keypair);
        setCertificateValidator(certValidator);
        this.hostnameVerifier = hostnameVerifier;
    }

    public HttpsSettings(X509KeyManager keyManager, TrustManager trustManager, X509HostnameVerifier hostnameVerifier) {

        this.keyManager = keyManager;
        this.trustManager = trustManager;
        this.hostnameVerifier = hostnameVerifier;
    }

    public HttpsSettings(X509KeyManager keyManager, TrustManager trustManager, X509HostnameVerifier hostnameVerifier, String username, String password) {

        this.keyManager = keyManager;
        this.trustManager = trustManager;
        this.hostnameVerifier = hostnameVerifier;
        this.username = username;
        this.password = password;
    }

    /**
     * Set keypair of a https application. This replaces a keyManager.
     * Additional CA certifications can be attached.
     *
     * @param keypair key pair
     * @param caCerts ca certs
     */
    @SneakyThrows({NoSuchAlgorithmException.class, CertificateException.class, IOException.class, ServiceResultException.class})
    public void setKeyPair(KeyPair keypair, Cert... caCerts) {

        if (keypair == null) {
            return;
        }

        try {
            KeyStore keystore = KeyStore.getInstance("jks");
            Certificate[] certs = new Certificate[]{keypair.getCertificate().getCertificate()};
            PrivateKeyEntry entry = new PrivateKeyEntry(keypair.getPrivateKey().getPrivateKey(), certs);
            keystore.load(null);
            keystore.setEntry("myentry-" + keypair.hashCode(), entry, new PasswordProtection(new char[0]));

            for (int i = 0; i < caCerts.length; i++) {
                String id = "cacert-" + (i + 1);
                keystore.setEntry(id, new TrustedCertificateEntry(caCerts[i].getCertificate()), null);
            }
            setKeyStore(keystore, "");
        } catch (KeyStoreException e) {
            // Expected if JKS is not available (e.g. in Android)
            log.error("set key pair exception", e);
        }
    }

    /**
     * Set keypairs to a https application. This replaces previous keyManager.
     * Additional CA certifications can be attached.
     *
     * @param keypairs key paris
     * @param caCerts  ca certs
     */
    @SneakyThrows({NoSuchAlgorithmException.class, CertificateException.class, IOException.class, ServiceResultException.class,
            KeyStoreException.class})
    public void setKeyPairs(KeyPair[] keypairs, Cert... caCerts) {

        KeyStore keystore = KeyStore.getInstance("jks");
        PasswordProtection prot = new PasswordProtection(new char[0]);
        keystore.load(null);

        for (int i = 0; i < keypairs.length; i++) {
            Certificate[] certs = new Certificate[1 + caCerts.length];
            certs[0] = keypairs[i].getCertificate().getCertificate();
            for (int j = 0; j < caCerts.length; j++) {
                certs[j + 1] = caCerts[j].getCertificate();
            }
            PrivateKeyEntry entry = new PrivateKeyEntry(keypairs[i].getPrivateKey().privateKey, certs);
            keystore.setEntry("my-key-pair-entry-" + (i + 1), entry, prot);
        }
        int count = caCerts.length;
        for (int i = 0; i < count; i++) {
            String id = "cacert-" + (i + 1);
            keystore.setEntry(id, new TrustedCertificateEntry(caCerts[i].getCertificate()), null);
        }

        setKeyStore(keystore, "");
    }


    /**
     * Set keystore as the key manager for a https application.
     *
     * @param keystore key store
     * @param password the password for the key store
     * @throws ServiceResultException if error
     */
    public void setKeyStore(KeyStore keystore, String password) throws ServiceResultException {

        try {
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keystore, password.toCharArray());
            KeyManager[] kms = kmfactory.getKeyManagers();
            keyManager = kms.length == 0 ? null : (X509KeyManager) kms[0];
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
            throw new ServiceResultException(e);
        }
    }


    /**
     * Set an implementation of CertificateValidator as TrustManager.
     * Trust manager validates peer's certificates and certificate issuers.
     *
     * @param certValidator certificate validator
     */
    public void setCertificateValidator(CertificateValidator certValidator) {

        this.trustManager = new CertValidatorTrustManager(certValidator);
    }

    /**
     * Set SSL Authentication information. Must be set before #initialization.
     *
     * @param username user name
     * @param password password
     */
    public void setHttpsAuthentication(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public TrustManager[] getTrustManagers() {

        return trustManager == null ? new TrustManager[0] : new TrustManager[]{trustManager};
    }

    public KeyManager[] getKeyManagers() {

        return keyManager == null ? new KeyManager[0] : new KeyManager[]{keyManager};
    }

    public void readFrom(HttpsSettings src) {

        if (src.hostnameVerifier != null) hostnameVerifier = src.hostnameVerifier;
        if (src.trustManager != null) this.trustManager = src.trustManager;
        if (src.keyManager != null) this.keyManager = src.keyManager;
        if (src.username != null && src.password != null) {
            username = src.username;
            password = src.password;
        }
        if (src.httpParams != null) this.httpParams = src.httpParams;
        if (src.httpsSecurityPolicies != null) this.httpsSecurityPolicies = src.httpsSecurityPolicies;
    }

    public void setHttpsSecurityPolicies(HttpsSecurityPolicy... httpsSecurityPolicy) {

        this.httpsSecurityPolicies = httpsSecurityPolicy;
    }


}
