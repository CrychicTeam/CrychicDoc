package se.mickelus.tetra.module;

import java.util.Arrays;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.ModuleData;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.TweakData;

@ParametersAreNonnullByDefault
public class MultiSlotModule extends ItemModule {

    protected String slotSuffix;

    protected String unlocalizedName;

    public MultiSlotModule(ResourceLocation identifier, ModuleData data) {
        super(data.slots[0], identifier.getPath());
        this.slotSuffix = data.slotSuffixes[0];
        this.unlocalizedName = identifier.getPath().substring(0, identifier.getPath().length() - data.slotSuffixes[0].length());
        this.renderLayer = data.renderLayer;
        this.namePriority = data.namePriority;
        this.prefixPriority = data.prefixPriority;
        this.variantData = data.variants;
        if (data.tweakKey != null) {
            TweakData[] tweaks = DataManager.instance.tweakData.getData(data.tweakKey);
            if (tweaks != null) {
                this.tweaks = tweaks;
            } else {
                this.tweaks = new TweakData[0];
            }
        }
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    @Override
    public ModuleModel[] getModels(ItemStack itemStack) {
        return (ModuleModel[]) Arrays.stream(super.getModels(itemStack)).map(ModuleModel::copy).peek(model -> model.location = new ResourceLocation(model.location.getNamespace(), model.location.getPath() + this.slotSuffix)).toArray(ModuleModel[]::new);
    }
}