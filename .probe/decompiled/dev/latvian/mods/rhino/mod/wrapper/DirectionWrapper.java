package dev.latvian.mods.rhino.mod.wrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;

public interface DirectionWrapper {

    Direction down = Direction.DOWN;

    Direction up = Direction.UP;

    Direction north = Direction.NORTH;

    Direction south = Direction.SOUTH;

    Direction west = Direction.WEST;

    Direction east = Direction.EAST;

    Direction DOWN = Direction.DOWN;

    Direction UP = Direction.UP;

    Direction NORTH = Direction.NORTH;

    Direction SOUTH = Direction.SOUTH;

    Direction WEST = Direction.WEST;

    Direction EAST = Direction.EAST;

    Map<String, Direction> ALL = Collections.unmodifiableMap((Map) Arrays.stream(Direction.values()).collect(Collectors.toMap(Direction::m_7912_, Function.identity())));
}