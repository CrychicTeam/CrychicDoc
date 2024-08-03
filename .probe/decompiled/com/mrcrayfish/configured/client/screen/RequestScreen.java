package com.mrcrayfish.configured.client.screen;

import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RequestScreen extends ListMenuScreen implements IEditing {

    private static final Component REQUESTING_LABEL = Component.translatable("configured.gui.requesting_config");

    private static final Component FAILED_LABEL = Component.translatable("configured.gui.failed_request");

    private static final int TIMEOUT = 100;

    private int time;

    private boolean requested;

    private boolean failed;

    private Component message = null;

    private final IModConfig config;

    private IModConfig response;

    protected RequestScreen(Screen parent, Component title, ResourceLocation background, IModConfig config) {
        super(parent, title, background, 20);
        this.config = config;
    }

    @Override
    public IModConfig getActiveConfig() {
        return this.config;
    }

    @Override
    protected void constructEntries(List<ListMenuScreen.Item> entries) {
    }

    @Override
    protected void init() {
        super.init();
        if (!this.requested) {
            this.config.requestFromServer();
            this.requested = true;
        }
        this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 - 75, this.f_96544_ - 29, 150, 20, CommonComponents.GUI_CANCEL, button -> this.f_96541_.setScreen(this.parent)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float deltaTick) {
        super.render(graphics, mouseX, mouseY, deltaTick);
        if (this.failed) {
            graphics.drawCenteredString(this.f_96547_, this.message != null ? this.message : FAILED_LABEL, this.f_96543_ / 2, this.f_96544_ / 2, 8421504);
        } else if (this.requested) {
            String label = switch((int) (Util.getMillis() / 300L % 4L)) {
                case 1, 3 ->
                    "o O o";
                case 2 ->
                    "o o O";
                default ->
                    "O o o";
            };
            graphics.drawCenteredString(this.f_96547_, REQUESTING_LABEL, this.f_96543_ / 2, this.f_96544_ / 2 - 9, -1);
            graphics.drawCenteredString(this.f_96547_, label, this.f_96543_ / 2, this.f_96544_ / 2 + 5, 8421504);
        }
    }

    @Override
    public void tick() {
        if (!this.failed && this.time++ >= 100) {
            this.failed = true;
        }
        if (!this.failed && this.response != null && this.time >= 10) {
            this.f_96541_.setScreen(new ConfigScreen(this.parent, this.f_96539_, this.response, this.background));
            this.response = null;
        }
    }

    public void handleResponse(@Nullable IModConfig config, @Nullable Component message) {
        if (!this.failed) {
            if (config != null) {
                this.response = config;
            } else {
                this.failed = true;
                this.message = message;
            }
        }
    }
}