package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.screen.list.IListType;
import com.mrcrayfish.configured.client.screen.list.ListTypes;
import com.mrcrayfish.configured.client.screen.widget.ConfiguredButton;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import com.mrcrayfish.configured.util.ConfigHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class EditListScreen<T> extends Screen implements IBackgroundTexture, IEditing {

    private final Screen parent;

    private final IModConfig config;

    private final List<EditListScreen.StringHolder> initialValues = new ArrayList();

    private final List<EditListScreen.StringHolder> values = new ArrayList();

    private final ResourceLocation background;

    private final IConfigValue<List<T>> holder;

    private final IListType<T> listType;

    private EditListScreen<T>.ObjectList list;

    public EditListScreen(Screen parent, IModConfig config, Component titleIn, IConfigValue<List<T>> holder, ResourceLocation background) {
        super(titleIn);
        this.parent = parent;
        this.config = config;
        this.holder = holder;
        this.listType = ListTypes.getType(holder);
        this.initialValues.addAll(holder.get().stream().map(o -> new EditListScreen.StringHolder((String) this.listType.getStringParser().apply(o))).toList());
        this.values.addAll(this.initialValues);
        this.background = background;
    }

    @Override
    protected void init() {
        this.list = new EditListScreen.ObjectList();
        this.list.m_93488_(!ConfigHelper.isPlayingGame());
        this.m_7787_(this.list);
        if (!this.config.isReadOnly()) {
            this.m_142416_(new IconButton(this.f_96543_ / 2 - 140, this.f_96544_ - 29, 0, 44, 90, Component.translatable("configured.gui.apply"), button -> {
                List<T> newValues = (List<T>) this.values.stream().map(EditListScreen.StringHolder::getValue).map(s -> this.listType.getValueParser().apply(s)).collect(Collectors.toList());
                this.holder.set(newValues);
                this.f_96541_.setScreen(this.parent);
            }));
            this.m_142416_(new IconButton(this.f_96543_ / 2 - 45, this.f_96544_ - 29, 22, 33, 90, Component.translatable("configured.gui.add_value"), button -> this.f_96541_.setScreen(new EditStringScreen(this, this.config, this.background, Component.translatable("configured.gui.edit_value"), "", s -> {
                T value = (T) this.listType.getValueParser().apply(s);
                if (value != null) {
                    return this.holder.isValid(Collections.singletonList(value)) ? Pair.of(true, CommonComponents.EMPTY) : Pair.of(false, this.holder.getValidationHint());
                } else {
                    return Pair.of(false, this.listType.getHint());
                }
            }, s -> {
                EditListScreen.StringHolder holder = new EditListScreen.StringHolder(s);
                this.values.add(holder);
                this.list.addEntry(new EditListScreen.StringEntry(this.list, holder));
            }))));
        }
        boolean readOnly = this.config.isReadOnly();
        int cancelWidth = readOnly ? 150 : 90;
        int cancelOffset = readOnly ? -75 : 50;
        Component cancelLabel = (Component) (readOnly ? Component.translatable("configured.gui.close") : CommonComponents.GUI_CANCEL);
        this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 + cancelOffset, this.f_96544_ - 29, cancelWidth, 20, cancelLabel, button -> {
            if (this.isModified()) {
                ConfirmationScreen confirmScreen = new ActiveConfirmationScreen(this, this.config, Component.translatable("configured.gui.list_changed"), ConfirmationScreen.Icon.WARNING, result -> {
                    if (!result) {
                        return true;
                    } else {
                        this.f_96541_.setScreen(this.parent);
                        return false;
                    }
                });
                this.f_96541_.setScreen(confirmScreen);
            } else {
                this.f_96541_.setScreen(this.parent);
            }
        }));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        this.list.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 14, 16777215);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public IModConfig getActiveConfig() {
        return this.config;
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.background;
    }

    public boolean isModified() {
        if (this.initialValues.size() != this.values.size()) {
            return true;
        } else {
            for (int i = 0; i < this.initialValues.size(); i++) {
                String s1 = ((EditListScreen.StringHolder) this.initialValues.get(i)).getValue();
                String s2 = ((EditListScreen.StringHolder) this.values.get(i)).getValue();
                if (!s1.equals(s2)) {
                    return true;
                }
            }
            return false;
        }
    }

    public class ObjectList extends ContainerObjectSelectionList<EditListScreen<T>.StringEntry> implements IBackgroundTexture {

        public ObjectList() {
            super(EditListScreen.this.f_96541_, EditListScreen.this.f_96543_, EditListScreen.this.f_96544_, 36, EditListScreen.this.f_96544_ - 36, 24);
            EditListScreen.this.values.forEach(value -> this.addEntry(EditListScreen.this.new StringEntry(this, value)));
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93388_ / 2 + 144;
        }

        @Override
        public int getRowWidth() {
            return 260;
        }

        public int addEntry(EditListScreen<T>.StringEntry entry) {
            return super.m_7085_(entry);
        }

        public boolean removeEntry(EditListScreen<T>.StringEntry entry) {
            return super.m_93502_(entry);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.m_88315_(graphics, mouseX, mouseY, partialTicks);
            this.m_6702_().forEach(entry -> entry.children().forEach(o -> {
                if (o instanceof Button) {
                }
            }));
        }

        @Override
        public ResourceLocation getBackgroundTexture() {
            return EditListScreen.this.background;
        }
    }

    public class StringEntry extends ContainerObjectSelectionList.Entry<EditListScreen<T>.StringEntry> {

        private final EditListScreen.StringHolder holder;

        private final EditListScreen<T>.ObjectList list;

        private final ConfiguredButton editButton;

        private final ConfiguredButton deleteButton;

        public StringEntry(EditListScreen<T>.ObjectList list, EditListScreen.StringHolder holder) {
            this.list = list;
            this.holder = holder;
            this.editButton = new IconButton(0, 0, 1, 22, 20, CommonComponents.EMPTY, onPress -> EditListScreen.this.f_96541_.setScreen(new EditStringScreen(EditListScreen.this, EditListScreen.this.config, EditListScreen.this.background, Component.translatable("configured.gui.edit_value"), this.holder.getValue(), s -> {
                T value = (T) EditListScreen.this.listType.getValueParser().apply(s);
                if (value != null) {
                    return EditListScreen.this.holder.isValid(Collections.singletonList(value)) ? Pair.of(true, CommonComponents.EMPTY) : Pair.of(false, EditListScreen.this.holder.getValidationHint());
                } else {
                    return Pair.of(false, EditListScreen.this.listType.getHint());
                }
            }, this.holder::setValue)));
            this.editButton.setTooltip(Tooltip.create(Component.translatable("configured.gui.edit")), btn -> btn.m_142518_() && btn.m_198029_());
            this.editButton.f_93623_ = !EditListScreen.this.config.isReadOnly();
            this.deleteButton = new IconButton(0, 0, 11, 0, onPress -> {
                EditListScreen.this.values.remove(this.holder);
                this.list.removeEntry(this);
            });
            this.deleteButton.setTooltip(Tooltip.create(Component.translatable("configured.gui.remove")), btn -> btn.m_142518_() && btn.m_198029_());
            this.deleteButton.f_93623_ = !EditListScreen.this.config.isReadOnly();
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean selected, float partialTicks) {
            if (x % 2 != 0) {
                graphics.fill(left, top, left + width, top + 24, 1426063360);
            }
            graphics.drawString(EditListScreen.this.f_96541_.font, Component.literal(this.holder.getValue()), left + 5, top + 8, 16777215);
            this.editButton.f_93624_ = true;
            this.editButton.m_252865_(left + width - 44);
            this.editButton.m_253211_(top + 2);
            this.editButton.render(graphics, mouseX, mouseY, partialTicks);
            this.deleteButton.f_93624_ = true;
            this.deleteButton.m_252865_(left + width - 22);
            this.deleteButton.m_253211_(top + 2);
            this.deleteButton.render(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.editButton, this.deleteButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {

                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput output) {
                    output.add(NarratedElementType.TITLE, StringEntry.this.holder.getValue());
                }
            }, this.editButton, this.deleteButton);
        }
    }

    public static class StringHolder {

        private String value;

        public StringHolder(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}