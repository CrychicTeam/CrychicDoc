package noppes.npcs;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.ChunkMapMixin;
import noppes.npcs.mixin.PersistentEntitySectionManagerMixin;
import noppes.npcs.mixin.ServerLevelMixin;

public class NPCSpawning {

    public static void findChunksForSpawning(ServerLevel level) {
        if (!SpawnController.instance.data.isEmpty() && level.m_46467_() % 400L == 0L) {
            EntitySectionStorage<Entity> sectionManager = ((PersistentEntitySectionManagerMixin) ((ServerLevelMixin) level).entityManager()).sectionStorage();
            ChunkMap chunkManager = level.getChunkSource().chunkMap;
            List<ChunkHolder> list = new ArrayList(((ChunkMapMixin) chunkManager).visibleChunkMap().values());
            Collections.shuffle(list);
            for (ChunkHolder chunkHolder : list) {
                LevelChunk levelchunk = chunkHolder.getTickingChunk();
                if (levelchunk == null) {
                    break;
                }
                ChunkPos pos = levelchunk.m_7697_();
                Biome biome = (Biome) level.m_204166_(pos.getWorldPosition()).value();
                if (SpawnController.instance.hasSpawnList(ForgeRegistries.BIOMES.getKey(biome))) {
                    AABB bb = new AABB((double) pos.getMinBlockX(), 0.0, (double) pos.getMinBlockZ(), (double) pos.getMaxBlockX(), (double) level.m_151558_(), (double) pos.getMaxBlockZ());
                    List<Entity> entities = Lists.newArrayList();
                    sectionManager.getEntities(EntityType.PLAYER, bb.inflate(4.0), e -> {
                        entities.add(e);
                        return AbortableIterationConsumer.Continuation.CONTINUE;
                    });
                    if (entities.isEmpty()) {
                        sectionManager.getEntities(CustomEntities.entityCustomNpc, bb, e -> {
                            entities.add(e);
                            return AbortableIterationConsumer.Continuation.CONTINUE;
                        });
                        if (entities.size() < CustomNpcs.NpcNaturalSpawningChunkLimit) {
                            spawnChunk(level, levelchunk);
                        }
                    }
                }
            }
        }
    }

    private static void spawnChunk(ServerLevel level, LevelChunk chunk) {
        BlockPos chunkposition = getChunk(level, chunk);
        int j1 = chunkposition.m_123341_();
        int k1 = chunkposition.m_123342_();
        int l1 = chunkposition.m_123343_();
        for (int i = 0; i < 3; i++) {
            byte b1 = 6;
            int x = j1 + (level.f_46441_.nextInt(b1) - level.f_46441_.nextInt(b1));
            int z = l1 + (level.f_46441_.nextInt(b1) - level.f_46441_.nextInt(b1));
            BlockPos pos = new BlockPos(x, k1, z);
            ResourceLocation name = ForgeRegistries.BIOMES.getKey((Biome) level.m_204166_(pos).value());
            SpawnData data = SpawnController.instance.getRandomSpawnData(name);
            if (data != null && !data.data.isEmpty() && canCreatureTypeSpawnAtLocation(data, level, pos)) {
                spawnData(data, level, pos);
            }
        }
    }

    public static int countNPCs(ServerLevel level) {
        int count = 0;
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof EntityNPCInterface) {
                count++;
            }
        }
        return count;
    }

    private static BlockPos getChunk(Level level, LevelChunk chunk) {
        ChunkPos chunkpos = chunk.m_7697_();
        int i = chunkpos.getMinBlockX() + level.random.nextInt(16);
        int j = chunkpos.getMinBlockZ() + level.random.nextInt(16);
        int k = chunk.m_5885_(Heightmap.Types.WORLD_SURFACE, i, j) + 1;
        int l = level.random.nextInt(k + 1);
        return new BlockPos(i, l, j);
    }

    public static void performLevelGenSpawning(Level level, Biome biome, int x, int z, RandomSource rand) {
        if (!(biome.getMobSettings().getCreatureProbability() >= 1.0F) && !(biome.getMobSettings().getCreatureProbability() < 0.0F) && SpawnController.instance.hasSpawnList(ForgeRegistries.BIOMES.getKey(biome))) {
            int tries = 0;
            while (rand.nextFloat() < biome.getMobSettings().getCreatureProbability()) {
                if (++tries > 20) {
                    break;
                }
                SpawnData data = SpawnController.instance.getRandomSpawnData(ForgeRegistries.BIOMES.getKey(biome));
                int size = 16;
                int j1 = x + rand.nextInt(size);
                int k1 = z + rand.nextInt(size);
                int l1 = j1;
                int i2 = k1;
                for (int k2 = 0; k2 < 4; k2++) {
                    BlockPos pos = getTopNonCollidingPos(level, CustomEntities.entityCustomNpc, 0, k1);
                    if (canCreatureTypeSpawnAtLocation(data, level, pos)) {
                        if (spawnData(data, level, pos)) {
                            break;
                        }
                    } else {
                        j1 += rand.nextInt(5) - rand.nextInt(5);
                        for (k1 += rand.nextInt(5) - rand.nextInt(5); j1 < x || j1 >= x + size || k1 < z || k1 >= z + size; k1 = i2 + rand.nextInt(5) - rand.nextInt(5)) {
                            j1 = l1 + rand.nextInt(5) - rand.nextInt(5);
                        }
                    }
                }
            }
        }
    }

    private static boolean spawnData(SpawnData data, Level level, BlockPos pos) {
        try {
            CompoundTag nbt = data.getCompound(1);
            if (nbt == null) {
                return false;
            }
            Entity entity = (Entity) EntityType.create(nbt, level).orElse(null);
            if (entity == null || !(entity instanceof Mob entityliving)) {
                return false;
            }
            if (entity instanceof EntityCustomNpc npc) {
                npc.stats.spawnCycle = 4;
                npc.stats.respawnTime = 0;
                npc.ais.returnToStart = false;
                npc.ais.setStartPos(pos);
            }
            entity.moveTo((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, level.random.nextFloat() * 360.0F, 0.0F);
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
        if (level instanceof ServerLevel && !ForgeEventFactory.checkSpawnPosition(entityliving, (ServerLevel) level, MobSpawnType.NATURAL)) {
            return false;
        } else {
            level.getServer().m_18707_(() -> level.m_7967_(entityliving));
            return true;
        }
    }

    public static boolean canCreatureTypeSpawnAtLocation(SpawnData data, Level level, BlockPos pos) {
        if (!level.getWorldBorder().isWithinBounds(pos) || !level.m_45772_(CustomEntities.entityCustomNpc.getAABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_()))) {
            return false;
        } else if ((data.type != 1 || level.m_7146_(pos) <= 8) && (data.type != 2 || level.m_7146_(pos) > 8)) {
            BlockState state = level.getBlockState(pos);
            Block block = state.m_60734_();
            if (data.liquid) {
                return state.m_278721_() && level.getBlockState(pos.below()).m_278721_() && !level.getBlockState(pos.above()).m_60796_(level, pos.above());
            } else {
                BlockPos blockpos1 = pos.below();
                BlockState state1 = level.getBlockState(blockpos1);
                Block block1 = state1.m_60734_();
                boolean flag = block1 != Blocks.BEDROCK && block1 != Blocks.BARRIER;
                BlockPos down = blockpos1.below();
                flag |= level.getBlockState(down).m_60734_().isValidSpawn(level.getBlockState(down), level, down, SpawnPlacements.Type.ON_GROUND, CustomEntities.entityCustomNpc);
                return flag && !state.m_60803_() && !state.m_278721_() && !level.getBlockState(pos.above()).m_60803_();
            }
        } else {
            return false;
        }
    }

    private static BlockPos getTopNonCollidingPos(LevelReader p_208498_0_, EntityType<?> p_208498_1_, int p_208498_2_, int p_208498_3_) {
        int i = p_208498_0_.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, p_208498_2_, p_208498_3_);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(p_208498_2_, i, p_208498_3_);
        if (p_208498_0_.dimensionType().hasCeiling()) {
            do {
                blockpos$mutable.move(Direction.DOWN);
            } while (!p_208498_0_.m_8055_(blockpos$mutable).m_60795_());
            do {
                blockpos$mutable.move(Direction.DOWN);
            } while (p_208498_0_.m_8055_(blockpos$mutable).m_60795_() && blockpos$mutable.m_123342_() > 0);
        }
        BlockPos blockpos = blockpos$mutable.m_7495_();
        return p_208498_0_.m_8055_(blockpos).m_60647_(p_208498_0_, blockpos, PathComputationType.LAND) ? blockpos : blockpos$mutable.immutable();
    }
}