package me.jellysquid.mods.lithium.mixin.ai.raid;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Raider.ObtainRaidLeaderBannerGoal.class })
public class PickupBannerAsLeaderGoalMixin {

    private static final ItemStack CACHED_OMINOUS_BANNER = Raid.getLeaderBannerInstance();

    @Redirect(method = { "canStart()Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/Raid;getOminousBanner()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getOminousBanner() {
        return CACHED_OMINOUS_BANNER;
    }
}