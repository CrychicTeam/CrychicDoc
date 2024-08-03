package net.minecraft.world.level.levelgen.structure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.function.Function;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;

public abstract class TemplateStructurePiece extends StructurePiece {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final String templateName;

    protected StructureTemplate template;

    protected StructurePlaceSettings placeSettings;

    protected BlockPos templatePosition;

    public TemplateStructurePiece(StructurePieceType structurePieceType0, int int1, StructureTemplateManager structureTemplateManager2, ResourceLocation resourceLocation3, String string4, StructurePlaceSettings structurePlaceSettings5, BlockPos blockPos6) {
        super(structurePieceType0, int1, structureTemplateManager2.getOrCreate(resourceLocation3).getBoundingBox(structurePlaceSettings5, blockPos6));
        this.m_73519_(Direction.NORTH);
        this.templateName = string4;
        this.templatePosition = blockPos6;
        this.template = structureTemplateManager2.getOrCreate(resourceLocation3);
        this.placeSettings = structurePlaceSettings5;
    }

    public TemplateStructurePiece(StructurePieceType structurePieceType0, CompoundTag compoundTag1, StructureTemplateManager structureTemplateManager2, Function<ResourceLocation, StructurePlaceSettings> functionResourceLocationStructurePlaceSettings3) {
        super(structurePieceType0, compoundTag1);
        this.m_73519_(Direction.NORTH);
        this.templateName = compoundTag1.getString("Template");
        this.templatePosition = new BlockPos(compoundTag1.getInt("TPX"), compoundTag1.getInt("TPY"), compoundTag1.getInt("TPZ"));
        ResourceLocation $$4 = this.makeTemplateLocation();
        this.template = structureTemplateManager2.getOrCreate($$4);
        this.placeSettings = (StructurePlaceSettings) functionResourceLocationStructurePlaceSettings3.apply($$4);
        this.f_73383_ = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
    }

    protected ResourceLocation makeTemplateLocation() {
        return new ResourceLocation(this.templateName);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        compoundTag1.putInt("TPX", this.templatePosition.m_123341_());
        compoundTag1.putInt("TPY", this.templatePosition.m_123342_());
        compoundTag1.putInt("TPZ", this.templatePosition.m_123343_());
        compoundTag1.putString("Template", this.templateName);
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
        this.placeSettings.setBoundingBox(boundingBox4);
        this.f_73383_ = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
        if (this.template.placeInWorld(worldGenLevel0, this.templatePosition, blockPos6, this.placeSettings, randomSource3, 2)) {
            for (StructureTemplate.StructureBlockInfo $$8 : this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.STRUCTURE_BLOCK)) {
                if ($$8.nbt() != null) {
                    StructureMode $$9 = StructureMode.valueOf($$8.nbt().getString("mode"));
                    if ($$9 == StructureMode.DATA) {
                        this.handleDataMarker($$8.nbt().getString("metadata"), $$8.pos(), worldGenLevel0, randomSource3, boundingBox4);
                    }
                }
            }
            for (StructureTemplate.StructureBlockInfo $$11 : this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.JIGSAW)) {
                if ($$11.nbt() != null) {
                    String $$12 = $$11.nbt().getString("final_state");
                    BlockState $$13 = Blocks.AIR.defaultBlockState();
                    try {
                        $$13 = BlockStateParser.parseForBlock(worldGenLevel0.m_246945_(Registries.BLOCK), $$12, true).blockState();
                    } catch (CommandSyntaxException var15) {
                        LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", $$12, $$11.pos());
                    }
                    worldGenLevel0.m_7731_($$11.pos(), $$13, 3);
                }
            }
        }
    }

    protected abstract void handleDataMarker(String var1, BlockPos var2, ServerLevelAccessor var3, RandomSource var4, BoundingBox var5);

    @Deprecated
    @Override
    public void move(int int0, int int1, int int2) {
        super.move(int0, int1, int2);
        this.templatePosition = this.templatePosition.offset(int0, int1, int2);
    }

    @Override
    public Rotation getRotation() {
        return this.placeSettings.getRotation();
    }

    public StructureTemplate template() {
        return this.template;
    }

    public BlockPos templatePosition() {
        return this.templatePosition;
    }

    public StructurePlaceSettings placeSettings() {
        return this.placeSettings;
    }
}