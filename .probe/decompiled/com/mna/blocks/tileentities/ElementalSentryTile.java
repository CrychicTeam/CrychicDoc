package com.mna.blocks.tileentities;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.blocks.tile.OwnerInformation;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.blocks.artifice.ElementalSentryBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.entities.projectile.SentryProjectile;
import com.mna.tools.SummonUtils;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

public class ElementalSentryTile extends BlockEntity implements IForgeBlockEntity, IEldrinConsumerTile {

    private static final int TARGET_TIME = 100;

    private static final float POWER_PER_SHOT = 20.0F;

    private LivingEntity target = null;

    private int findTargetCounter = 0;

    private int shootCounter = 0;

    private int warmupCounter = 0;

    public float nextPageTurningSpeed;

    public float pageTurningSpeed;

    public float nextPageAngle;

    public float pageAngle;

    public float flip;

    public float oFlip;

    public float flipT;

    public float flipA;

    public float yaw;

    public int ticks;

    public int ticksSinceTargetChange;

    private static final Random random = new Random();

    private static float radius = 0.5F;

    private static float yOffset = 1.3F;

    private OwnerInformation ownerInfo;

    private Affinity selectedAffinity;

    private float accruedPower = 0.0F;

    public ElementalSentryTile(BlockPos pos, BlockState state, Affinity shootAffinity) {
        super(TileEntityInit.ELEMENTAL_SENTRY.get(), pos, state);
        this.selectedAffinity = shootAffinity;
        this.ownerInfo = new OwnerInformation();
    }

    public ElementalSentryTile(BlockPos pos, BlockState state) {
        this(pos, state, Affinity.UNKNOWN);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ElementalSentryTile blockEntity) {
        if (!blockEntity.hasTarget()) {
            blockEntity.getTarget();
        } else {
            blockEntity.shoot();
        }
        if (level.isClientSide()) {
            blockEntity.updatePageTurning();
            blockEntity.spawnParticles();
        }
    }

    private void updatePageTurning() {
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        this.pageAngle = this.nextPageAngle;
        if (this.target != null) {
            double d0 = this.target.m_20185_() - ((double) this.f_58858_.m_123341_() + 0.5);
            double d1 = this.target.m_20189_() - ((double) this.f_58858_.m_123343_() + 0.5);
            this.yaw = (float) Mth.atan2(d1, d0);
            this.nextPageTurningSpeed += 0.1F;
            if (this.nextPageTurningSpeed < 0.5F || random.nextInt(40) == 0) {
                float f1 = this.flipT;
                do {
                    this.flipT = this.flipT + (float) (random.nextInt(4) - random.nextInt(4));
                } while (f1 == this.flipT);
            }
        } else {
            this.yaw += 0.02F;
            this.nextPageTurningSpeed -= 0.1F;
        }
        while (this.nextPageAngle >= (float) Math.PI) {
            this.nextPageAngle -= (float) (Math.PI * 2);
        }
        while (this.nextPageAngle < (float) -Math.PI) {
            this.nextPageAngle += (float) (Math.PI * 2);
        }
        while (this.yaw >= (float) Math.PI) {
            this.yaw -= (float) (Math.PI * 2);
        }
        while (this.yaw < (float) -Math.PI) {
            this.yaw += (float) (Math.PI * 2);
        }
        float f2 = this.yaw - this.nextPageAngle;
        while (f2 >= (float) Math.PI) {
            f2 -= (float) (Math.PI * 2);
        }
        while (f2 < (float) -Math.PI) {
            f2 += (float) (Math.PI * 2);
        }
        this.nextPageAngle += f2 * 0.4F;
        this.nextPageTurningSpeed = Mth.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        this.ticks++;
        this.ticksSinceTargetChange++;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.flipA = this.flipA + (f - this.flipA) * 0.9F;
        this.flip = this.flip + this.flipA;
    }

    private Vec3 getPointInFront() {
        if (this.target == null) {
            return Vec3.ZERO;
        } else {
            Vec3 pos = new Vec3((double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + yOffset), (double) ((float) this.m_58899_().m_123343_() + 0.5F));
            Vec3 targetPos = this.target.m_20182_();
            Vec3 delta = targetPos.subtract(pos).normalize().scale(0.5);
            return pos.add(delta);
        }
    }

    private void spawnParticles() {
        if ((Boolean) this.m_58900_().m_61143_(ElementalSentryBlock.SHOOTING)) {
            int numParticles = 5;
            Vec3 pos = this.getPointInFront();
            MAParticleType particle = null;
            particle = switch(this.selectedAffinity) {
                case EARTH ->
                    new MAParticleType(ParticleInit.DUST_LERP.get());
                case ENDER ->
                    new MAParticleType(ParticleInit.ENDER.get());
                case FIRE, LIGHTNING, HELLFIRE ->
                    new MAParticleType(ParticleInit.FLAME_LERP.get());
                case WATER, ICE ->
                    new MAParticleType(ParticleInit.WATER_LERP.get());
                case WIND ->
                    new MAParticleType(ParticleInit.AIR_LERP.get());
                default ->
                    new MAParticleType(ParticleInit.ARCANE_LERP.get());
            };
            for (int i = 0; i < numParticles; i++) {
                Vec3 offset = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().scale((double) radius);
                this.m_58904_().addParticle(particle, pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, pos.x, pos.y, pos.z);
            }
        }
    }

    public Affinity getAffinity() {
        return this.selectedAffinity;
    }

    public void setOwner(Player owner) {
        this.ownerInfo.setOwner(owner);
    }

    public boolean hasTarget() {
        if (this.target != null) {
            if (this.target.isAlive() && !(this.target.m_20238_(new Vec3((double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_())) > 2304.0)) {
                return true;
            } else {
                this.clearTarget();
                this.getTarget(true);
                return false;
            }
        } else {
            return false;
        }
    }

    private void getTarget() {
        this.getTarget(false);
    }

    private void getTarget(boolean force) {
        if (!(Boolean) this.m_58900_().m_61143_(ElementalSentryBlock.TARGET_OVERRIDE)) {
            this.findTargetCounter++;
            if (this.findTargetCounter >= 100 || force) {
                this.findTargetCounter = 0;
                Vec3 myPos = new Vec3((double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_());
                List<LivingEntity> potentialTargets = this.m_58904_().m_6443_(LivingEntity.class, new AABB(this.m_58899_()).inflate(32.0), e -> {
                    Vec3 theirpos = new Vec3(e.m_20185_(), e.m_20188_(), e.m_20189_());
                    boolean subPredicate = e instanceof Enemy || e instanceof FlyingMob;
                    if (SummonUtils.isSummon(e)) {
                        LivingEntity summoner = SummonUtils.getSummoner(e);
                        if (summoner != null && this.ownerInfo.isFriendlyTo(summoner)) {
                            subPredicate = false;
                        }
                    }
                    return e.isAlive() && subPredicate && this.m_58904_().m_45547_(new ClipContext(myPos, theirpos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null)).getType() == HitResult.Type.MISS;
                });
                if (potentialTargets.size() > 0) {
                    potentialTargets.sort((o1, o2) -> {
                        Double o1Dist = o1.m_20238_(myPos);
                        Double o2Dist = o2.m_20238_(myPos);
                        return o1Dist.compareTo(o2Dist);
                    });
                    this.target = (LivingEntity) potentialTargets.get(0);
                    this.ticksSinceTargetChange = 0;
                }
            }
        }
    }

    public void forceTarget(LivingEntity target) {
        if (!this.m_58904_().isClientSide() && !(Boolean) this.m_58900_().m_61143_(ElementalSentryBlock.TARGET_OVERRIDE)) {
            this.m_58904_().setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(ElementalSentryBlock.TARGET_OVERRIDE, true), 3);
        }
        if (this.target != target) {
            this.target = target;
            this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(ElementalSentryBlock.SHOOTING, false));
        }
    }

    private void clearTarget() {
        this.target = null;
        this.ticksSinceTargetChange = 0;
        this.warmupCounter = 0;
        this.shootCounter = 0;
        this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(ElementalSentryBlock.SHOOTING, false));
    }

    private int getPowerupTime() {
        switch(this.selectedAffinity.getShiftAffinity()) {
            case EARTH:
            case FIRE:
                return 15;
            case ENDER:
            case LIGHTNING:
            case HELLFIRE:
            case WATER:
            case ICE:
            case ARCANE:
            default:
                return 60;
            case WIND:
                return 20;
        }
    }

    private int getShootInterval() {
        switch(this.selectedAffinity.getShiftAffinity()) {
            case EARTH:
            case FIRE:
                return 5;
            case ENDER:
            case LIGHTNING:
            case HELLFIRE:
            case WATER:
            case ICE:
            case ARCANE:
            default:
                return 60;
            case WIND:
                return 40;
        }
    }

    private SoundEvent getShootSound() {
        switch(this.selectedAffinity.getShiftAffinity()) {
            case EARTH:
                return SFX.Spell.Cast.EARTH;
            case ENDER:
                return SFX.Spell.Cast.ENDER;
            case FIRE:
                return SFX.Spell.Cast.FIRE;
            case LIGHTNING:
            case HELLFIRE:
            case ICE:
            case ARCANE:
            default:
                return SFX.Spell.Cast.ARCANE;
            case WATER:
                return SFX.Spell.Cast.WATER;
            case WIND:
                return SFX.Spell.Cast.WIND;
        }
    }

    private boolean consumeFuel() {
        if (this.accruedPower <= 20.0F) {
            this.accruedPower = this.accruedPower + this.consumeDirect(this.ownerInfo.getOwnerId(), this.f_58857_, this.selectedAffinity, 20.0F);
            if (this.accruedPower <= 20.0F) {
                return false;
            }
        }
        return true;
    }

    private void shoot() {
        if (this.target != null) {
            if (this.m_58904_().m_45547_(new ClipContext(Vec3.atCenterOf(this.m_58899_()), this.target.m_146892_(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null)).getType() == HitResult.Type.MISS) {
                if (this.shootCounter < this.getShootInterval()) {
                    this.shootCounter++;
                } else {
                    if (!this.m_58904_().isClientSide() && this.consumeFuel()) {
                        if (!(Boolean) this.m_58900_().m_61143_(ElementalSentryBlock.SHOOTING)) {
                            this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(ElementalSentryBlock.SHOOTING, true));
                        }
                        if (this.warmupCounter < this.getPowerupTime()) {
                            this.warmupCounter++;
                            return;
                        }
                        this.warmupCounter = 0;
                        this.shootCounter = 0;
                        this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(ElementalSentryBlock.SHOOTING, false));
                        this.m_58904_().playSound(null, this.m_58899_(), this.getShootSound(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + Math.random() * 0.4F));
                        Vec3 myPos = this.getPointInFront();
                        SentryProjectile projectile = new SentryProjectile(this.m_58904_(), myPos.x(), myPos.y(), myPos.z(), this.selectedAffinity);
                        if (this.target != null) {
                            Vec3 direction = this.target.m_20299_(0.0F).subtract(myPos).normalize();
                            projectile.shoot(direction.x, direction.y, direction.z, 2.0F, 0.0F);
                            this.m_58904_().m_7967_(projectile);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("affinity", this.selectedAffinity.ordinal());
        this.ownerInfo.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.ownerInfo.load(tag);
        if (tag.contains("affinity")) {
            try {
                this.selectedAffinity = Affinity.values()[tag.getInt("affinity")];
            } catch (Throwable var3) {
                this.selectedAffinity = Affinity.UNKNOWN;
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
}