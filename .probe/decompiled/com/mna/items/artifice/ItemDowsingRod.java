package com.mna.items.artifice;

import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.capabilities.WellspringNode;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.items.IPositionalItem;
import com.mna.api.items.TieredItem;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPlayerCharm;
import com.mna.sound.ItemInUseLoopingSound;
import com.mna.tools.math.MathUtils;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;

public class ItemDowsingRod extends TieredItem {

    private static final float MANA_PER_TICK_WELLSPRING = 5.0F;

    private static final float MANA_PER_TICK_BED = 2.0F;

    private static final float MANA_PER_TICK_PLAYER = 20.0F;

    private static final String KEY_STORED_POINT = "stored_point";

    private static final String KEY_MODE = "mode";

    public ItemDowsingRod() {
        super(new Item.Properties());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 999999;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int ticks) {
        if (user instanceof Player) {
            ItemDowsingRod.Modes mode = this.getMode(stack);
            float manaCost = 2.0F;
            switch(mode) {
                case Wellspring:
                    manaCost = 5.0F;
                    break;
                case Player:
                    manaCost = 20.0F;
                    break;
                case Bed:
                case Point:
                    manaCost = 2.0F;
            }
            Player player = (Player) user;
            boolean manaConsumed = this.consumeMana(player, manaCost);
            if (!manaConsumed) {
                player.m_21253_();
            } else {
                BlockPos trackPos = null;
                ItemStack offhand = user.getUsedItemHand() == InteractionHand.MAIN_HAND ? user.getItemInHand(InteractionHand.OFF_HAND) : user.getItemInHand(InteractionHand.MAIN_HAND);
                switch(mode) {
                    case Wellspring:
                        trackPos = this.trackWellspring(player, world, stack);
                        break;
                    case Player:
                        trackPos = this.trackPlayer(player, world, stack, offhand);
                        break;
                    case Bed:
                    case Point:
                        trackPos = this.trackCachedPoint(player, world, stack);
                }
                if (trackPos != null) {
                    Vec3 closestPos = new Vec3((double) trackPos.m_123341_() + 0.5, player.m_20186_() + (double) player.m_20192_(), (double) trackPos.m_123343_() + 0.5);
                    if (world.isClientSide) {
                        MathUtils.rotateEntityLookTowards(EntityAnchorArgument.Anchor.EYES, player, closestPos, 5.0F);
                    }
                } else if (!world.isClientSide) {
                    player.m_213846_(Component.translatable("item.mna.dowsing_rod.no_target"));
                    player.m_21253_();
                }
            }
        }
    }

    private boolean consumeMana(Player player, float amount) {
        MutableBoolean consumeSuccess = new MutableBoolean(true);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            if (m.isMagicUnlocked() && m.getCastingResource().hasEnoughAbsolute(player, amount)) {
                m.getCastingResource().consume(player, amount);
            } else {
                consumeSuccess.setFalse();
            }
        });
        return consumeSuccess.booleanValue();
    }

    @Nullable
    private BlockPos trackWellspring(Player player, Level level, ItemStack stack) {
        MutableObject<BlockPos> closestPos = new MutableObject(null);
        level.getCapability(WorldMagicProvider.MAGIC).ifPresent(wm -> {
            if (stack.hasTag() && stack.getTag().contains("stored_point")) {
                closestPos.setValue(BlockPos.of(stack.getTag().getLong("stored_point")));
            } else {
                HashMap<BlockPos, WellspringNode> nodes = wm.getWellspringRegistry().getNearbyNodes(player.m_20183_(), 0, (int) ((double) GeneralConfigValues.WellspringDistance * 1.5));
                MutableDouble closestDist = new MutableDouble();
                nodes.keySet().forEach(bp -> {
                    double dist = bp.m_123331_(player.m_20183_());
                    if (closestPos.getValue() == null || dist < closestDist.getValue()) {
                        closestPos.setValue(bp);
                        closestDist.setValue(dist);
                    }
                });
                if (closestPos.getValue() != null) {
                    stack.getOrCreateTag().putLong("stored_point", ((BlockPos) closestPos.getValue()).asLong());
                }
            }
        });
        return (BlockPos) closestPos.getValue();
    }

    @Nullable
    private BlockPos trackPlayer(Player player, Level level, ItemStack stack, ItemStack offhand) {
        if (level.getGameTime() % 20L != 0L && stack.hasTag() && stack.getTag().contains("stored_point")) {
            if (stack.hasTag() && stack.getTag().contains("stored_point")) {
                return BlockPos.of(stack.getTag().getLong("stored_point"));
            }
        } else if (offhand.getItem() instanceof ItemPlayerCharm) {
            ItemPlayerCharm charm = (ItemPlayerCharm) offhand.getItem();
            Player target = charm.GetPlayerTarget(offhand, level);
            if (target != null) {
                stack.getOrCreateTag().putLong("stored_point", target.m_20183_().asLong());
                return target.m_20183_();
            }
        }
        return null;
    }

    private BlockPos trackCachedPoint(Player player, Level level, ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("stored_point") ? BlockPos.of(stack.getTag().getLong("stored_point")) : null;
    }

    private ItemDowsingRod.Modes getMode(ItemStack stack) {
        if (stack.hasTag()) {
            int mode = stack.getTag().getInt("mode");
            return ItemDowsingRod.Modes.values()[mode % ItemDowsingRod.Modes.values().length];
        } else {
            return ItemDowsingRod.Modes.Bed;
        }
    }

    private void setMode(ItemStack stack, ItemDowsingRod.Modes mode) {
        stack.getOrCreateTag().putInt("mode", mode.ordinal());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int ticks) {
        if (stack.hasTag()) {
            stack.getTag().remove("stored_point");
            stack.getTag().remove("mode");
        }
        if (user instanceof Player) {
            ((Player) user).getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        MutableBoolean magicUnlocked = new MutableBoolean(true);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            if (!m.isMagicUnlocked()) {
                if (world.isClientSide) {
                    player.m_213846_(Component.translatable("item.mna.dowsing_rod.confusion"));
                }
                player.getCooldowns().addCooldown(this, 100);
                magicUnlocked.setFalse();
            }
        });
        ItemStack itemstack = player.m_21120_(hand);
        if (magicUnlocked.getValue()) {
            ItemDowsingRod.Modes mode = null;
            if (world.isClientSide) {
                mode = ItemDowsingRod.Modes.Bed;
                this.PlayLoopingSound(SFX.Loops.MANAWEAVING, player);
            } else {
                ItemStack offhandStack = player.m_21120_(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                if (offhandStack.getItem() instanceof IPositionalItem) {
                    IPositionalItem<?> markItem = (IPositionalItem<?>) offhandStack.getItem();
                    DirectionalPoint point = markItem.getDirectionalPoint(offhandStack);
                    if (point != null && point.getPosition() != null) {
                        mode = ItemDowsingRod.Modes.Point;
                        itemstack.getOrCreateTag().putLong("stored_point", point.getPosition().asLong());
                    } else {
                        player.m_213846_(Component.translatable("item.mna.rune_marking.noPosition"));
                    }
                } else if (offhandStack.getItem() instanceof ItemPlayerCharm) {
                    ItemPlayerCharm charm = (ItemPlayerCharm) offhandStack.getItem();
                    if (charm.GetPlayerTarget(offhandStack, world) != null) {
                        mode = ItemDowsingRod.Modes.Player;
                    } else if (charm.getPlayerUUID(offhandStack) == null) {
                        player.m_213846_(Component.translatable("mna:rituals/summon.not_found"));
                    } else {
                        player.m_213846_(Component.translatable("mna:rituals/summon.wrong_dimension"));
                    }
                } else if (offhandStack.getItem() == ItemInit.CHIMERITE_GEM.get()) {
                    mode = ItemDowsingRod.Modes.Wellspring;
                } else if (!world.dimension().equals(((ServerPlayer) player).getRespawnDimension())) {
                    player.m_213846_(Component.translatable("item.mna.bed_charm.dimension-wrong"));
                } else {
                    BlockPos bedPos = ((ServerPlayer) player).getRespawnPosition();
                    if (bedPos == null) {
                        player.m_213846_(Component.translatable("item.mna.bed_charm.no-bed"));
                    } else {
                        mode = ItemDowsingRod.Modes.Bed;
                        itemstack.getOrCreateTag().putLong("stored_point", bedPos.asLong());
                    }
                }
            }
            if (mode != null) {
                if (!world.isClientSide) {
                    this.setMode(itemstack, mode);
                    player.m_6672_(hand);
                }
                return InteractionResultHolder.consume(itemstack);
            }
            if (!world.isClientSide) {
                player.m_213846_(Component.translatable("item.mna.dowsing_rod.no_target"));
            }
        }
        player.getCooldowns().addCooldown(this, 20);
        return InteractionResultHolder.fail(itemstack);
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void PlayLoopingSound(SoundEvent soundID, Player player) {
        Minecraft.getInstance().getSoundManager().play(new ItemInUseLoopingSound(soundID, player));
    }

    private static enum Modes {

        Bed, Wellspring, Player, Point
    }
}