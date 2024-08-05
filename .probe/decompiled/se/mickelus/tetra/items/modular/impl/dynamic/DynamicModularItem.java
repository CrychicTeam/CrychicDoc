package se.mickelus.tetra.items.modular.impl.dynamic;

import java.util.Arrays;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

public class DynamicModularItem extends ItemModularHandheld {

    public static final String identifier = "dynamic_handheld";

    public static final String typeKey = "archetype";

    public DynamicModularItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    protected Optional<ArchetypeDefinition> getDefinition(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getTag()).map(tag -> tag.getString("archetype")).map(key -> new ResourceLocation("tetra", key)).map(rl -> DataManager.instance.archetypeData.getData(rl));
    }

    @Override
    public String[] getMajorModuleKeys(ItemStack itemStack) {
        return (String[]) this.getDefinition(itemStack).map(ArchetypeDefinition::slots).map(slots -> (String[]) Arrays.stream(slots).filter(ArchetypeSlotDefinition::major).map(ArchetypeSlotDefinition::key).toArray(String[]::new)).orElse(new String[0]);
    }

    @Override
    public String[] getMinorModuleKeys(ItemStack itemStack) {
        return (String[]) this.getDefinition(itemStack).map(ArchetypeDefinition::slots).map(slots -> (String[]) Arrays.stream(slots).filter(definition -> !definition.major()).map(ArchetypeSlotDefinition::key).toArray(String[]::new)).orElse(new String[0]);
    }

    @Override
    public String[] getRequiredModules(ItemStack itemStack) {
        return (String[]) this.getDefinition(itemStack).map(ArchetypeDefinition::slots).map(slots -> (String[]) Arrays.stream(slots).filter(ArchetypeSlotDefinition::required).map(ArchetypeSlotDefinition::key).toArray(String[]::new)).orElse(new String[0]);
    }

    @Override
    public boolean canGainHoneProgress(ItemStack itemStack) {
        return (Boolean) this.getDefinition(itemStack).map(ArchetypeDefinition::honeable).orElse(false);
    }

    @Override
    public int getHoneBase(ItemStack itemStack) {
        return (Integer) this.getDefinition(itemStack).map(ArchetypeDefinition::honeBase).orElse(0);
    }

    @Override
    public int getHoneIntegrityMultiplier(ItemStack itemStack) {
        return (Integer) this.getDefinition(itemStack).map(ArchetypeDefinition::honeIntegrityMultiplier).orElse(0);
    }

    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return (GuiModuleOffsets) this.getDefinition(itemStack).map(ArchetypeDefinition::slots).map(slots -> (ArchetypeSlotDefinition[]) Arrays.stream(slots).filter(ArchetypeSlotDefinition::major).toArray(ArchetypeSlotDefinition[]::new)).map(GuiModuleOffsets::new).orElse(new GuiModuleOffsets());
    }

    @Override
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return (GuiModuleOffsets) this.getDefinition(itemStack).map(ArchetypeDefinition::slots).map(slots -> (ArchetypeSlotDefinition[]) Arrays.stream(slots).filter(slot -> !slot.major()).toArray(ArchetypeSlotDefinition[]::new)).map(GuiModuleOffsets::new).orElse(new GuiModuleOffsets());
    }
}