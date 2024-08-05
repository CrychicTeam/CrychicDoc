package snownee.kiwi.customization.shape;

import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public class ShapeRef implements UnbakedShape {

    private final ResourceLocation id;

    private ShapeGenerator baked;

    public ShapeRef(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation id() {
        return this.id;
    }

    @Override
    public ShapeGenerator bake(BakingContext context) {
        return this.baked;
    }

    @Override
    public Stream<UnbakedShape> dependencies() {
        return Stream.empty();
    }

    public boolean isResolved() {
        return this.baked != null;
    }

    public boolean bindValue(BakingContext context) {
        this.baked = context.getShape(this.id);
        return this.baked != null;
    }
}