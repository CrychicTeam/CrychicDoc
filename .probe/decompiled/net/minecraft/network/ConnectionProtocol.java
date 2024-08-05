package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.network.protocol.BundleDelimiterPacket;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAddExperienceOrbPacket;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundDeleteChatPacket;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ClientboundPingPacket;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateEnabledFeaturesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundChatAckPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.util.VisibleForDebug;
import org.slf4j.Logger;

public enum ConnectionProtocol implements BundlerInfo.Provider {

    HANDSHAKING(-1, protocol().addFlow(PacketFlow.SERVERBOUND, new ConnectionProtocol.PacketSet().addPacket(ClientIntentionPacket.class, ClientIntentionPacket::new))), PLAY(0, protocol().addFlow(PacketFlow.CLIENTBOUND, new ConnectionProtocol.PacketSet().withBundlePacket(ClientboundBundlePacket.class, ClientboundBundlePacket::new).addPacket(ClientboundAddEntityPacket.class, ClientboundAddEntityPacket::new).addPacket(ClientboundAddExperienceOrbPacket.class, ClientboundAddExperienceOrbPacket::new).addPacket(ClientboundAddPlayerPacket.class, ClientboundAddPlayerPacket::new).addPacket(ClientboundAnimatePacket.class, ClientboundAnimatePacket::new).addPacket(ClientboundAwardStatsPacket.class, ClientboundAwardStatsPacket::new).addPacket(ClientboundBlockChangedAckPacket.class, ClientboundBlockChangedAckPacket::new).addPacket(ClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacket::new).addPacket(ClientboundBlockEntityDataPacket.class, ClientboundBlockEntityDataPacket::new).addPacket(ClientboundBlockEventPacket.class, ClientboundBlockEventPacket::new).addPacket(ClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacket::new).addPacket(ClientboundBossEventPacket.class, ClientboundBossEventPacket::new).addPacket(ClientboundChangeDifficultyPacket.class, ClientboundChangeDifficultyPacket::new).addPacket(ClientboundChunksBiomesPacket.class, ClientboundChunksBiomesPacket::new).addPacket(ClientboundClearTitlesPacket.class, ClientboundClearTitlesPacket::new).addPacket(ClientboundCommandSuggestionsPacket.class, ClientboundCommandSuggestionsPacket::new).addPacket(ClientboundCommandsPacket.class, ClientboundCommandsPacket::new).addPacket(ClientboundContainerClosePacket.class, ClientboundContainerClosePacket::new).addPacket(ClientboundContainerSetContentPacket.class, ClientboundContainerSetContentPacket::new).addPacket(ClientboundContainerSetDataPacket.class, ClientboundContainerSetDataPacket::new).addPacket(ClientboundContainerSetSlotPacket.class, ClientboundContainerSetSlotPacket::new).addPacket(ClientboundCooldownPacket.class, ClientboundCooldownPacket::new).addPacket(ClientboundCustomChatCompletionsPacket.class, ClientboundCustomChatCompletionsPacket::new).addPacket(ClientboundCustomPayloadPacket.class, ClientboundCustomPayloadPacket::new).addPacket(ClientboundDamageEventPacket.class, ClientboundDamageEventPacket::new).addPacket(ClientboundDeleteChatPacket.class, ClientboundDeleteChatPacket::new).addPacket(ClientboundDisconnectPacket.class, ClientboundDisconnectPacket::new).addPacket(ClientboundDisguisedChatPacket.class, ClientboundDisguisedChatPacket::new).addPacket(ClientboundEntityEventPacket.class, ClientboundEntityEventPacket::new).addPacket(ClientboundExplodePacket.class, ClientboundExplodePacket::new).addPacket(ClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacket::new).addPacket(ClientboundGameEventPacket.class, ClientboundGameEventPacket::new).addPacket(ClientboundHorseScreenOpenPacket.class, ClientboundHorseScreenOpenPacket::new).addPacket(ClientboundHurtAnimationPacket.class, ClientboundHurtAnimationPacket::new).addPacket(ClientboundInitializeBorderPacket.class, ClientboundInitializeBorderPacket::new).addPacket(ClientboundKeepAlivePacket.class, ClientboundKeepAlivePacket::new).addPacket(ClientboundLevelChunkWithLightPacket.class, ClientboundLevelChunkWithLightPacket::new).addPacket(ClientboundLevelEventPacket.class, ClientboundLevelEventPacket::new).addPacket(ClientboundLevelParticlesPacket.class, ClientboundLevelParticlesPacket::new).addPacket(ClientboundLightUpdatePacket.class, ClientboundLightUpdatePacket::new).addPacket(ClientboundLoginPacket.class, ClientboundLoginPacket::new).addPacket(ClientboundMapItemDataPacket.class, ClientboundMapItemDataPacket::new).addPacket(ClientboundMerchantOffersPacket.class, ClientboundMerchantOffersPacket::new).addPacket(ClientboundMoveEntityPacket.Pos.class, ClientboundMoveEntityPacket.Pos::m_179000_).addPacket(ClientboundMoveEntityPacket.PosRot.class, ClientboundMoveEntityPacket.PosRot::m_179002_).addPacket(ClientboundMoveEntityPacket.Rot.class, ClientboundMoveEntityPacket.Rot::m_179004_).addPacket(ClientboundMoveVehiclePacket.class, ClientboundMoveVehiclePacket::new).addPacket(ClientboundOpenBookPacket.class, ClientboundOpenBookPacket::new).addPacket(ClientboundOpenScreenPacket.class, ClientboundOpenScreenPacket::new).addPacket(ClientboundOpenSignEditorPacket.class, ClientboundOpenSignEditorPacket::new).addPacket(ClientboundPingPacket.class, ClientboundPingPacket::new).addPacket(ClientboundPlaceGhostRecipePacket.class, ClientboundPlaceGhostRecipePacket::new).addPacket(ClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacket::new).addPacket(ClientboundPlayerChatPacket.class, ClientboundPlayerChatPacket::new).addPacket(ClientboundPlayerCombatEndPacket.class, ClientboundPlayerCombatEndPacket::new).addPacket(ClientboundPlayerCombatEnterPacket.class, ClientboundPlayerCombatEnterPacket::new).addPacket(ClientboundPlayerCombatKillPacket.class, ClientboundPlayerCombatKillPacket::new).addPacket(ClientboundPlayerInfoRemovePacket.class, ClientboundPlayerInfoRemovePacket::new).addPacket(ClientboundPlayerInfoUpdatePacket.class, ClientboundPlayerInfoUpdatePacket::new).addPacket(ClientboundPlayerLookAtPacket.class, ClientboundPlayerLookAtPacket::new).addPacket(ClientboundPlayerPositionPacket.class, ClientboundPlayerPositionPacket::new).addPacket(ClientboundRecipePacket.class, ClientboundRecipePacket::new).addPacket(ClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacket::new).addPacket(ClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacket::new).addPacket(ClientboundResourcePackPacket.class, ClientboundResourcePackPacket::new).addPacket(ClientboundRespawnPacket.class, ClientboundRespawnPacket::new).addPacket(ClientboundRotateHeadPacket.class, ClientboundRotateHeadPacket::new).addPacket(ClientboundSectionBlocksUpdatePacket.class, ClientboundSectionBlocksUpdatePacket::new).addPacket(ClientboundSelectAdvancementsTabPacket.class, ClientboundSelectAdvancementsTabPacket::new).addPacket(ClientboundServerDataPacket.class, ClientboundServerDataPacket::new).addPacket(ClientboundSetActionBarTextPacket.class, ClientboundSetActionBarTextPacket::new).addPacket(ClientboundSetBorderCenterPacket.class, ClientboundSetBorderCenterPacket::new).addPacket(ClientboundSetBorderLerpSizePacket.class, ClientboundSetBorderLerpSizePacket::new).addPacket(ClientboundSetBorderSizePacket.class, ClientboundSetBorderSizePacket::new).addPacket(ClientboundSetBorderWarningDelayPacket.class, ClientboundSetBorderWarningDelayPacket::new).addPacket(ClientboundSetBorderWarningDistancePacket.class, ClientboundSetBorderWarningDistancePacket::new).addPacket(ClientboundSetCameraPacket.class, ClientboundSetCameraPacket::new).addPacket(ClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacket::new).addPacket(ClientboundSetChunkCacheCenterPacket.class, ClientboundSetChunkCacheCenterPacket::new).addPacket(ClientboundSetChunkCacheRadiusPacket.class, ClientboundSetChunkCacheRadiusPacket::new).addPacket(ClientboundSetDefaultSpawnPositionPacket.class, ClientboundSetDefaultSpawnPositionPacket::new).addPacket(ClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacket::new).addPacket(ClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacket::new).addPacket(ClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacket::new).addPacket(ClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacket::new).addPacket(ClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacket::new).addPacket(ClientboundSetExperiencePacket.class, ClientboundSetExperiencePacket::new).addPacket(ClientboundSetHealthPacket.class, ClientboundSetHealthPacket::new).addPacket(ClientboundSetObjectivePacket.class, ClientboundSetObjectivePacket::new).addPacket(ClientboundSetPassengersPacket.class, ClientboundSetPassengersPacket::new).addPacket(ClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacket::new).addPacket(ClientboundSetScorePacket.class, ClientboundSetScorePacket::new).addPacket(ClientboundSetSimulationDistancePacket.class, ClientboundSetSimulationDistancePacket::new).addPacket(ClientboundSetSubtitleTextPacket.class, ClientboundSetSubtitleTextPacket::new).addPacket(ClientboundSetTimePacket.class, ClientboundSetTimePacket::new).addPacket(ClientboundSetTitleTextPacket.class, ClientboundSetTitleTextPacket::new).addPacket(ClientboundSetTitlesAnimationPacket.class, ClientboundSetTitlesAnimationPacket::new).addPacket(ClientboundSoundEntityPacket.class, ClientboundSoundEntityPacket::new).addPacket(ClientboundSoundPacket.class, ClientboundSoundPacket::new).addPacket(ClientboundStopSoundPacket.class, ClientboundStopSoundPacket::new).addPacket(ClientboundSystemChatPacket.class, ClientboundSystemChatPacket::new).addPacket(ClientboundTabListPacket.class, ClientboundTabListPacket::new).addPacket(ClientboundTagQueryPacket.class, ClientboundTagQueryPacket::new).addPacket(ClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacket::new).addPacket(ClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacket::new).addPacket(ClientboundUpdateAdvancementsPacket.class, ClientboundUpdateAdvancementsPacket::new).addPacket(ClientboundUpdateAttributesPacket.class, ClientboundUpdateAttributesPacket::new).addPacket(ClientboundUpdateEnabledFeaturesPacket.class, ClientboundUpdateEnabledFeaturesPacket::new).addPacket(ClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacket::new).addPacket(ClientboundUpdateRecipesPacket.class, ClientboundUpdateRecipesPacket::new).addPacket(ClientboundUpdateTagsPacket.class, ClientboundUpdateTagsPacket::new)).addFlow(PacketFlow.SERVERBOUND, new ConnectionProtocol.PacketSet().addPacket(ServerboundAcceptTeleportationPacket.class, ServerboundAcceptTeleportationPacket::new).addPacket(ServerboundBlockEntityTagQuery.class, ServerboundBlockEntityTagQuery::new).addPacket(ServerboundChangeDifficultyPacket.class, ServerboundChangeDifficultyPacket::new).addPacket(ServerboundChatAckPacket.class, ServerboundChatAckPacket::new).addPacket(ServerboundChatCommandPacket.class, ServerboundChatCommandPacket::new).addPacket(ServerboundChatPacket.class, ServerboundChatPacket::new).addPacket(ServerboundChatSessionUpdatePacket.class, ServerboundChatSessionUpdatePacket::new).addPacket(ServerboundClientCommandPacket.class, ServerboundClientCommandPacket::new).addPacket(ServerboundClientInformationPacket.class, ServerboundClientInformationPacket::new).addPacket(ServerboundCommandSuggestionPacket.class, ServerboundCommandSuggestionPacket::new).addPacket(ServerboundContainerButtonClickPacket.class, ServerboundContainerButtonClickPacket::new).addPacket(ServerboundContainerClickPacket.class, ServerboundContainerClickPacket::new).addPacket(ServerboundContainerClosePacket.class, ServerboundContainerClosePacket::new).addPacket(ServerboundCustomPayloadPacket.class, ServerboundCustomPayloadPacket::new).addPacket(ServerboundEditBookPacket.class, ServerboundEditBookPacket::new).addPacket(ServerboundEntityTagQuery.class, ServerboundEntityTagQuery::new).addPacket(ServerboundInteractPacket.class, ServerboundInteractPacket::new).addPacket(ServerboundJigsawGeneratePacket.class, ServerboundJigsawGeneratePacket::new).addPacket(ServerboundKeepAlivePacket.class, ServerboundKeepAlivePacket::new).addPacket(ServerboundLockDifficultyPacket.class, ServerboundLockDifficultyPacket::new).addPacket(ServerboundMovePlayerPacket.Pos.class, ServerboundMovePlayerPacket.Pos::m_179685_).addPacket(ServerboundMovePlayerPacket.PosRot.class, ServerboundMovePlayerPacket.PosRot::m_179687_).addPacket(ServerboundMovePlayerPacket.Rot.class, ServerboundMovePlayerPacket.Rot::m_179689_).addPacket(ServerboundMovePlayerPacket.StatusOnly.class, ServerboundMovePlayerPacket.StatusOnly::m_179697_).addPacket(ServerboundMoveVehiclePacket.class, ServerboundMoveVehiclePacket::new).addPacket(ServerboundPaddleBoatPacket.class, ServerboundPaddleBoatPacket::new).addPacket(ServerboundPickItemPacket.class, ServerboundPickItemPacket::new).addPacket(ServerboundPlaceRecipePacket.class, ServerboundPlaceRecipePacket::new).addPacket(ServerboundPlayerAbilitiesPacket.class, ServerboundPlayerAbilitiesPacket::new).addPacket(ServerboundPlayerActionPacket.class, ServerboundPlayerActionPacket::new).addPacket(ServerboundPlayerCommandPacket.class, ServerboundPlayerCommandPacket::new).addPacket(ServerboundPlayerInputPacket.class, ServerboundPlayerInputPacket::new).addPacket(ServerboundPongPacket.class, ServerboundPongPacket::new).addPacket(ServerboundRecipeBookChangeSettingsPacket.class, ServerboundRecipeBookChangeSettingsPacket::new).addPacket(ServerboundRecipeBookSeenRecipePacket.class, ServerboundRecipeBookSeenRecipePacket::new).addPacket(ServerboundRenameItemPacket.class, ServerboundRenameItemPacket::new).addPacket(ServerboundResourcePackPacket.class, ServerboundResourcePackPacket::new).addPacket(ServerboundSeenAdvancementsPacket.class, ServerboundSeenAdvancementsPacket::new).addPacket(ServerboundSelectTradePacket.class, ServerboundSelectTradePacket::new).addPacket(ServerboundSetBeaconPacket.class, ServerboundSetBeaconPacket::new).addPacket(ServerboundSetCarriedItemPacket.class, ServerboundSetCarriedItemPacket::new).addPacket(ServerboundSetCommandBlockPacket.class, ServerboundSetCommandBlockPacket::new).addPacket(ServerboundSetCommandMinecartPacket.class, ServerboundSetCommandMinecartPacket::new).addPacket(ServerboundSetCreativeModeSlotPacket.class, ServerboundSetCreativeModeSlotPacket::new).addPacket(ServerboundSetJigsawBlockPacket.class, ServerboundSetJigsawBlockPacket::new).addPacket(ServerboundSetStructureBlockPacket.class, ServerboundSetStructureBlockPacket::new).addPacket(ServerboundSignUpdatePacket.class, ServerboundSignUpdatePacket::new).addPacket(ServerboundSwingPacket.class, ServerboundSwingPacket::new).addPacket(ServerboundTeleportToEntityPacket.class, ServerboundTeleportToEntityPacket::new).addPacket(ServerboundUseItemOnPacket.class, ServerboundUseItemOnPacket::new).addPacket(ServerboundUseItemPacket.class, ServerboundUseItemPacket::new))), STATUS(1, protocol().addFlow(PacketFlow.SERVERBOUND, new ConnectionProtocol.PacketSet().addPacket(ServerboundStatusRequestPacket.class, ServerboundStatusRequestPacket::new).addPacket(ServerboundPingRequestPacket.class, ServerboundPingRequestPacket::new)).addFlow(PacketFlow.CLIENTBOUND, new ConnectionProtocol.PacketSet().addPacket(ClientboundStatusResponsePacket.class, ClientboundStatusResponsePacket::new).addPacket(ClientboundPongResponsePacket.class, ClientboundPongResponsePacket::new))), LOGIN(2, protocol().addFlow(PacketFlow.CLIENTBOUND, new ConnectionProtocol.PacketSet().addPacket(ClientboundLoginDisconnectPacket.class, ClientboundLoginDisconnectPacket::new).addPacket(ClientboundHelloPacket.class, ClientboundHelloPacket::new).addPacket(ClientboundGameProfilePacket.class, ClientboundGameProfilePacket::new).addPacket(ClientboundLoginCompressionPacket.class, ClientboundLoginCompressionPacket::new).addPacket(ClientboundCustomQueryPacket.class, ClientboundCustomQueryPacket::new)).addFlow(PacketFlow.SERVERBOUND, new ConnectionProtocol.PacketSet().addPacket(ServerboundHelloPacket.class, ServerboundHelloPacket::new).addPacket(ServerboundKeyPacket.class, ServerboundKeyPacket::new).addPacket(ServerboundCustomQueryPacket.class, ServerboundCustomQueryPacket::new)));

    public static final int NOT_REGISTERED = -1;

    private static final int MIN_PROTOCOL_ID = -1;

    private static final int MAX_PROTOCOL_ID = 2;

    private static final ConnectionProtocol[] LOOKUP = new ConnectionProtocol[4];

    private static final Map<Class<? extends Packet<?>>, ConnectionProtocol> PROTOCOL_BY_PACKET = Maps.newHashMap();

    private final int id;

    private final Map<PacketFlow, ? extends ConnectionProtocol.PacketSet<?>> flows;

    private static ConnectionProtocol.ProtocolBuilder protocol() {
        return new ConnectionProtocol.ProtocolBuilder();
    }

    private ConnectionProtocol(int p_129580_, ConnectionProtocol.ProtocolBuilder p_129581_) {
        this.id = p_129580_;
        this.flows = p_129581_.flows;
    }

    public int getPacketId(PacketFlow p_265671_, Packet<?> p_265442_) {
        return ((ConnectionProtocol.PacketSet) this.flows.get(p_265671_)).getId(p_265442_.getClass());
    }

    @Override
    public BundlerInfo getBundlerInfo(PacketFlow p_265790_) {
        return ((ConnectionProtocol.PacketSet) this.flows.get(p_265790_)).bundlerInfo();
    }

    @VisibleForDebug
    public Int2ObjectMap<Class<? extends Packet<?>>> getPacketsByIds(PacketFlow p_195621_) {
        Int2ObjectMap<Class<? extends Packet<?>>> $$1 = new Int2ObjectOpenHashMap();
        ConnectionProtocol.PacketSet<?> $$2 = (ConnectionProtocol.PacketSet<?>) this.flows.get(p_195621_);
        if ($$2 == null) {
            return Int2ObjectMaps.emptyMap();
        } else {
            $$2.classToId.forEach((p_195611_, p_195612_) -> $$1.put(p_195612_, p_195611_));
            return $$1;
        }
    }

    @Nullable
    public Packet<?> createPacket(PacketFlow p_178322_, int p_178323_, FriendlyByteBuf p_178324_) {
        return ((ConnectionProtocol.PacketSet) this.flows.get(p_178322_)).createPacket(p_178323_, p_178324_);
    }

    public int getId() {
        return this.id;
    }

    @Nullable
    public static ConnectionProtocol getById(int p_129584_) {
        return p_129584_ >= -1 && p_129584_ <= 2 ? LOOKUP[p_129584_ - -1] : null;
    }

    @Nullable
    public static ConnectionProtocol getProtocolForPacket(Packet<?> p_129593_) {
        return (ConnectionProtocol) PROTOCOL_BY_PACKET.get(p_129593_.getClass());
    }

    static {
        for (ConnectionProtocol $$0 : values()) {
            int $$1 = $$0.getId();
            if ($$1 < -1 || $$1 > 2) {
                throw new Error("Invalid protocol ID " + $$1);
            }
            LOOKUP[$$1 - -1] = $$0;
            $$0.flows.forEach((p_264719_, p_264720_) -> p_264720_.listAllPackets(p_195615_ -> {
                if (PROTOCOL_BY_PACKET.containsKey(p_195615_) && PROTOCOL_BY_PACKET.get(p_195615_) != $$0) {
                    throw new IllegalStateException("Packet " + p_195615_ + " is already assigned to protocol " + PROTOCOL_BY_PACKET.get(p_195615_) + " - can't reassign to " + $$0);
                } else {
                    PROTOCOL_BY_PACKET.put(p_195615_, $$0);
                }
            }));
        }
    }

    static class PacketSet<T extends PacketListener> {

        private static final Logger LOGGER = LogUtils.getLogger();

        final Object2IntMap<Class<? extends Packet<T>>> classToId = Util.make(new Object2IntOpenHashMap(), p_129613_ -> p_129613_.defaultReturnValue(-1));

        private final List<Function<FriendlyByteBuf, ? extends Packet<T>>> idToDeserializer = Lists.newArrayList();

        private BundlerInfo bundlerInfo = BundlerInfo.EMPTY;

        private final Set<Class<? extends Packet<T>>> extraClasses = new HashSet();

        public <P extends Packet<T>> ConnectionProtocol.PacketSet<T> addPacket(Class<P> classP0, Function<FriendlyByteBuf, P> functionFriendlyByteBufP1) {
            int $$2 = this.idToDeserializer.size();
            int $$3 = this.classToId.put(classP0, $$2);
            if ($$3 != -1) {
                String $$4 = "Packet " + classP0 + " is already registered to ID " + $$3;
                LOGGER.error(LogUtils.FATAL_MARKER, $$4);
                throw new IllegalArgumentException($$4);
            } else {
                this.idToDeserializer.add(functionFriendlyByteBufP1);
                return this;
            }
        }

        public <P extends BundlePacket<T>> ConnectionProtocol.PacketSet<T> withBundlePacket(Class<P> classP0, Function<Iterable<Packet<T>>, P> functionIterablePacketTP1) {
            if (this.bundlerInfo != BundlerInfo.EMPTY) {
                throw new IllegalStateException("Bundle packet already configured");
            } else {
                BundleDelimiterPacket<T> $$2 = new BundleDelimiterPacket<>();
                this.addPacket((Class<P>) BundleDelimiterPacket.class, p_264723_ -> $$2);
                this.bundlerInfo = BundlerInfo.createForPacket(classP0, functionIterablePacketTP1, $$2);
                this.extraClasses.add(classP0);
                return this;
            }
        }

        public int getId(Class<?> class0) {
            return this.classToId.getInt(class0);
        }

        @Nullable
        public Packet<?> createPacket(int int0, FriendlyByteBuf friendlyByteBuf1) {
            Function<FriendlyByteBuf, ? extends Packet<T>> $$2 = (Function<FriendlyByteBuf, ? extends Packet<T>>) this.idToDeserializer.get(int0);
            return $$2 != null ? (Packet) $$2.apply(friendlyByteBuf1) : null;
        }

        public void listAllPackets(Consumer<Class<? extends Packet<?>>> consumerClassExtendsPacket0) {
            this.classToId.keySet().stream().filter(p_264721_ -> p_264721_ != BundleDelimiterPacket.class).forEach(consumerClassExtendsPacket0);
            this.extraClasses.forEach(consumerClassExtendsPacket0);
        }

        public BundlerInfo bundlerInfo() {
            return this.bundlerInfo;
        }
    }

    static class ProtocolBuilder {

        final Map<PacketFlow, ConnectionProtocol.PacketSet<?>> flows = Maps.newEnumMap(PacketFlow.class);

        public <T extends PacketListener> ConnectionProtocol.ProtocolBuilder addFlow(PacketFlow packetFlow0, ConnectionProtocol.PacketSet<T> connectionProtocolPacketSetT1) {
            this.flows.put(packetFlow0, connectionProtocolPacketSetT1);
            return this;
        }
    }
}