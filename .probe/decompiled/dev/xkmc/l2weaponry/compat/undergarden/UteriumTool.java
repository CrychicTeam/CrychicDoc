package dev.xkmc.l2weaponry.compat.undergarden;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import quek.undergarden.network.CreateCritParticlePacket;
import quek.undergarden.network.UGPacketHandler;
import quek.undergarden.registry.UGParticleTypes;
import quek.undergarden.registry.UGTags.Entities;

public class UteriumTool extends ExtraToolConfig implements LWExtraConfig {

    @Override
    public void onHurt(AttackCache cache, LivingEntity attacker, ItemStack stack) {
        LivingEntity target = cache.getAttackTarget();
        if (target.m_6095_().is(Entities.ROTSPAWN)) {
            cache.addHurtModifier(DamageModifier.multTotal(1.5F));
            if (!target.m_9236_().isClientSide()) {
                UGPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new CreateCritParticlePacket(target.m_19879_(), 2, (ParticleType) UGParticleTypes.UTHERIUM_CRIT.get()));
            }
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("tooltip.utheric_sword").withStyle(ChatFormatting.RED));
    }
}