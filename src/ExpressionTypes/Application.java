package ExpressionTypes;

import Types.Type;

public class Application extends Expression {

    private Expression e1, e2;

    public Application(Expression e1, Expression e2, Type type) {
        super(type);
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getE1() {
        return e1;
    }

    public Expression getE2() {
        return e2;
    }

    @Override
    public String toString() {
        return "Application{" +
                "e1=" + e1 +
                ", e2=" + e2 + ", type='" + super.getType() + '\'' +
                "} ";
    }
}
