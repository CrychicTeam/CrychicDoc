package de.keksuccino.konkrete.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.config.Config;
import de.keksuccino.konkrete.config.ConfigEntry;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.ExtendedEditBox;
import de.keksuccino.konkrete.gui.content.scrollarea.ScrollArea;
import de.keksuccino.konkrete.gui.content.scrollarea.ScrollAreaEntry;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.rendering.RenderUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    protected Config config;

    protected ScrollArea configList;

    protected Screen parent;

    protected String title;

    protected AdvancedButton doneBtn;

    protected String activeDescription = null;

    protected Map<String, String> descriptions = new HashMap();

    protected static final Color ENTRY_BACKGROUND_COLOR = new Color(92, 92, 92);

    protected static final Color SCREEN_BACKGROUND_COLOR = new Color(54, 54, 54);

    protected static final Color HEADER_FOOTER_COLOR = new Color(33, 33, 33);

    public ConfigScreen(Config config, String title, Screen parent) {
        super(Component.literal(""));
        this.config = config;
        this.parent = parent;
        this.title = title;
        this.configList = new ScrollArea(0, 50, 300, 0);
        this.configList.backgroundColor = ENTRY_BACKGROUND_COLOR;
        for (String s : this.config.getCategorys()) {
            this.configList.addEntry(new ConfigScreen.CategoryConfigScrollAreaEntry(this.configList, s));
            for (ConfigEntry e : this.config.getEntrysForCategory(s)) {
                if (e.getType() == ConfigEntry.EntryType.STRING) {
                    this.configList.addEntry(new ConfigScreen.StringConfigScrollAreaEntry(this.configList, e));
                }
                if (e.getType() == ConfigEntry.EntryType.INTEGER) {
                    this.configList.addEntry(new ConfigScreen.IntegerConfigScrollAreaEntry(this.configList, e));
                }
                if (e.getType() == ConfigEntry.EntryType.DOUBLE) {
                    this.configList.addEntry(new ConfigScreen.DoubleConfigScrollAreaEntry(this.configList, e));
                }
                if (e.getType() == ConfigEntry.EntryType.FLOAT) {
                    this.configList.addEntry(new ConfigScreen.FloatConfigScrollAreaEntry(this.configList, e));
                }
                if (e.getType() == ConfigEntry.EntryType.LONG) {
                    this.configList.addEntry(new ConfigScreen.LongConfigScrollAreaEntry(this.configList, e));
                }
                if (e.getType() == ConfigEntry.EntryType.BOOLEAN) {
                    this.configList.addEntry(new ConfigScreen.BooleanConfigScrollAreaEntry(this.configList, e));
                }
            }
        }
        this.doneBtn = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.done"), true, press -> Minecraft.getInstance().setScreen(this.parent));
        colorizeButton(this.doneBtn);
        this.doneBtn.ignoreBlockedInput = true;
        this.doneBtn.ignoreLeftMouseDownClickBlock = true;
    }

    @Override
    protected void init() {
        this.configList.x = this.f_96543_ / 2 - 150;
        this.configList.height = this.f_96544_ - 100;
    }

    @Override
    public void removed() {
        this.saveConfig();
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, SCREEN_BACKGROUND_COLOR.getRGB());
        this.configList.render(graphics);
        graphics.fill(0, 0, this.f_96543_, 50, HEADER_FOOTER_COLOR.getRGB());
        if (this.title != null) {
            graphics.drawString(this.f_96547_, this.title, this.f_96543_ / 2 - this.f_96547_.width(this.title) / 2, 20, Color.WHITE.getRGB());
        }
        graphics.fill(0, this.f_96544_ - 50, this.f_96543_, this.f_96544_, HEADER_FOOTER_COLOR.getRGB());
        this.doneBtn.setX(this.f_96543_ / 2 - this.doneBtn.getWidth() / 2);
        this.doneBtn.setY(this.f_96544_ - 35);
        this.doneBtn.m_88315_(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (e instanceof ConfigScreen.ConfigScrollAreaEntry && e.isHoveredOrFocused()) {
                String name = ((ConfigScreen.ConfigScrollAreaEntry) e).configEntry.getName();
                if (this.descriptions.containsKey(name)) {
                    if (!ConfigScreen.ConfigScrollAreaEntry.isHeaderFooterHovered()) {
                        renderDescription(graphics, (String) this.descriptions.get(name), mouseX, mouseY);
                    }
                    break;
                }
            }
        }
    }

    public void setValueDescription(String valueName, String desc) {
        this.descriptions.put(valueName, desc);
    }

    public void setCategoryDisplayName(String categoryName, String displayName) {
        ConfigScreen.CategoryConfigScrollAreaEntry e = this.getCategoryEntryByName(categoryName);
        if (e != null) {
            e.displayName = displayName;
        }
    }

    public void setValueDisplayName(String valueName, String displayName) {
        ConfigScreen.ConfigScrollAreaEntry e = this.getEntryByValueName(valueName);
        if (e != null) {
            e.displayName = displayName;
        }
    }

    protected ConfigScreen.ConfigScrollAreaEntry getEntryByValueName(String valueName) {
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (e instanceof ConfigScreen.ConfigScrollAreaEntry && ((ConfigScreen.ConfigScrollAreaEntry) e).configEntry.getName().equals(valueName)) {
                return (ConfigScreen.ConfigScrollAreaEntry) e;
            }
        }
        return null;
    }

    protected ConfigScreen.CategoryConfigScrollAreaEntry getCategoryEntryByName(String categoryName) {
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (e instanceof ConfigScreen.CategoryConfigScrollAreaEntry && ((ConfigScreen.CategoryConfigScrollAreaEntry) e).category.equals(categoryName)) {
                return (ConfigScreen.CategoryConfigScrollAreaEntry) e;
            }
        }
        return null;
    }

    protected void saveConfig() {
        for (ScrollAreaEntry e : this.configList.getEntries()) {
            if (e instanceof ConfigScreen.ConfigScrollAreaEntry) {
                ((ConfigScreen.ConfigScrollAreaEntry) e).onSave();
            }
        }
        this.config.syncConfig();
    }

    protected static void renderDescription(GuiGraphics graphics, String description, int mouseX, int mouseY) {
        if (description != null) {
            int width = 10;
            int height = 10;
            String[] desc = StringUtils.splitLines(description, "%n%");
            for (String s : desc) {
                int i = Minecraft.getInstance().font.width(s) + 10;
                if (i > width) {
                    width = i;
                }
                height += 10;
            }
            mouseX += 5;
            mouseY += 5;
            if (Minecraft.getInstance().screen.width < mouseX + width) {
                mouseX -= width + 10;
            }
            if (Minecraft.getInstance().screen.height < mouseY + height) {
                mouseY -= height + 10;
            }
            RenderUtils.setZLevelPre(graphics.pose(), 600);
            renderDescriptionBackground(graphics, mouseX, mouseY, width, height);
            RenderSystem.enableBlend();
            int i2 = 5;
            for (String s : desc) {
                graphics.drawString(Minecraft.getInstance().font, s, mouseX + 5, mouseY + i2, Color.WHITE.getRGB());
                i2 += 10;
            }
            RenderUtils.setZLevelPost(graphics.pose());
            RenderSystem.disableBlend();
        }
    }

    protected static void renderDescriptionBackground(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, new Color(26, 26, 26, 250).getRGB());
    }

    protected static void colorizeButton(AdvancedButton b) {
        b.setBackgroundColor(new Color(100, 100, 100), new Color(130, 130, 130), new Color(180, 180, 180), new Color(199, 199, 199), 1);
    }

    protected static class BooleanConfigScrollAreaEntry extends ConfigScreen.ConfigScrollAreaEntry {

        private AdvancedButton toggleBtn;

        private boolean state = false;

        public BooleanConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent, configEntry);
            if (configEntry.getValue().equalsIgnoreCase("true")) {
                this.state = true;
            }
            this.toggleBtn = new AdvancedButton(0, 0, 102, 20, "", true, press -> {
                if (!isHeaderFooterHovered()) {
                    this.state = !this.state;
                    if (this.state) {
                        this.toggleBtn.m_93666_(Component.literal("§a" + Locals.localize("configscreen.boolean.enabled")));
                    } else {
                        this.toggleBtn.m_93666_(Component.literal("§c" + Locals.localize("configscreen.boolean.disabled")));
                    }
                }
            });
            if (this.state) {
                this.toggleBtn.m_93666_(Component.literal("§a" + Locals.localize("configscreen.boolean.enabled")));
            } else {
                this.toggleBtn.m_93666_(Component.literal("§c" + Locals.localize("configscreen.boolean.disabled")));
            }
            ConfigScreen.colorizeButton(this.toggleBtn);
        }

        @Override
        public void render(GuiGraphics graphics) {
            super.render(graphics);
            int center = this.x + this.getWidth() / 2;
            this.toggleBtn.setX(center + 9);
            this.toggleBtn.setY(this.y + 3);
            this.toggleBtn.m_88315_(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getFrameTime());
        }

        @Override
        protected void onSave() {
            this.configEntry.setValue(this.state + "");
        }
    }

    protected static class CategoryConfigScrollAreaEntry extends ScrollAreaEntry {

        protected String category;

        protected Font font;

        protected String displayName;

        public CategoryConfigScrollAreaEntry(ScrollArea parent, String category) {
            super(parent);
            this.font = Minecraft.getInstance().font;
            this.category = category;
        }

        @Override
        public void renderEntry(GuiGraphics graphics) {
            int center = this.x + this.getWidth() / 2;
            graphics.fill(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), ConfigScreen.ENTRY_BACKGROUND_COLOR.getRGB());
            if (this.displayName != null) {
                int nameWidth = this.font.width(this.displayName);
                graphics.drawString(this.font, this.displayName, center - nameWidth / 2, this.y + 10, Color.WHITE.getRGB());
            } else {
                int nameWidth = this.font.width(this.category);
                graphics.drawString(this.font, this.category, center - nameWidth / 2, this.y + 10, Color.WHITE.getRGB());
            }
        }

        @Override
        public int getHeight() {
            return 30;
        }
    }

    protected abstract static class ConfigScrollAreaEntry extends ScrollAreaEntry {

        protected ConfigEntry configEntry;

        protected Font font;

        protected String displayName;

        public ConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent);
            this.font = Minecraft.getInstance().font;
            this.configEntry = configEntry;
        }

        @Override
        public void renderEntry(GuiGraphics graphics) {
            int center = this.x + this.getWidth() / 2;
            graphics.fill(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), ConfigScreen.ENTRY_BACKGROUND_COLOR.getRGB());
            if (this.displayName != null) {
                int nameWidth = this.font.width(this.displayName);
                graphics.drawString(this.font, this.displayName, center - nameWidth - 10, this.y + 10, Color.WHITE.getRGB());
            } else {
                int nameWidth = this.font.width(this.configEntry.getName());
                graphics.drawString(this.font, this.configEntry.getName(), center - nameWidth - 10, this.y + 10, Color.WHITE.getRGB());
            }
        }

        @Override
        public int getHeight() {
            return 26;
        }

        protected abstract void onSave();

        public static boolean isHeaderFooterHovered() {
            Screen s = Minecraft.getInstance().screen;
            if (s != null) {
                int mouseX = MouseInput.getMouseX();
                int mouseY = MouseInput.getMouseY();
                int minXHeaderFooter = 0;
                int maxXHeaderFooter = s.width;
                int minYHeader = 0;
                int maxYHeader = 50;
                int minYFooter = s.height - 50;
                int maxYFooter = s.height;
                if (mouseX >= minXHeaderFooter && mouseX <= maxXHeaderFooter && mouseY >= minYHeader && mouseY <= maxYHeader) {
                    return true;
                }
                if (mouseX >= minXHeaderFooter && mouseX <= maxXHeaderFooter && mouseY >= minYFooter && mouseY <= maxYFooter) {
                    return true;
                }
            }
            return false;
        }
    }

    protected static class DoubleConfigScrollAreaEntry extends ConfigScreen.ConfigScrollAreaEntry {

        private ExtendedEditBox input = new ExtendedEditBox(this.font, 0, 0, 100, 20, Component.empty(), true);

        public DoubleConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent, configEntry);
            this.input.setCharacterFilter(CharacterFilter.getDoubleCharacterFiler());
            this.input.m_94199_(10000);
            this.input.m_94144_(configEntry.getValue());
        }

        @Override
        public void render(GuiGraphics graphics) {
            super.render(graphics);
            int center = this.x + this.getWidth() / 2;
            this.input.m_252865_(center + 10);
            this.input.m_253211_(this.y + 3);
            this.input.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getFrameTime());
        }

        @Override
        protected void onSave() {
            if (MathUtils.isDouble(this.input.m_94155_())) {
                this.configEntry.setValue(this.input.m_94155_());
            } else {
                System.out.println("################ ERROR [KONKRETE] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.m_94155_());
                System.out.println("Variable Type: DOUBLE");
                System.out.println("##################################################");
            }
        }
    }

    protected static class FloatConfigScrollAreaEntry extends ConfigScreen.ConfigScrollAreaEntry {

        private ExtendedEditBox input = new ExtendedEditBox(this.font, 0, 0, 100, 20, Component.empty(), true);

        public FloatConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent, configEntry);
            this.input.setCharacterFilter(CharacterFilter.getDoubleCharacterFiler());
            this.input.m_94199_(10000);
            this.input.m_94144_(configEntry.getValue());
        }

        @Override
        public void render(GuiGraphics graphics) {
            super.render(graphics);
            int center = this.x + this.getWidth() / 2;
            this.input.m_252865_(center + 10);
            this.input.m_253211_(this.y + 3);
            this.input.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getFrameTime());
        }

        @Override
        protected void onSave() {
            if (MathUtils.isFloat(this.input.m_94155_())) {
                this.configEntry.setValue(this.input.m_94155_());
            } else {
                System.out.println("################ ERROR [KONKRETE] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.m_94155_());
                System.out.println("Variable Type: FLOAT");
                System.out.println("##################################################");
            }
        }
    }

    protected static class IntegerConfigScrollAreaEntry extends ConfigScreen.ConfigScrollAreaEntry {

        private ExtendedEditBox input = new ExtendedEditBox(this.font, 0, 0, 100, 20, Component.empty(), true);

        public IntegerConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent, configEntry);
            this.input.setCharacterFilter(CharacterFilter.getIntegerCharacterFiler());
            this.input.m_94199_(10000);
            this.input.m_94144_(configEntry.getValue());
        }

        @Override
        public void render(GuiGraphics graphics) {
            super.render(graphics);
            int center = this.x + this.getWidth() / 2;
            this.input.m_252865_(center + 10);
            this.input.m_253211_(this.y + 3);
            this.input.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getFrameTime());
        }

        @Override
        protected void onSave() {
            if (MathUtils.isInteger(this.input.m_94155_())) {
                this.configEntry.setValue(this.input.m_94155_());
            } else {
                System.out.println("################ ERROR [KONKRETE] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.m_94155_());
                System.out.println("Variable Type: INTEGER");
                System.out.println("##################################################");
            }
        }
    }

    protected static class LongConfigScrollAreaEntry extends ConfigScreen.ConfigScrollAreaEntry {

        private ExtendedEditBox input = new ExtendedEditBox(this.font, 0, 0, 100, 20, Component.empty(), true);

        public LongConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent, configEntry);
            this.input.setCharacterFilter(CharacterFilter.getIntegerCharacterFiler());
            this.input.m_94199_(10000);
            this.input.m_94144_(configEntry.getValue());
        }

        @Override
        public void render(GuiGraphics graphics) {
            super.render(graphics);
            int center = this.x + this.getWidth() / 2;
            this.input.m_252865_(center + 10);
            this.input.m_253211_(this.y + 3);
            this.input.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getFrameTime());
        }

        @Override
        protected void onSave() {
            if (MathUtils.isLong(this.input.m_94155_())) {
                this.configEntry.setValue(this.input.m_94155_());
            } else {
                System.out.println("################ ERROR [KONKRETE] ################");
                System.out.println("Unable to save value to config! Invalid value type!");
                System.out.println("Value: " + this.input.m_94155_());
                System.out.println("Variable Type: LONG");
                System.out.println("##################################################");
            }
        }
    }

    protected static class StringConfigScrollAreaEntry extends ConfigScreen.ConfigScrollAreaEntry {

        private ExtendedEditBox input = new ExtendedEditBox(this.font, 0, 0, 100, 20, Component.empty(), true);

        public StringConfigScrollAreaEntry(ScrollArea parent, ConfigEntry configEntry) {
            super(parent, configEntry);
            this.input.m_94199_(10000);
            this.input.m_94144_(configEntry.getValue());
        }

        @Override
        public void render(GuiGraphics graphics) {
            super.render(graphics);
            int center = this.x + this.getWidth() / 2;
            this.input.m_252865_(center + 10);
            this.input.m_253211_(this.y + 3);
            this.input.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getFrameTime());
        }

        @Override
        protected void onSave() {
            this.configEntry.setValue(this.input.m_94155_());
        }
    }
}