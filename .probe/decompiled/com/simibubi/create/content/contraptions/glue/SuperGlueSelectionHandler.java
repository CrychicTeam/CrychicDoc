package com.simibubi.create.content.contraptions.glue;

import com.google.common.base.Objects;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.chassis.AbstractChassisBlock;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RaycastHelper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class SuperGlueSelectionHandler {

    private static final int PASSIVE = 5083490;

    private static final int HIGHLIGHT = 6866310;

    private static final int FAIL = 12957000;

    private Object clusterOutlineSlot = new Object();

    private Object bbOutlineSlot = new Object();

    private int clusterCooldown;

    private BlockPos firstPos;

    private BlockPos hoveredPos;

    private Set<BlockPos> currentCluster;

    private int glueRequired;

    private SuperGlueEntity selected;

    private BlockPos soundSourceForRemoval;

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        BlockPos hovered = null;
        ItemStack stack = player.m_21205_();
        if (!this.isGlue(stack)) {
            if (this.firstPos != null) {
                this.discard();
            }
        } else {
            if (this.clusterCooldown > 0) {
                if (this.clusterCooldown == 25) {
                    player.displayClientMessage(Components.immutableEmpty(), true);
                }
                CreateClient.OUTLINER.keep(this.clusterOutlineSlot);
                this.clusterCooldown--;
            }
            AABB scanArea = player.m_20191_().inflate(32.0, 16.0, 32.0);
            List<SuperGlueEntity> glueNearby = mc.level.m_45976_(SuperGlueEntity.class, scanArea);
            this.selected = null;
            if (this.firstPos == null) {
                double range = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue() + 1.0;
                Vec3 traceOrigin = RaycastHelper.getTraceOrigin(player);
                Vec3 traceTarget = RaycastHelper.getTraceTarget(player, range, traceOrigin);
                double bestDistance = Double.MAX_VALUE;
                for (SuperGlueEntity glueEntity : glueNearby) {
                    Optional<Vec3> clip = glueEntity.m_20191_().clip(traceOrigin, traceTarget);
                    if (!clip.isEmpty()) {
                        Vec3 vec3 = (Vec3) clip.get();
                        double distanceToSqr = vec3.distanceToSqr(traceOrigin);
                        if (!(distanceToSqr > bestDistance)) {
                            this.selected = glueEntity;
                            this.soundSourceForRemoval = BlockPos.containing(vec3);
                            bestDistance = distanceToSqr;
                        }
                    }
                }
                for (SuperGlueEntity glueEntityx : glueNearby) {
                    boolean h = this.clusterCooldown == 0 && glueEntityx == this.selected;
                    AllSpecialTextures faceTex = h ? AllSpecialTextures.GLUE : null;
                    CreateClient.OUTLINER.showAABB(glueEntityx, glueEntityx.m_20191_()).colored(h ? 6866310 : 5083490).withFaceTextures(faceTex, faceTex).disableLineNormals().lineWidth(h ? 0.0625F : 0.015625F);
                }
            }
            HitResult hitResult = mc.hitResult;
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                hovered = ((BlockHitResult) hitResult).getBlockPos();
            }
            if (hovered == null) {
                this.hoveredPos = null;
            } else if (this.firstPos != null && !this.firstPos.m_123314_(hovered, 24.0)) {
                Lang.translate("super_glue.too_far").color(12957000).sendStatus(player);
            } else {
                boolean cancel = player.isShiftKeyDown();
                if (!cancel || this.firstPos != null) {
                    AABB currentSelectionBox = this.getCurrentSelectionBox();
                    boolean unchanged = Objects.equal(hovered, this.hoveredPos);
                    if (!unchanged) {
                        this.hoveredPos = hovered;
                        Set<BlockPos> cluster = SuperGlueSelectionHelper.searchGlueGroup(mc.level, this.firstPos, this.hoveredPos, true);
                        this.currentCluster = cluster;
                        this.glueRequired = 1;
                    } else {
                        if (this.currentCluster != null) {
                            boolean canReach = this.currentCluster.contains(hovered);
                            boolean canAfford = SuperGlueSelectionHelper.collectGlueFromInventory(player, this.glueRequired, true);
                            int color = 6866310;
                            String key = "super_glue.click_to_confirm";
                            if (!canReach) {
                                color = 12957000;
                                key = "super_glue.cannot_reach";
                            } else if (!canAfford) {
                                color = 12957000;
                                key = "super_glue.not_enough";
                            } else if (cancel) {
                                color = 12957000;
                                key = "super_glue.click_to_discard";
                            }
                            Lang.translate(key).color(color).sendStatus(player);
                            if (currentSelectionBox != null) {
                                CreateClient.OUTLINER.showAABB(this.bbOutlineSlot, currentSelectionBox).colored(canReach && canAfford && !cancel ? 6866310 : 12957000).withFaceTextures(AllSpecialTextures.GLUE, AllSpecialTextures.GLUE).disableLineNormals().lineWidth(0.0625F);
                            }
                            CreateClient.OUTLINER.showCluster(this.clusterOutlineSlot, this.currentCluster).colored(5083490).disableLineNormals().lineWidth(0.015625F);
                        }
                    }
                }
            }
        }
    }

    private boolean isGlue(ItemStack stack) {
        return stack.getItem() instanceof SuperGlueItem;
    }

    private AABB getCurrentSelectionBox() {
        return this.firstPos != null && this.hoveredPos != null ? new AABB(this.firstPos, this.hoveredPos).expandTowards(1.0, 1.0, 1.0) : null;
    }

    public boolean onMouseInput(boolean attack) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (!this.isGlue(player.m_21205_())) {
            return false;
        } else if (!player.m_36326_()) {
            return false;
        } else if (attack) {
            if (this.selected == null) {
                return false;
            } else {
                AllPackets.getChannel().sendToServer(new SuperGlueRemovalPacket(this.selected.m_19879_(), this.soundSourceForRemoval));
                this.selected = null;
                this.clusterCooldown = 0;
                return true;
            }
        } else if (player.isShiftKeyDown()) {
            if (this.firstPos != null) {
                this.discard();
                return true;
            } else {
                return false;
            }
        } else if (this.hoveredPos == null) {
            return false;
        } else {
            Direction face = null;
            if (mc.hitResult instanceof BlockHitResult bhr) {
                face = bhr.getDirection();
                BlockState blockState = level.m_8055_(this.hoveredPos);
                if (blockState.m_60734_() instanceof AbstractChassisBlock cb && cb.getGlueableSide(blockState, bhr.getDirection()) != null) {
                    return false;
                }
            }
            if (this.firstPos != null && this.currentCluster != null) {
                boolean canReach = this.currentCluster.contains(this.hoveredPos);
                boolean canAfford = SuperGlueSelectionHelper.collectGlueFromInventory(player, this.glueRequired, true);
                if (canReach && canAfford) {
                    this.confirm();
                    return true;
                } else {
                    return true;
                }
            } else {
                this.firstPos = this.hoveredPos;
                if (face != null) {
                    SuperGlueItem.spawnParticles(level, this.firstPos, face, true);
                }
                Lang.translate("super_glue.first_pos").sendStatus(player);
                AllSoundEvents.SLIME_ADDED.playAt(level, this.firstPos, 0.5F, 0.85F, false);
                level.m_5594_(player, this.firstPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75F, 1.0F);
                return true;
            }
        }
    }

    public void discard() {
        LocalPlayer player = Minecraft.getInstance().player;
        this.currentCluster = null;
        this.firstPos = null;
        Lang.translate("super_glue.abort").sendStatus(player);
        this.clusterCooldown = 0;
    }

    public void confirm() {
        LocalPlayer player = Minecraft.getInstance().player;
        AllPackets.getChannel().sendToServer(new SuperGlueSelectionPacket(this.firstPos, this.hoveredPos));
        AllSoundEvents.SLIME_ADDED.playAt(player.m_9236_(), this.hoveredPos, 0.5F, 0.95F, false);
        player.m_9236_().playSound((Player) player, this.hoveredPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75F, 1.0F);
        if (this.currentCluster != null) {
            CreateClient.OUTLINER.showCluster(this.clusterOutlineSlot, this.currentCluster).colored(11924166).withFaceTextures(AllSpecialTextures.GLUE, AllSpecialTextures.HIGHLIGHT_CHECKERED).disableLineNormals().lineWidth(0.041666668F);
        }
        this.discard();
        Lang.translate("super_glue.success").sendStatus(player);
        this.clusterCooldown = 40;
    }
}