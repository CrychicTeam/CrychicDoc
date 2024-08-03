package noppes.npcs.shared.client.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import noppes.npcs.shared.common.util.NaturalOrderComparator;

public class GuiStringSlotNop<E extends GuiStringSlotNop.ListEntry> extends ObjectSelectionList {

    public HashSet<String> selectedList = new HashSet();

    private boolean multiSelect;

    private GuiBasic parent;

    public GuiStringSlotNop(Collection<String> list, GuiBasic parent, boolean multiSelect) {
        super(Minecraft.getInstance(), parent.f_96543_, parent.f_96544_, 32, parent.f_96544_ - 64, 9 + 3);
        this.parent = parent;
        this.multiSelect = multiSelect;
        if (list != null) {
            this.setList(list);
        }
    }

    public void setList(Collection<String> l) {
        this.m_93516_();
        List<String> list = new ArrayList(l);
        Collections.sort(list, new NaturalOrderComparator());
        for (String s : list) {
            this.m_7085_(new GuiStringSlotNop.ListEntry(s));
        }
        this.m_6987_((GuiStringSlotNop.ListEntry) null);
    }

    public void setColoredList(Map<String, Integer> m) {
        this.m_93516_();
        List<String> list = new ArrayList(m.keySet());
        Collections.sort(list, new NaturalOrderComparator());
        for (String s : list) {
            this.m_7085_(new GuiStringSlotNop.ListEntry(s, (Integer) m.get(s)));
        }
        this.m_6987_((GuiStringSlotNop.ListEntry) null);
    }

    public void setSelected(String s) {
        if (s == null) {
            this.m_6987_((GuiStringSlotNop.ListEntry) null);
        } else {
            for (Object e : this.m_6702_()) {
                if (((GuiStringSlotNop.ListEntry) e).data.equals(s)) {
                    this.m_6987_((GuiStringSlotNop.ListEntry) e);
                }
            }
        }
    }

    public String getSelectedString() {
        return this.m_93511_() == null ? null : ((GuiStringSlotNop.ListEntry) this.m_93511_()).data;
    }

    @Override
    protected boolean isSelectedItem(int i) {
        return !this.multiSelect ? super.m_7987_(i) : this.selectedList.contains(((GuiStringSlotNop.ListEntry) this.m_93500_(i)).data);
    }

    @Override
    protected void renderBackground(GuiGraphics graphics) {
        this.parent.m_280273_(graphics);
    }

    public void clear() {
        this.m_93516_();
    }

    public class ListEntry extends ObjectSelectionList.Entry {

        public final String data;

        public final int color;

        private long prevTime = 0L;

        public ListEntry(String data) {
            this.data = data;
            this.color = 16777215;
        }

        public ListEntry(String data, int color) {
            this.data = data;
            this.color = color;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int rowTop, int rowBottom, int width, int height, int mouseX, int mouseY, boolean mouseOver, float partialTicks) {
            graphics.drawString(GuiStringSlotNop.this.parent.getFontRenderer(), this.data, rowBottom, rowTop, this.color);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            long time = System.currentTimeMillis();
            GuiStringSlotNop<E>.ListEntry s = (GuiStringSlotNop.ListEntry) GuiStringSlotNop.this.m_93511_();
            if (s == this && time - this.prevTime < 400L) {
                GuiStringSlotNop.this.parent.doubleClicked();
            }
            this.prevTime = time;
            GuiStringSlotNop.this.m_6987_(this);
            if (GuiStringSlotNop.this.selectedList.contains(this.data)) {
                GuiStringSlotNop.this.selectedList.remove(this.data);
            } else {
                GuiStringSlotNop.this.selectedList.add(this.data);
            }
            GuiStringSlotNop.this.parent.elementClicked();
            return true;
        }

        @Override
        public Component getNarration() {
            return null;
        }
    }
}