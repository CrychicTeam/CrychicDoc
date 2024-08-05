package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.UUID;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class GolemBannerLayer<T extends AbstractGolemEntity<?, ?>, M extends EntityModel<T> & IHeadedModel> extends RenderLayer<T, M> {

    private final float scaleX;

    private final float scaleY;

    private final float scaleZ;

    private final ItemInHandRenderer itemInHandRenderer;

    public GolemBannerLayer(RenderLayerParent<T, M> parent, ItemInHandRenderer iihr) {
        this(parent, 1.0F, 1.0F, 1.0F, iihr);
    }

    public GolemBannerLayer(RenderLayerParent<T, M> parent, float sx, float sy, float sz, ItemInHandRenderer iihr) {
        super(parent);
        this.scaleX = sx;
        this.scaleY = sy;
        this.scaleZ = sz;
        this.itemInHandRenderer = iihr;
    }

    public void render(PoseStack pose, MultiBufferSource buffer, int light, T entity, float f1, float f2, float f3, float f4, float f5, float f6) {
        ItemStack stack = this.getBanner(entity);
        if (this.renders(stack)) {
            pose.pushPose();
            pose.scale(this.scaleX, this.scaleY, this.scaleZ);
            ((HeadedModel) this.m_117386_()).getHead().translateAndRotate(pose);
            ((IHeadedModel) this.m_117386_()).translateToHead(pose);
            this.itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.HEAD, false, pose, buffer, light);
            pose.popPose();
        }
    }

    public ItemStack getBanner(T entity) {
        ItemStack stack = entity.m_6844_(EquipmentSlot.HEAD);
        if (entity instanceof HumanoidGolemEntity && this.renders(stack)) {
            return ItemStack.EMPTY;
        } else {
            if (entity instanceof MetalGolemEntity && !this.renders(stack)) {
                stack = entity.m_6844_(EquipmentSlot.FEET);
            }
            if (this.renders(stack)) {
                return stack;
            } else {
                GolemConfigEntry entry = entity.getConfigEntry(MGLangData.LOADING.get());
                if (entry != null) {
                    entry.clientTick(entity.m_9236_(), false);
                    UUID captainId = entry.squadConfig.getCaptainId();
                    boolean showFlag = captainId != null && entity.m_20148_().equals(captainId);
                    if (showFlag) {
                        String color = DyeColor.values()[entry.getColor()].getName();
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color + "_banner"));
                        if (item != null) {
                            return item.getDefaultInstance();
                        }
                    }
                }
                return ItemStack.EMPTY;
            }
        }
    }

    public boolean renders(ItemStack stack) {
        return stack.getItem() instanceof BannerItem;
    }
}