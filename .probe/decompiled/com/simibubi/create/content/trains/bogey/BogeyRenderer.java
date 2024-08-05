package com.simibubi.create.content.trains.bogey;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public abstract class BogeyRenderer {

    Map<String, BogeyRenderer.BogeyModelData[]> contraptionModelData = new HashMap();

    public BogeyRenderer.BogeyModelData[] getTransform(PartialModel model, PoseStack ms, boolean inInstancedContraption, int size) {
        return inInstancedContraption ? this.transformContraptionModelData(this.keyFromModel(model), ms) : this.createModelData(model, size);
    }

    public BogeyRenderer.BogeyModelData[] getTransform(BlockState state, PoseStack ms, boolean inContraption, int size) {
        return inContraption ? this.transformContraptionModelData(this.keyFromModel(state), ms) : this.createModelData(state, size);
    }

    public BogeyRenderer.BogeyModelData getTransform(PartialModel model, PoseStack ms, boolean inInstancedContraption) {
        return inInstancedContraption ? ((BogeyRenderer.BogeyModelData[]) this.contraptionModelData.get(this.keyFromModel(model)))[0].setTransform(ms) : BogeyRenderer.BogeyModelData.from(model);
    }

    public BogeyRenderer.BogeyModelData getTransform(BlockState state, PoseStack ms, boolean inContraption) {
        return inContraption ? ((BogeyRenderer.BogeyModelData[]) this.contraptionModelData.get(this.keyFromModel(state)))[0].setTransform(ms) : BogeyRenderer.BogeyModelData.from(state);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(CompoundTag var1, float var2, PoseStack var3, int var4, VertexConsumer var5, boolean var6);

    @OnlyIn(Dist.CLIENT)
    public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms) {
        this.render(bogeyData, wheelAngle, ms, 0, null, true);
    }

    public abstract BogeySizes.BogeySize getSize();

    private BogeyRenderer.BogeyModelData[] transformContraptionModelData(String key, PoseStack ms) {
        BogeyRenderer.BogeyModelData[] modelData = (BogeyRenderer.BogeyModelData[]) this.contraptionModelData.get(key);
        Arrays.stream(modelData).forEach(modelDataElement -> modelDataElement.setTransform(ms));
        return modelData;
    }

    private BogeyRenderer.BogeyModelData[] createModelData(PartialModel model, int size) {
        BogeyRenderer.BogeyModelData[] data = new BogeyRenderer.BogeyModelData[] { BogeyRenderer.BogeyModelData.from(model) };
        return this.expandArrayToLength(data, size);
    }

    private BogeyRenderer.BogeyModelData[] createModelData(BlockState state, int size) {
        BogeyRenderer.BogeyModelData[] data = new BogeyRenderer.BogeyModelData[] { BogeyRenderer.BogeyModelData.from(state) };
        return this.expandArrayToLength(data, size);
    }

    private BogeyRenderer.BogeyModelData[] expandArrayToLength(BogeyRenderer.BogeyModelData[] data, int size) {
        return (BogeyRenderer.BogeyModelData[]) Arrays.stream(Collections.nCopies(size, data).toArray()).flatMap(inner -> Arrays.stream((BogeyRenderer.BogeyModelData[]) inner)).toArray(BogeyRenderer.BogeyModelData[]::new);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void initialiseContraptionModelData(MaterialManager var1, CarriageBogey var2);

    public void createModelInstance(MaterialManager materialManager, PartialModel model, int count) {
        BogeyRenderer.BogeyModelData[] modelData = (BogeyRenderer.BogeyModelData[]) IntStream.range(0, count).mapToObj(i -> (ModelData) materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(model).createInstance()).map(BogeyRenderer.BogeyModelData::new).toArray(BogeyRenderer.BogeyModelData[]::new);
        this.contraptionModelData.put(this.keyFromModel(model), modelData);
    }

    public void createModelInstance(MaterialManager materialManager, BlockState state, int count) {
        BogeyRenderer.BogeyModelData[] modelData = (BogeyRenderer.BogeyModelData[]) IntStream.range(0, count).mapToObj(i -> (ModelData) materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(state).createInstance()).map(BogeyRenderer.BogeyModelData::new).toArray(BogeyRenderer.BogeyModelData[]::new);
        this.contraptionModelData.put(this.keyFromModel(state), modelData);
    }

    public void createModelInstance(MaterialManager materialManager, BlockState... states) {
        for (BlockState state : states) {
            this.createModelInstance(materialManager, state, 1);
        }
    }

    public void createModelInstance(MaterialManager materialManager, PartialModel... models) {
        for (PartialModel model : models) {
            this.createModelInstance(materialManager, model, 1);
        }
    }

    @Deprecated
    public static <B extends Transform<?>> void finalize(B b, PoseStack ms, int light, @Nullable VertexConsumer vb) {
        b.scale(0.9980469F);
        if (b instanceof SuperByteBuffer byteBuf && vb != null) {
            byteBuf.light(light).renderInto(ms, vb);
        }
    }

    public void emptyTransforms() {
        for (BogeyRenderer.BogeyModelData[] data : this.contraptionModelData.values()) {
            for (BogeyRenderer.BogeyModelData model : data) {
                model.setEmptyTransform();
            }
        }
    }

    public void updateLight(int blockLight, int skyLight) {
        for (BogeyRenderer.BogeyModelData[] data : this.contraptionModelData.values()) {
            for (BogeyRenderer.BogeyModelData model : data) {
                model.updateLight(blockLight, skyLight);
            }
        }
    }

    public void remove() {
        for (BogeyRenderer.BogeyModelData[] data : this.contraptionModelData.values()) {
            for (BogeyRenderer.BogeyModelData model : data) {
                model.delete();
            }
        }
        this.contraptionModelData.clear();
    }

    private String keyFromModel(PartialModel partialModel) {
        return partialModel.getLocation().toString();
    }

    private String keyFromModel(BlockState state) {
        return state.toString();
    }

    public static record BogeyModelData(Transform<?> transform) implements Transform<BogeyRenderer.BogeyModelData> {

        public static BogeyRenderer.BogeyModelData from(PartialModel model) {
            BlockState air = Blocks.AIR.defaultBlockState();
            return new BogeyRenderer.BogeyModelData(CachedBufferer.partial(model, air));
        }

        public static BogeyRenderer.BogeyModelData from(BlockState model) {
            return new BogeyRenderer.BogeyModelData(CachedBufferer.block(model));
        }

        public void render(PoseStack ms, int light, @Nullable VertexConsumer vb) {
            this.transform.scale(0.9980469F);
            if (this.transform instanceof SuperByteBuffer byteBuf && vb != null) {
                byteBuf.light(light).renderInto(ms, vb);
            }
        }

        public BogeyRenderer.BogeyModelData setTransform(PoseStack ms) {
            if (this.transform instanceof ModelData model) {
                model.setTransform(ms);
            }
            return this;
        }

        public BogeyRenderer.BogeyModelData setEmptyTransform() {
            if (this.transform instanceof ModelData model) {
                model.setEmptyTransform();
            }
            return this;
        }

        public BogeyRenderer.BogeyModelData delete() {
            if (this.transform instanceof ModelData model) {
                model.delete();
            }
            return this;
        }

        public BogeyRenderer.BogeyModelData updateLight(int blockLight, int skyLight) {
            if (this.transform instanceof ModelData model) {
                model.setBlockLight(blockLight).setSkyLight(skyLight);
            }
            return this;
        }

        public BogeyRenderer.BogeyModelData mulPose(Matrix4f pose) {
            this.transform.mulPose(pose);
            return this;
        }

        public BogeyRenderer.BogeyModelData mulNormal(Matrix3f normal) {
            this.transform.mulNormal(normal);
            return this;
        }

        public BogeyRenderer.BogeyModelData multiply(Quaternionf quaternion) {
            this.transform.multiply(quaternion);
            return this;
        }

        public BogeyRenderer.BogeyModelData scale(float factorX, float factorY, float factorZ) {
            this.transform.scale(factorX, factorY, factorZ);
            return this;
        }

        public BogeyRenderer.BogeyModelData translate(double x, double y, double z) {
            this.transform.translate(x, y, z);
            return this;
        }
    }

    public abstract static class CommonRenderer extends BogeyRenderer {

        @Override
        public BogeySizes.BogeySize getSize() {
            return null;
        }
    }
}