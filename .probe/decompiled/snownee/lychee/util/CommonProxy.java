package snownee.lychee.util;

import com.google.common.collect.Streams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeConfig;
import snownee.lychee.LycheeRegistries;
import snownee.lychee.LycheeTags;
import snownee.lychee.PostActionTypes;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.compat.IngredientInfo;
import snownee.lychee.compat.forge.AlwaysTrueIngredient;
import snownee.lychee.core.contextual.CustomCondition;
import snownee.lychee.core.post.CustomAction;
import snownee.lychee.core.post.Explode;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.dripstone_dripping.DripstoneRecipeMod;
import snownee.lychee.interaction.InteractionRecipeMod;
import snownee.lychee.item_exploding.ItemExplodingRecipe;
import snownee.lychee.mixin.RecipeManagerAccess;

@Mod("lychee")
public class CommonProxy {

    private static final Random RANDOM = new Random();

    private static final List<CommonProxy.CustomActionListener> customActionListeners = ObjectArrayList.of();

    private static final List<CommonProxy.CustomConditionListener> customConditionListeners = ObjectArrayList.of();

    public static boolean hasKiwi = isModLoaded("kiwi");

    public static boolean hasDFLib = isModLoaded("dripstone_fluid_lib");

    private static RecipeManager recipeManager;

    public CommonProxy() {
        LycheeTags.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(CommonProxy::newRegistries);
        modEventBus.addListener(CommonProxy::register);
        modEventBus.addListener(CommonProxy::registerRecipeBookCategories);
        MinecraftForge.EVENT_BUS.addListener(event -> {
            InteractionResult result = InteractionRecipeMod.useItemOn(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            event.setCanceled(result.consumesAction());
            event.setCancellationResult(result);
        });
        MinecraftForge.EVENT_BUS.addListener(event -> {
            InteractionResult result = InteractionRecipeMod.clickItemOn(event.getEntity(), event.getLevel(), event.getHand(), event.getPos(), event.getFace());
            event.setCanceled(result.consumesAction());
            event.setCancellationResult(result);
        });
        MinecraftForge.EVENT_BUS.addListener(event -> {
            Explosion explosion = event.getExplosion();
            ItemExplodingRecipe.on(event.getLevel(), explosion.x, explosion.y, explosion.z, event.getAffectedEntities(), explosion.radius);
        });
        if (isPhysicalClient()) {
            ClientProxy.init();
        }
    }

    public static void newRegistries(NewRegistryEvent event) {
        LycheeRegistries.init(event);
    }

    public static void register(RegisterEvent event) {
        event.register(LycheeRegistries.CONTEXTUAL.key(), helper -> ContextualConditionTypes.init());
        event.register(LycheeRegistries.POST_ACTION.key(), helper -> {
            PostActionTypes.init();
            if (isPhysicalClient()) {
                ClientProxy.registerPostActionRenderers();
            }
        });
        event.register(ForgeRegistries.RECIPE_SERIALIZERS.getRegistryKey(), helper -> {
            RecipeSerializers.init();
            CraftingHelper.register(new ResourceLocation("lychee", "always_true"), AlwaysTrueIngredient.Serializer.INSTANCE);
        });
        event.register(ForgeRegistries.RECIPE_TYPES.getRegistryKey(), helper -> RecipeTypes.init());
        event.register(ForgeRegistries.PARTICLE_TYPES.getRegistryKey(), helper -> {
            helper.register(new ResourceLocation("lychee", "dripstone_dripping"), DripstoneRecipeMod.DRIPSTONE_DRIPPING);
            helper.register(new ResourceLocation("lychee", "dripstone_falling"), DripstoneRecipeMod.DRIPSTONE_FALLING);
            helper.register(new ResourceLocation("lychee", "dripstone_splash"), DripstoneRecipeMod.DRIPSTONE_SPLASH);
        });
    }

    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        Function<Recipe<?>, RecipeBookCategories> lookup = $ -> RecipeBookCategories.UNKNOWN;
        RecipeTypes.ALL.forEach($ -> event.registerRecipeCategoryFinder($, lookup));
    }

    public static void dropItemStack(Level pLevel, double pX, double pY, double pZ, ItemStack pStack, @Nullable Consumer<ItemEntity> extraStep) {
        while (!pStack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(pLevel, pX + RANDOM.nextGaussian() * 0.1 - 0.05, pY, pZ + RANDOM.nextGaussian() * 0.1 - 0.05, pStack.split(Math.min(RANDOM.nextInt(21) + 10, pStack.getMaxStackSize())));
            itementity.m_20334_(RANDOM.nextGaussian() * 0.05 - 0.025, RANDOM.nextGaussian() * 0.05 + 0.2, RANDOM.nextGaussian() * 0.05 - 0.025);
            if (extraStep != null) {
                extraStep.accept(itementity);
            }
            pLevel.m_7967_(itementity);
        }
    }

    public static String makeDescriptionId(String pType, @Nullable ResourceLocation pId) {
        return pId == null ? pType + ".unregistered_sadface" : pType + "." + wrapNamespace(pId.getNamespace()) + "." + pId.getPath().replace('/', '.');
    }

    public static String wrapNamespace(String modid) {
        return "minecraft".equals(modid) ? "lychee" : modid;
    }

    public static MutableComponent white(CharSequence s) {
        return Component.literal(s.toString()).withStyle(ChatFormatting.WHITE);
    }

    public static String chance(float chance) {
        return ((double) chance < 0.01 ? "<1" : String.valueOf((int) (chance * 100.0F))) + "%";
    }

    public static String capitaliseAllWords(String str) {
        int sz = str.length();
        StringBuilder buffer = new StringBuilder(sz);
        boolean space = true;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                space = true;
            } else if (space) {
                buffer.append(Character.toTitleCase(ch));
                space = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static <T> T getCycledItem(List<T> list, T fallback, int interval) {
        if (list.isEmpty()) {
            return fallback;
        } else if (list.size() == 1) {
            return (T) list.get(0);
        } else {
            long index = System.currentTimeMillis() / (long) interval % (long) list.size();
            return (T) list.get(Math.toIntExact(index));
        }
    }

    public static ResourceLocation readNullableRL(FriendlyByteBuf buf) {
        String string = buf.readUtf();
        return string.isEmpty() ? null : new ResourceLocation(string);
    }

    public static void writeNullableRL(ResourceLocation rl, FriendlyByteBuf buf) {
        if (rl == null) {
            buf.writeUtf("");
        } else {
            buf.writeUtf(rl.toString());
        }
    }

    public static InteractionResult interactionResult(Boolean bool) {
        if (bool == null) {
            return InteractionResult.PASS;
        } else {
            return bool ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    public static <T> T readRegistryId(LycheeRegistries.MappedRegistry<T> registry, FriendlyByteBuf buf) {
        return (T) buf.readRegistryIdUnsafe(registry.registry());
    }

    public static <T> void writeRegistryId(LycheeRegistries.MappedRegistry<T> registry, T entry, FriendlyByteBuf buf) {
        buf.writeRegistryIdUnsafe(registry.registry(), entry);
    }

    public static <T> T readRegistryId(Registry<T> registry, FriendlyByteBuf buf) {
        return (T) buf.readRegistryIdUnsafe(RegistryManager.ACTIVE.getRegistry(registry.key()));
    }

    public static <T> void writeRegistryId(Registry<T> registry, T entry, FriendlyByteBuf buf) {
        buf.writeRegistryIdUnsafe(RegistryManager.ACTIVE.getRegistry(registry.key()), entry);
    }

    public static RecipeManager recipeManager() {
        if (recipeManager == null && FMLEnvironment.dist.isClient()) {
            if (LycheeConfig.debug) {
                Lychee.LOGGER.trace("Early loading recipes..");
            }
            recipeManager = Minecraft.getInstance().getConnection().getRecipeManager();
        }
        return recipeManager;
    }

    public static void setRecipeManager(RecipeManager recipeManager, boolean fromServer) {
        if (fromServer || !isPhysicalClient() || !ClientProxy.isSinglePlayer()) {
            CommonProxy.recipeManager = recipeManager;
            if (LycheeConfig.debug) {
                Lychee.LOGGER.trace("Setting recipe manager..");
            }
        }
    }

    public static Recipe<?> recipe(ResourceLocation id) {
        return (Recipe<?>) recipeManager().byKey(id).orElse(null);
    }

    public static <T extends Recipe<?>> Collection<T> recipes(RecipeType<T> type) {
        return ((RecipeManagerAccess) recipeManager()).callByType(type).values();
    }

    public static BlockPos getOnPos(Entity entity) {
        int i = Mth.floor(entity.getX());
        int j = Mth.floor(entity.getY() - 0.05);
        int k = Mth.floor(entity.getZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        if (entity.level().m_46859_(blockpos)) {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = entity.level().getBlockState(blockpos1);
            if (blockstate.collisionExtendsVertically(entity.level(), blockpos1, entity)) {
                return blockpos1;
            }
        }
        return blockpos;
    }

    public static Vec3 clampPos(Vec3 origin, BlockPos pos) {
        double x = clamp(origin.x, pos.m_123341_());
        double y = clamp(origin.y, pos.m_123342_());
        double z = clamp(origin.z, pos.m_123343_());
        return x == origin.x && y == origin.y && z == origin.z ? origin : new Vec3(x, y, z);
    }

    private static double clamp(double v, int target) {
        if (v < (double) target) {
            return (double) target;
        } else {
            return v >= (double) (target + 1) ? (double) target + 0.999999 : v;
        }
    }

    public static <T> List<T> tagElements(Registry<T> registry, TagKey<T> tag) {
        return Streams.stream(registry.getTagOrEmpty(tag)).map(Holder::m_203334_).toList();
    }

    public static boolean isSimpleIngredient(Ingredient ingredient) {
        return ingredient.isSimple();
    }

    public static BlockPos parseOffset(JsonObject o) {
        int x = GsonHelper.getAsInt(o, "offsetX", 0);
        int y = GsonHelper.getAsInt(o, "offsetY", 0);
        int z = GsonHelper.getAsInt(o, "offsetZ", 0);
        BlockPos offset = BlockPos.ZERO;
        if (x != 0 || y != 0 || z != 0) {
            offset = new BlockPos(x, y, z);
        }
        return offset;
    }

    public static boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
    }

    public static void itemstackToJson(ItemStack stack, JsonObject jsonObject) {
        jsonObject.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.hasTag()) {
            jsonObject.addProperty("nbt", stack.getTag().toString());
        }
        if (stack.getCount() > 1) {
            jsonObject.addProperty("count", stack.getCount());
        }
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static JsonObject tagToJson(CompoundTag tag) {
        return NbtOps.INSTANCE.<JsonElement>convertTo(JsonOps.INSTANCE, tag).getAsJsonObject();
    }

    public static CompoundTag jsonToTag(JsonElement json) {
        if (json.isJsonObject()) {
            return (CompoundTag) JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, json);
        } else {
            try {
                return TagParser.parseTag(json.getAsString());
            } catch (CommandSyntaxException var2) {
                throw new IllegalArgumentException(var2);
            }
        }
    }

    public static synchronized void registerCustomActionListener(CommonProxy.CustomActionListener listener) {
        customActionListeners.add(listener);
    }

    public static synchronized void registerCustomConditionListener(CommonProxy.CustomConditionListener listener) {
        customConditionListeners.add(listener);
    }

    public static synchronized void postCustomActionEvent(String id, CustomAction action, ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
        for (CommonProxy.CustomActionListener listener : customActionListeners) {
            if (listener.on(id, action, recipe, patchContext)) {
                return;
            }
        }
    }

    public static synchronized void postCustomConditionEvent(String id, CustomCondition condition) {
        for (CommonProxy.CustomConditionListener listener : customConditionListeners) {
            if (listener.on(id, condition)) {
                return;
            }
        }
    }

    public static boolean hasModdedDripParticle(FluidState fluid) {
        return false;
    }

    public static IngredientInfo.Type getIngredientType(Ingredient ingredient) {
        if (ingredient == LycheeRecipe.Serializer.EMPTY_INGREDIENT) {
            return IngredientInfo.Type.AIR;
        } else {
            return ingredient.getSerializer() == AlwaysTrueIngredient.Serializer.INSTANCE ? IngredientInfo.Type.ANY : IngredientInfo.Type.NORMAL;
        }
    }

    public static ItemStack dispensePlacement(BlockSource pSource, ItemStack pStack, Direction direction) {
        if (pStack.getItem() instanceof BlockItem item) {
            BlockPos var6 = pSource.getPos().relative(direction);
            BlockState state = pSource.getLevel().m_8055_(var6);
            if (FallingBlock.isFree(state)) {
                item.place(new DirectionalPlaceContext(pSource.getLevel(), var6, direction, pStack, direction));
            }
            return pStack;
        } else {
            return pStack;
        }
    }

    public static void explode(Explode action, ServerLevel level, Vec3 pos, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, float radius) {
        Explosion explosion = new Explosion(level, entity, damageSource, damageCalculator, pos.x, pos.y, pos.z, radius, action.fire, action.blockInteraction);
        explosion.explode();
        explosion.finalizeExplosion(true);
        if (!explosion.interactsWithBlocks()) {
            explosion.clearToBlow();
        }
        for (ServerPlayer player : level.players()) {
            if (player.m_20238_(pos) < 4096.0) {
                player.connection.send(new ClientboundExplodePacket(pos.x, pos.y, pos.z, radius, explosion.getToBlow(), (Vec3) explosion.getHitPlayers().get(player)));
            }
        }
    }

    public static ParticleType<BlockParticleOption> registerParticleType(ParticleOptions.Deserializer<BlockParticleOption> deserializer) {
        return new ParticleType<BlockParticleOption>(false, deserializer) {

            @Override
            public Codec<BlockParticleOption> codec() {
                return BlockParticleOption.codec(this);
            }
        };
    }

    public interface CustomActionListener {

        boolean on(String var1, CustomAction var2, ILycheeRecipe<?> var3, ILycheeRecipe.NBTPatchContext var4);
    }

    public interface CustomConditionListener {

        boolean on(String var1, CustomCondition var2);
    }
}