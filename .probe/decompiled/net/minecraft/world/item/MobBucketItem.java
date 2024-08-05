package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public class MobBucketItem extends BucketItem {

    private final EntityType<?> type;

    private final SoundEvent emptySound;

    public MobBucketItem(EntityType<?> entityType0, Fluid fluid1, SoundEvent soundEvent2, Item.Properties itemProperties3) {
        super(fluid1, itemProperties3);
        this.type = entityType0;
        this.emptySound = soundEvent2;
    }

    @Override
    public void checkExtraContent(@Nullable Player player0, Level level1, ItemStack itemStack2, BlockPos blockPos3) {
        if (level1 instanceof ServerLevel) {
            this.spawn((ServerLevel) level1, itemStack2, blockPos3);
            level1.m_142346_(player0, GameEvent.ENTITY_PLACE, blockPos3);
        }
    }

    @Override
    protected void playEmptySound(@Nullable Player player0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        levelAccessor1.playSound(player0, blockPos2, this.emptySound, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerLevel serverLevel0, ItemStack itemStack1, BlockPos blockPos2) {
        if (this.type.spawn(serverLevel0, itemStack1, null, blockPos2, MobSpawnType.BUCKET, true, false) instanceof Bucketable $$4) {
            $$4.loadFromBucketTag(itemStack1.getOrCreateTag());
            $$4.setFromBucket(true);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        if (this.type == EntityType.TROPICAL_FISH) {
            CompoundTag $$4 = itemStack0.getTag();
            if ($$4 != null && $$4.contains("BucketVariantTag", 3)) {
                int $$5 = $$4.getInt("BucketVariantTag");
                ChatFormatting[] $$6 = new ChatFormatting[] { ChatFormatting.ITALIC, ChatFormatting.GRAY };
                String $$7 = "color.minecraft." + TropicalFish.getBaseColor($$5);
                String $$8 = "color.minecraft." + TropicalFish.getPatternColor($$5);
                for (int $$9 = 0; $$9 < TropicalFish.COMMON_VARIANTS.size(); $$9++) {
                    if ($$5 == ((TropicalFish.Variant) TropicalFish.COMMON_VARIANTS.get($$9)).getPackedId()) {
                        listComponent2.add(Component.translatable(TropicalFish.getPredefinedName($$9)).withStyle($$6));
                        return;
                    }
                }
                listComponent2.add(TropicalFish.getPattern($$5).displayName().plainCopy().withStyle($$6));
                MutableComponent $$10 = Component.translatable($$7);
                if (!$$7.equals($$8)) {
                    $$10.append(", ").append(Component.translatable($$8));
                }
                $$10.withStyle($$6);
                listComponent2.add($$10);
            }
        }
    }
}