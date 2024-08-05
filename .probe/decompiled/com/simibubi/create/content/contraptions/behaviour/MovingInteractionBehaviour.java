package com.simibubi.create.content.contraptions.behaviour;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.MutablePair;

public abstract class MovingInteractionBehaviour {

    protected void setContraptionActorData(AbstractContraptionEntity contraptionEntity, int index, StructureTemplate.StructureBlockInfo info, MovementContext ctx) {
        contraptionEntity.getContraption().getActors().remove(index);
        contraptionEntity.getContraption().getActors().add(index, MutablePair.of(info, ctx));
        if (contraptionEntity.m_9236_().isClientSide) {
            contraptionEntity.getContraption().deferInvalidate = true;
        }
    }

    protected void setContraptionBlockData(AbstractContraptionEntity contraptionEntity, BlockPos pos, StructureTemplate.StructureBlockInfo info) {
        if (!contraptionEntity.m_9236_().isClientSide()) {
            contraptionEntity.setBlock(pos, info);
        }
    }

    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        return true;
    }

    public void handleEntityCollision(Entity entity, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
    }
}