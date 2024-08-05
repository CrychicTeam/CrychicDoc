package snownee.kiwi.customization.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;

public record ConfigureWallShape(float width, float depth, float wallPostHeight, float wallMinY, float wallLowHeight, float wallTallHeight) implements ConfiguringShape {

    public static Codec<ConfigureWallShape> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(Codec.FLOAT.fieldOf("post_width").forGetter(ConfigureWallShape::width), Codec.FLOAT.fieldOf("side_width").forGetter(ConfigureWallShape::depth), Codec.FLOAT.fieldOf("post_max_y").forGetter(ConfigureWallShape::wallPostHeight), Codec.FLOAT.fieldOf("side_min_y").forGetter(ConfigureWallShape::wallMinY), Codec.FLOAT.fieldOf("low_side_max_y").forGetter(ConfigureWallShape::wallLowHeight), Codec.FLOAT.fieldOf("tall_side_max_y").forGetter(ConfigureWallShape::wallTallHeight)).apply(instance, ConfigureWallShape::new));
    }

    @Override
    public void configure(Block block, BlockShapeType type) {
        if (block instanceof WallBlock wallBlock) {
            Map shapes = wallBlock.makeShapes(this.width / 2.0F, this.depth / 2.0F, this.wallPostHeight, this.wallMinY, this.wallLowHeight, this.wallTallHeight);
            switch(type) {
                case MAIN:
                    wallBlock.shapeByIndex = shapes;
                    break;
                case COLLISION:
                    wallBlock.collisionShapeByIndex = shapes;
                    break;
                case INTERACTION:
                    throw new UnsupportedOperationException();
            }
        } else {
            throw new IllegalArgumentException("Block %s is not a WallBlock".formatted(block));
        }
    }
}