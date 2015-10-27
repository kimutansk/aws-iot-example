package com.github.kimutansk.awsiot

import java.io.{ByteArrayInputStream, InputStreamReader}
import java.nio.file.{Paths, Files}
import java.security.{KeyPair, KeyStore, Security}
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLContext, KeyManagerFactory, TrustManagerFactory, SSLSocketFactory}

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMParser

/** Factory for [[javax.net.ssl.SSLSocketFactory]] instances. */
object SocketFactoryGenerator {

  /**
   * Generate [[javax.net.ssl.SSLSocketFactory]] from pem file paths.
   *
   * @param rootCaFilePath Root CA file path
   * @param certFilePath Certificate file path
   * @param keyFilePath Private key file path
   * @return Generated [[javax.net.ssl.SSLSocketFactory]]
   */
  def generateFromFilePath(rootCaFilePath:String, certFilePath:String, keyFilePath:String, keyStorePassword:String):SSLSocketFactory = {
    Security.addProvider(new BouncyCastleProvider())

    // load Root CA certificate
    val rootCaParser:PEMParser  = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(rootCaFilePath)))))
    val rootCaCert:X509Certificate = rootCaParser.readObject().asInstanceOf[X509Certificate]
    rootCaParser.close()

    // load Server certificate
    val certParser:PEMParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(certFilePath)))))
    val serverCert:X509Certificate = certParser.readObject.asInstanceOf[X509Certificate]
    certParser.close()

    // load Private Key
    val keyParser:PEMParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFilePath)))))
    val keyPair:KeyPair = keyParser.readObject.asInstanceOf[KeyPair]
    keyParser.close()

    // Root CA certificate is used to authenticate server
    val rootCAKeyStore:KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    rootCAKeyStore.load(null, null)
    rootCAKeyStore.setCertificateEntry("ca-certificate", rootCaCert);
    val tmf:TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tmf.init(rootCAKeyStore);

    // client key and certificates are sent to server so it can authenticate us
    val ks:KeyStore  = KeyStore.getInstance(KeyStore.getDefaultType())
    ks.load(null, null)
    ks.setCertificateEntry("certificate", serverCert)
    ks.setKeyEntry("private-key", keyPair.getPrivate(), keyStorePassword.toCharArray, Array(serverCert))
    val kmf:KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    kmf.init(ks, keyStorePassword.toCharArray());

    // finally, create SSL socket factory
    val context:SSLContext = SSLContext.getInstance("TLSv1.2")
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null)

    context.getSocketFactory()
  }
}
