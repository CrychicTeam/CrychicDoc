package snownee.jade.api.theme;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.Identifiers;

public class Theme {

    public static final Theme DARK = new Theme();

    public ResourceLocation id;

    public int backgroundColor = -1;

    public final int[] borderColor = new int[] { -13092808, -13092808, -14408668, -14408668 };

    public int titleColor = -1;

    public int normalColor = -6250336;

    public int infoColor = -1;

    public int successColor = -11141291;

    public int warningColor = -3123;

    public int dangerColor = -43691;

    public int failureColor = -5636096;

    public int boxBorderColor = -8355712;

    public int itemAmountColor = -1;

    public boolean textShadow = true;

    public ResourceLocation backgroundTexture;

    public int[] backgroundTextureUV;

    public ResourceLocation backgroundTexture_withIcon;

    public int[] backgroundTextureUV_withIcon;

    public final int[] padding = new int[] { 4, 3, 1, 4 };

    public Boolean squareBorder;

    public float opacity;

    public int[] bottomProgressOffset;

    public int bottomProgressNormalColor = -1;

    public int bottomProgressFailureColor = -48060;

    public boolean lightColorScheme;

    public boolean hidden;

    static {
        DARK.id = Identifiers.JADE("dark");
        DARK.backgroundColor = 1250067;
    }
}