package dev.ftb.mods.ftblibrary.ui.misc;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class SimpleToast implements Toast {

    private boolean hasPlayedSound = false;

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent gui, long delta) {
        GuiHelper.setupDrawing();
        Minecraft mc = gui.getMinecraft();
        graphics.blit(f_94893_, 0, 0, 0, 0, 160, 32);
        List<FormattedCharSequence> list = mc.font.split(this.getSubtitle(), 125);
        int i = this.isImportant() ? 16746751 : 16776960;
        if (list.size() == 1) {
            graphics.drawString(mc.font, this.getTitle(), 30, 7, i | 0xFF000000, true);
            graphics.drawString(mc.font, (FormattedCharSequence) list.get(0), 30, 18, -1);
        } else if (delta < 1500L) {
            int k = Mth.floor(Mth.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
            graphics.drawString(mc.font, this.getTitle(), 30, 11, i | k, true);
        } else {
            int i1 = Mth.floor(Mth.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
            int l = 16 - list.size() * 9 / 2;
            for (FormattedCharSequence s : list) {
                graphics.drawString(mc.font, s, 30, l, 16777215 | i1, true);
                l += 9;
            }
        }
        if (!this.hasPlayedSound && delta > 0L) {
            this.hasPlayedSound = true;
            this.playSound(mc.getSoundManager());
        }
        this.getIcon().draw(graphics, 8, 8, 16, 16);
        return (double) delta >= 5000.0 * gui.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    public Component getTitle() {
        return Component.literal("<error>");
    }

    public Component getSubtitle() {
        return Component.empty();
    }

    public boolean isImportant() {
        return false;
    }

    public Icon getIcon() {
        return Icons.INFO;
    }

    public void playSound(SoundManager handler) {
    }

    public static void info(Component title, Component subtitle) {
        Minecraft.getInstance().getToasts().addToast(new SimpleToast() {

            @Override
            public Component getTitle() {
                return title;
            }

            @Override
            public Component getSubtitle() {
                return subtitle;
            }
        });
    }

    public static void error(Component title, Component subtitle) {
        Minecraft.getInstance().getToasts().addToast(new SimpleToast() {

            @Override
            public Component getTitle() {
                return title;
            }

            @Override
            public Component getSubtitle() {
                return subtitle;
            }

            @Override
            public Icon getIcon() {
                return Icons.BARRIER;
            }
        });
    }
}