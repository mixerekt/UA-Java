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

package org.opcfoundation.ua.application;

import lombok.*;
import lombok.extern.slf4j.*;
import org.opcfoundation.ua.builtintypes.*;
import org.opcfoundation.ua.common.*;
import org.opcfoundation.ua.core.*;
import org.opcfoundation.ua.encoding.*;
import org.opcfoundation.ua.transport.*;
import org.opcfoundation.ua.transport.https.*;
import org.opcfoundation.ua.transport.security.*;
import org.opcfoundation.ua.transport.tcp.io.*;
import org.opcfoundation.ua.transport.tcp.nio.*;
import org.opcfoundation.ua.utils.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * This class contains the mechanisms that are common for both client and server
 * application.
 *
 * @see Client OPC UA Client Application
 * @see Server OPC UA Server Application
 */
@Slf4j
public class Application {

    /**
     * Application description
     */
    @Getter
    private ApplicationDescription applicationDescription = new ApplicationDescription();
    /**
     * Application Instance Certificates
     */
    private List<KeyPair> applicationInstanceCertificates = new CopyOnWriteArrayList<>();
    /**
     * Software Certificates
     */
    private List<SignedSoftwareCertificate> softwareCertificates = new CopyOnWriteArrayList<>();
    /**
     * Locales
     */
    private List<Locale> locales = new CopyOnWriteArrayList<>();
    /**
     * Https Settings for Https Endpoint Servers
     */
    @Getter
    @Setter
    private HttpsSettings httpsSettings = new HttpsSettings();
    /**
     * OpcTcp Settings for OpcTcp Endpoint Servers
     */
    @Getter
    @Setter
    private OpcTcpSettings opctcpSettings = new OpcTcpSettings();
    /**
     * Https Server
     */
    @Getter
    private HttpsServer httpsServer;
    /**
     * OpcTcp Server
     */
    @Getter
    private OpcTcpServer opctcpServer;

    @Getter
    private EncoderContext encoderContext = new EncoderContext(new NamespaceTable(), new ServerTable(), StackUtils.getDefaultSerializer());

    /**
     * <p>Constructor for Application.</p>
     */
    @SneakyThrows(UnknownHostException.class)
    public Application() {

        String publicHostname = InetAddress.getLocalHost().getHostName();
        applicationDescription.setApplicationUri("urn:" + publicHostname + ":" + UUID.randomUUID());
        opctcpSettings.setCertificateValidator(CertificateValidator.ALLOW_ALL);
        httpsSettings.setCertificateValidator(CertificateValidator.ALLOW_ALL);
    }

    /**
     * <p>getOrCreateEndpointServer.</p>
     *
     * @param scheme a {@link java.lang.String} object.
     * @return a {@link org.opcfoundation.ua.transport.EndpointServer} object.
     * @throws org.opcfoundation.ua.common.ServiceResultException if any.
     */
    public synchronized EndpointServer getOrCreateEndpointServer(String scheme) throws ServiceResultException {

        if (UriUtil.SCHEME_OPCTCP.equals(scheme)) {
            return getOrCreateOpcTcpServer();
        } else if (UriUtil.SCHEME_HTTP.equals(scheme) || UriUtil.SCHEME_HTTPS.equals(scheme)) {
            return getOrCreateHttpsServer();
        } else {
            throw new ServiceResultException(StatusCodes.Bad_UnexpectedError, "Cannot find EndpointServer for scheme " + scheme);
        }
    }

    /**
     * <p>getOrCreateOpcTcpServer.</p>
     *
     * @return a {@link org.opcfoundation.ua.transport.tcp.nio.OpcTcpServer} object.
     * @throws org.opcfoundation.ua.common.ServiceResultException if any.
     */
    private synchronized OpcTcpServer getOrCreateOpcTcpServer() throws ServiceResultException {

        if (opctcpServer == null) {
            opctcpServer = new OpcTcpServer(this);
        }
        return opctcpServer;
    }

    /**
     * <p>getOrCreateHttpsServer.</p>
     *
     * @return a {@link org.opcfoundation.ua.transport.https.HttpsServer} object.
     * @throws org.opcfoundation.ua.common.ServiceResultException if any.
     */
    private synchronized HttpsServer getOrCreateHttpsServer() throws ServiceResultException {

        if (httpsServer == null) {
            httpsServer = new HttpsServer(this);
        }
        return httpsServer;
    }

    /**
     * <p>Getter for the field <code>softwareCertificates</code>.</p>
     *
     * @return an array of {@link org.opcfoundation.ua.core.SignedSoftwareCertificate} objects.
     */
    public SignedSoftwareCertificate[] getSoftwareCertificates() {

        return softwareCertificates.toArray(new SignedSoftwareCertificate[0]);
    }

    /**
     * <p>addSoftwareCertificate.</p>
     *
     * @param cert a {@link org.opcfoundation.ua.core.SignedSoftwareCertificate} object.
     */
    public void addSoftwareCertificate(SignedSoftwareCertificate cert) {
        Objects.requireNonNull(cert);

        softwareCertificates.add(cert);
    }

    /**
     * <p>Getter for the field <code>applicationInstanceCertificates</code>.</p>
     *
     * @return an array of {@link org.opcfoundation.ua.transport.security.KeyPair} objects.
     */
    public KeyPair[] getApplicationInstanceCertificates() {

        return applicationInstanceCertificates.toArray(new KeyPair[0]);
    }

    /**
     * <p>addApplicationInstanceCertificate.</p>
     *
     * @param cert a {@link org.opcfoundation.ua.transport.security.KeyPair} object.
     */
    public void addApplicationInstanceCertificate(KeyPair cert) {
        Objects.requireNonNull(cert);

        applicationInstanceCertificates.add(cert);
    }

    /**
     * <p>removeApplicationInstanceCertificate.</p>
     *
     * @param applicationInstanceCertificate a {@link org.opcfoundation.ua.transport.security.KeyPair} object.
     */
    public void removeApplicationInstanceCertificate(KeyPair applicationInstanceCertificate) {

        applicationInstanceCertificates.remove(applicationInstanceCertificate);
    }

    /**
     * <p>getApplicationInstanceCertificate.</p>
     *
     * @param thumb an array of byte.
     * @return a {@link org.opcfoundation.ua.transport.security.KeyPair} object.
     */
    public KeyPair getApplicationInstanceCertificate(byte[] thumb) {

        if (thumb == null) {
            return null;
        }

        log.debug("getApplicationInstanceCertificate: expected={}", CryptoUtil.toHex(thumb));

        return applicationInstanceCertificates.stream()
                .filter(cert -> Arrays.equals(cert.getCertificate().getEncodedCertificateThumbprint(), thumb))
                .findFirst()
                .orElse(null);
    }

    /**
     * <p>getApplicationInstanceCertificate.</p>
     *
     * @return a {@link org.opcfoundation.ua.transport.security.KeyPair} object.
     */
    public KeyPair getApplicationInstanceCertificate() {

        if (applicationInstanceCertificates.isEmpty()) {
            return null;
        }

        return applicationInstanceCertificates.get(applicationInstanceCertificates.size() - 1);
    }

    /**
     * <p>getApplicationUri.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getApplicationUri() {

        return applicationDescription.getApplicationUri();
    }

    /**
     * <p>setApplicationUri.</p>
     *
     * @param applicationUri a {@link java.lang.String} object.
     */
    public void setApplicationUri(String applicationUri) {

        applicationDescription.setApplicationUri(applicationUri);
    }

    /**
     * <p>setApplicationName.</p>
     *
     * @param applicationName a {@link org.opcfoundation.ua.builtintypes.LocalizedText} object.
     */
    public void setApplicationName(LocalizedText applicationName) {

        applicationDescription.setApplicationName(applicationName);
    }

    /**
     * <p>getProductUri.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getProductUri() {

        return applicationDescription.getProductUri();
    }

    /**
     * <p>setProductUri.</p>
     *
     * @param productUri a {@link java.lang.String} object.
     */
    public void setProductUri(String productUri) {

        applicationDescription.setProductUri(productUri);
    }

    /**
     * <p>addLocale.</p>
     *
     * @param locale a {@link java.util.Locale} object.
     */
    public void addLocale(Locale locale) {
        Objects.requireNonNull(locale);

        locales.add(locale);
    }

    /**
     * <p>removeLocale.</p>
     *
     * @param locale a {@link java.util.Locale} object.
     */
    public void removeLocale(Locale locale) {

        locales.remove(locale);
    }

    /**
     * <p>Getter for the field <code>locales</code>.</p>
     *
     * @return an array of {@link java.util.Locale} objects.
     */
    public Locale[] getLocales() {

        return locales.toArray(new Locale[0]);
    }

    /**
     * <p>getLocaleIds.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getLocaleIds() {

        return locales.stream()
                .map(LocalizedText::toLocaleId)
                .toArray(String[]::new);
    }

    /**
     * <p>close.</p>
     */
    public void close() {

        if (httpsServer != null) {
            httpsServer.close();
            httpsServer = null;
        }
        if (opctcpServer != null) {
            opctcpServer.close();
            opctcpServer = null;
        }
    }

}
