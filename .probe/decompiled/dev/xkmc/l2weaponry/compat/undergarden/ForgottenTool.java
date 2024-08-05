package dev.xkmc.l2weaponry.compat.undergarden;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgottenTool extends ExtraToolConfig implements LWExtraConfig {

    public float getDestroySpeed(ItemStack stack, BlockState state, float old) {
        float ans = 1.0F;
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(state.m_60734_());
        if (rl != null && rl.getNamespace().equals("undergarden")) {
            ans = 1.5F;
        }
        return ans * super.getDestroySpeed(stack, state, old);
    }

    @Override
    public void onHurt(AttackCache cache, LivingEntity attacker, ItemStack stack) {
        LivingEntity target = cache.getAttackTarget();
        if (!target.m_6095_().is(Tags.EntityTypes.BOSSES)) {
            ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(cache.getAttackTarget().m_6095_());
            if (rl != null && rl.getNamespace().equals("undergarden")) {
                cache.addHurtModifier(DamageModifier.multTotal(1.5F));
            }
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("tooltip.forgotten_sword").withStyle(ChatFormatting.GREEN));
        list.add(Component.translatable("tooltip.forgotten_tool").withStyle(ChatFormatting.GREEN));
    }
}