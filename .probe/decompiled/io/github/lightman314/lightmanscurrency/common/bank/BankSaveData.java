package io.github.lightman314.lightmanscurrency.common.bank;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.client.data.ClientBankData;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketSelectBankAccount;
import io.github.lightman314.lightmanscurrency.network.message.data.bank.SPacketClearClientBank;
import io.github.lightman314.lightmanscurrency.network.message.data.bank.SPacketSyncSelectedBankAccount;
import io.github.lightman314.lightmanscurrency.network.message.data.bank.SPacketUpdateClientBank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "lightmanscurrency")
public class BankSaveData extends SavedData {

    private final Map<UUID, Pair<BankAccount, BankReference>> playerBankData = new HashMap();

    private int interestTick = 0;

    private BankSaveData() {
    }

    private BankSaveData(CompoundTag compound) {
        ListTag bankData = compound.getList("PlayerBankData", 10);
        for (int i = 0; i < bankData.size(); i++) {
            CompoundTag tag = bankData.getCompound(i);
            UUID player = tag.getUUID("Player");
            BankAccount bankAccount = loadBankAccount(player, tag.getCompound("BankAccount"));
            BankReference lastSelected = BankReference.load(tag.getCompound("LastSelected"));
            this.playerBankData.put(player, Pair.of(bankAccount, lastSelected));
        }
        if (compound.contains("InterestTick")) {
            this.interestTick = compound.getInt("InterestTick");
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag bankData = new ListTag();
        this.playerBankData.forEach((player, data) -> {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("Player", player);
            tag.put("BankAccount", ((BankAccount) data.getFirst()).save());
            tag.put("LastSelected", ((BankReference) data.getSecond()).save());
            bankData.add(tag);
        });
        compound.put("PlayerBankData", bankData);
        compound.putInt("InterestTick", this.interestTick);
        return compound;
    }

    private static BankAccount loadBankAccount(UUID player, CompoundTag compound) {
        BankAccount bankAccount = new BankAccount(() -> MarkBankAccountDirty(player), compound);
        try {
            bankAccount.setNotificationConsumer(BankAccount.generateNotificationAcceptor(player));
            bankAccount.updateOwnersName(PlayerReference.of(player, bankAccount.getOwnersName()).getName(false));
        } catch (Throwable var4) {
        }
        return bankAccount;
    }

    private static BankAccount generateBankAccount(UUID player) {
        BankAccount bankAccount = new BankAccount(() -> MarkBankAccountDirty(player));
        try {
            bankAccount.setNotificationConsumer(BankAccount.generateNotificationAcceptor(player));
            bankAccount.updateOwnersName(PlayerReference.of(player, bankAccount.getOwnersName()).getName(false));
        } catch (Throwable var3) {
        }
        return bankAccount;
    }

    private static BankSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                return level.getDataStorage().computeIfAbsent(BankSaveData::new, BankSaveData::new, "lightmanscurrency_bank_data");
            }
        }
        return null;
    }

    public static List<BankReference> GetPlayerBankAccounts(boolean isClient) {
        if (isClient) {
            return ClientBankData.GetPlayerBankAccounts();
        } else {
            List<BankReference> results = new ArrayList();
            BankSaveData bsd = get();
            if (bsd != null) {
                for (UUID player : bsd.playerBankData.keySet()) {
                    results.add(PlayerBankReference.of(player));
                }
            }
            return results;
        }
    }

    public static BankAccount GetBankAccount(Player player) {
        return GetBankAccount(player.m_9236_().isClientSide, player.m_20148_());
    }

    public static BankAccount GetBankAccount(boolean isClient, UUID player) {
        if (isClient) {
            return ClientBankData.GetPlayerBankAccount(player);
        } else {
            BankSaveData bsd = get();
            if (bsd != null) {
                if (bsd.playerBankData.containsKey(player)) {
                    return (BankAccount) ((Pair) bsd.playerBankData.get(player)).getFirst();
                } else {
                    BankAccount newAccount = generateBankAccount(player);
                    bsd.playerBankData.put(player, Pair.of(newAccount, PlayerBankReference.of(player)));
                    MarkBankAccountDirty(player);
                    return newAccount;
                }
            } else {
                return null;
            }
        }
    }

    public static void MarkBankAccountDirty(UUID player) {
        BankSaveData bsd = get();
        if (bsd != null) {
            bsd.m_77762_();
            BankAccount bankAccount = GetBankAccount(false, player);
            new SPacketUpdateClientBank(player, bankAccount.save()).sendToAll();
        }
    }

    public static BankReference GetSelectedBankAccount(Player player) {
        if (player.m_9236_().isClientSide) {
            ClientBankData.GetLastSelectedAccount();
        } else {
            BankSaveData bsd = get();
            if (bsd != null) {
                if (bsd.playerBankData.containsKey(player.m_20148_())) {
                    BankReference account = (BankReference) ((Pair) bsd.playerBankData.get(player.m_20148_())).getSecond();
                    if (!account.allowedAccess(player)) {
                        LightmansCurrency.LogInfo(player.getName().getString() + " is no longer allowed to access their selected bank account. Switching back to their personal account.");
                        account = PlayerBankReference.of(player);
                        SetSelectedBankAccount(player, account);
                    }
                    return account;
                }
                BankReference account = PlayerBankReference.of(player);
                SetSelectedBankAccount(player, account);
                return account;
            }
        }
        return PlayerBankReference.of(player);
    }

    public static void SetSelectedBankAccount(Player player, BankReference account) {
        if (account != null) {
            if (player.m_9236_().isClientSide) {
                new CPacketSelectBankAccount(account).send();
            } else {
                if (!account.allowedAccess(player)) {
                    LightmansCurrency.LogInfo("Player does not have access to the selected account. Canceling selection.");
                    return;
                }
                BankSaveData bsd = get();
                if (bsd != null) {
                    if (bsd.playerBankData.containsKey(player.m_20148_())) {
                        bsd.playerBankData.put(player.m_20148_(), Pair.of((BankAccount) ((Pair) bsd.playerBankData.get(player.m_20148_())).getFirst(), account));
                    } else {
                        bsd.playerBankData.put(player.m_20148_(), Pair.of(generateBankAccount(player.m_20148_()), account));
                        MarkBankAccountDirty(player.m_20148_());
                    }
                    bsd.m_77762_();
                    try {
                        new SPacketSyncSelectedBankAccount(account).sendTo(player);
                    } catch (Throwable var4) {
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void OnPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PacketDistributor.PacketTarget target = LightmansCurrencyPacketHandler.getTarget(event.getEntity());
        BankSaveData bsd = get();
        GetBankAccount(event.getEntity());
        SPacketClearClientBank.INSTANCE.sendToTarget(target);
        bsd.playerBankData.forEach((id, data) -> new SPacketUpdateClientBank(id, ((BankAccount) data.getFirst()).save()).sendToTarget(target));
        BankReference selectedAccount = GetSelectedBankAccount(event.getEntity());
        new SPacketSyncSelectedBankAccount(selectedAccount).sendToTarget(target);
    }

    public static int InterestTick() {
        BankSaveData bsd = get();
        if (bsd != null) {
            bsd.interestTick++;
            bsd.m_77762_();
            return bsd.interestTick;
        } else {
            return 0;
        }
    }

    public static void ResetInterestTick() {
        BankSaveData bsd = get();
        if (bsd != null) {
            bsd.interestTick = 0;
            bsd.m_77762_();
        }
    }
}