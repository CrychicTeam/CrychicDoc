package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.mult.ToolTipBlockMethod;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ClickTraitMethod implements OnClickBlockMethod, ToolTipBlockMethod {

    public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!LHConfig.COMMON.allowHostilitySpawner.get()) {
            return InteractionResult.PASS;
        } else {
            switch((TraitSpawnerBlock.State) state.m_61143_(TraitSpawnerBlock.STATE)) {
                case IDLE:
                    if (level.getBlockEntity(pos) instanceof TraitSpawnerBlockEntity be) {
                        be.activate();
                    }
                    return InteractionResult.SUCCESS;
                case FAILED:
                    if (level.getBlockEntity(pos) instanceof TraitSpawnerBlockEntity be) {
                        be.deactivate();
                    }
                    return InteractionResult.SUCCESS;
                default:
                    return InteractionResult.PASS;
            }
        }
    }

    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        if (!LHConfig.COMMON.allowHostilitySpawner.get()) {
            list.add(LangData.IDS.BANNED.get());
        }
    }
}