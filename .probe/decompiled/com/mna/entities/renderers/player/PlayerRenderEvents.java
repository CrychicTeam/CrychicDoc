package com.mna.entities.renderers.player;

import com.mna.items.ItemInit;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class PlayerRenderEvents {

    private static boolean addedElytraLayer = false;

    private static boolean addedSpellLayer = false;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        if (!addedElytraLayer) {
            event.getRenderer().m_115326_(new SpectralElytraLayer<>(event.getRenderer(), Minecraft.getInstance().getEntityModels()));
            addedElytraLayer = true;
        }
        if (!addedSpellLayer) {
            event.getRenderer().m_115326_(new HandParticleLayer<>(event.getRenderer()));
            addedSpellLayer = true;
        }
        if (event.getEntity().getPersistentData().getBoolean("eldrin_flight")) {
            event.setResult(Result.DENY);
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onPlayerRenderPost(RenderPlayerEvent.Post event) {
        if (!event.getEntity().getPersistentData().getBoolean("eldrin_flight")) {
            CuriosApi.getCuriosHelper().findFirstCurio(event.getEntity(), ItemInit.ELDRITCH_ORB.get()).ifPresent(t -> {
                Vector3f translation = new Vector3f(1.0F, 2.0F, 0.0F);
                Quaternionf rotation = Axis.YN.rotation((float) ((double) (MathUtils.lerpf(event.getEntity().f_20886_, event.getEntity().f_20885_, event.getPartialTick()) / 180.0F) * Math.PI));
                translation.rotate(rotation);
                event.getPoseStack().pushPose();
                event.getPoseStack().translate(translation.x(), translation.y(), translation.z());
                WorldRenderUtils.renderRadiant(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), new int[] { 0, 51, 102 }, new int[] { 179, 217, 255 }, 255, 0.01F);
                event.getPoseStack().popPose();
            });
        }
    }
}