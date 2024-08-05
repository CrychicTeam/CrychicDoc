package net.minecraft.server;

import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootDataManager;
import org.slf4j.Logger;

public class ReloadableServerResources {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);

    private final CommandBuildContext.Configurable commandBuildContext;

    private final Commands commands;

    private final RecipeManager recipes = new RecipeManager();

    private final TagManager tagManager;

    private final LootDataManager lootData = new LootDataManager();

    private final ServerAdvancementManager advancements = new ServerAdvancementManager(this.lootData);

    private final ServerFunctionLibrary functionLibrary;

    public ReloadableServerResources(RegistryAccess.Frozen registryAccessFrozen0, FeatureFlagSet featureFlagSet1, Commands.CommandSelection commandsCommandSelection2, int int3) {
        this.tagManager = new TagManager(registryAccessFrozen0);
        this.commandBuildContext = CommandBuildContext.configurable(registryAccessFrozen0, featureFlagSet1);
        this.commands = new Commands(commandsCommandSelection2, this.commandBuildContext);
        this.commandBuildContext.missingTagAccessPolicy(CommandBuildContext.MissingTagAccessPolicy.CREATE_NEW);
        this.functionLibrary = new ServerFunctionLibrary(int3, this.commands.getDispatcher());
    }

    public ServerFunctionLibrary getFunctionLibrary() {
        return this.functionLibrary;
    }

    public LootDataManager getLootData() {
        return this.lootData;
    }

    public RecipeManager getRecipeManager() {
        return this.recipes;
    }

    public Commands getCommands() {
        return this.commands;
    }

    public ServerAdvancementManager getAdvancements() {
        return this.advancements;
    }

    public List<PreparableReloadListener> listeners() {
        return List.of(this.tagManager, this.lootData, this.recipes, this.functionLibrary, this.advancements);
    }

    public static CompletableFuture<ReloadableServerResources> loadResources(ResourceManager resourceManager0, RegistryAccess.Frozen registryAccessFrozen1, FeatureFlagSet featureFlagSet2, Commands.CommandSelection commandsCommandSelection3, int int4, Executor executor5, Executor executor6) {
        ReloadableServerResources $$7 = new ReloadableServerResources(registryAccessFrozen1, featureFlagSet2, commandsCommandSelection3, int4);
        return SimpleReloadInstance.create(resourceManager0, $$7.listeners(), executor5, executor6, DATA_RELOAD_INITIAL_TASK, LOGGER.isDebugEnabled()).done().whenComplete((p_255534_, p_255535_) -> $$7.commandBuildContext.missingTagAccessPolicy(CommandBuildContext.MissingTagAccessPolicy.FAIL)).thenApply(p_214306_ -> $$7);
    }

    public void updateRegistryTags(RegistryAccess registryAccess0) {
        this.tagManager.getResult().forEach(p_214315_ -> updateRegistryTags(registryAccess0, p_214315_));
        Blocks.rebuildCache();
    }

    private static <T> void updateRegistryTags(RegistryAccess registryAccess0, TagManager.LoadResult<T> tagManagerLoadResultT1) {
        ResourceKey<? extends Registry<T>> $$2 = tagManagerLoadResultT1.key();
        Map<TagKey<T>, List<Holder<T>>> $$3 = (Map<TagKey<T>, List<Holder<T>>>) tagManagerLoadResultT1.tags().entrySet().stream().collect(Collectors.toUnmodifiableMap(p_214303_ -> TagKey.create($$2, (ResourceLocation) p_214303_.getKey()), p_214312_ -> List.copyOf((Collection) p_214312_.getValue())));
        registryAccess0.registryOrThrow($$2).bindTags($$3);
    }
}