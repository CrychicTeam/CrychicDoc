package com.mna.spells.shapes;

import com.google.common.collect.Lists;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.SummonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;

public class ShapeChain extends ShapeRaytrace {

    public ShapeChain(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 5.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.MAGNITUDE, 10.0F, 1.0F, 100.0F, 1.0F, 3.0F), new AttributeValuePair(Attribute.RANGE, 8.0F, 8.0F, 32.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 10.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        List<SpellTarget> tgts = super.Target(source, world, modificationData, recipe);
        SpellTarget tgt = (SpellTarget) tgts.get(0);
        return tgt == SpellTarget.NONE ? tgts : this.performChaining(source, world, modificationData, recipe, tgt);
    }

    @Override
    public List<SpellTarget> TargetNPCCast(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget targetHint) {
        List<SpellTarget> tgts = super.TargetNPCCast(source, world, modificationData, recipe, targetHint);
        SpellTarget tgt = (SpellTarget) tgts.get(0);
        return tgt == SpellTarget.NONE ? tgts : this.performChaining(source, world, modificationData, recipe, tgt);
    }

    private List<SpellTarget> performChaining(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget tgt) {
        int radius = (int) modificationData.getValue(Attribute.RADIUS);
        int arcChance = (int) modificationData.getValue(Attribute.MAGNITUDE);
        ArrayList<SpellTarget> targetsList = new ArrayList();
        targetsList.add(tgt);
        if (tgt.isBlock()) {
            Block search = world.getBlockState(tgt.getBlock()).m_60734_();
            ArrayList<Long> blockTargets = new ArrayList();
            blockTargets.add(tgt.getBlock().asLong());
            boolean chainDiagonally = modificationData.getValue(Attribute.PRECISION) == 1.0F;
            this.chainTargetBlocks(world, search, tgt.getBlockFace(null), tgt.getBlock(), radius, arcChance, chainDiagonally, blockTargets, targetsList);
        } else if (tgt.isEntity()) {
            ArrayList<Integer> entityTargets = new ArrayList();
            entityTargets.add(tgt.getEntity().getId());
            this.chainTargetEntities(world, source.getCaster(), tgt.getEntity(), radius, arcChance, entityTargets, targetsList);
        }
        if (!world.isClientSide) {
            for (int i = 0; i < targetsList.size(); i++) {
                SpellTarget prev = i == 0 ? new SpellTarget(source.getCaster()) : (SpellTarget) targetsList.get(i - 1);
                SpellTarget next = (SpellTarget) targetsList.get(i);
                Vec3 prevPos;
                if (prev.isBlock()) {
                    prevPos = new Vec3((double) ((float) prev.getBlock().m_123341_() + 0.5F), (double) ((float) prev.getBlock().m_123342_() + 0.5F), (double) ((float) prev.getBlock().m_123343_() + 0.5F));
                } else {
                    prevPos = prev.getEntity().getEyePosition();
                }
                Vec3 nextPos;
                if (next.isBlock()) {
                    nextPos = new Vec3((double) ((float) next.getBlock().m_123341_() + 0.5F), (double) ((float) next.getBlock().m_123342_() + 0.5F), (double) ((float) next.getBlock().m_123343_() + 0.5F));
                } else {
                    nextPos = prev.getEntity().getEyePosition();
                }
                ServerMessageDispatcher.sendParticleSpawn(prevPos.x, prevPos.y, prevPos.z, nextPos.x, nextPos.y, nextPos.z, recipe.getParticleColorOverride(), 64.0F, world.dimension(), ParticleInit.LIGHTNING_BOLT.get());
            }
        }
        return targetsList;
    }

    private void chainTargetBlocks(Level world, Block searchType, Direction castDir, BlockPos origin, int range, int max, boolean targetDiagonal, List<Long> searchedPositions, List<SpellTarget> targets) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(origin, 0));
        int searchedBlocks = 0;
        List<Vec3i> offsets = new ArrayList();
        for (Direction direction : Direction.values()) {
            offsets.add(direction.getNormal());
        }
        if (targetDiagonal) {
            for (int i = -1; i <= 1; i++) {
                offsets.add(new Vec3i(-1, i, -1));
                offsets.add(new Vec3i(-1, i, 1));
                offsets.add(new Vec3i(1, i, 1));
                offsets.add(new Vec3i(1, i, -1));
                if (i != 0) {
                    offsets.add(new Vec3i(1, i, 0));
                    offsets.add(new Vec3i(-1, i, 0));
                    offsets.add(new Vec3i(0, i, 1));
                    offsets.add(new Vec3i(0, i, -1));
                }
            }
        }
        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = (Tuple<BlockPos, Integer>) queue.poll();
            BlockPos blockpos = tuple.getA();
            int idx = tuple.getB();
            Iterator var24 = offsets.iterator();
            while (true) {
                if (var24.hasNext()) {
                    Vec3i offset = (Vec3i) var24.next();
                    BlockPos relative = blockpos.offset(offset);
                    if (searchedPositions.contains(relative.asLong())) {
                        continue;
                    }
                    searchedPositions.add(relative.asLong());
                    BlockState blockstate = world.getBlockState(relative);
                    if (blockstate.m_60734_() == searchType) {
                        targets.add(new SpellTarget(relative, castDir));
                        searchedBlocks++;
                        queue.add(new Tuple<>(relative, idx + 1));
                    }
                    if (searchedBlocks < max - 1) {
                        continue;
                    }
                }
                if (searchedBlocks >= max - 1) {
                    return;
                }
                break;
            }
        }
    }

    private void chainTargetEntities(Level world, LivingEntity caster, Entity origin, int range, int max, List<Integer> searchedEntities, List<SpellTarget> targets) {
        MutableObject<Entity> lastEntity = new MutableObject(null);
        world.m_6443_(Mob.class, new AABB(origin.blockPosition()).inflate((double) range), e -> SummonUtils.isSummon(e) && SummonUtils.getSummoner(e) == caster ? false : e.m_6084_() && !searchedEntities.contains(e.m_19879_())).stream().sorted((o1, o2) -> {
            double d1 = o1.m_20280_(origin);
            double d2 = o2.m_20280_(origin);
            return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
        }).forEach(e -> {
            if (targets.size() < max) {
                lastEntity.setValue(e);
                targets.add(new SpellTarget(e));
                searchedEntities.add(e.m_19879_());
            }
        });
        if (lastEntity.getValue() != null && searchedEntities.size() < max) {
            this.chainTargetEntities(world, caster, (Entity) lastEntity.getValue(), range, max, searchedEntities, targets);
        }
    }

    @Override
    public float initialComplexity() {
        return 40.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }
}