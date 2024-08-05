package net.minecraft.client.gui.components.toasts;

import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class ToastComponent {

    private static final int SLOT_COUNT = 5;

    private static final int NO_SPACE = -1;

    final Minecraft minecraft;

    private final List<ToastComponent.ToastInstance<?>> visible = new ArrayList();

    private final BitSet occupiedSlots = new BitSet(5);

    private final Deque<Toast> queued = Queues.newArrayDeque();

    public ToastComponent(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void render(GuiGraphics guiGraphics0) {
        if (!this.minecraft.options.hideGui) {
            int $$1 = guiGraphics0.guiWidth();
            this.visible.removeIf(p_280780_ -> {
                if (p_280780_ != null && p_280780_.render($$1, guiGraphics0)) {
                    this.occupiedSlots.clear(p_280780_.index, p_280780_.index + p_280780_.slotCount);
                    return true;
                } else {
                    return false;
                }
            });
            if (!this.queued.isEmpty() && this.freeSlots() > 0) {
                this.queued.removeIf(p_243239_ -> {
                    int $$1x = p_243239_.slotCount();
                    int $$2 = this.findFreeIndex($$1x);
                    if ($$2 != -1) {
                        this.visible.add(new ToastComponent.ToastInstance<>(p_243239_, $$2, $$1x));
                        this.occupiedSlots.set($$2, $$2 + $$1x);
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }
    }

    private int findFreeIndex(int int0) {
        if (this.freeSlots() >= int0) {
            int $$1 = 0;
            for (int $$2 = 0; $$2 < 5; $$2++) {
                if (this.occupiedSlots.get($$2)) {
                    $$1 = 0;
                } else if (++$$1 == int0) {
                    return $$2 + 1 - $$1;
                }
            }
        }
        return -1;
    }

    private int freeSlots() {
        return 5 - this.occupiedSlots.cardinality();
    }

    @Nullable
    public <T extends Toast> T getToast(Class<? extends T> classExtendsT0, Object object1) {
        for (ToastComponent.ToastInstance<?> $$2 : this.visible) {
            if ($$2 != null && classExtendsT0.isAssignableFrom($$2.getToast().getClass()) && $$2.getToast().getToken().equals(object1)) {
                return (T) $$2.getToast();
            }
        }
        for (Toast $$3 : this.queued) {
            if (classExtendsT0.isAssignableFrom($$3.getClass()) && $$3.getToken().equals(object1)) {
                return (T) $$3;
            }
        }
        return null;
    }

    public void clear() {
        this.occupiedSlots.clear();
        this.visible.clear();
        this.queued.clear();
    }

    public void addToast(Toast toast0) {
        this.queued.add(toast0);
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public double getNotificationDisplayTimeMultiplier() {
        return this.minecraft.options.notificationDisplayTime().get();
    }

    class ToastInstance<T extends Toast> {

        private static final long ANIMATION_TIME = 600L;

        private final T toast;

        final int index;

        final int slotCount;

        private long animationTime = -1L;

        private long visibleTime = -1L;

        private Toast.Visibility visibility = Toast.Visibility.SHOW;

        ToastInstance(T t0, int int1, int int2) {
            this.toast = t0;
            this.index = int1;
            this.slotCount = int2;
        }

        public T getToast() {
            return this.toast;
        }

        private float getVisibility(long long0) {
            float $$1 = Mth.clamp((float) (long0 - this.animationTime) / 600.0F, 0.0F, 1.0F);
            $$1 *= $$1;
            return this.visibility == Toast.Visibility.HIDE ? 1.0F - $$1 : $$1;
        }

        public boolean render(int int0, GuiGraphics guiGraphics1) {
            long $$2 = Util.getMillis();
            if (this.animationTime == -1L) {
                this.animationTime = $$2;
                this.visibility.playSound(ToastComponent.this.minecraft.getSoundManager());
            }
            if (this.visibility == Toast.Visibility.SHOW && $$2 - this.animationTime <= 600L) {
                this.visibleTime = $$2;
            }
            guiGraphics1.pose().pushPose();
            guiGraphics1.pose().translate((float) int0 - (float) this.toast.width() * this.getVisibility($$2), (float) (this.index * 32), 800.0F);
            Toast.Visibility $$3 = this.toast.render(guiGraphics1, ToastComponent.this, $$2 - this.visibleTime);
            guiGraphics1.pose().popPose();
            if ($$3 != this.visibility) {
                this.animationTime = $$2 - (long) ((int) ((1.0F - this.getVisibility($$2)) * 600.0F));
                this.visibility = $$3;
                this.visibility.playSound(ToastComponent.this.minecraft.getSoundManager());
            }
            return this.visibility == Toast.Visibility.HIDE && $$2 - this.animationTime > 600L;
        }
    }
}