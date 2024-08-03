package dev.xkmc.l2complements.content.item.misc;

import dev.xkmc.l2complements.content.entity.fireball.BaseFireball;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FireChargeItem<T extends BaseFireball<T>> extends TooltipItem {

    private final FireChargeItem.PlayerFire<T> playerFire;

    private final FireChargeItem.BlockFire<T> blockFire;

    public FireChargeItem(Item.Properties pProperties, FireChargeItem.PlayerFire<T> playerFire, FireChargeItem.BlockFire<T> blockFire, Supplier<MutableComponent> tooltip) {
        super(pProperties, tooltip);
        this.playerFire = playerFire;
        this.blockFire = blockFire;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        RandomSource r = player.m_217043_();
        level.playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, (r.nextFloat() - r.nextFloat()) * 0.2F + 1.0F);
        if (!level.isClientSide) {
            Vec3 v = RayTraceUtil.getRayTerm(Vec3.ZERO, player.m_146909_(), player.m_146908_(), 1.0);
            T t = this.playerFire.create(player, 0.0, 0.0, 0.0, level);
            t.m_37010_(itemstack);
            t.m_146884_(player.m_146892_().add(0.0, -0.1, 0.0).add(v));
            t.m_20256_(v);
            level.m_7967_(t);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.IDS.CHARGE_THROW.get().withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, list, flag);
    }

    public interface BlockFire<T extends BaseFireball<T>> {

        T create(double var1, double var3, double var5, double var7, double var9, double var11, Level var13);
    }

    public class FireChargeBehavior extends DefaultDispenseItemBehavior {

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
            Position position = DispenserBlock.getDispensePosition(source);
            double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
            double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
            double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
            Level level = source.getLevel();
            RandomSource randomsource = level.random;
            double d3 = randomsource.triangle((double) direction.getStepX(), 0.11485);
            double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485);
            double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485);
            T t = FireChargeItem.this.blockFire.create(d0, d1, d2, 0.0, 0.0, 0.0, level);
            t.m_37010_(stack);
            t.m_20256_(new Vec3(d3, d4, d5).normalize());
            level.m_7967_(t);
            stack.shrink(1);
            return stack;
        }

        @Override
        protected void playSound(BlockSource level) {
            level.getLevel().m_46796_(1018, level.getPos(), 0);
        }
    }

    public interface PlayerFire<T extends BaseFireball<T>> {

        T create(Player var1, double var2, double var4, double var6, Level var8);
    }
}