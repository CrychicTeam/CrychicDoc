package team.lodestar.lodestone.systems.particle.screen;

import java.util.ArrayList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class FrameSetScreenParticle extends GenericScreenParticle {

    public ArrayList<Integer> frameSet = new ArrayList();

    public FrameSetScreenParticle(ClientLevel world, ScreenParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, data, spriteSet, x, y, xMotion, yMotion);
    }

    @Override
    public void tick() {
        this.setSprite((Integer) this.frameSet.get(this.age));
        super.tick();
    }

    public void setSprite(int spriteIndex) {
        if (spriteIndex < this.spriteSet.sprites.size() && spriteIndex >= 0) {
            this.setSprite((TextureAtlasSprite) this.spriteSet.sprites.get(spriteIndex));
        }
    }

    protected void addLoop(int min, int max, int times) {
        for (int i = 0; i < times; i++) {
            this.addFrames(min, max);
        }
    }

    protected void addFrames(int min, int max) {
        for (int i = min; i <= max; i++) {
            this.frameSet.add(i);
        }
    }

    protected void insertFrames(int insertIndex, int min, int max) {
        for (int i = min; i <= max; i++) {
            this.frameSet.add(insertIndex, i);
        }
    }
}