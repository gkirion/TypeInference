package ExpressionTypes;

import ExpressionTypes.Expression;
import Types.Type;

public class Variable extends Expression {

    private String x;

    public Variable(String x, Type type) {
        super(type);
        this.x = x;
    }

    public String getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "x='" + x + '\'' + ", type='" + super.getType() + '\'' +
                "} ";
    }
}
