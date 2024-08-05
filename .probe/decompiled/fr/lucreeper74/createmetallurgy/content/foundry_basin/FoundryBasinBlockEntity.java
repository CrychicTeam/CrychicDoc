package fr.lucreeper74.createmetallurgy.content.foundry_basin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.LangBuilder;
import fr.lucreeper74.createmetallurgy.utils.CMLang;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class FoundryBasinBlockEntity extends BasinBlockEntity {

    public FoundryBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inputInventory = (BasinInventory) new BasinInventory(1, this).withMaxStackSize(9);
        this.outputInventory = new BasinInventory(1, this).forbidInsertion().withMaxStackSize(9);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CMLang.translate("gui.goggles.foundrybasin_contents").forGoggles(tooltip);
        IItemHandlerModifiable items = this.itemCapability.orElse(new ItemStackHandler());
        IFluidHandler fluids = this.fluidCapability.orElse(new FluidTank(0));
        boolean isEmpty = true;
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                CMLang.text("").add(Components.translatable(stackInSlot.getDescriptionId()).withStyle(ChatFormatting.GRAY)).add(CMLang.text(" x" + stackInSlot.getCount()).style(ChatFormatting.GREEN)).forGoggles(tooltip, 1);
                isEmpty = false;
            }
        }
        LangBuilder mb = CMLang.translate("generic.unit.millibuckets");
        for (int ix = 0; ix < fluids.getTanks(); ix++) {
            FluidStack fluidStack = fluids.getFluidInTank(ix);
            if (!fluidStack.isEmpty()) {
                CMLang.text("").add(CMLang.fluidName(fluidStack).add(CMLang.text(" ")).style(ChatFormatting.GRAY).add(CMLang.number((double) fluidStack.getAmount()).add(mb).style(ChatFormatting.BLUE))).forGoggles(tooltip, 1);
                isEmpty = false;
            }
        }
        if (isEmpty) {
            tooltip.remove(0);
        }
        return true;
    }
}