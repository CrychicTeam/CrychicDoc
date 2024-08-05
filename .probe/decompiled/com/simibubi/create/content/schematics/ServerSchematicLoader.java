package com.simibubi.create.content.schematics;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.schematics.table.SchematicTableBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CSchematics;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ServerSchematicLoader {

    private Map<String, ServerSchematicLoader.SchematicUploadEntry> activeUploads = new HashMap();

    public String getSchematicPath() {
        return "schematics/uploaded";
    }

    public void tick() {
        Set<String> deadEntries = new HashSet();
        for (String upload : this.activeUploads.keySet()) {
            ServerSchematicLoader.SchematicUploadEntry entry = (ServerSchematicLoader.SchematicUploadEntry) this.activeUploads.get(upload);
            if (entry.idleTime++ > this.getConfig().schematicIdleTimeout.get()) {
                Create.LOGGER.warn("Schematic Upload timed out: " + upload);
                deadEntries.add(upload);
            }
        }
        deadEntries.forEach(this::cancelUpload);
    }

    public void shutdown() {
        new HashSet(this.activeUploads.keySet()).forEach(this::cancelUpload);
    }

    public void handleNewUpload(ServerPlayer player, String schematic, long size, BlockPos pos) {
        String playerPath = this.getSchematicPath() + "/" + player.m_36316_().getName();
        String playerSchematicId = player.m_36316_().getName() + "/" + schematic;
        FilesHelper.createFolderIfMissing(playerPath);
        if (!schematic.endsWith(".nbt")) {
            Create.LOGGER.warn("Attempted Schematic Upload with non-supported Format: " + playerSchematicId);
        } else {
            Path playerSchematicsPath = Paths.get(this.getSchematicPath(), player.m_36316_().getName()).toAbsolutePath();
            Path uploadPath = playerSchematicsPath.resolve(schematic).normalize();
            if (!uploadPath.startsWith(playerSchematicsPath)) {
                Create.LOGGER.warn("Attempted Schematic Upload with directory escape: {}", playerSchematicId);
            } else if (this.validateSchematicSizeOnServer(player, size)) {
                if (!this.activeUploads.containsKey(playerSchematicId)) {
                    try {
                        SchematicTableBlockEntity table = this.getTable(player.m_20193_(), pos);
                        if (table == null) {
                            return;
                        }
                        Files.deleteIfExists(uploadPath);
                        Stream<Path> list = Files.list(Paths.get(playerPath));
                        long count;
                        try {
                            count = list.count();
                        } catch (Throwable var17) {
                            if (list != null) {
                                try {
                                    list.close();
                                } catch (Throwable var16) {
                                    var17.addSuppressed(var16);
                                }
                            }
                            throw var17;
                        }
                        if (list != null) {
                            list.close();
                        }
                        if (count >= (long) this.getConfig().maxSchematics.get().intValue()) {
                            list = Files.list(Paths.get(playerPath));
                            Optional<Path> lastFilePath = list.filter(f -> !Files.isDirectory(f, new LinkOption[0])).min(Comparator.comparingLong(f -> f.toFile().lastModified()));
                            list.close();
                            if (lastFilePath.isPresent()) {
                                Files.deleteIfExists((Path) lastFilePath.get());
                            }
                        }
                        OutputStream writer = Files.newOutputStream(uploadPath);
                        this.activeUploads.put(playerSchematicId, new ServerSchematicLoader.SchematicUploadEntry(writer, size, player.m_9236_(), pos));
                        table.startUpload(schematic);
                    } catch (IOException var18) {
                        Create.LOGGER.error("Exception Thrown when starting Upload: " + playerSchematicId);
                        var18.printStackTrace();
                    }
                }
            }
        }
    }

    protected boolean validateSchematicSizeOnServer(ServerPlayer player, long size) {
        Integer maxFileSize = this.getConfig().maxTotalSchematicSize.get();
        if (size > (long) (maxFileSize * 1000)) {
            player.sendSystemMessage(Lang.translateDirect("schematics.uploadTooLarge").append(Components.literal(" (" + size / 1000L + " KB).")));
            player.sendSystemMessage(Lang.translateDirect("schematics.maxAllowedSize").append(Components.literal(" " + maxFileSize + " KB")));
            return false;
        } else {
            return true;
        }
    }

    public CSchematics getConfig() {
        return AllConfigs.server().schematics;
    }

    public void handleWriteRequest(ServerPlayer player, String schematic, byte[] data) {
        String playerSchematicId = player.m_36316_().getName() + "/" + schematic;
        if (this.activeUploads.containsKey(playerSchematicId)) {
            ServerSchematicLoader.SchematicUploadEntry entry = (ServerSchematicLoader.SchematicUploadEntry) this.activeUploads.get(playerSchematicId);
            entry.bytesUploaded += (long) data.length;
            if (data.length > this.getConfig().maxSchematicPacketSize.get()) {
                Create.LOGGER.warn("Oversized Upload Packet received: " + playerSchematicId);
                this.cancelUpload(playerSchematicId);
                return;
            }
            if (entry.bytesUploaded > entry.totalBytes) {
                Create.LOGGER.warn("Received more data than Expected: " + playerSchematicId);
                this.cancelUpload(playerSchematicId);
                return;
            }
            try {
                entry.stream.write(data);
                entry.idleTime = 0;
                SchematicTableBlockEntity table = this.getTable(entry.world, entry.tablePos);
                if (table == null) {
                    return;
                }
                table.uploadingProgress = (float) ((double) entry.bytesUploaded / (double) entry.totalBytes);
                table.sendUpdate = true;
            } catch (IOException var7) {
                Create.LOGGER.error("Exception Thrown when uploading Schematic: " + playerSchematicId);
                var7.printStackTrace();
                this.cancelUpload(playerSchematicId);
            }
        }
    }

    protected void cancelUpload(String playerSchematicId) {
        if (this.activeUploads.containsKey(playerSchematicId)) {
            ServerSchematicLoader.SchematicUploadEntry entry = (ServerSchematicLoader.SchematicUploadEntry) this.activeUploads.remove(playerSchematicId);
            try {
                entry.stream.close();
                Files.deleteIfExists(Paths.get(this.getSchematicPath(), playerSchematicId));
                Create.LOGGER.warn("Cancelled Schematic Upload: " + playerSchematicId);
            } catch (IOException var5) {
                Create.LOGGER.error("Exception Thrown when cancelling Upload: " + playerSchematicId);
                var5.printStackTrace();
            }
            BlockPos pos = entry.tablePos;
            if (pos != null) {
                SchematicTableBlockEntity table = this.getTable(entry.world, pos);
                if (table != null) {
                    table.finishUpload();
                }
            }
        }
    }

    public SchematicTableBlockEntity getTable(Level world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        return !(be instanceof SchematicTableBlockEntity) ? null : (SchematicTableBlockEntity) be;
    }

    public void handleFinishedUpload(ServerPlayer player, String schematic) {
        String playerSchematicId = player.m_36316_().getName() + "/" + schematic;
        if (this.activeUploads.containsKey(playerSchematicId)) {
            try {
                ((ServerSchematicLoader.SchematicUploadEntry) this.activeUploads.get(playerSchematicId)).stream.close();
                ServerSchematicLoader.SchematicUploadEntry removed = (ServerSchematicLoader.SchematicUploadEntry) this.activeUploads.remove(playerSchematicId);
                Level world = removed.world;
                BlockPos pos = removed.tablePos;
                Create.LOGGER.info("New Schematic Uploaded: " + playerSchematicId);
                if (pos == null) {
                    return;
                }
                BlockState blockState = world.getBlockState(pos);
                if (AllBlocks.SCHEMATIC_TABLE.get() != blockState.m_60734_()) {
                    return;
                }
                SchematicTableBlockEntity table = this.getTable(world, pos);
                if (table == null) {
                    return;
                }
                table.finishUpload();
                table.inventory.setStackInSlot(1, SchematicItem.create(world.m_246945_(Registries.BLOCK), schematic, player.m_36316_().getName()));
            } catch (IOException var9) {
                Create.LOGGER.error("Exception Thrown when finishing Upload: " + playerSchematicId);
                var9.printStackTrace();
            }
        }
    }

    public void handleInstantSchematic(ServerPlayer player, String schematic, Level world, BlockPos pos, BlockPos bounds) {
        String playerName = player.m_36316_().getName();
        String playerPath = this.getSchematicPath() + "/" + playerName;
        String playerSchematicId = playerName + "/" + schematic;
        FilesHelper.createFolderIfMissing(playerPath);
        if (!schematic.endsWith(".nbt")) {
            Create.LOGGER.warn("Attempted Schematic Upload with non-supported Format: {}", playerSchematicId);
        } else {
            Path schematicPath = Paths.get(this.getSchematicPath()).toAbsolutePath();
            Path path = schematicPath.resolve(playerSchematicId).normalize();
            if (!path.startsWith(schematicPath)) {
                Create.LOGGER.warn("Attempted Schematic Upload with directory escape: {}", playerSchematicId);
            } else if (AllItems.SCHEMATIC_AND_QUILL.isIn(player.m_21205_())) {
                Path playerSchematics = Paths.get(playerPath);
                if (this.tryDeleteOldestSchematic(playerSchematics)) {
                    SchematicExport.SchematicExportResult result = SchematicExport.saveSchematic(playerSchematics, schematic, true, world, pos, pos.offset(bounds).offset(-1, -1, -1));
                    if (result != null) {
                        player.m_21008_(InteractionHand.MAIN_HAND, SchematicItem.create(world.m_246945_(Registries.BLOCK), schematic, playerName));
                    } else {
                        Lang.translate("schematicAndQuill.instant_failed").style(ChatFormatting.RED).sendStatus(player);
                    }
                }
            }
        }
    }

    private boolean tryDeleteOldestSchematic(Path dir) {
        try {
            Stream<Path> stream = Files.list(dir);
            boolean var9;
            label48: {
                boolean var5;
                try {
                    List<Path> files = stream.toList();
                    if (files.size() < this.getConfig().maxSchematics.get()) {
                        var9 = true;
                        break label48;
                    }
                    Optional<Path> oldest = files.stream().min(Comparator.comparingLong(this::getLastModifiedTime));
                    Files.delete((Path) oldest.orElseThrow());
                    var5 = true;
                } catch (Throwable var7) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }
                    throw var7;
                }
                if (stream != null) {
                    stream.close();
                }
                return var5;
            }
            if (stream != null) {
                stream.close();
            }
            return var9;
        } catch (IllegalStateException | IOException var8) {
            Create.LOGGER.error("Error deleting oldest schematic", var8);
            return false;
        }
    }

    private long getLastModifiedTime(Path file) {
        try {
            return Files.getLastModifiedTime(file).toMillis();
        } catch (IOException var3) {
            Create.LOGGER.error("Error getting modification time of file " + file.getFileName(), var3);
            throw new IllegalStateException(var3);
        }
    }

    public class SchematicUploadEntry {

        public Level world;

        public BlockPos tablePos;

        public OutputStream stream;

        public long bytesUploaded;

        public long totalBytes;

        public int idleTime;

        public SchematicUploadEntry(OutputStream stream, long totalBytes, Level world, BlockPos tablePos) {
            this.stream = stream;
            this.totalBytes = totalBytes;
            this.tablePos = tablePos;
            this.world = world;
            this.bytesUploaded = 0L;
            this.idleTime = 0;
        }
    }
}