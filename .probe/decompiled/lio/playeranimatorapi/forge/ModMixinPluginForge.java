package lio.playeranimatorapi.forge;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import lio.liosmultiloaderutils.utils.Platform;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class ModMixinPluginForge implements IMixinConfigPlugin {

    private static final Supplier<Boolean> TRUE = () -> true;

    private static boolean madeInterface = false;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of("lio.playeranimatorapi.forge.mixin.EmoteCraftClientInitMixinForge", (Supplier) () -> Platform.isModLoaded("emotecraft", "io.github.kosmx.emotes.forge.ForgeWrapper"));

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!madeInterface) {
            madeInterface = true;
            this.checkAndDefineInterface();
        }
        return (Boolean) ((Supplier) CONDITIONS.getOrDefault(mixinClassName, TRUE)).get();
    }

    private void checkAndDefineInterface() {
        if (!isModLoaded("liolib")) {
            defineInterface();
        }
    }

    private static boolean isModLoaded(String modId) {
        return Platform.isModLoaded("liolib", "net.liopyu.liolib.LioLib");
    }

    private static void defineInterface() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(52, 513, "lio/playeranimatorapi/GeoPlayer", null, "java/lang/Object", null);
        cw.visitEnd();
    }

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
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