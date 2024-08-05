package net.minecraft.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PiglinHeadModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.dragon.DragonHeadModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class SkullBlockRenderer implements BlockEntityRenderer<SkullBlockEntity> {

    private final Map<SkullBlock.Type, SkullModelBase> modelByType;

    private static final Map<SkullBlock.Type, ResourceLocation> SKIN_BY_TYPE = Util.make(Maps.newHashMap(), p_261388_ -> {
        p_261388_.put(SkullBlock.Types.SKELETON, new ResourceLocation("textures/entity/skeleton/skeleton.png"));
        p_261388_.put(SkullBlock.Types.WITHER_SKELETON, new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"));
        p_261388_.put(SkullBlock.Types.ZOMBIE, new ResourceLocation("textures/entity/zombie/zombie.png"));
        p_261388_.put(SkullBlock.Types.CREEPER, new ResourceLocation("textures/entity/creeper/creeper.png"));
        p_261388_.put(SkullBlock.Types.DRAGON, new ResourceLocation("textures/entity/enderdragon/dragon.png"));
        p_261388_.put(SkullBlock.Types.PIGLIN, new ResourceLocation("textures/entity/piglin/piglin.png"));
        p_261388_.put(SkullBlock.Types.PLAYER, DefaultPlayerSkin.getDefaultSkin());
    });

    public static Map<SkullBlock.Type, SkullModelBase> createSkullRenderers(EntityModelSet entityModelSet0) {
        Builder<SkullBlock.Type, SkullModelBase> $$1 = ImmutableMap.builder();
        $$1.put(SkullBlock.Types.SKELETON, new SkullModel(entityModelSet0.bakeLayer(ModelLayers.SKELETON_SKULL)));
        $$1.put(SkullBlock.Types.WITHER_SKELETON, new SkullModel(entityModelSet0.bakeLayer(ModelLayers.WITHER_SKELETON_SKULL)));
        $$1.put(SkullBlock.Types.PLAYER, new SkullModel(entityModelSet0.bakeLayer(ModelLayers.PLAYER_HEAD)));
        $$1.put(SkullBlock.Types.ZOMBIE, new SkullModel(entityModelSet0.bakeLayer(ModelLayers.ZOMBIE_HEAD)));
        $$1.put(SkullBlock.Types.CREEPER, new SkullModel(entityModelSet0.bakeLayer(ModelLayers.CREEPER_HEAD)));
        $$1.put(SkullBlock.Types.DRAGON, new DragonHeadModel(entityModelSet0.bakeLayer(ModelLayers.DRAGON_SKULL)));
        $$1.put(SkullBlock.Types.PIGLIN, new PiglinHeadModel(entityModelSet0.bakeLayer(ModelLayers.PIGLIN_HEAD)));
        return $$1.build();
    }

    public SkullBlockRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.modelByType = createSkullRenderers(blockEntityRendererProviderContext0.getModelSet());
    }

    public void render(SkullBlockEntity skullBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        float $$6 = skullBlockEntity0.getAnimation(float1);
        BlockState $$7 = skullBlockEntity0.m_58900_();
        boolean $$8 = $$7.m_60734_() instanceof WallSkullBlock;
        Direction $$9 = $$8 ? (Direction) $$7.m_61143_(WallSkullBlock.FACING) : null;
        int $$10 = $$8 ? RotationSegment.convertToSegment($$9.getOpposite()) : (Integer) $$7.m_61143_(SkullBlock.ROTATION);
        float $$11 = RotationSegment.convertToDegrees($$10);
        SkullBlock.Type $$12 = ((AbstractSkullBlock) $$7.m_60734_()).getType();
        SkullModelBase $$13 = (SkullModelBase) this.modelByType.get($$12);
        RenderType $$14 = getRenderType($$12, skullBlockEntity0.getOwnerProfile());
        renderSkull($$9, $$11, $$6, poseStack2, multiBufferSource3, int4, $$13, $$14);
    }

    public static void renderSkull(@Nullable Direction direction0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5, SkullModelBase skullModelBase6, RenderType renderType7) {
        poseStack3.pushPose();
        if (direction0 == null) {
            poseStack3.translate(0.5F, 0.0F, 0.5F);
        } else {
            float $$8 = 0.25F;
            poseStack3.translate(0.5F - (float) direction0.getStepX() * 0.25F, 0.25F, 0.5F - (float) direction0.getStepZ() * 0.25F);
        }
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer $$9 = multiBufferSource4.getBuffer(renderType7);
        skullModelBase6.setupAnim(float2, float1, 0.0F);
        skullModelBase6.m_7695_(poseStack3, $$9, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.popPose();
    }

    public static RenderType getRenderType(SkullBlock.Type skullBlockType0, @Nullable GameProfile gameProfile1) {
        ResourceLocation $$2 = (ResourceLocation) SKIN_BY_TYPE.get(skullBlockType0);
        if (skullBlockType0 == SkullBlock.Types.PLAYER && gameProfile1 != null) {
            Minecraft $$3 = Minecraft.getInstance();
            Map<Type, MinecraftProfileTexture> $$4 = $$3.getSkinManager().getInsecureSkinInformation(gameProfile1);
            return $$4.containsKey(Type.SKIN) ? RenderType.entityTranslucent($$3.getSkinManager().registerTexture((MinecraftProfileTexture) $$4.get(Type.SKIN), Type.SKIN)) : RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(gameProfile1)));
        } else {
            return RenderType.entityCutoutNoCullZOffset($$2);
        }
    }
}