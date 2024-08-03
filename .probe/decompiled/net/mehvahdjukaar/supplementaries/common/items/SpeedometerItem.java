package net.mehvahdjukaar.supplementaries.common.items;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SpeedometerItem extends Item {

    public SpeedometerItem(Item.Properties properties) {
        super(properties);
    }

    private static double roundToSignificantFigures(double num, int n) {
        if (num == 0.0) {
            return 0.0;
        } else {
            double d = Math.ceil(Math.log10(num < 0.0 ? -num : num));
            int power = n - (int) d;
            double magnitude = Math.pow(10.0, (double) power);
            long shifted = Math.round(num * magnitude);
            return (double) shifted / magnitude;
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (player.m_9236_().isClientSide) {
            this.calculateSpeed(player, entity);
        }
        return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
    }

    private void calculateSpeed(Player player, Entity entity) {
        double speed = getBPS(entity);
        double s = roundToSignificantFigures(speed, 3);
        player.displayClientMessage(Component.translatable("message.supplementaries.speedometer", s), true);
    }

    private static double getBPS(Entity entity) {
        Entity mount = entity.getVehicle();
        Entity e = entity;
        if (mount != null) {
            e = mount;
        }
        Vec3 v = e.getDeltaMovement();
        if (e.onGround()) {
            v = v.subtract(0.0, v.y, 0.0);
        }
        return v.length() * 20.0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) {
            this.calculateSpeed(player, player);
        }
        return InteractionResultHolder.success(player.m_21120_(hand));
    }

    public static class SpeedometerItemProperty implements ItemPropertyFunction {

        @Override
        public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity player, int seed) {
            Entity entity = (Entity) (player != null ? player : stack.getEntityRepresentation());
            if (entity == null) {
                return 0.0F;
            } else {
                double speed = SpeedometerItem.getBPS(entity);
                double max = 60.0;
                return (float) Math.min(speed / max, 1.0);
            }
        }
    }
}