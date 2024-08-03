package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class JungleTempleStructure extends SinglePieceStructure {

    public static final Codec<JungleTempleStructure> CODEC = m_226607_(JungleTempleStructure::new);

    public JungleTempleStructure(Structure.StructureSettings structureStructureSettings0) {
        super(JungleTemplePiece::new, 12, 15, structureStructureSettings0);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.JUNGLE_TEMPLE;
    }
}