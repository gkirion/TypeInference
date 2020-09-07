package Types;

public class Constraint {

    private Type t1, t2;

    public Constraint(Type t1, Type t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Type getT1() {
        return t1;
    }

    public Type getT2() {
        return t2;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "t1=" + t1 +
                ", t2=" + t2 +
                '}';
    }

}
