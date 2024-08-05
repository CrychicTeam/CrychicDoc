package journeymap.client.ui.minimap;

import com.google.common.base.Strings;
import java.util.UUID;
import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import net.minecraft.resources.ResourceLocation;

public enum EntityDisplay implements KeyedEnum {

    LargeDots("jm.common.entity_display.dots", TextureCache.MobDot, TextureCache.MobDotArrow), LargeIcons("jm.common.entity_display.icons", null, TextureCache.MobIconArrow);

    public final String key;

    private final ResourceLocation defaultTexture;

    private final ResourceLocation showHeadingTexture;

    private EntityDisplay(String key, ResourceLocation defaultTexture, ResourceLocation showHeadingTexture) {
        this.key = key;
        this.defaultTexture = defaultTexture;
        this.showHeadingTexture = showHeadingTexture;
    }

    public static Texture getLocatorTexture(EntityDisplay entityDisplay, boolean showHeading) {
        return TextureCache.getTexture(showHeading ? entityDisplay.showHeadingTexture : entityDisplay.defaultTexture);
    }

    public static Texture getEntityTexture(EntityDisplay entityDisplay) {
        return getEntityTexture(entityDisplay, (UUID) null, (String) null);
    }

    public static Texture getEntityTexture(EntityDisplay entityDisplay, UUID entityId, String playerName) {
        switch(entityDisplay) {
            case LargeDots:
                return TextureCache.getTexture(TextureCache.MobDotChevron);
            default:
                return !Strings.isNullOrEmpty(playerName) ? TextureCache.getPlayerSkin(entityId, playerName) : null;
        }
    }

    public static Texture getEntityTexture(EntityDisplay entityDisplay, ResourceLocation iconLocation) {
        return entityDisplay == LargeDots ? TextureCache.getTexture(TextureCache.MobDotChevron) : TextureCache.getTexture(iconLocation);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public String toString() {
        return Constants.getString(this.key);
    }

    public boolean isDots() {
        return this == LargeDots;
    }

    public boolean isLarge() {
        return this == LargeDots || this == LargeIcons;
    }

    public EntityDisplay getDot() {
        return this == LargeIcons ? LargeDots : this;
    }
}