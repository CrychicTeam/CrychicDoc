package dev.xkmc.l2backpack.content.capability;

import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

public abstract class InvPickupCap<T extends IItemHandlerModifiable> implements PickupModeCap {

    @Nullable
    public abstract T getInv(PickupTrace var1);

    public boolean isValid(ItemStack stack) {
        return true;
    }

    public boolean mayStack(T inv, int slot, ItemStack stack, PickupConfig config) {
        if (!this.isValid(stack)) {
            return false;
        } else {
            ItemStack old = inv.getStackInSlot(slot);
            if (old.getCapability(PickupModeCap.TOKEN).resolve().isPresent()) {
                return false;
            } else if (config.pickup() == PickupMode.ALL && old.isEmpty()) {
                return true;
            } else {
                return config.destroy() == DestroyMode.MATCH ? stack.getItem() == old.getItem() : ItemStack.isSameItemSameTags(old, stack);
            }
        }
    }

    @Override
    public int doPickup(ItemStack stack, PickupTrace trace) {
        if (!this.isValid(stack)) {
            return 0;
        } else if (stack.isEmpty() || this.getPickupMode().pickup() == PickupMode.NONE) {
            return 0;
        } else if (!trace.push(this.getSignature(), this.getPickupMode())) {
            return 0;
        } else {
            int ans = this.doPickupInternal(stack, trace);
            trace.pop();
            return ans;
        }
    }

    private int doPickupInternal(ItemStack stack, PickupTrace trace) {
        T inv = this.getInv(trace);
        if (inv == null) {
            return 0;
        } else {
            int ans = 0;
            PickupMode pickup = trace.getMode().pickup();
            DestroyMode destroy = trace.getMode().destroy();
            if (pickup == PickupMode.NONE) {
                return ans;
            } else {
                boolean doClear = false;
                if (destroy == DestroyMode.ALL) {
                    doClear = true;
                } else {
                    for (int i = 0; i < inv.getSlots() && !stack.isEmpty(); i++) {
                        if (this.mayStack(inv, i, stack, trace.getMode())) {
                            boolean empty = inv.getStackInSlot(i).isEmpty();
                            if (!doClear && empty || destroy.attemptInsert) {
                                int count = stack.getCount();
                                ItemStack result = inv.insertItem(i, stack.copy(), trace.simulate);
                                int remain = result.getCount();
                                stack.setCount(remain);
                                ans += count - remain;
                            }
                            if (!empty && destroy != DestroyMode.NONE) {
                                doClear = true;
                            }
                        } else {
                            ItemStack slot = inv.getStackInSlot(i);
                            if (!slot.isEmpty()) {
                                Optional<PickupModeCap> opt = slot.getCapability(PickupModeCap.TOKEN).resolve();
                                if (!opt.isEmpty()) {
                                    ans += ((PickupModeCap) opt.get()).doPickup(stack, trace);
                                }
                            }
                        }
                    }
                }
                if (pickup == PickupMode.ALL && destroy == DestroyMode.EXCESS) {
                    doClear = true;
                }
                if (doClear) {
                    ans += stack.getCount();
                    stack.setCount(0);
                }
                return ans;
            }
        }
    }
}