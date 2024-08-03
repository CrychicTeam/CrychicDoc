package dev.lambdaurora.lambdynlights.mixin.sodium;

import com.google.common.collect.ImmutableList;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import dev.lambdaurora.lambdynlights.config.QualityMode;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin({ SodiumOptionsGUI.class })
public abstract class SodiumSettingsMixin {

    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage dynamicLightsOpts = new SodiumOptionsStorage();

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void DynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList();
        OptionImpl<SodiumGameOptions, QualityMode> qualityMode = OptionImpl.<SodiumGameOptions, QualityMode>createBuilder(QualityMode.class, dynamicLightsOpts).setName(Component.nullToEmpty("Dynamic Lights Speed")).setTooltip(Component.nullToEmpty("Controls how often dynamic lights will update. \n\nLighting recalculation can be expensive, so slower values will give better performance.\n\nOff - Self explanatory\nSlow - Twice a second\nFast - Five times a second\nRealtime - Every tick")).setControl(option -> new CyclingControl(option, QualityMode.class, new Component[] { Component.nullToEmpty("Off"), Component.nullToEmpty("Slow"), Component.nullToEmpty("Fast"), Component.nullToEmpty("Realtime") })).setBinding((options, value) -> {
            DynamicLightsConfig.Quality.set(QualityMode.valueOf(value.toString()));
            LambDynLights.get().clearLightSources();
        }, options -> QualityMode.valueOf(String.valueOf(DynamicLightsConfig.Quality.get()))).setImpact(OptionImpact.MEDIUM).build();
        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.<SodiumGameOptions, Boolean>createBuilder(Boolean.class, dynamicLightsOpts).setName(Component.nullToEmpty("Dynamic Entity Lights")).setTooltip(Component.nullToEmpty("Turning this on will show dynamic lighting on entities (dropped items, mobs, etc). \n\nThis can drastically increase the amount of lighting updates, even when you're not holding a torch.")).setControl(TickBoxControl::new).setBinding((options, value) -> DynamicLightsConfig.EntityLighting.set(value), options -> DynamicLightsConfig.EntityLighting.get()).setImpact(OptionImpact.MEDIUM).build();
        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.<SodiumGameOptions, Boolean>createBuilder(Boolean.class, dynamicLightsOpts).setName(Component.nullToEmpty("Dynamic Block Lights")).setTooltip(Component.nullToEmpty("Turning this on will show dynamic lighting on tile entities (furnaces, modded machines, etc). \n\nThis can drastically increase the amount of lighting updates, even when you're not holding a torch.")).setControl(TickBoxControl::new).setBinding((options, value) -> DynamicLightsConfig.TileEntityLighting.set(value), options -> DynamicLightsConfig.TileEntityLighting.get()).setImpact(OptionImpact.MEDIUM).build();
        groups.add(OptionGroup.createBuilder().add(qualityMode).add(entityLighting).add(tileEntityLighting).build());
        this.pages.add(new OptionPage(Component.nullToEmpty("Dynamic Lights"), ImmutableList.copyOf(groups)));
    }
}