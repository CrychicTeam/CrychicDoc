package com.simibubi.create.content.kinetics.deployer;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.MutablePair;

public class DeployerMovingInteraction extends MovingInteractionBehaviour {

    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
        MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> actor = contraptionEntity.getContraption().getActorAt(localPos);
        if (actor != null && actor.right != null) {
            MovementContext ctx = (MovementContext) actor.right;
            ItemStack heldStack = player.m_21120_(activeHand);
            if (heldStack.getItem().equals(AllItems.WRENCH.get())) {
                DeployerBlockEntity.Mode mode = NBTHelper.readEnum(ctx.blockEntityData, "Mode", DeployerBlockEntity.Mode.class);
                NBTHelper.writeEnum(ctx.blockEntityData, "Mode", mode == DeployerBlockEntity.Mode.PUNCH ? DeployerBlockEntity.Mode.USE : DeployerBlockEntity.Mode.PUNCH);
            } else {
                if (ctx.world.isClientSide) {
                    return true;
                }
                DeployerFakePlayer fake = null;
                if (!(ctx.temporaryData instanceof DeployerFakePlayer) && ctx.world instanceof ServerLevel) {
                    UUID owner = ctx.blockEntityData.contains("Owner") ? ctx.blockEntityData.getUUID("Owner") : null;
                    DeployerFakePlayer deployerFakePlayer = new DeployerFakePlayer((ServerLevel) ctx.world, owner);
                    deployerFakePlayer.onMinecartContraption = ctx.contraption instanceof MountedContraption;
                    deployerFakePlayer.m_150109_().load(ctx.blockEntityData.getList("Inventory", 10));
                    fake = deployerFakePlayer;
                    ctx.temporaryData = deployerFakePlayer;
                    ctx.blockEntityData.remove("Inventory");
                } else {
                    fake = (DeployerFakePlayer) ctx.temporaryData;
                }
                if (fake == null) {
                    return false;
                }
                ItemStack deployerItem = fake.m_21205_();
                player.m_21008_(activeHand, deployerItem.copy());
                fake.m_21008_(InteractionHand.MAIN_HAND, heldStack.copy());
                ctx.blockEntityData.put("HeldItem", heldStack.serializeNBT());
                ctx.data.put("HeldItem", heldStack.serializeNBT());
            }
            return true;
        } else {
            return false;
        }
    }
}