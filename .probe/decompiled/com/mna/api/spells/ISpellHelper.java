package com.mna.api.spells;

import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISpellHelper {

    SpellCastingResult playerCast(ItemStack var1, Player var2, InteractionHand var3, boolean var4);

    SpellCastingResult affect(ItemStack var1, ISpellDefinition var2, Level var3, SpellSource var4, @Nullable SpellTarget var5);

    SpellCastingResult affect(ItemStack var1, ISpellDefinition var2, Level var3, SpellSource var4, @Nullable SpellTarget var5, @Nullable SpellContext var6);

    SpellCastingResult affect(ItemStack var1, ISpellDefinition var2, Level var3, SpellSource var4);

    boolean containsSpell(ItemStack var1);

    ISpellDefinition parseSpellDefinition(ItemStack var1);

    ISpellDefinition parseSpellDefinition(ItemStack var1, @Nullable Player var2);

    void writeSpellDefinition(ISpellDefinition var1, ItemStack var2);

    ISpellDefinition createSpell(Shape var1, SpellEffect var2, Modifier... var3);

    @Deprecated
    void registerSpellAdjuster(Predicate<SpellAdjustingContext> var1, BiConsumer<ISpellDefinition, LivingEntity> var2);

    void registerSpellAdjuster(Predicate<SpellAdjustingContext> var1, Consumer<SpellAdjustingContext> var2);

    void registerSpellCastingItem(Item var1);

    IForgeRegistry<Shape> getShapeRegistry();

    IForgeRegistry<SpellEffect> getComponentRegistry();

    IForgeRegistry<Modifier> getModifierRegistry();

    boolean reflectSpell(Level var1, @Nullable LivingEntity var2, ISpellDefinition var3, SpellSource var4, Vec3 var5, Vec3 var6, boolean var7);

    void spawnSpellVFX(Level var1, Vec3 var2, Vec3 var3, SpellSource var4, SpellEffect var5);
}