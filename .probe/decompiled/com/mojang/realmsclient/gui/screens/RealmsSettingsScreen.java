package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsSettingsScreen extends RealmsScreen {

    private static final int COMPONENT_WIDTH = 212;

    private static final Component NAME_LABEL = Component.translatable("mco.configure.world.name");

    private static final Component DESCRIPTION_LABEL = Component.translatable("mco.configure.world.description");

    private final RealmsConfigureWorldScreen configureWorldScreen;

    private final RealmsServer serverData;

    private Button doneButton;

    private EditBox descEdit;

    private EditBox nameEdit;

    public RealmsSettingsScreen(RealmsConfigureWorldScreen realmsConfigureWorldScreen0, RealmsServer realmsServer1) {
        super(Component.translatable("mco.configure.world.settings.title"));
        this.configureWorldScreen = realmsConfigureWorldScreen0;
        this.serverData = realmsServer1;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.descEdit.tick();
        this.doneButton.f_93623_ = !this.nameEdit.getValue().trim().isEmpty();
    }

    @Override
    public void init() {
        int $$0 = this.f_96543_ / 2 - 106;
        this.doneButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.configure.world.buttons.done"), p_89847_ -> this.save()).bounds($$0 - 2, m_120774_(12), 106, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280749_ -> this.f_96541_.setScreen(this.configureWorldScreen)).bounds(this.f_96543_ / 2 + 2, m_120774_(12), 106, 20).build());
        String $$1 = this.serverData.state == RealmsServer.State.OPEN ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open";
        Button $$2 = Button.builder(Component.translatable($$1), p_287303_ -> {
            if (this.serverData.state == RealmsServer.State.OPEN) {
                Component $$1x = Component.translatable("mco.configure.world.close.question.line1");
                Component $$2x = Component.translatable("mco.configure.world.close.question.line2");
                this.f_96541_.setScreen(new RealmsLongConfirmationScreen(p_280750_ -> {
                    if (p_280750_) {
                        this.configureWorldScreen.closeTheWorld(this);
                    } else {
                        this.f_96541_.setScreen(this);
                    }
                }, RealmsLongConfirmationScreen.Type.INFO, $$1x, $$2x, true));
            } else {
                this.configureWorldScreen.openTheWorld(false, this);
            }
        }).bounds(this.f_96543_ / 2 - 53, m_120774_(0), 106, 20).build();
        this.m_142416_($$2);
        this.nameEdit = new EditBox(this.f_96541_.font, $$0, m_120774_(4), 212, 20, null, Component.translatable("mco.configure.world.name"));
        this.nameEdit.setMaxLength(32);
        this.nameEdit.setValue(this.serverData.getName());
        this.m_7787_(this.nameEdit);
        this.m_94725_(this.nameEdit);
        this.descEdit = new EditBox(this.f_96541_.font, $$0, m_120774_(8), 212, 20, null, Component.translatable("mco.configure.world.description"));
        this.descEdit.setMaxLength(32);
        this.descEdit.setValue(this.serverData.getDescription());
        this.m_7787_(this.descEdit);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(this.configureWorldScreen);
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 17, 16777215);
        guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 106, m_120774_(3), 10526880, false);
        guiGraphics0.drawString(this.f_96547_, DESCRIPTION_LABEL, this.f_96543_ / 2 - 106, m_120774_(7), 10526880, false);
        this.nameEdit.m_88315_(guiGraphics0, int1, int2, float3);
        this.descEdit.m_88315_(guiGraphics0, int1, int2, float3);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    public void save() {
        this.configureWorldScreen.saveSettings(this.nameEdit.getValue(), this.descEdit.getValue());
    }
}