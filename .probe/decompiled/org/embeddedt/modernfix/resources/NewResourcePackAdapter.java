package org.embeddedt.modernfix.resources;

import java.io.InputStream;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.IoSupplier;

public class NewResourcePackAdapter {

    public static void sendToOutput(Function<ResourceLocation, IoSupplier<InputStream>> streamCreator, PackResources.ResourceOutput output, Collection<ResourceLocation> locations) {
        for (ResourceLocation rl : locations) {
            output.accept(rl, (IoSupplier) streamCreator.apply(rl));
        }
    }
}