package com.simibubi.create.foundation.blockEntity.behaviour.edgeInteraction;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.RaycastHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EdgeInteractionHandler {

    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.m_21120_(hand);
        if (!player.m_6144_() && !player.isSpectator()) {
            EdgeInteractionBehaviour behaviour = BlockEntityBehaviour.get(world, pos, EdgeInteractionBehaviour.TYPE);
            if (behaviour != null) {
                if (!behaviour.requiredItem.isPresent() || behaviour.requiredItem.get() == heldItem.getItem()) {
                    BlockHitResult ray = RaycastHelper.rayTraceRange(world, player, 10.0);
                    if (ray != null) {
                        Direction activatedDirection = getActivatedDirection(world, pos, ray.getDirection(), ray.m_82450_(), behaviour);
                        if (activatedDirection != null) {
                            if (event.getSide() != LogicalSide.CLIENT) {
                                behaviour.connectionCallback.apply(world, pos, pos.relative(activatedDirection));
                            }
                            event.setCanceled(true);
                            event.setCancellationResult(InteractionResult.SUCCESS);
                            world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.25F, 0.1F);
                        }
                    }
                }
            }
        }
    }

    public static List<Direction> getConnectiveSides(Level world, BlockPos pos, Direction face, EdgeInteractionBehaviour behaviour) {
        List<Direction> sides = new ArrayList(6);
        if (BlockHelper.hasBlockSolidSide(world.getBlockState(pos.relative(face)), world, pos.relative(face), face.getOpposite())) {
            return sides;
        } else {
            for (Direction direction : Iterate.directions) {
                if (direction.getAxis() != face.getAxis()) {
                    BlockPos neighbourPos = pos.relative(direction);
                    if (!BlockHelper.hasBlockSolidSide(world.getBlockState(neighbourPos.relative(face)), world, neighbourPos.relative(face), face.getOpposite()) && behaviour.connectivityPredicate.test(world, pos, face, direction)) {
                        sides.add(direction);
                    }
                }
            }
            return sides;
        }
    }

    public static Direction getActivatedDirection(Level world, BlockPos pos, Direction face, Vec3 hit, EdgeInteractionBehaviour behaviour) {
        for (Direction facing : getConnectiveSides(world, pos, face, behaviour)) {
            AABB bb = getBB(pos, facing);
            if (bb.contains(hit)) {
                return facing;
            }
        }
        return null;
    }

    static AABB getBB(BlockPos pos, Direction direction) {
        AABB bb = new AABB(pos);
        Vec3i vec = direction.getNormal();
        int x = vec.getX();
        int y = vec.getY();
        int z = vec.getZ();
        double margin = 0.625;
        double absX = (double) Math.abs(x) * margin;
        double absY = (double) Math.abs(y) * margin;
        double absZ = (double) Math.abs(z) * margin;
        bb = bb.contract(absX, absY, absZ);
        bb = bb.move(absX / 2.0, absY / 2.0, absZ / 2.0);
        bb = bb.move((double) x / 2.0, (double) y / 2.0, (double) z / 2.0);
        return bb.inflate(0.00390625);
    }
}