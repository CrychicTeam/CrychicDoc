package me.steinborn.krypton.mod.shared.network;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public interface ClientConnectionEncryptionExtension {

    void setupEncryption(SecretKey var1) throws GeneralSecurityException;
}