package net.minecraft.client.renderer.debug;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    final Minecraft minecraft;

    private double lastUpdateTime = Double.MIN_VALUE;

    private final int radius = 12;

    @Nullable
    private ChunkDebugRenderer.ChunkData data;

    public ChunkDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        double $$5 = (double) Util.getNanos();
        if ($$5 - this.lastUpdateTime > 3.0E9) {
            this.lastUpdateTime = $$5;
            IntegratedServer $$6 = this.minecraft.getSingleplayerServer();
            if ($$6 != null) {
                this.data = new ChunkDebugRenderer.ChunkData($$6, double2, double4);
            } else {
                this.data = null;
            }
        }
        if (this.data != null) {
            Map<ChunkPos, String> $$7 = (Map<ChunkPos, String>) this.data.serverData.getNow(null);
            double $$8 = this.minecraft.gameRenderer.getMainCamera().getPosition().y * 0.85;
            for (Entry<ChunkPos, String> $$9 : this.data.clientData.entrySet()) {
                ChunkPos $$10 = (ChunkPos) $$9.getKey();
                String $$11 = (String) $$9.getValue();
                if ($$7 != null) {
                    $$11 = $$11 + (String) $$7.get($$10);
                }
                String[] $$12 = $$11.split("\n");
                int $$13 = 0;
                for (String $$14 : $$12) {
                    DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, $$14, (double) SectionPos.sectionToBlockCoord($$10.x, 8), $$8 + (double) $$13, (double) SectionPos.sectionToBlockCoord($$10.z, 8), -1, 0.15F, true, 0.0F, true);
                    $$13 -= 2;
                }
            }
        }
    }

    final class ChunkData {

        final Map<ChunkPos, String> clientData;

        final CompletableFuture<Map<ChunkPos, String>> serverData;

        ChunkData(IntegratedServer integratedServer0, double double1, double double2) {
            ClientLevel $$3 = ChunkDebugRenderer.this.minecraft.level;
            ResourceKey<Level> $$4 = $$3.m_46472_();
            int $$5 = SectionPos.posToSectionCoord(double1);
            int $$6 = SectionPos.posToSectionCoord(double2);
            Builder<ChunkPos, String> $$7 = ImmutableMap.builder();
            ClientChunkCache $$8 = $$3.getChunkSource();
            for (int $$9 = $$5 - 12; $$9 <= $$5 + 12; $$9++) {
                for (int $$10 = $$6 - 12; $$10 <= $$6 + 12; $$10++) {
                    ChunkPos $$11 = new ChunkPos($$9, $$10);
                    String $$12 = "";
                    LevelChunk $$13 = $$8.m_62227_($$9, $$10, false);
                    $$12 = $$12 + "Client: ";
                    if ($$13 == null) {
                        $$12 = $$12 + "0n/a\n";
                    } else {
                        $$12 = $$12 + ($$13.isEmpty() ? " E" : "");
                        $$12 = $$12 + "\n";
                    }
                    $$7.put($$11, $$12);
                }
            }
            this.clientData = $$7.build();
            this.serverData = integratedServer0.m_18691_(() -> {
                ServerLevel $$4x = integratedServer0.m_129880_($$4);
                if ($$4x == null) {
                    return ImmutableMap.of();
                } else {
                    Builder<ChunkPos, String> $$5x = ImmutableMap.builder();
                    ServerChunkCache $$6x = $$4x.getChunkSource();
                    for (int $$7x = $$5 - 12; $$7x <= $$5 + 12; $$7x++) {
                        for (int $$8x = $$6 - 12; $$8x <= $$6 + 12; $$8x++) {
                            ChunkPos $$9x = new ChunkPos($$7x, $$8x);
                            $$5x.put($$9x, "Server: " + $$6x.getChunkDebugData($$9x));
                        }
                    }
                    return $$5x.build();
                }
            });
        }
    }
}