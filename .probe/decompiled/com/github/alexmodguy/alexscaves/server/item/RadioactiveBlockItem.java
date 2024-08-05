package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.item.BlockItemWithSupplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class RadioactiveBlockItem extends BlockItemWithSupplier {

    private final float randomChanceOfRadiation;

    public RadioactiveBlockItem(RegistryObject<Block> blockSupplier, Item.Properties props, float randomChanceOfRadiation) {
        super(blockSupplier, props);
        this.randomChanceOfRadiation = randomChanceOfRadiation;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        super.m_6883_(stack, level, entity, i, held);
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            if (living instanceof Player player && player.isCreative()) {
                return;
            }
            float stackChance = (float) stack.getCount() * this.randomChanceOfRadiation;
            float hazmatMultiplier = 1.0F - (float) HazmatArmorItem.getWornAmount(living) / 4.0F;
            if (!living.hasEffect(ACEffectRegistry.IRRADIATED.get()) && level.random.nextFloat() < stackChance * hazmatMultiplier) {
                MobEffectInstance instance = new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 1800);
                living.addEffect(instance);
                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.getId(), entity.getId(), 0, instance.getDuration()));
            }
        }
    }
}