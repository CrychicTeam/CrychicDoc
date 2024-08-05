package pie.ilikepiefoo.compat.jade;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import pie.ilikepiefoo.compat.jade.builder.ServerDataProviderBuilder;
import pie.ilikepiefoo.compat.jade.builder.ServerExtensionProviderBuilder;
import pie.ilikepiefoo.compat.jade.impl.CustomServerDataProvider;
import pie.ilikepiefoo.compat.jade.impl.CustomServerExtensionProvider;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaCommonRegistration;

public class WailaCommonRegistrationEventJS extends EventJS {

    private final IWailaCommonRegistration registration;

    private final List<Runnable> registrationCallbacks;

    public WailaCommonRegistrationEventJS(IWailaCommonRegistration registration) {
        this.registration = registration;
        this.registrationCallbacks = new ArrayList();
    }

    public IWailaCommonRegistration getRegistration() {
        return this.registration;
    }

    public ServerDataProviderBuilder<BlockAccessor> blockDataProvider(ResourceLocation location, Class<? extends BlockEntity> block) {
        ServerDataProviderBuilder<BlockAccessor> builder = new ServerDataProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerBlockDataProvider(new CustomServerDataProvider<>(builder), block));
        return builder;
    }

    public ServerDataProviderBuilder<EntityAccessor> entityDataProvider(ResourceLocation location, Class<? extends Entity> entity) {
        ServerDataProviderBuilder<EntityAccessor> builder = new ServerDataProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerEntityDataProvider(new CustomServerDataProvider<>(builder), entity));
        return builder;
    }

    public <T> ServerExtensionProviderBuilder<T, ItemStack> itemStorage(ResourceLocation location, Class<? extends T> highestClass) {
        ServerExtensionProviderBuilder<T, ItemStack> builder = new ServerExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerItemStorage(new CustomServerExtensionProvider<>(builder), highestClass));
        return builder;
    }

    public <T> ServerExtensionProviderBuilder<T, CompoundTag> fluidStorage(ResourceLocation location, Class<? extends T> highestClass) {
        ServerExtensionProviderBuilder<T, CompoundTag> builder = new ServerExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerFluidStorage(new CustomServerExtensionProvider<>(builder), highestClass));
        return builder;
    }

    public <T> ServerExtensionProviderBuilder<T, CompoundTag> energyStorage(ResourceLocation location, Class<? extends T> highestClass) {
        ServerExtensionProviderBuilder<T, CompoundTag> builder = new ServerExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerEnergyStorage(new CustomServerExtensionProvider<>(builder), highestClass));
        return builder;
    }

    public <T> ServerExtensionProviderBuilder<T, CompoundTag> progress(ResourceLocation location, Class<? extends T> highestClass) {
        ServerExtensionProviderBuilder<T, CompoundTag> builder = new ServerExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerProgress(new CustomServerExtensionProvider<>(builder), highestClass));
        return builder;
    }

    @HideFromJS
    public void register() {
        this.registrationCallbacks.forEach(Runnable::run);
    }
}