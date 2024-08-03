package me.steinborn.krypton.mixin.shared.network.pipeline.encryption;

import java.security.GeneralSecurityException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import me.steinborn.krypton.mod.shared.network.ClientConnectionEncryptionExtension;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ServerLoginPacketListenerImpl.class })
public class ServerLoginNetworkHandlerMixin {

    @Shadow
    @Final
    public Connection connection;

    @Redirect(method = { "onKey" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/encryption/NetworkEncryptionUtils;cipherFromKey(ILjava/security/Key;)Ljavax/crypto/Cipher;"))
    private Cipher onKey$initializeVelocityCipher(int ignored1, Key secretKey) throws GeneralSecurityException {
        ((ClientConnectionEncryptionExtension) this.connection).setupEncryption((SecretKey) secretKey);
        return null;
    }

    @Redirect(method = { "onKey" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;setupEncryption(Ljavax/crypto/Cipher;Ljavax/crypto/Cipher;)V"))
    public void onKey$ignoreMinecraftEncryptionPipelineInjection(Connection connection, Cipher ignored1, Cipher ignored2) {
    }
}