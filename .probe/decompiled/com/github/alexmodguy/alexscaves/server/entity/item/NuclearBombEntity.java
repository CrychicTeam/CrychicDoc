package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.google.common.base.Predicates;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class NuclearBombEntity extends Entity {

    private static final EntityDataAccessor<Integer> TIME = SynchedEntityData.defineId(NuclearBombEntity.class, EntityDataSerializers.INT);

    public static final int MAX_TIME = 300;

    public NuclearBombEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public NuclearBombEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.NUCLEAR_BOMB.get(), level);
        this.m_20011_(this.m_142242_());
    }

    public NuclearBombEntity(Level level, double x, double y, double z) {
        this(ACEntityRegistry.NUCLEAR_BOMB.get(), level);
        this.m_6034_(x, y, z);
        double d0 = level.random.nextDouble() * (float) (Math.PI * 2);
        this.m_20334_(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.98));
        if (this.m_20096_()) {
            this.m_20256_(this.m_20184_().multiply(0.7, -0.7, 0.7));
        }
        if ((this.f_19797_ + this.m_19879_()) % 10 == 0 && this.m_9236_() instanceof ServerLevel serverLevel) {
            this.getNearbySirens(serverLevel, 256).forEach(this::activateSiren);
        }
        int i = this.getTime() + 1;
        if (i > 300) {
            this.m_146870_();
            if (!this.m_9236_().isClientSide) {
                this.explode();
            }
        } else {
            this.setTime(i);
            this.m_20073_();
            if (this.m_9236_().isClientSide && 300 - i > 10 && this.f_19796_.nextFloat() < 0.3F && this.m_20096_()) {
                Vec3 center = this.m_146892_();
                this.m_9236_().addParticle(ACParticleRegistry.PROTON.get(), center.x, center.y, center.z, center.x, center.y, center.z);
            }
        }
    }

    private void activateSiren(BlockPos pos) {
        if (this.m_9236_().getBlockEntity(pos) instanceof NuclearSirenBlockEntity nuclearSirenBlock) {
            nuclearSirenBlock.setNearestNuclearBomb(this);
        }
    }

    private void explode() {
        NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(this.m_9236_());
        explosion.m_20359_(this);
        explosion.setSize(AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue());
        this.m_9236_().m_7967_(explosion);
    }

    @Override
    public void resetFallDistance() {
        if (this.f_19789_ > 20.0F) {
            this.m_146870_();
            if (!this.m_9236_().isClientSide) {
                this.explode();
            }
        }
    }

    private Stream<BlockPos> getNearbySirens(ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.NUCLEAR_SIREN.getKey()), Predicates.alwaysTrue(), this.m_20183_(), range, PoiManager.Occupancy.ANY);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TIME, 0);
    }

    public int getTime() {
        return this.f_19804_.get(TIME);
    }

    public void setTime(int time) {
        this.f_19804_.set(TIME, time);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public boolean shouldBeSaved() {
        return !this.m_213877_();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ACBlockRegistry.NUCLEAR_BOMB.get());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (itemStack.is(Tags.Items.SHEARS)) {
            player.m_6674_(hand);
            this.m_216990_(ACSoundRegistry.NUCLEAR_BOMB_DEFUSE.get());
            this.m_142687_(Entity.RemovalReason.KILLED);
            this.m_19983_(new ItemStack(ACBlockRegistry.NUCLEAR_BOMB.get()));
            if (!player.getAbilities().instabuild) {
                itemStack.hurtAndBreak(1, player, e -> e.m_21190_(hand));
            }
            return InteractionResult.SUCCESS;
        } else if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (!this.m_9236_().isClientSide) {
            return player.m_20329_(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
            float progress = (float) this.getTime() / 300.0F;
            float expandScale = 1.0F + (float) Math.sin((double) (progress * progress) * Math.PI) * 0.5F;
            float f1 = -(this.m_146909_() / 40.0F);
            float j = expandScale - progress * 0.3F;
            double d0 = this.m_20186_() + (double) j + passenger.getMyRidingOffset() - 0.2F;
            moveFunction.accept(passenger, this.m_20185_(), d0, this.m_20189_());
            passenger.fallDistance = 0.0F;
            return;
        }
        super.positionRider(passenger, moveFunction);
    }

    @Override
    public boolean causeFallDamage(float f, float f1, DamageSource damageSource) {
        return false;
    }
}