package com.cupboard.util;

import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class BlockSearch {

    final BiPredicate<BlockGetter, BlockPos> DOUBLE_AIR = (world, pos) -> world.getBlockState(pos).m_60795_() && world.getBlockState(pos.above()).m_60795_();

    final BiPredicate<BlockGetter, BlockPos> DOUBLE_AIR_GROUND = this.DOUBLE_AIR.and((world, pos) -> world.getBlockState(pos.below()).m_280296_());

    public static BlockPos findAround(BlockGetter world, BlockPos start, int verticalRange, int horizontalRange, int yStep, BiPredicate<BlockGetter, BlockPos> predicate) {
        if (horizontalRange < 1 && verticalRange < 1) {
            return null;
        } else {
            BlockPos.MutableBlockPos temp = new BlockPos.MutableBlockPos();
            int y = 0;
            int y_offset = yStep;
            boolean checkLoaded = world instanceof Level;
            Level level = checkLoaded ? (Level) world : null;
            for (int i = 0; i < verticalRange + 2; i++) {
                for (int steps = 1; steps <= horizontalRange; steps++) {
                    temp.set(start.m_123341_() - steps, start.m_123342_() + y, start.m_123343_() - steps);
                    for (int x = 0; x <= steps; x++) {
                        temp.set(temp.m_123341_() + 1, temp.m_123342_(), temp.m_123343_());
                        if (checkLoaded) {
                            if (level.m_7232_(temp.m_123341_() >> 4, temp.m_123343_() >> 4) && predicate.test(world, temp)) {
                                return temp;
                            }
                        } else if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                    for (int z = 0; z <= steps; z++) {
                        temp.set(temp.m_123341_(), temp.m_123342_(), temp.m_123343_() + 1);
                        if (checkLoaded) {
                            if (level.m_7232_(temp.m_123341_() >> 4, temp.m_123343_() >> 4) && predicate.test(world, temp)) {
                                return temp;
                            }
                        } else if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                    for (int xx = 0; xx <= steps; xx++) {
                        temp.set(temp.m_123341_() - 1, temp.m_123342_(), temp.m_123343_());
                        if (checkLoaded) {
                            if (level.m_7232_(temp.m_123341_() >> 4, temp.m_123343_() >> 4) && predicate.test(world, temp)) {
                                return temp;
                            }
                        } else if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                    for (int zx = 0; zx <= steps; zx++) {
                        temp.set(temp.m_123341_(), temp.m_123342_(), temp.m_123343_() - 1);
                        if (checkLoaded) {
                            if (level.m_7232_(temp.m_123341_() >> 4, temp.m_123343_() >> 4) && predicate.test(world, temp)) {
                                return temp;
                            }
                        } else if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                }
                y += y_offset;
                y_offset = y_offset > 0 ? y_offset + yStep : y_offset - yStep;
                y_offset *= -1;
            }
            return null;
        }
    }
}