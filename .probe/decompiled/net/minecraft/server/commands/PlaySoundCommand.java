package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class PlaySoundCommand {

    private static final SimpleCommandExceptionType ERROR_TOO_FAR = new SimpleCommandExceptionType(Component.translatable("commands.playsound.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> $$1 = Commands.argument("sound", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        for (SoundSource $$2 : SoundSource.values()) {
            $$1.then(source($$2));
        }
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("playsound").requires(p_138159_ -> p_138159_.hasPermission(2))).then($$1));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> source(SoundSource soundSource0) {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal(soundSource0.getName()).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(p_138180_ -> playSound((CommandSourceStack) p_138180_.getSource(), EntityArgument.getPlayers(p_138180_, "targets"), ResourceLocationArgument.getId(p_138180_, "sound"), soundSource0, ((CommandSourceStack) p_138180_.getSource()).getPosition(), 1.0F, 1.0F, 0.0F))).then(((RequiredArgumentBuilder) Commands.argument("pos", Vec3Argument.vec3()).executes(p_138177_ -> playSound((CommandSourceStack) p_138177_.getSource(), EntityArgument.getPlayers(p_138177_, "targets"), ResourceLocationArgument.getId(p_138177_, "sound"), soundSource0, Vec3Argument.getVec3(p_138177_, "pos"), 1.0F, 1.0F, 0.0F))).then(((RequiredArgumentBuilder) Commands.argument("volume", FloatArgumentType.floatArg(0.0F)).executes(p_138174_ -> playSound((CommandSourceStack) p_138174_.getSource(), EntityArgument.getPlayers(p_138174_, "targets"), ResourceLocationArgument.getId(p_138174_, "sound"), soundSource0, Vec3Argument.getVec3(p_138174_, "pos"), (Float) p_138174_.getArgument("volume", Float.class), 1.0F, 0.0F))).then(((RequiredArgumentBuilder) Commands.argument("pitch", FloatArgumentType.floatArg(0.0F, 2.0F)).executes(p_138171_ -> playSound((CommandSourceStack) p_138171_.getSource(), EntityArgument.getPlayers(p_138171_, "targets"), ResourceLocationArgument.getId(p_138171_, "sound"), soundSource0, Vec3Argument.getVec3(p_138171_, "pos"), (Float) p_138171_.getArgument("volume", Float.class), (Float) p_138171_.getArgument("pitch", Float.class), 0.0F))).then(Commands.argument("minVolume", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(p_138155_ -> playSound((CommandSourceStack) p_138155_.getSource(), EntityArgument.getPlayers(p_138155_, "targets"), ResourceLocationArgument.getId(p_138155_, "sound"), soundSource0, Vec3Argument.getVec3(p_138155_, "pos"), (Float) p_138155_.getArgument("volume", Float.class), (Float) p_138155_.getArgument("pitch", Float.class), (Float) p_138155_.getArgument("minVolume", Float.class))))))));
    }

    private static int playSound(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, ResourceLocation resourceLocation2, SoundSource soundSource3, Vec3 vec4, float float5, float float6, float float7) throws CommandSyntaxException {
        Holder<SoundEvent> $$8 = Holder.direct(SoundEvent.createVariableRangeEvent(resourceLocation2));
        double $$9 = (double) Mth.square($$8.value().getRange(float5));
        int $$10 = 0;
        long $$11 = commandSourceStack0.getLevel().m_213780_().nextLong();
        for (ServerPlayer $$12 : collectionServerPlayer1) {
            double $$13 = vec4.x - $$12.m_20185_();
            double $$14 = vec4.y - $$12.m_20186_();
            double $$15 = vec4.z - $$12.m_20189_();
            double $$16 = $$13 * $$13 + $$14 * $$14 + $$15 * $$15;
            Vec3 $$17 = vec4;
            float $$18 = float5;
            if ($$16 > $$9) {
                if (float7 <= 0.0F) {
                    continue;
                }
                double $$19 = Math.sqrt($$16);
                $$17 = new Vec3($$12.m_20185_() + $$13 / $$19 * 2.0, $$12.m_20186_() + $$14 / $$19 * 2.0, $$12.m_20189_() + $$15 / $$19 * 2.0);
                $$18 = float7;
            }
            $$12.connection.send(new ClientboundSoundPacket($$8, soundSource3, $$17.x(), $$17.y(), $$17.z(), $$18, float6, $$11));
            $$10++;
        }
        if ($$10 == 0) {
            throw ERROR_TOO_FAR.create();
        } else {
            if (collectionServerPlayer1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.playsound.success.single", resourceLocation2, ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.playsound.success.multiple", resourceLocation2, collectionServerPlayer1.size()), true);
            }
            return $$10;
        }
    }
}