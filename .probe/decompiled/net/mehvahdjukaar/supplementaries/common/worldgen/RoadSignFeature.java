package net.mehvahdjukaar.supplementaries.common.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NoticeBoardBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlockGeneratorBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.NoticeBoardBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.phys.BlockHitResult;

public class RoadSignFeature extends Feature<RoadSignFeature.Config> {

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    private static final BlockState PATH = Blocks.DIRT_PATH.defaultBlockState();

    private static final BlockState SANDSTONE_PATH = Blocks.SMOOTH_SANDSTONE.defaultBlockState();

    public RoadSignFeature(Codec<RoadSignFeature.Config> codec) {
        super(codec);
    }

    public static boolean isNotSolid(LevelAccessor world, BlockPos pos) {
        return !world.m_7433_(pos, state -> state.m_60796_(world, pos));
    }

    @Override
    public boolean place(FeaturePlaceContext<RoadSignFeature.Config> context) {
        WorldGenLevel reader = context.level();
        RandomSource rand = context.random();
        BlockPos pos = context.origin();
        RoadSignFeature.Config c = context.config();
        pos = pos.below();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) != 2 || Math.abs(j) != 2) {
                    for (int k = 1; k <= 4; k++) {
                        if (Math.abs(i) != 2 && Math.abs(j) != 2 || k != 1) {
                            reader.m_7731_(pos.offset(i, k, j), ((Block) ModRegistry.STRUCTURE_TEMP.get()).defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
        float humidity = SuppPlatformStuff.getDownfall((Biome) reader.m_204166_(pos).value());
        for (int i = -2; i <= 2; i++) {
            for (int jx = -2; jx <= 2; jx++) {
                if (Math.abs(i) != 2 || Math.abs(jx) != 2) {
                    reader.m_7731_(pos.offset(i, -1, jx), c.cobble, 2);
                    BlockPos pathPos = pos.offset(i, 0, jx);
                    double dist = pos.m_203198_((double) pathPos.m_123341_(), (double) pathPos.m_123342_(), (double) pathPos.m_123343_()) / 5.2F;
                    if (!((double) rand.nextFloat() < dist - 0.15)) {
                        boolean m = (double) humidity * 0.75 > (double) rand.nextFloat();
                        reader.m_7731_(pathPos, m ? c.mossyCobble : c.cobble, 2);
                    }
                }
            }
        }
        boolean m = (double) humidity * 0.75 > (double) rand.nextFloat();
        pos = pos.above();
        reader.m_7731_(pos, m ? c.mossyWall : c.wall, 2);
        pos = pos.above();
        reader.m_7731_(pos, c.fence, 2);
        pos = pos.above();
        reader.m_7731_(pos, c.fence, 2);
        reader.m_7731_(pos.above(), ((Block) ModRegistry.BLOCK_GENERATOR.get()).defaultBlockState(), 2);
        if (reader.m_7702_(pos.above()) instanceof BlockGeneratorBlockTile t) {
            t.setConfig(c);
        } else {
            Supplementaries.LOGGER.error("Failed to get Road Sign Block Entity during generation. How did this happen?");
        }
        return true;
    }

    public static void applyPostProcess(RoadSignFeature.Config c, ServerLevel level, BlockPos generatorPos, List<StructureLocator.LocatedStruct> foundVillages) {
        RoadSignFeature.RandomState r = c.randomState;
        BlockState topState = c.trapdoor;
        BlockPos pos = generatorPos.below(2);
        List<Pair<Integer, BlockPos>> villages = new ArrayList();
        for (StructureLocator.LocatedStruct f : foundVillages) {
            villages.add(Pair.of((int) Mth.sqrt((float) f.pos().m_203198_((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_())), f.pos()));
        }
        boolean inVillage = false;
        if (inVillage) {
            Holder<Biome> b = level.m_204166_(pos);
            BlockState replace = b.is(BiomeTags.HAS_VILLAGE_DESERT) ? SANDSTONE_PATH : PATH;
            replaceCobbleWithPath(c, level, pos, replace);
        }
        if (!villages.isEmpty()) {
            RandomSource rand = level.f_46441_;
            boolean twoSigns = true;
            BlockPos village1;
            BlockPos village2;
            int dist1;
            int dist2;
            if (villages.size() != 1 && (!(r.doubleSignChance > rand.nextFloat()) || (Integer) ((Pair) villages.get(0)).getFirst() <= 192)) {
                boolean inv = rand.nextBoolean();
                dist1 = (Integer) ((Pair) villages.get(inv ? 0 : 1)).getFirst();
                village1 = (BlockPos) ((Pair) villages.get(inv ? 0 : 1)).getSecond();
                dist2 = (Integer) ((Pair) villages.get(inv ? 1 : 0)).getFirst();
                village2 = (BlockPos) ((Pair) villages.get(inv ? 1 : 0)).getSecond();
            } else {
                dist1 = (Integer) ((Pair) villages.get(0)).getFirst();
                village1 = (BlockPos) ((Pair) villages.get(0)).getSecond();
                dist2 = dist1;
                village2 = village1;
                twoSigns = false;
            }
            level.m_46597_(pos, ((Block) ModRegistry.SIGN_POST.get()).defaultBlockState());
            if (level.m_7702_(pos) instanceof SignPostBlockTile tile) {
                tile.setHeldBlock(c.fence);
                boolean left = rand.nextBoolean();
                SignPostBlockTile.Sign up = tile.getSignUp();
                SignPostBlockTile.Sign down = tile.getSignDown();
                up.setActive(true);
                up.setLeft(left);
                up.setWoodType(c.signWood);
                up.pointToward(tile.m_58899_(), village1);
                down.setActive(twoSigns);
                down.setLeft(left);
                down.setWoodType(c.signWood);
                down.pointToward(tile.m_58899_(), village2);
                if (Math.abs(up.yaw() - down.yaw()) > 90.0F) {
                    down.toggleDirection();
                    down.pointToward(tile.m_58899_(), village2);
                }
                if ((Boolean) CommonConfigs.Building.WAY_SIGN_DISTANCE_TEXT.get()) {
                    tile.getTextHolder(0).setMessage(0, getSignText(dist1));
                    if (twoSigns) {
                        tile.getTextHolder(1).setMessage(0, getSignText(dist2));
                    }
                }
                float yaw = Mth.wrapDegrees(90.0F + 360.0F * MthUtils.averageAngles((180.0F - up.yaw()) / 360.0F, (180.0F - down.yaw()) / 360.0F));
                Direction backDir = Direction.fromYRot((double) yaw);
                float diff = Mth.degreesDifference(yaw, backDir.toYRot());
                Direction sideDir = diff < 0.0F ? backDir.getClockWise() : backDir.getCounterClockWise();
                ArrayList<Direction> lampDir = new ArrayList();
                lampDir.add(backDir.getOpposite());
                lampDir.add(backDir.getOpposite());
                lampDir.add(backDir.getOpposite());
                if (Math.abs(diff) > 30.0F) {
                    lampDir.add(sideDir.getOpposite());
                }
                boolean hasGroundLantern = false;
                boolean hasFirefly = false;
                if (rand.nextFloat() < r.stoneChance && Mth.degreesDifferenceAbs(tile.getPointingYaw(true) + 180.0F, yaw) > 70.0F) {
                    BlockPos stonePos = pos.below().offset(backDir.getNormal());
                    if (rand.nextBoolean()) {
                        level.m_7731_(stonePos, c.stoneSlab, 2);
                    } else {
                        level.m_7731_(stonePos, (BlockState) c.stoneStairs.m_61124_(StairBlock.FACING, sideDir), 2);
                    }
                    stonePos = stonePos.offset(sideDir.getNormal());
                    level.m_7731_(stonePos, c.stone, 2);
                    if (rand.nextFloat() < r.stoneLanternChance) {
                        level.m_7731_(stonePos.above(), hasFirefly ? c.lanternDown : c.lanternDown, 3);
                        hasGroundLantern = true;
                    }
                    stonePos = stonePos.offset(sideDir.getNormal());
                    if (!isNotSolid(level, stonePos.below())) {
                        if (rand.nextBoolean()) {
                            level.m_7731_(stonePos, c.stoneSlab, 2);
                        } else {
                            level.m_7731_(stonePos, (BlockState) c.stoneStairs.m_61124_(StairBlock.FACING, sideDir.getOpposite()), 2);
                        }
                    }
                }
                if (!hasGroundLantern) {
                    pos = pos.above(2);
                    BlockState light = hasFirefly ? c.lanternUp : c.lanternUp;
                    if (rand.nextFloat() < r.candleHolderChance) {
                        light = (BlockState) ((BlockState) c.candleHolder.m_61124_(CandleHolderBlock.LIT, true)).m_61124_(CandleHolderBlock.FACE, AttachFace.CEILING);
                    }
                    Direction dir = (Direction) lampDir.get(rand.nextInt(lampDir.size()));
                    boolean doubleSided = r.doubleLanternChance > rand.nextFloat();
                    if (doubleSided) {
                        dir = dir.getClockWise();
                    }
                    Block wl = (Block) CompatObjects.WALL_LANTERN.get();
                    if (wl != null && rand.nextFloat() < r.wallLanternChance) {
                        topState = rand.nextFloat() < r.trapdoorChance ? c.trapdoor : AIR;
                        placeWallLantern(c.lanternDown, level, dir, wl, pos.below());
                        if (doubleSided) {
                            placeWallLantern(c.lanternDown, level, dir.getOpposite(), wl, pos.below());
                        }
                    } else {
                        boolean isTrapdoor = r.trapdoorChance > rand.nextFloat();
                        if (!isTrapdoor) {
                            topState = c.fence;
                        }
                        if (doubleSided) {
                            BlockPos backPos = pos.relative(dir.getOpposite());
                            level.m_7731_(backPos, isTrapdoor ? c.trapdoor : c.fence, 2);
                            if (r.logChance > rand.nextFloat()) {
                                topState = isTrapdoor ? c.slab : c.log;
                            }
                            level.m_7731_(backPos.below(), light, 3);
                        }
                        pos = pos.relative(dir);
                        BlockState frontState = isTrapdoor ? c.trapdoor : c.fence;
                        level.m_7731_(pos, frontState, 2);
                        level.m_7731_(pos.below(), light, 3);
                    }
                }
            }
        } else {
            ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
            CompoundTag com = new CompoundTag();
            ListTag listTag = new ListTag();
            listTag.add(StringTag.valueOf("nothing here but monsters\n\n\n"));
            com.put("pages", listTag);
            book.setTag(com);
            BlockPos belowPos = generatorPos.below(2);
            level.m_46597_(belowPos, (BlockState) ((BlockState) ((Block) ModRegistry.NOTICE_BOARD.get()).defaultBlockState().m_61124_(NoticeBoardBlock.HAS_BOOK, true)).m_61124_(NoticeBoardBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(level.f_46441_)));
            if (level.m_7702_(belowPos) instanceof NoticeBoardBlockTile board) {
                board.setDisplayedItem(book);
            }
        }
        level.m_7731_(generatorPos, topState, 3);
    }

    private static Component getSignText(int d) {
        int s;
        if (d < 100) {
            s = 10;
        } else if (d < 2000) {
            s = 100;
        } else {
            s = 1000;
        }
        return Component.translatable("message.supplementaries.road_sign", (d + s / 2) / s * s);
    }

    private static void replaceCobbleWithPath(RoadSignFeature.Config c, Level world, BlockPos pos, BlockState path) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if ((Math.abs(i) != 2 || Math.abs(j) != 2) && (i != 0 || j != 0)) {
                    BlockPos pathPos = pos.offset(i, -2, j);
                    BlockState state = world.getBlockState(pathPos);
                    if (state.m_60713_(c.cobble.m_60734_()) || state.m_60713_(c.mossyCobble.m_60734_())) {
                        world.setBlock(pathPos, path, 2);
                    }
                }
            }
        }
    }

    private static void placeWallLantern(BlockState lanternState, ServerLevel level, Direction dir, Block wallLantern, BlockPos pos) {
        pos = pos.relative(dir);
        BlockState state = wallLantern.getStateForPlacement(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, wallLantern.asItem().getDefaultInstance(), new BlockHitResult(pos.getCenter(), dir, pos, false)));
        if (state != null) {
            level.m_46597_(pos, state);
        }
        if (level.m_7702_(pos) instanceof IBlockHolder tt) {
            tt.setHeldBlock(lanternState);
        }
    }

    public static record Config(RoadSignFeature.RandomState randomState, WoodType postWood, WoodType signWood, BlockState fence, BlockState trapdoor, BlockState slab, BlockState log, BlockState cobble, BlockState mossyCobble, BlockState wall, BlockState mossyWall, BlockState lanternUp, BlockState lanternDown, BlockState candleHolder, BlockState stone, BlockState stoneSlab, BlockState stoneStairs, String invalidMessage) implements FeatureConfiguration {

        public static final Codec<RoadSignFeature.Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(RoadSignFeature.RandomState.CODEC.fieldOf("random_state").forGetter(RoadSignFeature.Config::randomState), WoodType.CODEC.fieldOf("post_wood").forGetter(RoadSignFeature.Config::postWood), WoodType.CODEC.fieldOf("sign_wood").forGetter(RoadSignFeature.Config::signWood), BlockState.CODEC.fieldOf("cobble").forGetter(RoadSignFeature.Config::cobble), BlockState.CODEC.fieldOf("mossy_cobble").forGetter(RoadSignFeature.Config::mossyCobble), BlockState.CODEC.fieldOf("wall").forGetter(RoadSignFeature.Config::wall), BlockState.CODEC.fieldOf("mossy_wall").forGetter(RoadSignFeature.Config::mossyWall), BlockState.CODEC.fieldOf("lantern_up").forGetter(RoadSignFeature.Config::lanternUp), BlockState.CODEC.fieldOf("lantern_down").forGetter(RoadSignFeature.Config::lanternDown), BlockState.CODEC.fieldOf("candle_holder").forGetter(RoadSignFeature.Config::candleHolder), BlockState.CODEC.fieldOf("stone").forGetter(RoadSignFeature.Config::stone), BlockState.CODEC.fieldOf("stone_slab").forGetter(RoadSignFeature.Config::stoneSlab), BlockState.CODEC.fieldOf("stone_stairs").forGetter(RoadSignFeature.Config::stoneStairs)).apply(instance, RoadSignFeature.Config::of)).comapFlatMap(s -> s.invalidMessage != null ? DataResult.error(() -> s.invalidMessage) : DataResult.success(s), Function.identity());

        private static RoadSignFeature.Config of(RoadSignFeature.RandomState randomState, WoodType postWood, WoodType signWood, BlockState cobble, BlockState mossyCobble, BlockState wall, BlockState mossyWall, BlockState lanternUp, BlockState lanternDown, BlockState candleHolder, BlockState stone, BlockState stoneSlab, BlockState stoneStairs) {
            String message = null;
            Block fence = postWood.getBlockOfThis("fence");
            if (fence == null) {
                message = "Post wood type does not have a fence";
                fence = Blocks.AIR;
            }
            Block trapdoor = postWood.getBlockOfThis("trapdoor");
            if (trapdoor == null) {
                message = "Post wood type does not have a trapdoor";
                trapdoor = Blocks.AIR;
            }
            Block slab = postWood.getBlockOfThis("slab");
            if (slab == null) {
                message = "Post wood type does not have a slab";
                slab = Blocks.AIR;
            }
            Block log = postWood.getBlockOfThis("stripped_log");
            if (log == null) {
                message = "Post wood type does not have a valid stripped log";
                log = Blocks.AIR;
            }
            if (!(stoneSlab.m_60734_() instanceof SlabBlock)) {
                message = "Stone slab must be a SlabBlock, was " + stoneSlab;
            }
            if (!(stoneStairs.m_60734_() instanceof StairBlock)) {
                message = "Stone slab must be a StairBlock, was " + stoneStairs;
            }
            if (!candleHolder.m_61138_(CandleHolderBlock.FACE) || !candleHolder.m_61138_(CandleHolderBlock.LIT)) {
                message = "Candle holder block has to have a face and lit property";
            }
            return new RoadSignFeature.Config(randomState, postWood, signWood, fence.defaultBlockState(), trapdoor.defaultBlockState(), slab.defaultBlockState(), log.defaultBlockState(), cobble, mossyCobble, wall, mossyWall, lanternUp, lanternDown, candleHolder, stone, stoneSlab, stoneStairs, message);
        }
    }

    private static record RandomState(float doubleSignChance, float stoneChance, float stoneLanternChance, float candleHolderChance, float wallLanternChance, float doubleLanternChance, float trapdoorChance, float logChance) {

        public static final Codec<RoadSignFeature.RandomState> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("double_sign_chance").forGetter(RoadSignFeature.RandomState::doubleSignChance), Codec.floatRange(0.0F, 1.0F).fieldOf("stone_chance").forGetter(RoadSignFeature.RandomState::stoneChance), Codec.floatRange(0.0F, 1.0F).fieldOf("stone_lantern_chance").forGetter(RoadSignFeature.RandomState::stoneLanternChance), Codec.floatRange(0.0F, 1.0F).fieldOf("candle_holder_chance").forGetter(RoadSignFeature.RandomState::candleHolderChance), Codec.floatRange(0.0F, 1.0F).fieldOf("wall_lantern_chance").forGetter(RoadSignFeature.RandomState::wallLanternChance), Codec.floatRange(0.0F, 1.0F).fieldOf("double_lantern_chance").forGetter(RoadSignFeature.RandomState::doubleLanternChance), Codec.floatRange(0.0F, 1.0F).fieldOf("trapdoor_chance").forGetter(RoadSignFeature.RandomState::trapdoorChance), Codec.floatRange(0.0F, 1.0F).fieldOf("log_chance").forGetter(RoadSignFeature.RandomState::logChance)).apply(instance, RoadSignFeature.RandomState::new));
    }
}