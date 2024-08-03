package net.liopyu.entityjs.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.liopyu.entityjs.builders.nonliving.entityjs.ProjectileEntityJSBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.ProjectileEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectileItemBuilder extends ItemBuilder {

    public final transient ProjectileEntityJSBuilder parent;

    public transient boolean canThrow;

    public transient float projectileZ;

    public transient float projectileVelocity;

    public transient float projectileInaccuracy;

    public ProjectileItemBuilder(ResourceLocation i, ProjectileEntityJSBuilder parent) {
        super(i);
        this.parent = parent;
        this.canThrow = false;
        this.projectileZ = 0.0F;
        this.projectileVelocity = 1.5F;
        this.projectileInaccuracy = 1.0F;
        this.texture = i.getNamespace() + ":item/" + i.getPath();
    }

    public Item createObject() {
        return new Item(this.createItemProperties()) {

            @Override
            public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
                if (ProjectileItemBuilder.this.canThrow) {
                    ItemStack $$3 = pPlayer.m_21120_(pUsedHand);
                    if (!pLevel.isClientSide) {
                        float pZ = ProjectileItemBuilder.this.projectileZ;
                        float pVelocity = ProjectileItemBuilder.this.projectileVelocity;
                        float pInaccuracy = ProjectileItemBuilder.this.projectileInaccuracy;
                        ProjectileEntityJS $$4 = new ProjectileEntityJS(ProjectileItemBuilder.this.parent, (EntityType<? extends ThrowableItemProjectile>) ProjectileItemBuilder.this.parent.get(), pPlayer, pLevel);
                        $$4.m_37446_($$3);
                        $$4.shootFromRotation(pPlayer, pPlayer.m_146909_(), pPlayer.m_146908_(), pZ, pVelocity, pInaccuracy);
                        pLevel.m_7967_($$4);
                    }
                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    if (!pPlayer.getAbilities().instabuild) {
                        $$3.shrink(1);
                    }
                    return InteractionResultHolder.sidedSuccess($$3, pLevel.isClientSide());
                } else {
                    return super.use(pLevel, pPlayer, pUsedHand);
                }
            }
        };
    }

    @Info("Sets whether the item can be thrown.\n\n@param canThrow True if the item can be thrown, false otherwise.\n\nExample usage:\n```javascript\nitemBuilder.canThrow(true);\n```\n")
    public ItemBuilder canThrow(boolean canThrow) {
        this.canThrow = canThrow;
        return this;
    }

    @Info("Sets the Z offset for the projectile.\n\n@param projectileZ The Z offset for the projectile.\n\nExample usage:\n```javascript\nitemBuilder.projectileZ(0.5f);\n```\n")
    public ItemBuilder projectileZ(float projectileZ) {
        this.projectileZ = projectileZ;
        return this;
    }

    @Info("Sets the velocity of the projectile.\n\n@param projectileVelocity The velocity of the projectile.\n\nExample usage:\n```javascript\nitemBuilder.projectileVelocity(1.5f);\n```\n")
    public ItemBuilder projectileVelocity(float projectileVelocity) {
        this.projectileVelocity = projectileVelocity;
        return this;
    }

    @Info("Sets the inaccuracy of the projectile.\n\n@param projectileInaccuracy The inaccuracy of the projectile.\n\nExample usage:\n```javascript\nitemBuilder.projectileInaccuracy(0.1f);\n```\n")
    public ItemBuilder projectileInaccuracy(float projectileInaccuracy) {
        this.projectileInaccuracy = projectileInaccuracy;
        return this;
    }
}