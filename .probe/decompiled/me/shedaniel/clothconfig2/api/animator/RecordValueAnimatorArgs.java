package me.shedaniel.clothconfig2.api.animator;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
class RecordValueAnimatorArgs {

    public static class Arg1<A1, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final RecordValueAnimatorArgs.Arg1.Op<A1, T> op;

        private final RecordValueAnimatorArgs.Arg1.Up<A1, T> up;

        public Arg1(ValueAnimator<A1> a1, RecordValueAnimatorArgs.Arg1.Op<A1, T> op, RecordValueAnimatorArgs.Arg1.Up<A1, T> up) {
            this.a1 = a1;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value());
        }

        @FunctionalInterface
        public interface Op<A1, T> {

            T construct(A1 var1);
        }

        @FunctionalInterface
        public interface Up<A1, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2);
        }
    }

    public static class Arg10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final RecordValueAnimatorArgs.Arg10.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> op;

        private final RecordValueAnimatorArgs.Arg10.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> up;

        public Arg10(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, RecordValueAnimatorArgs.Arg10.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> op, RecordValueAnimatorArgs.Arg10.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11);
        }
    }

    public static class Arg11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final RecordValueAnimatorArgs.Arg11.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> op;

        private final RecordValueAnimatorArgs.Arg11.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> up;

        public Arg11(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, RecordValueAnimatorArgs.Arg11.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> op, RecordValueAnimatorArgs.Arg11.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12);
        }
    }

    public static class Arg12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final RecordValueAnimatorArgs.Arg12.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> op;

        private final RecordValueAnimatorArgs.Arg12.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> up;

        public Arg12(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, RecordValueAnimatorArgs.Arg12.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> op, RecordValueAnimatorArgs.Arg12.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13);
        }
    }

    public static class Arg13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final RecordValueAnimatorArgs.Arg13.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> op;

        private final RecordValueAnimatorArgs.Arg13.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> up;

        public Arg13(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, RecordValueAnimatorArgs.Arg13.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> op, RecordValueAnimatorArgs.Arg13.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14);
        }
    }

    public static class Arg14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final RecordValueAnimatorArgs.Arg14.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> op;

        private final RecordValueAnimatorArgs.Arg14.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> up;

        public Arg14(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, RecordValueAnimatorArgs.Arg14.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> op, RecordValueAnimatorArgs.Arg14.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15);
        }
    }

    public static class Arg15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final ValueAnimator<A15> a15;

        private final RecordValueAnimatorArgs.Arg15.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> op;

        private final RecordValueAnimatorArgs.Arg15.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> up;

        public Arg15(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, ValueAnimator<A15> a15, RecordValueAnimatorArgs.Arg15.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> op, RecordValueAnimatorArgs.Arg15.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).add(this.a15).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration), v15 -> this.a15.setTo(v15, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget, this.a15::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target(), this.a15.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value(), this.a15.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14, A15 var15);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15, RecordValueAnimatorArgs.Setter<A15> var16);
        }
    }

    public static class Arg16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final ValueAnimator<A15> a15;

        private final ValueAnimator<A16> a16;

        private final RecordValueAnimatorArgs.Arg16.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> op;

        private final RecordValueAnimatorArgs.Arg16.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> up;

        public Arg16(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, ValueAnimator<A15> a15, ValueAnimator<A16> a16, RecordValueAnimatorArgs.Arg16.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> op, RecordValueAnimatorArgs.Arg16.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).add(this.a15).add(this.a16).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration), v15 -> this.a15.setTo(v15, duration), v16 -> this.a16.setTo(v16, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget, this.a15::setTarget, this.a16::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target(), this.a15.target(), this.a16.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value(), this.a15.value(), this.a16.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14, A15 var15, A16 var16);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15, RecordValueAnimatorArgs.Setter<A15> var16, RecordValueAnimatorArgs.Setter<A16> var17);
        }
    }

    public static class Arg17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final ValueAnimator<A15> a15;

        private final ValueAnimator<A16> a16;

        private final ValueAnimator<A17> a17;

        private final RecordValueAnimatorArgs.Arg17.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> op;

        private final RecordValueAnimatorArgs.Arg17.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> up;

        public Arg17(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, ValueAnimator<A15> a15, ValueAnimator<A16> a16, ValueAnimator<A17> a17, RecordValueAnimatorArgs.Arg17.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> op, RecordValueAnimatorArgs.Arg17.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).add(this.a15).add(this.a16).add(this.a17).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration), v15 -> this.a15.setTo(v15, duration), v16 -> this.a16.setTo(v16, duration), v17 -> this.a17.setTo(v17, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget, this.a15::setTarget, this.a16::setTarget, this.a17::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target(), this.a15.target(), this.a16.target(), this.a17.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value(), this.a15.value(), this.a16.value(), this.a17.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14, A15 var15, A16 var16, A17 var17);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15, RecordValueAnimatorArgs.Setter<A15> var16, RecordValueAnimatorArgs.Setter<A16> var17, RecordValueAnimatorArgs.Setter<A17> var18);
        }
    }

    public static class Arg18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final ValueAnimator<A15> a15;

        private final ValueAnimator<A16> a16;

        private final ValueAnimator<A17> a17;

        private final ValueAnimator<A18> a18;

        private final RecordValueAnimatorArgs.Arg18.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> op;

        private final RecordValueAnimatorArgs.Arg18.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> up;

        public Arg18(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, ValueAnimator<A15> a15, ValueAnimator<A16> a16, ValueAnimator<A17> a17, ValueAnimator<A18> a18, RecordValueAnimatorArgs.Arg18.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> op, RecordValueAnimatorArgs.Arg18.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).add(this.a15).add(this.a16).add(this.a17).add(this.a18).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration), v15 -> this.a15.setTo(v15, duration), v16 -> this.a16.setTo(v16, duration), v17 -> this.a17.setTo(v17, duration), v18 -> this.a18.setTo(v18, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget, this.a15::setTarget, this.a16::setTarget, this.a17::setTarget, this.a18::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target(), this.a15.target(), this.a16.target(), this.a17.target(), this.a18.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value(), this.a15.value(), this.a16.value(), this.a17.value(), this.a18.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14, A15 var15, A16 var16, A17 var17, A18 var18);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15, RecordValueAnimatorArgs.Setter<A15> var16, RecordValueAnimatorArgs.Setter<A16> var17, RecordValueAnimatorArgs.Setter<A17> var18, RecordValueAnimatorArgs.Setter<A18> var19);
        }
    }

    public static class Arg19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final ValueAnimator<A15> a15;

        private final ValueAnimator<A16> a16;

        private final ValueAnimator<A17> a17;

        private final ValueAnimator<A18> a18;

        private final ValueAnimator<A19> a19;

        private final RecordValueAnimatorArgs.Arg19.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> op;

        private final RecordValueAnimatorArgs.Arg19.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> up;

        public Arg19(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, ValueAnimator<A15> a15, ValueAnimator<A16> a16, ValueAnimator<A17> a17, ValueAnimator<A18> a18, ValueAnimator<A19> a19, RecordValueAnimatorArgs.Arg19.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> op, RecordValueAnimatorArgs.Arg19.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).add(this.a15).add(this.a16).add(this.a17).add(this.a18).add(this.a19).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration), v15 -> this.a15.setTo(v15, duration), v16 -> this.a16.setTo(v16, duration), v17 -> this.a17.setTo(v17, duration), v18 -> this.a18.setTo(v18, duration), v19 -> this.a19.setTo(v19, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget, this.a15::setTarget, this.a16::setTarget, this.a17::setTarget, this.a18::setTarget, this.a19::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target(), this.a15.target(), this.a16.target(), this.a17.target(), this.a18.target(), this.a19.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value(), this.a15.value(), this.a16.value(), this.a17.value(), this.a18.value(), this.a19.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14, A15 var15, A16 var16, A17 var17, A18 var18, A19 var19);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15, RecordValueAnimatorArgs.Setter<A15> var16, RecordValueAnimatorArgs.Setter<A16> var17, RecordValueAnimatorArgs.Setter<A17> var18, RecordValueAnimatorArgs.Setter<A18> var19, RecordValueAnimatorArgs.Setter<A19> var20);
        }
    }

    public static class Arg2<A1, A2, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final RecordValueAnimatorArgs.Arg2.Op<A1, A2, T> op;

        private final RecordValueAnimatorArgs.Arg2.Up<A1, A2, T> up;

        public Arg2(ValueAnimator<A1> a1, ValueAnimator<A2> a2, RecordValueAnimatorArgs.Arg2.Op<A1, A2, T> op, RecordValueAnimatorArgs.Arg2.Up<A1, A2, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, T> {

            T construct(A1 var1, A2 var2);
        }

        @FunctionalInterface
        public interface Up<A1, A2, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3);
        }
    }

    public static class Arg20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final ValueAnimator<A10> a10;

        private final ValueAnimator<A11> a11;

        private final ValueAnimator<A12> a12;

        private final ValueAnimator<A13> a13;

        private final ValueAnimator<A14> a14;

        private final ValueAnimator<A15> a15;

        private final ValueAnimator<A16> a16;

        private final ValueAnimator<A17> a17;

        private final ValueAnimator<A18> a18;

        private final ValueAnimator<A19> a19;

        private final ValueAnimator<A20> a20;

        private final RecordValueAnimatorArgs.Arg20.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> op;

        private final RecordValueAnimatorArgs.Arg20.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> up;

        public Arg20(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, ValueAnimator<A10> a10, ValueAnimator<A11> a11, ValueAnimator<A12> a12, ValueAnimator<A13> a13, ValueAnimator<A14> a14, ValueAnimator<A15> a15, ValueAnimator<A16> a16, ValueAnimator<A17> a17, ValueAnimator<A18> a18, ValueAnimator<A19> a19, ValueAnimator<A20> a20, RecordValueAnimatorArgs.Arg20.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> op, RecordValueAnimatorArgs.Arg20.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).add(this.a10).add(this.a11).add(this.a12).add(this.a13).add(this.a14).add(this.a15).add(this.a16).add(this.a17).add(this.a18).add(this.a19).add(this.a20).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration), v10 -> this.a10.setTo(v10, duration), v11 -> this.a11.setTo(v11, duration), v12 -> this.a12.setTo(v12, duration), v13 -> this.a13.setTo(v13, duration), v14 -> this.a14.setTo(v14, duration), v15 -> this.a15.setTo(v15, duration), v16 -> this.a16.setTo(v16, duration), v17 -> this.a17.setTo(v17, duration), v18 -> this.a18.setTo(v18, duration), v19 -> this.a19.setTo(v19, duration), v20 -> this.a20.setTo(v20, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget, this.a10::setTarget, this.a11::setTarget, this.a12::setTarget, this.a13::setTarget, this.a14::setTarget, this.a15::setTarget, this.a16::setTarget, this.a17::setTarget, this.a18::setTarget, this.a19::setTarget, this.a20::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target(), this.a10.target(), this.a11.target(), this.a12.target(), this.a13.target(), this.a14.target(), this.a15.target(), this.a16.target(), this.a17.target(), this.a18.target(), this.a19.target(), this.a20.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value(), this.a10.value(), this.a11.value(), this.a12.value(), this.a13.value(), this.a14.value(), this.a15.value(), this.a16.value(), this.a17.value(), this.a18.value(), this.a19.value(), this.a20.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9, A10 var10, A11 var11, A12 var12, A13 var13, A14 var14, A15 var15, A16 var16, A17 var17, A18 var18, A19 var19, A20 var20);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10, RecordValueAnimatorArgs.Setter<A10> var11, RecordValueAnimatorArgs.Setter<A11> var12, RecordValueAnimatorArgs.Setter<A12> var13, RecordValueAnimatorArgs.Setter<A13> var14, RecordValueAnimatorArgs.Setter<A14> var15, RecordValueAnimatorArgs.Setter<A15> var16, RecordValueAnimatorArgs.Setter<A16> var17, RecordValueAnimatorArgs.Setter<A17> var18, RecordValueAnimatorArgs.Setter<A18> var19, RecordValueAnimatorArgs.Setter<A19> var20, RecordValueAnimatorArgs.Setter<A20> var21);
        }
    }

    public static class Arg3<A1, A2, A3, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final RecordValueAnimatorArgs.Arg3.Op<A1, A2, A3, T> op;

        private final RecordValueAnimatorArgs.Arg3.Up<A1, A2, A3, T> up;

        public Arg3(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, RecordValueAnimatorArgs.Arg3.Op<A1, A2, A3, T> op, RecordValueAnimatorArgs.Arg3.Up<A1, A2, A3, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, T> {

            T construct(A1 var1, A2 var2, A3 var3);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4);
        }
    }

    public static class Arg4<A1, A2, A3, A4, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final RecordValueAnimatorArgs.Arg4.Op<A1, A2, A3, A4, T> op;

        private final RecordValueAnimatorArgs.Arg4.Up<A1, A2, A3, A4, T> up;

        public Arg4(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, RecordValueAnimatorArgs.Arg4.Op<A1, A2, A3, A4, T> op, RecordValueAnimatorArgs.Arg4.Up<A1, A2, A3, A4, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5);
        }
    }

    public static class Arg5<A1, A2, A3, A4, A5, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final RecordValueAnimatorArgs.Arg5.Op<A1, A2, A3, A4, A5, T> op;

        private final RecordValueAnimatorArgs.Arg5.Up<A1, A2, A3, A4, A5, T> up;

        public Arg5(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, RecordValueAnimatorArgs.Arg5.Op<A1, A2, A3, A4, A5, T> op, RecordValueAnimatorArgs.Arg5.Up<A1, A2, A3, A4, A5, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6);
        }
    }

    public static class Arg6<A1, A2, A3, A4, A5, A6, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final RecordValueAnimatorArgs.Arg6.Op<A1, A2, A3, A4, A5, A6, T> op;

        private final RecordValueAnimatorArgs.Arg6.Up<A1, A2, A3, A4, A5, A6, T> up;

        public Arg6(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, RecordValueAnimatorArgs.Arg6.Op<A1, A2, A3, A4, A5, A6, T> op, RecordValueAnimatorArgs.Arg6.Up<A1, A2, A3, A4, A5, A6, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7);
        }
    }

    public static class Arg7<A1, A2, A3, A4, A5, A6, A7, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final RecordValueAnimatorArgs.Arg7.Op<A1, A2, A3, A4, A5, A6, A7, T> op;

        private final RecordValueAnimatorArgs.Arg7.Up<A1, A2, A3, A4, A5, A6, A7, T> up;

        public Arg7(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, RecordValueAnimatorArgs.Arg7.Op<A1, A2, A3, A4, A5, A6, A7, T> op, RecordValueAnimatorArgs.Arg7.Up<A1, A2, A3, A4, A5, A6, A7, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8);
        }
    }

    public static class Arg8<A1, A2, A3, A4, A5, A6, A7, A8, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final RecordValueAnimatorArgs.Arg8.Op<A1, A2, A3, A4, A5, A6, A7, A8, T> op;

        private final RecordValueAnimatorArgs.Arg8.Up<A1, A2, A3, A4, A5, A6, A7, A8, T> up;

        public Arg8(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, RecordValueAnimatorArgs.Arg8.Op<A1, A2, A3, A4, A5, A6, A7, A8, T> op, RecordValueAnimatorArgs.Arg8.Up<A1, A2, A3, A4, A5, A6, A7, A8, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9);
        }
    }

    public static class Arg9<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> implements RecordValueAnimator.Arg<T> {

        private final ValueAnimator<A1> a1;

        private final ValueAnimator<A2> a2;

        private final ValueAnimator<A3> a3;

        private final ValueAnimator<A4> a4;

        private final ValueAnimator<A5> a5;

        private final ValueAnimator<A6> a6;

        private final ValueAnimator<A7> a7;

        private final ValueAnimator<A8> a8;

        private final ValueAnimator<A9> a9;

        private final RecordValueAnimatorArgs.Arg9.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> op;

        private final RecordValueAnimatorArgs.Arg9.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> up;

        public Arg9(ValueAnimator<A1> a1, ValueAnimator<A2> a2, ValueAnimator<A3> a3, ValueAnimator<A4> a4, ValueAnimator<A5> a5, ValueAnimator<A6> a6, ValueAnimator<A7> a7, ValueAnimator<A8> a8, ValueAnimator<A9> a9, RecordValueAnimatorArgs.Arg9.Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> op, RecordValueAnimatorArgs.Arg9.Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> up) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.op = op;
            this.up = up;
        }

        @Override
        public List<ValueAnimator<?>> dependencies() {
            return ImmutableList.builder().add(this.a1).add(this.a2).add(this.a3).add(this.a4).add(this.a5).add(this.a6).add(this.a7).add(this.a8).add(this.a9).build();
        }

        @Override
        public void set(T value, long duration) {
            this.up.update(value, v1 -> this.a1.setTo(v1, duration), v2 -> this.a2.setTo(v2, duration), v3 -> this.a3.setTo(v3, duration), v4 -> this.a4.setTo(v4, duration), v5 -> this.a5.setTo(v5, duration), v6 -> this.a6.setTo(v6, duration), v7 -> this.a7.setTo(v7, duration), v8 -> this.a8.setTo(v8, duration), v9 -> this.a9.setTo(v9, duration));
        }

        @Override
        public void setTarget(T value) {
            this.up.update(value, this.a1::setTarget, this.a2::setTarget, this.a3::setTarget, this.a4::setTarget, this.a5::setTarget, this.a6::setTarget, this.a7::setTarget, this.a8::setTarget, this.a9::setTarget);
        }

        @Override
        public T target() {
            return this.op.construct(this.a1.target(), this.a2.target(), this.a3.target(), this.a4.target(), this.a5.target(), this.a6.target(), this.a7.target(), this.a8.target(), this.a9.target());
        }

        @Override
        public T value() {
            return this.op.construct(this.a1.value(), this.a2.value(), this.a3.value(), this.a4.value(), this.a5.value(), this.a6.value(), this.a7.value(), this.a8.value(), this.a9.value());
        }

        @FunctionalInterface
        public interface Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> {

            T construct(A1 var1, A2 var2, A3 var3, A4 var4, A5 var5, A6 var6, A7 var7, A8 var8, A9 var9);
        }

        @FunctionalInterface
        public interface Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> {

            void update(T var1, RecordValueAnimatorArgs.Setter<A1> var2, RecordValueAnimatorArgs.Setter<A2> var3, RecordValueAnimatorArgs.Setter<A3> var4, RecordValueAnimatorArgs.Setter<A4> var5, RecordValueAnimatorArgs.Setter<A5> var6, RecordValueAnimatorArgs.Setter<A6> var7, RecordValueAnimatorArgs.Setter<A7> var8, RecordValueAnimatorArgs.Setter<A8> var9, RecordValueAnimatorArgs.Setter<A9> var10);
        }
    }

    @FunctionalInterface
    public interface Setter<T> {

        void set(T var1);
    }
}