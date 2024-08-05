package com.velocitypowered.natives.encryption;

import com.google.common.base.Preconditions;
import com.velocitypowered.natives.util.BufferPreference;
import io.netty.buffer.ByteBuf;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public class JavaVelocityCipher implements VelocityCipher {

    public static final VelocityCipherFactory FACTORY = new VelocityCipherFactory() {

        @Override
        public VelocityCipher forEncryption(SecretKey key) throws GeneralSecurityException {
            return new JavaVelocityCipher(true, key);
        }

        @Override
        public VelocityCipher forDecryption(SecretKey key) throws GeneralSecurityException {
            return new JavaVelocityCipher(false, key);
        }
    };

    private final Cipher cipher;

    private boolean disposed = false;

    private JavaVelocityCipher(boolean encrypt, SecretKey key) throws GeneralSecurityException {
        this.cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        this.cipher.init(encrypt ? 1 : 2, key, new IvParameterSpec(key.getEncoded()));
    }

    @Override
    public void process(ByteBuf source) {
        this.ensureNotDisposed();
        Preconditions.checkArgument(source.hasArray(), "No source array");
        int inBytes = source.readableBytes();
        int baseOffset = source.arrayOffset() + source.readerIndex();
        try {
            this.cipher.update(source.array(), baseOffset, inBytes, source.array(), baseOffset);
        } catch (ShortBufferException var5) {
            throw new AssertionError("Cipher update did not operate in place and requested a larger buffer than the source buffer");
        }
    }

    @Override
    public void close() {
        this.disposed = true;
    }

    private void ensureNotDisposed() {
        Preconditions.checkState(!this.disposed, "Object already disposed");
    }

    @Override
    public BufferPreference preferredBufferType() {
        return BufferPreference.HEAP_REQUIRED;
    }
}