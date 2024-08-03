package net.mehvahdjukaar.supplementaries.common.items;

import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.InventoryTooltip;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkClientCompat;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class SackItem extends BlockItem {

    public SackItem(Block blockIn, Item.Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.m_6883_(stack, worldIn, entityIn, itemSlot, isSelected);
        if ((Boolean) CommonConfigs.Functional.SACK_PENALTY.get()) {
            if (worldIn.getGameTime() % 27L == 0L && entityIn instanceof ServerPlayer player && !player.isCreative() && !entityIn.isSpectator() && stack.getTagElement("BlockEntityTag") != null) {
                float amount = ItemsUtil.getEncumbermentFromInventory(stack, player);
                int inc = (Integer) CommonConfigs.Functional.SACK_INCREMENT.get();
                if (amount > (float) inc) {
                    player.m_7292_(new MobEffectInstance((MobEffect) ModRegistry.OVERENCUMBERED.get(), 200, ((int) amount - 1) / inc - 1, false, false, true));
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!CompatHandler.QUARK || !QuarkClientCompat.canRenderQuarkTooltip()) {
            CompoundTag tag = stack.getTagElement("BlockEntityTag");
            if (tag != null) {
                if (tag.contains("LootTable", 8)) {
                    tooltip.add(Component.literal("???????").withStyle(ChatFormatting.GRAY));
                }
                if (tag.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(9, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(tag, nonnulllist);
                    int i = 0;
                    int j = 0;
                    for (ItemStack itemstack : nonnulllist) {
                        if (!itemstack.isEmpty()) {
                            j++;
                            if (i <= 4) {
                                i++;
                                MutableComponent component = itemstack.getHoverName().copy();
                                component.append(" x").append(String.valueOf(itemstack.getCount()));
                                tooltip.add(component.withStyle(ChatFormatting.GRAY));
                            }
                        }
                    }
                    if (j - i > 0) {
                        tooltip.add(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                    }
                }
            }
        }
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public void onDestroyed(ItemEntity pItemEntity) {
        CompoundTag compoundtag = pItemEntity.getItem().getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getCompound("BlockEntityTag").getList("Items", 10);
            ItemUtils.onContainerDestroyed(pItemEntity, listtag.stream().map(CompoundTag.class::cast).map(ItemStack::m_41712_));
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack incoming, Slot slot, ClickAction action, Player player, SlotAccess accessor) {
        return CompatHandler.QUARK && QuarkCompat.isShulkerDropInOn() ? ItemsUtil.tryInteractingWithContainerItem(stack, incoming, slot, action, player, true) : false;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        return CompatHandler.QUARK && QuarkCompat.isShulkerDropInOn() ? ItemsUtil.tryInteractingWithContainerItem(stack, slot.getItem(), slot, action, player, false) : false;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        if (CompatHandler.QUARK && QuarkClientCompat.canRenderQuarkTooltip()) {
            CompoundTag cmp = pStack.getTagElement("BlockEntityTag");
            if (cmp != null && !cmp.contains("LootTable")) {
                return Optional.of(new InventoryTooltip(cmp, this, (Integer) CommonConfigs.Functional.SACK_SLOTS.get()));
            }
        }
        return Optional.empty();
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    public static float getEncumber(ItemStack slotItem) {
        if (slotItem.getItem() instanceof SackItem) {
            CompoundTag tag = slotItem.getTag();
            if (tag != null) {
                CompoundTag bet = tag.getCompound("BlockEntityTag");
                if (!bet.isEmpty()) {
                    ListTag l = bet.getList("Items", 10);
                    if (!l.isEmpty()) {
                        return 1.0F;
                    }
                }
            }
            return 0.0F;
        } else {
            return slotItem.is(ModTags.OVERENCUMBERING) ? (float) slotItem.getCount() / (float) slotItem.getMaxStackSize() : 0.0F;
        }
    }
}