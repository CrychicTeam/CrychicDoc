package snownee.lychee;

import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;
import snownee.kiwi.Kiwi;
import snownee.lychee.core.contextual.ContextualConditionType;
import snownee.lychee.core.post.PostActionType;
import snownee.lychee.util.CommonProxy;

public final class LycheeRegistries {

    public static LycheeRegistries.MappedRegistry<ContextualConditionType<?>> CONTEXTUAL;

    public static LycheeRegistries.MappedRegistry<PostActionType<?>> POST_ACTION;

    public static void init(NewRegistryEvent event) {
        register("contextual", ContextualConditionType.class, event, v -> CONTEXTUAL = v);
        register("post_action", PostActionType.class, event, v -> POST_ACTION = v);
    }

    private static <T> void register(String name, Class<?> clazz, NewRegistryEvent event, Consumer<LycheeRegistries.MappedRegistry<T>> consumer) {
        RegistryBuilder<T> builder = new RegistryBuilder<T>().setName(new ResourceLocation("lychee", name));
        event.create(builder, v -> {
            consumer.accept(new LycheeRegistries.MappedRegistry(v));
            if (CommonProxy.hasKiwi) {
                Kiwi.registerRegistry(v, clazz);
            }
        });
    }

    public static record MappedRegistry<T>(IForgeRegistry<T> registry) implements Iterable<T> {

        public ResourceKey<Registry<T>> key() {
            return this.registry.getRegistryKey();
        }

        public void register(ResourceLocation id, T t) {
            this.registry.register(id, t);
        }

        public T get(ResourceLocation key) {
            return this.registry.getValue(key);
        }

        public ResourceLocation getKey(T t) {
            return this.registry.getKey(t);
        }

        @NotNull
        public Iterator<T> iterator() {
            return this.registry.iterator();
        }
    }
}