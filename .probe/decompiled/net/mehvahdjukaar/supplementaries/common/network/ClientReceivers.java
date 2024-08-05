package net.mehvahdjukaar.supplementaries.common.network;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.PlayerSuggestionBoxWidget;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlintBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpeakerBlockTile;
import net.mehvahdjukaar.supplementaries.common.inventories.RedMerchantMenu;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.common.items.InstrumentItem;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClientReceivers {

    private static void withPlayerDo(Consumer<Player> action) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            action.accept(player);
        }
    }

    private static void withLevelDo(Consumer<Level> action) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            action.accept(level);
        }
    }

    public static void handlePlaySpeakerMessagePacket(ClientBoundPlaySpeakerMessagePacket message) {
        SpeakerBlockTile.Mode mode = message.mode;
        Component str = Minecraft.getInstance().isTextFilteringEnabled() ? message.filtered : message.message;
        if (mode == SpeakerBlockTile.Mode.NARRATOR && !(Boolean) ClientConfigs.Blocks.SPEAKER_BLOCK_MUTE.get()) {
            Minecraft.getInstance().getNarrator().narrator.say(str.getString(), true);
        } else if (mode == SpeakerBlockTile.Mode.TITLE) {
            Gui gui = Minecraft.getInstance().gui;
            gui.clear();
            gui.resetTitleTimes();
            gui.setTitle(str);
        } else {
            withPlayerDo(p -> p.displayClientMessage(str, mode == SpeakerBlockTile.Mode.STATUS_MESSAGE));
        }
    }

    public static void handleSendBombKnockbackPacket(ClientBoundSendKnockbackPacket message) {
        withLevelDo(l -> {
            Entity e = l.getEntity(message.id);
            if (e != null) {
                e.setDeltaMovement(e.getDeltaMovement().add(message.knockbackX, message.knockbackY, message.knockbackZ));
            }
        });
    }

    public static void handleLoginPacket(ClientBoundSendLoginPacket message) {
        withPlayerDo(p -> PlayerSuggestionBoxWidget.setUsernameCache(message.usernameCache));
    }

    public static void handleSpawnBlockParticlePacket(ClientBoundParticlePacket message) {
        withLevelDo(l -> {
            switch(message.id) {
                case BUBBLE_BLOW:
                    ParticleUtil.spawnParticlesOnBlockFaces(l, BlockPos.containing(message.pos), (ParticleOptions) ModParticles.SUDS_PARTICLE.get(), UniformInt.of(2, 4), 0.001F, 0.01F, true);
                    break;
                case BUBBLE_CLEAN:
                    ParticleUtil.spawnParticleOnBlockShape(l, BlockPos.containing(message.pos), (ParticleOptions) ModParticles.SUDS_PARTICLE.get(), UniformInt.of(2, 4), 0.01F);
                    break;
                case WAX_ON:
                    ParticleUtil.spawnParticleOnBlockShape(l, BlockPos.containing(message.pos), ParticleTypes.WAX_ON, UniformInt.of(3, 5), 0.01F);
                    break;
                case BUBBLE_CLEAN_ENTITY:
                    if (message.extraData != null) {
                        Entity e = l.getEntity(message.extraData);
                        if (e != null) {
                            ParticleUtil.spawnParticleOnBoundingBox(e.getBoundingBox(), l, (ParticleOptions) ModParticles.SUDS_PARTICLE.get(), UniformInt.of(2, 4), 0.01F);
                        }
                    }
                    break;
                case DISPENSER_MINECART:
                    int j1 = 0;
                    int j2 = 1;
                    int k2 = 0;
                    double d18 = message.pos.x + (double) j1 * 0.6;
                    double d24 = message.pos.y + (double) j2 * 0.6;
                    double d28 = message.pos.z + (double) k2 * 0.6;
                    for (int i3 = 0; i3 < 10; i3++) {
                        double d4 = l.random.nextDouble() * 0.2 + 0.01;
                        double d6 = d18 + (double) j1 * 0.01 + (l.random.nextDouble() - 0.5) * (double) k2 * 0.5;
                        double d8 = d24 + (double) j2 * 0.01 + (l.random.nextDouble() - 0.5) * (double) j2 * 0.5;
                        double d30 = d28 + (double) k2 * 0.01 + (l.random.nextDouble() - 0.5) * (double) j1 * 0.5;
                        double d9 = (double) j1 * d4 + l.random.nextGaussian() * 0.01;
                        double d10 = (double) j2 * d4 + l.random.nextGaussian() * 0.01;
                        double d11 = (double) k2 * d4 + l.random.nextGaussian() * 0.01;
                        l.addParticle(ParticleTypes.SMOKE, d6, d8, d30, d9, d10, d11);
                    }
                    break;
                case FLINT_BLOCK_IGNITE:
                    if (message.extraData != null && message.pos != null) {
                        boolean isIronMoving = message.extraData == 1;
                        BlockPos pos = BlockPos.containing(message.pos);
                        for (Direction ironDir : Direction.values()) {
                            BlockPos facingPos = pos.relative(ironDir);
                            BlockState facingState = l.getBlockState(facingPos);
                            if (isIronMoving ? facingState.m_60713_((Block) ModRegistry.FLINT_BLOCK.get()) : FlintBlock.canBlockCreateSpark(facingState, l, facingPos, ironDir.getOpposite())) {
                                for (int i = 0; i < 6; i++) {
                                    ParticleUtil.spawnParticleOnFace(l, facingPos, ironDir.getOpposite(), ParticleTypes.CRIT, -0.5F, 0.5F, false);
                                }
                            }
                        }
                    }
            }
        });
    }

    public static void handleSyncAntiqueInkPacket(ClientBoundSyncAntiqueInk message) {
        withLevelDo(l -> {
            BlockEntity tile = l.getBlockEntity(message.pos);
            if (tile != null) {
                AntiqueInkItem.setAntiqueInk(tile, message.ink);
            }
        });
    }

    public static void handlePlaySongNotesPacket(ClientBoundPlaySongNotesPacket message) {
        withLevelDo(l -> {
            if (l.getEntity(message.entityID) instanceof Player p && p.m_21211_().getItem() instanceof InstrumentItem instrumentItem) {
                IntListIterator var7 = message.notes.iterator();
                while (var7.hasNext()) {
                    int note = (Integer) var7.next();
                    if (note > 0) {
                        l.playSound(Minecraft.getInstance().player, p.m_20185_(), p.m_20186_(), p.m_20189_(), instrumentItem.getSound(), SoundSource.PLAYERS, instrumentItem.getVolume(), instrumentItem.getPitch(note));
                        instrumentItem.spawnNoteParticle(l, p, note);
                    }
                }
            }
        });
    }

    public static void handleSyncTradesPacket(ClientBoundSyncTradesPacket message) {
        withPlayerDo(p -> {
            AbstractContainerMenu container = p.containerMenu;
            if (message.containerId == container.containerId && container instanceof RedMerchantMenu containerMenu) {
                containerMenu.setOffers(new MerchantOffers(message.offers.createTag()));
                containerMenu.setXp(message.villagerXp);
                containerMenu.setMerchantLevel(message.villagerLevel);
                containerMenu.setShowProgressBar(message.showProgress);
                containerMenu.setCanRestock(message.canRestock);
            }
        });
    }

    public static void handleSyncQuiverPacket(SyncSkellyQuiverPacket message) {
        withLevelDo(l -> {
            if (l.getEntity(message.entityID) instanceof IQuiverEntity qe) {
                qe.supplementaries$setQuiver(message.on ? ((QuiverItem) ModRegistry.QUIVER_ITEM.get()).m_7968_() : ItemStack.EMPTY);
            }
        });
    }
}