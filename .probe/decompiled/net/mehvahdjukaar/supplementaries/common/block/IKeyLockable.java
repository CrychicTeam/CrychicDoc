package net.mehvahdjukaar.supplementaries.common.block;

import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IKeyLockable {

    int MAX_ITEM_NAME_LEN = 50;

    void setPassword(String var1);

    String getPassword();

    void clearPassword();

    default void onPasswordCleared(Player player, BlockPos pos) {
        player.displayClientMessage(Component.translatable("message.supplementaries.safe.cleared"), true);
        player.m_9236_().playSound(null, pos, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.5F, 1.5F);
    }

    default boolean shouldShowPassword() {
        String password = this.getPassword();
        return password != null && password.length() <= 50;
    }

    default void onKeyAssigned(Level level, BlockPos pos, Player player, String newKey) {
        Component message;
        if (this.shouldShowPassword()) {
            message = Component.translatable("message.supplementaries.safe.assigned_key", newKey);
        } else {
            message = Component.translatable("message.supplementaries.safe.assigned_key_generic");
        }
        player.displayClientMessage(message, true);
        level.playSound(null, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.5F, 1.5F);
    }

    default IKeyLockable.KeyStatus getKeyStatus(ItemStack key) {
        return getKeyStatus(key, this.getPassword());
    }

    static IKeyLockable.KeyStatus getKeyStatus(ItemStack key, String password) {
        String correct = getKeyPassword(key);
        if (correct != null) {
            return correct.equals(password) ? IKeyLockable.KeyStatus.CORRECT_KEY : IKeyLockable.KeyStatus.INCORRECT_KEY;
        } else {
            return IKeyLockable.KeyStatus.NO_KEY;
        }
    }

    @Nullable
    static String getKeyPassword(ItemStack key) {
        if (key.getItem() instanceof KeyItem k) {
            return k.getPassword(key);
        } else {
            return key.is(ModTags.KEYS) ? ((KeyItem) ModRegistry.KEY_ITEM.get()).getPassword(key) : null;
        }
    }

    default boolean testIfHasCorrectKey(Player player, String lockPassword, boolean feedbackMessage, @Nullable String translName) {
        IKeyLockable.KeyStatus key = ItemsUtil.hasKeyInInventory(player, lockPassword);
        if (key == IKeyLockable.KeyStatus.INCORRECT_KEY) {
            if (feedbackMessage) {
                player.displayClientMessage(Component.translatable("message.supplementaries.safe.incorrect_key"), true);
            }
            return false;
        } else if (key == IKeyLockable.KeyStatus.CORRECT_KEY) {
            return true;
        } else {
            if (feedbackMessage) {
                player.displayClientMessage(Component.translatable("message.supplementaries." + translName + ".locked"), true);
            }
            return false;
        }
    }

    public static enum KeyStatus {

        CORRECT_KEY, INCORRECT_KEY, NO_KEY;

        public boolean isCorrect() {
            return this == CORRECT_KEY;
        }
    }
}