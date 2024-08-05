package net.minecraftforge.common.extensions;

import com.google.common.base.Preconditions;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;

public interface IForgeFriendlyByteBuf {

    private FriendlyByteBuf self() {
        return (FriendlyByteBuf) this;
    }

    default <T> void writeRegistryIdUnsafe(@NotNull IForgeRegistry<T> registry, @NotNull T entry) {
        ForgeRegistry<T> forgeRegistry = (ForgeRegistry<T>) registry;
        int id = forgeRegistry.getID(entry);
        this.self().writeVarInt(id);
    }

    default void writeRegistryIdUnsafe(@NotNull IForgeRegistry<?> registry, @NotNull ResourceLocation entryKey) {
        ForgeRegistry<?> forgeRegistry = (ForgeRegistry<?>) registry;
        int id = forgeRegistry.getID(entryKey);
        this.self().writeVarInt(id);
    }

    default <T> T readRegistryIdUnsafe(@NotNull IForgeRegistry<T> registry) {
        ForgeRegistry<T> forgeRegistry = (ForgeRegistry<T>) registry;
        int id = this.self().readVarInt();
        return forgeRegistry.getValue(id);
    }

    default <T> void writeRegistryId(@NotNull IForgeRegistry<T> registry, @NotNull T entry) {
        Objects.requireNonNull(registry, "Cannot write a null registry key!");
        Objects.requireNonNull(entry, "Cannot write a null registry entry!");
        ResourceLocation name = registry.getRegistryName();
        Preconditions.checkArgument(registry.containsValue(entry), "Cannot find %s in %s", registry.getKey(entry) != null ? registry.getKey(entry) : entry, name);
        ForgeRegistry<T> reg = (ForgeRegistry<T>) registry;
        this.self().writeResourceLocation(name);
        this.self().writeVarInt(reg.getID(entry));
    }

    default <T> T readRegistryId() {
        ResourceLocation location = this.self().readResourceLocation();
        ForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(location);
        return registry.getValue(this.self().readVarInt());
    }

    default <T> T readRegistryIdSafe(Class<? super T> registrySuperType) {
        T value = this.readRegistryId();
        if (!registrySuperType.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Attempted to read an registryValue of the wrong type from the Buffer!");
        } else {
            return value;
        }
    }

    default void writeFluidStack(FluidStack stack) {
        if (stack.isEmpty()) {
            this.self().writeBoolean(false);
        } else {
            this.self().writeBoolean(true);
            stack.writeToPacket(this.self());
        }
    }

    default FluidStack readFluidStack() {
        return !this.self().readBoolean() ? FluidStack.EMPTY : FluidStack.readFromPacket(this.self());
    }
}