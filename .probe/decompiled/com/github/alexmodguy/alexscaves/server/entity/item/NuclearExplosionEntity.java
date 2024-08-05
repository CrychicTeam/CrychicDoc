package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.TremorzillaEggBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.List;
import java.util.Stack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class NuclearExplosionEntity extends Entity {

    private boolean spawnedParticle = false;

    private Stack<BlockPos> destroyingChunks = new Stack();

    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(NuclearExplosionEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> NO_GRIEFING = SynchedEntityData.defineId(NuclearExplosionEntity.class, EntityDataSerializers.BOOLEAN);

    private boolean loadingChunks = false;

    private Explosion dummyExplosion;

    public NuclearExplosionEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public NuclearExplosionEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.NUCLEAR_EXPLOSION.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        int chunksAffected = this.getChunksAffected();
        int radius = chunksAffected * 15;
        if (!this.spawnedParticle) {
            this.spawnedParticle = true;
            int particleY = (int) Math.ceil(this.m_20186_());
            while (particleY > this.m_9236_().m_141937_() && (double) particleY > this.m_20186_() - (double) ((float) radius / 2.0F) && this.isDestroyable(this.m_9236_().getBlockState(BlockPos.containing(this.m_20185_(), (double) particleY, this.m_20189_())))) {
                particleY--;
            }
            this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.MUSHROOM_CLOUD.get(), true, this.m_20185_(), (double) (particleY + 2), this.m_20189_(), (double) this.getSize(), 0.0, 0.0);
        }
        if (this.f_19797_ > 40 && this.destroyingChunks.isEmpty()) {
            this.remove(Entity.RemovalReason.DISCARDED);
        } else {
            if (!this.m_9236_().isClientSide && !this.isNoGriefing()) {
                if (!this.loadingChunks && !this.m_213877_()) {
                    this.loadingChunks = true;
                    this.loadChunksAround(true);
                }
                if (!this.destroyingChunks.isEmpty()) {
                    int tickChunkCount = Math.min(this.destroyingChunks.size(), 3);
                    for (int i = 0; i < tickChunkCount; i++) {
                        this.removeChunk(radius);
                    }
                } else {
                    BlockPos center = this.m_20183_();
                    int chunks = chunksAffected;
                    for (int i = -chunksAffected; i <= chunks; i++) {
                        for (int j = -chunks; j <= chunks; j++) {
                            for (int k = -chunks; k <= chunks; k++) {
                                this.destroyingChunks.push(center.offset(i * 16, j * 16, k * 16));
                            }
                        }
                    }
                    this.destroyingChunks.sort((blockPos1, blockPos2) -> Double.compare((double) blockPos2.m_123333_(this.m_20183_()), (double) blockPos1.m_123333_(this.m_20183_())));
                }
            }
            AABB killBox = this.m_20191_().inflate((double) ((float) radius + (float) radius * 0.5F), (double) radius * 0.6, (double) ((float) radius + (float) radius * 0.5F));
            float flingStrength = this.getSize() * 0.33F;
            float maximumDistance = (float) radius + (float) radius * 0.5F + 1.0F;
            for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, killBox)) {
                float dist = entity.m_20270_(this);
                float damage = this.calculateDamage(dist, maximumDistance);
                Vec3 vec3 = entity.m_20182_().subtract(this.m_20182_()).add(0.0, 0.3, 0.0).normalize();
                float playerFling = entity instanceof Player ? 0.5F * flingStrength : flingStrength;
                if (damage > 0.0F) {
                    if (entity instanceof RaycatEntity) {
                        damage = 0.0F;
                    } else if (entity.m_6095_().is(ACTagRegistry.RESISTS_RADIATION)) {
                        damage *= 0.25F;
                        playerFling *= 0.1F;
                        if (entity instanceof TremorzillaEntity) {
                            playerFling = 0.0F;
                            damage = 0.0F;
                        }
                    }
                    if (damage > 0.0F) {
                        entity.hurt(ACDamageTypes.causeNukeDamage(this.m_9236_().registryAccess()), damage);
                    }
                }
                entity.m_20256_(vec3.scale((double) (damage * 0.1F * playerFling)));
                entity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 48000, this.getSize() <= 1.5F ? 1 : 2, false, false, true));
            }
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if (!this.m_9236_().isClientSide && this.loadingChunks) {
            this.loadingChunks = false;
            this.loadChunksAround(false);
        }
        super.remove(removalReason);
    }

    private int getChunksAffected() {
        return (int) Math.ceil((double) this.getSize());
    }

    private void loadChunksAround(boolean load) {
        if (this.m_9236_() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = new ChunkPos(this.m_20183_());
            int dist = Math.max(this.getChunksAffected(), serverLevel.getServer().getPlayerList().getViewDistance() / 2);
            for (int i = -dist; i <= dist; i++) {
                for (int j = -dist; j <= dist; j++) {
                    ForgeChunkManager.forceChunk(serverLevel, "alexscaves", this, chunkPos.x + i, chunkPos.z + j, load, load);
                }
            }
        }
    }

    private float calculateDamage(float dist, float max) {
        float revert = (max - dist) / max;
        float baseDmg = this.getSize() <= 1.5F ? 100.0F : 100.0F + (this.getSize() - 1.5F) * 400.0F;
        return revert * baseDmg;
    }

    private void removeChunk(int radius) {
        BlockPos chunkCorner = (BlockPos) this.destroyingChunks.pop();
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveBelow = new BlockPos.MutableBlockPos();
        carve.set(chunkCorner);
        carveBelow.set(chunkCorner);
        float itemDropModifier = 0.025F / Math.min(1.0F, this.getSize());
        if (AlexsCaves.COMMON_CONFIG.nukeMaxBlockExplosionResistance.get() > 0) {
            if (this.dummyExplosion == null) {
                this.dummyExplosion = new Explosion(this.m_9236_(), null, this.m_20185_(), this.m_20186_(), this.m_20189_(), 10.0F, List.of());
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 15; y >= 0; y--) {
                        carve.set(chunkCorner.m_123341_() + x, Mth.clamp(chunkCorner.m_123342_() + y, this.m_9236_().m_141937_(), this.m_9236_().m_151558_()), chunkCorner.m_123343_() + z);
                        float widthSimplexNoise1 = (ACMath.sampleNoise3D(carve.m_123341_(), carve.m_123342_(), carve.m_123343_(), (float) radius) - 0.5F) * 0.45F + 0.55F;
                        double yDist = (double) ACMath.smin(0.6F - (float) Math.abs(this.m_20183_().m_123342_() - carve.m_123342_()) / (float) radius, 0.6F, 0.2F);
                        double distToCenter = carve.m_203202_((double) this.m_20183_().m_123341_(), (double) (carve.m_123342_() - 1), (double) this.m_20183_().m_123343_());
                        double targetRadius = yDist * (double) ((float) radius + widthSimplexNoise1 * (float) radius) * (double) radius;
                        if (distToCenter <= targetRadius) {
                            BlockState state = this.m_9236_().getBlockState(carve);
                            if ((!state.m_60795_() || !state.m_60819_().isEmpty()) && this.isDestroyable(state)) {
                                carveBelow.set(carve.m_123341_(), carve.m_123342_() - 1, carve.m_123343_());
                                if (state.m_60713_(ACBlockRegistry.TREMORZILLA_EGG.get()) && state.m_60734_() instanceof TremorzillaEggBlock tremorzillaEggBlock) {
                                    tremorzillaEggBlock.spawnDinosaurs(this.m_9236_(), carve, state);
                                } else if (AlexsCaves.COMMON_CONFIG.nukesSpawnItemDrops.get() && this.f_19796_.nextFloat() < itemDropModifier && state.m_60819_().isEmpty()) {
                                    this.m_9236_().m_46961_(carve, true);
                                } else {
                                    state.onBlockExploded(this.m_9236_(), carve, this.dummyExplosion);
                                }
                            }
                        }
                    }
                    if ((double) this.f_19796_.nextFloat() < 0.15 && !this.m_9236_().getBlockState(carveBelow).m_60795_()) {
                        this.m_9236_().setBlockAndUpdate(carveBelow.m_7494_(), Blocks.FIRE.defaultBlockState());
                    }
                }
            }
        }
    }

    private boolean isDestroyable(BlockState state) {
        return !state.m_204336_(ACTagRegistry.NUKE_PROOF) && state.m_60734_().getExplosionResistance() < (float) AlexsCaves.COMMON_CONFIG.nukeMaxBlockExplosionResistance.get().intValue() || state.m_60713_(ACBlockRegistry.TREMORZILLA_EGG.get());
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(SIZE, 1.0F);
        this.f_19804_.define(NO_GRIEFING, false);
    }

    public float getSize() {
        return this.f_19804_.get(SIZE);
    }

    public void setSize(float f) {
        this.f_19804_.set(SIZE, f);
    }

    public boolean isNoGriefing() {
        return this.f_19804_.get(NO_GRIEFING);
    }

    public void setNoGriefing(boolean noGriefing) {
        this.f_19804_.set(NO_GRIEFING, noGriefing);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.loadingChunks = compoundTag.getBoolean("WasLoadingChunks");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putBoolean("WasLoadingChunks", this.loadingChunks);
    }
}