package icyllis.modernui.graphics;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

@Deprecated
public class ImageStore {

    private static final ImageStore INSTANCE = new ImageStore();

    private final Object mLock = new Object();

    private HashMap<String, HashMap<String, WeakReference<Image>>> mImages = new HashMap();

    private ImageStore() {
    }

    public static ImageStore getInstance() {
        return INSTANCE;
    }

    public void clear() {
        synchronized (this.mLock) {
            for (HashMap<String, WeakReference<Image>> cache : this.mImages.values()) {
                for (WeakReference<Image> entry : cache.values()) {
                    Image image = (Image) entry.get();
                    if (image != null) {
                        image.close();
                    }
                }
            }
            this.mImages = new HashMap();
        }
    }

    @Nullable
    public Image getOrCreate(@NonNull String namespace, @NonNull String path) {
        synchronized (this.mLock) {
            HashMap<String, WeakReference<Image>> cache = (HashMap<String, WeakReference<Image>>) this.mImages.computeIfAbsent(namespace, __ -> new HashMap());
            WeakReference<Image> imageRef = (WeakReference<Image>) cache.get(path);
            Image image;
            if (imageRef != null && (image = (Image) imageRef.get()) != null && !image.isClosed()) {
                return image;
            }
        }
        try {
            InputStream stream = ModernUI.getInstance().getResourceStream(namespace, path);
            Image var23;
            label136: {
                label155: {
                    try (Bitmap bitmap = BitmapFactory.decodeStream(stream)) {
                        Image newImage = Image.createTextureFromBitmap(bitmap);
                        synchronized (this.mLock) {
                            HashMap<String, WeakReference<Image>> cache = (HashMap<String, WeakReference<Image>>) this.mImages.computeIfAbsent(namespace, __ -> new HashMap());
                            WeakReference<Image> imageRef = (WeakReference<Image>) cache.get(path);
                            Image image;
                            if (imageRef != null && (image = (Image) imageRef.get()) != null && !image.isClosed()) {
                                if (newImage != null) {
                                    newImage.close();
                                }
                                var23 = image;
                                break label155;
                            }
                            if (newImage != null) {
                                cache.put(path, new WeakReference(newImage));
                                var23 = newImage;
                                break label136;
                            }
                        }
                    } catch (Throwable var16) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var12) {
                                var16.addSuppressed(var12);
                            }
                        }
                        throw var16;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    return null;
                }
                if (stream != null) {
                    stream.close();
                }
                return var23;
            }
            if (stream != null) {
                stream.close();
            }
            return var23;
        } catch (IOException var17) {
            var17.printStackTrace();
            return null;
        }
    }
}