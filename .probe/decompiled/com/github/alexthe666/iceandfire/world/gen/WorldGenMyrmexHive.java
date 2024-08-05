package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockMyrmexBiolight;
import com.github.alexthe666.iceandfire.block.BlockMyrmexConnectedResin;
import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexQueen;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenMyrmexHive extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    private static final BlockState DESERT_RESIN = IafBlockRegistry.MYRMEX_DESERT_RESIN.get().defaultBlockState();

    private static final BlockState STICKY_DESERT_RESIN = IafBlockRegistry.MYRMEX_DESERT_RESIN_STICKY.get().defaultBlockState();

    private static final BlockState JUNGLE_RESIN = IafBlockRegistry.MYRMEX_JUNGLE_RESIN.get().defaultBlockState();

    private static final BlockState STICKY_JUNGLE_RESIN = IafBlockRegistry.MYRMEX_JUNGLE_RESIN_STICKY.get().defaultBlockState();

    public MyrmexHive hive;

    private int entrances = 0;

    private int totalRooms;

    private boolean hasFoodRoom;

    private boolean hasNursery;

    private boolean small;

    private final boolean jungle;

    private BlockPos centerOfHive;

    public WorldGenMyrmexHive(boolean small, boolean jungle, Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
        this.small = small;
        this.jungle = jungle;
    }

    public boolean placeSmallGen(WorldGenLevel worldIn, RandomSource rand, BlockPos pos) {
        this.hasFoodRoom = false;
        this.hasNursery = false;
        this.totalRooms = 0;
        this.entrances = 0;
        this.centerOfHive = pos;
        this.generateMainRoom(worldIn, rand, pos);
        this.small = false;
        return false;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos pos = context.origin();
        if (!this.small) {
            if (rand.nextInt(IafConfig.myrmexColonyGenChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, pos) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, pos, this.getId())) {
                return false;
            }
            if (MyrmexWorldData.get(worldIn.m_6018_()) != null && MyrmexWorldData.get(worldIn.m_6018_()).getNearestHive(pos, 200) != null) {
                return false;
            }
        }
        if (!this.small && !worldIn.m_6425_(pos.below()).isEmpty()) {
            return false;
        } else {
            this.hasFoodRoom = false;
            this.hasNursery = false;
            this.totalRooms = 0;
            int down = Math.max(15, pos.m_123342_() - 20 + rand.nextInt(10));
            BlockPos undergroundPos = new BlockPos(pos.m_123341_(), down, pos.m_123343_());
            this.entrances = 0;
            this.centerOfHive = undergroundPos;
            this.generateMainRoom(worldIn, rand, undergroundPos);
            this.small = false;
            return true;
        }
    }

    private void generateMainRoom(ServerLevelAccessor world, RandomSource rand, BlockPos position) {
        this.hive = new MyrmexHive(world.getLevel(), position, 100);
        MyrmexWorldData.addHive(world.getLevel(), this.hive);
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        this.generateSphere(world, rand, position, 14, 7, resin, sticky_resin);
        this.generateSphere(world, rand, position, 12, 5, Blocks.AIR.defaultBlockState());
        this.decorateSphere(world, rand, position, 12, 5, WorldGenMyrmexHive.RoomType.QUEEN);
        this.generatePath(world, rand, position.relative(Direction.NORTH, 9).below(), 15 + rand.nextInt(10), Direction.NORTH, 100);
        this.generatePath(world, rand, position.relative(Direction.SOUTH, 9).below(), 15 + rand.nextInt(10), Direction.SOUTH, 100);
        this.generatePath(world, rand, position.relative(Direction.WEST, 9).below(), 15 + rand.nextInt(10), Direction.WEST, 100);
        this.generatePath(world, rand, position.relative(Direction.EAST, 9).below(), 15 + rand.nextInt(10), Direction.EAST, 100);
        if (!this.small) {
            EntityMyrmexQueen queen = new EntityMyrmexQueen(IafEntityRegistry.MYRMEX_QUEEN.get(), world.getLevel());
            BlockPos ground = MyrmexHive.getGroundedPos(world, position);
            queen.m_6518_(world, world.m_6436_(ground), MobSpawnType.CHUNK_GENERATION, null, null);
            queen.setHive(this.hive);
            queen.setJungleVariant(this.jungle);
            queen.m_19890_((double) ground.m_123341_() + 0.5, (double) ground.m_123342_() + 1.0, (double) ground.m_123343_() + 0.5, 0.0F, 0.0F);
            world.m_7967_(queen);
            for (int i = 0; i < 4 + rand.nextInt(3); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(), world.getLevel());
                myrmex.finalizeSpawn(world, world.m_6436_(ground), MobSpawnType.CHUNK_GENERATION, null, null);
                myrmex.setHive(this.hive);
                myrmex.m_19890_((double) ground.m_123341_() + 0.5, (double) ground.m_123342_() + 1.0, (double) ground.m_123343_() + 0.5, 0.0F, 0.0F);
                myrmex.setJungleVariant(this.jungle);
                world.m_7967_(myrmex);
            }
            for (int i = 0; i < 2 + rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSoldier(IafEntityRegistry.MYRMEX_SOLDIER.get(), world.getLevel());
                myrmex.finalizeSpawn(world, world.m_6436_(ground), MobSpawnType.CHUNK_GENERATION, null, null);
                myrmex.setHive(this.hive);
                myrmex.m_19890_((double) ground.m_123341_() + 0.5, (double) ground.m_123342_() + 1.0, (double) ground.m_123343_() + 0.5, 0.0F, 0.0F);
                myrmex.setJungleVariant(this.jungle);
                world.m_7967_(myrmex);
            }
            for (int i = 0; i < rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSentinel(IafEntityRegistry.MYRMEX_SENTINEL.get(), world.getLevel());
                myrmex.finalizeSpawn(world, world.m_6436_(ground), MobSpawnType.CHUNK_GENERATION, null, null);
                myrmex.setHive(this.hive);
                myrmex.m_19890_((double) ground.m_123341_() + 0.5, (double) ground.m_123342_() + 1.0, (double) ground.m_123343_() + 0.5, 0.0F, 0.0F);
                myrmex.setJungleVariant(this.jungle);
                world.m_7967_(myrmex);
            }
        }
    }

    private void generatePath(LevelAccessor world, RandomSource rand, BlockPos offset, int length, Direction direction, int roomChance) {
        if (roomChance != 0) {
            if (this.small) {
                length /= 2;
                if (this.entrances < 1) {
                    for (int i = 0; i < length; i++) {
                        this.generateCircle(world, rand, offset.relative(direction, i), 3, 5, direction);
                    }
                    this.generateEntrance(world, rand, offset.relative(direction, length), 4, 4, direction);
                } else if (this.totalRooms < 2) {
                    for (int i = 0; i < length; i++) {
                        this.generateCircle(world, rand, offset.relative(direction, i), 3, 5, direction);
                    }
                    this.generateRoom(world, rand, offset.relative(direction, length), 6, 4, roomChance / 2, direction);
                    for (int i = -3; i < 3; i++) {
                        this.generateCircleAir(world, rand, offset.relative(direction, i), 3, 5, direction);
                        this.generateCircleAir(world, rand, offset.relative(direction, length + i), 3, 5, direction);
                    }
                    this.totalRooms++;
                }
            } else if (rand.nextInt(100) < roomChance) {
                if ((this.entrances >= 3 || rand.nextInt(1 + this.entrances * 2) != 0 || !this.hasFoodRoom || !this.hasNursery || this.totalRooms <= 3) && this.entrances != 0) {
                    for (int i = 0; i < length; i++) {
                        this.generateCircle(world, rand, offset.relative(direction, i), 3, 5, direction);
                    }
                    for (int i = -3; i < 3; i++) {
                        this.generateCircleAir(world, rand, offset.relative(direction, length + i), 3, 5, direction);
                    }
                    this.totalRooms++;
                    this.generateRoom(world, rand, offset.relative(direction, length), 7, 4, roomChance / 2, direction);
                } else {
                    this.generateEntrance(world, rand, offset.relative(direction, 1), 4, 4, direction);
                }
            }
        }
    }

    private void generateRoom(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, int roomChance, Direction direction) {
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        WorldGenMyrmexHive.RoomType type = WorldGenMyrmexHive.RoomType.random(rand);
        if (!this.hasFoodRoom) {
            type = WorldGenMyrmexHive.RoomType.FOOD;
            this.hasFoodRoom = true;
        } else if (!this.hasNursery) {
            type = WorldGenMyrmexHive.RoomType.NURSERY;
            this.hasNursery = true;
        }
        this.generateSphereRespectResin(world, rand, position, size + 2, height + 2, resin, sticky_resin);
        this.generateSphere(world, rand, position, size, height - 1, Blocks.AIR.defaultBlockState());
        this.decorateSphere(world, rand, position, size, height - 1, type);
        this.hive.addRoom(position, type);
        if (!this.small) {
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.NORTH) {
                this.generatePath(world, rand, position.relative(Direction.NORTH, size - 2), 5 + rand.nextInt(20), Direction.NORTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.SOUTH) {
                this.generatePath(world, rand, position.relative(Direction.SOUTH, size - 2), 5 + rand.nextInt(20), Direction.SOUTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.WEST) {
                this.generatePath(world, rand, position.relative(Direction.WEST, size - 2), 5 + rand.nextInt(20), Direction.WEST, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.EAST) {
                this.generatePath(world, rand, position.relative(Direction.EAST, size - 2), 5 + rand.nextInt(20), Direction.EAST, roomChance);
            }
        }
    }

    private void generateEntrance(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        BlockPos up = position.above();
        this.hive.getEntranceBottoms().put(up, direction);
        while (up.m_123342_() < world.m_5452_(this.small ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : Heightmap.Types.WORLD_SURFACE_WG, up).m_123342_() && !world.m_8055_(up).m_204336_(BlockTags.LOGS)) {
            this.generateCircleRespectSky(world, rand, up, size, height, direction);
            up = up.above().relative(direction);
        }
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        this.generateSphereRespectAir(world, rand, up, size + 4, height + 2, resin, sticky_resin);
        this.generateSphere(world, rand, up.above(), size, height, Blocks.AIR.defaultBlockState());
        this.decorateSphere(world, rand, up.above(), size, height - 1, WorldGenMyrmexHive.RoomType.ENTERANCE);
        this.hive.getEntrances().put(up, direction);
        this.entrances++;
    }

    private void generateCircle(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size + 2;
        for (float i = 0.0F; i < (float) radius; i = (float) ((double) i + 0.5)) {
            for (float j = 0.0F; (double) j < (Math.PI * 2) * (double) i; j = (float) ((double) j + 0.5)) {
                int x = (int) Math.floor((double) (Mth.sin(j) * i));
                int z = (int) Math.floor((double) (Mth.cos(j) * i));
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    world.m_7731_(position.offset(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin, 2);
                } else {
                    world.m_7731_(position.offset(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin, 2);
                }
            }
        }
        radius -= 2;
        for (float i = 0.0F; i < (float) radius; i = (float) ((double) i + 0.5)) {
            for (float jx = 0.0F; (double) jx < (Math.PI * 2) * (double) i; jx = (float) ((double) jx + 0.5)) {
                int x = (int) Math.floor((double) (Mth.sin(jx) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F)));
                int z = (int) Math.floor((double) (Mth.cos(jx) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F)));
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    world.m_7731_(position.offset(x, z, 0), Blocks.AIR.defaultBlockState(), 2);
                } else {
                    world.m_7731_(position.offset(0, x, z), Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }
        this.decorateCircle(world, rand, position, size, height, direction);
    }

    private void generateCircleRespectSky(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        BlockState resin = this.jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = this.jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size + 2;
        for (float i = 0.0F; i < (float) radius; i = (float) ((double) i + 0.5)) {
            for (float j = 0.0F; (double) j < (Math.PI * 2) * (double) i; j = (float) ((double) j + 0.5)) {
                int x = (int) Math.floor((double) (Mth.sin(j) * i));
                int z = (int) Math.floor((double) (Mth.cos(j) * i));
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    if (!world.m_46861_(position.offset(x, z, 0))) {
                        world.m_7731_(position.offset(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin, 3);
                    }
                } else if (!world.m_46861_(position.offset(0, x, z))) {
                    world.m_7731_(position.offset(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin, 3);
                }
            }
        }
        radius -= 2;
        for (float i = 0.0F; i < (float) radius; i = (float) ((double) i + 0.5)) {
            for (float jx = 0.0F; (double) jx < (Math.PI * 2) * (double) i; jx = (float) ((double) jx + 0.5)) {
                int x = (int) Math.floor((double) (Mth.sin(jx) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F)));
                int z = (int) Math.floor((double) (Mth.cos(jx) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F)));
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    world.m_7731_(position.offset(x, z, 0), Blocks.AIR.defaultBlockState(), 3);
                } else {
                    world.m_7731_(position.offset(0, x, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        this.decorateCircle(world, rand, position, size, height, direction);
    }

    private void generateCircleAir(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        int radius = size;
        for (float i = 0.0F; i < (float) radius; i = (float) ((double) i + 0.5)) {
            for (float j = 0.0F; (double) j < (Math.PI * 2) * (double) i; j = (float) ((double) j + 0.5)) {
                int x = (int) Math.floor((double) (Mth.sin(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F)));
                int z = (int) Math.floor((double) (Mth.cos(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F)));
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    world.m_7731_(position.offset(x, z, 0), Blocks.AIR.defaultBlockState(), 2);
                } else {
                    world.m_7731_(position.offset(0, x, z), Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }
        this.decorateCircle(world, rand, position, size, height, direction);
    }

    public void generateSphere(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
            if (blockpos.m_123331_(position) <= (double) (f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F)) && !world.m_46859_(blockpos)) {
                world.m_7731_(blockpos, fill, 3);
            }
        }
    }

    public void generateSphere(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
            if (blockpos.m_123331_(position) <= (double) (f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F))) {
                world.m_7731_(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    public void generateSphereRespectResin(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
            if (blockpos.m_123331_(position) <= (double) (f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F)) && (!world.m_46859_(blockpos) || world.m_46859_(blockpos) && !this.hasResinUnder(blockpos, world))) {
                world.m_7731_(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    public void generateSphereRespectAir(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
            if (blockpos.m_123331_(position) <= (double) (f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F)) && !world.m_46859_(blockpos)) {
                world.m_7731_(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    private boolean hasResinUnder(BlockPos pos, LevelAccessor world) {
        BlockPos copy = pos.below();
        while (world.m_46859_(copy) && copy.m_123342_() > 1) {
            copy = copy.below();
        }
        return world.m_8055_(copy).m_60734_() instanceof BlockMyrmexResin || world.m_8055_(copy).m_60734_() instanceof BlockMyrmexConnectedResin;
    }

    private void decorateCircle(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        int radius = size + 2;
        for (float i = 0.0F; i < (float) radius; i = (float) ((double) i + 0.5)) {
            for (float j = 0.0F; (double) j < (Math.PI * 2) * (double) i; j = (float) ((double) j + 0.5)) {
                int x = (int) Math.floor((double) (Mth.sin(j) * i));
                int z = (int) Math.floor((double) (Mth.cos(j) * i));
                if (direction != Direction.WEST && direction != Direction.EAST) {
                    if (world.m_46859_(position.offset(x, z, 0))) {
                        this.decorate(world, position.offset(x, z, 0), position, size, rand, WorldGenMyrmexHive.RoomType.TUNNEL);
                    }
                    if (world.m_46859_(position.offset(0, x, z))) {
                        this.decorateTubers(world, position.offset(0, x, z), rand, WorldGenMyrmexHive.RoomType.TUNNEL);
                    }
                } else {
                    if (world.m_46859_(position.offset(0, x, z))) {
                        this.decorate(world, position.offset(0, x, z), position, size, rand, WorldGenMyrmexHive.RoomType.TUNNEL);
                    }
                    if (world.m_46859_(position.offset(0, x, z))) {
                        this.decorateTubers(world, position.offset(0, x, z), rand, WorldGenMyrmexHive.RoomType.TUNNEL);
                    }
                }
            }
        }
    }

    private void decorateSphere(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, WorldGenMyrmexHive.RoomType roomType) {
        int ySize = rand.nextInt(2);
        int j = size + rand.nextInt(2);
        int k = height + ySize;
        int l = size + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k + 1, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
            if (blockpos.m_123331_(position) <= (double) (f * f)) {
                if (world.m_8055_(blockpos.below()).m_60815_() && world.m_46859_(blockpos)) {
                    this.decorate(world, blockpos, position, size, rand, roomType);
                }
                if (world.m_46859_(blockpos)) {
                    this.decorateTubers(world, blockpos, rand, roomType);
                }
            }
        }
    }

    private void decorate(LevelAccessor world, BlockPos blockpos, BlockPos center, int size, RandomSource random, WorldGenMyrmexHive.RoomType roomType) {
        switch(roomType) {
            case FOOD:
                if (random.nextInt(45) == 0 && world.m_8055_(blockpos.below()).m_60734_() instanceof BlockMyrmexResin) {
                    WorldGenMyrmexDecoration.generateSkeleton(world, blockpos, center, size, random);
                }
                if (random.nextInt(13) == 0) {
                    WorldGenMyrmexDecoration.generateLeaves(world, blockpos, center, size, random, this.jungle);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generatePumpkins(world, blockpos, center, size, random, this.jungle);
                }
                if (random.nextInt(6) == 0) {
                    WorldGenMyrmexDecoration.generateMushrooms(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, this.jungle, this.jungle ? WorldGenMyrmexDecoration.JUNGLE_MYRMEX_FOOD_CHEST : WorldGenMyrmexDecoration.DESERT_MYRMEX_FOOD_CHEST);
                }
            case NURSERY:
            default:
                break;
            case SHINY:
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateGold(world, blockpos, center, size, random);
                }
                break;
            case TRASH:
                if (random.nextInt(24) == 0) {
                    WorldGenMyrmexDecoration.generateTrashHeap(world, blockpos, center, size, random);
                }
                if (random.nextBoolean()) {
                    WorldGenMyrmexDecoration.generateTrashOre(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, this.jungle, WorldGenMyrmexDecoration.MYRMEX_TRASH_CHEST);
                }
        }
    }

    private void decorateTubers(LevelAccessor world, BlockPos blockpos, RandomSource random, WorldGenMyrmexHive.RoomType roomType) {
        if (world.m_8055_(blockpos.above()).m_60815_() && random.nextInt(roomType != WorldGenMyrmexHive.RoomType.ENTERANCE && roomType != WorldGenMyrmexHive.RoomType.TUNNEL ? 6 : 20) == 0) {
            int tuberLength = roomType != WorldGenMyrmexHive.RoomType.ENTERANCE && roomType != WorldGenMyrmexHive.RoomType.TUNNEL ? (roomType == WorldGenMyrmexHive.RoomType.QUEEN ? 1 + random.nextInt(5) : 1 + random.nextInt(3)) : 1;
            for (int i = 0; i < tuberLength; i++) {
                if (world.m_46859_(blockpos.below(i))) {
                    boolean connected = i != tuberLength - 1;
                    world.m_7731_(blockpos.below(i), this.jungle ? (BlockState) IafBlockRegistry.MYRMEX_JUNGLE_BIOLIGHT.get().defaultBlockState().m_61124_(BlockMyrmexBiolight.CONNECTED_DOWN, connected) : (BlockState) IafBlockRegistry.MYRMEX_DESERT_BIOLIGHT.get().defaultBlockState().m_61124_(BlockMyrmexBiolight.CONNECTED_DOWN, connected), 2);
                }
            }
        }
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "myrmex_hive";
    }

    public static enum RoomType {

        DEFAULT(false),
        TUNNEL(false),
        ENTERANCE(false),
        QUEEN(false),
        FOOD(true),
        EMPTY(true),
        NURSERY(true),
        SHINY(true),
        TRASH(true);

        boolean random;

        private RoomType(boolean random) {
            this.random = random;
        }

        public static WorldGenMyrmexHive.RoomType random(RandomSource rand) {
            List<WorldGenMyrmexHive.RoomType> list = new ArrayList();
            for (WorldGenMyrmexHive.RoomType type : values()) {
                if (type.random) {
                    list.add(type);
                }
            }
            return (WorldGenMyrmexHive.RoomType) list.get(rand.nextInt(list.size()));
        }
    }
}