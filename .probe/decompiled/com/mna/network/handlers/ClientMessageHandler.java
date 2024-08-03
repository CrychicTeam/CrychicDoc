package com.mna.network.handlers;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.capabilities.entity.MAPFXProvider;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.entities.IAnimPacketSync;
import com.mna.entities.rituals.Ritual;
import com.mna.gui.HUDOverlayRenderer;
import com.mna.gui.containers.entity.ContainerWanderingWizard;
import com.mna.gui.containers.item.ContainerEnderDisc;
import com.mna.network.messages.BaseMessage;
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
import com.mna.tools.loot.LootTableCache;
import com.mna.tools.math.MathUtils;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientMessageHandler {

    private static <T extends BaseMessage> boolean validateBasics(T message, NetworkEvent.Context ctx) {
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);
        if (sideReceived != LogicalSide.CLIENT) {
            ManaAndArtifice.LOGGER.error(message.getClass().getName() + " received on wrong side: " + sideReceived);
            return false;
        } else if (!message.isMessageValid()) {
            ManaAndArtifice.LOGGER.error(message.getClass().getName() + " was invalid: " + message);
            return false;
        } else {
            return true;
        }
    }

    public static void handleMagicSyncMessage(MagicSyncMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MagicSyncMessageToClient context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> processMagicSyncMessage((Level) clientWorld.get(), message));
            }
        }
    }

    public static void handleEnderDiscGuiDimensionCycle(EnderDiscGuiDimensionCycle message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("EnderDiscGuiDimensionCycle context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> processEnderDiscGuiDimensionCycle(Minecraft.getInstance(), message));
            }
        }
    }

    public static void handleRespondLootTableItems(RespondLootTableItems message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("RespondLootTableItems context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> processRespondLootTableItems(Minecraft.getInstance(), message));
            }
        }
    }

    public static void handleShowDidYouKnow(ShowDidYouKnow message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("ShowDidYouKnow context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> message.Handle());
            }
        }
    }

    public static void handleSetIcarianDataMessage(SetIcarianDataMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("SetIcarianDataMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> message.Handle((Level) clientWorld.get()));
            }
        }
    }

    public static void handleClientboundPlayerFocusDistanceMessage(ClientboundPlayerFocusDistanceMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MagicSyncMessageToClient context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
                    if (player != null) {
                        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.setFocusDistance(message.getDelta()));
                    }
                });
            }
        }
    }

    public static void handleEntityStateMessage(EntityStateMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MagicSyncMessageToClient context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> processEntityStateMessage((Level) clientWorld.get(), message));
            }
        }
    }

    public static void handleProgressionSyncMessage(ProgressionSyncMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("ProgressionSyncMessageToClient context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> processProgressionSyncMessage((Level) clientWorld.get(), message));
            }
        }
    }

    public static void handleRoteSyncMessage(RoteProgressSyncMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("RoteProgressSyncMessageToClient context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> processRoteSyncMessage((Level) clientWorld.get(), message));
            }
        }
    }

    public static void handleInscriptionTableCraftingUpdate(InscriptionTableCraftingUpdateMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MagicSyncMessageToClient context could not provide a ClientWorld");
            } else {
                InscriptionTableTile.handleCraftingUpdate((Level) clientWorld.get(), message);
            }
        }
    }

    public static void handleSpawnParticleMessage(SpawnParticleMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("SpawnParticleMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    ParticleType<?> resolvedType = ForgeRegistries.PARTICLE_TYPES.getValue(message.getType());
                    if (resolvedType != null && resolvedType instanceof MAParticleType) {
                        MAParticleType particle = new MAParticleType((MAParticleType) resolvedType);
                        if (message.getColor() != 0) {
                            int clr = message.getColor();
                            float pct = MathUtils.clamp((float) FastColor.ARGB32.alpha(clr) / 255.0F, 0.1F, 1.0F);
                            particle.setColor((float) FastColor.ARGB32.red(clr) * pct, (float) FastColor.ARGB32.green(clr) * pct, (float) FastColor.ARGB32.blue(clr) * pct);
                        }
                        ((Level) clientWorld.get()).addParticle(particle, (double) message.getPosition().x, (double) message.getPosition().y, (double) message.getPosition().z, (double) message.getSpeed().x, (double) message.getSpeed().y, (double) message.getSpeed().z);
                    }
                });
            }
        }
    }

    public static void handleSpawnParticleEffectMessage(SpawnParticleEffectMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("SpawnParticleMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> message.handle((Level) clientWorld.get()));
            }
        }
    }

    public static void handleSetLiftPositionMessage(SetLiftPositionMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ctx.enqueueWork(() -> {
                Player clientPlayer = ManaAndArtifice.instance.proxy.getClientPlayer();
                LazyOptional<IPlayerMagic> magicCap = clientPlayer.getCapability(PlayerMagicProvider.MAGIC);
                if (magicCap.isPresent()) {
                    IPlayerMagic magicProperties = magicCap.orElse(null);
                    if (magicProperties != null) {
                        magicProperties.setLiftPosition(message.getPosition());
                    }
                }
            });
        }
    }

    public static void handleSetRitualReagentData(SetRitualCollectedReagentsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ctx.enqueueWork(() -> {
                Entity e = ManaAndArtifice.instance.proxy.getClientWorld().getEntity(message.getEntityID());
                if (e != null && e instanceof Ritual) {
                    ((Ritual) e).readCollectedReagents(message.getData().getList("data", 10));
                }
            });
        }
    }

    public static void handleCantripCastingTimerMessage(CantripCastingTimerMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ctx.enqueueWork(() -> HUDOverlayRenderer.instance.setCastingCantrip(message.getCantripId(), message.getTicks()));
        }
    }

    public static void handleWanderingWizardInventoryMessage(WanderingWizardInventoryMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ctx.enqueueWork(() -> processWanderingWizardInventoryMessage(Minecraft.getInstance(), message));
        }
    }

    public static void handleWellspringSyncMessage(WellspringSyncMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("ProgressionSyncMessageToClient context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> ((Level) clientWorld.get()).getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().readFromNBT(message.getData())));
            }
        }
    }

    public static void handleMindVisionMessage(MindVisionMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MindVisionMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    if (message.getEntityID() == -1) {
                        ManaAndArtifice.instance.proxy.resetRenderViewEntity();
                    } else {
                        Entity e = ((Level) clientWorld.get()).getEntity(message.getEntityID());
                        if (e != null) {
                            ManaAndArtifice.instance.proxy.setRenderViewEntity(e);
                        }
                    }
                });
            }
        }
    }

    public static void handlePosessionMessage(PosessionMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("PosessionMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    Entity e = ((Level) clientWorld.get()).getEntity(message.getEntityID());
                    if (e != null) {
                        ManaAndArtifice.instance.proxy.setRenderViewEntity(e);
                    }
                });
            }
        }
    }

    public static void handleMAPFXMessage(MAPFXMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MAPFXMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    Entity e = ((Level) clientWorld.get()).getEntity(message.getEntityID());
                    if (e != null && e instanceof LivingEntity) {
                        ((LivingEntity) e).getCapability(MAPFXProvider.MAPFX).ifPresent(p -> p.setFlags(message.getFlags()));
                    }
                });
            }
        }
    }

    public static void handleCloudstepJumpMessage(CloudstepJumpMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MAPFXMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    Entity e = ((Level) clientWorld.get()).getEntity(message.getEntityID());
                    if (e != null && e instanceof LivingEntity) {
                        for (int i = 0; i < 20; i++) {
                            Vec3 pos = e.position();
                            e.level().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), pos.x, pos.y, pos.z, 0.25, 0.05F, 0.5);
                        }
                    }
                });
            }
        }
    }

    public static void handleStructureSyncMessage(StructureSyncMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("MAPFXMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> ((Level) clientWorld.get()).getRecipeManager().byKey(message.getStructureID()).ifPresent(r -> {
                    if (r instanceof MultiblockDefinition) {
                        ((MultiblockDefinition) r).deserializeCoreBlocks(message.getData());
                    }
                }));
            }
        }
    }

    public static void handleWellspringPowerNetworkSyncMessage(WellspringPowerNetworkSyncMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("handleWellspringPowerNetworkSyncMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    if (((Level) clientWorld.get()).dimension().location().equals(message.getDimension().location())) {
                        if (((Level) clientWorld.get()).dimension().registry().equals(message.getDimension().registry())) {
                            ctx.enqueueWork(() -> ((Level) clientWorld.get()).getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().deserializeNetworkStrength(ManaAndArtifice.instance.proxy.getClientPlayer(), message.getData())));
                        }
                    }
                });
            }
        }
    }

    public static void handleExtendedSlotContentsMessage(ExtendedSlotContentsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("handleExtendedSlotContentsMessage context could not provide a ClientWorld");
            } else {
                Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
                if (player != null) {
                    ctx.enqueueWork(() -> {
                        if (player.containerMenu.containerId == message.getScreenID()) {
                            player.containerMenu.setItem(message.getSlotIndex(), message.getStateID(), message.getStack());
                        }
                    });
                }
            }
        }
    }

    public static void handleExtendedSlotContainerContentsMessage(ExtendedSlotContainerContentsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("handleExtendedSlotContentsMessage context could not provide a ClientWorld");
            } else {
                Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
                if (player != null) {
                    ctx.enqueueWork(() -> {
                        if (message.getContainerID() == 0) {
                            player.inventoryMenu.m_182410_(message.getStateID(), message.getItems(), message.getCarried());
                        } else if (message.getContainerID() == player.containerMenu.containerId) {
                            player.containerMenu.initializeContents(message.getStateID(), message.getItems(), message.getCarried());
                        }
                    });
                }
            }
        }
    }

    public static void handlePlayerAuraSyncMessage(AuraSyncMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("PlayerAuraSyncMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    Level world = ManaAndArtifice.instance.proxy.getClientWorld();
                    if (world != null) {
                        if (message.isBlock() && world.isLoaded(message.getBlockPos())) {
                            BlockEntity be = world.getBlockEntity(message.getBlockPos());
                            if (be != null && be instanceof ParticleEmitterTile) {
                                ((ParticleEmitterTile) be).setData(message.getTag());
                            }
                        } else {
                            Entity entity = world.getEntity(message.getEntityID());
                            if (entity != null && entity instanceof Player) {
                                ((Player) entity).getCapability(ParticleAuraProvider.AURA).ifPresent(a -> a.load(message.getTag()));
                            }
                        }
                    }
                });
            }
        }
    }

    public static void handleSetLiftSpeedMessage(SetLiftSpeedMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
            Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
            if (!clientWorld.isPresent()) {
                ManaAndArtifice.LOGGER.error("PlayerAuraSyncMessage context could not provide a ClientWorld");
            } else {
                ctx.enqueueWork(() -> {
                    Level world = ManaAndArtifice.instance.proxy.getClientWorld();
                    if (world != null) {
                        Entity e = world.getEntity(message.getEntityID());
                        if (e != null) {
                            e.getPersistentData().putFloat("lift_speed", message.getLiftSpeed());
                        }
                    }
                });
            }
        }
    }

    private static void processMagicSyncMessage(Level worldClient, MagicSyncMessageToClient message) {
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        if (player != null) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                m.setMagicLevel(player, message.getLevel());
                m.setMagicXP(message.getXP());
                if (message.syncCastingResource()) {
                    m.setCastingResourceType(message.getCastingResourceType());
                    m.getCastingResource().readNBT(message.getCastingResourceData());
                }
                for (int i = 0; i < Math.min(message.getAffinities().length, Affinity.values().length); i++) {
                    m.setAffinityDepth(Affinity.values()[i], message.getAffinities()[i]);
                }
                if (message.syncGrimoire()) {
                    m.getGrimoireInventory().m_6211_();
                    for (int i = 0; i < message.getGrimoireInventory().size(); i++) {
                        m.getGrimoireInventory().m_6836_(i, message.getGrimoireInventory().get(i));
                    }
                }
                if (message.syncRote()) {
                    m.getRoteInventory().m_6211_();
                    for (int i = 0; i < message.getRoteInventory().size(); i++) {
                        m.getRoteInventory().m_6836_(i, message.getRoteInventory().get(i));
                    }
                }
                if (message.syncCantrips()) {
                    m.getCantripData().readFromNBT(message.getCantripData());
                }
                m.updateClientsideTeleportData(message.getIsTeleporting(), message.getTeleportElapsed(), message.getTeleportTotal());
            });
        }
    }

    private static void processProgressionSyncMessage(Level worldClient, ProgressionSyncMessageToClient message) {
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        if (player != null) {
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                p.setTier(message.getTier(), null);
                p.setFactionStanding(message.getFactionStanding());
                p.setAlliedFaction(message.getFaction(), player);
                p.setTierProgression(message.getCompletedProgressionSteps());
            });
        }
    }

    private static void processEntityStateMessage(Level clientWorld, EntityStateMessage message) {
        Entity entity = clientWorld.getEntity(message.getEntityID());
        if (entity != null && entity instanceof IAnimPacketSync) {
            ((IAnimPacketSync) entity).handlePacketData(message.getData());
        }
    }

    private static void processRoteSyncMessage(Level worldClient, RoteProgressSyncMessageToClient message) {
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        if (player != null) {
            player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                r.resetMastery();
                r.resetRote();
                for (Entry<ResourceLocation, Float> e : message.getRoteProgress().entrySet()) {
                    r.setRoteXP((ResourceLocation) e.getKey(), (Float) e.getValue());
                }
                for (Entry<ResourceLocation, Float> e : message.getSpellMastery().entrySet()) {
                    r.setMastery((ResourceLocation) e.getKey(), (Float) e.getValue());
                }
            });
        }
    }

    private static void processWanderingWizardInventoryMessage(Minecraft minecraft, WanderingWizardInventoryMessage message) {
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        if (player != null) {
            AbstractContainerMenu container = player.containerMenu;
            if (message.getContainerId() == container.containerId && container instanceof ContainerWanderingWizard) {
                ((ContainerWanderingWizard) container).setClientSideOffers(new MerchantOffers(message.getOffers().createTag()));
                ((ContainerWanderingWizard) container).setXp(message.getExp());
                ((ContainerWanderingWizard) container).setFinalized(message.getIsFinal());
            }
        }
    }

    private static void processEnderDiscGuiDimensionCycle(Minecraft minecraft, EnderDiscGuiDimensionCycle message) {
        AbstractContainerMenu menu = minecraft.player.f_36096_;
        if (menu != null && menu instanceof ContainerEnderDisc) {
            ((ContainerEnderDisc) menu).setDimension(message.getDimensionID());
        }
    }

    private static void processRespondLootTableItems(Minecraft minecraft, RespondLootTableItems message) {
        LootTableCache.cacheLoot(message.getLootTableID(), message.getLootDrops());
    }
}