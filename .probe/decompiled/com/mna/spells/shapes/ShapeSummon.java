package com.mna.spells.shapes;

import com.mna.api.config.GeneralConfigValues;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.PhylacteryStaffItem;
import com.mna.tools.SummonUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class ShapeSummon extends ShapeRaytrace {

    public ShapeSummon(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 240.0F, 30.0F, 20.0F), new AttributeValuePair(Attribute.RANGE, 8.0F, 1.0F, 32.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (source.isPlayerCaster()) {
            List<SpellTarget> supertargets = super.Target(source, world, modificationData, recipe);
            SpellTarget superTarget = (SpellTarget) supertargets.get(0);
            if (superTarget != SpellTarget.NONE) {
                if (!world.isClientSide) {
                    ItemStack phylactery = source.getHand() == InteractionHand.MAIN_HAND ? source.getPlayer().m_21206_() : source.getPlayer().m_21205_();
                    if (phylactery.isEmpty() || !PhylacteryStaffItem.isFilled(phylactery)) {
                        for (SlotResult sr : CuriosApi.getCuriosHelper().findCurios(source.getPlayer(), is -> is.getItem() == ItemInit.STAFF_PHYLACTERY.get() || is.getItem() == ItemInit.CRYSTAL_PHYLACTERY.get())) {
                            if (!sr.stack().isEmpty()) {
                                phylactery = sr.stack().copy();
                            }
                        }
                    }
                    if (!PhylacteryStaffItem.isFilled(phylactery)) {
                        source.getPlayer().m_213846_(Component.translatable("mna:shapes/summon.nophylactery"));
                        return Arrays.asList(SpellTarget.NONE);
                    }
                    EntityType<? extends Mob> type = PhylacteryStaffItem.getEntityType(phylactery);
                    if (type == null) {
                        source.getPlayer().m_213846_(Component.translatable("mna:shapes/summon.nophylactery"));
                        return Arrays.asList(SpellTarget.NONE);
                    }
                    if (GeneralConfigValues.SummonBlacklist.contains(ForgeRegistries.ENTITY_TYPES.getKey(type).toString())) {
                        source.getPlayer().m_213846_(Component.translatable("mna:shapes/summon.blacklisted"));
                        return Arrays.asList(SpellTarget.NONE);
                    }
                    ItemStack castItem = source.getHand() == InteractionHand.MAIN_HAND ? source.getPlayer().m_21205_() : source.getPlayer().m_21206_();
                    SummonUtils.clampTrackedEntities(source.getPlayer());
                    int numSummons = 1 + (int) Math.ceil((double) ((float) castItem.getEnchantmentLevel(Enchantments.LOYALTY) / 2.0F));
                    for (int i = 0; i < numSummons; i++) {
                        if (!this.summonCreature(source, world, modificationData, recipe, type, superTarget)) {
                            return Arrays.asList(SpellTarget.NONE);
                        }
                    }
                }
                return Arrays.asList(new SpellTarget(source.getPlayer()));
            }
        }
        return Arrays.asList(SpellTarget.NONE);
    }

    private boolean summonCreature(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, EntityType<? extends Mob> type, SpellTarget superTarget) {
        String name = source.getPlayer().getDisplayName().getString();
        MutableComponent summonName = Component.literal(name);
        summonName.append("'s ");
        summonName.append(Component.translatable(type.getDescriptionId()));
        Mob summon = type.create(world);
        summon.m_146884_(Vec3.atBottomCenterOf(superTarget.getBlock().above()));
        if (!GeneralConfigValues.SummonBosses && !summon.m_6072_()) {
            return false;
        } else {
            int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
            if (!this.magnitudeHealthCheck(source, new SpellTarget(summon), magnitude, 20)) {
                return false;
            } else {
                SummonUtils.setSummon(summon, source.getPlayer(), (int) (modificationData.getValue(Attribute.DURATION) * 20.0F));
                SpellTarget tgt = new SpellTarget(summon);
                SpellContext context = new SpellContext(world, recipe, summon);
                recipe.getComponents().forEach(c -> {
                    int delay = (int) Math.max(10.0F, c.getValue(Attribute.DELAY) * 20.0F);
                    DelayedEventQueue.pushEvent(context.getServerLevel(), new TimedDelayedSpellEffect("summon_delay", delay, source, tgt, c, context));
                });
                if (!world.m_7967_(summon)) {
                    return false;
                } else {
                    SummonUtils.addTrackedEntity(source.getPlayer(), summon);
                    return true;
                }
            }
        }
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public float initialComplexity() {
        return 40.0F;
    }

    @Override
    public int baselineCooldown() {
        return 20;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }
}