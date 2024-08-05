package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.tools.BlockUtils;
import com.mna.tools.InventoryUtilities;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ComponentPlaceBlock extends SpellEffect {

    public ComponentPlaceBlock(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        BlockPos blockTarget = target.getBlock().offset(target.getBlockFace(this).getNormal());
        if (blockTarget == null) {
            return ComponentApplicationResult.FAIL;
        } else {
            ItemStack offhandItem = source.getCaster().getItemInHand(source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            if (!offhandItem.isEmpty() && offhandItem.getItem() instanceof BlockItem) {
                Block toPlace = ((BlockItem) offhandItem.getItem()).getBlock();
                if (toPlace == null) {
                    return ComponentApplicationResult.FAIL;
                } else {
                    if (context.getServerLevel().m_6249_((Entity) null, new AABB(blockTarget), e -> e.isAlive() && e instanceof LivingEntity).size() == 0) {
                        Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
                        BlockPlaceContext bpc = new BlockPlaceContext(player, InteractionHand.MAIN_HAND, offhandItem, new BlockHitResult(Vec3.atCenterOf(blockTarget), target.getBlockFace(this), blockTarget, true));
                        if (context.getServerLevel().m_8055_(blockTarget).m_60629_(bpc)) {
                            BlockState placeState = toPlace.defaultBlockState();
                            if (BlockUtils.placeBlock(context.getServerLevel(), blockTarget, target.getBlockFace(this), placeState, player) && !player.isCreative()) {
                                InventoryUtilities.removeItemFromInventory(new ItemStack(toPlace), false, false, new InvWrapper(player.getInventory()));
                            }
                            return ComponentApplicationResult.SUCCESS;
                        }
                    }
                    return ComponentApplicationResult.FAIL;
                }
            } else {
                return ComponentApplicationResult.FAIL;
            }
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.ENDER;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsEntities() {
        return true;
    }

    @Override
    public Direction defaultBlockFace() {
        return Direction.DOWN;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER, Affinity.WIND, Affinity.ICE);
    }
}