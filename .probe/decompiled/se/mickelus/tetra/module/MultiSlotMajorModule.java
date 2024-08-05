package se.mickelus.tetra.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.Filter;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.ModuleData;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.TweakData;

@ParametersAreNonnullByDefault
public class MultiSlotMajorModule extends ItemModuleMajor {

    protected String slotSuffix;

    protected String unlocalizedName;

    public MultiSlotMajorModule(ResourceLocation identifier, ModuleData data) {
        super(data.slots[0], identifier.getPath());
        this.slotSuffix = data.slotSuffixes[0];
        this.unlocalizedName = identifier.getPath().substring(0, identifier.getPath().length() - data.slotSuffixes[0].length());
        this.renderLayer = data.renderLayer;
        this.namePriority = data.namePriority;
        this.prefixPriority = data.prefixPriority;
        this.variantData = data.variants;
        if (data.improvements.length > 0) {
            this.improvements = (ImprovementData[]) Arrays.stream(data.improvements).map(rl -> rl.getPath().endsWith("/") ? DataManager.instance.improvementData.getDataIn(rl) : (Collection) Optional.ofNullable(DataManager.instance.improvementData.getData(rl)).map(Collections::singletonList).orElseGet(Collections::emptyList)).flatMap(Collection::stream).filter(Objects::nonNull).flatMap(Arrays::stream).filter(Filter.distinct(improvement -> improvement.key + ":" + improvement.level)).toArray(ImprovementData[]::new);
            this.settleMax = Arrays.stream(this.improvements).filter(improvement -> improvement.key.equals("settled")).mapToInt(ImprovementData::getLevel).max().orElse(0);
        }
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
    protected ModuleModel[] getImprovementModels(ItemStack itemStack, int tint) {
        return super.getImprovementModels(itemStack, tint);
    }

    @Override
    public ModuleModel[] getModels(ItemStack itemStack) {
        return (ModuleModel[]) Arrays.stream(super.getModels(itemStack)).map(ModuleModel::copy).peek(model -> model.location = new ResourceLocation(model.location.getNamespace(), model.location.getPath() + this.slotSuffix)).toArray(ModuleModel[]::new);
    }
}