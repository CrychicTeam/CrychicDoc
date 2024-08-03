package com.mna.items.runes;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.items.TieredItem;
import com.mna.entities.utility.ThrownRunescribingPattern;
import com.mna.items.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemRunePattern extends TieredItem {

    private static final String NBT_KEY_HMUTEX = "hmutex";

    private static final String NBT_KEY_VMUTEX = "vmutex";

    private static final String NBT_KEY_USES = "uses";

    private static final int max_uses = 4;

    public ItemRunePattern() {
        super(new Item.Properties());
    }

    public ItemRunePattern(Item.Properties properties) {
        super(properties);
    }

    public long getHMutex(ItemStack stack) {
        if (!stack.hasTag()) {
            return 0L;
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            return tag.getLong("hmutex");
        }
    }

    public void setHMutex(ItemStack stack, long hMutex) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong("hmutex", hMutex);
    }

    public long getVMutex(ItemStack stack) {
        if (!stack.hasTag()) {
            return 0L;
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            return tag.getLong("vmutex");
        }
    }

    public void setVMutex(ItemStack stack, long vMutex) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong("vmutex", vMutex);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("uses") ? 13 - Math.round(13.0F * ((float) stack.getTag().getInt("uses") / 4.0F)) : 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("uses")) {
            float f = Math.max(0.0F, (4.0F - (float) stack.getTag().getInt("uses")) / 4.0F);
            return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        } else {
            return Mth.hsvToRgb(0.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.getBarWidth(stack) > 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        if (itemstack.getItem() == ItemInit.RUNE_PATTERN.get() && (this.getHMutex(itemstack) != 0L || this.getVMutex(itemstack) != 0L)) {
            worldIn.playSound((Player) null, playerIn.m_20185_(), playerIn.m_20186_(), playerIn.m_20189_(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
            if (!worldIn.isClientSide) {
                ThrownRunescribingPattern dangit = new ThrownRunescribingPattern(worldIn, playerIn);
                dangit.m_37446_(itemstack);
                dangit.m_37251_(playerIn, playerIn.m_146909_(), playerIn.m_146908_(), 0.0F, 1.5F, 1.0F);
                worldIn.m_7967_(dangit);
                CustomAdvancementTriggers.THROWN_RUNESCRIBE_PATTERN.trigger((ServerPlayer) playerIn);
            }
            playerIn.awardStat(Stats.ITEM_USED.get(this));
            if (!playerIn.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public static boolean incrementDamage(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        int uses = nbt.getInt("uses") + 1;
        nbt.putInt("uses", uses);
        return uses == 4;
    }
}