package com.simibubi.create.content.redstone.displayLink;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTarget;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DisplayLinkBlockItem extends BlockItem {

    private static BlockPos lastShownPos = null;

    private static AABB lastShownAABB = null;

    public DisplayLinkBlockItem(Block pBlock, Item.Properties pProperties) {
        super(pBlock, pProperties);
    }

    @SubscribeEvent
    public static void gathererItemAlwaysPlacesWhenUsed(PlayerInteractEvent.RightClickBlock event) {
        ItemStack usedItem = event.getItemStack();
        if (usedItem.getItem() instanceof DisplayLinkBlockItem) {
            if (AllBlocks.DISPLAY_LINK.has(event.getLevel().getBlockState(event.getPos()))) {
                return;
            }
            event.setUseBlock(Result.DENY);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = pContext.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        } else if (player.m_6144_() && stack.hasTag()) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Lang.translateDirect("display_link.clear"), true);
                stack.setTag(null);
                return InteractionResult.SUCCESS;
            }
        } else if (!stack.hasTag()) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                CompoundTag stackTag = stack.getOrCreateTag();
                stackTag.put("SelectedPos", NbtUtils.writeBlockPos(pos));
                player.displayClientMessage(Lang.translateDirect("display_link.set"), true);
                stack.setTag(stackTag);
                return InteractionResult.SUCCESS;
            }
        } else {
            CompoundTag tag = stack.getTag();
            CompoundTag teTag = new CompoundTag();
            BlockPos selectedPos = NbtUtils.readBlockPos(tag.getCompound("SelectedPos"));
            BlockPos placedPos = pos.relative(pContext.getClickedFace(), state.m_247087_() ? 0 : 1);
            if (!selectedPos.m_123314_(placedPos, (double) AllConfigs.server().logistics.displayLinkRange.get().intValue())) {
                player.displayClientMessage(Lang.translateDirect("display_link.too_far").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            } else {
                teTag.put("TargetOffset", NbtUtils.writeBlockPos(selectedPos.subtract(placedPos)));
                tag.put("BlockEntityTag", teTag);
                InteractionResult useOn = super.useOn(pContext);
                if (!level.isClientSide && useOn != InteractionResult.FAIL) {
                    ItemStack itemInHand = player.m_21120_(pContext.getHand());
                    if (!itemInHand.isEmpty()) {
                        itemInHand.setTag(null);
                    }
                    player.displayClientMessage(Lang.translateDirect("display_link.success").withStyle(ChatFormatting.GREEN), true);
                    return useOn;
                } else {
                    return useOn;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack heldItemMainhand = player.m_21205_();
            if (heldItemMainhand.getItem() instanceof DisplayLinkBlockItem) {
                if (heldItemMainhand.hasTag()) {
                    CompoundTag stackTag = heldItemMainhand.getOrCreateTag();
                    if (stackTag.contains("SelectedPos")) {
                        BlockPos selectedPos = NbtUtils.readBlockPos(stackTag.getCompound("SelectedPos"));
                        if (!selectedPos.equals(lastShownPos)) {
                            lastShownAABB = getBounds(selectedPos);
                            lastShownPos = selectedPos;
                        }
                        CreateClient.OUTLINER.showAABB("target", lastShownAABB).colored(16763764).lineWidth(0.0625F);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static AABB getBounds(BlockPos pos) {
        Level world = Minecraft.getInstance().level;
        DisplayTarget target = AllDisplayBehaviours.targetOf(world, pos);
        if (target != null) {
            return target.getMultiblockBounds(world, pos);
        } else {
            BlockState state = world.getBlockState(pos);
            VoxelShape shape = state.m_60808_(world, pos);
            return shape.isEmpty() ? new AABB(BlockPos.ZERO) : shape.bounds().move(pos);
        }
    }
}