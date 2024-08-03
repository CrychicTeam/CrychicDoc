package dev.architectury.hooks.forge;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

public class PackRepositoryHooksImpl {

    public static void addSource(PackRepository repository, RepositorySource source) {
        repository.addPackFinder(source);
    }
}