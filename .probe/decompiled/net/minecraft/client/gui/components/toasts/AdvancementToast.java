package net.minecraft.client.gui.components.toasts;

import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class AdvancementToast implements Toast {

    public static final int DISPLAY_TIME = 5000;

    private final Advancement advancement;

    private boolean playedSound;

    public AdvancementToast(Advancement advancement0) {
        this.advancement = advancement0;
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics0, ToastComponent toastComponent1, long long2) {
        DisplayInfo $$3 = this.advancement.getDisplay();
        guiGraphics0.blit(f_94893_, 0, 0, 0, 0, this.m_7828_(), this.m_94899_());
        if ($$3 != null) {
            List<FormattedCharSequence> $$4 = toastComponent1.getMinecraft().font.split($$3.getTitle(), 125);
            int $$5 = $$3.getFrame() == FrameType.CHALLENGE ? 16746751 : 16776960;
            if ($$4.size() == 1) {
                guiGraphics0.drawString(toastComponent1.getMinecraft().font, $$3.getFrame().getDisplayName(), 30, 7, $$5 | 0xFF000000, false);
                guiGraphics0.drawString(toastComponent1.getMinecraft().font, (FormattedCharSequence) $$4.get(0), 30, 18, -1, false);
            } else {
                int $$6 = 1500;
                float $$7 = 300.0F;
                if (long2 < 1500L) {
                    int $$8 = Mth.floor(Mth.clamp((float) (1500L - long2) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    guiGraphics0.drawString(toastComponent1.getMinecraft().font, $$3.getFrame().getDisplayName(), 30, 11, $$5 | $$8, false);
                } else {
                    int $$9 = Mth.floor(Mth.clamp((float) (long2 - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int $$10 = this.m_94899_() / 2 - $$4.size() * 9 / 2;
                    for (FormattedCharSequence $$11 : $$4) {
                        guiGraphics0.drawString(toastComponent1.getMinecraft().font, $$11, 30, $$10, 16777215 | $$9, false);
                        $$10 += 9;
                    }
                }
            }
            if (!this.playedSound && long2 > 0L) {
                this.playedSound = true;
                if ($$3.getFrame() == FrameType.CHALLENGE) {
                    toastComponent1.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
                }
            }
            guiGraphics0.renderFakeItem($$3.getIcon(), 8, 8);
            return (double) long2 >= 5000.0 * toastComponent1.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        } else {
            return Toast.Visibility.HIDE;
        }
    }
}