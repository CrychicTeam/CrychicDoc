package yesman.epicfight.client.gui.screen;

import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;
import yesman.epicfight.api.utils.ParseUtil;

@OnlyIn(Dist.CLIENT)
public class SelectFromRegistryScreen<T> extends Screen {

    private final SelectFromRegistryScreen<T>.RegistryList registryList;

    private final Screen parentScreen;

    private final Consumer<T> selectCallback;

    private final Consumer<T> onSelect;

    public SelectFromRegistryScreen(Screen parentScreen, IForgeRegistry<T> registry, Consumer<T> selectCallback) {
        this(parentScreen, registry, select -> {
        }, selectCallback);
    }

    public SelectFromRegistryScreen(Screen parentScreen, IForgeRegistry<T> registry, Consumer<T> onSelect, Consumer<T> selectCallback) {
        super(Component.translatable("gui.epicfight.select", ParseUtil.makeFirstLetterToUpper(registry.getRegistryName().getPath())));
        this.registryList = new SelectFromRegistryScreen.RegistryList(parentScreen.getMinecraft(), this.f_96543_, this.f_96544_, 36, this.f_96544_ - 16, 21, registry);
        this.parentScreen = parentScreen;
        this.selectCallback = selectCallback;
        this.onSelect = onSelect;
    }

    @Override
    protected void init() {
        this.registryList.m_93437_(this.f_96543_, this.f_96544_, 36, this.f_96544_ - 32);
        EditBox editBox = new EditBox(this.f_96541_.font, this.f_96543_ / 2, 12, this.f_96543_ / 2 - 12, 16, Component.literal("epicfight:"));
        editBox.setResponder(this.registryList::applyFilter);
        this.m_142416_(this.registryList);
        this.m_142416_(editBox);
        this.m_142416_(Button.builder(CommonComponents.GUI_OK, button -> {
            this.selectCallback.accept(((SelectFromRegistryScreen.RegistryList.RegistryEntry) this.registryList.m_93511_()).item);
            this.f_96541_.setScreen(this.parentScreen);
        }).pos(this.f_96543_ / 2 - 162, this.f_96544_ - 28).size(160, 21).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, button -> this.f_96541_.setScreen(this.parentScreen)).pos(this.f_96543_ / 2 + 2, this.f_96544_ - 28).size(160, 21).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280039_(guiGraphics);
        guiGraphics.drawString(this.f_96547_, this.f_96539_, 20, 16, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }

    @OnlyIn(Dist.CLIENT)
    class RegistryList extends ObjectSelectionList<SelectFromRegistryScreen<T>.RegistryList.RegistryEntry> {

        private final IForgeRegistry<T> registry;

        public RegistryList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight, IForgeRegistry<T> registry) {
            super(minecraft, width, height, y0, y1, itemHeight);
            this.registry = registry;
            for (java.util.Map.Entry<ResourceKey<T>, T> entry : registry.getEntries()) {
                this.m_7085_(new SelectFromRegistryScreen.RegistryList.RegistryEntry(entry.getValue(), ((ResourceKey) entry.getKey()).location().toString()));
            }
        }

        public void setSelected(@Nullable SelectFromRegistryScreen<T>.RegistryList.RegistryEntry selEntry) {
            SelectFromRegistryScreen.this.onSelect.accept(selEntry.item);
            super.m_6987_(selEntry);
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93392_ - 6;
        }

        public void applyFilter(String keyward) {
            this.m_93410_(0.0);
            this.m_6702_().clear();
            this.registry.getEntries().stream().filter(entry -> StringUtil.isNullOrEmpty(keyward) ? true : ((ResourceKey) entry.getKey()).toString().contains(keyward)).map(entry -> new SelectFromRegistryScreen.RegistryList.RegistryEntry(entry.getValue(), ((ResourceKey) entry.getKey()).location().toString())).forEach(x$0 -> this.m_7085_(x$0));
        }

        @OnlyIn(Dist.CLIENT)
        class RegistryEntry extends ObjectSelectionList.Entry<SelectFromRegistryScreen<T>.RegistryList.RegistryEntry> {

            private final T item;

            private final String name;

            public RegistryEntry(T item, String name) {
                this.item = item;
                this.name = name;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                guiGraphics.drawString(SelectFromRegistryScreen.this.f_96541_.font, this.name, left + 25, top + 5, 16777215, false);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select");
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    if (RegistryList.this.m_93511_() == this) {
                        SelectFromRegistryScreen.this.selectCallback.accept(this.item);
                        SelectFromRegistryScreen.this.f_96541_.setScreen(SelectFromRegistryScreen.this.parentScreen);
                        return true;
                    } else {
                        RegistryList.this.setSelected(this);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }
}