package team.lodestar.lodestone.systems.particle.world;

import java.util.ArrayList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class FrameSetParticle extends LodestoneWorldParticle {

    public ArrayList<Integer> frameSet = new ArrayList();

    public FrameSetParticle(ClientLevel world, WorldParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
    }

    @Override
    public void tick() {
        if (this.f_107224_ < this.frameSet.size()) {
            this.pickSprite((Integer) this.frameSet.get(this.f_107224_));
        }
        super.tick();
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return SimpleParticleOptions.ParticleSpritePicker.FIRST_INDEX;
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