package icyllis.modernui.animation;

public abstract class BidirectionalTypeConverter<T, V> implements TypeConverter<T, V> {

    BidirectionalTypeConverter<V, T> mInvertedConverter;

    public abstract T convertBack(V var1);

    public final BidirectionalTypeConverter<V, T> invert() {
        if (this.mInvertedConverter == null) {
            this.mInvertedConverter = new BidirectionalTypeConverter.InvertedConverter<>(this);
        }
        return this.mInvertedConverter;
    }

    private static class InvertedConverter<From, To> extends BidirectionalTypeConverter<From, To> {

        public InvertedConverter(BidirectionalTypeConverter<To, From> converter) {
            this.mInvertedConverter = converter;
        }

        @Override
        public From convertBack(To value) {
            return this.mInvertedConverter.convert(value);
        }

        @Override
        public To convert(From value) {
            return this.mInvertedConverter.convertBack(value);
        }
    }
}