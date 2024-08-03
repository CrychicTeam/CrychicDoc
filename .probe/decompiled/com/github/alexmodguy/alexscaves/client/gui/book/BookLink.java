package com.github.alexmodguy.alexscaves.client.gui.book;

public class BookLink {

    private int lineNumber;

    private int characterStartsAt;

    private String displayText;

    private String linksTo;

    private boolean enabled;

    private boolean hovered = false;

    public BookLink(int lineNumber, int characterStartsAt, String displayText, String linksTo, boolean enabled) {
        this.lineNumber = lineNumber;
        this.characterStartsAt = characterStartsAt;
        this.displayText = displayText;
        this.linksTo = linksTo;
        this.enabled = enabled;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getCharacterStartsAt() {
        return this.characterStartsAt;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public String getLinksTo() {
        return this.linksTo;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}