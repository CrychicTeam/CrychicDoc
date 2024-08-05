package vazkii.patchouli.client.book;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;

public abstract class BookPage {

    public transient Minecraft mc;

    public transient Font fontRenderer;

    public transient GuiBookEntry parent;

    public transient Book book;

    protected transient BookEntry entry;

    protected transient int pageNum;

    private transient List<Button> buttons;

    public transient int left;

    public transient int top;

    public transient JsonObject sourceObject;

    protected String type;

    protected String flag;

    protected String advancement;

    protected String anchor;

    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        this.book = entry.getBook();
        this.entry = entry;
        this.pageNum = pageNum;
    }

    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        this.mc = parent.getMinecraft();
        this.book = parent.book;
        this.fontRenderer = this.mc.font;
        this.parent = parent;
        this.left = left;
        this.top = top;
        this.buttons = new ArrayList();
    }

    public boolean isPageUnlocked() {
        return this.advancement == null || this.advancement.isEmpty() || ClientAdvancements.hasDone(this.advancement);
    }

    public void onHidden(GuiBookEntry parent) {
        parent.removeDrawablesIn(this.buttons);
    }

    protected void addButton(Button button) {
        button.m_252865_(button.m_252754_() + this.parent.bookLeft + this.left);
        button.m_253211_(button.m_252907_() + this.parent.bookTop + this.top);
        this.buttons.add(button);
        this.parent.m_142416_(button);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    public boolean canAdd(Book book) {
        return this.flag == null || this.flag.isEmpty() || PatchouliConfig.getConfigFlag(this.flag);
    }

    public String i18n(String text) {
        return this.book.i18n ? I18n.get(text) : text;
    }

    public Component i18nText(String text) {
        return this.book.i18n ? Component.translatable(text) : Component.literal(text);
    }
}