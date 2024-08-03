package com.simibubi.create.content.contraptions.actors.trainControls;

import com.google.common.base.Objects;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ControlsInteractionBehaviour extends MovingInteractionBehaviour {

    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        if (AllItems.WRENCH.isIn(player.m_21120_(activeHand))) {
            return false;
        } else {
            UUID currentlyControlling = (UUID) contraptionEntity.getControllingPlayer().orElse(null);
            if (currentlyControlling != null) {
                contraptionEntity.stopControlling(localPos);
                if (Objects.equal(currentlyControlling, player.m_20148_())) {
                    return true;
                }
            }
            if (!contraptionEntity.startControlling(localPos, player)) {
                return false;
            } else {
                contraptionEntity.setControllingPlayer(player.m_20148_());
                if (player.m_9236_().isClientSide) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ControlsHandler.startControlling(contraptionEntity, localPos));
                }
                return true;
            }
        }
    }
}