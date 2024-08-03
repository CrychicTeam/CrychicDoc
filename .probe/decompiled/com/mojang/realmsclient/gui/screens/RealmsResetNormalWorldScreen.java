package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.util.LevelType;
import com.mojang.realmsclient.util.WorldGenerationInfo;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsResetNormalWorldScreen extends RealmsScreen {

    private static final Component SEED_LABEL = Component.translatable("mco.reset.world.seed");

    private final Consumer<WorldGenerationInfo> callback;

    private EditBox seedEdit;

    private LevelType levelType = LevelType.DEFAULT;

    private boolean generateStructures = true;

    private final Component buttonTitle;

    public RealmsResetNormalWorldScreen(Consumer<WorldGenerationInfo> consumerWorldGenerationInfo0, Component component1) {
        super(Component.translatable("mco.reset.world.generate"));
        this.callback = consumerWorldGenerationInfo0;
        this.buttonTitle = component1;
    }

    @Override
    public void tick() {
        this.seedEdit.tick();
        super.m_86600_();
    }

    @Override
    public void init() {
        this.seedEdit = new EditBox(this.f_96541_.font, this.f_96543_ / 2 - 100, m_120774_(2), 200, 20, null, Component.translatable("mco.reset.world.seed"));
        this.seedEdit.setMaxLength(32);
        this.m_7787_(this.seedEdit);
        this.m_264313_(this.seedEdit);
        this.m_142416_(CycleButton.<LevelType>builder(LevelType::m_167607_).withValues(LevelType.values()).withInitialValue(this.levelType).create(this.f_96543_ / 2 - 102, m_120774_(4), 205, 20, Component.translatable("selectWorld.mapType"), (p_167441_, p_167442_) -> this.levelType = p_167442_));
        this.m_142416_(CycleButton.onOffBuilder(this.generateStructures).create(this.f_96543_ / 2 - 102, m_120774_(6) - 2, 205, 20, Component.translatable("selectWorld.mapFeatures"), (p_167444_, p_167445_) -> this.generateStructures = p_167445_));
        this.m_142416_(Button.builder(this.buttonTitle, p_89291_ -> this.callback.accept(new WorldGenerationInfo(this.seedEdit.getValue(), this.levelType, this.generateStructures))).bounds(this.f_96543_ / 2 - 102, m_120774_(12), 97, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_89288_ -> this.onClose()).bounds(this.f_96543_ / 2 + 8, m_120774_(12), 97, 20).build());
    }

    @Override
    public void onClose() {
        this.callback.accept(null);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 17, 16777215);
        guiGraphics0.drawString(this.f_96547_, SEED_LABEL, this.f_96543_ / 2 - 100, m_120774_(1), 10526880, false);
        this.seedEdit.m_88315_(guiGraphics0, int1, int2, float3);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}