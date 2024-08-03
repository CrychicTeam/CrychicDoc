package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.OnInject;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosCapability;

@SerialClass
public class ArtifactSwapData {

    @SerialField
    public final ArtifactSwapData.SwapSlot[] contents = new ArtifactSwapData.SwapSlot[45];

    @SerialField
    public int select = 0;

    public ArtifactSwapData() {
        int slot_ind = 0;
        for (ArtifactSlot slot : ArtifactTypeRegistry.SLOT.get()) {
            for (int set_ind = 0; set_ind < 9; set_ind++) {
                this.contents[set_ind + slot_ind * 9] = new ArtifactSwapData.SwapSlot(slot);
            }
            slot_ind++;
        }
    }

    @OnInject
    public void onInject() {
        int slot_ind = 0;
        for (ArtifactSlot slot : ArtifactTypeRegistry.SLOT.get()) {
            for (int set_ind = 0; set_ind < 9; set_ind++) {
                this.contents[set_ind + slot_ind * 9].slot = slot;
            }
            slot_ind++;
        }
    }

    public void swap(Player player) {
        player.getCapability(CuriosCapability.INVENTORY).resolve().ifPresent(cap -> {
            for (int slot_ind = 0; slot_ind < 5; slot_ind++) {
                ArtifactSwapData.SwapSlot slot = this.contents[slot_ind * 9 + this.select];
                if (!slot.disabled) {
                    cap.getStacksHandler(slot.slot.getCurioIdentifier()).ifPresent(h -> {
                        ItemStack old = h.getStacks().getStackInSlot(0);
                        ItemStack store = slot.getStack();
                        if (old.isEmpty()) {
                            if (!store.isEmpty()) {
                                h.getStacks().setStackInSlot(0, store);
                                slot.setStack(ItemStack.EMPTY);
                            }
                        } else if (old.getItem() instanceof BaseArtifact) {
                            slot.setStack(old.copy());
                            h.getStacks().setStackInSlot(0, store);
                        }
                    });
                }
            }
        });
    }

    @SerialClass
    public static class SwapSlot {

        public ArtifactSlot slot;

        @SerialField
        private ItemStack stack = ItemStack.EMPTY;

        @SerialField
        private boolean disabled = false;

        @Deprecated
        public SwapSlot() {
        }

        public SwapSlot(ArtifactSlot slot) {
            this.slot = slot;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public void setStack(ItemStack item) {
            this.stack = item;
            if (this.disabled && !item.isEmpty()) {
                this.disabled = false;
            }
        }

        public void toggle() {
            if (this.stack.isEmpty()) {
                this.disabled = !this.disabled;
            }
        }

        public boolean canAccept(ItemStack st) {
            return !this.disabled && st.getItem() instanceof BaseArtifact a && a.slot.get() == this.slot;
        }

        public boolean isLocked() {
            return this.disabled;
        }
    }
}