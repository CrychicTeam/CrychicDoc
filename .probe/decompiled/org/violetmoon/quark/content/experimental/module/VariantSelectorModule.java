package org.violetmoon.quark.content.experimental.module;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.base.network.message.experimental.PlaceVariantRestoreMessage;
import org.violetmoon.quark.base.network.message.experimental.PlaceVariantUpdateMessage;
import org.violetmoon.quark.content.experimental.client.screen.VariantSelectorScreen;
import org.violetmoon.quark.content.experimental.client.tooltip.VariantsComponent;
import org.violetmoon.quark.content.experimental.config.VariantsConfig;
import org.violetmoon.quark.content.experimental.item.HammerItem;
import org.violetmoon.quark.mixin.mixins.accessor.AccessorBlockItem;
import org.violetmoon.zeta.client.event.load.ZKeyMapping;
import org.violetmoon.zeta.client.event.load.ZTooltipComponents;
import org.violetmoon.zeta.client.event.play.ZInput;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.client.event.play.ZRenderTooltip;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.ZEntityJoinLevel;
import org.violetmoon.zeta.event.play.entity.player.ZPlayer;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "experimental", enabledByDefault = false, description = "Allows placing variant blocks automatically via a selector menu triggered from a keybind")
public class VariantSelectorModule extends ZetaModule {

    private static final String TAG_CURRENT_VARIANT = "quark:CurrentSelectedVariant";

    @NotNull
    private static String clientVariant = "";

    private static boolean staticEnabled;

    @Config(description = "Set this to true to automatically convert any dropped variant items into their originals. Do this ONLY if you intend to take control of every recipe via a data pack or equivalent, as this will introduce dupes otherwise.")
    public static boolean convertVariantItems = false;

    @Config(flag = "hammer", description = "Enable the hammer, allowing variants to be swapped between eachother, including the original block. Do this ONLY under the same circumstances as Convert Variant Items.")
    public static boolean enableHammer = false;

    @Config
    public static boolean showTooltip = true;

    @Config
    public static boolean alignHudToHotbar = false;

    @Config
    public static boolean showSimpleHud = false;

    @Config
    public static boolean showHud = true;

    @Config
    public static boolean enableGreenTint = true;

    @Config
    public static boolean overrideHeldItemRender = true;

    @Config
    public static int hudOffsetX = 0;

    @Config
    public static int hudOffsetY = 0;

    @Config(description = "When true, selector arrow will render in same style as crosshair")
    public static boolean renderLikeCrossHair = true;

    @Config(description = "Uses smaller arrow icon for variant selector overlay")
    public static boolean smallerArrow = false;

    @Config
    public static VariantsConfig variants = new VariantsConfig();

    public static Item hammer;

    @LoadEvent
    public final void register(ZRegister event) {
        hammer = new HammerItem(this).setCondition(() -> enableHammer);
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    @NotNull
    public static String getSavedVariant(Player player) {
        return player.m_9236_().isClientSide ? clientVariant : player.getPersistentData().getString("quark:CurrentSelectedVariant");
    }

    public static void setSavedVariant(ServerPlayer player, @NotNull String variant) {
        if (variant.isEmpty() || variants.isKnownVariant(variant)) {
            player.getPersistentData().putString("quark:CurrentSelectedVariant", variant);
        }
    }

    @Nullable
    private static Block getMainHandVariantBlock(Player player, String variant) {
        ItemStack mainHand = player.m_21205_();
        if (mainHand.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return getVariantBlockFromOriginal(block, variant);
        } else {
            return null;
        }
    }

    @Nullable
    public static Block getVariantBlockFromOriginal(Block original, @NotNull String variant) {
        return variants.getBlockOfVariant(original, variant);
    }

    @Nullable
    public static Block getVariantBlockFromAny(Block block, @NotNull String variant) {
        Block originalBlock = variants.getOriginalBlock(block);
        Block variantBlock = variant.isEmpty() ? originalBlock : getVariantBlockFromOriginal(originalBlock, variant);
        return variantBlock != block ? variantBlock : null;
    }

    @PlayEvent
    public void onPlayerJoin(ZPlayer.LoggedIn event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            String variant = getSavedVariant(player);
            Quark.ZETA.network.sendToPlayer(new PlaceVariantRestoreMessage(variant), player);
        }
    }

    @PlayEvent
    public void addEntityToWorld(ZEntityJoinLevel event) {
        if (convertVariantItems && event.getEntity() instanceof ItemEntity ie) {
            ItemStack stack = ie.getItem();
            if (stack.getItem() instanceof BlockItem bi) {
                Block block = bi.getBlock();
                Block otherBlock = variants.getOriginalBlock(block);
                if (otherBlock != block) {
                    ItemStack clone = new ItemStack(otherBlock.asItem());
                    clone.setTag(stack.getTag());
                    clone.setCount(stack.getCount());
                    ie.setItem(clone);
                }
            }
        }
    }

    public static BlockState modifyBlockPlacementState(BlockState state, BlockPlaceContext ctx) {
        if (staticEnabled && state != null) {
            Player player = ctx.m_43723_();
            if (player != null) {
                String variant = getSavedVariant(player);
                if (!variant.isEmpty()) {
                    Block target = getVariantBlockFromOriginal(state.m_60734_(), variant);
                    if (target != null && target.asItem() instanceof AccessorBlockItem accessor) {
                        return accessor.quark$getPlacementState(ctx);
                    }
                }
            }
            return state;
        } else {
            return state;
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends VariantSelectorModule {

        private static KeyMapping variantSelectorKey;

        public static ItemStack modifyHeldItemStack(AbstractClientPlayer player, ItemStack stack) {
            if (VariantSelectorModule.staticEnabled && overrideHeldItemRender) {
                Minecraft mc = Minecraft.getInstance();
                if (player == mc.player && stack.getItem() instanceof BlockItem bi) {
                    Block block = bi.getBlock();
                    if (!VariantSelectorModule.clientVariant.isEmpty()) {
                        Block variant = variants.getBlockOfVariant(block, VariantSelectorModule.clientVariant);
                        if (variant != null && variant != block) {
                            return new ItemStack(variant);
                        }
                    }
                }
                return stack;
            } else {
                return stack;
            }
        }

        @LoadEvent
        public void registerKeybinds(ZKeyMapping event) {
            variantSelectorKey = event.init("quark.keybind.variant_selector", "r", "quark.gui.keygroup.misc");
        }

        public static void setClientVariant(String variant, boolean sync) {
            if (sync && !Objects.equals(VariantSelectorModule.clientVariant, variant)) {
                QuarkClient.ZETA_CLIENT.sendToServer(new PlaceVariantUpdateMessage(variant));
            }
            VariantSelectorModule.clientVariant = variant;
        }

        public static ItemStack onPickBlock(Player player, ItemStack pickResult) {
            if (!VariantSelectorModule.staticEnabled) {
                return null;
            } else {
                if (pickResult.getItem() instanceof BlockItem pickedVariant) {
                    Block pickedBlock = pickedVariant.getBlock();
                    Block baseBlock = null;
                    ItemStack mainHand = player.m_21205_();
                    if (mainHand.getItem() instanceof BlockItem handItem) {
                        baseBlock = handItem.getBlock();
                    } else if (mainHand.is(hammer)) {
                        baseBlock = variants.getOriginalBlock(pickedBlock);
                    }
                    if (baseBlock != pickedBlock) {
                        String variantKey = variants.getVariantOfBlock(baseBlock, pickedBlock);
                        if (variantKey != null) {
                            setClientVariant(variantKey, true);
                            return pickResult;
                        }
                    }
                    baseBlock = variants.getOriginalBlock(pickedBlock);
                    if (baseBlock != pickedBlock) {
                        ItemStack baseItem = new ItemStack(baseBlock);
                        if (!baseItem.isEmpty() && player.getInventory().m_216874_(i -> i.is(baseItem.getItem()))) {
                            String variantKey = variants.getVariantOfBlock(baseBlock, pickedBlock);
                            if (variantKey != null) {
                                setClientVariant(variantKey, true);
                            }
                            return baseItem;
                        }
                    }
                }
                return null;
            }
        }

        @PlayEvent
        public void keystroke(ZInput.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && event.getAction() == 1 && variantSelectorKey.isDown()) {
                ItemStack stack = mc.player.m_21205_();
                Block originalBlock = null;
                if (stack.is(hammer)) {
                    originalBlock = variants.getOriginalBlock(this.getLookedAtBlock());
                } else if (!stack.isEmpty() && stack.getItem() instanceof BlockItem bi) {
                    originalBlock = bi.getBlock();
                }
                if (originalBlock != null && variants.hasVariants(originalBlock)) {
                    mc.setScreen(new VariantSelectorScreen(originalBlock, variantSelectorKey, VariantSelectorModule.clientVariant, variants.getVisibleVariants()));
                }
            }
        }

        private Block getLookedAtBlock() {
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult instanceof BlockHitResult bhr) {
                BlockPos pos = bhr.getBlockPos();
                return mc.player.m_9236_().getBlockState(pos).m_60734_();
            } else {
                return null;
            }
        }

        @LoadEvent
        public void registerClientTooltipComponentFactories(ZTooltipComponents event) {
            event.register(VariantsComponent.class);
        }

        @PlayEvent
        public void gatherComponents(ZRenderTooltip.GatherComponents event) {
            if (showTooltip) {
                ItemStack stack = event.getItemStack();
                if (this.hasTooltip(stack)) {
                    List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
                    int index = 1;
                    if (Screen.hasShiftDown()) {
                        elements.add(index, Either.left(Component.translatable("quark.misc.variant_tooltip_header").withStyle(ChatFormatting.GRAY)));
                        elements.add(index + 1, Either.right(new VariantsComponent(stack)));
                    } else {
                        elements.add(index, Either.left(Component.translatable("quark.misc.variant_tooltip_hold_shift").withStyle(ChatFormatting.GRAY)));
                    }
                }
            }
        }

        private boolean hasTooltip(ItemStack stack) {
            return !stack.isEmpty() && stack.getItem() instanceof BlockItem bi && variants.hasVariants(bi.getBlock());
        }

        @PlayEvent
        public void onRender(ZRenderGuiOverlay.Crosshair.Pre event) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            Minecraft mc = Minecraft.getInstance();
            if (!(mc.screen instanceof VariantSelectorScreen) && showHud) {
                Player player = mc.player;
                String savedVariant = getSavedVariant(player);
                ItemStack mainHand = player.m_21205_();
                ItemStack displayLeft = mainHand.copy();
                Block variantBlock = null;
                if (displayLeft.is(hammer)) {
                    if (mc.hitResult instanceof BlockHitResult bhr) {
                        BlockPos pos = bhr.getBlockPos();
                        Block testBlock = player.m_9236_().getBlockState(pos).m_60734_();
                        displayLeft = new ItemStack(testBlock);
                        variantBlock = getVariantBlockFromAny(testBlock, savedVariant);
                    }
                } else {
                    variantBlock = VariantSelectorModule.getMainHandVariantBlock(player, savedVariant);
                }
                if (variantBlock != null) {
                    ItemStack displayRight = new ItemStack(variantBlock);
                    if (displayLeft.getItem() == displayRight.getItem()) {
                        return;
                    }
                    Window window = event.getWindow();
                    int x = window.getGuiScaledWidth() / 2;
                    int y = window.getGuiScaledHeight() / 2 + 12;
                    if (alignHudToHotbar) {
                        HumanoidArm arm = mc.options.mainHand().get();
                        if (arm == HumanoidArm.RIGHT) {
                            x += 125;
                        } else {
                            x -= 93;
                        }
                        y = window.getGuiScaledHeight() - 19;
                    }
                    int offset = 8;
                    int width = smallerArrow ? 13 : 16;
                    displayLeft.setCount(1);
                    int posX = x - offset - width + hudOffsetX;
                    int posY = y + hudOffsetY;
                    if (!showSimpleHud) {
                        guiGraphics.renderFakeItem(displayLeft, posX, posY);
                        RenderSystem.enableBlend();
                        if (renderLikeCrossHair) {
                            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        } else {
                            RenderSystem.defaultBlendFunc();
                            RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 0.7F);
                        }
                        if (smallerArrow) {
                            guiGraphics.blit(ClientUtil.GENERAL_ICONS, posX + 8, posY + 5, 0.0F, 158.0F, 22, 15, 256, 256);
                        } else {
                            guiGraphics.blit(ClientUtil.GENERAL_ICONS, posX + 8, posY, 0.0F, 141.0F, 22, 15, 256, 256);
                        }
                        RenderSystem.defaultBlendFunc();
                        posX += width * 2;
                    } else {
                        ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widget.png");
                        if (alignHudToHotbar) {
                            RenderSystem.enableBlend();
                            RenderSystem.blendFunc(770, 771);
                            if (enableGreenTint) {
                                RenderSystem.setShaderColor(0.5F, 1.0F, 0.5F, 1.0F);
                            } else {
                                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            }
                            guiGraphics.blit(WIDGETS_LOCATION, posX - 3, posY - 3, 24.0F, 23.0F, 22, 22, 256, 256);
                        } else {
                            posX += width;
                        }
                    }
                    guiGraphics.renderFakeItem(displayRight, posX, posY);
                }
            }
        }
    }
}