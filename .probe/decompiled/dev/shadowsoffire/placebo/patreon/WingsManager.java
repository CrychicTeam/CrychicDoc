package dev.shadowsoffire.placebo.patreon;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import dev.shadowsoffire.placebo.patreon.wings.Wing;
import dev.shadowsoffire.placebo.patreon.wings.WingLayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class WingsManager {

    static Map<UUID, PatreonUtils.WingType> WINGS = new HashMap();

    public static final KeyMapping TOGGLE = new KeyMapping("placebo.toggleWings", 328, "key.categories.placebo");

    public static final Set<UUID> DISABLED = new HashSet();

    public static final ModelLayerLocation WING_LOC = new ModelLayerLocation(new ResourceLocation("placebo", "wings"), "main");

    public static void init(FMLClientSetupEvent e) {
        e.enqueueWork(() -> ForgeHooksClient.registerLayerDefinition(WING_LOC, Wing::createLayer));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WingsManager::addLayers);
        new Thread(() -> {
            Placebo.LOGGER.info("Loading patreon wing data...");
            try {
                URL url = new URL("https://raw.githubusercontent.com/Shadows-of-Fire/Placebo/1.16/PatreonWings.txt");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    try {
                        String s;
                        while ((s = reader.readLine()) != null) {
                            String[] split = s.split(" ", 2);
                            if (split.length != 2) {
                                Placebo.LOGGER.error("Invalid patreon wing entry {} will be ignored.", s);
                            } else {
                                WINGS.put(UUID.fromString(split[0]), PatreonUtils.WingType.valueOf(split[1]));
                            }
                        }
                        reader.close();
                    } catch (Throwable var5) {
                        try {
                            reader.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                        throw var5;
                    }
                    reader.close();
                } catch (IOException var6) {
                    Placebo.LOGGER.error("Exception loading patreon wing data!");
                    var6.printStackTrace();
                }
            } catch (Exception var7) {
            }
            Placebo.LOGGER.info("Loaded {} patreon wings.", WINGS.size());
            if (WINGS.size() > 0) {
                MinecraftForge.EVENT_BUS.register(WingsManager.class);
            }
        }, "Placebo Patreon Wing Loader").start();
    }

    @SubscribeEvent
    public static void keys(InputEvent.Key e) {
        if (e.getAction() == 1 && TOGGLE.matches(e.getKey(), e.getScanCode()) && Minecraft.getInstance().getConnection() != null) {
            Placebo.CHANNEL.sendToServer(new PatreonDisableMessage(1));
        }
    }

    public static void addLayers(EntityRenderersEvent.AddLayers e) {
        Wing.INSTANCE = new Wing(e.getEntityModels().bakeLayer(WING_LOC));
        for (String s : e.getSkins()) {
            LivingEntityRenderer skin = e.getSkin(s);
            skin.addLayer(new WingLayer(skin));
        }
    }

    public static PatreonUtils.WingType getType(UUID id) {
        return (PatreonUtils.WingType) WINGS.get(id);
    }
}