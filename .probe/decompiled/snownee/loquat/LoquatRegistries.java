package snownee.loquat;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import snownee.loquat.core.AreaEvent;
import snownee.loquat.core.area.Area;
import snownee.loquat.program.PlaceProgram;
import snownee.loquat.util.RegistryBridge;

public final class LoquatRegistries {

    public static RegistryBridge<Area.Type<?>> AREA;

    public static RegistryBridge<AreaEvent.Type<?>> AREA_EVENT;

    public static RegistryBridge<PlaceProgram.Type<?>> PLACE_PROGRAM;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(LoquatRegistries::newRegistries);
    }

    public static void newRegistries(NewRegistryEvent event) {
        event.create(register("area"), v -> AREA = new RegistryBridge<>(v));
        event.create(register("area_event"), v -> AREA_EVENT = new RegistryBridge<>(v));
        event.create(register("place_program"), v -> PLACE_PROGRAM = new RegistryBridge<>(v));
    }

    private static <T> RegistryBuilder<T> register(String name) {
        return new RegistryBuilder<T>().setName(Loquat.id(name));
    }
}