package me.jellysquid.mods.sodium.client.gui.screen;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigCorruptedScreen extends Screen {

    private static final String TEXT_BODY_RAW = "A problem occurred while trying to load the configuration file. This\ncan happen when the file has been corrupted on disk, or when trying\nto manually edit the file by hand.\n\nWe can attempt to fix this problem automatically by restoring the\nconfig file back to known-good defaults, but you will lose any\nchanges that have since been made to your video settings.\n\nMore information about the error can be found in the log file.\n";

    private static final List<Component> TEXT_BODY = (List<Component>) Arrays.stream("A problem occurred while trying to load the configuration file. This\ncan happen when the file has been corrupted on disk, or when trying\nto manually edit the file by hand.\n\nWe can attempt to fix this problem automatically by restoring the\nconfig file back to known-good defaults, but you will lose any\nchanges that have since been made to your video settings.\n\nMore information about the error can be found in the log file.\n".split("\n")).map(Component::m_237113_).collect(Collectors.toList());

    private static final Component TEXT_BUTTON_RESTORE_DEFAULTS = Component.literal("Restore defaults");

    private static final Component TEXT_BUTTON_CLOSE_GAME = Component.literal("Close game");

    private final Supplier<Screen> child;

    public ConfigCorruptedScreen(Supplier<Screen> child) {
        super(Component.literal("Config corruption detected"));
        this.child = child;
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(Button.builder(TEXT_BUTTON_RESTORE_DEFAULTS, btn -> {
            SodiumClientMod.restoreDefaultOptions();
            Minecraft.getInstance().setScreen((Screen) this.child.get());
        }).bounds(32, this.f_96544_ - 40, 174, 20).build());
        this.m_142416_(Button.builder(TEXT_BUTTON_CLOSE_GAME, btn -> Minecraft.getInstance().stop()).bounds(this.f_96543_ - 174 - 32, this.f_96544_ - 40, 174, 20).build());
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        this.m_280273_(drawContext);
        super.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawString(this.f_96547_, Component.literal("Sodium Renderer"), 32, 32, 16777215);
        drawContext.drawString(this.f_96547_, Component.literal("Could not load configuration file"), 32, 48, 16711680);
        for (int i = 0; i < TEXT_BODY.size(); i++) {
            if (!((Component) TEXT_BODY.get(i)).getString().isEmpty()) {
                drawContext.drawString(this.f_96547_, (Component) TEXT_BODY.get(i), 32, 68 + i * 12, 16777215);
            }
        }
    }
}