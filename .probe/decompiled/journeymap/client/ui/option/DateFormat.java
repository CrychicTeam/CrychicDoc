package journeymap.client.ui.option;

import java.util.Arrays;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.ui.component.PropertyDropdownButton;
import journeymap.common.properties.config.StringField;

public class DateFormat {

    private static String[] timeFormatValues = new String[] { "MM-dd-yyyy", "MM-dd-yy", "dd-MM-yyyy", "dd-MM-yy", "yyyy-MM-dd", "yy-MM-dd" };

    public static class Button extends PropertyDropdownButton<String> {

        DateFormat timeFormat;

        public Button(StringField valueHolder) {
            super(Arrays.asList(DateFormat.timeFormatValues), Constants.getString("jm.common.date_format"), valueHolder);
            if (this.timeFormat == null) {
                this.timeFormat = new DateFormat();
            }
        }
    }

    public static class Provider implements StringField.ValuesProvider {

        @Override
        public List<String> getStrings() {
            return Arrays.asList(DateFormat.timeFormatValues);
        }

        @Override
        public String getDefaultString() {
            return DateFormat.timeFormatValues[0];
        }
    }
}