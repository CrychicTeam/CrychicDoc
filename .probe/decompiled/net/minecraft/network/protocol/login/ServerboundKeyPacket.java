package net.minecraft.network.protocol.login;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.SecretKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;

public class ServerboundKeyPacket implements Packet<ServerLoginPacketListener> {

    private final byte[] keybytes;

    private final byte[] encryptedChallenge;

    public ServerboundKeyPacket(SecretKey secretKey0, PublicKey publicKey1, byte[] byte2) throws CryptException {
        this.keybytes = Crypt.encryptUsingKey(publicKey1, secretKey0.getEncoded());
        this.encryptedChallenge = Crypt.encryptUsingKey(publicKey1, byte2);
    }

    public ServerboundKeyPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.keybytes = friendlyByteBuf0.readByteArray();
        this.encryptedChallenge = friendlyByteBuf0.readByteArray();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByteArray(this.keybytes);
        friendlyByteBuf0.writeByteArray(this.encryptedChallenge);
    }

    public void handle(ServerLoginPacketListener serverLoginPacketListener0) {
        serverLoginPacketListener0.handleKey(this);
    }

    public SecretKey getSecretKey(PrivateKey privateKey0) throws CryptException {
        return Crypt.decryptByteToSecretKey(privateKey0, this.keybytes);
    }

    public boolean isChallengeValid(byte[] byte0, PrivateKey privateKey1) {
        try {
            return Arrays.equals(byte0, Crypt.decryptUsingKey(privateKey1, this.encryptedChallenge));
        } catch (CryptException var4) {
            return false;
        }
    }
}