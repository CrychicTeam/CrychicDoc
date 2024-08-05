package dev.xkmc.l2complements.init.data;

import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class LCConfigGen extends ConfigDataProvider {

    public LCConfigGen(DataGenerator generator) {
        super(generator, "L2Complements Armor Config");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
        collector.add(L2DamageTracker.ARMOR, new ResourceLocation("l2complements", "default"), new ArmorEffectConfig().add(LCMats.TOTEMIC_GOLD.armorPrefix(), new MobEffect[] { MobEffects.POISON, MobEffects.WITHER, MobEffects.HUNGER }).add(LCMats.POSEIDITE.armorPrefix(), new MobEffect[] { MobEffects.DIG_SLOWDOWN }).add(LCMats.POSEIDITE.armorPrefix(), new MobEffect[] { MobEffects.DIG_SLOWDOWN }).add(LCMats.SCULKIUM.armorPrefix(), new MobEffect[] { MobEffects.DARKNESS, MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DIG_SLOWDOWN, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN }));
    }
}