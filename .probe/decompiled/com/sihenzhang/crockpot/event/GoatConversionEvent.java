package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "crockpot")
public class GoatConversionEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGoatStruckByLightning(EntityStruckByLightningEvent event) {
        LightningBolt lightning = event.getLightning();
        if (lightning.m_9236_() instanceof ServerLevel level && event.getEntity() instanceof Goat goat && !event.isCanceled() && ForgeEventFactory.canLivingConvert(goat, CrockPotEntities.VOLT_GOAT.get(), timer -> {
        })) {
            VoltGoat voltGoat = CrockPotEntities.VOLT_GOAT.get().create(level);
            if (voltGoat != null) {
                voltGoat.m_7678_(goat.m_20185_(), goat.m_20186_(), goat.m_20189_(), goat.m_146908_(), goat.m_146909_());
                voltGoat.setLastLightningBolt(lightning.m_20148_());
                voltGoat.m_21557_(goat.m_21525_());
                voltGoat.m_6863_(goat.m_6162_());
                if (goat.m_8077_()) {
                    voltGoat.m_6593_(goat.m_7770_());
                    voltGoat.m_20340_(goat.m_20151_());
                }
                voltGoat.m_21530_();
                ForgeEventFactory.onLivingConvert(goat, voltGoat);
                level.addFreshEntity(voltGoat);
                goat.m_146870_();
            }
        }
    }
}