package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ TridentItem.class })
public class TridentItemMixin {

    @Inject(method = { "releaseUsing" }, at = { @At("TAIL") })
    public void releaseUsing(ItemStack itemStack0, Level level1, LivingEntity livingEntity, int int2, CallbackInfo ci) {
        if (livingEntity.f_19853_.isClientSide) {
            ClientMagicData.getSyncedSpellData(livingEntity).setSpinAttackType(SpinAttackType.RIPTIDE);
        } else {
            MagicData.getPlayerMagicData(livingEntity).getSyncedData().setSpinAttackType(SpinAttackType.RIPTIDE);
        }
    }
}