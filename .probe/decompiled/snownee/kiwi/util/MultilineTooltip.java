package snownee.kiwi.util;

import java.util.List;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class MultilineTooltip {

    public static Tooltip create(List<Component> components) {
        return create(components, components);
    }

    public static Tooltip create(List<Component> components, @Nullable List<Component> narration) {
        return Tooltip.create(compose(components), narration == null ? null : compose(narration));
    }

    private static Component compose(List<Component> components) {
        if (components.isEmpty()) {
            return Component.empty();
        } else if (components.size() == 1) {
            return (Component) components.get(0);
        } else {
            Component linebreak = Component.literal("\n");
            return (Component) components.stream().skip(1L).reduce(((Component) components.get(0)).copy(), (a, b) -> a.append(linebreak).append(b), (a, b) -> a.append(linebreak).append(b));
        }
    }
}