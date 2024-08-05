package com.rekindled.embers.upgrade;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.AlchemyResultEvent;
import com.rekindled.embers.api.event.AlchemyStartEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.block.MnemonicInscriberBlock;
import com.rekindled.embers.blockentity.MnemonicInscriberBlockEntity;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MnemonicInscriberUpgrade extends DefaultUpgradeProvider {

    public MnemonicInscriberUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "mnemonic_inscriber"), tile);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof AlchemyStartEvent alchemyEvent && alchemyEvent.getRecipe() != null) {
            BlockState state = tile.getLevel().getBlockState(this.tile.getBlockPos());
            if (state.m_61138_(MnemonicInscriberBlock.ACTIVE)) {
                this.tile.getLevel().setBlock(this.tile.getBlockPos(), (BlockState) state.m_61124_(MnemonicInscriberBlock.ACTIVE, true), 3);
            }
        }
        if (event instanceof AlchemyResultEvent alchemyEventx && this.tile instanceof MnemonicInscriberBlockEntity inscriber) {
            if (!alchemyEventx.isFailure() && inscriber.inventory.getStackInSlot(0).is(EmbersItemTags.INSCRIBABLE_PAPER)) {
                inscriber.inventory.setStackInSlot(0, alchemyEventx.getResult().createResultStack(new ItemStack(RegistryManager.ALCHEMICAL_NOTE.get())));
                tile.getLevel().playSound(null, this.tile.getBlockPos(), EmbersSounds.EMBER_EMIT_BIG.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                if (tile.getLevel() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3(0.0, 1.0E-6, 0.0), 2.0F, 40), (double) this.tile.getBlockPos().m_123341_() + 0.5, (double) this.tile.getBlockPos().m_123342_() + 0.5, (double) this.tile.getBlockPos().m_123343_() + 0.5, 40, 0.12F, 0.12F, 0.12F, 0.0);
                }
            }
            BlockState state = tile.getLevel().getBlockState(this.tile.getBlockPos());
            if (state.m_61138_(MnemonicInscriberBlock.ACTIVE)) {
                this.tile.getLevel().setBlock(this.tile.getBlockPos(), (BlockState) state.m_61124_(MnemonicInscriberBlock.ACTIVE, false), 3);
            }
        }
    }
}