package se.mickelus.tetra.craftingeffect.condition;

import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class ToolCondition implements CraftingEffectCondition {

    ToolData tools;

    @Override
    public boolean test(ResourceLocation[] unlocks, ItemStack upgradedStack, String slot, boolean isReplacing, Player player, ItemStack[] materials, Map<ToolAction, Integer> tools, UpgradeSchematic schematic, Level world, BlockPos pos, BlockState blockState) {
        for (Entry<ToolAction, Float> req : this.tools.levelMap.entrySet()) {
            if (!tools.containsKey(req.getKey()) || (float) ((Integer) tools.get(req.getKey())).intValue() < (Float) req.getValue()) {
                return false;
            }
        }
        return true;
    }
}