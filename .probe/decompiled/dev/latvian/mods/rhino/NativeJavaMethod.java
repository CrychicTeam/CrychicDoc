package dev.latvian.mods.rhino;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

public class NativeJavaMethod extends BaseFunction {

    private static final int PREFERENCE_EQUAL = 0;

    private static final int PREFERENCE_FIRST_ARG = 1;

    private static final int PREFERENCE_SECOND_ARG = 2;

    private static final int PREFERENCE_AMBIGUOUS = 3;

    private static final boolean debug = false;

    private final String functionName;

    private final transient CopyOnWriteArrayList<ResolvedOverload> overloadCache = new CopyOnWriteArrayList();

    public transient MemberBox[] methods;

    static String scriptSignature(Object[] values) {
        StringBuilder sig = new StringBuilder();
        for (int i = 0; i != values.length; i++) {
            Object value = values[i];
            String s;
            if (value == null) {
                s = "null";
            } else if (value instanceof Boolean) {
                s = "boolean";
            } else if (value instanceof String) {
                s = "string";
            } else if (value instanceof Number) {
                s = "number";
            } else if (value instanceof Scriptable) {
                if (value instanceof Undefined) {
                    s = "undefined";
                } else if (value instanceof Wrapper) {
                    Object wrapped = ((Wrapper) value).unwrap();
                    s = wrapped.getClass().getName();
                } else if (value instanceof Function) {
                    s = "function";
                } else {
                    s = "object";
                }
            } else {
                s = JavaMembers.javaSignature(value.getClass());
            }
            if (i != 0) {
                sig.append(',');
            }
            sig.append(s);
        }
        return sig.toString();
    }

    static int findFunction(Context cx, MemberBox[] methodsOrCtors, Object[] args) {
        if (methodsOrCtors.length == 0) {
            return -1;
        } else if (methodsOrCtors.length == 1) {
            MemberBox member = methodsOrCtors[0];
            int alength = member.argTypes.length;
            if (member.vararg) {
                if (--alength > args.length) {
                    return -1;
                }
            } else if (alength != args.length) {
                return -1;
            }
            for (int j = 0; j != alength; j++) {
                if (!NativeJavaObject.canConvert(cx, args[j], member.argTypes[j])) {
                    return -1;
                }
            }
            return 0;
        } else {
            int firstBestFit = -1;
            int[] extraBestFits = null;
            int extraBestFitsCount = 0;
            label139: for (int i = 0; i < methodsOrCtors.length; i++) {
                MemberBox member = methodsOrCtors[i];
                int alength = member.argTypes.length;
                if (member.vararg ? --alength <= args.length : alength == args.length) {
                    for (int jx = 0; jx < alength; jx++) {
                        if (!NativeJavaObject.canConvert(cx, args[jx], member.argTypes[jx])) {
                            continue label139;
                        }
                    }
                    if (firstBestFit < 0) {
                        firstBestFit = i;
                    } else {
                        int betterCount = 0;
                        int worseCount = 0;
                        for (int jxx = -1; jxx != extraBestFitsCount; jxx++) {
                            int bestFitIndex;
                            if (jxx == -1) {
                                bestFitIndex = firstBestFit;
                            } else {
                                bestFitIndex = extraBestFits[jxx];
                            }
                            MemberBox bestFit = methodsOrCtors[bestFitIndex];
                            int preference = preferSignature(cx, args, member.argTypes, member.vararg, bestFit.argTypes, bestFit.vararg);
                            if (preference == 3) {
                                break;
                            }
                            if (preference == 1) {
                                betterCount++;
                            } else {
                                if (preference != 2) {
                                    if (preference != 0) {
                                        Kit.codeBug();
                                    }
                                    if (bestFit.isStatic() && bestFit.getDeclaringClass().isAssignableFrom(member.getDeclaringClass())) {
                                        if (jxx == -1) {
                                            firstBestFit = i;
                                        } else {
                                            extraBestFits[jxx] = i;
                                        }
                                    }
                                    continue label139;
                                }
                                worseCount++;
                            }
                        }
                        if (betterCount == 1 + extraBestFitsCount) {
                            firstBestFit = i;
                            extraBestFitsCount = 0;
                        } else if (worseCount != 1 + extraBestFitsCount) {
                            if (extraBestFits == null) {
                                extraBestFits = new int[methodsOrCtors.length - 1];
                            }
                            extraBestFits[extraBestFitsCount] = i;
                            extraBestFitsCount++;
                        }
                    }
                }
            }
            if (firstBestFit < 0) {
                return -1;
            } else if (extraBestFitsCount == 0) {
                return firstBestFit;
            } else {
                StringBuilder buf = new StringBuilder();
                for (int jxx = -1; jxx != extraBestFitsCount; jxx++) {
                    int bestFitIndexx;
                    if (jxx == -1) {
                        bestFitIndexx = firstBestFit;
                    } else {
                        bestFitIndexx = extraBestFits[jxx];
                    }
                    buf.append("\n    ");
                    buf.append(methodsOrCtors[bestFitIndexx].toJavaDeclaration());
                }
                MemberBox firstFitMember = methodsOrCtors[firstBestFit];
                String memberName = firstFitMember.getName();
                String memberClass = firstFitMember.getDeclaringClass().getName();
                if (methodsOrCtors[0].isCtor()) {
                    throw Context.reportRuntimeError3("msg.constructor.ambiguous", memberName, scriptSignature(args), buf.toString(), cx);
                } else {
                    throw Context.reportRuntimeError4("msg.method.ambiguous", memberClass, memberName, scriptSignature(args), buf.toString(), cx);
                }
            }
        }
    }

    private static int preferSignature(Context cx, Object[] args, Class<?>[] sig1, boolean vararg1, Class<?>[] sig2, boolean vararg2) {
        int totalPreference = 0;
        for (int j = 0; j < args.length; j++) {
            Class<?> type1 = vararg1 && j >= sig1.length ? sig1[sig1.length - 1] : sig1[j];
            Class<?> type2 = vararg2 && j >= sig2.length ? sig2[sig2.length - 1] : sig2[j];
            if (type1 != type2) {
                Object arg = args[j];
                int rank1 = NativeJavaObject.getConversionWeight(cx, arg, type1);
                int rank2 = NativeJavaObject.getConversionWeight(cx, arg, type2);
                int preference;
                if (rank1 < rank2) {
                    preference = 1;
                } else if (rank1 > rank2) {
                    preference = 2;
                } else if (rank1 == 0) {
                    if (type1.isAssignableFrom(type2)) {
                        preference = 2;
                    } else if (type2.isAssignableFrom(type1)) {
                        preference = 1;
                    } else {
                        preference = 3;
                    }
                } else {
                    preference = 3;
                }
                totalPreference |= preference;
                if (totalPreference == 3) {
                    break;
                }
            }
        }
        return totalPreference;
    }

    private static void printDebug(String msg, MemberBox member, Object[] args) {
    }

    NativeJavaMethod(MemberBox[] methods) {
        this.functionName = methods[0].getName();
        this.methods = methods;
    }

    NativeJavaMethod(MemberBox[] methods, String name) {
        this.functionName = name;
        this.methods = methods;
    }

    NativeJavaMethod(MemberBox method, String name) {
        this.functionName = name;
        this.methods = new MemberBox[] { method };
    }

    public NativeJavaMethod(Method method, String name) {
        this(new MemberBox(method), name);
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (int N = this.methods.length; i != N; i++) {
            if (i > 0) {
                sb.append('\n');
            }
            if (this.methods[i].isMethod()) {
                sb.append(JavaMembers.javaSignature(this.methods[i].getReturnType()));
                sb.append(' ');
                sb.append(this.methods[i].getName());
            } else {
                sb.append(this.methods[i].getName());
            }
            sb.append(JavaMembers.liveConnectSignature(this.methods[i].argTypes));
        }
        return sb.toString();
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (this.methods.length == 0) {
            throw new RuntimeException("No methods defined for call");
        } else {
            int index = this.findCachedFunction(cx, args);
            if (index < 0) {
                Class<?> c = this.methods[0].getDeclaringClass();
                String sig = c.getName() + "." + this.getFunctionName() + "(" + scriptSignature(args) + ")";
                throw Context.reportRuntimeError1("msg.java.no_such_method", sig, cx);
            } else {
                MemberBox meth = this.methods[index];
                Class<?>[] argTypes = meth.argTypes;
                if (meth.vararg) {
                    Object[] newArgs = new Object[argTypes.length];
                    for (int i = 0; i < argTypes.length - 1; i++) {
                        newArgs[i] = Context.jsToJava(cx, args[i], argTypes[i]);
                    }
                    Object varArgs;
                    if (args.length != argTypes.length || args[args.length - 1] != null && !(args[args.length - 1] instanceof NativeArray) && !(args[args.length - 1] instanceof NativeJavaArray)) {
                        Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
                        varArgs = Array.newInstance(componentType, args.length - argTypes.length + 1);
                        for (int i = 0; i < Array.getLength(varArgs); i++) {
                            Object value = Context.jsToJava(cx, args[argTypes.length - 1 + i], componentType);
                            Array.set(varArgs, i, value);
                        }
                    } else {
                        varArgs = Context.jsToJava(cx, args[args.length - 1], argTypes[argTypes.length - 1]);
                    }
                    newArgs[argTypes.length - 1] = varArgs;
                    args = newArgs;
                } else {
                    Object[] origArgs = args;
                    for (int i = 0; i < args.length; i++) {
                        Object arg = args[i];
                        Object var24 = Context.jsToJava(cx, arg, argTypes[i]);
                        if (var24 != arg) {
                            if (origArgs == args) {
                                args = (Object[]) args.clone();
                            }
                            args[i] = var24;
                        }
                    }
                }
                Object javaObject;
                if (meth.isStatic()) {
                    javaObject = null;
                } else {
                    Scriptable o = thisObj;
                    Class<?> c = meth.getDeclaringClass();
                    while (true) {
                        if (o == null) {
                            throw Context.reportRuntimeError3("msg.nonjava.method", this.getFunctionName(), ScriptRuntime.toString(cx, thisObj), c.getName(), cx);
                        }
                        if (o instanceof Wrapper) {
                            javaObject = ((Wrapper) o).unwrap();
                            if (c.isInstance(javaObject)) {
                                break;
                            }
                        }
                        o = o.getPrototype(cx);
                    }
                }
                Object retval = meth.invoke(javaObject, args, cx, scope);
                Class<?> staticType = meth.getReturnType();
                Object wrapped = cx.getWrapFactory().wrap(cx, scope, retval, staticType);
                if (wrapped == null && staticType == void.class) {
                    wrapped = Undefined.instance;
                }
                return wrapped;
            }
        }
    }

    int findCachedFunction(Context cx, Object[] args) {
        if (this.methods.length > 1) {
            for (ResolvedOverload ovl : this.overloadCache) {
                if (ovl.matches(args)) {
                    return ovl.index;
                }
            }
            int index = findFunction(cx, this.methods, args);
            if (this.overloadCache.size() < this.methods.length * 2) {
                ResolvedOverload ovlx = new ResolvedOverload(args, index);
                this.overloadCache.addIfAbsent(ovlx);
            }
            return index;
        } else {
            return findFunction(cx, this.methods, args);
        }
    }
}