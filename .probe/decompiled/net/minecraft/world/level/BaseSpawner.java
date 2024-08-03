package net.minecraft.world.level;

import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

public abstract class BaseSpawner {

    public static final String SPAWN_DATA_TAG = "SpawnData";

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int EVENT_SPAWN = 1;

    private int spawnDelay = 20;

    private SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();

    @Nullable
    private SpawnData nextSpawnData;

    private double spin;

    private double oSpin;

    private int minSpawnDelay = 200;

    private int maxSpawnDelay = 800;

    private int spawnCount = 4;

    @Nullable
    private Entity displayEntity;

    private int maxNearbyEntities = 6;

    private int requiredPlayerRange = 16;

    private int spawnRange = 4;

    public void setEntityId(EntityType<?> entityType0, @Nullable Level level1, RandomSource randomSource2, BlockPos blockPos3) {
        this.getOrCreateNextSpawnData(level1, randomSource2, blockPos3).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType0).toString());
    }

    private boolean isNearPlayer(Level level0, BlockPos blockPos1) {
        return level0.m_45914_((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, (double) this.requiredPlayerRange);
    }

    public void clientTick(Level level0, BlockPos blockPos1) {
        if (!this.isNearPlayer(level0, blockPos1)) {
            this.oSpin = this.spin;
        } else if (this.displayEntity != null) {
            RandomSource $$2 = level0.getRandom();
            double $$3 = (double) blockPos1.m_123341_() + $$2.nextDouble();
            double $$4 = (double) blockPos1.m_123342_() + $$2.nextDouble();
            double $$5 = (double) blockPos1.m_123343_() + $$2.nextDouble();
            level0.addParticle(ParticleTypes.SMOKE, $$3, $$4, $$5, 0.0, 0.0, 0.0);
            level0.addParticle(ParticleTypes.FLAME, $$3, $$4, $$5, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
                this.spawnDelay--;
            }
            this.oSpin = this.spin;
            this.spin = (this.spin + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0;
        }
    }

    public void serverTick(ServerLevel serverLevel0, BlockPos blockPos1) {
        if (this.isNearPlayer(serverLevel0, blockPos1)) {
            if (this.spawnDelay == -1) {
                this.delay(serverLevel0, blockPos1);
            }
            if (this.spawnDelay > 0) {
                this.spawnDelay--;
            } else {
                boolean $$2 = false;
                RandomSource $$3 = serverLevel0.m_213780_();
                SpawnData $$4 = this.getOrCreateNextSpawnData(serverLevel0, $$3, blockPos1);
                for (int $$5 = 0; $$5 < this.spawnCount; $$5++) {
                    CompoundTag $$6 = $$4.getEntityToSpawn();
                    Optional<EntityType<?>> $$7 = EntityType.by($$6);
                    if ($$7.isEmpty()) {
                        this.delay(serverLevel0, blockPos1);
                        return;
                    }
                    ListTag $$8 = $$6.getList("Pos", 6);
                    int $$9 = $$8.size();
                    double $$10 = $$9 >= 1 ? $$8.getDouble(0) : (double) blockPos1.m_123341_() + ($$3.nextDouble() - $$3.nextDouble()) * (double) this.spawnRange + 0.5;
                    double $$11 = $$9 >= 2 ? $$8.getDouble(1) : (double) (blockPos1.m_123342_() + $$3.nextInt(3) - 1);
                    double $$12 = $$9 >= 3 ? $$8.getDouble(2) : (double) blockPos1.m_123343_() + ($$3.nextDouble() - $$3.nextDouble()) * (double) this.spawnRange + 0.5;
                    if (serverLevel0.m_45772_(((EntityType) $$7.get()).getAABB($$10, $$11, $$12))) {
                        BlockPos $$13 = BlockPos.containing($$10, $$11, $$12);
                        if ($$4.getCustomSpawnRules().isPresent()) {
                            if (!((EntityType) $$7.get()).getCategory().isFriendly() && serverLevel0.m_46791_() == Difficulty.PEACEFUL) {
                                continue;
                            }
                            SpawnData.CustomSpawnRules $$14 = (SpawnData.CustomSpawnRules) $$4.getCustomSpawnRules().get();
                            if (!$$14.blockLightLimit().isValueInRange(serverLevel0.m_45517_(LightLayer.BLOCK, $$13)) || !$$14.skyLightLimit().isValueInRange(serverLevel0.m_45517_(LightLayer.SKY, $$13))) {
                                continue;
                            }
                        } else if (!SpawnPlacements.checkSpawnRules((EntityType) $$7.get(), serverLevel0, MobSpawnType.SPAWNER, $$13, serverLevel0.m_213780_())) {
                            continue;
                        }
                        Entity $$15 = EntityType.loadEntityRecursive($$6, serverLevel0, p_151310_ -> {
                            p_151310_.moveTo($$10, $$11, $$12, p_151310_.getYRot(), p_151310_.getXRot());
                            return p_151310_;
                        });
                        if ($$15 == null) {
                            this.delay(serverLevel0, blockPos1);
                            return;
                        }
                        int $$16 = serverLevel0.m_45976_($$15.getClass(), new AABB((double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), (double) (blockPos1.m_123341_() + 1), (double) (blockPos1.m_123342_() + 1), (double) (blockPos1.m_123343_() + 1)).inflate((double) this.spawnRange)).size();
                        if ($$16 >= this.maxNearbyEntities) {
                            this.delay(serverLevel0, blockPos1);
                            return;
                        }
                        $$15.moveTo($$15.getX(), $$15.getY(), $$15.getZ(), $$3.nextFloat() * 360.0F, 0.0F);
                        if ($$15 instanceof Mob $$17) {
                            if ($$4.getCustomSpawnRules().isEmpty() && !$$17.checkSpawnRules(serverLevel0, MobSpawnType.SPAWNER) || !$$17.checkSpawnObstruction(serverLevel0)) {
                                continue;
                            }
                            if ($$4.getEntityToSpawn().size() == 1 && $$4.getEntityToSpawn().contains("id", 8)) {
                                ((Mob) $$15).finalizeSpawn(serverLevel0, serverLevel0.m_6436_($$15.blockPosition()), MobSpawnType.SPAWNER, null, null);
                            }
                        }
                        if (!serverLevel0.tryAddFreshEntityWithPassengers($$15)) {
                            this.delay(serverLevel0, blockPos1);
                            return;
                        }
                        serverLevel0.m_46796_(2004, blockPos1, 0);
                        serverLevel0.m_142346_($$15, GameEvent.ENTITY_PLACE, $$13);
                        if ($$15 instanceof Mob) {
                            ((Mob) $$15).spawnAnim();
                        }
                        $$2 = true;
                    }
                }
                if ($$2) {
                    this.delay(serverLevel0, blockPos1);
                }
            }
        }
    }

    private void delay(Level level0, BlockPos blockPos1) {
        RandomSource $$2 = level0.random;
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + $$2.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }
        this.spawnPotentials.m_216829_($$2).ifPresent(p_186386_ -> this.setNextSpawnData(level0, blockPos1, (SpawnData) p_186386_.getData()));
        this.broadcastEvent(level0, blockPos1, 1);
    }

    public void load(@Nullable Level level0, BlockPos blockPos1, CompoundTag compoundTag2) {
        this.spawnDelay = compoundTag2.getShort("Delay");
        boolean $$3 = compoundTag2.contains("SpawnData", 10);
        if ($$3) {
            SpawnData $$4 = (SpawnData) SpawnData.CODEC.parse(NbtOps.INSTANCE, compoundTag2.getCompound("SpawnData")).resultOrPartial(p_186391_ -> LOGGER.warn("Invalid SpawnData: {}", p_186391_)).orElseGet(SpawnData::new);
            this.setNextSpawnData(level0, blockPos1, $$4);
        }
        boolean $$5 = compoundTag2.contains("SpawnPotentials", 9);
        if ($$5) {
            ListTag $$6 = compoundTag2.getList("SpawnPotentials", 10);
            this.spawnPotentials = (SimpleWeightedRandomList<SpawnData>) SpawnData.LIST_CODEC.parse(NbtOps.INSTANCE, $$6).resultOrPartial(p_186388_ -> LOGGER.warn("Invalid SpawnPotentials list: {}", p_186388_)).orElseGet(SimpleWeightedRandomList::m_185864_);
        } else {
            this.spawnPotentials = SimpleWeightedRandomList.single(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData());
        }
        if (compoundTag2.contains("MinSpawnDelay", 99)) {
            this.minSpawnDelay = compoundTag2.getShort("MinSpawnDelay");
            this.maxSpawnDelay = compoundTag2.getShort("MaxSpawnDelay");
            this.spawnCount = compoundTag2.getShort("SpawnCount");
        }
        if (compoundTag2.contains("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = compoundTag2.getShort("MaxNearbyEntities");
            this.requiredPlayerRange = compoundTag2.getShort("RequiredPlayerRange");
        }
        if (compoundTag2.contains("SpawnRange", 99)) {
            this.spawnRange = compoundTag2.getShort("SpawnRange");
        }
        this.displayEntity = null;
    }

    public CompoundTag save(CompoundTag compoundTag0) {
        compoundTag0.putShort("Delay", (short) this.spawnDelay);
        compoundTag0.putShort("MinSpawnDelay", (short) this.minSpawnDelay);
        compoundTag0.putShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
        compoundTag0.putShort("SpawnCount", (short) this.spawnCount);
        compoundTag0.putShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
        compoundTag0.putShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
        compoundTag0.putShort("SpawnRange", (short) this.spawnRange);
        if (this.nextSpawnData != null) {
            compoundTag0.put("SpawnData", (Tag) SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(() -> new IllegalStateException("Invalid SpawnData")));
        }
        compoundTag0.put("SpawnPotentials", (Tag) SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
        return compoundTag0;
    }

    @Nullable
    public Entity getOrCreateDisplayEntity(Level level0, RandomSource randomSource1, BlockPos blockPos2) {
        if (this.displayEntity == null) {
            CompoundTag $$3 = this.getOrCreateNextSpawnData(level0, randomSource1, blockPos2).getEntityToSpawn();
            if (!$$3.contains("id", 8)) {
                return null;
            }
            this.displayEntity = EntityType.loadEntityRecursive($$3, level0, Function.identity());
            if ($$3.size() == 1 && this.displayEntity instanceof Mob) {
            }
        }
        return this.displayEntity;
    }

    public boolean onEventTriggered(Level level0, int int1) {
        if (int1 == 1) {
            if (level0.isClientSide) {
                this.spawnDelay = this.minSpawnDelay;
            }
            return true;
        } else {
            return false;
        }
    }

    protected void setNextSpawnData(@Nullable Level level0, BlockPos blockPos1, SpawnData spawnData2) {
        this.nextSpawnData = spawnData2;
    }

    private SpawnData getOrCreateNextSpawnData(@Nullable Level level0, RandomSource randomSource1, BlockPos blockPos2) {
        if (this.nextSpawnData != null) {
            return this.nextSpawnData;
        } else {
            this.setNextSpawnData(level0, blockPos2, (SpawnData) this.spawnPotentials.m_216829_(randomSource1).map(WeightedEntry.Wrapper::m_146310_).orElseGet(SpawnData::new));
            return this.nextSpawnData;
        }
    }

    public abstract void broadcastEvent(Level var1, BlockPos var2, int var3);

    public double getSpin() {
        return this.spin;
    }

    public double getoSpin() {
        return this.oSpin;
    }
}