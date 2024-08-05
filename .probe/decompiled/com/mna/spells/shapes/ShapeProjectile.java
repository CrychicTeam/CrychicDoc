package com.mna.spells.shapes;

import com.mna.api.items.ChargeableItem;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.boss.Odin;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class ShapeProjectile extends Shape {

    public ShapeProjectile(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 0.0F, 0.0F, 3.0F, 1.0F), new AttributeValuePair(Attribute.SPEED, 1.0F, 1.0F, 3.0F, 0.2F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (source == null) {
            return Arrays.asList(SpellTarget.NONE);
        } else {
            Optional<SlotResult> curio = CuriosApi.getCuriosHelper().findFirstCurio(source.getCaster(), ItemInit.PROJECTILE_AMULET.get());
            if (source.isPlayerCaster() && curio.isPresent() && ((ChargeableItem) ((SlotResult) curio.get()).stack().getItem()).consumeMana(((SlotResult) curio.get()).stack(), 1.0F, source.getPlayer())) {
                for (int i = -1; i <= 1; i++) {
                    this.SpawnProjectile(source, world, modificationData, recipe, (float) i, null);
                }
            } else {
                this.SpawnProjectile(source, world, modificationData, recipe, 0.0F, null);
            }
            return Arrays.asList(new SpellTarget(source.getCaster()));
        }
    }

    @Override
    public List<SpellTarget> TargetNPCCast(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget targetHint) {
        if (source == null) {
            return Arrays.asList(SpellTarget.NONE);
        } else {
            this.SpawnProjectile(source, world, modificationData, recipe, 0.0F, targetHint);
            return Arrays.asList(new SpellTarget(source.getCaster()));
        }
    }

    private void SpawnProjectile(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, float offset, @Nullable SpellTarget targetHint) {
        SpellProjectile projectile = new SpellProjectile(source.getCaster(), world);
        if (offset != 0.0F) {
            Vec3 eyePos = source.getCaster().m_146892_();
            Vec3 forward = source.getCaster().m_20154_();
            Vec3 cross = forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize().scale((double) offset);
            projectile.m_146884_(eyePos.add(cross));
        }
        ItemStack stack = source.getCaster().getItemInHand(source.getHand());
        if (stack.getEnchantmentLevel(Enchantments.AQUA_AFFINITY) > 0) {
            projectile.setNoWaterFizzle();
        }
        if (source.isPlayerCaster()) {
            source.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(m -> {
                if (m.getAlliedFaction() == Factions.COUNCIL) {
                    float homing = (float) stack.getEnchantmentLevel(Enchantments.POWER_ARROWS) * 0.2F;
                    projectile.setHomingStrength(homing);
                }
            });
        }
        if (source.getCaster() instanceof Odin) {
            projectile.setHomingStrength(0.75F);
        }
        Vec3 direction = source.getForward();
        if (targetHint != null) {
            direction = targetHint.getPosition().subtract(source.getCaster().m_146892_());
        }
        CompoundTag nbt = new CompoundTag();
        recipe.writeToNBT(nbt);
        projectile.setSpellRecipe(nbt);
        projectile.shoot(source.getCaster(), direction, modificationData.getValue(Attribute.SPEED), 0.0F);
        if (source.isPlayerCaster() && source.getPlayer().m_21120_(source.getHand()).getItem() == ItemInit.PUNKIN_STAFF.get()) {
            projectile.setSpecialRender(SpellProjectile.SpecialRenderType.HALLOWEEN);
        }
        world.m_7967_(projectile);
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }
}