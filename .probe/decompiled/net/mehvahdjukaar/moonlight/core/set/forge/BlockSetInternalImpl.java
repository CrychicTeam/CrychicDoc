package net.mehvahdjukaar.moonlight.core.set.forge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.BlockTypeRegistry;
import net.mehvahdjukaar.moonlight.core.set.BlockSetInternal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

public class BlockSetInternalImpl {

    private static final Map<String, List<Runnable>> LATE_REGISTRATION_QUEUE = new ConcurrentHashMap();

    private static boolean hasFilledBlockSets = false;

    public static <T extends BlockType, E> void addDynamicRegistration(BlockSetAPI.BlockTypeRegistryCallback<E, T> registrationFunction, Class<T> blockType, Registry<E> registry) {
        if (registry == BuiltInRegistries.BLOCK) {
            addEvent(ForgeRegistries.BLOCKS, registrationFunction, blockType);
        } else if (registry == BuiltInRegistries.ITEM) {
            addEvent(ForgeRegistries.ITEMS, registrationFunction, blockType);
        } else {
            if (registry == BuiltInRegistries.FLUID || registry == BuiltInRegistries.SOUND_EVENT) {
                throw new IllegalArgumentException("Fluid and Sound Events registry not supported here");
            }
            getOrAddQueue();
            RegHelper.registerInBatch(registry, e -> registrationFunction.accept(e, BlockSetAPI.<T>getBlockSet(blockType).getValues()));
        }
    }

    public static <T extends BlockType, E> void addEvent(IForgeRegistry<E> reg, BlockSetAPI.BlockTypeRegistryCallback<E, T> registrationFunction, Class<T> blockType) {
        List<Runnable> registrationQueues = getOrAddQueue();
        Consumer<RegisterEvent> eventConsumer = e -> {
            if (e.getRegistryKey().equals(reg.getRegistryKey())) {
                Runnable lateRegistration = () -> {
                    IForgeRegistry<E> registry = e.getForgeRegistry();
                    if (registry instanceof ForgeRegistry<?> fr) {
                        boolean frozen = fr.isLocked();
                        fr.unfreeze();
                        registrationFunction.accept(registry::register, BlockSetAPI.<T>getBlockSet(blockType).getValues());
                        if (frozen) {
                            fr.freeze();
                        }
                    }
                };
                registrationQueues.add(lateRegistration);
            }
        };
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(EventPriority.HIGHEST, eventConsumer);
    }

    @NotNull
    private static List<Runnable> getOrAddQueue() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        String modId = ModLoadingContext.get().getActiveContainer().getModId();
        return (List<Runnable>) LATE_REGISTRATION_QUEUE.computeIfAbsent(modId, s -> {
            bus.addListener(EventPriority.HIGHEST, BlockSetInternalImpl::registerLateBlockAndItems);
            return new ArrayList();
        });
    }

    protected static void registerLateBlockAndItems(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.ATTRIBUTES.getRegistryKey()) && !hasFilledBlockSets) {
            BlockSetInternal.initializeBlockSets();
            hasFilledBlockSets = true;
        }
        if (event.getRegistryKey().equals(ForgeRegistries.ENTITY_TYPES.getRegistryKey())) {
            BlockSetInternal.getRegistries().forEach(BlockTypeRegistry::onItemInit);
            if (!hasFilledBlockSets) {
                BlockSetInternal.initializeBlockSets();
                hasFilledBlockSets = true;
            }
            String modId = ModLoadingContext.get().getActiveContainer().getModId();
            List<Runnable> registrationQueues = (List<Runnable>) LATE_REGISTRATION_QUEUE.get(modId);
            if (registrationQueues != null) {
                registrationQueues.forEach(Runnable::run);
            }
            LATE_REGISTRATION_QUEUE.remove(modId);
        }
    }

    public static boolean hasFilledBlockSets() {
        return hasFilledBlockSets;
    }
}