package net.blay09.mods.waystones.item;

import java.util.List;
import java.util.Random;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IResetUseOnDamage;
import net.blay09.mods.waystones.compat.Compat;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WarpStoneItem extends Item implements IResetUseOnDamage {

    private final Random random = new Random();

    private static final BalmMenuProvider containerProvider = new BalmMenuProvider() {

        @Override
        public Component getDisplayName() {
            return Component.translatable("container.waystones.waystone_selection");
        }

        @Override
        public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
            return WaystoneSelectionMenu.createWaystoneSelection(i, playerEntity, WarpMode.WARP_STONE, null);
        }

        @Override
        public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            buf.writeByte(WarpMode.WARP_STONE.ordinal());
        }
    };

    public WarpStoneItem(Item.Properties properties) {
        super(properties.durability(100));
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return WaystonesConfig.getActive().cooldowns.warpStoneUseTime;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return this.getUseDuration(itemStack) > 0 && !Compat.isVivecraftInstalled ? UseAnim.BOW : UseAnim.NONE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if (level.isClientSide) {
            int duration = this.getUseDuration(itemStack);
            float progress = (float) (duration - remainingTicks) / (float) duration;
            boolean shouldMirror = entity.getUsedItemHand() == InteractionHand.MAIN_HAND ^ entity.getMainArm() == HumanoidArm.RIGHT;
            Vec3 handOffset = new Vec3(shouldMirror ? 0.3F : -0.3F, 1.0, 0.52F);
            handOffset = handOffset.yRot(-entity.m_146908_() * (float) (Math.PI / 180.0));
            handOffset = handOffset.zRot(entity.m_146909_() * (float) (Math.PI / 180.0));
            int maxParticles = Math.max(4, (int) (progress * 48.0F));
            if (remainingTicks % 5 == 0) {
                for (int i = 0; i < Math.min(4, maxParticles); i++) {
                    level.addParticle(ParticleTypes.REVERSE_PORTAL, entity.m_20185_() + handOffset.x + (this.random.nextDouble() - 0.5) * 0.5, entity.m_20186_() + handOffset.y + this.random.nextDouble(), entity.m_20189_() + handOffset.z + (this.random.nextDouble() - 0.5) * 0.5, 0.0, 0.05F, 0.0);
                }
                if (progress >= 0.25F) {
                    for (int i = 0; i < maxParticles; i++) {
                        level.addParticle(ParticleTypes.CRIMSON_SPORE, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + this.random.nextDouble(), entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, 0.0, this.random.nextDouble() * 0.5, 0.0);
                    }
                }
                if (progress >= 0.5F) {
                    for (int i = 0; i < maxParticles; i++) {
                        level.addParticle(ParticleTypes.REVERSE_PORTAL, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + this.random.nextDouble(), entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, 0.0, this.random.nextDouble(), 0.0);
                    }
                }
                if (progress >= 0.75F) {
                    for (int i = 0; i < maxParticles / 3; i++) {
                        level.addParticle(ParticleTypes.WITCH, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + 0.5 + this.random.nextDouble(), entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, 0.0, this.random.nextDouble(), 0.0);
                    }
                }
            }
            if (remainingTicks == 1) {
                for (int i = 0; i < maxParticles; i++) {
                    level.addParticle(ParticleTypes.REVERSE_PORTAL, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + this.random.nextDouble() + 1.0, entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, (this.random.nextDouble() - 0.5) * 0.0, this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 0.0);
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide && entity instanceof ServerPlayer) {
            Balm.getNetworking().openGui((ServerPlayer) entity, containerProvider);
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (player.getAbilities().instabuild) {
            PlayerWaystoneManager.setWarpStoneCooldownUntil(player, 0L);
        }
        if (PlayerWaystoneManager.canUseWarpStone(player, itemStack)) {
            if (!player.m_6117_() && !world.isClientSide) {
                world.playSound(null, player, SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 0.1F, 2.0F);
            }
            if (this.getUseDuration(itemStack) > 0 && !Compat.isVivecraftInstalled) {
                player.m_6672_(hand);
            } else {
                this.finishUsingItem(itemStack, world, player);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        } else {
            MutableComponent chatComponent = Component.translatable("chat.waystones.warpstone_not_charged");
            chatComponent.withStyle(ChatFormatting.RED);
            player.displayClientMessage(chatComponent, true);
            return new InteractionResultHolder<>(InteractionResult.FAIL, itemStack);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return this.getBarWidth(itemStack) < 13;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        Player player = Balm.getProxy().getClientPlayer();
        if (player == null) {
            return 13;
        } else {
            long timeLeft = PlayerWaystoneManager.getWarpStoneCooldownLeft(player);
            int maxCooldown = WaystonesConfig.getActive().cooldowns.warpStoneCooldown * 1000;
            return maxCooldown == 0 ? 13 : Math.round(13.0F - (float) timeLeft * 13.0F / (float) maxCooldown);
        }
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        Player player = Balm.getProxy().getClientPlayer();
        return player != null ? PlayerWaystoneManager.canUseWarpStone(player, itemStack) || super.isFoil(itemStack) : super.isFoil(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        Player player = Balm.getProxy().getClientPlayer();
        if (player != null) {
            long timeLeft = PlayerWaystoneManager.getWarpStoneCooldownLeft(player);
            int secondsLeft = (int) (timeLeft / 1000L);
            if (secondsLeft > 0) {
                MutableComponent secondsLeftText = Component.translatable("tooltip.waystones.cooldown_left", secondsLeft);
                secondsLeftText.withStyle(ChatFormatting.GOLD);
                list.add(secondsLeftText);
            }
        }
    }
}