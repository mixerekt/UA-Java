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

package org.opcfoundation.ua.transport.security;

import lombok.*;
import org.opcfoundation.ua.common.*;
import org.opcfoundation.ua.core.*;
import org.opcfoundation.ua.utils.*;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.security.interfaces.*;

/**
 * Cert is a X509 certificate that contains a public key.
 * The instance is valid and encodedable.
 * Wrapper to {@link java.security.cert.Certificate}.
 * <p>
 * To Create a new certificate See {@link CertificateUtils}
 */
@EqualsAndHashCode
public class Cert {

    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----" + StringUtils.lineSeparator();
    private static final String END_CERT = StringUtils.lineSeparator() + "-----END CERTIFICATE-----";

    @Getter
    private final X509Certificate certificate;

    @Getter
    private final byte[] encodedCertificate;

    @Getter
    private final byte[] encodedCertificateThumbprint;

    /**
     * Load X.509 Certificate from an url
     *
     * @param url a {@link java.net.URL} object.
     * @return Certificate
     * @throws java.io.IOException                     if any.
     * @throws java.security.cert.CertificateException In case the certificate is not valid
     */
    public static Cert load(URL url) throws IOException, CertificateException {
        X509Certificate cert = CertificateUtils.readX509Certificate(url);
        return new Cert(cert);
    }

    /**
     * Load X.509 Certificate from a file
     *
     * @param file a {@link java.io.File} object.
     * @return Certificate
     * @throws java.io.IOException                     if any.
     * @throws java.security.cert.CertificateException In case the certificate is not valid
     */
    public static Cert load(File file) throws IOException, CertificateException {

        return load(file.toURI().toURL());
    }


    /**
     * <p>save.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public void save(File file) throws IOException {

        FileUtil.writeFile(file, encodedCertificate);
    }

    /**
     * <p>saveToPem.</p>
     *
     * @param file a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public void saveToPem(File file) throws IOException {

        FileWriter fw = new FileWriter(file);
        try {
            fw.append(BEGIN_CERT);
            fw.append(StringUtils.addLineBreaks(CryptoUtil.base64Encode(getEncodedCertificate()), 72));
            fw.append(END_CERT);
        } finally {
            fw.close();
        }
        //CertificateUtils.writeToPem(certificate, file);
    }

    /**
     * <p>getKeySize.</p>
     *
     * @return a int.
     */
    public int getKeySize() {

        PublicKey key = certificate.getPublicKey();
        if (key instanceof RSAPublicKey) {
            RSAPublicKey rsaKey = (RSAPublicKey) key;
            return rsaKey.getModulus().bitLength();
        }
        return -1;
    }

    /**
     * Create Certificate from encoded data, if the data contains more than one certificate, only the first one is read.
     *
     * @param data encoded Certificate
     * @throws org.opcfoundation.ua.common.ServiceResultException if any.
     */
    public Cert(byte[] data) throws ServiceResultException {

        try {
            certificate = CertificateUtils.decodeX509Certificate(data);
            encodedCertificate = certificate.getEncoded();
            encodedCertificateThumbprint = CertificateUtils.createThumbprint(encodedCertificate);
        } catch (CertificateNotYetValidException | CertificateExpiredException ce) {
            throw new ServiceResultException(StatusCodes.Bad_CertificateTimeInvalid, ce);
        } catch (CertificateException ce) {
            throw new ServiceResultException(StatusCodes.Bad_CertificateInvalid, ce);
        }
    }

    /**
     * Create Certificate from a X509Certificate certificate
     *
     * @param certificate a X509Certificate certificate
     * @throws java.security.cert.CertificateEncodingException if any.
     */
    public Cert(X509Certificate certificate) throws CertificateEncodingException {

        this.certificate = certificate;
        this.encodedCertificate = certificate.getEncoded();
        this.encodedCertificateThumbprint = CertificateUtils.createThumbprint(encodedCertificate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return certificate.toString();
    }

}
