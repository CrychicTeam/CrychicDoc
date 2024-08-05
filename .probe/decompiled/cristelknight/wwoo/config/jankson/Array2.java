package cristelknight.wwoo.config.jankson;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonGrammar.Builder;

public class Array2 extends JsonArray {

    public String toJson(JsonGrammar grammar, int depth) {
        return super.toJson(((Builder) ConfigUtil.JSON_GRAMMAR_BUILDER.get()).printWhitespace(false).build(), depth);
    }
}