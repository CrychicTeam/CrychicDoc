package noppes.npcs.shared.client.util;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.mixin.SkinManagerMixin;
import noppes.npcs.shared.SharedReferences;

public class ResourceDownloader {

    private static final Set<ResourceLocation> active = Collections.synchronizedSet(new HashSet());

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void load(ImageDownloadAlt resource) {
        if (!active.contains(resource.location)) {
            active.add(resource.location);
            executor.execute(() -> {
                resource.loadTextureFromServer();
                Minecraft.getInstance().m_18707_(() -> {
                    Minecraft.getInstance().getTextureManager().register(resource.location, resource);
                    active.remove(resource.location);
                });
                try {
                    Thread.sleep(400L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            });
        }
    }

    public static ResourceLocation getUrlResourceLocation(String url, boolean fixSkin) {
        return new ResourceLocation(SharedReferences.modid(), "skins/" + (url + fixSkin).hashCode() + (fixSkin ? "" : "32"));
    }

    public static File getUrlFile(String url, boolean fixSkin) {
        return new File(((SkinManagerMixin) Minecraft.getInstance().getSkinManager()).getDir(), (url + fixSkin).hashCode() + "");
    }

    public static boolean contains(ResourceLocation location) {
        return active.contains(location);
    }
}