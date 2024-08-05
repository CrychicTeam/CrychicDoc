package snownee.jade.addon.harvest;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpecialToolHandler implements ToolHandler {

    private final String name;

    protected final ItemStack tool;

    public final Set<Block> blocks = Sets.newIdentityHashSet();

    public SpecialToolHandler(String name, ItemStack tool) {
        this.name = name;
        this.tool = tool;
    }

    @Override
    public ItemStack test(BlockState state, Level world, BlockPos pos) {
        return this.blocks.contains(state.m_60734_()) ? this.tool : ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getTools() {
        return List.of(this.tool);
    }

    @Override
    public String getName() {
        return this.name;
    }
}