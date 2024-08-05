package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.living.ZLivingTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class HorsesSwimModule extends ZetaModule {

    @PlayEvent
    public void tick(ZLivingTick event) {
        if (event.getEntity() instanceof AbstractHorse honse) {
            boolean ridden = !honse.m_20197_().isEmpty();
            boolean water = honse.m_20069_();
            if (ridden && water) {
                boolean tallWater = honse.m_9236_().m_46801_(honse.m_20183_().below());
                if (tallWater) {
                    honse.m_6478_(MoverType.PLAYER, new Vec3(0.0, 0.1, 0.0));
                }
            }
        }
    }
}