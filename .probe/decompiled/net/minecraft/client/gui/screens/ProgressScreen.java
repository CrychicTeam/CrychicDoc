package net.minecraft.client.gui.screens;

import javax.annotation.Nullable;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProgressListener;

public class ProgressScreen extends Screen implements ProgressListener {

    @Nullable
    private Component header;

    @Nullable
    private Component stage;

    private int progress;

    private boolean stop;

    private final boolean clearScreenAfterStop;

    public ProgressScreen(boolean boolean0) {
        super(GameNarrator.NO_TITLE);
        this.clearScreenAfterStop = boolean0;
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
    public void progressStartNoAbort(Component component0) {
        this.progressStart(component0);
    }

    @Override
    public void progressStart(Component component0) {
        this.header = component0;
        this.progressStage(Component.translatable("progress.working"));
    }

    @Override
    public void progressStage(Component component0) {
        this.stage = component0;
        this.progressStagePercentage(0);
    }

    @Override
    public void progressStagePercentage(int int0) {
        this.progress = int0;
    }

    @Override
    public void stop() {
        this.stop = true;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.stop) {
            if (this.clearScreenAfterStop) {
                this.f_96541_.setScreen(null);
            }
        } else {
            this.m_280273_(guiGraphics0);
            if (this.header != null) {
                guiGraphics0.drawCenteredString(this.f_96547_, this.header, this.f_96543_ / 2, 70, 16777215);
            }
            if (this.stage != null && this.progress != 0) {
                guiGraphics0.drawCenteredString(this.f_96547_, Component.empty().append(this.stage).append(" " + this.progress + "%"), this.f_96543_ / 2, 90, 16777215);
            }
            super.render(guiGraphics0, int1, int2, float3);
        }
    }
}