package yesman.epicfight.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.provider.ItemCapabilityProvider;

@OnlyIn(Dist.CLIENT)
public class EditSwitchingItemScreen extends Screen {

    private EditSwitchingItemScreen.RegisteredItemList battleAutoSwitchItems;

    private EditSwitchingItemScreen.RegisteredItemList miningAutoSwitchItems;

    protected final Screen parentScreen;

    private Runnable deferredTooltip;

    public EditSwitchingItemScreen(Screen parentScreen) {
        super(Component.translatable("epicfight.gui.configuration.autoswitching"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        if (this.battleAutoSwitchItems == null) {
            this.battleAutoSwitchItems = new EditSwitchingItemScreen.RegisteredItemList(200, this.f_96544_, Component.translatable("epicfight.gui.to_battle_mode"), EpicFightMod.CLIENT_CONFIGS.battleAutoSwitchItems);
        } else {
            this.battleAutoSwitchItems.resize(200, this.f_96544_);
        }
        if (this.miningAutoSwitchItems == null) {
            this.miningAutoSwitchItems = new EditSwitchingItemScreen.RegisteredItemList(200, this.f_96544_, Component.translatable("epicfight.gui.to_mining_mode"), EpicFightMod.CLIENT_CONFIGS.miningAutoSwitchItems);
        } else {
            this.miningAutoSwitchItems.resize(200, this.f_96544_);
        }
        this.battleAutoSwitchItems.m_93507_(this.f_96543_ / 2 - 204);
        this.miningAutoSwitchItems.m_93507_(this.f_96543_ / 2 + 4);
        this.m_142416_(this.battleAutoSwitchItems);
        this.m_142416_(this.miningAutoSwitchItems);
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> {
            EpicFightMod.CLIENT_CONFIGS.battleAutoSwitchItems.clear();
            EpicFightMod.CLIENT_CONFIGS.miningAutoSwitchItems.clear();
            this.battleAutoSwitchItems.toList().forEach(item -> EpicFightMod.CLIENT_CONFIGS.battleAutoSwitchItems.add(item));
            this.miningAutoSwitchItems.toList().forEach(item -> EpicFightMod.CLIENT_CONFIGS.miningAutoSwitchItems.add(item));
            EpicFightMod.CLIENT_CONFIGS.save();
            this.onClose();
        }).bounds(this.f_96543_ / 2 - 80, this.f_96544_ - 28, 160, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280039_(guiGraphics);
        this.battleAutoSwitchItems.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        this.miningAutoSwitchItems.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 16, 16777215);
        if (this.deferredTooltip != null) {
            this.deferredTooltip.run();
            this.deferredTooltip = null;
        }
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }

    @OnlyIn(Dist.CLIENT)
    class RegisteredItemList extends ObjectSelectionList<EditSwitchingItemScreen.RegisteredItemList.ItemEntry> {

        private final Component title;

        public RegisteredItemList(int width, int height, Component title, List<Item> saved) {
            super(EditSwitchingItemScreen.this.f_96541_, width, height, 32, height - 50, 22);
            this.title = title;
            this.m_93473_(true, 13);
            if (this.m_93511_() != null) {
                this.m_93494_((EditSwitchingItemScreen.RegisteredItemList.ItemEntry) this.m_93511_());
            }
            this.m_7085_(new EditSwitchingItemScreen.RegisteredItemList.ButtonInEntry());
            for (Item item : saved) {
                this.m_7085_(new EditSwitchingItemScreen.RegisteredItemList.ItemEntry(item.getDefaultInstance()));
            }
        }

        public void resize(int width, int height) {
            this.f_93388_ = width;
            this.f_93389_ = height;
            this.f_93390_ = 32;
            this.f_93391_ = height - 50;
            this.f_93393_ = 0;
            this.f_93392_ = width;
        }

        @Override
        protected void renderHeader(GuiGraphics guiGraphics, int x, int y) {
            Component component = Component.literal("").append(this.title).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD);
            guiGraphics.drawString(this.f_93386_.font, component, x + this.f_93388_ / 2 - this.f_93386_.font.width(component) / 2, Math.min(this.f_93390_ + 3, y), 16777215, false);
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93392_ - 6;
        }

        protected void addEntry(Item item) {
            this.m_6702_().add(new EditSwitchingItemScreen.RegisteredItemList.ItemEntry(item.getDefaultInstance()));
        }

        protected void removeIfPresent(Item item) {
            this.m_6702_().remove(new EditSwitchingItemScreen.RegisteredItemList.ItemEntry(item.getDefaultInstance()));
        }

        protected List<Item> toList() {
            List<Item> list = Lists.newArrayList();
            for (EditSwitchingItemScreen.RegisteredItemList.ItemEntry entry : this.m_6702_()) {
                if (!entry.itemStack.isEmpty()) {
                    list.add(entry.itemStack.getItem());
                }
            }
            return list;
        }

        @OnlyIn(Dist.CLIENT)
        class ButtonInEntry extends EditSwitchingItemScreen.RegisteredItemList.ItemEntry {

            private final Button addItemButton = Button.builder(Component.literal("+"), button -> {
                EditSwitchingItemScreen.RegisteredItemList thisList = RegisteredItemList.this == EditSwitchingItemScreen.this.battleAutoSwitchItems ? EditSwitchingItemScreen.this.battleAutoSwitchItems : EditSwitchingItemScreen.this.miningAutoSwitchItems;
                EditSwitchingItemScreen.RegisteredItemList opponentList = RegisteredItemList.this == EditSwitchingItemScreen.this.battleAutoSwitchItems ? EditSwitchingItemScreen.this.miningAutoSwitchItems : EditSwitchingItemScreen.this.battleAutoSwitchItems;
                RegisteredItemList.this.f_93386_.setScreen(new EditItemListScreen(EditSwitchingItemScreen.this, thisList, opponentList));
            }).bounds(0, 0, 20, 20).build();

            private final Button removeAllButton = Button.builder(Component.translatable("epicfight.gui.delete_all"), button -> {
                RegisteredItemList.this.m_93516_();
                RegisteredItemList.this.m_7085_(this);
            }).bounds(0, 0, 60, 20).build();

            private final Button automaticRegisterButton = Button.builder(Component.translatable("epicfight.gui.auto_add"), button -> {
                boolean isBattleTab = RegisteredItemList.this == EditSwitchingItemScreen.this.battleAutoSwitchItems;
                if (isBattleTab) {
                    for (Item item : ForgeRegistries.ITEMS.getValues()) {
                        CapabilityItem itemCap = ItemCapabilityProvider.get(item);
                        if (itemCap != null && itemCap instanceof WeaponCapability) {
                            EditSwitchingItemScreen.RegisteredItemList.ItemEntry itemEntry = RegisteredItemList.this.new ItemEntry(item.getDefaultInstance());
                            if (!EditSwitchingItemScreen.this.battleAutoSwitchItems.m_6702_().contains(itemEntry)) {
                                EditSwitchingItemScreen.this.battleAutoSwitchItems.m_7085_(itemEntry);
                            }
                        }
                    }
                } else {
                    for (Item itemx : ForgeRegistries.ITEMS.getValues()) {
                        EditSwitchingItemScreen.RegisteredItemList.ItemEntry itemEntry = RegisteredItemList.this.new ItemEntry(itemx.getDefaultInstance());
                        if (!EditSwitchingItemScreen.this.battleAutoSwitchItems.m_6702_().contains(itemEntry) && !EditSwitchingItemScreen.this.miningAutoSwitchItems.m_6702_().contains(itemEntry)) {
                            EditSwitchingItemScreen.this.miningAutoSwitchItems.m_7085_(itemEntry);
                        }
                    }
                }
            }).bounds(0, 0, 60, 20).tooltip(Tooltip.create(RegisteredItemList.this == EditSwitchingItemScreen.this.battleAutoSwitchItems ? Component.translatable("epicfight.gui.tooltip_battle") : Component.translatable("epicfight.gui.tooltip_mining"))).build();

            public ButtonInEntry() {
                super(ItemStack.EMPTY);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                this.addItemButton.m_252865_(left + 25);
                this.addItemButton.m_253211_(top - 2);
                this.addItemButton.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
                this.removeAllButton.m_252865_(left + 47);
                this.removeAllButton.m_253211_(top - 2);
                this.removeAllButton.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
                this.automaticRegisterButton.m_252865_(left + 109);
                this.automaticRegisterButton.m_253211_(top - 2);
                this.automaticRegisterButton.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    if (this.addItemButton.m_5953_(mouseX, mouseY)) {
                        this.addItemButton.m_7435_(EditSwitchingItemScreen.this.f_96541_.getSoundManager());
                        this.addItemButton.onPress();
                    }
                    if (this.removeAllButton.m_5953_(mouseX, mouseY)) {
                        this.removeAllButton.m_7435_(EditSwitchingItemScreen.this.f_96541_.getSoundManager());
                        this.removeAllButton.onPress();
                    }
                    if (this.automaticRegisterButton.m_5953_(mouseX, mouseY)) {
                        this.automaticRegisterButton.m_7435_(EditSwitchingItemScreen.this.f_96541_.getSoundManager());
                        this.automaticRegisterButton.onPress();
                    }
                }
                return false;
            }
        }

        @OnlyIn(Dist.CLIENT)
        class ItemEntry extends ObjectSelectionList.Entry<EditSwitchingItemScreen.RegisteredItemList.ItemEntry> {

            private static final Set<Item> UNRENDERABLES = Sets.newHashSet();

            private final ItemStack itemStack;

            public ItemEntry(ItemStack itemStack) {
                this.itemStack = itemStack;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                try {
                    if (!UNRENDERABLES.contains(this.itemStack.getItem())) {
                        guiGraphics.renderItem(this.itemStack, left + 4, top + 1);
                    }
                } catch (Exception var12) {
                    UNRENDERABLES.add(this.itemStack.getItem());
                }
                Component component = this.itemStack.getHoverName();
                guiGraphics.drawString(RegisteredItemList.this.f_93386_.font, component, left + 30, top + 5, 16777215, false);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    if (RegisteredItemList.this.m_93511_() != null && ((EditSwitchingItemScreen.RegisteredItemList.ItemEntry) RegisteredItemList.this.m_93511_()).equals(this)) {
                        RegisteredItemList.this.m_93502_(this);
                        return false;
                    } else {
                        RegisteredItemList.this.m_6987_(this);
                        return true;
                    }
                } else {
                    return false;
                }
            }

            public boolean equals(Object obj) {
                return obj instanceof EditSwitchingItemScreen.RegisteredItemList.ItemEntry && !(this instanceof EditSwitchingItemScreen.RegisteredItemList.ButtonInEntry) ? this.itemStack.getItem().equals(((EditSwitchingItemScreen.RegisteredItemList.ItemEntry) obj).itemStack.getItem()) : super.equals(obj);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.itemStack.getHoverName());
            }
        }
    }
}