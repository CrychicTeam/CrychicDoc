package com.simibubi.create.content.equipment.clipboard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.track.TrackBlockOutline;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ClipboardValueSettingsHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void drawCustomBlockSelection(RenderHighlightEvent.Block event) {
        Minecraft mc = Minecraft.getInstance();
        BlockHitResult target = event.getTarget();
        BlockPos pos = target.getBlockPos();
        BlockState blockstate = mc.level.m_8055_(pos);
        if (mc.player != null && !mc.player.m_5833_()) {
            if (mc.level.m_6857_().isWithinBounds(pos)) {
                if (AllBlocks.CLIPBOARD.isIn(mc.player.m_21205_())) {
                    if (mc.level.m_7702_(pos) instanceof SmartBlockEntity smartBE) {
                        if (smartBE.getAllBehaviours().stream().anyMatch(b -> {
                            if (b instanceof ClipboardCloneable cc && cc.writeToClipboard(new CompoundTag(), target.getDirection())) {
                                return true;
                            }
                            return false;
                        })) {
                            VoxelShape shape = blockstate.m_60808_(mc.level, pos);
                            if (!shape.isEmpty()) {
                                VertexConsumer vb = event.getMultiBufferSource().getBuffer(RenderType.lines());
                                Vec3 camPos = event.getCamera().getPosition();
                                PoseStack ms = event.getPoseStack();
                                ms.pushPose();
                                ms.translate((double) pos.m_123341_() - camPos.x, (double) pos.m_123342_() - camPos.y, (double) pos.m_123343_() - camPos.z);
                                TrackBlockOutline.renderShape(shape, ms, vb, true);
                                event.setCanceled(true);
                                ms.popPose();
                            }
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.hitResult instanceof BlockHitResult target) {
            if (AllBlocks.CLIPBOARD.isIn(mc.player.m_21205_())) {
                BlockPos pos = target.getBlockPos();
                if (mc.level.m_7702_(pos) instanceof SmartBlockEntity smartBE) {
                    CompoundTag var9;
                    boolean var10000;
                    label69: {
                        var9 = mc.player.m_21205_().getTagElement("CopiedValues");
                        label55: if (!smartBE.getAllBehaviours().stream().anyMatch(b -> {
                            if (b instanceof ClipboardCloneable cc && cc.writeToClipboard(new CompoundTag(), target.getDirection())) {
                                return true;
                            }
                            return false;
                        })) {
                            if (smartBE instanceof ClipboardCloneable ccbe && ccbe.writeToClipboard(new CompoundTag(), target.getDirection())) {
                                break label55;
                            }
                            var10000 = false;
                            break label69;
                        }
                        var10000 = true;
                    }
                    boolean canCopy = var10000;
                    boolean canPaste = var9 != null && (smartBE.getAllBehaviours().stream().anyMatch(b -> {
                        if (b instanceof ClipboardCloneable cc && cc.readFromClipboard(var9.getCompound(cc.getClipboardKey()), mc.player, target.getDirection(), true)) {
                            return true;
                        }
                        return false;
                    }) || smartBE instanceof ClipboardCloneable ccbe && ccbe.readFromClipboard(var9.getCompound(ccbe.getClipboardKey()), mc.player, target.getDirection(), true));
                    if (canCopy || canPaste) {
                        List<MutableComponent> tip = new ArrayList();
                        tip.add(Lang.translateDirect("clipboard.actions"));
                        if (canCopy) {
                            tip.add(Lang.translateDirect("clipboard.to_copy", Components.keybind("key.use")));
                        }
                        if (canPaste) {
                            tip.add(Lang.translateDirect("clipboard.to_paste", Components.keybind("key.attack")));
                        }
                        CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tip);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void rightClickToCopy(PlayerInteractEvent.RightClickBlock event) {
        interact(event, false);
    }

    @SubscribeEvent
    public static void leftClickToPaste(PlayerInteractEvent.LeftClickBlock event) {
        interact(event, true);
    }

    private static void interact(PlayerInteractEvent event, boolean paste) {
        ItemStack itemStack = event.getItemStack();
        if (AllBlocks.CLIPBOARD.isIn(itemStack)) {
            BlockPos pos = event.getPos();
            Level world = event.getLevel();
            Player player = event.getEntity();
            if (player == null || !player.isSpectator()) {
                if (!player.m_6144_()) {
                    if (world.getBlockEntity(pos) instanceof SmartBlockEntity smartBE) {
                        CompoundTag var16 = itemStack.getTagElement("CopiedValues");
                        if (!paste || var16 != null) {
                            if (!paste) {
                                var16 = new CompoundTag();
                            }
                            boolean anySuccess = false;
                            boolean anyValid = false;
                            for (BlockEntityBehaviour behaviour : smartBE.getAllBehaviours()) {
                                if (behaviour instanceof ClipboardCloneable) {
                                    ClipboardCloneable cc = (ClipboardCloneable) behaviour;
                                    anyValid = true;
                                    String clipboardKey = cc.getClipboardKey();
                                    if (paste) {
                                        anySuccess |= cc.readFromClipboard(var16.getCompound(clipboardKey), player, event.getFace(), world.isClientSide());
                                    } else {
                                        CompoundTag compoundTag = new CompoundTag();
                                        boolean success = cc.writeToClipboard(compoundTag, event.getFace());
                                        anySuccess |= success;
                                        if (success) {
                                            var16.put(clipboardKey, compoundTag);
                                        }
                                    }
                                }
                            }
                            if (smartBE instanceof ClipboardCloneable ccbe) {
                                anyValid = true;
                                String clipboardKey = ccbe.getClipboardKey();
                                if (paste) {
                                    anySuccess |= ccbe.readFromClipboard(var16.getCompound(clipboardKey), player, event.getFace(), world.isClientSide());
                                } else {
                                    CompoundTag compoundTag = new CompoundTag();
                                    boolean success = ccbe.writeToClipboard(compoundTag, event.getFace());
                                    anySuccess |= success;
                                    if (success) {
                                        var16.put(clipboardKey, compoundTag);
                                    }
                                }
                            }
                            if (anyValid) {
                                event.setCanceled(true);
                                event.setCancellationResult(InteractionResult.SUCCESS);
                                if (!world.isClientSide()) {
                                    if (anySuccess) {
                                        player.displayClientMessage(Lang.translate(paste ? "clipboard.pasted_to" : "clipboard.copied_from", world.getBlockState(pos).m_60734_().getName().withStyle(ChatFormatting.WHITE)).style(ChatFormatting.GREEN).component(), true);
                                        if (!paste) {
                                            ClipboardOverrides.switchTo(ClipboardOverrides.ClipboardType.WRITTEN, itemStack);
                                            itemStack.getOrCreateTag().put("CopiedValues", var16);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}