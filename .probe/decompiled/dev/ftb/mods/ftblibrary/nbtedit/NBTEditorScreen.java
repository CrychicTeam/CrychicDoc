package dev.ftb.mods.ftblibrary.nbtedit;

import dev.ftb.mods.ftblibrary.config.ConfigFromString;
import dev.ftb.mods.ftblibrary.config.ConfigValue;
import dev.ftb.mods.ftblibrary.config.DoubleConfig;
import dev.ftb.mods.ftblibrary.config.IntConfig;
import dev.ftb.mods.ftblibrary.config.LongConfig;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconWithBorder;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
import dev.ftb.mods.ftblibrary.ui.misc.SimpleToast;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBTEditorScreen extends AbstractThreePanelScreen<NBTEditorScreen.NBTPanel> {

    private static final int TOP_PANEL_H = 20;

    public static final Icon NBT_BYTE = getIcon("byte");

    public static final Icon NBT_SHORT = getIcon("short");

    public static final Icon NBT_INT = getIcon("int");

    public static final Icon NBT_LONG = getIcon("long");

    public static final Icon NBT_FLOAT = getIcon("float");

    public static final Icon NBT_DOUBLE = getIcon("double");

    public static final Icon NBT_STRING = getIcon("string");

    public static final Icon NBT_LIST = getIcon("list");

    public static final Icon NBT_LIST_CLOSED = getIcon("list").combineWith(getIcon("map_closed").withColor(Color4I.rgba(-1056964609)));

    public static final Icon NBT_LIST_OPEN = getIcon("list");

    public static final Icon NBT_MAP = getIcon("map");

    public static final Icon NBT_MAP_CLOSED = getIcon("map").combineWith(getIcon("map_closed").withColor(Color4I.rgba(-1056964609)));

    public static final Icon NBT_MAP_OPEN = getIcon("map");

    public static final Icon NBT_BYTE_ARRAY = getIcon("byte_array");

    public static final Icon NBT_BYTE_ARRAY_CLOSED = getIcon("byte_array_closed");

    public static final Icon NBT_BYTE_ARRAY_OPEN = getIcon("byte_array_open");

    public static final Icon NBT_INT_ARRAY = getIcon("int_array");

    public static final Icon NBT_INT_ARRAY_CLOSED = getIcon("int_array_closed");

    public static final Icon NBT_INT_ARRAY_OPEN = getIcon("int_array_open");

    private final CompoundTag info;

    private final NBTEditorScreen.NBTCallback callback;

    private final NBTEditorScreen.ButtonNBTMap buttonNBTRoot;

    private NBTEditorScreen.ButtonNBT selected;

    public final Panel panelTopLeft;

    public final Panel panelTopRight;

    private boolean accepted = false;

    public NBTEditorScreen(CompoundTag info, CompoundTag nbt, NBTEditorScreen.NBTCallback callback) {
        this.info = info;
        this.callback = callback;
        this.panelTopLeft = new NBTEditorScreen.TopLeftPanel();
        this.panelTopRight = new NBTEditorScreen.TopRightPanel();
        this.buttonNBTRoot = new NBTEditorScreen.ButtonNBTMap(this.mainPanel, null, this.getInfoTitle(info), nbt);
        this.buttonNBTRoot.updateChildren(true);
        this.buttonNBTRoot.setCollapsedTree(true);
        this.buttonNBTRoot.setCollapsed(false);
        this.setSelected(this.buttonNBTRoot);
    }

    private String getInfoTitle(CompoundTag info) {
        if (info.contains("title")) {
            MutableComponent title = Component.Serializer.fromJson(info.getString("title"));
            if (title != null) {
                return title.getString();
            }
        } else if (info.contains("type")) {
            return info.getString("type").toUpperCase();
        }
        return "ROOT";
    }

    private void collapseAll(boolean collapse) {
        for (Widget w : this.mainPanel.getWidgets()) {
            if (w instanceof NBTEditorScreen.ButtonNBTCollection collection) {
                collection.setCollapsedTree(collapse);
            }
        }
        this.mainPanel.refreshWidgets();
    }

    @Override
    protected void doCancel() {
        this.getGui().closeGui();
    }

    @Override
    protected void doAccept() {
        this.accepted = true;
        this.getGui().closeGui();
    }

    @Override
    protected int getTopPanelHeight() {
        return 20;
    }

    protected NBTEditorScreen.NBTPanel createMainPanel() {
        return new NBTEditorScreen.NBTPanel();
    }

    @Override
    protected Panel createTopPanel() {
        return new NBTEditorScreen.CustomTopPanel();
    }

    private void setSelected(@NotNull NBTEditorScreen.ButtonNBT newSelected) {
        NBTEditorScreen.ButtonNBT prevSelected = this.selected;
        this.selected = newSelected;
        if (prevSelected != null) {
            prevSelected.updateTitle();
        }
        this.selected.updateTitle();
    }

    @Override
    public boolean onInit() {
        return this.setSizeProportional(0.75F, 0.9F);
    }

    @Override
    public void onClosed() {
        super.onClosed();
        this.callback.handle(this.accepted, this.buttonNBTRoot.map);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public boolean keyPressed(Key key) {
        if (super.keyPressed(key)) {
            return true;
        } else if ((key.is(257) || key.is(335)) && key.modifiers.shift()) {
            this.doAccept();
            return true;
        } else {
            if (key.is(334) || key.is(61)) {
                this.collapseAll(false);
            } else if (key.is(45) || key.is(333)) {
                this.collapseAll(true);
            } else if (key.is(67) && key.modifiers.control()) {
                this.copyToClipboard();
            }
            return false;
        }
    }

    private void copyToClipboard() {
        setClipboardString(this.selected.toNBT().toString());
        SimpleToast.info(Component.translatable("ftblibrary.gui.nbt_copied"), Component.literal(" "));
    }

    private NBTEditorScreen.ButtonNBT makeNBTButton(NBTEditorScreen.ButtonNBTCollection parent, String key) {
        Tag nbt = parent.getTag(key);
        return (NBTEditorScreen.ButtonNBT) (switch(nbt.getId()) {
            case 7 ->
                new NBTEditorScreen.ButtonNBTByteArray(this.mainPanel, parent, key, (ByteArrayTag) nbt);
            default ->
                new NBTEditorScreen.ButtonBasicTag(this.mainPanel, parent, key, nbt);
            case 9 ->
                new NBTEditorScreen.ButtonNBTList(this.mainPanel, parent, key, (ListTag) nbt);
            case 10 ->
                new NBTEditorScreen.ButtonNBTMap(this.mainPanel, parent, key, (CompoundTag) nbt);
            case 11 ->
                new NBTEditorScreen.ButtonNBTIntArray(this.mainPanel, parent, key, (IntArrayTag) nbt);
        });
    }

    public SimpleButton newTag(Panel panel, String title, Icon icon, Supplier<Tag> supplier) {
        return new SimpleButton(panel, Component.literal(title), icon, (btn, mb) -> {
            if (this.selected instanceof NBTEditorScreen.ButtonNBTMap) {
                StringConfig value = new StringConfig(Pattern.compile("^.+$"));
                EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this, value, accepted -> {
                    if (accepted && !value.getValue().isEmpty()) {
                        ((NBTEditorScreen.ButtonNBTCollection) this.selected).setTag(value.getValue(), (Tag) supplier.get());
                        this.selected.updateChildren(false);
                        this.mainPanel.refreshWidgets();
                    }
                    this.openGui();
                });
                overlay.setPos(btn.posX, btn.posY + btn.height + 4);
                this.getGui().pushModalPanel(overlay);
            } else if (this.selected instanceof NBTEditorScreen.ButtonNBTCollection) {
                ((NBTEditorScreen.ButtonNBTCollection) this.selected).setTag("-1", (Tag) supplier.get());
                this.selected.updateChildren(false);
                this.mainPanel.refreshWidgets();
            }
        }) {

            @Override
            public void drawBackground(GuiGraphics stack, Theme theme, int x, int y, int w, int h) {
                IconWithBorder.BUTTON_ROUND_GRAY.draw(stack, x, y, w, h);
            }
        };
    }

    private static Icon getIcon(String name) {
        return Icon.getIcon("ftblibrary:textures/icons/nbt/" + name + ".png");
    }

    public class ButtonBasicTag extends NBTEditorScreen.ButtonNBT {

        private Tag nbt;

        public ButtonBasicTag(Panel panel, NBTEditorScreen.ButtonNBTCollection parent, String key, Tag nbt) {
            super(panel, parent, key);
            this.nbt = nbt;
            switch(this.nbt.getId()) {
                case 1:
                    this.setIcon(NBTEditorScreen.NBT_BYTE);
                    break;
                case 2:
                    this.setIcon(NBTEditorScreen.NBT_SHORT);
                    break;
                case 3:
                    this.setIcon(NBTEditorScreen.NBT_INT);
                    break;
                case 4:
                    this.setIcon(NBTEditorScreen.NBT_LONG);
                    break;
                case 5:
                    this.setIcon(NBTEditorScreen.NBT_FLOAT);
                    break;
                case 6:
                case 99:
                    this.setIcon(NBTEditorScreen.NBT_DOUBLE);
                    break;
                case 8:
                    this.setIcon(NBTEditorScreen.NBT_STRING);
            }
            this.parent.setTag(this.key, this.nbt);
            this.updateTitle();
        }

        @Override
        public void updateTitle() {
            // $VF: Couldn't be decompiled
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.RuntimeException: invalid constant type: Ljava/lang/Object; with value
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ConstExprent.toJava(ConstExprent.java:356)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.SwitchExprent.toJava(SwitchExprent.java:151)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1018)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.AssignmentExprent.toJava(AssignmentExprent.java:154)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:895)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:90)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
            //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
            //
            // Bytecode:
            // 00: aload 0
            // 01: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.nbt Lnet/minecraft/nbt/Tag;
            // 04: invokeinterface net/minecraft/nbt/Tag.m_7060_ ()B 1
            // 09: lookupswitch 135 8 1 75 2 75 3 75 4 91 5 107 6 107 8 123 99 107
            // 54: aload 0
            // 55: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.nbt Lnet/minecraft/nbt/Tag;
            // 58: checkcast net/minecraft/nbt/NumericTag
            // 5b: invokevirtual net/minecraft/nbt/NumericTag.m_7047_ ()I
            // 5e: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
            // 61: goto 92
            // 64: aload 0
            // 65: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.nbt Lnet/minecraft/nbt/Tag;
            // 68: checkcast net/minecraft/nbt/NumericTag
            // 6b: invokevirtual net/minecraft/nbt/NumericTag.m_7046_ ()J
            // 6e: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
            // 71: goto 92
            // 74: aload 0
            // 75: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.nbt Lnet/minecraft/nbt/Tag;
            // 78: checkcast net/minecraft/nbt/NumericTag
            // 7b: invokevirtual net/minecraft/nbt/NumericTag.m_7061_ ()D
            // 7e: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
            // 81: goto 92
            // 84: aload 0
            // 85: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.nbt Lnet/minecraft/nbt/Tag;
            // 88: invokeinterface net/minecraft/nbt/Tag.m_7916_ ()Ljava/lang/String; 1
            // 8d: goto 92
            // 90: ldc ""
            // 92: astore 1
            // 93: aload 0
            // 94: invokevirtual dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.isSelected ()Z
            // 97: ifeq a0
            // 9a: getstatic net/minecraft/ChatFormatting.WHITE Lnet/minecraft/ChatFormatting;
            // 9d: goto a3
            // a0: getstatic net/minecraft/ChatFormatting.GRAY Lnet/minecraft/ChatFormatting;
            // a3: astore 2
            // a4: aload 0
            // a5: invokevirtual dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.isSelected ()Z
            // a8: ifeq b1
            // ab: getstatic net/minecraft/ChatFormatting.AQUA Lnet/minecraft/ChatFormatting;
            // ae: goto b4
            // b1: getstatic net/minecraft/ChatFormatting.DARK_AQUA Lnet/minecraft/ChatFormatting;
            // b4: astore 3
            // b5: aload 0
            // b6: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.key Ljava/lang/String;
            // b9: invokestatic net/minecraft/network/chat/Component.m_237113_ (Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;
            // bc: aload 2
            // bd: invokevirtual net/minecraft/network/chat/MutableComponent.m_130940_ (Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;
            // c0: ldc ": "
            // c2: invokevirtual net/minecraft/network/chat/MutableComponent.m_130946_ (Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;
            // c5: aload 1
            // c6: invokevirtual java/lang/Object.toString ()Ljava/lang/String;
            // c9: invokestatic net/minecraft/network/chat/Component.m_237113_ (Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;
            // cc: aload 3
            // cd: invokevirtual net/minecraft/network/chat/MutableComponent.m_130940_ (Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;
            // d0: invokevirtual net/minecraft/network/chat/MutableComponent.m_7220_ (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
            // d3: astore 4
            // d5: aload 0
            // d6: aload 4
            // d8: invokevirtual dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.setTitle (Lnet/minecraft/network/chat/Component;)Ldev/ftb/mods/ftblibrary/ui/Button;
            // db: pop
            // dc: aload 0
            // dd: bipush 12
            // df: aload 0
            // e0: getfield dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.this$0 Ldev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen;
            // e3: invokevirtual dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen.getTheme ()Ldev/ftb/mods/ftblibrary/ui/Theme;
            // e6: aload 4
            // e8: invokevirtual dev/ftb/mods/ftblibrary/ui/Theme.getStringWidth (Lnet/minecraft/network/chat/FormattedText;)I
            // eb: iadd
            // ec: invokevirtual dev/ftb/mods/ftblibrary/nbtedit/NBTEditorScreen$ButtonBasicTag.setWidth (I)V
            // ef: return
        }

        @Override
        public void onClicked(MouseButton button) {
            NBTEditorScreen.this.setSelected(this);
            NBTEditorScreen.this.panelTopLeft.refreshWidgets();
            if (button.isRight()) {
                this.edit();
            }
        }

        @Override
        public boolean mouseDoubleClicked(MouseButton button) {
            if (this.isMouseOver()) {
                this.edit();
                return true;
            } else {
                return false;
            }
        }

        public void edit() {
            switch(this.nbt.getId()) {
                case 1:
                case 2:
                case 3:
                    this.openEditOverlay(new IntConfig(Integer.MIN_VALUE, Integer.MAX_VALUE), ((NumericTag) this.nbt).getAsInt());
                    break;
                case 4:
                    this.openEditOverlay(new LongConfig(Long.MIN_VALUE, Long.MAX_VALUE), ((NumericTag) this.nbt).getAsLong());
                    break;
                case 5:
                case 6:
                case 99:
                    this.openEditOverlay(new DoubleConfig(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), ((NumericTag) this.nbt).getAsDouble());
                    break;
                case 8:
                    this.openEditOverlay(new StringConfig(), this.nbt.getAsString());
            }
        }

        private <T> void openEditOverlay(ConfigFromString<T> config, T val) {
            config.setValue(val);
            this.getGui().pushModalPanel(new EditStringConfigOverlay<>(this.getGui(), config, accepted -> this.onCallback(config, accepted)).atMousePosition());
        }

        public void onCallback(ConfigValue<?> value, boolean accepted) {
            if (accepted) {
                switch(this.nbt.getId()) {
                    case 1:
                    case 2:
                    case 3:
                        this.nbt = IntTag.valueOf(((Number) value.getValue()).intValue());
                        break;
                    case 4:
                        this.nbt = LongTag.valueOf(((Number) value.getValue()).longValue());
                        break;
                    case 5:
                    case 6:
                    case 99:
                        this.nbt = DoubleTag.valueOf(((Number) value.getValue()).doubleValue());
                        break;
                    case 8:
                        this.nbt = StringTag.valueOf(value.getValue().toString());
                }
                this.parent.setTag(this.key, this.nbt);
                this.updateTitle();
            }
            NBTEditorScreen.this.openGui();
        }

        @Override
        public CompoundTag toNBT() {
            return Util.make(new CompoundTag(), t -> t.put(this.key, this.nbt));
        }
    }

    public abstract class ButtonNBT extends Button {

        protected final NBTEditorScreen.ButtonNBTCollection parent;

        protected String key;

        public ButtonNBT(Panel panel, @Nullable NBTEditorScreen.ButtonNBTCollection parent, String key) {
            super(panel);
            this.parent = parent;
            this.key = key;
            this.setPosAndSize(parent == null ? 0 : parent.posX + 10, 0, 10, 10);
            this.setTitle(Component.literal(this.key));
        }

        public abstract CompoundTag toNBT();

        public void updateChildren(boolean first) {
        }

        public void addChildren() {
        }

        public boolean canCreateNew(int id) {
            return false;
        }

        @Override
        public void addMouseOverText(TooltipList list) {
        }

        @Override
        public void draw(GuiGraphics pose, Theme theme, int x, int y, int w, int h) {
            if (this.isSelected()) {
                Color4I.WHITE.withAlpha(64).draw(pose, x, y, w, h);
            }
            IconWithBorder.BUTTON_ROUND_GRAY.draw(pose, x + 1, y + 1, 8, 8);
            this.drawIcon(pose, theme, x + 1, y + 1, 8, 8);
            theme.drawString(pose, this.getTitle(), x + 11, y + 1);
        }

        public boolean isSelected() {
            return this == NBTEditorScreen.this.selected;
        }

        public void updateTitle() {
        }
    }

    public class ButtonNBTByteArray extends NBTEditorScreen.ButtonNBTCollection {

        private final ByteArrayList list;

        public ButtonNBTByteArray(Panel panel, NBTEditorScreen.ButtonNBTCollection p, String key, ByteArrayTag l) {
            super(panel, p, key, NBTEditorScreen.NBT_BYTE_ARRAY_OPEN, NBTEditorScreen.NBT_BYTE_ARRAY_CLOSED);
            this.list = new ByteArrayList(l.getAsByteArray());
        }

        @Override
        public void updateChildren(boolean first) {
            this.children.clear();
            for (int i = 0; i < this.list.size(); i++) {
                String s = Integer.toString(i);
                NBTEditorScreen.ButtonNBT nbt = NBTEditorScreen.this.makeNBTButton(this, s);
                this.children.put(s, nbt);
                nbt.updateChildren(first);
            }
        }

        @Override
        public Tag getTag(String key) {
            return ByteTag.valueOf(this.list.getByte(Integer.parseInt(key)));
        }

        @Override
        public void setTag(String key, @Nullable Tag base) {
            int id = Integer.parseInt(key);
            if (id == -1) {
                if (base != null) {
                    this.list.add(((NumericTag) base).getAsByte());
                }
            } else if (base != null) {
                this.list.set(id, ((NumericTag) base).getAsByte());
            } else {
                this.list.removeByte(id);
            }
            if (this.parent != null) {
                this.parent.setTag(this.key, new ByteArrayTag(this.list.toByteArray()));
            }
        }

        @Override
        public boolean canCreateNew(int id) {
            return id == 1;
        }

        @Override
        public CompoundTag toNBT() {
            return Util.make(new CompoundTag(), t -> t.put(this.key, new ByteArrayTag(this.list.toByteArray())));
        }
    }

    public abstract class ButtonNBTCollection extends NBTEditorScreen.ButtonNBT {

        public boolean collapsed;

        public final Map<String, NBTEditorScreen.ButtonNBT> children;

        public final Icon iconOpen;

        public final Icon iconClosed;

        public ButtonNBTCollection(Panel panel, @Nullable NBTEditorScreen.ButtonNBTCollection parent, String key, Icon open, Icon closed) {
            super(panel, parent, key);
            this.iconOpen = open;
            this.iconClosed = closed;
            this.setCollapsed(false);
            this.setWidth(this.width + 2 + NBTEditorScreen.this.getTheme().getStringWidth(key));
            this.children = new LinkedHashMap();
            this.updateTitle();
        }

        @Override
        public void addChildren() {
            if (!this.collapsed) {
                for (NBTEditorScreen.ButtonNBT button : this.children.values()) {
                    NBTEditorScreen.this.mainPanel.add(button);
                    button.addChildren();
                }
            }
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.getMouseX() <= this.getX() + this.height) {
                this.setCollapsed(!this.collapsed);
                NBTEditorScreen.this.mainPanel.refreshWidgets();
            } else {
                NBTEditorScreen.this.setSelected(this);
                NBTEditorScreen.this.panelTopLeft.refreshWidgets();
            }
        }

        @Override
        public boolean mouseDoubleClicked(MouseButton button) {
            if (this.isMouseOver()) {
                this.setCollapsed(!this.collapsed);
                NBTEditorScreen.this.mainPanel.refreshWidgets();
                return true;
            } else {
                return false;
            }
        }

        public void setCollapsed(boolean c) {
            this.collapsed = c;
            this.setIcon(this.collapsed ? this.iconClosed : this.iconOpen);
        }

        public void setCollapsedTree(boolean c) {
            this.setCollapsed(c);
            for (NBTEditorScreen.ButtonNBT button : this.children.values()) {
                if (button instanceof NBTEditorScreen.ButtonNBTCollection collection) {
                    collection.setCollapsedTree(c);
                }
            }
        }

        public abstract Tag getTag(String var1);

        public abstract void setTag(String var1, @Nullable Tag var2);
    }

    public class ButtonNBTIntArray extends NBTEditorScreen.ButtonNBTCollection {

        private final IntArrayList list;

        public ButtonNBTIntArray(Panel panel, NBTEditorScreen.ButtonNBTCollection parent, String key, IntArrayTag l) {
            super(panel, parent, key, NBTEditorScreen.NBT_INT_ARRAY_OPEN, NBTEditorScreen.NBT_INT_ARRAY_CLOSED);
            this.list = new IntArrayList(l.getAsIntArray());
        }

        @Override
        public void updateChildren(boolean first) {
            this.children.clear();
            for (int i = 0; i < this.list.size(); i++) {
                String s = Integer.toString(i);
                NBTEditorScreen.ButtonNBT nbt = NBTEditorScreen.this.makeNBTButton(this, s);
                this.children.put(s, nbt);
                nbt.updateChildren(first);
            }
        }

        @Override
        public Tag getTag(String key) {
            return IntTag.valueOf(this.list.getInt(Integer.parseInt(key)));
        }

        @Override
        public void setTag(String key, @Nullable Tag base) {
            int id = Integer.parseInt(key);
            if (id == -1) {
                if (base != null) {
                    this.list.add(((NumericTag) base).getAsInt());
                }
            } else if (base != null) {
                this.list.set(id, ((NumericTag) base).getAsInt());
            } else {
                this.list.rem(id);
            }
            if (this.parent != null) {
                this.parent.setTag(this.key, new IntArrayTag(this.list.toIntArray()));
            }
        }

        @Override
        public boolean canCreateNew(int id) {
            return id == 3;
        }

        @Override
        public CompoundTag toNBT() {
            return Util.make(new CompoundTag(), t -> t.put(this.key, new IntArrayTag(this.list.toIntArray())));
        }
    }

    public class ButtonNBTList extends NBTEditorScreen.ButtonNBTCollection {

        private final ListTag list;

        public ButtonNBTList(Panel panel, NBTEditorScreen.ButtonNBTCollection p, String key, ListTag l) {
            super(panel, p, key, NBTEditorScreen.NBT_LIST_OPEN, NBTEditorScreen.NBT_LIST_CLOSED);
            this.list = l;
        }

        @Override
        public void updateTitle() {
            this.setTitle(Component.literal(this.key).withStyle(this.isSelected() ? ChatFormatting.YELLOW : ChatFormatting.GOLD));
        }

        @Override
        public void updateChildren(boolean first) {
            this.children.clear();
            for (int i = 0; i < this.list.size(); i++) {
                String s = Integer.toString(i);
                NBTEditorScreen.ButtonNBT nbt = NBTEditorScreen.this.makeNBTButton(this, s);
                this.children.put(s, nbt);
                nbt.updateChildren(first);
            }
        }

        @Override
        public Tag getTag(String key) {
            return this.list.get(Integer.parseInt(key));
        }

        @Override
        public void setTag(String key, @Nullable Tag base) {
            int id = Integer.parseInt(key);
            if (id == -1) {
                if (base != null) {
                    this.list.add(base);
                }
            } else if (base != null) {
                this.list.set(id, base);
            } else {
                this.list.remove(id);
            }
            if (this.parent != null) {
                this.parent.setTag(this.key, this.list);
            }
        }

        @Override
        public boolean canCreateNew(int id) {
            return this.list.isEmpty() || this.list.getElementType() == id;
        }

        @Override
        public CompoundTag toNBT() {
            return Util.make(new CompoundTag(), t -> t.put(this.key, this.list));
        }
    }

    public class ButtonNBTMap extends NBTEditorScreen.ButtonNBTCollection {

        private final CompoundTag map;

        private Icon hoverIcon = Icon.empty();

        public ButtonNBTMap(Panel panel, @Nullable NBTEditorScreen.ButtonNBTCollection parent, String key, CompoundTag map) {
            super(panel, parent, key, NBTEditorScreen.NBT_MAP_OPEN, NBTEditorScreen.NBT_MAP_CLOSED);
            this.map = map;
        }

        @Override
        public void updateTitle() {
            this.setTitle(Component.literal(this.key).withStyle(this.isSelected() ? ChatFormatting.GREEN : ChatFormatting.DARK_GREEN));
        }

        @Override
        public void updateChildren(boolean first) {
            this.children.clear();
            this.map.getAllKeys().stream().sorted(StringUtils.IGNORE_CASE_COMPARATOR).forEach(key -> {
                NBTEditorScreen.ButtonNBT nbt = NBTEditorScreen.this.makeNBTButton(this, key);
                this.children.put(key, nbt);
                nbt.updateChildren(first);
            });
            this.updateHoverIcon();
            if (first && !this.hoverIcon.isEmpty()) {
                this.setCollapsed(true);
            }
        }

        private void updateHoverIcon() {
            this.hoverIcon = Icon.empty();
            if (this.map.contains("id", 8) && this.map.contains("Count", 99)) {
                ItemStack stack = ItemStack.of(this.map);
                if (!stack.isEmpty()) {
                    this.hoverIcon = ItemIcon.getItemIcon(stack);
                }
            }
            this.setWidth(12 + NBTEditorScreen.this.getTheme().getStringWidth(this.getTitle()) + (this.hoverIcon.isEmpty() ? 0 : 10));
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (this == NBTEditorScreen.this.buttonNBTRoot) {
                ListTag infoList = NBTEditorScreen.this.info.getList("text", 8);
                if (!infoList.isEmpty()) {
                    list.add(Component.translatable("gui.info").append(":"));
                    for (int i = 0; i < infoList.size(); i++) {
                        MutableComponent component = Component.Serializer.fromJson(infoList.getString(i));
                        if (component != null) {
                            list.add(component);
                        }
                    }
                }
            }
        }

        @Override
        public void draw(GuiGraphics pose, Theme theme, int x, int y, int w, int h) {
            super.draw(pose, theme, x, y, w, h);
            if (!this.hoverIcon.isEmpty()) {
                this.hoverIcon.draw(pose, x + 12 + theme.getStringWidth(this.getTitle()), y + 1, 8, 8);
            }
        }

        @Override
        public Optional<PositionedIngredient> getIngredientUnderMouse() {
            return PositionedIngredient.of(this.hoverIcon.getIngredient(), this, true);
        }

        @Override
        public Tag getTag(String key) {
            return this.map.get(key);
        }

        @Override
        public void setTag(String key, @Nullable Tag base) {
            if (base != null) {
                this.map.put(key, base);
            } else {
                this.map.remove(key);
            }
            this.updateHoverIcon();
            if (this.parent != null) {
                this.parent.setTag(this.key, this.map);
            }
        }

        @Override
        public boolean canCreateNew(int id) {
            return true;
        }

        @Override
        public CompoundTag toNBT() {
            CompoundTag nbt = this.map.copy();
            if (this == NBTEditorScreen.this.buttonNBTRoot) {
                ListTag infoList1 = new ListTag();
                ListTag infoList0 = NBTEditorScreen.this.info.getList("text", 8);
                if (!infoList0.isEmpty()) {
                    for (int i = 0; i < infoList0.size(); i++) {
                        MutableComponent component = Component.Serializer.fromJson(infoList0.getString(i));
                        if (component != null) {
                            infoList1.add(StringTag.valueOf(component.getString()));
                        }
                    }
                    nbt.put("_", infoList1);
                }
            }
            return nbt;
        }
    }

    private class CustomTopPanel extends AbstractThreePanelScreen<NBTEditorScreen.NBTPanel>.TopPanel {

        @Override
        public void addWidgets() {
            this.add(NBTEditorScreen.this.panelTopLeft);
            this.add(NBTEditorScreen.this.panelTopRight);
        }

        @Override
        public void alignWidgets() {
            NBTEditorScreen.this.panelTopLeft.setPosAndSize(0, 2, NBTEditorScreen.this.panelTopLeft.width, 20);
            NBTEditorScreen.this.panelTopRight.setPosAndSize(this.width - NBTEditorScreen.this.panelTopRight.width, 2, 0, 20);
            NBTEditorScreen.this.panelTopRight.alignWidgets();
        }
    }

    public interface NBTCallback {

        void handle(boolean var1, CompoundTag var2);
    }

    protected class NBTPanel extends Panel {

        public NBTPanel() {
            super(NBTEditorScreen.this);
        }

        @Override
        public void addWidgets() {
            this.add(NBTEditorScreen.this.buttonNBTRoot);
            NBTEditorScreen.this.buttonNBTRoot.addChildren();
        }

        @Override
        public void alignWidgets() {
            this.align(WidgetLayout.VERTICAL);
        }
    }

    private class TopLeftPanel extends Panel {

        public TopLeftPanel() {
            super(NBTEditorScreen.this.topPanel);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
        }

        @Override
        public void addWidgets() {
            this.add(new SimpleButton(this, Component.translatable("selectServer.delete"), NBTEditorScreen.this.selected == NBTEditorScreen.this.buttonNBTRoot ? Icons.BIN.combineWith(Color4I.rgba(-1071636448)) : Icons.BIN, (widget, button) -> this.deleteTag()));
            boolean canRename = NBTEditorScreen.this.selected.parent instanceof NBTEditorScreen.ButtonNBTMap;
            Icon renameIcon = Icons.NOTES;
            this.add(new SimpleButton(this, Component.translatable("ftblibrary.gui.edit_tag_name"), canRename ? renameIcon : renameIcon.combineWith(Color4I.rgba(-1071636448)), (btn, mb) -> {
                if (canRename) {
                    this.getGui().pushModalPanel(this.makeRenameOverlay(btn));
                }
            }));
            if (NBTEditorScreen.this.selected instanceof NBTEditorScreen.ButtonBasicTag) {
                this.add(new SimpleButton(this, Component.translatable("ftblibrary.gui.edit_tag_value"), Icons.FEATHER, (widget, button) -> ((NBTEditorScreen.ButtonBasicTag) NBTEditorScreen.this.selected).edit()));
            }
            List<Widget> addBtns = this.buildAddButtons();
            if (!addBtns.isEmpty()) {
                TextField addLabel = new TextField(this).setText(Component.literal("   ").append(Component.translatable("gui.add"))).addFlags(32);
                this.add(addLabel);
                this.addAll(addBtns);
            }
        }

        private void deleteTag() {
            if (NBTEditorScreen.this.selected != NBTEditorScreen.this.buttonNBTRoot && NBTEditorScreen.this.selected.parent != null) {
                NBTEditorScreen.this.selected.parent.setTag(NBTEditorScreen.this.selected.key, null);
                NBTEditorScreen.this.selected.parent.updateChildren(false);
                NBTEditorScreen.this.setSelected(NBTEditorScreen.this.selected.parent);
                NBTEditorScreen.this.mainPanel.refreshWidgets();
                NBTEditorScreen.this.panelTopLeft.refreshWidgets();
            }
        }

        @NotNull
        private List<Widget> buildAddButtons() {
            List<Widget> addBtns = new ArrayList();
            if (NBTEditorScreen.this.selected.canCreateNew(10)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Compound", NBTEditorScreen.NBT_MAP, CompoundTag::new));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(9)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "List", NBTEditorScreen.NBT_LIST, ListTag::new));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(8)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "String", NBTEditorScreen.NBT_STRING, () -> StringTag.valueOf("")));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(1)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Byte", NBTEditorScreen.NBT_BYTE, () -> ByteTag.valueOf((byte) 0)));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(2)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Short", NBTEditorScreen.NBT_SHORT, () -> ShortTag.valueOf((short) 0)));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(3)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Int", NBTEditorScreen.NBT_INT, () -> IntTag.valueOf(0)));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(4)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Long", NBTEditorScreen.NBT_LONG, () -> LongTag.valueOf(0L)));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(5)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Float", NBTEditorScreen.NBT_FLOAT, () -> FloatTag.valueOf(0.0F)));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(6)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Double", NBTEditorScreen.NBT_DOUBLE, () -> DoubleTag.valueOf(0.0)));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(7)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Byte Array", NBTEditorScreen.NBT_BYTE_ARRAY, () -> new ByteArrayTag(new byte[0])));
            }
            if (NBTEditorScreen.this.selected.canCreateNew(11)) {
                addBtns.add(NBTEditorScreen.this.newTag(this, "Int Array", NBTEditorScreen.NBT_INT_ARRAY, () -> new IntArrayTag(new int[0])));
            }
            return addBtns;
        }

        @NotNull
        private EditStringConfigOverlay<String> makeRenameOverlay(SimpleButton button) {
            StringConfig value = new StringConfig();
            if (NBTEditorScreen.this.selected != null) {
                value.setValue(NBTEditorScreen.this.selected.key);
            }
            EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this, value, accepted -> {
                if (accepted && !value.getValue().isEmpty() && NBTEditorScreen.this.selected.parent != null) {
                    NBTEditorScreen.ButtonNBTCollection parent = NBTEditorScreen.this.selected.parent;
                    Tag nbt = parent.getTag(NBTEditorScreen.this.selected.key);
                    parent.setTag(NBTEditorScreen.this.selected.key, null);
                    parent.setTag(value.getValue(), nbt);
                    parent.updateChildren(false);
                    NBTEditorScreen.this.setSelected((NBTEditorScreen.ButtonNBT) parent.children.get(value.getValue()));
                    NBTEditorScreen.this.mainPanel.refreshWidgets();
                }
                this.getGui().openGui();
            }, Component.literal("New name"));
            overlay.setPos(button.posX, button.posY + button.height + 4);
            overlay.setExtraZlevel(300);
            return overlay;
        }

        @Override
        public void alignWidgets() {
            this.setWidth(this.align(new WidgetLayout.Horizontal(2, 4, 2)));
            this.widgets.forEach(w -> w.setY((20 - w.height) / 2 - 2));
        }
    }

    private class TopRightPanel extends Panel {

        public TopRightPanel() {
            super(NBTEditorScreen.this.topPanel);
        }

        @Override
        public void addWidgets() {
            this.add(new SimpleButton(this, List.of(Component.translatable("gui.copy"), TextComponentUtils.hotkeyTooltip("Ctrl + C")), ItemIcon.getItemIcon(Items.PAPER), (widget, button) -> NBTEditorScreen.this.copyToClipboard()));
            this.add(new SimpleButton(this, List.of(Component.translatable("gui.collapse_all"), TextComponentUtils.hotkeyTooltip("-")), Icons.DOWN, (widget, button) -> NBTEditorScreen.this.collapseAll(true)));
            this.add(new SimpleButton(this, List.of(Component.translatable("gui.expand_all"), TextComponentUtils.hotkeyTooltip("="), TextComponentUtils.hotkeyTooltip("+")), Icons.UP, (widget, button) -> NBTEditorScreen.this.collapseAll(false)));
        }

        @Override
        public void alignWidgets() {
            this.setWidth(this.align(new WidgetLayout.Horizontal(2, 4, 2)));
        }
    }
}