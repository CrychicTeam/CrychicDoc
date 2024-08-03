package dev.shadowsoffire.attributeslib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.shadowsoffire.attributeslib.ALConfig;
import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import dev.shadowsoffire.attributeslib.impl.BooleanAttribute;
import dev.shadowsoffire.placebo.PlaceboClient;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributesGui implements Renderable, GuiEventListener {

    public static final ResourceLocation TEXTURES = AttributesLib.loc("textures/gui/attributes_gui.png");

    public static final int ENTRY_HEIGHT = 22;

    public static final int MAX_ENTRIES = 6;

    public static final int WIDTH = 131;

    public static boolean wasOpen = false;

    protected static float scrollOffset = 0.0F;

    protected static boolean hideUnchanged = false;

    protected static boolean swappedFromCurios = false;

    protected final InventoryScreen parent;

    protected final Player player;

    protected final Font font;

    protected final ImageButton toggleBtn;

    protected final ImageButton recipeBookButton;

    protected final AttributesGui.HideUnchangedButton hideUnchangedBtn;

    protected int leftPos;

    protected int topPos;

    protected boolean scrolling;

    protected int startIndex;

    protected List<AttributeInstance> data;

    @Nullable
    protected AttributeInstance selected;

    protected boolean open;

    protected long lastRenderTick;

    private static DecimalFormat f = ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

    public AttributesGui(InventoryScreen parent) {
        this.font = Minecraft.getInstance().font;
        this.data = new ArrayList();
        this.selected = null;
        this.open = false;
        this.lastRenderTick = -1L;
        this.parent = parent;
        this.player = Minecraft.getInstance().player;
        this.refreshData();
        this.leftPos = parent.getGuiLeft() - 131;
        this.topPos = parent.getGuiTop();
        this.toggleBtn = new ImageButton(parent.getGuiLeft() + 63, parent.getGuiTop() + 10, 10, 10, 131, 0, 10, TEXTURES, 256, 256, btnx -> this.toggleVisibility(), Component.translatable("attributeslib.gui.show_attributes")) {

            @Override
            public void setFocused(boolean pFocused) {
            }
        };
        if (this.parent.m_6702_().size() > 1) {
            GuiEventListener btn = (GuiEventListener) this.parent.m_6702_().get(0);
            this.recipeBookButton = btn instanceof ImageButton imgBtn ? imgBtn : null;
        } else {
            this.recipeBookButton = null;
        }
        this.hideUnchangedBtn = new AttributesGui.HideUnchangedButton(0, 0);
    }

    public void refreshData() {
        this.data.clear();
        ForgeRegistries.ATTRIBUTES.getValues().stream().map(this.player::m_21051_).filter(Objects::nonNull).filter(ai -> !ALConfig.hiddenAttributes.contains(BuiltInRegistries.ATTRIBUTE.getKey(ai.getAttribute()))).filter(ai -> !hideUnchanged || ai.getBaseValue() != ai.getValue()).forEach(this.data::add);
        this.data.sort(this::compareAttrs);
        this.startIndex = (int) ((double) (scrollOffset * (float) this.getOffScreenRows()) + 0.5);
    }

    public void toggleVisibility() {
        this.open = !this.open;
        if (this.open && this.parent.getRecipeBookComponent().isVisible()) {
            this.parent.getRecipeBookComponent().toggleVisibility();
        }
        this.hideUnchangedBtn.f_93624_ = this.open;
        int newLeftPos;
        if (this.open && this.parent.f_96543_ >= 379) {
            newLeftPos = 177 + (this.parent.f_96543_ - this.parent.f_97726_ - 200) / 2;
        } else {
            newLeftPos = (this.parent.f_96543_ - this.parent.f_97726_) / 2;
        }
        this.parent.f_97735_ = newLeftPos;
        this.leftPos = this.parent.getGuiLeft() - 131;
        this.topPos = this.parent.getGuiTop();
        if (this.recipeBookButton != null) {
            this.recipeBookButton.m_264152_(this.parent.getGuiLeft() + 104, this.parent.f_96544_ / 2 - 22);
        }
        this.hideUnchangedBtn.m_264152_(this.leftPos + 7, this.topPos + 151);
    }

    protected int compareAttrs(AttributeInstance a1, AttributeInstance a2) {
        String name = I18n.get(a1.getAttribute().getDescriptionId());
        String name2 = I18n.get(a2.getAttribute().getDescriptionId());
        return name.compareTo(name2);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return !this.open ? false : this.isHovering(0, 0, 131, 166, pMouseX, pMouseY);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.toggleBtn.m_252865_(this.parent.getGuiLeft() + 63);
        this.toggleBtn.m_253211_(this.parent.getGuiTop() + 10);
        if (this.parent.getRecipeBookComponent().isVisible()) {
            this.open = false;
        }
        wasOpen = this.open;
        if (this.open) {
            if (this.lastRenderTick != PlaceboClient.ticks) {
                this.lastRenderTick = PlaceboClient.ticks;
                this.refreshData();
            }
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURES);
            int left = this.leftPos;
            int top = this.topPos;
            gfx.blit(TEXTURES, left, top, 0, 0, 131, 166);
            int scrollbarPos = (int) (117.0F * scrollOffset);
            gfx.blit(TEXTURES, left + 111, top + 16 + scrollbarPos, 244, this.isScrollBarActive() ? 0 : 15, 12, 15);
            for (int idx = this.startIndex; idx < this.startIndex + 6 && idx < this.data.size(); idx++) {
                this.renderEntry(gfx, (AttributeInstance) this.data.get(idx), this.leftPos + 8, this.topPos + 16 + 22 * (idx - this.startIndex), mouseX, mouseY);
            }
            this.renderTooltip(gfx, mouseX, mouseY);
            gfx.drawString(this.font, Component.translatable("attributeslib.gui.attributes"), this.leftPos + 8, this.topPos + 5, 4210752, false);
            gfx.drawString(this.font, Component.literal("Hide Unchanged"), this.leftPos + 20, this.topPos + 152, 4210752, false);
        }
    }

    protected void renderTooltip(GuiGraphics gfx, int mouseX, int mouseY) {
        AttributeInstance inst = this.getHoveredSlot(mouseX, mouseY);
        if (inst != null) {
            Attribute attr = inst.getAttribute();
            IFormattableAttribute fAttr = (IFormattableAttribute) attr;
            List<Component> list = new ArrayList();
            MutableComponent name = Component.translatable(attr.getDescriptionId()).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withUnderlined(true));
            if (AttributesLib.getTooltipFlag().isAdvanced()) {
                Style style = Style.EMPTY.withColor(ChatFormatting.GRAY).withUnderlined(false);
                name.append(Component.literal(" [" + BuiltInRegistries.ATTRIBUTE.getKey(attr) + "]").withStyle(style));
            }
            list.add(name);
            String key = attr.getDescriptionId() + ".desc";
            if (I18n.exists(key)) {
                Component txt = Component.translatable(key).withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC);
                list.add(txt);
            } else if (AttributesLib.getTooltipFlag().isAdvanced()) {
                Component txt = Component.literal(key).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                list.add(txt);
            }
            list.add(CommonComponents.EMPTY);
            ChatFormatting color = ChatFormatting.GRAY;
            if (attr instanceof RangedAttribute ra) {
                if (inst.getValue() > inst.getBaseValue()) {
                    color = ChatFormatting.YELLOW;
                } else if (inst.getValue() < inst.getBaseValue()) {
                    color = ChatFormatting.RED;
                }
            }
            MutableComponent valueComp = fAttr.toValueComponent(null, inst.getValue(), AttributesLib.getTooltipFlag());
            list.add(Component.translatable("attributeslib.gui.current", valueComp.withStyle(color)).withStyle(ChatFormatting.GRAY));
            MutableComponent baseVal = fAttr.toValueComponent(null, inst.getBaseValue(), AttributesLib.getTooltipFlag());
            baseVal = Component.translatable("attributeslib.gui.base", baseVal);
            if (attr instanceof RangedAttribute rax) {
                Component min = fAttr.toValueComponent(null, rax.getMinValue(), AttributesLib.getTooltipFlag());
                min = Component.translatable("attributeslib.gui.min", min);
                Component max = fAttr.toValueComponent(null, rax.getMaxValue(), AttributesLib.getTooltipFlag());
                max = Component.translatable("attributeslib.gui.max", max);
                list.add(Component.translatable("%s ┇ %s ┇ %s", baseVal, min, max).withStyle(ChatFormatting.GRAY));
            } else {
                list.add(baseVal.withStyle(ChatFormatting.GRAY));
            }
            List<ClientTooltipComponent> finalTooltip = new ArrayList(list.size());
            for (Component txt : list) {
                this.addComp(txt, finalTooltip);
            }
            if (inst.getModifiers().stream().anyMatch(modifx -> modifx.getAmount() != 0.0)) {
                this.addComp(CommonComponents.EMPTY, finalTooltip);
                this.addComp(Component.translatable("attributeslib.gui.modifiers").withStyle(ChatFormatting.GOLD), finalTooltip);
                Map<UUID, ModifierSource<?>> modifiersToSources = new HashMap();
                for (ModifierSourceType<?> type : ModifierSourceType.getTypes()) {
                    type.extract(this.player, (modifx, source) -> modifiersToSources.put(modifx.getId(), source));
                }
                Component[] opValues = new Component[3];
                for (AttributeModifier.Operation op : AttributeModifier.Operation.values()) {
                    List<AttributeModifier> modifiers = new ArrayList(inst.getModifiers(op));
                    double opValue = modifiers.stream().mapToDouble(AttributeModifier::m_22218_).reduce(op == AttributeModifier.Operation.MULTIPLY_TOTAL ? 1.0 : 0.0, (res, elem) -> op == AttributeModifier.Operation.MULTIPLY_TOTAL ? res * (1.0 + elem) : res + elem);
                    modifiers.sort(ModifierSourceType.compareBySource(modifiersToSources));
                    for (AttributeModifier modif : modifiers) {
                        if (modif.getAmount() != 0.0) {
                            Component comp = fAttr.toComponent(modif, AttributesLib.getTooltipFlag());
                            ModifierSource<?> src = (ModifierSource<?>) modifiersToSources.get(modif.getId());
                            finalTooltip.add(new AttributeModifierComponent(src, comp, this.font, this.leftPos - 16));
                        }
                    }
                    color = ChatFormatting.GRAY;
                    double threshold = op == AttributeModifier.Operation.MULTIPLY_TOTAL ? 1.0005 : 5.0E-4;
                    if (opValue > threshold) {
                        color = ChatFormatting.YELLOW;
                    } else if (opValue < -threshold) {
                        color = ChatFormatting.RED;
                    }
                    Component valueComp2 = fAttr.toValueComponent(op, opValue, AttributesLib.getTooltipFlag()).withStyle(color);
                    Component comp = Component.translatable("attributeslib.gui." + op.name().toLowerCase(Locale.ROOT), valueComp2).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                    opValues[op.ordinal()] = comp;
                }
                if (AttributesLib.getTooltipFlag().isAdvanced()) {
                    this.addComp(CommonComponents.EMPTY, finalTooltip);
                    for (Component comp : opValues) {
                        this.addComp(comp, finalTooltip);
                    }
                }
            }
            gfx.renderTooltipInternal(this.font, finalTooltip, this.leftPos - 16 - (Integer) finalTooltip.stream().map(c -> c.getWidth(this.font)).max(Integer::compare).get(), mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }

    private void addComp(Component comp, List<ClientTooltipComponent> finalTooltip) {
        if (comp == CommonComponents.EMPTY) {
            finalTooltip.add(ClientTooltipComponent.create(comp.getVisualOrderText()));
        } else {
            for (FormattedText fTxt : this.font.getSplitter().splitLines(comp, this.leftPos - 16, comp.getStyle())) {
                finalTooltip.add(ClientTooltipComponent.create(Language.getInstance().getVisualOrder(fTxt)));
            }
        }
    }

    private void renderEntry(GuiGraphics gfx, AttributeInstance inst, int x, int y, int mouseX, int mouseY) {
        boolean hover = this.getHoveredSlot(mouseX, mouseY) == inst;
        gfx.blit(TEXTURES, x, y, 142, hover ? 22 : 0, 100, 22);
        Component txt = Component.translatable(inst.getAttribute().getDescriptionId());
        int splitWidth = 60;
        List<FormattedCharSequence> lines;
        for (lines = this.font.split(txt, splitWidth); lines.size() > 2; lines = this.font.split(txt, splitWidth)) {
            splitWidth += 10;
        }
        PoseStack stack = gfx.pose();
        stack.pushPose();
        float scale = 1.0F;
        int maxWidth = (Integer) lines.stream().map(this.font::m_92724_).max(Integer::compareTo).get();
        if (maxWidth > 66) {
            scale = 66.0F / (float) maxWidth;
            stack.scale(scale, scale, 1.0F);
        }
        for (int i = 0; i < lines.size(); i++) {
            FormattedCharSequence line = (FormattedCharSequence) lines.get(i);
            float width = (float) this.font.width(line) * scale;
            float lineX = ((float) (x + 1) + (68.0F - width) / 2.0F) / scale;
            float lineY = (float) (y + (lines.size() == 1 ? 7 : 2) + i * 10) / scale;
            gfx.drawString(this.font, line, lineX, lineY, 4210752, false);
        }
        stack.popPose();
        stack.pushPose();
        IFormattableAttribute attr = (IFormattableAttribute) inst.getAttribute();
        MutableComponent value = attr.toValueComponent(null, inst.getValue(), TooltipFlag.Default.f_256752_);
        scale = 1.0F;
        if (this.font.width(value) > 27) {
            scale = 27.0F / (float) this.font.width(value);
            stack.scale(scale, scale, 1.0F);
        }
        int color = 16777215;
        if (attr instanceof RangedAttribute ra) {
            if (inst.getValue() > inst.getBaseValue()) {
                color = 5627221;
            } else if (inst.getValue() < inst.getBaseValue()) {
                color = 16736352;
            }
        } else if (attr instanceof BooleanAttribute ba && inst.getValue() > 0.0) {
            color = 5627221;
        }
        gfx.drawString(this.font, value, (int) (((float) (x + 72) + (27.0F - (float) this.font.width(value) * scale) / 2.0F) / scale), (int) ((float) (y + 7) / scale), color, true);
        stack.popPose();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.open && this.isScrollBarActive()) {
            this.scrolling = false;
            int left = this.leftPos + 111;
            int top = this.topPos + 15;
            if (pMouseX >= (double) left && pMouseX < (double) (left + 12) && pMouseY >= (double) top && pMouseY < (double) (top + 155)) {
                this.scrolling = true;
                int i = this.topPos + 15;
                int j = i + 138;
                scrollOffset = ((float) pMouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
                scrollOffset = Mth.clamp(scrollOffset, 0.0F, 1.0F);
                this.startIndex = (int) ((double) (scrollOffset * (float) this.getOffScreenRows()) + 0.5);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (!this.open) {
            return false;
        } else if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 15;
            int j = i + 138;
            scrollOffset = ((float) pMouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            scrollOffset = Mth.clamp(scrollOffset, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (scrollOffset * (float) this.getOffScreenRows()) + 0.5);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (!this.open) {
            return false;
        } else if (this.isScrollBarActive()) {
            int i = this.getOffScreenRows();
            scrollOffset = (float) ((double) scrollOffset - pDelta / (double) i);
            scrollOffset = Mth.clamp(scrollOffset, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (scrollOffset * (float) i) + 0.5);
            return true;
        } else {
            return false;
        }
    }

    private boolean isScrollBarActive() {
        return this.data.size() > 6;
    }

    protected int getOffScreenRows() {
        return Math.max(0, this.data.size() - 6);
    }

    @Nullable
    public AttributeInstance getHoveredSlot(int mouseX, int mouseY) {
        for (int i = 0; i < 6; i++) {
            if (this.startIndex + i < this.data.size() && this.isHovering(8, 14 + 22 * i, 100, 22, (double) mouseX, (double) mouseY)) {
                return (AttributeInstance) this.data.get(this.startIndex + i);
            }
        }
        return null;
    }

    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pMouseX -= (double) i;
        pMouseY -= (double) j;
        return pMouseX >= (double) (pX - 1) && pMouseX < (double) (pX + pWidth + 1) && pMouseY >= (double) (pY - 1) && pMouseY < (double) (pY + pHeight + 1);
    }

    public static String format(int n) {
        int log = (int) StrictMath.log10((double) n);
        if (log <= 4) {
            return String.valueOf(n);
        } else if (log == 5) {
            return f.format((double) n / 1000.0) + "K";
        } else {
            return log <= 8 ? f.format((double) n / 1000000.0) + "M" : f.format((double) n / 1.0E9) + "B";
        }
    }

    @Override
    public void setFocused(boolean pFocused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public class HideUnchangedButton extends ImageButton {

        public HideUnchangedButton(int pX, int pY) {
            super(pX, pY, 10, 10, 131, 20, 10, AttributesGui.TEXTURES, 256, 256, null, Component.literal("Hide Unchanged Attributes"));
            this.f_93624_ = false;
        }

        @Override
        public void onPress() {
            AttributesGui.hideUnchanged = !AttributesGui.hideUnchanged;
        }

        @Override
        public void renderWidget(GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
            int u = 131;
            int v = 20;
            int vOffset = AttributesGui.hideUnchanged ? 0 : 10;
            if (this.f_93622_) {
                vOffset += 20;
            }
            RenderSystem.enableDepthTest();
            PoseStack pose = gfx.pose();
            pose.pushPose();
            pose.translate(0.0F, 0.0F, 100.0F);
            gfx.blit(AttributesGui.TEXTURES, this.m_252754_(), this.m_252907_(), (float) u, (float) (v + vOffset), 10, 10, 256, 256);
            pose.popPose();
        }
    }
}