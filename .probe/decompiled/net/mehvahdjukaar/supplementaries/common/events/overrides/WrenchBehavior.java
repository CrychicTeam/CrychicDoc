package net.mehvahdjukaar.supplementaries.common.events.overrides;

import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

class WrenchBehavior implements ItemUseOnBlockOverride {

    @Override
    public boolean altersWorld() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.Tools.WRENCH_ENABLED.get();
    }

    @Override
    public boolean appliesToItem(Item item) {
        return item == ModRegistry.WRENCH.get();
    }

    @Override
    public InteractionResult tryPerformingAction(Level world, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        CommonConfigs.Hands h = (CommonConfigs.Hands) CommonConfigs.Tools.WRENCH_BYPASS.get();
        return (h != CommonConfigs.Hands.MAIN_HAND || hand != InteractionHand.MAIN_HAND) && (h != CommonConfigs.Hands.OFF_HAND || hand != InteractionHand.OFF_HAND) && h != CommonConfigs.Hands.BOTH ? InteractionResult.PASS : stack.useOn(new UseOnContext(player, hand, hit));
    }
}