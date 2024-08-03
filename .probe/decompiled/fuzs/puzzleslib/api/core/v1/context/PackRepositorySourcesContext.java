package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.server.packs.repository.RepositorySource;

@FunctionalInterface
public interface PackRepositorySourcesContext {

    void addRepositorySource(RepositorySource... var1);
}