package team.lodestar.lodestone.systems.postprocess;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.EffectInstance;
import team.lodestar.lodestone.LodestoneLib;

public abstract class MultiInstancePostProcessor<I extends DynamicShaderFxInstance> extends PostProcessor {

    private final List<DynamicShaderFxInstance> instances = new ArrayList(this.getMaxInstances());

    private final ShaderDataBuffer dataBuffer = new ShaderDataBuffer();

    protected abstract int getMaxInstances();

    protected abstract int getDataSizePerInstance();

    @Override
    public void init() {
        super.init();
        this.dataBuffer.generate((long) this.getMaxInstances() * (long) this.getDataSizePerInstance());
    }

    @Nullable
    public I addFxInstance(I instance) {
        if (this.instances.size() >= this.getMaxInstances()) {
            LodestoneLib.LOGGER.warn("Failed to add fx instance to " + this + ": reached max instance count of " + this.getMaxInstances());
            return null;
        } else {
            this.instances.add(instance);
            this.setActive(true);
            return instance;
        }
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        for (int i = this.instances.size() - 1; i >= 0; i--) {
            DynamicShaderFxInstance instance = (DynamicShaderFxInstance) this.instances.get(i);
            instance.update((double) MC.getDeltaFrameTime());
            if (instance.isRemoved()) {
                this.instances.remove(i);
            }
        }
        if (this.instances.isEmpty()) {
            this.setActive(false);
        } else {
            float[] data = new float[this.instances.size() * this.getDataSizePerInstance()];
            for (int ins = 0; ins < this.instances.size(); ins++) {
                DynamicShaderFxInstance instance = (DynamicShaderFxInstance) this.instances.get(ins);
                int offset = ins * this.getDataSizePerInstance();
                instance.writeDataToBuffer((index, d) -> {
                    if (index < this.getDataSizePerInstance() && index >= 0) {
                        data[offset + index] = d;
                    } else {
                        throw new IndexOutOfBoundsException(index);
                    }
                });
            }
            this.dataBuffer.upload(data);
        }
    }

    protected void setDataBufferUniform(EffectInstance effectInstance, String bufferName, String countName) {
        this.dataBuffer.apply(effectInstance, bufferName);
        effectInstance.safeGetUniform(countName).set(this.instances.size());
    }
}