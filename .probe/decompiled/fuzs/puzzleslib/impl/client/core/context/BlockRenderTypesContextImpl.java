package fuzs.puzzleslib.impl.client.core.context;

import com.google.common.base.Preconditions;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import fuzs.puzzleslib.api.client.core.v1.context.RenderTypesContext;
import java.util.Objects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public final class BlockRenderTypesContextImpl implements RenderTypesContext<Block> {

    public void registerRenderType(RenderType renderType, Block... blocks) {
        Objects.requireNonNull(renderType, "render type is null");
        Objects.requireNonNull(blocks, "blocks is null");
        Preconditions.checkPositionIndex(1, blocks.length, "blocks is empty");
        for (Block block : blocks) {
            Objects.requireNonNull(block, "block is null");
            ClientAbstractions.INSTANCE.registerRenderType(block, renderType);
        }
    }

    public RenderType getRenderType(Block object) {
        return ClientAbstractions.INSTANCE.getRenderType(object);
    }
}