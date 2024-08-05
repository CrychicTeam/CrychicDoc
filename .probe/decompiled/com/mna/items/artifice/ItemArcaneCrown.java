package com.mna.items.artifice;

import com.mna.api.capabilities.CurioItemCapability;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ItemArcaneCrown extends TieredItem implements IFactionSpecific {

    public ItemArcaneCrown() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.m_21120_(handIn);
        EquipmentSlot slotType = EquipmentSlot.HEAD;
        ItemStack equippedItem = playerIn.getItemBySlot(slotType);
        if (equippedItem.isEmpty()) {
            playerIn.setItemSlot(slotType, heldItem.copy());
            heldItem.setCount(0);
            return InteractionResultHolder.success(heldItem);
        } else {
            return InteractionResultHolder.fail(heldItem);
        }
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return entity instanceof Player && stack.getItem() == this && armorType == EquipmentSlot.HEAD;
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        this.tickEffect(player, world);
    }

    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag nbt) {
        return CurioItemCapability.createProvider(new ICurio() {

            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (livingEntity instanceof Player) {
                    ItemArcaneCrown.this.tickEffect((Player) livingEntity, livingEntity.m_9236_());
                }
            }

            @Override
            public ItemStack getStack() {
                return stack;
            }
        });
    }

    @Override
    public float getMaxIre() {
        return 5.0E-4F;
    }

    @Override
    public IFaction getFaction() {
        return Factions.COUNCIL;
    }

    private void tickEffect(Player player, Level world) {
        if (!world.isClientSide) {
            MobEffectInstance manaBoost = player.m_21124_(EffectInit.MANA_BOOST.get());
            if (manaBoost == null || manaBoost.getDuration() < 10 || manaBoost.getAmplifier() < 1) {
                player.m_7292_(new MobEffectInstance(EffectInit.MANA_BOOST.get(), 100, 1, false, false, false));
                this.usedByPlayer(player);
            }
        }
        MobEffectInstance dispelExhaustion = player.m_21124_(EffectInit.DISPEL_EXHAUSTION.get());
        if (dispelExhaustion == null) {
            int effectsRemoved = 0;
            this.usedByPlayer(player);
            ArrayList<MobEffect> toRemove = new ArrayList();
            for (MobEffectInstance inst : player.m_21220_()) {
                if (inst.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    toRemove.add(inst.getEffect());
                }
            }
            for (MobEffect e : toRemove) {
                if (player.m_21195_(e)) {
                    effectsRemoved++;
                }
            }
            if (effectsRemoved > 0 && !world.isClientSide) {
                player.m_7292_(new MobEffectInstance(EffectInit.DISPEL_EXHAUSTION.get(), 6000, 0, false, false, false));
            }
        }
    }
}