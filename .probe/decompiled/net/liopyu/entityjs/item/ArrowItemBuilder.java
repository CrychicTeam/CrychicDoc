package net.liopyu.entityjs.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.nonliving.entityjs.ArrowEntityJSBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.ArrowEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArrowItemBuilder extends ItemBuilder {

    public final transient ArrowEntityJSBuilder parent;

    public transient boolean canBePickedUp;

    public ArrowItemBuilder(ResourceLocation i, ArrowEntityJSBuilder parent) {
        super(i);
        this.parent = parent;
        this.canBePickedUp = true;
        this.texture = i.getNamespace() + ":item/" + i.getPath();
    }

    @Info("Sets if the arrow can be picked up")
    public ArrowItemBuilder canBePickedup(boolean canBePickedUp) {
        this.canBePickedUp = canBePickedUp;
        return this;
    }

    public Item createObject() {
        return new ArrowItem(this.createItemProperties()) {

            @Override
            public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
                ArrowEntityJS arrow = new ArrowEntityJS(pLevel, pShooter, ArrowItemBuilder.this.parent);
                if (ArrowItemBuilder.this.canBePickedUp) {
                    ItemStack stack = new ItemStack(pStack.getItem());
                    stack.setTag(pStack.getTag());
                    arrow.setPickUpItem(stack);
                }
                return arrow;
            }
        };
    }
}