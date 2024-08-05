package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;

public class DirectionProperty extends EnumProperty<Direction> {

    protected DirectionProperty(String string0, Collection<Direction> collectionDirection1) {
        super(string0, Direction.class, collectionDirection1);
    }

    public static DirectionProperty create(String string0) {
        return create(string0, (Predicate<Direction>) (p_187558_ -> true));
    }

    public static DirectionProperty create(String string0, Predicate<Direction> predicateDirection1) {
        return create(string0, (Collection<Direction>) Arrays.stream(Direction.values()).filter(predicateDirection1).collect(Collectors.toList()));
    }

    public static DirectionProperty create(String string0, Direction... direction1) {
        return create(string0, Lists.newArrayList(direction1));
    }

    public static DirectionProperty create(String string0, Collection<Direction> collectionDirection1) {
        return new DirectionProperty(string0, collectionDirection1);
    }
}