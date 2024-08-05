package dev.latvian.mods.kubejs.client.painter.screen;

import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.unit.FixedColorUnit;
import dev.latvian.mods.unit.Unit;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class AtlasTextureObject extends BoxObject {

    public Unit color = FixedColorUnit.WHITE;

    public ResourceLocation atlas = InventoryMenu.BLOCK_ATLAS;

    public ResourceLocation texture = null;

    public TextureAtlas textureAtlas;

    public AtlasTextureObject(Painter painter) {
        super(painter);
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        this.color = properties.getColor("color", this.color);
        this.atlas = properties.getResourceLocation("atlas", this.atlas);
        this.texture = properties.getResourceLocation("texture", this.texture);
        this.textureAtlas = null;
    }

    @Override
    public void draw(PaintScreenEventJS event) {
        if (this.texture != null) {
            if (this.textureAtlas == null) {
                this.textureAtlas = event.mc.getModelManager().getAtlas(this.atlas);
            }
            if (this.textureAtlas != null) {
                float aw = this.w.getFloat(event);
                float ah = this.h.getFloat(event);
                float ax = event.alignX(this.x.getFloat(event), aw, this.alignX);
                float ay = event.alignY(this.y.getFloat(event), ah, this.alignY);
                float az = this.z.getFloat(event);
                TextureAtlasSprite sprite = this.textureAtlas.getSprite(this.texture);
                float u0 = sprite.getU0();
                float v0 = sprite.getV0();
                float u1 = sprite.getU1();
                float v1 = sprite.getV1();
                event.resetShaderColor();
                event.setPositionColorTextureShader();
                event.setShaderTexture(this.atlas);
                event.blend(true);
                event.beginQuads(true);
                event.rectangle(ax, ay, az, aw, ah, this.color.getInt(event), u0, v0, u1, v1);
                event.end();
            }
        }
    }
}