package pie.ilikepiefoo.compat.jade;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import pie.ilikepiefoo.compat.jade.builder.BlockComponentProviderBuilder;
import pie.ilikepiefoo.compat.jade.builder.ClientExtensionProviderBuilder;
import pie.ilikepiefoo.compat.jade.builder.EntityComponentProviderBuilder;
import pie.ilikepiefoo.compat.jade.impl.CustomBlockComponentProvider;
import pie.ilikepiefoo.compat.jade.impl.CustomClientExtensionProvider;
import pie.ilikepiefoo.compat.jade.impl.CustomEntityComponentProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.callback.JadeAfterRenderCallback;
import snownee.jade.api.callback.JadeBeforeRenderCallback;
import snownee.jade.api.callback.JadeItemModNameCallback;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.callback.JadeRenderBackgroundCallback;
import snownee.jade.api.callback.JadeTooltipCollectedCallback;
import snownee.jade.api.platform.CustomEnchantPower;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ProgressView;

public class WailaClientRegistrationEventJS extends EventJS {

    private final IWailaClientRegistration registration;

    private final List<Runnable> registrationCallbacks;

    public WailaClientRegistrationEventJS(IWailaClientRegistration registration) {
        this.registration = registration;
        this.registrationCallbacks = new ArrayList();
    }

    public void addConfig(ResourceLocation key, boolean defaultValue) {
        this.registration.addConfig(key, defaultValue);
    }

    public void addConfig(ResourceLocation key, Enum<?> defaultValue) {
        this.registration.addConfig(key, defaultValue);
    }

    public void addConfig(ResourceLocation key, String defaultValue, Predicate<String> validator) {
        this.registration.addConfig(key, defaultValue, validator);
    }

    public void addConfig(ResourceLocation key, int defaultValue, int min, int max, boolean slider) {
        this.registration.addConfig(key, defaultValue, min, max, slider);
    }

    public void addConfig(ResourceLocation key, float defaultValue, float min, float max, boolean slider) {
        this.registration.addConfig(key, defaultValue, min, max, slider);
    }

    public void addConfigListener(ResourceLocation key, Consumer<ResourceLocation> listener) {
        this.registration.addConfigListener(key, listener);
    }

    public BlockComponentProviderBuilder block(ResourceLocation location, Class<? extends Block> block) {
        BlockComponentProviderBuilder builder = new BlockComponentProviderBuilder(location);
        this.registrationCallbacks.add((Runnable) () -> {
            CustomBlockComponentProvider provider = new CustomBlockComponentProvider(builder);
            if (builder.getIconRetriever() != null) {
                this.registration.registerBlockIcon(provider, block);
            }
            if (builder.getTooltipRetriever() != null) {
                this.registration.registerBlockComponent(provider, block);
            }
        });
        return builder;
    }

    public EntityComponentProviderBuilder entity(ResourceLocation location, Class<? extends Entity> entity) {
        EntityComponentProviderBuilder builder = new EntityComponentProviderBuilder(location);
        this.registrationCallbacks.add((Runnable) () -> {
            CustomEntityComponentProvider provider = new CustomEntityComponentProvider(builder);
            if (builder.getIconRetriever() != null) {
                this.registration.registerEntityIcon(provider, entity);
            }
            if (builder.getTooltipRetriever() != null) {
                this.registration.registerEntityComponent(provider, entity);
            }
        });
        return builder;
    }

    public ClientExtensionProviderBuilder<ItemStack, ItemView> itemStorage(ResourceLocation location) {
        ClientExtensionProviderBuilder<ItemStack, ItemView> builder = new ClientExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerItemStorageClient(new CustomClientExtensionProvider<>(builder)));
        return builder;
    }

    public ClientExtensionProviderBuilder<CompoundTag, FluidView> fluidStorage(ResourceLocation location) {
        ClientExtensionProviderBuilder<CompoundTag, FluidView> builder = new ClientExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerFluidStorageClient(new CustomClientExtensionProvider<>(builder)));
        return builder;
    }

    public ClientExtensionProviderBuilder<CompoundTag, EnergyView> energyStorage(ResourceLocation location) {
        ClientExtensionProviderBuilder<CompoundTag, EnergyView> builder = new ClientExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerEnergyStorageClient(new CustomClientExtensionProvider<>(builder)));
        return builder;
    }

    public ClientExtensionProviderBuilder<CompoundTag, ProgressView> progress(ResourceLocation location) {
        ClientExtensionProviderBuilder<CompoundTag, ProgressView> builder = new ClientExtensionProviderBuilder<>(location);
        this.registrationCallbacks.add((Runnable) () -> this.registration.registerProgressClient(new CustomClientExtensionProvider<>(builder)));
        return builder;
    }

    public void registerBlockIcon(IBlockComponentProvider provider, Class<? extends Block> block) {
        this.registration.registerBlockIcon(provider, block);
    }

    public void registerBlockComponent(IBlockComponentProvider provider, Class<? extends Block> block) {
        this.registration.registerBlockComponent(provider, block);
    }

    public void registerEntityIcon(IEntityComponentProvider provider, Class<? extends Entity> entity) {
        this.registration.registerEntityIcon(provider, entity);
    }

    public void registerEntityComponent(IEntityComponentProvider provider, Class<? extends Entity> entity) {
        this.registration.registerEntityComponent(provider, entity);
    }

    public void hideTarget(Block block) {
        this.registration.hideTarget(block);
    }

    public void hideTarget(EntityType<?> entityType) {
        this.registration.hideTarget(entityType);
    }

    public void usePickedResult(Block block) {
        this.registration.usePickedResult(block);
    }

    public void usePickedResult(EntityType<?> entityType) {
        this.registration.usePickedResult(entityType);
    }

    public BlockAccessor.Builder blockAccessor() {
        return this.registration.blockAccessor();
    }

    public EntityAccessor.Builder entityAccessor() {
        return this.registration.entityAccessor();
    }

    public boolean shouldHide(Entity target) {
        return this.registration.shouldHide(target);
    }

    public boolean shouldHide(BlockState state) {
        return this.registration.shouldHide(state);
    }

    public boolean shouldPick(Entity entity) {
        return this.registration.shouldPick(entity);
    }

    public boolean shouldPick(BlockState blockState) {
        return this.registration.shouldPick(blockState);
    }

    public void addAfterRenderCallback(JadeAfterRenderCallback callback) {
        this.registration.addAfterRenderCallback(callback);
    }

    public void addAfterRenderCallback(int priority, JadeAfterRenderCallback callback) {
        this.registration.addAfterRenderCallback(priority, callback);
    }

    public void addBeforeRenderCallback(JadeBeforeRenderCallback callback) {
        this.registration.addBeforeRenderCallback(callback);
    }

    public void addBeforeRenderCallback(int priority, JadeBeforeRenderCallback callback) {
        this.registration.addBeforeRenderCallback(priority, callback);
    }

    public void addRayTraceCallback(JadeRayTraceCallback callback) {
        this.registration.addRayTraceCallback(callback);
    }

    public void addRayTraceCallback(int priority, JadeRayTraceCallback callback) {
        this.registration.addRayTraceCallback(priority, callback);
    }

    public void addTooltipCollectedCallback(JadeTooltipCollectedCallback callback) {
        this.registration.addTooltipCollectedCallback(callback);
    }

    public void addTooltipCollectedCallback(int priority, JadeTooltipCollectedCallback callback) {
        this.registration.addTooltipCollectedCallback(priority, callback);
    }

    public void addItemModNameCallback(JadeItemModNameCallback callback) {
        this.registration.addItemModNameCallback(callback);
    }

    public void addItemModNameCallback(int priority, JadeItemModNameCallback callback) {
        this.registration.addItemModNameCallback(priority, callback);
    }

    public void addRenderBackgroundCallback(JadeRenderBackgroundCallback callback) {
        this.registration.addRenderBackgroundCallback(callback);
    }

    public void addRenderBackgroundCallback(int priority, JadeRenderBackgroundCallback callback) {
        this.registration.addRenderBackgroundCallback(priority, callback);
    }

    public Screen createPluginConfigScreen(@Nullable Screen parent, @Nullable String namespace) {
        return this.registration.createPluginConfigScreen(parent, namespace);
    }

    public void registerItemStorageClient(IClientExtensionProvider<ItemStack, ItemView> provider) {
        this.registration.registerItemStorageClient(provider);
    }

    public void registerFluidStorageClient(IClientExtensionProvider<CompoundTag, FluidView> provider) {
        this.registration.registerFluidStorageClient(provider);
    }

    public void registerEnergyStorageClient(IClientExtensionProvider<CompoundTag, EnergyView> provider) {
        this.registration.registerEnergyStorageClient(provider);
    }

    public void registerProgressClient(IClientExtensionProvider<CompoundTag, ProgressView> provider) {
        this.registration.registerProgressClient(provider);
    }

    public boolean isServerConnected() {
        return this.registration.isServerConnected();
    }

    public boolean isShowDetailsPressed() {
        return this.registration.isShowDetailsPressed();
    }

    public boolean maybeLowVisionUser() {
        return this.registration.maybeLowVisionUser();
    }

    public CompoundTag getServerData() {
        return this.registration.getServerData();
    }

    public void setServerData(CompoundTag tag) {
        this.registration.setServerData(tag);
    }

    public ItemStack getBlockCamouflage(LevelAccessor level, BlockPos pos) {
        return this.registration.getBlockCamouflage(level, pos);
    }

    public void markAsClientFeature(ResourceLocation uid) {
        this.registration.markAsClientFeature(uid);
    }

    public void markAsServerFeature(ResourceLocation uid) {
        this.registration.markAsServerFeature(uid);
    }

    public boolean isClientFeature(ResourceLocation uid) {
        return this.registration.isClientFeature(uid);
    }

    public <T extends Accessor<?>> void registerAccessorHandler(Class<T> clazz, Accessor.ClientHandler<T> handler) {
        this.registration.registerAccessorHandler(clazz, handler);
    }

    public Accessor.ClientHandler<Accessor<?>> getAccessorHandler(Class<? extends Accessor<?>> clazz) {
        return this.registration.getAccessorHandler(clazz);
    }

    public void registerCustomEnchantPower(Block block, CustomEnchantPower customEnchantPower) {
        this.registration.registerCustomEnchantPower(block, customEnchantPower);
    }

    @HideFromJS
    public void register() {
        this.registrationCallbacks.forEach(Runnable::run);
    }
}