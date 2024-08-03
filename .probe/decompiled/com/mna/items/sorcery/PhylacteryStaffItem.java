package com.mna.items.sorcery;

import com.mna.api.config.GeneralConfigValues;
import com.mna.api.items.IPhylacteryItem;
import com.mna.gui.containers.providers.NamedPhylacteryBook;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IRadialInventorySelect;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.tools.math.MathUtils;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class PhylacteryStaffItem extends ItemBagBase implements IRadialInventorySelect {

    public static final int CAPACITY = 24;

    public static final int RADIAL_CAPACITY = 12;

    @Override
    public ItemFilterGroup filterGroup() {
        return ItemFilterGroup.ANY_NON_EMPTY_PHYLACTERY;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedPhylacteryBook(stack);
    }

    @Override
    public int getIndex(ItemStack stack) {
        return stack.getItem() == ItemInit.STAFF_PHYLACTERY.get() && stack.hasTag() && stack.getTag().contains("index") ? stack.getTag().getInt("index") : 0;
    }

    @Override
    public void setIndex(ItemStack stack, int index) {
        if (stack.getItem() == ItemInit.STAFF_PHYLACTERY.get()) {
            stack.getOrCreateTag().putInt("index", index);
        }
    }

    public static boolean isFilled(ItemStack stack) {
        if (stack.getItem() == ItemInit.CRYSTAL_PHYLACTERY.get()) {
            return ItemCrystalPhylactery.isFilled(stack);
        } else if (stack.getItem() == ItemInit.STAFF_PHYLACTERY.get()) {
            int index = ItemInit.STAFF_PHYLACTERY.get().getIndex(stack);
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack invStack = inv.getStackInSlot(index);
            return ItemCrystalPhylactery.isFilled(invStack);
        } else {
            return false;
        }
    }

    @Nullable
    public static EntityType<? extends Mob> getEntityType(ItemStack stack) {
        if (stack.getItem() == ItemInit.CRYSTAL_PHYLACTERY.get()) {
            return ItemCrystalPhylactery.getEntityType(stack);
        } else if (stack.getItem() == ItemInit.STAFF_PHYLACTERY.get()) {
            int index = ItemInit.STAFF_PHYLACTERY.get().getIndex(stack);
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack invStack = inv.getStackInSlot(index);
            return ItemCrystalPhylactery.getEntityType(invStack);
        } else {
            return null;
        }
    }

    public static boolean addToPhylactery(ItemStack staffStack, EntityType<? extends Mob> type, float amount, Level world) {
        if (GeneralConfigValues.SummonBlacklist.contains(ForgeRegistries.ENTITY_TYPES.getKey(type).toString())) {
            return false;
        } else {
            ItemInventoryBase inv = new ItemInventoryBase(staffStack);
            int matching_slot = -1;
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.getItem() instanceof IPhylacteryItem) {
                    EntityType<?> containedType = ((IPhylacteryItem) stack.getItem()).getContainedEntity(stack);
                    if (containedType == type) {
                        float containedAmount = ((IPhylacteryItem) stack.getItem()).getFillPct(stack);
                        if (containedAmount < (float) ((IPhylacteryItem) stack.getItem()).getMaximumFill()) {
                            matching_slot = i;
                        }
                    }
                }
            }
            if (matching_slot > -1) {
                ItemStack matching = inv.getStackInSlot(matching_slot);
                if (matching.getItem() instanceof IPhylacteryItem) {
                    float existing = ((IPhylacteryItem) matching.getItem()).getContainedSouls(matching);
                    existing = MathUtils.clamp(existing + amount, 0.0F, (float) ((IPhylacteryItem) matching.getItem()).getMaximumFill());
                    ((IPhylacteryItem) matching.getItem()).setContainedSouls(matching, existing);
                    inv.writeItemStack();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        int index = ItemInit.STAFF_PHYLACTERY.get().getIndex(stack);
        ItemInventoryBase inv = new ItemInventoryBase(stack);
        ItemStack invStack = inv.getStackInSlot(index);
        if (invStack.getItem() instanceof IPhylacteryItem) {
            ItemInit.CRYSTAL_PHYLACTERY.get().appendHoverText(invStack, worldIn, tooltip, flagIn);
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        IRadialInventorySelect.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int capacity() {
        return 12;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack book = player.m_21120_(hand);
        player.m_6674_(hand);
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            this.openGuiIfModifierPressed(book, player, world);
        }
        return InteractionResultHolder.success(player.m_21120_(hand));
    }
}