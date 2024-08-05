package noppes.npcs.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.CustomEntities;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.event.ProjectileEvent;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.data.DataRanged;

public class EntityProjectile extends ThrowableProjectile {

    private static final EntityDataAccessor<Boolean> Gravity = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> Arrow = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> Is3d = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> Glows = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> Rotating = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> Sticks = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<ItemStack> ItemStackThrown = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Integer> Velocity = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> Size = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> Particle = SynchedEntityData.defineId(EntityProjectile.class, EntityDataSerializers.INT);

    private BlockPos tilePos = BlockPos.ZERO;

    private BlockState inBlock;

    protected boolean inGround = false;

    public int throwableShake = 0;

    public int arrowShake = 0;

    public boolean canBePickedUp = false;

    public boolean destroyedOnEntityHit = true;

    private Entity thrower;

    private EntityNPCInterface npc;

    private String throwerName = null;

    private int ticksInGround;

    public int ticksInAir = 0;

    private double accelerationX;

    private double accelerationY;

    private double accelerationZ;

    public float damage = 5.0F;

    public int punch = 0;

    public boolean accelerate = false;

    public boolean explosiveDamage = true;

    public int explosiveRadius = 0;

    public int effect = 0;

    public int duration = 5;

    public int amplify = 0;

    public int accuracy = 60;

    public EntityProjectile.IProjectileCallback callback;

    public List<ScriptContainer> scripts = new ArrayList();

    public EntityProjectile(EntityType type, Level par1Level) {
        super(type, par1Level);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(ItemStackThrown, ItemStack.EMPTY);
        this.f_19804_.define(Velocity, 10);
        this.f_19804_.define(Size, 10);
        this.f_19804_.define(Particle, 0);
        this.f_19804_.define(Gravity, false);
        this.f_19804_.define(Glows, false);
        this.f_19804_.define(Arrow, false);
        this.f_19804_.define(Is3d, false);
        this.f_19804_.define(Rotating, false);
        this.f_19804_.define(Sticks, false);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRenderAtSqrDistance(double par1) {
        double d1 = this.m_20191_().getSize() * 4.0;
        d1 *= 64.0;
        return par1 < d1 * d1;
    }

    public EntityProjectile(Level level, LivingEntity limbSwingAmountEntityLiving, ItemStack item, boolean isNPC) {
        super(CustomEntities.entityProjectile, level);
        this.thrower = limbSwingAmountEntityLiving;
        if (this.thrower != null) {
            this.throwerName = this.thrower.getUUID().toString();
        }
        this.setThrownItem(item);
        this.f_19804_.set(Arrow, this.getItem() == Items.ARROW);
        this.m_7678_(limbSwingAmountEntityLiving.m_20185_(), limbSwingAmountEntityLiving.m_20186_() + (double) limbSwingAmountEntityLiving.m_20192_(), limbSwingAmountEntityLiving.m_20189_(), limbSwingAmountEntityLiving.m_146908_(), limbSwingAmountEntityLiving.m_146909_());
        double posX = this.m_20185_() - (double) (Mth.cos(this.m_146908_() / 180.0F * (float) Math.PI) * 0.1F);
        double posY = this.m_20186_() - 0.1F;
        double posZ = this.m_20189_() - (double) (Mth.sin(this.m_146908_() / 180.0F * (float) Math.PI) * 0.1F);
        this.m_6034_(posX, posY, posZ);
        if (isNPC) {
            this.npc = (EntityNPCInterface) this.thrower;
            this.getStatProperties(this.npc.stats.ranged);
            this.m_6210_();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> para) {
        if (Size.equals(para)) {
            this.m_6210_();
        }
    }

    public void setThrownItem(ItemStack item) {
        this.f_19804_.set(ItemStackThrown, item);
    }

    public int getSize() {
        return this.f_19804_.get(Size);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return new EntityDimensions((float) this.getSize() / 10.0F, (float) this.getSize() / 10.0F, false);
    }

    @Override
    public void shoot(double par1, double par3, double par5, float par7, float par8) {
        double f2 = Math.sqrt(par1 * par1 + par3 * par3 + par5 * par5);
        double f3 = Math.sqrt(par1 * par1 + par5 * par5);
        float yaw = (float) (Math.atan2(par1, par5) * 180.0 / Math.PI);
        float pitch = this.hasGravity() ? par7 : (float) (Math.atan2(par3, f3) * 180.0 / Math.PI);
        this.f_19859_ = yaw;
        this.f_19860_ = pitch;
        this.m_146922_(yaw);
        this.m_146926_(pitch);
        Vec3 m = new Vec3((double) (Mth.sin(yaw / 180.0F * (float) Math.PI) * Mth.cos(pitch / 180.0F * (float) Math.PI)), (double) Mth.sin((pitch + 1.0F) / 180.0F * (float) Math.PI), (double) (Mth.cos(yaw / 180.0F * (float) Math.PI) * Mth.cos(pitch / 180.0F * (float) Math.PI))).add(this.f_19796_.nextGaussian() * 0.0075 * (double) par8, this.f_19796_.nextGaussian() * 0.0075 * (double) par8, this.f_19796_.nextGaussian() * 0.0075 * (double) par8).scale((double) this.getSpeed());
        this.m_20256_(m);
        this.accelerationX = par1 / f2 * 0.1;
        this.accelerationY = par3 / f2 * 0.1;
        this.accelerationZ = par5 / f2 * 0.1;
        this.ticksInGround = 0;
    }

    public float getAngleForXYZ(double varX, double varY, double varZ, float horiDist, boolean arc) {
        float g = this.m_7139_();
        float var1x = this.getSpeed() * this.getSpeed();
        float var2 = g * horiDist;
        float var3x = (float) ((double) (g * horiDist * horiDist) + 2.0 * varY * (double) var1x);
        float var4 = var1x * var1x - g * var3x;
        if (var4 < 0.0F) {
            return 30.0F;
        } else {
            float var6 = arc ? var1x + Mth.sqrt(var4) : var1x - Mth.sqrt(var4);
            return (float) (Math.atan2((double) var6, (double) var2) * 180.0 / Math.PI);
        }
    }

    public void shoot(float speed) {
        double varX = (double) (-Mth.sin(this.m_146908_() / 180.0F * (float) Math.PI) * Mth.cos(this.m_146909_() / 180.0F * (float) Math.PI));
        double varZ = (double) (Mth.cos(this.m_146908_() / 180.0F * (float) Math.PI) * Mth.cos(this.m_146909_() / 180.0F * (float) Math.PI));
        double varY = (double) (-Mth.sin(this.m_146909_() / 180.0F * (float) Math.PI));
        this.shoot(varX, varY, varZ, -this.m_146909_(), speed);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double par1, double par3, double par5, float par7, float par8, int par9, boolean bo) {
        if (!this.m_9236_().isClientSide || !this.inGround) {
            this.m_6034_(par1, par3, par5);
            this.m_19915_(par7, par8);
        }
    }

    @Override
    public void tick() {
        super.m_6075_();
        if (++this.f_19797_ % 10 == 0) {
            EventHooks.onProjectileTick(this);
        }
        Vec3 motion = this.m_20184_();
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            double f = motion.horizontalDistance();
            this.m_146922_((float) (Mth.atan2(motion.x, motion.z) * 180.0F / (float) Math.PI));
            this.m_146926_((float) (Mth.atan2(motion.y, f) * 180.0F / (float) Math.PI));
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
        }
        if (this.effect == 666 && !this.inGround) {
        }
        BlockState state = this.m_9236_().getBlockState(this.tilePos);
        if ((this.isArrow() || this.sticksToWalls()) && this.tilePos != BlockPos.ZERO) {
            VoxelShape shape = state.m_60808_(this.m_9236_(), this.tilePos);
            if (!shape.isEmpty()) {
                AABB axisalignedbb = shape.bounds();
                if (axisalignedbb != null && axisalignedbb.contains(this.m_20182_())) {
                    this.inGround = true;
                }
            }
        }
        if (this.arrowShake > 0) {
            this.arrowShake--;
        }
        if (this.inGround) {
            if (state == this.inBlock) {
                this.ticksInGround++;
                if (this.ticksInGround == 1200) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            } else {
                this.inGround = false;
                this.m_20256_(this.m_20184_().multiply((double) (this.f_19796_.nextFloat() * 0.2F), (double) (this.f_19796_.nextFloat() * 0.2F), (double) (this.f_19796_.nextFloat() * 0.2F)));
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            this.ticksInAir++;
            if (this.ticksInAir == 1200) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            Vec3 pos = this.m_20182_();
            Vec3 nextpos = pos.add(motion);
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHit);
            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
                this.f_19804_.set(Rotating, false);
                this.onHit(hitresult);
            }
            motion = this.m_20184_();
            double f1 = motion.horizontalDistance();
            this.m_146926_(m_37273_(this.f_19860_, (float) (Mth.atan2(motion.y, f1) * 180.0F / (float) Math.PI)));
            this.m_146922_(m_37273_(this.f_19859_, (float) (Mth.atan2(motion.x, motion.z) * 180.0F / (float) Math.PI)));
            if (this.isRotating()) {
                int spin = this.isBlock() ? 10 : 20;
                this.m_146926_(this.m_146909_() - (float) spin * this.getSpeed());
            }
            float f2 = this.getMotionFactor();
            float f3 = this.m_7139_();
            if (this.m_20069_()) {
                for (int j = 0; j < 4; j++) {
                    float f4 = 0.25F;
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE, nextpos.x - motion.x * 0.25, nextpos.y - motion.y * 0.25, nextpos.z - motion.z * 0.25, motion.x, motion.y, motion.z);
                }
                f2 = 0.6F;
            }
            motion = motion.scale((double) f2);
            if (this.hasGravity()) {
                motion = motion.subtract(0.0, (double) f3, 0.0);
            }
            if (this.accelerate) {
                motion = motion.add(this.accelerationX, this.accelerationY, this.accelerationZ);
            }
            if (this.m_9236_().isClientSide && this.f_19804_.get(Particle) > 0) {
                this.m_9236_().addParticle(ParticleType.getMCType(this.f_19804_.get(Particle)), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
            this.m_20256_(motion);
            this.m_6034_(nextpos.x, nextpos.y, nextpos.z);
            this.m_20101_();
        }
    }

    protected boolean canHit(Entity entity) {
        if (super.m_5603_(entity) && entity != this.thrower && (this.npc == null || entity != this.npc && !this.npc.isAlliedTo(entity))) {
            if (entity instanceof Player entityplayer && (entityplayer.getAbilities().invulnerable || this.thrower instanceof Player && !((Player) this.thrower).canHarmPlayer(entityplayer))) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isBlock() {
        ItemStack item = this.getItemDisplay();
        return item.isEmpty() ? false : item.getItem() instanceof BlockItem;
    }

    private Item getItem() {
        ItemStack item = this.getItemDisplay();
        return item.isEmpty() ? Items.AIR : item.getItem();
    }

    protected float getMotionFactor() {
        return this.accelerate ? 0.95F : 1.0F;
    }

    @Override
    protected void onHit(HitResult movingobjectposition) {
        if (!this.m_9236_().isClientSide) {
            BlockPos pos = BlockPos.ZERO;
            Entity e = null;
            ProjectileEvent.ImpactEvent event;
            if (movingobjectposition.getType() == HitResult.Type.ENTITY) {
                e = ((EntityHitResult) movingobjectposition).getEntity();
                pos = e.blockPosition();
                event = new ProjectileEvent.ImpactEvent((IProjectile) NpcAPI.Instance().getIEntity(this), 0, e);
            } else {
                pos = ((BlockHitResult) movingobjectposition).getBlockPos();
                BlockState state = this.m_9236_().getBlockState(pos);
                event = new ProjectileEvent.ImpactEvent((IProjectile) NpcAPI.Instance().getIEntity(this), 1, NpcAPI.Instance().getIBlock(this.m_9236_(), pos));
            }
            if (pos == BlockPos.ZERO) {
                pos = new BlockPos((int) movingobjectposition.getLocation().x, (int) movingobjectposition.getLocation().y, (int) movingobjectposition.getLocation().z);
            }
            if (this.callback != null && this.callback.onImpact(this, pos, e)) {
                return;
            }
            EventHooks.onProjectileImpact(this, event);
        }
        if (movingobjectposition.getType() == HitResult.Type.ENTITY) {
            Entity ex = ((EntityHitResult) movingobjectposition).getEntity();
            float damage = this.damage;
            if (damage == 0.0F) {
                damage = 0.001F;
            }
            if (ex.hurt(this.m_269291_().thrown(this, this.getOwner()), damage)) {
                if (ex instanceof LivingEntity entityliving) {
                    if (!this.m_9236_().isClientSide && (this.isArrow() || this.sticksToWalls())) {
                        entityliving.setArrowCount(entityliving.getArrowCount() + 1);
                    }
                    if (this.destroyedOnEntityHit && !(ex instanceof EnderMan)) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                    if (this.effect != 0) {
                        if (this.effect != 666) {
                            MobEffect p = PotionEffectType.getMCType(this.effect);
                            entityliving.addEffect(new MobEffectInstance(p, this.duration * 20, this.amplify));
                        } else {
                            entityliving.m_7311_(this.duration * 20);
                        }
                    }
                }
                if (this.isBlock()) {
                    this.m_9236_().m_5898_((Player) null, 2001, ex.blockPosition(), Block.getId(((BlockItem) this.getItem()).getBlock().defaultBlockState()));
                } else if (!this.isArrow() && !this.sticksToWalls()) {
                    for (int i = 0; i < 8; i++) {
                        this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemDisplay()), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.15, this.f_19796_.nextGaussian() * 0.2, this.f_19796_.nextGaussian() * 0.15);
                    }
                }
                if (this.punch > 0) {
                    Vec3 m = this.m_20184_();
                    double f3 = m.horizontalDistance();
                    if (f3 > 0.0) {
                        ex.push(m.x() * (double) this.punch * 0.6 / f3, 0.1, m.z() * (double) this.punch * 0.6 / f3);
                    }
                }
            } else if (this.hasGravity() && (this.isArrow() || this.sticksToWalls())) {
                this.m_20256_(this.m_20184_().scale(-0.1));
                this.m_146922_(this.m_146908_() + 180.0F);
                this.f_19859_ += 180.0F;
                this.ticksInAir = 0;
            }
        } else if (this.isArrow() || this.sticksToWalls()) {
            this.tilePos = ((BlockHitResult) movingobjectposition).getBlockPos();
            this.inBlock = this.m_9236_().getBlockState(this.tilePos);
            Vec3 m = movingobjectposition.getLocation().subtract(this.m_20182_());
            this.m_20256_(m);
            Vec3 vector3d1 = m.normalize().scale(0.05F);
            this.m_20343_(this.m_20185_() - vector3d1.x, this.m_20186_() - vector3d1.y, this.m_20189_() - vector3d1.z);
            this.inGround = true;
            this.arrowShake = 7;
            if (!this.hasGravity()) {
                this.f_19804_.set(Gravity, true);
            }
            if (this.inBlock != null) {
                this.inBlock.m_60682_(this.m_9236_(), this.tilePos, this);
            }
        } else if (this.isBlock()) {
            this.m_9236_().m_5898_((Player) null, 2001, this.m_20183_(), Block.getId(((BlockItem) this.getItem()).getBlock().defaultBlockState()));
        } else {
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemDisplay()), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.f_19796_.nextGaussian() * 0.15, this.f_19796_.nextGaussian() * 0.2, this.f_19796_.nextGaussian() * 0.15);
            }
        }
        if (this.explosiveRadius > 0) {
            boolean terraindamage = this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.explosiveDamage;
            this.m_9236_().explode((Entity) (this.getOwner() == null ? this : this.getOwner()), this.m_20185_(), this.m_20186_(), this.m_20189_(), (float) this.explosiveRadius, this.effect == 666, terraindamage ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
            if (this.effect != 0) {
                AABB axisalignedbb = this.m_20191_().inflate((double) (this.explosiveRadius * 2), (double) (this.explosiveRadius * 2), (double) (this.explosiveRadius * 2));
                List<LivingEntity> list1 = this.m_9236_().m_45976_(LivingEntity.class, axisalignedbb);
                MobEffect p = PotionEffectType.getMCType(this.effect);
                for (LivingEntity entity : list1) {
                    if (this.effect != 666) {
                        entity.addEffect(new MobEffectInstance(p, this.duration * 20, this.amplify));
                    } else {
                        entity.m_7311_(this.duration * 20);
                    }
                }
                this.m_9236_().m_5898_((Player) null, 2002, this.m_20183_(), this.getPotionColor(this.effect));
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (!this.m_9236_().isClientSide && !this.isArrow() && !this.sticksToWalls()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    private void blockParticles() {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag par1CompoundTag) {
        par1CompoundTag.putShort("xTile", (short) this.tilePos.m_123341_());
        par1CompoundTag.putShort("yTile", (short) this.tilePos.m_123342_());
        par1CompoundTag.putShort("zTile", (short) this.tilePos.m_123343_());
        if (this.inBlock != null) {
            par1CompoundTag.put("inBlockState", NbtUtils.writeBlockState(this.inBlock));
        }
        par1CompoundTag.putByte("shake", (byte) this.throwableShake);
        par1CompoundTag.putBoolean("inGround", this.inGround);
        par1CompoundTag.putBoolean("isArrow", this.isArrow());
        Vec3 m = this.m_20184_();
        par1CompoundTag.put("direction", this.m_20063_(new double[] { m.x, m.y, m.z }));
        par1CompoundTag.putBoolean("canBePickedUp", this.canBePickedUp);
        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof Player) {
            this.throwerName = this.thrower.getUUID().toString();
        }
        par1CompoundTag.putString("ownerName", this.throwerName == null ? "" : this.throwerName);
        par1CompoundTag.put("Item", this.getItemDisplay().save(new CompoundTag()));
        par1CompoundTag.putFloat("damagev2", this.damage);
        par1CompoundTag.putInt("punch", this.punch);
        par1CompoundTag.putInt("size", this.f_19804_.get(Size));
        par1CompoundTag.putInt("velocity", this.f_19804_.get(Velocity));
        par1CompoundTag.putInt("explosiveRadius", this.explosiveRadius);
        par1CompoundTag.putInt("effectDuration", this.duration);
        par1CompoundTag.putBoolean("gravity", this.hasGravity());
        par1CompoundTag.putBoolean("accelerate", this.accelerate);
        par1CompoundTag.putBoolean("glows", this.f_19804_.get(Glows));
        par1CompoundTag.putInt("PotionEffect", this.effect);
        par1CompoundTag.putInt("trailenum", this.f_19804_.get(Particle));
        par1CompoundTag.putBoolean("Render3D", this.f_19804_.get(Is3d));
        par1CompoundTag.putBoolean("Spins", this.f_19804_.get(Rotating));
        par1CompoundTag.putBoolean("Sticks", this.f_19804_.get(Sticks));
        par1CompoundTag.putInt("accuracy", this.accuracy);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.tilePos = new BlockPos(compound.getShort("xTile"), compound.getShort("yTile"), compound.getShort("zTile"));
        if (compound.contains("inBlockState", 10)) {
            this.inBlock = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compound.getCompound("inBlockState"));
        }
        this.throwableShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;
        this.f_19804_.set(Arrow, compound.getBoolean("isArrow"));
        this.throwerName = compound.getString("ownerName");
        this.canBePickedUp = compound.getBoolean("canBePickedUp");
        this.damage = compound.getFloat("damagev2");
        this.punch = compound.getInt("punch");
        this.explosiveRadius = compound.getInt("explosiveRadius");
        this.duration = compound.getInt("effectDuration");
        this.accelerate = compound.getBoolean("accelerate");
        this.effect = compound.getInt("PotionEffect");
        this.accuracy = compound.getInt("accuracy");
        this.f_19804_.set(Particle, compound.getInt("trailenum"));
        this.f_19804_.set(Size, compound.getInt("size"));
        this.f_19804_.set(Glows, compound.getBoolean("glows"));
        this.f_19804_.set(Velocity, compound.getInt("velocity"));
        this.f_19804_.set(Gravity, compound.getBoolean("gravity"));
        this.f_19804_.set(Is3d, compound.getBoolean("Render3D"));
        this.f_19804_.set(Rotating, compound.getBoolean("Spins"));
        this.f_19804_.set(Sticks, compound.getBoolean("Sticks"));
        if (this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }
        if (compound.contains("direction")) {
            ListTag nbttaglist = compound.getList("direction", 6);
            this.m_20256_(new Vec3(nbttaglist.getDouble(0), nbttaglist.getDouble(1), nbttaglist.getDouble(2)));
        }
        CompoundTag var2 = compound.getCompound("Item");
        ItemStack item = ItemStack.of(var2);
        if (item.isEmpty()) {
            this.m_146870_();
        } else {
            this.f_19804_.set(ItemStackThrown, item);
        }
    }

    @Override
    public Entity getOwner() {
        if (this.throwerName != null && !this.throwerName.isEmpty()) {
            try {
                UUID uuid = UUID.fromString(this.throwerName);
                if (this.thrower == null && uuid != null) {
                    this.thrower = this.m_9236_().m_46003_(uuid);
                }
            } catch (IllegalArgumentException var2) {
            }
            return this.thrower;
        } else {
            return null;
        }
    }

    private int getPotionColor(int p) {
        switch(p) {
            case 2:
                return 32698;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16:
            default:
                return 0;
            case 9:
                return 32732;
            case 15:
                return 15;
            case 17:
                return 32660;
            case 18:
                return 32696;
            case 19:
                return 32660;
            case 20:
                return 32732;
        }
    }

    public void getStatProperties(DataRanged stats) {
        this.damage = (float) stats.getStrength();
        this.punch = stats.getKnockback();
        this.accelerate = stats.getAccelerate();
        this.explosiveRadius = stats.getExplodeSize();
        this.effect = stats.getEffectType();
        this.duration = stats.getEffectTime();
        this.amplify = stats.getEffectStrength();
        this.setParticleEffect(stats.getParticle());
        this.f_19804_.set(Size, stats.getSize());
        this.f_19804_.set(Glows, stats.getGlows());
        this.setSpeed(stats.getSpeed());
        this.setHasGravity(stats.getHasGravity());
        this.setIs3D(stats.getRender3D());
        this.setRotating(stats.getSpins());
        this.setStickInWall(stats.getSticks());
    }

    public void setParticleEffect(int type) {
        this.f_19804_.set(Particle, type);
    }

    public void setHasGravity(boolean bo) {
        this.f_19804_.set(Gravity, bo);
    }

    public void setIs3D(boolean bo) {
        this.f_19804_.set(Is3d, bo);
    }

    public void setStickInWall(boolean bo) {
        this.f_19804_.set(Sticks, bo);
    }

    public ItemStack getItemDisplay() {
        return this.f_19804_.get(ItemStackThrown);
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return this.f_19804_.get(Glows) ? 1.0F : super.m_213856_();
    }

    public boolean hasGravity() {
        return this.f_19804_.get(Gravity);
    }

    public void setSpeed(int speed) {
        this.f_19804_.set(Velocity, speed);
    }

    public float getSpeed() {
        return (float) this.f_19804_.get(Velocity).intValue() / 10.0F;
    }

    public boolean isArrow() {
        return this.f_19804_.get(Arrow);
    }

    public void setRotating(boolean bo) {
        this.f_19804_.set(Rotating, bo);
    }

    public boolean isRotating() {
        return this.f_19804_.get(Rotating);
    }

    public boolean glows() {
        return this.f_19804_.get(Glows);
    }

    public boolean is3D() {
        return this.f_19804_.get(Is3d) || this.isBlock();
    }

    public boolean sticksToWalls() {
        return this.is3D() && this.f_19804_.get(Sticks);
    }

    @Override
    public void playerTouch(Player par1Player) {
        if (!this.m_9236_().isClientSide && this.canBePickedUp && this.inGround && this.arrowShake <= 0) {
            if (par1Player.getInventory().add(this.getItemDisplay())) {
                this.inGround = false;
                this.m_5496_(SoundEvents.ITEM_PICKUP, 0.2F, ((this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1Player.m_7938_(this, 1);
                this.m_146870_();
            }
        }
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public Component getDisplayName() {
        return !this.getItemDisplay().isEmpty() ? this.getItemDisplay().getDisplayName() : super.m_5446_();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    public interface IProjectileCallback {

        boolean onImpact(EntityProjectile var1, BlockPos var2, Entity var3);
    }
}