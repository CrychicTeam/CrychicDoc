package io.github.lightman314.lightmanscurrency.client.gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

public class TooltipUtil {

    @Nullable
    public static List<Component> lazyList(Component entry) {
        if (entry == null) {
            return null;
        } else {
            List<Component> list = new ArrayList();
            list.add(entry);
            return list;
        }
    }

    @NotNull
    public static Supplier<List<Component>> createToggleTooltip(@NotNull NonNullSupplier<Boolean> toggle, List<Component> trueTooltip, List<Component> falseTooltip) {
        return () -> toggle.get() ? trueTooltip : falseTooltip;
    }

    @NotNull
    public static Supplier<List<Component>> createToggleSingleTooltip(@NotNull NonNullSupplier<Boolean> toggle, Component trueTooltip, Component falseTooltip) {
        return () -> toggle.get() ? lazyList(trueTooltip) : lazyList(falseTooltip);
    }

    @NotNull
    public static Supplier<List<Component>> createToggleTooltip(@NotNull NonNullSupplier<Boolean> toggle, Supplier<List<Component>> trueTooltip, Supplier<List<Component>> falseTooltip) {
        return () -> toggle.get() ? (List) trueTooltip.get() : (List) falseTooltip.get();
    }

    @NotNull
    public static Supplier<List<Component>> createToggleSingleTooltip(@NotNull NonNullSupplier<Boolean> toggle, Supplier<Component> trueTooltip, Supplier<Component> falseTooltip) {
        return () -> toggle.get() ? lazyList((Component) trueTooltip.get()) : lazyList((Component) falseTooltip.get());
    }
}