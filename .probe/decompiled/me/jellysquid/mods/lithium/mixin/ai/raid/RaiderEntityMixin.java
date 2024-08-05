package me.jellysquid.mods.lithium.mixin.ai.raid;

import java.util.function.Predicate;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Raider.class })
public class RaiderEntityMixin {

    private static final ItemStack CACHED_OMINOUS_BANNER = Raid.getLeaderBannerInstance();

    @Mutable
    @Shadow
    @Final
    static Predicate<ItemEntity> ALLOWED_ITEMS = itemEntity -> !itemEntity.hasPickUpDelay() && itemEntity.m_6084_() && ItemStack.matches(itemEntity.getItem(), CACHED_OMINOUS_BANNER);

    @Redirect(method = { "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/Raid;getOminousBanner()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getOminousBanner() {
        return CACHED_OMINOUS_BANNER;
    }
}