package se.mickelus.tetra.module.schematic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.GlyphData;

@ParametersAreNonnullByDefault
public class RepairSchematic extends BaseSchematic {

    private static final String localizationPrefix = "tetra/schematic/";

    private static final String nameSuffix = ".name";

    private static final String slotSuffix = ".slot1";

    private static final String descriptionSuffix = ".description";

    private static final String extendedDescriptionSuffix = ".description_details";

    private final String key = "repair";

    private final IModularItem item;

    private final String identifier;

    private final GlyphData glyph = new GlyphData(GuiTextures.toolActions, 0, 0);

    public RepairSchematic(IModularItem item, String identifier) {
        this.item = item;
        this.identifier = "repair/" + identifier;
    }

    public String getSlot(ItemStack itemStack) {
        return (String) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getRepairSlot(itemStack)).orElse(null);
    }

    @Override
    public String getKey() {
        return this.identifier;
    }

    @Override
    public String getName() {
        return I18n.get("tetra/schematic/repair.name");
    }

    @Override
    public String[] getSources() {
        return new String[] { "tetra" };
    }

    @Override
    public String getDescription(@Nullable ItemStack itemStack) {
        return (String) Optional.ofNullable(itemStack).flatMap(stack -> CastOptional.cast(itemStack.getItem(), IModularItem.class)).map(item -> {
            ItemModule[] cycle = item.getRepairCycle(itemStack);
            ItemModule currentTarget = (ItemModule) item.getRepairModule(itemStack).orElse(null);
            return currentTarget != null ? (String) Arrays.stream(cycle).map(module -> {
                String name = module.getName(itemStack);
                return currentTarget.equals(module) ? ChatFormatting.WHITE + name + ChatFormatting.RESET : name;
            }).collect(Collectors.joining(", ")) : null;
        }).map(cycle -> I18n.get("tetra/schematic/repair.description_details", cycle)).orElseGet(() -> I18n.get("tetra/schematic/repair.description"));
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return I18n.get("tetra/schematic/repair.slot1");
    }

    @Override
    public ItemStack[] getSlotPlaceholders(ItemStack itemStack, int index) {
        return (ItemStack[]) ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getRepairDefinitions(itemStack)).map(Collection::stream).orElse(Stream.empty())).map(definition -> definition.material).map(OutcomeMaterial::getApplicableItemStacks).flatMap(Arrays::stream).toArray(ItemStack[]::new);
    }

    @Override
    public int getRequiredQuantity(ItemStack itemStack, int index, ItemStack materialStack) {
        if (index == 0 && itemStack.getItem() instanceof IModularItem) {
            IModularItem item = (IModularItem) itemStack.getItem();
            return item.getRepairMaterialCount(itemStack, materialStack);
        } else {
            return 0;
        }
    }

    @Override
    public boolean acceptsMaterial(ItemStack itemStack, String itemSlot, int index, ItemStack materialStack) {
        return index == 0 ? ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getRepairDefinitions(itemStack)).map(Collection::stream).orElse(Stream.empty())).map(definition -> definition.material.getPredicate()).anyMatch(predicate -> predicate.matches(materialStack)) : false;
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return this.item.getClass().isInstance(itemStack.getItem());
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        return slot == null;
    }

    @Override
    public ItemStack applyUpgrade(ItemStack itemStack, ItemStack[] materials, boolean consumeMaterials, String slot, Player player) {
        ItemStack upgradedStack = itemStack.copy();
        IModularItem item = (IModularItem) upgradedStack.getItem();
        int quantity = this.getRequiredQuantity(itemStack, 0, materials[0]);
        item.repair(upgradedStack);
        if (consumeMaterials) {
            materials[0].shrink(quantity);
        }
        return upgradedStack;
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, String itemSlot, ItemStack[] materials) {
        return this.acceptsMaterial(itemStack, itemSlot, 0, materials[0]) && materials[0].getCount() >= this.getRequiredQuantity(itemStack, 0, materials[0]);
    }

    @Override
    public boolean isIntegrityViolation(Player player, ItemStack itemStack, ItemStack[] materials, String slot) {
        return false;
    }

    @Override
    public Map<ToolAction, Integer> getRequiredToolLevels(ItemStack targetStack, ItemStack[] materials) {
        return (Map<ToolAction, Integer>) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getRepairRequiredToolLevels(targetStack, materials[0])).orElseGet(Collections::emptyMap);
    }

    @Override
    public int getRequiredToolLevel(ItemStack targetStack, ItemStack[] materials, ToolAction toolAction) {
        return (Integer) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getRepairRequiredToolLevel(targetStack, materials[0], toolAction)).orElse(0);
    }

    @Override
    public int getExperienceCost(ItemStack targetStack, ItemStack[] materials, String slot) {
        return (Integer) CastOptional.cast(targetStack.getItem(), IModularItem.class).map(item -> item.getRepairRequiredExperience(targetStack, materials[0])).orElse(0);
    }

    @Override
    public SchematicType getType() {
        return SchematicType.other;
    }

    @Override
    public GlyphData getGlyph() {
        return this.glyph;
    }
}