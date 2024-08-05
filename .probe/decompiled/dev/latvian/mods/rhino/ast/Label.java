package dev.latvian.mods.rhino.ast;

public class Label extends Jump {

    private String name;

    public Label() {
        this.type = 131;
    }

    public Label(int pos) {
        this(pos, -1);
    }

    public Label(int pos, int len) {
        this.type = 131;
        this.position = pos;
        this.length = len;
    }

    public Label(int pos, int len, String name) {
        this(pos, len);
        this.setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        name = name == null ? null : name.trim();
        if (name != null && !"".equals(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("invalid label name");
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}