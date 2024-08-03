package com.mna.network;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.capabilities.entity.MAPFXProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.IAnimPacketSync;
import com.mna.network.messages.to_client.AuraSyncMessageToClient;
import com.mna.network.messages.to_client.CantripCastingTimerMessage;
import com.mna.network.messages.to_client.ClientboundPlayerFocusDistanceMessage;
import com.mna.network.messages.to_client.CloudstepJumpMessage;
import com.mna.network.messages.to_client.EnderDiscGuiDimensionCycle;
import com.mna.network.messages.to_client.EntityStateMessage;
import com.mna.network.messages.to_client.ExtendedSlotContainerContentsMessage;
import com.mna.network.messages.to_client.ExtendedSlotContentsMessage;
import com.mna.network.messages.to_client.MAPFXMessage;
import com.mna.network.messages.to_client.MagicSyncMessageToClient;
import com.mna.network.messages.to_client.MindVisionMessage;
import com.mna.network.messages.to_client.PosessionMessage;
import com.mna.network.messages.to_client.ProgressionSyncMessageToClient;
import com.mna.network.messages.to_client.RespondLootTableItems;
import com.mna.network.messages.to_client.RoteProgressSyncMessageToClient;
import com.mna.network.messages.to_client.SetIcarianDataMessage;
import com.mna.network.messages.to_client.SetLiftPositionMessage;
import com.mna.network.messages.to_client.SetLiftSpeedMessage;
import com.mna.network.messages.to_client.SetRitualCollectedReagentsMessage;
import com.mna.network.messages.to_client.ShowDidYouKnow;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import com.mna.network.messages.to_client.SpawnParticleMessage;
import com.mna.network.messages.to_client.StructureSyncMessage;
import com.mna.network.messages.to_client.WanderingWizardInventoryMessage;
import com.mna.network.messages.to_client.WellspringPowerNetworkSyncMessage;
import com.mna.network.messages.to_client.WellspringSyncMessage;
import com.mna.network.messages.to_server.InscriptionTableCraftingUpdateMessage;
import com.mna.recipes.multiblock.MultiblockDefinition;
import com.mna.tools.TeleportHelper;
import com.mna.tools.loot.LootDrop;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class ServerMessageDispatcher {

    public static void sendVelocityPacketToAllNearby(Entity entity) {
        MAPacketHandler.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new ClientboundSetEntityMotionPacket(entity));
    }

    public static void sendMagicSyncMessage(ServerPlayer player) {
        LazyOptional<IPlayerMagic> magic = player.getCapability(PlayerMagicProvider.MAGIC);
        if (magic.isPresent()) {
            MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), MagicSyncMessageToClient.fromCapability(magic.orElse(null)));
        }
    }

    public static void sendEnderDiscGuiDimensionCycle(ServerPlayer player, ResourceLocation dimensionID) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new EnderDiscGuiDimensionCycle(dimensionID));
    }

    public static void sendRespondLootTableItems(ServerPlayer player, ResourceLocation lootTableID, List<LootDrop> drops) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new RespondLootTableItems(lootTableID, drops));
    }

    public static void sendDidYouKnow(ServerPlayer player, String message) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new ShowDidYouKnow(message));
    }

    public static void sendSetIcarianData(LivingEntity living, Vec3 vel) {
        MAPacketHandler.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> living), new SetIcarianDataMessage(living.m_19879_(), vel));
        if (living instanceof ServerPlayer) {
            MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) living), new SetIcarianDataMessage(living.m_19879_(), vel));
        }
    }

    public static void sendStructureSyncMessage(MultiblockDefinition structure, ServerPlayer player) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), StructureSyncMessage.fromMultiblock(structure, (ServerLevel) player.m_9236_()));
    }

    public static void sendEntityStateMessage(IAnimPacketSync<?> entity) {
        MAPacketHandler.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> (Entity) entity), EntityStateMessage.fromEntity((IAnimPacketSync<? extends Entity>) entity));
    }

    public static void sendEntityStateMessage(IAnimPacketSync<?> entity, ServerPlayer player) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), EntityStateMessage.fromEntity((IAnimPacketSync<? extends Entity>) entity));
    }

    public static void sendSetLiftSpeedMessage(ServerPlayer player, float speed) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new SetLiftSpeedMessage(player.m_19879_(), speed));
    }

    public static void sendMAPFXMessage(LivingEntity entity, ServerPlayer player) {
        entity.getCapability(MAPFXProvider.MAPFX).ifPresent(p -> MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new MAPFXMessage(entity.m_19879_(), p.getFlags())));
    }

    public static void sendMAPFXMessage(LivingEntity entity) {
        entity.getCapability(MAPFXProvider.MAPFX).ifPresent(p -> MAPacketHandler.network.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MAPFXMessage(entity.m_19879_(), p.getFlags())));
    }

    public static void sendPlayerFocusDistanceChange(ServerPlayer player, float delta, float maximum) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new ClientboundPlayerFocusDistanceMessage(delta, maximum));
    }

    public static void sendCloudstepJumpMessage(ServerPlayer sendingPlayer) {
        sendingPlayer.getCapability(MAPFXProvider.MAPFX).ifPresent(p -> MAPacketHandler.network.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sendingPlayer), new CloudstepJumpMessage(sendingPlayer.m_19879_())));
    }

    public static void sendProgressionSyncMessage(ServerPlayer player) {
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), ProgressionSyncMessageToClient.fromCapability(p)));
    }

    public static void sendRoteSyncMessage(ServerPlayer player) {
        player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), RoteProgressSyncMessageToClient.fromCapability(r)));
    }

    public static void sendInscriptionTableCraftingUpdate(InscriptionTableTile te) {
        ServerLevel world = (ServerLevel) te.getLevel();
        BlockPos pos = te.getBlockPos();
        MAPacketHandler.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) world.m_46865_(pos)), InscriptionTableCraftingUpdateMessage.fromInscriptionTable(te));
    }

    public static void sendRitualReagentData(CompoundTag ritualReagentData, int entityID, ServerLevel world, BlockPos pos) {
        MAPacketHandler.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) world.m_46865_(pos)), new SetRitualCollectedReagentsMessage(ritualReagentData, entityID));
    }

    public static void sendParticleSpawn(double x, double y, double z, double vX, double vY, double vZ, int color, float radius, ResourceKey<Level> dimension, MAParticleType type) {
        SpawnParticleMessage msg = new SpawnParticleMessage(x, y, z, vX, vY, vZ, color, ForgeRegistries.PARTICLE_TYPES.getKey(type));
        MAPacketHandler.network.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, (double) radius, dimension)), msg);
    }

    public static void sendParticleEffect(ResourceKey<Level> dimension, float radius, double x, double y, double z, SpawnParticleEffectMessage.ParticleTypes type) {
        sendParticleEffect(dimension, radius, x, y, z, type, null);
    }

    public static void sendParticleEffect(ResourceKey<Level> dimension, float radius, double x, double y, double z, SpawnParticleEffectMessage.ParticleTypes type, CompoundTag meta) {
        SpawnParticleEffectMessage msg = new SpawnParticleEffectMessage(x, y, z, type, meta);
        MAPacketHandler.network.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, (double) radius, dimension)), msg);
    }

    public static void sendSetLiftPosition(double x, double y, double z, ServerPlayer player) {
        SetLiftPositionMessage msg = new SetLiftPositionMessage(x, y, z);
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendCantripTimerMessage(String id, int ticks, ServerPlayer player) {
        CantripCastingTimerMessage msg = new CantripCastingTimerMessage(id, ticks);
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendWanderingWizardContainerMessage(int containerID, MerchantOffers offers, int level, int xp, boolean hasXPBar, boolean canRestock, boolean isFinal, ServerPlayer player) {
        WanderingWizardInventoryMessage msg = new WanderingWizardInventoryMessage(containerID, offers, level, xp, hasXPBar, canRestock, isFinal);
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendWellspringPowerNetworkSyncMessage(ResourceKey<Level> targetDimension, ServerPlayer player, boolean fullSync) {
        ServerLevel targetWorld = TeleportHelper.resolveRegistryKey((ServerLevel) player.m_9236_(), targetDimension);
        sendWellspringPowerNetworkSyncMessage(targetWorld, player, fullSync);
    }

    public static void sendWellspringPowerNetworkSyncMessage(ServerLevel world, ServerPlayer player, boolean fullSync) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), WellspringPowerNetworkSyncMessage.from(world, player, fullSync));
    }

    public static void sendWellspringSyncMessage(ServerLevel world, ServerPlayer player) {
        sendWellspringSyncMessage(world, player, 64);
    }

    public static void sendWellspringSyncMessage(ServerLevel world, ServerPlayer player, int radius) {
        CompoundTag nbt = new CompoundTag();
        world.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
            if (m.getWellspringRegistry().writeToNBT(nbt, player.m_20183_(), radius)) {
                MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new WellspringSyncMessage(nbt));
            }
        });
    }

    public static void sendPlayerMindVisionMessage(ServerPlayer player, Entity entity) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new MindVisionMessage(entity != null ? entity.getId() : -1));
    }

    public static void sendPlayerPosessionMessage(ServerPlayer player, Entity entity) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new PosessionMessage(entity.getId()));
    }

    public static void sendExtendedItemStack(ServerPlayer player, int screenID, int stateID, int slotID, ItemStack stack) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new ExtendedSlotContentsMessage(screenID, stateID, slotID, stack));
    }

    public static void sendExtendedInitialContainer(ServerPlayer player, int screenID, int stateID, NonNullList<ItemStack> items, ItemStack carried) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> player), new ExtendedSlotContainerContentsMessage(screenID, stateID, items, carried));
    }

    public static void sendAuraSyncMessage(ServerPlayer player) {
        MAPacketHandler.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), AuraSyncMessageToClient.fromPlayer(player));
    }

    public static void sendAuraSyncMessage(ServerPlayer sendTo, ServerPlayer dataFor) {
        MAPacketHandler.network.send(PacketDistributor.PLAYER.with(() -> sendTo), AuraSyncMessageToClient.fromPlayer(dataFor));
    }

    public static void sendAuraSyncMessage(ParticleEmitterTile tile) {
        MAPacketHandler.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> (LevelChunk) tile.m_58904_().m_46865_(tile.m_58899_())), AuraSyncMessageToClient.fromTile(tile));
    }
}