package snownee.kiwi.customization.command;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.customization.block.BlockFundamentals;
import snownee.kiwi.customization.shape.ShapeGenerator;
import snownee.kiwi.customization.shape.UnbakedShapeCodec;
import snownee.kiwi.util.resource.OneTimeLoader;

public class ExportShapesCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("shapes").executes(ctx -> exportShapes((CommandSourceStack) ctx.getSource())));
    }

    private static int exportShapes(CommandSourceStack source) {
        Map<String, String> data = Maps.newTreeMap();
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("exported_shapes.json"));
            try {
                BlockFundamentals fundamentals = BlockFundamentals.reload(CustomizationHooks.collectKiwiPacks(), new OneTimeLoader.Context(), false);
                fundamentals.shapes().forEach((key, value) -> {
                    if (value.getClass() == ShapeGenerator.Unit.class) {
                        String string = key.toString();
                        if (!"minecraft:empty".equals(string) && !"minecraft:block".equals(string)) {
                            data.put(string, UnbakedShapeCodec.encodeVoxelShape(ShapeGenerator.Unit.unboxOrThrow(value)));
                        }
                    }
                });
                new GsonBuilder().setPrettyPrinting().create().toJson(data, writer);
            } catch (Throwable var6) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (writer != null) {
                writer.close();
            }
        } catch (Exception var7) {
            source.sendFailure(Component.literal(var7.getMessage()));
            return 0;
        }
        source.sendSuccess(() -> Component.literal("Shapes exported"), false);
        return 1;
    }
}