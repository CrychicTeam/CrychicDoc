package se.mickelus.tetra.levelgen;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.hammer.HammerEffect;
import se.mickelus.tetra.items.cell.ThermalCellItem;

@ParametersAreNonnullByDefault
public class ForgedHammerProcessor extends StructureProcessor {

    public static final ForgedHammerProcessor INSTANCE = new ForgedHammerProcessor();

    public static final Codec<ForgedHammerProcessor> codec = Codec.unit(() -> INSTANCE);

    public static RegistryObject<StructureProcessorType<?>> type;

    @Nullable
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo $, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettings, @Nullable StructureTemplate template) {
        if (blockInfo.state().m_60734_() instanceof HammerBaseBlock) {
            RandomSource random = placementSettings.getRandom(blockInfo.pos());
            CompoundTag newCompound = blockInfo.nbt().copy();
            ItemStack cell1 = random.nextBoolean() ? new ItemStack(ThermalCellItem.instance.get()) : null;
            ItemStack cell2 = random.nextBoolean() ? new ItemStack(ThermalCellItem.instance.get()) : null;
            int charge1 = random.nextInt(128);
            if (cell1 != null) {
                ThermalCellItem.recharge(cell1, charge1);
            }
            int charge2 = 128 - random.nextInt(Math.max(charge1, 1));
            if (cell2 != null) {
                ThermalCellItem.recharge(cell2, charge2);
            }
            HammerBaseBlockEntity.writeCells(newCompound, cell1, cell2);
            HammerEffect module = HammerEffect.efficient;
            if ((double) random.nextFloat() < 0.1) {
                module = HammerEffect.reliable;
            } else if ((double) random.nextFloat() < 0.1) {
                module = random.nextBoolean() ? HammerEffect.precise : HammerEffect.power;
            }
            if (random.nextBoolean()) {
                HammerBaseBlockEntity.writeModules(newCompound, module, null);
            } else {
                HammerBaseBlockEntity.writeModules(newCompound, null, module);
            }
            return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), blockInfo.state(), newCompound);
        } else {
            return blockInfo;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return type.get();
    }
}