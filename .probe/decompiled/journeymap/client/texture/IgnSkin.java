package journeymap.client.texture;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.platform.NativeImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import journeymap.common.Journeymap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

public class IgnSkin {

    private static String ID_LOOKUP_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%s";

    private static String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    private static Map<UUID, NativeImage> faceImageCache = Maps.newHashMap();

    private static final Map<URL, CompletableFuture<NativeImage>> imageRequests = Maps.newConcurrentMap();

    public static NativeImage getFaceImage(UUID playerId, String username) {
        boolean firstPass = !faceImageCache.containsKey(playerId);
        if (firstPass) {
            faceImageCache.put(playerId, null);
            SkullBlockEntity.updateGameprofile(new GameProfile(playerId, username), gameProfile -> {
                try {
                    MinecraftSessionService mss = Minecraft.getInstance().getMinecraftSessionService();
                    Map<Type, MinecraftProfileTexture> map = mss.getTextures(gameProfile, false);
                    if (map.containsKey(Type.SKIN)) {
                        Journeymap.getLogger().debug("Retrieved skin for {} : {}", playerId, username);
                        MinecraftProfileTexture mpt = (MinecraftProfileTexture) map.get(Type.SKIN);
                        getImageFromUrl(new URL(mpt.getUrl()), image -> faceImageCache.put(playerId, cropToFace(image)));
                    } else {
                        Journeymap.getLogger().debug("Unable to retrieve skin for {} : {}", playerId, username);
                        ResourceLocation resourceLocation = DefaultPlayerSkin.getDefaultSkin(playerId);
                        NativeImage skinImage = TextureCache.getTexture(resourceLocation).getNativeImage();
                        faceImageCache.put(playerId, cropToFace(skinImage));
                    }
                } catch (Throwable var7) {
                    Journeymap.getLogger().warn("Error getting face image for " + username + ": " + var7.getMessage());
                }
            });
        }
        return (NativeImage) faceImageCache.get(playerId);
    }

    private static void getImageFromUrl(URL imageURL, Consumer<NativeImage> imageConsumer) {
        CompletableFuture<NativeImage> future = (CompletableFuture<NativeImage>) imageRequests.get(imageURL);
        if (future != null) {
            imageRequests.put(imageURL, future.whenCompleteAsync((image, throwable) -> imageConsumer.accept(image), Util.backgroundExecutor()));
        } else {
            imageRequests.put(imageURL, CompletableFuture.supplyAsync(() -> downloadImage(imageURL), Util.backgroundExecutor()).whenCompleteAsync((image, throwable) -> imageConsumer.accept(image), Util.backgroundExecutor()));
        }
    }

    private static NativeImage downloadImage(URL imageURL) {
        NativeImage img = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) imageURL.openConnection(Minecraft.getInstance().getProxy());
            HttpURLConnection.setFollowRedirects(true);
            conn.setInstanceFollowRedirects(true);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.connect();
            if (conn.getResponseCode() / 100 == 2) {
                img = NativeImage.read(conn.getInputStream());
            } else {
                Journeymap.getLogger().debug("Bad Response getting image: " + imageURL + " : " + conn.getResponseCode());
            }
        } catch (Throwable var7) {
            Journeymap.getLogger().error("Error getting skin image: " + imageURL + " : " + var7.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Journeymap.getLogger().debug("Getting Skin for URL: {}", imageURL);
        return img;
    }

    private static NativeImage cropToFace(NativeImage playerSkin) {
        if (playerSkin == null) {
            return new NativeImage(24, 24, false);
        } else {
            if (playerSkin.format().hasAlpha()) {
                NativeImage hat = ImageUtil.getSubImage(40, 8, 8, 8, playerSkin, false);
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        int hatPixel = hat.getPixelRGBA(x, y);
                        playerSkin.blendPixel(x + 8, y + 8, hatPixel);
                    }
                }
                hat.close();
            }
            NativeImage sub = ImageUtil.getSubImage(8, 8, 8, 8, playerSkin, true);
            return ImageUtil.getSizedImage(24, 24, sub, true);
        }
    }
}