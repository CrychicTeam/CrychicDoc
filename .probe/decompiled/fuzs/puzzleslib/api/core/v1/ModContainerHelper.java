package fuzs.puzzleslib.api.core.v1;

import java.util.Objects;
import java.util.Optional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

public final class ModContainerHelper {

    private ModContainerHelper() {
    }

    public static IEventBus getActiveModEventBus() {
        return (IEventBus) getOptionalActiveModEventBus().orElseThrow(() -> new NullPointerException("active event bus is null"));
    }

    public static Optional<IEventBus> getOptionalActiveModEventBus() {
        return Optional.ofNullable(FMLJavaModLoadingContext.get()).map(FMLJavaModLoadingContext::getModEventBus);
    }

    public static IEventBus getModEventBus(String modId) {
        return (IEventBus) getOptionalModEventBus(modId).orElseThrow(() -> new NullPointerException("event bus for %s is null".formatted(modId)));
    }

    public static Optional<IEventBus> getOptionalModEventBus(String modId) {
        return getOptionalModContainer(modId).filter(FMLModContainer.class::isInstance).map(FMLModContainer.class::cast).map(FMLModContainer::getEventBus);
    }

    public static net.minecraftforge.fml.ModContainer getModContainer(String modId) {
        return (net.minecraftforge.fml.ModContainer) getOptionalModContainer(modId).orElseThrow(() -> new NullPointerException("mod container for %s is null".formatted(modId)));
    }

    public static Optional<? extends net.minecraftforge.fml.ModContainer> getOptionalModContainer(String modId) {
        ModList modList = ModList.get();
        Objects.requireNonNull(modList, "mod list is null");
        return modList.getModContainerById(modId);
    }

    @Deprecated(forRemoval = true)
    public static Optional<IEventBus> findModEventBus(String modId) {
        return getOptionalModEventBus(modId);
    }

    @Deprecated(forRemoval = true)
    public static net.minecraftforge.fml.ModContainer findModContainer(String modId) {
        return getModContainer(modId);
    }
}