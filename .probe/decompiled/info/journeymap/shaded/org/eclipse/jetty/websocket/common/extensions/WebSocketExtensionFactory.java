package info.journeymap.shaded.org.eclipse.jetty.websocket.common.extensions;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Extension;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;

public class WebSocketExtensionFactory extends ExtensionFactory {

    private WebSocketContainerScope container;

    public WebSocketExtensionFactory(WebSocketContainerScope container) {
        this.container = container;
    }

    @Override
    public Extension newInstance(ExtensionConfig config) {
        if (config == null) {
            return null;
        } else {
            String name = config.getName();
            if (StringUtil.isBlank(name)) {
                return null;
            } else {
                Class<? extends Extension> extClass = this.getExtension(name);
                if (extClass == null) {
                    return null;
                } else {
                    try {
                        Extension ext = this.container.getObjectFactory().createInstance(extClass);
                        if (ext instanceof AbstractExtension) {
                            AbstractExtension aext = (AbstractExtension) ext;
                            aext.init(this.container);
                            aext.setConfig(config);
                        }
                        return ext;
                    } catch (IllegalAccessException | InstantiationException var6) {
                        throw new WebSocketException("Cannot instantiate extension: " + extClass, var6);
                    }
                }
            }
        }
    }
}