package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.tools.RLoc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;

public class ConstructDecorationModels {

    public static final String MATERIAL_COMMON = "common";

    protected final ArrayList<ConstructMutexedModel> eye_models = new ArrayList();

    protected ConstructMutexedModel TUBE_LEFT;

    protected ConstructMutexedModel TUBE_RIGHT;

    public List<ResourceLocation> getModelIdentifiers() {
        List<ResourceLocation> all = (List<ResourceLocation>) this.eye_models.stream().map(m -> m.rLoc).collect(Collectors.toList());
        all.add(this.TUBE_LEFT.rLoc);
        all.add(this.TUBE_RIGHT.rLoc);
        return all;
    }

    public List<ResourceLocation> getForMoodlet(int moodlet) {
        return (List<ResourceLocation>) this.eye_models.stream().filter(m -> m.matchesMutex(moodlet)).map(m -> m.rLoc).collect(Collectors.toList());
    }

    public List<ResourceLocation> getForSlot(ConstructSlot slot, IConstructConstruction construction, int mutex) {
        ArrayList<ResourceLocation> rLocs = new ArrayList();
        if (slot == ConstructSlot.TORSO) {
            construction.getPart(ConstructSlot.LEFT_ARM).ifPresent(c -> {
                if (c.isMutex(64)) {
                    rLocs.add(this.TUBE_LEFT.rLoc);
                }
            });
            construction.getPart(ConstructSlot.RIGHT_ARM).ifPresent(c -> {
                if (c.isMutex(64)) {
                    rLocs.add(this.TUBE_RIGHT.rLoc);
                }
            });
        }
        return rLocs;
    }

    private ResourceLocation getEyesRLoc(String mood) {
        return RLoc.create("construct/common/eyes_" + mood);
    }

    public final void build() {
        this.eye_models.add(new ConstructMutexedModel(this.getEyesRLoc("angry"), 1));
        this.eye_models.add(new ConstructMutexedModel(this.getEyesRLoc("concern"), 2));
        this.eye_models.add(new ConstructMutexedModel(this.getEyesRLoc("confused"), 4));
        this.eye_models.add(new ConstructMutexedModel(this.getEyesRLoc("happy"), 8));
        this.eye_models.add(new ConstructMutexedModel(this.getEyesRLoc("neutral"), 16));
        this.eye_models.add(new ConstructMutexedModel(this.getEyesRLoc("unimpressed"), 32));
        this.TUBE_LEFT = new ConstructMutexedModel(RLoc.create("construct/common/torso_tube_l"), 8);
        this.TUBE_RIGHT = new ConstructMutexedModel(RLoc.create("construct/common/torso_tube_r"), 8);
    }
}