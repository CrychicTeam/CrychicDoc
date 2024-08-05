package snownee.lychee.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.text.MessageFormat;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import snownee.lychee.PostActionTypes;
import snownee.lychee.client.core.post.CycleStatePropertyPostActionRenderer;
import snownee.lychee.client.core.post.IfPostActionRenderer;
import snownee.lychee.client.core.post.ItemBasedPostActionRenderer;
import snownee.lychee.client.core.post.ItemStackPostActionRenderer;
import snownee.lychee.client.core.post.PlaceBlockPostActionRenderer;
import snownee.lychee.client.core.post.PostActionRenderer;
import snownee.lychee.compat.IngredientInfo;
import snownee.lychee.core.post.DropXp;
import snownee.lychee.core.post.Execute;
import snownee.lychee.core.post.Explode;
import snownee.lychee.core.post.Hurt;
import snownee.lychee.core.post.input.DamageItem;
import snownee.lychee.core.post.input.PreventDefault;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.dripstone_dripping.DripstoneRecipeMod;
import snownee.lychee.dripstone_dripping.client.ParticleFactories;

public interface ClientProxy {

    List<ClientProxy.RecipeViewerWidgetClickListener> recipeViewerWidgetClickListeners = ObjectArrayList.of();

    static MutableComponent getDimensionDisplayName(ResourceKey<Level> dimension) {
        String key = Util.makeDescriptionId("dimension", dimension.location());
        return I18n.exists(key) ? Component.translatable(key) : Component.literal(CommonProxy.capitaliseAllWords(dimension.location().getPath()));
    }

    static MutableComponent getStructureDisplayName(ResourceLocation rawName) {
        String key = Util.makeDescriptionId("structure", rawName);
        return I18n.exists(key) ? Component.translatable(key) : Component.literal(CommonProxy.capitaliseAllWords(rawName.getPath()));
    }

    static MutableComponent format(String s, Object... objects) {
        try {
            return Component.literal(MessageFormat.format(I18n.get(s), objects));
        } catch (Exception var3) {
            return Component.translatable(s, objects);
        }
    }

    static void registerInfoBadgeClickListener(ClientProxy.RecipeViewerWidgetClickListener listener) {
        synchronized (recipeViewerWidgetClickListeners) {
            recipeViewerWidgetClickListeners.add(listener);
        }
    }

    static boolean postInfoBadgeClickEvent(ILycheeRecipe<?> recipe, int button) {
        for (ClientProxy.RecipeViewerWidgetClickListener listener : recipeViewerWidgetClickListeners) {
            if (listener.onClick(recipe, button)) {
                return true;
            }
        }
        return false;
    }

    static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(event -> {
            event.registerSpriteSet(DripstoneRecipeMod.DRIPSTONE_DRIPPING, ParticleFactories.Dripping::new);
            event.registerSpriteSet(DripstoneRecipeMod.DRIPSTONE_FALLING, ParticleFactories.Falling::new);
            event.registerSpriteSet(DripstoneRecipeMod.DRIPSTONE_SPLASH, ParticleFactories.Splash::new);
        });
    }

    static void registerPostActionRenderers() {
        PostActionRenderer.register(PostActionTypes.DROP_ITEM, (ItemStackPostActionRenderer) action -> action.stack);
        PostActionRenderer.register(PostActionTypes.SET_ITEM, (ItemStackPostActionRenderer) action -> action.stack);
        PostActionRenderer.register(PostActionTypes.DROP_XP, (ItemBasedPostActionRenderer<DropXp>) action -> Items.EXPERIENCE_BOTTLE.getDefaultInstance());
        PostActionRenderer.register(PostActionTypes.EXECUTE, (ItemBasedPostActionRenderer<Execute>) action -> Items.COMMAND_BLOCK.getDefaultInstance());
        PostActionRenderer.register(PostActionTypes.EXPLODE, (ItemBasedPostActionRenderer<Explode>) action -> Items.TNT.getDefaultInstance());
        PostActionRenderer.register(PostActionTypes.HURT, (ItemBasedPostActionRenderer<Hurt>) action -> Items.IRON_SWORD.getDefaultInstance());
        PostActionRenderer.register(PostActionTypes.IF, new IfPostActionRenderer());
        PostActionRenderer.register(PostActionTypes.PLACE, new PlaceBlockPostActionRenderer());
        PostActionRenderer.register(PostActionTypes.CYCLE_STATE_PROPERTY, new CycleStatePropertyPostActionRenderer());
        PostActionRenderer.register(PostActionTypes.DAMAGE_ITEM, new PostActionRenderer<DamageItem>() {

            public void loadCatalystsInfo(DamageItem action, ILycheeRecipe<?> recipe, List<IngredientInfo> ingredients) {
                String key = CommonProxy.makeDescriptionId("postAction", PostActionTypes.DAMAGE_ITEM.getRegistryName());
                Component component = Component.translatable(key, action.damage).withStyle(ChatFormatting.YELLOW);
                Minecraft mc = Minecraft.getInstance();
                recipe.getItemIndexes(action.target).forEach(i -> {
                    IngredientInfo info = (IngredientInfo) ingredients.get(i);
                    info.addTooltip(component);
                    action.getConditionTooltips(info.tooltips, 0, mc.level, mc.player);
                    info.isCatalyst = true;
                });
            }
        });
        PostActionRenderer.register(PostActionTypes.PREVENT_DEFAULT, new PostActionRenderer<PreventDefault>() {

            public void loadCatalystsInfo(PreventDefault action, ILycheeRecipe<?> recipe, List<IngredientInfo> ingredients) {
                if (recipe instanceof LycheeRecipe<?> lycheeRecipe && lycheeRecipe.getType().canPreventConsumeInputs) {
                    Minecraft mc = Minecraft.getInstance();
                    for (IngredientInfo ingredient : ingredients) {
                        if (ingredient.tooltips.isEmpty()) {
                            ingredient.addTooltip(lycheeRecipe.getType().getPreventDefaultDescription(lycheeRecipe));
                            action.getConditionTooltips(ingredient.tooltips, 0, mc.level, mc.player);
                            ingredient.isCatalyst = true;
                        }
                    }
                    return;
                }
            }
        });
    }

    static boolean isSinglePlayer() {
        return Minecraft.getInstance().getSingleplayerServer() != null;
    }

    @FunctionalInterface
    public interface RecipeViewerWidgetClickListener {

        boolean onClick(ILycheeRecipe<?> var1, int var2);
    }
}