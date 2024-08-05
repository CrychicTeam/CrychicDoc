package com.github.alexthe666.citadel.repack.jaad.aac.sbr;

class DCT {

    private static final int n = 32;

    private static final float[] w_array_real = new float[] { 1.0F, 0.98078525F, 0.9238795F, 0.8314696F, 0.70710677F, 0.5555702F, 0.3826834F, 0.19509028F, 0.0F, -0.19509037F, -0.3826835F, -0.5555703F, -0.7071068F, -0.83146966F, -0.92387956F, -0.9807853F };

    private static final float[] w_array_imag = new float[] { 0.0F, -0.19509032F, -0.38268346F, -0.55557024F, -0.70710677F, -0.83146966F, -0.92387956F, -0.9807853F, -1.0F, -0.98078525F, -0.9238795F, -0.8314696F, -0.7071067F, -0.5555702F, -0.38268337F, -0.19509023F };

    private static final float[] dct4_64_tab = new float[] { 0.9999247F, 0.9981181F, 0.993907F, 0.9873014F, 0.9783174F, 0.96697646F, 0.953306F, 0.937339F, 0.9191139F, 0.8986745F, 0.8760701F, 0.8513552F, 0.82458925F, 0.7958369F, 0.76516724F, 0.7326543F, 0.69837624F, 0.66241574F, 0.62485945F, 0.58579785F, 0.545325F, 0.5035384F, 0.46053872F, 0.41642955F, 0.37131715F, 0.32531023F, 0.2785196F, 0.23105814F, 0.18303989F, 0.13458069F, 0.08579727F, 0.036807165F, -1.0121963F, -1.0594388F, -1.1041292F, -1.1461595F, -1.1854287F, -1.2218422F, -1.255312F, -1.2857577F, -1.313106F, -1.3372908F, -1.3582538F, -1.3759449F, -1.390321F, -1.4013479F, -1.4089987F, -1.4132552F, -1.4141071F, -1.4115522F, -1.4055967F, -1.396255F, -1.3835497F, -1.3675113F, -1.3481784F, -1.3255975F, -1.2998233F, -1.2709177F, -1.2389501F, -1.2039981F, -1.1661453F, -1.1254834F, -1.0821099F, -1.0361296F, -0.9876532F, -0.9367974F, -0.88368475F, -0.8284433F, -0.771206F, -0.71211076F, -0.6513001F, -0.58892035F, -0.5251218F, -0.46005824F, -0.39388633F, -0.32676548F, -0.25885743F, -0.19032592F, -0.121335685F, -0.052053273F, 0.017354608F, 0.086720645F, 0.15587783F, 0.22465932F, 0.29289973F, 0.3604344F, 0.42710093F, 0.49273846F, 0.5571889F, 0.62029713F, 0.681911F, 0.74188185F, 0.8000656F, 0.856322F, 0.91051537F, 0.96251523F, 1.0F, 0.99879545F, 0.9951847F, 0.9891765F, 0.98078525F, 0.97003126F, 0.95694035F, 0.94154406F, 0.9238795F, 0.9039893F, 0.88192123F, 0.8577286F, 0.8314696F, 0.8032075F, 0.77301043F, 0.7409511F, 0.70710677F, 0.6715589F, 0.6343933F, 0.5956993F, 0.5555702F, 0.5141027F, 0.47139665F, 0.4275551F, 0.38268343F, 0.33688983F, 0.29028463F, 0.24298012F, 0.19509023F, 0.1467305F, 0.098017134F, 0.04906765F, -1.0F, -1.0478631F, -1.0932019F, -1.1359069F, -1.1758755F, -1.2130115F, -1.247225F, -1.2784339F, -1.3065629F, -1.3315444F, -1.353318F, -1.3718314F, -1.3870399F, -1.3989068F, -1.4074037F, -1.4125102F, 0.0F, -1.4125102F, -1.4074037F, -1.3989068F, -1.3870399F, -1.3718314F, -1.353318F, -1.3315444F, -1.3065629F, -1.2784339F, -1.247225F, -1.2130114F, -1.1758755F, -1.135907F, -1.0932019F, -1.0478631F, -1.0F, -0.9497278F, -0.89716756F, -0.842446F, -0.78569496F, -0.7270511F, -0.66665566F, -0.6046542F, -0.54119605F, -0.47643423F, -0.4105245F, -0.34362584F, -0.27589935F, -0.2075082F, -0.1386171F, -0.069392145F, 0.0F, 0.069392264F, 0.13861716F, 0.2075082F, 0.27589947F, 0.34362596F, 0.41052464F, 0.4764342F, 0.5411961F, 0.6046542F, 0.6666557F, 0.72705114F, 0.7856951F, 0.842446F, 0.89716756F, 0.9497278F };

    private static final int[] bit_rev_tab = new int[] { 0, 16, 8, 24, 4, 20, 12, 28, 2, 18, 10, 26, 6, 22, 14, 30, 1, 17, 9, 25, 5, 21, 13, 29, 3, 19, 11, 27, 7, 23, 15, 31 };

    private static void fft_dif(float[] Real, float[] Imag) {
        for (int i = 0; i < 16; i++) {
            float point1_real = Real[i];
            float point1_imag = Imag[i];
            int i2 = i + 16;
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            float w_real = w_array_real[i];
            float w_imag = w_array_imag[i];
            point1_real -= point2_real;
            point1_imag -= point2_imag;
            Real[i] += point2_real;
            Imag[i] += point2_imag;
            Real[i2] = point1_real * w_real - point1_imag * w_imag;
            Imag[i2] = point1_real * w_imag + point1_imag * w_real;
        }
        int j = 0;
        for (int w_index = 0; j < 8; w_index += 2) {
            float w_real = w_array_real[w_index];
            float w_imag = w_array_imag[w_index];
            float point1_real = Real[j];
            float point1_imag = Imag[j];
            int i2 = j + 8;
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            point1_real -= point2_real;
            point1_imag -= point2_imag;
            Real[j] += point2_real;
            Imag[j] += point2_imag;
            Real[i2] = point1_real * w_real - point1_imag * w_imag;
            Imag[i2] = point1_real * w_imag + point1_imag * w_real;
            int var62 = j + 16;
            point1_real = Real[var62];
            point1_imag = Imag[var62];
            i2 = var62 + 8;
            point2_real = Real[i2];
            point2_imag = Imag[i2];
            point1_real -= point2_real;
            point1_imag -= point2_imag;
            Real[var62] += point2_real;
            Imag[var62] += point2_imag;
            Real[i2] = point1_real * w_real - point1_imag * w_imag;
            Imag[i2] = point1_real * w_imag + point1_imag * w_real;
            j++;
        }
        for (int var63 = 0; var63 < 32; var63 += 8) {
            int i2 = var63 + 4;
            float point1_real = Real[var63];
            float point1_imag = Imag[var63];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            Real[var63] += point2_real;
            Imag[var63] += point2_imag;
            Real[i2] = point1_real - point2_real;
            Imag[i2] = point1_imag - point2_imag;
        }
        float w_real = w_array_real[4];
        for (int var64 = 1; var64 < 32; var64 += 8) {
            int i2 = var64 + 4;
            float point1_real = Real[var64];
            float point1_imag = Imag[var64];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            point1_real -= point2_real;
            point1_imag -= point2_imag;
            Real[var64] += point2_real;
            Imag[var64] += point2_imag;
            Real[i2] = (point1_real + point1_imag) * w_real;
            Imag[i2] = (point1_imag - point1_real) * w_real;
        }
        for (int var65 = 2; var65 < 32; var65 += 8) {
            int i2 = var65 + 4;
            float point1_real = Real[var65];
            float point1_imag = Imag[var65];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            Real[var65] += point2_real;
            Imag[var65] += point2_imag;
            Real[i2] = point1_imag - point2_imag;
            Imag[i2] = point2_real - point1_real;
        }
        w_real = w_array_real[12];
        for (int var66 = 3; var66 < 32; var66 += 8) {
            int i2 = var66 + 4;
            float point1_real = Real[var66];
            float point1_imag = Imag[var66];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            point1_real -= point2_real;
            point1_imag -= point2_imag;
            Real[var66] += point2_real;
            Imag[var66] += point2_imag;
            Real[i2] = (point1_real - point1_imag) * w_real;
            Imag[i2] = (point1_real + point1_imag) * w_real;
        }
        for (int var67 = 0; var67 < 32; var67 += 4) {
            int i2 = var67 + 2;
            float point1_real = Real[var67];
            float point1_imag = Imag[var67];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            Real[var67] += point2_real;
            Imag[var67] += point2_imag;
            Real[i2] = point1_real - point2_real;
            Imag[i2] = point1_imag - point2_imag;
        }
        for (int var68 = 1; var68 < 32; var68 += 4) {
            int i2 = var68 + 2;
            float point1_real = Real[var68];
            float point1_imag = Imag[var68];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            Real[var68] += point2_real;
            Imag[var68] += point2_imag;
            Real[i2] = point1_imag - point2_imag;
            Imag[i2] = point2_real - point1_real;
        }
        for (int var69 = 0; var69 < 32; var69 += 2) {
            int i2 = var69 + 1;
            float point1_real = Real[var69];
            float point1_imag = Imag[var69];
            float point2_real = Real[i2];
            float point2_imag = Imag[i2];
            Real[var69] += point2_real;
            Imag[var69] += point2_imag;
            Real[i2] = point1_real - point2_real;
            Imag[i2] = point1_imag - point2_imag;
        }
    }

    public static void dct4_kernel(float[] in_real, float[] in_imag, float[] out_real, float[] out_imag) {
        for (int i = 0; i < 32; i++) {
            float x_re = in_real[i];
            float x_im = in_imag[i];
            float tmp = (x_re + x_im) * dct4_64_tab[i];
            in_real[i] = x_im * dct4_64_tab[i + 64] + tmp;
            in_imag[i] = x_re * dct4_64_tab[i + 32] + tmp;
        }
        fft_dif(in_real, in_imag);
        for (int var9 = 0; var9 < 16; var9++) {
            int i_rev = bit_rev_tab[var9];
            float x_re = in_real[i_rev];
            float x_im = in_imag[i_rev];
            float tmp = (x_re + x_im) * dct4_64_tab[var9 + 96];
            out_real[var9] = x_im * dct4_64_tab[var9 + 160] + tmp;
            out_imag[var9] = x_re * dct4_64_tab[var9 + 128] + tmp;
        }
        out_imag[16] = (in_imag[1] - in_real[1]) * dct4_64_tab[112];
        out_real[16] = (in_real[1] + in_imag[1]) * dct4_64_tab[112];
        for (int var10 = 17; var10 < 32; var10++) {
            int i_rev = bit_rev_tab[var10];
            float x_re = in_real[i_rev];
            float x_im = in_imag[i_rev];
            float tmp = (x_re + x_im) * dct4_64_tab[var10 + 96];
            out_real[var10] = x_im * dct4_64_tab[var10 + 160] + tmp;
            out_imag[var10] = x_re * dct4_64_tab[var10 + 128] + tmp;
        }
    }
}