package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.traits.EffectBooster;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.tags.ITagManager;

public class DrainTrait extends MobTrait {

    public DrainTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void postInit(LivingEntity mob, int lv) {
        MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
        ITagManager<MobTrait> manager = LHTraits.TRAITS.get().tags();
        if (manager != null) {
            for (int i = 0; i < 4; i++) {
                Optional<MobTrait> opt = manager.getTag(LHTraits.POTION).getRandomElement(mob.getRandom());
                if (!opt.isEmpty()) {
                    MobTrait trait = (MobTrait) opt.get();
                    if (trait.allow(mob) && !cap.hasTrait(trait)) {
                        cap.setTrait(trait, lv);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        super.onHurtTarget(level, attacker, cache, traitCache);
        LivingEntity target = cache.getAttackTarget();
        long neg = target.getActiveEffects().stream().filter(e -> e.getEffect().getCategory() == MobEffectCategory.HARMFUL).count();
        cache.addHurtModifier(DamageModifier.multTotal((float) (1.0 + LHConfig.COMMON.drainDamage.get() * (double) level * (double) neg)));
    }

    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        ArrayList<MobEffectInstance> pos = new ArrayList(target.getActiveEffects().stream().filter(e -> e.getEffect().getCategory() == MobEffectCategory.BENEFICIAL).toList());
        for (int i = 0; i < level; i++) {
            if (!pos.isEmpty()) {
                MobEffectInstance ins = (MobEffectInstance) pos.remove(attacker.getRandom().nextInt(pos.size()));
                target.removeEffect(ins.getEffect());
            }
        }
        double factor = 1.0 + LHConfig.COMMON.drainDuration.get() * (double) level;
        int maxTime = level * LHConfig.COMMON.drainDurationMax.get();
        EffectBooster.boostTrait(target, factor, maxTime);
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal(i + "").withStyle(ChatFormatting.AQUA)), this.mapLevel(i -> Component.literal(Math.round((double) i.intValue() * LHConfig.COMMON.drainDamage.get() * 100.0) + "%").withStyle(ChatFormatting.AQUA)), this.mapLevel(i -> Component.literal(Math.round((double) i.intValue() * LHConfig.COMMON.drainDuration.get() * 100.0) + "%").withStyle(ChatFormatting.AQUA)), this.mapLevel(i -> Component.literal(Math.round((float) (i * LHConfig.COMMON.drainDurationMax.get()) / 20.0F) + "").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }
}