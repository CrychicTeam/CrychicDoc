package portb.biggerstacks.net;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import portb.biggerstacks.Constants;
import portb.configlib.template.ConfigTemplate;
import portb.configlib.xml.Condition;
import portb.configlib.xml.Operator;
import portb.configlib.xml.OrBlock;
import portb.configlib.xml.Property;
import portb.configlib.xml.Rule;

public class ServerboundCreateConfigTemplatePacket extends GenericTemplateOptionsPacket {

    public ServerboundCreateConfigTemplatePacket(int normalStackLimit, int potionsStackLimit, int enchantedBooksStackLimit) {
        super(normalStackLimit, potionsStackLimit, enchantedBooksStackLimit);
    }

    public ServerboundCreateConfigTemplatePacket(FriendlyByteBuf buf) {
        super(buf);
    }

    static void handleCreateConfigTemplate(ServerboundCreateConfigTemplatePacket serverboundCreateConfigTemplatePacket, Supplier<NetworkEvent.Context> contextSupplier) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            ServerPlayer sender = ((NetworkEvent.Context) contextSupplier.get()).getSender();
            if (sender == null || !sender.m_20310_(4)) {
                return;
            }
        }
        ConfigTemplate template = ConfigTemplate.generateTemplate(serverboundCreateConfigTemplatePacket);
        if (ModList.get().isLoaded("ic2")) {
            template.getRules().add(new Rule(Arrays.asList(new Condition(Property.MOD_ID, Operator.EQUALS, "ic2"), new OrBlock(Arrays.asList(new Condition(Property.ID, Operator.STRING_STARTS_WITH, "ic2:upgrade"), new Condition(Property.ID, Operator.STRING_ENDS_WITH, "pad_upgrade"), new Condition(Property.ID, Operator.STRING_ENDS_WITH, "upgrade_kit")))), 64));
        }
        try {
            Files.writeString(Constants.RULESET_FILE, template.toXML(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        super.encode(buf);
    }
}