package fuzs.puzzleslib.impl.core.resources;

import com.google.common.collect.ImmutableList;
import fuzs.puzzleslib.api.core.v1.resources.NamedReloadListener;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

public class ForwardingReloadListener<T extends PreparableReloadListener> implements NamedReloadListener {

    private final ResourceLocation identifier;

    private final Supplier<Collection<T>> supplier;

    @Nullable
    private Collection<T> reloadListeners;

    public ForwardingReloadListener(ResourceLocation identifier, Supplier<Collection<T>> supplier) {
        Objects.requireNonNull(identifier, "identifier is null");
        Objects.requireNonNull(supplier, "supplier is null");
        this.identifier = identifier;
        this.supplier = supplier;
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.completedFuture(null).thenCompose($ -> CompletableFuture.allOf((CompletableFuture[]) this.reloadListeners().stream().map(reloadListener -> {
            try {
                return reloadListener.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            } catch (Exception var8) {
                PuzzlesLib.LOGGER.error("Unable to reload listener {}", reloadListener.getName(), var8);
                return CompletableFuture.completedFuture(null).thenCompose(preparationBarrier::m_6769_);
            }
        }).toArray(CompletableFuture[]::new)));
    }

    public final String toString() {
        return this.m_7812_();
    }

    @Override
    public ResourceLocation identifier() {
        return this.identifier;
    }

    final synchronized Collection<T> reloadListeners() {
        if (this.reloadListeners == null) {
            Collection<T> collection = (Collection<T>) this.supplier.get();
            Objects.requireNonNull(collection, "collection is null");
            if (collection.isEmpty()) {
                PuzzlesLib.LOGGER.error("{} is empty", this.identifier);
                return collection;
            } else {
                return this.reloadListeners = ImmutableList.copyOf(collection);
            }
        } else {
            return this.reloadListeners;
        }
    }
}