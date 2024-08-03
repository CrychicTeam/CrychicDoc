package net.cristellib.forge.extrapackutil;

import java.util.function.Consumer;
import net.cristellib.builtinpacks.BuiltInDataPacks;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import org.jetbrains.annotations.NotNull;

public class RepositorySourceMaker implements RepositorySource {

    @Override
    public void loadPacks(@NotNull Consumer<Pack> consumer) {
        BuiltInDataPacks.getPacks(consumer);
    }
}