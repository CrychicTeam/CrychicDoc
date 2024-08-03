package dev.xkmc.l2hostility.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ Mob.class })
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;IZ)Lnet/minecraft/world/item/ItemStack;") }, method = { "enchantSpawnedArmor" })
    public ItemStack l2hostility_addEnchantValueForArmor(RandomSource r, ItemStack stack, int level, boolean treasure, Operation<ItemStack> op) {
        LazyOptional<MobTraitCap> opt = this.getCapability(MobTraitCap.CAPABILITY);
        if (opt.resolve().isPresent()) {
            level += ((MobTraitCap) opt.resolve().get()).getEnchantBonus();
        }
        return (ItemStack) op.call(new Object[] { r, stack, level, treasure });
    }

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;IZ)Lnet/minecraft/world/item/ItemStack;") }, method = { "enchantSpawnedWeapon" })
    public ItemStack l2hostility_addEnchantValueForWeapon(RandomSource r, ItemStack stack, int level, boolean treasure, Operation<ItemStack> op) {
        LazyOptional<MobTraitCap> opt = this.getCapability(MobTraitCap.CAPABILITY);
        if (opt.resolve().isPresent()) {
            level += ((MobTraitCap) opt.resolve().get()).getEnchantBonus();
        }
        return (ItemStack) op.call(new Object[] { r, stack, level, treasure });
    }
}