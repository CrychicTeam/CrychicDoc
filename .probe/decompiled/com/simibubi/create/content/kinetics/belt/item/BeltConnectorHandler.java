package com.simibubi.create.content.kinetics.belt.item;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class BeltConnectorHandler {

    private static Random r = new Random();

    public static void tick() {
        Player player = Minecraft.getInstance().player;
        Level world = Minecraft.getInstance().level;
        if (player != null && world != null) {
            if (Minecraft.getInstance().screen == null) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack heldItem = player.m_21120_(hand);
                    if (AllItems.BELT_CONNECTOR.isIn(heldItem) && heldItem.hasTag()) {
                        CompoundTag tag = heldItem.getTag();
                        if (tag.contains("FirstPulley")) {
                            BlockPos first = NbtUtils.readBlockPos(tag.getCompound("FirstPulley"));
                            if (world.getBlockState(first).m_61138_(BlockStateProperties.AXIS)) {
                                Direction.Axis axis = (Direction.Axis) world.getBlockState(first).m_61143_(BlockStateProperties.AXIS);
                                HitResult rayTrace = Minecraft.getInstance().hitResult;
                                if (rayTrace != null && rayTrace instanceof BlockHitResult) {
                                    BlockPos selected = ((BlockHitResult) rayTrace).getBlockPos();
                                    if (world.getBlockState(selected).m_247087_()) {
                                        return;
                                    }
                                    if (!ShaftBlock.isShaft(world.getBlockState(selected))) {
                                        selected = selected.relative(((BlockHitResult) rayTrace).getDirection());
                                    }
                                    if (!selected.m_123314_(first, (double) AllConfigs.server().kinetics.maxBeltLength.get().intValue())) {
                                        return;
                                    }
                                    boolean canConnect = BeltConnectorItem.validateAxis(world, selected) && BeltConnectorItem.canConnect(world, first, selected);
                                    Vec3 start = Vec3.atLowerCornerOf(first);
                                    Vec3 end = Vec3.atLowerCornerOf(selected);
                                    Vec3 actualDiff = end.subtract(start);
                                    end = end.subtract(axis.choose(actualDiff.x, 0.0, 0.0), axis.choose(0.0, actualDiff.y, 0.0), axis.choose(0.0, 0.0, actualDiff.z));
                                    Vec3 diff = end.subtract(start);
                                    double x = Math.abs(diff.x);
                                    double y = Math.abs(diff.y);
                                    double z = Math.abs(diff.z);
                                    float length = (float) Math.max(x, Math.max(y, z));
                                    Vec3 step = diff.normalize();
                                    int sames = (x == y ? 1 : 0) + (y == z ? 1 : 0) + (z == x ? 1 : 0);
                                    if (sames == 0) {
                                        List<Vec3> validDiffs = new LinkedList();
                                        for (int i = -1; i <= 1; i++) {
                                            for (int j = -1; j <= 1; j++) {
                                                for (int k = -1; k <= 1; k++) {
                                                    if (axis.choose(i, j, k) == 0 && (axis != Direction.Axis.Y || i == 0 || k == 0) && (i != 0 || j != 0 || k != 0)) {
                                                        validDiffs.add(new Vec3((double) i, (double) j, (double) k));
                                                    }
                                                }
                                            }
                                        }
                                        int closestIndex = 0;
                                        float closest = Float.MAX_VALUE;
                                        for (Vec3 validDiff : validDiffs) {
                                            double distanceTo = step.distanceTo(validDiff);
                                            if (distanceTo < (double) closest) {
                                                closest = (float) distanceTo;
                                                closestIndex = validDiffs.indexOf(validDiff);
                                            }
                                        }
                                        step = (Vec3) validDiffs.get(closestIndex);
                                    }
                                    if (axis == Direction.Axis.Y && step.x != 0.0 && step.z != 0.0) {
                                        return;
                                    }
                                    step = new Vec3(Math.signum(step.x), Math.signum(step.y), Math.signum(step.z));
                                    for (float f = 0.0F; f < length; f += 0.0625F) {
                                        Vec3 position = start.add(step.scale((double) f));
                                        if (r.nextInt(10) == 0) {
                                            world.addParticle(new DustParticleOptions(new Vector3f(canConnect ? 0.3F : 0.9F, canConnect ? 0.9F : 0.3F, 0.5F), 1.0F), position.x + 0.5, position.y + 0.5, position.z + 0.5, 0.0, 0.0, 0.0);
                                        }
                                    }
                                    return;
                                }
                                if (r.nextInt(50) == 0) {
                                    world.addParticle(new DustParticleOptions(new Vector3f(0.3F, 0.9F, 0.5F), 1.0F), (double) ((float) first.m_123341_() + 0.5F + randomOffset(0.25F)), (double) ((float) first.m_123342_() + 0.5F + randomOffset(0.25F)), (double) ((float) first.m_123343_() + 0.5F + randomOffset(0.25F)), 0.0, 0.0, 0.0);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static float randomOffset(float range) {
        return (r.nextFloat() - 0.5F) * 2.0F * range;
    }
}