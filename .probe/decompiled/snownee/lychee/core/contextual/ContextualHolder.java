package snownee.lychee.core.contextual;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeRegistries;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ChanceRecipe;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class ContextualHolder {

    private static final Component SECRET_TITLE = Component.translatable("contextual.lychee.secret").withStyle(ChatFormatting.GRAY);

    private List<ContextualCondition> conditions = List.of();

    @Nullable
    private BitSet secretFlags;

    @Nullable
    private List<Component> overrideDesc;

    public List<ContextualCondition> getConditions() {
        return this.conditions;
    }

    public int showingConditionsCount() {
        return this.conditions.stream().mapToInt(ContextualCondition::showingCount).sum();
    }

    public void withCondition(ContextualCondition condition) {
        Objects.requireNonNull(condition);
        if (this.conditions.isEmpty()) {
            this.conditions = Lists.newArrayList();
        }
        this.conditions.add(condition);
    }

    public void parseConditions(JsonElement element) {
        if (element != null) {
            if (element.isJsonObject()) {
                this.parse(element.getAsJsonObject());
            } else {
                JsonArray array = element.getAsJsonArray();
                for (int x = 0; x < array.size(); x++) {
                    this.parse(array.get(x).getAsJsonObject());
                }
            }
        }
    }

    private void parse(JsonObject o) {
        this.withCondition(ContextualCondition.parse(o));
        if (GsonHelper.getAsBoolean(o, "secret", false)) {
            if (this.secretFlags == null) {
                this.secretFlags = new BitSet(this.conditions.size());
            }
            this.secretFlags.set(this.conditions.size() - 1);
        }
        if (o.has("description")) {
            if (this.overrideDesc == null) {
                this.overrideDesc = Lists.newArrayList();
            }
            while (this.overrideDesc.size() + 1 < this.conditions.size()) {
                this.overrideDesc.add(null);
            }
            this.overrideDesc.add(Component.translatable(GsonHelper.getAsString(o, "description")));
        }
    }

    public void conditionsFromNetwork(FriendlyByteBuf pBuffer) {
        int size = pBuffer.readVarInt();
        for (int i = 0; i < size; i++) {
            ContextualConditionType<?> type = CommonProxy.readRegistryId(LycheeRegistries.CONTEXTUAL, pBuffer);
            this.withCondition(type.fromNetwork(pBuffer));
        }
        if (pBuffer.readBoolean()) {
            this.secretFlags = pBuffer.readBitSet();
        }
        if (pBuffer.readBoolean()) {
            this.overrideDesc = Lists.newArrayListWithCapacity(size);
            for (int i = 0; i < size; i++) {
                String key = pBuffer.readUtf();
                if (key.isEmpty()) {
                    this.overrideDesc.add(null);
                } else {
                    this.overrideDesc.add(Component.translatable(key));
                }
            }
        }
    }

    public void conditionsToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.conditions.size());
        for (ContextualCondition condition : this.conditions) {
            ContextualConditionType type = condition.getType();
            CommonProxy.writeRegistryId(LycheeRegistries.CONTEXTUAL, type, pBuffer);
            type.toNetwork(condition, pBuffer);
        }
        pBuffer.writeBoolean(this.secretFlags != null);
        if (this.secretFlags != null) {
            pBuffer.writeBitSet(this.secretFlags);
        }
        pBuffer.writeBoolean(this.overrideDesc != null);
        if (this.overrideDesc != null) {
            for (Component component : this.overrideDesc) {
                if (component != null && component.getContents() instanceof TranslatableContents translatable) {
                    pBuffer.writeUtf(translatable.getKey());
                } else {
                    pBuffer.writeUtf("");
                }
            }
        }
    }

    public JsonElement rawConditionsToJson() {
        if (this.conditions.size() == 1) {
            return ((ContextualCondition) this.conditions.get(0)).toJson();
        } else {
            JsonArray array = new JsonArray();
            this.conditions.forEach($ -> array.add($.toJson()));
            return array;
        }
    }

    public int checkConditions(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        try {
            boolean first = true;
            for (ContextualCondition condition : this.conditions) {
                if (!first || condition.getType() != ContextualConditionTypes.CHANCE || !(this instanceof ChanceRecipe)) {
                    first = false;
                    times = condition.test(recipe, ctx, times);
                    if (times == 0) {
                        break;
                    }
                }
            }
            return times;
        } catch (Throwable var7) {
            Lychee.LOGGER.error("Failed to check conditions for recipe {}", recipe.lychee$getId(), var7);
            return 0;
        }
    }

    public boolean isSecretCondition(int index) {
        return this.secretFlags == null ? false : this.secretFlags.get(index);
    }

    public void getConditionTooltips(List<Component> list, int indent, @Nullable Level level, @Nullable Player player) {
        if (level != null) {
            int i = 0;
            for (ContextualCondition condition : this.getConditions()) {
                if (this.isSecretCondition(i)) {
                    InteractionResult result = condition.testInTooltips(level, player);
                    ContextualCondition.desc(list, result, indent, SECRET_TITLE.copy());
                } else if (this.isOverridenDesc(i)) {
                    InteractionResult result = condition.testInTooltips(level, player);
                    ContextualCondition.desc(list, result, indent, ((Component) this.overrideDesc.get(i)).copy());
                } else {
                    condition.appendTooltips(list, level, player, indent, false);
                }
                i++;
            }
        }
    }

    private boolean isOverridenDesc(int i) {
        return this.overrideDesc != null && this.overrideDesc.size() > i ? this.overrideDesc.get(i) != null : false;
    }
}