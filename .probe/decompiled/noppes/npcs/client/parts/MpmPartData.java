package noppes.npcs.client.parts;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.shared.client.util.ImageDownloadAlt;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.shared.client.util.ResourceDownloader;
import noppes.npcs.shared.common.util.NopVector3f;

public class MpmPartData {

    public static final NopVector3f WHITE = new NopVector3f(1.0F, 1.0F, 1.0F);

    public ResourceLocation partId;

    public boolean usePlayerSkin = false;

    public NopVector3f color = WHITE;

    public ResourceLocation texture = null;

    private ResourceLocation textureUrl = null;

    public String url = "";

    public MpmPart getPart() {
        return (MpmPart) MpmPartReader.PARTS.get(this.partId);
    }

    public ResourceLocation getTexture() {
        if (this.getUrlTexture() != null) {
            return this.getUrlTexture();
        } else if (this.texture != null) {
            return this.texture;
        } else {
            MpmPart part = this.getPart();
            return part != null && part.texture != null ? this.getPart().texture : MissingTextureAtlasSprite.getLocation();
        }
    }

    public ResourceLocation getUrlTexture() {
        if (this.textureUrl != null) {
            return this.textureUrl;
        } else {
            if (!this.url.isEmpty()) {
                ResourceLocation resource = ResourceDownloader.getUrlResourceLocation(this.url, false);
                File file = ResourceDownloader.getUrlFile(this.url, false);
                TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
                AbstractTexture object = texturemanager.getTexture(resource, null);
                if (object == null) {
                    this.textureUrl = this.getDefaultTexture();
                    ResourceDownloader.load(new ImageDownloadAlt(file, this.url, resource, this.getDefaultTexture(), false, () -> this.textureUrl = resource));
                } else {
                    this.textureUrl = resource;
                }
            }
            return this.textureUrl;
        }
    }

    public void setTexture(String s) {
        if (s != null && !s.isEmpty()) {
            this.texture = new ResourceLocation(s);
        } else {
            this.texture = null;
        }
    }

    public void setUrl(String url) {
        if (!NoppesStringUtils.areEqual(this.url, url)) {
            this.url = url;
            this.textureUrl = null;
        }
    }

    public ResourceLocation getDefaultTexture() {
        return this.texture != null ? this.texture : this.getPart().texture;
    }

    public int getColor() {
        int r = (int) (this.color.x * 255.0F) << 16;
        int g = (int) (this.color.y * 255.0F) << 8;
        int b = (int) (this.color.z * 255.0F);
        return r + g + b;
    }

    public void setColor(int color) {
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        this.color = new NopVector3f(r, g, b);
    }

    public CompoundTag getNbt() {
        CompoundTag item = new CompoundTag();
        item.putString("Id", this.partId.toString());
        item.putBoolean("UsePlayerSkin", this.usePlayerSkin);
        item.putString("Url", this.url);
        item.putString("Texture", this.texture == null ? "" : this.texture.toString());
        item.putFloat("ColorR", this.color.x);
        item.putFloat("ColorG", this.color.y);
        item.putFloat("ColorB", this.color.z);
        return item;
    }

    public void setNbt(CompoundTag compound) {
        this.partId = new ResourceLocation(compound.getString("Id"));
        this.usePlayerSkin = compound.getBoolean("UsePlayerSkin");
        this.setUrl(compound.getString("Url"));
        this.setTexture(compound.getString("Texture"));
        this.color = new NopVector3f(compound.getFloat("ColorR"), compound.getFloat("ColorG"), compound.getFloat("ColorB"));
    }
}