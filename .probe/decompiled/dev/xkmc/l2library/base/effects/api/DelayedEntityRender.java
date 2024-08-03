package dev.xkmc.l2library.base.effects.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public record DelayedEntityRender(LivingEntity entity, IconRenderRegion region, ResourceLocation rl, float tx, float ty, float tw, float th) {

    public static DelayedEntityRender icon(LivingEntity entity, ResourceLocation rl) {
        return icon(entity, IconRenderRegion.identity(), rl);
    }

    public static DelayedEntityRender icon(LivingEntity entity, IconRenderRegion r, ResourceLocation rl) {
        return new DelayedEntityRender(entity, r, rl, 0.0F, 0.0F, 1.0F, 1.0F);
    }

    public DelayedEntityRender resize(IconRenderRegion r) {
        return new DelayedEntityRender(this.entity, r.resize(this.region), this.rl, this.tx, this.ty, this.tw, this.th);
    }
}