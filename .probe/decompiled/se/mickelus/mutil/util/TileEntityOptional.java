package se.mickelus.mutil.util;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@ParametersAreNonnullByDefault
public class TileEntityOptional {

    public static <T> Optional<T> from(BlockGetter world, BlockPos pos, Class<T> tileEntityClass) {
        return CastOptional.cast(world.getBlockEntity(pos), tileEntityClass);
    }
}