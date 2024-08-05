package fr.frinn.custommachinery.client.screen.creation.appearance;

import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class AppearancePropertyBuilderRegistry {

    private static Map<MachineAppearanceProperty<?>, IAppearancePropertyBuilder<?>> appearanceBuilders;

    public static void init() {
        RegisterAppearancePropertyBuilderEvent event = new RegisterAppearancePropertyBuilderEvent();
        RegisterAppearancePropertyBuilderEvent.EVENT.invoker().registerAppearancePropertyBuilders(event);
        appearanceBuilders = event.getBuilders();
    }

    @Nullable
    public static <T> IAppearancePropertyBuilder<T> getBuilder(MachineAppearanceProperty<T> property) {
        return (IAppearancePropertyBuilder<T>) appearanceBuilders.get(property);
    }
}