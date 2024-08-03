package com.almostreliable.summoningrituals.compat.kubejs;

import com.almostreliable.summoningrituals.altar.AltarBlockEntity;
import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

public class AlmostKube extends KubeJSPlugin {

    private static final EventGroup GROUP = EventGroup.of("Summoning Rituals".replace(" ", ""));

    private static final EventHandler START = GROUP.server("start", () -> SummoningEventJS.class).hasResult();

    private static final EventHandler COMPLETE = GROUP.server("complete", () -> SummoningEventJS.class);

    @Override
    public void init() {
        AltarBlockEntity.SUMMONING_START.register((level, pos, recipe, player) -> START.post(new SummoningEventJS(level, pos, recipe, player)).interruptFalse());
        AltarBlockEntity.SUMMONING_COMPLETE.register((level, pos, recipe, player) -> COMPLETE.post(new SummoningEventJS(level, pos, recipe, player)).interruptFalse());
    }

    @Override
    public void registerEvents() {
        GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType() == ScriptType.SERVER) {
            event.add("SummoningOutput", AlmostKube.OutputWrapper.class);
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (type == ScriptType.SERVER) {
            typeWrappers.registerSimple(RecipeOutputs.ItemOutputBuilder.class, AlmostKube.OutputWrapper::item);
            typeWrappers.registerSimple(RecipeOutputs.MobOutputBuilder.class, AlmostKube.OutputWrapper::mob);
        }
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace("summoningrituals").register("altar", AltarRecipeSchema.SCHEMA);
    }

    public static final class OutputWrapper {

        private OutputWrapper() {
        }

        public static RecipeOutputs.ItemOutputBuilder item(@Nullable Object o) {
            if (o instanceof RecipeOutputs.ItemOutputBuilder) {
                return (RecipeOutputs.ItemOutputBuilder) o;
            } else {
                ItemStack stack = ItemStackJS.of(o);
                if (stack.isEmpty()) {
                    ConsoleJS.SERVER.error("Empty or null ItemStack specified for SummoningOutput.item");
                }
                return new RecipeOutputs.ItemOutputBuilder(stack);
            }
        }

        public static RecipeOutputs.MobOutputBuilder mob(@Nullable Object o) {
            if (o instanceof RecipeOutputs.MobOutputBuilder) {
                return (RecipeOutputs.MobOutputBuilder) o;
            } else if (!(o instanceof CharSequence) && !(o instanceof ResourceLocation)) {
                ConsoleJS.SERVER.error("Missing or invalid entity specified for SummoningOutput.mob");
                return new RecipeOutputs.MobOutputBuilder(EntityType.ITEM);
            } else {
                ResourceLocation id = ResourceLocation.tryParse(o.toString());
                EntityType<?> mob = Platform.mobFromId(id);
                return new RecipeOutputs.MobOutputBuilder(mob);
            }
        }
    }
}