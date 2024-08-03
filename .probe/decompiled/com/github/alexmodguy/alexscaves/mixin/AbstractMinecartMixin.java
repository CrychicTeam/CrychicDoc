package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MagneticLevitationRailBlock;
import com.github.alexmodguy.alexscaves.server.entity.util.MinecartAccessor;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractMinecart.class })
public abstract class AbstractMinecartMixin extends Entity implements MinecartAccessor {

    @Shadow
    private boolean flipped;

    @Shadow
    private int lSteps;

    @Shadow
    private double lx;

    @Shadow
    private double ly;

    @Shadow
    private double lz;

    @Shadow
    private double lyr;

    @Shadow
    private double lxr;

    private BlockPos lastMagLevCheck = null;

    private BlockPos magLevBelow = null;

    private float magLevProgress = 0.0F;

    private float prevMagLevProgress = 0.0F;

    @Shadow
    public abstract int getHurtTime();

    @Shadow
    public abstract void setHurtTime(int var1);

    @Shadow
    public abstract void setDamage(float var1);

    @Shadow
    public abstract float getDamage();

    @Shadow
    @Nullable
    public abstract Vec3 getPos(double var1, double var3, double var5);

    @Shadow
    private static Pair<Vec3i, Vec3i> exits(RailShape railShape0) {
        return null;
    }

    @Shadow
    protected abstract void applyNaturalSlowdown();

    @Shadow
    protected abstract boolean isRedstoneConductor(BlockPos var1);

    @Shadow
    @Override
    public abstract boolean isOnRails();

    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;tick()V" }, at = { @At("HEAD") }, cancellable = true)
    public void ac_tick(CallbackInfo ci) {
        this.prevMagLevProgress = this.magLevProgress;
        if (this.lastMagLevCheck == null || !this.lastMagLevCheck.equals(this.m_20183_())) {
            this.lastMagLevCheck = this.m_20183_();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.m_20185_(), this.m_20186_(), this.m_20189_());
            boolean flag = false;
            for (int i = 0; i < 3; i++) {
                if (this.m_9236_().getBlockState(mutableBlockPos).m_60713_(ACBlockRegistry.MAGNETIC_LEVITATION_RAIL.get())) {
                    flag = true;
                    break;
                }
                mutableBlockPos.move(0, -1, 0);
            }
            this.magLevBelow = flag ? mutableBlockPos.immutable() : null;
        }
        if (this.magLevBelow != null) {
            if (this.magLevProgress < 1.0F) {
                this.magLevProgress += 0.2F;
            }
            BlockState magLevBlockState = this.m_9236_().getBlockState(this.magLevBelow);
            if (magLevBlockState.m_60713_(ACBlockRegistry.MAGNETIC_LEVITATION_RAIL.get())) {
                ci.cancel();
                if (this.getHurtTime() > 0) {
                    this.setHurtTime(this.getHurtTime() - 1);
                }
                if (this.getDamage() > 0.0F) {
                    this.setDamage(this.getDamage() - 1.0F);
                }
                this.m_146871_();
                this.m_20157_();
                if (this.m_9236_().isClientSide) {
                    if (this.lSteps > 0) {
                        double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                        double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                        double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                        double d2 = Mth.wrapDegrees(this.lyr - (double) this.m_146908_());
                        this.m_146922_(this.m_146908_() + (float) d2 / (float) this.lSteps);
                        this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                        this.lSteps--;
                        this.m_6034_(d5, d6, d7);
                        this.m_19915_(this.m_146908_(), this.m_146909_());
                    } else {
                        this.m_20090_();
                        this.m_19915_(this.m_146908_(), this.m_146909_());
                    }
                } else {
                    if (!this.m_20068_()) {
                        double d0 = this.m_20069_() ? -0.005 : -0.04;
                        this.m_20256_(this.m_20184_().add(0.0, d0, 0.0));
                    }
                    this.moveAlongMagLev(this.magLevBelow, magLevBlockState);
                    this.m_20101_();
                    this.m_146926_(0.0F);
                    double d1 = this.f_19854_ - this.m_20185_();
                    double d3 = this.f_19856_ - this.m_20189_();
                    if (d1 * d1 + d3 * d3 > 0.001) {
                        this.m_146922_((float) (Mth.atan2(d3, d1) * 180.0 / Math.PI));
                        if (this.flipped) {
                            this.m_146922_(this.m_146908_() + 180.0F);
                        }
                    }
                    double d4 = (double) Mth.wrapDegrees(this.m_146908_() - this.f_19859_);
                    if (d4 < -170.0 || d4 >= 170.0) {
                        this.m_146922_(this.m_146908_() + 180.0F);
                        this.flipped = !this.flipped;
                    }
                    this.m_19915_(this.m_146908_(), this.m_146909_());
                    AABB box = this.m_20191_().inflate(0.2F, 0.0, 0.2F);
                    if (((AbstractMinecart) this).canBeRidden() && this.m_20184_().horizontalDistanceSqr() > 0.01) {
                        List<Entity> list = this.m_9236_().getEntities(this, box, EntitySelector.pushableBy(this));
                        if (!list.isEmpty()) {
                            for (int l = 0; l < list.size(); l++) {
                                Entity entity1 = (Entity) list.get(l);
                                if (!(entity1 instanceof Player) && !(entity1 instanceof IronGolem) && !(entity1 instanceof AbstractMinecart) && !this.m_20160_() && !entity1.isPassenger()) {
                                    entity1.startRiding(this);
                                } else {
                                    entity1.push(this);
                                }
                            }
                        }
                    } else {
                        for (Entity entity : this.m_9236_().m_45933_(this, box)) {
                            if (!this.m_20363_(entity) && entity.isPushable() && entity instanceof AbstractMinecart) {
                                entity.push(this);
                            }
                        }
                    }
                    this.m_20073_();
                    if (this.m_20077_()) {
                        this.m_20093_();
                        this.f_19789_ *= 0.5F;
                    }
                    this.f_19803_ = false;
                }
            }
            if (this.m_9236_().isClientSide && this.f_19796_.nextFloat() < 0.4F) {
                Vec3 randomLightningFrom = this.magLevBelow.getCenter().add((double) (this.f_19796_.nextFloat() - 0.5F), -0.4F, (double) (this.f_19796_.nextFloat() - 0.5F));
                Vec3 vec3 = this.m_20182_().add(this.m_20184_()).add(new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), 0.2F, (double) (this.f_19796_.nextFloat() - 0.5F)));
                this.m_9236_().addParticle(ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get(), randomLightningFrom.x, randomLightningFrom.y, randomLightningFrom.z, vec3.x, vec3.y, vec3.z);
            }
        } else if (this.magLevProgress > 0.0F) {
            this.magLevProgress -= 0.2F;
            if (!this.isOnRails()) {
                this.m_20256_(this.m_20184_().multiply(1.5, 1.0, 1.5).add(0.0, 0.1F, 0.0));
            }
        }
    }

    private void moveAlongMagLev(BlockPos railPos, BlockState railState) {
        boolean doRailFunctions = ((AbstractMinecart) this).shouldDoRailFunctions();
        this.m_183634_();
        double d0 = this.m_20185_();
        double d1 = this.m_20186_();
        double d2 = this.m_20189_();
        Vec3 vec3 = this.getPos(d0, d1, d2);
        boolean flag = true;
        boolean flag1 = true;
        double d3 = ((AbstractMinecart) this).getSlopeAdjustment();
        if (this.m_20069_()) {
            d3 *= 0.2;
        }
        Vec3 vec31 = this.m_20184_();
        RailShape railshape = ((BaseRailBlock) railState.m_60734_()).getRailDirection(railState, this.m_9236_(), railPos, (AbstractMinecart) this);
        switch(railshape) {
            case ASCENDING_EAST:
                this.m_20256_(vec31.add(-d3, 0.0, 0.0));
                d1++;
                break;
            case ASCENDING_WEST:
                this.m_20256_(vec31.add(d3, 0.0, 0.0));
                d1++;
                break;
            case ASCENDING_NORTH:
                this.m_20256_(vec31.add(0.0, 0.0, d3));
                d1++;
                break;
            case ASCENDING_SOUTH:
                this.m_20256_(vec31.add(0.0, 0.0, -d3));
                d1++;
        }
        vec31 = this.m_20184_();
        Pair<Vec3i, Vec3i> pair = exits(railshape);
        Vec3i vec3i = (Vec3i) pair.getFirst();
        Vec3i vec3i1 = (Vec3i) pair.getSecond();
        double d4 = (double) (vec3i1.getX() - vec3i.getX());
        double d5 = (double) (vec3i1.getZ() - vec3i.getZ());
        double d6 = Math.sqrt(d4 * d4 + d5 * d5);
        double d7 = vec31.x * d4 + vec31.z * d5;
        if (d7 < 0.0) {
            d4 = -d4;
            d5 = -d5;
        }
        double d8 = Math.min(2.0, vec31.horizontalDistance());
        vec31 = new Vec3(d8 * d4 / d6, vec31.y, d8 * d5 / d6);
        this.m_20256_(vec31);
        Entity entity = this.m_146895_();
        if (entity instanceof Player) {
            Vec3 vec32 = entity.getDeltaMovement();
            double d9 = vec32.horizontalDistanceSqr();
            double d11 = this.m_20184_().horizontalDistanceSqr();
            if (d9 > 1.0E-4 && d11 < 0.01) {
                this.m_20256_(this.m_20184_().add(vec32.x * 0.1, 0.0, vec32.z * 0.1));
                flag1 = false;
            }
        }
        if (flag1 && doRailFunctions) {
            double d22 = this.m_20184_().horizontalDistance();
            if (d22 < 0.03) {
                this.m_20256_(Vec3.ZERO);
            } else {
                this.m_20256_(this.m_20184_().multiply(0.5, 0.0, 0.5));
            }
        }
        double d23 = (double) railPos.m_123341_() + 0.5 + (double) vec3i.getX() * 0.5;
        double d10 = (double) railPos.m_123343_() + 0.5 + (double) vec3i.getZ() * 0.5;
        double d12 = (double) railPos.m_123341_() + 0.5 + (double) vec3i1.getX() * 0.5;
        double d13 = (double) railPos.m_123343_() + 0.5 + (double) vec3i1.getZ() * 0.5;
        d4 = d12 - d23;
        d5 = d13 - d10;
        double d14;
        if (d4 == 0.0) {
            d14 = d2 - (double) railPos.m_123343_();
        } else if (d5 == 0.0) {
            d14 = d0 - (double) railPos.m_123341_();
        } else {
            double d15 = d0 - d23;
            double d16 = d2 - d10;
            d14 = (d15 * d4 + d16 * d5) * 2.0;
        }
        d0 = d23 + d4 * d14;
        d2 = d10 + d5 * d14;
        this.m_6034_(d0, d1, d2);
        Vec3 idealPos = new Vec3(this.m_20185_(), (double) ((float) this.magLevBelow.m_123342_() + 1.5F) + Math.sin((double) this.f_19797_ * 0.2) * 0.5, this.m_20189_());
        this.m_20256_(this.m_20184_().add(idealPos.subtract(this.m_20182_()).scale(0.2F)));
        this.moveMinecartOnMagLev(railPos);
        if (vec3i.getY() != 0 && Mth.floor(this.m_20185_()) - railPos.m_123341_() == vec3i.getX() && Mth.floor(this.m_20189_()) - railPos.m_123343_() == vec3i.getZ()) {
            this.m_6034_(this.m_20185_(), this.m_20186_() + (double) vec3i.getY(), this.m_20189_());
        } else if (vec3i1.getY() != 0 && Mth.floor(this.m_20185_()) - railPos.m_123341_() == vec3i1.getX() && Mth.floor(this.m_20189_()) - railPos.m_123343_() == vec3i1.getZ()) {
            this.m_6034_(this.m_20185_(), this.m_20186_() + (double) vec3i1.getY(), this.m_20189_());
        }
        Vec3 vec33 = this.getPos(this.m_20185_(), this.m_20186_(), this.m_20189_());
        if (vec33 != null && vec3 != null) {
            double d17 = (vec3.y - vec33.y) * 0.05;
            Vec3 vec34 = this.m_20184_();
            double d18 = vec34.horizontalDistance();
            if (d18 > 0.0) {
                this.m_20256_(vec34.multiply((d18 + d17) / d18, 0.3F, (d18 + d17) / d18));
            }
        }
        int j = Mth.floor(this.m_20185_());
        int i = Mth.floor(this.m_20189_());
        if (j != railPos.m_123341_() || i != railPos.m_123343_()) {
            Vec3 vec35 = this.m_20184_();
            double d26 = vec35.horizontalDistance();
            this.m_20334_(d26 * (double) (j - railPos.m_123341_()), vec35.y, d26 * (double) (i - railPos.m_123343_()));
        }
        if (doRailFunctions) {
            ((BaseRailBlock) railState.m_60734_()).onMinecartPass(railState, this.m_9236_(), railPos, (AbstractMinecart) this);
        }
        if (flag && doRailFunctions) {
            Vec3 vec36 = this.m_20184_();
            double d27 = vec36.horizontalDistance();
            if (d27 > 0.01) {
                double d19 = 1.0;
                this.m_20256_(vec36.add(vec36.x / d27 * d19, 0.0, vec36.z / d27 * d19));
            } else {
                Vec3 vec37 = this.m_20184_();
                double d20 = vec37.x;
                double d21 = vec37.z;
                if (railshape == RailShape.EAST_WEST) {
                    if (this.isRedstoneConductor(railPos.west())) {
                        d20 = 0.02;
                    } else if (this.isRedstoneConductor(railPos.east())) {
                        d20 = -0.02;
                    }
                } else {
                    if (railshape != RailShape.NORTH_SOUTH) {
                        return;
                    }
                    if (this.isRedstoneConductor(railPos.north())) {
                        d21 = 0.02;
                    } else if (this.isRedstoneConductor(railPos.south())) {
                        d21 = -0.02;
                    }
                }
                this.m_20334_(d20, vec37.y, d21);
            }
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getPos(DDD)Lnet/minecraft/world/phys/Vec3;" }, remap = true, at = { @At("RETURN") }, cancellable = true)
    public void ac_getPos(double x, double y, double z, CallbackInfoReturnable<Vec3> cir) {
        double magLevAmount = (double) (this.prevMagLevProgress + (this.magLevProgress - this.prevMagLevProgress) * AlexsCaves.PROXY.getPartialTicks());
        if (magLevAmount >= 0.0) {
            double yClientSide = this.f_19791_ + (this.m_20186_() - this.f_19791_) * (double) AlexsCaves.PROXY.getPartialTicks();
            Vec3 prev = (Vec3) cir.getReturnValue();
            Vec3 modified = prev == null ? this.m_20318_(AlexsCaves.PROXY.getPartialTicks()) : new Vec3(prev.x, prev.y + (yClientSide - prev.y) * magLevAmount, prev.z);
            cir.setReturnValue(modified);
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;getPosOffs(DDDD)Lnet/minecraft/world/phys/Vec3;" }, remap = true, at = { @At("RETURN") }, cancellable = true)
    public void ac_getPosOffs(double x, double y, double z, double offset, CallbackInfoReturnable<Vec3> cir) {
        double magLevAmount = (double) (this.prevMagLevProgress + (this.magLevProgress - this.prevMagLevProgress) * AlexsCaves.PROXY.getPartialTicks());
        if (magLevAmount >= 0.0) {
            double yClientSide = this.f_19791_ + (this.m_20186_() - this.f_19791_) * (double) AlexsCaves.PROXY.getPartialTicks();
            Vec3 prev = (Vec3) cir.getReturnValue();
            Vec3 modified = prev == null ? this.m_20318_(AlexsCaves.PROXY.getPartialTicks()) : new Vec3(prev.x, prev.y + (yClientSide - prev.y) * magLevAmount, prev.z);
            cir.setReturnValue(modified);
        }
    }

    public void moveMinecartOnMagLev(BlockPos pos) {
        double d24 = this.m_20160_() ? 0.75 : 1.0;
        double d25 = 0.05F;
        if (this.magLevBelow != null) {
            BlockState magLevState = this.m_9236_().getBlockState(this.magLevBelow);
            if (magLevState.m_60734_() instanceof MagneticLevitationRailBlock magRailBlock) {
                d25 = (double) magRailBlock.getRailMaxSpeed(magLevState, this.m_9236_(), pos, (AbstractMinecart) this);
            }
        }
        Vec3 vec3d1 = this.m_20184_();
        this.m_6478_(MoverType.SELF, new Vec3(Mth.clamp(d24 * vec3d1.x, -d25, d25), vec3d1.y, Mth.clamp(d24 * vec3d1.z, -d25, d25)));
    }

    @Override
    public boolean isOnMagLevRail() {
        return this.magLevProgress >= 0.5F;
    }
}