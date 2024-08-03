package icyllis.modernui.mc.testforge.trash;

import icyllis.modernui.ModernUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.objectweb.asm.Type;

@Deprecated
public class PluginList {

    private static PluginList sInstance;

    private final List<Plugin> mPlugins = new ArrayList();

    private PluginList() {
    }

    private void scanPlugins() {
        Map<String, Plugin> plugins = new HashMap();
        Type target = Type.getType(DefinePlugin.class);
        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (AnnotationData data : scanData.getAnnotations()) {
                if (data.annotationType().equals(target)) {
                    try {
                        String pid = (String) data.annotationData().get("value");
                        Plugin v = (Plugin) plugins.putIfAbsent(pid, (Plugin) Class.forName(data.memberName()).asSubclass(Plugin.class).getDeclaredConstructor().newInstance());
                        if (v != null) {
                            ModernUI.LOGGER.error(ModernUI.MARKER, "{} is annotated with the same plugin id {} as {}", data.memberName(), pid, v);
                        }
                    } catch (Throwable var9) {
                        ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to load plugin: {}", data.memberName(), var9);
                    }
                }
            }
        }
        this.mPlugins.addAll(plugins.values());
    }

    @Nonnull
    public static PluginList get() {
        if (sInstance == null) {
            synchronized (PluginList.class) {
                if (sInstance == null) {
                    sInstance = new PluginList();
                }
            }
        }
        return sInstance;
    }

    public int size() {
        return this.mPlugins.size();
    }
}