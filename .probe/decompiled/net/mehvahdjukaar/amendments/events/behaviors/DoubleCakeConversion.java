package net.mehvahdjukaar.amendments.events.behaviors;

import com.google.common.base.Suppliers;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.common.block.DirectionalCakeBlock;
import net.mehvahdjukaar.amendments.common.block.DoubleCakeBlock;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

class DoubleCakeConversion implements ItemUseOnBlock {

    private static final Supplier<Set<Block>> CAKES = Suppliers.memoize(() -> (Set) CakeRegistry.INSTANCE.getValues().stream().map(v -> v.cake).collect(Collectors.toUnmodifiableSet()));

    private static final Supplier<Set<Item>> CAKES_ITEMS = Suppliers.memoize(() -> (Set) CakeRegistry.INSTANCE.getValues().stream().map(v -> v.cake.asItem()).collect(Collectors.toUnmodifiableSet()));

    @Override
    public boolean altersWorld() {
        return true;
    }

    @Override
    public boolean placesBlock() {
        return true;
    }

    @Nullable
    @Override
    public MutableComponent getTooltip() {
        return Component.translatable("message.amendments.double_cake");
    }

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.DOUBLE_CAKES.get();
    }

    @Override
    public boolean appliesToItem(Item item) {
        return ((Set) CAKES_ITEMS.get()).contains(item);
    }

    @Override
    public InteractionResult tryPerformingAction(Level level, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.m_60734_();
        if ((((Set) CAKES.get()).contains(block) || block instanceof DirectionalCakeBlock) && (Integer) state.m_61143_(DirectionalCakeBlock.f_51180_) == 0) {
            CakeRegistry.CakeType t;
            if (block instanceof DirectionalCakeBlock dc) {
                t = dc.type;
            } else {
                t = CakeRegistry.INSTANCE.getBlockTypeOf(block);
            }
            if (t == null || !(stack.getItem() instanceof BlockItem bi) || CakeRegistry.INSTANCE.getBlockTypeOf(bi.getBlock()) != t) {
                return InteractionResult.PASS;
            }
            Block doubleCake = t.getBlockOfThis("double_cake");
            if (doubleCake != null) {
                return InteractEvents.replaceSimilarBlock(doubleCake, player, stack, pos, level, state, null, true, true, DoubleCakeBlock.FACING);
            }
        }
        return InteractionResult.PASS;
    }
}