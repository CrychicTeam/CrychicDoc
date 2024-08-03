package mezz.jei.forge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mezz.jei.api.IModPlugin;
import mezz.jei.common.Internal;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.network.ClientPacketRouter;
import mezz.jei.forge.events.PermanentEventSubscriptions;
import mezz.jei.forge.network.ConnectionToServer;
import mezz.jei.forge.network.NetworkHandler;
import mezz.jei.forge.startup.ForgePluginFinder;
import mezz.jei.forge.startup.StartEventObserver;
import mezz.jei.gui.config.InternalKeyMappings;
import mezz.jei.library.startup.JeiStarter;
import mezz.jei.library.startup.StartData;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class JustEnoughItemsClient {

    private final PermanentEventSubscriptions subscriptions;

    private final StartEventObserver startEventObserver;

    public JustEnoughItemsClient(NetworkHandler networkHandler, PermanentEventSubscriptions subscriptions, IServerConfig serverConfig) {
        this.subscriptions = subscriptions;
        ConnectionToServer serverConnection = new ConnectionToServer(networkHandler);
        Internal.setServerConnection(serverConnection);
        InternalKeyMappings keyMappings = createKeyMappings(subscriptions);
        Internal.setKeyMappings(keyMappings);
        ClientPacketRouter packetRouter = new ClientPacketRouter(serverConnection, serverConfig);
        networkHandler.registerClientPacketHandler(packetRouter);
        List<IModPlugin> plugins = ForgePluginFinder.getModPlugins();
        StartData startData = new StartData(plugins, serverConnection, keyMappings);
        JeiStarter jeiStarter = new JeiStarter(startData);
        this.startEventObserver = new StartEventObserver(jeiStarter::start, jeiStarter::stop);
        this.startEventObserver.register(subscriptions);
    }

    public void register() {
        this.subscriptions.register(RegisterClientReloadListenersEvent.class, this::onRegisterReloadListenerEvent);
    }

    private void onRegisterReloadListenerEvent(RegisterClientReloadListenersEvent event) {
        Textures textures = Internal.getTextures();
        event.registerReloadListener(textures.getSpriteUploader());
        event.registerReloadListener(this.startEventObserver);
    }

    private static InternalKeyMappings createKeyMappings(PermanentEventSubscriptions subscriptions) {
        Set<KeyMapping> keysToRegister = new HashSet();
        subscriptions.register(RegisterKeyMappingsEvent.class, e -> keysToRegister.forEach(e::register));
        return new InternalKeyMappings(keysToRegister::add);
    }
}