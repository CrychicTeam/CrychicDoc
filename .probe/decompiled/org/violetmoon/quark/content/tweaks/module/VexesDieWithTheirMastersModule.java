package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vex;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.living.ZLivingTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class VexesDieWithTheirMastersModule extends ZetaModule {

    @PlayEvent
    public void checkWhetherAlreadyDead(ZLivingTick event) {
        if (event.getEntity() instanceof Vex vex) {
            Mob owner = vex.getOwner();
            if (owner != null && owner.m_21224_() && !vex.m_21224_()) {
                vex.m_6469_(vex.m_9236_().damageSources().mobAttack(owner), vex.m_21223_());
            }
        }
    }
}