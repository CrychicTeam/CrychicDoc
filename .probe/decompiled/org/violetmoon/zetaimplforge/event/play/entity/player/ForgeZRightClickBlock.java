package org.violetmoon.zetaimplforge.event.play.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.violetmoon.zeta.event.bus.ZResult;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zetaimplforge.ForgeZeta;

public class ForgeZRightClickBlock implements ZRightClickBlock {

    private final PlayerInteractEvent.RightClickBlock e;

    public ForgeZRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        this.e = e;
    }

    public Player getEntity() {
        return this.e.getEntity();
    }

    @Override
    public Level getLevel() {
        return this.e.getLevel();
    }

    @Override
    public BlockPos getPos() {
        return this.e.getPos();
    }

    @Override
    public InteractionHand getHand() {
        return this.e.getHand();
    }

    @Override
    public ItemStack getItemStack() {
        return this.e.getItemStack();
    }

    @Override
    public BlockHitResult getHitVec() {
        return this.e.getHitVec();
    }

    @Override
    public Direction getFace() {
        return this.e.getFace();
    }

    @Override
    public ZResult getUseBlock() {
        return ForgeZeta.from(this.e.getUseBlock());
    }

    @Override
    public void setCancellationResult(InteractionResult result) {
        this.e.setCancellationResult(result);
    }

    @Override
    public boolean isCanceled() {
        return this.e.isCanceled();
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.e.setCanceled(cancel);
    }

    @Override
    public ZResult getResult() {
        return ForgeZeta.from(this.e.getResult());
    }

    @Override
    public void setResult(ZResult value) {
        this.e.setResult(ForgeZeta.to(value));
    }

    public static class Low extends ForgeZRightClickBlock implements ZRightClickBlock.Low {

        public Low(PlayerInteractEvent.RightClickBlock e) {
            super(e);
        }
    }
}