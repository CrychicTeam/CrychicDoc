package org.violetmoon.quark.content.client.module;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.language.I18n;
import org.violetmoon.zeta.client.event.load.ZKeyMapping;
import org.violetmoon.zeta.client.event.play.ZScreen;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "client")
public class BackButtonKeybindModule extends ZetaModule {

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends BackButtonKeybindModule {

        private KeyMapping backKey;

        private List<GuiEventListener> listeners;

        @LoadEvent
        public void registerKeybinds(ZKeyMapping event) {
            this.backKey = event.initMouse("quark.keybind.back", 4, "quark.gui.keygroup.misc", key -> key.getType() != InputConstants.Type.MOUSE || key.getValue() != 0);
        }

        @PlayEvent
        public void openGui(ZScreen.Init.Pre event) {
            this.listeners = event.getListenersList();
        }

        @PlayEvent
        public void onKeyInput(ZScreen.KeyPressed.Post event) {
            if (this.backKey.getKey().getType() == InputConstants.Type.KEYSYM && event.getKeyCode() == this.backKey.getKey().getValue()) {
                this.clicc();
            }
        }

        @PlayEvent
        public void onMouseInput(ZScreen.MouseButtonPressed.Post event) {
            int btn = event.getButton();
            if (this.backKey.getKey().getType() == InputConstants.Type.MOUSE && btn != 0 && btn == this.backKey.getKey().getValue()) {
                this.clicc();
            }
        }

        private void clicc() {
            ImmutableSet<String> buttons = ImmutableSet.of(I18n.get("gui.back"), I18n.get("gui.done"), I18n.get("gui.cancel"), I18n.get("gui.toTitle"), I18n.get("gui.toMenu"), I18n.get("quark.gui.config.save"), new String[0]);
            UnmodifiableIterator mc = buttons.iterator();
            while (mc.hasNext()) {
                String b = (String) mc.next();
                for (GuiEventListener listener : this.listeners) {
                    if (listener instanceof Button w && w.m_6035_() != null && w.m_6035_().getString().equals(b) && w.f_93624_ && w.f_93623_) {
                        w.m_5716_(0.0, 0.0);
                        return;
                    }
                }
            }
            Minecraft mcx = Minecraft.getInstance();
            if (mcx.level != null && mcx.screen != null) {
                mcx.screen.onClose();
            }
        }
    }
}