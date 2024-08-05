package com.mna.entities.boss.attacks;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.DamageHelper;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.Odin;
import com.mna.items.ItemInit;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.SummonUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ThrownAllfatherAxe extends ThrownWeapon {

    private static final EntityDataAccessor<Byte> IS_RETURNING = SynchedEntityData.defineId(ThrownAllfatherAxe.class, EntityDataSerializers.BYTE);

    public static final ItemStack renderStack = new ItemStack(ItemInit.ALLFATHER_AXE.get());

    private CompoundTag itemData;

    public ThrownAllfatherAxe(EntityType<? extends AbstractHurtingProjectile> type, Level world) {
        super(type, world);
        this.setRenderStack(renderStack);
        this.setTrailParticle(new MAParticleType(ParticleInit.FROST.get()).setMaxAge(0));
    }

    public ThrownAllfatherAxe(LivingEntity shooter, Level worldIn, CompoundTag data) {
        this(worldIn, shooter.m_20185_(), shooter.m_20188_() - 0.1F, shooter.m_20189_());
        this.m_5602_(shooter);
        this.itemData = data;
    }

    public ThrownAllfatherAxe(Level worldIn, double x, double y, double z) {
        super(EntityInit.ALLFATHER_AXE.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20242_(true);
        this.setRenderStack(renderStack);
        this.setTrailParticle(new MAParticleType(ParticleInit.FROST.get()).setMaxAge(0));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 Vector3d = new Vec3(x, y, z).normalize().scale((double) velocity);
        this.m_20256_(Vector3d);
        double f = Vector3d.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(Vector3d.x, Vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(Vector3d.y, f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    @Override
    public void push(Entity pEntity) {
        if (pEntity == this.m_19749_()) {
            this.handleHitOwner((LivingEntity) pEntity);
        }
        super.m_7334_(pEntity);
    }

    @Override
    protected void onHitEntity(EntityHitResult ray) {
        Entity hitEntity = ray.getEntity();
        if (this.isValidEntity(hitEntity)) {
            LivingEntity livingHit = (LivingEntity) hitEntity;
            Entity eOwner = this.m_19749_();
            if (eOwner instanceof LivingEntity) {
                if (livingHit == eOwner) {
                    this.handleHitOwner(livingHit);
                } else {
                    livingHit.hurt(DamageHelper.createSourcedType(DamageHelper.FROST, this.m_9236_().registryAccess(), this.m_19749_()), 15.0F);
                    livingHit.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 100));
                }
            }
        }
    }

    private boolean handleHitOwner(LivingEntity livingHit) {
        if (!this.m_9236_().isClientSide() && this.isReturning()) {
            ItemStack returnStack = new ItemStack(ItemInit.ALLFATHER_AXE.get());
            returnStack.setTag(this.itemData);
            if (livingHit instanceof Player) {
                InventoryUtilities.removeItemFromInventory(new ItemStack(ItemInit.ALLFATHER_AXE_CONTROL.get()), true, false, new InvWrapper(((Player) livingHit).getInventory()));
                if (((Player) livingHit).addItem(returnStack)) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                } else {
                    this.drop();
                }
            } else if (livingHit instanceof Odin) {
                ((Odin) livingHit).showAxe();
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult ray) {
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        BlockState blockstate = this.m_9236_().getBlockState(this.m_20183_());
        if (blockstate.m_60734_() == Blocks.WATER) {
            this.m_9236_().setBlock(this.m_20183_(), Blocks.ICE.defaultBlockState(), 3);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(IS_RETURNING, (byte) 0);
    }

    @Override
    public void tick() {
        Entity owner = this.m_19749_();
        if (owner != null && owner.isAlive()) {
            int returnTicks = owner instanceof Player ? 40 : 20;
            if (this.f_19797_ >= returnTicks && !this.isReturning()) {
                this.setReturning();
            }
            if (this.isReturning()) {
                this.m_20256_(owner.position().add(0.0, 1.0, 0.0).subtract(this.m_20182_()).normalize());
            }
            if (this.m_9236_().isClientSide()) {
                Vec3 dm = this.m_20184_();
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.FROST.get()).setScale(0.05F).setGravity(0.01F).setPhysics(true), this.m_20185_() + Math.random() * dm.x, this.m_20186_() + Math.random() * dm.y, this.m_20189_() + Math.random() * dm.z, 0.0, 0.0, 0.0);
            }
            if (this.f_19803_) {
                this.m_5496_(SFX.Spell.Cast.ICE, 0.05F, 1.0F);
                this.m_5496_(SFX.Entity.SkeletonAssassin.BOLO_THROW, 0.75F, 0.5F);
            }
            super.tick();
        } else {
            this.drop();
        }
    }

    @Override
    protected float getInertia() {
        return 0.95F;
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    private void drop() {
        if (!this.m_9236_().isClientSide()) {
            ItemEntity ie = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), new ItemStack(ItemInit.ALLFATHER_AXE.get()));
            ie.m_20256_(this.m_20184_());
            this.m_9236_().m_7967_(ie);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public boolean isReturning() {
        return this.f_19804_.get(IS_RETURNING) == 1;
    }

    public void setReturning() {
        this.f_19804_.set(IS_RETURNING, (byte) 1);
        this.f_36813_ = -this.f_36813_;
        this.f_36814_ = -this.f_36814_;
        this.f_36815_ = -this.f_36815_;
    }

    private boolean isValidEntity(Entity e) {
        if (!(e instanceof LivingEntity)) {
            return false;
        } else {
            Entity shooter = this.m_19749_();
            if (shooter == null) {
                return false;
            } else if (!(shooter instanceof Player player)) {
                return true;
            } else if (SummonUtils.isSummon(e) && SummonUtils.getSummoner((LivingEntity) e) == player) {
                return false;
            } else {
                if (e instanceof IFactionEnemy) {
                    LazyOptional<IPlayerProgression> op = player.getCapability(PlayerProgressionProvider.PROGRESSION);
                    if (op.isPresent() && ((IFactionEnemy) e).getFaction() == ((IPlayerProgression) op.resolve().get()).getAlliedFaction()) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        super.writeSpawnData(buffer);
        if (this.m_19749_() != null) {
            buffer.writeInt(this.m_19749_().getId());
        } else {
            buffer.writeInt(-1);
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        super.readSpawnData(additionalData);
        int ownerID = additionalData.readInt();
        if (ownerID > -1) {
            Entity owner = this.m_9236_().getEntity(ownerID);
            this.m_5602_(owner);
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean save(CompoundTag nbt) {
        if (this.itemData != null) {
            nbt.put("itemData", this.itemData);
        }
        return super.m_20223_(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.m_20258_(nbt);
        if (nbt.contains("itemData")) {
            this.itemData = nbt.getCompound("itemData");
        }
    }
}