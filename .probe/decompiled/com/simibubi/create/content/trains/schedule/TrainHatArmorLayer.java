package com.simibubi.create.content.trains.schedule;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.mixin.accessor.AgeableListModelAccessor;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Couple;
import java.util.Iterator;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TrainHatArmorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private Vec3 offset;

    public TrainHatArmorLayer(RenderLayerParent<T, M> renderer, Vec3 offset) {
        super(renderer);
        this.offset = offset;
    }

    public void render(PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float yaw, float pitch, float pt, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (this.shouldRenderOn(entity)) {
            M entityModel = (M) this.m_117386_();
            RenderType renderType = Sheets.cutoutBlockSheet();
            ms.pushPose();
            boolean valid = false;
            TransformStack msr = TransformStack.cast(ms);
            float scale = 1.0F;
            if (entityModel instanceof AgeableListModel<?> model) {
                if (model.f_102610_) {
                    if (model.scaleHead) {
                        float f = 1.5F / model.babyHeadScale;
                        ms.scale(f, f, f);
                    }
                    ms.translate(0.0, (double) (model.babyYHeadOffset / 16.0F), (double) (model.babyZHeadOffset / 16.0F));
                }
                ModelPart head = getHeadPart(model);
                if (head != null) {
                    head.translateAndRotate(ms);
                    if (model instanceof WolfModel) {
                        head = head.getChild("real_head");
                    }
                    if (model instanceof AxolotlModel) {
                        head = head.getChild("head");
                    }
                    ms.translate(this.offset.x / 16.0, this.offset.y / 16.0, this.offset.z / 16.0);
                    if (!head.isEmpty()) {
                        ModelPart.Cube cube = (ModelPart.Cube) head.cubes.get(0);
                        ms.translate(this.offset.x / 16.0, ((double) (cube.minY - cube.maxY) + this.offset.y) / 16.0, this.offset.z / 16.0);
                        float max = Math.max(cube.maxX - cube.minX, cube.maxZ - cube.minZ) / 8.0F;
                        ms.scale(max, max, max);
                    }
                    valid = true;
                }
            } else if (entityModel instanceof HierarchicalModel<?> model) {
                boolean slime = model instanceof SlimeModel || model instanceof LavaSlimeModel;
                ModelPart head = (ModelPart) model.root().children.get(slime ? "cube" : "head");
                if (model instanceof WardenModel) {
                    head = (ModelPart) ((ModelPart) ((ModelPart) model.root().children.get("bone")).children.get("body")).children.get("head");
                }
                if (model instanceof FrogModel) {
                    head = (ModelPart) ((ModelPart) model.root().children.get("body")).children.get("head");
                    scale = 0.5F;
                }
                if (head != null) {
                    head.translateAndRotate(ms);
                    if (!head.isEmpty()) {
                        ModelPart.Cube cube = (ModelPart.Cube) head.cubes.get(0);
                        ms.translate(this.offset.x, ((double) (cube.minY - cube.maxY) + this.offset.y) / 16.0, this.offset.z / 16.0);
                        float max = Math.max(cube.maxX - cube.minX, cube.maxZ - cube.minZ) / (slime ? 6.5F : 8.0F) * scale;
                        ms.scale(max, max, max);
                    }
                    valid = true;
                }
            }
            if (valid) {
                ms.scale(1.0F, -1.0F, -1.0F);
                ms.translate(0.0F, -0.140625F, 0.0F);
                msr.rotateX(-8.5);
                BlockState air = Blocks.AIR.defaultBlockState();
                CachedBufferer.partial(AllPartialModels.TRAIN_HAT, air).forEntityRender().light(light).renderInto(ms, buffer.getBuffer(renderType));
            }
            ms.popPose();
        }
    }

    private boolean shouldRenderOn(LivingEntity entity) {
        if (entity == null) {
            return false;
        } else if (entity.getPersistentData().contains("TrainHat")) {
            return true;
        } else if (!entity.m_20159_()) {
            return false;
        } else {
            if (entity instanceof Player p) {
                ItemStack headItem = p.getItemBySlot(EquipmentSlot.HEAD);
                if (!headItem.isEmpty()) {
                    return false;
                }
            }
            if (entity.m_20202_() instanceof CarriageContraptionEntity cce) {
                if (!cce.hasSchedule() && !(entity instanceof Player)) {
                    return false;
                } else if (cce.getContraption() instanceof CarriageContraption cc) {
                    BlockPos seatOf = cc.getSeatOf(entity.m_20148_());
                    if (seatOf == null) {
                        return false;
                    } else {
                        Couple<Boolean> validSides = (Couple<Boolean>) cc.conductorSeats.get(seatOf);
                        return validSides != null;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static void registerOnAll(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values()) {
            registerOn(renderer);
        }
        for (EntityRenderer<?> renderer : renderManager.renderers.values()) {
            registerOn(renderer);
        }
    }

    public static void registerOn(EntityRenderer<?> entityRenderer) {
        if (entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            EntityModel<?> model = livingRenderer.getModel();
            if (model instanceof HierarchicalModel || model instanceof AgeableListModel) {
                Vec3 offset = TrainHatOffsets.getOffset(model);
                TrainHatArmorLayer<?, ?> layer = new TrainHatArmorLayer<>((RenderLayerParent<LivingEntity, EntityModel<LivingEntity>>) livingRenderer, offset);
                livingRenderer.addLayer(layer);
            }
        }
    }

    private static ModelPart getHeadPart(AgeableListModel<?> model) {
        Iterator var1 = ((AgeableListModelAccessor) model).create$callHeadParts().iterator();
        if (var1.hasNext()) {
            return (ModelPart) var1.next();
        } else {
            var1 = ((AgeableListModelAccessor) model).create$callBodyParts().iterator();
            return var1.hasNext() ? (ModelPart) var1.next() : null;
        }
    }
}