package io.github.lightman314.lightmanscurrency.api.money.bank;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountAdvancedMenu;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountMenu;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReferenceType;
import io.github.lightman314.lightmanscurrency.api.money.bank.source.BankAccountSource;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.impl.BankAPIImpl;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public abstract class BankAPI {

    public static final BankAPI API = BankAPIImpl.INSTANCE;

    public abstract void RegisterReferenceType(@Nonnull BankReferenceType var1);

    @Deprecated(since = "2.2.1.1")
    public static void registerType(@Nonnull BankReferenceType type) {
        API.RegisterReferenceType(type);
    }

    public abstract void RegisterBankAccountSource(@Nonnull BankAccountSource var1);

    @Nullable
    public abstract BankReferenceType GetReferenceType(@Nonnull ResourceLocation var1);

    @Deprecated(since = "2.2.1.1")
    @Nullable
    public static BankReferenceType getType(@Nonnull ResourceLocation type) {
        return API.GetReferenceType(type);
    }

    public abstract List<IBankAccount> GetAllBankAccounts(boolean var1);

    public abstract List<BankReference> GetAllBankReferences(boolean var1);

    public abstract void BankDeposit(@Nonnull IBankAccountMenu var1, @Nonnull MoneyValue var2);

    public abstract void BankDeposit(@Nonnull Player var1, @Nonnull Container var2, @Nonnull BankReference var3, @Nonnull MoneyValue var4);

    @Deprecated(since = "2.2.1.1")
    public static void DepositCoins(@Nonnull IBankAccountMenu menu, @Nonnull MoneyValue amount) {
        API.BankDeposit(menu, amount);
    }

    @Deprecated(since = "2.2.1.1")
    public static void DepositCoins(@Nonnull Player player, @Nonnull Container coinInput, @Nonnull IBankAccount account, @Nonnull MoneyValue amount) {
    }

    public final boolean BankDepositFromServer(@Nonnull IBankAccount account, @Nonnull MoneyValue amount) {
        return this.BankDepositFromServer(account, amount, true);
    }

    public abstract boolean BankDepositFromServer(@Nonnull IBankAccount var1, @Nonnull MoneyValue var2, boolean var3);

    @Nonnull
    public final Pair<Boolean, MoneyValue> BankWithdrawFromServer(@Nonnull IBankAccount account, @Nonnull MoneyValue amount) {
        return this.BankWithdrawFromServer(account, amount, true);
    }

    @Nonnull
    public abstract Pair<Boolean, MoneyValue> BankWithdrawFromServer(@Nonnull IBankAccount var1, @Nonnull MoneyValue var2, boolean var3);

    @Deprecated(since = "2.2.1.1")
    public static boolean ServerGiveCoins(@Nonnull IBankAccount account, @Nonnull MoneyValue amount) {
        return API.BankDepositFromServer(account, amount);
    }

    @Deprecated(since = "2.2.1.1")
    public static Pair<Boolean, MoneyValue> ServerTakeCoins(@Nonnull IBankAccount account, MoneyValue amount) {
        return API.BankWithdrawFromServer(account, amount);
    }

    public abstract void BankWithdraw(@Nonnull IBankAccountMenu var1, @Nonnull MoneyValue var2);

    public abstract void BankWithdraw(@Nonnull Player var1, @Nonnull Container var2, @Nonnull BankReference var3, @Nonnull MoneyValue var4);

    @Deprecated(since = "2.2.1.1")
    public static void WithdrawCoins(@Nonnull IBankAccountMenu menu, @Nonnull MoneyValue amount) {
        API.BankWithdraw(menu, amount);
    }

    @Deprecated(since = "2.2.1.1")
    public static void WithdrawCoins(@Nonnull Player player, @Nonnull Container coinOutput, @Nonnull IBankAccount account, @Nonnull MoneyValue amount) {
    }

    @Nonnull
    public abstract MutableComponent BankTransfer(@Nonnull IBankAccountAdvancedMenu var1, @Nonnull MoneyValue var2, @Nonnull IBankAccount var3);

    @Nonnull
    public abstract MutableComponent BankTransfer(@Nonnull Player var1, BankReference var2, @Nonnull MoneyValue var3, IBankAccount var4);

    @Deprecated(since = "2.2.1.1")
    public static MutableComponent TransferCoins(@Nonnull IBankAccountAdvancedMenu menu, @Nonnull MoneyValue amount, BankReference destination) {
        return TransferCoins(menu.getPlayer(), menu.getBankAccount(), amount, destination == null ? null : destination.get());
    }

    @Deprecated(since = "2.2.1.1")
    public static MutableComponent TransferCoins(@Nonnull Player player, @Nonnull IBankAccount fromAccount, @Nonnull MoneyValue amount, @Nonnull IBankAccount destinationAccount) {
        return EasyText.literal("Outdated API usage!");
    }
}