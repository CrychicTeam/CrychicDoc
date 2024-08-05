package com.simibubi.create.content.equipment.zapper;

import com.simibubi.create.AllPackets;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class ShootableGadgetItemMethods {

    public static void applyCooldown(Player player, ItemStack item, InteractionHand hand, Predicate<ItemStack> predicate, int cooldown) {
        if (cooldown > 0) {
            boolean gunInOtherHand = predicate.test(player.m_21120_(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND));
            player.getCooldowns().addCooldown(item.getItem(), gunInOtherHand ? cooldown * 2 / 3 : cooldown);
        }
    }

    public static void sendPackets(Player player, Function<Boolean, ? extends ShootGadgetPacket> factory) {
        if (player instanceof ServerPlayer) {
            AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> player), (ShootGadgetPacket) factory.apply(false));
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), (ShootGadgetPacket) factory.apply(true));
        }
    }

    public static boolean shouldSwap(Player player, ItemStack item, InteractionHand hand, Predicate<ItemStack> predicate) {
        boolean isSwap = item.getTag().contains("_Swap");
        boolean mainHand = hand == InteractionHand.MAIN_HAND;
        boolean gunInOtherHand = predicate.test(player.m_21120_(mainHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND));
        if (mainHand && isSwap && gunInOtherHand) {
            return true;
        } else {
            if (mainHand && !isSwap && gunInOtherHand) {
                item.getTag().putBoolean("_Swap", true);
            }
            if (!mainHand && isSwap) {
                item.getTag().remove("_Swap");
            }
            if (!mainHand && gunInOtherHand) {
                player.m_21120_(InteractionHand.MAIN_HAND).getTag().remove("_Swap");
            }
            player.m_6672_(hand);
            return false;
        }
    }

    public static Vec3 getGunBarrelVec(Player player, boolean mainHand, Vec3 rightHandForward) {
        Vec3 start = player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0);
        float yaw = (float) ((double) (player.m_146908_() / -180.0F) * Math.PI);
        float pitch = (float) ((double) (player.m_146909_() / -180.0F) * Math.PI);
        int flip = mainHand == (player.getMainArm() == HumanoidArm.RIGHT) ? -1 : 1;
        Vec3 barrelPosNoTransform = new Vec3((double) flip * rightHandForward.x, rightHandForward.y, rightHandForward.z);
        return start.add(barrelPosNoTransform.xRot(pitch).yRot(yaw));
    }
}