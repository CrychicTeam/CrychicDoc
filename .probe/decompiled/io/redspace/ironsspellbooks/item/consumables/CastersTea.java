package io.redspace.ironsspellbooks.item.consumables;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CastersTea extends DrinkableItem {

    public CastersTea(Item.Properties pProperties) {
        super(pProperties, CastersTea::onConsume, null, true);
    }

    private static void onConsume(ItemStack itemStack, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            PlayerCooldowns cooldowns = MagicData.getPlayerMagicData(livingEntity).getPlayerCooldowns();
            cooldowns.getSpellCooldowns().forEach((key, value) -> cooldowns.decrementCooldown(value, (int) ((float) value.getSpellCooldown() * 0.15F)));
            cooldowns.syncToPlayer(serverPlayer);
        }
    }
}