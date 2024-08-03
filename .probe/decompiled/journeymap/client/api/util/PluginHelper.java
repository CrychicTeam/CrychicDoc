package journeymap.client.api.util;

import com.google.common.base.Strings;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.ClientPlugin;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

@ParametersAreNonnullByDefault
public enum PluginHelper {

    INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger("journeymap");

    public static final Type PLUGIN_ANNOTATION_NAME = Type.getType(ClientPlugin.class);

    public static final String PLUGIN_INTERFACE_NAME = IClientPlugin.class.getSimpleName();

    protected Map<String, IClientPlugin> plugins = null;

    protected boolean initialized;

    public Map<String, IClientPlugin> preInitPlugins(List<String> pluginList) {
        if (this.plugins == null) {
            HashMap<String, IClientPlugin> discovered = new HashMap();
            for (String className : pluginList) {
                try {
                    Class<?> pluginClass = Class.forName(className);
                    if (IClientPlugin.class.isAssignableFrom(pluginClass)) {
                        Class<? extends IClientPlugin> interfaceImplClass = pluginClass.asSubclass(IClientPlugin.class);
                        IClientPlugin instance = (IClientPlugin) interfaceImplClass.getDeclaredConstructor().newInstance();
                        String modId = instance.getModId();
                        if (Strings.isNullOrEmpty(modId)) {
                            throw new Exception("IClientPlugin.getModId() must return a non-empty, non-null value");
                        }
                        if (discovered.containsKey(modId)) {
                            Class otherPluginClass = ((IClientPlugin) discovered.get(modId)).getClass();
                            throw new Exception(String.format("Multiple plugins trying to use the same modId: %s and %s", interfaceImplClass, otherPluginClass));
                        }
                        discovered.put(modId, instance);
                        LOGGER.info(String.format("Found @%s: %s", PLUGIN_ANNOTATION_NAME, className));
                    } else {
                        LOGGER.error(String.format("Found @%s: %s, but it doesn't implement %s", PLUGIN_ANNOTATION_NAME, className, PLUGIN_INTERFACE_NAME));
                    }
                } catch (Exception var10) {
                    LOGGER.error(String.format("Found @%s: %s, but failed to instantiate it: %s", PLUGIN_ANNOTATION_NAME, className, var10.getMessage()), var10);
                }
            }
            if (discovered.isEmpty()) {
                LOGGER.info("No plugins for JourneyMap API discovered.");
            }
            this.plugins = Collections.unmodifiableMap(discovered);
        }
        return this.plugins;
    }

    public Map<String, IClientPlugin> initPlugins(IClientAPI clientAPI) {
        if (this.plugins == null) {
            LOGGER.warn("Plugin discovery never occurred.", new IllegalStateException());
        } else if (!this.initialized) {
            LOGGER.info(String.format("Initializing plugins with Client API: %s", clientAPI.getClass().getName()));
            HashMap<String, IClientPlugin> discovered = new HashMap(this.plugins);
            Iterator<IClientPlugin> iter = discovered.values().iterator();
            while (iter.hasNext()) {
                IClientPlugin plugin = (IClientPlugin) iter.next();
                try {
                    plugin.initialize(clientAPI);
                    LOGGER.info(String.format("Initialized %s: %s", PLUGIN_INTERFACE_NAME, plugin.getClass().getName()));
                } catch (Exception var6) {
                    LOGGER.error("Failed to initialize IClientPlugin: " + plugin.getClass().getName(), var6);
                    iter.remove();
                }
            }
            this.plugins = Collections.unmodifiableMap(discovered);
            this.initialized = true;
        } else {
            LOGGER.warn("Plugins already initialized!", new IllegalStateException());
        }
        return this.plugins;
    }

    public Map<String, IClientPlugin> getPlugins() {
        return this.plugins;
    }
}