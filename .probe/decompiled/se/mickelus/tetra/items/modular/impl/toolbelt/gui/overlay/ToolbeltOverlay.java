package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import com.mojang.blaze3d.platform.Window;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.client.keymap.TetraKeyMappings;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.EquipToolbeltItemPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.OpenToolbeltItemPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.StoreToolbeltItemPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PotionsInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuiverInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltSlotType;

@ParametersAreNonnullByDefault
public class ToolbeltOverlay extends GuiRoot implements IGuiOverlay {

    private final QuickslotGroupGui quickslotGroup;

    private final PotionGroupGui potionGroup;

    private final QuiverGroupGui quiverGroup;

    private final HolosphereGroupGui holosphereGroup;

    private long openTime = -1L;

    private boolean isActive = false;

    public ToolbeltOverlay(Minecraft mc) {
        super(mc);
        this.quickslotGroup = new QuickslotGroupGui(52, 0);
        this.quickslotGroup.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.quickslotGroup);
        this.potionGroup = new PotionGroupGui(0, 40);
        this.potionGroup.setAttachmentPoint(GuiAttachment.topCenter);
        this.potionGroup.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.potionGroup);
        this.quiverGroup = new QuiverGroupGui(-40, -40);
        this.quiverGroup.setAttachmentPoint(GuiAttachment.bottomRight);
        this.quiverGroup.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.quiverGroup);
        this.holosphereGroup = new HolosphereGroupGui(0, -40);
        this.holosphereGroup.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.holosphereGroup);
    }

    public void setInventories(ItemStack itemStack) {
    }

    public void toggleActive(boolean active) {
        this.isActive = active;
        this.quickslotGroup.setVisible(active);
        this.potionGroup.setVisible(active);
        this.quiverGroup.setVisible(active);
        this.holosphereGroup.setVisible(active);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (TetraKeyMappings.restockBinding.isDown()) {
            this.storeToolbeltItem();
        } else if (TetraKeyMappings.openBinding.consumeClick()) {
            this.openToolbelt();
        } else if (TetraKeyMappings.accessBinding.isDown() && this.mc.isWindowActive() && !this.isActive) {
            this.showView();
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (!TetraKeyMappings.accessBinding.isDown() && this.isActive) {
            this.hideView();
        }
        this.draw(graphics);
    }

    @Override
    public void draw(GuiGraphics graphics) {
        super.draw(graphics);
        if (this.isVisible()) {
            Window window = this.mc.getWindow();
            int mouseX = (int) (this.mc.mouseHandler.xpos() * (double) this.width / (double) window.getScreenWidth());
            int mouseY = (int) (this.mc.mouseHandler.ypos() * (double) this.height / (double) window.getScreenHeight());
            this.updateFocusState(0, 0, mouseX, mouseY);
        }
    }

    private void showView() {
        boolean canOpen = this.updateGuiData();
        if (canOpen) {
            this.toggleActive(true);
            this.mc.mouseHandler.releaseMouse();
            this.openTime = System.currentTimeMillis();
        }
    }

    private void hideView() {
        this.toggleActive(false);
        this.mc.mouseHandler.grabMouse();
        int focusIndex = this.getFocusIndex();
        if (focusIndex != -1) {
            this.equipToolbeltItem(this.getFocusType(), focusIndex, this.getFocusHand());
        } else if (System.currentTimeMillis() - this.openTime < 500L) {
            this.quickEquip();
        }
        this.holosphereGroup.performActions();
    }

    private boolean openToolbelt() {
        ItemStack itemStack = ToolbeltHelper.findToolbelt(this.mc.player);
        if (!itemStack.isEmpty()) {
            TetraMod.packetHandler.sendToServer(new OpenToolbeltItemPacket());
        }
        return !itemStack.isEmpty();
    }

    private boolean updateGuiData() {
        boolean canShow = false;
        ItemStack toolbeltStack = ToolbeltHelper.findToolbelt(this.mc.player);
        if (!toolbeltStack.isEmpty()) {
            this.quickslotGroup.setInventory(new QuickslotInventory(toolbeltStack));
            this.potionGroup.setInventory(new PotionsInventory(toolbeltStack));
            this.quiverGroup.setInventory(new QuiverInventory(toolbeltStack));
            canShow = true;
        } else {
            this.quickslotGroup.clear();
            this.potionGroup.clear();
            this.quiverGroup.clear();
        }
        ItemStack holosphereStack = ModularHolosphereItem.findHolosphere(this.mc.player);
        if (!holosphereStack.isEmpty()) {
            this.holosphereGroup.update(holosphereStack);
            canShow = true;
        } else {
            this.holosphereGroup.clear();
        }
        return canShow;
    }

    private void equipToolbeltItem(ToolbeltSlotType slotType, int toolbeltItemIndex, InteractionHand hand) {
        if (toolbeltItemIndex > -1) {
            EquipToolbeltItemPacket packet = new EquipToolbeltItemPacket(slotType, toolbeltItemIndex, hand);
            TetraMod.packetHandler.sendToServer(packet);
            ToolbeltHelper.equipItemFromToolbelt(this.mc.player, slotType, toolbeltItemIndex, hand);
        }
    }

    private void storeToolbeltItem() {
        boolean storeItemSuccess = ToolbeltHelper.storeItemInToolbelt(this.mc.player);
        StoreToolbeltItemPacket packet = new StoreToolbeltItemPacket();
        TetraMod.packetHandler.sendToServer(packet);
        if (!storeItemSuccess) {
            this.mc.player.displayClientMessage(Component.translatable("tetra.toolbelt.full"), true);
        }
    }

    private void quickEquip() {
        if (this.mc.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult raytrace = (BlockHitResult) this.mc.hitResult;
            BlockState blockState = this.mc.level.m_8055_(raytrace.getBlockPos());
            int index = ToolbeltHelper.getQuickAccessSlotIndex(this.mc.player, this.mc.hitResult, blockState);
            if (index > -1) {
                this.equipToolbeltItem(ToolbeltSlotType.quickslot, index, InteractionHand.MAIN_HAND);
            }
        }
    }

    public ToolbeltSlotType getFocusType() {
        if (this.quickslotGroup.getFocus() != -1) {
            return ToolbeltSlotType.quickslot;
        } else if (this.potionGroup.getFocus() != -1) {
            return ToolbeltSlotType.potion;
        } else {
            return this.quiverGroup.getFocus() != -1 ? ToolbeltSlotType.quiver : ToolbeltSlotType.quickslot;
        }
    }

    public int getFocusIndex() {
        int quickslotFocus = this.quickslotGroup.getFocus();
        if (quickslotFocus != -1) {
            return quickslotFocus;
        } else {
            int potionFocus = this.potionGroup.getFocus();
            if (potionFocus != -1) {
                return potionFocus;
            } else {
                int quiverFocus = this.quiverGroup.getFocus();
                return quiverFocus != -1 ? quiverFocus : -1;
            }
        }
    }

    public InteractionHand getFocusHand() {
        InteractionHand quickslotHand = this.quickslotGroup.getHand();
        if (quickslotHand != null) {
            return quickslotHand;
        } else {
            InteractionHand potionHand = this.potionGroup.getHand();
            if (potionHand != null) {
                return potionHand;
            } else {
                InteractionHand quiverHand = this.quiverGroup.getHand();
                return quiverHand != null ? quiverHand : InteractionHand.OFF_HAND;
            }
        }
    }
}