package yesman.epicfight.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class EditItemListScreen extends Screen {

    private final Screen parentScreen;

    private final EditSwitchingItemScreen.RegisteredItemList targetList;

    private final EditSwitchingItemScreen.RegisteredItemList opponentList;

    private final List<Item> registered;

    private final List<Item> opponentRegistered;

    private EditItemListScreen.ButtonList itemButtonList;

    private EditItemListScreen.ButtonList selectedItemList;

    protected EditItemListScreen(Screen parentScreen, EditSwitchingItemScreen.RegisteredItemList targetList, EditSwitchingItemScreen.RegisteredItemList opponentList) {
        super(Component.empty());
        this.parentScreen = parentScreen;
        this.targetList = targetList;
        this.opponentList = opponentList;
        this.registered = targetList.toList();
        this.opponentRegistered = opponentList.toList();
    }

    @Override
    protected void init() {
        List<Item> itemList = Lists.newArrayList(ForgeRegistries.ITEMS.getValues());
        List<Item> selectedItemList = (List<Item>) (this.selectedItemList == null ? Lists.newArrayList() : this.selectedItemList.toList());
        this.itemButtonList = new EditItemListScreen.ButtonList(this.f_96541_, this.f_96543_ - 50, this.f_96544_, 24, this.f_96544_ - 120, itemList, EditItemListScreen.Type.LIST);
        this.selectedItemList = new EditItemListScreen.ButtonList(this.f_96541_, this.f_96543_ - 50, this.f_96544_, this.f_96544_ - 100, this.f_96544_ - 30, selectedItemList, EditItemListScreen.Type.SELECTED);
        this.itemButtonList.m_93507_(25);
        this.selectedItemList.m_93507_(25);
        this.m_142416_(this.itemButtonList);
        this.m_142416_(this.selectedItemList);
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> {
            for (Item item : this.selectedItemList.toList()) {
                this.targetList.addEntry(item);
                this.opponentList.removeIfPresent(item);
            }
            this.onClose();
        }).bounds(this.f_96543_ / 2 + 125, this.f_96544_ - 26, 60, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280039_(guiGraphics);
        this.itemButtonList.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.selectedItemList.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawString(this.f_96547_, Component.literal("Item List").withStyle(ChatFormatting.UNDERLINE), 28, 10, 16777215, false);
        guiGraphics.drawString(this.f_96547_, Component.literal("Seleted Items").withStyle(ChatFormatting.UNDERLINE), 28, this.f_96544_ - 114, 16777215, false);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }

    class ButtonList extends ObjectSelectionList<EditItemListScreen.ButtonList.ButtonEntry> {

        private final EditItemListScreen.Type type;

        private final int itemsInColumn;

        public ButtonList(Minecraft mcIn, int width, int height, int top, int bottom, List<Item> items, EditItemListScreen.Type type) {
            super(mcIn, width, height, top, bottom, 18);
            this.itemsInColumn = width / 17;
            this.type = type;
            this.m_7085_(new EditItemListScreen.ButtonList.ButtonEntry());
            for (Item item : items) {
                if (this.type != EditItemListScreen.Type.LIST || !EditItemListScreen.this.registered.contains(item)) {
                    this.addItem(item);
                }
            }
        }

        public boolean has(Item item) {
            for (EditItemListScreen.ButtonList.ButtonEntry entry : this.m_6702_()) {
                for (EditItemListScreen.ItemButton button : entry.buttonList) {
                    if (button.itemStack.getItem().equals(item)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void addItem(Item item) {
            EditItemListScreen.ButtonList.ButtonEntry entry = (EditItemListScreen.ButtonList.ButtonEntry) this.m_93500_(this.m_6702_().size() - 1);
            if (entry.buttonList.size() > this.itemsInColumn) {
                this.m_7085_(new EditItemListScreen.ButtonList.ButtonEntry());
                entry = (EditItemListScreen.ButtonList.ButtonEntry) this.m_93500_(this.m_6702_().size() - 1);
            }
            EditItemListScreen.IPressableExtended pressAction = null;
            if (this.type == EditItemListScreen.Type.LIST) {
                pressAction = (screen, button, x, y) -> {
                    if (!screen.selectedItemList.has(item)) {
                        screen.selectedItemList.addItem(button.itemStack.getItem());
                    }
                };
            } else if (this.type == EditItemListScreen.Type.SELECTED) {
                pressAction = (screen, button, x, y) -> screen.selectedItemList.removeAndRearrange(x, y);
            }
            entry.buttonList.add(EditItemListScreen.this.new ItemButton(0, 0, 16, 16, pressAction, EditItemListScreen.this, item.getDefaultInstance()));
        }

        public void removeAndRearrange(int x, int y) {
            ((EditItemListScreen.ButtonList.ButtonEntry) this.m_93500_(y)).buttonList.remove(x);
        }

        public List<Item> toList() {
            List<Item> result = Lists.newArrayList();
            for (EditItemListScreen.ButtonList.ButtonEntry entry : this.m_6702_()) {
                for (EditItemListScreen.ItemButton button : entry.buttonList) {
                    result.add(button.itemStack.getItem());
                }
            }
            return result;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            EditItemListScreen.ButtonList.ButtonEntry listener = this.getEntry(mouseX, mouseY);
            return listener != null ? listener.mouseClicked(mouseX, mouseY, button) : false;
        }

        public EditItemListScreen.ButtonList.ButtonEntry getEntry(double mouseX, double mouseY) {
            if (!(mouseX < (double) (this.f_93393_ + 2)) && !(mouseX > (double) (this.f_93392_ - 8)) && !(mouseY < (double) (this.f_93390_ + 2)) && !(mouseY > (double) (this.f_93391_ - 2))) {
                int column = (int) ((this.m_93517_() + mouseY - (double) this.f_93390_ - 4.0) / (double) this.f_93387_);
                return this.m_6702_().size() > column ? (EditItemListScreen.ButtonList.ButtonEntry) this.m_93500_(column) : null;
            } else {
                return null;
            }
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            this.m_7733_(guiGraphics);
            int i = this.getScrollbarPosition();
            int j = i + 6;
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            RenderSystem.setShader(GameRenderer::m_172820_);
            RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93391_, 0.0).uv((float) this.f_93393_ / 32.0F, (float) (this.f_93391_ + (int) this.m_93517_()) / 32.0F).color(32, 32, 32, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) this.f_93391_, 0.0).uv((float) this.f_93392_ / 32.0F, (float) (this.f_93391_ + (int) this.m_93517_()) / 32.0F).color(32, 32, 32, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) this.f_93390_, 0.0).uv((float) this.f_93392_ / 32.0F, (float) (this.f_93390_ + (int) this.m_93517_()) / 32.0F).color(32, 32, 32, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93390_, 0.0).uv((float) this.f_93393_ / 32.0F, (float) (this.f_93390_ + (int) this.m_93517_()) / 32.0F).color(32, 32, 32, 255).endVertex();
            tessellator.end();
            int j1 = this.getRowLeft();
            int k = this.f_93390_ + 4 - (int) this.m_93517_();
            this.m_7415_(guiGraphics, j1, k);
            this.m_239227_(guiGraphics, mouseX, mouseY, partialTicks);
            RenderSystem.setShader(GameRenderer::m_172820_);
            RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(519);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93390_, -100.0).uv(0.0F, (float) this.f_93390_ / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) (this.f_93393_ + this.f_93388_), (double) this.f_93390_, -100.0).uv((float) this.f_93388_ / 32.0F, (float) this.f_93390_ / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) (this.f_93393_ + this.f_93388_), (double) this.f_93390_ - 16.0, -100.0).uv((float) this.f_93388_ / 32.0F, (float) (this.f_93390_ - 16) / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93390_ - 16.0, -100.0).uv(0.0F, (float) (this.f_93390_ - 16) / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93389_, -100.0).uv(0.0F, (float) this.f_93389_ / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) (this.f_93393_ + this.f_93388_), (double) this.f_93389_, -100.0).uv((float) this.f_93388_ / 32.0F, (float) this.f_93389_ / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) (this.f_93393_ + this.f_93388_), (double) this.f_93391_, -100.0).uv((float) this.f_93388_ / 32.0F, (float) this.f_93391_ / 32.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93391_, -100.0).uv(0.0F, (float) this.f_93391_ / 32.0F).color(64, 64, 64, 255).endVertex();
            tessellator.end();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            RenderSystem.setShader(GameRenderer::m_172811_);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.m_5483_((double) this.f_93393_, (double) (this.f_93390_ + 4), 0.0).color(0, 0, 0, 0).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) (this.f_93390_ + 4), 0.0).color(0, 0, 0, 0).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) (this.f_93391_ - 4), 0.0).color(0, 0, 0, 0).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) (this.f_93391_ - 4), 0.0).color(0, 0, 0, 0).endVertex();
            tessellator.end();
            int k1 = this.m_93518_();
            if (k1 > 0) {
                RenderSystem.setShader(GameRenderer::m_172811_);
                int l1 = (int) ((float) ((this.f_93391_ - this.f_93390_) * (this.f_93391_ - this.f_93390_)) / (float) this.m_5775_());
                l1 = Mth.clamp(l1, 32, this.f_93391_ - this.f_93390_ - 8);
                int i2 = (int) this.m_93517_() * (this.f_93391_ - this.f_93390_ - l1) / k1 + this.f_93390_;
                if (i2 < this.f_93390_) {
                    i2 = this.f_93390_;
                }
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                bufferbuilder.m_5483_((double) i, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.m_5483_((double) j, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.m_5483_((double) j, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.m_5483_((double) i, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.m_5483_((double) i, (double) (i2 + l1), 0.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.m_5483_((double) j, (double) (i2 + l1), 0.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.m_5483_((double) j, (double) i2, 0.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.m_5483_((double) i, (double) i2, 0.0).color(128, 128, 128, 255).endVertex();
                bufferbuilder.m_5483_((double) i, (double) (i2 + l1 - 1), 0.0).color(192, 192, 192, 255).endVertex();
                bufferbuilder.m_5483_((double) (j - 1), (double) (i2 + l1 - 1), 0.0).color(192, 192, 192, 255).endVertex();
                bufferbuilder.m_5483_((double) (j - 1), (double) i2, 0.0).color(192, 192, 192, 255).endVertex();
                bufferbuilder.m_5483_((double) i, (double) i2, 0.0).color(192, 192, 192, 255).endVertex();
                tessellator.end();
            }
            this.m_7154_(guiGraphics, mouseX, mouseY);
            RenderSystem.disableBlend();
        }

        @Override
        public int getRowLeft() {
            return this.f_93393_ + 2;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93392_ - 6;
        }

        @OnlyIn(Dist.CLIENT)
        class ButtonEntry extends ObjectSelectionList.Entry<EditItemListScreen.ButtonList.ButtonEntry> {

            private final List<EditItemListScreen.ItemButton> buttonList = Lists.newArrayList();

            public ButtonEntry() {
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                int x = 0;
                for (EditItemListScreen.ItemButton button : this.buttonList) {
                    button.m_252865_(left + x);
                    button.m_253211_(top);
                    button.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
                    x += 16;
                }
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    int row = (int) ((mouseX - (double) ButtonList.this.f_93393_ - 2.0) / 16.0);
                    int column = (int) ((ButtonList.this.m_93517_() + mouseY - (double) ButtonList.this.f_93390_ - 4.0) / (double) ButtonList.this.f_93387_);
                    EditItemListScreen.ItemButton itembutton = this.getButton(row);
                    if (itembutton != null) {
                        itembutton = itembutton.m_5953_(mouseX, mouseY) ? itembutton : null;
                        if (itembutton != null) {
                            itembutton.pressedAction.onPress(EditItemListScreen.this, itembutton, row, column);
                            itembutton.m_7435_(Minecraft.getInstance().getSoundManager());
                        }
                    }
                }
                return false;
            }

            public EditItemListScreen.ItemButton getButton(int index) {
                return this.buttonList.size() > index ? (EditItemListScreen.ItemButton) this.buttonList.get(index) : null;
            }

            @Override
            public Component getNarration() {
                return Component.empty();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressableExtended {

        void onPress(EditItemListScreen var1, EditItemListScreen.ItemButton var2, int var3, int var4);
    }

    class ItemButton extends Button {

        private static final Set<Item> UNRENDERABLES = Sets.newHashSet();

        private final ItemStack itemStack;

        private final EditItemListScreen.IPressableExtended pressedAction;

        public ItemButton(int x, int y, int width, int height, EditItemListScreen.IPressableExtended pressedAction, EditItemListScreen screen, ItemStack itemStack) {
            super(x, y, width, height, Component.empty(), button -> {
            }, Button.DEFAULT_NARRATION);
            this.itemStack = itemStack;
            this.pressedAction = pressedAction;
        }

        @Override
        public Tooltip getTooltip() {
            Component displayName = this.itemStack.getHoverName();
            Component tooltipMessage = null;
            if (EditItemListScreen.this.opponentRegistered.contains(this.itemStack.getItem())) {
                tooltipMessage = Component.translatable("epicfight.gui.warn_already_registered", displayName.equals(Component.empty()) ? Component.literal(ForgeRegistries.ITEMS.getKey(this.itemStack.getItem()).toString()) : displayName);
            } else {
                tooltipMessage = (Component) (displayName.equals(Component.empty()) ? Component.literal(ForgeRegistries.ITEMS.getKey(this.itemStack.getItem()).toString()) : displayName);
            }
            return Tooltip.create(tooltipMessage);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (this.m_5953_((double) mouseX, (double) mouseY)) {
                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuilder();
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                bufferbuilder.m_5483_((double) this.m_252754_(), (double) this.m_252907_() + (double) this.f_93619_, 0.0).color(255, 255, 255, 255).endVertex();
                bufferbuilder.m_5483_((double) this.m_252754_() + (double) this.f_93618_, (double) this.m_252907_() + (double) this.f_93619_, 0.0).color(255, 255, 255, 255).endVertex();
                bufferbuilder.m_5483_((double) this.m_252754_() + (double) this.f_93618_, (double) this.m_252907_(), 0.0).color(255, 255, 255, 255).endVertex();
                bufferbuilder.m_5483_((double) this.m_252754_(), (double) this.m_252907_(), 0.0).color(255, 255, 255, 255).endVertex();
                tessellator.end();
                Component displayName = this.itemStack.getHoverName();
                Component tooltipMessage = null;
                if (EditItemListScreen.this.opponentRegistered.contains(this.itemStack.getItem())) {
                    tooltipMessage = Component.translatable("epicfight.gui.warn_already_registered", displayName.equals(Component.empty()) ? Component.literal(ForgeRegistries.ITEMS.getKey(this.itemStack.getItem()).toString()) : displayName);
                } else {
                    tooltipMessage = (Component) (displayName.equals(Component.empty()) ? Component.literal(ForgeRegistries.ITEMS.getKey(this.itemStack.getItem()).toString()) : displayName);
                }
                this.m_257544_(Tooltip.create(tooltipMessage));
            } else {
                this.m_257544_(null);
            }
            try {
                try {
                    if (!UNRENDERABLES.contains(this.itemStack.getItem())) {
                        guiGraphics.renderItem(this.itemStack, this.m_252754_(), this.m_252907_());
                    }
                } catch (Exception var9) {
                    UNRENDERABLES.add(this.itemStack.getItem());
                }
            } catch (Throwable var10) {
            }
        }
    }

    private static enum Type {

        LIST, SELECTED
    }
}