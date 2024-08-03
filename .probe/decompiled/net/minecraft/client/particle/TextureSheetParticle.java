package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class TextureSheetParticle extends SingleQuadParticle {

    protected TextureAtlasSprite sprite;

    protected TextureSheetParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3);
    }

    protected TextureSheetParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
    }

    protected void setSprite(TextureAtlasSprite textureAtlasSprite0) {
        this.sprite = textureAtlasSprite0;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU0();
    }

    @Override
    protected float getU1() {
        return this.sprite.getU1();
    }

    @Override
    protected float getV0() {
        return this.sprite.getV0();
    }

    @Override
    protected float getV1() {
        return this.sprite.getV1();
    }

    public void pickSprite(SpriteSet spriteSet0) {
        this.setSprite(spriteSet0.get(this.f_107223_));
    }

    public void setSpriteFromAge(SpriteSet spriteSet0) {
        if (!this.f_107220_) {
            this.setSprite(spriteSet0.get(this.f_107224_, this.f_107225_));
        }
    }
}