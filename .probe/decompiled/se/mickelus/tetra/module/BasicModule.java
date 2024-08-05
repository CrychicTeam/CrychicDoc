package se.mickelus.tetra.module;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.ModuleData;
import se.mickelus.tetra.module.data.TweakData;

@ParametersAreNonnullByDefault
public class BasicModule extends ItemModule {

    public BasicModule(ResourceLocation identifier, ModuleData data) {
        super(data.slots[0], identifier.getPath());
        this.variantData = data.variants;
        this.renderLayer = data.renderLayer;
        this.namePriority = data.namePriority;
        this.prefixPriority = data.prefixPriority;
        this.perk = data.perk;
        if (data.tweakKey != null) {
            TweakData[] tweaks = DataManager.instance.tweakData.getData(data.tweakKey);
            if (tweaks != null) {
                this.tweaks = tweaks;
            } else {
                this.tweaks = new TweakData[0];
            }
        }
    }
}