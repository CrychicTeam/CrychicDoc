package com.simibubi.create.content.logistics.depot;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Vector3f;

@EventBusSubscriber({ Dist.CLIENT })
public class EjectorTargetHandler {

    static BlockPos currentSelection;

    static ItemStack currentItem;

    static long lastHoveredBlockPos = -1L;

    static EntityLauncher launcher;

    @SubscribeEvent
    public static void rightClickingBlocksSelectsThem(PlayerInteractEvent.RightClickBlock event) {
        if (currentItem != null) {
            BlockPos pos = event.getPos();
            Level world = event.getLevel();
            if (world.isClientSide) {
                Player player = event.getEntity();
                if (player != null && !player.isSpectator() && player.m_6144_()) {
                    String key = "weighted_ejector.target_set";
                    ChatFormatting colour = ChatFormatting.GOLD;
                    player.displayClientMessage(Lang.translateDirect(key).withStyle(colour), true);
                    currentSelection = pos;
                    launcher = null;
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
    }

    @SubscribeEvent
    public static void leftClickingBlocksDeselectsThem(PlayerInteractEvent.LeftClickBlock event) {
        if (currentItem != null) {
            if (event.getLevel().isClientSide) {
                if (event.getEntity().m_6144_()) {
                    BlockPos pos = event.getPos();
                    if (pos.equals(currentSelection)) {
                        currentSelection = null;
                        launcher = null;
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }
                }
            }
        }
    }

    public static void flushSettings(BlockPos pos) {
        int h = 0;
        int v = 0;
        LocalPlayer player = Minecraft.getInstance().player;
        String key = "weighted_ejector.target_not_valid";
        ChatFormatting colour = ChatFormatting.WHITE;
        if (currentSelection == null) {
            key = "weighted_ejector.no_target";
        }
        Direction validTargetDirection = getValidTargetDirection(pos);
        if (validTargetDirection == null) {
            player.displayClientMessage(Lang.translateDirect(key).withStyle(colour), true);
            currentItem = null;
            currentSelection = null;
        } else {
            key = "weighted_ejector.targeting";
            colour = ChatFormatting.GREEN;
            player.displayClientMessage(Lang.translateDirect(key, currentSelection.m_123341_(), currentSelection.m_123342_(), currentSelection.m_123343_()).withStyle(colour), true);
            BlockPos diff = pos.subtract(currentSelection);
            h = Math.abs(diff.m_123341_() + diff.m_123343_());
            v = -diff.m_123342_();
            AllPackets.getChannel().sendToServer(new EjectorPlacementPacket(h, v, pos, validTargetDirection));
            currentSelection = null;
            currentItem = null;
        }
    }

    public static Direction getValidTargetDirection(BlockPos pos) {
        if (currentSelection == null) {
            return null;
        } else if (VecHelper.onSameAxis(pos, currentSelection, Direction.Axis.Y)) {
            return null;
        } else {
            int xDiff = currentSelection.m_123341_() - pos.m_123341_();
            int zDiff = currentSelection.m_123343_() - pos.m_123343_();
            int max = AllConfigs.server().kinetics.maxEjectorDistance.get();
            if (Math.abs(xDiff) > max || Math.abs(zDiff) > max) {
                return null;
            } else if (xDiff == 0) {
                return Direction.get(zDiff < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE, Direction.Axis.Z);
            } else {
                return zDiff == 0 ? Direction.get(xDiff < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE, Direction.Axis.X) : null;
            }
        }
    }

    public static void tick() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack heldItemMainhand = player.m_21205_();
            if (!AllBlocks.WEIGHTED_EJECTOR.isIn(heldItemMainhand)) {
                currentItem = null;
            } else {
                if (heldItemMainhand != currentItem) {
                    currentSelection = null;
                    currentItem = heldItemMainhand;
                }
                drawOutline(currentSelection);
            }
            checkForWrench(heldItemMainhand);
            drawArc();
        }
    }

    protected static void drawArc() {
        Minecraft mc = Minecraft.getInstance();
        boolean wrench = AllItems.WRENCH.isIn(mc.player.m_21205_());
        if (currentSelection != null) {
            if (currentItem != null || wrench) {
                if (mc.hitResult instanceof BlockHitResult blockRayTraceResult) {
                    if (blockRayTraceResult.getType() != HitResult.Type.MISS) {
                        BlockPos pos = blockRayTraceResult.getBlockPos();
                        if (!wrench) {
                            pos = pos.relative(blockRayTraceResult.getDirection());
                        }
                        int xDiff = currentSelection.m_123341_() - pos.m_123341_();
                        int yDiff = currentSelection.m_123342_() - pos.m_123342_();
                        int zDiff = currentSelection.m_123343_() - pos.m_123343_();
                        int validX = Math.abs(zDiff) > Math.abs(xDiff) ? 0 : xDiff;
                        int validZ = Math.abs(zDiff) < Math.abs(xDiff) ? 0 : zDiff;
                        BlockPos validPos = currentSelection.offset(validX, yDiff, validZ);
                        Direction d = getValidTargetDirection(validPos);
                        if (d != null) {
                            if (launcher == null || lastHoveredBlockPos != pos.asLong()) {
                                lastHoveredBlockPos = pos.asLong();
                                launcher = new EntityLauncher(Math.abs(validX + validZ), yDiff);
                            }
                            double totalFlyingTicks = launcher.getTotalFlyingTicks() + 3.0;
                            int segments = (int) totalFlyingTicks / 3 + 1;
                            double tickOffset = totalFlyingTicks / (double) segments;
                            boolean valid = xDiff == validX && zDiff == validZ;
                            int intColor = valid ? 10411635 : 16740721;
                            Vector3f color = new Color(intColor).asVectorF();
                            DustParticleOptions data = new DustParticleOptions(color, 1.0F);
                            ClientLevel world = mc.level;
                            AABB bb = new AABB(0.0, 0.0, 0.0, 1.0, 0.0, 1.0).move(currentSelection.offset(-validX, -yDiff, -validZ));
                            CreateClient.OUTLINER.chaseAABB("valid", bb).colored(intColor).lineWidth(0.0625F);
                            for (int i = 0; i < segments; i++) {
                                double ticks = (double) (AnimationTickHolder.getRenderTime() / 3.0F) % tickOffset + (double) i * tickOffset;
                                Vec3 vec = launcher.getGlobalPos(ticks, d, pos).add((double) (xDiff - validX), 0.0, (double) (zDiff - validZ));
                                world.addParticle(data, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void checkForWrench(ItemStack heldItem) {
        if (AllItems.WRENCH.isIn(heldItem)) {
            if (Minecraft.getInstance().hitResult instanceof BlockHitResult result) {
                BlockPos pos = result.getBlockPos();
                BlockEntity be = Minecraft.getInstance().level.m_7702_(pos);
                if (!(be instanceof EjectorBlockEntity)) {
                    lastHoveredBlockPos = -1L;
                    currentSelection = null;
                } else {
                    if (lastHoveredBlockPos == -1L || lastHoveredBlockPos != pos.asLong()) {
                        EjectorBlockEntity ejector = (EjectorBlockEntity) be;
                        if (!ejector.getTargetPosition().equals(ejector.m_58899_())) {
                            currentSelection = ejector.getTargetPosition();
                        }
                        lastHoveredBlockPos = pos.asLong();
                        launcher = null;
                    }
                    if (lastHoveredBlockPos != -1L) {
                        drawOutline(currentSelection);
                    }
                }
            }
        }
    }

    public static void drawOutline(BlockPos selection) {
        Level world = Minecraft.getInstance().level;
        if (selection != null) {
            BlockState state = world.getBlockState(selection);
            VoxelShape shape = state.m_60808_(world, selection);
            AABB boundingBox = shape.isEmpty() ? new AABB(BlockPos.ZERO) : shape.bounds();
            CreateClient.OUTLINER.showAABB("target", boundingBox.move(selection)).colored(16763764).lineWidth(0.0625F);
        }
    }
}