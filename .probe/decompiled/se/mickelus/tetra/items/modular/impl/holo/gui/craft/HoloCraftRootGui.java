package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.LinkedList;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.HoloRootBaseGui;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloCraftRootGui extends HoloRootBaseGui {

    public static final char backBinding = 'q';

    private final HoloBreadcrumbsGui breadcrumbs;

    private final HoloItemsGui itemsView;

    private final HoloSchematicListGui schematicsView;

    private final HoloSchematicGui schematicView;

    private final HoloMaterialListGui materialsView;

    private int depth = 0;

    private IModularItem item;

    private ItemStack itemStack;

    private String slot;

    private UpgradeSchematic schematic;

    private OutcomePreview openVariant;

    private boolean showingMaterials = false;

    public HoloCraftRootGui(int x, int y) {
        super(x, y);
        this.breadcrumbs = new HoloBreadcrumbsGui(0, 0, this.width, this::onBreadcrumbClick);
        this.addChild(this.breadcrumbs);
        this.itemsView = new HoloItemsGui(0, 70, this.width, this.height, this::onItemSelect, this::onSlotSelect, this::onMaterialsSelect);
        this.addChild(this.itemsView);
        this.schematicsView = new HoloSchematicListGui(0, 20, this.width, this.height, this::onSchematicSelect);
        this.schematicsView.setVisible(false);
        this.addChild(this.schematicsView);
        this.schematicView = new HoloSchematicGui(0, 20, this.width, this.height, this::onVariantSelect);
        this.schematicView.setVisible(false);
        this.addChild(this.schematicView);
        this.materialsView = new HoloMaterialListGui(0, 20, this.width, this.height);
        this.materialsView.setVisible(false);
        this.addChild(this.materialsView);
    }

    @Override
    public boolean onCharType(char character, int modifiers) {
        if (super.onCharType(character, modifiers)) {
            return true;
        } else if (character == 'q' && this.depth > 0) {
            this.onBreadcrumbClick(this.depth - 1);
            return true;
        } else {
            return false;
        }
    }

    private void onBreadcrumbClick(int depth) {
        switch(depth) {
            case 0:
                this.onItemSelect(null, null);
                break;
            case 1:
                if (!this.showingMaterials) {
                    this.onItemSelect(this.item, this.itemStack);
                }
                break;
            case 2:
                this.onSlotSelect(this.slot);
                break;
            case 3:
                this.onSchematicSelect(this.schematic);
        }
        this.depth = depth;
    }

    private void onMaterialsSelect() {
        this.item = null;
        this.itemsView.setVisible(false);
        this.slot = null;
        this.schematicsView.setVisible(false);
        this.schematic = null;
        this.schematicView.setVisible(false);
        this.showingMaterials = true;
        this.materialsView.setVisible(true);
        this.updateBreadcrumb();
    }

    private void onItemSelect(IModularItem item, ItemStack itemStack) {
        this.item = item;
        this.itemStack = itemStack;
        this.itemsView.changeItem(item);
        this.itemsView.setVisible(true);
        this.slot = null;
        this.schematicsView.setVisible(false);
        this.openVariant = null;
        this.schematic = null;
        this.schematicView.setVisible(false);
        if (this.depth > 1) {
            this.itemsView.animateBack();
        }
        this.showingMaterials = false;
        this.materialsView.setVisible(false);
        this.updateBreadcrumb();
    }

    private void onSlotSelect(String slot) {
        this.slot = slot;
        this.schematicsView.update(this.item, slot);
        this.schematicsView.setVisible(true);
        this.itemsView.setVisible(false);
        this.openVariant = null;
        this.schematic = null;
        this.schematicView.setVisible(false);
        this.showingMaterials = false;
        this.materialsView.setVisible(false);
        this.updateBreadcrumb();
    }

    private void onSchematicSelect(UpgradeSchematic schematic) {
        this.schematic = schematic;
        this.schematicView.update(this.item, this.slot, schematic);
        this.schematicView.setVisible(true);
        this.openVariant = null;
        this.schematicView.openVariant(null);
        this.schematicsView.setVisible(false);
        this.itemsView.setVisible(false);
        this.showingMaterials = false;
        this.materialsView.setVisible(false);
        this.updateBreadcrumb();
    }

    private void onVariantSelect(OutcomePreview variant) {
        this.openVariant = variant;
        this.schematicView.openVariant(this.openVariant);
        this.schematicView.setVisible(true);
        this.schematicsView.setVisible(false);
        this.itemsView.setVisible(false);
        this.showingMaterials = false;
        this.materialsView.setVisible(false);
        this.updateBreadcrumb();
    }

    public void updateState(IModularItem item, ItemStack itemStack, @Nullable String slot, @Nullable UpgradeSchematic schematic) {
        this.item = item;
        this.itemStack = itemStack;
        if (slot == null && schematic == null) {
            this.itemsView.changeItem(item);
        }
        this.slot = slot;
        this.onSchematicSelect(schematic);
        this.breadcrumbs.animateOpen(true);
    }

    private void updateBreadcrumb() {
        LinkedList<String> result = new LinkedList();
        if (this.item != null) {
            result.add(I18n.get("tetra.holo.craft.breadcrumb.root"));
            result.add(I18n.get("tetra.holo.craft." + this.item.getItem().toString()));
            if (this.slot != null) {
                result.add(this.getSlotName());
            }
            if (this.schematic != null) {
                result.add(this.schematic.getName());
            }
            if (this.openVariant != null) {
                result.add(this.openVariant.variantName);
            }
        } else if (this.showingMaterials) {
            result.add(I18n.get("tetra.holo.craft.breadcrumb.root"));
            result.add(I18n.get("tetra.holo.craft.breadcrumb.materials"));
        }
        this.depth = result.size() - 1;
        this.breadcrumbs.setVisible(result.size() > 1);
        this.breadcrumbs.setItems((String[]) result.toArray(new String[0]));
    }

    private String getSlotName() {
        if (this.item != null) {
            String[] majorKeys = this.item.getMajorModuleKeys(this.itemStack);
            for (int i = 0; i < majorKeys.length; i++) {
                if (majorKeys[i].equals(this.slot)) {
                    return this.item.getMajorModuleNames(this.itemStack)[i];
                }
            }
            String[] minorKeys = this.item.getMinorModuleKeys(this.itemStack);
            for (int ix = 0; ix < minorKeys.length; ix++) {
                if (minorKeys[ix].equals(this.slot)) {
                    return this.item.getMinorModuleNames(this.itemStack)[ix];
                }
            }
        }
        return this.slot;
    }

    @Override
    public void animateOpen() {
        switch(this.depth) {
            case 0:
                this.itemsView.animateOpenAll();
                break;
            case 1:
                if (this.showingMaterials) {
                    this.materialsView.animateOpen();
                } else {
                    this.itemsView.animateOpen();
                }
                break;
            case 2:
                this.schematicsView.animateOpen();
                break;
            case 3:
                this.schematicView.animateOpen();
        }
        this.breadcrumbs.animateOpen(this.depth > 1);
    }

    @Override
    public void onReload() {
        if (this.schematic != null) {
            this.schematicView.setVisible(false);
            UpgradeSchematic newSchematic = SchematicRegistry.getSchematic(this.schematic.getKey());
            this.onSchematicSelect(newSchematic);
        } else if (this.slot != null) {
            this.onSlotSelect(this.slot);
        }
        this.materialsView.reload();
    }
}