package net.liopyu.entityjs.mixin;

import net.liopyu.entityjs.builders.modification.ModifyPathfinderMobBuilder;
import net.liopyu.entityjs.events.EntityModificationEventJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.EventHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { PathfinderMob.class }, remap = false)
public abstract class PathfinderMobMixin {

    @Unique
    public Object entityJs$builder;

    @Unique
    private Object entityJs$entityObject = this;

    @Unique
    private PathfinderMob entityJs$getLivingEntity() {
        return (PathfinderMob) this.entityJs$entityObject;
    }

    @Unique
    private String entityJs$entityName() {
        return this.entityJs$getLivingEntity().m_6095_().toString();
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void entityjs$onMobInit(EntityType<?> pEntityType, Level pLevel, CallbackInfo ci) {
        EntityType<?> entityType = this.entityJs$getLivingEntity().m_6095_();
        if (EventHandlers.modifyEntity.hasListeners()) {
            EntityModificationEventJS eventJS = EntityModificationEventJS.getOrCreate(entityType, this.entityJs$getLivingEntity());
            EventHandlers.modifyEntity.post(eventJS);
            this.entityJs$builder = eventJS.getBuilder();
        }
    }

    @Inject(method = { "getWalkTargetValue(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/LevelReader;)F" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    public void getWalkTargetValue(BlockPos pPos, LevelReader pLevel, CallbackInfoReturnable<Float> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyPathfinderMobBuilder builder) {
            if (builder.walkTargetValue == null) {
                return;
            }
            ContextUtils.EntityBlockPosLevelContext context = new ContextUtils.EntityBlockPosLevelContext(pPos, pLevel, this.entityJs$getLivingEntity());
            Object obj = EntityJSHelperClass.convertObjectToDesired(builder.walkTargetValue.apply(context), "float");
            if (obj != null) {
                cir.setReturnValue((Float) obj);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for walkTargetValue from entity: " + this.entityJs$entityName() + ". Value: " + builder.walkTargetValue.apply(context) + ". Must be a float. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "shouldStayCloseToLeashHolder" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void shouldStayCloseToLeashHolder(CallbackInfoReturnable<Boolean> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyPathfinderMobBuilder builder) {
            if (builder.shouldStayCloseToLeashHolder == null) {
                return;
            }
            Object value = builder.shouldStayCloseToLeashHolder.apply(this.entityJs$getLivingEntity());
            if (value instanceof Boolean b) {
                cir.setReturnValue(b);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldStayCloseToLeashHolder from entity: " + this.entityJs$entityName() + ". Value: " + value + ". Must be a boolean. Defaulting to " + cir.getReturnValue());
            }
        }
    }

    @Inject(method = { "followLeashSpeed" }, at = { @At(value = "HEAD", ordinal = 0) }, remap = false, cancellable = true)
    protected void followLeashSpeed(CallbackInfoReturnable<Double> cir) {
        if (this.entityJs$builder != null && this.entityJs$builder instanceof ModifyPathfinderMobBuilder builder) {
            if (builder.followLeashSpeed == null) {
                return;
            }
            cir.setReturnValue(builder.followLeashSpeed);
        }
    }
}