package net.minecraft.world.item;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class DebugStickItem extends Item {

    public DebugStickItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        if (!level1.isClientSide) {
            this.handleInteraction(player3, blockState0, level1, blockPos2, false, player3.m_21120_(InteractionHand.MAIN_HAND));
        }
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Player $$1 = useOnContext0.getPlayer();
        Level $$2 = useOnContext0.getLevel();
        if (!$$2.isClientSide && $$1 != null) {
            BlockPos $$3 = useOnContext0.getClickedPos();
            if (!this.handleInteraction($$1, $$2.getBlockState($$3), $$2, $$3, true, useOnContext0.getItemInHand())) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.sidedSuccess($$2.isClientSide);
    }

    private boolean handleInteraction(Player player0, BlockState blockState1, LevelAccessor levelAccessor2, BlockPos blockPos3, boolean boolean4, ItemStack itemStack5) {
        if (!player0.canUseGameMasterBlocks()) {
            return false;
        } else {
            Block $$6 = blockState1.m_60734_();
            StateDefinition<Block, BlockState> $$7 = $$6.getStateDefinition();
            Collection<Property<?>> $$8 = $$7.getProperties();
            String $$9 = BuiltInRegistries.BLOCK.getKey($$6).toString();
            if ($$8.isEmpty()) {
                message(player0, Component.translatable(this.m_5524_() + ".empty", $$9));
                return false;
            } else {
                CompoundTag $$10 = itemStack5.getOrCreateTagElement("DebugProperty");
                String $$11 = $$10.getString($$9);
                Property<?> $$12 = $$7.getProperty($$11);
                if (boolean4) {
                    if ($$12 == null) {
                        $$12 = (Property<?>) $$8.iterator().next();
                    }
                    BlockState $$13 = cycleState(blockState1, $$12, player0.isSecondaryUseActive());
                    levelAccessor2.m_7731_(blockPos3, $$13, 18);
                    message(player0, Component.translatable(this.m_5524_() + ".update", $$12.getName(), getNameHelper($$13, $$12)));
                } else {
                    $$12 = getRelative($$8, $$12, player0.isSecondaryUseActive());
                    String $$14 = $$12.getName();
                    $$10.putString($$9, $$14);
                    message(player0, Component.translatable(this.m_5524_() + ".select", $$14, getNameHelper(blockState1, $$12)));
                }
                return true;
            }
        }
    }

    private static <T extends Comparable<T>> BlockState cycleState(BlockState blockState0, Property<T> propertyT1, boolean boolean2) {
        return (BlockState) blockState0.m_61124_(propertyT1, getRelative(propertyT1.getPossibleValues(), blockState0.m_61143_(propertyT1), boolean2));
    }

    private static <T> T getRelative(Iterable<T> iterableT0, @Nullable T t1, boolean boolean2) {
        return boolean2 ? Util.findPreviousInIterable(iterableT0, t1) : Util.findNextInIterable(iterableT0, t1);
    }

    private static void message(Player player0, Component component1) {
        ((ServerPlayer) player0).sendSystemMessage(component1, true);
    }

    private static <T extends Comparable<T>> String getNameHelper(BlockState blockState0, Property<T> propertyT1) {
        return propertyT1.getName((T) blockState0.m_61143_(propertyT1));
    }
}