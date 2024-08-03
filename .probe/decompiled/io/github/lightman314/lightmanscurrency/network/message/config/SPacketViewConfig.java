package io.github.lightman314.lightmanscurrency.network.message.config;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketViewConfig extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketViewConfig> HANDLER = new SPacketViewConfig.H();

    private final String fileName;

    private final String option;

    public SPacketViewConfig(@Nonnull String fileName, @Nonnull String option) {
        this.fileName = fileName;
        this.option = option;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(this.fileName);
        buffer.writeUtf(this.option);
    }

    private static class H extends CustomPacket.Handler<SPacketViewConfig> {

        @Nonnull
        public SPacketViewConfig decode(@Nonnull FriendlyByteBuf buffer) {
            String fileName = buffer.readUtf();
            String option = buffer.readUtf();
            return new SPacketViewConfig(fileName, option);
        }

        protected void handle(@Nonnull SPacketViewConfig message, @Nullable ServerPlayer sender) {
            for (ConfigFile file : ConfigFile.getAvailableFiles()) {
                if (file.isClientOnly() && file.getFileName().equals(message.fileName)) {
                    Map<String, ConfigOption<?>> optionMap = file.getAllOptions();
                    if (optionMap.containsKey(message.option)) {
                        ConfigOption<?> option = (ConfigOption<?>) optionMap.get(message.option);
                        LightmansCurrency.PROXY.sendClientMessage(LCText.COMMAND_CONFIG_VIEW.get(option.getName()));
                        LightmansCurrency.PROXY.sendClientMessage(EasyText.literal(option.write()));
                    } else {
                        LightmansCurrency.PROXY.sendClientMessage(LCText.COMMAND_CONFIG_FAIL_MISSING.get().withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }
}