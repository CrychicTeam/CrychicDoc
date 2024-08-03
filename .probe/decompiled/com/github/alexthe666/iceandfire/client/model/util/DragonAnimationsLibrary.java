package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import java.io.IOException;
import java.util.HashMap;

public class DragonAnimationsLibrary {

    private static final HashMap<String, TabulaModel> models = new HashMap();

    private static String toKey(IEnumDragonPoses p, IEnumDragonModelTypes m) {
        return p.getPose() + m.getModelType();
    }

    public static TabulaModel getModel(IEnumDragonPoses pose, IEnumDragonModelTypes modelType) {
        TabulaModel result = (TabulaModel) models.get(toKey(pose, modelType));
        if (result == null) {
            IceAndFire.LOGGER.error("No model defined for " + pose.getPose() + modelType.getModelType() + " have you registered your animations?");
        }
        return result;
    }

    public static void registerSingle(IEnumDragonPoses pose, IEnumDragonModelTypes modelType) {
        registerSingle(pose, modelType, "iceandfire");
    }

    public static void register(IEnumDragonPoses[] poses, IEnumDragonModelTypes[] modelTypes) {
        for (IEnumDragonPoses p : poses) {
            for (IEnumDragonModelTypes m : modelTypes) {
                registerSingle(p, m, "iceandfire");
            }
        }
    }

    public static void register(IEnumDragonPoses[] poses, IEnumDragonModelTypes[] modelTypes, String modID) {
        for (IEnumDragonPoses p : poses) {
            for (IEnumDragonModelTypes m : modelTypes) {
                registerSingle(p, m, modID);
            }
        }
    }

    public static void registerSingle(IEnumDragonPoses pose, IEnumDragonModelTypes modelType, String modID) {
        String location = "/assets/" + modID + "/models/tabula/" + modelType.getModelType() + "dragon/" + modelType.getModelType() + "dragon_" + pose.getPose() + ".tbl";
        TabulaModel result;
        try {
            result = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel(location));
        } catch (NullPointerException | IOException var6) {
            IceAndFire.LOGGER.warn("Could not load " + location + ": " + var6.getMessage());
            return;
        }
        models.put(toKey(pose, modelType), result);
    }

    public static void registerReferences(IEnumDragonPoses[] poses, IEnumDragonModelTypes modelSource, IEnumDragonModelTypes[] modelDestinations) {
        for (int i = 0; i < poses.length; i++) {
            registerReference(poses[i], modelSource, modelDestinations[i]);
        }
    }

    public static void registerReference(IEnumDragonPoses pose, IEnumDragonModelTypes modelSource, IEnumDragonModelTypes modelDestination) {
        TabulaModel source = getModel(pose, modelSource);
        String destKey = toKey(pose, modelDestination);
        if (source != null) {
            if (models.containsKey(destKey)) {
                IceAndFire.LOGGER.info("Overriding existing model '" + destKey + "' with reference to '" + toKey(pose, modelSource));
            }
            models.put(destKey, source);
        }
    }
}