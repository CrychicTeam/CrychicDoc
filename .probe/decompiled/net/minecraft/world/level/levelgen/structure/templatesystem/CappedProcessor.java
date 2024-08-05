package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.ServerLevelAccessor;

public class CappedProcessor extends StructureProcessor {

    public static final Codec<CappedProcessor> CODEC = RecordCodecBuilder.create(p_277598_ -> p_277598_.group(StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter(p_277456_ -> p_277456_.delegate), IntProvider.POSITIVE_CODEC.fieldOf("limit").forGetter(p_277680_ -> p_277680_.limit)).apply(p_277598_, CappedProcessor::new));

    private final StructureProcessor delegate;

    private final IntProvider limit;

    public CappedProcessor(StructureProcessor structureProcessor0, IntProvider intProvider1) {
        this.delegate = structureProcessor0;
        this.limit = intProvider1;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.CAPPED;
    }

    @Override
    public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevelAccessor0, BlockPos blockPos1, BlockPos blockPos2, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo3, List<StructureTemplate.StructureBlockInfo> listStructureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        if (this.limit.getMaxValue() != 0 && !listStructureTemplateStructureBlockInfo4.isEmpty()) {
            if (listStructureTemplateStructureBlockInfo3.size() != listStructureTemplateStructureBlockInfo4.size()) {
                Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + listStructureTemplateStructureBlockInfo3.size() + ", Processed size: " + listStructureTemplateStructureBlockInfo4.size());
                return listStructureTemplateStructureBlockInfo4;
            } else {
                RandomSource $$6 = RandomSource.create(serverLevelAccessor0.getLevel().getSeed()).forkPositional().at(blockPos1);
                int $$7 = Math.min(this.limit.sample($$6), listStructureTemplateStructureBlockInfo4.size());
                if ($$7 < 1) {
                    return listStructureTemplateStructureBlockInfo4;
                } else {
                    IntArrayList $$8 = Util.toShuffledList(IntStream.range(0, listStructureTemplateStructureBlockInfo4.size()), $$6);
                    IntIterator $$9 = $$8.intIterator();
                    int $$10 = 0;
                    while ($$9.hasNext() && $$10 < $$7) {
                        int $$11 = $$9.nextInt();
                        StructureTemplate.StructureBlockInfo $$12 = (StructureTemplate.StructureBlockInfo) listStructureTemplateStructureBlockInfo3.get($$11);
                        StructureTemplate.StructureBlockInfo $$13 = (StructureTemplate.StructureBlockInfo) listStructureTemplateStructureBlockInfo4.get($$11);
                        StructureTemplate.StructureBlockInfo $$14 = this.delegate.processBlock(serverLevelAccessor0, blockPos1, blockPos2, $$12, $$13, structurePlaceSettings5);
                        if ($$14 != null && !$$13.equals($$14)) {
                            $$10++;
                            listStructureTemplateStructureBlockInfo4.set($$11, $$14);
                        }
                    }
                    return listStructureTemplateStructureBlockInfo4;
                }
            }
        } else {
            return listStructureTemplateStructureBlockInfo4;
        }
    }
}