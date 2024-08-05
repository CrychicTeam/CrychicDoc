package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.recipes.manaweaving.TransmutationRecipe;
import com.mna.recipes.manaweaving.TransmutationRecipeSerializer;
import com.mna.tools.BlockUtils;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ComponentTransmute extends SpellEffect {

    public ComponentTransmute(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.getCaster() == null) {
            return ComponentApplicationResult.FAIL;
        } else {
            if (target.isBlock() && context.getServerLevel().m_46749_(target.getBlock())) {
                BlockState state = context.getServerLevel().m_8055_(target.getBlock());
                Block block = state.m_60734_();
                List<TransmutationRecipe> recipes = (List<TransmutationRecipe>) TransmutationRecipeSerializer.ALL_RECIPES.values().stream().filter(r -> MATags.isBlockIn(block, r.getTargetBlock()) || r.getTargetBlock().equals(ForgeRegistries.BLOCKS.getKey(block))).collect(Collectors.toList());
                if (recipes.size() == 0) {
                    return ComponentApplicationResult.SUCCESS;
                }
                TransmutationRecipe recipe = (TransmutationRecipe) recipes.get(0);
                if (!source.isPlayerCaster() && (recipe.getTier() > 0 || recipe.getFactionRequirement() != null)) {
                    return ComponentApplicationResult.FAIL;
                }
                if (source.isPlayerCaster()) {
                    IPlayerProgression progression = (IPlayerProgression) source.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (progression != null) {
                        if (progression.getTier() < recipe.getTier()) {
                            source.getPlayer().m_213846_(Component.translatable("mna:components/transmute.low_tier"));
                            return ComponentApplicationResult.FAIL;
                        }
                        if (recipe.getFactionRequirement() != null && progression.getAlliedFaction() != recipe.getFactionRequirement()) {
                            source.getPlayer().m_213846_(Component.translatable("mna:components/transmute.wrong_faction"));
                            return ComponentApplicationResult.FAIL;
                        }
                    } else if (recipe.getTier() > 0 || recipe.getFactionRequirement() != null) {
                        return ComponentApplicationResult.FAIL;
                    }
                }
                if (recipe != null && BlockUtils.destroyBlock(source.getCaster(), context.getServerLevel(), target.getBlock(), false, Tiers.IRON)) {
                    if (recipe.hasReplaceBlock()) {
                        Block replace = ForgeRegistries.BLOCKS.getValue(recipe.getReplaceBlock());
                        if (replace != null) {
                            context.getServerLevel().m_7731_(target.getBlock(), replace.defaultBlockState(), 3);
                        }
                    }
                    if (recipe.hasLootTable()) {
                        LootTable lootTable = context.getServerLevel().getServer().getLootData().m_278676_(recipe.getLootTable());
                        if (lootTable != null) {
                            LootParams lootparams = new LootParams.Builder(context.getServerLevel()).withParameter(LootContextParams.THIS_ENTITY, source.getCaster()).withParameter(LootContextParams.DAMAGE_SOURCE, source.getCaster().m_269291_().magic()).withOptionalParameter(LootContextParams.TOOL, source.getCaster().getItemInHand(source.getHand())).withParameter(LootContextParams.ORIGIN, target.getPosition()).create(LootContextParamSets.ENTITY);
                            Vec3 isPos = Vec3.atCenterOf(target.getBlock());
                            lootTable.getRandomItems(lootparams).forEach(is -> {
                                ItemEntity item = new ItemEntity(context.getServerLevel(), isPos.x, isPos.y, isPos.z, is);
                                context.getServerLevel().addFreshEntity(item);
                            });
                        }
                    }
                    return ComponentApplicationResult.SUCCESS;
                }
            }
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.ARCANE;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age < 5) {
            Vec3 rotationOffset = new Vec3(1.5, 0.0, 0.0);
            BlockPos bp = BlockPos.containing(impact_position).offset(0, -1, 0);
            for (int angle = 0; angle < 360; angle += 30) {
                Vec3 point = rotationOffset.yRot((float) ((double) angle * Math.PI / 180.0));
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get()), caster), (double) ((float) bp.m_123341_() + 0.5F) + point.x, (double) bp.m_123342_(), (double) ((float) bp.m_123343_() + 0.5F) + point.z, (double) ((float) bp.m_123341_() + 0.5F), (double) (bp.m_123342_() + 2), (double) ((float) bp.m_123343_() + 0.5F));
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}