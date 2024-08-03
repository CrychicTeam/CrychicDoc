package net.mehvahdjukaar.supplementaries.common.items;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus.Internal;

public class CandyItem extends Item {

    private static final FoodProperties CANDIE_FOOD = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).fast().alwaysEat().build();

    private static final int SUGAR_PER_CANDY = 200;

    private static final int EFFECT_THRESHOLD = 1600;

    private static final Object2IntMap<UUID> SWEET_TOOTH_COUNTER = (Object2IntMap<UUID>) (PlatHelper.getPhysicalSide().isServer() ? new Object2IntOpenHashMap() : new Object2IntArrayMap());

    @Internal
    public static void checkSweetTooth(Player entity) {
        UUID id = entity.m_20148_();
        int newValue = SWEET_TOOTH_COUNTER.computeIntIfPresent(id, (k, i) -> i - 1);
        if (newValue <= 0) {
            SWEET_TOOTH_COUNTER.removeInt(id);
        }
    }

    public CandyItem(Item.Properties properties) {
        super(properties.food(CANDIE_FOOD));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        increaseSweetTooth(world, entity, 200);
        return super.finishUsingItem(stack, world, entity);
    }

    public static void increaseSweetTooth(Level world, LivingEntity entity, int amount) {
        if (!world.isClientSide && entity instanceof Player) {
            UUID id = entity.m_20148_();
            int i = SWEET_TOOTH_COUNTER.mergeInt(id, amount, Integer::sum);
            if (i > 1600) {
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400));
            }
        }
    }
}