package dev.latvian.mods.rhino.regexp;

import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ScriptRuntime;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.TopLevel;
import dev.latvian.mods.rhino.Undefined;

class NativeRegExpCtor extends BaseFunction {

    private static final int Id_multiline = 1;

    private static final int Id_STAR = 2;

    private static final int Id_input = 3;

    private static final int Id_UNDERSCORE = 4;

    private static final int Id_lastMatch = 5;

    private static final int Id_AMPERSAND = 6;

    private static final int Id_lastParen = 7;

    private static final int Id_PLUS = 8;

    private static final int Id_leftContext = 9;

    private static final int Id_BACK_QUOTE = 10;

    private static final int Id_rightContext = 11;

    private static final int Id_QUOTE = 12;

    private static final int DOLLAR_ID_BASE = 12;

    private static final int Id_DOLLAR_1 = 13;

    private static final int Id_DOLLAR_2 = 14;

    private static final int Id_DOLLAR_3 = 15;

    private static final int Id_DOLLAR_4 = 16;

    private static final int Id_DOLLAR_5 = 17;

    private static final int Id_DOLLAR_6 = 18;

    private static final int Id_DOLLAR_7 = 19;

    private static final int Id_DOLLAR_8 = 20;

    private static final int Id_DOLLAR_9 = 21;

    private static final int MAX_INSTANCE_ID = 21;

    private int multilineAttr = 4;

    private int starAttr = 4;

    private int inputAttr = 4;

    private int underscoreAttr = 4;

    @Override
    public String getFunctionName() {
        return "RegExp";
    }

    @Override
    public int getLength() {
        return 2;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return args.length <= 0 || !(args[0] instanceof NativeRegExp) || args.length != 1 && args[1] != Undefined.instance ? this.construct(cx, scope, args) : args[0];
    }

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        NativeRegExp re = new NativeRegExp();
        re.compile(cx, scope, args);
        ScriptRuntime.setBuiltinProtoAndParent(cx, scope, re, TopLevel.Builtins.RegExp);
        return re;
    }

    @Override
    protected int getMaxInstanceId() {
        return super.getMaxInstanceId() + 21;
    }

    @Override
    protected int findInstanceIdInfo(String s, Context cx) {
        int id = switch(s) {
            case "multiline" ->
                1;
            case "$*" ->
                2;
            case "input" ->
                3;
            case "$_" ->
                4;
            case "lastMatch" ->
                5;
            case "$&" ->
                6;
            case "lastParen" ->
                7;
            case "$+" ->
                8;
            case "leftContext" ->
                9;
            case "$`" ->
                10;
            case "rightContext" ->
                11;
            case "$'" ->
                12;
            case "$1" ->
                13;
            case "$2" ->
                14;
            case "$3" ->
                15;
            case "$4" ->
                16;
            case "$5" ->
                17;
            case "$6" ->
                18;
            case "$7" ->
                19;
            case "$8" ->
                20;
            case "$9" ->
                21;
            default ->
                0;
        };
        if (id == 0) {
            return super.findInstanceIdInfo(s, cx);
        } else {
            int attr = switch(id) {
                case 1 ->
                    this.multilineAttr;
                case 2 ->
                    this.starAttr;
                case 3 ->
                    this.inputAttr;
                case 4 ->
                    this.underscoreAttr;
                default ->
                    5;
            };
            return instanceIdInfo(attr, super.getMaxInstanceId() + id);
        }
    }

    @Override
    protected String getInstanceIdName(int id) {
        int shifted = id - super.getMaxInstanceId();
        if (1 <= shifted && shifted <= 21) {
            return switch(shifted) {
                case 1 ->
                    "multiline";
                case 2 ->
                    "$*";
                case 3 ->
                    "input";
                case 4 ->
                    "$_";
                case 5 ->
                    "lastMatch";
                case 6 ->
                    "$&";
                case 7 ->
                    "lastParen";
                case 8 ->
                    "$+";
                case 9 ->
                    "leftContext";
                case 10 ->
                    "$`";
                case 11 ->
                    "rightContext";
                case 12 ->
                    "$'";
                case 13 ->
                    "$1";
                case 14 ->
                    "$2";
                case 15 ->
                    "$3";
                case 16 ->
                    "$4";
                case 17 ->
                    "$5";
                case 18 ->
                    "$6";
                case 19 ->
                    "$7";
                case 20 ->
                    "$8";
                case 21 ->
                    "$9";
                default ->
                    super.getInstanceIdName(id);
            };
        } else {
            return super.getInstanceIdName(id);
        }
    }

    @Override
    protected Object getInstanceIdValue(int id, Context cx) {
        int shifted = id - super.getMaxInstanceId();
        if (1 <= shifted && shifted <= 21) {
            RegExp impl = cx.getRegExp();
            Object stringResult;
            switch(shifted) {
                case 1:
                case 2:
                    return impl.multiline;
                case 3:
                case 4:
                    stringResult = impl.input;
                    break;
                case 5:
                case 6:
                    stringResult = impl.lastMatch;
                    break;
                case 7:
                case 8:
                    stringResult = impl.lastParen;
                    break;
                case 9:
                case 10:
                    stringResult = impl.leftContext;
                    break;
                case 11:
                case 12:
                    stringResult = impl.rightContext;
                    break;
                default:
                    int substring_number = shifted - 12 - 1;
                    stringResult = impl.getParenSubString(substring_number);
            }
            return stringResult == null ? "" : stringResult.toString();
        } else {
            return super.getInstanceIdValue(id, cx);
        }
    }

    @Override
    protected void setInstanceIdValue(int id, Object value, Context cx) {
        int shifted = id - super.getMaxInstanceId();
        switch(shifted) {
            case 1:
            case 2:
                cx.getRegExp().multiline = ScriptRuntime.toBoolean(cx, value);
                return;
            case 3:
            case 4:
                cx.getRegExp().input = ScriptRuntime.toString(cx, value);
                return;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return;
            default:
                int substring_number = shifted - 12 - 1;
                if (0 > substring_number || substring_number > 8) {
                    super.setInstanceIdValue(id, value, cx);
                }
        }
    }

    @Override
    protected void setInstanceIdAttributes(int id, int attr, Context cx) {
        int shifted = id - super.getMaxInstanceId();
        switch(shifted) {
            case 1:
                this.multilineAttr = attr;
                return;
            case 2:
                this.starAttr = attr;
                return;
            case 3:
                this.inputAttr = attr;
                return;
            case 4:
                this.underscoreAttr = attr;
                return;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return;
            default:
                int substring_number = shifted - 12 - 1;
                if (0 > substring_number || substring_number > 8) {
                    super.setInstanceIdAttributes(id, attr, cx);
                }
        }
    }
}