package com.mna.items.artifice;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.factions.Factions;
import com.mna.tools.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSpectralElytra extends TieredItem implements IFactionSpecific {

    private static final float MANA_COST_PER_TICK = 2.0F;

    private static final float FLIGHT_SPEED = 1.5F;

    public ItemSpectralElytra() {
        super(new Item.Properties());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.CHEST;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.m_21120_(handIn);
        EquipmentSlot slotType = EquipmentSlot.CHEST;
        ItemStack equippedItem = playerIn.getItemBySlot(slotType);
        if (equippedItem.isEmpty()) {
            playerIn.setItemSlot(slotType, heldItem.copy());
            heldItem.setCount(0);
            return InteractionResultHolder.success(heldItem);
        } else {
            return InteractionResultHolder.fail(heldItem);
        }
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return EntityUtil.doElytraFlightTick(stack, entity, flightTicks, 2.0F, 1.5F);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return entity instanceof Player && !entity.m_20072_() && !entity.m_20077_() && ((Player) entity).getCapability(PlayerMagicProvider.MAGIC).isPresent() && ((IPlayerMagic) ((Player) entity).getCapability(PlayerMagicProvider.MAGIC).orElse(null)).getCastingResource().hasEnoughAbsolute(entity, 2.0F);
    }

    @Override
    public IFaction getFaction() {
        return Factions.FEY;
    }
}