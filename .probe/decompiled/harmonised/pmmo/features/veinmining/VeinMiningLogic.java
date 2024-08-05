package harmonised.pmmo.features.veinmining;

import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.compat.curios.CurioCompat;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.codecs.VeinData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.veinmining.capability.VeinProvider;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_SyncVein;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.RegistryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class VeinMiningLogic {

    public static final String VEIN_DATA = "vein_data";

    public static final String CURRENT_CHARGE = "vein_charge";

    public static final Map<UUID, Integer> maxBlocksPerPlayer = new HashMap();

    public static final Map<UUID, VeinShapeData.ShapeType> shapePerPlayer = new HashMap();

    public static void applyVein(ServerPlayer player, BlockPos pos) {
        if (Config.VEIN_ENABLED.get()) {
            ServerLevel level = player.serverLevel();
            Block block = level.m_8055_(pos).m_60734_();
            int cost = Core.get(level).getBlockConsume(block);
            if (cost > 0) {
                int charge = getCurrentCharge(player);
                int consumed = 0;
                int maxBlocks = Math.min(charge / cost, (Integer) maxBlocksPerPlayer.computeIfAbsent(player.m_20148_(), id -> 64));
                VeinShapeData.ShapeType mode = (VeinShapeData.ShapeType) shapePerPlayer.computeIfAbsent(player.m_20148_(), id -> VeinShapeData.ShapeType.AOE);
                VeinShapeData veinData = new VeinShapeData(level, pos, maxBlocks, mode, player.m_6350_());
                for (BlockPos veinable : veinData.getVein()) {
                    consumed += cost;
                    player.gameMode.destroyAndAck(veinable, 1, "Vein Break");
                }
                MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Vein Consumed: " + consumed + " charge");
                applyChargeCost(player, consumed, (double) charge);
            }
        }
    }

    public static void regenerateVein(ServerPlayer player) {
        if (Config.VEIN_ENABLED.get()) {
            Inventory inv = player.m_150109_();
            List<ItemStack> items = new ArrayList();
            items.addAll(inv.armor);
            items.add(inv.getSelected());
            items.addAll(inv.offhand);
            if (CurioCompat.hasCurio) {
                items = new ArrayList(items);
                items.addAll(CurioCompat.getItems(player));
            }
            Core core = Core.get(player.m_9236_());
            double currentCharge = (double) getCurrentCharge(player);
            int chargeCap = Config.BASE_CHARGE_CAP.get();
            double chargeRate = Config.BASE_CHARGE_RATE.get();
            for (ItemStack stack : items) {
                VeinData data;
                if (core.isActionPermitted(ReqType.WEAR, stack, player) && (data = core.getLoader().ITEM_LOADER.getData(RegistryUtil.getId(stack)).veinData()).chargeRate.isPresent()) {
                    chargeCap += data.chargeCap.orElse(0);
                    chargeRate += data.chargeRate.orElse(0.0);
                }
            }
            if (chargeRate != 0.0 && chargeCap != 0 && !(currentCharge >= (double) chargeCap)) {
                int fCap = chargeCap;
                double fRate = chargeRate * Config.VEIN_CHARGE_MODIFIER.get();
                if (currentCharge + fRate >= (double) fCap) {
                    player.getCapability(VeinProvider.VEIN_CAP).ifPresent(vein -> {
                        vein.setCharge((double) fCap);
                        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Regen at Cap: " + fCap);
                        Networking.sendToClient(new CP_SyncVein((double) fCap), player);
                    });
                } else {
                    player.getCapability(VeinProvider.VEIN_CAP).ifPresent(vein -> {
                        vein.setCharge(vein.getCharge() + fRate);
                        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Regen: " + (vein.getCharge() + fRate));
                        Networking.sendToClient(new CP_SyncVein(vein.getCharge() + fRate), player);
                    });
                }
            }
        }
    }

    public static int getCurrentCharge(Player player) {
        return (Integer) player.getCapability(VeinProvider.VEIN_CAP).map(vein -> (int) vein.getCharge()).orElse(0);
    }

    public static int getMaxChargeFromAllItems(Player player) {
        Inventory inv = player.getInventory();
        List<ItemStack> items = List.of(inv.getItem(36), inv.getItem(37), inv.getItem(38), inv.getItem(39), player.m_21205_(), player.m_21206_());
        if (CurioCompat.hasCurio) {
            items = new ArrayList(items);
            items.addAll(CurioCompat.getItems(player));
        }
        int totalCapacity = Config.BASE_CHARGE_CAP.get();
        for (ItemStack stack : items) {
            if (Core.get(player.m_9236_()).isActionPermitted(ReqType.WEAR, stack, player)) {
                totalCapacity += Core.get(player.m_9236_()).getLoader().ITEM_LOADER.getData(RegistryUtil.getId(stack)).veinData().chargeCap.orElse(0);
            }
        }
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Vein Capacity: " + totalCapacity);
        return totalCapacity;
    }

    private static void applyChargeCost(ServerPlayer player, int cost, double currentCharge) {
        player.getCapability(VeinProvider.VEIN_CAP).ifPresent(vein -> vein.setCharge(currentCharge - (double) cost));
        Networking.sendToClient(new CP_SyncVein(currentCharge - (double) cost), player);
    }
}