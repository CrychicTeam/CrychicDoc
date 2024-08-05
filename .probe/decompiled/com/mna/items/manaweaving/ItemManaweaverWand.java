package com.mna.items.manaweaving;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IShowHud;
import com.mna.api.items.TieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.manaweaving.Manaweave;
import com.mna.gui.containers.providers.NamedCantrips;
import com.mna.items.base.IItemWithGui;
import com.mna.items.base.IRadialMenuItem;
import com.mna.network.ClientMessageDispatcher;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mna.sound.ItemInUseLoopingSound;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joml.Vector3f;

public class ItemManaweaverWand extends TieredItem implements IShowHud, IItemWithGui<ItemManaweaverWand>, IRadialMenuItem {

    public static final float MANA_COST_PER_TICK = 1.0F;

    private static final int AUTOWEAVE_TICKS_REQUIRED = 30;

    private static final String KEY_STORED_ALTAR = "altar_location";

    private static final String KEY_MODEL_DATA = "CustomModelData";

    private static final String KEY_SELECTED_PATTERN = "SelectedPattern";

    public ItemManaweaverWand() {
        this(new Item.Properties().stacksTo(1));
    }

    public ItemManaweaverWand(Item.Properties props) {
        super(props);
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.MAINHAND;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            if (!player.getCooldowns().isOnCooldown(this)) {
                if (worldIn.isClientSide) {
                    IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (magic != null && progression != null) {
                        Vector3f[] points = magic.getRememberedPoints();
                        Vector3f[] looks = magic.getRememberedLooks();
                        int useTicks = this.getUseDuration(stack) - timeLeft;
                        if (points.length > 0) {
                            ManaweavingPattern pattern = getStoredPattern(worldIn, stack);
                            if (pattern == null) {
                                pattern = ManaweavingPattern.match(worldIn, progression.getTier(), points, looks);
                            } else if (useTicks < getAutoweaveTicks(player)) {
                                player.m_213846_(Component.translatable("item.mna.manaweaver_wand.channel_too_short"));
                                return;
                            }
                            if (pattern == null) {
                                player.m_213846_(Component.translatable("item.mna.manaweaver_wand.not_recognized"));
                            } else {
                                if (pattern.getTier() > progression.getTier()) {
                                    player.m_213846_(Component.translatable("item.mna.manaweaver_wand.low_tier"));
                                    return;
                                }
                                double avgX = 0.0;
                                double avgZ = 0.0;
                                double minY = Double.MAX_VALUE;
                                for (Vector3f vec : points) {
                                    avgX += (double) vec.x();
                                    avgZ += (double) vec.z();
                                    if ((double) vec.y() < minY) {
                                        minY = (double) vec.y();
                                    }
                                }
                                avgX /= (double) points.length;
                                avgZ /= (double) points.length;
                                ClientMessageDispatcher.sendManaweavePatternDrawn(player, pattern.m_6423_(), new Vec3(avgX, minY - 0.4, avgZ), player.m_7655_(), useTicks);
                            }
                        }
                    }
                }
                player.getCooldowns().addCooldown(stack.getItem(), 5);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        MutableBoolean success = new MutableBoolean(true);
        playerIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            if (m.isMagicUnlocked()) {
                if (!this.openGuiIfModifierPressed(itemstack, playerIn, worldIn)) {
                    if (worldIn.isClientSide) {
                        m.clearRememberedPoints();
                        this.PlayLoopingSound(SFX.Loops.MANAWEAVING, playerIn);
                    }
                    ManaweavingPattern pattern = getStoredPattern(worldIn, itemstack);
                    LazyOptional<IPlayerProgression> progression = playerIn.getCapability(PlayerProgressionProvider.PROGRESSION);
                    if (pattern != null && progression.isPresent() && ((IPlayerProgression) progression.resolve().get()).getTier() < pattern.getTier()) {
                        setStoredPattern(itemstack, null);
                    }
                    playerIn.m_6672_(handIn);
                } else {
                    success.setFalse();
                }
            } else {
                if (worldIn.isClientSide) {
                    playerIn.m_213846_(Component.translatable("item.mna.manaweaver_wand.confusion"));
                }
                playerIn.getCooldowns().addCooldown(this, 100);
            }
        });
        return success.getValue() ? InteractionResultHolder.consume(itemstack) : InteractionResultHolder.fail(itemstack);
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getPlayer().m_6047_()) {
            BlockState state = context.getLevel().getBlockState(context.getClickedPos());
            if (state.m_60734_() == BlockInit.MANAWEAVING_ALTAR.get()) {
                if (!context.getLevel().isClientSide()) {
                    stack.getOrCreateTag().put("altar_location", NbtUtils.writeBlockPos(context.getClickedPos()));
                    stack.getOrCreateTag().putInt("CustomModelData", 1);
                    context.getPlayer().m_213846_(Component.translatable("item.mna.manaweaver_wand.position_stored"));
                }
                return InteractionResult.FAIL;
            }
            if (stack.hasTag() && stack.getTag().contains("altar_location")) {
                if (!context.getLevel().isClientSide()) {
                    stack.getOrCreateTag().remove("altar_location");
                    stack.getOrCreateTag().remove("CustomModelData");
                    context.getPlayer().m_213846_(Component.translatable("item.mna.manaweaver_wand.position_cleared"));
                }
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    public static BlockPos getStoredBlockPos(ItemStack stack) {
        return stack.getItem() instanceof ItemManaweaverWand && stack.hasTag() && stack.getTag().contains("altar_location") ? NbtUtils.readBlockPos(stack.getTag().getCompound("altar_location")) : null;
    }

    @Nullable
    public static ManaweavingPattern getStoredPattern(Level world, ItemStack stack) {
        ResourceLocation rLoc = getStoredPatternID(world, stack);
        return rLoc != null ? ManaweavingPatternHelper.GetManaweavingRecipe(world, rLoc) : null;
    }

    @Nullable
    public static ResourceLocation getStoredPatternID(Level world, ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("SelectedPattern") ? new ResourceLocation(stack.getTag().getString("SelectedPattern")) : null;
    }

    public static void setStoredPattern(ItemStack stack, ResourceLocation rLoc) {
        if (rLoc == null) {
            stack.getTag().remove("SelectedPattern");
        } else {
            stack.getOrCreateTag().putString("SelectedPattern", rLoc.toString());
        }
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof Manaweave mw && !mw.isMerging()) {
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null) {
                magic.getCastingResource().restore(mw.getManaReturn(player));
                magic.getCastingResource().setNeedsSync();
            }
            player.m_9236_().playSound(player, player.m_20183_(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
            mw.m_142687_(Entity.RemovalReason.DISCARDED);
            return false;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 999999;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
        LazyOptional<IPlayerMagic> magicContainer = player.getCapability(PlayerMagicProvider.MAGIC);
        LazyOptional<IPlayerProgression> progContainer = player.getCapability(PlayerProgressionProvider.PROGRESSION);
        if (magicContainer.isPresent()) {
            IPlayerMagic magic = magicContainer.orElse(null);
            if (!magic.isMagicUnlocked()) {
                player.releaseUsingItem();
                return;
            }
            if (!magic.getCastingResource().hasEnoughAbsolute(player, 1.0F)) {
                if (player instanceof Player) {
                    ((Player) player).getCooldowns().addCooldown(this, 100);
                    if (player.m_9236_().isClientSide() && player == ManaAndArtifice.instance.proxy.getClientPlayer()) {
                        ((Player) player).m_213846_(Component.translatable("item.mna.manaweaver_wand.oom"));
                    }
                }
                player.releaseUsingItem();
                return;
            }
            if (player.m_9236_().isClientSide()) {
                Vec3 eyePos = player.m_20299_(0.0F);
                Vec3 look = player.m_20154_();
                Vec3 position = eyePos.add(look);
                Vec3 particlePosition = eyePos.add(look.scale(1.5));
                magicContainer.orElse(null).addRememberedPoint(position.toVector3f(), new Vector3f(Mth.wrapDegrees(player.m_146908_()), Mth.wrapDegrees(player.m_146909_()), 0.0F));
                MAParticleType particle = new MAParticleType(ParticleInit.BLUE_SPARKLE_STATIONARY.get()).setMaxAge(40);
                if (progContainer.isPresent()) {
                    IFaction faction = ((IPlayerProgression) progContainer.resolve().get()).getAlliedFaction();
                    if (faction != null) {
                        int[] color = faction.getManaweaveRGB();
                        if (color != null) {
                            particle.setColor(color[0], color[1], color[2]);
                        }
                    }
                }
                player.m_9236_().addParticle(particle, particlePosition.x, particlePosition.y, particlePosition.z, 0.0, 0.0, 0.0);
                ManaweavingPattern pattern = getStoredPattern(player.m_9236_(), stack);
                if (pattern != null) {
                    int ticksUsed = this.getUseDuration(stack) - count;
                    if (ticksUsed == getAutoweaveTicks(player)) {
                        player.m_5496_(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    }
                }
            }
            magic.getCastingResource().consume(player, 1.0F);
        }
    }

    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void PlayLoopingSound(SoundEvent soundID, Player player) {
        Minecraft.getInstance().getSoundManager().play(new ItemInUseLoopingSound(soundID, player));
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedCantrips();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ManaweavingPattern stored = getStoredPattern(worldIn, stack);
        if (stored != null) {
            tooltip.add(Component.translatable("item.mna.manaweaver_wand.auto", Component.translatable(stored.m_6423_().toString()).getString()).withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.translatable("item.mna.manaweaver_wand.manual").withStyle(ChatFormatting.GOLD));
        }
        IItemWithGui.super.appendHoverText(stack, worldIn, tooltip, flagIn);
        IRadialMenuItem.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static int getAutoweaveTicks(LivingEntity living) {
        if (!(living instanceof Player player)) {
            return 30;
        } else {
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            int ticks = 30;
            if (progression != null) {
                ticks -= 5 * progression.getTier();
            }
            return ticks;
        }
    }
}