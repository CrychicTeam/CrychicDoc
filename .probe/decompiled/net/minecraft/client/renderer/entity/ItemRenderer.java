package net.minecraft.client.renderer.entity;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.MatrixUtil;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;

public class ItemRenderer implements ResourceManagerReloadListener {

    public static final ResourceLocation ENCHANTED_GLINT_ENTITY = new ResourceLocation("textures/misc/enchanted_glint_entity.png");

    public static final ResourceLocation ENCHANTED_GLINT_ITEM = new ResourceLocation("textures/misc/enchanted_glint_item.png");

    private static final Set<Item> IGNORED = Sets.newHashSet(new Item[] { Items.AIR });

    public static final int GUI_SLOT_CENTER_X = 8;

    public static final int GUI_SLOT_CENTER_Y = 8;

    public static final int ITEM_COUNT_BLIT_OFFSET = 200;

    public static final float COMPASS_FOIL_UI_SCALE = 0.5F;

    public static final float COMPASS_FOIL_FIRST_PERSON_SCALE = 0.75F;

    public static final float COMPASS_FOIL_TEXTURE_SCALE = 0.0078125F;

    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");

    public static final ModelResourceLocation TRIDENT_IN_HAND_MODEL = ModelResourceLocation.vanilla("trident_in_hand", "inventory");

    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");

    public static final ModelResourceLocation SPYGLASS_IN_HAND_MODEL = ModelResourceLocation.vanilla("spyglass_in_hand", "inventory");

    private final Minecraft minecraft;

    private final ItemModelShaper itemModelShaper;

    private final TextureManager textureManager;

    private final ItemColors itemColors;

    private final BlockEntityWithoutLevelRenderer blockEntityRenderer;

    public ItemRenderer(Minecraft minecraft0, TextureManager textureManager1, ModelManager modelManager2, ItemColors itemColors3, BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer4) {
        this.minecraft = minecraft0;
        this.textureManager = textureManager1;
        this.itemModelShaper = new ItemModelShaper(modelManager2);
        this.blockEntityRenderer = blockEntityWithoutLevelRenderer4;
        for (Item $$5 : BuiltInRegistries.ITEM) {
            if (!IGNORED.contains($$5)) {
                this.itemModelShaper.register($$5, new ModelResourceLocation(BuiltInRegistries.ITEM.getKey($$5), "inventory"));
            }
        }
        this.itemColors = itemColors3;
    }

    public ItemModelShaper getItemModelShaper() {
        return this.itemModelShaper;
    }

    private void renderModelLists(BakedModel bakedModel0, ItemStack itemStack1, int int2, int int3, PoseStack poseStack4, VertexConsumer vertexConsumer5) {
        RandomSource $$6 = RandomSource.create();
        long $$7 = 42L;
        for (Direction $$8 : Direction.values()) {
            $$6.setSeed(42L);
            this.renderQuadList(poseStack4, vertexConsumer5, bakedModel0.getQuads(null, $$8, $$6), itemStack1, int2, int3);
        }
        $$6.setSeed(42L);
        this.renderQuadList(poseStack4, vertexConsumer5, bakedModel0.getQuads(null, null, $$6), itemStack1, int2, int3);
    }

    public void render(ItemStack itemStack0, ItemDisplayContext itemDisplayContext1, boolean boolean2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5, int int6, BakedModel bakedModel7) {
        if (!itemStack0.isEmpty()) {
            poseStack3.pushPose();
            boolean $$8 = itemDisplayContext1 == ItemDisplayContext.GUI || itemDisplayContext1 == ItemDisplayContext.GROUND || itemDisplayContext1 == ItemDisplayContext.FIXED;
            if ($$8) {
                if (itemStack0.is(Items.TRIDENT)) {
                    bakedModel7 = this.itemModelShaper.getModelManager().getModel(TRIDENT_MODEL);
                } else if (itemStack0.is(Items.SPYGLASS)) {
                    bakedModel7 = this.itemModelShaper.getModelManager().getModel(SPYGLASS_MODEL);
                }
            }
            bakedModel7.getTransforms().getTransform(itemDisplayContext1).apply(boolean2, poseStack3);
            poseStack3.translate(-0.5F, -0.5F, -0.5F);
            if (!bakedModel7.isCustomRenderer() && (!itemStack0.is(Items.TRIDENT) || $$8)) {
                boolean $$10;
                if (itemDisplayContext1 != ItemDisplayContext.GUI && !itemDisplayContext1.firstPerson() && itemStack0.getItem() instanceof BlockItem) {
                    Block $$9 = ((BlockItem) itemStack0.getItem()).getBlock();
                    $$10 = !($$9 instanceof HalfTransparentBlock) && !($$9 instanceof StainedGlassPaneBlock);
                } else {
                    $$10 = true;
                }
                RenderType $$12 = ItemBlockRenderTypes.getRenderType(itemStack0, $$10);
                VertexConsumer $$14;
                if (hasAnimatedTexture(itemStack0) && itemStack0.hasFoil()) {
                    poseStack3.pushPose();
                    PoseStack.Pose $$13 = poseStack3.last();
                    if (itemDisplayContext1 == ItemDisplayContext.GUI) {
                        MatrixUtil.mulComponentWise($$13.pose(), 0.5F);
                    } else if (itemDisplayContext1.firstPerson()) {
                        MatrixUtil.mulComponentWise($$13.pose(), 0.75F);
                    }
                    if ($$10) {
                        $$14 = getCompassFoilBufferDirect(multiBufferSource4, $$12, $$13);
                    } else {
                        $$14 = getCompassFoilBuffer(multiBufferSource4, $$12, $$13);
                    }
                    poseStack3.popPose();
                } else if ($$10) {
                    $$14 = getFoilBufferDirect(multiBufferSource4, $$12, true, itemStack0.hasFoil());
                } else {
                    $$14 = getFoilBuffer(multiBufferSource4, $$12, true, itemStack0.hasFoil());
                }
                this.renderModelLists(bakedModel7, itemStack0, int5, int6, poseStack3, $$14);
            } else {
                this.blockEntityRenderer.renderByItem(itemStack0, itemDisplayContext1, poseStack3, multiBufferSource4, int5, int6);
            }
            poseStack3.popPose();
        }
    }

    private static boolean hasAnimatedTexture(ItemStack itemStack0) {
        return itemStack0.is(ItemTags.COMPASSES) || itemStack0.is(Items.CLOCK);
    }

    public static VertexConsumer getArmorFoilBuffer(MultiBufferSource multiBufferSource0, RenderType renderType1, boolean boolean2, boolean boolean3) {
        return boolean3 ? VertexMultiConsumer.create(multiBufferSource0.getBuffer(boolean2 ? RenderType.armorGlint() : RenderType.armorEntityGlint()), multiBufferSource0.getBuffer(renderType1)) : multiBufferSource0.getBuffer(renderType1);
    }

    public static VertexConsumer getCompassFoilBuffer(MultiBufferSource multiBufferSource0, RenderType renderType1, PoseStack.Pose poseStackPose2) {
        return VertexMultiConsumer.create(new SheetedDecalTextureGenerator(multiBufferSource0.getBuffer(RenderType.glint()), poseStackPose2.pose(), poseStackPose2.normal(), 0.0078125F), multiBufferSource0.getBuffer(renderType1));
    }

    public static VertexConsumer getCompassFoilBufferDirect(MultiBufferSource multiBufferSource0, RenderType renderType1, PoseStack.Pose poseStackPose2) {
        return VertexMultiConsumer.create(new SheetedDecalTextureGenerator(multiBufferSource0.getBuffer(RenderType.glintDirect()), poseStackPose2.pose(), poseStackPose2.normal(), 0.0078125F), multiBufferSource0.getBuffer(renderType1));
    }

    public static VertexConsumer getFoilBuffer(MultiBufferSource multiBufferSource0, RenderType renderType1, boolean boolean2, boolean boolean3) {
        if (boolean3) {
            return Minecraft.useShaderTransparency() && renderType1 == Sheets.translucentItemSheet() ? VertexMultiConsumer.create(multiBufferSource0.getBuffer(RenderType.glintTranslucent()), multiBufferSource0.getBuffer(renderType1)) : VertexMultiConsumer.create(multiBufferSource0.getBuffer(boolean2 ? RenderType.glint() : RenderType.entityGlint()), multiBufferSource0.getBuffer(renderType1));
        } else {
            return multiBufferSource0.getBuffer(renderType1);
        }
    }

    public static VertexConsumer getFoilBufferDirect(MultiBufferSource multiBufferSource0, RenderType renderType1, boolean boolean2, boolean boolean3) {
        return boolean3 ? VertexMultiConsumer.create(multiBufferSource0.getBuffer(boolean2 ? RenderType.glintDirect() : RenderType.entityGlintDirect()), multiBufferSource0.getBuffer(renderType1)) : multiBufferSource0.getBuffer(renderType1);
    }

    private void renderQuadList(PoseStack poseStack0, VertexConsumer vertexConsumer1, List<BakedQuad> listBakedQuad2, ItemStack itemStack3, int int4, int int5) {
        boolean $$6 = !itemStack3.isEmpty();
        PoseStack.Pose $$7 = poseStack0.last();
        for (BakedQuad $$8 : listBakedQuad2) {
            int $$9 = -1;
            if ($$6 && $$8.isTinted()) {
                $$9 = this.itemColors.getColor(itemStack3, $$8.getTintIndex());
            }
            float $$10 = (float) ($$9 >> 16 & 0xFF) / 255.0F;
            float $$11 = (float) ($$9 >> 8 & 0xFF) / 255.0F;
            float $$12 = (float) ($$9 & 0xFF) / 255.0F;
            vertexConsumer1.putBulkData($$7, $$8, $$10, $$11, $$12, int4, int5);
        }
    }

    public BakedModel getModel(ItemStack itemStack0, @Nullable Level level1, @Nullable LivingEntity livingEntity2, int int3) {
        BakedModel $$4;
        if (itemStack0.is(Items.TRIDENT)) {
            $$4 = this.itemModelShaper.getModelManager().getModel(TRIDENT_IN_HAND_MODEL);
        } else if (itemStack0.is(Items.SPYGLASS)) {
            $$4 = this.itemModelShaper.getModelManager().getModel(SPYGLASS_IN_HAND_MODEL);
        } else {
            $$4 = this.itemModelShaper.getItemModel(itemStack0);
        }
        ClientLevel $$7 = level1 instanceof ClientLevel ? (ClientLevel) level1 : null;
        BakedModel $$8 = $$4.getOverrides().resolve($$4, itemStack0, $$7, livingEntity2, int3);
        return $$8 == null ? this.itemModelShaper.getModelManager().getMissingModel() : $$8;
    }

    public void renderStatic(ItemStack itemStack0, ItemDisplayContext itemDisplayContext1, int int2, int int3, PoseStack poseStack4, MultiBufferSource multiBufferSource5, @Nullable Level level6, int int7) {
        this.renderStatic(null, itemStack0, itemDisplayContext1, false, poseStack4, multiBufferSource5, level6, int2, int3, int7);
    }

    public void renderStatic(@Nullable LivingEntity livingEntity0, ItemStack itemStack1, ItemDisplayContext itemDisplayContext2, boolean boolean3, PoseStack poseStack4, MultiBufferSource multiBufferSource5, @Nullable Level level6, int int7, int int8, int int9) {
        if (!itemStack1.isEmpty()) {
            BakedModel $$10 = this.getModel(itemStack1, level6, livingEntity0, int9);
            this.render(itemStack1, itemDisplayContext2, boolean3, poseStack4, multiBufferSource5, int7, int8, $$10);
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.itemModelShaper.rebuildCache();
    }
}