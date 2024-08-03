package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.screens.SpeakerBlockScreen;
import net.mehvahdjukaar.supplementaries.common.block.IOnePlayerGui;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpeakerBlock;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundPlaySpeakerMessagePacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SpeakerBlockTile extends BlockEntity implements Nameable, IOwnerProtected, IOnePlayerGui {

    private UUID owner = null;

    private Component message = Component.empty();

    private Component filteredMessage = Component.empty();

    private SpeakerBlockTile.Mode mode = SpeakerBlockTile.Mode.CHAT;

    private double volume = (double) ((Integer) CommonConfigs.Redstone.SPEAKER_RANGE.get()).intValue();

    private Component customName;

    @Nullable
    private UUID playerWhoMayEdit = null;

    public Object ccHack = null;

    public SpeakerBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.SPEAKER_BLOCK_TILE.get(), pos, state);
    }

    public void setCustomName(Component name) {
        this.customName = name;
    }

    @Override
    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    @Override
    public Component getCustomName() {
        return this.customName;
    }

    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.speaker_block");
    }

    public double getVolume() {
        return this.volume;
    }

    public SpeakerBlockTile.Mode getMode() {
        return this.mode;
    }

    public Component getMessage(boolean filtered) {
        return filtered ? this.filteredMessage : this.message;
    }

    public void setMode(SpeakerBlockTile.Mode mode) {
        this.mode = mode;
    }

    public void setMessage(Component message) {
        this.setMessage(message, message);
    }

    public void setMessage(Component message, Component filteredMessage) {
        this.message = message;
        this.filteredMessage = filteredMessage;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        this.message = Component.Serializer.fromJson(compound.getString("Message"));
        if (compound.contains("FilteredMessage")) {
            this.filteredMessage = Component.Serializer.fromJson(compound.getString("FilteredMessage"));
        } else {
            this.filteredMessage = this.message;
        }
        SpeakerBlockTile.Mode m = SpeakerBlockTile.Mode.values()[compound.getInt("Mode")];
        if (m == SpeakerBlockTile.Mode.NARRATOR && !(Boolean) CommonConfigs.Redstone.SPEAKER_NARRATOR.get()) {
            m = SpeakerBlockTile.Mode.CHAT;
        }
        this.mode = m;
        this.volume = compound.getDouble("Volume");
        this.loadOwner(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
        compound.putString("Message", Component.Serializer.toJson(this.message));
        if (this.message != this.filteredMessage) {
            compound.putString("FilteredMessage", Component.Serializer.toJson(this.filteredMessage));
        }
        compound.putInt("Mode", this.mode.ordinal());
        compound.putDouble("Volume", this.volume);
        this.saveOwner(compound);
    }

    public void sendMessage() {
        BlockState state = this.m_58900_();
        if (this.f_58857_ instanceof ServerLevel server && !this.message.equals("")) {
            BlockPos pos = this.m_58899_();
            this.f_58857_.blockEvent(pos, this.m_58900_().m_60734_(), 0, 0);
            Style style = !state.m_61143_(SpeakerBlock.ANTIQUE) ? Style.EMPTY.applyFormats(ChatFormatting.ITALIC) : Style.EMPTY.withFont(ModTextures.ANTIQUABLE_FONT).applyFormats(ChatFormatting.ITALIC);
            String name = this.getName().getString();
            String s = "";
            if (name.isEmpty()) {
                s = "Speaker Block: ";
            } else if (!name.equals("\"\"") && !name.equals("\"")) {
                s = s + name + ": ";
            }
            Component component = Component.literal(s + this.message.getString()).withStyle(style);
            Component filtered = Component.literal(s + this.filteredMessage.getString()).withStyle(style);
            ModNetwork.CHANNEL.sendToAllClientPlayersInRange(server, pos, this.volume, new ClientBoundPlaySpeakerMessagePacket(component, filtered, this.mode));
        }
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public boolean tryAcceptingClientText(ServerPlayer player, FilteredText filteredText) {
        this.validatePlayerWhoMayEdit(this.f_58857_, this.f_58858_);
        if (player.m_20148_().equals(this.getPlayerWhoMayEdit())) {
            this.acceptClientMessages(player, filteredText);
            this.setPlayerWhoMayEdit(null);
            return true;
        } else {
            Supplementaries.LOGGER.warn("Player {} just tried to change non-editable speaker block", player.m_7755_().getString());
            return false;
        }
    }

    private void acceptClientMessages(Player player, FilteredText filteredText) {
        Style style = this.getMessage(player.isTextFilteringEnabled()).getStyle();
        if (player.isTextFilteringEnabled()) {
            this.setMessage(Component.literal(filteredText.filteredOrEmpty()).setStyle(style));
        } else {
            this.setMessage(Component.literal(filteredText.raw()).setStyle(style), Component.literal(filteredText.filteredOrEmpty()).setStyle(style));
        }
    }

    @Override
    public void setPlayerWhoMayEdit(UUID playerWhoMayEdit) {
        this.playerWhoMayEdit = playerWhoMayEdit;
    }

    @Override
    public UUID getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }

    @Override
    public void openScreen(Level level, BlockPos pos, Player player) {
        SpeakerBlockScreen.open(this);
    }

    public static enum Mode {

        CHAT, STATUS_MESSAGE, TITLE, NARRATOR
    }
}