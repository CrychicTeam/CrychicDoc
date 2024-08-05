package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.util.task.WorldCreationTask;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsCreateRealmScreen extends RealmsScreen {

    private static final Component NAME_LABEL = Component.translatable("mco.configure.world.name");

    private static final Component DESCRIPTION_LABEL = Component.translatable("mco.configure.world.description");

    private final RealmsServer server;

    private final RealmsMainScreen lastScreen;

    private EditBox nameBox;

    private EditBox descriptionBox;

    private Button createButton;

    public RealmsCreateRealmScreen(RealmsServer realmsServer0, RealmsMainScreen realmsMainScreen1) {
        super(Component.translatable("mco.selectServer.create"));
        this.server = realmsServer0;
        this.lastScreen = realmsMainScreen1;
    }

    @Override
    public void tick() {
        if (this.nameBox != null) {
            this.nameBox.tick();
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.tick();
        }
    }

    @Override
    public void init() {
        this.createButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.create.world"), p_88592_ -> this.createWorld()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120 + 17, 97, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280726_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 + 5, this.f_96544_ / 4 + 120 + 17, 95, 20).build());
        this.createButton.f_93623_ = false;
        this.nameBox = new EditBox(this.f_96541_.font, this.f_96543_ / 2 - 100, 65, 200, 20, null, Component.translatable("mco.configure.world.name"));
        this.m_7787_(this.nameBox);
        this.m_264313_(this.nameBox);
        this.descriptionBox = new EditBox(this.f_96541_.font, this.f_96543_ / 2 - 100, 115, 200, 20, null, Component.translatable("mco.configure.world.description"));
        this.m_7787_(this.descriptionBox);
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        boolean $$2 = super.m_5534_(char0, int1);
        this.createButton.f_93623_ = this.valid();
        return $$2;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(this.lastScreen);
            return true;
        } else {
            boolean $$3 = super.m_7933_(int0, int1, int2);
            this.createButton.f_93623_ = this.valid();
            return $$3;
        }
    }

    private void createWorld() {
        if (this.valid()) {
            RealmsResetWorldScreen $$0 = new RealmsResetWorldScreen(this.lastScreen, this.server, Component.translatable("mco.selectServer.create"), Component.translatable("mco.create.world.subtitle"), 10526880, Component.translatable("mco.create.world.skip"), () -> this.f_96541_.execute(() -> this.f_96541_.setScreen(this.lastScreen.newScreen())), () -> this.f_96541_.setScreen(this.lastScreen.newScreen()));
            $$0.setResetTitle(Component.translatable("mco.create.world.reset.title"));
            this.f_96541_.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new WorldCreationTask(this.server.id, this.nameBox.getValue(), this.descriptionBox.getValue(), $$0)));
        }
    }

    private boolean valid() {
        return !this.nameBox.getValue().trim().isEmpty();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 11, 16777215);
        guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 100, 52, 10526880, false);
        guiGraphics0.drawString(this.f_96547_, DESCRIPTION_LABEL, this.f_96543_ / 2 - 100, 102, 10526880, false);
        if (this.nameBox != null) {
            this.nameBox.m_88315_(guiGraphics0, int1, int2, float3);
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.m_88315_(guiGraphics0, int1, int2, float3);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}