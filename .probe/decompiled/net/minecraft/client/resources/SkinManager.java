package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecurePublicKeyException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

public class SkinManager {

    public static final String PROPERTY_TEXTURES = "textures";

    private final TextureManager textureManager;

    private final File skinsDirectory;

    private final MinecraftSessionService sessionService;

    private final LoadingCache<String, Map<Type, MinecraftProfileTexture>> insecureSkinCache;

    public SkinManager(TextureManager textureManager0, File file1, final MinecraftSessionService minecraftSessionService2) {
        this.textureManager = textureManager0;
        this.skinsDirectory = file1;
        this.sessionService = minecraftSessionService2;
        this.insecureSkinCache = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader<String, Map<Type, MinecraftProfileTexture>>() {

            public Map<Type, MinecraftProfileTexture> load(String p_118853_) {
                GameProfile $$1 = new GameProfile(null, "dummy_mcdummyface");
                $$1.getProperties().put("textures", new Property("textures", p_118853_, ""));
                try {
                    return minecraftSessionService2.getTextures($$1, false);
                } catch (Throwable var4) {
                    return ImmutableMap.of();
                }
            }
        });
    }

    public ResourceLocation registerTexture(MinecraftProfileTexture minecraftProfileTexture0, Type type1) {
        return this.registerTexture(minecraftProfileTexture0, type1, null);
    }

    private ResourceLocation registerTexture(MinecraftProfileTexture minecraftProfileTexture0, Type type1, @Nullable SkinManager.SkinTextureCallback skinManagerSkinTextureCallback2) {
        String $$3 = Hashing.sha1().hashUnencodedChars(minecraftProfileTexture0.getHash()).toString();
        ResourceLocation $$4 = getTextureLocation(type1, $$3);
        AbstractTexture $$5 = this.textureManager.getTexture($$4, MissingTextureAtlasSprite.getTexture());
        if ($$5 == MissingTextureAtlasSprite.getTexture()) {
            File $$6 = new File(this.skinsDirectory, $$3.length() > 2 ? $$3.substring(0, 2) : "xx");
            File $$7 = new File($$6, $$3);
            HttpTexture $$8 = new HttpTexture($$7, minecraftProfileTexture0.getUrl(), DefaultPlayerSkin.getDefaultSkin(), type1 == Type.SKIN, () -> {
                if (skinManagerSkinTextureCallback2 != null) {
                    skinManagerSkinTextureCallback2.onSkinTextureAvailable(type1, $$4, minecraftProfileTexture0);
                }
            });
            this.textureManager.register($$4, $$8);
        } else if (skinManagerSkinTextureCallback2 != null) {
            skinManagerSkinTextureCallback2.onSkinTextureAvailable(type1, $$4, minecraftProfileTexture0);
        }
        return $$4;
    }

    private static ResourceLocation getTextureLocation(Type type0, String string1) {
        String $$2 = switch(type0) {
            case SKIN ->
                "skins";
            case CAPE ->
                "capes";
            case ELYTRA ->
                "elytra";
            default ->
                throw new IncompatibleClassChangeError();
        };
        return new ResourceLocation($$2 + "/" + string1);
    }

    public void registerSkins(GameProfile gameProfile0, SkinManager.SkinTextureCallback skinManagerSkinTextureCallback1, boolean boolean2) {
        Runnable $$3 = () -> {
            Map<Type, MinecraftProfileTexture> $$3x = Maps.newHashMap();
            try {
                $$3x.putAll(this.sessionService.getTextures(gameProfile0, boolean2));
            } catch (InsecurePublicKeyException var7) {
            }
            if ($$3x.isEmpty()) {
                gameProfile0.getProperties().clear();
                if (gameProfile0.getId().equals(Minecraft.getInstance().getUser().getGameProfile().getId())) {
                    gameProfile0.getProperties().putAll(Minecraft.getInstance().getProfileProperties());
                    $$3x.putAll(this.sessionService.getTextures(gameProfile0, false));
                } else {
                    this.sessionService.fillProfileProperties(gameProfile0, boolean2);
                    try {
                        $$3x.putAll(this.sessionService.getTextures(gameProfile0, boolean2));
                    } catch (InsecurePublicKeyException var6) {
                    }
                }
            }
            Minecraft.getInstance().execute(() -> RenderSystem.recordRenderCall(() -> ImmutableList.of(Type.SKIN, Type.CAPE).forEach(p_174848_ -> {
                if ($$3x.containsKey(p_174848_)) {
                    this.registerTexture((MinecraftProfileTexture) $$3x.get(p_174848_), p_174848_, skinManagerSkinTextureCallback1);
                }
            })));
        };
        Util.backgroundExecutor().execute($$3);
    }

    public Map<Type, MinecraftProfileTexture> getInsecureSkinInformation(GameProfile gameProfile0) {
        Property $$1 = (Property) Iterables.getFirst(gameProfile0.getProperties().get("textures"), null);
        return (Map<Type, MinecraftProfileTexture>) ($$1 == null ? ImmutableMap.of() : (Map) this.insecureSkinCache.getUnchecked($$1.getValue()));
    }

    public ResourceLocation getInsecureSkinLocation(GameProfile gameProfile0) {
        MinecraftProfileTexture $$1 = (MinecraftProfileTexture) this.getInsecureSkinInformation(gameProfile0).get(Type.SKIN);
        return $$1 != null ? this.registerTexture($$1, Type.SKIN) : DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(gameProfile0));
    }

    public interface SkinTextureCallback {

        void onSkinTextureAvailable(Type var1, ResourceLocation var2, MinecraftProfileTexture var3);
    }
}