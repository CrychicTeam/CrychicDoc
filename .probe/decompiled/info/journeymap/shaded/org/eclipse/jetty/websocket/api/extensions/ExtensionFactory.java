package info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class ExtensionFactory implements Iterable<Class<? extends Extension>> {

    private ServiceLoader<Extension> extensionLoader = ServiceLoader.load(Extension.class);

    private Map<String, Class<? extends Extension>> availableExtensions = new HashMap();

    public ExtensionFactory() {
        for (Extension ext : this.extensionLoader) {
            if (ext != null) {
                this.availableExtensions.put(ext.getName(), ext.getClass());
            }
        }
    }

    public Map<String, Class<? extends Extension>> getAvailableExtensions() {
        return this.availableExtensions;
    }

    public Class<? extends Extension> getExtension(String name) {
        return (Class<? extends Extension>) this.availableExtensions.get(name);
    }

    public Set<String> getExtensionNames() {
        return this.availableExtensions.keySet();
    }

    public boolean isAvailable(String name) {
        return this.availableExtensions.containsKey(name);
    }

    public Iterator<Class<? extends Extension>> iterator() {
        return this.availableExtensions.values().iterator();
    }

    public abstract Extension newInstance(ExtensionConfig var1);

    public void register(String name, Class<? extends Extension> extension) {
        this.availableExtensions.put(name, extension);
    }

    public void unregister(String name) {
        this.availableExtensions.remove(name);
    }
}