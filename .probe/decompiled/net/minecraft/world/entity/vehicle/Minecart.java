package net.minecraft.world.entity.vehicle;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Minecart extends AbstractMinecart {

    public Minecart(EntityType<?> entityType0, Level level1) {
        super(entityType0, level1);
    }

    public Minecart(Level level0, double double1, double double2, double double3) {
        super(EntityType.MINECART, level0, double1, double2, double3);
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        if (player0.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (this.m_20160_()) {
            return InteractionResult.PASS;
        } else if (!this.m_9236_().isClientSide) {
            return player0.m_20329_(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public void activateMinecart(int int0, int int1, int int2, boolean boolean3) {
        if (boolean3) {
            if (this.m_20160_()) {
                this.m_20153_();
            }
            if (this.m_38176_() == 0) {
                this.m_38160_(-this.m_38177_());
                this.m_38154_(10);
                this.m_38109_(50.0F);
                this.m_5834_();
            }
        }
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.RIDEABLE;
    }
}