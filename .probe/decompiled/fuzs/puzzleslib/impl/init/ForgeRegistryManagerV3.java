package fuzs.puzzleslib.impl.init;

import com.google.common.collect.Maps;
import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.init.v2.builder.ExtendedMenuSupplier;
import fuzs.puzzleslib.api.init.v3.RegistryHelper;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ObjectHolderRegistry;
import org.jetbrains.annotations.Nullable;

public final class ForgeRegistryManagerV3 extends RegistryManagerV3Impl {

    @Nullable
    private final IEventBus eventBus;

    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registers = Maps.newIdentityHashMap();

    public ForgeRegistryManagerV3(String modId) {
        super(modId);
        this.eventBus = (IEventBus) ModContainerHelper.getOptionalModEventBus(modId).orElse(null);
    }

    @Override
    public <T> Holder.Reference<T> getHolder(ResourceKey<? extends Registry<? super T>> registryKey, String path) {
        Registry<T> registry = RegistryHelper.findBuiltInRegistry(registryKey);
        ResourceKey<T> resourceKey = this.makeResourceKey(registryKey, path);
        Holder.Reference<T> holder = Holder.Reference.createStandAlone(registry.asLookup(), resourceKey);
        ObjectHolderRegistry.addHandler(predicate -> {
            if (predicate.test(registryKey.location()) && !holder.isBound()) {
                T value = registry.get(resourceKey);
                Objects.requireNonNull(value, "value is null");
                holder.bindValue(value);
            }
        });
        MinecraftForge.EVENT_BUS.addListener(evt -> holder.bindTags(registry.getHolder(resourceKey).stream().flatMap(Holder.Reference::m_203616_).toList()));
        if (ModLoaderEnvironment.INSTANCE.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(evt -> {
                if (!evt.getConnection().isMemoryConnection()) {
                    holder.bindTags(Set.of());
                }
            });
        }
        return holder;
    }

    @Override
    protected <T> Holder.Reference<T> _register(ResourceKey<? extends Registry<? super T>> registryKey, String path, Supplier<T> supplier) {
        DeferredRegister<T> register = (DeferredRegister<T>) this.registers.computeIfAbsent(registryKey, $ -> {
            DeferredRegister<T> deferredRegister = DeferredRegister.create((ResourceKey<? extends Registry<T>>) registryKey, this.modId);
            Objects.requireNonNull(this.eventBus, "mod event bus for %s is null".formatted(this.modId));
            deferredRegister.register(this.eventBus);
            return deferredRegister;
        });
        register.register(path, () -> {
            T value = (T) supplier.get();
            Objects.requireNonNull(value, "value is null");
            return value;
        });
        return this.getHolder(registryKey, path);
    }

    @Override
    public Holder.Reference<Item> registerSpawnEggItem(Holder.Reference<? extends EntityType<? extends Mob>> entityTypeReference, int backgroundColor, int highlightColor, Item.Properties itemProperties) {
        return this.registerItem(entityTypeReference.key().location().getPath() + "_spawn_egg", () -> new ForgeSpawnEggItem(entityTypeReference, backgroundColor, highlightColor, itemProperties));
    }

    @Override
    public <T extends AbstractContainerMenu> Holder.Reference<MenuType<T>> registerExtendedMenuType(String path, Supplier<ExtendedMenuSupplier<T>> entry) {
        return this.register(Registries.MENU, path, () -> IForgeMenuType.create(((ExtendedMenuSupplier) entry.get())::create));
    }

    @Override
    public Holder.Reference<PoiType> registerPoiType(String path, Supplier<Set<BlockState>> matchingStates, int maxTickets, int validRange) {
        return this.register(Registries.POINT_OF_INTEREST_TYPE, path, () -> new PoiType((Set<BlockState>) matchingStates.get(), maxTickets, validRange));
    }
}