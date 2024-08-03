package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class ItemChain extends Item {

    private final boolean sticky;

    public ItemChain(boolean sticky) {
        super(new Item.Properties());
        this.sticky = sticky;
    }

    public static void attachToFence(Player player, Level worldIn, BlockPos fence) {
        double d0 = 30.0;
        int i = fence.m_123341_();
        int j = fence.m_123342_();
        int k = fence.m_123343_();
        for (LivingEntity livingEntity : worldIn.m_45976_(LivingEntity.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            EntityDataProvider.getCapability(livingEntity).ifPresent(data -> {
                if (data.chainData.isChainedTo(player)) {
                    EntityChainTie entityleashknot = EntityChainTie.getKnotForPosition(worldIn, fence);
                    if (entityleashknot == null) {
                        entityleashknot = EntityChainTie.createTie(worldIn, fence);
                    }
                    data.chainData.removeChain(player);
                    data.chainData.attachChain(entityleashknot);
                }
            });
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.chain.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.chain.desc_1").withStyle(ChatFormatting.GRAY));
        if (this.sticky) {
            tooltip.add(Component.translatable("item.iceandfire.chain_sticky.desc_2").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.iceandfire.chain_sticky.desc_3").withStyle(ChatFormatting.GREEN));
        }
    }

    @NotNull
    @Override
    public InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player playerIn, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        EntityDataProvider.getCapability(target).ifPresent(targetData -> {
            if (!targetData.chainData.isChainedTo(playerIn)) {
                if (this.sticky) {
                    double d0 = 60.0;
                    double i = playerIn.m_20185_();
                    double j = playerIn.m_20186_();
                    double k = playerIn.m_20189_();
                    List<LivingEntity> nearbyEntities = playerIn.m_9236_().m_45976_(LivingEntity.class, new AABB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0));
                    if (playerIn.m_6047_()) {
                        targetData.chainData.clearChains();
                        for (LivingEntity livingEntity : nearbyEntities) {
                            EntityDataProvider.getCapability(livingEntity).ifPresent(nearbyData -> nearbyData.chainData.removeChain(target));
                        }
                        return;
                    }
                    AtomicBoolean flag = new AtomicBoolean(false);
                    for (LivingEntity livingEntity : nearbyEntities) {
                        EntityDataProvider.getCapability(livingEntity).ifPresent(nearbyData -> {
                            if (nearbyData.chainData.isChainedTo(playerIn)) {
                                targetData.chainData.removeChain(playerIn);
                                nearbyData.chainData.removeChain(playerIn);
                                nearbyData.chainData.attachChain(target);
                                flag.set(true);
                            }
                        });
                    }
                    if (!flag.get()) {
                        targetData.chainData.attachChain(playerIn);
                    }
                } else {
                    targetData.chainData.attachChain(playerIn);
                }
                if (!playerIn.isCreative()) {
                    stack.shrink(1);
                }
            }
        });
        return InteractionResult.SUCCESS;
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Block block = context.getLevel().getBlockState(context.getClickedPos()).m_60734_();
        if (!(block instanceof WallBlock)) {
            return InteractionResult.PASS;
        } else {
            if (!context.getLevel().isClientSide) {
                attachToFence(context.getPlayer(), context.getLevel(), context.getClickedPos());
            }
            return InteractionResult.SUCCESS;
        }
    }
}