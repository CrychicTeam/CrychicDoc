package team.lodestar.lodestone.handlers;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.registry.common.tag.LodestoneDamageTypeTags;

public class LodestoneAttributeEventHandler {

    public static void processAttributes(LivingHurtEvent event) {
        if (!event.isCanceled() && !(event.getAmount() <= 0.0F)) {
            DamageSource source = event.getSource();
            LivingEntity target = event.getEntity();
            if (source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
                float amount = event.getAmount();
                if (source.getEntity() instanceof LivingEntity attacker) {
                    AttributeInstance magicProficiency = attacker.getAttribute(LodestoneAttributeRegistry.MAGIC_PROFICIENCY.get());
                    if (magicProficiency != null && magicProficiency.getValue() > 0.0) {
                        amount = (float) ((double) amount * (1.0 + magicProficiency.getValue() * 0.1F));
                    }
                }
                AttributeInstance magicResistance = target.getAttribute(LodestoneAttributeRegistry.MAGIC_RESISTANCE.get());
                if (magicResistance != null && magicResistance.getValue() > 0.0) {
                    amount = (float) ((double) amount * applyMagicResistance(magicResistance.getValue()));
                }
                event.setAmount(amount);
            }
            if (source.getEntity() instanceof LivingEntity attackerx && !source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
                AttributeInstance magicDamage = attackerx.getAttribute(LodestoneAttributeRegistry.MAGIC_DAMAGE.get());
                if (magicDamage != null && magicDamage.getValue() > 0.0 && !target.isDeadOrDying()) {
                    Level level = source.getEntity().level();
                    Holder.Reference<DamageType> holderOrThrow = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC);
                    DamageSource magic = new DamageSource(holderOrThrow, attackerx, attackerx);
                    target.f_19802_ = 0;
                    target.hurt(magic, (float) magicDamage.getValue());
                }
            }
        }
    }

    public static double applyMagicResistance(double magicResistance) {
        return magicResistance >= 20.0 ? Math.max(0.25, 0.5 - (magicResistance - 20.0) * 0.0125F) : 1.0 - magicResistance * 0.025F;
    }
}