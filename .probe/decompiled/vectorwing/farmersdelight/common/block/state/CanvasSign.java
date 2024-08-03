package vectorwing.farmersdelight.common.block.state;

import javax.annotation.Nullable;
import net.minecraft.world.item.DyeColor;
import vectorwing.farmersdelight.common.Configuration;

public interface CanvasSign {

    @Nullable
    DyeColor getBackgroundColor();

    default boolean isDarkBackground() {
        DyeColor backgroundDye = this.getBackgroundColor();
        return backgroundDye != null && Configuration.CANVAS_SIGN_DARK_BACKGROUND_LIST.get().contains(backgroundDye.getName());
    }
}