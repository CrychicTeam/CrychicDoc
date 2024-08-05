package top.prefersmin.waystoneandlightman.handler;

import com.mojang.logging.LogUtils;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import net.blay09.mods.waystones.api.WaystoneTeleportEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import top.prefersmin.waystoneandlightman.config.ModConfig;
import top.prefersmin.waystoneandlightman.util.CostUtil;
import top.prefersmin.waystoneandlightman.vo.TeleportCostVo;

@EventBusSubscriber(modid = "waystoneandlightman", bus = Bus.FORGE)
public class TeleportHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    @NotNull
    private static String getMoneyCostString(MoneyValue moneyCost) {
        String moneyCostString = moneyCost.getString();
        if (ModConfig.forceEnableChineseLanguage) {
            moneyCostString = moneyCostString.replace("c", "铜币 ").replace("i", "铁币 ").replace("g", "金币 ").replace("e", "绿宝石币 ").replace("d", "钻石币 ").replace("n", "下界合金币 ");
        }
        return moneyCostString;
    }

    @SubscribeEvent
    public void onWayStoneTeleport(WaystoneTeleportEvent.Pre event) {
        if (event.getContext().getEntity() instanceof Player player) {
            if (player.getAbilities().instabuild) {
                return;
            }
            BlockPos pos = event.getContext().getTargetWaystone().getPos();
            int distance = (int) player.m_20182_().distanceTo(pos.getCenter());
            TeleportCostVo teleportCostVo = CostUtil.TeleportCostCalculate(player, distance);
            if (teleportCostVo.isCanAfford()) {
                MoneyAPI.API.GetPlayersMoneyHandler(player).extractMoney(teleportCostVo.getCost(), false);
            } else {
                event.setCanceled(true);
            }
            String moneyCostString = getMoneyCostString(teleportCostVo.getCost());
            if (ModConfig.enableConsoleLog) {
                LOGGER.info("--------------------------------------------");
                LOGGER.info(Component.translatable("gui.teleportLog", player.getName().getString(), distance).getString());
                if (teleportCostVo.isCanAfford()) {
                    LOGGER.info(Component.translatable("gui.teleportCost", moneyCostString).getString());
                } else {
                    LOGGER.info(Component.translatable("gui.notSufficientFundsLog", moneyCostString).getString());
                }
                LOGGER.info("--------------------------------------------");
            }
            if (teleportCostVo.getCost().isFree()) {
                return;
            }
            if (ModConfig.enableCostTip) {
                Component message;
                if (teleportCostVo.isCanAfford()) {
                    message = Component.translatable("gui.alertMoneyCost", moneyCostString);
                } else {
                    message = Component.translatable("gui.notSufficientFunds");
                }
                player.displayClientMessage(message, true);
            }
        }
    }
}