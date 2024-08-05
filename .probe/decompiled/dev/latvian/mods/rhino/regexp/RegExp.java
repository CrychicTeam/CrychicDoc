package dev.latvian.mods.rhino.regexp;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.Kit;
import dev.latvian.mods.rhino.ScriptRuntime;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;
import dev.latvian.mods.rhino.Undefined;

public class RegExp {

    public static final int RA_MATCH = 1;

    public static final int RA_REPLACE = 2;

    public static final int RA_SEARCH = 3;

    protected String input;

    protected boolean multiline;

    protected SubString[] parens;

    protected SubString lastMatch;

    protected SubString lastParen;

    protected SubString leftContext;

    protected SubString rightContext;

    private static NativeRegExp createRegExp(Context cx, Scriptable scope, Object[] args, int optarg, boolean forceFlat) {
        Scriptable topScope = ScriptableObject.getTopLevelScope(scope);
        if (args.length == 0 || args[0] == Undefined.instance) {
            RECompiled compiled = NativeRegExp.compileRE(cx, "", "", false);
            re = new NativeRegExp(topScope, compiled, cx);
        } else if (!(args[0] instanceof NativeRegExp re)) {
            String src = ScriptRuntime.toString(cx, args[0]);
            String opt;
            if (optarg < args.length) {
                args[0] = src;
                opt = ScriptRuntime.toString(cx, args[optarg]);
            } else {
                opt = null;
            }
            RECompiled compiled = NativeRegExp.compileRE(cx, src, opt, forceFlat);
            re = new NativeRegExp(topScope, compiled, cx);
        }
        return re;
    }

    private static Object matchOrReplace(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, RegExp reImpl, GlobData data, NativeRegExp re) {
        String str = data.str;
        data.global = (re.getFlags() & 1) != 0;
        int[] indexp = new int[] { 0 };
        Object result = null;
        if (data.mode == 3) {
            result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
            if (result != null && result.equals(Boolean.TRUE)) {
                result = reImpl.leftContext.length;
            } else {
                result = -1;
            }
        } else if (data.global) {
            re.lastIndex = ScriptRuntime.zeroObj;
            for (int count = 0; indexp[0] <= str.length(); count++) {
                result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
                if (result == null || !result.equals(Boolean.TRUE)) {
                    break;
                }
                if (data.mode == 1) {
                    match_glob(data, cx, scope, count, reImpl);
                } else {
                    if (data.mode != 2) {
                        Kit.codeBug();
                    }
                    SubString lastMatch = reImpl.lastMatch;
                    int leftIndex = data.leftIndex;
                    int leftlen = lastMatch.index - leftIndex;
                    data.leftIndex = lastMatch.index + lastMatch.length;
                    replace_glob(data, cx, scope, reImpl, leftIndex, leftlen);
                }
                if (reImpl.lastMatch.length == 0) {
                    if (indexp[0] == str.length()) {
                        break;
                    }
                    indexp[0]++;
                }
            }
        } else {
            result = re.executeRegExp(cx, scope, reImpl, str, indexp, data.mode == 2 ? 0 : 1);
        }
        return result;
    }

    private static void match_glob(GlobData mdata, Context cx, Scriptable scope, int count, RegExp reImpl) {
        if (mdata.arrayobj == null) {
            mdata.arrayobj = cx.newArray(scope, 0);
        }
        SubString matchsub = reImpl.lastMatch;
        String matchstr = matchsub.toString();
        mdata.arrayobj.put(cx, count, mdata.arrayobj, matchstr);
    }

    private static void replace_glob(GlobData rdata, Context cx, Scriptable scope, RegExp reImpl, int leftIndex, int leftlen) {
        int replen;
        String lambdaStr;
        if (rdata.lambda != null) {
            SubString[] parens = reImpl.parens;
            int parenCount = parens == null ? 0 : parens.length;
            Object[] args = new Object[parenCount + 3];
            args[0] = reImpl.lastMatch.toString();
            for (int i = 0; i < parenCount; i++) {
                SubString sub = parens[i];
                if (sub != null) {
                    args[i + 1] = sub.toString();
                } else {
                    args[i + 1] = Undefined.instance;
                }
            }
            args[parenCount + 1] = reImpl.leftContext.length;
            args[parenCount + 2] = rdata.str;
            if (reImpl != cx.getRegExp()) {
                Kit.codeBug();
            }
            RegExp re2 = new RegExp();
            re2.multiline = reImpl.multiline;
            re2.input = reImpl.input;
            ScriptRuntime.setRegExpProxy(cx, re2);
            try {
                Scriptable parent = ScriptableObject.getTopLevelScope(scope);
                Object result = rdata.lambda.call(cx, parent, parent, args);
                lambdaStr = ScriptRuntime.toString(cx, result);
            } finally {
                ScriptRuntime.setRegExpProxy(cx, reImpl);
            }
            replen = lambdaStr.length();
        } else {
            lambdaStr = null;
            replen = rdata.repstr.length();
            if (rdata.dollar >= 0) {
                int[] skip = new int[1];
                int dp = rdata.dollar;
                do {
                    SubString sub = interpretDollar(cx, reImpl, rdata.repstr, dp, skip);
                    if (sub != null) {
                        replen += sub.length - skip[0];
                        dp += skip[0];
                    } else {
                        dp++;
                    }
                    dp = rdata.repstr.indexOf(36, dp);
                } while (dp >= 0);
            }
        }
        int growth = leftlen + replen + reImpl.rightContext.length;
        StringBuilder charBuf = rdata.charBuf;
        if (charBuf == null) {
            charBuf = new StringBuilder(growth);
            rdata.charBuf = charBuf;
        } else {
            charBuf.ensureCapacity(rdata.charBuf.length() + growth);
        }
        charBuf.append(reImpl.leftContext.str, leftIndex, leftIndex + leftlen);
        if (rdata.lambda != null) {
            charBuf.append(lambdaStr);
        } else {
            do_replace(rdata, cx, reImpl);
        }
    }

    private static SubString interpretDollar(Context cx, RegExp res, String da, int dp, int[] skip) {
        if (da.charAt(dp) != '$') {
            Kit.codeBug();
        }
        int daL = da.length();
        if (dp + 1 >= daL) {
            return null;
        } else {
            char dc = da.charAt(dp + 1);
            if (NativeRegExp.isDigit(dc)) {
                int parenCount = res.parens == null ? 0 : res.parens.length;
                int num = dc - '0';
                if (num > parenCount) {
                    return null;
                } else {
                    int cp = dp + 2;
                    if (dp + 2 < daL) {
                        dc = da.charAt(dp + 2);
                        if (NativeRegExp.isDigit(dc)) {
                            int tmp = 10 * num + (dc - '0');
                            if (tmp <= parenCount) {
                                cp++;
                                num = tmp;
                            }
                        }
                    }
                    if (num == 0) {
                        return null;
                    } else {
                        num--;
                        skip[0] = cp - dp;
                        return res.getParenSubString(num);
                    }
                }
            } else {
                skip[0] = 2;
                return switch(dc) {
                    case '$' ->
                        new SubString("$");
                    case '&' ->
                        res.lastMatch;
                    case '\'' ->
                        res.rightContext;
                    case '+' ->
                        res.lastParen;
                    case '`' ->
                        res.leftContext;
                    default ->
                        null;
                };
            }
        }
    }

    private static void do_replace(GlobData rdata, Context cx, RegExp regExp) {
        StringBuilder charBuf = rdata.charBuf;
        int cp = 0;
        String da = rdata.repstr;
        int dp = rdata.dollar;
        if (dp != -1) {
            int[] skip = new int[1];
            do {
                int len = dp - cp;
                charBuf.append(da, cp, dp);
                cp = dp;
                SubString sub = interpretDollar(cx, regExp, da, dp, skip);
                if (sub != null) {
                    len = sub.length;
                    if (len > 0) {
                        charBuf.append(sub.str, sub.index, sub.index + len);
                    }
                    cp = dp + skip[0];
                    dp += skip[0];
                } else {
                    dp++;
                }
                dp = da.indexOf(36, dp);
            } while (dp >= 0);
        }
        int daL = da.length();
        if (daL > cp) {
            charBuf.append(da, cp, daL);
        }
    }

    private static int find_split(Context cx, Scriptable scope, String target, String separator, RegExp reProxy, Scriptable re, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp) {
        int i = ip[0];
        int length = target.length();
        if (i > length) {
            return -1;
        } else if (re != null) {
            return reProxy.find_split(cx, scope, target, separator, re, ip, matchlen, matched, parensp);
        } else if (separator.length() == 0) {
            return i == length ? -1 : i + 1;
        } else if (ip[0] >= length) {
            return length;
        } else {
            i = target.indexOf(separator, ip[0]);
            return i != -1 ? i : length;
        }
    }

    public boolean isRegExp(Scriptable obj) {
        return obj instanceof NativeRegExp;
    }

    public Object compileRegExp(Context cx, String source, String flags) {
        return NativeRegExp.compileRE(cx, source, flags, false);
    }

    public Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
        return new NativeRegExp(scope, (RECompiled) compiled, cx);
    }

    public Object action(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType) {
        GlobData data = new GlobData();
        data.mode = actionType;
        data.str = ScriptRuntime.toString(cx, thisObj);
        switch(actionType) {
            case 1:
                {
                    int optarg = Integer.MAX_VALUE;
                    NativeRegExp re = createRegExp(cx, scope, args, optarg, false);
                    Object rval = matchOrReplace(cx, scope, thisObj, args, this, data, re);
                    return data.arrayobj == null ? rval : data.arrayobj;
                }
            case 2:
                {
                    boolean useRE = args.length > 0 && args[0] instanceof NativeRegExp;
                    NativeRegExp re = null;
                    String search = null;
                    if (useRE) {
                        re = createRegExp(cx, scope, args, 2, true);
                    } else {
                        Object arg0 = args.length < 1 ? Undefined.instance : args[0];
                        search = ScriptRuntime.toString(cx, arg0);
                    }
                    Object arg1 = args.length < 2 ? Undefined.instance : args[1];
                    String repstr = null;
                    Function lambda = null;
                    if (arg1 instanceof Function && !(arg1 instanceof NativeRegExp)) {
                        lambda = (Function) arg1;
                    } else {
                        repstr = ScriptRuntime.toString(cx, arg1);
                    }
                    data.lambda = lambda;
                    data.repstr = repstr;
                    data.dollar = repstr == null ? -1 : repstr.indexOf(36);
                    data.charBuf = null;
                    data.leftIndex = 0;
                    Object val;
                    if (useRE) {
                        val = matchOrReplace(cx, scope, thisObj, args, this, data, re);
                    } else {
                        String str = data.str;
                        int index = str.indexOf(search);
                        if (index >= 0) {
                            int slen = search.length();
                            this.parens = null;
                            this.lastParen = null;
                            this.leftContext = new SubString(str, 0, index);
                            this.lastMatch = new SubString(str, index, slen);
                            this.rightContext = new SubString(str, index + slen, str.length() - index - slen);
                            val = Boolean.TRUE;
                        } else {
                            val = Boolean.FALSE;
                        }
                    }
                    if (data.charBuf == null) {
                        if (data.global || val == null || !val.equals(Boolean.TRUE)) {
                            return data.str;
                        }
                        SubString lc = this.leftContext;
                        replace_glob(data, cx, scope, this, lc.index, lc.length);
                    }
                    SubString rc = this.rightContext;
                    data.charBuf.append(rc.str, rc.index, rc.index + rc.length);
                    return data.charBuf.toString();
                }
            case 3:
                {
                    int optarg = Integer.MAX_VALUE;
                    NativeRegExp re = createRegExp(cx, scope, args, optarg, false);
                    return matchOrReplace(cx, scope, thisObj, args, this, data, re);
                }
            default:
                throw Kit.codeBug();
        }
    }

    public int find_split(Context cx, Scriptable scope, String target, String separator, Scriptable reObj, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp) {
        int i = ip[0];
        int length = target.length();
        NativeRegExp re = (NativeRegExp) reObj;
        int result;
        while (true) {
            int ipsave = ip[0];
            ip[0] = i;
            Object ret = re.executeRegExp(cx, scope, this, target, ip, 0);
            if (!Boolean.TRUE.equals(ret)) {
                ip[0] = ipsave;
                matchlen[0] = 1;
                matched[0] = false;
                return length;
            }
            i = ip[0];
            ip[0] = ipsave;
            matched[0] = true;
            SubString sep = this.lastMatch;
            matchlen[0] = sep.length;
            if (matchlen[0] != 0 || i != ip[0]) {
                result = i - matchlen[0];
                break;
            }
            if (i == length) {
                result = -1;
                break;
            }
            i++;
        }
        int size = this.parens == null ? 0 : this.parens.length;
        parensp[0] = new String[size];
        for (int num = 0; num < size; num++) {
            SubString parsub = this.getParenSubString(num);
            parensp[0][num] = parsub.toString();
        }
        return result;
    }

    SubString getParenSubString(int i) {
        if (this.parens != null && i < this.parens.length) {
            SubString parsub = this.parens[i];
            if (parsub != null) {
                return parsub;
            }
        }
        return new SubString();
    }

    public Object js_split(Context cx, Scriptable scope, String target, Object[] args) {
        Scriptable result = cx.newArray(scope, 0);
        boolean limited = args.length > 1 && args[1] != Undefined.instance;
        long limit = 0L;
        if (limited) {
            limit = ScriptRuntime.toUint32(cx, args[1]);
            if (limit == 0L) {
                return result;
            }
            if (limit > (long) target.length()) {
                limit = (long) (1 + target.length());
            }
        }
        if (args.length >= 1 && args[0] != Undefined.instance) {
            String separator = null;
            int[] matchlen = new int[1];
            Scriptable re = null;
            RegExp reProxy = null;
            if (args[0] instanceof Scriptable) {
                reProxy = cx.getRegExp();
                if (reProxy != null) {
                    Scriptable test = (Scriptable) args[0];
                    if (reProxy.isRegExp(test)) {
                        re = test;
                    }
                }
            }
            if (re == null) {
                separator = ScriptRuntime.toString(cx, args[0]);
                matchlen[0] = separator.length();
            }
            int[] ip = new int[] { 0 };
            int len = 0;
            boolean[] matched = new boolean[] { false };
            String[][] parens = new String[][] { null };
            int match;
            while ((match = find_split(cx, scope, target, separator, reProxy, re, ip, matchlen, matched, parens)) >= 0 && (!limited || (long) len < limit) && match <= target.length()) {
                String substr;
                if (target.length() == 0) {
                    substr = target;
                } else {
                    substr = target.substring(ip[0], match);
                }
                result.put(cx, len, result, substr);
                len++;
                if (re != null && matched[0]) {
                    int size = parens[0].length;
                    for (int num = 0; num < size && (!limited || (long) len < limit); num++) {
                        result.put(cx, len, result, parens[0][num]);
                        len++;
                    }
                    matched[0] = false;
                }
                ip[0] = match + matchlen[0];
            }
            return result;
        } else {
            result.put(cx, 0, result, target);
            return result;
        }
    }
}