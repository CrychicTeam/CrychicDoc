package pie.ilikepiefoo.compat.jade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin("kubejsadditions")
public class AdditionalJadePlugin implements IWailaPlugin {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void register(IWailaCommonRegistration registration) {
        LOG.info("Jade Plugin Registering Common Data");
        WailaCommonRegistrationEventJS event = new WailaCommonRegistrationEventJS(registration);
        JadeEvents.ON_COMMON_REGISTRATION.post(event);
        event.register();
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        LOG.info("Jade Plugin Registering Client Data");
        WailaClientRegistrationEventJS event = new WailaClientRegistrationEventJS(registration);
        JadeEvents.ON_CLIENT_REGISTRATION.post(event);
        event.register();
    }
}