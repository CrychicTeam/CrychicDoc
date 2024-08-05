package top.prefersmin.waystoneandlightman.util;

import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import net.minecraft.world.entity.player.Player;
import top.prefersmin.waystoneandlightman.config.ModConfig;
import top.prefersmin.waystoneandlightman.vo.TeleportCostVo;

public class CostUtil {

    public static TeleportCostVo TeleportCostCalculate(Player player, int distance) {
        TeleportCostVo teleportCost = new TeleportCostVo();
        if (player.getAbilities().instabuild) {
            return teleportCost.free();
        } else {
            int moneyCostPerHundredMeter = ModConfig.moneyCostPerHundredMeter;
            int minimumCost = ModConfig.minimumCost;
            int maximumCost = ModConfig.maximumCost;
            int moneyValue = (ModConfig.roundUp ? (int) Math.ceil((double) distance / 100.0) : (int) Math.floor((double) distance / 100.0)) * moneyCostPerHundredMeter;
            int moneyInt = Math.min(Math.max(moneyValue, minimumCost), maximumCost);
            if (moneyInt == 0) {
                return teleportCost.free();
            } else {
                MoneyValue moneyCost = CoinValue.fromNumber("main", (long) moneyInt);
                IMoneyHolder handler = MoneyAPI.API.GetPlayersMoneyHandler(player);
                boolean canPlayerAfford = handler.getStoredMoney().containsValue(moneyCost) && handler.extractMoney(moneyCost, true).isEmpty();
                return teleportCost.canAfford(canPlayerAfford).cost(moneyCost);
            }
        }
    }
}