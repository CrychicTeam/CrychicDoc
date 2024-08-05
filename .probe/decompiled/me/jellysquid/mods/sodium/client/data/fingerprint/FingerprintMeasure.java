package me.jellysquid.mods.sodium.client.data.fingerprint;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FingerprintMeasure(@NotNull String uuid, @NotNull String path) {

    private static final int SALT_LENGTH = 64;

    @Nullable
    public static FingerprintMeasure create() {
        UUID uuid = Minecraft.getInstance().getUser().getProfileId();
        Path path = FMLPaths.GAMEDIR.get();
        return uuid != null && path != null ? new FingerprintMeasure(uuid.toString(), path.toAbsolutePath().toString()) : null;
    }

    public HashedFingerprint hashed() {
        Instant date = Instant.now();
        String salt = createSalt();
        String uuidHashHex = sha512(salt, this.uuid());
        String pathHashHex = sha512(salt, this.path());
        return new HashedFingerprint(1, salt, uuidHashHex, pathHashHex, date.getEpochSecond());
    }

    public boolean looselyMatches(HashedFingerprint hashed) {
        String uuidHashHex = sha512(hashed.saltHex(), this.uuid());
        String pathHashHex = sha512(hashed.saltHex(), this.path());
        return Objects.equals(uuidHashHex, hashed.uuidHashHex()) || Objects.equals(pathHashHex, hashed.pathHashHex());
    }

    private static String sha512(@NotNull String salt, @NotNull String message) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(Hex.decodeHex(salt));
            md.update(message.getBytes(StandardCharsets.UTF_8));
        } catch (Throwable var4) {
            throw new RuntimeException("Failed to hash value", var4);
        }
        return Hex.encodeHexString(md.digest());
    }

    private static String createSalt() {
        SecureRandom rng = new SecureRandom();
        byte[] salt = new byte[64];
        rng.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }
}