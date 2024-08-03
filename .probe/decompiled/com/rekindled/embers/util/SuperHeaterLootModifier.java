package com.rekindled.embers.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.recipe.SingleItemContainer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.LootModifier;

public class SuperHeaterLootModifier extends LootModifier {

    public static final Codec<SuperHeaterLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SuperHeaterLootModifier::new));

    public SmeltingRecipe cachedRecipe = null;

    public SuperHeaterLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public Codec<SuperHeaterLootModifier> codec() {
        return CODEC;
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity user = null;
        if (context.hasParam(LootContextParams.KILLER_ENTITY)) {
            user = context.getParam(LootContextParams.KILLER_ENTITY);
        } else if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            user = context.getParam(LootContextParams.THIS_ENTITY);
        }
        if (user instanceof Player player && EmberInventoryUtil.getEmberTotal(player) >= RegistryManager.SUPERHEATER_AUGMENT.getCost()) {
            boolean smelted = false;
            int limit = generatedLoot.size();
            for (int i = 0; i < limit; i++) {
                SingleItemContainer wrapper = new SingleItemContainer((ItemStack) generatedLoot.get(i));
                this.cachedRecipe = Misc.getRecipe(this.cachedRecipe, RecipeType.SMELTING, wrapper, context.getLevel());
                if (this.cachedRecipe != null) {
                    ItemStack stack = this.cachedRecipe.m_5874_(wrapper, context.getLevel().m_9598_());
                    generatedLoot.add(stack);
                    generatedLoot.set(i, ItemStack.EMPTY);
                    smelted = true;
                }
            }
            if (smelted) {
                Vec3 pos = context.getParam(LootContextParams.ORIGIN);
                context.getLevel().m_6263_(null, pos.x, pos.y, pos.z, EmbersSounds.FIREBALL_HIT.get(), SoundSource.PLAYERS, 0.5F, Misc.random.nextFloat() * 0.5F + 0.2F);
                EmberInventoryUtil.removeEmber(player, RegistryManager.SUPERHEATER_AUGMENT.getCost());
                context.getLevel().sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, 1.0F), pos.x, pos.y, pos.z, 10, 0.0, 0.0, 0.0, 1.0);
                context.getLevel().sendParticles(GlowParticleOptions.EMBER, pos.x, pos.y, pos.z, 20, 0.25, 0.25, 0.25, 0.1);
            }
        }
        return generatedLoot;
    }
}