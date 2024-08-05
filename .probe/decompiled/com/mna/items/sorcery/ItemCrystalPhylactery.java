package com.mna.items.sorcery;

import com.mna.api.config.GeneralConfigValues;
import com.mna.api.items.IPhylacteryItem;
import com.mna.api.items.TieredItem;
import com.mna.tools.math.MathUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemCrystalPhylactery extends TieredItem implements IPhylacteryItem {

    private static final String NBT_ENTITYTYPE = "mna:summon_entity_type";

    private static final String NBT_AMOUNT = "mna:summon_fill_pct";

    public ItemCrystalPhylactery() {
        super(new Item.Properties());
    }

    public static int getSoulCap() {
        return Math.max(GeneralConfigValues.PhylacteryKillCount, 1);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        IPhylacteryItem.super.appendTooltip(stack, worldIn, tooltip, flagIn);
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float amount = getFillAmount(stack);
        return amount < (float) getSoulCap() ? 16755200 : ChatFormatting.LIGHT_PURPLE.getColor();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float amount = getFillAmount(stack);
        if (amount > 0.0F && amount < 8.0F) {
            amount = 8.0F;
        }
        return (int) (13.0F * (amount / (float) getSoulCap()));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public static boolean isFilled(ItemStack stack) {
        return getFillAmount(stack) == (float) getSoulCap();
    }

    public static boolean addToPhylactery(Container inventory, EntityType<? extends Mob> type, float amount, Level world, boolean allowAddNew) {
        InvWrapper wrapper = new InvWrapper(inventory);
        return addToPhylactery(wrapper, type, amount, world, allowAddNew);
    }

    public static boolean addToPhylactery(IItemHandler inventory, EntityType<? extends Mob> type, float amount, Level world, boolean allowAddNew) {
        if (GeneralConfigValues.SummonBlacklist.contains(ForgeRegistries.ENTITY_TYPES.getKey(type).toString())) {
            return false;
        } else {
            int matching_slot = -1;
            int empty_slot = -1;
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.getItem() instanceof IPhylacteryItem) {
                    EntityType<?> containedType = ((IPhylacteryItem) stack.getItem()).getContainedEntity(stack);
                    if (containedType == type) {
                        float containedAmount = ((IPhylacteryItem) stack.getItem()).getContainedSouls(stack);
                        if (containedAmount < (float) ((IPhylacteryItem) stack.getItem()).getMaximumFill()) {
                            matching_slot = i;
                        }
                    } else if (containedType == null) {
                        empty_slot = i;
                    }
                }
            }
            if (matching_slot > -1) {
                ItemStack matching = inventory.getStackInSlot(matching_slot);
                if (matching.getItem() instanceof IPhylacteryItem) {
                    float existing = ((IPhylacteryItem) matching.getItem()).getContainedSouls(matching);
                    amount = MathUtils.clamp(existing + amount, 0.0F, (float) getSoulCap());
                    ((IPhylacteryItem) matching.getItem()).setContainedSouls(matching, amount);
                    return true;
                }
            } else if (allowAddNew && empty_slot > -1) {
                ItemStack empty = inventory.getStackInSlot(empty_slot);
                if (inventory instanceof Inventory) {
                    empty = ItemStack.EMPTY;
                    Inventory inv = (Inventory) inventory;
                    ItemStack mainHand = inv.player.m_21205_();
                    ItemStack offHand = inv.player.m_21206_();
                    if (offHand.getItem() instanceof IPhylacteryItem) {
                        EntityType<?> containedType = ((IPhylacteryItem) offHand.getItem()).getContainedEntity(offHand);
                        if (containedType == null) {
                            empty = offHand;
                        }
                    } else if (mainHand.getItem() instanceof IPhylacteryItem) {
                        EntityType<?> containedType = ((IPhylacteryItem) mainHand.getItem()).getContainedEntity(mainHand);
                        if (containedType == null) {
                            empty = mainHand;
                        }
                    }
                }
                if (!empty.isEmpty()) {
                    setEntityType(empty, type, world);
                    setFillAmount(empty, amount);
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean setFillAmount(ItemStack stack, float amount) {
        if (stack.getItem() instanceof IPhylacteryItem && getEntityType(stack) != null) {
            stack.getTag().putFloat("mna:summon_fill_pct", amount);
            return true;
        } else {
            return false;
        }
    }

    public static float getFillAmount(ItemStack stack) {
        return stack.getItem() instanceof IPhylacteryItem && getEntityType(stack) != null ? MathUtils.clamp(stack.getTag().getFloat("mna:summon_fill_pct"), 0.0F, (float) getSoulCap()) : 0.0F;
    }

    @Nullable
    public static EntityType<? extends Mob> getEntityType(ItemStack stack) {
        if (stack.getItem() instanceof IPhylacteryItem && stack.getDamageValue() <= 0 && stack.hasTag() && stack.getTag().contains("mna:summon_entity_type")) {
            String type = stack.getTag().getString("mna:summon_entity_type");
            ResourceLocation rLoc = new ResourceLocation(type);
            try {
                return (EntityType<? extends Mob>) ForgeRegistries.ENTITY_TYPES.getValue(rLoc);
            } catch (ClassCastException var4) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean setEntityType(ItemStack stack, EntityType<? extends Mob> type, Level world) {
        if (stack.getItem() instanceof IPhylacteryItem && getEntityType(stack) == null) {
            Mob inst = type.create(world);
            if (!GeneralConfigValues.BossesAllowedInPhylacteries && !inst.m_6072_()) {
                return false;
            } else {
                stack.getOrCreateTag().putString("mna:summon_entity_type", ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean fill(IItemHandler inventory, EntityType<? extends Mob> type, float amount, Level world, boolean allowAddNew) {
        return addToPhylactery(inventory, type, amount, world, allowAddNew);
    }

    @Override
    public boolean fill(Container inventory, EntityType<? extends Mob> type, float amount, Level world, boolean allowAddNew) {
        return addToPhylactery(inventory, type, amount, world, allowAddNew);
    }

    @Override
    public boolean fill(ItemStack stack, EntityType<? extends Mob> type, float amount, Level world) {
        if (!(stack.getItem() instanceof IPhylacteryItem)) {
            return false;
        } else {
            EntityType<?> containedType = getEntityType(stack);
            if (containedType != null && containedType != type) {
                return false;
            } else if (containedType == null && !setEntityType(stack, type, world)) {
                return false;
            } else {
                float existing = ((IPhylacteryItem) stack.getItem()).getContainedSouls(stack);
                return setFillAmount(stack, existing + amount);
            }
        }
    }

    @Override
    public EntityType<? extends Mob> getContainedEntity(ItemStack stack) {
        return getEntityType(stack);
    }

    @Override
    public float getFillPct(ItemStack stack) {
        return MathUtils.clamp01(getFillAmount(stack) / (float) getSoulCap());
    }

    @Override
    public boolean isFull(ItemStack stack) {
        return isFilled(stack);
    }

    @Override
    public int getMaximumFill() {
        return getSoulCap();
    }

    @Override
    public float getContainedSouls(ItemStack stack) {
        return getFillAmount(stack);
    }

    @Override
    public boolean setContainedSouls(ItemStack stack, float amount) {
        return setFillAmount(stack, amount);
    }
}