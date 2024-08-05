package snownee.kiwi.mixin;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import snownee.kiwi.customization.CustomizationServiceFinder;

public class MixinPlugin implements IMixinConfigPlugin {

    private boolean customization;

    private boolean persistentCreativeInventory;

    private boolean fastScrolling;

    private boolean ksit;

    public void onLoad(String mixinPackage) {
        this.customization = CustomizationServiceFinder.shouldEnable(LoadingModList.get().getMods());
        this.persistentCreativeInventory = this.customization || LoadingModList.get().getModFileById("persistentcreativeinventory") != null || !FMLEnvironment.production;
        this.fastScrolling = LoadingModList.get().getModFileById("fastscroll") != null || !FMLEnvironment.production;
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith("snownee.kiwi.mixin.customization.")) {
            return this.customization;
        } else if (mixinClassName.equals("snownee.kiwi.mixin.client.CreativeModeInventoryScreenMixin")) {
            return this.persistentCreativeInventory;
        } else {
            return mixinClassName.equals("snownee.kiwi.mixin.client.OptionInstanceMixin") ? this.fastScrolling : true;
        }
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}