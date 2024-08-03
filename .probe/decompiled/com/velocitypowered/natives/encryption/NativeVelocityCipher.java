package com.velocitypowered.natives.encryption;

import com.google.common.base.Preconditions;
import com.velocitypowered.natives.util.BufferPreference;
import io.netty.buffer.ByteBuf;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public class NativeVelocityCipher implements VelocityCipher {

    public static final VelocityCipherFactory FACTORY = new VelocityCipherFactory() {

        @Override
        public VelocityCipher forEncryption(SecretKey key) throws GeneralSecurityException {
            return new NativeVelocityCipher(true, key);
        }

        @Override
        public VelocityCipher forDecryption(SecretKey key) throws GeneralSecurityException {
            return new NativeVelocityCipher(false, key);
        }
    };

    private final long ctx;

    private boolean disposed = false;

    private NativeVelocityCipher(boolean encrypt, SecretKey key) throws GeneralSecurityException {
        this.ctx = OpenSslCipherImpl.init(key.getEncoded(), encrypt);
    }

    @Override
    public void process(ByteBuf source) {
        this.ensureNotDisposed();
        long base = source.memoryAddress() + (long) source.readerIndex();
        int len = source.readableBytes();
        OpenSslCipherImpl.process(this.ctx, base, len, base);
    }

    @Override
    public void close() {
        if (!this.disposed) {
            OpenSslCipherImpl.free(this.ctx);
        }
        this.disposed = true;
    }

    private void ensureNotDisposed() {
        Preconditions.checkState(!this.disposed, "Object already disposed");
    }

    @Override
    public BufferPreference preferredBufferType() {
        return BufferPreference.DIRECT_REQUIRED;
    }
}