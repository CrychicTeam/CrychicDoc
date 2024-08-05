package net.minecraft.client.gui.components.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TutorialToast implements Toast {

    public static final int PROGRESS_BAR_WIDTH = 154;

    public static final int PROGRESS_BAR_HEIGHT = 1;

    public static final int PROGRESS_BAR_X = 3;

    public static final int PROGRESS_BAR_Y = 28;

    private final TutorialToast.Icons icon;

    private final Component title;

    @Nullable
    private final Component message;

    private Toast.Visibility visibility = Toast.Visibility.SHOW;

    private long lastProgressTime;

    private float lastProgress;

    private float progress;

    private final boolean progressable;

    public TutorialToast(TutorialToast.Icons tutorialToastIcons0, Component component1, @Nullable Component component2, boolean boolean3) {
        this.icon = tutorialToastIcons0;
        this.title = component1;
        this.message = component2;
        this.progressable = boolean3;
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics0, ToastComponent toastComponent1, long long2) {
        guiGraphics0.blit(f_94893_, 0, 0, 0, 96, this.m_7828_(), this.m_94899_());
        this.icon.render(guiGraphics0, 6, 6);
        if (this.message == null) {
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, this.title, 30, 12, -11534256, false);
        } else {
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, this.title, 30, 7, -11534256, false);
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, this.message, 30, 18, -16777216, false);
        }
        if (this.progressable) {
            guiGraphics0.fill(3, 28, 157, 29, -1);
            float $$3 = Mth.clampedLerp(this.lastProgress, this.progress, (float) (long2 - this.lastProgressTime) / 100.0F);
            int $$4;
            if (this.progress >= this.lastProgress) {
                $$4 = -16755456;
            } else {
                $$4 = -11206656;
            }
            guiGraphics0.fill(3, 28, (int) (3.0F + 154.0F * $$3), 29, $$4);
            this.lastProgress = $$3;
            this.lastProgressTime = long2;
        }
        return this.visibility;
    }

    public void hide() {
        this.visibility = Toast.Visibility.HIDE;
    }

    public void updateProgress(float float0) {
        this.progress = float0;
    }

    public static enum Icons {

        MOVEMENT_KEYS(0, 0),
        MOUSE(1, 0),
        TREE(2, 0),
        RECIPE_BOOK(0, 1),
        WOODEN_PLANKS(1, 1),
        SOCIAL_INTERACTIONS(2, 1),
        RIGHT_CLICK(3, 1);

        private final int x;

        private final int y;

        private Icons(int p_94982_, int p_94983_) {
            this.x = p_94982_;
            this.y = p_94983_;
        }

        public void render(GuiGraphics p_282818_, int p_283064_, int p_282765_) {
            RenderSystem.enableBlend();
            p_282818_.blit(Toast.TEXTURE, p_283064_, p_282765_, 176 + this.x * 20, this.y * 20, 20, 20);
        }
    }
}