package me.jellysquid.mods.sodium.client.model.light;

import java.util.EnumMap;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.jellysquid.mods.sodium.client.model.light.flat.FlatLightPipeline;
import me.jellysquid.mods.sodium.client.model.light.smooth.SmoothLightPipeline;
import net.minecraftforge.common.ForgeConfig;
import org.embeddedt.embeddium.render.chunk.light.ForgeLightPipeline;

public class LightPipelineProvider {

    private final EnumMap<LightMode, LightPipeline> lighters = new EnumMap(LightMode.class);

    private final LightDataAccess lightData;

    public LightPipelineProvider(LightDataAccess cache) {
        this.lightData = cache;
        if (ForgeConfig.CLIENT.experimentalForgeLightPipelineEnabled.get()) {
            this.lighters.put(LightMode.SMOOTH, ForgeLightPipeline.smooth(cache));
            this.lighters.put(LightMode.FLAT, ForgeLightPipeline.flat(cache));
        } else {
            this.lighters.put(LightMode.SMOOTH, new SmoothLightPipeline(cache));
            this.lighters.put(LightMode.FLAT, new FlatLightPipeline(cache));
        }
    }

    public LightPipeline getLighter(LightMode type) {
        LightPipeline pipeline = (LightPipeline) this.lighters.get(type);
        if (pipeline == null) {
            throw new NullPointerException("No lighter exists for mode: " + type.name());
        } else {
            return pipeline;
        }
    }

    public LightDataAccess getLightData() {
        return this.lightData;
    }

    public void reset() {
        for (LightPipeline pipeline : this.lighters.values()) {
            pipeline.reset();
        }
    }
}