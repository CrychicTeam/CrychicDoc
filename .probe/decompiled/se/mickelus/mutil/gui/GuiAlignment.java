package se.mickelus.mutil.gui;

public enum GuiAlignment {

    left, center, right;

    public GuiAlignment flip() {
        if (this == left) {
            return right;
        } else {
            return this == right ? left : center;
        }
    }

    public GuiAttachment toAttachment() {
        if (this == left) {
            return GuiAttachment.topLeft;
        } else {
            return this == right ? GuiAttachment.topRight : GuiAttachment.topCenter;
        }
    }
}