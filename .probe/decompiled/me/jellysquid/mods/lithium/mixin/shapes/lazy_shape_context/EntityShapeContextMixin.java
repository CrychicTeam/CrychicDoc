package me.jellysquid.mods.lithium.mixin.shapes.lazy_shape_context;

import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EntityCollisionContext.class })
public class EntityShapeContextMixin {

    @Mutable
    @Shadow
    @Final
    private ItemStack heldItem;

    @Mutable
    @Shadow
    @Final
    private Predicate<FluidState> canStandOnFluid;

    @Shadow
    @Final
    @Nullable
    private Entity entity;

    @ModifyConstant(method = { "<init>(Lnet/minecraft/entity/Entity;)V" }, constant = { @Constant(classValue = LivingEntity.class, ordinal = 0) })
    private static boolean redirectInstanceOf(Object obj, Class<?> clazz) {
        return false;
    }

    @ModifyConstant(method = { "<init>(Lnet/minecraft/entity/Entity;)V" }, constant = { @Constant(classValue = LivingEntity.class, ordinal = 2) })
    private static boolean redirectInstanceOf2(Object obj, Class<?> clazz) {
        return false;
    }

    @Inject(method = { "<init>(Lnet/minecraft/entity/Entity;)V" }, at = { @At("RETURN") })
    private void initFields(Entity entity, CallbackInfo ci) {
        this.heldItem = null;
        this.canStandOnFluid = null;
    }

    @Inject(method = { "isHolding(Lnet/minecraft/item/Item;)Z" }, at = { @At("HEAD") })
    public void isHolding(Item item, CallbackInfoReturnable<Boolean> cir) {
        if (this.heldItem == null) {
            this.heldItem = this.entity instanceof LivingEntity ? ((LivingEntity) this.entity).getMainHandItem() : ItemStack.EMPTY;
        }
    }

    @Inject(method = { "canWalkOnFluid(Lnet/minecraft/fluid/FluidState;Lnet/minecraft/fluid/FluidState;)Z" }, at = { @At("HEAD") })
    public void canWalkOnFluid(FluidState state, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (this.canStandOnFluid == null) {
            if (this.entity instanceof LivingEntity livingEntity) {
                this.canStandOnFluid = livingEntity::m_203441_;
            } else {
                this.canStandOnFluid = liquid -> false;
            }
        }
    }
}