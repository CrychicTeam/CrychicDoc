package team.lodestar.lodestone.systems.particle.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

public class ScreenParticleHolder {

    public final Map<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> particles = new HashMap();

    public void tick() {
        this.particles.forEach((pair, particles) -> {
            Iterator<ScreenParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ScreenParticle particle = (ScreenParticle) iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        });
    }

    public void addFrom(ScreenParticleHolder otherHolder) {
        this.particles.putAll(otherHolder.particles);
    }

    public boolean isEmpty() {
        return this.particles.values().stream().allMatch(ArrayList::isEmpty);
    }
}