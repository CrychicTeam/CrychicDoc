package me.lucko.spark.common.ws;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import me.lucko.spark.lib.protobuf.ByteString;

public enum CryptoAlgorithm {

    Ed25519("Ed25519", 255, "Ed25519"), RSA2048("RSA", 2048, "SHA256withRSA");

    private final String keyAlgorithm;

    private final int keySize;

    private final String signatureAlgorithm;

    private CryptoAlgorithm(String keyAlgorithm, int keySize, String signatureAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        this.keySize = keySize;
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public KeyPairGenerator createKeyPairGenerator() throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(this.keyAlgorithm);
    }

    public KeyFactory createKeyFactory() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(this.keyAlgorithm);
    }

    public Signature createSignature() throws NoSuchAlgorithmException {
        return Signature.getInstance(this.signatureAlgorithm);
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = this.createKeyPairGenerator();
            generator.initialize(this.keySize);
            return generator.generateKeyPair();
        } catch (Exception var2) {
            throw new RuntimeException("Exception generating keypair", var2);
        }
    }

    public PublicKey decodePublicKey(byte[] bytes) throws IllegalArgumentException {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = this.createKeyFactory();
            return factory.generatePublic(spec);
        } catch (Exception var4) {
            throw new IllegalArgumentException("Exception parsing public key", var4);
        }
    }

    public PublicKey decodePublicKey(ByteString bytes) throws IllegalArgumentException {
        return bytes == null ? null : this.decodePublicKey(bytes.toByteArray());
    }
}