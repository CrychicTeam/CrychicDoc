package dev.shadowsoffire.placebo.reload;

import dev.shadowsoffire.placebo.codec.CodecProvider;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface RegistryCallback<R extends CodecProvider<? super R>> {

    void beginReload(DynamicRegistry<R> var1);

    void onReload(DynamicRegistry<R> var1);

    static <R extends CodecProvider<? super R>> RegistryCallback<R> create(Consumer<DynamicRegistry<R>> beginReload, Consumer<DynamicRegistry<R>> onReload) {
        return new RegistryCallback.Delegated<>(beginReload, onReload);
    }

    static <R extends CodecProvider<? super R>> RegistryCallback<R> beginOnly(Consumer<DynamicRegistry<R>> beginReload) {
        return new RegistryCallback.Delegated<>(beginReload, v -> {
        });
    }

    static <R extends CodecProvider<? super R>> RegistryCallback<R> reloadOnly(Consumer<DynamicRegistry<R>> onReload) {
        return new RegistryCallback.Delegated<>(v -> {
        }, onReload);
    }

    @Internal
    public static class Delegated<R extends CodecProvider<? super R>> implements RegistryCallback<R> {

        private Consumer<DynamicRegistry<R>> beginReload;

        private Consumer<DynamicRegistry<R>> onReload;

        public Delegated(Consumer<DynamicRegistry<R>> beginReload, Consumer<DynamicRegistry<R>> onReload) {
            this.beginReload = beginReload;
            this.onReload = onReload;
        }

        @Override
        public void beginReload(DynamicRegistry<R> manager) {
            this.beginReload.accept(manager);
        }

        @Override
        public void onReload(DynamicRegistry<R> manager) {
            this.onReload.accept(manager);
        }
    }
}