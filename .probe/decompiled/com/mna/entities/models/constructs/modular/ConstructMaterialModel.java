package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ModelsTypes;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.models.constructs.ConstructModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;

public class ConstructMaterialModel {

    public final ConstructArmModels RightArm;

    public final ConstructArmModels LeftArm;

    public final ConstructLegModels RightLeg;

    public final ConstructLegModels LeftLeg;

    public final ConstructPelvisModels Pelvis;

    public final ConstructTorsoModels Torso;

    public final ConstructHeadModels Head;

    public static ConstructDecorationModels Decorations;

    private final HashMap<String, ConstructModelCollection> bone_overrides;

    public ConstructMaterialModel(String material_identifier) {
        if (Decorations == null) {
            Decorations = new ConstructDecorationModels();
        }
        this.RightArm = new ConstructArmModels(false, material_identifier);
        this.LeftArm = new ConstructArmModels(true, material_identifier);
        this.RightLeg = new ConstructLegModels(false, material_identifier);
        this.LeftLeg = new ConstructLegModels(true, material_identifier);
        this.Torso = new ConstructTorsoModels(material_identifier);
        this.Head = new ConstructHeadModels(material_identifier);
        this.Pelvis = new ConstructPelvisModels(material_identifier);
        this.bone_overrides = new HashMap();
        this.bone_overrides.put("TORSO", this.Torso);
        this.bone_overrides.put("NECK", this.Head);
        this.bone_overrides.put("SHOULDER_R", this.RightArm);
        this.bone_overrides.put("SHOULDER_L", this.LeftArm);
        this.bone_overrides.put("LEG_L", this.LeftLeg);
        this.bone_overrides.put("LEG_R", this.RightLeg);
        this.bone_overrides.put("PELVIS", this.Pelvis);
    }

    public List<ResourceLocation> getModelIdentifiers() {
        ArrayList<ResourceLocation> elements = new ArrayList();
        elements.addAll(this.RightArm.getModelIdentifiers());
        elements.addAll(this.LeftArm.getModelIdentifiers());
        elements.addAll(this.RightLeg.getModelIdentifiers());
        elements.addAll(this.LeftLeg.getModelIdentifiers());
        elements.addAll(this.Torso.getModelIdentifiers());
        elements.addAll(this.Head.getModelIdentifiers());
        elements.addAll(this.Pelvis.getModelIdentifiers());
        elements.addAll(Decorations.getModelIdentifiers());
        return elements;
    }

    public ConstructMaterialModel setPredicate(ModelsTypes type, Predicate<String> predicate) {
        switch(type) {
            case HEAD:
                this.Head.setPartTypePredicate(predicate);
                break;
            case ARM:
                this.LeftArm.setPartTypePredicate(predicate);
                this.RightArm.setPartTypePredicate(predicate);
                break;
            case LEG:
                this.RightLeg.setPartTypePredicate(predicate);
                this.LeftLeg.setPartTypePredicate(predicate);
                this.Pelvis.setPartTypePredicate(predicate);
                break;
            case TORSO:
                this.Torso.setPartTypePredicate(predicate);
        }
        return this;
    }

    public ConstructMaterialModel build() {
        this.RightArm.build();
        this.LeftArm.build();
        this.RightLeg.build();
        this.LeftLeg.build();
        this.Pelvis.build();
        this.Torso.build();
        this.Head.build();
        Decorations.build();
        return this;
    }

    public List<ResourceLocation> getForBone(String name, ConstructModel model, Construct construct) {
        ConstructModelCollection collection = (ConstructModelCollection) this.bone_overrides.getOrDefault(name, null);
        if (collection != null) {
            int mutex = model.getMutex(collection.getSlot());
            List<ResourceLocation> models = collection.getForMutex(mutex);
            models.addAll(Decorations.getForSlot(collection.getSlot(), construct.getConstructData(), mutex));
            if (collection.getSlot() == ConstructSlot.HEAD) {
                construct.getConstructData().getPart(ConstructSlot.HEAD).ifPresent(m -> {
                    if (m.getMaterial() != ConstructMaterial.WICKERWOOD) {
                        models.addAll(Decorations.getForMoodlet(construct.getMoodlets().getStrongestMoodlet()));
                    }
                });
            }
            return models;
        } else {
            return Arrays.asList();
        }
    }
}