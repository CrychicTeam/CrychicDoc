package snownee.kiwi.customization.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.phys.shapes.VoxelShape;

public record ConfigureCrossCollisionShape(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) implements ConfiguringShape {

    public static Codec<ConfigureCrossCollisionShape> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(Codec.FLOAT.fieldOf("post_width").forGetter(ConfigureCrossCollisionShape::nodeWidth), Codec.FLOAT.fieldOf("side_width").forGetter(ConfigureCrossCollisionShape::extensionWidth), Codec.FLOAT.fieldOf("post_max_y").forGetter(ConfigureCrossCollisionShape::nodeHeight), Codec.FLOAT.fieldOf("side_min_y").forGetter(ConfigureCrossCollisionShape::extensionBottom), Codec.FLOAT.fieldOf("side_max_y").forGetter(ConfigureCrossCollisionShape::extensionHeight)).apply(instance, ConfigureCrossCollisionShape::new));
    }

    @Override
    public void configure(Block block, BlockShapeType type) {
        if (block instanceof CrossCollisionBlock crossCollisionBlock) {
            VoxelShape[] shapes = crossCollisionBlock.makeShapes(this.nodeWidth / 2.0F, this.extensionWidth / 2.0F, this.nodeHeight, this.extensionBottom, this.extensionHeight);
            switch(type) {
                case MAIN:
                    crossCollisionBlock.shapeByIndex = shapes;
                    break;
                case COLLISION:
                    crossCollisionBlock.collisionShapeByIndex = shapes;
                    break;
                case INTERACTION:
                    throw new UnsupportedOperationException();
            }
        } else {
            throw new IllegalArgumentException("Block %s is not a CrossCollisionBlock".formatted(block));
        }
    }
}