package snownee.lychee.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.core.Reference;
import snownee.lychee.core.contextual.CustomCondition;
import snownee.lychee.core.post.CustomAction;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.ClientProxy;
import snownee.lychee.util.CommonProxy;

public class LycheeKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        Lychee.LOGGER.info("LycheeKubeJSPlugin is there!");
        CommonProxy.registerCustomActionListener(this::onCustomAction);
        CommonProxy.registerCustomConditionListener(this::onCustomCondition);
        if (CommonProxy.isPhysicalClient()) {
            ClientProxy.registerInfoBadgeClickListener(this::onInfoBadgeClicked);
        }
    }

    private boolean onCustomAction(String id, CustomAction action, ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
        return LycheeKubeJSEvents.CUSTOM_ACTION.hasListeners() ? LycheeKubeJSEvents.CUSTOM_ACTION.post(ScriptType.STARTUP, id, new CustomActionEventJS(id, action, recipe, patchContext)).override() : false;
    }

    private boolean onCustomCondition(String id, CustomCondition condition) {
        return LycheeKubeJSEvents.CUSTOM_CONDITION.hasListeners() ? LycheeKubeJSEvents.CUSTOM_CONDITION.post(ScriptType.STARTUP, id, new CustomConditionEventJS(id, condition)).override() : false;
    }

    private boolean onInfoBadgeClicked(ILycheeRecipe<?> recipe, int button) {
        return LycheeKubeJSEvents.CLICKED_INFO_BADGE.hasListeners() ? LycheeKubeJSEvents.CLICKED_INFO_BADGE.post(ScriptType.CLIENT, recipe.lychee$getId(), new ClickedInfoBadgeEventJS(recipe, button)).override() : false;
    }

    @Override
    public void registerEvents() {
        LycheeKubeJSEvents.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("InteractionResult", InteractionResult.class);
        event.add("LootContextParams", LootContextParams.class);
        event.add("LycheeLootContextParams", LycheeLootContextParams.class);
        event.add("LycheeReference", Reference.class);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (typeWrappers.getWrapperFactory(LootContextParam.class, null) == null) {
            typeWrappers.registerSimple(LootContextParam.class, o -> {
                if (o instanceof LootContextParam) {
                    return (LootContextParam) o;
                } else {
                    return !(o instanceof CharSequence) && !(o instanceof ResourceLocation) ? null : (LootContextParam) LycheeLootContextParams.ALL.get(LycheeLootContextParams.trimRL(o.toString()));
                }
            });
        }
    }
}