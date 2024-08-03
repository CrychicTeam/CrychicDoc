package dev.xkmc.modulargolems.init.material;

import java.util.Locale;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum VanillaGolemWeaponMaterial implements IGolemWeaponMaterial {

    IRON(6, false, Items.IRON_INGOT), DIAMOND(8, false, Items.DIAMOND), NETHERITE(10, true, Items.NETHERITE_INGOT);

    private final int damage;

    private final boolean fireResistant;

    private final Item ingot;

    private VanillaGolemWeaponMaterial(int damage, boolean fireResistant, Item ingot) {
        this.damage = damage;
        this.fireResistant = fireResistant;
        this.ingot = ingot;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean fireResistant() {
        return this.fireResistant;
    }

    @Override
    public Item getIngot() {
        return this.ingot;
    }
}