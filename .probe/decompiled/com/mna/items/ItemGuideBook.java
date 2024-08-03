package com.mna.items;

import com.mna.api.capabilities.CodexBreadcrumb;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.TieredItem;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.containers.providers.NamedGuideBook;
import com.mna.items.renderers.books.CodexBookRenderer;
import com.mna.tools.DidYouKnowHelper;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.network.NetworkHooks;

public class ItemGuideBook extends TieredItem {

    private int guiOpenCount = 0;

    private long lastActivateTime = 0L;

    public ItemGuideBook() {
        super(new Item.Properties().stacksTo(1));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new CodexBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack book = player.m_21120_(hand);
        if (hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide) {
                if (!this.checkMagicUnlock((ServerLevel) world, player)) {
                    NetworkHooks.openScreen((ServerPlayer) player, new NamedGuideBook());
                }
            } else {
                player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                    if (p.peekCodexBreadcrumb() != null && p.peekCodexBreadcrumb().Type == CodexBreadcrumb.Type.RECIPE) {
                        if (world.getGameTime() - this.lastActivateTime < 200L) {
                            this.guiOpenCount++;
                            if (this.guiOpenCount >= 4) {
                                DidYouKnowHelper.CheckAndShowDidYouKnow(player, "helptip.mna.pin_recipe");
                            }
                        }
                    } else {
                        this.guiOpenCount = 0;
                    }
                    this.lastActivateTime = world.getGameTime();
                });
            }
        }
        return InteractionResultHolder.success(book);
    }

    public boolean checkMagicUnlock(ServerLevel world, Player player) {
        IPlayerMagic playerMagic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (playerMagic != null && !playerMagic.isMagicUnlocked()) {
            if (world.getServer().isSingleplayer()) {
                world.setDayTime(world.m_46467_() + 12000L);
            }
            player.m_213846_(Component.translatable("item.mna.guide_book.magic_unlocked").withStyle(ChatFormatting.AQUA));
            playerMagic.setMagicLevel(player, 1);
            world.m_5594_(null, player.m_20183_(), SFX.Event.Player.MAGIC_UNLOCKED, SoundSource.PLAYERS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.m_60713_(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(context.getPlayer(), world, blockpos, blockstate, context.getItemInHand()) ? InteractionResult.sidedSuccess(world.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }
}