package mezz.jei.library.load;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.IModPlugin;
import mezz.jei.library.plugins.jei.JeiInternalPlugin;
import mezz.jei.library.plugins.vanilla.VanillaPlugin;
import org.jetbrains.annotations.Nullable;

public class PluginHelper {

    public static void sortPlugins(List<IModPlugin> plugins, VanillaPlugin vanillaPlugin, @Nullable JeiInternalPlugin jeiInternalPlugin) {
        plugins.remove(vanillaPlugin);
        plugins.add(0, vanillaPlugin);
        if (jeiInternalPlugin != null) {
            plugins.remove(jeiInternalPlugin);
            plugins.add(jeiInternalPlugin);
        }
    }

    public static <T extends IModPlugin> Optional<T> getPluginWithClass(Class<? extends T> pluginClass, List<IModPlugin> modPlugins) {
        for (IModPlugin modPlugin : modPlugins) {
            if (pluginClass.isInstance(modPlugin)) {
                T cast = (T) pluginClass.cast(modPlugin);
                return Optional.of(cast);
            }
        }
        return Optional.empty();
    }
}