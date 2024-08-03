package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;

public abstract class ValueListDisplaySource extends DisplaySource {

    static final int ENTRIES_PER_PAGE = 8;

    protected abstract Stream<IntAttached<MutableComponent>> provideEntries(DisplayLinkContext var1, int var2);

    protected abstract boolean valueFirst();

    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        boolean isBook = context.getTargetBlockEntity() instanceof LecternBlockEntity;
        List<MutableComponent> list = this.provideEntries(context, stats.maxRows() * (isBook ? 8 : 1)).map(e -> this.createComponentsFromEntry(context, e)).map(l -> {
            MutableComponent combined = ((MutableComponent) l.get(0)).append((Component) l.get(1));
            if (l.size() > 2) {
                combined.append((Component) l.get(2));
            }
            return combined;
        }).toList();
        if (isBook) {
            list = this.condensePages(list);
        }
        return list;
    }

    private List<MutableComponent> condensePages(List<MutableComponent> list) {
        List<MutableComponent> condensed = new ArrayList();
        MutableComponent current = null;
        for (int i = 0; i < list.size(); i++) {
            MutableComponent atIndex = (MutableComponent) list.get(i);
            if (current == null) {
                current = atIndex;
            } else {
                current.append(Components.literal("\n")).append(atIndex);
                if ((i + 1) % 8 == 0) {
                    condensed.add(current);
                    current = null;
                }
            }
        }
        if (current != null) {
            condensed.add(current);
        }
        return condensed;
    }

    @Override
    public List<List<MutableComponent>> provideFlapDisplayText(DisplayLinkContext context, DisplayTargetStats stats) {
        MutableInt highest = new MutableInt(0);
        context.flapDisplayContext = highest;
        return this.provideEntries(context, stats.maxRows()).map(e -> {
            highest.setValue(Math.max(highest.getValue(), e.getFirst()));
            return this.createComponentsFromEntry(context, e);
        }).toList();
    }

    protected List<MutableComponent> createComponentsFromEntry(DisplayLinkContext context, IntAttached<MutableComponent> entry) {
        int number = entry.getFirst();
        MutableComponent name = entry.getSecond().append(WHITESPACE);
        if (this.shortenNumbers(context)) {
            Couple<MutableComponent> shortened = this.shorten(number);
            return this.valueFirst() ? Arrays.asList(shortened.getFirst(), shortened.getSecond(), name) : Arrays.asList(name, shortened.getFirst(), shortened.getSecond());
        } else {
            MutableComponent formattedNumber = Components.literal(String.valueOf(number)).append(WHITESPACE);
            return this.valueFirst() ? Arrays.asList(formattedNumber, name) : Arrays.asList(name, formattedNumber);
        }
    }

    @Override
    public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayBlockEntity flapDisplay, FlapDisplayLayout layout) {
        boolean valueFirst = this.valueFirst();
        boolean shortenNumbers = this.shortenNumbers(context);
        int valueFormat = shortenNumbers ? 0 : Math.max(4, 1 + (int) Math.log10((double) ((MutableInt) context.flapDisplayContext).intValue()));
        String layoutKey = "ValueList_" + valueFirst + "_" + valueFormat;
        if (!layout.isLayout(layoutKey)) {
            int maxCharCount = flapDisplay.getMaxCharCount(1);
            int numberLength = Math.min(maxCharCount, Math.max(3, valueFormat));
            int nameLength = Math.max(maxCharCount - numberLength - (shortenNumbers ? 1 : 0), 0);
            FlapDisplaySection name = new FlapDisplaySection(7.0F * (float) nameLength, "alphabet", false, !valueFirst);
            FlapDisplaySection value = new FlapDisplaySection(7.0F * (float) numberLength, "number", false, !shortenNumbers && valueFirst).rightAligned();
            if (shortenNumbers) {
                FlapDisplaySection suffix = new FlapDisplaySection(7.0F, "shortened_numbers", false, valueFirst);
                layout.configure(layoutKey, valueFirst ? Arrays.asList(value, suffix, name) : Arrays.asList(name, value, suffix));
            } else {
                layout.configure(layoutKey, valueFirst ? Arrays.asList(value, name) : Arrays.asList(name, value));
            }
        }
    }

    private Couple<MutableComponent> shorten(int number) {
        if (number >= 1000000) {
            return Couple.create(Components.literal(String.valueOf(number / 1000000)), Lang.translateDirect("display_source.value_list.million").append(WHITESPACE));
        } else {
            return number >= 1000 ? Couple.create(Components.literal(String.valueOf(number / 1000)), Lang.translateDirect("display_source.value_list.thousand").append(WHITESPACE)) : Couple.create(Components.literal(String.valueOf(number)), WHITESPACE);
        }
    }

    protected boolean shortenNumbers(DisplayLinkContext context) {
        return context.sourceConfig().getInt("Format") == 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        if (isFirstLine) {
            this.addFullNumberConfig(builder);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void addFullNumberConfig(ModularGuiLineBuilder builder) {
        builder.addSelectionScrollInput(0, 75, (si, l) -> si.forOptions(Lang.translatedOptions("display_source.value_list", "shortened", "full_number")).titled(Lang.translateDirect("display_source.value_list.display")), "Format");
    }
}