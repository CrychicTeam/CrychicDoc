package eu.midnightdust.lib.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.midnightdust.lib.util.PlatformFunctions;
import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class MidnightConfig {

    private static final Pattern INTEGER_ONLY = Pattern.compile("(-?[0-9]*)");

    private static final Pattern DECIMAL_ONLY = Pattern.compile("-?([\\d]+\\.?[\\d]*|[\\d]*\\.?[\\d]+|\\.)");

    private static final Pattern HEXADECIMAL_ONLY = Pattern.compile("(-?[#0-9a-fA-F]*)");

    private static final List<MidnightConfig.EntryInfo> entries = new ArrayList();

    public static final Map<String, Class<?>> configClass = new HashMap();

    private static Path path;

    private static final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(new int[] { 128 }).excludeFieldsWithModifiers(new int[] { 2 }).addSerializationExclusionStrategy(new MidnightConfig.HiddenAnnotationExclusionStrategy()).setPrettyPrinting().create();

    public static void init(String modid, Class<?> config) {
        path = PlatformFunctions.getConfigDirectory().resolve(modid + ".json");
        configClass.put(modid, config);
        for (Field field : config.getFields()) {
            MidnightConfig.EntryInfo info = new MidnightConfig.EntryInfo();
            if ((field.isAnnotationPresent(MidnightConfig.Entry.class) || field.isAnnotationPresent(MidnightConfig.Comment.class)) && !field.isAnnotationPresent(MidnightConfig.Server.class) && !field.isAnnotationPresent(MidnightConfig.Hidden.class) && PlatformFunctions.isClientEnv()) {
                initClient(modid, field, info);
            }
            if (field.isAnnotationPresent(MidnightConfig.Comment.class)) {
                info.centered = ((MidnightConfig.Comment) field.getAnnotation(MidnightConfig.Comment.class)).centered();
            }
            if (field.isAnnotationPresent(MidnightConfig.Entry.class)) {
                try {
                    info.defaultValue = field.get(null);
                } catch (IllegalAccessException var10) {
                }
            }
        }
        try {
            gson.fromJson(Files.newBufferedReader(path), config);
        } catch (Exception var9) {
            write(modid);
        }
        for (MidnightConfig.EntryInfo infox : entries) {
            if (infox.field.isAnnotationPresent(MidnightConfig.Entry.class)) {
                try {
                    infox.value = infox.field.get(null);
                    infox.tempValue = infox.value.toString();
                } catch (IllegalAccessException var8) {
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void initClient(String modid, Field field, MidnightConfig.EntryInfo info) {
        Class<?> type = field.getType();
        MidnightConfig.Entry e = (MidnightConfig.Entry) field.getAnnotation(MidnightConfig.Entry.class);
        info.width = e != null ? e.width() : 0;
        info.field = field;
        info.id = modid;
        if (e != null) {
            if (!e.name().equals("")) {
                info.name = Component.translatable(e.name());
            }
            if (type == int.class) {
                textField(info, Integer::parseInt, INTEGER_ONLY, (double) ((int) e.min()), (double) ((int) e.max()), true);
            } else if (type == float.class) {
                textField(info, Float::parseFloat, DECIMAL_ONLY, (double) ((float) e.min()), (double) ((float) e.max()), false);
            } else if (type == double.class) {
                textField(info, Double::parseDouble, DECIMAL_ONLY, e.min(), e.max(), false);
            } else if (type == String.class || type == List.class) {
                textField(info, String::length, null, Math.min(e.min(), 0.0), Math.max(e.max(), 1.0), true);
            } else if (type == boolean.class) {
                Function<Object, Component> func = value -> Component.translatable((Boolean) value ? "gui.yes" : "gui.no").withStyle((Boolean) value ? ChatFormatting.GREEN : ChatFormatting.RED);
                info.widget = new SimpleEntry((Button.OnPress) button -> {
                    info.value = !(Boolean) info.value;
                    button.m_93666_((Component) func.apply(info.value));
                }, func);
            } else if (type.isEnum()) {
                List<?> values = Arrays.asList(field.getType().getEnumConstants());
                Function<Object, Component> func = value -> Component.translatable(modid + ".midnightconfig.enum." + type.getSimpleName() + "." + info.value.toString());
                info.widget = new SimpleEntry((Button.OnPress) button -> {
                    int index = values.indexOf(info.value) + 1;
                    info.value = values.get(index >= values.size() ? 0 : index);
                    button.m_93666_((Component) func.apply(info.value));
                }, func);
            }
        }
        entries.add(info);
    }

    public static Tooltip getTooltip(MidnightConfig.EntryInfo info) {
        return Tooltip.create((Component) (info.error != null ? info.error : (I18n.exists(info.id + ".midnightconfig." + info.field.getName() + ".tooltip") ? Component.translatable(info.id + ".midnightconfig." + info.field.getName() + ".tooltip") : Component.empty())));
    }

    private static void textField(MidnightConfig.EntryInfo info, Function<String, Number> f, Pattern pattern, double min, double max, boolean cast) {
        boolean isNumber = pattern != null;
        info.widget = (BiFunction) (t, b) -> s -> {
            s = s.trim();
            if (!s.isEmpty() && isNumber && !pattern.matcher(s).matches()) {
                return false;
            } else {
                Number value = 0;
                boolean inLimits = false;
                info.error = null;
                if ((!isNumber || !s.isEmpty()) && !s.equals("-") && !s.equals(".")) {
                    try {
                        value = (Number) f.apply(s);
                    } catch (NumberFormatException var16) {
                        return false;
                    }
                    inLimits = value.doubleValue() >= min && value.doubleValue() <= max;
                    info.error = inLimits ? null : Component.literal(value.doubleValue() < min ? "§cMinimum " + (isNumber ? "value" : "length") + (cast ? " is " + (int) min : " is " + min) : "§cMaximum " + (isNumber ? "value" : "length") + (cast ? " is " + (int) max : " is " + max)).withStyle(ChatFormatting.RED);
                    t.m_257544_(getTooltip(info));
                }
                info.tempValue = s;
                t.setTextColor(inLimits ? -1 : -34953);
                info.inLimits = inLimits;
                b.f_93623_ = entries.stream().allMatch(e -> e.inLimits);
                if (inLimits && info.field.getType() != List.class) {
                    info.value = isNumber ? value : s;
                } else if (inLimits) {
                    if (((List) info.value).size() == info.index) {
                        ((List) info.value).add("");
                    }
                    ((List) info.value).set(info.index, (String) Arrays.stream(info.tempValue.replace("[", "").replace("]", "").split(", ")).toList().get(0));
                }
                if (((MidnightConfig.Entry) info.field.getAnnotation(MidnightConfig.Entry.class)).isColor()) {
                    if (!s.contains("#")) {
                        s = "#" + s;
                    }
                    if (!HEXADECIMAL_ONLY.matcher(s).matches()) {
                        return false;
                    }
                    try {
                        info.colorButton.setMessage(Component.literal("⬛").setStyle(Style.EMPTY.withColor(Color.decode(info.tempValue).getRGB())));
                    } catch (Exception var15) {
                    }
                }
                return true;
            }
        };
    }

    public static void write(String modid) {
        path = PlatformFunctions.getConfigDirectory().resolve(modid + ".json");
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                Files.createFile(path);
            }
            Files.write(path, gson.toJson(((Class) configClass.get(modid)).getDeclaredConstructor().newInstance()).getBytes(), new OpenOption[0]);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static Screen getScreen(Screen parent, String modid) {
        return new MidnightConfig.MidnightConfigScreen(parent, modid);
    }

    public static class ButtonEntry extends ContainerObjectSelectionList.Entry<MidnightConfig.ButtonEntry> {

        private static final Font textRenderer = Minecraft.getInstance().font;

        public final List<AbstractWidget> buttons;

        private final Component text;

        public final MidnightConfig.EntryInfo info;

        private final List<AbstractWidget> children = new ArrayList();

        public static final Map<AbstractWidget, Component> buttonsWithText = new HashMap();

        private ButtonEntry(List<AbstractWidget> buttons, Component text, MidnightConfig.EntryInfo info) {
            if (!buttons.isEmpty()) {
                buttonsWithText.put((AbstractWidget) buttons.get(0), text);
            }
            this.buttons = buttons;
            this.text = text;
            this.info = info;
            this.children.addAll(buttons);
        }

        @Override
        public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.buttons.forEach(b -> {
                b.setY(y);
                b.render(context, mouseX, mouseY, tickDelta);
            });
            if (this.text != null && (!this.text.getString().contains("spacer") || !this.buttons.isEmpty())) {
                if (this.info.centered) {
                    context.drawString(textRenderer, this.text, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - textRenderer.width(this.text) / 2, y + 5, 16777215);
                } else {
                    int wrappedY = y;
                    for (FormattedCharSequence orderedText : textRenderer.split(this.text, this.buttons.size() > 1 ? ((AbstractWidget) this.buttons.get(1)).getX() - 24 : Minecraft.getInstance().getWindow().getGuiScaledWidth() - 24)) {
                        context.drawString(textRenderer, orderedText, 12, wrappedY + 5, 16777215);
                        wrappedY += 9;
                    }
                }
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Client {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Comment {

        boolean centered() default false;

        String category() default "default";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Entry {

        int width() default 100;

        double min() default Double.MIN_NORMAL;

        double max() default Double.MAX_VALUE;

        String name() default "";

        boolean isColor() default false;

        boolean isSlider() default false;

        int precision() default 100;

        String category() default "default";
    }

    protected static class EntryInfo {

        Field field;

        Object widget;

        int width;

        boolean centered;

        Component error;

        Object defaultValue;

        Object value;

        String tempValue;

        boolean inLimits = true;

        String id;

        Component name;

        int index;

        AbstractWidget colorButton;

        Tab tab;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Hidden {
    }

    public static class HiddenAnnotationExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(MidnightConfig.Entry.class) == null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class MidnightConfigListWidget extends ContainerObjectSelectionList<MidnightConfig.ButtonEntry> {

        Font textRenderer;

        public MidnightConfigListWidget(Minecraft minecraftClient, int i, int j, int k, int l, int m) {
            super(minecraftClient, i, j, k, l, m);
            this.f_93394_ = false;
            this.textRenderer = minecraftClient.font;
        }

        @Override
        public int getScrollbarPosition() {
            return this.f_93388_ - 7;
        }

        public void addButton(List<AbstractWidget> buttons, Component text, MidnightConfig.EntryInfo info) {
            this.m_7085_(new MidnightConfig.ButtonEntry(buttons, text, info));
        }

        public void clear() {
            this.m_93516_();
        }

        @Override
        public int getRowWidth() {
            return 10000;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class MidnightConfigScreen extends Screen {

        public final String translationPrefix;

        public final Screen parent;

        public final String modid;

        public MidnightConfig.MidnightConfigListWidget list;

        public boolean reload = false;

        public TabManager tabManager = new TabManager(a -> {
        }, a -> {
        });

        public Map<String, Tab> tabs = new HashMap();

        public Tab prevTab;

        public TabNavigationBar tabNavigation;

        public Button done;

        public double scrollProgress = 0.0;

        protected MidnightConfigScreen(Screen parent, String modid) {
            super(Component.translatable(modid + ".midnightconfig.title"));
            this.parent = parent;
            this.modid = modid;
            this.translationPrefix = modid + ".midnightconfig.";
            this.loadValues();
            for (MidnightConfig.EntryInfo e : MidnightConfig.entries) {
                if (e.id.equals(modid)) {
                    String tabId = e.field.isAnnotationPresent(MidnightConfig.Entry.class) ? ((MidnightConfig.Entry) e.field.getAnnotation(MidnightConfig.Entry.class)).category() : ((MidnightConfig.Comment) e.field.getAnnotation(MidnightConfig.Comment.class)).category();
                    String name = this.translationPrefix + "category." + tabId;
                    if (!I18n.exists(name) && tabId.equals("default")) {
                        name = this.translationPrefix + "title";
                    }
                    if (!this.tabs.containsKey(name)) {
                        Tab tab = new GridLayoutTab(Component.translatable(name));
                        e.tab = tab;
                        this.tabs.put(name, tab);
                    } else {
                        e.tab = (Tab) this.tabs.get(name);
                    }
                }
            }
            this.tabNavigation = TabNavigationBar.builder(this.tabManager, this.f_96543_).addTabs((Tab[]) this.tabs.values().toArray(new Tab[0])).build();
            this.tabNavigation.selectTab(0, false);
            this.tabNavigation.arrangeElements();
            this.prevTab = this.tabManager.getCurrentTab();
        }

        @Override
        public void tick() {
            super.tick();
            if (this.prevTab != null && this.prevTab != this.tabManager.getCurrentTab()) {
                this.prevTab = this.tabManager.getCurrentTab();
                this.list.clear();
                this.fillList();
                this.list.m_93410_(0.0);
            }
            this.scrollProgress = this.list.m_93517_();
            for (MidnightConfig.EntryInfo info : MidnightConfig.entries) {
                try {
                    info.field.set(null, info.value);
                } catch (IllegalAccessException var4) {
                }
            }
            this.updateResetButtons();
        }

        public void updateResetButtons() {
            if (this.list != null) {
                for (MidnightConfig.ButtonEntry entry : this.list.m_6702_()) {
                    if (entry.buttons != null && entry.buttons.size() > 1 && entry.buttons.get(1) instanceof Button button) {
                        button.f_93623_ = !Objects.equals(entry.info.value.toString(), entry.info.defaultValue.toString());
                    }
                }
            }
        }

        public void loadValues() {
            try {
                MidnightConfig.gson.fromJson(Files.newBufferedReader(MidnightConfig.path), (Class) MidnightConfig.configClass.get(this.modid));
            } catch (Exception var5) {
                MidnightConfig.write(this.modid);
            }
            for (MidnightConfig.EntryInfo info : MidnightConfig.entries) {
                if (info.field.isAnnotationPresent(MidnightConfig.Entry.class)) {
                    try {
                        info.value = info.field.get(null);
                        info.tempValue = info.value.toString();
                    } catch (IllegalAccessException var4) {
                    }
                }
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return this.tabNavigation.keyPressed(keyCode) ? true : super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public void init() {
            super.init();
            this.tabNavigation.setWidth(this.f_96543_);
            this.tabNavigation.arrangeElements();
            if (this.tabs.size() > 1) {
                this.m_142416_(this.tabNavigation);
            }
            this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, button -> {
                this.loadValues();
                ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(this.parent);
            }).bounds(this.f_96543_ / 2 - 154, this.f_96544_ - 28, 150, 20).build());
            this.done = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> {
                for (MidnightConfig.EntryInfo info : MidnightConfig.entries) {
                    if (info.id.equals(this.modid)) {
                        try {
                            info.field.set(null, info.value);
                        } catch (IllegalAccessException var5) {
                        }
                    }
                }
                MidnightConfig.write(this.modid);
                ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(this.parent);
            }).bounds(this.f_96543_ / 2 + 4, this.f_96544_ - 28, 150, 20).build());
            this.list = new MidnightConfig.MidnightConfigListWidget(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
            if (this.f_96541_ != null && this.f_96541_.level != null) {
                this.list.m_93488_(false);
            }
            this.m_7787_(this.list);
            this.fillList();
            this.reload = true;
        }

        public void fillList() {
            for (MidnightConfig.EntryInfo info : MidnightConfig.entries) {
                if (info.id.equals(this.modid) && (info.tab == null || info.tab == this.tabManager.getCurrentTab())) {
                    Component name = (Component) Objects.requireNonNullElseGet(info.name, () -> Component.translatable(this.translationPrefix + info.field.getName()));
                    Button resetButton = Button.builder(Component.literal("Reset").withStyle(ChatFormatting.RED), button -> {
                        info.value = info.defaultValue;
                        info.tempValue = info.defaultValue.toString();
                        info.index = 0;
                        this.list.clear();
                        this.fillList();
                    }).bounds(this.f_96543_ - 205, 0, 40, 20).build();
                    if (info.widget instanceof java.util.Map.Entry<Button.OnPress, Function<Object, Component>> widget) {
                        if (info.field.getType().isEnum()) {
                            widget.setValue((Function) value -> Component.translatable(this.translationPrefix + "enum." + info.field.getType().getSimpleName() + "." + info.value.toString()));
                        }
                        this.list.addButton(List.of(Button.builder((Component) ((Function) widget.getValue()).apply(info.value), (Button.OnPress) widget.getKey()).bounds(this.f_96543_ - 160, 0, 150, 20).tooltip(MidnightConfig.getTooltip(info)).build(), resetButton), name, info);
                    } else if (info.field.getType() == List.class) {
                        if (!this.reload) {
                            info.index = 0;
                        }
                        EditBox widget = new EditBox(this.f_96547_, this.f_96543_ - 160, 0, 150, 20, Component.empty());
                        widget.setMaxLength(info.width);
                        if (info.index < ((List) info.value).size()) {
                            widget.setValue(String.valueOf(((List) info.value).get(info.index)));
                        }
                        Predicate<String> processor = (Predicate<String>) ((BiFunction) info.widget).apply(widget, this.done);
                        widget.setFilter(processor);
                        resetButton.m_93674_(20);
                        resetButton.m_93666_(Component.literal("R").withStyle(ChatFormatting.RED));
                        Button cycleButton = Button.builder(Component.literal(String.valueOf(info.index)).withStyle(ChatFormatting.GOLD), button -> {
                            if (((List) info.value).contains("")) {
                                ((List) info.value).remove("");
                            }
                            info.index++;
                            if (info.index > ((List) info.value).size()) {
                                info.index = 0;
                            }
                            this.list.clear();
                            this.fillList();
                        }).bounds(this.f_96543_ - 185, 0, 20, 20).build();
                        widget.m_257544_(MidnightConfig.getTooltip(info));
                        this.list.addButton(List.of(widget, resetButton, cycleButton), name, info);
                    } else if (info.widget != null) {
                        MidnightConfig.Entry e = (MidnightConfig.Entry) info.field.getAnnotation(MidnightConfig.Entry.class);
                        AbstractWidget widget;
                        if (e.isSlider()) {
                            widget = new MidnightConfig.MidnightSliderWidget(this.f_96543_ - 160, 0, 150, 20, Component.nullToEmpty(info.tempValue), (Double.parseDouble(info.tempValue) - e.min()) / (e.max() - e.min()), info);
                        } else {
                            widget = new EditBox(this.f_96547_, this.f_96543_ - 160, 0, 150, 20, null, Component.nullToEmpty(info.tempValue));
                        }
                        if (widget instanceof EditBox textField) {
                            textField.setMaxLength(info.width);
                            textField.setValue(info.tempValue);
                            Predicate<String> processor = (Predicate<String>) ((BiFunction) info.widget).apply(textField, this.done);
                            textField.setFilter(processor);
                        }
                        widget.setTooltip(MidnightConfig.getTooltip(info));
                        if (e.isColor()) {
                            resetButton.m_93674_(20);
                            resetButton.m_93666_(Component.literal("R").withStyle(ChatFormatting.RED));
                            Button colorButton = Button.builder(Component.literal("⬛"), button -> {
                            }).bounds(this.f_96543_ - 185, 0, 20, 20).build();
                            try {
                                colorButton.m_93666_(Component.literal("⬛").setStyle(Style.EMPTY.withColor(Color.decode(info.tempValue).getRGB())));
                            } catch (Exception var9) {
                            }
                            info.colorButton = colorButton;
                            colorButton.f_93623_ = false;
                            this.list.addButton(List.of(widget, resetButton, colorButton), name, info);
                        } else {
                            this.list.addButton(List.of(widget, resetButton), name, info);
                        }
                    } else {
                        this.list.addButton(List.of(), name, info);
                    }
                }
                this.list.m_93410_(this.scrollProgress);
                this.updateResetButtons();
            }
        }

        @Override
        public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
            this.m_280273_(context);
            this.list.m_88315_(context, mouseX, mouseY, delta);
            if (this.tabs.size() < 2) {
                context.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 15, 16777215);
            }
            super.render(context, mouseX, mouseY, delta);
        }
    }

    public static class MidnightSliderWidget extends AbstractSliderButton {

        private final MidnightConfig.EntryInfo info;

        private final MidnightConfig.Entry e;

        public MidnightSliderWidget(int x, int y, int width, int height, Component text, double value, MidnightConfig.EntryInfo info) {
            super(x, y, width, height, text, value);
            this.e = (MidnightConfig.Entry) info.field.getAnnotation(MidnightConfig.Entry.class);
            this.info = info;
        }

        @Override
        protected void updateMessage() {
            this.m_93666_(Component.nullToEmpty(this.info.tempValue));
        }

        @Override
        protected void applyValue() {
            if (this.info.field.getType() == int.class) {
                this.info.value = Double.valueOf(this.e.min() + this.f_93577_ * (this.e.max() - this.e.min())).intValue();
            } else if (this.info.field.getType() == double.class) {
                this.info.value = (double) Math.round((this.e.min() + this.f_93577_ * (this.e.max() - this.e.min())) * (double) this.e.precision()) / (double) this.e.precision();
            } else if (this.info.field.getType() == float.class) {
                this.info.value = (float) Math.round((this.e.min() + this.f_93577_ * (this.e.max() - this.e.min())) * (double) ((float) this.e.precision())) / (float) this.e.precision();
            }
            this.info.tempValue = String.valueOf(this.info.value);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Server {
    }
}