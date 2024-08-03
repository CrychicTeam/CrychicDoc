package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.IConstructModelRegistrationHelper;
import com.mna.api.entities.construct.ModelsTypes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ConstructModelRegistry implements IConstructModelRegistrationHelper {

    public static final String MODEL_BASE = "construct/";

    private static HashMap<ConstructMaterial, ConstructMaterialModel> material_model = new HashMap();

    private static final ConstructMaterialModel STONE = new ConstructMaterialModel("stone").build();

    private static final ConstructMaterialModel WOOD = new ConstructMaterialModel("wood").build();

    private static final ConstructMaterialModel IRON = new ConstructMaterialModel("iron").build();

    private static final ConstructMaterialModel GOLD = new ConstructMaterialModel("gold").build();

    private static final ConstructMaterialModel DIAMOND = new ConstructMaterialModel("diamond").build();

    private static final ConstructMaterialModel OBSIDIAN = new ConstructMaterialModel("obsidian").build();

    private static final ConstructMaterialModel WICKERWOOD = new ConstructMaterialModel("wickerwood").setPredicate(ModelsTypes.HEAD, ConstructModelCollection.BASIC_ONLY).setPredicate(ModelsTypes.LEG, ConstructModelCollection.BASIC_ONLY).setPredicate(ModelsTypes.TORSO, s -> Arrays.asList("basic", "storage").contains(s)).setPredicate(ModelsTypes.ARM, s -> Arrays.asList("axe", "blade", "grabber", "hammer", "fisher").contains(s)).build();

    public static ConstructMaterialModel getMaterialModelFor(ConstructMaterial material) {
        return (ConstructMaterialModel) material_model.getOrDefault(material, STONE);
    }

    @SubscribeEvent
    public static void onRegisterSpecialModels(ModelEvent.RegisterAdditional event) {
        ConstructMaterial.ALL_MATERIALS.forEach(mat -> {
            if (material_model.containsKey(mat)) {
                ((ConstructMaterialModel) material_model.get(mat)).getModelIdentifiers().forEach(rLoc -> event.register(rLoc));
            }
        });
    }

    @Override
    public void register(ConstructMaterial material, String material_type, Predicate<String> headTypePredicate, Predicate<String> torsoTypePredicate, Predicate<String> legTypePredicate, Predicate<String> armTypePredicate) {
        material_model.put(material, new ConstructMaterialModel(material_type).setPredicate(ModelsTypes.HEAD, headTypePredicate).setPredicate(ModelsTypes.TORSO, torsoTypePredicate).setPredicate(ModelsTypes.LEG, legTypePredicate).setPredicate(ModelsTypes.ARM, armTypePredicate));
    }

    static {
        material_model.put(ConstructMaterial.WICKERWOOD, WICKERWOOD);
        material_model.put(ConstructMaterial.STONE, STONE);
        material_model.put(ConstructMaterial.WOOD, WOOD);
        material_model.put(ConstructMaterial.IRON, IRON);
        material_model.put(ConstructMaterial.GOLD, GOLD);
        material_model.put(ConstructMaterial.DIAMOND, DIAMOND);
        material_model.put(ConstructMaterial.OBSIDIAN, OBSIDIAN);
    }
}