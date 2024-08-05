package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EntityRenderDispatcher.class })
public class EntityDispatcherMixin {

    @Inject(at = { @At("HEAD") }, method = { "shouldRender" }, cancellable = true)
    public <E extends Entity> void inject$shouldRender(E entity, Frustum clippingHelper, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        MobCategory category = entity.getType().getCategory();
        int distY;
        int distX;
        if (category == MobCategory.MONSTER) {
            if (!EmbyConfig.monsterDistanceCullingCache) {
                return;
            }
            distY = EmbyConfig.monsterCullingDistanceYCache;
            distX = EmbyConfig.monsterCullingDistanceXCache;
        } else {
            if (!EmbyConfig.entityDistanceCullingCache) {
                return;
            }
            distY = EmbyConfig.entityCullingDistanceYCache;
            distX = EmbyConfig.entityCullingDistanceXCache;
        }
        boolean isWhitelisted = ((IWhitelistCheck) entity.getType()).embPlus$isWhitelisted();
        if (!isWhitelisted && !EmbyTools.isEntityInRange(entity, x, y, z, distY, distX)) {
            cir.setReturnValue(false);
        }
    }
}