package dev.ftb.mods.ftblibrary.ui;

public abstract class ModalPanel extends Panel {

    int extraZlevel = 1;

    public ModalPanel(Panel panel) {
        super(panel);
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        int ax = this.getX();
        int ay = this.getY();
        return mouseX >= ax && mouseY >= ay && mouseX < ax + this.width && mouseY < ay + this.height;
    }

    public int getExtraZlevel() {
        return this.extraZlevel;
    }

    public void setExtraZlevel(int extraZlevel) {
        this.extraZlevel = extraZlevel;
    }
}