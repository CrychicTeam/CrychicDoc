package snownee.kiwi.util;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.loader.Platform;

public class EnumUtil {

    public static final Direction[] DIRECTIONS = Direction.values();

    public static final Direction[] HORIZONTAL_DIRECTIONS = (Direction[]) Arrays.stream(DIRECTIONS).filter($ -> $.getAxis().isHorizontal()).sorted(Comparator.comparingInt(Direction::m_122416_)).toArray(Direction[]::new);

    @OnlyIn(Dist.CLIENT)
    public static Set<RenderType> BLOCK_RENDER_TYPES;

    static {
        if (Platform.isPhysicalClient()) {
            BLOCK_RENDER_TYPES = ImmutableSet.of(RenderType.solid(), RenderType.cutout(), RenderType.cutoutMipped(), RenderType.translucent());
        }
    }
}