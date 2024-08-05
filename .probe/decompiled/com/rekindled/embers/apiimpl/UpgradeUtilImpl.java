package com.rekindled.embers.apiimpl;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.IUpgradeProvider;
import com.rekindled.embers.api.upgrades.IUpgradeProxy;
import com.rekindled.embers.api.upgrades.IUpgradeUtil;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public class UpgradeUtilImpl implements IUpgradeUtil {

    ThreadLocal<Set<IUpgradeProxy>> checkedProxies = ThreadLocal.withInitial(HashSet::new);

    @Override
    public List<UpgradeContext> getUpgrades(Level world, BlockPos pos, Direction[] facings) {
        LinkedList<UpgradeContext> upgrades = new LinkedList();
        this.getUpgrades(world, pos, facings, upgrades);
        return upgrades;
    }

    @Override
    public void getUpgrades(Level world, BlockPos pos, Direction[] facings, List<UpgradeContext> upgrades) {
        for (Direction facing : facings) {
            this.collectUpgrades(world, pos.relative(facing), facing.getOpposite(), upgrades);
        }
        this.resetCheckedProxies();
    }

    private boolean isProxyChecked(IUpgradeProxy proxy) {
        return ((Set) this.checkedProxies.get()).contains(proxy);
    }

    private void addCheckedProxy(IUpgradeProxy proxy) {
        ((Set) this.checkedProxies.get()).add(proxy);
    }

    private void resetCheckedProxies() {
        this.checkedProxies.remove();
    }

    @Override
    public void collectUpgrades(Level world, BlockPos pos, Direction side, List<UpgradeContext> upgrades) {
        this.collectUpgrades(world, pos, side, upgrades, ConfigManager.MAX_PROXY_DISTANCE.get());
    }

    @Override
    public void collectUpgrades(Level world, BlockPos pos, Direction side, List<UpgradeContext> upgrades, int distanceLeft) {
        if (distanceLeft >= 0) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te != null) {
                IUpgradeProvider cap = (IUpgradeProvider) te.getCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY, side).orElse(null);
                if (cap != null) {
                    upgrades.add(new UpgradeContext(cap, ConfigManager.MAX_PROXY_DISTANCE.get() - distanceLeft));
                }
            }
            if (te instanceof IUpgradeProxy proxy && !this.isProxyChecked(proxy) && proxy.isProvider(side)) {
                this.addCheckedProxy(proxy);
                proxy.collectUpgrades(upgrades, distanceLeft - 1);
            }
        }
    }

    @Override
    public void verifyUpgrades(BlockEntity tile, List<UpgradeContext> list) {
        HashMap<String, Integer> upgradeCounts = new HashMap();
        list.forEach(x -> {
            String id = x.upgrade().getUpgradeId().toString();
            upgradeCounts.put(id, (Integer) upgradeCounts.getOrDefault(id, 0) + 1);
        });
        list.removeIf(x -> (Integer) upgradeCounts.get(x.upgrade().getUpgradeId().toString()) > x.upgrade().getLimit(tile));
        list.sort((x, y) -> Integer.compare(x.upgrade().getPriority(), y.upgrade().getPriority()));
        for (UpgradeContext upgrade : list) {
            upgrade.setCount((Integer) upgradeCounts.getOrDefault(upgrade.upgrade().getUpgradeId().toString(), 0));
        }
    }

    @Override
    public int getWorkTime(BlockEntity tile, int time, List<UpgradeContext> list) {
        double speedmod = this.getTotalSpeedModifier(tile, list);
        return speedmod == 0.0 ? Integer.MAX_VALUE : (int) ((double) time * (1.0 / speedmod));
    }

    @Override
    public double getTotalSpeedModifier(BlockEntity tile, List<UpgradeContext> list) {
        double total = 1.0;
        for (UpgradeContext upgrade : list) {
            total = upgrade.upgrade().getSpeed(tile, total, upgrade.distance(), upgrade.count());
        }
        return total;
    }

    @Override
    public boolean doTick(BlockEntity tile, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            if (upgrade.upgrade().doTick(tile, list, upgrade.distance(), upgrade.count())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doWork(BlockEntity tile, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            if (upgrade.upgrade().doWork(tile, list, upgrade.distance(), upgrade.count())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getTotalEmberConsumption(BlockEntity tile, double ember, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            ember = upgrade.upgrade().transformEmberConsumption(tile, ember, upgrade.distance(), upgrade.count());
        }
        return ember;
    }

    @Override
    public double getTotalEmberProduction(BlockEntity tile, double ember, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            ember = upgrade.upgrade().transformEmberProduction(tile, ember, upgrade.distance(), upgrade.count());
        }
        return ember;
    }

    @Override
    public void transformOutput(BlockEntity tile, List<ItemStack> outputs, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            upgrade.upgrade().transformOutput(tile, outputs, upgrade.distance(), upgrade.count());
        }
    }

    @Override
    public FluidStack transformOutput(BlockEntity tile, FluidStack output, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            output = upgrade.upgrade().transformOutput(tile, output, upgrade.distance(), upgrade.count());
        }
        return output;
    }

    @Override
    public boolean getOtherParameter(BlockEntity tile, String type, boolean initial, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            initial = upgrade.upgrade().getOtherParameter(tile, type, initial, upgrade.distance(), upgrade.count());
        }
        return initial;
    }

    @Override
    public double getOtherParameter(BlockEntity tile, String type, double initial, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            initial = upgrade.upgrade().getOtherParameter(tile, type, initial, upgrade.distance(), upgrade.count());
        }
        return initial;
    }

    @Override
    public int getOtherParameter(BlockEntity tile, String type, int initial, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            initial = upgrade.upgrade().getOtherParameter(tile, type, initial, upgrade.distance(), upgrade.count());
        }
        return initial;
    }

    @Override
    public String getOtherParameter(BlockEntity tile, String type, String initial, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            initial = upgrade.upgrade().getOtherParameter(tile, type, initial, upgrade.distance(), upgrade.count());
        }
        return initial;
    }

    @Override
    public <T> T getOtherParameter(BlockEntity tile, String type, T initial, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            initial = upgrade.upgrade().getOtherParameter(tile, type, initial, upgrade.distance(), upgrade.count());
        }
        return initial;
    }

    @Override
    public void throwEvent(BlockEntity tile, UpgradeEvent event, List<UpgradeContext> list) {
        for (UpgradeContext upgrade : list) {
            upgrade.upgrade().throwEvent(tile, list, event, upgrade.distance(), upgrade.count());
        }
    }
}