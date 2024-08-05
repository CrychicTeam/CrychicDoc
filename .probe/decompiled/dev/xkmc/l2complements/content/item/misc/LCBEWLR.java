package dev.xkmc.l2complements.content.item.misc;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.util.Proxy;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.WitherArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class LCBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() -> new LCBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return (BlockEntityWithoutLevelRenderer) LCBEWLR.INSTANCE.get();
        }
    };

    private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");

    private static final ResourceLocation WITHER_LOCATION = new ResourceLocation("textures/entity/wither/wither.png");

    private final EntityModelSet entityModelSet;

    private WitherBossModel<WitherBoss> wither_model;

    private WitherArmorLayer wither_armor;

    private WitherBoss wither;

    public LCBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
        this.entityModelSet = set;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.wither = null;
        this.wither_model = new WitherBossModel<>(this.entityModelSet.bakeLayer(ModelLayers.WITHER_ARMOR));
        this.wither_armor = new WitherArmorLayer(new RenderLayerParent<WitherBoss, WitherBossModel<WitherBoss>>() {

            public WitherBossModel<WitherBoss> getModel() {
                return LCBEWLR.this.wither_model;
            }

            public ResourceLocation getTextureLocation(WitherBoss boss) {
                return LCBEWLR.WITHER_LOCATION;
            }
        }, this.entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.is((Item) LCItems.FORCE_FIELD.get())) {
            this.renderWither(type, poseStack, bufferSource, light, overlay);
        }
    }

    private void renderWither(ItemDisplayContext type, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        this.setUpWither();
        poseStack.pushPose();
        this.translateWither(poseStack, type);
        this.wither_armor.m_6494_(poseStack, bufferSource, light, this.wither, 0.0F, 0.0F, Minecraft.getInstance().getPartialTick(), (float) this.wither.f_19797_, 0.0F, 0.0F);
        poseStack.popPose();
    }

    private void setUpWither() {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            this.wither = null;
        } else {
            if (this.wither != null && this.wither.m_9236_() != level) {
                this.wither = null;
            }
            if (this.wither == null) {
                this.wither = EntityType.WITHER.create(level);
            }
            if (this.wither != null) {
                this.wither.f_19797_ = Proxy.getClientPlayer().f_19797_;
                this.wither.m_21153_(1.0F);
            }
        }
    }

    private void translateWither(PoseStack stack, ItemDisplayContext transform) {
        switch(transform) {
            case GUI:
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
            default:
                break;
            case THIRD_PERSON_LEFT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
                {
                    stack.translate(0.25, 0.4, 0.5);
                    float size = 0.625F;
                    stack.scale(size, size, size);
                    break;
                }
            case GROUND:
                {
                    stack.translate(0.25, 0.0, 0.5);
                    float size = 0.625F;
                    stack.scale(size, size, size);
                    break;
                }
            case NONE:
            case HEAD:
            case FIXED:
                {
                    stack.translate(0.5, 0.5, 0.5);
                    float size = 0.6F;
                    stack.scale(size, -size, size);
                    stack.translate(0.0, -0.45, 0.0);
                    return;
                }
        }
        stack.mulPose(Axis.ZP.rotationDegrees(135.0F));
        stack.mulPose(Axis.YP.rotationDegrees(-155.0F));
        float size = 0.6F;
        stack.scale(size, size, size);
        stack.translate(0.0, -1.6, 0.0);
    }
}