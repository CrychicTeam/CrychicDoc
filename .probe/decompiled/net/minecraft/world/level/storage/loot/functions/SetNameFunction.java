package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class SetNameFunction extends LootItemConditionalFunction {

    private static final Logger LOGGER = LogUtils.getLogger();

    final Component name;

    @Nullable
    final LootContext.EntityTarget resolutionContext;

    SetNameFunction(LootItemCondition[] lootItemCondition0, @Nullable Component component1, @Nullable LootContext.EntityTarget lootContextEntityTarget2) {
        super(lootItemCondition0);
        this.name = component1;
        this.resolutionContext = lootContextEntityTarget2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_NAME;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.resolutionContext != null ? ImmutableSet.of(this.resolutionContext.getParam()) : ImmutableSet.of();
    }

    public static UnaryOperator<Component> createResolver(LootContext lootContext0, @Nullable LootContext.EntityTarget lootContextEntityTarget1) {
        if (lootContextEntityTarget1 != null) {
            Entity $$2 = lootContext0.getParamOrNull(lootContextEntityTarget1.getParam());
            if ($$2 != null) {
                CommandSourceStack $$3 = $$2.createCommandSourceStack().withPermission(2);
                return p_81147_ -> {
                    try {
                        return ComponentUtils.updateForEntity($$3, p_81147_, $$2, 0);
                    } catch (CommandSyntaxException var4) {
                        LOGGER.warn("Failed to resolve text component", var4);
                        return p_81147_;
                    }
                };
            }
        }
        return p_81152_ -> p_81152_;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (this.name != null) {
            itemStack0.setHoverName((Component) createResolver(lootContext1, this.resolutionContext).apply(this.name));
        }
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> setName(Component component0) {
        return m_80683_(p_165468_ -> new SetNameFunction(p_165468_, component0, null));
    }

    public static LootItemConditionalFunction.Builder<?> setName(Component component0, LootContext.EntityTarget lootContextEntityTarget1) {
        return m_80683_(p_165465_ -> new SetNameFunction(p_165465_, component0, lootContextEntityTarget1));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetNameFunction> {

        public void serialize(JsonObject jsonObject0, SetNameFunction setNameFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setNameFunction1, jsonSerializationContext2);
            if (setNameFunction1.name != null) {
                jsonObject0.add("name", Component.Serializer.toJsonTree(setNameFunction1.name));
            }
            if (setNameFunction1.resolutionContext != null) {
                jsonObject0.add("entity", jsonSerializationContext2.serialize(setNameFunction1.resolutionContext));
            }
        }

        public SetNameFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            Component $$3 = Component.Serializer.fromJson(jsonObject0.get("name"));
            LootContext.EntityTarget $$4 = GsonHelper.getAsObject(jsonObject0, "entity", null, jsonDeserializationContext1, LootContext.EntityTarget.class);
            return new SetNameFunction(lootItemCondition2, $$3, $$4);
        }
    }
}