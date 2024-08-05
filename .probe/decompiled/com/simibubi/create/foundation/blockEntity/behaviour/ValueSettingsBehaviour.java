package com.simibubi.create.foundation.blockEntity.behaviour;

import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public interface ValueSettingsBehaviour extends ClipboardCloneable {

    boolean testHit(Vec3 var1);

    boolean isActive();

    default boolean onlyVisibleWithWrench() {
        return false;
    }

    default void newSettingHovered(ValueSettingsBehaviour.ValueSettings valueSetting) {
    }

    ValueBoxTransform getSlotPositioning();

    ValueSettingsBoard createBoard(Player var1, BlockHitResult var2);

    void setValueSettings(Player var1, ValueSettingsBehaviour.ValueSettings var2, boolean var3);

    ValueSettingsBehaviour.ValueSettings getValueSettings();

    default boolean acceptsValueSettings() {
        return true;
    }

    @Override
    default String getClipboardKey() {
        return "Settings";
    }

    @Override
    default boolean writeToClipboard(CompoundTag tag, Direction side) {
        if (!this.acceptsValueSettings()) {
            return false;
        } else {
            ValueSettingsBehaviour.ValueSettings valueSettings = this.getValueSettings();
            tag.putInt("Value", valueSettings.value());
            tag.putInt("Row", valueSettings.row());
            return true;
        }
    }

    @Override
    default boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!this.acceptsValueSettings()) {
            return false;
        } else if (!tag.contains("Value") || !tag.contains("Row")) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            this.setValueSettings(player, new ValueSettingsBehaviour.ValueSettings(tag.getInt("Row"), tag.getInt("Value")), false);
            return true;
        }
    }

    default void playFeedbackSound(BlockEntityBehaviour origin) {
        origin.getWorld().playSound(null, origin.getPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.25F, 2.0F);
        origin.getWorld().playSound(null, origin.getPos(), (SoundEvent) SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.get(), SoundSource.BLOCKS, 0.03F, 1.125F);
    }

    default void onShortInteract(Player player, InteractionHand hand, Direction side) {
    }

    public static record ValueSettings(int row, int value) {

        public MutableComponent format() {
            return Lang.number((double) this.value).component();
        }
    }
}