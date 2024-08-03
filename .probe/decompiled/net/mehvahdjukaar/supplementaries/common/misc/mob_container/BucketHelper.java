package net.mehvahdjukaar.supplementaries.common.misc.mob_container;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collection;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BucketHelper {

    private static final BiMap<Item, EntityType<?>> BUCKET_TO_MOB_MAP = HashBiMap.create();

    @Nullable
    public static EntityType<?> getEntityTypeFromBucket(Item bucket) {
        BUCKET_TO_MOB_MAP.clear();
        EntityType<?> type = (EntityType<?>) BUCKET_TO_MOB_MAP.get(bucket);
        if (type != null) {
            return type;
        } else {
            if (bucket instanceof MobBucketItem bucketItem) {
                EntityType<?> en = SuppPlatformStuff.getFishType(bucketItem);
                if (en != null) {
                    associateMobToBucketIfAbsent(en, bucket);
                    return en;
                }
            } else {
                String mobId = null;
                String itemName = Utils.getID(bucket).toString();
                if (itemName.contains("_bucket")) {
                    mobId = itemName.replace("_bucket", "");
                } else if (itemName.contains("bucket_of_")) {
                    mobId = itemName.replace("_bucket", "");
                } else if (itemName.contains("bucket_")) {
                    mobId = itemName.replace("bucket_", "");
                }
                if (mobId != null) {
                    ResourceLocation res = new ResourceLocation(mobId);
                    Optional<EntityType<?>> opt = BuiltInRegistries.ENTITY_TYPE.m_6612_(res);
                    if (opt.isPresent()) {
                        EntityType<?> en = (EntityType<?>) opt.get();
                        associateMobToBucketIfAbsent(en, bucket);
                        return en;
                    }
                }
            }
            return null;
        }
    }

    @NotNull
    public static ItemStack getBucketFromEntity(Entity entity) {
        ItemStack bucket = ItemStack.EMPTY;
        if (entity instanceof Bucketable bucketable) {
            bucket = (ItemStack) Preconditions.checkNotNull(bucketable.getBucketItemStack(), "Bucketable modded entity " + Utils.getID(entity.getType()) + " returned a null bucket!");
        } else if (entity instanceof WaterAnimal) {
            bucket = tryGettingFishBucketHackery(entity, entity.level());
        } else if (CompatHandler.QUARK) {
            ItemStack b = QuarkCompat.getSlimeBucket(entity);
            if (!b.isEmpty()) {
                bucket = b;
            }
        }
        if (!bucket.isEmpty()) {
            associateMobToBucketIfAbsent(entity.getType(), bucket.getItem());
        }
        return bucket;
    }

    private static ItemStack tryGettingFishBucketHackery(Entity entity, Level level) {
        ItemStack bucket = ItemStack.EMPTY;
        Player player = FakePlayerManager.getDefault(level);
        if (player != null) {
            player.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
            InteractionResult result = entity.interact(player, InteractionHand.MAIN_HAND);
            if (!result.consumesAction()) {
                player.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                result = entity.interact(player, InteractionHand.MAIN_HAND);
            }
            if (result.consumesAction()) {
                ItemStack filledBucket = player.m_21120_(InteractionHand.MAIN_HAND);
                if (!filledBucket.isEmpty() && !entity.isAlive()) {
                    bucket = filledBucket;
                }
            }
        }
        return bucket;
    }

    public static Collection<Item> getValidBuckets() {
        return BUCKET_TO_MOB_MAP.keySet();
    }

    public static boolean isFishBucket(Item item) {
        return getEntityTypeFromBucket(item) != null;
    }

    private static void associateMobToBucketIfAbsent(EntityType<?> entity, Item item) {
        if (!BUCKET_TO_MOB_MAP.inverse().containsKey(entity)) {
            BUCKET_TO_MOB_MAP.putIfAbsent(item, entity);
        }
    }

    public static boolean isModdedFish(Entity entity) {
        return entity instanceof Bucketable;
    }
}