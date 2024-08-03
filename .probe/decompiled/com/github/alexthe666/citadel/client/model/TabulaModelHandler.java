package com.github.alexthe666.citadel.client.model;

import com.github.alexthe666.citadel.client.model.container.TabulaCubeContainer;
import com.github.alexthe666.citadel.client.model.container.TabulaCubeGroupContainer;
import com.github.alexthe666.citadel.client.model.container.TabulaModelBlock;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum TabulaModelHandler implements JsonDeserializationContext {

    INSTANCE;

    private Gson gson = new GsonBuilder().registerTypeAdapter(ItemTransform.class, new ItemTransform.Deserializer()).registerTypeAdapter(ItemTransforms.class, new ItemTransforms.Deserializer()).create();

    private JsonParser parser = new JsonParser();

    private TabulaModelBlock.Deserializer TabulaModelBlockDeserializer = new TabulaModelBlock.Deserializer();

    private ResourceManager manager;

    private final Set<String> enabledDomains = new HashSet();

    public void addDomain(String domain) {
        this.enabledDomains.add(domain.toLowerCase(Locale.ROOT));
    }

    public TabulaModelContainer loadTabulaModel(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith(".tbl")) {
            path = path + ".tbl";
        }
        InputStream stream = TabulaModelHandler.class.getResourceAsStream(path);
        return INSTANCE.loadTabulaModel(this.getModelJsonStream(path, stream));
    }

    public TabulaModelContainer loadTabulaModel(InputStream stream) {
        return (TabulaModelContainer) this.gson.fromJson(new InputStreamReader(stream), TabulaModelContainer.class);
    }

    public TabulaCubeContainer getCubeByName(String name, TabulaModelContainer model) {
        for (TabulaCubeContainer cube : this.getAllCubes(model)) {
            if (cube.getName().equals(name)) {
                return cube;
            }
        }
        return null;
    }

    public TabulaCubeContainer getCubeByIdentifier(String identifier, TabulaModelContainer model) {
        for (TabulaCubeContainer cube : this.getAllCubes(model)) {
            if (cube.getIdentifier().equals(identifier)) {
                return cube;
            }
        }
        return null;
    }

    public List<TabulaCubeContainer> getAllCubes(TabulaModelContainer model) {
        List<TabulaCubeContainer> cubes = new ArrayList();
        for (TabulaCubeGroupContainer cubeGroup : model.getCubeGroups()) {
            cubes.addAll(this.traverse(cubeGroup));
        }
        for (TabulaCubeContainer cube : model.getCubes()) {
            cubes.addAll(this.traverse(cube));
        }
        return cubes;
    }

    private List<TabulaCubeContainer> traverse(TabulaCubeGroupContainer group) {
        List<TabulaCubeContainer> retCubes = new ArrayList();
        for (TabulaCubeContainer child : group.getCubes()) {
            retCubes.addAll(this.traverse(child));
        }
        for (TabulaCubeGroupContainer child : group.getCubeGroups()) {
            retCubes.addAll(this.traverse(child));
        }
        return retCubes;
    }

    private List<TabulaCubeContainer> traverse(TabulaCubeContainer cube) {
        List<TabulaCubeContainer> retCubes = new ArrayList();
        retCubes.add(cube);
        for (TabulaCubeContainer child : cube.getChildren()) {
            retCubes.addAll(this.traverse(child));
        }
        return retCubes;
    }

    private InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return zip;
            }
        }
        throw new RuntimeException("No model.json present in " + name);
    }

    public <T> T deserialize(JsonElement json, Type type) throws JsonParseException {
        return (T) this.gson.fromJson(json, type);
    }
}