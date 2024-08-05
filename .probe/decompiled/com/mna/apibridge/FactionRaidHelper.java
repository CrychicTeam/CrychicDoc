package com.mna.apibridge;

import com.mna.api.faction.IFaction;
import com.mna.api.faction.IRaidHelper;
import com.mna.entities.faction.util.FactionRaid;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FactionRaidHelper implements IRaidHelper {

    @Override
    public boolean spawnRaidAt(Player target, IFaction faction, int strength, Vec3 position, boolean friendly, MobEffectInstance... effects) {
        if (target.m_9236_().isClientSide()) {
            return false;
        } else {
            FactionRaid fre = new FactionRaid(target.m_9236_(), target, strength);
            fre.setFaction(faction);
            fre.m_6034_(position.x, position.y, position.z);
            if (friendly) {
                fre.setProtective(target);
            }
            if (effects != null && effects.length > 0) {
                fre.setAdditionalEffects(effects);
            }
            target.m_9236_().m_7967_(fre);
            return true;
        }
    }
}