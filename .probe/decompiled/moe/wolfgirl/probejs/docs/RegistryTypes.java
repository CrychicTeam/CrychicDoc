package moe.wolfgirl.probejs.docs;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.snippet.Snippet;
import moe.wolfgirl.probejs.lang.snippet.SnippetDump;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.TypeScriptFile;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.FieldDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.TypeDecl;
import moe.wolfgirl.probejs.lang.typescript.code.ts.Wrapped;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;
import moe.wolfgirl.probejs.utils.NameUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RegistryTypes extends ProbeJSPlugin {

    public static final String LITERAL_FIELD = "probejsInternal$$Literal";

    public static final String TAG_FIELD = "probejsInternal$$Tag";

    public static final String OF_TYPE_DECL = "T extends { %s: infer U } ? U : string";

    @Override
    public void assignType(ScriptDump scriptDump) {
        List<BaseType> registryNames = new ArrayList();
        for (Entry<ResourceKey<? extends Registry<?>>, RegistryInfo<?>> entry : RegistryInfo.MAP.entrySet()) {
            ResourceKey<? extends Registry<?>> key = (ResourceKey<? extends Registry<?>>) entry.getKey();
            RegistryInfo<?> info = (RegistryInfo<?>) entry.getValue();
            if (info.getVanillaRegistry() != null) {
                String typeName = NameUtils.rlToTitle(key.location().getPath());
                scriptDump.assignType(info.objectBaseClass, Types.primitive("Special.%s".formatted(typeName)));
                registryNames.add(Types.literal(key.location().toString()));
            }
        }
        scriptDump.assignType(ResourceKey.class, Types.parameterized(Types.primitive("Special.LiteralOf"), Types.generic("T")));
        scriptDump.assignType(Holder.class, Types.parameterized(Types.primitive("Special.LiteralOf"), Types.generic("T")));
        scriptDump.assignType(Registry.class, Types.or((BaseType[]) registryNames.toArray(BaseType[]::new)));
        scriptDump.assignType(TagKey.class, Types.parameterized(Types.primitive("Special.TagOf"), Types.generic("T")));
    }

    @Override
    public void addGlobals(ScriptDump scriptDump) {
        Wrapped.Namespace special = new Wrapped.Namespace("Special");
        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
        if (currentServer != null) {
            RegistryAccess registryAccess = currentServer.registryAccess();
            for (ResourceKey<? extends Registry<?>> key : RegistryInfo.MAP.keySet()) {
                Registry<?> registry = (Registry<?>) registryAccess.registry(key).orElse(null);
                if (registry != null) {
                    List<String> entryNames = new ArrayList();
                    for (ResourceLocation entryName : registry.keySet()) {
                        if (entryName.getNamespace().equals("minecraft")) {
                            entryNames.add(entryName.getPath());
                        }
                        entryNames.add(entryName.toString());
                    }
                    BaseType types = Types.or((BaseType[]) entryNames.stream().map(Types::literal).toArray(BaseType[]::new));
                    String typeName = NameUtils.rlToTitle(key.location().getPath());
                    TypeDecl typeDecl = new TypeDecl(typeName, types);
                    special.addCode(typeDecl);
                    BaseType[] tagNames = (BaseType[]) registry.getTagNames().map(TagKey::f_203868_).map(ResourceLocation::toString).map(Types::literal).toArray(BaseType[]::new);
                    BaseType tagTypes = Types.or(tagNames);
                    String tagName = typeName + "Tag";
                    TypeDecl tagDecl = new TypeDecl(tagName, tagTypes);
                    special.addCode(tagDecl);
                }
            }
            TypeDecl literalOf = new TypeDecl("LiteralOf<T>", Types.primitive("T extends { %s: infer U } ? U : string".formatted("probejsInternal$$Literal")));
            TypeDecl tagOf = new TypeDecl("TagOf<T>", Types.primitive("T extends { %s: infer U } ? U : string".formatted("probejsInternal$$Tag")));
            special.addCode(literalOf);
            special.addCode(tagOf);
            scriptDump.addGlobal("registry_type", special);
        }
    }

    @Override
    public void modifyClasses(ScriptDump scriptDump, Map<ClassPath, TypeScriptFile> globalClasses) {
        for (Entry<ResourceKey<? extends Registry<?>>, RegistryInfo<?>> entry : RegistryInfo.MAP.entrySet()) {
            ResourceKey<? extends Registry<?>> key = (ResourceKey<? extends Registry<?>>) entry.getKey();
            RegistryInfo<?> info = (RegistryInfo<?>) entry.getValue();
            TypeScriptFile typeScriptFile = (TypeScriptFile) globalClasses.get(new ClassPath(info.objectBaseClass));
            if (typeScriptFile != null) {
                ClassDecl classDecl = (ClassDecl) typeScriptFile.findCode(ClassDecl.class).orElse(null);
                if (classDecl != null) {
                    String typeName = NameUtils.rlToTitle(key.location().getPath());
                    String tagName = typeName + "Tag";
                    FieldDecl literalField = new FieldDecl("probejsInternal$$Literal", Types.primitive("Special.%s".formatted(typeName)));
                    literalField.addComment(new String[] { "This field is a type stub generated by ProbeJS and shall not be used in any sense." });
                    classDecl.fields.add(literalField);
                    FieldDecl tagField = new FieldDecl("probejsInternal$$Tag", Types.primitive("Special.%s".formatted(tagName)));
                    tagField.addComment(new String[] { "This field is a type stub generated by ProbeJS and shall not be used in any sense." });
                    classDecl.fields.add(tagField);
                }
            }
        }
    }

    @Override
    public Set<Class<?>> provideJavaClass(ScriptDump scriptDump) {
        Set<Class<?>> registryObjectClasses = new HashSet();
        for (RegistryInfo<?> value : RegistryInfo.MAP.values()) {
            Registry<?> registry = value.getVanillaRegistry();
            if (registry != null) {
                for (Object o : registry) {
                    registryObjectClasses.add(o.getClass());
                }
                registryObjectClasses.add(value.objectBaseClass);
            }
        }
        return registryObjectClasses;
    }

    @Override
    public void addVSCodeSnippets(SnippetDump dump) {
        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
        if (currentServer != null) {
            RegistryAccess registryAccess = currentServer.registryAccess();
            for (ResourceKey<? extends Registry<?>> key : RegistryInfo.MAP.keySet()) {
                Registry<?> registry = (Registry<?>) registryAccess.registry(key).orElse(null);
                if (registry != null) {
                    List<String> entries = registry.keySet().stream().map(ResourceLocation::toString).toList();
                    if (!entries.isEmpty()) {
                        String registryName = key.location().getNamespace().equals("minecraft") ? key.location().getPath() : key.location().toString();
                        Snippet registrySnippet = dump.snippet("probejs$$" + key.location());
                        registrySnippet.prefix("@%s".formatted(registryName)).description("All available items in the registry \"%s\"".formatted(key.location())).literal("\"").choices(entries).literal("\"");
                        List<String> tags = registry.getTagNames().map(TagKey::f_203868_).map(ResourceLocation::toString).toList();
                        if (!tags.isEmpty()) {
                            Snippet tagSnippet = dump.snippet("probejs_tag$$" + key.location());
                            tagSnippet.prefix("@%s_tag".formatted(registryName)).description("All available tags in the registry \"%s\", no # is added".formatted(key.location())).literal("\"").choices(tags).literal("\"");
                        }
                    }
                }
            }
        }
    }
}