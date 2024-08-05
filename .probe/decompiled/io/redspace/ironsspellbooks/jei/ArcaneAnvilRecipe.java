package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.capabilities.magic.UpgradeData;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArcaneAnvilRecipe {

    @NotNull
    ArcaneAnvilRecipe.Type type;

    @Nullable
    ItemStack leftItem;

    @Nullable
    List<ItemStack> rightItem;

    @Nullable
    AbstractSpell spell;

    @Nullable
    int level;

    public ArcaneAnvilRecipe(ItemStack leftItem, List<ItemStack> rightItem) {
        this.leftItem = leftItem;
        this.rightItem = rightItem;
        this.type = ArcaneAnvilRecipe.Type.Item_Upgrade;
    }

    public ArcaneAnvilRecipe(ItemStack leftItem, AbstractSpell spell) {
        this.leftItem = leftItem;
        this.spell = spell;
        this.type = ArcaneAnvilRecipe.Type.Imbue;
    }

    public ArcaneAnvilRecipe(AbstractSpell spell, int baseLevel) {
        this.spell = spell;
        this.level = baseLevel;
        this.type = ArcaneAnvilRecipe.Type.Scroll_Upgrade;
    }

    public ArcaneAnvilRecipe.Tuple<List<ItemStack>, List<ItemStack>, List<ItemStack>> getRecipeItems() {
        return switch(this.type) {
            case Scroll_Upgrade ->
                {
                }
            case Imbue ->
                {
                    ArcaneAnvilRecipe.Tuple<List<ItemStack>, List<ItemStack>, List<ItemStack>> tuple = new ArcaneAnvilRecipe.Tuple<>(new ArrayList(), new ArrayList(), new ArrayList());
                    SpellRegistry.getEnabledSpells().forEach(spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).forEach(i -> {
                        ItemStack scroll = new ItemStack(ItemRegistry.SCROLL.get());
                        ISpellContainer.createScrollContainer(spell, i, scroll);
                        ItemStack result = this.leftItem.copy();
                        ISpellContainer.createScrollContainer(spell, i, result);
                        tuple.a.add(this.leftItem);
                        tuple.b.add(scroll);
                        tuple.c.add(result);
                    }));
                    yield tuple;
                }
            case Item_Upgrade ->
                {
                    ArcaneAnvilRecipe.Tuple<List<ItemStack>, List<ItemStack>, List<ItemStack>> tuple = new ArcaneAnvilRecipe.Tuple<>(new ArrayList(), new ArrayList(), new ArrayList());
                    this.rightItem.forEach(upgradeStack -> {
                        ItemStack result = this.leftItem.copy();
                        UpgradeData.setUpgradeData(result, UpgradeData.NONE.addUpgrade(result, ((UpgradeOrbItem) upgradeStack.getItem()).getUpgradeType(), UpgradeUtils.getRelevantEquipmentSlot(this.leftItem)));
                        tuple.a.add(this.leftItem);
                        tuple.b.add(upgradeStack);
                        tuple.c.add(result);
                    });
                    yield tuple;
                }
        };
    }

    private ItemStack spellContainerOf(ItemStack stack, ISpellContainer container) {
        container.save(stack);
        return stack;
    }

    public static record Tuple<A, B, C>(A a, B b, C c) {
    }

    static enum Type {

        Scroll_Upgrade, Item_Upgrade, Imbue
    }
}