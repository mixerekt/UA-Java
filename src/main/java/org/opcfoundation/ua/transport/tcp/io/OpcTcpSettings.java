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
package org.opcfoundation.ua.transport.tcp.io;

import lombok.*;
import org.opcfoundation.ua.transport.security.*;

import java.security.cert.*;
import java.util.*;

/**
 * <p>OpcTcpSettings class.</p>
 */
public class OpcTcpSettings implements Cloneable {

    @Getter
    @Setter
    private PrivKey privKey;

    @Getter
    @Setter
    private Cert clientCertificate;

    /**
     * Getter for the field <code>clientCertificate</code>
     */
    @Getter
    @Setter
    private CertificateValidator certificateValidator;

    @Getter
    @Setter
    private EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);

    public enum Flag {
        /**
         * In multithread mode, depending on implementation, channels
         * encrypt and decrypt messages simultaneously in multiple threads.
         * <p>
         * This allows higher throughput in secured data intensive applications with
         * large messages.
         */
        MultiThread
    }

    @SneakyThrows
    public static OpcTcpSettings newInstanceFrom(OpcTcpSettings source) {
        Objects.requireNonNull(source);

        return (OpcTcpSettings) source.clone();
    }

    /**
     * <p>readFrom.</p>
     *
     * @param tcs a {@link org.opcfoundation.ua.transport.tcp.io.OpcTcpSettings} object.
     */
    public void readFrom(OpcTcpSettings tcs) {
        if (tcs.clientCertificate != null) clientCertificate = tcs.clientCertificate;
        if (tcs.certificateValidator != null) certificateValidator = tcs.certificateValidator;
        if (tcs.privKey != null) privKey = tcs.privKey;
        flags = tcs.flags;
    }

    /**
     * <p>Setter for the field <code>clientCertificate</code>.</p>
     *
     * @param clientCertificate a {@link java.security.cert.X509Certificate} object.
     * @throws java.security.cert.CertificateEncodingException if any.
     */
    public void setClientCertificateByX509Certification(X509Certificate clientCertificate) throws CertificateEncodingException {
        this.clientCertificate = new Cert(clientCertificate);
    }
}
