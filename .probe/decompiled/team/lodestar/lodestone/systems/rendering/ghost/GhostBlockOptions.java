package team.lodestar.lodestone.systems.rendering.ghost;

import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;

public class GhostBlockOptions {

    protected final BlockState blockState;

    protected BlockPos blockPos;

    protected Supplier<Float> alphaSupplier;

    protected Supplier<Float> scaleSupplier;

    protected float red = 1.0F;

    protected float green = 1.0F;

    protected float blue = 1.0F;

    protected RenderType renderType = RenderType.translucent();

    private GhostBlockOptions(BlockState state) {
        this.blockState = state;
        this.blockPos = BlockPos.ZERO;
        this.alphaSupplier = () -> PlacementAssistantHandler.getCurrentAlpha() * 0.75F;
        this.scaleSupplier = () -> 1.0F;
    }

    public static GhostBlockOptions create(BlockState state, BlockPos pos) {
        return new GhostBlockOptions(state).at(pos);
    }

    public static GhostBlockOptions create(BlockState state) {
        return new GhostBlockOptions(state);
    }

    public static GhostBlockOptions create(Block block, BlockPos pos) {
        return create(block.defaultBlockState(), pos);
    }

    public static GhostBlockOptions create(Block block) {
        return create(block.defaultBlockState());
    }

    public GhostBlockOptions at(BlockPos pos) {
        this.blockPos = pos;
        return this;
    }

    public GhostBlockOptions at(int x, int y, int z) {
        return this.at(new BlockPos(x, y, z));
    }

    public GhostBlockOptions withAlpha(Supplier<Float> alphaSupplier) {
        this.alphaSupplier = alphaSupplier;
        return this;
    }

    public GhostBlockOptions withScale(Supplier<Float> scaleSupplier) {
        this.scaleSupplier = scaleSupplier;
        return this;
    }

    public GhostBlockOptions withColor(Color color) {
        return this.withColor((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F);
    }

    public GhostBlockOptions withColor(float r, float g, float b) {
        this.red = r;
        this.green = g;
        this.blue = b;
        return this;
    }

    public GhostBlockOptions withRenderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }
}