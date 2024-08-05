package portb.biggerstacks;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService.Phase;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import portb.biggerstacks.config.StackSizeRules;
import portb.slw.MyLoggerFactory;
import portb.transformerlib.TransformerLib;

public class TransformerEngine implements IMixinConfigPlugin {

    private static final Map<String, List<String>> MODS_THAT_CONFLICT_WITH_PATCHES = Map.of("portb.biggerstacks.mixin.vanilla.AnvilMenuMixin", List.of("tiered"));

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerEngine.class);

    private static final Pattern MOD_ID_PACKAGE_TARGET_PATTERN = Pattern.compile("mixin\\.compat\\.([^.]+)\\.[^.]+$");

    private static <T> T getPrivateField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static boolean isAnyModInstalled(List<String> listOfMods) {
        for (String modId : listOfMods) {
            if (FMLLoader.getLoadingModList().getModFileById(modId) != null) {
                return true;
            }
        }
        return false;
    }

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Matcher matcher = MOD_ID_PACKAGE_TARGET_PATTERN.matcher(mixinClassName);
        if (matcher.find()) {
            String modId = matcher.group(1);
            boolean isModLoaded = FMLLoader.getLoadingModList().getModFileById(modId) != null;
            if (isModLoaded) {
                LOGGER.info(modId + " is installed, applying patches");
            } else {
                LOGGER.debug(modId + " is NOT installed");
            }
            return isModLoaded;
        } else {
            return !MODS_THAT_CONFLICT_WITH_PATCHES.containsKey(mixinClassName) ? true : !isAnyModInstalled((List<String>) MODS_THAT_CONFLICT_WITH_PATCHES.get(mixinClassName));
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

    static {
        final EnumSet<Phase> NONE = EnumSet.noneOf(Phase.class);
        final EnumSet<Phase> BEFORE = EnumSet.of(Phase.BEFORE);
        LaunchPluginHandler launchPlugins = getPrivateField(Launcher.INSTANCE, "launchPlugins");
        Map<String, ILaunchPluginService> plugins = getPrivateField(launchPlugins, "plugins");
        TransformerLib.LOGGER = MyLoggerFactory.createMyLogger(LoggerFactory.getLogger(TransformerLib.class));
        TransformerLib.setGlobalStackLimitSupplier(StackSizeRules::getMaxStackSize);
        TransformerLib.loadTransformers(TransformerEngine.class);
        plugins.put("biggerstacks_transformer", new ILaunchPluginService() {

            public String name() {
                return "biggerstacks_transformer";
            }

            public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
                return !isEmpty && !classType.getClassName().contains("/mixin/") ? BEFORE : NONE;
            }

            public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason) {
                if (phase == Phase.AFTER) {
                    return 0;
                } else {
                    return TransformerLib.handleTransformation(classNode) ? 1 : 0;
                }
            }
        });
    }
}