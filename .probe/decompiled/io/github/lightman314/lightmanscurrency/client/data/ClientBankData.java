package io.github.lightman314.lightmanscurrency.client.data;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.common.bank.BankAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class ClientBankData {

    private static final Map<UUID, BankAccount> loadedBankAccounts = new HashMap();

    private static BankReference lastSelectedAccount = null;

    public static BankAccount GetPlayerBankAccount(UUID playerID) {
        if (loadedBankAccounts.containsKey(playerID)) {
            return (BankAccount) loadedBankAccounts.get(playerID);
        } else {
            LightmansCurrency.LogWarning("No bank account for player with id " + playerID.toString() + " is present on the client.");
            return new BankAccount().flagAsClient();
        }
    }

    public static void ClearBankAccounts() {
        loadedBankAccounts.clear();
    }

    public static void UpdateBankAccount(UUID player, CompoundTag compound) {
        try {
            BankAccount account = new BankAccount(compound).flagAsClient();
            if (player != null && account != null) {
                loadedBankAccounts.put(player, account);
            }
        } catch (Exception var3) {
            LightmansCurrency.LogError("Error loading bank account on client!", var3);
        }
    }

    public static void UpdateLastSelectedAccount(BankReference reference) {
        lastSelectedAccount = reference;
    }

    public static BankReference GetLastSelectedAccount() {
        return lastSelectedAccount;
    }

    public static List<BankReference> GetPlayerBankAccounts() {
        List<BankReference> list = new ArrayList();
        for (UUID player : loadedBankAccounts.keySet()) {
            list.add(PlayerBankReference.of(player).flagAsClient());
        }
        return list;
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        loadedBankAccounts.clear();
        lastSelectedAccount = null;
    }
}