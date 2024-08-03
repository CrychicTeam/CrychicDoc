package se.mickelus.tetra.items.modular.impl.crossbow;

import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorBlock;

@ParametersAreNonnullByDefault
public class ShootableDummyItem extends ProjectileWeaponItem {

    public static final String identifier = "shootable_dummy";

    public static final Predicate<ItemStack> ammoPredicate = f_43006_.or(stack -> stack.getItem() == ChthonicExtractorBlock.item).or(stack -> stack.getItem() == ChthonicExtractorBlock.usedItem);

    public ShootableDummyItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ammoPredicate;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return f_43005_;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }
}