package net.minecraft.world.level.levelgen.structure;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;

public class PoolElementStructurePiece extends StructurePiece {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final StructurePoolElement element;

    protected BlockPos position;

    private final int groundLevelDelta;

    protected final Rotation rotation;

    private final List<JigsawJunction> junctions = Lists.newArrayList();

    private final StructureTemplateManager structureTemplateManager;

    public PoolElementStructurePiece(StructureTemplateManager structureTemplateManager0, StructurePoolElement structurePoolElement1, BlockPos blockPos2, int int3, Rotation rotation4, BoundingBox boundingBox5) {
        super(StructurePieceType.JIGSAW, 0, boundingBox5);
        this.structureTemplateManager = structureTemplateManager0;
        this.element = structurePoolElement1;
        this.position = blockPos2;
        this.groundLevelDelta = int3;
        this.rotation = rotation4;
    }

    public PoolElementStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        super(StructurePieceType.JIGSAW, compoundTag1);
        this.structureTemplateManager = structurePieceSerializationContext0.structureTemplateManager();
        this.position = new BlockPos(compoundTag1.getInt("PosX"), compoundTag1.getInt("PosY"), compoundTag1.getInt("PosZ"));
        this.groundLevelDelta = compoundTag1.getInt("ground_level_delta");
        DynamicOps<Tag> $$2 = RegistryOps.create(NbtOps.INSTANCE, structurePieceSerializationContext0.registryAccess());
        this.element = (StructurePoolElement) StructurePoolElement.CODEC.parse($$2, compoundTag1.getCompound("pool_element")).resultOrPartial(LOGGER::error).orElseThrow(() -> new IllegalStateException("Invalid pool element found"));
        this.rotation = Rotation.valueOf(compoundTag1.getString("rotation"));
        this.f_73383_ = this.element.getBoundingBox(this.structureTemplateManager, this.position, this.rotation);
        ListTag $$3 = compoundTag1.getList("junctions", 10);
        this.junctions.clear();
        $$3.forEach(p_204943_ -> this.junctions.add(JigsawJunction.deserialize(new Dynamic($$2, p_204943_))));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        compoundTag1.putInt("PosX", this.position.m_123341_());
        compoundTag1.putInt("PosY", this.position.m_123342_());
        compoundTag1.putInt("PosZ", this.position.m_123343_());
        compoundTag1.putInt("ground_level_delta", this.groundLevelDelta);
        DynamicOps<Tag> $$2 = RegistryOps.create(NbtOps.INSTANCE, structurePieceSerializationContext0.registryAccess());
        StructurePoolElement.CODEC.encodeStart($$2, this.element).resultOrPartial(LOGGER::error).ifPresent(p_163125_ -> compoundTag1.put("pool_element", p_163125_));
        compoundTag1.putString("rotation", this.rotation.name());
        ListTag $$3 = new ListTag();
        for (JigsawJunction $$4 : this.junctions) {
            $$3.add((Tag) $$4.serialize($$2).getValue());
        }
        compoundTag1.put("junctions", $$3);
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
        this.place(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, blockPos6, false);
    }

    public void place(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, BlockPos blockPos5, boolean boolean6) {
        this.element.place(this.structureTemplateManager, worldGenLevel0, structureManager1, chunkGenerator2, this.position, blockPos5, this.rotation, boundingBox4, randomSource3, boolean6);
    }

    @Override
    public void move(int int0, int int1, int int2) {
        super.move(int0, int1, int2);
        this.position = this.position.offset(int0, int1, int2);
    }

    @Override
    public Rotation getRotation() {
        return this.rotation;
    }

    public String toString() {
        return String.format(Locale.ROOT, "<%s | %s | %s | %s>", this.getClass().getSimpleName(), this.position, this.rotation, this.element);
    }

    public StructurePoolElement getElement() {
        return this.element;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public int getGroundLevelDelta() {
        return this.groundLevelDelta;
    }

    public void addJunction(JigsawJunction jigsawJunction0) {
        this.junctions.add(jigsawJunction0);
    }

    public List<JigsawJunction> getJunctions() {
        return this.junctions;
    }
}