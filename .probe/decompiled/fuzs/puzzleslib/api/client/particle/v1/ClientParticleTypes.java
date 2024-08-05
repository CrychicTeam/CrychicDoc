package fuzs.puzzleslib.api.client.particle.v1;

import fuzs.puzzleslib.impl.client.particle.ClientParticleTypesImpl;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface ClientParticleTypes {

    ClientParticleTypes INSTANCE = new ClientParticleTypesImpl();

    @Nullable
    default Particle createParticle(ResourceLocation identifier, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return this.createParticle(identifier, new SimpleParticleType(false), x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Nullable
    Particle createParticle(ResourceLocation var1, ParticleOptions var2, double var3, double var5, double var7, double var9, double var11, double var13);
}