package dev.architectury.hooks;

import dev.architectury.hooks.forge.PackRepositoryHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

public class PackRepositoryHooks {

    private PackRepositoryHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static void addSource(PackRepository repository, RepositorySource source) {
        PackRepositoryHooksImpl.addSource(repository, source);
    }
}