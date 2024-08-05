package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.rewards.CitadelPatreonRenderer;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

@OnlyIn(Dist.CLIENT)
public class GuiCitadelPatreonConfig extends OptionsSubScreen {

    private ForgeSlider distSlider;

    private ForgeSlider speedSlider;

    private ForgeSlider heightSlider;

    private Button changeButton;

    private float rotateDist;

    private float rotateSpeed;

    private float rotateHeight;

    private String followType;

    public GuiCitadelPatreonConfig(Screen parentScreenIn, Options gameSettingsIn) {
        super(parentScreenIn, gameSettingsIn, Component.translatable("citadel.gui.patreon_customization"));
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
        float distance = tag.contains("CitadelRotateDistance") ? tag.getFloat("CitadelRotateDistance") : 2.0F;
        float speed = tag.contains("CitadelRotateSpeed") ? tag.getFloat("CitadelRotateSpeed") : 1.0F;
        float height = tag.contains("CitadelRotateHeight") ? tag.getFloat("CitadelRotateHeight") : 1.0F;
        this.rotateDist = roundTo(distance, 3);
        this.rotateSpeed = roundTo(speed, 3);
        this.rotateHeight = roundTo(height, 3);
        this.followType = tag.contains("CitadelFollowerType") ? tag.getString("CitadelFollowerType") : "citadel";
    }

    private void setSliderValue(int i, float sliderValue) {
        boolean flag = false;
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
        if (i == 0) {
            this.rotateDist = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateDistance", this.rotateDist);
        } else if (i == 1) {
            this.rotateSpeed = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateSpeed", this.rotateSpeed);
        } else {
            this.rotateHeight = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateHeight", this.rotateHeight);
        }
        CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
        Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, Minecraft.getInstance().player.m_19879_()));
    }

    public static float roundTo(float value, int places) {
        return value;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        super.m_7856_();
        int i = this.f_96543_ / 2;
        int j = this.f_96544_ / 6;
        Button doneButton = Button.builder(CommonComponents.GUI_DONE, p_213079_1_ -> this.f_96541_.setScreen(this.f_96281_)).size(200, 20).pos(i - 100, j + 120).build();
        this.m_142416_(doneButton);
        this.m_142416_(this.distSlider = new ForgeSlider(i - 75 - 25, j + 30, 150, 20, Component.translatable("citadel.gui.orbit_dist").append(Component.translatable(": ")), Component.translatable(""), 0.125, 5.0, (double) this.rotateDist, 0.1, 1, true) {

            @Override
            protected void applyValue() {
                GuiCitadelPatreonConfig.this.setSliderValue(0, (float) this.getValue());
            }
        });
        Button reset1Button = Button.builder(Component.translatable("citadel.gui.reset"), p_213079_1_ -> this.setSliderValue(0, 0.4F)).size(40, 20).pos(i - 75 + 135, j + 30).build();
        this.m_142416_(reset1Button);
        this.m_142416_(this.speedSlider = new ForgeSlider(i - 75 - 25, j + 60, 150, 20, Component.translatable("citadel.gui.orbit_speed").append(Component.translatable(": ")), Component.translatable(""), 0.0, 5.0, (double) this.rotateSpeed, 0.1, 2, true) {

            @Override
            protected void applyValue() {
                GuiCitadelPatreonConfig.this.setSliderValue(1, (float) this.getValue());
            }
        });
        Button reset2Button = Button.builder(Component.translatable("citadel.gui.reset"), p_213079_1_ -> this.setSliderValue(1, 0.2F)).size(40, 20).pos(i - 75 + 135, j + 60).build();
        this.m_142416_(reset2Button);
        this.m_142416_(this.heightSlider = new ForgeSlider(i - 75 - 25, j + 90, 150, 20, Component.translatable("citadel.gui.orbit_height").append(Component.translatable(": ")), Component.translatable(""), 0.0, 2.0, (double) this.rotateHeight, 0.1, 2, true) {

            @Override
            protected void applyValue() {
                GuiCitadelPatreonConfig.this.setSliderValue(2, (float) this.getValue());
            }
        });
        Button reset3Button = Button.builder(Component.translatable("citadel.gui.reset"), p_213079_1_ -> this.setSliderValue(2, 0.5F)).size(40, 20).pos(i - 75 + 135, j + 90).build();
        this.m_142416_(reset3Button);
        this.changeButton = Button.builder(this.getTypeText(), p_213079_1_ -> {
            this.followType = CitadelPatreonRenderer.getIdOfNext(this.followType);
            CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
            if (tag != null) {
                tag.putString("CitadelFollowerType", this.followType);
                CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
            }
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, Minecraft.getInstance().player.m_19879_()));
            this.changeButton.m_93666_(this.getTypeText());
        }).size(200, 20).pos(i - 100, j).build();
        this.m_142416_(this.changeButton);
    }

    private Component getTypeText() {
        return Component.translatable("citadel.gui.follower_type").append(Component.translatable("citadel.follower." + this.followType));
    }
}