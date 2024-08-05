package com.simibubi.create.content.contraptions.behaviour;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public abstract class SimpleBlockMovingInteraction extends MovingInteractionBehaviour {

    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        Contraption contraption = contraptionEntity.getContraption();
        StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(localPos);
        BlockState newState = this.handle(player, contraption, localPos, info.state());
        if (info.state() == newState) {
            return false;
        } else {
            this.setContraptionBlockData(contraptionEntity, localPos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
            if (this.updateColliders()) {
                contraption.invalidateColliders();
            }
            return true;
        }
    }

    protected boolean updateColliders() {
        return false;
    }

    protected void playSound(Player player, SoundEvent sound, float pitch) {
        player.m_9236_().playSound(player, player.m_20183_(), sound, SoundSource.BLOCKS, 0.3F, pitch);
    }

    protected abstract BlockState handle(Player var1, Contraption var2, BlockPos var3, BlockState var4);
}