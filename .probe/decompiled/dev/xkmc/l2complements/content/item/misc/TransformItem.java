package dev.xkmc.l2complements.content.item.misc;

import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class TransformItem extends TooltipItem {

    private final Supplier<EntityType<? extends Mob>> from;

    private final Supplier<EntityType<? extends Mob>> to;

    public TransformItem(Item.Properties properties, Supplier<MutableComponent> sup, Supplier<EntityType<? extends Mob>> from, Supplier<EntityType<? extends Mob>> to) {
        super(properties, sup);
        this.from = from;
        this.to = to;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = target.m_9236_();
        if (target.m_6095_() != this.from.get()) {
            return InteractionResult.FAIL;
        } else if (level.m_46791_() == Difficulty.PEACEFUL) {
            return InteractionResult.FAIL;
        } else if (level instanceof ServerLevel server) {
            if (ForgeEventFactory.canLivingConvert(target, (EntityType<? extends LivingEntity>) this.to.get(), timer -> {
            })) {
                Mob result = (Mob) ((EntityType) this.to.get()).create(level);
                assert result != null;
                result.m_7678_(target.m_20185_(), target.m_20186_(), target.m_20189_(), target.m_146908_(), target.m_146909_());
                ForgeEventFactory.onFinalizeSpawn(result, server, level.getCurrentDifficultyAt(result.m_20183_()), MobSpawnType.CONVERSION, null, null);
                result.setNoAi(((Mob) target).isNoAi());
                if (target.m_8077_()) {
                    result.m_6593_(target.m_7770_());
                    result.m_20340_(target.m_20151_());
                }
                result.setPersistenceRequired();
                ForgeEventFactory.onLivingConvert(target, result);
                level.m_7967_(result);
                target.m_146870_();
                stack.shrink(1);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.SUCCESS;
        }
    }
}