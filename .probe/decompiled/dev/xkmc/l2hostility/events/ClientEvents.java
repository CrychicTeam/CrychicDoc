package dev.xkmc.l2hostility.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkClearRenderer;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MasterData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.traits.EnchantmentDisabler;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.ClientEffectRenderEvents;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.joml.Matrix4f;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2hostility", bus = Bus.FORGE)
public class ClientEvents {

    private static boolean renderChunk = false;

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null) {
            EnchantmentDisabler.modifyTooltip(event.getItemStack(), event.getToolTip(), event.getEntity().m_9236_());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void renderNamePlate(RenderNameTagEvent event) {
        if (event.getEntity() instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
            LocalPlayer player = Proxy.getClientPlayer();
            assert player != null;
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(le);
            boolean needHover = le.m_20145_() || LHConfig.CLIENT.showOnlyWhenHovered.get();
            if (needHover && RayTraceUtil.rayTraceEntity(player, player.getEntityReach(), ex -> ex == le) == null) {
                return;
            }
            List<Component> list = cap.getTitle(LHConfig.CLIENT.showLevelOverHead.get(), LHConfig.CLIENT.showTraitOverHead.get());
            int offset = list.size();
            float off = (float) LHConfig.CLIENT.overHeadRenderOffset.get().doubleValue();
            Font.DisplayMode mode = player.m_142582_(event.getEntity()) ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL;
            for (Component e : list) {
                renderNameTag(event, e, event.getPoseStack(), ((float) offset + off) * 0.2F, mode);
                offset--;
            }
        }
    }

    protected static void renderNameTag(RenderNameTagEvent event, Component text, PoseStack pose, float offset, Font.DisplayMode mode) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        double d0 = dispatcher.distanceToSqr(event.getEntity());
        int max = LHConfig.CLIENT.overHeadRenderDistance.get();
        int light = LHConfig.CLIENT.overHeadRenderFullBright.get() ? 15728880 : event.getPackedLight();
        if (d0 < (double) (max * max)) {
            float f = event.getEntity().getNameTagOffsetY() + offset;
            pose.pushPose();
            pose.translate(0.0F, f, 0.0F);
            pose.mulPose(dispatcher.cameraOrientation());
            pose.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pose.last().pose();
            Font font = event.getEntityRenderer().getFont();
            float f2 = (float) (-font.width(text) / 2);
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (f1 * 255.0F) << 24;
            font.drawInBatch(text, f2, 0.0F, -1, false, matrix4f, event.getMultiBufferSource(), mode, j, light);
            pose.popPose();
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            LocalPlayer player = Proxy.getClientPlayer();
            if (player != null && player.f_19797_ % 2 == 0) {
                renderChunk = CurioCompat.hasItemInCurioOrSlot(player, (Item) LHItems.DETECTOR_GLASSES.get()) && CurioCompat.hasItemInCurioOrSlot(player, (Item) LHItems.DETECTOR.get());
            }
        }
    }

    @SubscribeEvent
    public static void onLevelRenderLast(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            Optional<ChunkDifficulty> opt = ChunkDifficulty.at(player.m_9236_(), player.m_20183_());
            if (opt.isEmpty()) {
                return;
            }
            if (!renderChunk) {
                return;
            }
            ChunkClearRenderer.render(event.getPoseStack(), player, (ChunkDifficulty) opt.get(), event.getPartialTick());
        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            LevelRenderer renderer = event.getLevelRenderer();
            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer cons = buffers.getBuffer(ClientEffectRenderEvents.get2DIcon(new ResourceLocation("l2hostility:textures/entity/chain.png")));
            PoseStack pose = event.getPoseStack();
            pose.pushPose();
            Vec3 cam = event.getCamera().getPosition();
            pose.translate(-cam.x, -cam.y, -cam.z);
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                for (Entity e : level.entitiesForRendering()) {
                    if (e instanceof Mob) {
                        Mob mob = (Mob) e;
                        if (e.isAlive() && MobTraitCap.HOLDER.isProper(mob)) {
                            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
                            if (cap.asMaster != null) {
                                Vec3 p0 = e.position().add(0.0, (double) (e.getBbHeight() / 2.0F), 0.0);
                                for (MasterData.Minion minions : cap.asMaster.data) {
                                    Mob m = minions.minion;
                                    if (m != null && m.m_6084_()) {
                                        MobTraitCap scap = (MobTraitCap) MobTraitCap.HOLDER.get(m);
                                        if (scap.asMinion != null) {
                                            Vec3 p1 = m.m_20182_().add(0.0, (double) (m.m_20206_() / 2.0F), 0.0);
                                            renderLink(event.getPoseStack(), cons, p0, p1, scap.asMinion.protectMaster);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            pose.popPose();
        }
    }

    private static void renderLink(PoseStack pose, VertexConsumer cons, Vec3 p0, Vec3 p1, boolean protect) {
        Vec3 vec3 = p1.subtract(p0);
        float len = (float) vec3.length();
        if (!(len < 0.2F)) {
            pose.pushPose();
            pose.translate(p0.x, p0.y, p0.z);
            double d0 = vec3.horizontalDistance();
            pose.mulPose(Axis.YP.rotation((float) Mth.atan2(vec3.x, vec3.z)));
            pose.mulPose(Axis.XP.rotation((float) ((Math.PI / 2) - Mth.atan2(vec3.y, d0))));
            float r = 0.125F;
            float off = protect ? 0.5F : 0.0F;
            renderQuad(pose.last(), cons, 0.0F, len, -r, r, 0.0F, 0.0F, off, off + 0.25F, 0.0F, len);
            renderQuad(pose.last(), cons, 0.0F, len, r, -r, 0.0F, 0.0F, off, off + 0.25F, 0.0F, len);
            renderQuad(pose.last(), cons, 0.0F, len, 0.0F, 0.0F, -r, r, off + 0.25F, off + 0.5F, 0.0F, len);
            renderQuad(pose.last(), cons, 0.0F, len, 0.0F, 0.0F, r, -r, off + 0.25F, off + 0.5F, 0.0F, len);
            pose.popPose();
        }
    }

    private static void renderQuad(PoseStack.Pose entry, VertexConsumer vc, float y0, float y1, float x0, float x1, float z0, float z1, float u0, float u1, float v0, float v1) {
        vertex(entry, vc, x0, y1, z0, u1, v0);
        vertex(entry, vc, x0, y0, z0, u1, v1);
        vertex(entry, vc, x1, y0, z1, u0, v1);
        vertex(entry, vc, x1, y1, z1, u0, v0);
    }

    private static void vertex(PoseStack.Pose entry, VertexConsumer vc, float x, float y, float z, float u, float v) {
        vc.vertex(entry.pose(), x, y, z).uv(u, v).normal(entry.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }
}