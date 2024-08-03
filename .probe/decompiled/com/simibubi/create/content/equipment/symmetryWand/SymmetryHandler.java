package com.simibubi.create.content.equipment.symmetryWand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.symmetryWand.mirror.EmptyMirror;
import com.simibubi.create.content.equipment.symmetryWand.mirror.SymmetryMirror;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.joml.Vector3f;

@EventBusSubscriber(bus = Bus.FORGE)
public class SymmetryHandler {

    private static int tickCounter = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!event.getLevel().m_5776_()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                Inventory inv = player.getInventory();
                for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                    if (!inv.getItem(i).isEmpty() && inv.getItem(i).getItem() == AllItems.WAND_OF_SYMMETRY.get()) {
                        SymmetryWandItem.apply(player.m_9236_(), inv.getItem(i), player, event.getPos(), event.getPlacedBlock());
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockDestroyed(BlockEvent.BreakEvent event) {
        if (!event.getLevel().m_5776_()) {
            Player player = event.getPlayer();
            Inventory inv = player.getInventory();
            for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                if (!inv.getItem(i).isEmpty() && AllItems.WAND_OF_SYMMETRY.isIn(inv.getItem(i))) {
                    SymmetryWandItem.remove(player.m_9236_(), inv.getItem(i), player, event.getPos());
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            RandomSource random = RandomSource.create();
            for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                ItemStack stackInSlot = player.m_150109_().getItem(i);
                if (AllItems.WAND_OF_SYMMETRY.isIn(stackInSlot) && SymmetryWandItem.isEnabled(stackInSlot)) {
                    SymmetryMirror mirror = SymmetryWandItem.getMirror(stackInSlot);
                    if (!(mirror instanceof EmptyMirror)) {
                        BlockPos pos = BlockPos.containing(mirror.getPosition());
                        float yShift = 0.0F;
                        double speed = 0.0625;
                        yShift = Mth.sin((float) ((double) AnimationTickHolder.getRenderTime() * speed)) / 5.0F;
                        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
                        Camera info = mc.gameRenderer.getMainCamera();
                        Vec3 view = info.getPosition();
                        PoseStack ms = event.getPoseStack();
                        ms.pushPose();
                        ms.translate((double) pos.m_123341_() - view.x(), (double) pos.m_123342_() - view.y(), (double) pos.m_123343_() - view.z());
                        ms.translate(0.0F, yShift + 0.2F, 0.0F);
                        mirror.applyModelTransform(ms);
                        BakedModel model = mirror.getModel().get();
                        VertexConsumer builder = buffer.getBuffer(RenderType.solid());
                        mc.getBlockRenderer().getModelRenderer().tesselateBlock(player.m_9236_(), model, Blocks.AIR.defaultBlockState(), pos, ms, builder, true, random, Mth.getSeed(pos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
                        ms.popPose();
                        buffer.endBatch();
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (mc.level != null) {
                if (!mc.isPaused()) {
                    tickCounter++;
                    if (tickCounter % 10 == 0) {
                        for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                            ItemStack stackInSlot = player.m_150109_().getItem(i);
                            if (stackInSlot != null && AllItems.WAND_OF_SYMMETRY.isIn(stackInSlot) && SymmetryWandItem.isEnabled(stackInSlot)) {
                                SymmetryMirror mirror = SymmetryWandItem.getMirror(stackInSlot);
                                if (!(mirror instanceof EmptyMirror)) {
                                    Random r = new Random();
                                    double offsetX = (r.nextDouble() - 0.5) * 0.3;
                                    double offsetZ = (r.nextDouble() - 0.5) * 0.3;
                                    Vec3 pos = mirror.getPosition().add(0.5 + offsetX, 0.25, 0.5 + offsetZ);
                                    Vec3 speed = new Vec3(0.0, r.nextDouble() * 1.0 / 8.0, 0.0);
                                    mc.level.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void drawEffect(BlockPos from, BlockPos to) {
        double density = 0.8F;
        Vec3 start = Vec3.atLowerCornerOf(from).add(0.5, 0.5, 0.5);
        Vec3 end = Vec3.atLowerCornerOf(to).add(0.5, 0.5, 0.5);
        Vec3 diff = end.subtract(start);
        Vec3 step = diff.normalize().scale(density);
        int steps = (int) (diff.length() / step.length());
        Random r = new Random();
        for (int i = 3; i < steps - 1; i++) {
            Vec3 pos = start.add(step.scale((double) i));
            Vec3 speed = new Vec3(0.0, r.nextDouble() * -40.0, 0.0);
            Minecraft.getInstance().level.addParticle(new DustParticleOptions(new Vector3f(1.0F, 1.0F, 1.0F), 1.0F), pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
        }
        Vec3 speed = new Vec3(0.0, r.nextDouble() * 1.0 / 32.0, 0.0);
        Vec3 pos = start.add(step.scale(2.0));
        Minecraft.getInstance().level.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
        speed = new Vec3(0.0, r.nextDouble() * 1.0 / 32.0, 0.0);
        pos = start.add(step.scale((double) steps));
        Minecraft.getInstance().level.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
    }
}