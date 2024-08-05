package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PresentBlock;
import net.mehvahdjukaar.supplementaries.common.inventories.PresentContainerMenu;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PresentBlockTile extends AbstractPresentBlockTile {

    public static final String PUBLIC_KEY = "@e";

    private String recipient = "";

    private String sender = "";

    private String description = "";

    public PresentBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.PRESENT_TILE.get(), pos, state);
    }

    @Override
    public boolean canHoldItems() {
        return this.isPacked();
    }

    public boolean isPacked() {
        return (Boolean) this.m_58900_().m_61143_(PresentBlock.PACKED);
    }

    public String getSender() {
        return this.sender;
    }

    public String getDescription() {
        return this.description;
    }

    public String getRecipient() {
        return this.recipient.equalsIgnoreCase("@e") ? "" : this.recipient;
    }

    public void setSender(String sender) {
        this.sender = sender.trim();
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient.trim();
    }

    public void setPublic() {
        this.setRecipient("@e");
    }

    public void updateState(boolean shouldPack, String newRecipient, String sender, String description) {
        if (shouldPack) {
            if (newRecipient.isEmpty()) {
                newRecipient = "@e";
            }
            this.recipient = newRecipient;
            this.sender = sender;
            this.description = description;
        } else {
            this.recipient = "";
            this.sender = "";
            this.description = "";
        }
        if (!this.f_58857_.isClientSide && this.isPacked() != shouldPack) {
            if (shouldPack) {
                this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) ModSounds.PRESENT_PACK.get(), SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.95F);
            } else {
                this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) ModSounds.PRESENT_OPEN.get(), SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 1.2F);
            }
            this.f_58857_.setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(PresentBlock.PACKED, shouldPack), 3);
        }
    }

    @Override
    public boolean canOpen(Player player) {
        if (!super.m_7525_(player)) {
            return false;
        } else if (!this.isUnused()) {
            return false;
        } else {
            return player.isCreative() ? true : this.recipient.isEmpty() || this.recipient.equalsIgnoreCase("@e") || this.recipient.equalsIgnoreCase(player.getName().getString()) || this.sender.equalsIgnoreCase(player.getName().getString());
        }
    }

    @Override
    public InteractionResult interact(Level level, BlockPos pos, BlockState state, Player player) {
        if (this.isUnused()) {
            if (this.canOpen(player)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PlatHelper.openCustomMenu(serverPlayer, this, pos);
                    PiglinAi.angerNearbyPiglins(player, true);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                player.displayClientMessage(Component.translatable("message.supplementaries.present.info", this.recipient), true);
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.present");
    }

    @Override
    public void load(CompoundTag tag) {
        super.m_142466_(tag);
        this.recipient = "";
        this.sender = "";
        this.description = "";
        if (tag.contains("Recipient")) {
            this.recipient = tag.getString("Recipient");
        }
        if (tag.contains("Sender")) {
            this.sender = tag.getString("Sender");
        }
        if (tag.contains("Description")) {
            this.description = tag.getString("Description");
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.m_183515_(tag);
        if (!this.recipient.isEmpty()) {
            tag.putString("Recipient", this.recipient);
        }
        if (!this.sender.isEmpty()) {
            tag.putString("Sender", this.sender);
        }
        if (!this.description.isEmpty()) {
            tag.putString("Description", this.description);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new PresentContainerMenu(id, player, this);
    }

    @Nullable
    public Component getSenderMessage() {
        return getSenderMessage(this.sender);
    }

    @Nullable
    public static Component getSenderMessage(String sender) {
        return sender.isEmpty() ? null : Component.translatable("message.supplementaries.present.from", sender);
    }

    @Nullable
    public Component getRecipientMessage() {
        return getRecipientMessage(this.recipient);
    }

    @Nullable
    public static Component getRecipientMessage(String recipient) {
        if (recipient.isEmpty()) {
            return null;
        } else {
            return recipient.equalsIgnoreCase("@e") ? Component.translatable("message.supplementaries.present.public") : Component.translatable("message.supplementaries.present.to", recipient);
        }
    }
}