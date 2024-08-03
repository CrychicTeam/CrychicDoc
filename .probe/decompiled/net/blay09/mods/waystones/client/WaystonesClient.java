package net.blay09.mods.waystones.client;

import java.util.Locale;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.waystones.compat.Compat;
import net.minecraft.client.ClientBrandRetriever;

public class WaystonesClient {

    public static void initialize() {
        ModClientEventHandlers.initialize();
        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModTextures.initialize(BalmClient.getTextures());
        InventoryButtonGuiHandler.initialize();
        Compat.isVivecraftInstalled = ClientBrandRetriever.getClientModName().toLowerCase(Locale.ENGLISH).contains("vivecraft");
    }
}