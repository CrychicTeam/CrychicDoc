package me.shedaniel.clothconfig2.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClothRequiresRestartScreen extends ConfirmScreen {

    public ClothRequiresRestartScreen(Screen parent) {
        super(t -> {
            if (t) {
                Minecraft.getInstance().stop();
            } else {
                Minecraft.getInstance().setScreen(parent);
            }
        }, Component.translatable("text.cloth-config.restart_required"), Component.translatable("text.cloth-config.restart_required_sub"), Component.translatable("text.cloth-config.exit_minecraft"), Component.translatable("text.cloth-config.ignore_restart"));
    }
}