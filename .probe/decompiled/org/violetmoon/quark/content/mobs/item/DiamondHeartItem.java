package org.violetmoon.quark.content.mobs.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.entity.Stoneling;
import org.violetmoon.quark.content.mobs.module.StonelingsModule;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

public class DiamondHeartItem extends ZetaItem {

    public DiamondHeartItem(String regname, ZetaModule module, Item.Properties properties) {
        super(regname, module, properties);
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Items.ENDER_PEARL, true);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        Direction facing = context.getClickedFace();
        if (player != null) {
            BlockState stateAt = world.getBlockState(pos);
            ItemStack stack = player.m_21120_(hand);
            if (player.mayUseItemAt(pos, facing, stack) && stateAt.m_60800_(world, pos) != -1.0F) {
                Stoneling.StonelingVariant variant = null;
                for (Stoneling.StonelingVariant possibleVariant : Stoneling.StonelingVariant.values()) {
                    if (possibleVariant.getBlocks().contains(stateAt.m_60734_())) {
                        variant = possibleVariant;
                    }
                }
                if (variant != null) {
                    if (world instanceof ServerLevelAccessor serverLevel) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        world.m_46796_(2001, pos, Block.getId(stateAt));
                        Stoneling stoneling = new Stoneling(StonelingsModule.stonelingType, world);
                        stoneling.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
                        stoneling.setPlayerMade(true);
                        stoneling.m_146922_(player.m_146908_() + 180.0F);
                        stoneling.finalizeSpawn(serverLevel, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, variant, null);
                        world.m_7967_(stoneling);
                        if (player instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, stoneling);
                            StonelingsModule.makeStonelingTrigger.trigger(serverPlayer);
                        }
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @NotNull
    @Override
    public Rarity getRarity(@NotNull ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}