package com.structureessentials.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class Command {

    public LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext buildContext) {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("structureessentials").then(Commands.literal("getBiomeTags").then(Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(buildContext, Registries.BIOME)).executes(context -> {
            ResourceKey<Biome> biome = ((Holder.Reference) ResourceOrTagArgument.getResourceOrTag(context, "biome", Registries.BIOME).unwrap().left().get()).key();
            List<TagKey<Biome>> biomeTags = (List<TagKey<Biome>>) ((Holder.Reference) ((Registry) ((CommandSourceStack) context.getSource()).registryAccess().registry(Registries.BIOME).get()).getHolder(biome).get()).tags().collect(Collectors.toList());
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Biome tags for: " + biome.location()).withStyle(ChatFormatting.GOLD));
            for (TagKey<Biome> biomeTag : biomeTags) {
                ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("#" + biomeTag.location()));
            }
            return 1;
        })))).then(Commands.literal("getBiomesForTag").then(Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(buildContext, Registries.BIOME)).executes(context -> {
            TagKey<Biome> biomeTag = ((HolderSet.Named) ResourceOrTagArgument.getResourceOrTag(context, "biome", Registries.BIOME).unwrap().right().get()).key();
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Biomes for tag: " + biomeTag.location()).withStyle(ChatFormatting.GOLD));
            for (Holder<Biome> biomeHolder : ((Registry) ((CommandSourceStack) context.getSource()).registryAccess().registry(Registries.BIOME).get()).asHolderIdMap()) {
                if (biomeHolder.is(biomeTag)) {
                    ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Biome: " + ((ResourceKey) biomeHolder.unwrapKey().get()).location()));
                }
            }
            return 1;
        })))).then(((LiteralArgumentBuilder) Commands.literal("getStructuresNearby").requires(stack -> stack.hasPermission(2))).executes(context -> {
            ServerLevel world = ((CommandSourceStack) context.getSource()).getLevel();
            Map<Structure, LongSet> structures = new HashMap();
            ChunkPos start = new ChunkPos(BlockPos.containing(((CommandSourceStack) context.getSource()).getPosition()));
            for (int x = -5; x < 5; x++) {
                for (int z = -5; z < 5; z++) {
                    for (Entry<Structure, LongSet> entry : world.structureManager().getAllStructuresAt(new BlockPos(start.x + x << 4, 0, start.z + z << 4)).entrySet()) {
                        ((LongSet) structures.computeIfAbsent((Structure) entry.getKey(), k -> new LongOpenHashSet((LongCollection) entry.getValue()))).addAll((LongCollection) entry.getValue());
                    }
                }
            }
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Structures nearby: ").withStyle(ChatFormatting.GOLD));
            Map<BlockPos, String> structurePositions = new HashMap();
            for (Entry<Structure, LongSet> structureEntry : structures.entrySet()) {
                world.structureManager().fillStartsForStructure((Structure) structureEntry.getKey(), (LongSet) structureEntry.getValue(), structureStart -> structurePositions.put(structureStart.getBoundingBox().getCenter(), ((Registry) ((CommandSourceStack) context.getSource()).registryAccess().registry(Registries.STRUCTURE).get()).getKey((Structure) structureEntry.getKey()).toString()));
            }
            List<Entry<BlockPos, String>> sortedStructures = new ArrayList(structurePositions.entrySet());
            sortedStructures.sort(Comparator.comparingDouble(p -> ((BlockPos) p.getKey()).m_123331_(BlockPos.containing(((CommandSourceStack) context.getSource()).getPosition()))));
            for (Entry<BlockPos, String> structureEntry : sortedStructures) {
                ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal((String) structureEntry.getValue()).append(Component.literal(" " + structureEntry.getKey()).withStyle(ChatFormatting.YELLOW).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + ((BlockPos) structureEntry.getKey()).m_123341_() + " " + ((BlockPos) structureEntry.getKey()).m_123342_() + " " + ((BlockPos) structureEntry.getKey()).m_123343_())))));
            }
            return 1;
        }))).then(Commands.literal("getSimilarForBiome").then(Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(buildContext, Registries.BIOME)).executes(context -> {
            ResourceKey<Biome> biome = ((Holder.Reference) ResourceOrTagArgument.getResourceOrTag(context, "biome", Registries.BIOME).unwrap().left().get()).key();
            List<TagKey<Biome>> biomeTags = (List<TagKey<Biome>>) ((Holder.Reference) ((Registry) ((CommandSourceStack) context.getSource()).registryAccess().registry(Registries.BIOME).get()).getHolder(biome).get()).tags().collect(Collectors.toList());
            List<Holder<Biome>> similarBiomes = new ArrayList();
            for (Holder<Biome> currentBiome : ((Registry) ((CommandSourceStack) context.getSource()).registryAccess().registry(Registries.BIOME).get()).asHolderIdMap()) {
                for (TagKey<Biome> tag : biomeTags) {
                    if (currentBiome.is(tag)) {
                        similarBiomes.add(currentBiome);
                    }
                }
            }
            Map<Holder<Biome>, Integer> countMap = new HashMap();
            for (Holder<Biome> similarBiome : similarBiomes) {
                for (TagKey<Biome> similarBiomeTagKey : similarBiome.tags().toList()) {
                    if (biomeTags.contains(similarBiomeTagKey)) {
                        countMap.put(similarBiome, (Integer) countMap.getOrDefault(similarBiome, 0) + 2);
                    } else {
                        countMap.put(similarBiome, (Integer) countMap.getOrDefault(similarBiome, 0) - 1);
                    }
                }
            }
            List<Entry<Holder<Biome>, Integer>> sortedBiomeHolders = new ArrayList(countMap.entrySet());
            sortedBiomeHolders.sort(Comparator.comparingInt(e -> (Integer) ((Entry) e).getValue()).reversed());
            Map<TagKey<Biome>, Double> tagCountMap = new HashMap();
            for (int i = 0; i < sortedBiomeHolders.size(); i++) {
                double weight = ((double) sortedBiomeHolders.size() / 6.0 - (double) i) / ((double) sortedBiomeHolders.size() / 6.0);
                if ((double) i > (double) sortedBiomeHolders.size() / 6.0) {
                    weight = -((double) i - (double) sortedBiomeHolders.size() * 0.16666666666666666) / ((double) sortedBiomeHolders.size() * 0.8333333333333334);
                }
                Entry<Holder<Biome>, Integer> biomeHolderEntry = (Entry<Holder<Biome>, Integer>) sortedBiomeHolders.get(i);
                for (TagKey<Biome> biomeHolderEntryTag : ((Holder) biomeHolderEntry.getKey()).tags().toList()) {
                    if (biomeTags.contains(biomeHolderEntryTag)) {
                        tagCountMap.put(biomeHolderEntryTag, (Double) tagCountMap.getOrDefault(biomeHolderEntryTag, 0.0) + 1.0 * weight);
                    }
                }
            }
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Similar biomes for: " + biome.location()).withStyle(ChatFormatting.GOLD));
            for (int i = 0; i < sortedBiomeHolders.size() && i < 10; i++) {
                ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Weight:" + ((Entry) sortedBiomeHolders.get(i)).getValue() + " Biome: " + ((ResourceKey) ((Holder) ((Entry) sortedBiomeHolders.get(i)).getKey()).unwrap().left().get()).location()));
            }
            List<Entry<TagKey<Biome>, Double>> sortedBiomeTagKeys = new ArrayList(tagCountMap.entrySet());
            sortedBiomeTagKeys.sort(Comparator.comparingDouble(e -> (Double) ((Entry) e).getValue()).reversed());
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Similar biome tags for: " + biome.location()).withStyle(ChatFormatting.GOLD));
            for (Entry<TagKey<Biome>, Double> tagx : sortedBiomeTagKeys) {
                ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.literal("Weight:" + Math.round((Double) tagx.getValue()) + " Tag: #" + ((TagKey) tagx.getKey()).location()));
            }
            return 1;
        })));
    }
}