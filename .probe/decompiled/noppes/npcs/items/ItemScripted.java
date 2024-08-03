package noppes.npcs.items;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;

public class ItemScripted extends Item {

    public ItemScripted(Item.Properties props) {
        super(props);
    }

    public static ItemScriptedWrapper GetWrapper(ItemStack stack) {
        return (ItemScriptedWrapper) NpcAPI.Instance().getIItemStack(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ItemScriptedWrapper ? ((ItemScriptedWrapper) istack).durabilityShow : super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ItemScriptedWrapper ? Math.round(13.0F - ((ItemScriptedWrapper) istack).durabilityValue * 13.0F) : super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        if (!(istack instanceof ItemScriptedWrapper)) {
            return super.getBarColor(stack);
        } else {
            int color = ((ItemScriptedWrapper) istack).durabilityColor;
            return color >= 0 ? color : Mth.hsvToRgb(Math.max(0.0F, 1.0F - (float) this.getBarWidth(stack)) / 3.0F, 1.0F, 1.0F);
        }
    }

    public int getMaxStackSize(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ItemScriptedWrapper ? ((ItemScriptedWrapper) istack).getMaxStackSize() : super.getMaxStackSize(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }

    public CompoundTag getShareTag(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ItemScriptedWrapper ? ((ItemScriptedWrapper) istack).getMCNbt() : null;
    }

    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null) {
            IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
            if (istack instanceof ItemScriptedWrapper) {
                ((ItemScriptedWrapper) istack).setMCNbt(nbt);
            }
        }
    }
}