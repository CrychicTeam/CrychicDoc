package io.redspace.ironsspellbooks.entity.mobs;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CatacombsZombie extends Zombie {

    public CatacombsZombie(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(EntityType.ZOMBIE, pLevel);
        if (this.f_19796_.nextFloat() < 0.2F) {
            switch(this.f_19796_.nextIntBetweenInclusive(1, 4)) {
                case 1:
                    this.m_7292_(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE));
                    break;
                case 2:
                    this.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1));
                    break;
                case 3:
                    this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                    break;
                case 4:
                    this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE));
            }
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(random, pDifficulty);
        Item[] leather = new Item[] { Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET };
        Item[] chain = new Item[] { Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET };
        Item[] iron = new Item[] { Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_HELMET };
        float power = random.nextFloat();
        ItemStack[] equipment = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            if (random.nextFloat() > 0.6F) {
                equipment[i] = ItemStack.EMPTY;
            } else {
                float stray = (random.nextFloat() - 0.5F) / 3.0F;
                if ((double) (power + stray) > 0.85) {
                    equipment[i] = new ItemStack(iron[i]);
                } else if ((double) (power + stray) > 0.45) {
                    equipment[i] = new ItemStack(chain[i]);
                } else {
                    equipment[i] = new ItemStack(leather[i]);
                }
            }
        }
        this.m_8061_(EquipmentSlot.FEET, equipment[0]);
        this.m_8061_(EquipmentSlot.LEGS, equipment[1]);
        this.m_8061_(EquipmentSlot.CHEST, equipment[2]);
        this.m_8061_(EquipmentSlot.HEAD, equipment[3]);
        if (random.nextFloat() < 0.01F) {
            this.m_8061_(EquipmentSlot.HEAD, new ItemStack(Items.CYAN_BANNER));
        }
        this.m_21409_(EquipmentSlot.FEET, 0.0F);
        this.m_21409_(EquipmentSlot.LEGS, 0.0F);
        this.m_21409_(EquipmentSlot.CHEST, 0.0F);
        this.m_21409_(EquipmentSlot.HEAD, 0.0F);
    }
}