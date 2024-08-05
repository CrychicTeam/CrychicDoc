package journeymap.client.ui.fullscreen.layer;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import journeymap.client.JourneymapClient;
import journeymap.client.api.impl.BlockInfo;
import journeymap.client.data.DataCache;
import journeymap.client.event.dispatchers.FullscreenEventDispatcher;
import journeymap.client.io.FileHandler;
import journeymap.client.io.ThemeLoader;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.RegionCoord;
import journeymap.client.properties.FullMapProperties;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.option.LocationFormat;
import journeymap.client.ui.theme.Theme;
import journeymap.client.world.JmBlockAccess;
import journeymap.common.helper.BiomeHelper;
import journeymap.common.nbt.RegionData;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockInfoLayer extends Layer {

    private final List<DrawStep> drawStepList = new ArrayList(1);

    LocationFormat locationFormat = new LocationFormat();

    LocationFormat.LocationFormatKeys locationFormatKeys;

    BlockPos lastCoord = null;

    BlockInfoLayer.PlayerInfoStep playerInfoStep;

    BlockInfoLayer.BlockInfoStep blockInfoStep;

    private boolean isSinglePlayer;

    long lastHover = 0L;

    private final long hoverDelay = 25L;

    private final Minecraft mc;

    public BlockInfoLayer(Fullscreen fullscreen) {
        super(fullscreen);
        this.blockInfoStep = new BlockInfoLayer.BlockInfoStep();
        this.playerInfoStep = new BlockInfoLayer.PlayerInfoStep();
        this.mc = Minecraft.getInstance();
        this.isSinglePlayer = this.mc.hasSingleplayerServer();
    }

    @Override
    public List<DrawStep> onMouseMove(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockPos, float fontScale, boolean isScrolling) {
        long now = Util.getMillis();
        java.awt.geom.Rectangle2D.Double optionsToolbarRect = this.fullscreen.getOptionsToolbarBounds();
        java.awt.geom.Rectangle2D.Double menuToolbarRect = this.fullscreen.getMenuToolbarBounds();
        if (optionsToolbarRect != null && menuToolbarRect != null) {
            if (now - this.lastHover < 25L) {
                return this.drawStepList;
            } else {
                if (this.drawStepList.isEmpty()) {
                    this.drawStepList.add(this.playerInfoStep);
                    this.drawStepList.add(this.blockInfoStep);
                }
                this.playerInfoStep.update((double) (mc.getWindow().getScreenWidth() / 2), optionsToolbarRect.getMaxY());
                if (!blockPos.equals(this.lastCoord)) {
                    this.getBlockInfo(blockPos, gridRenderer, (info, builder) -> {
                        FullscreenEventDispatcher.moveEvent(Minecraft.getInstance().level.m_46472_(), builder.build(), mousePosition);
                        this.blockInfoStep.update(info, (double) (gridRenderer.getWidth() >> 1), menuToolbarRect.getMinY());
                    });
                }
                this.lastHover = now;
                return this.drawStepList;
            }
        } else {
            return Collections.emptyList();
        }
    }

    private void getBlockInfo(BlockPos blockPos, GridRenderer gridRenderer, BiConsumer<String, BlockInfo.Builder> blockInfo) {
        BlockInfo.Builder infoBuilder = new BlockInfo.Builder();
        CompletableFuture.supplyAsync(() -> this.buildBlockInfo(blockPos, gridRenderer, infoBuilder), Util.backgroundExecutor()).whenCompleteAsync((info, throwable) -> blockInfo.accept(info, infoBuilder), Util.backgroundExecutor());
    }

    private String buildBlockInfo(BlockPos blockPos, GridRenderer gridRenderer, BlockInfo.Builder infoBuilder) {
        this.lastCoord = blockPos;
        ChunkMD chunkMD = DataCache.INSTANCE.getChunkMD(blockPos);
        String info = "";
        infoBuilder.withBlockPos(blockPos);
        RegionCoord regionCoord = RegionCoord.fromChunkPos(FileHandler.getJMWorldDir(Minecraft.getInstance()), gridRenderer.getMapType(), blockPos.m_123341_() >> 4, blockPos.m_123343_() >> 4);
        RegionData regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(regionCoord, gridRenderer.getMapType());
        Block block = null;
        Biome biome = null;
        if (regionData != null && JourneymapClient.getInstance().getCoreProperties().dataCachingEnabled.get()) {
            BlockState blockState = regionData.getBlockState(blockPos);
            if (blockState != null) {
                block = blockState.m_60734_();
                infoBuilder.withBlockState(blockState);
            }
            biome = regionData.getBiome(blockPos);
        }
        if (chunkMD != null && chunkMD.hasChunk() && block == null) {
            BlockMD blockMD = chunkMD.getBlockMD(blockPos.above());
            if (blockMD == null || blockMD.isIgnore()) {
                blockMD = chunkMD.getBlockMD(blockPos);
            }
            if (blockMD.isIgnore()) {
                blockMD = chunkMD.getBlockMD(blockPos.below());
            }
            if (!blockMD.isIgnore()) {
                block = blockMD.getBlock();
            }
            infoBuilder.withBlockState(blockMD.getBlockState());
            infoBuilder.withChunk(chunkMD.getChunk());
        }
        if (biome == null) {
            biome = JmBlockAccess.INSTANCE.getBiome(blockPos);
        }
        String biomeName = "";
        if (block != null && biome != null) {
            biomeName = BiomeHelper.getTranslatedBiomeName(biome);
        }
        infoBuilder.withChunkPos(new ChunkPos(blockPos.m_123341_() >> 4, blockPos.m_123343_() >> 4));
        infoBuilder.withBiome(biome);
        infoBuilder.withRegionX(regionCoord.regionX);
        infoBuilder.withRegionZ(regionCoord.regionZ);
        info = this.getBlockInfo(blockPos, biomeName, regionCoord);
        if (block != null) {
            infoBuilder.withBlock(block);
            String blockName = BlockMD.getBlockName(block);
            info = String.format("%s ■ %s", blockName, info);
        }
        return info;
    }

    private String getBlockInfo(BlockPos blockPos, String biomeName, RegionCoord regionCoord) {
        FullMapProperties fullMapProperties = JourneymapClient.getInstance().getFullMapProperties();
        String region = "Region: x:" + regionCoord.regionX + " z:" + regionCoord.regionZ;
        this.locationFormatKeys = this.locationFormat.getFormatKeys(fullMapProperties.locationFormat.get());
        return this.locationFormatKeys.format(fullMapProperties.locationFormatVerbose.get(), blockPos.m_123341_(), blockPos.m_123343_(), blockPos.m_123342_(), blockPos.m_123342_() >> 4) + " " + biomeName + " " + region;
    }

    @Override
    public List<DrawStep> onMouseClick(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, int button, boolean doubleClick, float fontScale) {
        if (button == 1) {
            this.fullscreen.popupMenu.displayBasicOptions(blockCoord);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean propagateClick() {
        return true;
    }

    class BlockInfoStep implements DrawStep {

        private Theme.LabelSpec labelSpec;

        private double x;

        private double y;

        private String text;

        void update(String text, double x, double y) {
            Theme theme = ThemeLoader.getCurrentTheme();
            this.labelSpec = theme.fullscreen.statusLabel;
            this.text = text;
            this.x = x;
            this.y = y - (double) theme.container.toolbar.horizontal.margin * BlockInfoLayer.this.fullscreen.getScaleFactor();
        }

        @Override
        public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
            if (pass == DrawStep.Pass.Text && JourneymapClient.getInstance().getFullMapProperties().showMouseLoc.get() && this.text != null) {
                DrawUtil.drawLabel(graphics, this.text, this.labelSpec, this.x, this.y, DrawUtil.HAlign.Center, DrawUtil.VAlign.Above, fontScale, 0.0);
            }
        }

        @Override
        public int getDisplayOrder() {
            return 0;
        }

        @Override
        public String getModId() {
            return "journeymap";
        }
    }

    class PlayerInfoStep implements DrawStep {

        private Theme.LabelSpec labelSpec;

        private String prefix;

        private double x;

        private double y;

        void update(double x, double y) {
            Theme theme = ThemeLoader.getCurrentTheme();
            this.labelSpec = theme.fullscreen.statusLabel;
            if (this.prefix == null) {
                this.prefix = BlockInfoLayer.this.mc.player.m_7755_().getString() + " ■ ";
            }
            this.x = x;
            this.y = y + (double) theme.container.toolbar.horizontal.margin * BlockInfoLayer.this.fullscreen.getScaleFactor();
        }

        @Override
        public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
            if (pass == DrawStep.Pass.Text && JourneymapClient.getInstance().getFullMapProperties().showPlayerLoc.get()) {
                DrawUtil.drawLabel(graphics, this.prefix + Fullscreen.state().playerLastPos, this.labelSpec, this.x, this.y, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, fontScale, 0.0);
            }
        }

        @Override
        public int getDisplayOrder() {
            return 0;
        }

        @Override
        public String getModId() {
            return "journeymap";
        }
    }
}