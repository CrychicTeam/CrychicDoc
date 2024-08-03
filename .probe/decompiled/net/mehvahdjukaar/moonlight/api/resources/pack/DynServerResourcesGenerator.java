package net.mehvahdjukaar.moonlight.api.resources.pack;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;

public abstract class DynServerResourcesGenerator extends DynResourceGenerator<DynamicDataPack> {

    protected DynServerResourcesGenerator(DynamicDataPack pack) {
        super(pack, pack.mainNamespace);
    }

    @Override
    protected PackRepository getRepository() {
        MinecraftServer s = PlatHelper.getCurrentServer();
        return s != null ? s.getPackRepository() : null;
    }
}