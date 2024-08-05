package net.minecraft.client.gui.components.toasts;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class SystemToast implements Toast {

    private static final int MAX_LINE_SIZE = 200;

    private static final int LINE_SPACING = 12;

    private static final int MARGIN = 10;

    private final SystemToast.SystemToastIds id;

    private Component title;

    private List<FormattedCharSequence> messageLines;

    private long lastChanged;

    private boolean changed;

    private final int width;

    public SystemToast(SystemToast.SystemToastIds systemToastSystemToastIds0, Component component1, @Nullable Component component2) {
        this(systemToastSystemToastIds0, component1, nullToEmpty(component2), Math.max(160, 30 + Math.max(Minecraft.getInstance().font.width(component1), component2 == null ? 0 : Minecraft.getInstance().font.width(component2))));
    }

    public static SystemToast multiline(Minecraft minecraft0, SystemToast.SystemToastIds systemToastSystemToastIds1, Component component2, Component component3) {
        Font $$4 = minecraft0.font;
        List<FormattedCharSequence> $$5 = $$4.split(component3, 200);
        int $$6 = Math.max(200, $$5.stream().mapToInt($$4::m_92724_).max().orElse(200));
        return new SystemToast(systemToastSystemToastIds1, component2, $$5, $$6 + 30);
    }

    private SystemToast(SystemToast.SystemToastIds systemToastSystemToastIds0, Component component1, List<FormattedCharSequence> listFormattedCharSequence2, int int3) {
        this.id = systemToastSystemToastIds0;
        this.title = component1;
        this.messageLines = listFormattedCharSequence2;
        this.width = int3;
    }

    private static ImmutableList<FormattedCharSequence> nullToEmpty(@Nullable Component component0) {
        return component0 == null ? ImmutableList.of() : ImmutableList.of(component0.getVisualOrderText());
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return 20 + Math.max(this.messageLines.size(), 1) * 12;
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics0, ToastComponent toastComponent1, long long2) {
        if (this.changed) {
            this.lastChanged = long2;
            this.changed = false;
        }
        int $$3 = this.width();
        if ($$3 == 160 && this.messageLines.size() <= 1) {
            guiGraphics0.blit(f_94893_, 0, 0, 0, 64, $$3, this.height());
        } else {
            int $$4 = this.height();
            int $$5 = 28;
            int $$6 = Math.min(4, $$4 - 28);
            this.renderBackgroundRow(guiGraphics0, toastComponent1, $$3, 0, 0, 28);
            for (int $$7 = 28; $$7 < $$4 - $$6; $$7 += 10) {
                this.renderBackgroundRow(guiGraphics0, toastComponent1, $$3, 16, $$7, Math.min(16, $$4 - $$7 - $$6));
            }
            this.renderBackgroundRow(guiGraphics0, toastComponent1, $$3, 32 - $$6, $$4 - $$6, $$6);
        }
        if (this.messageLines == null) {
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, this.title, 18, 12, -256, false);
        } else {
            guiGraphics0.drawString(toastComponent1.getMinecraft().font, this.title, 18, 7, -256, false);
            for (int $$8 = 0; $$8 < this.messageLines.size(); $$8++) {
                guiGraphics0.drawString(toastComponent1.getMinecraft().font, (FormattedCharSequence) this.messageLines.get($$8), 18, 18 + $$8 * 12, -1, false);
            }
        }
        return (double) (long2 - this.lastChanged) < (double) this.id.displayTime * toastComponent1.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }

    private void renderBackgroundRow(GuiGraphics guiGraphics0, ToastComponent toastComponent1, int int2, int int3, int int4, int int5) {
        int $$6 = int3 == 0 ? 20 : 5;
        int $$7 = Math.min(60, int2 - $$6);
        guiGraphics0.blit(f_94893_, 0, int4, 0, 64 + int3, $$6, int5);
        for (int $$8 = $$6; $$8 < int2 - $$7; $$8 += 64) {
            guiGraphics0.blit(f_94893_, $$8, int4, 32, 64 + int3, Math.min(64, int2 - $$8 - $$7), int5);
        }
        guiGraphics0.blit(f_94893_, int2 - $$7, int4, 160 - $$7, 64 + int3, $$7, int5);
    }

    public void reset(Component component0, @Nullable Component component1) {
        this.title = component0;
        this.messageLines = nullToEmpty(component1);
        this.changed = true;
    }

    public SystemToast.SystemToastIds getToken() {
        return this.id;
    }

    public static void add(ToastComponent toastComponent0, SystemToast.SystemToastIds systemToastSystemToastIds1, Component component2, @Nullable Component component3) {
        toastComponent0.addToast(new SystemToast(systemToastSystemToastIds1, component2, component3));
    }

    public static void addOrUpdate(ToastComponent toastComponent0, SystemToast.SystemToastIds systemToastSystemToastIds1, Component component2, @Nullable Component component3) {
        SystemToast $$4 = toastComponent0.getToast(SystemToast.class, systemToastSystemToastIds1);
        if ($$4 == null) {
            add(toastComponent0, systemToastSystemToastIds1, component2, component3);
        } else {
            $$4.reset(component2, component3);
        }
    }

    public static void onWorldAccessFailure(Minecraft minecraft0, String string1) {
        add(minecraft0.getToasts(), SystemToast.SystemToastIds.WORLD_ACCESS_FAILURE, Component.translatable("selectWorld.access_failure"), Component.literal(string1));
    }

    public static void onWorldDeleteFailure(Minecraft minecraft0, String string1) {
        add(minecraft0.getToasts(), SystemToast.SystemToastIds.WORLD_ACCESS_FAILURE, Component.translatable("selectWorld.delete_failure"), Component.literal(string1));
    }

    public static void onPackCopyFailure(Minecraft minecraft0, String string1) {
        add(minecraft0.getToasts(), SystemToast.SystemToastIds.PACK_COPY_FAILURE, Component.translatable("pack.copyFailure"), Component.literal(string1));
    }

    public static enum SystemToastIds {

        TUTORIAL_HINT,
        NARRATOR_TOGGLE,
        WORLD_BACKUP,
        PACK_LOAD_FAILURE,
        WORLD_ACCESS_FAILURE,
        PACK_COPY_FAILURE,
        PERIODIC_NOTIFICATION,
        UNSECURE_SERVER_WARNING(10000L);

        final long displayTime;

        private SystemToastIds(long p_232551_) {
            this.displayTime = p_232551_;
        }

        private SystemToastIds() {
            this(5000L);
        }
    }
}