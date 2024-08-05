package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.mixin.MouseHelperMixin;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.common.util.NaturalOrderComparator;

public class GuiCustomScrollNop extends Screen {

    public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/misc.png");

    protected List<String> list;

    private int listSize = 0;

    public int id;

    public int guiLeft = 0;

    public int guiTop = 0;

    private int selected = -1;

    private List<Integer> selectedList;

    private int hover;

    private int listHeight;

    private int scrollY;

    private int maxScrollY;

    private int scrollHeight;

    private boolean isScrolling;

    public boolean multipleSelection = false;

    public ICustomScrollListener listener;

    private boolean isSorted = true;

    public boolean visible = true;

    private boolean selectable = true;

    private boolean mouseInList = false;

    private int lastClickedItem = -1;

    private long lastClickedTime = 0L;

    private GuiTextFieldNop textField;

    protected boolean hasSearch = true;

    private String searchStr = "";

    private String[] searchWords = new String[0];

    public GuiCustomScrollNop(Screen parent, int id) {
        super(Component.empty());
        this.f_96543_ = 176;
        this.f_96544_ = 159;
        this.hover = -1;
        this.selectedList = new ArrayList();
        this.listHeight = 0;
        this.scrollY = 0;
        this.scrollHeight = 0;
        this.isScrolling = false;
        if (parent instanceof ICustomScrollListener) {
            this.listener = (ICustomScrollListener) parent;
        }
        this.list = new ArrayList();
        this.id = id;
        this.textField = new GuiTextFieldNop(0, null, 0, 0, 176, 20, "");
        this.f_96541_ = Minecraft.getInstance();
        this.f_96547_ = this.f_96541_.font;
    }

    public GuiCustomScrollNop(Screen parent, int id, boolean multipleSelection) {
        this(parent, id);
        this.multipleSelection = multipleSelection;
    }

    public void setSize(int x, int y) {
        this.textField.m_93674_(x);
        this.f_96544_ = y - this.textFieldHeight();
        this.f_96543_ = x;
        this.listHeight = 14 * this.listSize;
        if (this.listHeight > 0) {
            this.scrollHeight = (int) ((double) (this.f_96544_ - 8) / (double) this.listHeight * (double) (this.f_96544_ - 8));
        } else {
            this.scrollHeight = Integer.MAX_VALUE;
        }
        this.maxScrollY = this.listHeight - (this.f_96544_ - 8) - 1;
        if (this.maxScrollY > 0 && this.scrollY > this.maxScrollY || this.maxScrollY <= 0 && this.scrollY > this.scrollHeight) {
            this.scrollY = 0;
        }
    }

    public void disabledSearch() {
        this.hasSearch = false;
    }

    private int textFieldHeight() {
        return this.hasSearch ? 22 : 0;
    }

    private void reset() {
        if (this.searchWords.length == 0) {
            this.listSize = this.list.size();
        } else {
            this.listSize = (int) this.list.stream().filter(this::isSearched).count();
        }
        this.setSize(this.f_96543_, this.f_96544_ + this.textFieldHeight());
    }

    private boolean isSearched(String s) {
        s = I18n.get(s);
        for (String k : this.searchWords) {
            if (!s.toLowerCase().contains(k.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public int getWidth() {
        return this.f_96543_;
    }

    public int getHeight() {
        return this.f_96544_ + this.textFieldHeight();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            PoseStack matrixStack = graphics.pose();
            if (this.hasSearch) {
                this.textField.m_252865_(this.guiLeft);
                this.textField.m_253211_(this.guiTop);
                this.textField.m_88315_(graphics, mouseX, mouseY, partialTicks);
            }
            this.guiTop = this.guiTop + this.textFieldHeight();
            this.mouseInList = this.isMouseOver(mouseX, mouseY);
            graphics.fillGradient(this.guiLeft, this.guiTop, this.f_96543_ + this.guiLeft, this.f_96544_ + this.guiTop, -1072689136, -804253680);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, resource);
            if (this.scrollHeight < this.f_96544_ - 8) {
                this.drawScrollBar(graphics);
            }
            matrixStack.pushPose();
            matrixStack.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);
            if (this.selectable) {
                this.hover = this.getMouseOver(mouseX, mouseY);
            }
            this.drawItems(graphics);
            matrixStack.popPose();
            if (this.scrollHeight < this.f_96544_ - 8) {
                mouseX -= this.guiLeft;
                mouseY -= this.guiTop;
                if (((MouseHelperMixin) this.f_96541_.mouseHandler).getActiveButton() == 0) {
                    if (mouseX >= this.f_96543_ - 10 && mouseX < this.f_96543_ - 2 && mouseY >= 4 && mouseY < this.f_96544_) {
                        this.isScrolling = true;
                    }
                } else {
                    this.isScrolling = false;
                }
                if (this.isScrolling) {
                    this.scrollY = (mouseY - 8) * this.listHeight / (this.f_96544_ - 8) - this.scrollHeight;
                    if (this.scrollY < 0) {
                        this.scrollY = 0;
                    }
                    if (this.scrollY > this.maxScrollY) {
                        this.scrollY = this.maxScrollY;
                    }
                }
            }
            this.guiTop = this.guiTop - this.textFieldHeight();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double mouseScrolled) {
        if (mouseScrolled != 0.0 && this.mouseInList) {
            this.scrollY += mouseScrolled > 0.0 ? -14 : 14;
            if (this.scrollY > this.maxScrollY) {
                this.scrollY = this.maxScrollY;
            }
            if (this.scrollY < 0) {
                this.scrollY = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseInOption(int i, int j, int k) {
        int xOffset = this.scrollHeight < this.f_96544_ - 8 ? 10 : 0;
        int l = 4;
        int i1 = 14 * k + 4 - this.scrollY;
        return i >= l - 1 && i < this.f_96543_ - 2 - xOffset && j >= i1 - 1 && j < i1 + 8;
    }

    protected void drawItems(GuiGraphics graphics) {
        int displayIndex = 0;
        for (int i = 0; i < this.list.size(); i++) {
            if (this.isSearched((String) this.list.get(i))) {
                int j = 4;
                int k = 14 * displayIndex + 4 - this.scrollY;
                if (k >= 4 && k + 12 < this.f_96544_) {
                    int xOffset = this.scrollHeight < this.f_96544_ - 8 ? 0 : 10;
                    String displayString = I18n.get((String) this.list.get(i));
                    String text = "";
                    float maxWidth = (float) (this.f_96543_ + xOffset - 8) * 0.8F;
                    if (!((float) this.f_96547_.width(displayString) > maxWidth)) {
                        text = displayString;
                    } else {
                        for (int h = 0; h < displayString.length(); h++) {
                            char c = displayString.charAt(h);
                            text = text + c;
                            if ((float) this.f_96547_.width(text) > maxWidth) {
                                break;
                            }
                        }
                        if (displayString.length() > text.length()) {
                            text = text + "...";
                        }
                    }
                    if ((!this.multipleSelection || !this.selectedList.contains(i)) && (this.multipleSelection || this.selected != i)) {
                        if (i == this.hover) {
                            graphics.drawString(this.f_96547_, text, j, k, 65280);
                        } else {
                            graphics.drawString(this.f_96547_, text, j, k, 16777215);
                        }
                    } else {
                        graphics.vLine(j - 2, k - 4, k + 10, -1);
                        graphics.vLine(j + this.f_96543_ - 18 + xOffset, k - 4, k + 10, -1);
                        graphics.hLine(j - 2, j + this.f_96543_ - 18 + xOffset, k - 3, -1);
                        graphics.hLine(j - 2, j + this.f_96543_ - 18 + xOffset, k + 10, -1);
                        graphics.drawString(this.f_96547_, text, j, k, 16777215);
                    }
                }
                displayIndex++;
            }
        }
    }

    public String getSelected() {
        return this.selected >= 0 && this.selected < this.list.size() ? (String) this.list.get(this.selected) : null;
    }

    private int getMouseOver(int i, int j) {
        i -= this.guiLeft;
        j -= this.guiTop;
        if (i >= 4 && i < this.f_96543_ - 4 && j >= 4 && j < this.f_96544_) {
            int displayIndex = 0;
            for (int j1 = 0; j1 < this.list.size(); j1++) {
                if (this.isSearched((String) this.list.get(j1))) {
                    if (this.mouseInOption(i, j, displayIndex)) {
                        return j1;
                    }
                    displayIndex++;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        if (this.hasSearch) {
            boolean bo = this.textField.m_7933_(p_231046_1_, p_231046_2_, p_231046_3_);
            if (!this.searchStr.equals(this.textField.m_94155_())) {
                this.searchStr = this.textField.m_94155_().trim();
                this.searchWords = this.searchStr.split(" ");
                this.reset();
            }
            return bo;
        } else {
            return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_) {
        if (this.hasSearch) {
            boolean bo = this.textField.charTyped(p_231042_1_, p_231042_2_);
            if (!this.searchStr.equals(this.textField.m_94155_())) {
                this.searchStr = this.textField.m_94155_().trim();
                this.searchWords = this.searchStr.split(" ");
                this.reset();
            }
            return bo;
        } else {
            return super.m_5534_(p_231042_1_, p_231042_2_);
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if (this.hasSearch) {
            this.textField.mouseClicked(i, j, k);
        }
        if (k == 0 && this.hover >= 0) {
            if (this.multipleSelection) {
                if (this.selectedList.contains(this.hover)) {
                    this.selectedList.remove(this.hover);
                } else {
                    this.selectedList.add(this.hover);
                }
            } else {
                this.selected = this.hover;
                this.hover = -1;
            }
            if (this.listener != null) {
                long time = System.currentTimeMillis();
                this.listener.scrollClicked(i, j, k, this);
                if (this.selected >= 0 && this.selected == this.lastClickedItem && time - this.lastClickedTime < 500L) {
                    this.listener.scrollDoubleClicked(this.getSelected(), this);
                }
                this.lastClickedTime = time;
                this.lastClickedItem = this.selected;
            }
            return true;
        } else {
            return false;
        }
    }

    private void drawScrollBar(GuiGraphics graphics) {
        int i = this.guiLeft + this.f_96543_ - 9;
        int j = this.guiTop + (int) ((double) this.scrollY / (double) this.listHeight * (double) (this.f_96544_ - 8)) + 4;
        graphics.blit(resource, i, j, this.f_96543_, 9, 5, 1);
        int k;
        for (k = j + 1; k < j + this.scrollHeight - 1; k++) {
            graphics.blit(resource, i, k, this.f_96543_, 10, 5, 1);
        }
        graphics.blit(resource, i, k, this.f_96543_, 11, 5, 1);
    }

    public boolean hasSelected() {
        return this.selected >= 0;
    }

    public void setList(List<String> list) {
        if (!this.isSameList(list)) {
            this.isSorted = true;
            this.scrollY = 0;
            Collections.sort(list, new NaturalOrderComparator());
            this.list = list;
            this.reset();
        }
    }

    public void setUnsortedList(List<String> list) {
        if (!this.isSameList(list)) {
            this.isSorted = false;
            this.scrollY = 0;
            this.list = list;
            this.reset();
        }
    }

    private boolean isSameList(List<String> list) {
        if (this.list.size() != list.size()) {
            return false;
        } else {
            for (String s : this.list) {
                if (!list.contains(s)) {
                    return false;
                }
            }
            return true;
        }
    }

    public void replace(String old, String name) {
        String select = this.getSelected();
        this.list.remove(old);
        this.list.add(name);
        if (this.isSorted) {
            Collections.sort(this.list, new NaturalOrderComparator());
        }
        if (old.equals(select)) {
            select = name;
        }
        this.setSelected(select);
        this.reset();
    }

    public void setSelected(String name) {
        this.selected = this.list.indexOf(name);
    }

    public void clear() {
        this.list = new ArrayList();
        this.selected = -1;
        this.scrollY = 0;
        this.searchStr = "";
        this.searchWords = new String[0];
        this.textField.m_94144_("");
        this.reset();
    }

    public void clearSelection() {
        this.list = new ArrayList();
        this.selected = -1;
    }

    public List<String> getList() {
        return this.list;
    }

    public List<String> getSelectedList() {
        return (List<String>) IntStream.range(0, this.list.size()).filter(i -> this.selectedList.contains(i)).mapToObj(i -> (String) this.list.get(i)).collect(Collectors.toList());
    }

    public void setSelectedList(Collection<String> selectedList) {
        this.selectedList = (List<Integer>) selectedList.stream().map(t -> this.list.indexOf(t)).collect(Collectors.toList());
    }

    public GuiCustomScrollNop setUnselectable() {
        this.selectable = false;
        return this;
    }

    public void scrollTo(String name) {
        int i = this.list.indexOf(name);
        if (i >= 0 && this.scrollHeight < this.f_96544_ - 8) {
            int pos = (int) (1.0F * (float) i / (float) this.list.size() * (float) this.listHeight);
            if (pos > this.maxScrollY) {
                pos = this.maxScrollY;
            }
            this.scrollY = pos;
        }
    }

    public boolean isMouseOver(int x, int y) {
        return x >= this.guiLeft && x <= this.guiLeft + this.f_96543_ && y >= this.guiTop && y <= this.guiTop + this.f_96544_;
    }

    public int getSelectedIndex() {
        return this.selected;
    }

    public void setSelectedIndex(int i) {
        if (i < 0) {
            this.selected = -1;
        } else {
            this.selected = i;
        }
    }
}