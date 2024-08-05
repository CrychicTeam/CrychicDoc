package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.smartObserver.SmartObserverBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.TankManipulationBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.FluidFormatter;
import com.simibubi.create.foundation.utility.IntAttached;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;

public class FluidListDisplaySource extends ValueListDisplaySource {

    @Override
    protected Stream<IntAttached<MutableComponent>> provideEntries(DisplayLinkContext context, int maxRows) {
        if (context.getSourceBlockEntity() instanceof SmartObserverBlockEntity cobe) {
            TankManipulationBehaviour tankManipulationBehaviour = cobe.getBehaviour(TankManipulationBehaviour.OBSERVE);
            FilteringBehaviour filteringBehaviour = cobe.getBehaviour(FilteringBehaviour.TYPE);
            IFluidHandler handler = tankManipulationBehaviour.getInventory();
            if (handler == null) {
                return Stream.empty();
            } else {
                Map<Fluid, Integer> fluids = new HashMap();
                Map<Fluid, FluidStack> fluidNames = new HashMap();
                for (int i = 0; i < handler.getTanks(); i++) {
                    FluidStack stack = handler.getFluidInTank(i);
                    if (!stack.isEmpty() && filteringBehaviour.test(stack)) {
                        fluids.merge(stack.getFluid(), stack.getAmount(), Integer::sum);
                        fluidNames.putIfAbsent(stack.getFluid(), stack);
                    }
                }
                return fluids.entrySet().stream().sorted(Comparator.comparingInt(value -> (Integer) value.getValue()).reversed()).limit((long) maxRows).map(entry -> IntAttached.with((Integer) entry.getValue(), Components.translatable(((FluidStack) fluidNames.get(entry.getKey())).getTranslationKey())));
            }
        } else {
            return Stream.empty();
        }
    }

    @Override
    protected List<MutableComponent> createComponentsFromEntry(DisplayLinkContext context, IntAttached<MutableComponent> entry) {
        int amount = entry.getFirst();
        MutableComponent name = entry.getSecond().append(WHITESPACE);
        Couple<MutableComponent> formatted = FluidFormatter.asComponents((long) amount, this.shortenNumbers(context));
        return List.of(formatted.getFirst(), formatted.getSecond(), name);
    }

    @Override
    public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayBlockEntity flapDisplay, FlapDisplayLayout layout) {
        Integer max = ((MutableInt) context.flapDisplayContext).getValue();
        boolean shorten = this.shortenNumbers(context);
        int length = FluidFormatter.asString((long) max.intValue(), shorten).length();
        String layoutKey = "FluidList_" + length;
        if (!layout.isLayout(layoutKey)) {
            int maxCharCount = flapDisplay.getMaxCharCount(1);
            int numberLength = Math.min(maxCharCount, Math.max(3, length - 2));
            int nameLength = Math.max(maxCharCount - numberLength - 2, 0);
            FlapDisplaySection value = new FlapDisplaySection(7.0F * (float) numberLength, "number", false, false).rightAligned();
            FlapDisplaySection unit = new FlapDisplaySection(14.0F, "fluid_units", true, true);
            FlapDisplaySection name = new FlapDisplaySection(7.0F * (float) nameLength, "alphabet", false, false);
            layout.configure(layoutKey, List.of(value, unit, name));
        }
    }

    @Override
    protected String getTranslationKey() {
        return "list_fluids";
    }

    @Override
    protected boolean valueFirst() {
        return false;
    }
}