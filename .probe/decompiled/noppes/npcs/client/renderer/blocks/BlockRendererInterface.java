package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockRendererInterface<T extends BlockEntity> implements BlockEntityRenderer<T> {

    public static float[][] colorTable = new float[][] { { 1.0F, 1.0F, 1.0F }, { 0.95F, 0.7F, 0.2F }, { 0.9F, 0.5F, 0.85F }, { 0.6F, 0.7F, 0.95F }, { 0.9F, 0.9F, 0.2F }, { 0.5F, 0.8F, 0.1F }, { 0.95F, 0.7F, 0.8F }, { 0.3F, 0.3F, 0.3F }, { 0.6F, 0.6F, 0.6F }, { 0.3F, 0.6F, 0.7F }, { 0.7F, 0.4F, 0.9F }, { 0.2F, 0.4F, 0.8F }, { 0.5F, 0.4F, 0.3F }, { 0.4F, 0.5F, 0.2F }, { 0.8F, 0.3F, 0.3F }, { 0.1F, 0.1F, 0.1F } };

    public BlockRendererInterface(BlockEntityRendererProvider.Context dispatcher) {
    }

    public boolean playerTooFar(BlockEntity tile) {
        Minecraft mc = Minecraft.getInstance();
        double d6 = mc.getCameraEntity().getX() - (double) tile.getBlockPos().m_123341_();
        double d7 = mc.getCameraEntity().getY() - (double) tile.getBlockPos().m_123342_();
        double d8 = mc.getCameraEntity().getZ() - (double) tile.getBlockPos().m_123343_();
        return d6 * d6 + d7 * d7 + d8 * d8 > (double) (this.specialRenderDistance() * this.specialRenderDistance());
    }

    public int specialRenderDistance() {
        return 20;
    }
}