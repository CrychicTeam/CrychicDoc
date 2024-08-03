package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

public class AmberMonolithBlockEntity extends BlockEntity {

    public int tickCount;

    private int spawnsMobIn = 0;

    private int findSpawnsCooldown = 0;

    private EntityType spawnType;

    private int spawnCount;

    private Entity displayEntity;

    private Entity prevDisplayEntity;

    private float switchProgress;

    private float previousRotation;

    private float rotation = (float) (Math.random() * 360.0);

    private boolean hasDonePostBossSpawn;

    public AmberMonolithBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.AMBER_MONOLITH.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, AmberMonolithBlockEntity entity) {
        entity.tickCount++;
        entity.previousRotation = entity.rotation++;
        if (entity.spawnsMobIn <= 1000) {
            float f = (1000.0F - (float) entity.spawnsMobIn) / 1000.0F;
            entity.rotation += f * 20.0F;
        }
        if (entity.prevDisplayEntity != entity.displayEntity) {
            if (entity.displayEntity == null) {
                if (entity.switchProgress > 0.0F) {
                    entity.switchProgress--;
                } else {
                    entity.prevDisplayEntity = null;
                }
            } else if (entity.switchProgress < 10.0F) {
                entity.switchProgress++;
            } else {
                entity.prevDisplayEntity = entity.displayEntity;
            }
        }
        if (!level.isClientSide) {
            if (entity.spawnType == null && entity.findSpawnsCooldown-- <= 0 || !entity.hasDonePostBossSpawn && entity.isMigration()) {
                entity.findSpawnsCooldown = 40 + level.random.nextInt(50);
                entity.generateSpawnData();
                if (!entity.hasDonePostBossSpawn && entity.isMigration()) {
                    entity.hasDonePostBossSpawn = true;
                    entity.spawnsMobIn = (int) Math.ceil((double) ((float) entity.spawnsMobIn * 0.25F)) + 100;
                }
            }
            if (entity.spawnsMobIn <= 0) {
                if (entity.spawnType == null) {
                    entity.generateSpawnData();
                } else if (level.m_45924_((double) ((float) entity.m_58899_().m_123341_() + 0.5F), (double) ((float) entity.m_58899_().m_123342_() + 0.5F), (double) ((float) entity.m_58899_().m_123343_() + 0.5F), 28.0, false) != null && entity.spawnMobs()) {
                    level.m_247517_((Player) null, blockPos, ACSoundRegistry.AMBER_MONOLITH_SUMMON.get(), SoundSource.BLOCKS);
                    entity.generateSpawnData();
                }
            }
        }
        if (entity.spawnsMobIn > 0) {
            entity.spawnsMobIn--;
        }
    }

    private boolean spawnMobs() {
        SpawnGroupData spawngroupdata = null;
        boolean spawned = false;
        for (int l1 = 0; l1 < this.spawnCount; l1++) {
            boolean flag = false;
            for (int i2 = 0; !flag && i2 < 6; i2++) {
                BlockPos blockpos = this.getRandomSpawnPos();
                if (blockpos != null && this.spawnType.canSummon() && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(this.spawnType), this.f_58857_, blockpos, this.spawnType)) {
                    double d0 = (double) ((float) blockpos.m_123341_() + 0.5F);
                    double d1 = (double) ((float) blockpos.m_123343_() + 0.5F);
                    if (this.f_58857_.m_45772_(this.spawnType.getAABB(d0, (double) blockpos.m_123342_(), d1)) && SpawnPlacements.checkSpawnRules(this.spawnType, (ServerLevelAccessor) this.f_58857_, MobSpawnType.SPAWNER, BlockPos.containing(d0, (double) blockpos.m_123342_(), d1), this.f_58857_.getRandom())) {
                        Entity entity;
                        try {
                            entity = this.spawnType.create(this.f_58857_);
                        } catch (Exception var19) {
                            AlexsCaves.LOGGER.warn("Failed to create mob", var19);
                            continue;
                        }
                        if (entity != null) {
                            entity.moveTo(d0, (double) blockpos.m_123342_(), d1, this.f_58857_.random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof Mob mob && ForgeEventFactory.checkSpawnPosition(mob, (ServerLevelAccessor) this.f_58857_, MobSpawnType.CHUNK_GENERATION)) {
                                spawngroupdata = mob.finalizeSpawn((ServerLevelAccessor) this.f_58857_, this.f_58857_.getCurrentDifficultyAt(mob.m_20183_()), MobSpawnType.CHUNK_GENERATION, spawngroupdata, (CompoundTag) null);
                                ((ServerLevel) this.f_58857_).m_47205_(mob);
                                spawned = true;
                                flag = true;
                                Vec3 center = this.m_58899_().getCenter();
                                Vec3 target = entity.getEyePosition();
                                Vec3 distance = target.subtract(center);
                                int maxDist = (int) (distance.length() * 1.5);
                                for (int i = 0; i < maxDist; i++) {
                                    Vec3 vec3 = center.add(distance.normalize().scale(distance.length() * (double) ((float) i / (float) maxDist))).add((double) (this.f_58857_.random.nextFloat() - 0.5F), (double) (this.f_58857_.random.nextFloat() - 0.5F), (double) (this.f_58857_.random.nextFloat() - 0.5F));
                                    ((ServerLevel) this.f_58857_).sendParticles(ACParticleRegistry.AMBER_MONOLITH.get(), vec3.x, vec3.y, vec3.z, 0, target.x, target.y, target.z, 1.0);
                                }
                                for (int i = 0; i < 5; i++) {
                                    ((ServerLevel) this.f_58857_).sendParticles(ACParticleRegistry.AMBER_EXPLOSION.get(), entity.getRandomX(1.0), entity.getRandomY(), entity.getRandomZ(1.0), 0, 0.0, 0.0, 0.0, 1.0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return spawned;
    }

    private BlockPos getRandomSpawnPos() {
        boolean caveCreature = this.spawnType.getCategory() == ACEntityRegistry.CAVE_CREATURE;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 20; i++) {
            mutableBlockPos.set(this.m_58899_().m_123341_() + this.f_58857_.getRandom().nextInt(20) - 10, this.m_58899_().m_123342_() + 1, this.m_58899_().m_123343_() + this.f_58857_.getRandom().nextInt(20) - 10);
            if (this.f_58857_.isLoaded(mutableBlockPos)) {
                while ((this.f_58857_.getBlockState(mutableBlockPos).m_60795_() || this.f_58857_.getBlockState(mutableBlockPos).m_247087_()) && mutableBlockPos.m_123342_() > this.f_58857_.m_141937_()) {
                    mutableBlockPos.move(0, -1, 0);
                }
                if (Math.abs(mutableBlockPos.m_123342_() - this.m_58899_().m_123342_()) < 20) {
                    BlockPos pos = mutableBlockPos.immutable();
                    if (!caveCreature || !this.f_58857_.m_45527_(pos.above())) {
                        return pos.above();
                    }
                }
            }
        }
        return null;
    }

    private void generateSpawnData() {
        List<EntityType<?>> forcedEntityList = new ArrayList();
        if (this.isMigration() && !this.hasDonePostBossSpawn) {
            forcedEntityList.add(ACEntityRegistry.ATLATITAN.get());
        }
        MobSpawnSettings.SpawnerData spawnerData = getDepopulatedEntitySpawnData(this.f_58857_, this.m_58899_(), 4 + this.f_58857_.random.nextInt(8), 64, forcedEntityList);
        if (spawnerData != null) {
            this.spawnType = spawnerData.type;
            int j = Math.max(spawnerData.maxCount - spawnerData.minCount, 0);
            this.spawnCount = j <= 0 ? spawnerData.minCount : this.f_58857_.random.nextInt(j) + spawnerData.minCount;
        }
        int i = Math.max(1000, AlexsCaves.COMMON_CONFIG.amberMonolithMeanTime.get());
        this.spawnsMobIn = i / 2 + this.f_58857_.getRandom().nextInt(i);
        this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
    }

    private static boolean isEntitySpawnBlocked(MobSpawnSettings.SpawnerData settings, Level level, BlockPos pos, int range) {
        if (settings.type.is(ACTagRegistry.AMBER_MONOLITH_SKIPS)) {
            return true;
        } else {
            if (settings.type == ACEntityRegistry.ATLATITAN.get()) {
                ACWorldData worldData = ACWorldData.get(level);
                if (worldData != null && !worldData.isPrimordialBossDefeatedOnce()) {
                    return true;
                }
            }
            return !level.getEntities(settings.type, new AABB(pos).inflate((double) range), Entity::m_6084_).isEmpty();
        }
    }

    private static MobSpawnSettings.SpawnerData getEntitySpawnSettingsForBiome(Level level, BlockPos pos, List<EntityType<?>> forcedEntityTypes) {
        Biome biome = (Biome) level.m_204166_(pos).value();
        if (biome != null) {
            WeightedRandomList<MobSpawnSettings.SpawnerData> spawnList = biome.getMobSettings().getMobs(ACEntityRegistry.CAVE_CREATURE);
            if (spawnList.isEmpty()) {
                spawnList = biome.getMobSettings().getMobs(MobCategory.CREATURE);
            }
            if (!forcedEntityTypes.isEmpty()) {
                List<MobSpawnSettings.SpawnerData> matching = new ArrayList();
                for (MobSpawnSettings.SpawnerData unwrapped : spawnList.unwrap()) {
                    if (forcedEntityTypes.contains(unwrapped.type)) {
                        matching.add(unwrapped);
                    }
                }
                if (!matching.isEmpty()) {
                    spawnList = WeightedRandomList.create(matching);
                }
            }
            if (!spawnList.isEmpty()) {
                return (MobSpawnSettings.SpawnerData) spawnList.getRandom(level.random).get();
            }
        }
        return null;
    }

    private static MobSpawnSettings.SpawnerData getDepopulatedEntitySpawnData(Level level, BlockPos pos, int rolls, int range, List<EntityType<?>> forcedEntityTypes) {
        MobSpawnSettings.SpawnerData spawnerData = null;
        for (int roll = 0; roll < rolls; roll++) {
            if (spawnerData != null && !isEntitySpawnBlocked(spawnerData, level, pos, range)) {
                return spawnerData;
            }
            spawnerData = getEntitySpawnSettingsForBiome(level, pos, forcedEntityTypes);
        }
        return spawnerData;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            if (packet.getTag().contains("EntityType")) {
                String str = packet.getTag().getString("EntityType");
                this.spawnType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
            }
            this.spawnCount = packet.getTag().getInt("SpawnCount");
            this.spawnsMobIn = packet.getTag().getInt("SpawnMobsIn");
            this.hasDonePostBossSpawn = packet.getTag().getBoolean("PostBossSpawn");
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("EntityType")) {
            String str = tag.getString("EntityType");
            this.spawnType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(str));
        }
        this.spawnCount = tag.getInt("SpawnCount");
        this.spawnsMobIn = tag.getInt("SpawnMobsIn");
        this.hasDonePostBossSpawn = tag.getBoolean("PostBossSpawn");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.spawnType != null) {
            tag.putString("EntityType", ForgeRegistries.ENTITY_TYPES.getKey(this.spawnType).toString());
        }
        tag.putInt("SpawnCount", this.spawnCount);
        tag.putInt("SpawnMobsIn", this.spawnsMobIn);
        tag.putBoolean("PostBossSpawn", this.hasDonePostBossSpawn);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public int getSpawnsMobIn() {
        return this.spawnsMobIn;
    }

    public Entity getDisplayEntity(Level level) {
        if (this.displayEntity == null && this.spawnType != null || this.displayEntity != null && this.displayEntity.getType() != this.spawnType) {
            this.displayEntity = this.spawnType.create(level);
        }
        return this.displayEntity;
    }

    public Entity getPrevDisplayEntity() {
        return this.prevDisplayEntity;
    }

    public float getRotation(float partialTicks) {
        return this.previousRotation + (this.rotation - this.previousRotation) * partialTicks;
    }

    private boolean isMigration() {
        ACWorldData worldData = ACWorldData.get(this.f_58857_);
        return worldData == null ? false : worldData.isPrimordialBossDefeatedOnce() && worldData.getFirstPrimordialBossDefeatTimestamp() != -1L && worldData.getFirstPrimordialBossDefeatTimestamp() + 24000L > this.f_58857_.getGameTime();
    }
}