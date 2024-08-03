package net.minecraft.client.gui.screens;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.ChunkStatus;

public class LevelLoadingScreen extends Screen {

    private static final long NARRATION_DELAY_MS = 2000L;

    private final StoringChunkProgressListener progressListener;

    private long lastNarration = -1L;

    private boolean done;

    private static final Object2IntMap<ChunkStatus> COLORS = Util.make(new Object2IntOpenHashMap(), p_280803_ -> {
        p_280803_.defaultReturnValue(0);
        p_280803_.put(ChunkStatus.EMPTY, 5526612);
        p_280803_.put(ChunkStatus.STRUCTURE_STARTS, 10066329);
        p_280803_.put(ChunkStatus.STRUCTURE_REFERENCES, 6250897);
        p_280803_.put(ChunkStatus.BIOMES, 8434258);
        p_280803_.put(ChunkStatus.NOISE, 13750737);
        p_280803_.put(ChunkStatus.SURFACE, 7497737);
        p_280803_.put(ChunkStatus.CARVERS, 3159410);
        p_280803_.put(ChunkStatus.FEATURES, 2213376);
        p_280803_.put(ChunkStatus.INITIALIZE_LIGHT, 13421772);
        p_280803_.put(ChunkStatus.LIGHT, 16769184);
        p_280803_.put(ChunkStatus.SPAWN, 15884384);
        p_280803_.put(ChunkStatus.FULL, 16777215);
    });

    public LevelLoadingScreen(StoringChunkProgressListener storingChunkProgressListener0) {
        super(GameNarrator.NO_TITLE);
        this.progressListener = storingChunkProgressListener0;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected boolean shouldNarrateNavigation() {
        return false;
    }

    @Override
    public void removed() {
        this.done = true;
        this.m_169407_(true);
    }

    @Override
    protected void updateNarratedWidget(NarrationElementOutput narrationElementOutput0) {
        if (this.done) {
            narrationElementOutput0.add(NarratedElementType.TITLE, Component.translatable("narrator.loading.done"));
        } else {
            String $$1 = this.getFormattedProgress();
            narrationElementOutput0.add(NarratedElementType.TITLE, $$1);
        }
    }

    private String getFormattedProgress() {
        return Mth.clamp(this.progressListener.getProgress(), 0, 100) + "%";
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        long $$4 = Util.getMillis();
        if ($$4 - this.lastNarration > 2000L) {
            this.lastNarration = $$4;
            this.m_169407_(true);
        }
        int $$5 = this.f_96543_ / 2;
        int $$6 = this.f_96544_ / 2;
        int $$7 = 30;
        renderChunks(guiGraphics0, this.progressListener, $$5, $$6 + 30, 2, 0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.getFormattedProgress(), $$5, $$6 - 9 / 2 - 30, 16777215);
    }

    public static void renderChunks(GuiGraphics guiGraphics0, StoringChunkProgressListener storingChunkProgressListener1, int int2, int int3, int int4, int int5) {
        int $$6 = int4 + int5;
        int $$7 = storingChunkProgressListener1.getFullDiameter();
        int $$8 = $$7 * $$6 - int5;
        int $$9 = storingChunkProgressListener1.getDiameter();
        int $$10 = $$9 * $$6 - int5;
        int $$11 = int2 - $$10 / 2;
        int $$12 = int3 - $$10 / 2;
        int $$13 = $$8 / 2 + 1;
        int $$14 = -16772609;
        guiGraphics0.drawManaged(() -> {
            if (int5 != 0) {
                guiGraphics0.fill(int2 - $$13, int3 - $$13, int2 - $$13 + 1, int3 + $$13, -16772609);
                guiGraphics0.fill(int2 + $$13 - 1, int3 - $$13, int2 + $$13, int3 + $$13, -16772609);
                guiGraphics0.fill(int2 - $$13, int3 - $$13, int2 + $$13, int3 - $$13 + 1, -16772609);
                guiGraphics0.fill(int2 - $$13, int3 + $$13 - 1, int2 + $$13, int3 + $$13, -16772609);
            }
            for (int $$11x = 0; $$11x < $$9; $$11x++) {
                for (int $$12x = 0; $$12x < $$9; $$12x++) {
                    ChunkStatus $$13x = storingChunkProgressListener1.getStatus($$11x, $$12x);
                    int $$14x = $$11 + $$11x * $$6;
                    int $$15 = $$12 + $$12x * $$6;
                    guiGraphics0.fill($$14x, $$15, $$14x + int4, $$15 + int4, COLORS.getInt($$13x) | 0xFF000000);
                }
            }
        });
    }
}