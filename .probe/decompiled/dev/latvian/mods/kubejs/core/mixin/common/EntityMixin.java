package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.EntityKJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@RemapPrefixForJS("kjs$")
@Mixin({ Entity.class })
public abstract class EntityMixin implements EntityKJS {

    private CompoundTag kjs$persistentData;

    @Shadow
    @RemapForJS("stepHeight")
    public float maxUpStep;

    @Shadow
    @RemapForJS("age")
    public int tickCount;

    @Shadow
    public abstract boolean removeTag(String var1);

    @Override
    public CompoundTag kjs$getPersistentData() {
        if (this.kjs$persistentData == null) {
            this.kjs$persistentData = new CompoundTag();
        }
        return this.kjs$persistentData;
    }

    @Inject(method = { "saveWithoutId" }, at = { @At("RETURN") })
    private void saveKJS(CompoundTag tag, CallbackInfoReturnable<CompoundTag> ci) {
        if (this.kjs$persistentData != null && !this.kjs$persistentData.isEmpty()) {
            tag.put("KubeJSPersistentData", this.kjs$persistentData);
        }
    }

    @Inject(method = { "load" }, at = { @At("RETURN") })
    private void loadKJS(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("KubeJSPersistentData")) {
            this.kjs$persistentData = tag.getCompound("KubeJSPersistentData");
        } else {
            this.kjs$persistentData = null;
        }
    }

    @HideFromJS
    @Nullable
    @Override
    public CompoundTag kjs$getRawPersistentData() {
        return this.kjs$persistentData;
    }

    @HideFromJS
    @Override
    public void kjs$setRawPersistentData(@Nullable CompoundTag tag) {
        this.kjs$persistentData = tag;
    }

    @Shadow
    @RemapForJS("getUuid")
    public abstract UUID getUUID();

    @Shadow
    @RemapForJS("getStringUuid")
    public abstract String getStringUUID();

    @Shadow
    @RemapForJS("getUsername")
    public abstract String getScoreboardName();

    @Shadow
    @RemapForJS("isGlowing")
    public abstract boolean isCurrentlyGlowing();

    @Shadow
    @RemapForJS("setGlowing")
    public abstract void setGlowingTag(boolean var1);

    @Shadow
    @RemapForJS("getYaw")
    public abstract float getYRot();

    @Shadow
    @RemapForJS("setYaw")
    public abstract void setYRot(float var1);

    @Shadow
    @RemapForJS("getPitch")
    public abstract float getXRot();

    @Shadow
    @RemapForJS("setPitch")
    public abstract void setXRot(float var1);

    @Shadow
    @RemapForJS("setMotion")
    public abstract void setDeltaMovement(double var1, double var3, double var5);

    @Shadow
    @RemapForJS("setPositionAndRotation")
    public abstract void moveTo(double var1, double var3, double var5, float var7, float var8);

    @Shadow
    @RemapForJS("addMotion")
    public abstract void push(double var1, double var3, double var5);

    @Shadow
    @HideFromJS
    public abstract List<Entity> getPassengers();

    @Shadow
    @RemapForJS("isOnSameTeam")
    public abstract boolean isAlliedTo(Entity var1);

    @Shadow
    @RemapForJS("getHorizontalFacing")
    public abstract Direction getDirection();

    @Shadow
    @RemapForJS("extinguish")
    public abstract void clearFire();

    @Shadow
    @RemapForJS("attack")
    public abstract boolean hurt(DamageSource var1, float var2);

    @Shadow
    @RemapForJS("getDistanceSq")
    public abstract double distanceToSqr(double var1, double var3, double var5);

    @Shadow
    @RemapForJS("getEntityType")
    public abstract EntityType<?> getType();

    @Shadow
    @RemapForJS("distanceToEntitySqr")
    public abstract double distanceToSqr(Entity var1);

    @Shadow
    @RemapForJS("distanceToEntity")
    public abstract float distanceTo(Entity var1);

    @Shadow
    @HideFromJS
    public abstract Level level();
}