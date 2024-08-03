package com.github.alexthe666.alexsmobs.item;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ItemGhostlyPickaxe extends PickaxeItem {

    public ItemGhostlyPickaxe(Item.Properties props) {
        super(Tiers.IRON, 1, -2.8F, props);
    }

    public static boolean shouldStoreInGhost(LivingEntity player, ItemStack stack) {
        return player instanceof Player && ((Player) player).getInventory().getFreeSlot() == -1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState blockState) {
        return blockState.m_204336_(BlockTags.MINEABLE_WITH_PICKAXE) ? 20.0F : 1.0F;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (shouldStoreInGhost(user, stack)) {
            if (user instanceof Player player) {
                player.awardStat(Stats.BLOCK_MINED.get(state.m_60734_()));
                player.causeFoodExhaustion(0.005F);
            }
            if (!level.isClientSide) {
                BlockEntity blockentity = state.m_155947_() ? level.getBlockEntity(pos) : null;
                Block.getDrops(state, (ServerLevel) level, pos, blockentity, user, stack).forEach(item -> putItemInGhostInventoryOrDrop(user, stack, item));
                state.m_222967_((ServerLevel) level, pos, stack, true);
                int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
                int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
                int exp = state.getExpDrop((ServerLevel) level, level.random, pos, fortuneLevel, silkTouchLevel);
                if (exp > 0) {
                    state.m_60734_().popExperience((ServerLevel) level, pos, exp);
                }
            }
        }
        return super.m_6813_(stack, level, state, pos, user);
    }

    private static void putItemInGhostInventoryOrDrop(LivingEntity user, ItemStack pickaxe, ItemStack item) {
        CompoundTag compoundtag = pickaxe.getOrCreateTag();
        SimpleContainer container = new SimpleContainer(9);
        if (compoundtag.contains("Items")) {
            container.fromTag(compoundtag.getList("Items", 10));
        }
        if (user instanceof Player player) {
            if (player.getInventory().add(item)) {
                return;
            }
            if (container.canAddItem(item)) {
                ItemStack leftover = container.addItem(item);
                compoundtag.put("Items", container.createTag());
                pickaxe.setTag(compoundtag);
                item = leftover;
            }
        }
        if (!item.isEmpty()) {
            user.m_19983_(item);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean offhand) {
        super.m_6883_(stack, level, entity, i, offhand);
        if (entity instanceof Player player && player.f_19797_ % 3 == 0) {
            CompoundTag compoundtag = stack.getOrCreateTag();
            SimpleContainer container = new SimpleContainer(9);
            boolean flag = false;
            if (compoundtag.contains("Items")) {
                container.fromTag(compoundtag.getList("Items", 10));
            }
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                ItemStack stackAt = container.getItem(slot);
                if (!stackAt.isEmpty() && player.addItem(stackAt)) {
                    container.removeItem(slot, stack.getCount());
                    flag = true;
                    break;
                }
            }
            if (flag) {
                compoundtag.put("Items", container.createTag());
                stack.setTag(compoundtag);
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack pickaxe, ItemStack stack) {
        return stack.is(Items.PHANTOM_MEMBRANE);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_7373_(stack, worldIn, tooltip, flagIn);
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null && compoundtag.contains("Items", 9)) {
            SimpleContainer container = new SimpleContainer(9);
            container.fromTag(compoundtag.getList("Items", 10));
            int i = 0;
            int j = 0;
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                ItemStack itemstack = container.getItem(slot);
                if (!itemstack.isEmpty()) {
                    j++;
                    if (i <= 4) {
                        i++;
                        MutableComponent mutablecomponent = itemstack.getHoverName().copy();
                        mutablecomponent.append(" x").append(String.valueOf(itemstack.getCount()));
                        tooltip.add(mutablecomponent.withStyle(ChatFormatting.DARK_AQUA));
                    }
                }
            }
            if (j - i > 0) {
                tooltip.add(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC));
            }
        }
    }

    private void dropAllContents(Level level, Vec3 vec3, ItemStack pickaxe) {
        CompoundTag compoundtag = pickaxe.getTag();
        if (compoundtag != null && compoundtag.contains("Items", 9)) {
            SimpleContainer container = new SimpleContainer(9);
            container.fromTag(compoundtag.getList("Items", 10));
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                ItemStack itemstack = container.getItem(slot);
                if (!itemstack.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(level, vec3.x, vec3.y, vec3.z, itemstack.copy());
                    if (level.m_7967_(itemEntity)) {
                        container.removeItem(slot, itemstack.getCount());
                    }
                }
            }
            compoundtag.put("Items", container.createTag());
            pickaxe.setTag(compoundtag);
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        this.dropAllContents(itemEntity.m_9236_(), itemEntity.m_20182_(), itemEntity.getItem());
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        int i = super.damageItem(stack, amount, entity, onBroken);
        if (i + stack.getDamageValue() >= stack.getMaxDamage() && entity != null) {
            this.dropAllContents(entity.m_9236_(), entity.m_20182_(), stack);
        }
        return i;
    }

    public int getMaxDamage(ItemStack stack) {
        return 700;
    }
}