package snownee.jade.addon.harvest;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.util.CommonProxy;

public class ShearsToolHandler extends SpecialToolHandler {

    public ShearsToolHandler() {
        super("shears", Items.SHEARS.getDefaultInstance());
        this.blocks.add(Blocks.GLOW_LICHEN);
        this.blocks.add(Blocks.TRIPWIRE);
        this.blocks.add(Blocks.WHITE_WOOL);
        this.blocks.add(Blocks.ORANGE_WOOL);
        this.blocks.add(Blocks.MAGENTA_WOOL);
        this.blocks.add(Blocks.LIGHT_BLUE_WOOL);
        this.blocks.add(Blocks.YELLOW_WOOL);
        this.blocks.add(Blocks.LIME_WOOL);
        this.blocks.add(Blocks.PINK_WOOL);
        this.blocks.add(Blocks.GRAY_WOOL);
        this.blocks.add(Blocks.LIGHT_GRAY_WOOL);
        this.blocks.add(Blocks.CYAN_WOOL);
        this.blocks.add(Blocks.PURPLE_WOOL);
        this.blocks.add(Blocks.BLUE_WOOL);
        this.blocks.add(Blocks.BROWN_WOOL);
        this.blocks.add(Blocks.GREEN_WOOL);
        this.blocks.add(Blocks.RED_WOOL);
        this.blocks.add(Blocks.BLACK_WOOL);
    }

    @Override
    public ItemStack test(BlockState state, Level world, BlockPos pos) {
        return CommonProxy.isShearable(state) ? this.tool : super.test(state, world, pos);
    }
}