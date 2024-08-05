package moe.wolfgirl.probejs.docs;

import java.util.Collection;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.GlobalStates;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.snippet.SnippetDump;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.member.TypeDecl;
import moe.wolfgirl.probejs.lang.typescript.code.ts.Wrapped;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;

public class SpecialTypes extends ProbeJSPlugin {

    @Override
    public void addGlobals(ScriptDump scriptDump) {
        Wrapped.Namespace special = new Wrapped.Namespace("Special");
        defineLiteralTypes(special, "LangKey", (Collection<String>) GlobalStates.LANG_KEYS.get());
        defineLiteralTypes(special, "RecipeId", GlobalStates.RECIPE_IDS);
        defineLiteralTypes(special, "LootTable", GlobalStates.LOOT_TABLES);
        defineLiteralTypes(special, "RawTexture", (Collection<String>) GlobalStates.RAW_TEXTURES.get());
        defineLiteralTypes(special, "Texture", (Collection<String>) GlobalStates.TEXTURES.get());
        defineLiteralTypes(special, "Mod", (Collection<String>) GlobalStates.MODS.get());
        scriptDump.addGlobal("special_types", special);
    }

    @Override
    public void addVSCodeSnippets(SnippetDump dump) {
        defineLiteralSnippets(dump, "lang_key", (Collection<String>) GlobalStates.LANG_KEYS.get());
        defineLiteralSnippets(dump, "recipe_id", GlobalStates.RECIPE_IDS);
        defineLiteralSnippets(dump, "loot_table", GlobalStates.LOOT_TABLES);
        defineLiteralSnippets(dump, "texture", (Collection<String>) GlobalStates.TEXTURES.get());
        defineLiteralSnippets(dump, "mod", (Collection<String>) GlobalStates.MODS.get());
    }

    private static void defineLiteralTypes(Wrapped.Namespace special, String symbol, Collection<String> literals) {
        BaseType[] types = (BaseType[]) literals.stream().map(Types::literal).toArray(BaseType[]::new);
        TypeDecl declaration = new TypeDecl(symbol, Types.or(types));
        special.addCode(declaration);
    }

    private static void defineLiteralSnippets(SnippetDump dump, String symbol, Collection<String> literals) {
        dump.snippet(symbol).prefix("@" + symbol).choices((Collection<String>) literals.stream().map(ProbeJS.GSON::toJson).collect(Collectors.toSet()));
    }
}