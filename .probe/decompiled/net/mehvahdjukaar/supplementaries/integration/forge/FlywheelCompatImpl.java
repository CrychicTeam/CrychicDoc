package net.mehvahdjukaar.supplementaries.integration.forge;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.hardcoded.ModelPart;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BellowsBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BellowsBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.joml.Quaternionf;

public class FlywheelCompatImpl {

    public static void setupClient() {
        InstancedRenderRegistry.configure((BlockEntityType) ModRegistry.BELLOWS_TILE.get()).alwaysSkipRender().factory(FlywheelCompatImpl.BellowsInstance::new).apply();
    }

    private static class BellowsInstance extends BlockEntityInstance<BellowsBlockTile> implements DynamicInstance {

        private final TextureAtlasSprite texture;

        private final ModelData center;

        private final ModelData top;

        private final ModelData bottom;

        private final ModelData leather;

        private final PoseStack stack;

        private float lastProgress = 0.0F;

        public BellowsInstance(MaterialManager materialManager, BellowsBlockTile tile) {
            super(materialManager, tile);
            this.texture = ModMaterials.BELLOWS_MATERIAL.sprite();
            Quaternionf rotation = this.getDirection().getRotation();
            this.stack = new PoseStack();
            BlockPos p = this.getInstancePosition();
            this.stack.translate((float) p.m_123341_(), (float) p.m_123342_(), (float) p.m_123343_());
            this.stack.scale(0.9995F, 0.9995F, 0.9995F);
            this.stack.translate(2.5E-4, 2.5E-4, 2.5E-4);
            this.stack.translate(0.5, 0.5, 0.5);
            this.stack.mulPose(rotation);
            this.stack.mulPose(RotHlpr.X90);
            this.center = this.makeCenterInstance().setTransform(this.stack);
            this.stack.translate(-0.5, -0.5, -0.5);
            this.leather = this.makeLeatherInstance().setTransform(this.stack);
            this.top = this.makeTopInstance().setTransform(this.stack);
            this.bottom = this.makeBottomInstance().setTransform(this.stack);
        }

        public void beginFrame() {
            float dh = ((BellowsBlockTile) this.blockEntity).getHeight(AnimationTickHolder.getPartialTicks());
            this.stack.pushPose();
            this.stack.translate(0.5, 0.5, 0.5);
            this.stack.pushPose();
            this.stack.translate(0.0, -0.8125 - (double) dh, 0.0);
            this.top.setTransform(this.stack);
            this.stack.popPose();
            this.stack.pushPose();
            this.stack.translate(0.0F, dh, 0.0F);
            this.bottom.setTransform(this.stack);
            this.stack.popPose();
            float j = 3.2F;
            this.stack.scale(1.0F, 1.0F + j * dh, 1.0F);
            this.leather.setTransform(this.stack);
            this.stack.popPose();
        }

        public void remove() {
            this.top.delete();
            this.leather.delete();
            this.center.delete();
            this.bottom.delete();
        }

        public void updateLight() {
            this.relight(this.pos, new FlatLit[] { this.top, this.center, this.leather, this.bottom });
        }

        private ModelData makeTopInstance() {
            return (ModelData) this.materialManager.defaultCutout().material(Materials.TRANSFORMED).model("top_" + ((BellowsBlockTile) this.blockEntity).m_58903_(), this::makeLidModel).createInstance();
        }

        private ModelData makeBottomInstance() {
            return (ModelData) this.materialManager.defaultCutout().material(Materials.TRANSFORMED).model("bottom_" + ((BellowsBlockTile) this.blockEntity).m_58903_(), this::makeLidModel).createInstance();
        }

        private ModelData makeLeatherInstance() {
            return (ModelData) this.materialManager.defaultCutout().material(Materials.TRANSFORMED).model("leather_" + ((BellowsBlockTile) this.blockEntity).m_58903_(), this::makeLeatherModel).createInstance();
        }

        private ModelData makeCenterInstance() {
            return (ModelData) this.materialManager.defaultCutout().material(Materials.TRANSFORMED).model("center_" + ((BellowsBlockTile) this.blockEntity).m_58903_(), this::makeCenterModel).createInstance();
        }

        private ModelPart makeLeatherModel() {
            return ModelPart.builder("bellows_leather", 64, 64).sprite(this.texture).cuboid().textureOffset(0, 37).start(-7.0F, -5.0F, -7.0F).size(14.0F, 10.0F, 14.0F).endCuboid().build();
        }

        private ModelPart makeLidModel() {
            return ModelPart.builder("bellows_lid", 64, 64).sprite(this.texture).cuboid().textureOffset(0, 0).start(-8.0F, 5.0F, -8.0F).size(16.0F, 3.0F, 16.0F).endCuboid().build();
        }

        private ModelPart makeCenterModel() {
            return ModelPart.builder("bellows_center", 64, 64).sprite(this.texture).cuboid().textureOffset(0, 0).start(-2.0F, -2.0F, -8.0F).size(4.0F, 1.0F, 1.0F).endCuboid().cuboid().textureOffset(0, 2).start(-2.0F, 1.0F, -8.0F).size(4.0F, 1.0F, 1.0F).endCuboid().cuboid().textureOffset(0, 19).start(-8.0F, -1.0F, -8.0F).size(16.0F, 2.0F, 16.0F).endCuboid().build();
        }

        private Direction getDirection() {
            return (Direction) this.blockState.m_61143_(BellowsBlock.FACING);
        }
    }
}