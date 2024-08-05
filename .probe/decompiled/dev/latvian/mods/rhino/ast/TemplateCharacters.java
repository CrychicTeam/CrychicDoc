package dev.latvian.mods.rhino.ast;

public class TemplateCharacters extends AstNode {

    private String value;

    private String rawValue;

    public TemplateCharacters() {
        this.type = 168;
    }

    public TemplateCharacters(int pos) {
        super(pos);
        this.type = 168;
    }

    public TemplateCharacters(int pos, int len) {
        super(pos, len);
        this.type = 168;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.assertNotNull(value);
        this.value = value;
    }

    public String getRawValue() {
        return this.rawValue;
    }

    public void setRawValue(String rawValue) {
        this.assertNotNull(rawValue);
        this.rawValue = rawValue;
    }
}