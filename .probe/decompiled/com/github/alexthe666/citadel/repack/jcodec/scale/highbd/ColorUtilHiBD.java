package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;
import java.util.HashMap;
import java.util.Map;

public class ColorUtilHiBD {

    private static Map<ColorSpace, Map<ColorSpace, TransformHiBD>> map = new HashMap();

    public static TransformHiBD getTransform(ColorSpace from, ColorSpace to) {
        Map<ColorSpace, TransformHiBD> map2 = (Map<ColorSpace, TransformHiBD>) map.get(from);
        return map2 == null ? null : (TransformHiBD) map2.get(to);
    }

    static {
        Map<ColorSpace, TransformHiBD> rgb = new HashMap();
        rgb.put(ColorSpace.RGB, new ColorUtilHiBD.Idential());
        rgb.put(ColorSpace.YUV420, new RgbToYuv420pHiBD(0, 0));
        rgb.put(ColorSpace.YUV420J, new RgbToYuv420jHiBD());
        rgb.put(ColorSpace.YUV422, new RgbToYuv422pHiBD(0, 0));
        rgb.put(ColorSpace.YUV422_10, new RgbToYuv422pHiBD(2, 0));
        map.put(ColorSpace.RGB, rgb);
        Map<ColorSpace, TransformHiBD> yuv420 = new HashMap();
        yuv420.put(ColorSpace.YUV420, new ColorUtilHiBD.Idential());
        yuv420.put(ColorSpace.RGB, new Yuv420pToRgbHiBD(0, 0));
        yuv420.put(ColorSpace.YUV422, new Yuv420pToYuv422pHiBD(0, 0));
        yuv420.put(ColorSpace.YUV422_10, new Yuv420pToYuv422pHiBD(0, 2));
        map.put(ColorSpace.YUV420, yuv420);
        Map<ColorSpace, TransformHiBD> yuv422 = new HashMap();
        yuv422.put(ColorSpace.YUV422, new ColorUtilHiBD.Idential());
        yuv422.put(ColorSpace.RGB, new Yuv422pToRgbHiBD(0, 0));
        yuv422.put(ColorSpace.YUV420, new Yuv422pToYuv420pHiBD(0, 0));
        yuv422.put(ColorSpace.YUV420J, new Yuv422pToYuv420jHiBD(0, 0));
        map.put(ColorSpace.YUV422, yuv422);
        Map<ColorSpace, TransformHiBD> yuv422_10 = new HashMap();
        yuv422_10.put(ColorSpace.YUV422_10, new ColorUtilHiBD.Idential());
        yuv422_10.put(ColorSpace.RGB, new Yuv422pToRgbHiBD(2, 0));
        yuv422_10.put(ColorSpace.YUV420, new Yuv422pToYuv420pHiBD(0, 2));
        yuv422_10.put(ColorSpace.YUV420J, new Yuv422pToYuv420jHiBD(0, 2));
        map.put(ColorSpace.YUV422_10, yuv422_10);
        Map<ColorSpace, TransformHiBD> yuv444 = new HashMap();
        yuv444.put(ColorSpace.YUV444, new ColorUtilHiBD.Idential());
        yuv444.put(ColorSpace.RGB, new Yuv444pToRgb(0, 0));
        yuv444.put(ColorSpace.YUV420, new Yuv444pToYuv420pHiBD(0, 0));
        map.put(ColorSpace.YUV444, yuv444);
        Map<ColorSpace, TransformHiBD> yuv444_10 = new HashMap();
        yuv444_10.put(ColorSpace.YUV444_10, new ColorUtilHiBD.Idential());
        yuv444_10.put(ColorSpace.RGB, new Yuv444pToRgb(2, 0));
        yuv444_10.put(ColorSpace.YUV420, new Yuv444pToYuv420pHiBD(0, 2));
        map.put(ColorSpace.YUV444_10, yuv444_10);
        Map<ColorSpace, TransformHiBD> yuv420j = new HashMap();
        yuv420j.put(ColorSpace.YUV420J, new ColorUtilHiBD.Idential());
        yuv420j.put(ColorSpace.RGB, new Yuv420jToRgbHiBD());
        yuv420j.put(ColorSpace.YUV420, new Yuv420jToYuv420HiBD());
        map.put(ColorSpace.YUV420J, yuv420j);
        Map<ColorSpace, TransformHiBD> yuv422j = new HashMap();
        yuv422j.put(ColorSpace.YUV422J, new ColorUtilHiBD.Idential());
        yuv422j.put(ColorSpace.RGB, new Yuv422jToRgbHiBD());
        yuv422j.put(ColorSpace.YUV420, new Yuv422jToYuv420pHiBD());
        yuv422j.put(ColorSpace.YUV420J, new Yuv422pToYuv420pHiBD(0, 0));
        map.put(ColorSpace.YUV422J, yuv422j);
        Map<ColorSpace, TransformHiBD> yuv444j = new HashMap();
        yuv444j.put(ColorSpace.YUV444J, new ColorUtilHiBD.Idential());
        yuv444j.put(ColorSpace.RGB, new Yuv444jToRgbHiBD());
        yuv444j.put(ColorSpace.YUV420, new Yuv444jToYuv420pHiBD());
        yuv444j.put(ColorSpace.YUV420J, new Yuv444pToYuv420pHiBD(0, 0));
        map.put(ColorSpace.YUV444J, yuv444j);
    }

    public static class Idential implements TransformHiBD {

        @Override
        public void transform(PictureHiBD src, PictureHiBD dst) {
            for (int i = 0; i < 3; i++) {
                System.arraycopy(src.getPlaneData(i), 0, dst.getPlaneData(i), 0, Math.min(src.getPlaneWidth(i) * src.getPlaneHeight(i), dst.getPlaneWidth(i) * dst.getPlaneHeight(i)));
            }
        }
    }
}