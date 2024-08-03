package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

public class BoilerDisplaySource extends DisplaySource {

    public static final List<MutableComponent> notEnoughSpaceSingle = List.of(Lang.translateDirect("display_source.boiler.not_enough_space").append(Lang.translateDirect("display_source.boiler.for_boiler_status")));

    public static final List<MutableComponent> notEnoughSpaceDouble = List.of(Lang.translateDirect("display_source.boiler.not_enough_space"), Lang.translateDirect("display_source.boiler.for_boiler_status"));

    public static final List<List<MutableComponent>> notEnoughSpaceFlap = List.of(List.of(Lang.translateDirect("display_source.boiler.not_enough_space")), List.of(Lang.translateDirect("display_source.boiler.for_boiler_status")));

    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        if (stats.maxRows() < 2) {
            return notEnoughSpaceSingle;
        } else if (stats.maxRows() < 4) {
            return notEnoughSpaceDouble;
        } else {
            boolean isBook = context.getTargetBlockEntity() instanceof LecternBlockEntity;
            if (isBook) {
                Stream<MutableComponent> componentList = this.getComponents(context, false).map(components -> {
                    Optional<MutableComponent> reduce = components.stream().reduce(MutableComponent::m_7220_);
                    return (MutableComponent) reduce.orElse(EMPTY_LINE);
                });
                return List.of((MutableComponent) componentList.reduce((comp1, comp2) -> comp1.append(Components.literal("\n")).append(comp2)).orElse(EMPTY_LINE));
            } else {
                return this.getComponents(context, false).map(components -> {
                    Optional<MutableComponent> reduce = components.stream().reduce(MutableComponent::m_7220_);
                    return (MutableComponent) reduce.orElse(EMPTY_LINE);
                }).toList();
            }
        }
    }

    @Override
    public List<List<MutableComponent>> provideFlapDisplayText(DisplayLinkContext context, DisplayTargetStats stats) {
        if (stats.maxRows() < 4) {
            context.flapDisplayContext = Boolean.FALSE;
            return notEnoughSpaceFlap;
        } else {
            List<List<MutableComponent>> components = this.getComponents(context, true).toList();
            if ((float) stats.maxColumns() * 7.0F < 42.0F + (float) ((MutableComponent) ((List) components.get(1)).get(1)).getString().length() * 9.0F) {
                context.flapDisplayContext = Boolean.FALSE;
                return notEnoughSpaceFlap;
            } else {
                return components;
            }
        }
    }

    @Override
    public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayBlockEntity flapDisplay, FlapDisplayLayout layout, int lineIndex) {
        if (lineIndex == 0 || context.flapDisplayContext instanceof Boolean b && !b) {
            if (!layout.isLayout("Default")) {
                layout.loadDefault(flapDisplay.getMaxCharCount());
            }
        } else {
            String layoutKey = "Boiler";
            if (!layout.isLayout(layoutKey)) {
                int labelLength = (int) ((float) this.labelWidth() * 7.0F);
                float maxSpace = (float) flapDisplay.getMaxCharCount(1) * 7.0F;
                FlapDisplaySection label = new FlapDisplaySection((float) labelLength, "alphabet", false, true);
                FlapDisplaySection symbols = new FlapDisplaySection(maxSpace - (float) labelLength, "pixel", false, false).wideFlaps();
                layout.configure(layoutKey, List.of(label, symbols));
            }
        }
    }

    private Stream<List<MutableComponent>> getComponents(DisplayLinkContext context, boolean forFlapDisplay) {
        if (context.getSourceBlockEntity() instanceof FluidTankBlockEntity tankBlockEntity) {
            FluidTankBlockEntity var12 = tankBlockEntity.getControllerBE();
            if (var12 == null) {
                return Stream.of(EMPTY);
            } else {
                BoilerData boiler = var12.boiler;
                int totalTankSize = var12.getTotalTankSize();
                boiler.calcMinMaxForSize(totalTankSize);
                String label = forFlapDisplay ? "boiler.status" : "boiler.status_short";
                MutableComponent size = this.labelOf(forFlapDisplay ? "size" : "");
                MutableComponent water = this.labelOf(forFlapDisplay ? "water" : "");
                MutableComponent heat = this.labelOf(forFlapDisplay ? "heat" : "");
                int lw = this.labelWidth();
                if (forFlapDisplay) {
                    size = Components.literal(Strings.repeat(' ', lw - this.labelWidthOf("size"))).append(size);
                    water = Components.literal(Strings.repeat(' ', lw - this.labelWidthOf("water"))).append(water);
                    heat = Components.literal(Strings.repeat(' ', lw - this.labelWidthOf("heat"))).append(heat);
                }
                return Stream.of(List.of(Lang.translateDirect(label, boiler.getHeatLevelTextComponent())), List.of(size, boiler.getSizeComponent(!forFlapDisplay, forFlapDisplay, ChatFormatting.RESET)), List.of(water, boiler.getWaterComponent(!forFlapDisplay, forFlapDisplay, ChatFormatting.RESET)), List.of(heat, boiler.getHeatComponent(!forFlapDisplay, forFlapDisplay, ChatFormatting.RESET)));
            }
        } else {
            return Stream.of(EMPTY);
        }
    }

    private int labelWidth() {
        return Math.max(this.labelWidthOf("water"), Math.max(this.labelWidthOf("size"), this.labelWidthOf("heat")));
    }

    private int labelWidthOf(String label) {
        return this.labelOf(label).getString().length();
    }

    private MutableComponent labelOf(String label) {
        return label.isBlank() ? Components.empty() : Lang.translateDirect("boiler." + label);
    }

    @Override
    protected String getTranslationKey() {
        return "boiler_status";
    }
}