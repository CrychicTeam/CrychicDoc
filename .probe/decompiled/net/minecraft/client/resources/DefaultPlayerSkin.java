package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.resources.ResourceLocation;

public class DefaultPlayerSkin {

    private static final DefaultPlayerSkin.SkinType[] DEFAULT_SKINS = new DefaultPlayerSkin.SkinType[] { new DefaultPlayerSkin.SkinType("textures/entity/player/slim/alex.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/ari.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/efe.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/kai.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/makena.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/noor.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/steve.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/sunny.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/slim/zuri.png", DefaultPlayerSkin.ModelType.SLIM), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/alex.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/ari.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/efe.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/kai.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/makena.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/noor.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/steve.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/sunny.png", DefaultPlayerSkin.ModelType.WIDE), new DefaultPlayerSkin.SkinType("textures/entity/player/wide/zuri.png", DefaultPlayerSkin.ModelType.WIDE) };

    public static ResourceLocation getDefaultSkin() {
        return DEFAULT_SKINS[6].texture();
    }

    public static ResourceLocation getDefaultSkin(UUID uUID0) {
        return getSkinType(uUID0).texture;
    }

    public static String getSkinModelName(UUID uUID0) {
        return getSkinType(uUID0).model.id;
    }

    private static DefaultPlayerSkin.SkinType getSkinType(UUID uUID0) {
        return DEFAULT_SKINS[Math.floorMod(uUID0.hashCode(), DEFAULT_SKINS.length)];
    }

    static enum ModelType {

        SLIM("slim"), WIDE("default");

        final String id;

        private ModelType(String p_260160_) {
            this.id = p_260160_;
        }
    }

    static record SkinType(ResourceLocation f_256814_, DefaultPlayerSkin.ModelType f_256901_) {

        private final ResourceLocation texture;

        private final DefaultPlayerSkin.ModelType model;

        public SkinType(String p_259984_, DefaultPlayerSkin.ModelType p_259456_) {
            this(new ResourceLocation(p_259984_), p_259456_);
        }

        private SkinType(ResourceLocation f_256814_, DefaultPlayerSkin.ModelType f_256901_) {
            this.texture = f_256814_;
            this.model = f_256901_;
        }
    }
}