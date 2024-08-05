package journeymap.client.properties;

import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.CustomField;

public class WebMapProperties extends ClientPropertiesBase {

    public final BooleanField enabled = new BooleanField(ClientCategory.WebMap, "jm.webmap.enable", false, true);

    public final CustomField port = new CustomField(ClientCategory.WebMap, "jm.advanced.port", 80, 10000, 8080, false);

    @Override
    public String getName() {
        return "webmap";
    }
}