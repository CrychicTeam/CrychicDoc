package net.minecraft.world.entity.animal;

import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public interface Bucketable {

    boolean fromBucket();

    void setFromBucket(boolean var1);

    void saveToBucketTag(ItemStack var1);

    void loadFromBucketTag(CompoundTag var1);

    ItemStack getBucketItemStack();

    SoundEvent getPickupSound();

    @Deprecated
    static void saveDefaultDataToBucketTag(Mob mob0, ItemStack itemStack1) {
        CompoundTag $$2 = itemStack1.getOrCreateTag();
        if (mob0.m_8077_()) {
            itemStack1.setHoverName(mob0.m_7770_());
        }
        if (mob0.isNoAi()) {
            $$2.putBoolean("NoAI", mob0.isNoAi());
        }
        if (mob0.m_20067_()) {
            $$2.putBoolean("Silent", mob0.m_20067_());
        }
        if (mob0.m_20068_()) {
            $$2.putBoolean("NoGravity", mob0.m_20068_());
        }
        if (mob0.m_146886_()) {
            $$2.putBoolean("Glowing", mob0.m_146886_());
        }
        if (mob0.m_20147_()) {
            $$2.putBoolean("Invulnerable", mob0.m_20147_());
        }
        $$2.putFloat("Health", mob0.m_21223_());
    }

    @Deprecated
    static void loadDefaultDataFromBucketTag(Mob mob0, CompoundTag compoundTag1) {
        if (compoundTag1.contains("NoAI")) {
            mob0.setNoAi(compoundTag1.getBoolean("NoAI"));
        }
        if (compoundTag1.contains("Silent")) {
            mob0.m_20225_(compoundTag1.getBoolean("Silent"));
        }
        if (compoundTag1.contains("NoGravity")) {
            mob0.m_20242_(compoundTag1.getBoolean("NoGravity"));
        }
        if (compoundTag1.contains("Glowing")) {
            mob0.m_146915_(compoundTag1.getBoolean("Glowing"));
        }
        if (compoundTag1.contains("Invulnerable")) {
            mob0.m_20331_(compoundTag1.getBoolean("Invulnerable"));
        }
        if (compoundTag1.contains("Health", 99)) {
            mob0.m_21153_(compoundTag1.getFloat("Health"));
        }
    }

    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player0, InteractionHand interactionHand1, T t2) {
        ItemStack $$3 = player0.m_21120_(interactionHand1);
        if ($$3.getItem() == Items.WATER_BUCKET && t2.isAlive()) {
            t2.m_5496_(t2.getPickupSound(), 1.0F, 1.0F);
            ItemStack $$4 = t2.getBucketItemStack();
            t2.saveToBucketTag($$4);
            ItemStack $$5 = ItemUtils.createFilledResult($$3, player0, $$4, false);
            player0.m_21008_(interactionHand1, $$5);
            Level $$6 = t2.m_9236_();
            if (!$$6.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player0, $$4);
            }
            t2.m_146870_();
            return Optional.of(InteractionResult.sidedSuccess($$6.isClientSide));
        } else {
            return Optional.empty();
        }
    }
}