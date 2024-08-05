package fuzs.puzzleslib.mixin;

import fuzs.puzzleslib.api.core.v1.ModContainer;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class ModMixinConfigPlugin implements IMixinConfigPlugin {

    public void onLoad(String mixinPackage) {
        try {
            Class.forName("fuzs.puzzleslib.api.core.v1.ServiceProviderHelper");
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException(var3);
        }
        printModList();
    }

    private static void printModList() {
        if (!ModLoaderEnvironment.INSTANCE.getModLoader().isFabricLike()) {
            Collection<ModContainer> mods = ModLoaderEnvironment.INSTANCE.getModList().values();
            PuzzlesLib.LOGGER.info(dumpModList(mods));
        }
    }

    private static String dumpModList(Collection<ModContainer> mods) {
        StringBuilder builder = new StringBuilder();
        builder.append("Loading ");
        builder.append(mods.size());
        builder.append(" mod");
        if (mods.size() != 1) {
            builder.append("s");
        }
        builder.append(":");
        for (ModContainer mod : mods) {
            if (mod.getParent() == null) {
                printMod(builder, mod, 0, false);
            }
        }
        return builder.toString();
    }

    private static void printMod(StringBuilder builder, ModContainer mod, int depth, boolean lastChild) {
        builder.append('\n');
        builder.append("\t".repeat(depth + 1));
        builder.append(depth == 0 ? "-" : (lastChild ? "\\" : "|") + "--");
        builder.append(' ');
        builder.append(mod.getModId());
        builder.append(' ');
        builder.append(mod.getVersion());
        Iterator<ModContainer> iterator = mod.getChildren().iterator();
        while (iterator.hasNext()) {
            ModContainer childMod = (ModContainer) iterator.next();
            printMod(builder, childMod, depth + 1, !iterator.hasNext());
        }
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
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