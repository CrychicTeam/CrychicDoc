package org.violetmoon.zeta.client.event.play;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.gui.GuiGraphics;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderGuiOverlay extends IZetaPlayEvent {

    Window getWindow();

    GuiGraphics getGuiGraphics();

    float getPartialTick();

    boolean shouldDrawSurvivalElements();

    int getLeftHeight();

    public interface ArmorLevel extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.ArmorLevel {
        }

        public interface Pre extends ZRenderGuiOverlay.ArmorLevel {
        }
    }

    public interface ChatPanel extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.ChatPanel {
        }

        public interface Pre extends ZRenderGuiOverlay.ChatPanel {
        }
    }

    public interface Crosshair extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.Crosshair {
        }

        public interface Pre extends ZRenderGuiOverlay.Crosshair {
        }
    }

    public interface DebugText extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.DebugText {
        }

        public interface Pre extends ZRenderGuiOverlay.DebugText {
        }
    }

    public interface Hotbar extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.Hotbar {
        }

        public interface Pre extends ZRenderGuiOverlay.Hotbar {
        }
    }

    public interface PlayerHealth extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.PlayerHealth {
        }

        public interface Pre extends ZRenderGuiOverlay.PlayerHealth {
        }
    }

    public interface PotionIcons extends ZRenderGuiOverlay {

        public interface Post extends ZRenderGuiOverlay.PotionIcons {
        }

        public interface Pre extends ZRenderGuiOverlay.PotionIcons {
        }
    }
}