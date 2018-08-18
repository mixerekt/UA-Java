package org.opcfoundation.ua.application;

import lombok.*;
import org.junit.*;
import org.opcfoundation.ua.builtintypes.*;
import org.opcfoundation.ua.core.*;
import org.opcfoundation.ua.transport.security.*;
import org.opcfoundation.ua.unittests.*;

import static org.junit.Assert.*;

public class ApplicationTest {

    @Test
    public void getSoftwareCertificatesEquals() {

        Application target = new Application();

        SignedSoftwareCertificate[] expected = {
                new SignedSoftwareCertificate(ByteString.EMPTY, ByteString.EMPTY),
                new SignedSoftwareCertificate(ByteString.valueOf((byte) 0, (byte) 2, (byte) 3), ByteString.valueOf((byte) 1, (byte) 2)),
                new SignedSoftwareCertificate(ByteString.valueOf((byte) 5, (byte) 10, (byte) 7), ByteString.valueOf((byte) 8, (byte) 25))
        };

        target.addSoftwareCertificate(expected[0]);
        target.addSoftwareCertificate(expected[1]);
        target.addSoftwareCertificate(expected[2]);

        SignedSoftwareCertificate[] actual = target.getSoftwareCertificates();

        assertArrayEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    public void getApplicationInstanceCertificatesEquals() {

        Application target = new Application();

        ClassLoader classLoader = getClass().getClassLoader();
        Cert myClientCertificate = Cert.load( classLoader.getResource("org/opcfoundation/ua/unittests/ClientCert.der") );
        PrivKey myClientPrivateKey = PrivKey.loadFromKeyStore( classLoader.getResource( "org/opcfoundation/ua/unittests/ClientCert.pfx"), "Opc.Sample.Ua.Client");
        KeyPair[] expected = {
                new KeyPair(myClientCertificate, myClientPrivateKey),
                new KeyPair(myClientCertificate, myClientPrivateKey),
        };

        target.addApplicationInstanceCertificate(expected[0]);
        target.addApplicationInstanceCertificate(expected[1]);

        KeyPair[] actual = target.getApplicationInstanceCertificates();

        assertArrayEquals(expected, actual);
    }
}
