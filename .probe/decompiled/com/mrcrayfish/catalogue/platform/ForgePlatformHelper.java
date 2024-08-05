package com.mrcrayfish.catalogue.platform;

import com.mojang.blaze3d.platform.NativeImage;
import com.mrcrayfish.catalogue.client.ForgeModData;
import com.mrcrayfish.catalogue.client.IModData;
import com.mrcrayfish.catalogue.platform.services.IPlatformHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.resource.ResourcePackLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isForge() {
        return true;
    }

    @Override
    public List<IModData> getAllModData() {
        return (List<IModData>) ModList.get().getMods().stream().map(ForgeModData::new).collect(Collectors.toList());
    }

    @Override
    public File getModDirectory() {
        return FMLPaths.MODSDIR.get().toFile();
    }

    @Override
    public void loadNativeImage(String modId, String resource, Consumer<NativeImage> consumer) {
        ResourcePackLoader.getPackFor(modId).ifPresent(resources -> {
            IoSupplier<InputStream> supplier = resources.getRootResource(resource);
            if (supplier != null) {
                try {
                    InputStream is = supplier.get();
                    try (NativeImage image = NativeImage.read(is)) {
                        consumer.accept(image);
                    } catch (Throwable var11) {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Throwable var8) {
                                var11.addSuppressed(var8);
                            }
                        }
                        throw var11;
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException var12) {
                }
            }
        });
    }
}