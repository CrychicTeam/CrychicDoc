package snownee.loquat.mixin;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin implements IMixinConfigPlugin {

    private boolean hasKubeJS;

    private boolean hasCanary;

    private static boolean hasMod(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }

    public void onLoad(String mixinPackage) {
        this.hasKubeJS = hasMod("kubejs");
        this.hasCanary = hasMod("canary");
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith("snownee.loquat.mixin.kubejs.")) {
            return this.hasKubeJS;
        } else {
            return mixinClassName.startsWith("snownee.loquat.mixin.canary.") ? this.hasCanary : true;
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