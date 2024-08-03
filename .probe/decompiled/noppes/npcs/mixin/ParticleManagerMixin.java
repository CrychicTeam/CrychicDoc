package noppes.npcs.mixin;

import java.util.Map;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ParticleEngine.class })
public interface ParticleManagerMixin {

    @Accessor("spriteSets")
    Map<ResourceLocation, Object> getPacks();
}