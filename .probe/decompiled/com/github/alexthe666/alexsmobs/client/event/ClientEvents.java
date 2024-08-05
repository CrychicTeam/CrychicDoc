package com.github.alexthe666.alexsmobs.client.event;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.client.model.ModelRockyChestplateRolling;
import com.github.alexthe666.alexsmobs.client.model.ModelWanderingVillagerRider;
import com.github.alexthe666.alexsmobs.client.model.layered.AMModelLayers;
import com.github.alexthe666.alexsmobs.client.render.AMItemstackRenderer;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.LavaVisionFluidRenderer;
import com.github.alexthe666.alexsmobs.client.render.RenderVineLasso;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.EffectPowerDown;
import com.github.alexthe666.alexsmobs.entity.EntityBaldEagle;
import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.EntityElephant;
import com.github.alexthe666.alexsmobs.entity.IFalconry;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.alexsmobs.entity.util.RockyChestplateUtil;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemDimensionalCarver;
import com.github.alexthe666.alexsmobs.message.MessageUpdateEagleControls;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.client.event.EventGetFluidRenderType;
import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import com.github.alexthe666.citadel.client.event.EventGetStarBrightness;
import com.github.alexthe666.citadel.client.event.EventPosePlayerHand;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public class ClientEvents {

    private static final ResourceLocation ROCKY_CHESTPLATE_TEXTURE = new ResourceLocation("alexsmobs:textures/armor/rocky_chestplate.png");

    private static final ModelRockyChestplateRolling ROCKY_CHESTPLATE_MODEL = new ModelRockyChestplateRolling();

    private boolean previousLavaVision = false;

    private LiquidBlockRenderer previousFluidRenderer;

    public long lastStaticTick = -1L;

    public static int renderStaticScreenFor = 0;

    @SubscribeEvent
    public void onOutlineEntityColor(EventGetOutlineColor event) {
        if (event.getEntityIn() instanceof Enemy && AlexsMobs.PROXY.getSingingBlueJayId() != -1 && event.getEntityIn().level().getEntity(AlexsMobs.PROXY.getSingingBlueJayId()) instanceof EntityBlueJay jay && jay.m_6084_() && jay.isMakingMonstersBlue()) {
            event.setColor(4953598);
            event.setResult(Result.ALLOW);
        }
        if (event.getEntityIn() instanceof ItemEntity && ((ItemEntity) event.getEntityIn()).getItem().is(AMTagRegistry.VOID_WORM_DROPS)) {
            int fromColor = 0;
            int toColor = 2221567;
            float startR = (float) (fromColor >> 16 & 0xFF) / 255.0F;
            float startG = (float) (fromColor >> 8 & 0xFF) / 255.0F;
            float startB = (float) (fromColor & 0xFF) / 255.0F;
            float endR = (float) (toColor >> 16 & 0xFF) / 255.0F;
            float endG = (float) (toColor >> 8 & 0xFF) / 255.0F;
            float endB = (float) (toColor & 0xFF) / 255.0F;
            float f = (float) (Math.cos((double) (0.4F * ((float) event.getEntityIn().tickCount + Minecraft.getInstance().getFrameTime()))) + 1.0) * 0.5F;
            float r = (endR - startR) * f + startR;
            float g = (endG - startG) * f + startG;
            float b = (endB - startB) * f + startB;
            int j = ((int) (r * 255.0F) & 0xFF) << 16 | ((int) (g * 255.0F) & 0xFF) << 8 | ((int) (b * 255.0F) & 0xFF) << 0;
            event.setColor(j);
            event.setResult(Result.ALLOW);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onGetStarBrightness(EventGetStarBrightness event) {
        if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.POWER_DOWN.get()) && Minecraft.getInstance().player.m_21124_(AMEffectRegistry.POWER_DOWN.get()) != null) {
            MobEffectInstance instance = Minecraft.getInstance().player.m_21124_(AMEffectRegistry.POWER_DOWN.get());
            EffectPowerDown powerDown = (EffectPowerDown) instance.getEffect();
            int duration = instance.getDuration();
            float partialTicks = Minecraft.getInstance().getFrameTime();
            float f = ((float) Math.min(powerDown.getActiveTime(), duration) + partialTicks) * 0.1F;
            event.setBrightness(0.0F);
            event.setResult(Result.ALLOW);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.POWER_DOWN.get()) && Minecraft.getInstance().player.m_21124_(AMEffectRegistry.POWER_DOWN.get()) != null) {
            event.setBlue(0.0F);
            event.setRed(0.0F);
            event.setGreen(0.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    @OnlyIn(Dist.CLIENT)
    public void onFogDensity(ViewportEvent.RenderFog event) {
        FogType fogType = event.getCamera().getFluidInCamera();
        if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.LAVA_VISION.get()) && fogType == FogType.LAVA) {
            event.setNearPlaneDistance(-8.0F);
            event.setFarPlaneDistance(50.0F);
            event.setCanceled(true);
        }
        if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.POWER_DOWN.get()) && fogType == FogType.NONE && Minecraft.getInstance().player.m_21124_(AMEffectRegistry.POWER_DOWN.get()) != null) {
            float initEnd = event.getFarPlaneDistance();
            MobEffectInstance instance = Minecraft.getInstance().player.m_21124_(AMEffectRegistry.POWER_DOWN.get());
            EffectPowerDown powerDown = (EffectPowerDown) instance.getEffect();
            int duration = instance.getDuration();
            float partialTicks = Minecraft.getInstance().getFrameTime();
            float f = Math.min(20.0F, Math.min((float) powerDown.getActiveTime() + partialTicks, (float) duration + partialTicks)) * 0.05F;
            event.setNearPlaneDistance(-8.0F);
            float f1 = 8.0F + (1.0F - f) * Math.max(0.0F, initEnd - 8.0F);
            event.setFarPlaneDistance(f1);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPreRenderEntity(RenderLivingEvent.Pre event) {
        if (RockyChestplateUtil.isRockyRolling(event.getEntity())) {
            event.setCanceled(true);
            event.getPoseStack().pushPose();
            float limbSwing = event.getEntity().walkAnimation.position() - event.getEntity().walkAnimation.speed() * (1.0F - event.getPartialTick());
            float limbSwingAmount = event.getEntity().walkAnimation.speed((float) event.getPackedLight());
            float yRot = event.getEntity().yBodyRotO + (event.getEntity().yBodyRot - event.getEntity().yBodyRotO) * event.getPartialTick();
            float roll = event.getEntity().f_19867_ + (event.getEntity().f_19787_ - event.getEntity().f_19867_) * event.getPartialTick();
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(event.getMultiBufferSource(), RenderType.armorCutoutNoCull(ROCKY_CHESTPLATE_TEXTURE), false, event.getEntity().getItemBySlot(EquipmentSlot.CHEST).hasFoil());
            event.getPoseStack().translate(0.0, (double) (event.getEntity().m_20206_() - event.getEntity().m_20206_() * 0.5F), 0.0);
            event.getPoseStack().mulPose(Axis.YN.rotationDegrees(180.0F + yRot));
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(180.0F));
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(100.0F * roll));
            ROCKY_CHESTPLATE_MODEL.setupAnim(event.getEntity(), limbSwing, limbSwingAmount, (float) event.getEntity().f_19797_ + event.getPartialTick(), 0.0F, 0.0F);
            ROCKY_CHESTPLATE_MODEL.m_7695_(event.getPoseStack(), vertexconsumer, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            event.getPoseStack().popPose();
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
        } else {
            if (event.getEntity() instanceof WanderingTrader && event.getEntity().m_6095_() == EntityType.WANDERING_TRADER && event.getEntity().m_20202_() instanceof EntityElephant && !(event.getRenderer().model instanceof ModelWanderingVillagerRider)) {
                event.getRenderer().model = (M) (new ModelWanderingVillagerRider(Minecraft.getInstance().getEntityModels().bakeLayer(AMModelLayers.SITTING_WANDERING_VILLAGER)));
            }
            if (event.getEntity().hasEffect(AMEffectRegistry.CLINGING.get()) && event.getEntity().m_20192_() < event.getEntity().m_20206_() * 0.45F || event.getEntity().hasEffect(AMEffectRegistry.DEBILITATING_STING.get()) && event.getEntity().getMobType() == MobType.ARTHROPOD && event.getEntity().m_20205_() > event.getEntity().m_20206_()) {
                event.getPoseStack().pushPose();
                event.getPoseStack().translate(0.0, (double) (event.getEntity().m_20206_() + 0.1F), 0.0);
                event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(180.0F));
                event.getEntity().yBodyRotO = -event.getEntity().yBodyRotO;
                event.getEntity().yBodyRot = -event.getEntity().yBodyRot;
                event.getEntity().yHeadRotO = -event.getEntity().yHeadRotO;
                event.getEntity().yHeadRot = -event.getEntity().yHeadRot;
            }
            if (event.getEntity().hasEffect(AMEffectRegistry.ENDER_FLU.get())) {
                event.getPoseStack().pushPose();
                event.getPoseStack().mulPose(Axis.YP.rotationDegrees((float) (Math.cos((double) event.getEntity().f_19797_ * 7.0) * Math.PI * 1.2F)));
                float vibrate = 0.05F;
                event.getPoseStack().translate((event.getEntity().getRandom().nextFloat() - 0.5F) * vibrate, (event.getEntity().getRandom().nextFloat() - 0.5F) * vibrate, (event.getEntity().getRandom().nextFloat() - 0.5F) * vibrate);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPostRenderEntity(RenderLivingEvent.Post event) {
        if (!RockyChestplateUtil.isRockyRolling(event.getEntity())) {
            if (event.getEntity().hasEffect(AMEffectRegistry.ENDER_FLU.get())) {
                event.getPoseStack().popPose();
            }
            if (event.getEntity().hasEffect(AMEffectRegistry.CLINGING.get()) && event.getEntity().m_20192_() < event.getEntity().m_20206_() * 0.45F || event.getEntity().hasEffect(AMEffectRegistry.DEBILITATING_STING.get()) && event.getEntity().getMobType() == MobType.ARTHROPOD && event.getEntity().m_20205_() > event.getEntity().m_20206_()) {
                event.getPoseStack().popPose();
                event.getEntity().yBodyRotO = -event.getEntity().yBodyRotO;
                event.getEntity().yBodyRot = -event.getEntity().yBodyRot;
                event.getEntity().yHeadRotO = -event.getEntity().yHeadRotO;
                event.getEntity().yHeadRot = -event.getEntity().yHeadRot;
            }
            if (VineLassoUtil.hasLassoData(event.getEntity()) && !(event.getEntity() instanceof Player)) {
                Entity lassoedOwner = VineLassoUtil.getLassoedTo(event.getEntity());
                if (lassoedOwner instanceof LivingEntity && lassoedOwner != event.getEntity()) {
                    double d0 = Mth.lerp((double) event.getPartialTick(), event.getEntity().f_19790_, event.getEntity().m_20185_());
                    double d1 = Mth.lerp((double) event.getPartialTick(), event.getEntity().f_19791_, event.getEntity().m_20186_());
                    double d2 = Mth.lerp((double) event.getPartialTick(), event.getEntity().f_19792_, event.getEntity().m_20189_());
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(-d0, -d1, -d2);
                    RenderVineLasso.renderVine(event.getEntity(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), (LivingEntity) lassoedOwner, ((LivingEntity) lassoedOwner).getMainArm() == HumanoidArm.LEFT, 0.1F);
                    event.getPoseStack().popPose();
                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPoseHand(EventPosePlayerHand event) {
        LivingEntity player = (LivingEntity) event.getEntityIn();
        float f = Minecraft.getInstance().getFrameTime();
        boolean leftHand = false;
        boolean usingLasso = player.isUsingItem() && player.getUseItem().is(AMItemRegistry.VINE_LASSO.get());
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.VINE_LASSO.get()) {
            leftHand = player.getMainArm() == HumanoidArm.LEFT;
        } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.VINE_LASSO.get()) {
            leftHand = player.getMainArm() != HumanoidArm.LEFT;
        }
        if (leftHand && event.isLeftHand() && usingLasso) {
            event.setResult(Result.ALLOW);
            event.getModel().leftArm.xRot = Maths.rad(-120.0) + Mth.sin((float) player.f_19797_ + f) * 0.5F;
            event.getModel().leftArm.yRot = Maths.rad(-20.0) + Mth.cos((float) player.f_19797_ + f) * 0.5F;
        }
        if (!leftHand && !event.isLeftHand() && usingLasso) {
            event.setResult(Result.ALLOW);
            event.getModel().rightArm.xRot = Maths.rad(-120.0) + Mth.sin((float) player.f_19797_ + f) * 0.5F;
            event.getModel().rightArm.yRot = Maths.rad(20.0) - Mth.cos((float) player.f_19797_ + f) * 0.5F;
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderHand(RenderHandEvent event) {
        if (Minecraft.getInstance().getCameraEntity() instanceof IFalconry) {
            event.setCanceled(true);
        }
        if (!Minecraft.getInstance().player.m_20197_().isEmpty() && event.getHand() == InteractionHand.MAIN_HAND) {
            Player player = Minecraft.getInstance().player;
            boolean leftHand = false;
            if (player.m_21120_(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                leftHand = player.getMainArm() == HumanoidArm.LEFT;
            } else if (player.m_21120_(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                leftHand = player.getMainArm() != HumanoidArm.LEFT;
            }
            for (Entity entity : player.m_20197_()) {
                if (entity instanceof IFalconry) {
                    IFalconry falconry = (IFalconry) entity;
                    float yaw = player.f_20884_ + (player.f_20883_ - player.f_20884_) * event.getPartialTick();
                    ClientProxy.currentUnrenderedEntities.remove(entity.getUUID());
                    PoseStack matrixStackIn = event.getPoseStack();
                    matrixStackIn.pushPose();
                    matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                    matrixStackIn.translate(leftHand ? -falconry.getHandOffset() : falconry.getHandOffset(), -0.6F, -1.0F);
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));
                    if (leftHand) {
                        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
                    } else {
                        matrixStackIn.mulPose(Axis.YN.rotationDegrees(90.0F));
                    }
                    this.renderEntity(entity, 0.0, 0.0, 0.0, 0.0F, event.getPartialTick(), matrixStackIn, event.getMultiBufferSource(), event.getPackedLight());
                    matrixStackIn.popPose();
                    ClientProxy.currentUnrenderedEntities.add(entity.getUUID());
                }
            }
        }
        if (Minecraft.getInstance().player.m_21211_().getItem() instanceof ItemDimensionalCarver && event.getItemStack().getItem() instanceof ItemDimensionalCarver) {
            PoseStack matrixStackIn = event.getPoseStack();
            matrixStackIn.pushPose();
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            InteractionHand hand = (InteractionHand) MoreObjects.firstNonNull(Minecraft.getInstance().player.f_20912_, InteractionHand.MAIN_HAND);
            float f = Minecraft.getInstance().player.m_21324_(event.getPartialTick());
            float f5 = -0.4F * Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f6 = 0.2F * Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2));
            float f10 = -0.2F * Mth.sin(f * (float) Math.PI);
            HumanoidArm handside = hand == InteractionHand.MAIN_HAND ? Minecraft.getInstance().player.m_5737_() : Minecraft.getInstance().player.m_5737_().getOpposite();
            boolean flag3 = handside == HumanoidArm.RIGHT;
            int l = flag3 ? 1 : -1;
            matrixStackIn.translate((float) l * f5, f6, f10);
        }
    }

    public <E extends Entity> void renderEntity(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);
            if (render != null) {
                try {
                    render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable var19) {
                    throw new ReportedException(CrashReport.forThrowable(var19, "Rendering entity in world"));
                }
            }
        } catch (Throwable var20) {
            CrashReport crashreport = CrashReport.forThrowable(var20, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", yaw);
            crashreportcategory1.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderNameplate(RenderNameTagEvent event) {
        if (Minecraft.getInstance().getCameraEntity() instanceof EntityBaldEagle && event.getEntity() == Minecraft.getInstance().player && Minecraft.getInstance().hasSingleplayerServer()) {
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderWorldLastEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            if (!AMConfig.shadersCompat) {
                if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.LAVA_VISION.get())) {
                    if (!this.previousLavaVision) {
                        this.previousFluidRenderer = Minecraft.getInstance().getBlockRenderer().liquidBlockRenderer;
                        Minecraft.getInstance().getBlockRenderer().liquidBlockRenderer = new LavaVisionFluidRenderer();
                        this.updateAllChunks();
                    }
                } else if (this.previousLavaVision) {
                    if (this.previousFluidRenderer != null) {
                        Minecraft.getInstance().getBlockRenderer().liquidBlockRenderer = this.previousFluidRenderer;
                    }
                    this.updateAllChunks();
                }
                this.previousLavaVision = Minecraft.getInstance().player.m_21023_(AMEffectRegistry.LAVA_VISION.get());
                if (AMConfig.clingingFlipEffect) {
                    if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.CLINGING.get()) && Minecraft.getInstance().player.m_20192_() < Minecraft.getInstance().player.m_20206_() * 0.45F) {
                        Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("shaders/post/flip.json"));
                    } else if (Minecraft.getInstance().gameRenderer.currentEffect() != null && Minecraft.getInstance().gameRenderer.currentEffect().getName().equals("minecraft:shaders/post/flip.json")) {
                        Minecraft.getInstance().gameRenderer.shutdownEffect();
                    }
                }
            }
            if (Minecraft.getInstance().getCameraEntity() instanceof EntityBaldEagle) {
                EntityBaldEagle eagle = (EntityBaldEagle) Minecraft.getInstance().getCameraEntity();
                LocalPlayer playerEntity = Minecraft.getInstance().player;
                if (!((EntityBaldEagle) Minecraft.getInstance().getCameraEntity()).shouldHoodedReturn() && !eagle.m_213877_()) {
                    float rotX = Mth.wrapDegrees(playerEntity.m_146908_() + playerEntity.f_20885_);
                    float rotY = playerEntity.m_146909_();
                    Entity over = null;
                    if (Minecraft.getInstance().hitResult instanceof EntityHitResult) {
                        over = ((EntityHitResult) Minecraft.getInstance().hitResult).getEntity();
                    } else {
                        Minecraft.getInstance().hitResult = null;
                    }
                    boolean loadChunks = playerEntity.m_9236_().getDayTime() % 10L == 0L;
                    ((EntityBaldEagle) Minecraft.getInstance().getCameraEntity()).directFromPlayer(rotX, rotY, false, over);
                    AlexsMobs.NETWORK_WRAPPER.sendToServer(new MessageUpdateEagleControls(Minecraft.getInstance().getCameraEntity().getId(), rotX, rotY, loadChunks, over == null ? -1 : over.getId()));
                } else {
                    Minecraft.getInstance().setCameraEntity(playerEntity);
                    Minecraft.getInstance().options.setCameraType(CameraType.values()[AlexsMobs.PROXY.getPreviousPOV()]);
                }
            }
        }
    }

    private void updateAllChunks() {
        if (Minecraft.getInstance().levelRenderer.viewArea != null) {
            int length = Minecraft.getInstance().levelRenderer.viewArea.chunks.length;
            for (int i = 0; i < length; i++) {
                Minecraft.getInstance().levelRenderer.viewArea.chunks[i].dirty = true;
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onGetFluidRenderType(EventGetFluidRenderType event) {
        if (Minecraft.getInstance().player.m_21023_(AMEffectRegistry.LAVA_VISION.get()) && (event.getFluidState().is(Fluids.LAVA) || event.getFluidState().is(Fluids.FLOWING_LAVA))) {
            event.setRenderType(RenderType.translucent());
            event.setResult(Result.ALLOW);
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            AMItemstackRenderer.incrementTick();
        }
    }

    @SubscribeEvent
    public void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (Minecraft.getInstance().player.m_21124_(AMEffectRegistry.EARTHQUAKE.get()) != null && !Minecraft.getInstance().isPaused()) {
            int duration = Minecraft.getInstance().player.m_21124_(AMEffectRegistry.EARTHQUAKE.get()).getDuration();
            float f = ((float) Math.min(10, duration) + Minecraft.getInstance().getFrameTime()) * 0.1F;
            double intensity = (double) f * Minecraft.getInstance().options.screenEffectScale().get();
            RandomSource rng = Minecraft.getInstance().player.m_217043_();
            event.getCamera().move((double) (rng.nextFloat() * 0.1F) * intensity, (double) (rng.nextFloat() * 0.2F) * intensity, (double) (rng.nextFloat() * 0.4F) * intensity);
        }
    }

    @SubscribeEvent
    public void onPostGameOverlay(RenderGuiOverlayEvent.Post event) {
        if (renderStaticScreenFor > 0) {
            if (Minecraft.getInstance().player.m_6084_() && this.lastStaticTick != Minecraft.getInstance().level.m_46467_()) {
                renderStaticScreenFor--;
            }
            float staticLevel = (float) renderStaticScreenFor / 60.0F;
            if (event.getOverlay().id().equals(VanillaGuiOverlay.HELMET.id())) {
                float screenWidth = (float) event.getWindow().getScreenWidth();
                float screenHeight = (float) event.getWindow().getScreenHeight();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                float ageInTicks = (float) Minecraft.getInstance().level.m_46467_() + event.getPartialTick();
                float staticIndexX = (float) Math.sin((double) (ageInTicks * 0.2F)) * 2.0F;
                float staticIndexY = (float) Math.cos((double) (ageInTicks * 0.2F + 3.0F)) * 2.0F;
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, staticLevel);
                RenderSystem.setShaderTexture(0, AMRenderTypes.STATIC_TEXTURE);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tesselator.getBuilder();
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                float minU = 10.0F * staticIndexX * 0.125F;
                float maxU = 10.0F * (0.5F + staticIndexX * 0.125F);
                float minV = 10.0F * staticIndexY * 0.125F;
                float maxV = 10.0F * (0.125F + staticIndexY * 0.125F);
                bufferbuilder.m_5483_(0.0, (double) screenHeight, -190.0).uv(minU, maxV).endVertex();
                bufferbuilder.m_5483_((double) screenWidth, (double) screenHeight, -190.0).uv(maxU, maxV).endVertex();
                bufferbuilder.m_5483_((double) screenWidth, 0.0, -190.0).uv(maxU, minV).endVertex();
                bufferbuilder.m_5483_(0.0, 0.0, -190.0).uv(minU, minV).endVertex();
                tesselator.end();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.lastStaticTick = Minecraft.getInstance().level.m_46467_();
        }
    }
}