package harmonised.pmmo.client.utils;

import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.RegistryUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientUtils {

    private static Minecraft mc = Minecraft.getInstance();

    private static Map<String, Map<Integer, Map<ReqType, List<Component>>>> cache = new HashMap();

    public static List<ClientTooltipComponent> ctc(MutableComponent component, int width) {
        return mc.font.split(component, width).stream().map(fcs -> ClientTooltipComponent.create(fcs)).toList();
    }

    public static void invalidateUnlocksCache() {
        cache.clear();
    }

    private static void cacheUnlocks() {
        LocalPlayer player = mc.player;
        Core core = Core.get(LogicalSide.CLIENT);
        Arrays.stream(ReqType.values()).forEach(reqType -> {
            if (reqType.itemApplicable) {
                ForgeRegistries.ITEMS.getValues().stream().map(ItemStack::new).forEach(stack -> core.getReqMap(reqType, stack, false).forEach((key, value) -> ((List) ((Map) ((Map) cache.computeIfAbsent(key, s -> new HashMap())).computeIfAbsent(value, v -> new HashMap())).computeIfAbsent(reqType, r -> new ArrayList())).add(stack.getDisplayName())));
            }
            if (reqType.blockApplicable) {
                ForgeRegistries.BLOCKS.getValues().forEach(block -> core.getCommonReqData(new HashMap(), ObjectType.BLOCK, RegistryUtil.getId(block), reqType, new CompoundTag()).forEach((key, value) -> ((List) ((Map) ((Map) cache.computeIfAbsent(key, s -> new HashMap())).computeIfAbsent(value, v -> new HashMap())).computeIfAbsent(reqType, r -> new ArrayList())).add(new ItemStack(block.asItem()).getDisplayName())));
            }
            if (reqType.entityApplicable) {
                ForgeRegistries.ENTITY_TYPES.getValues().stream().map(entity -> entity.create(player.m_9236_())).forEach(entity -> core.getReqMap(reqType, entity).forEach((key, value) -> ((List) ((Map) ((Map) cache.computeIfAbsent(key, s -> new HashMap())).computeIfAbsent(value, v -> new HashMap())).computeIfAbsent(reqType, r -> new ArrayList())).add(entity.getDisplayName())));
            }
            if (reqType == ReqType.TRAVEL) {
                player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME).entrySet().stream().map(entry -> ((ResourceKey) entry.getKey()).location()).forEach(biomeID -> core.getCommonReqData(new HashMap(), ObjectType.BIOME, biomeID, reqType, new CompoundTag()).forEach((key, value) -> ((List) ((Map) ((Map) cache.computeIfAbsent(key, s -> new HashMap())).computeIfAbsent(value, v -> new HashMap())).computeIfAbsent(reqType, r -> new ArrayList())).add(Component.literal(biomeID.toString()))));
                player.connection.levels().stream().map(ResourceKey::m_135782_).forEach(dimID -> core.getCommonReqData(new HashMap(), ObjectType.DIMENSION, dimID, reqType, new CompoundTag()).forEach((key, value) -> ((List) ((Map) ((Map) cache.computeIfAbsent(key, s -> new HashMap())).computeIfAbsent(value, v -> new HashMap())).computeIfAbsent(reqType, r -> new ArrayList())).add(Component.literal(dimID.toString()))));
            }
        });
    }

    public static void sendLevelUpUnlocks(String skill, int level) {
        if (Config.SKILLUP_UNLOCKS.get()) {
            if (cache.isEmpty()) {
                cacheUnlocks();
            }
            LocalPlayer player = mc.player;
            ((Map) ((Map) cache.getOrDefault(skill, new HashMap())).getOrDefault(level, new HashMap())).entrySet().stream().filter(entry -> !((List) entry.getValue()).isEmpty()).forEach(entry -> {
                MutableComponent header = Component.translatable("pmmo.enum." + ((ReqType) entry.getKey()).name());
                header.setStyle(Style.EMPTY.applyFormats(ChatFormatting.GOLD, ChatFormatting.BOLD));
                player.sendSystemMessage(header);
                ((List) entry.getValue()).forEach(player::m_213846_);
            });
        }
    }
}