package noppes.npcs.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.controllers.data.MarkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LivingEntity.class })
public class EntityLivingMixin {

    @Inject(at = { @At("HEAD") }, method = { "addAdditionalSaveData" })
    private void renderToBuffer(CompoundTag compound, CallbackInfo callbackInfo) {
        LivingEntity e = (LivingEntity) this;
        if (!e.m_9236_().isClientSide()) {
            MarkData.get(e).save();
        }
    }
}