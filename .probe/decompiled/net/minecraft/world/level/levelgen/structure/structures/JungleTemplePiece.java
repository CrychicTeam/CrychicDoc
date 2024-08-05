package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class JungleTemplePiece extends ScatteredFeaturePiece {

    public static final int WIDTH = 12;

    public static final int DEPTH = 15;

    private boolean placedMainChest;

    private boolean placedHiddenChest;

    private boolean placedTrap1;

    private boolean placedTrap2;

    private static final JungleTemplePiece.MossStoneSelector STONE_SELECTOR = new JungleTemplePiece.MossStoneSelector();

    public JungleTemplePiece(RandomSource randomSource0, int int1, int int2) {
        super(StructurePieceType.JUNGLE_PYRAMID_PIECE, int1, 64, int2, 12, 10, 15, m_226760_(randomSource0));
    }

    public JungleTemplePiece(CompoundTag compoundTag0) {
        super(StructurePieceType.JUNGLE_PYRAMID_PIECE, compoundTag0);
        this.placedMainChest = compoundTag0.getBoolean("placedMainChest");
        this.placedHiddenChest = compoundTag0.getBoolean("placedHiddenChest");
        this.placedTrap1 = compoundTag0.getBoolean("placedTrap1");
        this.placedTrap2 = compoundTag0.getBoolean("placedTrap2");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
        compoundTag1.putBoolean("placedMainChest", this.placedMainChest);
        compoundTag1.putBoolean("placedHiddenChest", this.placedHiddenChest);
        compoundTag1.putBoolean("placedTrap1", this.placedTrap1);
        compoundTag1.putBoolean("placedTrap2", this.placedTrap2);
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
        if (this.m_72803_(worldGenLevel0, boundingBox4, 0)) {
            this.m_226776_(worldGenLevel0, boundingBox4, 0, -4, 0, this.f_72787_ - 1, 0, this.f_72789_ - 1, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 1, 2, 9, 2, 2, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 1, 12, 9, 2, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 1, 3, 2, 2, 11, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 9, 1, 3, 9, 2, 11, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 3, 1, 10, 6, 1, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 3, 13, 10, 6, 13, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 1, 3, 2, 1, 6, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 10, 3, 2, 10, 6, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 3, 2, 9, 3, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 6, 2, 9, 6, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 3, 7, 3, 8, 7, 11, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 8, 4, 7, 8, 10, false, randomSource3, STONE_SELECTOR);
            this.m_73535_(worldGenLevel0, boundingBox4, 3, 1, 3, 8, 2, 11);
            this.m_73535_(worldGenLevel0, boundingBox4, 4, 3, 6, 7, 3, 9);
            this.m_73535_(worldGenLevel0, boundingBox4, 2, 4, 2, 9, 5, 12);
            this.m_73535_(worldGenLevel0, boundingBox4, 4, 6, 5, 7, 6, 9);
            this.m_73535_(worldGenLevel0, boundingBox4, 5, 7, 6, 6, 7, 8);
            this.m_73535_(worldGenLevel0, boundingBox4, 5, 1, 2, 6, 2, 2);
            this.m_73535_(worldGenLevel0, boundingBox4, 5, 2, 12, 6, 2, 12);
            this.m_73535_(worldGenLevel0, boundingBox4, 5, 5, 1, 6, 5, 1);
            this.m_73535_(worldGenLevel0, boundingBox4, 5, 5, 13, 6, 5, 13);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 1, 5, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 10, 5, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 1, 5, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.AIR.defaultBlockState(), 10, 5, 9, boundingBox4);
            for (int $$7 = 0; $$7 <= 14; $$7 += 14) {
                this.m_226776_(worldGenLevel0, boundingBox4, 2, 4, $$7, 2, 5, $$7, false, randomSource3, STONE_SELECTOR);
                this.m_226776_(worldGenLevel0, boundingBox4, 4, 4, $$7, 4, 5, $$7, false, randomSource3, STONE_SELECTOR);
                this.m_226776_(worldGenLevel0, boundingBox4, 7, 4, $$7, 7, 5, $$7, false, randomSource3, STONE_SELECTOR);
                this.m_226776_(worldGenLevel0, boundingBox4, 9, 4, $$7, 9, 5, $$7, false, randomSource3, STONE_SELECTOR);
            }
            this.m_226776_(worldGenLevel0, boundingBox4, 5, 6, 0, 6, 6, 0, false, randomSource3, STONE_SELECTOR);
            for (int $$8 = 0; $$8 <= 11; $$8 += 11) {
                for (int $$9 = 2; $$9 <= 12; $$9 += 2) {
                    this.m_226776_(worldGenLevel0, boundingBox4, $$8, 4, $$9, $$8, 5, $$9, false, randomSource3, STONE_SELECTOR);
                }
                this.m_226776_(worldGenLevel0, boundingBox4, $$8, 6, 5, $$8, 6, 5, false, randomSource3, STONE_SELECTOR);
                this.m_226776_(worldGenLevel0, boundingBox4, $$8, 6, 9, $$8, 6, 9, false, randomSource3, STONE_SELECTOR);
            }
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 7, 2, 2, 9, 2, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 9, 7, 2, 9, 9, 2, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 2, 7, 12, 2, 9, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 9, 7, 12, 9, 9, 12, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 9, 4, 4, 9, 4, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 7, 9, 4, 7, 9, 4, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 9, 10, 4, 9, 10, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 7, 9, 10, 7, 9, 10, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 5, 9, 7, 6, 9, 7, false, randomSource3, STONE_SELECTOR);
            BlockState $$10 = (BlockState) Blocks.COBBLESTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.EAST);
            BlockState $$11 = (BlockState) Blocks.COBBLESTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.WEST);
            BlockState $$12 = (BlockState) Blocks.COBBLESTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.SOUTH);
            BlockState $$13 = (BlockState) Blocks.COBBLESTONE_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, Direction.NORTH);
            this.m_73434_(worldGenLevel0, $$13, 5, 9, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 6, 9, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, $$12, 5, 9, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, $$12, 6, 9, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 4, 0, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 5, 0, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 6, 0, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 7, 0, 0, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 4, 1, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 4, 2, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 4, 3, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 7, 1, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 7, 2, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, $$13, 7, 3, 10, boundingBox4);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 9, 4, 1, 9, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 7, 1, 9, 7, 1, 9, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 4, 1, 10, 7, 2, 10, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 5, 4, 5, 6, 4, 5, false, randomSource3, STONE_SELECTOR);
            this.m_73434_(worldGenLevel0, $$10, 4, 4, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, $$11, 7, 4, 5, boundingBox4);
            for (int $$14 = 0; $$14 < 4; $$14++) {
                this.m_73434_(worldGenLevel0, $$12, 5, 0 - $$14, 6 + $$14, boundingBox4);
                this.m_73434_(worldGenLevel0, $$12, 6, 0 - $$14, 6 + $$14, boundingBox4);
                this.m_73535_(worldGenLevel0, boundingBox4, 5, 0 - $$14, 7 + $$14, 6, 0 - $$14, 9 + $$14);
            }
            this.m_73535_(worldGenLevel0, boundingBox4, 1, -3, 12, 10, -1, 13);
            this.m_73535_(worldGenLevel0, boundingBox4, 1, -3, 1, 3, -1, 13);
            this.m_73535_(worldGenLevel0, boundingBox4, 1, -3, 1, 9, -1, 5);
            for (int $$15 = 1; $$15 <= 13; $$15 += 2) {
                this.m_226776_(worldGenLevel0, boundingBox4, 1, -3, $$15, 1, -2, $$15, false, randomSource3, STONE_SELECTOR);
            }
            for (int $$16 = 2; $$16 <= 12; $$16 += 2) {
                this.m_226776_(worldGenLevel0, boundingBox4, 1, -1, $$16, 3, -1, $$16, false, randomSource3, STONE_SELECTOR);
            }
            this.m_226776_(worldGenLevel0, boundingBox4, 2, -2, 1, 5, -2, 1, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 7, -2, 1, 9, -2, 1, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 6, -3, 1, 6, -3, 1, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 6, -1, 1, 6, -1, 1, false, randomSource3, STONE_SELECTOR);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.TRIPWIRE_HOOK.defaultBlockState().m_61124_(TripWireHookBlock.FACING, Direction.EAST)).m_61124_(TripWireHookBlock.ATTACHED, true), 1, -3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.TRIPWIRE_HOOK.defaultBlockState().m_61124_(TripWireHookBlock.FACING, Direction.WEST)).m_61124_(TripWireHookBlock.ATTACHED, true), 4, -3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) Blocks.TRIPWIRE.defaultBlockState().m_61124_(TripWireBlock.EAST, true)).m_61124_(TripWireBlock.WEST, true)).m_61124_(TripWireBlock.ATTACHED, true), 2, -3, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) Blocks.TRIPWIRE.defaultBlockState().m_61124_(TripWireBlock.EAST, true)).m_61124_(TripWireBlock.WEST, true)).m_61124_(TripWireBlock.ATTACHED, true), 3, -3, 8, boundingBox4);
            BlockState $$17 = (BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE);
            this.m_73434_(worldGenLevel0, $$17, 5, -3, 7, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 5, -3, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 5, -3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 5, -3, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 5, -3, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 5, -3, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 5, -3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 4, -3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3, -3, 1, boundingBox4);
            if (!this.placedTrap1) {
                this.placedTrap1 = this.m_226819_(worldGenLevel0, boundingBox4, randomSource3, 3, -2, 1, Direction.NORTH, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
            }
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.VINE.defaultBlockState().m_61124_(VineBlock.SOUTH, true), 3, -2, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.TRIPWIRE_HOOK.defaultBlockState().m_61124_(TripWireHookBlock.FACING, Direction.NORTH)).m_61124_(TripWireHookBlock.ATTACHED, true), 7, -3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.TRIPWIRE_HOOK.defaultBlockState().m_61124_(TripWireHookBlock.FACING, Direction.SOUTH)).m_61124_(TripWireHookBlock.ATTACHED, true), 7, -3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) Blocks.TRIPWIRE.defaultBlockState().m_61124_(TripWireBlock.NORTH, true)).m_61124_(TripWireBlock.SOUTH, true)).m_61124_(TripWireBlock.ATTACHED, true), 7, -3, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) Blocks.TRIPWIRE.defaultBlockState().m_61124_(TripWireBlock.NORTH, true)).m_61124_(TripWireBlock.SOUTH, true)).m_61124_(TripWireBlock.ATTACHED, true), 7, -3, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) Blocks.TRIPWIRE.defaultBlockState().m_61124_(TripWireBlock.NORTH, true)).m_61124_(TripWireBlock.SOUTH, true)).m_61124_(TripWireBlock.ATTACHED, true), 7, -3, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 8, -3, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.WEST, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE), 9, -3, 6, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.SOUTH, RedstoneSide.UP), 9, -3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 4, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 9, -2, 4, boundingBox4);
            if (!this.placedTrap2) {
                this.placedTrap2 = this.m_226819_(worldGenLevel0, boundingBox4, randomSource3, 9, -2, 3, Direction.WEST, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
            }
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.VINE.defaultBlockState().m_61124_(VineBlock.EAST, true), 8, -1, 3, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.VINE.defaultBlockState().m_61124_(VineBlock.EAST, true), 8, -2, 3, boundingBox4);
            if (!this.placedMainChest) {
                this.placedMainChest = this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 8, -3, 3, BuiltInLootTables.JUNGLE_TEMPLE);
            }
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 2, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 1, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 4, -3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -2, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -1, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 6, -3, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -2, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -1, 5, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 5, boundingBox4);
            this.m_226776_(worldGenLevel0, boundingBox4, 9, -1, 1, 9, -1, 5, false, randomSource3, STONE_SELECTOR);
            this.m_73535_(worldGenLevel0, boundingBox4, 8, -3, 8, 10, -1, 10);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 8, -2, 11, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 9, -2, 11, boundingBox4);
            this.m_73434_(worldGenLevel0, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 10, -2, 11, boundingBox4);
            BlockState $$18 = (BlockState) ((BlockState) Blocks.LEVER.defaultBlockState().m_61124_(LeverBlock.f_54117_, Direction.NORTH)).m_61124_(LeverBlock.f_53179_, AttachFace.WALL);
            this.m_73434_(worldGenLevel0, $$18, 8, -2, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, $$18, 9, -2, 12, boundingBox4);
            this.m_73434_(worldGenLevel0, $$18, 10, -2, 12, boundingBox4);
            this.m_226776_(worldGenLevel0, boundingBox4, 8, -3, 8, 8, -3, 10, false, randomSource3, STONE_SELECTOR);
            this.m_226776_(worldGenLevel0, boundingBox4, 10, -3, 8, 10, -3, 10, false, randomSource3, STONE_SELECTOR);
            this.m_73434_(worldGenLevel0, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 10, -2, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 8, -2, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, $$17, 8, -2, 10, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) ((BlockState) ((BlockState) ((BlockState) Blocks.REDSTONE_WIRE.defaultBlockState().m_61124_(RedStoneWireBlock.NORTH, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.SOUTH, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.EAST, RedstoneSide.SIDE)).m_61124_(RedStoneWireBlock.WEST, RedstoneSide.SIDE), 10, -1, 9, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.STICKY_PISTON.defaultBlockState().m_61124_(PistonBaseBlock.f_52588_, Direction.UP), 9, -2, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.STICKY_PISTON.defaultBlockState().m_61124_(PistonBaseBlock.f_52588_, Direction.WEST), 10, -2, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.STICKY_PISTON.defaultBlockState().m_61124_(PistonBaseBlock.f_52588_, Direction.WEST), 10, -1, 8, boundingBox4);
            this.m_73434_(worldGenLevel0, (BlockState) Blocks.REPEATER.defaultBlockState().m_61124_(RepeaterBlock.f_54117_, Direction.NORTH), 10, -2, 10, boundingBox4);
            if (!this.placedHiddenChest) {
                this.placedHiddenChest = this.m_213787_(worldGenLevel0, boundingBox4, randomSource3, 9, -3, 10, BuiltInLootTables.JUNGLE_TEMPLE);
            }
        }
    }

    static class MossStoneSelector extends StructurePiece.BlockSelector {

        @Override
        public void next(RandomSource randomSource0, int int1, int int2, int int3, boolean boolean4) {
            if (randomSource0.nextFloat() < 0.4F) {
                this.f_73553_ = Blocks.COBBLESTONE.defaultBlockState();
            } else {
                this.f_73553_ = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            }
        }
    }
}