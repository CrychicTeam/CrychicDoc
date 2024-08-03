package net.minecraft.client.gui.screens;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class ReceivingLevelScreen extends Screen {

    private static final Component DOWNLOADING_TERRAIN_TEXT = Component.translatable("multiplayer.downloadingTerrain");

    private static final long CHUNK_LOADING_START_WAIT_LIMIT_MS = 30000L;

    private boolean loadingPacketsReceived = false;

    private boolean oneTickSkipped = false;

    private final long createdAt = System.currentTimeMillis();

    public ReceivingLevelScreen() {
        super(GameNarrator.NO_TITLE);
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
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280039_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, DOWNLOADING_TERRAIN_TEXT, this.f_96543_ / 2, this.f_96544_ / 2 - 50, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() > this.createdAt + 30000L) {
            this.onClose();
        } else {
            if (this.oneTickSkipped) {
                if (this.f_96541_.player == null) {
                    return;
                }
                BlockPos $$0 = this.f_96541_.player.m_20183_();
                boolean $$1 = this.f_96541_.level != null && this.f_96541_.level.m_151562_($$0.m_123342_());
                if ($$1 || this.f_96541_.levelRenderer.isChunkCompiled($$0) || this.f_96541_.player.m_5833_() || !this.f_96541_.player.m_6084_()) {
                    this.onClose();
                }
            } else {
                this.oneTickSkipped = this.loadingPacketsReceived;
            }
        }
    }

    @Override
    public void onClose() {
        this.f_96541_.getNarrator().sayNow(Component.translatable("narrator.ready_to_play"));
        super.onClose();
    }

    public void loadingPacketsReceived() {
        this.loadingPacketsReceived = true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}