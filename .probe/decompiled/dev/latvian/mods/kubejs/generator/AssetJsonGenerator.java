package dev.latvian.mods.kubejs.generator;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.client.StencilTexture;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

public class AssetJsonGenerator extends ResourceGenerator {

    private final Map<String, StencilTexture> stencils = new HashMap();

    public AssetJsonGenerator(Map<ResourceLocation, GeneratedData> m) {
        super(ConsoleJS.CLIENT, m);
    }

    public void blockState(ResourceLocation id, Consumer<VariantBlockStateGenerator> consumer) {
        VariantBlockStateGenerator gen = Util.make(new VariantBlockStateGenerator(), consumer);
        this.json(new ResourceLocation(id.getNamespace(), "blockstates/" + id.getPath()), gen.toJson());
    }

    public void multipartState(ResourceLocation id, Consumer<MultipartBlockStateGenerator> consumer) {
        MultipartBlockStateGenerator gen = Util.make(new MultipartBlockStateGenerator(), consumer);
        this.json(new ResourceLocation(id.getNamespace(), "blockstates/" + id.getPath()), gen.toJson());
    }

    public void blockModel(ResourceLocation id, Consumer<ModelGenerator> consumer) {
        ModelGenerator gen = Util.make(new ModelGenerator(), consumer);
        this.json(new ResourceLocation(id.getNamespace(), "models/block/" + id.getPath()), gen.toJson());
    }

    public void itemModel(ResourceLocation id, Consumer<ModelGenerator> consumer) {
        ModelGenerator gen = Util.make(new ModelGenerator(), consumer);
        this.json(asItemModelLocation(id), gen.toJson());
    }

    public static ResourceLocation asItemModelLocation(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), "models/item/" + id.getPath());
    }

    public void stencil(ResourceLocation target, String stencil, JsonObject colors) throws IOException {
        StencilTexture st = (StencilTexture) this.stencils.get(stencil);
        if (st == null) {
            Path path = KubeJSPaths.ASSETS.resolve("kubejs/textures/stencil/" + stencil + ".png");
            if (Files.notExists(path, new LinkOption[0])) {
                throw new IllegalArgumentException("Stencil file 'kubejs/assets/kubejs/textures/stencil/'" + stencil + ".png' not found!");
            }
            BufferedInputStream in = new BufferedInputStream(Files.newInputStream(path));
            try {
                Path metaPath = KubeJSPaths.ASSETS.resolve("kubejs/textures/stencil/" + stencil + ".png.mcmeta");
                byte[] meta = null;
                if (Files.exists(metaPath, new LinkOption[0])) {
                    meta = Files.readAllBytes(metaPath);
                }
                st = new StencilTexture(ImageIO.read(in), meta);
                this.stencils.put(stencil, st);
            } catch (Throwable var10) {
                try {
                    in.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
                throw var10;
            }
            in.close();
        }
        StencilTexture st1 = st;
        this.add(new ResourceLocation(target.getNamespace(), "textures/" + target.getPath() + ".png"), () -> st1.create(colors), true);
        if (st.mcmeta != null) {
            this.add(new ResourceLocation(target.getNamespace(), "textures/" + target.getPath() + ".png.mcmeta"), () -> st1.mcmeta, false);
        }
    }
}