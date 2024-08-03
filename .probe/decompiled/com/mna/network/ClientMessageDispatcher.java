package com.mna.network;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.parts.Modifier;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mna.entities.constructs.animated.Construct;
import com.mna.gui.containers.item.ContainerRoteBook;
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
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;

public class ClientMessageDispatcher {

    public static void sendInscriptionTableAttributeChange(InscriptionTableTile te, Attribute attr, float newVal, InscriptionTableAttributeChangeMessage.ChangeType type) {
        if (te.m_58898_() && te.m_58904_().isClientSide) {
            MAPacketHandler.network.sendTo(new InscriptionTableAttributeChangeMessage(te.m_58899_(), attr, newVal, type), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void sendInscriptionTableSetShape(InscriptionTableTile te) {
        if (te.m_58898_() && te.m_58904_().isClientSide) {
            MAPacketHandler.network.sendTo(InscriptionTableSetShapeMessage.fromInscriptionTable(te), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void requestLootTableDrops(ResourceLocation lootTable) {
        MAPacketHandler.network.sendTo(new RequestLootTableItems(lootTable), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendInscriptionTableSetComponent(InscriptionTableTile te) {
        if (te.m_58898_() && te.m_58904_().isClientSide) {
            MAPacketHandler.network.sendTo(InscriptionTableSetComponentMessage.fromInscriptionTable(te), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void sendInscriptionTableSetModifier(InscriptionTableTile te, ResourceLocation modifier, int index) {
        if (te.m_58898_() && te.m_58904_().isClientSide) {
            MAPacketHandler.network.sendTo(new InscriptionTableSetModifierMessage(te.m_58899_(), modifier, index), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void sendInscriptionTableRequestStartCrafting(InscriptionTableTile te) {
        if (te.m_58898_() && te.m_58904_().isClientSide) {
            MAPacketHandler.network.sendTo(InscriptionTableRequestStartCraftingMessage.fromInscriptionTable(te), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void sendManaweavePatternDrawn(Player player, ResourceLocation patternID, Vec3 position, InteractionHand hand, int ticksDrawn) {
        if (player.m_9236_().isClientSide()) {
            MAPacketHandler.network.sendTo(new ManaweavePatternDrawnMessage(patternID, position, hand, ticksDrawn), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void sendRunescribingMutexChange(RunescribingTableTile table, long hMutex, long vMutex, int tier, boolean isDelete) {
        if (table.m_58898_() && table.m_58904_().isClientSide) {
            MAPacketHandler.network.sendTo(new RunescribingTableMutexChangeMessage(table.m_58899_(), hMutex, vMutex, tier, isDelete), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }

    public static void sendSpellBookSlotChange(int slot, boolean offhand) {
        MAPacketHandler.network.sendTo(new SpellBookSlotChangeMessage(slot, offhand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendPlayerFocusDistanceChange(float delta, float maximum) {
        MAPacketHandler.network.sendTo(new PlayerFocusDistanceMessage(delta, maximum), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendRadialInventorySlotChange(int slot, boolean offhand) {
        MAPacketHandler.network.sendTo(new RadialInventorySlotChangeMessage(slot, offhand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendManaweaveWandSlotChange(ResourceLocation selected, boolean offhand) {
        MAPacketHandler.network.sendTo(new ManaweaveWandSlotChangeMessage(selected, offhand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendEnderDiscPatternChange(ArrayList<ResourceLocation> patterns, ResourceLocation dimension, int patternIndex, String name) {
        MAPacketHandler.network.sendTo(new EnderDiscPatternSetMessage(patterns, dimension, patternIndex, name), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendEnderDiscIndexChange(int patternIndex, boolean offhand) {
        MAPacketHandler.network.sendTo(new EnderDiscIndexSetMessage(patternIndex, offhand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendRitualKitIndexChange(int patternIndex, boolean offhand) {
        MAPacketHandler.network.sendTo(new RitualKitIndexSetMessage(patternIndex, offhand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendSpellCustomizationValues(String name, int iconIndex, InteractionHand hand) {
        MAPacketHandler.network.sendTo(new SpellNameAndIconMessage(name, iconIndex, hand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendAnimatedConstructSyncRequestMessage(Construct construct, boolean diagOnly) {
        MAPacketHandler.network.sendTo(AnimatedConstructSyncRequestMessage.fromConstruct(construct, diagOnly), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendAnimatedConstructDropItemMessage(Construct construct, int slot) {
        MAPacketHandler.network.sendTo(AnimatedConstructDropItemMessage.fromConstruct(construct, slot), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendRequestWellspringNetworkSyncMessage(boolean nearbyNodes) {
        Minecraft mc = Minecraft.getInstance();
        MAPacketHandler.network.sendTo(new RequestWellspringPowerNetworkSyncMessage(mc.level.m_46472_(), nearbyNodes), mc.getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static boolean sendMAPFXSyncRequestMessage(LivingEntity entity) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.getConnection() != null && mc.getConnection().getConnection() != null) {
            MAPacketHandler.network.sendTo(new MAPFXSyncRequestMessage(entity.m_19879_(), false), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
            return true;
        } else {
            return false;
        }
    }

    public static boolean sendAuraXSyncRequestMessage(Player player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.getConnection() != null && mc.getConnection().getConnection() != null) {
            MAPacketHandler.network.sendTo(MAPFXSyncRequestMessage.forAura(player), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
            return true;
        } else {
            return false;
        }
    }

    public static boolean sendMultiblockSyncRequestMessage(LivingEntity entity, ResourceLocation recipe) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.getConnection() != null && mc.getConnection().getConnection() != null) {
            MAPacketHandler.network.sendTo(new MultiblockSyncRequestMessage(entity.m_19879_(), recipe), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
            return true;
        } else {
            return false;
        }
    }

    public static void sendRoteSpellsUpdate(ContainerRoteBook container) {
        MAPacketHandler.network.sendTo(RoteSpellsSyncMessageToServer.fromRoteBookContainer(container), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendPlayerBounce(Vec3 velocity) {
        MAPacketHandler.network.sendTo(new PlayerBounceMessage(velocity), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendPlayerJump() {
        MAPacketHandler.network.sendTo(new PlayerJumpMessage(), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendCantripUpdateMessage(IPlayerMagic magic) {
        MAPacketHandler.network.sendTo(CantripPatternUpdateMessage.fromCapability(magic), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendItemUIOpenMessage(boolean pressed) {
        MAPacketHandler.network.sendTo(new UIModifierPress(pressed), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendRequestOpenSpellNameAndIconGUI() {
        MAPacketHandler.network.sendTo(new OpenSpellRenameMessage(), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendSpellAdjustmentMessage(SpellRecipe recipe, InteractionHand hand) {
        CompoundTag nbt = new CompoundTag();
        recipe.writeToNBT(nbt);
        MAPacketHandler.network.sendTo(new SpellAdjustmentsMessage(nbt, hand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendSelectedModifierMessage(Modifier m, boolean offhand) {
        MAPacketHandler.network.sendTo(new SelectedModifierMessage(m.getRegistryName(), offhand), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void dispatchConstructHorn(int entityID) {
        MAPacketHandler.network.sendTo(new ConstructHornMessage(entityID), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendTradeSelected(int index) {
        MAPacketHandler.network.sendTo(new TradeSelectedMessage(index), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendPosessionMovementInput(float forward, float strafe, boolean jump, boolean sneak, float yaw, float yawHead, float pitch) {
        MAPacketHandler.network.sendTo(PossessionInputMessage.movement(forward, strafe, jump, sneak, yaw, yawHead, pitch), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendPosessionClickInput() {
        MAPacketHandler.network.sendTo(PossessionInputMessage.click(), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendRequestEndControlEffectEarlyMessage() {
        MAPacketHandler.network.sendTo(new EndControlEffectEarlyMessage(), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendMagiciansWorkbenchRecipeSetMessage(int index) {
        MAPacketHandler.network.sendTo(new MagiciansWorkbenchRecipeSetMessage(index), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendMagiciansWorkbenchClearMessage(boolean second) {
        MAPacketHandler.network.sendTo(new MagiciansWorkbenchClearMessage(second), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendLodestarLogicMessage(BlockPos pos, CompoundTag logic) {
        MAPacketHandler.network.sendTo(new LodestarLogicSetMessage(pos, logic), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendPatterningPrismCopyMessage(CompoundTag logic, InteractionHand hand, boolean isParticleEmitter) {
        MAPacketHandler.network.sendTo(new PatterningPrismPasteMessage(logic, hand, isParticleEmitter), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendAuraSyncMessage() {
        Minecraft m = Minecraft.getInstance();
        MAPacketHandler.network.sendTo(AuraSyncMessageToServer.fromPlayer(m.player), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendAuraSyncMessage(ParticleEmitterTile tile) {
        MAPacketHandler.network.sendTo(AuraSyncMessageToServer.fromTile(tile), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void sendWizardLabSelectSpellComponentMessage(ResourceLocation selected) {
        MAPacketHandler.network.sendTo(new WizardLabSelectSpellComponentMessage(selected), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
    }
}