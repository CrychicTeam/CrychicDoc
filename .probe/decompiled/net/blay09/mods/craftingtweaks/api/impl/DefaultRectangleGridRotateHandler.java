package net.blay09.mods.craftingtweaks.api.impl;

import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.GridRotateHandler;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class DefaultRectangleGridRotateHandler implements GridRotateHandler<AbstractContainerMenu> {

    private int rotateRectangularGrid(int slot, int width, int height, boolean clockwise) {
        int stay = 0;
        int moveRight = 1;
        int moveLeft = -1;
        int moveUp = -width;
        int x = slot % width;
        int y = slot / width;
        int smallestSide = Math.min(width, height);
        int hss = smallestSide / 2;
        int bd = height - 1 - y;
        int rd = width - 1 - x;
        int ved = Math.min(y, bd);
        int hed = Math.min(rd, x);
        int civcd = Math.max(hss - ved, 0);
        int cihcd = Math.max(hss - hed, 0);
        boolean diagonal = Math.abs(civcd) - Math.abs(cihcd) == 0 && civcd != 0 && cihcd != 0;
        boolean verticalQuadrant = civcd - cihcd > 0;
        boolean horizontalQuadrant = cihcd - civcd > 0;
        boolean leftHalf = x < rd;
        boolean topHalf = y < bd;
        boolean q1 = verticalQuadrant && topHalf;
        boolean q2 = horizontalQuadrant && !leftHalf;
        boolean q3 = verticalQuadrant && !topHalf;
        boolean q4 = horizontalQuadrant && leftHalf;
        boolean d1 = diagonal && topHalf && leftHalf;
        boolean d2 = diagonal && topHalf && !leftHalf;
        boolean d3 = diagonal && !topHalf && !leftHalf;
        boolean d4 = diagonal && !topHalf && leftHalf;
        if (clockwise) {
            if (q1 || d1) {
                return 1;
            } else if (q3 || d3) {
                return -1;
            } else if (q2 || d2) {
                return width;
            } else {
                return !q4 && !d4 ? 0 : moveUp;
            }
        } else if (q1 || d2) {
            return -1;
        } else if (q3 || d4) {
            return 1;
        } else if (q2 || d3) {
            return moveUp;
        } else {
            return !q4 && !d1 ? 0 : width;
        }
    }

    @Override
    public void rotateGrid(CraftingGrid grid, Player player, AbstractContainerMenu menu, boolean reverse) {
        Container craftMatrix = grid.getCraftingMatrix(player, menu);
        if (craftMatrix != null) {
            int start = grid.getGridStartSlot(player, menu);
            int size = grid.getGridSize(player, menu);
            int gridWidth = (int) Math.sqrt((double) size);
            int gridHeight = (int) Math.sqrt((double) size);
            Container matrixClone = new SimpleContainer(size);
            for (int i = 0; i < size; i++) {
                int slotIndex = menu.slots.get(start + i).getContainerSlot();
                matrixClone.setItem(i, craftMatrix.getItem(slotIndex));
            }
            for (int i = 0; i < size; i++) {
                int slotIndex = menu.slots.get(start + i + this.rotateRectangularGrid(i, gridWidth, gridHeight, !reverse)).getContainerSlot();
                craftMatrix.setItem(slotIndex, matrixClone.getItem(i));
            }
            menu.broadcastChanges();
        }
    }
}