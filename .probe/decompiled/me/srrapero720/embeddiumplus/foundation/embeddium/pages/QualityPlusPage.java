package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
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

public class QualityPlusPage extends OptionPage {

    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation("embeddiumplus", "quality"));

    public QualityPlusPage() {
        super(ID, Component.translatable("sodium.options.pages.quality").append("++"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        List<OptionGroup> groups = new ArrayList();
        OptionImpl<?, Boolean> fog = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.fog.title")).setTooltip(Component.translatable("embeddium.plus.options.fog.desc")).setControl(TickBoxControl::new).setBinding((options, value) -> {
            EmbyConfig.fog.set(value);
            EmbyConfig.fogCache = value;
        }, options -> EmbyConfig.fogCache).setImpact(OptionImpact.LOW).build();
        OptionImpl<?, Boolean> blueBand = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.blueband.title")).setTooltip(Component.translatable("embeddium.plus.options.blueband.desc")).setControl(TickBoxControl::new).setBinding((opt, v) -> {
            EmbyConfig.blueBand.set(v);
            EmbyConfig.blueBandCache = v;
        }, opt -> EmbyConfig.blueBandCache).build();
        OptionImpl<?, EmbyConfig.ChunkFadeSpeed> fadeInQuality = OptionImpl.createBuilder(EmbyConfig.ChunkFadeSpeed.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.fadein.title")).setTooltip(Component.translatable("embeddium.plus.options.fadein.desc")).setControl(option -> new CyclingControl(option, EmbyConfig.ChunkFadeSpeed.class, new Component[] { Component.translatable("options.off"), Component.translatable("options.graphics.fast"), Component.translatable("options.graphics.fancy") })).setBinding((opts, value) -> EmbyConfig.chunkFadeSpeed.set(value), opts -> (EmbyConfig.ChunkFadeSpeed) EmbyConfig.chunkFadeSpeed.get()).setImpact(OptionImpact.LOW).setEnabled(false).build();
        groups.add(OptionGroup.createBuilder().add(fog).add(blueBand).add(fadeInQuality).build());
        OptionImpl<?, Integer> cloudHeight = OptionImpl.createBuilder(int.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.clouds.height.title")).setTooltip(Component.translatable("embeddium.plus.options.clouds.height.desc")).setControl(option -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.biomeBlend())).setBinding((options, value) -> {
            EmbyConfig.cloudsHeight.set(value);
            EmbyConfig.cloudsHeightCache = value;
        }, options -> EmbyConfig.cloudsHeightCache).build();
        groups.add(OptionGroup.createBuilder().add(cloudHeight).build());
        OptionImpl<?, Boolean> disableNameTagRendering = OptionImpl.createBuilder(boolean.class, EmbPlusOptions.STORAGE).setName(Component.translatable("embeddium.plus.options.nametag.disable_rendering.title")).setTooltip(Component.translatable("embeddium.plus.options.nametag.disable_rendering.desc")).setControl(TickBoxControl::new).setBinding((opt, v) -> {
            EmbyConfig.disableNameTagRender.set(v);
            EmbyConfig.disableNameTagRenderCache = v;
        }, opt -> EmbyConfig.disableNameTagRenderCache).build();
        groups.add(OptionGroup.createBuilder().add(disableNameTagRendering).build());
        return ImmutableList.copyOf(groups);
    }
}