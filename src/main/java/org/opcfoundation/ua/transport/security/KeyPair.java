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

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;

/**
 * Valid and encodeable certificate, including signed public key and private key
 * This class aggregates private and public keys.
 * <p>
 * TODO Use {@link KeyPair} Instead?
 *
 * @author Toni Kalajainen (toni.kalajainen@iki.fi)
 * @author Mikko Salonen
 */
@AllArgsConstructor
@Getter
public final class KeyPair {

    private final Cert certificate;
    private final PrivKey privateKey;

    /**
     * Load Certificate and Private key pair from X.509 and keystore file
     *
     * @param certificateFile    a {@link java.net.URL} object.
     * @param privateKeyFile     a {@link java.net.URL} object.
     * @param privateKeyPassword a {@link java.lang.String} object.
     * @return a new keypair instance
     * @throws java.io.IOException                     if any.
     * @throws java.security.KeyStoreException         if any.
     * @throws java.security.cert.CertificateException if any.
     * @throws java.security.NoSuchAlgorithmException  if any.
     * @throws java.security.UnrecoverableKeyException if any.
     */
    public static KeyPair load(URL certificateFile, URL privateKeyFile, String privateKeyPassword)
            throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, KeyStoreException {

        Cert cert = Cert.load(certificateFile);
        PrivKey privKey = PrivKey.loadFromKeyStore(privateKeyFile, privateKeyPassword);
        return new KeyPair(cert, privKey);
    }

    /**
     * Load Certificate and Private key pair from X.509 and keystore file
     *
     * @param certificateFile    a {@link java.io.File} object.
     * @param privateKeyFile     a {@link java.io.File} object.
     * @param privateKeyPassword a {@link java.lang.String} object.
     * @return a new keypair instance
     * @throws java.io.IOException                     if any.
     * @throws java.security.KeyStoreException         if any.
     * @throws java.security.cert.CertificateException if any.
     * @throws java.security.NoSuchAlgorithmException  if any.
     * @throws java.security.UnrecoverableKeyException if any.
     */
    public static KeyPair load(File certificateFile, File privateKeyFile, String privateKeyPassword)
            throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, KeyStoreException {

        Cert cert = Cert.load(certificateFile);
        PrivKey privKey = PrivKey.loadFromKeyStore(privateKeyFile, privateKeyPassword);
        return new KeyPair(cert, privKey);
    }

    /**
     * <p>save.</p>
     *
     * @param certificateFile a {@link java.io.File} object.
     * @param privateKeyFile  a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     */
    public void save(File certificateFile, File privateKeyFile) throws IOException {

        certificate.save(certificateFile);
        privateKey.save(privateKeyFile);
    }

    /**
     * <p>save.</p>
     *
     * @param certificateFile    a {@link java.io.File} object.
     * @param privateKeyFile     a {@link java.io.File} object.
     * @param privateKeyPassword a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public void save(File certificateFile, File privateKeyFile, String privateKeyPassword) throws IOException {

        certificate.save(certificateFile);
        privateKey.save(privateKeyFile, privateKeyPassword);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return certificate.toString();
    }

}
