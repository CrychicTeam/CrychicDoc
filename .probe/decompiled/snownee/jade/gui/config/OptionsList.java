package snownee.jade.gui.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import snownee.jade.gui.BaseOptionsScreen;
import snownee.jade.gui.config.value.CycleOptionValue;
import snownee.jade.gui.config.value.InputOptionValue;
import snownee.jade.gui.config.value.OptionValue;
import snownee.jade.gui.config.value.SliderOptionValue;
import snownee.jade.util.ClientProxy;

public class OptionsList extends ContainerObjectSelectionList<OptionsList.Entry> {

    public static final Component OPTION_ON = CommonComponents.OPTION_ON.copy().withStyle(style -> style.withColor(-4589878));

    public static final Component OPTION_OFF = CommonComponents.OPTION_OFF.copy().withStyle(style -> style.withColor(-30080));

    public final Set<OptionsList.Entry> forcePreview = Sets.newIdentityHashSet();

    protected final List<OptionsList.Entry> entries = Lists.newArrayList();

    private final Runnable diskWriter;

    public OptionsList.Title currentTitle;

    public KeyMapping selectedKey;

    private BaseOptionsScreen owner;

    private double targetScroll;

    private OptionsList.Entry defaultParent;

    private int lastActiveIndex;

    public OptionsList(BaseOptionsScreen owner, Minecraft client, int width, int height, int y0, int y1, int entryHeight, Runnable diskWriter) {
        super(client, width, height, y0, y1, entryHeight);
        this.owner = owner;
        this.diskWriter = diskWriter;
        this.m_93471_(false);
    }

    public OptionsList(BaseOptionsScreen owner, Minecraft client, int width, int height, int y0, int y1, int entryHeight) {
        this(owner, client, width, height, y0, y1, entryHeight, null);
    }

    private static void walkChildren(OptionsList.Entry entry, Consumer<OptionsList.Entry> consumer) {
        consumer.accept(entry);
        for (OptionsList.Entry child : entry.children) {
            walkChildren(child, consumer);
        }
    }

    @Override
    public int getRowWidth() {
        return Math.min(this.f_93388_, 300);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.owner.f_96543_ - 6;
    }

    @Override
    public void setScrollAmount(double d) {
        super.m_93410_(d);
        this.targetScroll = this.m_93517_();
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        double speed = !ClientProxy.hasFastScroll && Screen.hasControlDown() ? 4.5 : 1.5;
        this.targetScroll = this.m_93517_() - f * (double) this.f_93387_ * speed;
        return true;
    }

    @Override
    public boolean isFocused() {
        return this.owner.m_7222_() == this;
    }

    @Override
    protected boolean isSelectedItem(int i) {
        return Objects.equals(this.m_93511_(), this.m_6702_().get(i));
    }

    @Override
    protected void renderItem(GuiGraphics guiGraphics, int i, int j, float f, int k, int l, int m, int n, int o) {
        if (this.isSelectedItem(k) && this.m_5953_((double) i, (double) j)) {
            this.renderSelection(guiGraphics, m, n, o, -1, -1);
        }
        super.m_238964_(guiGraphics, i, j, f, k, l, m, n, o);
    }

    @Override
    protected void renderSelection(GuiGraphics guiGraphics, int i, int j, int k, int l, int m) {
        guiGraphics.fill(this.f_93393_, i - 2, this.f_93392_, i + k + 2, 872415231);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.targetScroll = Math.min(this.targetScroll, (double) this.m_93518_());
        double diff = this.targetScroll - super.m_93517_();
        if (Math.abs(diff) > 3.0E-4) {
            super.m_93410_(super.m_93517_() + diff * (double) delta);
        }
        this.f_168789_ = null;
        if (this.m_5953_((double) mouseX, (double) mouseY)) {
            this.f_168789_ = this.m_93412_((double) mouseX, (double) mouseY);
        }
        if (this.f_168789_ instanceof OptionsList.Title) {
            this.m_6987_(null);
        } else {
            this.m_6987_((OptionsList.Entry) this.f_168789_);
        }
        int activeIndex = this.f_168789_ != null ? this.m_6702_().indexOf(this.f_168789_) : Mth.clamp((int) this.m_93517_() / this.f_93387_, 0, this.m_5773_() - 1);
        if (activeIndex >= 0 && activeIndex != this.lastActiveIndex) {
            this.lastActiveIndex = activeIndex;
            for (OptionsList.Entry entry = (OptionsList.Entry) this.m_93500_(activeIndex); entry != null; entry = entry.parent) {
                if (entry instanceof OptionsList.Title) {
                    this.currentTitle = (OptionsList.Title) entry;
                    break;
                }
            }
        }
        this.m_280310_(guiGraphics);
        this.m_7733_(guiGraphics);
        int scrollPosX = this.getScrollbarPosition();
        int j = scrollPosX + 6;
        RenderSystem.setShader(GameRenderer::m_172820_);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.m_239227_(guiGraphics, mouseX, mouseY, delta);
        int int_8 = Math.max(0, this.m_5775_() - (this.f_93391_ - this.f_93390_ - 4));
        if (int_8 > 0) {
            RenderSystem.setShader(GameRenderer::m_172811_);
            int int_9 = (int) ((float) ((this.f_93391_ - this.f_93390_) * (this.f_93391_ - this.f_93390_)) / (float) this.m_5775_());
            int_9 = Mth.clamp(int_9, 32, this.f_93391_ - this.f_93390_ - 8);
            int int_10 = (int) this.m_93517_() * (this.f_93391_ - this.f_93390_ - int_9) / int_8 + this.f_93390_;
            if (int_10 < this.f_93390_) {
                int_10 = this.f_93390_;
            }
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferBuilder.m_5483_((double) scrollPosX, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.m_5483_((double) j, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.m_5483_((double) j, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.m_5483_((double) scrollPosX, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.m_5483_((double) scrollPosX, (double) (int_10 + int_9), 0.0).color(128, 128, 128, 255).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.m_5483_((double) j, (double) (int_10 + int_9), 0.0).color(128, 128, 128, 255).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.m_5483_((double) j, (double) int_10, 0.0).color(128, 128, 128, 255).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.m_5483_((double) scrollPosX, (double) int_10, 0.0).color(128, 128, 128, 255).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.m_5483_((double) scrollPosX, (double) (int_10 + int_9 - 1), 0.0).color(192, 192, 192, 255).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.m_5483_((double) (j - 1), (double) (int_10 + int_9 - 1), 0.0).color(192, 192, 192, 255).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.m_5483_((double) (j - 1), (double) int_10, 0.0).color(192, 192, 192, 255).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.m_5483_((double) scrollPosX, (double) int_10, 0.0).color(192, 192, 192, 255).uv(0.0F, 0.0F).endVertex();
            tessellator.end();
        }
        this.m_7154_(guiGraphics, mouseX, mouseY);
        RenderSystem.disableBlend();
        guiGraphics.disableScissor();
        guiGraphics.setColor(0.35F, 0.35F, 0.35F, 1.0F);
        guiGraphics.blit(Screen.BACKGROUND_LOCATION, 0, this.owner.f_96544_ - 32, (float) this.owner.f_96543_, 32.0F, this.owner.f_96543_, this.owner.f_96544_, 32, 32);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.fillGradient(0, this.owner.f_96544_ - 32 - 4, this.owner.f_96543_, this.owner.f_96544_ - 32, 100, 0, -301989888);
    }

    public void save() {
        this.m_6702_().stream().filter(e -> e instanceof OptionValue).map(e -> (OptionValue) e).forEach(OptionValue::save);
        if (this.diskWriter != null) {
            this.diskWriter.run();
        }
    }

    public <T extends OptionsList.Entry> T add(T entry) {
        this.entries.add(entry);
        if (entry instanceof OptionsList.Title) {
            this.setDefaultParent(entry);
        } else if (this.defaultParent != null) {
            entry.parent(this.defaultParent);
        }
        return entry;
    }

    @Nullable
    public OptionsList.Entry getEntryAt(double x, double y) {
        return (OptionsList.Entry) this.m_93412_(x, y);
    }

    @Override
    public int getRowTop(int i) {
        return super.m_7610_(i);
    }

    @Override
    public int getRowBottom(int i) {
        return super.m_93485_(i);
    }

    public void setDefaultParent(OptionsList.Entry defaultParent) {
        this.defaultParent = defaultParent;
    }

    public MutableComponent title(String string) {
        return this.add(new OptionsList.Title(string)).getTitle();
    }

    public OptionValue<Float> slider(String optionName, float value, Consumer<Float> setter) {
        return this.slider(optionName, value, setter, 0.0F, 1.0F, FloatUnaryOperator.identity());
    }

    public OptionValue<Float> slider(String optionName, float value, Consumer<Float> setter, float min, float max, FloatUnaryOperator aligner) {
        return this.add(new SliderOptionValue(optionName, value, setter, min, max, aligner));
    }

    public <T> OptionValue<T> input(String optionName, T value, Consumer<T> setter, Predicate<String> validator) {
        return this.add(new InputOptionValue<>(this::updateSaveState, optionName, value, setter, validator));
    }

    public <T> OptionValue<T> input(String optionName, T value, Consumer<T> setter) {
        return this.input(optionName, value, setter, Predicates.alwaysTrue());
    }

    public OptionValue<Boolean> choices(String optionName, boolean value, BooleanConsumer setter) {
        return this.choices(optionName, value, setter, null);
    }

    public OptionValue<Boolean> choices(String optionName, boolean value, BooleanConsumer setter, @Nullable Consumer<CycleButton.Builder<Boolean>> builderConsumer) {
        CycleButton.Builder<Boolean> builder = CycleButton.booleanBuilder(OPTION_ON, OPTION_OFF);
        if (builderConsumer != null) {
            builderConsumer.accept(builder);
        }
        return this.add(new CycleOptionValue<>(optionName, builder, value, setter));
    }

    public <T extends Enum<T>> OptionValue<T> choices(String optionName, T value, Consumer<T> setter) {
        return this.choices(optionName, value, setter, null);
    }

    public <T extends Enum<T>> OptionValue<T> choices(String optionName, T value, Consumer<T> setter, @Nullable Consumer<CycleButton.Builder<T>> builderConsumer) {
        List<T> values = Arrays.asList((Enum[]) value.getClass().getEnumConstants());
        CycleButton.Builder<T> builder = CycleButton.<T>builder(v -> {
            String name = v.name().toLowerCase(Locale.ENGLISH);
            return (Component) (switch(name) {
                case "on" ->
                    OPTION_ON;
                case "off" ->
                    OPTION_OFF;
                default ->
                    OptionsList.Entry.makeTitle(optionName + "_" + name);
            });
        }).withValues(values);
        if (builderConsumer != null) {
            builderConsumer.accept(builder);
        }
        return this.add(new CycleOptionValue<>(optionName, builder, value, setter));
    }

    public <T> OptionValue<T> choices(String optionName, T value, List<T> values, Consumer<T> setter, Function<T, Component> nameProvider) {
        return this.add(new CycleOptionValue<>(optionName, CycleButton.<T>builder(nameProvider).withValues(values), value, setter));
    }

    public void keybind(KeyMapping keybind) {
        this.add(new KeybindOptionButton(this, keybind));
    }

    public void removed() {
        this.forcePreview.clear();
        for (OptionsList.Entry entry : this.entries) {
            entry.parent = null;
            if (!entry.children.isEmpty()) {
                entry.children.clear();
            }
        }
        this.m_93516_();
        this.owner = null;
    }

    public void updateSearch(String search) {
        this.m_93516_();
        if (search.isBlank()) {
            this.entries.forEach(x$0 -> this.m_7085_(x$0));
        } else {
            Set<OptionsList.Entry> matches = Sets.newLinkedHashSet();
            String[] keywords = search.split("\\s+");
            for (OptionsList.Entry entry : this.entries) {
                int bingo = 0;
                for (String keyword : keywords) {
                    keyword = keyword.toLowerCase(Locale.ENGLISH);
                    for (String message : entry.getMessages()) {
                        if (message.contains(keyword)) {
                            bingo++;
                            break;
                        }
                    }
                }
                if (bingo == keywords.length) {
                    walkChildren(entry, matches::add);
                    while (entry.parent != null) {
                        entry = entry.parent;
                        matches.add(entry);
                    }
                }
            }
            for (OptionsList.Entry entry : this.entries) {
                if (matches.contains(entry)) {
                    this.m_7085_(entry);
                }
            }
            if (matches.isEmpty()) {
                this.m_7085_(new OptionsList.Title(Component.translatable("gui.jade.no_results").withStyle(ChatFormatting.GRAY)));
            }
        }
    }

    public void updateSaveState() {
        for (OptionsList.Entry entry : this.entries) {
            if (entry instanceof OptionValue<?> value && !value.isValidValue()) {
                this.owner.saveButton.f_93623_ = false;
                return;
            }
        }
        this.owner.saveButton.f_93623_ = true;
    }

    public void showOnTop(OptionsList.Entry entry) {
        this.targetScroll = (double) (this.f_93387_ * this.m_6702_().indexOf(entry) + 1);
    }

    public void resetMappingAndUpdateButtons() {
        for (OptionsList.Entry entry : this.entries) {
            if (entry instanceof KeybindOptionButton button) {
                button.refresh(this.selectedKey);
            }
        }
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (this.selectedKey != null) {
            Options options = Minecraft.getInstance().options;
            if (i == 256) {
                options.setKey(this.selectedKey, InputConstants.UNKNOWN);
            } else {
                options.setKey(this.selectedKey, InputConstants.getKey(i, j));
            }
            this.selectedKey = null;
            this.resetMappingAndUpdateButtons();
            return true;
        } else {
            return super.m_7933_(i, j, k);
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.selectedKey != null) {
            Options options = Minecraft.getInstance().options;
            options.setKey(this.selectedKey, InputConstants.Type.MOUSE.getOrCreate(i));
            this.selectedKey = null;
            this.resetMappingAndUpdateButtons();
        }
        return super.m_6375_(d, e, i);
    }

    public static class Entry extends ContainerObjectSelectionList.Entry<OptionsList.Entry> {

        protected final Minecraft client;

        private final List<String> messages = Lists.newArrayList();

        private final List<AbstractWidget> widgets = Lists.newArrayList();

        private final List<Vector2i> widgetOffsets = Lists.newArrayList();

        @Nullable
        protected String description;

        private OptionsList.Entry parent;

        private List<OptionsList.Entry> children = List.of();

        public Entry() {
            this.client = Minecraft.getInstance();
        }

        public static MutableComponent makeTitle(String key) {
            return Component.translatable(makeKey(key));
        }

        public static String makeKey(String key) {
            return Util.makeDescriptionId("config", new ResourceLocation("jade", key));
        }

        public AbstractWidget getFirstWidget() {
            return this.widgets.isEmpty() ? null : (AbstractWidget) this.widgets.get(0);
        }

        public void addWidget(AbstractWidget widget, int offsetX) {
            this.addWidget(widget, offsetX, -widget.getHeight() / 2);
        }

        public void addWidget(AbstractWidget widget, int offsetX, int offsetY) {
            this.widgets.add(widget);
            this.widgetOffsets.add(new Vector2i(offsetX, offsetY));
        }

        @Override
        public List<? extends AbstractWidget> children() {
            return this.widgets;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int width, int height, int mouseX, int mouseY, boolean hovered, float deltaTime) {
            for (AbstractWidget widget : this.widgets) {
                Vector2i offset = (Vector2i) this.widgetOffsets.get(this.widgets.indexOf(widget));
                widget.setX(rowLeft + width - 110 + offset.x);
                widget.setY(rowTop + height / 2 + offset.y);
                widget.render(guiGraphics, mouseX, mouseY, deltaTime);
            }
        }

        public void setDisabled(boolean b) {
            for (AbstractWidget widget : this.widgets) {
                widget.active = !b;
                if (widget instanceof EditBox box) {
                    box.setEditable(!b);
                }
            }
        }

        @Nullable
        public String getDescription() {
            return this.description;
        }

        public int getTextX(int width) {
            return 0;
        }

        public int getTextWidth() {
            return 0;
        }

        public OptionsList.Entry parent(OptionsList.Entry parent) {
            this.parent = parent;
            if (parent.children.isEmpty()) {
                parent.children = Lists.newArrayList();
            }
            parent.children.add(this);
            return this;
        }

        public OptionsList.Entry parent() {
            return this.parent;
        }

        public final List<String> getMessages() {
            return this.messages;
        }

        public void addMessage(String message) {
            this.messages.add(StringUtil.stripColor(message).toLowerCase(Locale.ENGLISH));
        }

        public void addMessageKey(String key) {
            key = makeKey(key + "_extra_msg");
            if (I18n.exists(key)) {
                this.addMessage(I18n.get(key));
            }
        }
    }

    public static class Title extends OptionsList.Entry {

        public Component narration;

        private final MutableComponent title;

        private int x;

        public Title(String key) {
            this.title = makeTitle(key);
            this.addMessageKey(key);
            this.addMessage(this.title.getString());
            key = makeKey(key + "_desc");
            if (I18n.exists(key)) {
                this.description = I18n.get(key);
                this.addMessage(this.description);
            }
            this.narration = Component.translatable("narration.jade.category", this.title);
        }

        public Title(MutableComponent title) {
            this.title = title;
            this.narration = title;
        }

        public MutableComponent getTitle() {
            return this.title;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int width, int height, int mouseX, int mouseY, boolean hovered, float deltaTime) {
            this.x = rowLeft;
            guiGraphics.drawString(this.client.font, this.title, this.getTextX(width), rowTop + height - 9, 16777215);
        }

        @Override
        public int getTextX(int width) {
            return this.x + (width - this.client.font.width(this.title)) / 2;
        }

        @Override
        public int getTextWidth() {
            return this.client.font.width(this.title);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(new NarratableEntry() {

                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput narrationElementOutput) {
                    narrationElementOutput.add(NarratedElementType.TITLE, Title.this.narration);
                }
            });
        }
    }
}