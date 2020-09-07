package Types;

public class TypeArrow extends Type {

    private Type u, v;

    public TypeArrow(Type u, Type v) {
        this.u = u;
        this.v = v;
    }

    public Type getU() {
        return u;
    }

    public Type getV() {
        return v;
    }

    @Override
    public String toString() {
        String s = u instanceof TypeArrow ? "(" + u + ")" : u.toString();
        return s + " -> " + v;
    }

}
