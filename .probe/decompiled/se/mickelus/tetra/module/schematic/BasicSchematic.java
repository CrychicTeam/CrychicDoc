package se.mickelus.tetra.module.schematic;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;

public abstract class BasicSchematic extends BaseSchematic {

    private static final String nameSuffix = ".name";

    private static final String descriptionSuffix = ".description";

    private static final String slotSuffix = ".slot1";

    protected String key;

    protected ItemModule module;

    protected Item item;

    public BasicSchematic(String key, ItemModule module, Item item) {
        this.key = key;
        this.module = module;
        this.item = item;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getName() {
        return I18n.get(this.key + ".name");
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return I18n.get(this.key + ".description");
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(ItemStack itemStack, int index) {
        return I18n.get(this.key + ".slot1");
    }

    @Override
    public boolean isRelevant(ItemStack itemStack) {
        return this.item.equals(itemStack.getItem());
    }

    @Override
    public boolean isApplicableForSlot(String slot, ItemStack targetStack) {
        return this.module.getSlot().equals(slot);
    }

    protected ItemModule removePreviousModule(ItemStack itemStack) {
        IModularItem item = (IModularItem) itemStack.getItem();
        ItemModule previousModule = item.getModuleFromSlot(itemStack, this.module.getSlot());
        if (previousModule != null) {
            previousModule.removeModule(itemStack);
        }
        return previousModule;
    }

    @Override
    public SchematicType getType() {
        return this.module instanceof ItemModuleMajor ? SchematicType.major : SchematicType.minor;
    }

    @Override
    public GlyphData getGlyph() {
        return this.module.getDefaultData().glyph;
    }
}