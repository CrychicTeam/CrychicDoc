package vazkii.patchouli.client.book.template;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class TemplateInclusion {

    public String template;

    public String as;

    @SerializedName("using")
    public JsonObject localBindings = new JsonObject();

    public int x;

    public int y;

    transient Set<String> visitedTemplates = new LinkedHashSet();

    public void upperMerge(@Nullable TemplateInclusion parent) {
        if (parent != null) {
            if (parent.visitedTemplates.contains(this.template)) {
                throw new IllegalArgumentException("Breaking when include template " + this.template + ", circular dependencies aren't allowed (stack = " + parent.visitedTemplates + ")");
            } else {
                this.visitedTemplates = new LinkedHashSet(parent.visitedTemplates);
                this.visitedTemplates.add(this.template);
                this.as = parent.qualifyName(this.as);
                this.x = this.x + parent.x;
                this.y = this.y + parent.y;
                for (Entry<String, JsonElement> entry : this.localBindings.entrySet()) {
                    String key = (String) entry.getKey();
                    JsonElement val = (JsonElement) entry.getValue();
                    if (val.isJsonPrimitive() && val.getAsString().startsWith("#")) {
                        String realVal = val.getAsString().substring(1);
                        if (parent.localBindings.has(realVal)) {
                            entry.setValue(parent.localBindings.get(realVal));
                        }
                    }
                }
            }
        }
    }

    public void process(Level level, IComponentProcessor processor) {
        if (processor != null) {
            for (Entry<String, JsonElement> entry : this.localBindings.entrySet()) {
                String key = (String) entry.getKey();
                JsonElement val = (JsonElement) entry.getValue();
                if (val.isJsonPrimitive() && val.getAsString().startsWith("#")) {
                    String realVal = val.getAsString().substring(1);
                    IVariable res = processor.process(level, realVal);
                    if (res != null) {
                        entry.setValue(res.unwrap());
                    }
                }
            }
        }
    }

    public String qualifyName(String name) {
        boolean prefixed = name.startsWith("#");
        String query = prefixed ? name.substring(1) : name;
        String result = IVariable.wrap(this.localBindings.get(query)).asString();
        return result.startsWith("#") ? result.substring(1) : (prefixed ? "#" : "") + this.as + (query.isEmpty() ? "" : "." + query);
    }

    public IVariable attemptVariableLookup(String key) {
        if (key.startsWith("#")) {
            key = key.substring(1);
        }
        IVariable result = IVariable.wrap(this.localBindings.get(key));
        return !result.asString().isEmpty() && !this.isUpreference(result) ? result : null;
    }

    public boolean isUpreference(IVariable v) {
        return v.unwrap().isJsonPrimitive() && v.asString().startsWith("#");
    }

    public IVariableProvider wrapProvider(final IVariableProvider provider) {
        return new IVariableProvider() {

            @Override
            public boolean has(String key) {
                return TemplateInclusion.this.attemptVariableLookup(key) != null || provider.has(TemplateInclusion.this.qualifyName(key));
            }

            @Override
            public IVariable get(String key) {
                IVariable vari = TemplateInclusion.this.attemptVariableLookup(key);
                return vari == null ? provider.get(TemplateInclusion.this.qualifyName(key)) : vari;
            }
        };
    }
}