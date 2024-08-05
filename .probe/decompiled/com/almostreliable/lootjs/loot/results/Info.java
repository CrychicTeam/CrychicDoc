package com.almostreliable.lootjs.loot.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public interface Info {

    String transform();

    public static class Composite implements Info {

        private final Info base;

        protected List<Info> children = new ArrayList();

        public Composite(Info base) {
            this.base = base;
        }

        public Composite(String title) {
            this(new Info.TitledInfo(title));
        }

        public Composite(Icon icon, String title) {
            this(new Info.TitledInfo(icon, title));
        }

        @Override
        public String transform() {
            return this.getBase().transform();
        }

        public Info getBase() {
            return this.base;
        }

        public Collection<Info> getChildren() {
            return Collections.unmodifiableCollection(this.children);
        }

        public void addChildren(Info info) {
            this.children.add(info);
        }
    }

    public static class ResultInfo extends Info.TitledInfo {

        public ResultInfo(String title) {
            super(title);
        }

        public void setResult(boolean result) {
            this.setIcon(result ? Icon.SUCCEED : Icon.FAILED);
        }
    }

    public static class RowInfo extends Info.TitledInfo {

        private final String name;

        public RowInfo(String name, String text) {
            super(text);
            this.name = name;
        }

        @Override
        public String transform() {
            return String.format("%-14s : %s", this.name, super.transform());
        }
    }

    public static class TitledInfo implements Info {

        protected final String title;

        @Nullable
        protected Icon icon;

        public TitledInfo(String title) {
            this.title = title;
        }

        public TitledInfo(Icon icon, String title) {
            this(title);
            this.icon = icon;
        }

        public void setIcon(@Nullable Icon icon) {
            this.icon = icon;
        }

        @Override
        public String transform() {
            return this.icon != null ? this.icon + " " + this.title : this.title;
        }
    }
}