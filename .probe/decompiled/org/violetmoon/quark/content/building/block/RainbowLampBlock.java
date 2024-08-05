package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.module.RainbowLampsModule;
import org.violetmoon.zeta.block.ZetaGlassBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class RainbowLampBlock extends ZetaGlassBlock {

    public final float[] colorComponents;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private String corundumDescriptionId;

    public RainbowLampBlock(String regname, int color, @Nullable ZetaModule module, MapColor mapColor) {
        super(regname, module, true, BlockBehaviour.Properties.of().strength(0.3F, 0.0F).mapColor(mapColor).instrument(NoteBlockInstrument.HAT).sound(SoundType.AMETHYST).lightLevel(bx -> bx.m_61143_(LIT) ? RainbowLampsModule.lightLevel : 0).noOcclusion());
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        this.colorComponents = new float[] { r, g, b };
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
            this.setCreativeTab(CreativeModeTabs.COLORED_BLOCKS);
        }
    }

    @NotNull
    @Override
    public String getDescriptionId() {
        if (RainbowLampsModule.isCorundum()) {
            if (this.corundumDescriptionId == null) {
                this.corundumDescriptionId = super.m_7705_().replaceAll("crystal", "corundum");
            }
            return this.corundumDescriptionId;
        } else {
            return super.m_7705_();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) this.m_49966_().m_61124_(LIT, ctx.m_43725_().m_276867_(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        if (!world.isClientSide) {
            boolean flag = (Boolean) state.m_61143_(LIT);
            if (flag != world.m_276867_(pos)) {
                if (flag) {
                    world.m_186460_(pos, this, 4);
                } else {
                    world.setBlock(pos, (BlockState) state.m_61122_(LIT), 2);
                }
            }
        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if ((Boolean) state.m_61143_(LIT) && !world.m_276867_(pos)) {
            world.m_7731_(pos, (BlockState) state.m_61122_(LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplierZeta(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        return state.m_61143_(LIT) ? this.colorComponents : null;
    }
}