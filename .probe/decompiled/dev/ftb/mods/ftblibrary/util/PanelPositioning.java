package dev.ftb.mods.ftblibrary.util;

import dev.ftb.mods.ftblibrary.config.NameMap;

public enum PanelPositioning {

    TOP_LEFT(0, 0),
    TOP(1, 0),
    TOP_RIGHT(2, 0),
    RIGHT(2, 1),
    BOTTOM_RIGHT(2, 2),
    BOTTOM(1, 2),
    BOTTOM_LEFT(0, 2),
    LEFT(0, 1);

    public static final NameMap<PanelPositioning> NAME_MAP = NameMap.of(TOP_RIGHT, values()).baseNameKey("ftblibrary.panel.position").create();

    private final int posX;

    private final int posY;

    private PanelPositioning(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public PanelPositioning.PanelPos getPanelPos(int screenW, int screenH, int panelW, int panelH, int insetX, int insetY) {
        int px = switch(this.posX) {
            case 0 ->
                insetX;
            case 1 ->
                (screenW - panelW) / 2;
            default ->
                screenW - panelW - insetX;
        };
        int py = switch(this.posY) {
            case 0 ->
                insetY;
            case 1 ->
                (screenH - panelH) / 2;
            default ->
                screenH - panelH - insetY;
        };
        return new PanelPositioning.PanelPos(px, py);
    }

    public PanelPositioning.PanelPos getPanelPos(int screenW, int screenH, int panelW, int panelH, float insetX, float insetY) {
        return this.getPanelPos(screenW, screenH, panelW, panelH, (int) ((float) screenW * insetX / 2.0F), (int) ((float) screenH * insetY / 2.0F));
    }

    public static record PanelPos(int x, int y) {
    }
}