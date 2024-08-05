package net.mehvahdjukaar.supplementaries.common.entities;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.entities.dispenser_minecart.MovingBlockSource;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PearlMarker extends Entity {

    private Pair<ThrownEnderpearl, HitResult> event = null;

    private final List<ThrownEnderpearl> pearls = new ArrayList();

    private static final EntityDataAccessor<BlockPos> TELEPORT_POS = SynchedEntityData.defineId(PearlMarker.class, EntityDataSerializers.BLOCK_POS);

    public PearlMarker(Level worldIn) {
        super((EntityType<?>) ModEntities.PEARL_MARKER.get(), worldIn);
        this.m_20242_(true);
        this.m_20331_(true);
    }

    public PearlMarker(EntityType<PearlMarker> type, Level level) {
        super(type, level);
        this.m_6842_(true);
        this.m_20242_(true);
        this.m_20331_(true);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TELEPORT_POS, this.m_20183_());
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public void kill() {
        this.m_146870_();
    }

    @Override
    public void tick() {
        super.baseTick();
        boolean dead = this.pearls.isEmpty();
        Level level = this.m_9236_();
        if (!dead) {
            BlockPos pos = this.m_20183_();
            BlockState state = level.getBlockState(pos);
            if (!(state.m_60734_() instanceof DispenserBlock)) {
                PistonMovingBlockEntity piston;
                boolean didOffset;
                label49: {
                    piston = null;
                    didOffset = false;
                    if (level.getBlockEntity(pos) instanceof PistonMovingBlockEntity p && p.getMovedState().m_60734_() instanceof DispenserBlock) {
                        piston = p;
                        break label49;
                    }
                    for (Direction d : Direction.values()) {
                        BlockPos offPos = pos.relative(d);
                        if (level.getBlockEntity(offPos) instanceof PistonMovingBlockEntity p && p.getMovedState().m_60734_() instanceof DispenserBlock) {
                            piston = p;
                            break;
                        }
                    }
                }
                if (piston != null) {
                    Direction dir = piston.getMovementDirection();
                    this.m_6478_(MoverType.PISTON, new Vec3((double) dir.getStepX() * 0.33, (double) dir.getStepY() * 0.33, (double) dir.getStepZ() * 0.33));
                    didOffset = true;
                }
                dead = !didOffset;
            }
        }
        if (dead && !level.isClientSide) {
            this.m_146870_();
        }
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    private void removePearl(ThrownEnderpearl pearl) {
        this.pearls.remove(pearl);
    }

    public void addPearl(ThrownEnderpearl pearl) {
        this.pearls.add(pearl);
    }

    @Override
    public void teleportTo(double pX, double pY, double pZ) {
        if (this.event != null) {
            HitResult trace = (HitResult) this.event.getSecond();
            ThrownEnderpearl pearl = (ThrownEnderpearl) this.event.getFirst();
            if (trace instanceof BlockHitResult hitResult) {
                Level level = this.m_9236_();
                BlockPos fromPos = this.m_20183_();
                BlockState state = level.getBlockState(fromPos);
                BlockEntity blockEntity = level.getBlockEntity(fromPos);
                if (state.m_60734_() instanceof DispenserBlock && blockEntity instanceof DispenserBlockEntity) {
                    BlockPos toPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    if (level.getBlockState(toPos).m_247087_()) {
                        CompoundTag nbt = blockEntity.saveWithoutMetadata();
                        blockEntity.setRemoved();
                        if (level.setBlockAndUpdate(fromPos, Blocks.AIR.defaultBlockState()) && level.setBlockAndUpdate(toPos, (BlockState) state.m_61124_(DispenserBlock.FACING, hitResult.getDirection()))) {
                            BlockEntity dstEntity = level.getBlockEntity(toPos);
                            if (dstEntity instanceof DispenserBlockEntity) {
                                dstEntity.load(nbt);
                            }
                            SoundType type = state.m_60827_();
                            level.playSound(null, toPos, type.getPlaceSound(), SoundSource.BLOCKS, (type.getVolume() + 1.0F) / 2.0F, type.getPitch() * 0.8F);
                        }
                    }
                    this.setTeleportPos(toPos);
                    level.broadcastEntityEvent(this, (byte) 92);
                    super.teleportTo((double) toPos.m_123341_() + 0.5, (double) toPos.m_123342_() + 0.5 - (double) (this.m_20206_() / 2.0F), (double) toPos.m_123343_() + 0.5);
                }
            }
            this.removePearl(pearl);
            pearl.m_146870_();
            this.event = null;
        } else {
            super.teleportTo(pX, pY, pZ);
        }
    }

    public BlockPos getTeleportPos() {
        return this.f_19804_.get(TELEPORT_POS);
    }

    public void setTeleportPos(BlockPos pos) {
        this.f_19804_.set(TELEPORT_POS, pos);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 92) {
            Level level = this.m_9236_();
            if (level.isClientSide) {
                RandomSource random = level.random;
                BlockPos end = this.getTeleportPos();
                BlockPos start = this.m_20183_();
                for (int j = 0; j < 64; j++) {
                    double d0 = random.nextDouble();
                    float f = (random.nextFloat() - 0.5F) * 0.2F;
                    float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                    float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                    double d1 = Mth.lerp(d0, (double) end.m_123341_(), (double) start.m_123341_()) + (random.nextDouble() - 0.5) + 0.5;
                    double d2 = Mth.lerp(d0, (double) end.m_123342_(), (double) start.m_123342_()) + random.nextDouble() - 0.5;
                    double d3 = Mth.lerp(d0, (double) end.m_123343_(), (double) start.m_123343_()) + (random.nextDouble() - 0.5) + 0.5;
                    level.addParticle(ParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1, (double) f2);
                }
            }
        }
        super.handleEntityEvent(pId);
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        super.lerpTo(pX, pY, pZ, pYaw, pPitch, pPosRotationIncrements, pTeleport);
        this.m_6034_(pX, pY, pZ);
    }

    public static void onProjectileImpact(Projectile projectile, HitResult hitResult) {
        Level level = projectile.m_9236_();
        if (!level.isClientSide && projectile instanceof ThrownEnderpearl pearl && projectile.getOwner() instanceof PearlMarker markerEntity && projectile.m_20137_("dispensed")) {
            markerEntity.event = Pair.of(pearl, hitResult);
        }
    }

    public static ThrownEnderpearl getPearlToDispense(BlockSource source, Level level, BlockPos pos) {
        ThrownEnderpearl pearl = new ThrownEnderpearl(EntityType.ENDER_PEARL, level);
        if (source instanceof MovingBlockSource movingBlockSource) {
            pearl.m_5602_(movingBlockSource.getMinecartEntity());
        } else {
            Optional<PearlMarker> entity = level.m_6443_(PearlMarker.class, new AABB(pos), e -> e.m_20183_().equals(pos)).stream().findAny();
            PearlMarker marker;
            if (entity.isEmpty()) {
                marker = new PearlMarker(level);
                marker.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5 - (double) (marker.m_20206_() / 2.0F), (double) pos.m_123343_() + 0.5);
                level.m_7967_(marker);
            } else {
                marker = (PearlMarker) entity.get();
            }
            marker.addPearl(pearl);
            pearl.m_5602_(marker);
        }
        Position position = DispenserBlock.getDispensePosition(source);
        pearl.m_6034_(position.x(), position.y(), position.z());
        pearl.m_20049_("dispensed");
        return pearl;
    }
}