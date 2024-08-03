package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.gui.inscription_table.network.ServerboundInscribeSpell;
import io.redspace.ironsspellbooks.gui.inscription_table.network.ServerboundInscriptionTableSelectSpell;
import io.redspace.ironsspellbooks.gui.overlays.network.ServerboundSelectSpell;
import io.redspace.ironsspellbooks.gui.scroll_forge.network.ServerboundScrollForgeSelectSpell;
import io.redspace.ironsspellbooks.network.ClientBoundRemoveRecast;
import io.redspace.ironsspellbooks.network.ClientBoundSyncRecast;
import io.redspace.ironsspellbooks.network.ClientboundAddMotionToPlayer;
import io.redspace.ironsspellbooks.network.ClientboundCastErrorMessage;
import io.redspace.ironsspellbooks.network.ClientboundEntityEvent;
import io.redspace.ironsspellbooks.network.ClientboundEquipmentChanged;
import io.redspace.ironsspellbooks.network.ClientboundGuidingBoltManagerStartTracking;
import io.redspace.ironsspellbooks.network.ClientboundGuidingBoltManagerStopTracking;
import io.redspace.ironsspellbooks.network.ClientboundOpenEldritchScreen;
import io.redspace.ironsspellbooks.network.ClientboundSyncAnimation;
import io.redspace.ironsspellbooks.network.ClientboundSyncCameraShake;
import io.redspace.ironsspellbooks.network.ClientboundSyncCooldown;
import io.redspace.ironsspellbooks.network.ClientboundSyncCooldowns;
import io.redspace.ironsspellbooks.network.ClientboundSyncEntityData;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.network.ClientboundSyncPlayerData;
import io.redspace.ironsspellbooks.network.ClientboundSyncRecasts;
import io.redspace.ironsspellbooks.network.ClientboundUpdateCastingState;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.network.ServerboundCast;
import io.redspace.ironsspellbooks.network.ServerboundLearnSpell;
import io.redspace.ironsspellbooks.network.ServerboundQuickCast;
import io.redspace.ironsspellbooks.network.spell.ClientboundAborptionParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundBloodSiphonParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundFieryExplosionParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundFortifyAreaParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundFrostStepParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundHealParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundOakskinParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundOnCastFinished;
import io.redspace.ironsspellbooks.network.spell.ClientboundOnCastStarted;
import io.redspace.ironsspellbooks.network.spell.ClientboundOnClientCast;
import io.redspace.ironsspellbooks.network.spell.ClientboundParticleShockwave;
import io.redspace.ironsspellbooks.network.spell.ClientboundRegenCloudParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundSyncTargetingData;
import io.redspace.ironsspellbooks.network.spell.ClientboundTeleportParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("irons_spellbooks", "messages")).networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE = net;
        net.messageBuilder(ClientboundUpdateCastingState.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundUpdateCastingState::new).encoder(ClientboundUpdateCastingState::toBytes).consumerMainThread(ClientboundUpdateCastingState::handle).add();
        net.messageBuilder(ClientboundAddMotionToPlayer.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundAddMotionToPlayer::new).encoder(ClientboundAddMotionToPlayer::toBytes).consumerMainThread(ClientboundAddMotionToPlayer::handle).add();
        net.messageBuilder(ClientboundSyncMana.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncMana::new).encoder(ClientboundSyncMana::toBytes).consumerMainThread(ClientboundSyncMana::handle).add();
        net.messageBuilder(ClientboundOnClientCast.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundOnClientCast::new).encoder(ClientboundOnClientCast::toBytes).consumerMainThread(ClientboundOnClientCast::handle).add();
        net.messageBuilder(ClientboundSyncPlayerData.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncPlayerData::new).encoder(ClientboundSyncPlayerData::toBytes).consumerMainThread(ClientboundSyncPlayerData::handle).add();
        net.messageBuilder(ClientboundSyncEntityData.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncEntityData::new).encoder(ClientboundSyncEntityData::toBytes).consumerMainThread(ClientboundSyncEntityData::handle).add();
        net.messageBuilder(ServerboundInscribeSpell.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundInscribeSpell::new).encoder(ServerboundInscribeSpell::toBytes).consumerMainThread(ServerboundInscribeSpell::handle).add();
        net.messageBuilder(ClientboundSyncCooldown.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncCooldown::new).encoder(ClientboundSyncCooldown::toBytes).consumerMainThread(ClientboundSyncCooldown::handle).add();
        net.messageBuilder(ClientboundSyncCooldowns.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncCooldowns::new).encoder(ClientboundSyncCooldowns::toBytes).consumerMainThread(ClientboundSyncCooldowns::handle).add();
        net.messageBuilder(ClientboundSyncRecasts.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncRecasts::new).encoder(ClientboundSyncRecasts::toBytes).consumerMainThread(ClientboundSyncRecasts::handle).add();
        net.messageBuilder(ClientBoundSyncRecast.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientBoundSyncRecast::new).encoder(ClientBoundSyncRecast::toBytes).consumerMainThread(ClientBoundSyncRecast::handle).add();
        net.messageBuilder(ClientBoundRemoveRecast.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientBoundRemoveRecast::new).encoder(ClientBoundRemoveRecast::toBytes).consumerMainThread(ClientBoundRemoveRecast::handle).add();
        net.messageBuilder(ClientboundTeleportParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundTeleportParticles::new).encoder(ClientboundTeleportParticles::toBytes).consumerMainThread(ClientboundTeleportParticles::handle).add();
        net.messageBuilder(ClientboundFrostStepParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundFrostStepParticles::new).encoder(ClientboundFrostStepParticles::toBytes).consumerMainThread(ClientboundFrostStepParticles::handle).add();
        net.messageBuilder(ServerboundScrollForgeSelectSpell.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundScrollForgeSelectSpell::new).encoder(ServerboundScrollForgeSelectSpell::toBytes).consumerMainThread(ServerboundScrollForgeSelectSpell::handle).add();
        net.messageBuilder(ServerboundInscriptionTableSelectSpell.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundInscriptionTableSelectSpell::new).encoder(ServerboundInscriptionTableSelectSpell::toBytes).consumerMainThread(ServerboundInscriptionTableSelectSpell::handle).add();
        net.messageBuilder(ServerboundCancelCast.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundCancelCast::new).encoder(ServerboundCancelCast::toBytes).consumerMainThread(ServerboundCancelCast::handle).add();
        net.messageBuilder(ServerboundQuickCast.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundQuickCast::new).encoder(ServerboundQuickCast::toBytes).consumerMainThread(ServerboundQuickCast::handle).add();
        net.messageBuilder(ClientboundHealParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundHealParticles::new).encoder(ClientboundHealParticles::toBytes).consumerMainThread(ClientboundHealParticles::handle).add();
        net.messageBuilder(ClientboundBloodSiphonParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundBloodSiphonParticles::new).encoder(ClientboundBloodSiphonParticles::toBytes).consumerMainThread(ClientboundBloodSiphonParticles::handle).add();
        net.messageBuilder(ClientboundRegenCloudParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundRegenCloudParticles::new).encoder(ClientboundRegenCloudParticles::toBytes).consumerMainThread(ClientboundRegenCloudParticles::handle).add();
        net.messageBuilder(ClientboundOnCastStarted.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundOnCastStarted::new).encoder(ClientboundOnCastStarted::toBytes).consumerMainThread(ClientboundOnCastStarted::handle).add();
        net.messageBuilder(ClientboundOnCastFinished.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundOnCastFinished::new).encoder(ClientboundOnCastFinished::toBytes).consumerMainThread(ClientboundOnCastFinished::handle).add();
        net.messageBuilder(ClientboundAborptionParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundAborptionParticles::new).encoder(ClientboundAborptionParticles::toBytes).consumerMainThread(ClientboundAborptionParticles::handle).add();
        net.messageBuilder(ClientboundFortifyAreaParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundFortifyAreaParticles::new).encoder(ClientboundFortifyAreaParticles::toBytes).consumerMainThread(ClientboundFortifyAreaParticles::handle).add();
        net.messageBuilder(ClientboundSyncTargetingData.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncTargetingData::new).encoder(ClientboundSyncTargetingData::toBytes).consumerMainThread(ClientboundSyncTargetingData::handle).add();
        net.messageBuilder(ClientboundCastErrorMessage.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundCastErrorMessage::new).encoder(ClientboundCastErrorMessage::toBytes).consumerMainThread(ClientboundCastErrorMessage::handle).add();
        net.messageBuilder(ClientboundSyncAnimation.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncAnimation::new).encoder(ClientboundSyncAnimation::toBytes).consumerMainThread(ClientboundSyncAnimation::handle).add();
        net.messageBuilder(ClientboundOakskinParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundOakskinParticles::new).encoder(ClientboundOakskinParticles::toBytes).consumerMainThread(ClientboundOakskinParticles::handle).add();
        net.messageBuilder(ClientboundSyncCameraShake.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundSyncCameraShake::new).encoder(ClientboundSyncCameraShake::toBytes).consumerMainThread(ClientboundSyncCameraShake::handle).add();
        net.messageBuilder(ClientboundEquipmentChanged.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundEquipmentChanged::new).encoder(ClientboundEquipmentChanged::toBytes).consumerMainThread(ClientboundEquipmentChanged::handle).add();
        net.messageBuilder(ServerboundLearnSpell.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundLearnSpell::new).encoder(ServerboundLearnSpell::toBytes).consumerMainThread(ServerboundLearnSpell::handle).add();
        net.messageBuilder(ServerboundSelectSpell.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundSelectSpell::new).encoder(ServerboundSelectSpell::toBytes).consumerMainThread(ServerboundSelectSpell::handle).add();
        net.messageBuilder(ServerboundCast.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ServerboundCast::new).encoder(ServerboundCast::toBytes).consumerMainThread(ServerboundCast::handle).add();
        net.messageBuilder(ClientboundOpenEldritchScreen.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundOpenEldritchScreen::new).encoder(ClientboundOpenEldritchScreen::toBytes).consumerMainThread(ClientboundOpenEldritchScreen::handle).add();
        net.messageBuilder(ClientboundFieryExplosionParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundFieryExplosionParticles::new).encoder(ClientboundFieryExplosionParticles::toBytes).consumerMainThread(ClientboundFieryExplosionParticles::handle).add();
        net.messageBuilder(ClientboundEntityEvent.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundEntityEvent::new).encoder(ClientboundEntityEvent::toBytes).consumerMainThread(ClientboundEntityEvent::handle).add();
        net.messageBuilder(ClientboundGuidingBoltManagerStartTracking.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundGuidingBoltManagerStartTracking::new).encoder(ClientboundGuidingBoltManagerStartTracking::toBytes).consumerMainThread(ClientboundGuidingBoltManagerStartTracking::handle).add();
        net.messageBuilder(ClientboundGuidingBoltManagerStopTracking.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundGuidingBoltManagerStopTracking::new).encoder(ClientboundGuidingBoltManagerStopTracking::toBytes).consumerMainThread(ClientboundGuidingBoltManagerStopTracking::handle).add();
        net.messageBuilder(ClientboundParticleShockwave.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ClientboundParticleShockwave::new).encoder(ClientboundParticleShockwave::toBytes).consumerMainThread(ClientboundParticleShockwave::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if (sendToSource && entity instanceof ServerPlayer serverPlayer) {
            sendToPlayer(message, serverPlayer);
        }
    }
}