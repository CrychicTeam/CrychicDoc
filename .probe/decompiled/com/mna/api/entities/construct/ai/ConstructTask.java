package com.mna.api.entities.construct.ai;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class ConstructTask {

    private final Class<? extends ConstructAITask<?>> aiTask;

    private final boolean lodestar_assignable;

    private final boolean low_tier_assignable;

    private final boolean is_condition;

    private final ResourceLocation icon_texture;

    private final int num_outputs;

    public ConstructTask(ResourceLocation texture, Class<? extends ConstructAITask<?>> aiTask, int num_outputs, boolean lodestar_assignable, boolean low_tier_assignable) {
        this(texture, aiTask, num_outputs, lodestar_assignable, low_tier_assignable, false);
    }

    public ConstructTask(ResourceLocation texture, Class<? extends ConstructAITask<?>> aiTask, int num_outputs, boolean lodestar_assignable, boolean low_tier_assignable, boolean is_condition) {
        assert num_outputs >= 1;
        this.aiTask = aiTask;
        this.lodestar_assignable = lodestar_assignable;
        this.icon_texture = texture;
        this.num_outputs = num_outputs;
        this.low_tier_assignable = low_tier_assignable;
        this.is_condition = is_condition;
    }

    public ConstructTask(ResourceLocation texture, Class<? extends ConstructAITask<?>> aiTask, boolean lodestar_assignable, boolean low_tier_assignable) {
        this(texture, aiTask, lodestar_assignable, low_tier_assignable, false);
    }

    public ConstructTask(ResourceLocation texture, Class<? extends ConstructAITask<?>> aiTask, boolean lodestar_assignable, boolean low_tier_assignable, boolean is_condition) {
        this(texture, aiTask, 2, lodestar_assignable, low_tier_assignable, is_condition);
    }

    @Nullable
    public Class<? extends ConstructAITask<?>> getAIClass() {
        return this.aiTask;
    }

    public int getOutputs() {
        return this.num_outputs;
    }

    public final boolean isLodestarAssignable() {
        return this.lodestar_assignable;
    }

    public final boolean isLowTierAssignable() {
        return this.low_tier_assignable;
    }

    public final boolean isCondition() {
        return this.is_condition;
    }

    @Nullable
    public ResourceLocation getIconTexture() {
        return this.icon_texture;
    }

    @Nullable
    public final ConstructAITask<?> instantiateTask(IConstruct<?> construct) {
        Class<? extends ConstructAITask<?>> aiClass = this.getAIClass();
        ConstructAITask<?> inst = null;
        try {
            Constructor<? extends ConstructAITask<?>> ctor = aiClass.getConstructor(IConstruct.class, ResourceLocation.class);
            if (ctor != null) {
                inst = (ConstructAITask<?>) ctor.newInstance(construct, this.icon_texture);
            } else {
                ManaAndArtificeMod.LOGGER.error("Construct AI Task constructor doesn't define a version with parameters <IConstruct, ResourceLocation>: " + aiClass.getName());
            }
        } catch (Throwable var5) {
            ManaAndArtificeMod.LOGGER.error("Critical error attempting to load instanced construct AI task: " + aiClass.getName());
            ManaAndArtificeMod.LOGGER.catching(var5);
        }
        return inst;
    }

    public final List<ConstructAITaskParameter> getParameters() {
        ArrayList<ConstructAITaskParameter> parameters = new ArrayList();
        ConstructAITask<?> inst = this.instantiateTask(null);
        if (inst != null) {
            parameters.addAll(inst.getParameters());
        }
        return parameters;
    }

    public static class PhantomTask extends ConstructTask {

        public PhantomTask() {
            super(null, null, 1, false, false);
        }
    }
}