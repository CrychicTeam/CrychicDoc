package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.client.screens.DoormatScreen;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.supplementaries.common.block.blocks.DoormatBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DoormatBlockTile extends ItemDisplayTile implements ITextHolderProvider {

    public static final int MAX_LINES = 3;

    public final TextHolder textHolder;

    @Nullable
    private UUID playerWhoMayEdit;

    private boolean isWaxed = false;

    public DoormatBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.DOORMAT_TILE.get(), pos, state);
        this.textHolder = new TextHolder(3, 75);
    }

    @Override
    public TextHolder getTextHolder(int i) {
        return this.textHolder;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.textHolder.load(compound, this.f_58857_, this.m_58899_());
        if (compound.contains("Waxed")) {
            this.isWaxed = compound.getBoolean("Waxed");
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.textHolder.save(tag);
        if (this.isWaxed) {
            tag.putBoolean("Waxed", this.isWaxed);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.supplementaries.doormat");
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(DoormatBlock.FACING);
    }

    @Override
    public void openScreen(Level level, BlockPos pos, Player player) {
        DoormatScreen.open(this);
    }

    @Override
    public SoundEvent getAddItemSound() {
        return SoundEvents.BRUSH_GENERIC;
    }

    @Override
    public void setWaxed(boolean waxed) {
        this.isWaxed = waxed;
    }

    @Override
    public boolean isWaxed() {
        return this.isWaxed;
    }

    @Override
    public void setPlayerWhoMayEdit(UUID playerWhoMayEdit) {
        this.playerWhoMayEdit = playerWhoMayEdit;
    }

    @Override
    public UUID getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }
}