package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.embeddium.EmbPlusOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

public class TrueDarknessPage extends OptionPage {

    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation("embeddiumplus", "true_darkness"));

    public TrueDarknessPage() {
        super(ID, Component.translatable("embeddium.plus.options.darkness.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        List<OptionGroup> groups = new ArrayList();
        OptionImpl<?, EmbyConfig.DarknessMode> darknessMode = OptionImpl.createBuilder(EmbyConfig.DarknessMode.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.mode.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.mode.desc")).setControl(option -> new CyclingControl(option, EmbyConfig.DarknessMode.class, new Component[] { Component.translatable("embeddium.plus.options.darkness.mode.pitchblack"), Component.translatable("embeddium.plus.options.darkness.mode.reallydark"), Component.translatable("embeddium.plus.options.darkness.mode.dark"), Component.translatable("embeddium.plus.options.darkness.mode.dim"), Component.translatable("options.off") })).setBinding((opts, value) -> EmbyConfig.darknessMode.set(value), opts -> (EmbyConfig.DarknessMode) EmbyConfig.darknessMode.get()).build();
        OptionImpl<?, Boolean> noSkylight = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.noskylight.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.noskylight.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessOnNoSkyLight.set(value);
            EmbyConfig.darknessOnNoSkyLightCache = value;
        }, options -> EmbyConfig.darknessOnNoSkyLightCache).build();
        groups.add(OptionGroup.createBuilder().add(darknessMode).add(noSkylight).build());
        OptionImpl<?, Boolean> darknessOtherDim = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.others.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.others.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessByDefault.set(value);
            EmbyConfig.darknessByDefaultCache = value;
        }, options -> EmbyConfig.darknessByDefaultCache).build();
        OptionImpl<?, Boolean> darknessOnOverworld = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.overworld.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.overworld.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessOnOverworld.set(value);
            EmbyConfig.darknessOnOverworldCache = value;
        }, options -> EmbyConfig.darknessOnOverworldCache).build();
        OptionImpl<?, Boolean> darknessOnNether = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.nether.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.nether.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessOnNether.set(value);
            EmbyConfig.darknessOnNetherCache = value;
        }, options -> EmbyConfig.darknessOnNetherCache).build();
        OptionImpl<?, Integer> netherFogBright = OptionImpl.createBuilder(int.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.nether.brightness.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.nether.brightness.desc")).setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage())).setBinding((options, current) -> {
            double value = (double) current.intValue() / 100.0;
            EmbyConfig.darknessNetherFogBright.set(Double.valueOf(value));
            EmbyConfig.darknessNetherFogBrightCache = value;
        }, options -> Math.toIntExact(Math.round(EmbyConfig.darknessNetherFogBrightCache * 100.0))).build();
        OptionImpl<?, Boolean> darknessOnEnd = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.end.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.end.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessOnEnd.set(value);
            EmbyConfig.darknessOnEndCache = value;
        }, options -> EmbyConfig.darknessOnEndCache).build();
        OptionImpl<?, Integer> endFogBright = OptionImpl.createBuilder(int.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.end.brightness.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.end.brightness.desc")).setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage())).setBinding((options, current) -> {
            double value = (double) current.intValue() / 100.0;
            EmbyConfig.darknessEndFogBright.set(Double.valueOf(value));
            EmbyConfig.darknessEndFogBrightCache = value;
        }, options -> Math.toIntExact(Math.round(EmbyConfig.darknessEndFogBrightCache * 100.0))).build();
        groups.add(OptionGroup.createBuilder().add(darknessOtherDim).add(darknessOnOverworld).build());
        groups.add(OptionGroup.createBuilder().add(darknessOnNether).add(netherFogBright).build());
        groups.add(OptionGroup.createBuilder().add(darknessOnEnd).add(endFogBright).build());
        OptionImpl<?, Boolean> blockLightOnly = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.blocklightonly.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.blocklightonly.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessBlockLightOnly.set(value);
            EmbyConfig.darknessBlockLightOnlyCache = value;
        }, options -> EmbyConfig.darknessBlockLightOnlyCache).setEnabled(false).build();
        OptionImpl<?, Boolean> affectedByMoonPhase = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.moonphase.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.moonphase.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.darknessAffectedByMoonPhase.set(value);
            EmbyConfig.darknessAffectedByMoonPhaseCache = value;
        }, options -> EmbyConfig.darknessAffectedByMoonPhaseCache).build();
        OptionImpl<?, Integer> newMoonBright = OptionImpl.createBuilder(int.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.moonphase.fresh.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.moonphase.fresh.desc")).setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage())).setBinding((options, current) -> {
            double value = (double) current.intValue() / 100.0;
            EmbyConfig.darknessNewMoonBright.set(Double.valueOf(value));
            EmbyConfig.darknessNewMoonBrightCache = value;
        }, options -> Math.toIntExact(Math.round(EmbyConfig.darknessNewMoonBrightCache * 100.0))).build();
        OptionImpl<?, Integer> fullMoonBright = OptionImpl.createBuilder(int.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.darkness.moonphase.full.title")).setTooltip(Component.translatable("embeddium.plus.options.darkness.moonphase.full.desc")).setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage())).setBinding((options, current) -> {
            double value = (double) current.intValue() / 100.0;
            EmbyConfig.darknessFullMoonBright.set(Double.valueOf(value));
            EmbyConfig.darknessFullMoonBrightCache = value;
        }, options -> Math.toIntExact(Math.round(EmbyConfig.darknessFullMoonBrightCache * 100.0))).build();
        groups.add(OptionGroup.createBuilder().add(blockLightOnly).add(affectedByMoonPhase).add(newMoonBright).add(fullMoonBright).build());
        return ImmutableList.copyOf(groups);
    }
}