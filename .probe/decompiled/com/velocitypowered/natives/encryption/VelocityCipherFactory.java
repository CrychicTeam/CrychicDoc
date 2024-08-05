package com.velocitypowered.natives.encryption;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public interface VelocityCipherFactory {

    VelocityCipher forEncryption(SecretKey var1) throws GeneralSecurityException;

    VelocityCipher forDecryption(SecretKey var1) throws GeneralSecurityException;
}