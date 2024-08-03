package com.mna.capabilities;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IChunkMagic;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.particles.ParticleAura;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class CapabilitiesInit {

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IPlayerMagic.class);
        event.register(MAPFX.class);
        event.register(IWorldMagic.class);
        event.register(IChunkMagic.class);
        event.register(IPlayerProgression.class);
        event.register(IPlayerRoteSpells.class);
        event.register(ParticleAura.class);
        ManaAndArtifice.LOGGER.info("M&A -> Capabilities Registered");
    }
}