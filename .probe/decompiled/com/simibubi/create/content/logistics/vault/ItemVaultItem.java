package com.simibubi.create.content.logistics.vault;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ItemVaultItem extends BlockItem {

    public ItemVaultItem(Block p_i48527_1_, Item.Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult initialResult = super.place(ctx);
        if (!initialResult.consumesAction()) {
            return initialResult;
        } else {
            this.tryMultiPlace(ctx);
            return initialResult;
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos p_195943_1_, Level p_195943_2_, Player p_195943_3_, ItemStack p_195943_4_, BlockState p_195943_5_) {
        MinecraftServer minecraftserver = p_195943_2_.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CompoundTag nbt = p_195943_4_.getTagElement("BlockEntityTag");
            if (nbt != null) {
                nbt.remove("Length");
                nbt.remove("Size");
                nbt.remove("Controller");
                nbt.remove("LastKnownPos");
            }
            return super.updateCustomBlockEntityTag(p_195943_1_, p_195943_2_, p_195943_3_, p_195943_4_, p_195943_5_);
        }
    }

    private void tryMultiPlace(BlockPlaceContext ctx) {
        Player player = ctx.m_43723_();
        if (player != null) {
            if (!player.m_6144_()) {
                Direction face = ctx.m_43719_();
                ItemStack stack = ctx.m_43722_();
                Level world = ctx.m_43725_();
                BlockPos pos = ctx.getClickedPos();
                BlockPos placedOnPos = pos.relative(face.getOpposite());
                BlockState placedOnState = world.getBlockState(placedOnPos);
                if (ItemVaultBlock.isVault(placedOnState)) {
                    ItemVaultBlockEntity tankAt = ConnectivityHandler.partAt((BlockEntityType<?>) AllBlockEntityTypes.ITEM_VAULT.get(), world, placedOnPos);
                    if (tankAt != null) {
                        ItemVaultBlockEntity controllerBE = tankAt.getControllerBE();
                        if (controllerBE != null) {
                            int width = controllerBE.radius;
                            if (width != 1) {
                                int tanksToPlace = 0;
                                Direction.Axis vaultBlockAxis = ItemVaultBlock.getVaultBlockAxis(placedOnState);
                                if (vaultBlockAxis != null) {
                                    if (face.getAxis() == vaultBlockAxis) {
                                        Direction vaultFacing = Direction.fromAxisAndDirection(vaultBlockAxis, Direction.AxisDirection.POSITIVE);
                                        BlockPos startPos = face == vaultFacing.getOpposite() ? controllerBE.m_58899_().relative(vaultFacing.getOpposite()) : controllerBE.m_58899_().relative(vaultFacing, controllerBE.length);
                                        if (VecHelper.getCoordinate(startPos, vaultBlockAxis) == VecHelper.getCoordinate(pos, vaultBlockAxis)) {
                                            for (int xOffset = 0; xOffset < width; xOffset++) {
                                                for (int zOffset = 0; zOffset < width; zOffset++) {
                                                    BlockPos offsetPos = vaultBlockAxis == Direction.Axis.X ? startPos.offset(0, xOffset, zOffset) : startPos.offset(xOffset, zOffset, 0);
                                                    BlockState blockState = world.getBlockState(offsetPos);
                                                    if (!ItemVaultBlock.isVault(blockState)) {
                                                        if (!blockState.m_247087_()) {
                                                            return;
                                                        }
                                                        tanksToPlace++;
                                                    }
                                                }
                                            }
                                            if (player.isCreative() || stack.getCount() >= tanksToPlace) {
                                                for (int xOffset = 0; xOffset < width; xOffset++) {
                                                    for (int zOffsetx = 0; zOffsetx < width; zOffsetx++) {
                                                        BlockPos offsetPos = vaultBlockAxis == Direction.Axis.X ? startPos.offset(0, xOffset, zOffsetx) : startPos.offset(xOffset, zOffsetx, 0);
                                                        BlockState blockState = world.getBlockState(offsetPos);
                                                        if (!ItemVaultBlock.isVault(blockState)) {
                                                            BlockPlaceContext context = BlockPlaceContext.at(ctx, offsetPos, face);
                                                            player.getPersistentData().putBoolean("SilenceVaultSound", true);
                                                            super.place(context);
                                                            player.getPersistentData().remove("SilenceVaultSound");
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
            }
        }
    }
}