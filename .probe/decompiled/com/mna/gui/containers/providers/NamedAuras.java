package com.mna.gui.containers.providers;

import com.mna.capabilities.particles.ParticleAura;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.gui.containers.particle.ParticleEmissionContainer;
import com.mna.particles.emitter.EmitterData;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.util.LazyOptional;

public class NamedAuras implements MenuProvider {

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        LazyOptional<ParticleAura> auraCap = player.getCapability(ParticleAuraProvider.AURA);
        EmitterData _data = auraCap.isPresent() ? EmitterData.fromTag(((ParticleAura) auraCap.resolve().get()).save()) : new EmitterData();
        return new ParticleEmissionContainer(i, inventory, _data, BlockPos.ZERO);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}