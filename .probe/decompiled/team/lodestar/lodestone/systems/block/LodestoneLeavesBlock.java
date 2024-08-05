package team.lodestar.lodestone.systems.block;

import java.awt.Color;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.extensions.IForgeBlock;
import team.lodestar.lodestone.systems.easing.Easing;

public abstract class LodestoneLeavesBlock extends LeavesBlock implements IForgeBlock {

    public final Color minColor;

    public final Color maxColor;

    public LodestoneLeavesBlock(BlockBehaviour.Properties properties, Color minColor, Color maxColor) {
        super(properties);
        this.minColor = minColor;
        this.maxColor = maxColor;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(this.getColorProperty(), 0));
    }

    public abstract IntegerProperty getColorProperty();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54418_, f_54419_, f_221367_, this.getColorProperty());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(this.getColorProperty(), 0);
    }

    public static void registerSimpleGradientColors(BlockColors blockColors, LodestoneLeavesBlock leavesBlock) {
        blockColors.register((s, l, p, c) -> {
            IntegerProperty colorProperty = leavesBlock.getColorProperty();
            float colorMax = (float) colorProperty.getPossibleValues().size();
            float color = (float) ((Integer) s.m_61143_(colorProperty)).intValue();
            float pct = colorMax - color / colorMax;
            float value = Easing.SINE_IN_OUT.ease(pct, 0.0F, 1.0F, 1.0F);
            int red = (int) Mth.lerp(value, (float) leavesBlock.minColor.getRed(), (float) leavesBlock.maxColor.getRed());
            int green = (int) Mth.lerp(value, (float) leavesBlock.minColor.getGreen(), (float) leavesBlock.maxColor.getGreen());
            int blue = (int) Mth.lerp(value, (float) leavesBlock.minColor.getBlue(), (float) leavesBlock.maxColor.getBlue());
            return red << 16 | green << 8 | blue;
        }, leavesBlock);
    }
}