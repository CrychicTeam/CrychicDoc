package net.minecraft.util;

import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.logging.LogUtils;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Collection;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public interface SignatureValidator {

    SignatureValidator NO_VALIDATION = (p_216352_, p_216353_) -> true;

    Logger LOGGER = LogUtils.getLogger();

    boolean validate(SignatureUpdater var1, byte[] var2);

    default boolean validate(byte[] byte0, byte[] byte1) {
        return this.validate(p_216374_ -> p_216374_.update(byte0), byte1);
    }

    private static boolean verifySignature(SignatureUpdater signatureUpdater0, byte[] byte1, Signature signature2) throws SignatureException {
        signatureUpdater0.update(signature2::update);
        return signature2.verify(byte1);
    }

    static SignatureValidator from(PublicKey publicKey0, String string1) {
        return (p_216367_, p_216368_) -> {
            try {
                Signature $$4 = Signature.getInstance(string1);
                $$4.initVerify(publicKey0);
                return verifySignature(p_216367_, p_216368_, $$4);
            } catch (Exception var5) {
                LOGGER.error("Failed to verify signature", var5);
                return false;
            }
        };
    }

    @Nullable
    static SignatureValidator from(ServicesKeySet servicesKeySet0, ServicesKeyType servicesKeyType1) {
        Collection<ServicesKeyInfo> $$2 = servicesKeySet0.keys(servicesKeyType1);
        return $$2.isEmpty() ? null : (p_284690_, p_284691_) -> $$2.stream().anyMatch(p_216361_ -> {
            Signature $$3 = p_216361_.signature();
            try {
                return verifySignature(p_284690_, p_284691_, $$3);
            } catch (SignatureException var5) {
                LOGGER.error("Failed to verify Services signature", var5);
                return false;
            }
        });
    }
}