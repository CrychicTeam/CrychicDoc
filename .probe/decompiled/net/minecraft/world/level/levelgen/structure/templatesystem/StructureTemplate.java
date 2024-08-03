package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.IdMapper;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

public class StructureTemplate {

    public static final String PALETTE_TAG = "palette";

    public static final String PALETTE_LIST_TAG = "palettes";

    public static final String ENTITIES_TAG = "entities";

    public static final String BLOCKS_TAG = "blocks";

    public static final String BLOCK_TAG_POS = "pos";

    public static final String BLOCK_TAG_STATE = "state";

    public static final String BLOCK_TAG_NBT = "nbt";

    public static final String ENTITY_TAG_POS = "pos";

    public static final String ENTITY_TAG_BLOCKPOS = "blockPos";

    public static final String ENTITY_TAG_NBT = "nbt";

    public static final String SIZE_TAG = "size";

    private final List<StructureTemplate.Palette> palettes = Lists.newArrayList();

    private final List<StructureTemplate.StructureEntityInfo> entityInfoList = Lists.newArrayList();

    private Vec3i size = Vec3i.ZERO;

    private String author = "?";

    public Vec3i getSize() {
        return this.size;
    }

    public void setAuthor(String string0) {
        this.author = string0;
    }

    public String getAuthor() {
        return this.author;
    }

    public void fillFromWorld(Level level0, BlockPos blockPos1, Vec3i vecI2, boolean boolean3, @Nullable Block block4) {
        if (vecI2.getX() >= 1 && vecI2.getY() >= 1 && vecI2.getZ() >= 1) {
            BlockPos $$5 = blockPos1.offset(vecI2).offset(-1, -1, -1);
            List<StructureTemplate.StructureBlockInfo> $$6 = Lists.newArrayList();
            List<StructureTemplate.StructureBlockInfo> $$7 = Lists.newArrayList();
            List<StructureTemplate.StructureBlockInfo> $$8 = Lists.newArrayList();
            BlockPos $$9 = new BlockPos(Math.min(blockPos1.m_123341_(), $$5.m_123341_()), Math.min(blockPos1.m_123342_(), $$5.m_123342_()), Math.min(blockPos1.m_123343_(), $$5.m_123343_()));
            BlockPos $$10 = new BlockPos(Math.max(blockPos1.m_123341_(), $$5.m_123341_()), Math.max(blockPos1.m_123342_(), $$5.m_123342_()), Math.max(blockPos1.m_123343_(), $$5.m_123343_()));
            this.size = vecI2;
            for (BlockPos $$11 : BlockPos.betweenClosed($$9, $$10)) {
                BlockPos $$12 = $$11.subtract($$9);
                BlockState $$13 = level0.getBlockState($$11);
                if (block4 == null || !$$13.m_60713_(block4)) {
                    BlockEntity $$14 = level0.getBlockEntity($$11);
                    StructureTemplate.StructureBlockInfo $$15;
                    if ($$14 != null) {
                        $$15 = new StructureTemplate.StructureBlockInfo($$12, $$13, $$14.saveWithId());
                    } else {
                        $$15 = new StructureTemplate.StructureBlockInfo($$12, $$13, null);
                    }
                    addToLists($$15, $$6, $$7, $$8);
                }
            }
            List<StructureTemplate.StructureBlockInfo> $$17 = buildInfoList($$6, $$7, $$8);
            this.palettes.clear();
            this.palettes.add(new StructureTemplate.Palette($$17));
            if (boolean3) {
                this.fillEntityList(level0, $$9, $$10.offset(1, 1, 1));
            } else {
                this.entityInfoList.clear();
            }
        }
    }

    private static void addToLists(StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo0, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo1, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo2, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo3) {
        if (structureTemplateStructureBlockInfo0.nbt != null) {
            listStructureTemplateStructureBlockInfo2.add(structureTemplateStructureBlockInfo0);
        } else if (!structureTemplateStructureBlockInfo0.state.m_60734_().hasDynamicShape() && structureTemplateStructureBlockInfo0.state.m_60838_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
            listStructureTemplateStructureBlockInfo1.add(structureTemplateStructureBlockInfo0);
        } else {
            listStructureTemplateStructureBlockInfo3.add(structureTemplateStructureBlockInfo0);
        }
    }

    private static List<StructureTemplate.StructureBlockInfo> buildInfoList(List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo0, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo1, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo2) {
        Comparator<StructureTemplate.StructureBlockInfo> $$3 = Comparator.comparingInt(p_74641_ -> p_74641_.pos.m_123342_()).thenComparingInt(p_74637_ -> p_74637_.pos.m_123341_()).thenComparingInt(p_74572_ -> p_74572_.pos.m_123343_());
        listStructureTemplateStructureBlockInfo0.sort($$3);
        listStructureTemplateStructureBlockInfo2.sort($$3);
        listStructureTemplateStructureBlockInfo1.sort($$3);
        List<StructureTemplate.StructureBlockInfo> $$4 = Lists.newArrayList();
        $$4.addAll(listStructureTemplateStructureBlockInfo0);
        $$4.addAll(listStructureTemplateStructureBlockInfo2);
        $$4.addAll(listStructureTemplateStructureBlockInfo1);
        return $$4;
    }

    private void fillEntityList(Level level0, BlockPos blockPos1, BlockPos blockPos2) {
        List<Entity> $$3 = level0.m_6443_(Entity.class, new AABB(blockPos1, blockPos2), p_74499_ -> !(p_74499_ instanceof Player));
        this.entityInfoList.clear();
        for (Entity $$4 : $$3) {
            Vec3 $$5 = new Vec3($$4.getX() - (double) blockPos1.m_123341_(), $$4.getY() - (double) blockPos1.m_123342_(), $$4.getZ() - (double) blockPos1.m_123343_());
            CompoundTag $$6 = new CompoundTag();
            $$4.save($$6);
            BlockPos $$7;
            if ($$4 instanceof Painting) {
                $$7 = ((Painting) $$4).m_31748_().subtract(blockPos1);
            } else {
                $$7 = BlockPos.containing($$5);
            }
            this.entityInfoList.add(new StructureTemplate.StructureEntityInfo($$5, $$7, $$6.copy()));
        }
    }

    public List<StructureTemplate.StructureBlockInfo> filterBlocks(BlockPos blockPos0, StructurePlaceSettings structurePlaceSettings1, Block block2) {
        return this.filterBlocks(blockPos0, structurePlaceSettings1, block2, true);
    }

    public ObjectArrayList<StructureTemplate.StructureBlockInfo> filterBlocks(BlockPos blockPos0, StructurePlaceSettings structurePlaceSettings1, Block block2, boolean boolean3) {
        ObjectArrayList<StructureTemplate.StructureBlockInfo> $$4 = new ObjectArrayList();
        BoundingBox $$5 = structurePlaceSettings1.getBoundingBox();
        if (this.palettes.isEmpty()) {
            return $$4;
        } else {
            for (StructureTemplate.StructureBlockInfo $$6 : structurePlaceSettings1.getRandomPalette(this.palettes, blockPos0).blocks(block2)) {
                BlockPos $$7 = boolean3 ? calculateRelativePosition(structurePlaceSettings1, $$6.pos).offset(blockPos0) : $$6.pos;
                if ($$5 == null || $$5.isInside($$7)) {
                    $$4.add(new StructureTemplate.StructureBlockInfo($$7, $$6.state.m_60717_(structurePlaceSettings1.getRotation()), $$6.nbt));
                }
            }
            return $$4;
        }
    }

    public BlockPos calculateConnectedPosition(StructurePlaceSettings structurePlaceSettings0, BlockPos blockPos1, StructurePlaceSettings structurePlaceSettings2, BlockPos blockPos3) {
        BlockPos $$4 = calculateRelativePosition(structurePlaceSettings0, blockPos1);
        BlockPos $$5 = calculateRelativePosition(structurePlaceSettings2, blockPos3);
        return $$4.subtract($$5);
    }

    public static BlockPos calculateRelativePosition(StructurePlaceSettings structurePlaceSettings0, BlockPos blockPos1) {
        return transform(blockPos1, structurePlaceSettings0.getMirror(), structurePlaceSettings0.getRotation(), structurePlaceSettings0.getRotationPivot());
    }

    public boolean placeInWorld(ServerLevelAccessor serverLevelAccessor0, BlockPos blockPos1, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings3, RandomSource randomSource4, int int5) {
        if (this.palettes.isEmpty()) {
            return false;
        } else {
            List<StructureTemplate.StructureBlockInfo> $$6 = structurePlaceSettings3.getRandomPalette(this.palettes, blockPos1).blocks();
            if ((!$$6.isEmpty() || !structurePlaceSettings3.isIgnoreEntities() && !this.entityInfoList.isEmpty()) && this.size.getX() >= 1 && this.size.getY() >= 1 && this.size.getZ() >= 1) {
                BoundingBox $$7 = structurePlaceSettings3.getBoundingBox();
                List<BlockPos> $$8 = Lists.newArrayListWithCapacity(structurePlaceSettings3.shouldKeepLiquids() ? $$6.size() : 0);
                List<BlockPos> $$9 = Lists.newArrayListWithCapacity(structurePlaceSettings3.shouldKeepLiquids() ? $$6.size() : 0);
                List<Pair<BlockPos, CompoundTag>> $$10 = Lists.newArrayListWithCapacity($$6.size());
                int $$11 = Integer.MAX_VALUE;
                int $$12 = Integer.MAX_VALUE;
                int $$13 = Integer.MAX_VALUE;
                int $$14 = Integer.MIN_VALUE;
                int $$15 = Integer.MIN_VALUE;
                int $$16 = Integer.MIN_VALUE;
                for (StructureTemplate.StructureBlockInfo $$18 : processBlockInfos(serverLevelAccessor0, blockPos1, blockPos2, structurePlaceSettings3, $$6)) {
                    BlockPos $$19 = $$18.pos;
                    if ($$7 == null || $$7.isInside($$19)) {
                        FluidState $$20 = structurePlaceSettings3.shouldKeepLiquids() ? serverLevelAccessor0.m_6425_($$19) : null;
                        BlockState $$21 = $$18.state.m_60715_(structurePlaceSettings3.getMirror()).m_60717_(structurePlaceSettings3.getRotation());
                        if ($$18.nbt != null) {
                            BlockEntity $$22 = serverLevelAccessor0.m_7702_($$19);
                            Clearable.tryClear($$22);
                            serverLevelAccessor0.m_7731_($$19, Blocks.BARRIER.defaultBlockState(), 20);
                        }
                        if (serverLevelAccessor0.m_7731_($$19, $$21, int5)) {
                            $$11 = Math.min($$11, $$19.m_123341_());
                            $$12 = Math.min($$12, $$19.m_123342_());
                            $$13 = Math.min($$13, $$19.m_123343_());
                            $$14 = Math.max($$14, $$19.m_123341_());
                            $$15 = Math.max($$15, $$19.m_123342_());
                            $$16 = Math.max($$16, $$19.m_123343_());
                            $$10.add(Pair.of($$19, $$18.nbt));
                            if ($$18.nbt != null) {
                                BlockEntity $$23 = serverLevelAccessor0.m_7702_($$19);
                                if ($$23 != null) {
                                    if ($$23 instanceof RandomizableContainerBlockEntity) {
                                        $$18.nbt.putLong("LootTableSeed", randomSource4.nextLong());
                                    }
                                    $$23.load($$18.nbt);
                                }
                            }
                            if ($$20 != null) {
                                if ($$21.m_60819_().isSource()) {
                                    $$9.add($$19);
                                } else if ($$21.m_60734_() instanceof LiquidBlockContainer) {
                                    ((LiquidBlockContainer) $$21.m_60734_()).placeLiquid(serverLevelAccessor0, $$19, $$21, $$20);
                                    if (!$$20.isSource()) {
                                        $$8.add($$19);
                                    }
                                }
                            }
                        }
                    }
                }
                boolean $$24 = true;
                Direction[] $$25 = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
                while ($$24 && !$$8.isEmpty()) {
                    $$24 = false;
                    Iterator<BlockPos> $$26 = $$8.iterator();
                    while ($$26.hasNext()) {
                        BlockPos $$27 = (BlockPos) $$26.next();
                        FluidState $$28 = serverLevelAccessor0.m_6425_($$27);
                        for (int $$29 = 0; $$29 < $$25.length && !$$28.isSource(); $$29++) {
                            BlockPos $$30 = $$27.relative($$25[$$29]);
                            FluidState $$31 = serverLevelAccessor0.m_6425_($$30);
                            if ($$31.isSource() && !$$9.contains($$30)) {
                                $$28 = $$31;
                            }
                        }
                        if ($$28.isSource()) {
                            BlockState $$32 = serverLevelAccessor0.m_8055_($$27);
                            Block $$33 = $$32.m_60734_();
                            if ($$33 instanceof LiquidBlockContainer) {
                                ((LiquidBlockContainer) $$33).placeLiquid(serverLevelAccessor0, $$27, $$32, $$28);
                                $$24 = true;
                                $$26.remove();
                            }
                        }
                    }
                }
                if ($$11 <= $$14) {
                    if (!structurePlaceSettings3.getKnownShape()) {
                        DiscreteVoxelShape $$34 = new BitSetDiscreteVoxelShape($$14 - $$11 + 1, $$15 - $$12 + 1, $$16 - $$13 + 1);
                        int $$35 = $$11;
                        int $$36 = $$12;
                        int $$37 = $$13;
                        for (Pair<BlockPos, CompoundTag> $$38 : $$10) {
                            BlockPos $$39 = (BlockPos) $$38.getFirst();
                            $$34.fill($$39.m_123341_() - $$35, $$39.m_123342_() - $$36, $$39.m_123343_() - $$37);
                        }
                        updateShapeAtEdge(serverLevelAccessor0, int5, $$34, $$35, $$36, $$37);
                    }
                    for (Pair<BlockPos, CompoundTag> $$40 : $$10) {
                        BlockPos $$41 = (BlockPos) $$40.getFirst();
                        if (!structurePlaceSettings3.getKnownShape()) {
                            BlockState $$42 = serverLevelAccessor0.m_8055_($$41);
                            BlockState $$43 = Block.updateFromNeighbourShapes($$42, serverLevelAccessor0, $$41);
                            if ($$42 != $$43) {
                                serverLevelAccessor0.m_7731_($$41, $$43, int5 & -2 | 16);
                            }
                            serverLevelAccessor0.m_6289_($$41, $$43.m_60734_());
                        }
                        if ($$40.getSecond() != null) {
                            BlockEntity $$44 = serverLevelAccessor0.m_7702_($$41);
                            if ($$44 != null) {
                                $$44.setChanged();
                            }
                        }
                    }
                }
                if (!structurePlaceSettings3.isIgnoreEntities()) {
                    this.placeEntities(serverLevelAccessor0, blockPos1, structurePlaceSettings3.getMirror(), structurePlaceSettings3.getRotation(), structurePlaceSettings3.getRotationPivot(), $$7, structurePlaceSettings3.shouldFinalizeEntities());
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static void updateShapeAtEdge(LevelAccessor levelAccessor0, int int1, DiscreteVoxelShape discreteVoxelShape2, int int3, int int4, int int5) {
        discreteVoxelShape2.forAllFaces((p_74494_, p_74495_, p_74496_, p_74497_) -> {
            BlockPos $$9 = new BlockPos(int3 + p_74495_, int4 + p_74496_, int5 + p_74497_);
            BlockPos $$10 = $$9.relative(p_74494_);
            BlockState $$11 = levelAccessor0.m_8055_($$9);
            BlockState $$12 = levelAccessor0.m_8055_($$10);
            BlockState $$13 = $$11.m_60728_(p_74494_, $$12, levelAccessor0, $$9, $$10);
            if ($$11 != $$13) {
                levelAccessor0.m_7731_($$9, $$13, int1 & -2);
            }
            BlockState $$14 = $$12.m_60728_(p_74494_.getOpposite(), $$13, levelAccessor0, $$10, $$9);
            if ($$12 != $$14) {
                levelAccessor0.m_7731_($$10, $$14, int1 & -2);
            }
        });
    }

    public static List<StructureTemplate.StructureBlockInfo> processBlockInfos(ServerLevelAccessor serverLevelAccessor0, BlockPos blockPos1, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings3, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo4) {
        List<StructureTemplate.StructureBlockInfo> $$5 = new ArrayList();
        List<StructureTemplate.StructureBlockInfo> $$6 = new ArrayList();
        for (StructureTemplate.StructureBlockInfo $$7 : listStructureTemplateStructureBlockInfo4) {
            BlockPos $$8 = calculateRelativePosition(structurePlaceSettings3, $$7.pos).offset(blockPos1);
            StructureTemplate.StructureBlockInfo $$9 = new StructureTemplate.StructureBlockInfo($$8, $$7.state, $$7.nbt != null ? $$7.nbt.copy() : null);
            Iterator<StructureProcessor> $$10 = structurePlaceSettings3.getProcessors().iterator();
            while ($$9 != null && $$10.hasNext()) {
                $$9 = ((StructureProcessor) $$10.next()).processBlock(serverLevelAccessor0, blockPos1, blockPos2, $$7, $$9, structurePlaceSettings3);
            }
            if ($$9 != null) {
                $$6.add($$9);
                $$5.add($$7);
            }
        }
        for (StructureProcessor $$11 : structurePlaceSettings3.getProcessors()) {
            $$6 = $$11.finalizeProcessing(serverLevelAccessor0, blockPos1, blockPos2, $$5, $$6, structurePlaceSettings3);
        }
        return $$6;
    }

    private void placeEntities(ServerLevelAccessor serverLevelAccessor0, BlockPos blockPos1, Mirror mirror2, Rotation rotation3, BlockPos blockPos4, @Nullable BoundingBox boundingBox5, boolean boolean6) {
        for (StructureTemplate.StructureEntityInfo $$7 : this.entityInfoList) {
            BlockPos $$8 = transform($$7.blockPos, mirror2, rotation3, blockPos4).offset(blockPos1);
            if (boundingBox5 == null || boundingBox5.isInside($$8)) {
                CompoundTag $$9 = $$7.nbt.copy();
                Vec3 $$10 = transform($$7.pos, mirror2, rotation3, blockPos4);
                Vec3 $$11 = $$10.add((double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_());
                ListTag $$12 = new ListTag();
                $$12.add(DoubleTag.valueOf($$11.x));
                $$12.add(DoubleTag.valueOf($$11.y));
                $$12.add(DoubleTag.valueOf($$11.z));
                $$9.put("Pos", $$12);
                $$9.remove("UUID");
                createEntityIgnoreException(serverLevelAccessor0, $$9).ifPresent(p_275190_ -> {
                    float $$7x = p_275190_.rotate(rotation3);
                    $$7x += p_275190_.mirror(mirror2) - p_275190_.getYRot();
                    p_275190_.moveTo($$11.x, $$11.y, $$11.z, $$7x, p_275190_.getXRot());
                    if (boolean6 && p_275190_ instanceof Mob) {
                        ((Mob) p_275190_).finalizeSpawn(serverLevelAccessor0, serverLevelAccessor0.m_6436_(BlockPos.containing($$11)), MobSpawnType.STRUCTURE, null, $$9);
                    }
                    serverLevelAccessor0.addFreshEntityWithPassengers(p_275190_);
                });
            }
        }
    }

    private static Optional<Entity> createEntityIgnoreException(ServerLevelAccessor serverLevelAccessor0, CompoundTag compoundTag1) {
        try {
            return EntityType.create(compoundTag1, serverLevelAccessor0.getLevel());
        } catch (Exception var3) {
            return Optional.empty();
        }
    }

    public Vec3i getSize(Rotation rotation0) {
        switch(rotation0) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return new Vec3i(this.size.getZ(), this.size.getY(), this.size.getX());
            default:
                return this.size;
        }
    }

    public static BlockPos transform(BlockPos blockPos0, Mirror mirror1, Rotation rotation2, BlockPos blockPos3) {
        int $$4 = blockPos0.m_123341_();
        int $$5 = blockPos0.m_123342_();
        int $$6 = blockPos0.m_123343_();
        boolean $$7 = true;
        switch(mirror1) {
            case LEFT_RIGHT:
                $$6 = -$$6;
                break;
            case FRONT_BACK:
                $$4 = -$$4;
                break;
            default:
                $$7 = false;
        }
        int $$8 = blockPos3.m_123341_();
        int $$9 = blockPos3.m_123343_();
        switch(rotation2) {
            case COUNTERCLOCKWISE_90:
                return new BlockPos($$8 - $$9 + $$6, $$5, $$8 + $$9 - $$4);
            case CLOCKWISE_90:
                return new BlockPos($$8 + $$9 - $$6, $$5, $$9 - $$8 + $$4);
            case CLOCKWISE_180:
                return new BlockPos($$8 + $$8 - $$4, $$5, $$9 + $$9 - $$6);
            default:
                return $$7 ? new BlockPos($$4, $$5, $$6) : blockPos0;
        }
    }

    public static Vec3 transform(Vec3 vec0, Mirror mirror1, Rotation rotation2, BlockPos blockPos3) {
        double $$4 = vec0.x;
        double $$5 = vec0.y;
        double $$6 = vec0.z;
        boolean $$7 = true;
        switch(mirror1) {
            case LEFT_RIGHT:
                $$6 = 1.0 - $$6;
                break;
            case FRONT_BACK:
                $$4 = 1.0 - $$4;
                break;
            default:
                $$7 = false;
        }
        int $$8 = blockPos3.m_123341_();
        int $$9 = blockPos3.m_123343_();
        switch(rotation2) {
            case COUNTERCLOCKWISE_90:
                return new Vec3((double) ($$8 - $$9) + $$6, $$5, (double) ($$8 + $$9 + 1) - $$4);
            case CLOCKWISE_90:
                return new Vec3((double) ($$8 + $$9 + 1) - $$6, $$5, (double) ($$9 - $$8) + $$4);
            case CLOCKWISE_180:
                return new Vec3((double) ($$8 + $$8 + 1) - $$4, $$5, (double) ($$9 + $$9 + 1) - $$6);
            default:
                return $$7 ? new Vec3($$4, $$5, $$6) : vec0;
        }
    }

    public BlockPos getZeroPositionWithTransform(BlockPos blockPos0, Mirror mirror1, Rotation rotation2) {
        return getZeroPositionWithTransform(blockPos0, mirror1, rotation2, this.getSize().getX(), this.getSize().getZ());
    }

    public static BlockPos getZeroPositionWithTransform(BlockPos blockPos0, Mirror mirror1, Rotation rotation2, int int3, int int4) {
        int3--;
        int4--;
        int $$5 = mirror1 == Mirror.FRONT_BACK ? int3 : 0;
        int $$6 = mirror1 == Mirror.LEFT_RIGHT ? int4 : 0;
        BlockPos $$7 = blockPos0;
        switch(rotation2) {
            case COUNTERCLOCKWISE_90:
                $$7 = blockPos0.offset($$6, 0, int3 - $$5);
                break;
            case CLOCKWISE_90:
                $$7 = blockPos0.offset(int4 - $$6, 0, $$5);
                break;
            case CLOCKWISE_180:
                $$7 = blockPos0.offset(int3 - $$5, 0, int4 - $$6);
                break;
            case NONE:
                $$7 = blockPos0.offset($$5, 0, $$6);
        }
        return $$7;
    }

    public BoundingBox getBoundingBox(StructurePlaceSettings structurePlaceSettings0, BlockPos blockPos1) {
        return this.getBoundingBox(blockPos1, structurePlaceSettings0.getRotation(), structurePlaceSettings0.getRotationPivot(), structurePlaceSettings0.getMirror());
    }

    public BoundingBox getBoundingBox(BlockPos blockPos0, Rotation rotation1, BlockPos blockPos2, Mirror mirror3) {
        return getBoundingBox(blockPos0, rotation1, blockPos2, mirror3, this.size);
    }

    @VisibleForTesting
    protected static BoundingBox getBoundingBox(BlockPos blockPos0, Rotation rotation1, BlockPos blockPos2, Mirror mirror3, Vec3i vecI4) {
        Vec3i $$5 = vecI4.offset(-1, -1, -1);
        BlockPos $$6 = transform(BlockPos.ZERO, mirror3, rotation1, blockPos2);
        BlockPos $$7 = transform(BlockPos.ZERO.offset($$5), mirror3, rotation1, blockPos2);
        return BoundingBox.fromCorners($$6, $$7).move(blockPos0);
    }

    public CompoundTag save(CompoundTag compoundTag0) {
        if (this.palettes.isEmpty()) {
            compoundTag0.put("blocks", new ListTag());
            compoundTag0.put("palette", new ListTag());
        } else {
            List<StructureTemplate.SimplePalette> $$1 = Lists.newArrayList();
            StructureTemplate.SimplePalette $$2 = new StructureTemplate.SimplePalette();
            $$1.add($$2);
            for (int $$3 = 1; $$3 < this.palettes.size(); $$3++) {
                $$1.add(new StructureTemplate.SimplePalette());
            }
            ListTag $$4 = new ListTag();
            List<StructureTemplate.StructureBlockInfo> $$5 = ((StructureTemplate.Palette) this.palettes.get(0)).blocks();
            for (int $$6 = 0; $$6 < $$5.size(); $$6++) {
                StructureTemplate.StructureBlockInfo $$7 = (StructureTemplate.StructureBlockInfo) $$5.get($$6);
                CompoundTag $$8 = new CompoundTag();
                $$8.put("pos", this.newIntegerList($$7.pos.m_123341_(), $$7.pos.m_123342_(), $$7.pos.m_123343_()));
                int $$9 = $$2.idFor($$7.state);
                $$8.putInt("state", $$9);
                if ($$7.nbt != null) {
                    $$8.put("nbt", $$7.nbt);
                }
                $$4.add($$8);
                for (int $$10 = 1; $$10 < this.palettes.size(); $$10++) {
                    StructureTemplate.SimplePalette $$11 = (StructureTemplate.SimplePalette) $$1.get($$10);
                    $$11.addMapping(((StructureTemplate.StructureBlockInfo) ((StructureTemplate.Palette) this.palettes.get($$10)).blocks().get($$6)).state, $$9);
                }
            }
            compoundTag0.put("blocks", $$4);
            if ($$1.size() == 1) {
                ListTag $$12 = new ListTag();
                for (BlockState $$13 : $$2) {
                    $$12.add(NbtUtils.writeBlockState($$13));
                }
                compoundTag0.put("palette", $$12);
            } else {
                ListTag $$14 = new ListTag();
                for (StructureTemplate.SimplePalette $$15 : $$1) {
                    ListTag $$16 = new ListTag();
                    for (BlockState $$17 : $$15) {
                        $$16.add(NbtUtils.writeBlockState($$17));
                    }
                    $$14.add($$16);
                }
                compoundTag0.put("palettes", $$14);
            }
        }
        ListTag $$18 = new ListTag();
        for (StructureTemplate.StructureEntityInfo $$19 : this.entityInfoList) {
            CompoundTag $$20 = new CompoundTag();
            $$20.put("pos", this.newDoubleList($$19.pos.x, $$19.pos.y, $$19.pos.z));
            $$20.put("blockPos", this.newIntegerList($$19.blockPos.m_123341_(), $$19.blockPos.m_123342_(), $$19.blockPos.m_123343_()));
            if ($$19.nbt != null) {
                $$20.put("nbt", $$19.nbt);
            }
            $$18.add($$20);
        }
        compoundTag0.put("entities", $$18);
        compoundTag0.put("size", this.newIntegerList(this.size.getX(), this.size.getY(), this.size.getZ()));
        return NbtUtils.addCurrentDataVersion(compoundTag0);
    }

    public void load(HolderGetter<Block> holderGetterBlock0, CompoundTag compoundTag1) {
        this.palettes.clear();
        this.entityInfoList.clear();
        ListTag $$2 = compoundTag1.getList("size", 3);
        this.size = new Vec3i($$2.getInt(0), $$2.getInt(1), $$2.getInt(2));
        ListTag $$3 = compoundTag1.getList("blocks", 10);
        if (compoundTag1.contains("palettes", 9)) {
            ListTag $$4 = compoundTag1.getList("palettes", 9);
            for (int $$5 = 0; $$5 < $$4.size(); $$5++) {
                this.loadPalette(holderGetterBlock0, $$4.getList($$5), $$3);
            }
        } else {
            this.loadPalette(holderGetterBlock0, compoundTag1.getList("palette", 10), $$3);
        }
        ListTag $$6 = compoundTag1.getList("entities", 10);
        for (int $$7 = 0; $$7 < $$6.size(); $$7++) {
            CompoundTag $$8 = $$6.getCompound($$7);
            ListTag $$9 = $$8.getList("pos", 6);
            Vec3 $$10 = new Vec3($$9.getDouble(0), $$9.getDouble(1), $$9.getDouble(2));
            ListTag $$11 = $$8.getList("blockPos", 3);
            BlockPos $$12 = new BlockPos($$11.getInt(0), $$11.getInt(1), $$11.getInt(2));
            if ($$8.contains("nbt")) {
                CompoundTag $$13 = $$8.getCompound("nbt");
                this.entityInfoList.add(new StructureTemplate.StructureEntityInfo($$10, $$12, $$13));
            }
        }
    }

    private void loadPalette(HolderGetter<Block> holderGetterBlock0, ListTag listTag1, ListTag listTag2) {
        StructureTemplate.SimplePalette $$3 = new StructureTemplate.SimplePalette();
        for (int $$4 = 0; $$4 < listTag1.size(); $$4++) {
            $$3.addMapping(NbtUtils.readBlockState(holderGetterBlock0, listTag1.getCompound($$4)), $$4);
        }
        List<StructureTemplate.StructureBlockInfo> $$5 = Lists.newArrayList();
        List<StructureTemplate.StructureBlockInfo> $$6 = Lists.newArrayList();
        List<StructureTemplate.StructureBlockInfo> $$7 = Lists.newArrayList();
        for (int $$8 = 0; $$8 < listTag2.size(); $$8++) {
            CompoundTag $$9 = listTag2.getCompound($$8);
            ListTag $$10 = $$9.getList("pos", 3);
            BlockPos $$11 = new BlockPos($$10.getInt(0), $$10.getInt(1), $$10.getInt(2));
            BlockState $$12 = $$3.stateFor($$9.getInt("state"));
            CompoundTag $$13;
            if ($$9.contains("nbt")) {
                $$13 = $$9.getCompound("nbt");
            } else {
                $$13 = null;
            }
            StructureTemplate.StructureBlockInfo $$15 = new StructureTemplate.StructureBlockInfo($$11, $$12, $$13);
            addToLists($$15, $$5, $$6, $$7);
        }
        List<StructureTemplate.StructureBlockInfo> $$16 = buildInfoList($$5, $$6, $$7);
        this.palettes.add(new StructureTemplate.Palette($$16));
    }

    private ListTag newIntegerList(int... int0) {
        ListTag $$1 = new ListTag();
        for (int $$2 : int0) {
            $$1.add(IntTag.valueOf($$2));
        }
        return $$1;
    }

    private ListTag newDoubleList(double... double0) {
        ListTag $$1 = new ListTag();
        for (double $$2 : double0) {
            $$1.add(DoubleTag.valueOf($$2));
        }
        return $$1;
    }

    public static final class Palette {

        private final List<StructureTemplate.StructureBlockInfo> blocks;

        private final Map<Block, List<StructureTemplate.StructureBlockInfo>> cache = Maps.newHashMap();

        Palette(List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo0) {
            this.blocks = listStructureTemplateStructureBlockInfo0;
        }

        public List<StructureTemplate.StructureBlockInfo> blocks() {
            return this.blocks;
        }

        public List<StructureTemplate.StructureBlockInfo> blocks(Block block0) {
            return (List<StructureTemplate.StructureBlockInfo>) this.cache.computeIfAbsent(block0, p_74659_ -> (List) this.blocks.stream().filter(p_163818_ -> p_163818_.state.m_60713_(p_74659_)).collect(Collectors.toList()));
        }
    }

    static class SimplePalette implements Iterable<BlockState> {

        public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();

        private final IdMapper<BlockState> ids = new IdMapper<>(16);

        private int lastId;

        public int idFor(BlockState blockState0) {
            int $$1 = this.ids.getId(blockState0);
            if ($$1 == -1) {
                $$1 = this.lastId++;
                this.ids.addMapping(blockState0, $$1);
            }
            return $$1;
        }

        @Nullable
        public BlockState stateFor(int int0) {
            BlockState $$1 = this.ids.byId(int0);
            return $$1 == null ? DEFAULT_BLOCK_STATE : $$1;
        }

        public Iterator<BlockState> iterator() {
            return this.ids.iterator();
        }

        public void addMapping(BlockState blockState0, int int1) {
            this.ids.addMapping(blockState0, int1);
        }
    }

    public static record StructureBlockInfo(BlockPos f_74675_, BlockState f_74676_, @Nullable CompoundTag f_74677_) {

        private final BlockPos pos;

        private final BlockState state;

        @Nullable
        private final CompoundTag nbt;

        public StructureBlockInfo(BlockPos f_74675_, BlockState f_74676_, @Nullable CompoundTag f_74677_) {
            this.pos = f_74675_;
            this.state = f_74676_;
            this.nbt = f_74677_;
        }

        public String toString() {
            return String.format(Locale.ROOT, "<StructureBlockInfo | %s | %s | %s>", this.pos, this.state, this.nbt);
        }
    }

    public static class StructureEntityInfo {

        public final Vec3 pos;

        public final BlockPos blockPos;

        public final CompoundTag nbt;

        public StructureEntityInfo(Vec3 vec0, BlockPos blockPos1, CompoundTag compoundTag2) {
            this.pos = vec0;
            this.blockPos = blockPos1;
            this.nbt = compoundTag2;
        }
    }
}