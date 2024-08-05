package net.mehvahdjukaar.moonlight.api.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.BaseMapCodec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.forge.UtilsImpl;
import net.mehvahdjukaar.moonlight.core.map.MapDataInternal;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatType;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Utils {

    public static void swapItem(Player player, InteractionHand hand, ItemStack oldItem, ItemStack newItem, boolean bothSides) {
        if (!player.m_9236_().isClientSide || bothSides) {
            player.m_21008_(hand, ItemUtils.createFilledResult(oldItem.copy(), player, newItem, player.isCreative()));
        }
    }

    public static void swapItem(Player player, InteractionHand hand, ItemStack oldItem, ItemStack newItem) {
        swapItem(player, hand, oldItem, newItem, false);
    }

    public static void swapItemNBT(Player player, InteractionHand hand, ItemStack oldItem, ItemStack newItem) {
        if (!player.m_9236_().isClientSide) {
            player.m_21008_(hand, ItemUtils.createFilledResult(oldItem.copy(), player, newItem, false));
        }
    }

    public static void swapItem(Player player, InteractionHand hand, ItemStack newItem) {
        swapItem(player, hand, player.m_21120_(hand), newItem);
    }

    public static void addStackToExisting(Player player, ItemStack stack, boolean avoidHands) {
        Inventory inv = player.getInventory();
        boolean added = false;
        for (int j = 0; j < inv.items.size(); j++) {
            if (inv.getItem(j).is(stack.getItem()) && inv.add(j, stack)) {
                added = true;
                break;
            }
        }
        if (avoidHands && !added) {
            for (int jx = 0; jx < inv.items.size(); jx++) {
                if (inv.getItem(jx).isEmpty() && jx != inv.selected && inv.add(jx, stack)) {
                    added = true;
                    break;
                }
            }
        }
        if (!added && inv.add(stack)) {
            player.drop(stack, false);
        }
    }

    public static int getXPinaBottle(int bottleCount, RandomSource rand) {
        int xp = 0;
        for (int i = 0; i < bottleCount; i++) {
            xp += 3 + rand.nextInt(5) + rand.nextInt(5);
        }
        return xp;
    }

    public static ResourceLocation getID(Block object) {
        return BuiltInRegistries.BLOCK.getKey(object);
    }

    public static ResourceLocation getID(EntityType<?> object) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(object);
    }

    public static ResourceLocation getID(Biome object) {
        return hackyGetRegistry(Registries.BIOME).getKey(object);
    }

    public static ResourceLocation getID(DamageType type) {
        return hackyGetRegistry(Registries.DAMAGE_TYPE).getKey(type);
    }

    public static ResourceLocation getID(ConfiguredFeature<?, ?> object) {
        return hackyGetRegistry(Registries.CONFIGURED_FEATURE).getKey(object);
    }

    public static ResourceLocation getID(Item object) {
        return BuiltInRegistries.ITEM.getKey(object);
    }

    public static ResourceLocation getID(Fluid object) {
        return BuiltInRegistries.FLUID.getKey(object);
    }

    public static ResourceLocation getID(BlockEntityType<?> object) {
        return BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(object);
    }

    public static ResourceLocation getID(RecipeSerializer<?> object) {
        return BuiltInRegistries.RECIPE_SERIALIZER.getKey(object);
    }

    public static ResourceLocation getID(SoftFluid object) {
        return SoftFluidRegistry.hackyGetRegistry().getKey(object);
    }

    public static ResourceLocation getID(MapDecorationType<?, ?> object) {
        return MapDataInternal.hackyGetRegistry().getKey(object);
    }

    public static ResourceLocation getID(Potion object) {
        return BuiltInRegistries.POTION.getKey(object);
    }

    public static ResourceLocation getID(MobEffect object) {
        return BuiltInRegistries.MOB_EFFECT.getKey(object);
    }

    public static ResourceLocation getID(CreativeModeTab object) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getKey(object);
    }

    public static ResourceLocation getID(StatType<?> object) {
        return BuiltInRegistries.STAT_TYPE.getKey(object);
    }

    public static ResourceLocation getID(Object object) {
        if (object instanceof Block b) {
            return getID(b);
        } else if (object instanceof Item b) {
            return getID(b);
        } else if (object instanceof EntityType<?> b) {
            return getID(b);
        } else if (object instanceof BlockEntityType<?> b) {
            return getID(b);
        } else if (object instanceof Biome b) {
            return getID(b);
        } else if (object instanceof Fluid b) {
            return getID(b);
        } else if (object instanceof RecipeSerializer<?> b) {
            return getID(b);
        } else if (object instanceof ConfiguredFeature<?, ?> c) {
            return getID(c);
        } else if (object instanceof Potion c) {
            return getID(c);
        } else if (object instanceof MobEffect c) {
            return getID(c);
        } else if (object instanceof Supplier<?> s) {
            return getID(s.get());
        } else if (object instanceof SoftFluid s) {
            return getID(s);
        } else if (object instanceof MapDecorationType<?, ?> s) {
            return getID(s);
        } else if (object instanceof CreativeModeTab t) {
            return getID(t);
        } else if (object instanceof DamageType t) {
            return getID(t);
        } else if (object instanceof StatType t) {
            return getID(t);
        } else {
            throw new UnsupportedOperationException("Unsupported class type " + object.getClass() + ". Expected a registry entry for a call to Utils.getID()");
        }
    }

    @Deprecated(forRemoval = true)
    public static <T> boolean isTagged(T entry, Registry<T> registry, TagKey<T> tag) {
        return registry.wrapAsHolder(entry).is(tag);
    }

    public static RegistryAccess hackyGetRegistryAccess() {
        MinecraftServer s = PlatHelper.getCurrentServer();
        if (s != null) {
            return s.registryAccess();
        } else if (PlatHelper.getPhysicalSide().isClient()) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                return level.m_9598_();
            } else {
                throw new UnsupportedOperationException("Failed to get registry access: level was null");
            }
        } else {
            throw new UnsupportedOperationException("Failed to get registry access. This is a bug");
        }
    }

    public static <T> Registry<T> hackyGetRegistry(ResourceKey<Registry<T>> registry) {
        return hackyGetRegistryAccess().registryOrThrow(registry);
    }

    public static BlockBehaviour.Properties copyPropertySafe(Block blockBehaviour) {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(blockBehaviour);
        BlockState state = blockBehaviour.defaultBlockState();
        p.lightLevel(s -> state.m_60791_());
        p.offsetType(BlockBehaviour.OffsetType.NONE);
        p.isValidSpawn((blockState, blockGetter, pos, entityType) -> blockState.m_60783_(blockGetter, pos, Direction.UP) && blockState.m_60791_() < 14);
        p.mapColor(blockBehaviour.m_284356_());
        p.emissiveRendering((blockState, blockGetter, blockPos) -> false);
        return p;
    }

    @Deprecated(forRemoval = true)
    public static HitResult rayTrace(LivingEntity entity, Level world, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        return rayTrace(entity, world, blockMode, fluidMode, ForgeHelper.getReachDistance(entity));
    }

    @Deprecated(forRemoval = true)
    public static HitResult rayTrace(Entity entity, Level world, ClipContext.Block blockMode, ClipContext.Fluid fluidMode, double range) {
        Vec3 startPos = entity.getEyePosition();
        Vec3 ray = entity.getViewVector(1.0F).scale(range);
        Vec3 endPos = startPos.add(ray);
        ClipContext context = new ClipContext(startPos, endPos, blockMode, fluidMode, entity);
        return world.m_45547_(context);
    }

    public static void awardAdvancement(ServerPlayer sp, ResourceLocation name) {
        awardAdvancement(sp, name, "unlock");
    }

    public static void awardAdvancement(ServerPlayer sp, ResourceLocation name, String unlockProp) {
        Advancement advancement = sp.m_20194_().getAdvancements().getAdvancement(name);
        if (advancement != null) {
            PlayerAdvancements advancements = sp.getAdvancements();
            if (!advancements.getOrStartProgress(advancement).isDone()) {
                advancements.award(advancement, unlockProp);
            }
        }
    }

    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> getTicker(BlockEntityType<A> type, BlockEntityType<E> targetType, BlockEntityTicker<? super E> ticker) {
        return targetType == type ? ticker : null;
    }

    public static BlockState readBlockState(CompoundTag compound, @Nullable Level level) {
        HolderGetter<Block> holderGetter = (HolderGetter<Block>) (level != null ? level.m_246945_(Registries.BLOCK) : BuiltInRegistries.BLOCK.m_255303_());
        return NbtUtils.readBlockState(holderGetter, compound);
    }

    public static <T extends Comparable<T>, A extends Property<T>> BlockState replaceProperty(BlockState from, BlockState to, A property) {
        return from.m_61138_(property) ? (BlockState) to.m_61124_(property, from.m_61143_(property)) : to;
    }

    @Deprecated(forRemoval = true)
    public static boolean mayBuild(Player player, BlockPos pos) {
        return mayPerformBlockAction(player, pos, player.m_21205_());
    }

    @Deprecated(forRemoval = true)
    public static boolean mayPerformBlockAction(Player player, BlockPos pos) {
        return mayPerformBlockAction(player, pos, player.m_21205_());
    }

    public static boolean mayPerformBlockAction(Player player, BlockPos pos, ItemStack stack) {
        GameType gameMode;
        if (player instanceof ServerPlayer sp) {
            gameMode = sp.gameMode.getGameModeForPlayer();
        } else {
            gameMode = Minecraft.getInstance().gameMode.getPlayerMode();
        }
        boolean result = !player.blockActionRestricted(player.m_9236_(), pos, gameMode);
        return !result && gameMode == GameType.ADVENTURE && !stack.isEmpty() && stack.hasAdventureModePlaceTagForBlock(player.m_9236_().registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(player.m_9236_(), pos, false)) ? true : result;
    }

    public static boolean isMethodImplemented(Class<?> original, Class<?> subclass, String name) {
        Method declaredMethod = findMethodWithMatchingName(subclass, name);
        Method modMethod = findMethodWithMatchingName(original, name);
        return declaredMethod != null && modMethod != null && Arrays.equals(declaredMethod.getParameterTypes(), modMethod.getParameterTypes());
    }

    private static Method findMethodWithMatchingName(Class<?> clazz, String name) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    @ExpectPlatform
    @Transformed
    public static <K, V, C extends BaseMapCodec<K, V> & Codec<Map<K, V>>> C optionalMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
        return UtilsImpl.optionalMapCodec(keyCodec, elementCodec);
    }
}