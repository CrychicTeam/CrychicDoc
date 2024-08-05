package com.mna.network.handlers;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.blocks.tileentities.ManaweavingAltarTile;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.blocks.tileentities.wizard_lab.ISelectSpellComponents;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectHelper;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.entities.EntityInit;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.manaweaving.Manaweave;
import com.mna.entities.utility.EldrinFlight;
import com.mna.events.EventDispatcher;
import com.mna.gui.containers.block.ContainerLodestar;
import com.mna.gui.containers.block.ContainerMagiciansWorkbench;
import com.mna.gui.containers.entity.ContainerWanderingWizard;
import com.mna.gui.containers.providers.NamedSpellCustomization;
import com.mna.items.ItemInit;
import com.mna.items.artifice.ItemEnderDisk;
import com.mna.items.artifice.ItemLodestarCopier;
import com.mna.items.base.IRadialInventorySelect;
import com.mna.items.manaweaving.ItemManaweaverWand;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.sorcery.ItemBookOfRote;
import com.mna.items.sorcery.ItemModifierBook;
import com.mna.items.sorcery.ItemSpell;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.items.sorcery.ItemStaff;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.BaseMessage;
import com.mna.network.messages.to_server.AnimatedConstructDropItemMessage;
import com.mna.network.messages.to_server.AnimatedConstructSyncRequestMessage;
import com.mna.network.messages.to_server.AuraSyncMessageToServer;
import com.mna.network.messages.to_server.CantripPatternUpdateMessage;
import com.mna.network.messages.to_server.ConstructHornMessage;
import com.mna.network.messages.to_server.EndControlEffectEarlyMessage;
import com.mna.network.messages.to_server.EnderDiscIndexSetMessage;
import com.mna.network.messages.to_server.EnderDiscPatternSetMessage;
import com.mna.network.messages.to_server.InscriptionTableAttributeChangeMessage;
import com.mna.network.messages.to_server.InscriptionTableRequestStartCraftingMessage;
import com.mna.network.messages.to_server.InscriptionTableSetComponentMessage;
import com.mna.network.messages.to_server.InscriptionTableSetModifierMessage;
import com.mna.network.messages.to_server.InscriptionTableSetShapeMessage;
import com.mna.network.messages.to_server.LodestarLogicSetMessage;
import com.mna.network.messages.to_server.MAPFXSyncRequestMessage;
import com.mna.network.messages.to_server.MagiciansWorkbenchClearMessage;
import com.mna.network.messages.to_server.MagiciansWorkbenchRecipeSetMessage;
import com.mna.network.messages.to_server.ManaweavePatternDrawnMessage;
import com.mna.network.messages.to_server.ManaweaveWandSlotChangeMessage;
import com.mna.network.messages.to_server.MultiblockSyncRequestMessage;
import com.mna.network.messages.to_server.OpenSpellRenameMessage;
import com.mna.network.messages.to_server.PatterningPrismPasteMessage;
import com.mna.network.messages.to_server.PlayerBounceMessage;
import com.mna.network.messages.to_server.PlayerFocusDistanceMessage;
import com.mna.network.messages.to_server.PlayerJumpMessage;
import com.mna.network.messages.to_server.PossessionInputMessage;
import com.mna.network.messages.to_server.RadialInventorySlotChangeMessage;
import com.mna.network.messages.to_server.RequestLootTableItems;
import com.mna.network.messages.to_server.RequestWellspringPowerNetworkSyncMessage;
import com.mna.network.messages.to_server.RitualKitIndexSetMessage;
import com.mna.network.messages.to_server.RoteSpellsSyncMessageToServer;
import com.mna.network.messages.to_server.RunescribingTableMutexChangeMessage;
import com.mna.network.messages.to_server.SelectedModifierMessage;
import com.mna.network.messages.to_server.SpellAdjustmentsMessage;
import com.mna.network.messages.to_server.SpellBookSlotChangeMessage;
import com.mna.network.messages.to_server.SpellNameAndIconMessage;
import com.mna.network.messages.to_server.TradeSelectedMessage;
import com.mna.network.messages.to_server.UIModifierPress;
import com.mna.network.messages.to_server.WizardLabSelectSpellComponentMessage;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.multiblock.MultiblockDefinition;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.PossessionHelper;
import com.mna.tools.TeleportHelper;
import com.mna.tools.loot.LootDrop;
import com.mna.tools.loot.LootTableCache;
import com.mna.tools.loot.LootTableHelper;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;

public class ServerMessageHandler {

    private static <T extends BaseMessage> boolean validateBasics(T message, NetworkEvent.Context ctx) {
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);
        if (sideReceived != LogicalSide.SERVER) {
            ManaAndArtifice.LOGGER.error(message.getClass().getName() + " received on wrong side: " + sideReceived);
            return false;
        } else if (!message.isMessageValid()) {
            ManaAndArtifice.LOGGER.error(message.getClass().getName() + " was invalid: " + message);
            return false;
        } else {
            return true;
        }
    }

    public static void handleUIModifierPress(UIModifierPress message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when UIModifierPress was received");
            } else {
                ctx.enqueueWork(() -> sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.setModifierPressed(message.getPressed())));
            }
        }
    }

    public static void handlePlayerFocusDistanceMessage(PlayerFocusDistanceMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when UIModifierPress was received");
            } else {
                ctx.enqueueWork(() -> sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.offsetFocusDistance(message.getDelta(), message.getMaximum())));
            }
        }
    }

    public static void handleRequestLootTableItems(RequestLootTableItems message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when UIModifierPress was received");
            } else {
                ctx.enqueueWork(() -> {
                    List<LootDrop> lootItems = LootTableCache.getLoot(message.getLootTableID());
                    if (lootItems == null) {
                        lootItems = LootTableHelper.toDrops(sendingPlayer.serverLevel(), message.getLootTableID());
                    }
                    if (lootItems != null) {
                        ServerMessageDispatcher.sendRespondLootTableItems(sendingPlayer, message.getLootTableID(), lootItems);
                    }
                });
            }
        }
    }

    public static void handleInscriptionTableShapeSet(InscriptionTableSetShapeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableSetShapeMessage was received");
            } else {
                ctx.enqueueWork(() -> InscriptionTableTile.handleShapeSet(sendingPlayer, message));
            }
        }
    }

    public static void handleInscriptionTableComponentSet(InscriptionTableSetComponentMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableSetComponentMessage was received");
            } else {
                ctx.enqueueWork(() -> InscriptionTableTile.handleComponentSet(sendingPlayer, message));
            }
        }
    }

    public static void handleInscriptionTableModifierSet(InscriptionTableSetModifierMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableSetModifierMessage was received");
            } else {
                ctx.enqueueWork(() -> InscriptionTableTile.handleModifierSet(sendingPlayer, message));
            }
        }
    }

    public static void handleInscriptionTableAttributeChange(InscriptionTableAttributeChangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableAttributeChangeMessage was received");
            } else {
                ctx.enqueueWork(() -> InscriptionTableTile.handleAttributeValueChange(sendingPlayer, message));
            }
        }
    }

    public static void handleInscriptionTableStartCrafting(InscriptionTableRequestStartCraftingMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableRequestStartCraftingMessage was received");
            } else {
                ctx.enqueueWork(() -> InscriptionTableTile.handleRequestStartCrafting(sendingPlayer, message));
            }
        }
    }

    public static void handleManaweavePatternDrawnMessage(ManaweavePatternDrawnMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when ManaweavePatternDrawnMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    BlockPos messagePos = BlockPos.containing(message.getPosition());
                    if (sendingPlayer.m_9236_().isLoaded(messagePos)) {
                        Recipe<?> pattern = (Recipe<?>) sendingPlayer.m_9236_().getRecipeManager().byKey(message.getPatternID()).orElse(null);
                        if (pattern instanceof IManaweavePattern) {
                            if (EventDispatcher.DispatchManaweavePatternDrawn((IManaweavePattern) pattern, sendingPlayer)) {
                                BlockPos storedPos = ItemManaweaverWand.getStoredBlockPos(sendingPlayer.m_21120_(message.getHand()));
                                ManaweavingAltarTile tile = null;
                                if (storedPos != null && storedPos.m_123331_(messagePos) <= 64.0) {
                                    BlockEntity worldTile = sendingPlayer.m_9236_().getBlockEntity(storedPos);
                                    if (worldTile != null && worldTile instanceof ManaweavingAltarTile) {
                                        tile = (ManaweavingAltarTile) worldTile;
                                    }
                                }
                                if (tile == null) {
                                    Entity weave = EntityInit.MANAWEAVE_ENTITY.get().create(sendingPlayer.m_9236_());
                                    if (weave != null) {
                                        weave.moveTo(message.getPosition().x, message.getPosition().y, message.getPosition().z, 0.0F, 0.0F);
                                        if (weave instanceof Manaweave) {
                                            ((Manaweave) weave).setPattern(message.getPatternID());
                                            ((Manaweave) weave).setCaster(sendingPlayer, message.getHand());
                                            ((Manaweave) weave).setManuallyDrawn();
                                            ((Manaweave) weave).setManaRefunded(message.getTicksDrawn());
                                        }
                                        sendingPlayer.m_9236_().m_7967_(weave);
                                    }
                                } else {
                                    tile.pushPattern((ManaweavingPattern) pattern, sendingPlayer);
                                }
                                sendingPlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.addMagicXP(3, sendingPlayer, p)));
                                sendingPlayer.m_21205_().hurtAndBreak(1, sendingPlayer, i -> {
                                });
                            }
                        }
                    }
                });
            }
        }
    }

    public static void handleRunescribingTableMutexChangeMessage(RunescribingTableMutexChangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SpellCastMessageToServer was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (sendingPlayer.m_9236_().isLoaded(message.getPosition())) {
                        ctx.enqueueWork(() -> RunescribingTableTile.handleMutexChangeMessage(sendingPlayer, message));
                    }
                });
            }
        }
    }

    public static void handleSpellBookSlotChangeMessage(SpellBookSlotChangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SpellBookSlotChangeMessage was received");
            } else {
                ctx.enqueueWork(() -> ItemSpellBook.handleSlotChangeMessage(message, sendingPlayer));
            }
        }
    }

    public static void handleRadialInventorySlotChangeMessage(RadialInventorySlotChangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when RadialInventorySlotChangeMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = message.isOffhand() ? sendingPlayer.m_21206_() : sendingPlayer.m_21205_();
                    if (stack.getItem() instanceof IRadialInventorySelect) {
                        ((IRadialInventorySelect) stack.getItem()).setSlot(sendingPlayer, message.isOffhand() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, message.getSlot(), message.isOffhand(), false);
                    }
                });
            }
        }
    }

    public static void handleManaweaveWandSlotChangeMessage(ManaweaveWandSlotChangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when ManaweaveWandSlotChangeMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = message.isOffhand() ? sendingPlayer.m_21206_() : sendingPlayer.m_21205_();
                    if (stack.getItem() instanceof ItemManaweaverWand) {
                        ItemManaweaverWand.setStoredPattern(stack, message.getSelected());
                    }
                });
            }
        }
    }

    public static void handleAuraSyncMessage(AuraSyncMessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when ManaweaveWandSlotChangeMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (message.isBlock()) {
                        BlockEntity be = sendingPlayer.m_9236_().getBlockEntity(message.getBlockPos());
                        if (be != null && be instanceof ParticleEmitterTile) {
                            ((ParticleEmitterTile) be).setData(message.getTag());
                            ServerMessageDispatcher.sendAuraSyncMessage((ParticleEmitterTile) be);
                        }
                    } else if (sendingPlayer.m_36316_() != null && sendingPlayer.m_36316_().getId() != null && (ManaAndArtifice.instance.isDebug || ManaAndArtifice.instance.enabled_auras.contains(sendingPlayer.m_36316_().getId()))) {
                        sendingPlayer.getCapability(ParticleAuraProvider.AURA).ifPresent(a -> {
                            a.load(message.getTag());
                            ServerMessageDispatcher.sendAuraSyncMessage(sendingPlayer);
                        });
                    }
                });
            }
        }
    }

    public static void handleEnderDiscPatternSetMessage(EnderDiscPatternSetMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when EnderDiscPatternSetMessage was received");
            } else {
                ctx.enqueueWork(() -> ItemEnderDisk.setPattern(sendingPlayer.m_150109_().getSelected(), message.getPatterns(), message.getDimensionID(), message.getIndex(), message.getName()));
            }
        }
    }

    public static void handleEnderDiscIndexSetMessage(EnderDiscIndexSetMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when EnderDiscIndexSetMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = message.isOffhand() ? sendingPlayer.m_21206_() : sendingPlayer.m_21205_();
                    ItemEnderDisk.setIndex(stack, message.getIndex());
                });
            }
        }
    }

    public static void handleRitualKitIndexSetMessage(RitualKitIndexSetMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when RitualKitIndexSetMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = message.isOffhand() ? sendingPlayer.m_21206_() : sendingPlayer.m_21205_();
                    ItemPractitionersPouch.setIndex(stack, message.getIndex());
                });
            }
        }
    }

    public static void handleAnimatedConstructDropItemMessage(AnimatedConstructDropItemMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when AnimatedConstructSyncRequestMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    Construct eac = (Construct) sendingPlayer.m_9236_().getEntity(message.getEntityID());
                    if (eac != null && eac.playerCanCommand(sendingPlayer)) {
                        ItemStack stack = message.getSlot() == 0 ? eac.m_21205_() : (message.getSlot() == 1 ? eac.m_21206_() : (message.getSlot() == 2 ? eac.getConstructData().getHat() : (message.getSlot() == 3 ? eac.getConstructData().getBanner() : eac.getStackInSlot(message.getSlot() - 4))));
                        if (!stack.isEmpty()) {
                            if (message.getSlot() == 0) {
                                eac.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            } else if (message.getSlot() == 1) {
                                eac.m_21008_(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                            } else {
                                if (message.getSlot() == 2) {
                                    eac.setHat(ItemStack.EMPTY);
                                    return;
                                }
                                if (message.getSlot() == 3) {
                                    eac.setBanner(ItemStack.EMPTY);
                                    return;
                                }
                                eac.setStackInSlot(message.getSlot() - 4, ItemStack.EMPTY);
                            }
                            ItemEntity ie = new ItemEntity(eac.m_9236_(), eac.m_20185_(), eac.m_20186_(), eac.m_20189_(), stack);
                            ie.setDefaultPickUpDelay();
                            eac.m_9236_().m_7967_(ie);
                        }
                    }
                });
            }
        }
    }

    public static void handleAnimatedConstructSyncRequestMessage(AnimatedConstructSyncRequestMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when AnimatedConstructSyncRequestMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    Construct eac = (Construct) sendingPlayer.m_9236_().getEntity(message.getEntityID());
                    if (eac != null) {
                        eac.setRequestingDiagnostics(message.getDiagnosticsOnly());
                        ServerMessageDispatcher.sendEntityStateMessage(eac, sendingPlayer);
                        eac.setRequestingDiagnostics(false);
                    }
                });
            }
        }
    }

    public static void handleMAPFXSyncRequestMessage(MAPFXSyncRequestMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when MAPFXSyncRequestMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    Entity entity = sendingPlayer.m_9236_().getEntity(message.getEntityID());
                    if (message.isForAura()) {
                        if (entity instanceof ServerPlayer && (ManaAndArtifice.instance.isDebug || ManaAndArtifice.instance.enabled_auras.contains(((ServerPlayer) entity).m_36316_().getId()))) {
                            ServerMessageDispatcher.sendAuraSyncMessage(sendingPlayer, (ServerPlayer) entity);
                        }
                    } else if (entity != null && entity instanceof LivingEntity) {
                        ServerMessageDispatcher.sendMAPFXMessage((LivingEntity) entity, sendingPlayer);
                    }
                });
            }
        }
    }

    public static void handleMultiblockSyncRequestMessage(MultiblockSyncRequestMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when MultiblockSyncRequestMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    Entity entity = sendingPlayer.m_9236_().getEntity(message.getEntityID());
                    if (entity != null && entity instanceof ServerPlayer) {
                        entity.level().getRecipeManager().byKey(message.getRecipe()).ifPresent(r -> {
                            if (r instanceof MultiblockDefinition) {
                                ServerMessageDispatcher.sendStructureSyncMessage((MultiblockDefinition) r, (ServerPlayer) entity);
                            }
                        });
                    }
                });
            }
        }
    }

    public static void handleRequestWellspringPowerNetworkSyncMessage(RequestWellspringPowerNetworkSyncMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when MultiblockSyncRequestMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ServerLevel world = TeleportHelper.resolveRegistryKey((ServerLevel) sendingPlayer.m_9236_(), message.getDimension());
                    if (world != null) {
                        ServerMessageDispatcher.sendWellspringPowerNetworkSyncMessage(world, sendingPlayer, true);
                        if (message.getNearbyNodes()) {
                            ServerMessageDispatcher.sendWellspringSyncMessage(world, sendingPlayer, 512);
                        }
                    }
                });
            }
        }
    }

    public static void handleRoteSpellsSyncMessageToServer(RoteSpellsSyncMessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when RoteSpellsSyncMessageToServer was received");
            } else {
                ctx.enqueueWork(() -> sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                    MutableInt playerTier = new MutableInt(0);
                    sendingPlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> playerTier.setValue(p.getTier()));
                    for (int i = 0; i < message.getInventory().size(); i++) {
                        ItemStack roteStack = message.getInventory().get(i);
                        SpellRecipe spell = SpellRecipe.fromNBT(roteStack.getOrCreateTag());
                        if (!spell.isValid()) {
                            m.getRoteInventory().m_6836_(i, new ItemStack(ItemInit.SPELL.get()));
                        } else {
                            m.getRoteInventory().m_6836_(i, roteStack);
                        }
                    }
                    ItemStack spellStack = sendingPlayer.m_150109_().getItem(sendingPlayer.m_150109_().selected);
                    if (spellStack.getItem() instanceof ItemBookOfRote) {
                        int slot = ItemSpellBook.getActiveSpellSlot(spellStack);
                        ItemSpellBook.setSlot(sendingPlayer, spellStack, slot, false, false);
                    }
                    m.setSyncRote();
                    m.forceSync();
                }));
            }
        }
    }

    public static void handlePlayerBounceMessage(PlayerBounceMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when PlayerBounceMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    sendingPlayer.m_20256_(message.getVelocity());
                    sendingPlayer.connection.send(new ClientboundSetEntityMotionPacket(sendingPlayer));
                });
            }
        }
    }

    public static void handlePlayerJumpMessage(PlayerJumpMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when PlayerJumpMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (!sendingPlayer.m_20069_()) {
                        int enchLevel = sendingPlayer.m_6844_(EquipmentSlot.FEET).getEnchantmentLevel(EnchantmentInit.LEAPING.get());
                        sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                            if (m.getAirJumps() < enchLevel) {
                                m.incrementAirJumps(sendingPlayer);
                                sendingPlayer.m_6135_();
                                sendingPlayer.connection.send(new ClientboundSetEntityMotionPacket(sendingPlayer));
                                ServerMessageDispatcher.sendCloudstepJumpMessage(sendingPlayer);
                            }
                        });
                    }
                });
            }
        }
    }

    public static void handleCantripPatternUpdateMessage(CantripPatternUpdateMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when CantripPatternUpdateMessage was received");
            } else {
                ctx.enqueueWork(() -> sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCantripData().readFromNBT(message.getData())));
            }
        }
    }

    public static void handleSpellNameAndIconMessage(SpellNameAndIconMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SpellNameAndIconMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = sendingPlayer.m_21120_(message.getHand());
                    if (stack.getItem() == ItemInit.SPELL.get() || stack.getItem() instanceof ItemStaff) {
                        stack.setHoverName(Component.literal(message.getName()));
                        ItemSpell.setCustomIcon(stack, message.getIconIndex());
                    } else if (stack.getItem() instanceof ItemBookOfRote) {
                        sendingPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                            ItemStack roteStack = m.getRoteInventory().m_8020_(message.getItemIndex());
                            roteStack.setHoverName(Component.literal(message.getName()));
                            ItemSpell.setCustomIcon(roteStack, message.getIconIndex());
                        });
                    }
                });
            }
        }
    }

    public static void handleSpellAdjustmentsMessage(SpellAdjustmentsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SpellAdjustmentMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    SpellRecipe recipe = SpellRecipe.fromNBT(message.getData());
                    ItemStack heldStack = sendingPlayer.m_21120_(message.getHand());
                    if (recipe.isValid() && heldStack.getItem() instanceof ItemSpell && !(heldStack.getItem() instanceof ItemSpellBook)) {
                        recipe.writeToNBT(heldStack.getOrCreateTag());
                    } else {
                        sendingPlayer.sendSystemMessage(Component.literal("Error setting spell data values; serverside validation failed!").withStyle(ChatFormatting.RED));
                    }
                });
            }
        }
    }

    public static void handleOpenSpellRenameMessage(OpenSpellRenameMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when OpenSpellRenameMessage was received");
            } else {
                ctx.enqueueWork(() -> NetworkHooks.openScreen(sendingPlayer, new NamedSpellCustomization()));
            }
        }
    }

    public static void handleSelectedModifierMessage(SelectedModifierMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SelectedModifierMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = message.isOffhand() ? sendingPlayer.m_21206_() : sendingPlayer.m_21205_();
                    if (stack.getItem() == ItemInit.MODIFIER_BOOK.get()) {
                        ItemModifierBook.setModifier(stack, message.getModifierRLoc());
                    } else {
                        sendingPlayer.sendSystemMessage(Component.literal("Error setting selected modifier, couldn't verify held item"));
                    }
                });
            }
        }
    }

    public static void handleConstructHornMessage(ConstructHornMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SelectedModifierMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    Entity e = sendingPlayer.m_9236_().getEntity(message.getEntityId());
                    if (e != null && e instanceof Construct) {
                        ((Construct) e).soundHorn();
                    }
                });
            }
        }
    }

    public static void handleTradeSelectedMessage(TradeSelectedMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when SelectedModifierMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    int i = message.getIndex();
                    if (sendingPlayer.f_36096_ instanceof ContainerWanderingWizard merchantcontainer) {
                        merchantcontainer.setCurrentRecipeIndex(i);
                        merchantcontainer.setCurrentTradeRecipeItems(i);
                    }
                });
            }
        }
    }

    public static void handlePossessionInputMessage(PossessionInputMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when PosessionInputMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (sendingPlayer.getPersistentData().contains("posessed_entity_id")) {
                        int id = sendingPlayer.getPersistentData().getInt("posessed_entity_id");
                        Entity e = sendingPlayer.m_9236_().getEntity(id);
                        if (e != null && e instanceof Mob) {
                            PossessionHelper.handleRemoteInput(message, sendingPlayer, (Mob) e);
                        }
                    }
                    if (sendingPlayer.getPersistentData().contains("eldrin_flight_entity_id")) {
                        int id = sendingPlayer.getPersistentData().getInt("eldrin_flight_entity_id");
                        Entity e = sendingPlayer.m_9236_().getEntity(id);
                        if (e != null && e instanceof EldrinFlight) {
                            e.setYRot(message.getYaw());
                            e.setXRot(message.getPitch());
                        }
                    }
                });
            }
        }
    }

    public static void handleEndControlEffectEarlyMessage(EndControlEffectEarlyMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when EndControlEffectEarlyMessage was received");
            } else {
                ctx.enqueueWork(() -> EffectHelper.removeDoubleTapEvents(sendingPlayer));
            }
        }
    }

    public static void handleMagiciansWorkbenchRecipeSetMessage(MagiciansWorkbenchRecipeSetMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableSetShapeMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (sendingPlayer.f_36096_ != null && sendingPlayer.f_36096_ instanceof ContainerMagiciansWorkbench) {
                        ((ContainerMagiciansWorkbench) sendingPlayer.f_36096_).moveRecipeToCraftingGrid(message.getIndex());
                    }
                });
            }
        }
    }

    public static void handleMagiciansWorkbenchClearMessage(MagiciansWorkbenchClearMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when InscriptionTableSetShapeMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (sendingPlayer.f_36096_ != null && sendingPlayer.f_36096_ instanceof ContainerMagiciansWorkbench) {
                        ((ContainerMagiciansWorkbench) sendingPlayer.f_36096_).tryClearGrid(message.isSecond());
                    }
                });
            }
        }
    }

    public static void handleLodestarLogicSetMessage(LodestarLogicSetMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when LodestarLogicSetMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (sendingPlayer.f_36096_ != null && sendingPlayer.f_36096_ instanceof ContainerLodestar) {
                        ((ContainerLodestar) sendingPlayer.f_36096_).updateTileLogic(message.getLogic(), true);
                    }
                });
            }
        }
    }

    public static void handlePatterningPrismPasteMessage(PatterningPrismPasteMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when LodestarLogicSetMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    ItemStack stack = sendingPlayer.m_21120_(message.getHand());
                    if (stack.getItem() == ItemInit.LODESTAR_COPIER.get()) {
                        ((ItemLodestarCopier) stack.getItem()).setCopiedLogic(message.getLogic(), stack, message.isParticleEmitter());
                    }
                });
            }
        }
    }

    public static void handleWizardLabSelectSpellComponentMessage(WizardLabSelectSpellComponentMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) ctxSupplier.get();
        if (validateBasics(message, ctx)) {
            ServerPlayer sendingPlayer = ctx.getSender();
            if (sendingPlayer == null) {
                ManaAndArtifice.LOGGER.error("EntityPlayerMP was null when LodestarLogicSetMessage was received");
            } else {
                ctx.enqueueWork(() -> {
                    if (sendingPlayer.f_36096_ != null && sendingPlayer.f_36096_ instanceof ISelectSpellComponents) {
                        ResourceLocation comp = message.getSpellComponent();
                        ISpellComponent resolved = null;
                        if (((IForgeRegistry) Registries.Shape.get()).containsKey(comp)) {
                            resolved = (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(comp);
                        } else if (((IForgeRegistry) Registries.Modifier.get()).containsKey(comp)) {
                            resolved = (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(comp);
                        } else if (((IForgeRegistry) Registries.SpellEffect.get()).containsKey(comp)) {
                            resolved = (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(comp);
                        }
                        if (resolved != null) {
                            ((ISelectSpellComponents) sendingPlayer.f_36096_).setSpellComponent(resolved);
                        }
                    }
                });
            }
        }
    }
}