package ExpressionTypes;

import Types.Type;

public class Expression {

    private Type type;

    public Expression(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

}
