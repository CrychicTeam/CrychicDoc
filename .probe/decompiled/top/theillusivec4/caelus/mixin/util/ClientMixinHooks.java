package top.theillusivec4.caelus.mixin.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.caelus.api.RenderCapeEvent;
import top.theillusivec4.caelus.common.network.CaelusNetwork;

public class ClientMixinHooks {

    public static boolean checkFlight() {
        LocalPlayer playerEntity = Minecraft.getInstance().player;
        if (playerEntity != null) {
            CaelusApi.TriState flag = CaelusApi.getInstance().canFallFly(playerEntity);
            if (flag == CaelusApi.TriState.DENY) {
                return false;
            }
            if (!playerEntity.m_20096_() && !playerEntity.m_21255_() && !playerEntity.m_20069_() && !playerEntity.m_21023_(MobEffects.LEVITATION) && flag == CaelusApi.TriState.ALLOW) {
                playerEntity.m_36320_();
                CaelusNetwork.sendFlightC2S();
            }
        }
        return true;
    }

    public static boolean canRenderCape(Player playerEntity) {
        return !MinecraftForge.EVENT_BUS.post(new RenderCapeEvent(playerEntity));
    }
}