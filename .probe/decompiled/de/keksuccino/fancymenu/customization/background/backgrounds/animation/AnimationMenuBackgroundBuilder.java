package de.keksuccino.fancymenu.customization.background.backgrounds.animation;

import de.keksuccino.fancymenu.customization.background.MenuBackgroundBuilder;
import de.keksuccino.fancymenu.customization.background.SerializedMenuBackground;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimationMenuBackgroundBuilder extends MenuBackgroundBuilder<AnimationMenuBackground> {

    public AnimationMenuBackgroundBuilder() {
        super("animation");
    }

    @Override
    public boolean isDeprecated() {
        return true;
    }

    public void buildNewOrEditInstance(Screen currentScreen, @Nullable AnimationMenuBackground backgroundToEdit, @NotNull Consumer<AnimationMenuBackground> backgroundConsumer) {
        AnimationMenuBackground back = backgroundToEdit != null ? (AnimationMenuBackground) backgroundToEdit.copy() : null;
        if (back == null) {
            back = new AnimationMenuBackground(this);
        }
        AnimationMenuBackgroundConfigScreen s = new AnimationMenuBackgroundConfigScreen(currentScreen, back, call -> {
            if (call != null) {
                backgroundConsumer.accept(call);
            } else {
                backgroundConsumer.accept(backgroundToEdit);
            }
        });
        Minecraft.getInstance().setScreen(s);
    }

    public AnimationMenuBackground deserializeBackground(SerializedMenuBackground serializedMenuBackground) {
        AnimationMenuBackground b = new AnimationMenuBackground(this);
        b.animationName = serializedMenuBackground.getValue("animation_name");
        String restartOnLoad = serializedMenuBackground.getValue("restart_on_load");
        if (restartOnLoad != null && restartOnLoad.equals("true")) {
            b.restartOnMenuLoad = true;
        }
        return b;
    }

    public SerializedMenuBackground serializedBackground(AnimationMenuBackground background) {
        SerializedMenuBackground serialized = new SerializedMenuBackground();
        if (background.animationName != null) {
            serialized.putProperty("animation_name", background.animationName);
        }
        serialized.putProperty("restart_on_load", background.restartOnMenuLoad + "");
        return serialized;
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.translatable("fancymenu.background.animation");
    }

    @Nullable
    @Override
    public Component[] getDescription() {
        return LocalizationUtils.splitLocalizedLines("fancymenu.background.animation.desc");
    }
}