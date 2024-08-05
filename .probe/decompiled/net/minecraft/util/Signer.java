package net.minecraft.util;

import com.mojang.logging.LogUtils;
import java.security.PrivateKey;
import java.security.Signature;
import org.slf4j.Logger;

public interface Signer {

    Logger LOGGER = LogUtils.getLogger();

    byte[] sign(SignatureUpdater var1);

    default byte[] sign(byte[] byte0) {
        return this.sign(p_216394_ -> p_216394_.update(byte0));
    }

    static Signer from(PrivateKey privateKey0, String string1) {
        return p_216386_ -> {
            try {
                Signature $$3 = Signature.getInstance(string1);
                $$3.initSign(privateKey0);
                p_216386_.update($$3::update);
                return $$3.sign();
            } catch (Exception var4) {
                throw new IllegalStateException("Failed to sign message", var4);
            }
        };
    }
}