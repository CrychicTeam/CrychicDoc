package io.github.lightman314.lightmanscurrency.common.capability.wallet;

import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IWalletHandler extends IMoneyHandler {

    ItemStack getWallet();

    void setWallet(ItemStack var1);

    void syncWallet(ItemStack var1);

    boolean visible();

    void setVisible(boolean var1);

    LivingEntity entity();

    boolean isDirty();

    void clean();

    void tick();

    CompoundTag save();

    void load(CompoundTag var1);
}