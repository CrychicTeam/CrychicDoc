package journeymap.client.ui.component;

import journeymap.common.properties.config.ConfigField;

public interface IConfigFieldHolder<T extends ConfigField> {

    T getConfigField();
}