package noppes.npcs.api.wrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import noppes.npcs.api.block.IBlockFluidContainer;

public class BlockFluidContainerWrapper extends BlockWrapper implements IBlockFluidContainer {

    private IFluidBlock block;

    public BlockFluidContainerWrapper(Level level, Block block, BlockPos pos) {
        super(level, block, pos);
        this.block = (IFluidBlock) block;
    }

    @Override
    public float getFluidPercentage() {
        return this.block.getFilledPercentage(this.level.getMCLevel(), this.pos);
    }

    @Override
    public float getFuildDensity() {
        return (float) this.block.getFluid().getFluidType().getDensity(this.level.getMCLevel().m_6425_(this.pos), this.level.getMCLevel(), this.pos);
    }

    @Override
    public float getFuildTemperature() {
        return (float) this.block.getFluid().getFluidType().getTemperature(this.level.getMCLevel().m_6425_(this.pos), this.level.getMCLevel(), this.pos);
    }

    @Override
    public String getFluidName() {
        return ((IForgeRegistry) ForgeRegistries.FLUID_TYPES.get()).getKey(this.block.getFluid().getFluidType()).toString();
    }
}