package com.mna.entities.renderers.living;

import com.mna.effects.EffectInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class LivingRenderEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onLivingRenderPre(RenderLivingEvent.Pre<?, ?> event) {
        if (event.getEntity() != null) {
            if (event.getEntity().getEffect(EffectInit.TRUE_INVISIBILITY.get()) == null && event.getEntity().getEffect(EffectInit.MIST_FORM.get()) == null) {
                try {
                    float curScale = event.getEntity().getPersistentData().getFloat("mna_entity_scale");
                    float prevScale = event.getEntity().getPersistentData().getFloat("mna_entity_scale_prev");
                    if (curScale == 0.0F || prevScale == 0.0F) {
                        curScale = 1.0F;
                        prevScale = 1.0F;
                    }
                    float partial = event.getPartialTick();
                    float scale = curScale + (curScale - prevScale) * partial;
                    event.getPoseStack().scale(scale, scale, scale);
                    if (event.getEntity().m_20163_()) {
                        event.getPoseStack().translate(0.0, 0.125, 0.0);
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            } else {
                event.setResult(Result.DENY);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onLivingRenderPost(RenderLivingEvent.Post<?, ?> event) {
    }
}