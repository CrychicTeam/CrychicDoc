package net.minecraft.client.resources;

import com.google.common.hash.Hashing;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;

public class DownloadedPackSource implements RepositorySource {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");

    private static final int MAX_PACK_SIZE_BYTES = 262144000;

    private static final int MAX_KEPT_PACKS = 10;

    private static final String SERVER_ID = "server";

    private static final Component SERVER_NAME = Component.translatable("resourcePack.server.name");

    private static final Component APPLYING_PACK_TEXT = Component.translatable("multiplayer.applyingPack");

    private final File serverPackDir;

    private final ReentrantLock downloadLock = new ReentrantLock();

    @Nullable
    private CompletableFuture<?> currentDownload;

    @Nullable
    private Pack serverPack;

    public DownloadedPackSource(File file0) {
        this.serverPackDir = file0;
    }

    @Override
    public void loadPacks(Consumer<Pack> consumerPack0) {
        if (this.serverPack != null) {
            consumerPack0.accept(this.serverPack);
        }
    }

    private static Map<String, String> getDownloadHeaders() {
        return Map.of("X-Minecraft-Username", Minecraft.getInstance().getUser().getName(), "X-Minecraft-UUID", Minecraft.getInstance().getUser().getUuid(), "X-Minecraft-Version", SharedConstants.getCurrentVersion().getName(), "X-Minecraft-Version-ID", SharedConstants.getCurrentVersion().getId(), "X-Minecraft-Pack-Format", String.valueOf(SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES)), "User-Agent", "Minecraft Java/" + SharedConstants.getCurrentVersion().getName());
    }

    public CompletableFuture<?> downloadAndSelectResourcePack(URL uRL0, String string1, boolean boolean2) {
        String $$3 = Hashing.sha1().hashString(uRL0.toString(), StandardCharsets.UTF_8).toString();
        String $$4 = SHA1.matcher(string1).matches() ? string1 : "";
        this.downloadLock.lock();
        CompletableFuture var14;
        try {
            Minecraft $$5 = Minecraft.getInstance();
            File $$6 = new File(this.serverPackDir, $$3);
            CompletableFuture<?> $$7;
            if ($$6.exists()) {
                $$7 = CompletableFuture.completedFuture("");
            } else {
                ProgressScreen $$8 = new ProgressScreen(boolean2);
                Map<String, String> $$9 = getDownloadHeaders();
                $$5.m_18709_(() -> $$5.setScreen($$8));
                $$7 = HttpUtil.downloadTo($$6, uRL0, $$9, 262144000, $$8, $$5.getProxy());
            }
            this.currentDownload = $$7.thenCompose(p_251155_ -> {
                if (!this.checkHash($$4, $$6)) {
                    return CompletableFuture.failedFuture(new RuntimeException("Hash check failure for file " + $$6 + ", see log"));
                } else {
                    $$5.execute(() -> {
                        if (!boolean2) {
                            $$5.setScreen(new GenericDirtMessageScreen(APPLYING_PACK_TEXT));
                        }
                    });
                    return this.setServerPack($$6, PackSource.SERVER);
                }
            }).exceptionallyCompose(p_249744_ -> this.clearServerPack().thenAcceptAsync(p_251750_ -> {
                LOGGER.warn("Pack application failed: {}, deleting file {}", p_249744_.getMessage(), $$6);
                deleteQuietly($$6);
            }, Util.ioPool()).thenAcceptAsync(p_248937_ -> $$5.setScreen(new ConfirmScreen(p_249339_ -> {
                if (p_249339_) {
                    $$5.setScreen(null);
                } else {
                    ClientPacketListener $$2 = $$5.getConnection();
                    if ($$2 != null) {
                        $$2.getConnection().disconnect(Component.translatable("connect.aborted"));
                    }
                }
            }, Component.translatable("multiplayer.texturePrompt.failure.line1"), Component.translatable("multiplayer.texturePrompt.failure.line2"), CommonComponents.GUI_PROCEED, Component.translatable("menu.disconnect"))), $$5)).thenAcceptAsync(p_250279_ -> this.clearOldDownloads(), Util.ioPool());
            var14 = this.currentDownload;
        } finally {
            this.downloadLock.unlock();
        }
        return var14;
    }

    private static void deleteQuietly(File file0) {
        try {
            Files.delete(file0.toPath());
        } catch (IOException var2) {
            LOGGER.warn("Failed to delete file {}: {}", file0, var2.getMessage());
        }
    }

    public CompletableFuture<Void> clearServerPack() {
        this.downloadLock.lock();
        try {
            if (this.currentDownload != null) {
                this.currentDownload.cancel(true);
            }
            this.currentDownload = null;
            if (this.serverPack != null) {
                this.serverPack = null;
                return Minecraft.getInstance().delayTextureReload();
            }
        } finally {
            this.downloadLock.unlock();
        }
        return CompletableFuture.completedFuture(null);
    }

    private boolean checkHash(String string0, File file1) {
        try {
            String $$2 = com.google.common.io.Files.asByteSource(file1).hash(Hashing.sha1()).toString();
            if (string0.isEmpty()) {
                LOGGER.info("Found file {} without verification hash", file1);
                return true;
            }
            if ($$2.toLowerCase(Locale.ROOT).equals(string0.toLowerCase(Locale.ROOT))) {
                LOGGER.info("Found file {} matching requested hash {}", file1, string0);
                return true;
            }
            LOGGER.warn("File {} had wrong hash (expected {}, found {}).", new Object[] { file1, string0, $$2 });
        } catch (IOException var4) {
            LOGGER.warn("File {} couldn't be hashed.", file1, var4);
        }
        return false;
    }

    private void clearOldDownloads() {
        if (this.serverPackDir.isDirectory()) {
            try {
                List<File> $$0 = new ArrayList(FileUtils.listFiles(this.serverPackDir, TrueFileFilter.TRUE, null));
                $$0.sort(LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                int $$1 = 0;
                for (File $$2 : $$0) {
                    if ($$1++ >= 10) {
                        LOGGER.info("Deleting old server resource pack {}", $$2.getName());
                        FileUtils.deleteQuietly($$2);
                    }
                }
            } catch (Exception var5) {
                LOGGER.error("Error while deleting old server resource pack : {}", var5.getMessage());
            }
        }
    }

    public CompletableFuture<Void> setServerPack(File file0, PackSource packSource1) {
        Pack.ResourcesSupplier $$2 = p_255464_ -> new FilePackResources(p_255464_, file0, false);
        Pack.Info $$3 = Pack.readPackInfo("server", $$2);
        if ($$3 == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid pack metadata at " + file0));
        } else {
            LOGGER.info("Applying server pack {}", file0);
            this.serverPack = Pack.create("server", SERVER_NAME, true, $$2, $$3, PackType.CLIENT_RESOURCES, Pack.Position.TOP, true, packSource1);
            return Minecraft.getInstance().delayTextureReload();
        }
    }

    public CompletableFuture<Void> loadBundledResourcePack(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0) {
        Path $$1 = levelStorageSourceLevelStorageAccess0.getLevelPath(LevelResource.MAP_RESOURCE_FILE);
        return Files.exists($$1, new LinkOption[0]) && !Files.isDirectory($$1, new LinkOption[0]) ? this.setServerPack($$1.toFile(), PackSource.WORLD) : CompletableFuture.completedFuture(null);
    }
}