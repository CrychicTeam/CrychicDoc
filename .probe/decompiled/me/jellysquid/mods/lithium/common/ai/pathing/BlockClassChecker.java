package me.jellysquid.mods.lithium.common.ai.pathing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import me.jellysquid.mods.lithium.common.reflection.ReflectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;

public class BlockClassChecker {

    private static final Map<Class<?>, Boolean> DYNAMIC_TYPE_CACHE = new ConcurrentHashMap();

    private static final Function<Class<?>, Boolean> DYNAMIC_TYPE_CHECKER = hasNonstandardImplementation("getBlockPathType", BlockState.class, BlockGetter.class, BlockPos.class, Mob.class);

    private static final Map<Class<?>, Boolean> DYNAMIC_FIRE_CACHE = new ConcurrentHashMap();

    private static final Function<Class<?>, Boolean> DYNAMIC_FIRE_CHECKER = hasNonstandardImplementation("isBurning", BlockState.class, BlockGetter.class, BlockPos.class);

    public static boolean shouldUseDynamicTypeCheck(Class<?> blockClass) {
        return (Boolean) DYNAMIC_TYPE_CACHE.computeIfAbsent(blockClass, DYNAMIC_TYPE_CHECKER);
    }

    public static boolean shouldUseDynamicBurningCheck(Class<?> blockClass) {
        return (Boolean) DYNAMIC_FIRE_CACHE.computeIfAbsent(blockClass, DYNAMIC_FIRE_CHECKER);
    }

    private static Function<Class<?>, Boolean> hasNonstandardImplementation(String name, Class<?>... args) {
        return blockClass -> ReflectionUtil.hasMethodOverride(blockClass, IForgeBlock.class, false, name, args);
    }
}