package org.embeddedt.modernfix.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.structure.CachingStructureManager;

public class ModernFixCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("modernfix").then(((LiteralArgumentBuilder) Commands.literal("upgradeStructures").requires(source -> source.hasPermission(3))).executes(context -> {
            ServerLevel level = ((CommandSourceStack) context.getSource()).getLevel();
            if (level == null) {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Couldn't find server level"));
                return 0;
            } else {
                ResourceManager manager = level.getServer().resources.resourceManager();
                Map<ResourceLocation, Resource> structures = manager.listResources("structures", p -> p.getPath().endsWith(".nbt"));
                int upgradedNum = 0;
                Pattern pathPattern = Pattern.compile("^structures/(.*)\\.nbt$");
                for (Entry<ResourceLocation, Resource> entry : structures.entrySet()) {
                    upgradedNum++;
                    ResourceLocation found = (ResourceLocation) entry.getKey();
                    Matcher matcher = pathPattern.matcher(found.getPath());
                    if (matcher.matches()) {
                        ResourceLocation structureLocation = new ResourceLocation(found.getNamespace(), matcher.group(1));
                        try {
                            InputStream resource = ((Resource) entry.getValue()).open();
                            try {
                                CachingStructureManager.readStructureTag(structureLocation, level.getServer().getFixerUpper(), resource);
                                Component msg = Component.literal("checked " + structureLocation + " (" + upgradedNum + "/" + structures.size() + ")");
                                ((CommandSourceStack) context.getSource()).sendSuccess(() -> msg, false);
                            } catch (Throwable var15) {
                                if (resource != null) {
                                    try {
                                        resource.close();
                                    } catch (Throwable var14) {
                                        var15.addSuppressed(var14);
                                    }
                                }
                                throw var15;
                            }
                            if (resource != null) {
                                resource.close();
                            }
                        } catch (Throwable var16) {
                            ModernFix.LOGGER.error("Couldn't upgrade structure " + found, var16);
                            ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("error reading " + structureLocation + " (" + upgradedNum + "/" + structures.size() + ")"));
                        }
                    }
                }
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("All structures upgraded"), false);
                return 1;
            }
        })));
    }
}