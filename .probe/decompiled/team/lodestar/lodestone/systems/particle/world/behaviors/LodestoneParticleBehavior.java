package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.LodestoneBehaviorComponent;

public interface LodestoneParticleBehavior {

    LodestoneParticleBehavior BILLBOARD = new BillboardParticleBehavior();

    LodestoneParticleBehavior SPARK = new SparkParticleBehavior();

    LodestoneParticleBehavior DIRECTIONAL = new DirectionalParticleBehavior();

    void render(LodestoneWorldParticle var1, VertexConsumer var2, Camera var3, float var4);

    LodestoneBehaviorComponent getComponent(LodestoneBehaviorComponent var1);
}