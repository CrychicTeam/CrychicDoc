package se.mickelus.tetra.levelgen;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.blocks.multischematic.MultiblockSchematicBlock;
import se.mickelus.tetra.blocks.multischematic.PrimaryMultiblockSchematicBlock;
import se.mickelus.tetra.blocks.multischematic.RuinedMultiblockSchematicBlock;
import se.mickelus.tetra.util.StreamHelper;

@ParametersAreNonnullByDefault
public class MultiblockSchematicProcessor extends StructureProcessor {

    public static final MultiblockSchematicProcessor INSTANCE = new MultiblockSchematicProcessor();

    public static final Codec<MultiblockSchematicProcessor> codec = Codec.unit(() -> INSTANCE);

    public static RegistryObject<StructureProcessorType<?>> type;

    @Nullable
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo $, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template) {
        if (blockInfo.state().m_60734_() instanceof MultiblockSchematicBlock block) {
            Random random = new Random(Mth.getSeed(pos));
            int size = block.height * block.width;
            boolean isRuined = ((List) IntStream.range(0, size).boxed().collect(StreamHelper.toShuffledList(random))).stream().limit((long) ((int) ((double) size * 0.6))).anyMatch(index -> index == block.y * block.width + block.x);
            if (isRuined) {
                BlockState newState = (BlockState) block.ruinedRef.get().m_49966_().m_61124_(RuinedMultiblockSchematicBlock.facingProp, (Direction) blockInfo.state().m_61143_(MultiblockSchematicBlock.facingProp));
                return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), newState, blockInfo.nbt());
            }
        } else if (blockInfo.state().m_60734_() instanceof PrimaryMultiblockSchematicBlock) {
            BlockState newState = (BlockState) blockInfo.state().m_61124_(PrimaryMultiblockSchematicBlock.complete, false);
            return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), newState, blockInfo.nbt());
        }
        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return type.get();
    }
}