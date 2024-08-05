package snownee.lychee.core.post;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.LycheeRegistries;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.contextual.ContextualHolder;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.json.JsonPointer;

public abstract class PostAction extends ContextualHolder {

    @Nullable
    public String path;

    public static void parseActions(JsonElement element, Consumer<PostAction> consumer) {
        if (element != null) {
            if (element.isJsonObject()) {
                consumer.accept(parse(element.getAsJsonObject()));
            } else {
                JsonArray array = element.getAsJsonArray();
                for (int x = 0; x < array.size(); x++) {
                    consumer.accept(parse(array.get(x).getAsJsonObject()));
                }
            }
        }
    }

    public static PostAction parse(JsonObject o) {
        ResourceLocation key = new ResourceLocation(o.get("type").getAsString());
        PostActionType<?> type = LycheeRegistries.POST_ACTION.get(key);
        PostAction action = type.fromJson(o);
        action.parseConditions(o.get("contextual"));
        if (o.has("@path")) {
            action.path = GsonHelper.getAsString(o, "@path");
        }
        return action;
    }

    public static PostAction read(FriendlyByteBuf buf) {
        PostActionType<?> type = CommonProxy.readRegistryId(LycheeRegistries.POST_ACTION, buf);
        PostAction action = type.fromNetwork(buf);
        action.conditionsFromNetwork(buf);
        return action;
    }

    public abstract PostActionType<?> getType();

    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        for (int i = 0; i < times; i++) {
            this.apply(recipe, ctx, 1);
        }
    }

    protected abstract void apply(ILycheeRecipe<?> var1, LycheeContext var2, int var3);

    public List<ItemStack> getItemOutputs() {
        return List.of();
    }

    public Component getDisplayName() {
        return Component.translatable(CommonProxy.makeDescriptionId("postAction", this.getType().getRegistryName()));
    }

    public boolean isHidden() {
        return this.preventSync();
    }

    public boolean preventSync() {
        return false;
    }

    public String toString() {
        return this.toJson().toString();
    }

    public boolean canRepeat() {
        return true;
    }

    public final JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("type", this.getType().getRegistryName().toString());
        if (!this.getConditions().isEmpty()) {
            o.add("contextual", this.rawConditionsToJson());
        }
        if (!Strings.isNullOrEmpty(this.path)) {
            o.addProperty("@path", this.path);
        }
        ((PostActionType<PostAction>) this.getType()).toJson(this, o);
        return o;
    }

    public void validate(ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
    }

    public void getUsedPointers(ILycheeRecipe<?> recipe, Consumer<JsonPointer> consumer) {
    }

    public void preApply(ILycheeRecipe<?> recipe, LycheeContext ctx, ILycheeRecipe.NBTPatchContext patchContext) {
    }

    public List<BlockPredicate> getBlockOutputs() {
        return List.of();
    }

    public JsonElement provideJsonInfo(ILycheeRecipe<?> recipe, JsonPointer pointer, JsonObject recipeObject) {
        return JsonNull.INSTANCE;
    }

    public void onFailure(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
    }
}