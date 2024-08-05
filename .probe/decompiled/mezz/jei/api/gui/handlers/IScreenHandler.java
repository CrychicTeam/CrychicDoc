package mezz.jei.api.gui.handlers;

import java.util.function.Function;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IScreenHandler<T extends Screen> extends Function<T, IGuiProperties> {

    @Nullable
    IGuiProperties apply(T var1);
}