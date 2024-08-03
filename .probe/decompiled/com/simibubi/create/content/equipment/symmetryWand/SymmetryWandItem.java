package com.simibubi.create.content.equipment.symmetryWand;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.content.equipment.symmetryWand.mirror.CrossPlaneMirror;
import com.simibubi.create.content.equipment.symmetryWand.mirror.EmptyMirror;
import com.simibubi.create.content.equipment.symmetryWand.mirror.PlaneMirror;
import com.simibubi.create.content.equipment.symmetryWand.mirror.SymmetryMirror;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;

public class SymmetryWandItem extends Item {

    public static final String SYMMETRY = "symmetry";

    private static final String ENABLE = "enable";

    public SymmetryWandItem(Item.Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        if (player == null) {
            return InteractionResult.PASS;
        } else {
            player.getCooldowns().addCooldown(this, 5);
            ItemStack wand = player.m_21120_(context.getHand());
            checkNBT(wand);
            if (player.m_6144_()) {
                if (player.m_9236_().isClientSide) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.openWandGUI(wand, context.getHand()));
                    player.getCooldowns().addCooldown(this, 5);
                }
                return InteractionResult.SUCCESS;
            } else if (!context.getLevel().isClientSide && context.getHand() == InteractionHand.MAIN_HAND) {
                CompoundTag compound = wand.getTag().getCompound("symmetry");
                pos = pos.relative(context.getClickedFace());
                SymmetryMirror previousElement = SymmetryMirror.fromNBT(compound);
                wand.getTag().putBoolean("enable", true);
                Vec3 pos3d = new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
                SymmetryMirror newElement = new PlaneMirror(pos3d);
                if (previousElement instanceof EmptyMirror) {
                    newElement.setOrientation(player.m_6350_() != Direction.NORTH && player.m_6350_() != Direction.SOUTH ? PlaneMirror.Align.YZ.ordinal() : PlaneMirror.Align.XY.ordinal());
                    newElement.enable = true;
                    wand.getTag().putBoolean("enable", true);
                } else {
                    previousElement.setPosition(pos3d);
                    if (previousElement instanceof PlaneMirror) {
                        previousElement.setOrientation(player.m_6350_() != Direction.NORTH && player.m_6350_() != Direction.SOUTH ? PlaneMirror.Align.YZ.ordinal() : PlaneMirror.Align.XY.ordinal());
                    }
                    if (previousElement instanceof CrossPlaneMirror) {
                        float rotation = player.m_6080_();
                        float abs = Math.abs(rotation % 90.0F);
                        boolean diagonal = abs > 22.0F && abs < 67.0F;
                        previousElement.setOrientation(diagonal ? CrossPlaneMirror.Align.D.ordinal() : CrossPlaneMirror.Align.Y.ordinal());
                    }
                    newElement = previousElement;
                }
                compound = newElement.writeToNbt();
                wand.getTag().put("symmetry", compound);
                player.m_21008_(context.getHand(), wand);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack wand = playerIn.m_21120_(handIn);
        checkNBT(wand);
        if (playerIn.m_6144_()) {
            if (worldIn.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.openWandGUI(playerIn.m_21120_(handIn), handIn));
                playerIn.getCooldowns().addCooldown(this, 5);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, wand);
        } else {
            wand.getTag().putBoolean("enable", false);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, wand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void openWandGUI(ItemStack wand, InteractionHand hand) {
        ScreenOpener.open(new SymmetryWandScreen(wand, hand));
    }

    private static void checkNBT(ItemStack wand) {
        if (!wand.hasTag() || !wand.getTag().contains("symmetry")) {
            wand.setTag(new CompoundTag());
            wand.getTag().put("symmetry", new EmptyMirror(new Vec3(0.0, 0.0, 0.0)).writeToNbt());
            wand.getTag().putBoolean("enable", false);
        }
    }

    public static boolean isEnabled(ItemStack stack) {
        checkNBT(stack);
        CompoundTag tag = stack.getTag();
        return tag.getBoolean("enable") && !tag.getBoolean("Simulate");
    }

    public static SymmetryMirror getMirror(ItemStack stack) {
        checkNBT(stack);
        return SymmetryMirror.fromNBT(stack.getTag().getCompound("symmetry"));
    }

    public static void configureSettings(ItemStack stack, SymmetryMirror mirror) {
        checkNBT(stack);
        stack.getTag().put("symmetry", mirror.writeToNbt());
    }

    public static void apply(Level world, ItemStack wand, Player player, BlockPos pos, BlockState block) {
        checkNBT(wand);
        if (isEnabled(wand)) {
            if (BlockItem.f_41373_.containsKey(block.m_60734_())) {
                Map<BlockPos, BlockState> blockSet = new HashMap();
                blockSet.put(pos, block);
                SymmetryMirror symmetry = SymmetryMirror.fromNBT(wand.getTag().getCompound("symmetry"));
                Vec3 mirrorPos = symmetry.getPosition();
                if (!(mirrorPos.distanceTo(Vec3.atLowerCornerOf(pos)) > (double) AllConfigs.server().equipment.maxSymmetryWandRange.get().intValue())) {
                    if (player.isCreative() || !isHoldingBlock(player, block) || BlockHelper.findAndRemoveInInventory(block, player, 1) != 0) {
                        symmetry.process(blockSet);
                        BlockPos to = BlockPos.containing(mirrorPos);
                        List<BlockPos> targets = new ArrayList();
                        targets.add(pos);
                        for (BlockPos position : blockSet.keySet()) {
                            if (!position.equals(pos) && world.m_45752_(block, position, CollisionContext.of(player))) {
                                BlockState blockState = (BlockState) blockSet.get(position);
                                for (Direction face : Iterate.directions) {
                                    blockState = blockState.m_60728_(face, world.getBlockState(position.relative(face)), world, position, position.relative(face));
                                }
                                if (player.isCreative()) {
                                    world.setBlockAndUpdate(position, blockState);
                                    targets.add(position);
                                } else {
                                    BlockState toReplace = world.getBlockState(position);
                                    if (toReplace.m_247087_() && toReplace.m_60800_(world, position) != -1.0F) {
                                        if (AllBlocks.CART_ASSEMBLER.has(blockState)) {
                                            BlockState railBlock = CartAssemblerBlock.getRailBlock(blockState);
                                            if (BlockHelper.findAndRemoveInInventory(railBlock, player, 1) == 0) {
                                                continue;
                                            }
                                            if (BlockHelper.findAndRemoveInInventory(blockState, player, 1) == 0) {
                                                blockState = railBlock;
                                            }
                                        } else if (BlockHelper.findAndRemoveInInventory(blockState, player, 1) == 0) {
                                            continue;
                                        }
                                        BlockSnapshot blocksnapshot = BlockSnapshot.create(world.dimension(), world, position);
                                        FluidState ifluidstate = world.getFluidState(position);
                                        world.setBlock(position, ifluidstate.createLegacyBlock(), 16);
                                        world.setBlockAndUpdate(position, blockState);
                                        CompoundTag wandNbt = wand.getOrCreateTag();
                                        wandNbt.putBoolean("Simulate", true);
                                        boolean placeInterrupted = ForgeEventFactory.onBlockPlace(player, blocksnapshot, Direction.UP);
                                        wandNbt.putBoolean("Simulate", false);
                                        if (placeInterrupted) {
                                            blocksnapshot.restore(true, false);
                                        } else {
                                            targets.add(position);
                                        }
                                    }
                                }
                            }
                        }
                        AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SymmetryEffectPacket(to, targets));
                    }
                }
            }
        }
    }

    private static boolean isHoldingBlock(Player player, BlockState block) {
        ItemStack itemBlock = BlockHelper.getRequiredItem(block);
        return player.m_21055_(itemBlock.getItem());
    }

    public static void remove(Level world, ItemStack wand, Player player, BlockPos pos) {
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState ogBlock = world.getBlockState(pos);
        checkNBT(wand);
        if (isEnabled(wand)) {
            Map<BlockPos, BlockState> blockSet = new HashMap();
            blockSet.put(pos, air);
            SymmetryMirror symmetry = SymmetryMirror.fromNBT(wand.getTag().getCompound("symmetry"));
            Vec3 mirrorPos = symmetry.getPosition();
            if (!(mirrorPos.distanceTo(Vec3.atLowerCornerOf(pos)) > (double) AllConfigs.server().equipment.maxSymmetryWandRange.get().intValue())) {
                symmetry.process(blockSet);
                BlockPos to = BlockPos.containing(mirrorPos);
                List<BlockPos> targets = new ArrayList();
                targets.add(pos);
                for (BlockPos position : blockSet.keySet()) {
                    if ((player.isCreative() || ogBlock.m_60734_() == world.getBlockState(position).m_60734_()) && !position.equals(pos)) {
                        BlockState blockstate = world.getBlockState(position);
                        if (!blockstate.m_60795_()) {
                            targets.add(position);
                            world.m_46796_(2001, position, Block.getId(blockstate));
                            world.setBlock(position, air, 3);
                            if (!player.isCreative()) {
                                if (!player.m_21205_().isEmpty()) {
                                    player.m_21205_().mineBlock(world, blockstate, position, player);
                                }
                                BlockEntity blockEntity = blockstate.m_155947_() ? world.getBlockEntity(position) : null;
                                Block.dropResources(blockstate, world, pos, blockEntity, player, player.m_21205_());
                            }
                        }
                    }
                }
                AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SymmetryEffectPacket(to, targets));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SymmetryWandItemRenderer()));
    }
}