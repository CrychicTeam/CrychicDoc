package org.violetmoon.quark.base.client.config;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.config.QuarkGeneralConfig;
import org.violetmoon.zeta.client.event.play.ZScreen;
import org.violetmoon.zeta.event.bus.PlayEvent;

public class QButtonHandler {

    @PlayEvent
    public static void onGuiInit(ZScreen.Init.Post event) {
        if (QuarkGeneralConfig.enableQButton) {
            Set<String> targetButtonTranslationKeys = getTargetButtons(event.getScreen());
            if (targetButtonTranslationKeys != null && !targetButtonTranslationKeys.isEmpty()) {
                Set<String> targetButtonNames = (Set<String>) targetButtonTranslationKeys.stream().map(x$0 -> I18n.get(x$0)).collect(Collectors.toSet());
                for (GuiEventListener listener : event.getListenersList()) {
                    if (listener instanceof AbstractWidget widget && targetButtonNames.contains(widget.getMessage().getString())) {
                        int x = widget.getX();
                        if (QuarkGeneralConfig.qButtonOnRight) {
                            x += widget.getWidth() + 4;
                        } else {
                            x -= 24;
                        }
                        Button qButton = new QButton(x, widget.getY());
                        event.addListener(qButton);
                        return;
                    }
                }
            }
        }
    }

    @Nullable
    private static Set<String> getTargetButtons(Screen gui) {
        if (gui instanceof TitleScreen) {
            return QuarkGeneralConfig.qButtonOnRight ? Set.of("menu.online") : Set.of("fml.menu.mods.title", "fml.menu.mods");
        } else if (gui instanceof PauseScreen) {
            return QuarkGeneralConfig.qButtonOnRight ? Set.of("menu.shareToLan", "menu.playerReporting") : Set.of("menu.options");
        } else {
            return null;
        }
    }
}