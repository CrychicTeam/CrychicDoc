package com.mna.entities.renderers.ritual;

import com.mna.ManaAndArtifice;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.entities.rituals.Ritual;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.joml.Quaternionf;

public class RitualRenderer extends EntityRenderer<Ritual> {

    private final Minecraft mc;

    private final ItemRenderer itemRenderer;

    private static final float FX_Y_OFFSET = 1.2F;

    private int reagentSlotCount = 1;

    public RitualRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.mc = Minecraft.getInstance();
        this.itemRenderer = this.mc.getItemRenderer();
    }

    public ResourceLocation getTextureLocation(Ritual entity) {
        return null;
    }

    public void render(Ritual entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        boolean guiding_placement = entityIn.getState() == Ritual.RitualState.GUIDING_REAGENT_PLACEMENT;
        NonNullList<RitualBlockPos> rbp = guiding_placement ? entityIn.getRitualData((byte) 1) : entityIn.getRitualData((byte) 2);
        ArrayList<BlockPos> validPositions = entityIn.getValidReagentLocations();
        float speed = entityIn.getSpeed();
        int radiant_advance_rate = (int) Math.ceil((double) (10.0F * speed));
        int beam_advance_rate = (int) Math.ceil((double) (3.0F * speed));
        int maxPositions = entityIn.getState() == Ritual.RitualState.GUIDING_REAGENT_PLACEMENT ? 256 : entityIn.getAge() / radiant_advance_rate;
        int beamAge = entityIn.getAge() - radiant_advance_rate * rbp.size();
        int beamMaximum = 0;
        int iteratorMaximum = Math.min(maxPositions, rbp.size());
        int lastBeamStartIndex = -1;
        if (iteratorMaximum == rbp.size()) {
            beamMaximum = beamAge / beam_advance_rate;
        }
        this.reagentSlotCount = 1;
        for (int i = 0; i < iteratorMaximum; i++) {
            if (lastBeamStartIndex == -1) {
                lastBeamStartIndex = i;
            }
            BlockPos curPos = rbp.get(i).getBlockPos();
            BlockPos nextPos = null;
            if (!guiding_placement) {
                if (i == iteratorMaximum - 1) {
                    if (entityIn.getCurrentRitual().getConnectBeam()) {
                        nextPos = rbp.get(lastBeamStartIndex).getBlockPos();
                    }
                } else {
                    int curIndex = rbp.get(i).getDisplayIndex();
                    int nextIndex = rbp.get(i + 1).getDisplayIndex();
                    if (nextIndex == curIndex + 1) {
                        nextPos = rbp.get(i + 1).getBlockPos();
                    } else {
                        if (entityIn.getCurrentRitual().getConnectBeam()) {
                            nextPos = rbp.get(lastBeamStartIndex).getBlockPos();
                        }
                        lastBeamStartIndex = i + 1;
                    }
                }
            }
            double xPosCur = (double) curPos.m_123341_() - entityIn.m_20185_() + 0.5;
            double zPosCur = (double) curPos.m_123343_() - entityIn.m_20189_() + 0.5;
            if (entityIn.getState() == Ritual.RitualState.GUIDING_REAGENT_PLACEMENT) {
                this.renderReagentPlacementGuide(entityIn, rbp.get(i), matrixStackIn, xPosCur, zPosCur, bufferIn, packedLightIn, partialTicks, validPositions.stream().anyMatch(p -> p.equals(rbp.get(i).getBlockPos())));
            } else {
                matrixStackIn.pushPose();
                matrixStackIn.translate(xPosCur, 1.2F, zPosCur);
                int alpha = 128;
                if (i == iteratorMaximum - 1 && iteratorMaximum != rbp.size()) {
                    float ageTicks = (float) (entityIn.getAge() % radiant_advance_rate) + partialTicks;
                    float pct = ageTicks / (float) radiant_advance_rate;
                    alpha = (int) (128.0F * pct);
                }
                WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, colorsFromLong(entityIn.getCurrentRitual().getInnerColor()), colorsFromLong(entityIn.getCurrentRitual().getOuterColor()), alpha, 0.005F);
                if (nextPos != null) {
                    double xPosNext = (double) nextPos.m_123341_() - entityIn.m_20185_() + 0.5;
                    double zPosNext = (double) nextPos.m_123343_() - entityIn.m_20189_() + 0.5;
                    if (i < beamMaximum - 1) {
                        WorldRenderUtils.renderBeam(entityIn.m_9236_(), partialTicks, matrixStackIn, bufferIn, packedLightIn, new Vec3(xPosCur, 1.2F, zPosCur), new Vec3(xPosNext, 1.2F, zPosNext), 1.0F, colorsFromLong(entityIn.getCurrentRitual().getBeamColor()), MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                    } else if (i == beamMaximum - 1 && (beamMaximum < rbp.size() || !entityIn.getCurrentRitual().getConnectBeam())) {
                        float beamPct = ((float) (beamAge % beam_advance_rate) + partialTicks) / (float) beam_advance_rate;
                        WorldRenderUtils.renderBeam(entityIn.m_9236_(), partialTicks, matrixStackIn, bufferIn, packedLightIn, new Vec3(xPosCur, 1.2F, zPosCur), new Vec3(xPosNext, 1.2F, zPosNext), beamPct, colorsFromLong(entityIn.getCurrentRitual().getBeamColor()), MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                    }
                }
                matrixStackIn.popPose();
            }
        }
        if (entityIn.getState() == Ritual.RitualState.COLLECTING_PATTERNS) {
            this.renderPatternGuide(entityIn, matrixStackIn, bufferIn);
        }
        this.renderCollectedReagents(entityIn, matrixStackIn, partialTicks, packedLightIn, bufferIn);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void renderCollectedReagents(Ritual entityIn, PoseStack matrixStackIn, float partialTicks, int packedLightIn, MultiBufferSource bufferIn) {
        NonNullList<Pair<BlockPos, ItemStack>> collectedReagents = entityIn.getCollectedReagentsByLocation();
        BlockPos pos = entityIn.m_20183_();
        float scale = 0.5F;
        float collapse = 1.0F;
        if (entityIn.getState() == Ritual.RitualState.COLLAPSING) {
            collapse = 1.0F - (float) entityIn.getStageTicks() / 20.0F;
        } else if (entityIn.getState().ordinal() > Ritual.RitualState.COLLAPSING.ordinal()) {
            return;
        }
        long ticks = ManaAndArtifice.instance.proxy.getGameTicks();
        float up = entityIn.getState() == Ritual.RitualState.COLLECTING_REAGENTS ? Math.min(((float) entityIn.getStageTicks() + partialTicks) / 20.0F, 1.0F) : 1.0F;
        float rotation = 0.0F;
        if (entityIn.getState() == Ritual.RitualState.COLLECTING_REAGENTS && entityIn.getStageTicks() <= 20) {
            rotation = 0.0F;
        } else {
            rotation = (float) ((ticks - entityIn.worldTimeAtReagentCollectStart - 20L) % 360L) + partialTicks;
        }
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0F, up, 0.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        matrixStackIn.scale(collapse, collapse, collapse);
        for (Pair<BlockPos, ItemStack> e : collectedReagents) {
            matrixStackIn.pushPose();
            matrixStackIn.translate((float) (((BlockPos) e.getFirst()).m_123341_() - pos.m_123341_()), (float) (((BlockPos) e.getFirst()).m_123342_() - pos.m_123342_()), (float) (((BlockPos) e.getFirst()).m_123343_() - pos.m_123343_()));
            matrixStackIn.scale(scale, scale, scale);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
            if (((ItemStack) e.getSecond()).getItem() instanceof BlockItem) {
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(rotation));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(rotation));
            }
            this.itemRenderer.renderStatic((ItemStack) e.getSecond(), ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    private void renderReagentPlacementGuide(Ritual entityIn, RitualBlockPos posData, PoseStack matrixStackIn, double xPosCur, double zPosCur, MultiBufferSource bufferIn, int packedLightIn, float partialTicks, boolean present) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(xPosCur, -0.5, zPosCur);
        if (posData.isPresent() && !posData.getReagent().isDynamic()) {
            if (entityIn.getCurrentRitual().getDisplayIndexes()) {
                if (posData.getReagent().isOptional()) {
                    this.m_7649_(entityIn, Component.literal(this.reagentSlotCount++ + ""), matrixStackIn, bufferIn, packedLightIn);
                } else {
                    this.m_7649_(entityIn, Component.literal(this.reagentSlotCount++ + "").withStyle(ChatFormatting.GOLD), matrixStackIn, bufferIn, packedLightIn);
                }
            }
            Item renderItem = ForgeRegistries.ITEMS.getValue(posData.getReagent().getResourceLocation());
            if (renderItem == Items.AIR) {
                ITag<Item> tags = ForgeRegistries.ITEMS.tags().getTag(ForgeRegistries.ITEMS.tags().createTagKey(posData.getReagent().getResourceLocation()));
                if (tags != null) {
                    Optional<Item> rnd = tags.getRandomElement(RandomSource.create(432L));
                    if (rnd.isPresent()) {
                        renderItem = (Item) rnd.get();
                    }
                }
            }
            if (renderItem != null) {
                matrixStackIn.translate(0.0F, 1.2F, 0.0F);
                matrixStackIn.scale(0.3F, 0.3F, 0.3F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees((float) entityIn.getStageTicks() + partialTicks));
                if (!present) {
                    this.itemRenderer.renderStatic(new ItemStack(renderItem), ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
                }
            }
        }
        matrixStackIn.popPose();
    }

    private void renderPatternGuide(Ritual entityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf patternRotation = new Quaternionf(cameraRotation.x(), cameraRotation.y(), cameraRotation.z(), cameraRotation.w());
        ManaweavingPattern pattern = entityIn.getRequestedPattern();
        if (pattern != null) {
            WorldRenderUtils.renderManaweavePattern(pattern, patternRotation, matrixStackIn, bufferIn, false);
        }
    }

    private static int[] colorsFromLong(long packedColor) {
        return new int[] { (int) (packedColor >> 16 & 255L), (int) (packedColor >> 8 & 255L), (int) (packedColor & 255L) };
    }
}