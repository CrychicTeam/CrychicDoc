package se.mickelus.tetra.blocks.workbench.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.GuiTextureOffset;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.blocks.salvage.InteractiveBlockOverlay;
import se.mickelus.tetra.blocks.workbench.WorkbenchContainer;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.HoneProgressGui;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchContainer> {

    private final WorkbenchTile tileEntity;

    private final WorkbenchContainer container;

    private final GuiInventoryInfo inventoryInfo;

    private final GuiElement defaultGui;

    private final GuiModuleList moduleList;

    private final WorkbenchStatsGui statGroup;

    private final GuiIntegrityBar integrityBar;

    private final HoneProgressGui honeBar;

    private final GuiActionList actionList;

    private final GuiSlotDetail slotDetail;

    private final ItemStack[] currentMaterials;

    private Player viewingPlayer;

    private String selectedSlot;

    private int previewMaterialSlot = -1;

    private ItemStack currentTarget = ItemStack.EMPTY;

    private ItemStack currentPreview = ItemStack.EMPTY;

    private UpgradeSchematic currentSchematic = null;

    private boolean hadItem = false;

    private boolean isDirty = false;

    public WorkbenchScreen(WorkbenchContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.f_97726_ = 320;
        this.f_97727_ = 240;
        this.tileEntity = container.getTileEntity();
        this.container = container;
        this.defaultGui = new GuiElement(0, 0, this.f_97726_, this.f_97727_);
        this.defaultGui.addChild(new GuiTextureOffset(136, 42, 48, 48, GuiTextures.workbench));
        this.defaultGui.addChild(new GuiTexture(72, 153, 179, 106, GuiTextures.playerInventory));
        this.moduleList = new GuiModuleList(164, 49, this::selectSlot, this::updateSlotHoverPreview);
        this.defaultGui.addChild(this.moduleList);
        this.statGroup = new WorkbenchStatsGui(60, 0);
        this.defaultGui.addChild(this.statGroup);
        this.integrityBar = new GuiIntegrityBar(0, 90);
        this.integrityBar.setAttachmentAnchor(GuiAttachment.topCenter);
        this.defaultGui.addChild(this.integrityBar);
        this.honeBar = new HoneProgressGui(0, 90);
        this.honeBar.setAttachmentAnchor(GuiAttachment.topCenter);
        this.honeBar.setVisible(false);
        this.defaultGui.addChild(this.honeBar);
        this.inventoryInfo = new GuiInventoryInfo(84, 164, Minecraft.getInstance().player);
        this.defaultGui.addChild(this.inventoryInfo);
        this.actionList = new GuiActionList(0, 120);
        this.actionList.setAttachmentAnchor(GuiAttachment.topCenter);
        this.actionList.setAttachmentPoint(GuiAttachment.middleCenter);
        this.defaultGui.addChild(this.actionList);
        this.slotDetail = new GuiSlotDetail(48, 102, schematic -> this.tileEntity.setCurrentSchematic(schematic, this.selectedSlot), () -> this.selectSlot(null), this::craftUpgrade, this::previewTweaks, this::applyTweaks);
        this.defaultGui.addChild(this.slotDetail);
        this.tileEntity.addChangeListener("gui.workbench", () -> this.isDirty = true);
        this.currentMaterials = new ItemStack[4];
        Arrays.fill(this.currentMaterials, ItemStack.EMPTY);
    }

    @Override
    public void init() {
        super.init();
        this.viewingPlayer = this.f_96541_.player;
        this.statGroup.realignBars();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.defaultGui.updateFocusState(this.f_97735_, this.f_97736_, mouseX, mouseY);
        this.defaultGui.draw(graphics, this.f_97735_, this.f_97736_, this.f_96543_, this.f_96544_, mouseX, mouseY, 1.0F);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        List<Component> tooltipLines = this.defaultGui.getTooltipLines();
        if (tooltipLines != null) {
            graphics.renderTooltip(this.f_96547_, tooltipLines, Optional.empty(), mouseX, Math.max(mouseY, 14));
        }
        this.updateMaterialHoverPreview();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        return this.defaultGui.onMouseClick((int) mouseX, (int) mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        this.defaultGui.onMouseRelease((int) mouseX, (int) mouseY, button);
        return true;
    }

    @Override
    public boolean charTyped(char typecChar, int keyCode) {
        this.slotDetail.keyTyped(typecChar);
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double distance) {
        return this.defaultGui.onMouseScroll(mouseX, mouseY, distance) ? true : super.m_6050_(mouseX, mouseY, distance);
    }

    private void selectSlot(String slotKey) {
        this.selectedSlot = slotKey;
        this.tileEntity.clearSchematic();
        this.moduleList.setFocus(this.selectedSlot);
        if (this.selectedSlot != null) {
            this.slotDetail.onTileEntityChange(this.viewingPlayer, this.tileEntity, this.tileEntity.getTargetItemStack(), this.selectedSlot, this.tileEntity.getCurrentSchematic());
        }
        this.slotDetail.setVisible(this.selectedSlot != null);
    }

    private void deselectSchematic() {
        this.tileEntity.clearSchematic();
    }

    private void craftUpgrade() {
        this.tileEntity.initiateCrafting(this.viewingPlayer);
    }

    private void previewTweaks(Map<String, Integer> tweakMap) {
        ItemStack previewStack = this.currentTarget.copy();
        CastOptional.cast(previewStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(previewStack, this.selectedSlot)).ifPresent(module -> tweakMap.forEach((tweakKey, step) -> {
            if (module.hasTweak(previewStack, tweakKey)) {
                module.setTweakStep(previewStack, tweakKey, step);
            }
            IModularItem.updateIdentifier(previewStack);
        }));
        this.statGroup.update(this.currentTarget, previewStack, null, null, this.viewingPlayer);
    }

    private void applyTweaks(Map<String, Integer> tweakMap) {
        this.tileEntity.applyTweaks(this.viewingPlayer, this.selectedSlot, tweakMap);
    }

    private void onTileEntityChange() {
        ItemStack newTarget = this.tileEntity.getTargetItemStack();
        ItemStack newPreview = ItemStack.EMPTY;
        UpgradeSchematic newSchematic = this.tileEntity.getCurrentSchematic();
        String currentSlot = this.tileEntity.getCurrentSlot();
        if (newTarget.getItem() instanceof IModularItem && newSchematic != null) {
            newPreview = this.buildPreviewStack(newSchematic, newTarget, currentSlot, this.tileEntity.getMaterials());
        }
        boolean targetItemChanged = !ItemStack.matches(this.currentTarget, newTarget);
        boolean previewChanged = !ItemStack.matches(this.currentPreview, newPreview);
        boolean schematicChanged = !Objects.equals(this.currentSchematic, newSchematic);
        boolean materialsChanged = this.diffMaterials(this.tileEntity.getMaterials());
        this.currentPreview = newPreview;
        this.currentSchematic = newSchematic;
        if (targetItemChanged) {
            ItemStack.matches(this.currentTarget, newTarget);
            this.currentTarget = newTarget.copy();
            this.selectedSlot = null;
        }
        boolean slotChanged = !Objects.equals(this.selectedSlot, currentSlot);
        if (!this.currentTarget.isEmpty() && currentSlot != null) {
            this.selectedSlot = currentSlot;
        }
        this.container.updateSlots();
        if (slotChanged || targetItemChanged) {
            this.actionList.updateActions(this.currentTarget, this.tileEntity.getAvailableActions(this.viewingPlayer), this.viewingPlayer, action -> this.tileEntity.performAction(this.viewingPlayer, action.getKey()), this.tileEntity);
            InteractiveBlockOverlay.markDirty();
        }
        if (targetItemChanged || previewChanged || schematicChanged || slotChanged || materialsChanged) {
            this.updateItemDisplay(this.currentTarget, this.currentPreview);
            if (this.currentTarget.getItem() instanceof IModularItem) {
                this.slotDetail.onTileEntityChange(this.viewingPlayer, this.tileEntity, this.currentTarget, this.selectedSlot, this.currentSchematic);
            }
        }
        this.inventoryInfo.update(this.currentSchematic, currentSlot, this.currentTarget);
        if (!this.currentTarget.isEmpty()) {
            if (!this.hadItem) {
                this.hadItem = true;
                if (targetItemChanged && currentSlot == null) {
                    this.itemShowAnimation();
                }
            }
        } else {
            this.hadItem = false;
        }
        if (!this.currentTarget.isEmpty()) {
            if (this.currentSchematic == null && this.selectedSlot == null) {
                this.actionList.setVisible(true);
                this.slotDetail.setVisible(false);
            } else if (this.currentTarget.getItem() instanceof IModularItem) {
                this.actionList.setVisible(false);
                this.slotDetail.setVisible(this.selectedSlot != null);
            }
        } else {
            this.actionList.setVisible(false);
            this.slotDetail.setVisible(false);
        }
    }

    private boolean diffMaterials(ItemStack[] newMaterials) {
        boolean isDiff = false;
        for (int i = 0; i < newMaterials.length; i++) {
            if (!ItemStack.matches(newMaterials[i], this.currentMaterials[i])) {
                isDiff = true;
                break;
            }
        }
        for (int ix = 0; ix < newMaterials.length; ix++) {
            this.currentMaterials[ix] = newMaterials[ix].copy();
        }
        return isDiff;
    }

    @Override
    protected void containerTick() {
        this.inventoryInfo.update(this.tileEntity.getCurrentSchematic(), this.tileEntity.getCurrentSlot(), this.currentTarget);
        Level world = this.tileEntity.m_58904_();
        if (this.isDirty) {
            this.onTileEntityChange();
            this.isDirty = false;
        } else if (world != null && world.getGameTime() % 20L == 0L) {
            BlockPos pos = this.tileEntity.m_58899_();
            Map<ToolAction, Integer> availableTools = PropertyHelper.getCombinedToolLevels(this.viewingPlayer, world, pos, world.getBlockState(pos));
            if (this.tileEntity.getCurrentSchematic() != null && this.slotDetail.isVisible()) {
                this.slotDetail.update(this.viewingPlayer, this.tileEntity, availableTools);
            }
            if (this.actionList.isVisible()) {
                this.actionList.updateTools(availableTools);
            }
        }
    }

    private void updateItemDisplay(ItemStack itemStack, ItemStack previewStack) {
        this.moduleList.update(itemStack, previewStack, this.selectedSlot);
        this.statGroup.update(itemStack, previewStack, null, null, this.viewingPlayer);
        this.slotDetail.updatePreview(this.currentSchematic, this.selectedSlot, itemStack, previewStack);
        this.integrityBar.setItemStack(itemStack, previewStack);
        this.honeBar.update(itemStack, this.tileEntity.isTargetPlaceholder());
        this.honeBar.setX(Math.max(this.integrityBar.getWidth() / 2 + 8, 35));
    }

    private void itemShowAnimation() {
        this.moduleList.showAnimation();
        this.statGroup.showAnimation();
        this.integrityBar.showAnimation();
        this.honeBar.showAnimation();
        this.actionList.showAnimation();
    }

    private void updateSlotHoverPreview(String slot, String improvement) {
        if (this.tileEntity.getCurrentSlot() == null) {
            ItemStack itemStack = this.tileEntity.getTargetItemStack();
            this.statGroup.update(itemStack, ItemStack.EMPTY, slot, improvement, this.viewingPlayer);
        }
    }

    private void updateMaterialHoverPreview() {
        int newPreviewMaterialSlot = -1;
        Slot hoveredSlot = this.getSlotUnderMouse();
        UpgradeSchematic currentSchematic = this.tileEntity.getCurrentSchematic();
        ItemStack targetStack = this.tileEntity.getTargetItemStack();
        if (currentSchematic != null && hoveredSlot != null && hoveredSlot.hasItem()) {
            newPreviewMaterialSlot = hoveredSlot.getSlotIndex();
        }
        if (newPreviewMaterialSlot != this.previewMaterialSlot && targetStack.getItem() instanceof IModularItem) {
            ItemStack[] materials = this.tileEntity.getMaterials();
            if (newPreviewMaterialSlot != -1 && Arrays.stream(materials).allMatch(ItemStack::m_41619_)) {
                ItemStack previewStack = this.buildPreviewStack(currentSchematic, targetStack, this.selectedSlot, new ItemStack[] { hoveredSlot.getItem() });
                this.updateItemDisplay(targetStack, previewStack);
            } else {
                ItemStack previewStack = ItemStack.EMPTY;
                if (currentSchematic != null) {
                    previewStack = this.buildPreviewStack(currentSchematic, targetStack, this.selectedSlot, materials);
                }
                this.updateItemDisplay(targetStack, previewStack);
            }
            this.previewMaterialSlot = newPreviewMaterialSlot;
        }
    }

    private ItemStack buildPreviewStack(UpgradeSchematic schematic, ItemStack targetStack, String slot, ItemStack[] materials) {
        if (!schematic.isMaterialsValid(targetStack, slot, materials)) {
            return ItemStack.EMPTY;
        } else {
            ItemStack result = schematic.applyUpgrade(targetStack, materials, false, slot, null);
            boolean willReplace = schematic.willReplace(targetStack, materials, slot);
            if (willReplace) {
                TetraEnchantmentHelper.removeEnchantments(result, slot);
            }
            Map<ToolAction, Integer> tools = schematic.getRequiredToolLevels(targetStack, materials);
            for (Entry<ToolAction, Integer> entry : tools.entrySet()) {
                result = WorkbenchTile.consumeCraftingToolEffects(result, slot, willReplace, (ToolAction) entry.getKey(), (Integer) entry.getValue(), this.viewingPlayer, this.tileEntity.m_58904_(), this.tileEntity.m_58899_(), this.tileEntity.m_58900_(), false);
            }
            result = WorkbenchTile.applyCraftingBonusEffects(result, slot, willReplace, this.viewingPlayer, materials, materials, tools, schematic, this.tileEntity.m_58904_(), this.tileEntity.m_58899_(), this.tileEntity.m_58900_(), false);
            IModularItem.updateIdentifier(result);
            return result;
        }
    }
}