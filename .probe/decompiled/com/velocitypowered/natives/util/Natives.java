package com.velocitypowered.natives.util;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.natives.NativeSetupException;
import com.velocitypowered.natives.compression.JavaVelocityCompressor;
import com.velocitypowered.natives.compression.LibdeflateVelocityCompressor;
import com.velocitypowered.natives.compression.VelocityCompressorFactory;
import com.velocitypowered.natives.encryption.JavaVelocityCipher;
import com.velocitypowered.natives.encryption.NativeVelocityCipher;
import com.velocitypowered.natives.encryption.VelocityCipherFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Natives {

    public static final NativeCodeLoader<VelocityCompressorFactory> compress = new NativeCodeLoader<>(ImmutableList.of(new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_X86_64, copyAndLoadNative("/linux_x86_64/velocity-compress.so"), "libdeflate (Linux x86_64)", LibdeflateVelocityCompressor.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_AARCH64, copyAndLoadNative("/linux_aarch64/velocity-compress.so"), "libdeflate (Linux aarch64)", LibdeflateVelocityCompressor.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.MACOS_AARCH64, copyAndLoadNative("/macos_arm64/velocity-compress.dylib"), "libdeflate (macOS ARM64 / Apple Silicon)", LibdeflateVelocityCompressor.FACTORY), new NativeCodeLoader.Variant<>(NativeCodeLoader.ALWAYS, () -> {
    }, "Java", JavaVelocityCompressor.FACTORY)));

    public static final NativeCodeLoader<VelocityCipherFactory> cipher = new NativeCodeLoader<>(ImmutableList.of(new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_X86_64, copyAndLoadNative("/linux_x86_64/velocity-cipher.so"), "OpenSSL local (Linux x86_64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_X86_64, copyAndLoadNative("/linux_x86_64/velocity-cipher-ossl30x.so"), "OpenSSL 3.0.x (Linux x86_64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_X86_64, copyAndLoadNative("/linux_x86_64/velocity-cipher-ossl11x.so"), "OpenSSL 1.1.x (Linux x86_64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_X86_64, copyAndLoadNative("/linux_x86_64/velocity-cipher-ossl10x.so"), "OpenSSL 1.0.x (Linux x86_64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_AARCH64, copyAndLoadNative("/linux_aarch64/velocity-cipher.so"), "OpenSSL (Linux aarch64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_AARCH64, copyAndLoadNative("/linux_aarch64/velocity-cipher-ossl30x.so"), "OpenSSL (Linux aarch64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.LINUX_AARCH64, copyAndLoadNative("/linux_aarch64/velocity-cipher-ossl11x.so"), "OpenSSL 1.1.x (Linux aarch64)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeConstraints.MACOS_AARCH64, copyAndLoadNative("/macos_arm64/velocity-cipher.dylib"), "native (macOS ARM64 / Apple Silicon)", NativeVelocityCipher.FACTORY), new NativeCodeLoader.Variant<>(NativeCodeLoader.ALWAYS, () -> {
    }, "Java", JavaVelocityCipher.FACTORY)));

    private Natives() {
        throw new AssertionError();
    }

    private static Runnable copyAndLoadNative(String path) {
        return () -> {
            try {
                InputStream nativeLib = Natives.class.getResourceAsStream(path);
                if (nativeLib == null) {
                    throw new IllegalStateException("Native library " + path + " not found.");
                } else {
                    Path tempFile = createTemporaryNativeFilename(path.substring(path.lastIndexOf(46)));
                    Files.copy(nativeLib, tempFile, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            Files.deleteIfExists(tempFile);
                        } catch (IOException var2x) {
                        }
                    }));
                    try {
                        System.load(tempFile.toAbsolutePath().toString());
                    } catch (UnsatisfiedLinkError var4) {
                        throw new NativeSetupException("Unable to load native " + tempFile.toAbsolutePath(), var4);
                    }
                }
            } catch (IOException var5) {
                throw new NativeSetupException("Unable to copy natives", var5);
            }
        };
    }

    private static Path createTemporaryNativeFilename(String ext) throws IOException {
        String temporaryFolderPath = System.getProperty("velocity.natives-tmpdir");
        return temporaryFolderPath != null ? Files.createTempFile(Path.of(temporaryFolderPath), "native-", ext) : Files.createTempFile("native-", ext);
    }
}