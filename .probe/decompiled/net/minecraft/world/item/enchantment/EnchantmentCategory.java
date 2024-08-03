package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.block.Block;

public enum EnchantmentCategory {

    ARMOR {

        @Override
        public boolean canEnchant(Item p_44751_) {
            return p_44751_ instanceof ArmorItem;
        }
    }
    ,
    ARMOR_FEET {

        @Override
        public boolean canEnchant(Item p_44806_) {
            if (p_44806_ instanceof ArmorItem $$1 && $$1.getEquipmentSlot() == EquipmentSlot.FEET) {
                return true;
            }
            return false;
        }
    }
    ,
    ARMOR_LEGS {

        @Override
        public boolean canEnchant(Item p_44811_) {
            if (p_44811_ instanceof ArmorItem $$1 && $$1.getEquipmentSlot() == EquipmentSlot.LEGS) {
                return true;
            }
            return false;
        }
    }
    ,
    ARMOR_CHEST {

        @Override
        public boolean canEnchant(Item p_44816_) {
            if (p_44816_ instanceof ArmorItem $$1 && $$1.getEquipmentSlot() == EquipmentSlot.CHEST) {
                return true;
            }
            return false;
        }
    }
    ,
    ARMOR_HEAD {

        @Override
        public boolean canEnchant(Item p_44756_) {
            if (p_44756_ instanceof ArmorItem $$1 && $$1.getEquipmentSlot() == EquipmentSlot.HEAD) {
                return true;
            }
            return false;
        }
    }
    ,
    WEAPON {

        @Override
        public boolean canEnchant(Item p_44761_) {
            return p_44761_ instanceof SwordItem;
        }
    }
    ,
    DIGGER {

        @Override
        public boolean canEnchant(Item p_44766_) {
            return p_44766_ instanceof DiggerItem;
        }
    }
    ,
    FISHING_ROD {

        @Override
        public boolean canEnchant(Item p_44771_) {
            return p_44771_ instanceof FishingRodItem;
        }
    }
    ,
    TRIDENT {

        @Override
        public boolean canEnchant(Item p_44776_) {
            return p_44776_ instanceof TridentItem;
        }
    }
    ,
    BREAKABLE {

        @Override
        public boolean canEnchant(Item p_44781_) {
            return p_44781_.canBeDepleted();
        }
    }
    ,
    BOW {

        @Override
        public boolean canEnchant(Item p_44786_) {
            return p_44786_ instanceof BowItem;
        }
    }
    ,
    WEARABLE {

        @Override
        public boolean canEnchant(Item p_44791_) {
            return p_44791_ instanceof Equipable || Block.byItem(p_44791_) instanceof Equipable;
        }
    }
    ,
    CROSSBOW {

        @Override
        public boolean canEnchant(Item p_44796_) {
            return p_44796_ instanceof CrossbowItem;
        }
    }
    ,
    VANISHABLE {

        @Override
        public boolean canEnchant(Item p_44801_) {
            return p_44801_ instanceof Vanishable || Block.byItem(p_44801_) instanceof Vanishable || BREAKABLE.canEnchant(p_44801_);
        }
    }
    ;

    public abstract boolean canEnchant(Item var1);
}