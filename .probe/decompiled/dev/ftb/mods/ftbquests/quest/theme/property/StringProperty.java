package dev.ftb.mods.ftbquests.quest.theme.property;

public class StringProperty extends ThemeProperty<String> {

    public StringProperty(String n) {
        super(n, "");
    }

    public String parse(String string) {
        return string;
    }
}