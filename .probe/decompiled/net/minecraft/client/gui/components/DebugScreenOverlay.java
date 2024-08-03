package net.minecraft.client.gui.components;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.GlUtil;
import com.mojang.datafixers.DataFixUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class DebugScreenOverlay {

    private static final int COLOR_GREY = 14737632;

    private static final int MARGIN_RIGHT = 2;

    private static final int MARGIN_LEFT = 2;

    private static final int MARGIN_TOP = 2;

    private static final Map<Heightmap.Types, String> HEIGHTMAP_NAMES = Util.make(new EnumMap(Heightmap.Types.class), p_94070_ -> {
        p_94070_.put(Heightmap.Types.WORLD_SURFACE_WG, "SW");
        p_94070_.put(Heightmap.Types.WORLD_SURFACE, "S");
        p_94070_.put(Heightmap.Types.OCEAN_FLOOR_WG, "OW");
        p_94070_.put(Heightmap.Types.OCEAN_FLOOR, "O");
        p_94070_.put(Heightmap.Types.MOTION_BLOCKING, "M");
        p_94070_.put(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, "ML");
    });

    private final Minecraft minecraft;

    private final DebugScreenOverlay.AllocationRateCalculator allocationRateCalculator;

    private final Font font;

    private HitResult block;

    private HitResult liquid;

    @Nullable
    private ChunkPos lastPos;

    @Nullable
    private LevelChunk clientChunk;

    @Nullable
    private CompletableFuture<LevelChunk> serverChunk;

    private static final int RED = -65536;

    private static final int YELLOW = -256;

    private static final int GREEN = -16711936;

    public DebugScreenOverlay(Minecraft minecraft0) {
        this.minecraft = minecraft0;
        this.allocationRateCalculator = new DebugScreenOverlay.AllocationRateCalculator();
        this.font = minecraft0.font;
    }

    public void clearChunkCache() {
        this.serverChunk = null;
        this.clientChunk = null;
    }

    public void render(GuiGraphics guiGraphics0) {
        this.minecraft.getProfiler().push("debug");
        Entity $$1 = this.minecraft.getCameraEntity();
        this.block = $$1.pick(20.0, 0.0F, false);
        this.liquid = $$1.pick(20.0, 0.0F, true);
        guiGraphics0.drawManaged(() -> {
            this.drawGameInformation(guiGraphics0);
            this.drawSystemInformation(guiGraphics0);
            if (this.minecraft.options.renderFpsChart) {
                int $$1x = guiGraphics0.guiWidth();
                this.drawChart(guiGraphics0, this.minecraft.getFrameTimer(), 0, $$1x / 2, true);
                IntegratedServer $$2 = this.minecraft.getSingleplayerServer();
                if ($$2 != null) {
                    this.drawChart(guiGraphics0, $$2.m_129904_(), $$1x - Math.min($$1x / 2, 240), $$1x / 2, false);
                }
            }
        });
        this.minecraft.getProfiler().pop();
    }

    protected void drawGameInformation(GuiGraphics guiGraphics0) {
        List<String> $$1 = this.getGameInformation();
        $$1.add("");
        boolean $$2 = this.minecraft.getSingleplayerServer() != null;
        $$1.add("Debug: Pie [shift]: " + (this.minecraft.options.renderDebugCharts ? "visible" : "hidden") + ($$2 ? " FPS + TPS" : " FPS") + " [alt]: " + (this.minecraft.options.renderFpsChart ? "visible" : "hidden"));
        $$1.add("For help: press F3 + Q");
        this.renderLines(guiGraphics0, $$1, true);
    }

    protected void drawSystemInformation(GuiGraphics guiGraphics0) {
        List<String> $$1 = this.getSystemInformation();
        this.renderLines(guiGraphics0, $$1, false);
    }

    private void renderLines(GuiGraphics guiGraphics0, List<String> listString1, boolean boolean2) {
        int $$3 = 9;
        for (int $$4 = 0; $$4 < listString1.size(); $$4++) {
            String $$5 = (String) listString1.get($$4);
            if (!Strings.isNullOrEmpty($$5)) {
                int $$6 = this.font.width($$5);
                int $$7 = boolean2 ? 2 : guiGraphics0.guiWidth() - 2 - $$6;
                int $$8 = 2 + $$3 * $$4;
                guiGraphics0.fill($$7 - 1, $$8 - 1, $$7 + $$6 + 1, $$8 + $$3 - 1, -1873784752);
            }
        }
        for (int $$9 = 0; $$9 < listString1.size(); $$9++) {
            String $$10 = (String) listString1.get($$9);
            if (!Strings.isNullOrEmpty($$10)) {
                int $$11 = this.font.width($$10);
                int $$12 = boolean2 ? 2 : guiGraphics0.guiWidth() - 2 - $$11;
                int $$13 = 2 + $$3 * $$9;
                guiGraphics0.drawString(this.font, $$10, $$12, $$13, 14737632, false);
            }
        }
    }

    protected List<String> getGameInformation() {
        IntegratedServer $$0 = this.minecraft.getSingleplayerServer();
        Connection $$1 = this.minecraft.getConnection().getConnection();
        float $$2 = $$1.getAverageSentPackets();
        float $$3 = $$1.getAverageReceivedPackets();
        String $$4;
        if ($$0 != null) {
            $$4 = String.format(Locale.ROOT, "Integrated server @ %.0f ms ticks, %.0f tx, %.0f rx", $$0.m_129903_(), $$2, $$3);
        } else {
            $$4 = String.format(Locale.ROOT, "\"%s\" server, %.0f tx, %.0f rx", this.minecraft.player.getServerBrand(), $$2, $$3);
        }
        BlockPos $$6 = this.minecraft.getCameraEntity().blockPosition();
        if (this.minecraft.showOnlyReducedInfo()) {
            return Lists.newArrayList(new String[] { "Minecraft " + SharedConstants.getCurrentVersion().getName() + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.minecraft.fpsString, $$4, this.minecraft.levelRenderer.getChunkStatistics(), this.minecraft.levelRenderer.getEntityStatistics(), "P: " + this.minecraft.particleEngine.countParticles() + ". T: " + this.minecraft.level.getEntityCount(), this.minecraft.level.gatherChunkSourceStats(), "", String.format(Locale.ROOT, "Chunk-relative: %d %d %d", $$6.m_123341_() & 15, $$6.m_123342_() & 15, $$6.m_123343_() & 15) });
        } else {
            Entity $$7 = this.minecraft.getCameraEntity();
            Direction $$8 = $$7.getDirection();
            String $$13 = switch($$8) {
                case NORTH ->
                    "Towards negative Z";
                case SOUTH ->
                    "Towards positive Z";
                case WEST ->
                    "Towards negative X";
                case EAST ->
                    "Towards positive X";
                default ->
                    "Invalid";
            };
            ChunkPos $$14 = new ChunkPos($$6);
            if (!Objects.equals(this.lastPos, $$14)) {
                this.lastPos = $$14;
                this.clearChunkCache();
            }
            Level $$15 = this.getLevel();
            LongSet $$16 = (LongSet) ($$15 instanceof ServerLevel ? ((ServerLevel) $$15).getForcedChunks() : LongSets.EMPTY_SET);
            List<String> $$17 = Lists.newArrayList(new String[] { "Minecraft " + SharedConstants.getCurrentVersion().getName() + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType()) + ")", this.minecraft.fpsString, $$4, this.minecraft.levelRenderer.getChunkStatistics(), this.minecraft.levelRenderer.getEntityStatistics(), "P: " + this.minecraft.particleEngine.countParticles() + ". T: " + this.minecraft.level.getEntityCount(), this.minecraft.level.gatherChunkSourceStats() });
            String $$18 = this.getServerChunkStats();
            if ($$18 != null) {
                $$17.add($$18);
            }
            $$17.add(this.minecraft.level.m_46472_().location() + " FC: " + $$16.size());
            $$17.add("");
            $$17.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", this.minecraft.getCameraEntity().getX(), this.minecraft.getCameraEntity().getY(), this.minecraft.getCameraEntity().getZ()));
            $$17.add(String.format(Locale.ROOT, "Block: %d %d %d [%d %d %d]", $$6.m_123341_(), $$6.m_123342_(), $$6.m_123343_(), $$6.m_123341_() & 15, $$6.m_123342_() & 15, $$6.m_123343_() & 15));
            $$17.add(String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", $$14.x, SectionPos.blockToSectionCoord($$6.m_123342_()), $$14.z, $$14.getRegionLocalX(), $$14.getRegionLocalZ(), $$14.getRegionX(), $$14.getRegionZ()));
            $$17.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", $$8, $$13, Mth.wrapDegrees($$7.getYRot()), Mth.wrapDegrees($$7.getXRot())));
            LevelChunk $$19 = this.getClientChunk();
            if ($$19.isEmpty()) {
                $$17.add("Waiting for chunk...");
            } else {
                int $$20 = this.minecraft.level.getChunkSource().getLightEngine().getRawBrightness($$6, 0);
                int $$21 = this.minecraft.level.m_45517_(LightLayer.SKY, $$6);
                int $$22 = this.minecraft.level.m_45517_(LightLayer.BLOCK, $$6);
                $$17.add("Client Light: " + $$20 + " (" + $$21 + " sky, " + $$22 + " block)");
                LevelChunk $$23 = this.getServerChunk();
                StringBuilder $$24 = new StringBuilder("CH");
                for (Heightmap.Types $$25 : Heightmap.Types.values()) {
                    if ($$25.sendToClient()) {
                        $$24.append(" ").append((String) HEIGHTMAP_NAMES.get($$25)).append(": ").append($$19.m_5885_($$25, $$6.m_123341_(), $$6.m_123343_()));
                    }
                }
                $$17.add($$24.toString());
                $$24.setLength(0);
                $$24.append("SH");
                for (Heightmap.Types $$26 : Heightmap.Types.values()) {
                    if ($$26.keepAfterWorldgen()) {
                        $$24.append(" ").append((String) HEIGHTMAP_NAMES.get($$26)).append(": ");
                        if ($$23 != null) {
                            $$24.append($$23.m_5885_($$26, $$6.m_123341_(), $$6.m_123343_()));
                        } else {
                            $$24.append("??");
                        }
                    }
                }
                $$17.add($$24.toString());
                if ($$6.m_123342_() >= this.minecraft.level.m_141937_() && $$6.m_123342_() < this.minecraft.level.m_151558_()) {
                    $$17.add("Biome: " + printBiome(this.minecraft.level.m_204166_($$6)));
                    long $$27 = 0L;
                    float $$28 = 0.0F;
                    if ($$23 != null) {
                        $$28 = $$15.m_46940_();
                        $$27 = $$23.m_6319_();
                    }
                    DifficultyInstance $$29 = new DifficultyInstance($$15.m_46791_(), $$15.getDayTime(), $$27, $$28);
                    $$17.add(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", $$29.getEffectiveDifficulty(), $$29.getSpecialMultiplier(), this.minecraft.level.m_46468_() / 24000L));
                }
                if ($$23 != null && $$23.m_187675_()) {
                    $$17.add("Blending: Old");
                }
            }
            ServerLevel $$30 = this.getServerLevel();
            if ($$30 != null) {
                ServerChunkCache $$31 = $$30.getChunkSource();
                ChunkGenerator $$32 = $$31.getGenerator();
                RandomState $$33 = $$31.randomState();
                $$32.addDebugScreenInfo($$17, $$33, $$6);
                Climate.Sampler $$34 = $$33.sampler();
                BiomeSource $$35 = $$32.getBiomeSource();
                $$35.addDebugInfo($$17, $$6, $$34);
                NaturalSpawner.SpawnState $$36 = $$31.getLastSpawnState();
                if ($$36 != null) {
                    Object2IntMap<MobCategory> $$37 = $$36.getMobCategoryCounts();
                    int $$38 = $$36.getSpawnableChunkCount();
                    $$17.add("SC: " + $$38 + ", " + (String) Stream.of(MobCategory.values()).map(p_94068_ -> Character.toUpperCase(p_94068_.getName().charAt(0)) + ": " + $$37.getInt(p_94068_)).collect(Collectors.joining(", ")));
                } else {
                    $$17.add("SC: N/A");
                }
            }
            PostChain $$39 = this.minecraft.gameRenderer.currentEffect();
            if ($$39 != null) {
                $$17.add("Shader: " + $$39.getName());
            }
            $$17.add(this.minecraft.getSoundManager().getDebugString() + String.format(Locale.ROOT, " (Mood %d%%)", Math.round(this.minecraft.player.getCurrentMood() * 100.0F)));
            return $$17;
        }
    }

    private static String printBiome(Holder<Biome> holderBiome0) {
        return (String) holderBiome0.unwrap().map(p_205377_ -> p_205377_.location().toString(), p_205367_ -> "[unregistered " + p_205367_ + "]");
    }

    @Nullable
    private ServerLevel getServerLevel() {
        IntegratedServer $$0 = this.minecraft.getSingleplayerServer();
        return $$0 != null ? $$0.m_129880_(this.minecraft.level.m_46472_()) : null;
    }

    @Nullable
    private String getServerChunkStats() {
        ServerLevel $$0 = this.getServerLevel();
        return $$0 != null ? $$0.gatherChunkSourceStats() : null;
    }

    private Level getLevel() {
        return (Level) DataFixUtils.orElse(Optional.ofNullable(this.minecraft.getSingleplayerServer()).flatMap(p_288242_ -> Optional.ofNullable(p_288242_.m_129880_(this.minecraft.level.m_46472_()))), this.minecraft.level);
    }

    @Nullable
    private LevelChunk getServerChunk() {
        if (this.serverChunk == null) {
            ServerLevel $$0 = this.getServerLevel();
            if ($$0 != null) {
                this.serverChunk = $$0.getChunkSource().getChunkFuture(this.lastPos.x, this.lastPos.z, ChunkStatus.FULL, false).thenApply(p_205369_ -> (LevelChunk) p_205369_.map(p_205371_ -> (LevelChunk) p_205371_, p_205363_ -> null));
            }
            if (this.serverChunk == null) {
                this.serverChunk = CompletableFuture.completedFuture(this.getClientChunk());
            }
        }
        return (LevelChunk) this.serverChunk.getNow(null);
    }

    private LevelChunk getClientChunk() {
        if (this.clientChunk == null) {
            this.clientChunk = this.minecraft.level.m_6325_(this.lastPos.x, this.lastPos.z);
        }
        return this.clientChunk;
    }

    protected List<String> getSystemInformation() {
        long $$0 = Runtime.getRuntime().maxMemory();
        long $$1 = Runtime.getRuntime().totalMemory();
        long $$2 = Runtime.getRuntime().freeMemory();
        long $$3 = $$1 - $$2;
        List<String> $$4 = Lists.newArrayList(new String[] { String.format(Locale.ROOT, "Java: %s %dbit", System.getProperty("java.version"), this.minecraft.is64Bit() ? 64 : 32), String.format(Locale.ROOT, "Mem: % 2d%% %03d/%03dMB", $$3 * 100L / $$0, bytesToMegabytes($$3), bytesToMegabytes($$0)), String.format(Locale.ROOT, "Allocation rate: %03dMB /s", bytesToMegabytes(this.allocationRateCalculator.bytesAllocatedPerSecond($$3))), String.format(Locale.ROOT, "Allocated: % 2d%% %03dMB", $$1 * 100L / $$0, bytesToMegabytes($$1)), "", String.format(Locale.ROOT, "CPU: %s", GlUtil.getCpuInfo()), "", String.format(Locale.ROOT, "Display: %dx%d (%s)", Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight(), GlUtil.getVendor()), GlUtil.getRenderer(), GlUtil.getOpenGLVersion() });
        if (this.minecraft.showOnlyReducedInfo()) {
            return $$4;
        } else {
            if (this.block.getType() == HitResult.Type.BLOCK) {
                BlockPos $$5 = ((BlockHitResult) this.block).getBlockPos();
                BlockState $$6 = this.minecraft.level.m_8055_($$5);
                $$4.add("");
                $$4.add(ChatFormatting.UNDERLINE + "Targeted Block: " + $$5.m_123341_() + ", " + $$5.m_123342_() + ", " + $$5.m_123343_());
                $$4.add(String.valueOf(BuiltInRegistries.BLOCK.getKey($$6.m_60734_())));
                UnmodifiableIterator var12 = $$6.m_61148_().entrySet().iterator();
                while (var12.hasNext()) {
                    Entry<Property<?>, Comparable<?>> $$7 = (Entry<Property<?>, Comparable<?>>) var12.next();
                    $$4.add(this.getPropertyValueString($$7));
                }
                $$6.m_204343_().map(p_205365_ -> "#" + p_205365_.location()).forEach($$4::add);
            }
            if (this.liquid.getType() == HitResult.Type.BLOCK) {
                BlockPos $$8 = ((BlockHitResult) this.liquid).getBlockPos();
                FluidState $$9 = this.minecraft.level.m_6425_($$8);
                $$4.add("");
                $$4.add(ChatFormatting.UNDERLINE + "Targeted Fluid: " + $$8.m_123341_() + ", " + $$8.m_123342_() + ", " + $$8.m_123343_());
                $$4.add(String.valueOf(BuiltInRegistries.FLUID.getKey($$9.getType())));
                UnmodifiableIterator var17 = $$9.m_61148_().entrySet().iterator();
                while (var17.hasNext()) {
                    Entry<Property<?>, Comparable<?>> $$10 = (Entry<Property<?>, Comparable<?>>) var17.next();
                    $$4.add(this.getPropertyValueString($$10));
                }
                $$9.getTags().map(p_205379_ -> "#" + p_205379_.location()).forEach($$4::add);
            }
            Entity $$11 = this.minecraft.crosshairPickEntity;
            if ($$11 != null) {
                $$4.add("");
                $$4.add(ChatFormatting.UNDERLINE + "Targeted Entity");
                $$4.add(String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey($$11.getType())));
            }
            return $$4;
        }
    }

    private String getPropertyValueString(Entry<Property<?>, Comparable<?>> entryPropertyComparable0) {
        Property<?> $$1 = (Property<?>) entryPropertyComparable0.getKey();
        Comparable<?> $$2 = (Comparable<?>) entryPropertyComparable0.getValue();
        String $$3 = Util.getPropertyName($$1, $$2);
        if (Boolean.TRUE.equals($$2)) {
            $$3 = ChatFormatting.GREEN + $$3;
        } else if (Boolean.FALSE.equals($$2)) {
            $$3 = ChatFormatting.RED + $$3;
        }
        return $$1.getName() + ": " + $$3;
    }

    private void drawChart(GuiGraphics guiGraphics0, FrameTimer frameTimer1, int int2, int int3, boolean boolean4) {
        int $$5 = frameTimer1.getLogStart();
        int $$6 = frameTimer1.getLogEnd();
        long[] $$7 = frameTimer1.getLog();
        int $$9 = int2;
        int $$10 = Math.max(0, $$7.length - int3);
        int $$11 = $$7.length - $$10;
        int $$8 = frameTimer1.wrapIndex($$5 + $$10);
        long $$12 = 0L;
        int $$13 = Integer.MAX_VALUE;
        int $$14 = Integer.MIN_VALUE;
        for (int $$15 = 0; $$15 < $$11; $$15++) {
            int $$16 = (int) ($$7[frameTimer1.wrapIndex($$8 + $$15)] / 1000000L);
            $$13 = Math.min($$13, $$16);
            $$14 = Math.max($$14, $$16);
            $$12 += (long) $$16;
        }
        int $$17 = guiGraphics0.guiHeight();
        guiGraphics0.fill(RenderType.guiOverlay(), int2, $$17 - 60, int2 + $$11, $$17, -1873784752);
        while ($$8 != $$6) {
            int $$18 = frameTimer1.scaleSampleTo($$7[$$8], boolean4 ? 30 : 60, boolean4 ? 60 : 20);
            int $$19 = boolean4 ? 100 : 60;
            int $$20 = this.getSampleColor(Mth.clamp($$18, 0, $$19), 0, $$19 / 2, $$19);
            guiGraphics0.fill(RenderType.guiOverlay(), $$9, $$17 - $$18, $$9 + 1, $$17, $$20);
            $$9++;
            $$8 = frameTimer1.wrapIndex($$8 + 1);
        }
        if (boolean4) {
            guiGraphics0.fill(RenderType.guiOverlay(), int2 + 1, $$17 - 30 + 1, int2 + 14, $$17 - 30 + 10, -1873784752);
            guiGraphics0.drawString(this.font, "60 FPS", int2 + 2, $$17 - 30 + 2, 14737632, false);
            guiGraphics0.hLine(RenderType.guiOverlay(), int2, int2 + $$11 - 1, $$17 - 30, -1);
            guiGraphics0.fill(RenderType.guiOverlay(), int2 + 1, $$17 - 60 + 1, int2 + 14, $$17 - 60 + 10, -1873784752);
            guiGraphics0.drawString(this.font, "30 FPS", int2 + 2, $$17 - 60 + 2, 14737632, false);
            guiGraphics0.hLine(RenderType.guiOverlay(), int2, int2 + $$11 - 1, $$17 - 60, -1);
        } else {
            guiGraphics0.fill(RenderType.guiOverlay(), int2 + 1, $$17 - 60 + 1, int2 + 14, $$17 - 60 + 10, -1873784752);
            guiGraphics0.drawString(this.font, "20 TPS", int2 + 2, $$17 - 60 + 2, 14737632, false);
            guiGraphics0.hLine(RenderType.guiOverlay(), int2, int2 + $$11 - 1, $$17 - 60, -1);
        }
        guiGraphics0.hLine(RenderType.guiOverlay(), int2, int2 + $$11 - 1, $$17 - 1, -1);
        guiGraphics0.vLine(RenderType.guiOverlay(), int2, $$17 - 60, $$17, -1);
        guiGraphics0.vLine(RenderType.guiOverlay(), int2 + $$11 - 1, $$17 - 60, $$17, -1);
        int $$21 = this.minecraft.options.framerateLimit().get();
        if (boolean4 && $$21 > 0 && $$21 <= 250) {
            guiGraphics0.hLine(RenderType.guiOverlay(), int2, int2 + $$11 - 1, $$17 - 1 - (int) (1800.0 / (double) $$21), -16711681);
        }
        String $$22 = $$13 + " ms min";
        String $$23 = $$12 / (long) $$11 + " ms avg";
        String $$24 = $$14 + " ms max";
        guiGraphics0.drawString(this.font, $$22, int2 + 2, $$17 - 60 - 9, 14737632);
        guiGraphics0.drawCenteredString(this.font, $$23, int2 + $$11 / 2, $$17 - 60 - 9, 14737632);
        guiGraphics0.drawString(this.font, $$24, int2 + $$11 - this.font.width($$24), $$17 - 60 - 9, 14737632);
    }

    private int getSampleColor(int int0, int int1, int int2, int int3) {
        return int0 < int2 ? this.colorLerp(-16711936, -256, (float) int0 / (float) int2) : this.colorLerp(-256, -65536, (float) (int0 - int2) / (float) (int3 - int2));
    }

    private int colorLerp(int int0, int int1, float float2) {
        int $$3 = int0 >> 24 & 0xFF;
        int $$4 = int0 >> 16 & 0xFF;
        int $$5 = int0 >> 8 & 0xFF;
        int $$6 = int0 & 0xFF;
        int $$7 = int1 >> 24 & 0xFF;
        int $$8 = int1 >> 16 & 0xFF;
        int $$9 = int1 >> 8 & 0xFF;
        int $$10 = int1 & 0xFF;
        int $$11 = Mth.clamp((int) Mth.lerp(float2, (float) $$3, (float) $$7), 0, 255);
        int $$12 = Mth.clamp((int) Mth.lerp(float2, (float) $$4, (float) $$8), 0, 255);
        int $$13 = Mth.clamp((int) Mth.lerp(float2, (float) $$5, (float) $$9), 0, 255);
        int $$14 = Mth.clamp((int) Mth.lerp(float2, (float) $$6, (float) $$10), 0, 255);
        return $$11 << 24 | $$12 << 16 | $$13 << 8 | $$14;
    }

    private static long bytesToMegabytes(long long0) {
        return long0 / 1024L / 1024L;
    }

    static class AllocationRateCalculator {

        private static final int UPDATE_INTERVAL_MS = 500;

        private static final List<GarbageCollectorMXBean> GC_MBEANS = ManagementFactory.getGarbageCollectorMXBeans();

        private long lastTime = 0L;

        private long lastHeapUsage = -1L;

        private long lastGcCounts = -1L;

        private long lastRate = 0L;

        long bytesAllocatedPerSecond(long long0) {
            long $$1 = System.currentTimeMillis();
            if ($$1 - this.lastTime < 500L) {
                return this.lastRate;
            } else {
                long $$2 = gcCounts();
                if (this.lastTime != 0L && $$2 == this.lastGcCounts) {
                    double $$3 = (double) TimeUnit.SECONDS.toMillis(1L) / (double) ($$1 - this.lastTime);
                    long $$4 = long0 - this.lastHeapUsage;
                    this.lastRate = Math.round((double) $$4 * $$3);
                }
                this.lastTime = $$1;
                this.lastHeapUsage = long0;
                this.lastGcCounts = $$2;
                return this.lastRate;
            }
        }

        private static long gcCounts() {
            long $$0 = 0L;
            for (GarbageCollectorMXBean $$1 : GC_MBEANS) {
                $$0 += $$1.getCollectionCount();
            }
            return $$0;
        }
    }
}