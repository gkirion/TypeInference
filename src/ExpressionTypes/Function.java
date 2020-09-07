package ExpressionTypes;

import Types.Type;

public class Function extends Expression {

    private String x;
    private Expression e;

    public Function(String x, Expression e, Type type) {
        super(type);
        this.x = x;
        this.e = e;
    }

    public String getX() {
        return x;
    }

    public Expression getE() {
        return e;
    }

    @Override
    public String toString() {
        return "Function{" +
                "x='" + x + '\'' +
                ", e=" + e + ", type='" + super.getType() + '\'' +
                "} ";
    }
}
