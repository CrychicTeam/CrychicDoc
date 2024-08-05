package com.mna.gui.constructs;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.entities.attributes.AttributeInit;
import com.mna.entities.constructs.animated.Construct;
import com.mna.gui.GuiTextures;
import com.mna.gui.HUDOverlayRenderer;
import com.mna.gui.widgets.entity.ConstructDiagnosticsDisplay;
import com.mna.network.ClientMessageDispatcher;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class GuiConstructDiagnostics extends Screen {

    private Construct eac;

    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    private List<Component> tooltip = null;

    private int curMouseX = 0;

    private int curMouseY = 0;

    private int hoveredSlot = -1;

    public GuiConstructDiagnostics(Construct eac) {
        super(Component.literal(""));
        this.eac = eac;
        eac.setRequestingDiagnostics(true);
        this.tooltip = new ArrayList();
    }

    @Override
    protected void init() {
        super.init();
        this.m_169394_(new ConstructDiagnosticsDisplay(this.f_96541_.screen.width / 2 - 128, this.f_96541_.screen.height / 2 - 128, this.eac));
        this.m_142416_(new ImageButton(this.f_96541_.screen.width / 2 + 1, this.f_96541_.screen.height / 2 - 128, 14, 14, 24, 216, 14, GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, 256, 256, button -> {
            if (HUDOverlayRenderer.instance.getConstructDisplay().isValid()) {
                HUDOverlayRenderer.instance.setTrackedConstruct(null);
            } else {
                HUDOverlayRenderer.instance.setTrackedConstruct(this.eac);
            }
        }));
    }

    @Override
    public void removed() {
        super.removed();
        if (HUDOverlayRenderer.instance.getTrackedConstruct() != this.eac) {
            this.eac.setRequestingDiagnostics(false);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.tooltip.clear();
        this.hoveredSlot = -1;
        this.curMouseX = mouseX;
        this.curMouseY = mouseY;
        this.m_280273_(pGuiGraphics);
        int cX = this.f_96541_.screen.width / 2 - 128;
        int cY = this.f_96541_.screen.height / 2 - 128;
        pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, cX, cY, 0, 0, 256, 215);
        int yPos = 0;
        int xPos = 0;
        int step = 0;
        if (this.eac.m_6084_()) {
            IConstructConstruction data = this.eac.getConstructData();
            xPos = cX + 151;
            yPos = cY + 118;
            int var19 = 13;
            for (ConstructCapability cap : data.getEnabledCapabilities()) {
                this.renderCapabilityIcon(pGuiGraphics, xPos, yPos, cap);
                xPos += var19;
                if (xPos > cX + 242) {
                    xPos = cX + 151;
                    yPos += var19;
                }
            }
            Fluid stored = this.eac.getStoredFluid();
            MutableComponent storedFluid = Component.translatable("");
            if (stored != null) {
                storedFluid = Component.translatable(ForgeRegistries.FLUIDS.getKey(this.eac.getStoredFluid()).toString());
            }
            this.renderStatText(pGuiGraphics, cX + 165, cY + 6, Component.literal(String.format("%.1f", this.eac.m_21223_())), Component.translatable("gui.mna.construct.health"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 19, Component.literal(String.format("%d", (int) this.eac.m_21051_(AttributeInit.INTELLIGENCE.get()).getValue())), Component.translatable("gui.mna.construct.intelligence"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 32, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(AttributeInit.PERCEPTION_DISTANCE.get()).getValue())), Component.translatable("gui.mna.construct.perception"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 45, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(Attributes.ATTACK_SPEED).getValue())), Component.translatable("gui.mna.construct.attack_speed"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 58, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(Attributes.ATTACK_DAMAGE).getValue())), Component.translatable("gui.mna.construct.damage"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 71, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(AttributeInit.RANGED_DAMAGE.get()).getValue())), Component.translatable("gui.mna.construct.ranged_damage"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 84, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(Attributes.ATTACK_KNOCKBACK).getValue())), Component.translatable("gui.mna.construct.knockback"));
            this.renderStatText(pGuiGraphics, cX + 165, cY + 97, Component.literal(String.format("%.1f", this.eac.getBuoyancy())), Component.translatable("gui.mna.construct.buoyancy"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 6, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(Attributes.MOVEMENT_SPEED).getValue() * 10.0F)), Component.translatable("gui.mna.construct.move_speed"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 19, Component.literal(String.format("%d", (int) this.eac.m_21051_(Attributes.ARMOR).getValue())), Component.translatable("gui.mna.construct.armor"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 32, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(Attributes.ARMOR_TOUGHNESS).getValue())), Component.translatable("gui.mna.construct.toughness"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 45, Component.literal(String.format("%.1f", data.calculateExplosionResistance())), Component.translatable("gui.mna.construct.explosion_resist"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 58, Component.literal(String.format("%.1f", (float) this.eac.m_21051_(Attributes.KNOCKBACK_RESISTANCE).getValue())), Component.translatable("gui.mna.construct.knockback_resist"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 71, Component.literal(String.format("%d", this.eac.getSlots())), Component.translatable("gui.mna.construct.inventory_slots"));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 84, Component.literal(String.format("%d", this.eac.getStoredFluidAmount())), Component.translatable("gui.mna.construct.stored_fluid_amount", storedFluid.getString(), this.eac.getTankCapacity(0)));
            this.renderStatText(pGuiGraphics, cX + 217, cY + 97, Component.literal(String.format("%d", (int) this.eac.getMana())), Component.translatable("gui.mna.construct.mana", (int) this.eac.getMaxMana()));
            this.renderItem(pGuiGraphics, cX + 152, cY + 168, 0, this.eac.m_21205_());
            this.renderItem(pGuiGraphics, cX + 234, cY + 168, 1, this.eac.m_21206_());
            this.renderItem(pGuiGraphics, cX + 152, cY + 186, 2, this.eac.getConstructData().getHat());
            this.renderItem(pGuiGraphics, cX + 234, cY + 186, 3, this.eac.getConstructData().getBanner());
            if (this.eac.getSlots() > 0) {
                var19 = 18;
                for (int i = 0; i < 9 && i < this.eac.getSlots(); i++) {
                    this.renderItem(pGuiGraphics, cX + 175 + var19 * (i % 3), cY + 150 + (int) ((double) var19 * Math.floor((double) (i / 3))), i + 4, this.eac.getStackInSlot(i));
                }
                MutableComponent text = Component.literal(String.format("%d", this.eac.getSlotLimit(0)));
                int iconWidth = 14;
                int width = this.f_96547_.width(text) + iconWidth;
                xPos = cX + 201 - width / 2;
                pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, xPos, cY + 206, (Integer) ConstructCapability.ITEM_STORAGE.getIconCoords().getFirst(), (Integer) ConstructCapability.ITEM_STORAGE.getIconCoords().getSecond(), 11, 11);
                this.renderStatText(pGuiGraphics, xPos + iconWidth, cY + 206, text, Component.literal("Storage capacity of each slot in the construct's backpack."));
            }
        }
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.tooltip != null) {
            List<FormattedCharSequence> split = new ArrayList();
            this.tooltip.forEach(line -> split.addAll(this.f_96547_.split(line, 128)));
            pGuiGraphics.renderTooltip(this.f_96547_, split, mouseX, mouseY);
        }
    }

    private void renderStatText(GuiGraphics pGuiGraphics, int x, int y, Component c, Component tooltip) {
        int width = this.f_96547_.width(c);
        int iconPadding = 16;
        if (this.isCursorWithin(x - iconPadding, y, width + iconPadding, 9)) {
            this.tooltip.add(tooltip);
        }
        pGuiGraphics.drawString(this.f_96547_, c, x, y, WHITE, false);
    }

    private void renderCapabilityIcon(GuiGraphics pGuiGraphics, int x, int y, ConstructCapability cap) {
        int size = 11;
        if (this.isCursorWithin(x, y, size, size)) {
            this.tooltip.add(Component.translatable("gui.mna.construct." + cap.name().toLowerCase()));
        }
        pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, x, y, (Integer) cap.getIconCoords().getFirst(), (Integer) cap.getIconCoords().getSecond(), size, size);
    }

    private void renderItem(GuiGraphics pGuiGraphics, int x, int y, int index, ItemStack stack) {
        if (!stack.isEmpty()) {
            int size = 18;
            if (this.isCursorWithin(x, y, size, size)) {
                this.tooltip.addAll(stack.getTooltipLines(this.f_96541_.player, TooltipFlag.Default.f_256752_));
                this.hoveredSlot = index;
            }
            pGuiGraphics.renderItem(stack, x, y);
            pGuiGraphics.renderItemDecorations(this.f_96547_, stack, x, y);
        }
    }

    private boolean isCursorWithin(int x, int y, int width, int height) {
        return this.curMouseX > x && this.curMouseX < x + width && this.curMouseY > y && this.curMouseY < y + height;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hoveredSlot > -1) {
            ClientMessageDispatcher.sendAnimatedConstructDropItemMessage(this.eac, this.hoveredSlot);
        }
        return super.m_6375_(mouseX, mouseY, button);
    }
}