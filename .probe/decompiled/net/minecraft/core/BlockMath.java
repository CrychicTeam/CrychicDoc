package net.minecraft.core;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.Util;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;

public class BlockMath {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL = Util.make(Maps.newEnumMap(Direction.class), p_121851_ -> {
        p_121851_.put(Direction.SOUTH, Transformation.identity());
        p_121851_.put(Direction.EAST, new Transformation(null, new Quaternionf().rotateY((float) (Math.PI / 2)), null, null));
        p_121851_.put(Direction.WEST, new Transformation(null, new Quaternionf().rotateY((float) (-Math.PI / 2)), null, null));
        p_121851_.put(Direction.NORTH, new Transformation(null, new Quaternionf().rotateY((float) Math.PI), null, null));
        p_121851_.put(Direction.UP, new Transformation(null, new Quaternionf().rotateX((float) (-Math.PI / 2)), null, null));
        p_121851_.put(Direction.DOWN, new Transformation(null, new Quaternionf().rotateX((float) (Math.PI / 2)), null, null));
    });

    public static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL = Util.make(Maps.newEnumMap(Direction.class), p_121849_ -> {
        for (Direction $$1 : Direction.values()) {
            p_121849_.put($$1, ((Transformation) VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL.get($$1)).inverse());
        }
    });

    public static Transformation blockCenterToCorner(Transformation transformation0) {
        Matrix4f $$1 = new Matrix4f().translation(0.5F, 0.5F, 0.5F);
        $$1.mul(transformation0.getMatrix());
        $$1.translate(-0.5F, -0.5F, -0.5F);
        return new Transformation($$1);
    }

    public static Transformation blockCornerToCenter(Transformation transformation0) {
        Matrix4f $$1 = new Matrix4f().translation(-0.5F, -0.5F, -0.5F);
        $$1.mul(transformation0.getMatrix());
        $$1.translate(0.5F, 0.5F, 0.5F);
        return new Transformation($$1);
    }

    public static Transformation getUVLockTransform(Transformation transformation0, Direction direction1, Supplier<String> supplierString2) {
        Direction $$3 = Direction.rotate(transformation0.getMatrix(), direction1);
        Transformation $$4 = transformation0.inverse();
        if ($$4 == null) {
            LOGGER.warn((String) supplierString2.get());
            return new Transformation(null, null, new Vector3f(0.0F, 0.0F, 0.0F), null);
        } else {
            Transformation $$5 = ((Transformation) VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL.get(direction1)).compose($$4).compose((Transformation) VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL.get($$3));
            return blockCenterToCorner($$5);
        }
    }
}