package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.api.tile.ISparkable;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.damage.DamageEmber;
import com.rekindled.embers.datagen.EmbersDamageTypes;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageBeamCannonFX;
import com.rekindled.embers.power.DefaultEmberCapability;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class BeamCannonBlockEntity extends BlockEntity {

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            BeamCannonBlockEntity.this.m_6596_();
        }

        @Override
        public boolean acceptsVolatile() {
            return false;
        }
    };

    public static final double PULL_RATE = 2000.0;

    public static final int FIRE_THRESHOLD = 1000;

    public static final float DAMAGE = 25.0F;

    public static final int MAX_DISTANCE = 64;

    public long ticksExisted = 0L;

    public boolean lastPowered = false;

    public Random random = new Random();

    public int offset = this.random.nextInt(40);

    protected List<UpgradeContext> upgrades;

    public BeamCannonBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.BEAM_CANNON_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(2000.0);
    }

    public BeamCannonBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.lastPowered = nbt.getBoolean("lastPowered");
        this.capability.deserializeNBT(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("lastPowered", this.lastPowered);
        this.capability.writeToNBT(nbt);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BeamCannonBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        Direction facing = (Direction) state.m_61143_(BlockStateProperties.FACING);
        BlockEntity attachedTile = level.getBlockEntity(pos.relative(facing, -1));
        if (blockEntity.ticksExisted % 5L == 0L && attachedTile != null) {
            IEmberCapability cap = (IEmberCapability) attachedTile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing).orElse(null);
            if (cap != null && cap.getEmber() > 0.0 && blockEntity.capability.getEmber() < blockEntity.capability.getEmberCapacity()) {
                double removed = cap.removeAmount(2000.0, true);
                blockEntity.capability.addAmount(removed, true);
            }
        }
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Direction.values());
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
        boolean isPowered = level.m_276867_(pos);
        boolean redstoneEnabled = UpgradeUtil.getOtherParameter(blockEntity, "redstone_enabled", true, blockEntity.upgrades);
        int threshold = UpgradeUtil.getOtherParameter(blockEntity, "fire_threshold", 1000, blockEntity.upgrades);
        if (!cancel && blockEntity.capability.getEmber() >= (double) threshold && (!redstoneEnabled || isPowered && !blockEntity.lastPowered)) {
            blockEntity.fire(facing);
        }
        blockEntity.lastPowered = isPowered;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY && this.f_58857_.getBlockState(this.m_58899_()).m_61143_(BlockStateProperties.FACING) != side ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    public void fire(Direction facing) {
        Vec3 ray = new Vec3((double) facing.getNormal().getX(), (double) facing.getNormal().getY(), (double) facing.getNormal().getZ());
        double damage = UpgradeUtil.getOtherParameter(this, "damage", 25.0, this.upgrades);
        boolean doContinue = true;
        int maxDist = UpgradeUtil.getOtherParameter(this, "distance", 64, this.upgrades);
        double impactDist = (double) maxDist;
        BlockPos hitPos = this.f_58858_;
        for (int i = 0; i < maxDist && doContinue; i++) {
            hitPos = hitPos.relative(facing);
            BlockState state = this.f_58857_.getBlockState(hitPos);
            BlockEntity tile = this.f_58857_.getBlockEntity(hitPos);
            if (this.sparkTarget(tile)) {
                doContinue = false;
                impactDist = (double) (i + 1);
            } else if (tile instanceof IEmberPacketReceiver) {
                IEmberCapability cap = (IEmberCapability) tile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElseGet(null);
                if (cap != null) {
                    cap.addAmount(this.capability.getEmber(), true);
                }
                doContinue = false;
                impactDist = (double) (i + 1);
            } else if (!state.m_60812_(this.f_58857_, hitPos).isEmpty()) {
                doContinue = false;
                impactDist = (double) i + 0.5;
            }
            if (!doContinue) {
                this.f_58857_.playSound(null, hitPos, EmbersSounds.BEAM_CANNON_HIT.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        }
        for (Entity entity : this.f_58857_.getEntities((Entity) null, new AABB(this.f_58858_.getCenter(), hitPos.getCenter()), EntitySelector.NO_SPECTATORS)) {
            DamageSource damageSource = new DamageEmber(((Registry) this.f_58857_.registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(EmbersDamageTypes.EMBER_KEY), this.f_58858_.getCenter());
            entity.hurt(damageSource, (float) damage);
        }
        PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> this.f_58857_.getChunkAt(this.f_58858_)), new MessageBeamCannonFX((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5, ray.x * impactDist, ray.y * impactDist, ray.z * impactDist, 16728080));
        UpgradeUtil.throwEvent(this, new EmberEvent(this, EmberEvent.EnumType.TRANSFER, this.capability.getEmber()), this.upgrades);
        this.capability.setEmber(0.0);
        this.m_6596_();
        this.f_58857_.playSound(null, this.f_58858_, EmbersSounds.BEAM_CANNON_FIRE.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
    }

    public boolean sparkTarget(BlockEntity target) {
        if (target instanceof ISparkable) {
            ((ISparkable) target).sparkProgress(this, this.capability.getEmber());
            return true;
        } else {
            return false;
        }
    }
}