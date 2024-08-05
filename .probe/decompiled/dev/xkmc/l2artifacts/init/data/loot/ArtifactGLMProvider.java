package dev.xkmc.l2artifacts.init.data.loot;

import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ArtifactGLMProvider extends GlobalLootModifierProvider {

    public ArtifactGLMProvider(DataGenerator gen) {
        super(gen.getPackOutput(), "l2artifacts");
    }

    @Override
    protected void start() {
        this.add("health_based_1", new ArtifactLootModifier(100, 200, 1.0, ArtifactItems.RANDOM[0].asStack(), LootTableTemplate.byPlayer().build()));
        this.add("health_based_2", new ArtifactLootModifier(200, 300, 1.0, ArtifactItems.RANDOM[1].asStack(), LootTableTemplate.byPlayer().build()));
        this.add("health_based_3", new ArtifactLootModifier(300, 400, 1.0, ArtifactItems.RANDOM[2].asStack(), LootTableTemplate.byPlayer().build()));
        this.add("health_based_4", new ArtifactLootModifier(400, 500, 1.0, ArtifactItems.RANDOM[3].asStack(), LootTableTemplate.byPlayer().build()));
        this.add("health_based_5", new ArtifactLootModifier(500, 0, 1.0, ArtifactItems.RANDOM[4].asStack(), LootTableTemplate.byPlayer().build()));
        this.add("fungus_infection", new AddLootTableModifier(ArtifactLootGen.DROP_FUNGUS, DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()).build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().effects(MobEffectsPredicate.effects().and((MobEffect) ArtifactEffects.FUNGUS.get()))).build()));
    }
}