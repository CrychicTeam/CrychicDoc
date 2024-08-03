package net.minecraft.client.gui.layouts;

import com.mojang.math.Divisor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.util.Mth;

public class GridLayout extends AbstractLayout {

    private final List<LayoutElement> children = new ArrayList();

    private final List<GridLayout.CellInhabitant> cellInhabitants = new ArrayList();

    private final LayoutSettings defaultCellSettings = LayoutSettings.defaults();

    private int rowSpacing = 0;

    private int columnSpacing = 0;

    public GridLayout() {
        this(0, 0);
    }

    public GridLayout(int int0, int int1) {
        super(int0, int1, 0, 0);
    }

    @Override
    public void arrangeElements() {
        super.m_264036_();
        int $$0 = 0;
        int $$1 = 0;
        for (GridLayout.CellInhabitant $$2 : this.cellInhabitants) {
            $$0 = Math.max($$2.getLastOccupiedRow(), $$0);
            $$1 = Math.max($$2.getLastOccupiedColumn(), $$1);
        }
        int[] $$3 = new int[$$1 + 1];
        int[] $$4 = new int[$$0 + 1];
        for (GridLayout.CellInhabitant $$5 : this.cellInhabitants) {
            int $$6 = $$5.m_264608_() - ($$5.occupiedRows - 1) * this.rowSpacing;
            Divisor $$7 = new Divisor($$6, $$5.occupiedRows);
            for (int $$8 = $$5.row; $$8 <= $$5.getLastOccupiedRow(); $$8++) {
                $$4[$$8] = Math.max($$4[$$8], $$7.nextInt());
            }
            int $$9 = $$5.m_264477_() - ($$5.occupiedColumns - 1) * this.columnSpacing;
            Divisor $$10 = new Divisor($$9, $$5.occupiedColumns);
            for (int $$11 = $$5.column; $$11 <= $$5.getLastOccupiedColumn(); $$11++) {
                $$3[$$11] = Math.max($$3[$$11], $$10.nextInt());
            }
        }
        int[] $$12 = new int[$$1 + 1];
        int[] $$13 = new int[$$0 + 1];
        $$12[0] = 0;
        for (int $$14 = 1; $$14 <= $$1; $$14++) {
            $$12[$$14] = $$12[$$14 - 1] + $$3[$$14 - 1] + this.columnSpacing;
        }
        $$13[0] = 0;
        for (int $$15 = 1; $$15 <= $$0; $$15++) {
            $$13[$$15] = $$13[$$15 - 1] + $$4[$$15 - 1] + this.rowSpacing;
        }
        for (GridLayout.CellInhabitant $$16 : this.cellInhabitants) {
            int $$17 = 0;
            for (int $$18 = $$16.column; $$18 <= $$16.getLastOccupiedColumn(); $$18++) {
                $$17 += $$3[$$18];
            }
            $$17 += this.columnSpacing * ($$16.occupiedColumns - 1);
            $$16.m_264032_(this.m_252754_() + $$12[$$16.column], $$17);
            int $$19 = 0;
            for (int $$20 = $$16.row; $$20 <= $$16.getLastOccupiedRow(); $$20++) {
                $$19 += $$4[$$20];
            }
            $$19 += this.rowSpacing * ($$16.occupiedRows - 1);
            $$16.m_264572_(this.m_252907_() + $$13[$$16.row], $$19);
        }
        this.f_263674_ = $$12[$$1] + $$3[$$1];
        this.f_263818_ = $$13[$$0] + $$4[$$0];
    }

    public <T extends LayoutElement> T addChild(T t0, int int1, int int2) {
        return this.addChild(t0, int1, int2, this.newCellSettings());
    }

    public <T extends LayoutElement> T addChild(T t0, int int1, int int2, LayoutSettings layoutSettings3) {
        return this.addChild(t0, int1, int2, 1, 1, layoutSettings3);
    }

    public <T extends LayoutElement> T addChild(T t0, int int1, int int2, int int3, int int4) {
        return this.addChild(t0, int1, int2, int3, int4, this.newCellSettings());
    }

    public <T extends LayoutElement> T addChild(T t0, int int1, int int2, int int3, int int4, LayoutSettings layoutSettings5) {
        if (int3 < 1) {
            throw new IllegalArgumentException("Occupied rows must be at least 1");
        } else if (int4 < 1) {
            throw new IllegalArgumentException("Occupied columns must be at least 1");
        } else {
            this.cellInhabitants.add(new GridLayout.CellInhabitant(t0, int1, int2, int3, int4, layoutSettings5));
            this.children.add(t0);
            return t0;
        }
    }

    public GridLayout columnSpacing(int int0) {
        this.columnSpacing = int0;
        return this;
    }

    public GridLayout rowSpacing(int int0) {
        this.rowSpacing = int0;
        return this;
    }

    public GridLayout spacing(int int0) {
        return this.columnSpacing(int0).rowSpacing(int0);
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumerLayoutElement0) {
        this.children.forEach(consumerLayoutElement0);
    }

    public LayoutSettings newCellSettings() {
        return this.defaultCellSettings.copy();
    }

    public LayoutSettings defaultCellSetting() {
        return this.defaultCellSettings;
    }

    public GridLayout.RowHelper createRowHelper(int int0) {
        return new GridLayout.RowHelper(int0);
    }

    static class CellInhabitant extends AbstractLayout.AbstractChildWrapper {

        final int row;

        final int column;

        final int occupiedRows;

        final int occupiedColumns;

        CellInhabitant(LayoutElement layoutElement0, int int1, int int2, int int3, int int4, LayoutSettings layoutSettings5) {
            super(layoutElement0, layoutSettings5.getExposed());
            this.row = int1;
            this.column = int2;
            this.occupiedRows = int3;
            this.occupiedColumns = int4;
        }

        public int getLastOccupiedRow() {
            return this.row + this.occupiedRows - 1;
        }

        public int getLastOccupiedColumn() {
            return this.column + this.occupiedColumns - 1;
        }
    }

    public final class RowHelper {

        private final int columns;

        private int index;

        RowHelper(int int0) {
            this.columns = int0;
        }

        public <T extends LayoutElement> T addChild(T t0) {
            return this.addChild(t0, 1);
        }

        public <T extends LayoutElement> T addChild(T t0, int int1) {
            return this.addChild(t0, int1, this.defaultCellSetting());
        }

        public <T extends LayoutElement> T addChild(T t0, LayoutSettings layoutSettings1) {
            return this.addChild(t0, 1, layoutSettings1);
        }

        public <T extends LayoutElement> T addChild(T t0, int int1, LayoutSettings layoutSettings2) {
            int $$3 = this.index / this.columns;
            int $$4 = this.index % this.columns;
            if ($$4 + int1 > this.columns) {
                $$3++;
                $$4 = 0;
                this.index = Mth.roundToward(this.index, this.columns);
            }
            this.index += int1;
            return GridLayout.this.addChild(t0, $$3, $$4, 1, int1, layoutSettings2);
        }

        public GridLayout getGrid() {
            return GridLayout.this;
        }

        public LayoutSettings newCellSettings() {
            return GridLayout.this.newCellSettings();
        }

        public LayoutSettings defaultCellSetting() {
            return GridLayout.this.defaultCellSetting();
        }
    }
}