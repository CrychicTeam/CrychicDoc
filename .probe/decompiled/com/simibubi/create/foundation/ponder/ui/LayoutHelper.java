package com.simibubi.create.foundation.ponder.ui;

import net.minecraft.client.renderer.Rect2i;

public interface LayoutHelper {

    static LayoutHelper centeredHorizontal(int itemCount, int rows, int width, int height, int spacing) {
        return new LayoutHelper.CenteredHorizontalLayoutHelper(itemCount, rows, width, height, spacing);
    }

    int getX();

    int getY();

    void next();

    int getTotalWidth();

    int getTotalHeight();

    default Rect2i getArea() {
        int lWidth = this.getTotalWidth();
        int lHeight = this.getTotalHeight();
        return new Rect2i(-lWidth / 2, -lHeight / 2, lWidth, lHeight);
    }

    public static class CenteredHorizontalLayoutHelper implements LayoutHelper {

        int itemCount;

        int rows;

        int width;

        int height;

        int spacing;

        int currentColumn = 0;

        int currentRow = 0;

        int[] rowCounts;

        int x = 0;

        int y = 0;

        CenteredHorizontalLayoutHelper(int itemCount, int rows, int width, int height, int spacing) {
            this.itemCount = itemCount;
            this.rows = rows;
            this.width = width;
            this.height = height;
            this.spacing = spacing;
            this.rowCounts = new int[rows];
            int itemsPerRow = itemCount / rows;
            int itemDiff = itemCount - itemsPerRow * rows;
            for (int i = 0; i < rows; i++) {
                this.rowCounts[i] = itemsPerRow;
                if (itemDiff > 0) {
                    this.rowCounts[i]++;
                    itemDiff--;
                }
            }
            this.init();
        }

        @Override
        public int getX() {
            return this.x;
        }

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public void next() {
            this.currentColumn++;
            if (this.currentColumn >= this.rowCounts[this.currentRow]) {
                if (++this.currentRow >= this.rows) {
                    this.x = 0;
                    this.y = 0;
                } else {
                    this.currentColumn = 0;
                    this.prepareX();
                    this.y = this.y + this.height + this.spacing;
                }
            } else {
                this.x = this.x + this.width + this.spacing;
            }
        }

        private void init() {
            this.prepareX();
            this.prepareY();
        }

        private void prepareX() {
            int rowWidth = this.rowCounts[this.currentRow] * this.width + (this.rowCounts[this.currentRow] - 1) * this.spacing;
            this.x = -(rowWidth / 2);
        }

        private void prepareY() {
            int totalHeight = this.rows * this.height + (this.rows > 1 ? (this.rows - 1) * this.spacing : 0);
            this.y = -(totalHeight / 2);
        }

        @Override
        public int getTotalWidth() {
            return this.rowCounts[0] * this.width + (this.rowCounts[0] - 1) * this.spacing;
        }

        @Override
        public int getTotalHeight() {
            return this.rows * this.height + (this.rows > 1 ? (this.rows - 1) * this.spacing : 0);
        }
    }
}