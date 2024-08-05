package dev.latvian.mods.rhino;

public class IdFunctionObjectES6 extends IdFunctionObject {

    private static final int Id_length = 1;

    private static final int Id_name = 3;

    private boolean myLength = true;

    private boolean myName = true;

    public IdFunctionObjectES6(IdFunctionCall idcall, Object tag, int id, String name, int arity, Scriptable scope) {
        super(idcall, tag, id, name, arity, scope);
    }

    @Override
    protected int findInstanceIdInfo(String s, Context cx) {
        if (s.equals("length")) {
            return instanceIdInfo(3, 1);
        } else {
            return s.equals("name") ? instanceIdInfo(3, 3) : super.findInstanceIdInfo(s, cx);
        }
    }

    @Override
    protected Object getInstanceIdValue(int id, Context cx) {
        if (id == 1 && !this.myLength) {
            return NOT_FOUND;
        } else {
            return id == 3 && !this.myName ? NOT_FOUND : super.getInstanceIdValue(id, cx);
        }
    }

    @Override
    protected void setInstanceIdValue(int id, Object value, Context cx) {
        if (id == 1 && value == NOT_FOUND) {
            this.myLength = false;
        } else if (id == 3 && value == NOT_FOUND) {
            this.myName = false;
        } else {
            super.setInstanceIdValue(id, value, cx);
        }
    }
}