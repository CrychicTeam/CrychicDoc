package dev.ftb.mods.ftblibrary.snbt;

import java.util.ArrayList;
import java.util.List;

class SNBTBuilder {

    public String indent = "";

    public final List<String> lines = new ArrayList();

    public final StringBuilder line = new StringBuilder();

    public int singleLine = 0;

    public void print(Object string) {
        this.line.append(string);
    }

    public void println() {
        this.line.insert(0, this.indent);
        this.lines.add(this.line.toString());
        this.line.setLength(0);
    }

    public void push() {
        this.indent = this.indent + "\t";
    }

    public void pop() {
        this.indent = this.indent.substring(1);
    }
}