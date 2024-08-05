package journeymap.client.ui.option;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.ui.component.DropDownButton;
import journeymap.client.ui.component.DropDownItem;
import journeymap.client.ui.component.PropertyDropdownButton;
import journeymap.common.Journeymap;
import journeymap.common.properties.config.StringField;

public class LocationFormat {

    private static String[] locationFormatIds = new String[] { "xzyv", "xyvz", "xzy", "xyz", "xz" };

    private HashMap<String, LocationFormat.LocationFormatKeys> idToFormat = new HashMap();

    public LocationFormat() {
        for (String id : locationFormatIds) {
            this.idToFormat.put(id, new LocationFormat.LocationFormatKeys(id));
        }
    }

    public LocationFormat.LocationFormatKeys getFormatKeys(String id) {
        LocationFormat.LocationFormatKeys locationLocationFormatKeys = (LocationFormat.LocationFormatKeys) this.idToFormat.get(id);
        if (locationLocationFormatKeys == null) {
            Journeymap.getLogger().warn("Invalid location format id: " + id);
            locationLocationFormatKeys = (LocationFormat.LocationFormatKeys) this.idToFormat.get(locationFormatIds[0]);
        }
        return locationLocationFormatKeys;
    }

    public String getLabel(String id) {
        return Constants.getString(this.getFormatKeys(id).label_key);
    }

    public static class Button extends PropertyDropdownButton<String> {

        LocationFormat locationFormat;

        public Button(StringField valueHolder) {
            super(Arrays.asList(LocationFormat.locationFormatIds), Constants.getString("jm.common.location_format"), valueHolder);
            this.buttonBuffer = 50;
            if (this.locationFormat == null) {
                this.locationFormat = new LocationFormat();
            }
        }

        @Override
        public String getFormattedLabel(String id) {
            if (this.locationFormat == null) {
                this.locationFormat = new LocationFormat();
            }
            return String.format("%1$s : %2$s %3$s %2$s", this.baseLabel, "⇕", this.locationFormat.getLabel(id));
        }

        @Override
        protected String getLabel(DropDownItem item) {
            return this.getFormattedLabel(item.getLabel());
        }

        public String getLabel(String id) {
            return this.locationFormat.getLabel(id);
        }

        @Override
        protected List<DropDownItem> setItems(Collection<String> values) {
            List<DropDownItem> items = Lists.newArrayList();
            values.forEach(value -> items.add(new LocationFormat.LocationDropDownItem(this, value, this.getLabel(value))));
            return items;
        }
    }

    public static class IdProvider implements StringField.ValuesProvider {

        @Override
        public List<String> getStrings() {
            return Arrays.asList(LocationFormat.locationFormatIds);
        }

        @Override
        public String getDefaultString() {
            return LocationFormat.locationFormatIds[0];
        }
    }

    static class LocationDropDownItem extends DropDownItem {

        public LocationDropDownItem(DropDownButton parent, Object id, String label) {
            super(parent, id, label);
        }

        @Override
        public String getLabel() {
            return super.getId().toString();
        }
    }

    public static class LocationFormatKeys {

        final String id;

        final String label_key;

        final String verbose_key;

        final String plain_key;

        LocationFormatKeys(String id) {
            this.id = id;
            this.label_key = String.format("jm.common.location_%s_label", id);
            this.verbose_key = String.format("jm.common.location_%s_verbose", id);
            this.plain_key = String.format("jm.common.location_%s_plain", id);
        }

        public String format(boolean verbose, int x, int z, int y, int vslice) {
            return verbose ? Constants.getString(this.verbose_key, x, z, y, vslice) : Constants.getString(this.plain_key, x, z, y, vslice);
        }
    }
}