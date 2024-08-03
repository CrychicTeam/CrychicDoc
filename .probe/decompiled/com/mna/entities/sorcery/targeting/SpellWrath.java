package com.mna.entities.sorcery.targeting;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellWrath extends ChanneledSpellEntity {

    public SpellWrath(EntityType<? extends SpellWrath> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SpellWrath(LivingEntity caster, ISpellDefinition spell, Level world) {
        super(EntityInit.SPELL_WRATH.get(), caster, spell, world);
        this.m_6034_(caster.m_20185_(), caster.m_20186_() + (double) caster.m_20192_(), caster.m_20189_());
        this.m_19915_(this.getCaster().m_146908_(), this.getCaster().m_146909_());
        float x = -Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
        float y = -Mth.sin(this.m_146909_() * (float) (Math.PI / 180.0));
        float z = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
        Vec3 motion = new Vec3((double) x, (double) y, (double) z).normalize().scale((double) this.getShapeAttribute(Attribute.SPEED));
        this.m_20256_(motion);
    }

    @Override
    public void tick() {
        if (this.getCaster() != null && this.m_20184_().x == 0.0 && this.m_20184_().y == 0.0 && this.m_20184_().z == 0.0) {
            this.m_6034_(this.getCaster().m_20185_(), this.getCaster().m_6144_() ? this.getCaster().m_20186_() - 1.0 : this.getCaster().m_20186_(), this.getCaster().m_20189_());
        } else {
            this.m_6034_(this.m_20185_() + this.m_20184_().x, this.m_20186_() + this.m_20184_().y, this.m_20189_() + this.m_20184_().z);
        }
        super.tick();
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        if (caster != null) {
            float radius_h = this.getShapeAttributeByAge(Attribute.WIDTH);
            Vec3 position = this.m_20182_().add((double) (-radius_h) + Math.random() * (double) (2.0F * radius_h), 0.0, (double) (-radius_h) + Math.random() * (double) (2.0F * radius_h));
            int count = 0;
            for (BlockPos targetPos = BlockPos.containing(position); count < 15; targetPos = targetPos.above()) {
                if (!world.m_46859_(targetPos)) {
                    return;
                }
                count++;
            }
            position = position.add(0.0, 15.0, 0.0);
            CompoundTag recipeData = new CompoundTag();
            recipe.writeToNBT(recipeData);
            Entity projectile = new Smite(world, position, recipeData, caster);
            world.addFreshEntity(projectile);
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean ice) {
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
    }

    @Override
    protected int getApplicationRate() {
        return 2;
    }
}