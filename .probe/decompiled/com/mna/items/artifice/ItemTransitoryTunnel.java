package com.mna.items.artifice;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.TieredItem;
import com.mna.blocks.BlockInit;
import com.mna.blocks.artifice.TransitoryTunnelBlock;
import com.mna.blocks.tileentities.TransitoryTunnelTile;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.tools.BlockUtils;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemTransitoryTunnel extends TieredItem {

    public ItemTransitoryTunnel() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.PASS;
        } else {
            IPlayerMagic magic = (IPlayerMagic) context.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (!magic.isMagicUnlocked()) {
                return InteractionResult.FAIL;
            } else {
                BlockPos[] positions = BlockUtils.getBlocksInFrontOfCharacter(context.getPlayer(), 8, context.getClickedPos());
                ArrayList<BlockPos> checkedPositions = new ArrayList();
                ArrayList<BlockPos> processedPositions = new ArrayList();
                for (int i = 1; i < positions.length; i++) {
                    BlockPos pos = positions[i];
                    int preLoopCount = processedPositions.size();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockPos operator = pos.offset(x, y, z);
                                if (!checkedPositions.contains(operator)) {
                                    checkedPositions.add(operator);
                                    BlockState existing = world.getBlockState(operator);
                                    if (existing.m_60734_() != BlockInit.TRANSITORY_TUNNEL.get() && !world.m_46859_(operator) && !(existing.m_60800_(world, operator) < 0.0F) && existing.m_60815_() && world.getBlockEntity(operator) == null) {
                                        processedPositions.add(operator);
                                        world.setBlock(operator, BlockInit.TRANSITORY_TUNNEL.get().m_49966_(), 1);
                                        BlockEntity te = world.getBlockEntity(operator);
                                        if (te != null && te instanceof TransitoryTunnelTile) {
                                            ((TransitoryTunnelTile) te).setDurationAndPreviousState(200, existing);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (preLoopCount == processedPositions.size()) {
                        break;
                    }
                }
                for (BlockPos operator : processedPositions) {
                    int mask = TransitoryTunnelBlock.getStateBasedOnSurroundings(world, operator);
                    BlockState existing = BlockInit.TRANSITORY_TUNNEL.get().m_49966_();
                    BlockState newstate = (BlockState) BlockInit.TRANSITORY_TUNNEL.get().m_49966_().m_61124_(TransitoryTunnelBlock.FACE_VISIBILITY_MASK, mask);
                    world.setBlock(operator, newstate, 1);
                    world.sendBlockUpdated(operator, existing, newstate, 3);
                }
                if (processedPositions.size() > 0) {
                    context.getPlayer().getCooldowns().addCooldown(context.getItemInHand().getItem(), 20);
                }
                return InteractionResult.PASS;
            }
        }
    }
}