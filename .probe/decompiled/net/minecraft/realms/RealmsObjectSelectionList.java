package net.minecraft.realms;

import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

public abstract class RealmsObjectSelectionList<E extends ObjectSelectionList.Entry<E>> extends ObjectSelectionList<E> {

    protected RealmsObjectSelectionList(int int0, int int1, int int2, int int3, int int4) {
        super(Minecraft.getInstance(), int0, int1, int2, int3, int4);
    }

    public void setSelectedItem(int int0) {
        if (int0 == -1) {
            this.m_6987_(null);
        } else if (super.m_5773_() != 0) {
            this.m_6987_((ObjectSelectionList.Entry) this.m_93500_(int0));
        }
    }

    public void selectItem(int int0) {
        this.setSelectedItem(int0);
    }

    public void itemClicked(int int0, int int1, double double2, double double3, int int4, int int5) {
    }

    @Override
    public int getMaxPosition() {
        return 0;
    }

    @Override
    public int getScrollbarPosition() {
        return this.getRowLeft() + this.getRowWidth();
    }

    @Override
    public int getRowWidth() {
        return (int) ((double) this.f_93388_ * 0.6);
    }

    @Override
    public void replaceEntries(Collection<E> collectionE0) {
        super.m_5988_(collectionE0);
    }

    @Override
    public int getItemCount() {
        return super.m_5773_();
    }

    @Override
    public int getRowTop(int int0) {
        return super.m_7610_(int0);
    }

    @Override
    public int getRowLeft() {
        return super.m_5747_();
    }

    public int addEntry(E e0) {
        return super.m_7085_(e0);
    }

    public void clear() {
        this.m_93516_();
    }
}