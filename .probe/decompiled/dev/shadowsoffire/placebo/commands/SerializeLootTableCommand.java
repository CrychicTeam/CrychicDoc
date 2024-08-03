package dev.shadowsoffire.placebo.commands;

import com.google.gson.Gson;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LootCommand;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fml.loading.FMLPaths;

public class SerializeLootTableCommand {

    public static final Gson GSON = Deserializers.createLootTableSerializer().setPrettyPrinting().create();

    public static final DynamicCommandExceptionType NOT_FOUND = new DynamicCommandExceptionType(arg -> Component.translatable("placebo.cmd.not_found", arg));

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(((LiteralArgumentBuilder) Commands.literal("serialize_loot_table").requires(s -> s.hasPermission(2))).then(Commands.argument("loot_table", ResourceLocationArgument.id()).suggests(LootCommand.SUGGEST_LOOT_TABLE).executes(ctx -> {
            ResourceLocation id = ResourceLocationArgument.getId(ctx, "loot_table");
            LootTable table = ((CommandSourceStack) ctx.getSource()).getServer().getServerResources().managers().getLootData().m_278676_(id);
            if (table == LootTable.EMPTY) {
                throw NOT_FOUND.create(id);
            } else {
                String path = "placebo_serialized/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json";
                File file = new File(FMLPaths.GAMEDIR.get().toFile(), path);
                file.getParentFile().mkdirs();
                if (attemptSerialize(table, file)) {
                    ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.translatable("placebo.cmd.serialize_success", id, path), true);
                } else {
                    ((CommandSourceStack) ctx.getSource()).sendFailure(Component.translatable("placebo.cmd.serialize_failure"));
                }
                return 0;
            }
        })));
    }

    public static boolean attemptSerialize(LootTable table, File file) {
        String json = GSON.toJson(table);
        try {
            FileWriter w = new FileWriter(file);
            try {
                w.write(json);
            } catch (Throwable var7) {
                try {
                    w.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            w.close();
            return true;
        } catch (IOException var8) {
            var8.printStackTrace();
            return false;
        }
    }
}