package org.violetmoon.zeta.config;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;

public abstract class Definition {

    public final String name;

    public final String lowercaseName;

    public final String englishDisplayName;

    public final List<String> comment;

    @Nullable
    public SectionDefinition parent = null;

    public List<String> path = new ArrayList(1);

    private String configNameKey;

    private String configDescKey;

    @Nullable
    public final Object hint;

    private static final boolean translationDebug = System.getProperty("zeta.configTranslations", null) != null;

    public Definition(Definition.Builder<?, ? extends Definition> builder) {
        this.name = (String) Preconditions.checkNotNull(builder.name, "Definitions require a name");
        this.lowercaseName = builder.lowercaseName != null ? builder.lowercaseName : this.name.toLowerCase(Locale.ROOT).replace(' ', '_');
        this.englishDisplayName = builder.englishDisplayName != null ? builder.englishDisplayName : (this.name.matches("[A-Z ]") ? this.name : WordUtils.capitalizeFully(this.name.replaceAll("(?<=.)([A-Z])", " $1")));
        this.comment = builder.comment;
        this.hint = builder.hint;
    }

    public void setParent(SectionDefinition parent) {
        this.parent = parent;
        this.path = new ArrayList(5);
        Definition d = this;
        do {
            if (!d.lowercaseName.isEmpty()) {
                this.path.add(d.lowercaseName);
            }
            d = d.parent;
        } while (d != null);
        Collections.reverse(this.path);
        ((ArrayList) this.path).trimToSize();
        String stem = "quark.config." + String.join(".", this.path);
        this.configNameKey = stem + ".name";
        this.configDescKey = stem + ".desc";
    }

    public String[] commentToArray() {
        return this.comment == null ? new String[0] : (String[]) this.comment.toArray(new String[0]);
    }

    public String commentToString() {
        return this.comment == null ? "" : String.join("\n", this.comment);
    }

    public final String getTranslatedDisplayName(Function<String, String> i18nDotGet) {
        if (translationDebug) {
            return this.configNameKey;
        } else {
            String local = (String) i18nDotGet.apply(this.configNameKey);
            return !local.isEmpty() && !local.equals(this.configNameKey) ? local : this.englishDisplayName;
        }
    }

    public final List<String> getTranslatedComment(Function<String, String> i18nDotGet) {
        if (translationDebug) {
            return List.of(this.configDescKey);
        } else {
            String local = (String) i18nDotGet.apply(this.configDescKey);
            return !local.isEmpty() && !local.equals(this.configDescKey) ? List.of(local.split("\n")) : this.comment;
        }
    }

    public abstract static class Builder<B extends Definition.Builder<B, T>, T extends Definition> {

        @Nullable
        protected String name;

        @Nullable
        protected String lowercaseName;

        @Nullable
        protected String englishDisplayName;

        protected List<String> comment = new ArrayList(2);

        @Nullable
        protected Object hint;

        public abstract T build();

        public B name(String name) {
            this.name = name;
            return this.downcast();
        }

        public B lowercaseName(String lowercaseName) {
            this.lowercaseName = lowercaseName;
            return this.downcast();
        }

        public B englishDisplayName(String displayName) {
            this.englishDisplayName = displayName;
            return this.downcast();
        }

        public B comment(String comment) {
            return this.comment(List.of(comment));
        }

        public B comment(List<String> comment) {
            comment.stream().flatMap(line -> Stream.of(line.split("\n"))).filter(line -> !line.trim().isEmpty()).forEach(this.comment::add);
            return this.downcast();
        }

        public B hint(Object hint) {
            this.hint = hint;
            return this.downcast();
        }

        protected <X> X downcast() {
            return (X) this;
        }
    }
}