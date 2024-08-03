package fuzs.puzzleslib.api.client.core.v1.context;

import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import java.util.Objects;
import net.minecraft.client.searchtree.SearchRegistry;

@Deprecated(forRemoval = true)
public interface SearchRegistryContext {

    default <T> void registerSearchTree(SearchRegistry.Key<T> searchRegistryKey, SearchRegistry.TreeBuilderSupplier<T> treeBuilder) {
        Objects.requireNonNull(searchRegistryKey, "search registry key is null");
        Objects.requireNonNull(treeBuilder, "tree builder is null");
        SearchRegistry searchRegistry = ClientAbstractions.INSTANCE.getSearchRegistry();
        Objects.requireNonNull(searchRegistry, "search tree manager is null");
        searchRegistry.register(searchRegistryKey, treeBuilder);
    }
}