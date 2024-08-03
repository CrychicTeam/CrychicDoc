package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.FluidDialBlockEntity;
import com.rekindled.embers.datagen.EmbersFluidTags;
import com.rekindled.embers.util.FluidAmounts;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidDialBlock extends DialBaseBlock {

    public static final String DIAL_TYPE = "fluid";

    public FluidDialBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos.relative((Direction) state.m_61143_(f_52588_), -1));
        if (blockEntity != null) {
            IFluidHandler cap = (IFluidHandler) blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, ((Direction) state.m_61143_(f_52588_)).getOpposite()).orElse((IFluidHandler) blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null));
            if (cap != null) {
                int totalCapacity = 0;
                int totalContents = 0;
                for (int i = 0; i < cap.getTanks(); i++) {
                    totalCapacity += cap.getTankCapacity(i);
                    totalContents += cap.getFluidInTank(i).getAmount();
                }
                if (totalContents >= totalCapacity) {
                    return 15;
                }
                return (int) Math.ceil(14.0 * (double) totalContents / (double) totalCapacity);
            }
        }
        return 0;
    }

    @Override
    protected void getBEData(Direction facing, ArrayList<Component> text, BlockEntity blockEntity, int maxLines) {
        if (blockEntity instanceof FluidDialBlockEntity dial && dial.display) {
            int extraLines = 0;
            for (int i = 0; i < dial.fluids.length && i + extraLines < maxLines; i++) {
                FluidStack contents = dial.fluids[i];
                text.add(formatFluidStack(contents, dial.capacities[i]));
                if (contents.getFluid().is(EmbersFluidTags.INGOT_TOOLTIP) && contents.getAmount() >= 10) {
                    if (i + extraLines + 1 < maxLines) {
                        text.add(FluidAmounts.getIngotTooltip(contents.getAmount()));
                    }
                    extraLines++;
                }
            }
            if (dial.fluids.length + dial.extraLines + extraLines > Math.min(maxLines, dial.fluids.length + extraLines)) {
                text.add(Component.translatable("embers.tooltip.too_many", dial.fluids.length + extraLines - Math.min(maxLines, dial.fluids.length + extraLines) + dial.extraLines));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static MutableComponent formatFluidStack(FluidStack contents, int capacity) {
        return !contents.isEmpty() ? Component.translatable("embers.tooltip.fluiddial.fluid", contents.getDisplayName().getString(), contents.getAmount(), capacity) : Component.translatable("embers.tooltip.fluiddial.nofluid", capacity);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.FLUID_DIAL_ENTITY.get().create(pPos, pState);
    }

    @Override
    public String getDialType() {
        return "fluid";
    }
}