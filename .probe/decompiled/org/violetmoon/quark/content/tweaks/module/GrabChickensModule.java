package org.violetmoon.quark.content.tweaks.module;

import java.util.List;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.play.entity.ZEntityInteract;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class GrabChickensModule extends ZetaModule {

    @Config
    private static boolean needsNoHelmet = true;

    @Config(description = "Set to 0 to disable")
    private static int slownessLevel = 1;

    private static boolean staticEnabled;

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    @PlayEvent
    public void playerInteract(ZEntityInteract event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        Level level = event.getLevel();
        if (staticEnabled && event.getHand() == InteractionHand.MAIN_HAND && !player.m_6047_() && !(player instanceof FakePlayer) && player.m_21205_().isEmpty() && this.canPlayerHostChicken(player) && target.getType() == EntityType.CHICKEN && !((Chicken) target).m_6162_()) {
            List<Entity> passengers = player.m_20197_();
            boolean changed = false;
            if (passengers.contains(target)) {
                if (!level.isClientSide) {
                    target.stopRiding();
                }
                changed = true;
            } else if (passengers.isEmpty()) {
                if (!level.isClientSide) {
                    target.startRiding(player, false);
                }
                changed = true;
            }
            if (changed) {
                if (level instanceof ServerLevel slevel) {
                    slevel.getChunkSource().chunkMap.broadcast(target, new ClientboundSetPassengersPacket(player));
                } else {
                    player.m_6674_(InteractionHand.MAIN_HAND);
                }
            }
        }
    }

    @PlayEvent
    public void playerTick(ZPlayerTick.Start event) {
        Player player = event.getPlayer();
        Level level = player.m_9236_();
        if (player.m_146862_(e -> e.getType() == EntityType.CHICKEN)) {
            if (!this.canPlayerHostChicken(player)) {
                player.m_20153_();
                if (level instanceof ServerLevel slevel) {
                    slevel.getChunkSource().chunkMap.broadcast(player, new ClientboundSetPassengersPacket(player));
                }
            } else {
                player.m_7292_(new MobEffectInstance(MobEffects.SLOW_FALLING, 5, 0, true, false));
                if (slownessLevel > 0) {
                    player.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, slownessLevel - 1, true, false));
                }
            }
        }
    }

    private boolean canPlayerHostChicken(Player player) {
        return (!needsNoHelmet || player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) && !player.m_5842_();
    }

    public static class Client {

        public static void setRenderChickenFeetStatus(Chicken entity, ChickenModel<Chicken> model) {
            if (GrabChickensModule.staticEnabled) {
                boolean should = entity.m_20202_() == null || entity.m_20202_().getType() != EntityType.PLAYER;
                model.leftLeg.visible = should;
                model.rightLeg.visible = should;
            }
        }
    }
}