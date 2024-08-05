package net.minecraftforge.client.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;

public interface IQuadTransformer {

    int STRIDE = DefaultVertexFormat.BLOCK.getIntegerSize();

    int POSITION = findOffset(DefaultVertexFormat.ELEMENT_POSITION);

    int COLOR = findOffset(DefaultVertexFormat.ELEMENT_COLOR);

    int UV0 = findOffset(DefaultVertexFormat.ELEMENT_UV0);

    int UV1 = findOffset(DefaultVertexFormat.ELEMENT_UV1);

    int UV2 = findOffset(DefaultVertexFormat.ELEMENT_UV2);

    int NORMAL = findOffset(DefaultVertexFormat.ELEMENT_NORMAL);

    void processInPlace(BakedQuad var1);

    default void processInPlace(List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            this.processInPlace(quad);
        }
    }

    default BakedQuad process(BakedQuad quad) {
        BakedQuad copy = copy(quad);
        this.processInPlace(copy);
        return copy;
    }

    default List<BakedQuad> process(List<BakedQuad> inputs) {
        return inputs.stream().map(IQuadTransformer::copy).peek(this::processInPlace).toList();
    }

    default IQuadTransformer andThen(IQuadTransformer other) {
        return quad -> {
            this.processInPlace(quad);
            other.processInPlace(quad);
        };
    }

    private static BakedQuad copy(BakedQuad quad) {
        int[] vertices = quad.getVertices();
        return new BakedQuad(Arrays.copyOf(vertices, vertices.length), quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade(), quad.hasAmbientOcclusion());
    }

    private static int findOffset(VertexFormatElement element) {
        int index = DefaultVertexFormat.BLOCK.getElements().indexOf(element);
        return index < 0 ? -1 : DefaultVertexFormat.BLOCK.getOffset(index) / 4;
    }
}