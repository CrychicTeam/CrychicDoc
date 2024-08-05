package com.mna.blocks.ritual;

import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.FacingBlock;
import com.mna.entities.sorcery.AffinityIcon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChimeriteCrystalBlock extends FacingBlock implements ISpellInteractibleBlock<ChimeriteCrystalBlock>, ITranslucentBlock {

    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public ChimeriteCrystalBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion().strength(2.0F));
    }

    @Override
    public boolean onHitBySpell(Level world, BlockPos pos, ISpellDefinition spell) {
        for (IModifiedSpellPart<SpellEffect> c : spell.getComponents()) {
            if (c.getPart() == Components.BREAK) {
                return false;
            }
        }
        if (!world.isClientSide) {
            Vec3 entityPos = this.getPositionBasedOnState(world, pos);
            BlockPos search = BlockPos.containing(entityPos);
            world.m_45976_(AffinityIcon.class, new AABB(search)).stream().forEach(e -> e.m_142687_(Entity.RemovalReason.DISCARDED));
            AffinityIcon efi = new AffinityIcon(world);
            efi.setAffinity(spell.getHighestAffinity());
            efi.m_6034_(entityPos.x, entityPos.y, entityPos.z);
            world.m_7967_(efi);
        }
        return true;
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        super.m_5707_(worldIn, pos, state, player);
        this.removeEntities(worldIn, pos);
    }

    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, world, pos, explosion);
        this.removeEntities(world, pos);
    }

    private void removeEntities(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            Vec3 entityPos = this.getPositionBasedOnState(world, pos);
            BlockPos search = BlockPos.containing(entityPos);
            world.m_45976_(AffinityIcon.class, new AABB(search)).stream().forEach(e -> e.m_142687_(Entity.RemovalReason.DISCARDED));
        }
    }

    private Vec3 getPositionBasedOnState(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Vec3 basePos = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
        if (state.m_61138_(SURFACE_TYPE) && state.m_61138_(FACING)) {
            int surfType = (Integer) state.m_61143_(SURFACE_TYPE);
            if (surfType == 1) {
                return basePos.add(0.0, 1.0, 0.0);
            } else if (surfType == 2) {
                return basePos.add(0.0, -1.0, 0.0);
            } else {
                Direction facing = (Direction) state.m_61143_(FACING);
                return basePos.add((double) facing.getStepX(), (double) facing.getStepY(), (double) facing.getStepZ());
            }
        } else {
            return basePos;
        }
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 15;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }
}