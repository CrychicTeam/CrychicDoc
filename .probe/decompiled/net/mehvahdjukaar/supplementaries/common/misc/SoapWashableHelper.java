package net.mehvahdjukaar.supplementaries.common.misc;

import com.google.common.collect.BiMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.block.IWashable;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundParticlePacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.utils.BlockPredicate;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoapWashableHelper {

    public static boolean tryWash(Level level, BlockPos pos, BlockState state) {
        if (!tryWashWithInterface(level, pos, state) && !tryCleaningSign(level, pos, state) && !tryChangingColor(level, pos, state) && !tryUnoxidise(level, pos, state)) {
            return false;
        } else {
            if (level instanceof ServerLevel serverLevel) {
                ModNetwork.CHANNEL.sendToAllClientPlayersInRange(serverLevel, pos, 64.0, new ClientBoundParticlePacket(pos, ClientBoundParticlePacket.EventType.BUBBLE_CLEAN));
            }
            return true;
        }
    }

    private static boolean tryCleaningSign(Level level, BlockPos pos, BlockState state) {
        if (state.m_60734_() instanceof SignBlock && level.getBlockEntity(pos) instanceof SignBlockEntity te && te.isWaxed()) {
            te.setWaxed(false);
            if (!level.isClientSide) {
                te.m_6596_();
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean tryUnoxidise(Level level, BlockPos pos, BlockState state) {
        Block b = state.m_60734_();
        BlockState toPlace = null;
        for (Entry<BlockPredicate, ResourceLocation> e : ((Map) CommonConfigs.Functional.SOAP_SPECIAL.get()).entrySet()) {
            if (((BlockPredicate) e.getKey()).test(state)) {
                toPlace = (BlockState) BuiltInRegistries.BLOCK.m_6612_((ResourceLocation) e.getValue()).map(s -> s.withPropertiesOf(state)).orElse(null);
                break;
            }
        }
        if (toPlace == null) {
            toPlace = SuppPlatformStuff.getUnoxidised(level, pos, state);
        }
        if (toPlace == null) {
            Block unWaxed = (Block) ((BiMap) HoneycombItem.WAXABLES.get()).inverse().get(b);
            if (unWaxed == null) {
                unWaxed = b;
            }
            unWaxed = WeatheringCopper.getFirst(unWaxed);
            if (unWaxed != b) {
                toPlace = unWaxed.withPropertiesOf(state);
            }
        }
        if (toPlace == null) {
            toPlace = tryParse(state);
        }
        if (toPlace != null) {
            level.setBlock(pos, toPlace, 11);
            return true;
        } else {
            return false;
        }
    }

    private static BlockState tryParse(BlockState oldState) {
        ResourceLocation r = Utils.getID(oldState.m_60734_());
        String name = r.getPath();
        String[] keywords = new String[] { "waxed_", "weathered_", "exposed_", "oxidized_", "_waxed", "_weathered", "_exposed", "_oxidized" };
        for (String key : keywords) {
            if (name.contains(key)) {
                String newName = name.replace(key, "");
                Optional<Block> bb = BuiltInRegistries.BLOCK.m_6612_(new ResourceLocation(r.getNamespace(), newName));
                if (bb.isEmpty()) {
                    bb = BuiltInRegistries.BLOCK.m_6612_(new ResourceLocation(newName));
                }
                if (bb.isPresent()) {
                    BlockState newState = ((Block) bb.get()).withPropertiesOf(oldState);
                    if (newState != oldState) {
                        return newState;
                    }
                }
            }
        }
        return null;
    }

    private static boolean tryWashWithInterface(Level level, BlockPos pos, BlockState state) {
        Block b = state.m_60734_();
        IWashable cap;
        if (b instanceof IWashable soapWashable) {
            cap = soapWashable;
        } else {
            cap = SuppPlatformStuff.getForgeCap(b, IWashable.class);
        }
        if (cap == null) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile != null) {
                if (tile instanceof IWashable soapWashable) {
                    cap = soapWashable;
                } else {
                    cap = SuppPlatformStuff.getForgeCap(tile, IWashable.class);
                }
            }
        }
        return cap != null ? cap.tryWash(level, pos, state) : false;
    }

    private static boolean tryChangingColor(Level level, BlockPos pos, BlockState state) {
        Block newColor = BlocksColorAPI.changeColor(state.m_60734_(), null);
        if (newColor != null) {
            if (((List) CommonConfigs.Functional.SOAP_DYE_CLEAN_BLACKLIST.get()).contains(BlocksColorAPI.getKey(state.m_60734_()))) {
                return false;
            } else if (state.m_60734_() instanceof BedBlock) {
                return false;
            } else {
                CompoundTag tag = null;
                if (newColor instanceof EntityBlock) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be != null) {
                        tag = be.saveWithoutMetadata();
                    }
                }
                BlockState toPlace = newColor.withPropertiesOf(state);
                level.setBlock(pos, toPlace, 2);
                if (tag != null) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be != null) {
                        be.load(tag);
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }
}