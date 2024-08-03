package io.redspace.ironsspellbooks.entity.mobs.wizards;

import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IMerchantWizard extends Merchant {

    default void serializeMerchant(CompoundTag pCompound, @Nullable MerchantOffers offers, long lastRestockGameTime, int numberOfRestocksToday) {
        if (offers != null && !offers.isEmpty()) {
            pCompound.put("Offers", offers.createTag());
        }
        pCompound.putLong("LastRestock", lastRestockGameTime);
        pCompound.putInt("RestocksToday", numberOfRestocksToday);
    }

    default void deserializeMerchant(CompoundTag pCompound, Consumer<MerchantOffers> setOffers) {
        if (pCompound.contains("Offers", 10)) {
            setOffers.accept(new MerchantOffers(pCompound.getCompound("Offers")));
        }
        this.setLastRestockGameTime(pCompound.getLong("LastRestock"));
        this.setRestocksToday(pCompound.getInt("RestocksToday"));
    }

    default boolean isTrading() {
        return this.m_7962_() != null;
    }

    default boolean needsToRestock() {
        for (MerchantOffer merchantoffer : this.m_6616_()) {
            if (merchantoffer.needsRestock()) {
                return true;
            }
        }
        return false;
    }

    default boolean allowedToRestock() {
        return this.getRestocksToday() == 0 && this.level().getGameTime() > this.getLastRestockGameTime() + 2400L;
    }

    default boolean shouldRestock() {
        long timeToNextRestock = this.getLastRestockGameTime() + 12000L;
        long currentGameTime = this.level().getGameTime();
        boolean hasDayElapsed = currentGameTime > timeToNextRestock;
        long currentDayTime = this.level().getDayTime();
        if (this.getLastRestockCheckDayTime() > 0L) {
            long lastRestockDay = this.getLastRestockCheckDayTime() / 24000L;
            long currentDay = currentDayTime / 24000L;
            hasDayElapsed |= currentDay > lastRestockDay;
        } else {
            this.setLastRestockCheckDayTime(currentDayTime);
        }
        if (hasDayElapsed) {
            this.setLastRestockGameTime(currentGameTime);
            this.setLastRestockCheckDayTime(currentDayTime);
            this.setRestocksToday(0);
        }
        return this.needsToRestock() && this.allowedToRestock();
    }

    default void restock() {
        for (MerchantOffer offer : this.m_6616_()) {
            offer.updateDemand();
            offer.resetUses();
        }
        this.setRestocksToday(this.getRestocksToday() + 1);
    }

    int getRestocksToday();

    void setRestocksToday(int var1);

    long getLastRestockGameTime();

    void setLastRestockGameTime(long var1);

    long getLastRestockCheckDayTime();

    void setLastRestockCheckDayTime(long var1);

    Level level();

    @Override
    default int getVillagerXp() {
        return 0;
    }

    @Override
    default void overrideXp(int pXp) {
    }

    @Override
    default boolean showProgressBar() {
        return false;
    }

    @Override
    default boolean isClientSide() {
        return this.level().isClientSide();
    }
}