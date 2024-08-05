package se.mickelus.tetra.blocks.workbench.gui;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTextSmall;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.WorkbenchContainer;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.gui.GuiMagicUsage;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class GuiSchematicDetail extends GuiElement {

    private final GuiElement glyph;

    private final GuiString title;

    private final GuiSources sources;

    private final GuiTextSmall description;

    private final CraftButtonGui craftButton;

    private final SchemaSlotGui[] slots;

    private final GuiElement emptySlotsIndicator;

    private final GuiElement hasSlotsIndicator;

    private final GuiMagicUsage magicCapacity;

    private final ToolRequirementListGui toolRequirementList;

    private final GuiExperience experienceIndicator;

    private final AnimationChain flash;

    private UpgradeSchematic schematic;

    private List<Component> descriptionTooltip;

    public GuiSchematicDetail(int x, int y, Runnable backListener, Runnable craftListener) {
        super(x, y, 224, 67);
        this.addChild(new GuiTexture(-4, -4, 239, 69, 0, 187, GuiTextures.workbench));
        this.addChild(new GuiButton(-4, this.height - 2, 40, 8, "< " + I18n.get("tetra.workbench.schematic_detail.back"), backListener));
        this.glyph = new GuiElement(3, 3, 16, 16);
        this.addChild(this.glyph);
        this.title = new GuiString(19, 6, 100, "");
        this.addChild(this.title);
        this.sources = new GuiSources(19, 15, 81);
        this.addChild(this.sources);
        this.description = new GuiTextSmall(5, 22, 125, "");
        this.addChild(this.description);
        this.slots = new SchemaSlotGui[3];
        for (int i = 0; i < 3; i++) {
            this.slots[i] = new SchemaSlotGui(125, 5, 82, i);
            this.addChild(this.slots[i]);
        }
        this.emptySlotsIndicator = new GuiTexture(146, 6, 64, 16, 48, 32, GuiTextures.workbench);
        this.addChild(this.emptySlotsIndicator);
        this.hasSlotsIndicator = new GuiElement(0, 0, 0, 0);
        this.hasSlotsIndicator.addChild(new GuiTexture(132, 3, 4, 22, 240, 192, GuiTextures.workbench));
        this.hasSlotsIndicator.addChild(new GuiTexture(220, 3, 5, 22, 244, 192, GuiTextures.workbench));
        this.addChild(this.hasSlotsIndicator);
        this.magicCapacity = new GuiMagicUsage(138, 30, 80);
        this.addChild(this.magicCapacity);
        this.experienceIndicator = new GuiExperience(205, 41, "tetra.workbench.schematic_detail.experience");
        this.addChild(this.experienceIndicator);
        this.craftButton = new CraftButtonGui(155, 41, craftListener);
        this.addChild(this.craftButton);
        this.toolRequirementList = new ToolRequirementListGui(143, 40);
        this.addChild(this.toolRequirementList);
        GuiTexture flashOverlay = new GuiTexture(-4, -4, 239, 69, 0, 187, GuiTextures.workbench);
        flashOverlay.setOpacity(0.0F);
        flashOverlay.setColor(0);
        this.addChild(flashOverlay);
        this.flash = new AnimationChain(new KeyframeAnimation(60, flashOverlay).applyTo(new Applier.Opacity(0.3F)), new KeyframeAnimation(120, flashOverlay).applyTo(new Applier.Opacity(0.0F)));
    }

    public void update(Level level, BlockPos pos, WorkbenchTile blockEntity, UpgradeSchematic schematic, ItemStack itemStack, String slot, ItemStack[] materials, Map<ToolAction, Integer> availableTools, Player player) {
        this.schematic = schematic;
        this.title.setString(schematic.getName());
        this.title.setColor(schematic.getRarity().tint);
        this.sources.update(schematic);
        String descriptionString = schematic.getDescription(itemStack);
        this.description.setString(ChatFormatting.GRAY + descriptionString.replace(ChatFormatting.RESET.toString(), ChatFormatting.RESET.toString() + ChatFormatting.GRAY));
        this.descriptionTooltip = ImmutableList.of(Component.literal(descriptionString));
        this.glyph.clearChildren();
        GlyphData glyphData = schematic.getGlyph();
        GuiTexture border = null;
        GuiTexture glyphTexture;
        if (schematic.getType() == SchematicType.major) {
            border = new GuiTexture(0, 2, 16, 9, 52, 3, GuiTextures.workbench);
            glyphTexture = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.minor) {
            border = new GuiTexture(2, 1, 11, 11, 68, 0, GuiTextures.workbench);
            glyphTexture = new GuiTexture(4, 3, 8, 8, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.improvement) {
            border = new GuiTexture(0, 2, 16, 9, 52, 3, GuiTextures.workbench);
            glyphTexture = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else {
            glyphTexture = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        }
        if (border != null) {
            border.setOpacity(0.3F);
            border.setColor(schematic.getRarity().tint);
            this.glyph.addChild(border);
        }
        glyphTexture.setColor(schematic.getRarity().tint);
        this.glyph.addChild(glyphTexture);
        if (schematic.getType() == SchematicType.improvement) {
            this.glyph.addChild(new GuiTexture(7, 7, 7, 7, 68, 16, GuiTextures.workbench).setColor(8355711));
        }
        int numMaterialSlots = schematic.getNumMaterialSlots();
        for (int i = 0; i < 3; i++) {
            this.slots[i].update(schematic, player, level, pos, blockEntity, itemStack, slot, materials);
            this.slots[i].setX(136 + WorkbenchContainer.getSlotOffsetY(i, numMaterialSlots));
        }
        this.toolRequirementList.update(schematic, itemStack, slot, materials, availableTools);
        this.emptySlotsIndicator.setVisible(numMaterialSlots == 0);
        this.hasSlotsIndicator.setVisible(numMaterialSlots != 0);
        int xpCost = schematic.getExperienceCost(itemStack, materials, slot);
        this.experienceIndicator.setVisible(xpCost > 0);
        if (xpCost > 0) {
            if (!player.isCreative()) {
                this.experienceIndicator.update(xpCost, xpCost <= player.experienceLevel);
            } else {
                this.experienceIndicator.update(xpCost, true);
            }
        }
        this.flash();
    }

    public void updateMagicCapacity(UpgradeSchematic schematic, String slot, ItemStack itemStack, ItemStack previewStack) {
        if (slot == null || (schematic == null || !SchematicType.major.equals(schematic.getType()) || !this.magicCapacity.providesCapacity(itemStack, previewStack, slot)) && !this.magicCapacity.hasChanged(itemStack, previewStack, slot)) {
            this.magicCapacity.setVisible(false);
        } else {
            this.magicCapacity.update(itemStack, previewStack, slot);
            this.magicCapacity.setVisible(true);
        }
    }

    public void updateAvailableTools(Map<ToolAction, Integer> availableTools) {
        this.toolRequirementList.updateAvailableTools(availableTools);
    }

    public void updateButton(UpgradeSchematic schematic, Player player, ItemStack itemStack, ItemStack previewStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        this.craftButton.update(schematic, player, itemStack, previewStack, materials, slot, availableTools);
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.description.hasFocus() ? this.descriptionTooltip : super.getTooltipLines();
    }

    public void flash() {
        this.flash.stop();
        this.flash.start();
    }
}