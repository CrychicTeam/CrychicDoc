package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson;

public class JsonGrammar {

    public static final JsonGrammar JANKSON = builder().bareSpecialNumerics(true).build();

    public static final JsonGrammar JSON5 = builder().withComments(true).printTrailingCommas(true).bareSpecialNumerics(true).build();

    public static final JsonGrammar STRICT = builder().withComments(false).build();

    public static final JsonGrammar COMPACT = builder().withComments(false).printWhitespace(false).bareSpecialNumerics(true).build();

    protected boolean comments = true;

    protected boolean printWhitespace = true;

    protected boolean printCommas = true;

    protected boolean printTrailingCommas = false;

    protected boolean bareSpecialNumerics = false;

    protected boolean bareRootObject = false;

    protected boolean printUnquotedKeys = false;

    public boolean hasComments() {
        return this.comments;
    }

    public boolean shouldOutputWhitespace() {
        return this.printWhitespace;
    }

    public static JsonGrammar.Builder builder() {
        return new JsonGrammar.Builder();
    }

    public static class Builder {

        private JsonGrammar grammar = new JsonGrammar();

        public JsonGrammar.Builder withComments(boolean comments) {
            this.grammar.comments = comments;
            return this;
        }

        public JsonGrammar.Builder printWhitespace(boolean whitespace) {
            this.grammar.printWhitespace = whitespace;
            return this;
        }

        public JsonGrammar.Builder printCommas(boolean commas) {
            this.grammar.printCommas = commas;
            return this;
        }

        public JsonGrammar.Builder printTrailingCommas(boolean trailing) {
            this.grammar.printTrailingCommas = trailing;
            return this;
        }

        public JsonGrammar.Builder bareSpecialNumerics(boolean bare) {
            this.grammar.bareSpecialNumerics = bare;
            return this;
        }

        public JsonGrammar.Builder bareRootObject(boolean bare) {
            this.grammar.bareRootObject = bare;
            return this;
        }

        public JsonGrammar.Builder printUnquotedKeys(boolean unquoted) {
            this.grammar.printUnquotedKeys = unquoted;
            return this;
        }

        public JsonGrammar build() {
            return this.grammar;
        }
    }
}