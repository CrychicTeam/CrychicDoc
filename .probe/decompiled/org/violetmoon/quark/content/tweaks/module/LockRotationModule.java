package org.violetmoon.quark.content.tweaks.module;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.api.IRotationLockable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.base.network.message.SetLockProfileMessage;
import org.violetmoon.quark.content.building.block.QuarkVerticalSlabBlock;
import org.violetmoon.quark.content.building.block.VerticalSlabBlock;
import org.violetmoon.zeta.client.event.load.ZKeyMapping;
import org.violetmoon.zeta.client.event.play.ZInput;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.play.entity.player.ZPlayer;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class LockRotationModule extends ZetaModule {

    private static final String TAG_LOCKED_ONCE = "quark:locked_once";

    private static final HashMap<UUID, LockRotationModule.LockProfile> lockProfiles = new HashMap();

    @Config(description = "When true, lock rotation indicator in the same style as crosshair")
    public static boolean renderLikeCrossHair = true;

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        lockProfiles.clear();
    }

    public static BlockState fixBlockRotation(BlockState state, BlockPlaceContext ctx) {
        if (state != null && ctx.m_43723_() != null && Quark.ZETA.modules.isEnabled(LockRotationModule.class)) {
            UUID uuid = ctx.m_43723_().m_20148_();
            if (lockProfiles.containsKey(uuid)) {
                LockRotationModule.LockProfile profile = (LockRotationModule.LockProfile) lockProfiles.get(uuid);
                BlockState transformed = getRotatedState(ctx.m_43725_(), ctx.getClickedPos(), state, profile.facing.getOpposite(), profile.half);
                if (!transformed.equals(state)) {
                    return Block.updateFromNeighbourShapes(transformed, ctx.m_43725_(), ctx.getClickedPos());
                }
            }
            return state;
        } else {
            return state;
        }
    }

    public static BlockState getRotatedState(Level world, BlockPos pos, BlockState state, Direction face, int half) {
        BlockState setState = state;
        ImmutableMap<Property<?>, Comparable<?>> props = state.m_61148_();
        Block block = state.m_60734_();
        if (block instanceof IRotationLockable lockable) {
            setState = lockable.applyRotationLock(world, pos, state, face, half);
        } else if (props.containsKey(BlockStateProperties.FACING)) {
            setState = (BlockState) state.m_61124_(BlockStateProperties.FACING, face);
        } else if (props.containsKey(QuarkVerticalSlabBlock.TYPE) && props.get(QuarkVerticalSlabBlock.TYPE) != VerticalSlabBlock.VerticalSlabType.DOUBLE && face.getAxis() != Direction.Axis.Y) {
            setState = (BlockState) state.m_61124_(QuarkVerticalSlabBlock.TYPE, (VerticalSlabBlock.VerticalSlabType) Objects.requireNonNull(VerticalSlabBlock.VerticalSlabType.fromDirection(face)));
        } else if (props.containsKey(BlockStateProperties.HORIZONTAL_FACING) && face.getAxis() != Direction.Axis.Y) {
            if (block instanceof StairBlock) {
                setState = (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, face.getOpposite());
            } else {
                setState = (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, face);
            }
        } else if (props.containsKey(BlockStateProperties.AXIS)) {
            setState = (BlockState) state.m_61124_(BlockStateProperties.AXIS, face.getAxis());
        } else if (props.containsKey(BlockStateProperties.FACING_HOPPER)) {
            setState = (BlockState) state.m_61124_(BlockStateProperties.FACING_HOPPER, face == Direction.DOWN ? face : face.getOpposite());
        }
        if (half != -1) {
            if (props.containsKey(BlockStateProperties.SLAB_TYPE) && props.get(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE) {
                setState = (BlockState) setState.m_61124_(BlockStateProperties.SLAB_TYPE, half == 1 ? SlabType.TOP : SlabType.BOTTOM);
            } else if (props.containsKey(BlockStateProperties.HALF)) {
                setState = (BlockState) setState.m_61124_(BlockStateProperties.HALF, half == 1 ? Half.TOP : Half.BOTTOM);
            }
        }
        return setState;
    }

    @PlayEvent
    public void onPlayerLogoff(ZPlayer.LoggedOut event) {
        lockProfiles.remove(event.getEntity().m_20148_());
    }

    public static void setProfile(Player player, LockRotationModule.LockProfile profile) {
        UUID uuid = player.m_20148_();
        if (profile == null) {
            lockProfiles.remove(uuid);
        } else {
            boolean locked = player.getPersistentData().getBoolean("quark:locked_once");
            if (!locked) {
                Component keybind = Component.keybind("quark.keybind.lock_rotation").withStyle(ChatFormatting.AQUA);
                Component text = Component.translatable("quark.misc.rotation_lock", keybind);
                player.m_213846_(text);
                player.getPersistentData().putBoolean("quark:locked_once", true);
            }
            lockProfiles.put(uuid, profile);
        }
    }

    @PlayEvent
    public void respawn(ZPlayer.Clone event) {
        if (event.getOriginal().getPersistentData().getBoolean("quark:locked_once")) {
            event.getEntity().getPersistentData().putBoolean("quark:locked_once", true);
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends LockRotationModule {

        private LockRotationModule.LockProfile clientProfile;

        private KeyMapping keybind;

        @LoadEvent
        public void registerKeybinds(ZKeyMapping event) {
            this.keybind = event.init("quark.keybind.lock_rotation", "k", "quark.gui.keygroup.misc");
        }

        @PlayEvent
        public void onMouseInput(ZInput.MouseButton event) {
            this.acceptInput();
        }

        @PlayEvent
        public void onKeyInput(ZInput.Key event) {
            this.acceptInput();
        }

        private void acceptInput() {
            Minecraft mc = Minecraft.getInstance();
            boolean down = this.keybind.isDown();
            if (mc.isWindowActive() && down && mc.screen == null) {
                LockRotationModule.LockProfile newProfile;
                label32: {
                    HitResult result = mc.hitResult;
                    if (result instanceof BlockHitResult bresult && result.getType() == HitResult.Type.BLOCK) {
                        Vec3 hitVec = bresult.m_82450_();
                        Direction face = bresult.getDirection();
                        int half = Math.abs((int) ((hitVec.y - (double) ((int) hitVec.y)) * 2.0));
                        if (face.getAxis() == Direction.Axis.Y) {
                            half = -1;
                        } else if (hitVec.y < 0.0) {
                            half = 1 - half;
                        }
                        newProfile = new LockRotationModule.LockProfile(face.getOpposite(), half);
                        break label32;
                    }
                    Vec3 look = mc.player.m_20154_();
                    newProfile = new LockRotationModule.LockProfile(Direction.getNearest((float) look.x, (float) look.y, (float) look.z), -1);
                }
                if (this.clientProfile != null && this.clientProfile.equals(newProfile)) {
                    this.clientProfile = null;
                } else {
                    this.clientProfile = newProfile;
                }
                QuarkClient.ZETA_CLIENT.sendToServer(new SetLockProfileMessage(this.clientProfile));
            }
        }

        @PlayEvent
        public void onHUDRender(ZRenderGuiOverlay.Crosshair.Post event) {
            if (this.clientProfile != null) {
                GuiGraphics guiGraphics = event.getGuiGraphics();
                RenderSystem.enableBlend();
                if (renderLikeCrossHair) {
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
                }
                Window window = event.getWindow();
                int x = window.getGuiScaledWidth() / 2 + 20;
                int y = window.getGuiScaledHeight() / 2 - 8;
                guiGraphics.blit(ClientUtil.GENERAL_ICONS, x, y, (float) (this.clientProfile.facing.ordinal() * 16), 65.0F, 16, 16, 256, 256);
                if (this.clientProfile.half > -1) {
                    guiGraphics.blit(ClientUtil.GENERAL_ICONS, x + 16, y, (float) (this.clientProfile.half * 16), 79.0F, 16, 16, 256, 256);
                }
            }
        }
    }

    public static record LockProfile(Direction facing, int half) {

        public static LockRotationModule.LockProfile readProfile(FriendlyByteBuf buf, Field field) {
            boolean valid = buf.readBoolean();
            if (!valid) {
                return null;
            } else {
                int face = buf.readInt();
                int half = buf.readInt();
                return new LockRotationModule.LockProfile(Direction.from3DDataValue(face), half);
            }
        }

        public static void writeProfile(FriendlyByteBuf buf, Field field, LockRotationModule.LockProfile p) {
            if (p == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeInt(p.facing.get3DDataValue());
                buf.writeInt(p.half);
            }
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else {
                return !(other instanceof LockRotationModule.LockProfile otherProfile) ? false : otherProfile.facing == this.facing && otherProfile.half == this.half;
            }
        }

        public int hashCode() {
            return this.facing.hashCode() * 31 + this.half;
        }
    }
}