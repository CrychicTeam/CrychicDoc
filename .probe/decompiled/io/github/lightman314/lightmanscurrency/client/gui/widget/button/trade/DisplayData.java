package io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public record DisplayData(int xOffset, int yOffset, int width, int height) {

    @Nonnull
    public List<DisplayData> divide(int count) {
        if (count <= 1) {
            return Lists.newArrayList(new DisplayData[] { this });
        } else {
            int partialWidth = this.width / count;
            int x = this.xOffset;
            List<DisplayData> result = new ArrayList();
            for (int i = 0; i < count; i++) {
                result.add(new DisplayData(x, this.yOffset, partialWidth, this.height));
                x += partialWidth;
            }
            return result;
        }
    }
}