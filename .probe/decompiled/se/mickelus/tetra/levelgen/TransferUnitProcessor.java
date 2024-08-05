package se.mickelus.tetra.levelgen;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.blocks.forged.transfer.EnumTransferConfig;
import se.mickelus.tetra.blocks.forged.transfer.TransferUnitBlock;
import se.mickelus.tetra.blocks.forged.transfer.TransferUnitBlockEntity;
import se.mickelus.tetra.items.cell.ThermalCellItem;

@ParametersAreNonnullByDefault
public class TransferUnitProcessor extends StructureProcessor {

    public static final TransferUnitProcessor INSTANCE = new TransferUnitProcessor();

    public static final Codec<TransferUnitProcessor> codec = Codec.unit(() -> INSTANCE);

    public static RegistryObject<StructureProcessorType<?>> type;

    @Nullable
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo $, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template) {
        if (blockInfo.state().m_60734_() instanceof TransferUnitBlock) {
            RandomSource random = placementSettings.getRandom(blockInfo.pos());
            CompoundTag newCompound = blockInfo.nbt().copy();
            int cellState = 0;
            if ((double) random.nextFloat() < 0.1) {
                int charge = random.nextInt(128);
                ItemStack itemStack = new ItemStack(ThermalCellItem.instance.get());
                ThermalCellItem.recharge(itemStack, charge);
                cellState = charge > 0 ? 2 : 1;
                TransferUnitBlockEntity.writeCell(newCompound, itemStack);
            } else if ((double) random.nextFloat() < 0.2) {
                TransferUnitBlockEntity.writeCell(newCompound, new ItemStack(ThermalCellItem.instance.get()));
                cellState = 1;
            }
            EnumTransferConfig[] configs = EnumTransferConfig.values();
            BlockState newState = (BlockState) ((BlockState) ((BlockState) blockInfo.state().m_61124_(TransferUnitBlock.cellProp, cellState)).m_61124_(TransferUnitBlock.configProp, configs[random.nextInt(configs.length)])).m_61124_(TransferUnitBlock.plateProp, random.nextBoolean());
            return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), newState, newCompound);
        } else {
            return blockInfo;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return type.get();
    }
}