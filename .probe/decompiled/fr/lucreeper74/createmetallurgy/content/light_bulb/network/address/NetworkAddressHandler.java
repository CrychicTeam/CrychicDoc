package fr.lucreeper74.createmetallurgy.content.light_bulb.network.address;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.RaycastHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NetworkAddressHandler {

    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        if (!player.m_6144_() && !player.isSpectator()) {
            NetworkAddressBehaviour behaviour = BlockEntityBehaviour.get(world, pos, NetworkAddressBehaviour.TYPE);
            if (behaviour != null) {
                ItemStack heldItem = player.m_21120_(hand);
                BlockHitResult ray = RaycastHelper.rayTraceRange(world, player, 10.0);
                if (ray != null) {
                    if (!AllItems.LINKED_CONTROLLER.isIn(heldItem)) {
                        if (!AllItems.WRENCH.isIn(heldItem)) {
                            boolean fakePlayer = player instanceof FakePlayer;
                            if (behaviour.testHit(ray.m_82450_()) || fakePlayer) {
                                if (event.getSide() != LogicalSide.CLIENT) {
                                    behaviour.setAddress(heldItem);
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
    }
}