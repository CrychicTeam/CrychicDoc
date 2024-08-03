package com.simibubi.create.content.equipment.toolbox;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.ScreenOpener;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ToolboxHandlerClient {

    public static final IGuiOverlay OVERLAY = ToolboxHandlerClient::renderOverlay;

    static int COOLDOWN = 0;

    public static void clientTick() {
        if (COOLDOWN > 0 && !AllKeys.TOOLBELT.isPressed()) {
            COOLDOWN--;
        }
    }

    public static boolean onPickItem() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return false;
        } else {
            Level level = player.m_9236_();
            HitResult hitResult = mc.hitResult;
            if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                if (player.m_7500_()) {
                    return false;
                } else {
                    ItemStack result = ItemStack.EMPTY;
                    List<ToolboxBlockEntity> toolboxes = ToolboxHandler.getNearest(player.m_9236_(), player, 8);
                    if (toolboxes.isEmpty()) {
                        return false;
                    } else {
                        if (hitResult.getType() == HitResult.Type.BLOCK) {
                            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                            BlockState state = level.getBlockState(pos);
                            if (state.m_60795_()) {
                                return false;
                            }
                            result = state.getCloneItemStack(hitResult, level, pos, player);
                        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                            Entity entity = ((EntityHitResult) hitResult).getEntity();
                            result = entity.getPickedResult(hitResult);
                        }
                        if (result.isEmpty()) {
                            return false;
                        } else {
                            for (ToolboxBlockEntity toolboxBlockEntity : toolboxes) {
                                ToolboxInventory inventory = toolboxBlockEntity.inventory;
                                for (int comp = 0; comp < 8; comp++) {
                                    ItemStack inSlot = inventory.takeFromCompartment(1, comp, true);
                                    if (!inSlot.isEmpty() && inSlot.getItem() == result.getItem() && ItemStack.matches(inSlot, result)) {
                                        AllPackets.getChannel().sendToServer(new ToolboxEquipPacket(toolboxBlockEntity.m_58899_(), comp, player.m_150109_().selected));
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    public static void onKeyInput(int key, boolean pressed) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode != null && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            if (key == AllKeys.TOOLBELT.getBoundCode()) {
                if (COOLDOWN <= 0) {
                    LocalPlayer player = mc.player;
                    if (player != null) {
                        Level level = player.m_9236_();
                        List<ToolboxBlockEntity> toolboxes = ToolboxHandler.getNearest(player.m_9236_(), player, 8);
                        toolboxes.sort(Comparator.comparing(ToolboxBlockEntity::getUniqueId));
                        CompoundTag compound = player.getPersistentData().getCompound("CreateToolboxData");
                        String slotKey = String.valueOf(player.m_150109_().selected);
                        boolean equipped = compound.contains(slotKey);
                        if (equipped) {
                            BlockPos pos = NbtUtils.readBlockPos(compound.getCompound(slotKey).getCompound("Pos"));
                            double max = ToolboxHandler.getMaxRange(player);
                            boolean canReachToolbox = ToolboxHandler.distance(player.m_20182_(), pos) < max * max;
                            if (canReachToolbox) {
                                BlockEntity blockEntity = level.getBlockEntity(pos);
                                if (blockEntity instanceof ToolboxBlockEntity) {
                                    RadialToolboxMenu screen = new RadialToolboxMenu(toolboxes, RadialToolboxMenu.State.SELECT_ITEM_UNEQUIP, (ToolboxBlockEntity) blockEntity);
                                    screen.prevSlot(compound.getCompound(slotKey).getInt("Slot"));
                                    ScreenOpener.open(screen);
                                    return;
                                }
                            }
                            ScreenOpener.open(new RadialToolboxMenu(ImmutableList.of(), RadialToolboxMenu.State.DETACH, null));
                        } else if (!toolboxes.isEmpty()) {
                            if (toolboxes.size() == 1) {
                                ScreenOpener.open(new RadialToolboxMenu(toolboxes, RadialToolboxMenu.State.SELECT_ITEM, (ToolboxBlockEntity) toolboxes.get(0)));
                            } else {
                                ScreenOpener.open(new RadialToolboxMenu(toolboxes, RadialToolboxMenu.State.SELECT_BOX, null));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            int x = width / 2 - 90;
            int y = height - 23;
            RenderSystem.enableDepthTest();
            Player player = mc.player;
            CompoundTag persistentData = player.getPersistentData();
            if (persistentData.contains("CreateToolboxData")) {
                CompoundTag compound = player.getPersistentData().getCompound("CreateToolboxData");
                if (!compound.isEmpty()) {
                    PoseStack poseStack = graphics.pose();
                    poseStack.pushPose();
                    for (int slot = 0; slot < 9; slot++) {
                        String key = String.valueOf(slot);
                        if (compound.contains(key)) {
                            BlockPos pos = NbtUtils.readBlockPos(compound.getCompound(key).getCompound("Pos"));
                            double max = ToolboxHandler.getMaxRange(player);
                            boolean selected = player.getInventory().selected == slot;
                            int offset = selected ? 1 : 0;
                            AllGuiTextures texture = ToolboxHandler.distance(player.m_20182_(), pos) < max * max ? (selected ? AllGuiTextures.TOOLBELT_SELECTED_ON : AllGuiTextures.TOOLBELT_HOTBAR_ON) : (selected ? AllGuiTextures.TOOLBELT_SELECTED_OFF : AllGuiTextures.TOOLBELT_HOTBAR_OFF);
                            texture.render(graphics, x + 20 * slot - offset, y + offset);
                        }
                    }
                    poseStack.popPose();
                }
            }
        }
    }
}