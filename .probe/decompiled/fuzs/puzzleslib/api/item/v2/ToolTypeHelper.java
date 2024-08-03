package fuzs.puzzleslib.api.item.v2;

import fuzs.puzzleslib.impl.core.CommonFactories;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;

public interface ToolTypeHelper {

    ToolTypeHelper INSTANCE = CommonFactories.INSTANCE.getToolTypeHelper();

    default boolean isSword(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.is(ItemTags.SWORDS);
    }

    default boolean isAxe(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.is(ItemTags.AXES);
    }

    default boolean isHoe(ItemStack stack) {
        return stack.getItem() instanceof HoeItem || stack.is(ItemTags.HOES);
    }

    default boolean isPickaxe(ItemStack stack) {
        return stack.getItem() instanceof PickaxeItem || stack.is(ItemTags.PICKAXES);
    }

    default boolean isShovel(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.is(ItemTags.SHOVELS);
    }

    default boolean isShears(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem;
    }

    default boolean isShield(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    default boolean isBow(ItemStack stack) {
        return stack.getItem() instanceof BowItem;
    }

    default boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    default boolean isFishingRod(ItemStack stack) {
        return stack.getItem() instanceof FishingRodItem;
    }

    @Deprecated(forRemoval = true)
    default boolean isTrident(ItemStack stack) {
        return this.isTridentLike(stack);
    }

    default boolean isTridentLike(ItemStack stack) {
        return stack.getItem() instanceof TridentItem;
    }

    default boolean isMeleeWeapon(ItemStack stack) {
        return this.isSword(stack) || this.isAxe(stack) || this.isTridentLike(stack);
    }

    default boolean isRangedWeapon(ItemStack stack) {
        return this.isBow(stack) || this.isCrossbow(stack) || this.isTridentLike(stack);
    }

    default boolean isWeapon(ItemStack stack) {
        return this.isMeleeWeapon(stack) || this.isRangedWeapon(stack);
    }

    default boolean isMiningTool(ItemStack stack) {
        return this.isAxe(stack) || this.isHoe(stack) || this.isPickaxe(stack) || this.isShovel(stack);
    }

    default boolean isTool(ItemStack stack) {
        return this.isMiningTool(stack) || this.isMeleeWeapon(stack) || stack.is(ItemTags.TOOLS);
    }
}