package dev.latvian.mods.kubejs.client;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

public class KubeJSResourcePackFinder implements RepositorySource {

    @Override
    public void loadPacks(Consumer<Pack> nameToPackMap) {
        if (KubeJSPaths.FIRST_RUN.getValue()) {
            Path blockTextures = KubeJSPaths.dir(KubeJSPaths.ASSETS.resolve("kubejs/textures/block"));
            Path itemTextures = KubeJSPaths.dir(KubeJSPaths.ASSETS.resolve("kubejs/textures/item"));
            try {
                InputStream in = Files.newInputStream((Path) KubeJS.thisMod.findResource("data", "kubejs", "example_block_texture.png").get());
                try {
                    OutputStream out = Files.newOutputStream(blockTextures.resolve("example_block.png"));
                    try {
                        in.transferTo(out);
                    } catch (Throwable var15) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable var11) {
                                var15.addSuppressed(var11);
                            }
                        }
                        throw var15;
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable var16) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable var10) {
                            var16.addSuppressed(var10);
                        }
                    }
                    throw var16;
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }
            try {
                InputStream in = Files.newInputStream((Path) KubeJS.thisMod.findResource("data", "kubejs", "example_item_texture.png").get());
                try {
                    OutputStream out = Files.newOutputStream(itemTextures.resolve("example_item.png"));
                    try {
                        in.transferTo(out);
                    } catch (Throwable var12) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable var9) {
                                var12.addSuppressed(var9);
                            }
                        }
                        throw var12;
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable var13) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable var8) {
                            var13.addSuppressed(var8);
                        }
                    }
                    throw var13;
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }
        }
    }
}