package se.mickelus.tetra.blocks.workbench;

import java.util.Arrays;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.InteractionOutcome;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchAction;

@ParametersAreNonnullByDefault
public class ActionInteraction extends BlockInteraction {

    private final String actionKey;

    public ActionInteraction(ToolAction requiredType, int requiredLevel, String actionKey) {
        super(requiredType, requiredLevel, Direction.UP, 5.0F, 11.0F, 5.0F, 11.0F, InteractionOutcome.EMPTY);
        this.actionKey = actionKey;
        this.applyUsageEffects = false;
    }

    public static ActionInteraction create(WorkbenchTile tile) {
        ItemStack targetStack = tile.getTargetItemStack();
        return (ActionInteraction) Arrays.stream(tile.getAvailableActions(null)).filter(WorkbenchAction::allowInWorldInteraction).filter(action -> action.getRequiredTools(targetStack).entrySet().size() == 1).findFirst().map(action -> {
            Entry<ToolAction, Integer> requirementPair = (Entry<ToolAction, Integer>) action.getRequiredTools(targetStack).entrySet().stream().findFirst().get();
            return new ActionInteraction((ToolAction) requirementPair.getKey(), (Integer) requirementPair.getValue(), action.getKey());
        }).orElse(null);
    }

    @Override
    public boolean applicableForBlock(Level world, BlockPos pos, BlockState blockState) {
        return this.actionKey != null;
    }

    @Override
    public void applyOutcome(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, Direction hitFace) {
        if (!world.isClientSide) {
            CastOptional.cast(world.getBlockEntity(pos), WorkbenchTile.class).ifPresent(tile -> {
                if (player != null) {
                    tile.performAction(player, this.actionKey);
                } else {
                    tile.performAction(this.actionKey);
                }
            });
        }
    }
}