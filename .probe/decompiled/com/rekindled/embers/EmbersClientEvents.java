package com.rekindled.embers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.augment.IAugment;
import com.rekindled.embers.api.block.IDial;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IMechanicallyPowered;
import com.rekindled.embers.blockentity.EmberEmitterBlockEntity;
import com.rekindled.embers.blockentity.render.AtmosphericBellowsBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.AutomaticHammerBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.EmberBoreBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.EntropicEnumeratorBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.ExcavationBucketsBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.InfernoForgeTopBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.MechanicalPumpBlockEntityRenderer;
import com.rekindled.embers.blockentity.render.StamperBlockEntityRenderer;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.render.EmbersRenderTypes;
import com.rekindled.embers.upgrade.ExcavationBucketsUpgrade;
import com.rekindled.embers.util.EmberGenUtil;
import com.rekindled.embers.util.GlowingTextTooltip;
import com.rekindled.embers.util.HeatBarTooltip;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

@OnlyIn(Dist.CLIENT)
public class EmbersClientEvents {

    public static final IGuiOverlay INGAME_OVERLAY = EmbersClientEvents::renderIngameOverlay;

    public static int ticks = 0;

    public static double gaugeAngle = 0.0;

    public static long seed = 0L;

    public static BlockPos lastTarget = null;

    public static ResourceLocation GAUGE = new ResourceLocation("embers", "textures/gui/ember_meter_overlay.png");

    public static ResourceLocation GAUGE_POINTER = new ResourceLocation("embers", "textures/gui/ember_meter_pointer.png");

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            if (!mc.isPaused()) {
                ticks++;
                if (mc.hitResult instanceof BlockHitResult result) {
                    Level world = mc.level;
                    if (result != null && world != null && result.getType() == HitResult.Type.BLOCK) {
                        BlockState state = world.getBlockState(result.getBlockPos());
                        if (state.m_60734_() instanceof IDial) {
                            ((IDial) state.m_60734_()).updateBEData(result.getBlockPos(), Math.max(0, (mc.getWindow().getScreenHeight() / 2 - 100) / 11));
                        }
                    }
                }
            }
        }
    }

    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (event.getEntity().m_6117_() && !event.getEntity().m_20159_() && event.getEntity().m_21120_(event.getEntity().m_7655_()).is(EmbersItemTags.NORMAL_WALK_SPEED_TOOL)) {
            event.getInput().forwardImpulse /= 0.2F;
            event.getInput().leftImpulse /= 0.2F;
            if (event.getEntity().m_20142_()) {
                event.getEntity().m_6858_(false);
            }
        }
    }

    public static void onBlockHighlight(RenderHighlightEvent.Block event) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui) {
            Pair<BlockPos, Direction> target = Misc.getHammerTarget(mc.player);
            if (target != null && event.getTarget().getBlockPos().equals(target.getLeft())) {
                event.setCanceled(true);
            }
        }
    }

    public static void onLevelRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.options.hideGui) {
                return;
            }
            Player player = mc.player;
            Pair<BlockPos, Direction> target = Misc.getHammerTarget(player);
            if (target != null && player.m_9236_().isLoaded((BlockPos) target.getLeft())) {
                BlockPos targetPos = (BlockPos) target.getLeft();
                Direction targetDir = (Direction) target.getRight();
                Vec3 camPos = event.getCamera().getPosition();
                VertexConsumer consumer = mc.renderBuffers().bufferSource().getBuffer(EmbersRenderTypes.GLOW_LINES);
                float red = 1.0F;
                float green = 0.25F + 0.5F * ((float) Math.sin(Math.toRadians((double) (4.0F * ((float) event.getRenderTick() + event.getPartialTick())))) + 1.0F) * 0.25F;
                float blue = 0.062745F;
                float alpha = 0.8F;
                double x = (double) targetPos.m_123341_() - camPos.x;
                double y = (double) targetPos.m_123342_() - camPos.y;
                double z = (double) targetPos.m_123343_() - camPos.z;
                PoseStack.Pose pose = event.getPoseStack().last();
                Shapes.DoubleLineConsumer lineDrawer = (fromX, fromY, fromZ, toX, toY, toZ) -> {
                    float f = (float) (toX - fromX);
                    float f1 = (float) (toY - fromY);
                    float f2 = (float) (toZ - fromZ);
                    float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
                    f /= f3;
                    f1 /= f3;
                    f2 /= f3;
                    consumer.vertex(pose.pose(), (float) (fromX + x), (float) (fromY + y), (float) (fromZ + z)).color(red, green, blue, alpha).normal(pose.normal(), f, f1, f2).endVertex();
                    consumer.vertex(pose.pose(), (float) (toX + x), (float) (toY + y), (float) (toZ + z)).color(red, green, blue, alpha).normal(pose.normal(), f, f1, f2).endVertex();
                };
                player.m_9236_().getBlockState(targetPos).m_60808_(player.m_9236_(), targetPos).forAllEdges(lineDrawer);
                if (mc.hitResult instanceof BlockHitResult result && result != null && result.getType() == HitResult.Type.BLOCK && !result.getBlockPos().equals(targetPos) && mc.level.m_7702_(result.getBlockPos()) instanceof IEmberPacketReceiver) {
                    lastTarget = result.getBlockPos();
                }
                if (lastTarget != null) {
                    Vec3 hitPos = Vec3.atCenterOf(lastTarget.subtract(targetPos));
                    Vec3 motion = EmberEmitterBlockEntity.getBurstVelocity(targetDir);
                    Vec3 oldPos = new Vec3(0.5, 0.5, 0.5);
                    Vec3 newPos = oldPos.add(motion);
                    for (int i = 0; i <= 80; i++) {
                        Vec3 targetVector = hitPos.subtract(newPos);
                        double length = targetVector.length();
                        targetVector = targetVector.scale(0.3 / length);
                        double weight = 0.0;
                        if (length <= 3.0) {
                            weight = 0.9 * ((3.0 - length) / 3.0);
                            if (length <= 0.2) {
                                break;
                            }
                        }
                        motion = new Vec3((0.9 - weight) * motion.x + (0.1 + weight) * targetVector.x, (0.9 - weight) * motion.y + (0.1 + weight) * targetVector.y, (0.9 - weight) * motion.z + (0.1 + weight) * targetVector.z);
                        newPos = oldPos.add(motion);
                        lineDrawer.consume(oldPos.x, oldPos.y, oldPos.z, newPos.x, newPos.y, newPos.z);
                        oldPos = newPos;
                    }
                } else {
                    lineDrawer.consume(0.5, 0.5, 0.5, 0.5 + (double) targetDir.getStepX(), 0.5 + (double) targetDir.getStepY(), 0.5 + (double) targetDir.getStepZ());
                }
            } else {
                lastTarget = null;
            }
        }
    }

    public static void renderIngameOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = gui.getMinecraft();
        if (!mc.options.hideGui) {
            Player player = mc.player;
            if (mc.hitResult instanceof BlockHitResult result) {
                ClientLevel world = mc.level;
                if (result != null && result.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = result.getBlockPos();
                    BlockState state = world.m_8055_(pos);
                    Direction facing = result.getDirection();
                    List<Component> text = new ArrayList();
                    if (state.m_60734_() instanceof IDial) {
                        text.addAll(((IDial) state.m_60734_()).getDisplayInfo(world, result.getBlockPos(), state, Math.max(0, (height / 2 - 100) / 11)));
                    } else if (state.m_60734_() == RegistryManager.ATMOSPHERIC_GAUGE.get()) {
                        renderAtmosphericGauge(gui, graphics, player, partialTicks, width, height);
                    } else if (Misc.isWearingLens(player)) {
                        BlockEntity tileEntity = world.m_7702_(result.getBlockPos());
                        if (tileEntity != null) {
                            addCapabilityInformation(text, state, tileEntity, facing);
                        }
                    }
                    if (!text.isEmpty()) {
                        for (int i = 0; i < text.size(); i++) {
                            graphics.drawString(mc.font, (Component) text.get(i), width / 2 - mc.font.width((FormattedText) text.get(i)) / 2, height / 2 + 40 + 11 * i, 16777215);
                        }
                    }
                }
            }
            if (player.m_21205_().getItem() == RegistryManager.ATMOSPHERIC_GAUGE_ITEM.get() || player.m_21206_().getItem() == RegistryManager.ATMOSPHERIC_GAUGE_ITEM.get()) {
                renderAtmosphericGauge(gui, graphics, player, partialTicks, width, height);
            }
        }
    }

    public static void renderAtmosphericGauge(ForgeGui gui, GuiGraphics graphics, Player player, float partialTicks, int width, int height) {
        int x = width / 2;
        int y = height / 2;
        graphics.pose().pushPose();
        graphics.blit(GAUGE, x - 16, y - 16, 0, 0.0F, 0.0F, 32, 32, 32, 32);
        if (player != null) {
            double ratio = (double) EmberGenUtil.getEmberDensity(seed, player.m_146903_(), player.m_146907_());
            if (gaugeAngle == 0.0) {
                gaugeAngle = 165.0 + 210.0 * ratio;
            } else {
                gaugeAngle = gaugeAngle * 0.99 + 0.01 * (165.0 + 210.0 * ratio);
            }
        }
        graphics.pose().translate((float) x, (float) y, 0.0F);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees((float) gaugeAngle));
        graphics.pose().translate(-2.5, -2.5, 0.0);
        graphics.blit(GAUGE_POINTER, 0, 0, 0, 0.0F, 0.0F, 12, 5, 16, 16);
        graphics.pose().popPose();
    }

    private static void addCapabilityInformation(List<Component> text, BlockState state, BlockEntity tile, Direction facing) {
        addCapabilityItemDescription(text, tile, facing);
        addCapabilityFluidDescription(text, tile, facing);
        addCapabilityEmberDescription(text, tile, facing);
        if (tile.getCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY, facing).isPresent()) {
            text.add(Component.translatable("embers.tooltip.goggles.upgrade"));
        }
        if (Misc.isSideProxyable(state, facing)) {
            text.add(Component.translatable("embers.tooltip.goggles.accessor_slot"));
        }
        if (tile instanceof IMechanicallyPowered) {
            text.add(Component.translatable("embers.tooltip.goggles.actuator_slot"));
        }
        if (tile instanceof IExtraCapabilityInformation) {
            ((IExtraCapabilityInformation) tile).addOtherDescription(text, facing);
        }
    }

    public static void addCapabilityItemDescription(List<Component> text, BlockEntity tile, Direction facing) {
        Capability<IItemHandler> capability = ForgeCapabilities.ITEM_HANDLER;
        if (tile.getCapability(capability, facing).isPresent()) {
            IExtraCapabilityInformation.EnumIOType ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
            Component filter = null;
            if (tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability, facing);
            } else {
                text.add(IExtraCapabilityInformation.formatCapability(ioType, "embers.tooltip.goggles.item", filter));
            }
        }
    }

    public static void addCapabilityFluidDescription(List<Component> text, BlockEntity tile, Direction facing) {
        Capability<IFluidHandler> capability = ForgeCapabilities.FLUID_HANDLER;
        if (tile.getCapability(capability, facing).isPresent()) {
            IExtraCapabilityInformation.EnumIOType ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
            Component filter = null;
            if (tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability, facing);
            } else {
                text.add(IExtraCapabilityInformation.formatCapability(ioType, "embers.tooltip.goggles.fluid", filter));
            }
        }
    }

    public static void addCapabilityEmberDescription(List<Component> text, BlockEntity tile, Direction facing) {
        Capability<IEmberCapability> capability = EmbersCapabilities.EMBER_CAPABILITY;
        if (tile.getCapability(capability, facing).isPresent()) {
            IExtraCapabilityInformation.EnumIOType ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
            if (tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability, facing);
            } else {
                text.add(IExtraCapabilityInformation.formatCapability(ioType, "embers.tooltip.goggles.ember", null));
            }
        }
    }

    public static void afterModelBake(ModelEvent.BakingCompleted event) {
        ModelBakery bakery = event.getModelManager().getModelBakery();
        EmberBoreBlockEntityRenderer.blades = getModel(bakery, "ember_bore_blades");
        MechanicalPumpBlockEntityRenderer.pistonBottom = getModel(bakery, "mechanical_pump_piston_bottom");
        MechanicalPumpBlockEntityRenderer.pistonTop = getModel(bakery, "mechanical_pump_piston_top");
        StamperBlockEntityRenderer.arm = getModel(bakery, "stamper_arm");
        AutomaticHammerBlockEntityRenderer.hammer = getModel(bakery, "automatic_hammer_end");
        InfernoForgeTopBlockEntityRenderer.hatch = getModel(bakery, "inferno_forge_hatch");
        AtmosphericBellowsBlockEntityRenderer.top = getModel(bakery, "atmospheric_bellows_top");
        AtmosphericBellowsBlockEntityRenderer.leather = getModel(bakery, "atmospheric_bellows_leather");
        EntropicEnumeratorBlockEntityRenderer.cubies[0][0][0] = getModel(bakery, "entropic_enumerator_drf");
        EntropicEnumeratorBlockEntityRenderer.cubies[1][0][0] = getModel(bakery, "entropic_enumerator_dlf");
        EntropicEnumeratorBlockEntityRenderer.cubies[0][1][0] = getModel(bakery, "entropic_enumerator_urf");
        EntropicEnumeratorBlockEntityRenderer.cubies[1][1][0] = getModel(bakery, "entropic_enumerator_ulf");
        EntropicEnumeratorBlockEntityRenderer.cubies[0][0][1] = getModel(bakery, "entropic_enumerator_drb");
        EntropicEnumeratorBlockEntityRenderer.cubies[1][0][1] = getModel(bakery, "entropic_enumerator_dlb");
        EntropicEnumeratorBlockEntityRenderer.cubies[0][1][1] = getModel(bakery, "entropic_enumerator_urb");
        EntropicEnumeratorBlockEntityRenderer.cubies[1][1][1] = getModel(bakery, "entropic_enumerator_ulb");
        ExcavationBucketsBlockEntityRenderer.wheel = getModel(bakery, "excavation_buckets_wheel");
        ExcavationBucketsUpgrade.buckets = getModel(bakery, "ember_bore_excavation_buckets");
    }

    public static BakedModel getModel(ModelBakery bakery, String name) {
        ResourceLocation location = new ResourceLocation("embers", "block/" + name);
        ModelBakery.ModelBakerImpl bakerImpl = bakery.new ModelBakerImpl((modelLoc, material) -> material.sprite(), location);
        UnbakedModel model = bakery.getModel(location);
        return model.bake(bakerImpl, Material::m_119204_, BlockModelRotation.X0_Y0, location);
    }

    public static void onTooltip(RenderTooltipEvent.GatherComponents event) {
        if (AugmentUtil.hasHeat(event.getItemStack())) {
            event.getTooltipElements().add(Either.left(Component.empty()));
            if (AugmentUtil.getLevel(event.getItemStack()) > 0) {
                event.getTooltipElements().add(Either.right(new GlowingTextTooltip(Component.translatable("embers.tooltip.heat_level").withStyle(ChatFormatting.GRAY).getVisualOrderText(), Component.literal(AugmentUtil.getLevel(event.getItemStack()) + "").getVisualOrderText())));
                int slots = AugmentUtil.getLevel(event.getItemStack()) - AugmentUtil.getTotalAugmentLevel(event.getItemStack());
                if (slots > 0) {
                    event.getTooltipElements().add(Either.right(new GlowingTextTooltip(Component.translatable("embers.tooltip.augment_slots").withStyle(ChatFormatting.GRAY).getVisualOrderText(), Component.literal(slots + "").getVisualOrderText())));
                }
            }
            float heat = AugmentUtil.getHeat(event.getItemStack());
            float maxHeat = AugmentUtil.getMaxHeat(event.getItemStack());
            event.getTooltipElements().add(Either.right(new HeatBarTooltip(Component.translatable("embers.tooltip.heat_amount").withStyle(ChatFormatting.GRAY).getVisualOrderText(), heat, maxHeat)));
            if (Minecraft.getInstance().options.advancedItemTooltips) {
                event.getTooltipElements().add(Either.left(Component.translatable("embers.tooltip.heat_debug", heat, maxHeat).withStyle(ChatFormatting.DARK_GRAY)));
            }
            List<IAugment> augments = (List<IAugment>) AugmentUtil.getAugments(event.getItemStack()).stream().filter(x -> x.shouldRenderTooltip()).collect(Collectors.toList());
            if (augments.size() > 0) {
                event.getTooltipElements().add(Either.left(Component.translatable("embers.tooltip.augments").withStyle(ChatFormatting.GRAY)));
                for (IAugment augment : augments) {
                    int level = AugmentUtil.getAugmentLevel(event.getItemStack(), augment);
                    event.getTooltipElements().add(Either.right(new GlowingTextTooltip(Component.translatable("embers.tooltip.augment." + augment.getName().toLanguageKey(), Component.translatable(getFormattedModifierLevel(level))).getVisualOrderText())));
                }
            }
        }
    }

    public static String getFormattedModifierLevel(int level) {
        String key = "embers.tooltip.num" + level;
        return I18n.exists(key) ? key : "embers.tooltip.numstop";
    }
}