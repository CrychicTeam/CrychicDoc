package fr.frinn.custommachinery.common.upgrade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Upgrades {

    private List<MachineUpgrade> upgrades = Collections.emptyList();

    private Map<Item, List<MachineUpgrade>> upgradesByItem = Collections.emptyMap();

    private Map<ResourceLocation, List<MachineUpgrade>> upgradesByMachine = Collections.emptyMap();

    public void refresh(List<MachineUpgrade> upgrades) {
        this.upgrades = Collections.unmodifiableList(upgrades);
        this.upgradesByItem = (Map<Item, List<MachineUpgrade>>) upgrades.stream().collect(Collectors.groupingBy(MachineUpgrade::getItem));
        this.upgradesByMachine = (Map<ResourceLocation, List<MachineUpgrade>>) upgrades.stream().flatMap(upgrade -> upgrade.getMachines().stream()).distinct().collect(Collectors.toMap(Function.identity(), id -> upgrades.stream().filter(upgrade -> upgrade.getMachines().contains(id)).toList()));
    }

    public void addUpgrade(MachineUpgrade upgrade) {
        List<MachineUpgrade> upgrades = new ArrayList(this.upgrades);
        upgrades.add(upgrade);
        this.refresh(upgrades);
    }

    public List<MachineUpgrade> getAllUpgrades() {
        return this.upgrades;
    }

    public List<MachineUpgrade> getUpgradesForItem(Item item) {
        return (List<MachineUpgrade>) this.upgradesByItem.getOrDefault(item, Collections.emptyList());
    }

    public List<MachineUpgrade> getUpgradesForMachine(ResourceLocation machineID) {
        return (List<MachineUpgrade>) this.upgradesByMachine.getOrDefault(machineID, Collections.emptyList());
    }

    public List<MachineUpgrade> getUpgradesForItemAndMachine(Item item, ResourceLocation machineID) {
        return this.getUpgradesForItem(item).stream().filter(upgrade -> this.getUpgradesForMachine(machineID).contains(upgrade)).toList();
    }
}