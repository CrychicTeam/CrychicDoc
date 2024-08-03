package se.mickelus.tetra.client.model;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.data.DataManager;

@ParametersAreNonnullByDefault
public class ModularModelLoader implements IGeometryLoader<UnresolvedItemModel> {

    private static final Logger logger = LogManager.getLogger();

    private static List<UnresolvedItemModel> newModels = new LinkedList();

    private static List<UnresolvedItemModel> models = new LinkedList();

    public ModularModelLoader() {
        models.forEach(UnresolvedItemModel::clearCache);
    }

    public static void init() {
        DataManager.instance.moduleData.onReload(ModularModelLoader::clearCaches);
    }

    private static void shuffle() {
        if (!newModels.isEmpty()) {
            models = newModels;
            newModels = new LinkedList();
        }
    }

    public static synchronized void clearCaches() {
        logger.info("Clearing model cache for {} items, let's get bakin'", models.size());
        models.forEach(UnresolvedItemModel::clearCache);
        shuffle();
    }

    private static synchronized void addModel(UnresolvedItemModel model) {
        newModels.add(model);
    }

    public UnresolvedItemModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) throws JsonParseException {
        ItemTransforms cameraTransforms = (ItemTransforms) deserializationContext.deserialize(modelContents.get("display"), ItemTransforms.class);
        if (modelContents.has("variants")) {
            Map<String, ItemTransforms> transformVariants = (Map<String, ItemTransforms>) deserializationContext.deserialize(modelContents.get("variants"), (new TypeToken<Map<String, ItemTransforms>>() {
            }).getType());
            UnresolvedItemModel model = new UnresolvedItemModel(cameraTransforms, transformVariants);
            addModel(model);
            return model;
        } else {
            UnresolvedItemModel model = new UnresolvedItemModel(cameraTransforms);
            addModel(model);
            return model;
        }
    }
}