package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.blocks.sorcery.SpectralCraftingTableBlock;
import com.mna.gui.containers.block.ContainerSpectralCraftingTable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class ComponentSpectralCraftingTable extends SpellEffect {

    public ComponentSpectralCraftingTable(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.hasEntityBeenAffected(this, source.getCaster())) {
            return ComponentApplicationResult.FAIL;
        } else {
            context.addAffectedEntity(this, source.getCaster());
            if (target.isBlock()) {
                ServerLevel world = context.getServerLevel();
                BlockPos targetBlock = target.getBlock();
                Direction targetBlockFace = target.getBlockFace(this);
                BlockState against = world.m_8055_(targetBlock);
                if (against.m_60734_() == BlockInit.CHALK_RUNE.get() && (Boolean) against.m_61143_(ChalkRuneBlock.METAL) && source.isPlayerCaster()) {
                    boolean placed = false;
                    if (!ForgeEventFactory.onBlockPlace(source.getPlayer(), BlockSnapshot.create(world.m_46472_(), world, targetBlock), targetBlockFace)) {
                        BlockState placementState = (BlockState) BlockInit.SPECTRAL_CRAFTING_TABLE.get().m_49966_().m_61124_(SpectralCraftingTableBlock.PERMANENT, true);
                        world.m_46597_(targetBlock, placementState);
                        placed = true;
                    }
                    return ComponentApplicationResult.fromBoolean(placed);
                } else {
                    return ComponentApplicationResult.fromBoolean(this.tryPlaceBlock(source.getPlayer(), world, BlockInit.SPECTRAL_CRAFTING_TABLE.get(), targetBlock, targetBlockFace, true, null).success);
                }
            } else if (target.isEntity() && source.isPlayerCaster() && target.getEntity() instanceof Player) {
                ((Player) target.getEntity()).openMenu(this.getContainer(context.getServerLevel().m_8055_(source.getPlayer().m_20183_()), context.getServerLevel(), source.getPlayer().m_20183_()));
                return ComponentApplicationResult.SUCCESS;
            } else {
                return ComponentApplicationResult.FAIL;
            }
        }
    }

    public MenuProvider getContainer(BlockState state, Level worldIn, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) -> new ContainerSpectralCraftingTable(id, inventory, ContainerLevelAccess.create(worldIn, pos)), SpectralCraftingTableBlock.CONTAINER_NAME);
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 1) {
            BlockPos imp = BlockPos.containing(impact_position);
            for (int i = 0; i < 50; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), caster), (double) imp.m_123341_() + 0.5, (double) imp.m_123342_() + 0.5, (double) imp.m_123343_() + 0.5, -0.25 + Math.random() * 0.5, Math.random() * 0.25, -0.25 + Math.random() * 0.5);
            }
        }
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public float initialComplexity() {
        return 5.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}