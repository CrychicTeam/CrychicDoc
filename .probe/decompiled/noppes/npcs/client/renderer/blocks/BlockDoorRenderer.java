package noppes.npcs.client.renderer.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.data.ModelData;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileDoor;

public class BlockDoorRenderer extends BlockRendererInterface<TileDoor> {

    private static Random random = new Random();

    public BlockDoorRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    public void render(TileDoor tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        BlockState original = tile.m_58904_().getBlockState(tile.m_58899_());
        if (!original.m_60795_()) {
            BlockPos lowerPos = tile.m_58899_();
            if (original.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
                lowerPos = tile.m_58899_().below();
            }
            BlockPos upperPos = lowerPos.above();
            TileDoor lowerTile = (TileDoor) tile.m_58904_().getBlockEntity(lowerPos);
            TileDoor upperTile = (TileDoor) tile.m_58904_().getBlockEntity(upperPos);
            if (lowerTile != null && upperTile != null) {
                BlockState lowerState = lowerTile.m_58900_();
                BlockState upperState = upperTile.m_58900_();
                Block b = lowerTile.blockModel;
                if (this.overrideModel()) {
                    b = CustomBlocks.scripted_door;
                }
                BlockState state = b.defaultBlockState();
                state = (BlockState) state.m_61124_(DoorBlock.HALF, (DoubleBlockHalf) original.m_61143_(DoorBlock.HALF));
                state = (BlockState) state.m_61124_(DoorBlock.FACING, (Direction) lowerState.m_61143_(DoorBlock.FACING));
                state = (BlockState) state.m_61124_(DoorBlock.OPEN, (Boolean) lowerState.m_61143_(DoorBlock.OPEN));
                state = (BlockState) state.m_61124_(DoorBlock.HINGE, (DoorHingeSide) upperState.m_61143_(DoorBlock.HINGE));
                state = (BlockState) state.m_61124_(DoorBlock.POWERED, (Boolean) upperState.m_61143_(DoorBlock.POWERED));
                matrixStack.pushPose();
                this.renderBlock(matrixStack, buffer, tile, lowerState.m_60734_(), state, light, overlay);
                matrixStack.popPose();
            }
        }
    }

    private void renderBlock(PoseStack matrixStack, MultiBufferSource buffer, TileDoor tile, Block b, BlockState state, int light, int overlay) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel ibakedmodel = dispatcher.getBlockModel(state);
        if (ibakedmodel == null) {
            dispatcher.renderSingleBlock(state, matrixStack, buffer, light, overlay);
        } else {
            dispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, ibakedmodel, 1.0F, 1.0F, 1.0F, light, overlay, ModelData.EMPTY, ItemBlockRenderTypes.getRenderType(state, false));
        }
    }

    private boolean overrideModel() {
        ItemStack held = Minecraft.getInstance().player.m_21205_();
        return held == null ? false : held.getItem() == CustomItems.wand || held.getItem() == CustomItems.scripter || held.getItem() == CustomBlocks.scripted_door_item;
    }
}