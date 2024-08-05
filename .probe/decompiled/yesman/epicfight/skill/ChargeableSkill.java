package yesman.epicfight.skill;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public interface ChargeableSkill {

    void startCharging(PlayerPatch<?> var1);

    void resetCharging(PlayerPatch<?> var1);

    int getAllowedMaxChargingTicks();

    int getMaxChargingTicks();

    int getMinChargingTicks();

    default void chargingTick(PlayerPatch<?> caster) {
        caster.setChargingAmount(caster.getChargingAmount() + 1);
    }

    default int getChargingAmount(PlayerPatch<?> caster) {
        return caster.getChargingAmount();
    }

    void castSkill(ServerPlayerPatch var1, SkillContainer var2, int var3, SPSkillExecutionFeedback var4, boolean var5);

    @OnlyIn(Dist.CLIENT)
    void gatherChargingArguemtns(LocalPlayerPatch var1, ControllEngine var2, FriendlyByteBuf var3);

    @OnlyIn(Dist.CLIENT)
    KeyMapping getKeyMapping();

    default Skill asSkill() {
        return (Skill) this;
    }
}