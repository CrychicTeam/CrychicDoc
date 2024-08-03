package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class EndCityPieces {

    private static final int MAX_GEN_DEPTH = 8;

    static final EndCityPieces.SectionGenerator HOUSE_TOWER_GENERATOR = new EndCityPieces.SectionGenerator() {

        @Override
        public void init() {
        }

        @Override
        public boolean generate(StructureTemplateManager p_227456_, int p_227457_, EndCityPieces.EndCityPiece p_227458_, BlockPos p_227459_, List<StructurePiece> p_227460_, RandomSource p_227461_) {
            if (p_227457_ > 8) {
                return false;
            } else {
                Rotation $$6 = p_227458_.m_226913_().getRotation();
                EndCityPieces.EndCityPiece $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, p_227458_, p_227459_, "base_floor", $$6, true));
                int $$8 = p_227461_.nextInt(3);
                if ($$8 == 0) {
                    $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, $$7, new BlockPos(-1, 4, -1), "base_roof", $$6, true));
                } else if ($$8 == 1) {
                    $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, $$7, new BlockPos(-1, 0, -1), "second_floor_2", $$6, false));
                    $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, $$7, new BlockPos(-1, 8, -1), "second_roof", $$6, false));
                    EndCityPieces.recursiveChildren(p_227456_, EndCityPieces.TOWER_GENERATOR, p_227457_ + 1, $$7, null, p_227460_, p_227461_);
                } else if ($$8 == 2) {
                    $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, $$7, new BlockPos(-1, 0, -1), "second_floor_2", $$6, false));
                    $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, $$7, new BlockPos(-1, 4, -1), "third_floor_2", $$6, false));
                    $$7 = EndCityPieces.addHelper(p_227460_, EndCityPieces.addPiece(p_227456_, $$7, new BlockPos(-1, 8, -1), "third_roof", $$6, true));
                    EndCityPieces.recursiveChildren(p_227456_, EndCityPieces.TOWER_GENERATOR, p_227457_ + 1, $$7, null, p_227460_, p_227461_);
                }
                return true;
            }
        }
    };

    static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList(new Tuple[] { new Tuple<>(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple<>(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple<>(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple<>(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6)) });

    static final EndCityPieces.SectionGenerator TOWER_GENERATOR = new EndCityPieces.SectionGenerator() {

        @Override
        public void init() {
        }

        @Override
        public boolean generate(StructureTemplateManager p_227465_, int p_227466_, EndCityPieces.EndCityPiece p_227467_, BlockPos p_227468_, List<StructurePiece> p_227469_, RandomSource p_227470_) {
            Rotation $$6 = p_227467_.m_226913_().getRotation();
            EndCityPieces.EndCityPiece $$7 = EndCityPieces.addHelper(p_227469_, EndCityPieces.addPiece(p_227465_, p_227467_, new BlockPos(3 + p_227470_.nextInt(2), -3, 3 + p_227470_.nextInt(2)), "tower_base", $$6, true));
            $$7 = EndCityPieces.addHelper(p_227469_, EndCityPieces.addPiece(p_227465_, $$7, new BlockPos(0, 7, 0), "tower_piece", $$6, true));
            EndCityPieces.EndCityPiece $$8 = p_227470_.nextInt(3) == 0 ? $$7 : null;
            int $$9 = 1 + p_227470_.nextInt(3);
            for (int $$10 = 0; $$10 < $$9; $$10++) {
                $$7 = EndCityPieces.addHelper(p_227469_, EndCityPieces.addPiece(p_227465_, $$7, new BlockPos(0, 4, 0), "tower_piece", $$6, true));
                if ($$10 < $$9 - 1 && p_227470_.nextBoolean()) {
                    $$8 = $$7;
                }
            }
            if ($$8 != null) {
                for (Tuple<Rotation, BlockPos> $$11 : EndCityPieces.TOWER_BRIDGES) {
                    if (p_227470_.nextBoolean()) {
                        EndCityPieces.EndCityPiece $$12 = EndCityPieces.addHelper(p_227469_, EndCityPieces.addPiece(p_227465_, $$8, $$11.getB(), "bridge_end", $$6.getRotated($$11.getA()), true));
                        EndCityPieces.recursiveChildren(p_227465_, EndCityPieces.TOWER_BRIDGE_GENERATOR, p_227466_ + 1, $$12, null, p_227469_, p_227470_);
                    }
                }
                $$7 = EndCityPieces.addHelper(p_227469_, EndCityPieces.addPiece(p_227465_, $$7, new BlockPos(-1, 4, -1), "tower_top", $$6, true));
            } else {
                if (p_227466_ != 7) {
                    return EndCityPieces.recursiveChildren(p_227465_, EndCityPieces.FAT_TOWER_GENERATOR, p_227466_ + 1, $$7, null, p_227469_, p_227470_);
                }
                $$7 = EndCityPieces.addHelper(p_227469_, EndCityPieces.addPiece(p_227465_, $$7, new BlockPos(-1, 4, -1), "tower_top", $$6, true));
            }
            return true;
        }
    };

    static final EndCityPieces.SectionGenerator TOWER_BRIDGE_GENERATOR = new EndCityPieces.SectionGenerator() {

        public boolean shipCreated;

        @Override
        public void init() {
            this.shipCreated = false;
        }

        @Override
        public boolean generate(StructureTemplateManager p_227475_, int p_227476_, EndCityPieces.EndCityPiece p_227477_, BlockPos p_227478_, List<StructurePiece> p_227479_, RandomSource p_227480_) {
            Rotation $$6 = p_227477_.m_226913_().getRotation();
            int $$7 = p_227480_.nextInt(4) + 1;
            EndCityPieces.EndCityPiece $$8 = EndCityPieces.addHelper(p_227479_, EndCityPieces.addPiece(p_227475_, p_227477_, new BlockPos(0, 0, -4), "bridge_piece", $$6, true));
            $$8.m_226758_(-1);
            int $$9 = 0;
            for (int $$10 = 0; $$10 < $$7; $$10++) {
                if (p_227480_.nextBoolean()) {
                    $$8 = EndCityPieces.addHelper(p_227479_, EndCityPieces.addPiece(p_227475_, $$8, new BlockPos(0, $$9, -4), "bridge_piece", $$6, true));
                    $$9 = 0;
                } else {
                    if (p_227480_.nextBoolean()) {
                        $$8 = EndCityPieces.addHelper(p_227479_, EndCityPieces.addPiece(p_227475_, $$8, new BlockPos(0, $$9, -4), "bridge_steep_stairs", $$6, true));
                    } else {
                        $$8 = EndCityPieces.addHelper(p_227479_, EndCityPieces.addPiece(p_227475_, $$8, new BlockPos(0, $$9, -8), "bridge_gentle_stairs", $$6, true));
                    }
                    $$9 = 4;
                }
            }
            if (!this.shipCreated && p_227480_.nextInt(10 - p_227476_) == 0) {
                EndCityPieces.addHelper(p_227479_, EndCityPieces.addPiece(p_227475_, $$8, new BlockPos(-8 + p_227480_.nextInt(8), $$9, -70 + p_227480_.nextInt(10)), "ship", $$6, true));
                this.shipCreated = true;
            } else if (!EndCityPieces.recursiveChildren(p_227475_, EndCityPieces.HOUSE_TOWER_GENERATOR, p_227476_ + 1, $$8, new BlockPos(-3, $$9 + 1, -11), p_227479_, p_227480_)) {
                return false;
            }
            $$8 = EndCityPieces.addHelper(p_227479_, EndCityPieces.addPiece(p_227475_, $$8, new BlockPos(4, $$9, 0), "bridge_end", $$6.getRotated(Rotation.CLOCKWISE_180), true));
            $$8.m_226758_(-1);
            return true;
        }
    };

    static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList(new Tuple[] { new Tuple<>(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple<>(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple<>(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple<>(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12)) });

    static final EndCityPieces.SectionGenerator FAT_TOWER_GENERATOR = new EndCityPieces.SectionGenerator() {

        @Override
        public void init() {
        }

        @Override
        public boolean generate(StructureTemplateManager p_227484_, int p_227485_, EndCityPieces.EndCityPiece p_227486_, BlockPos p_227487_, List<StructurePiece> p_227488_, RandomSource p_227489_) {
            Rotation $$6 = p_227486_.m_226913_().getRotation();
            EndCityPieces.EndCityPiece $$7 = EndCityPieces.addHelper(p_227488_, EndCityPieces.addPiece(p_227484_, p_227486_, new BlockPos(-3, 4, -3), "fat_tower_base", $$6, true));
            $$7 = EndCityPieces.addHelper(p_227488_, EndCityPieces.addPiece(p_227484_, $$7, new BlockPos(0, 4, 0), "fat_tower_middle", $$6, true));
            for (int $$8 = 0; $$8 < 2 && p_227489_.nextInt(3) != 0; $$8++) {
                $$7 = EndCityPieces.addHelper(p_227488_, EndCityPieces.addPiece(p_227484_, $$7, new BlockPos(0, 8, 0), "fat_tower_middle", $$6, true));
                for (Tuple<Rotation, BlockPos> $$9 : EndCityPieces.FAT_TOWER_BRIDGES) {
                    if (p_227489_.nextBoolean()) {
                        EndCityPieces.EndCityPiece $$10 = EndCityPieces.addHelper(p_227488_, EndCityPieces.addPiece(p_227484_, $$7, $$9.getB(), "bridge_end", $$6.getRotated($$9.getA()), true));
                        EndCityPieces.recursiveChildren(p_227484_, EndCityPieces.TOWER_BRIDGE_GENERATOR, p_227485_ + 1, $$10, null, p_227488_, p_227489_);
                    }
                }
            }
            $$7 = EndCityPieces.addHelper(p_227488_, EndCityPieces.addPiece(p_227484_, $$7, new BlockPos(-2, 8, -2), "fat_tower_top", $$6, true));
            return true;
        }
    };

    static EndCityPieces.EndCityPiece addPiece(StructureTemplateManager structureTemplateManager0, EndCityPieces.EndCityPiece endCityPiecesEndCityPiece1, BlockPos blockPos2, String string3, Rotation rotation4, boolean boolean5) {
        EndCityPieces.EndCityPiece $$6 = new EndCityPieces.EndCityPiece(structureTemplateManager0, string3, endCityPiecesEndCityPiece1.m_226912_(), rotation4, boolean5);
        BlockPos $$7 = endCityPiecesEndCityPiece1.m_226911_().calculateConnectedPosition(endCityPiecesEndCityPiece1.m_226913_(), blockPos2, $$6.m_226913_(), BlockPos.ZERO);
        $$6.m_6324_($$7.m_123341_(), $$7.m_123342_(), $$7.m_123343_());
        return $$6;
    }

    public static void startHouseTower(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, List<StructurePiece> listStructurePiece3, RandomSource randomSource4) {
        FAT_TOWER_GENERATOR.init();
        HOUSE_TOWER_GENERATOR.init();
        TOWER_BRIDGE_GENERATOR.init();
        TOWER_GENERATOR.init();
        EndCityPieces.EndCityPiece $$5 = addHelper(listStructurePiece3, new EndCityPieces.EndCityPiece(structureTemplateManager0, "base_floor", blockPos1, rotation2, true));
        $$5 = addHelper(listStructurePiece3, addPiece(structureTemplateManager0, $$5, new BlockPos(-1, 0, -1), "second_floor_1", rotation2, false));
        $$5 = addHelper(listStructurePiece3, addPiece(structureTemplateManager0, $$5, new BlockPos(-1, 4, -1), "third_floor_1", rotation2, false));
        $$5 = addHelper(listStructurePiece3, addPiece(structureTemplateManager0, $$5, new BlockPos(-1, 8, -1), "third_roof", rotation2, true));
        recursiveChildren(structureTemplateManager0, TOWER_GENERATOR, 1, $$5, null, listStructurePiece3, randomSource4);
    }

    static EndCityPieces.EndCityPiece addHelper(List<StructurePiece> listStructurePiece0, EndCityPieces.EndCityPiece endCityPiecesEndCityPiece1) {
        listStructurePiece0.add(endCityPiecesEndCityPiece1);
        return endCityPiecesEndCityPiece1;
    }

    static boolean recursiveChildren(StructureTemplateManager structureTemplateManager0, EndCityPieces.SectionGenerator endCityPiecesSectionGenerator1, int int2, EndCityPieces.EndCityPiece endCityPiecesEndCityPiece3, BlockPos blockPos4, List<StructurePiece> listStructurePiece5, RandomSource randomSource6) {
        if (int2 > 8) {
            return false;
        } else {
            List<StructurePiece> $$7 = Lists.newArrayList();
            if (endCityPiecesSectionGenerator1.generate(structureTemplateManager0, int2, endCityPiecesEndCityPiece3, blockPos4, $$7, randomSource6)) {
                boolean $$8 = false;
                int $$9 = randomSource6.nextInt();
                for (StructurePiece $$10 : $$7) {
                    $$10.setGenDepth($$9);
                    StructurePiece $$11 = StructurePiece.findCollisionPiece(listStructurePiece5, $$10.getBoundingBox());
                    if ($$11 != null && $$11.getGenDepth() != endCityPiecesEndCityPiece3.m_73548_()) {
                        $$8 = true;
                        break;
                    }
                }
                if (!$$8) {
                    listStructurePiece5.addAll($$7);
                    return true;
                }
            }
            return false;
        }
    }

    public static class EndCityPiece extends TemplateStructurePiece {

        public EndCityPiece(StructureTemplateManager structureTemplateManager0, String string1, BlockPos blockPos2, Rotation rotation3, boolean boolean4) {
            super(StructurePieceType.END_CITY_PIECE, 0, structureTemplateManager0, makeResourceLocation(string1), string1, makeSettings(boolean4, rotation3), blockPos2);
        }

        public EndCityPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
            super(StructurePieceType.END_CITY_PIECE, compoundTag1, structureTemplateManager0, p_227512_ -> makeSettings(compoundTag1.getBoolean("OW"), Rotation.valueOf(compoundTag1.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(boolean boolean0, Rotation rotation1) {
            BlockIgnoreProcessor $$2 = boolean0 ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
            return new StructurePlaceSettings().setIgnoreEntities(true).addProcessor($$2).setRotation(rotation1);
        }

        @Override
        protected ResourceLocation makeTemplateLocation() {
            return makeResourceLocation(this.f_163658_);
        }

        private static ResourceLocation makeResourceLocation(String string0) {
            return new ResourceLocation("end_city/" + string0);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putString("Rot", this.f_73657_.getRotation().name());
            compoundTag1.putBoolean("OW", this.f_73657_.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
            if (string0.startsWith("Chest")) {
                BlockPos $$5 = blockPos1.below();
                if (boundingBox4.isInside($$5)) {
                    RandomizableContainerBlockEntity.setLootTable(serverLevelAccessor2, randomSource3, $$5, BuiltInLootTables.END_CITY_TREASURE);
                }
            } else if (boundingBox4.isInside(blockPos1) && Level.isInSpawnableBounds(blockPos1)) {
                if (string0.startsWith("Sentry")) {
                    Shulker $$6 = EntityType.SHULKER.create(serverLevelAccessor2.getLevel());
                    if ($$6 != null) {
                        $$6.setPos((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_() + 0.5);
                        serverLevelAccessor2.m_7967_($$6);
                    }
                } else if (string0.startsWith("Elytra")) {
                    ItemFrame $$7 = new ItemFrame(serverLevelAccessor2.getLevel(), blockPos1, this.f_73657_.getRotation().rotate(Direction.SOUTH));
                    $$7.setItem(new ItemStack(Items.ELYTRA), false);
                    serverLevelAccessor2.m_7967_($$7);
                }
            }
        }
    }

    interface SectionGenerator {

        void init();

        boolean generate(StructureTemplateManager var1, int var2, EndCityPieces.EndCityPiece var3, BlockPos var4, List<StructurePiece> var5, RandomSource var6);
    }
}