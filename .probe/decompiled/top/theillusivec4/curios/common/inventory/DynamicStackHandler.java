package top.theillusivec4.curios.common.inventory;

import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class DynamicStackHandler extends ItemStackHandler implements IDynamicStackHandler {

    protected NonNullList<ItemStack> previousStacks;

    protected Function<Integer, SlotContext> ctxBuilder;

    public DynamicStackHandler(int size, Function<Integer, SlotContext> ctxBuilder) {
        super(size);
        this.previousStacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.ctxBuilder = ctxBuilder;
    }

    @Override
    public void setPreviousStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.validateSlotIndex(slot);
        this.previousStacks.set(slot, stack);
        this.onContentsChanged(slot);
    }

    @Nonnull
    @Override
    public ItemStack getPreviousStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.previousStacks.get(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        SlotContext ctx = (SlotContext) this.ctxBuilder.apply(slot);
        CurioEquipEvent equipEvent = new CurioEquipEvent(stack, ctx);
        MinecraftForge.EVENT_BUS.post(equipEvent);
        Result result = equipEvent.getResult();
        return result == Result.DENY ? false : result == Result.ALLOW || CuriosApi.isStackValid(ctx, stack) && (Boolean) CuriosApi.getCurio(stack).map(curio -> curio.canEquip(ctx)).orElse(true) && super.isItemValid(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack existing = this.stacks.get(slot);
        SlotContext ctx = (SlotContext) this.ctxBuilder.apply(slot);
        CurioUnequipEvent unequipEvent = new CurioUnequipEvent(existing, ctx);
        MinecraftForge.EVENT_BUS.post(unequipEvent);
        Result result = unequipEvent.getResult();
        if (result == Result.DENY) {
            return ItemStack.EMPTY;
        } else {
            boolean var10000;
            label33: {
                if (ctx.entity() instanceof Player player && player.isCreative()) {
                    var10000 = true;
                    break label33;
                }
                var10000 = false;
            }
            boolean isCreative = var10000;
            return result != Result.ALLOW && (!existing.isEmpty() && !isCreative && EnchantmentHelper.hasBindingCurse(existing) || !CuriosApi.getCurio(existing).map(curio -> curio.canUnequip(ctx)).orElse(true)) ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
        }
    }

    @Override
    public void grow(int amount) {
        this.stacks = getResizedList(this.stacks.size() + amount, this.stacks);
        this.previousStacks = getResizedList(this.previousStacks.size() + amount, this.previousStacks);
    }

    @Override
    public void shrink(int amount) {
        this.stacks = getResizedList(this.stacks.size() - amount, this.stacks);
        this.previousStacks = getResizedList(this.previousStacks.size() - amount, this.previousStacks);
    }

    private static NonNullList<ItemStack> getResizedList(int size, NonNullList<ItemStack> stacks) {
        NonNullList<ItemStack> newList = NonNullList.withSize(Math.max(0, size), ItemStack.EMPTY);
        for (int i = 0; i < newList.size() && i < stacks.size(); i++) {
            newList.set(i, stacks.get(i));
        }
        return newList;
    }
}