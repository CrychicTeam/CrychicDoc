package fuzs.puzzleslib.impl.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ConfigDataHolder;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.jetbrains.annotations.Nullable;

class ConfigDataHolderImpl<T extends ConfigCore> implements ConfigDataHolder<T>, ValueCallback {

    final T config;

    private final Supplier<T> defaultConfigSupplier;

    private final Type configType;

    @Nullable
    private ModConfig modConfig;

    @Nullable
    private T defaultConfig;

    UnaryOperator<String> fileName;

    private final List<Consumer<T>> additionalCallbacks = Lists.newArrayList();

    private List<Runnable> configValueCallbacks = Lists.newArrayList();

    private boolean available;

    protected ConfigDataHolderImpl(Type configType, Supplier<T> supplier) {
        this.configType = configType;
        this.config = (T) supplier.get();
        this.defaultConfigSupplier = supplier;
        this.fileName = modId -> ConfigHolder.defaultName(modId, configType.extension());
    }

    @Override
    public T getConfig() {
        Objects.requireNonNull(this.config, "config is null");
        return this.isAvailable() ? this.config : this.getOrCreateDefaultConfig();
    }

    private T getOrCreateDefaultConfig() {
        if (this.defaultConfig == null) {
            this.testAvailable();
            this.defaultConfig = (T) this.defaultConfigSupplier.get();
            Objects.requireNonNull(this.defaultConfig, "default config is null");
            this.defaultConfig.afterConfigReload();
            for (Consumer<T> callback : this.additionalCallbacks) {
                callback.accept(this.defaultConfig);
            }
        }
        return this.defaultConfig;
    }

    @Override
    public boolean isAvailable() {
        return this.findErrorMessage().left().isPresent();
    }

    @Override
    public void accept(Consumer<T> callback) {
        this.additionalCallbacks.add(callback);
    }

    @Override
    public <S, V extends ForgeConfigSpec.ConfigValue<S>> V accept(V entry, Consumer<S> save) {
        Objects.requireNonNull(entry, "entry is null");
        this.acceptValueCallback(() -> save.accept(entry.get()));
        return entry;
    }

    void acceptValueCallback(Runnable runnable) {
        this.configValueCallbacks.add(runnable);
    }

    private void testAvailable() {
        this.findErrorMessage().ifRight(message -> PuzzlesLib.LOGGER.error("Calling {} config when it is not yet available! This is a bug! Message: {}", new Object[] { this.configType.extension(), message, new Exception("Config not yet available") }));
    }

    private Either<Unit, String> findErrorMessage() {
        if (this.modConfig == null) {
            return Either.right("Mod config instance is missing");
        } else if (this.modConfig.getConfigData() == null) {
            return Either.right("Config data is missing");
        } else {
            return !this.available ? Either.right("Config callbacks have not been loaded") : Either.left(Unit.INSTANCE);
        }
    }

    void onModConfig(ModConfig config, boolean reloading) {
        Objects.requireNonNull(this.config, "Attempting to register invalid config of type %s for mod id %s".formatted(this.configType.extension(), config.getModId()));
        if (config.getType() == this.configType && (this.modConfig == null || config == this.modConfig)) {
            String loading;
            if (config.getConfigData() != null) {
                loading = reloading ? "Reloading" : "Loading";
                this.configValueCallbacks.forEach(Runnable::run);
                this.available = true;
                for (Consumer<T> callback : this.additionalCallbacks) {
                    callback.accept(this.config);
                }
            } else {
                loading = "Unloading";
                this.available = false;
            }
            PuzzlesLib.LOGGER.info("{} {} config for {}", new Object[] { loading, config.getType().extension(), config.getModId() });
        }
    }

    void register(ConfigDataHolderImpl.ModConfigFactory factory) {
        Objects.requireNonNull(this.config, "Attempting to register invalid config of type %s".formatted(this.configType.extension()));
        if (this.modConfig != null) {
            throw new IllegalStateException(String.format("Config for type %s has already been registered!", this.configType));
        } else {
            this.modConfig = factory.createAndRegister(this.configType, this.buildSpec(), this.fileName);
        }
    }

    private ForgeConfigSpec buildSpec() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        AnnotatedConfigBuilder.serialize(builder, this, this.config);
        this.configValueCallbacks = ImmutableList.copyOf(this.configValueCallbacks);
        return builder.build();
    }

    interface ModConfigFactory {

        ModConfig createAndRegister(Type var1, ForgeConfigSpec var2, UnaryOperator<String> var3);
    }
}