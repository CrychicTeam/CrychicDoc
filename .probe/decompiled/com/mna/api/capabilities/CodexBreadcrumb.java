package com.mna.api.capabilities;

public class CodexBreadcrumb {

    public final String Key;

    public final CodexBreadcrumb.Type Type;

    public final int Page;

    public final String[] Metadata;

    public CodexBreadcrumb(CodexBreadcrumb.Type type, String Key, int Page, String... metadata) {
        this.Key = Key;
        this.Type = type;
        this.Page = Page;
        this.Metadata = metadata;
    }

    public static enum Type {

        ENTRY, RECIPE, SEARCH, CATEGORY, INDEX
    }
}