import ExpressionTypes.Application;
import ExpressionTypes.Expression;
import ExpressionTypes.Function;
import ExpressionTypes.Variable;
import Types.Type;
import Types.TypeArrow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionAnnotator {

    private TypeVariableGenerator typeVariableGenerator = new TypeVariableGenerator();
    private Map<String, Type> freeVariables = new HashMap<>();

    public Expression annotate(String expression) {
        typeVariableGenerator.reset();
        freeVariables.clear();
        return annotate(expression, new HashMap<>());
    }

    private Expression annotate(String expression, Map<String, Type> boundVariables) {
        expression = expression.trim();
        if (expression.startsWith("(") && expression.endsWith(")")) {
            expression = expression.substring(1, expression.length() - 1).trim();
        }
        if (expression.startsWith("\\")) { // function declaration
            int index = expression.indexOf(".");
            String x = expression.substring(1, index).trim();
            String e = expression.substring(index + 1);
            Type type = typeVariableGenerator.generateNext();
            boundVariables.put(x, type);
            Expression ae = annotate(e, boundVariables);
            return new Function(x, ae, new TypeArrow(type, ae.getType()));
        } else {
            String[] tokens = expression.split(" ");
            if (tokens.length == 1) { // variable
                String name = tokens[0];
                Type type;
                if (boundVariables.containsKey(name)) { // bound variable
                    type = boundVariables.get(name);
                } else if (freeVariables.containsKey(name)) { // free variable
                    type = freeVariables.get(name);
                } else { // new variable
                    type = typeVariableGenerator.generateNext();
                    freeVariables.put(name, type);
                }
                return new Variable(name, type);
            } else { // function application
                Map<String, Type> bv1 = new HashMap<>(boundVariables);
                Map<String, Type> bv2 = new HashMap<>(boundVariables);
                tokens = expression.split("\\)\\(");
                String e1, e2;
                if (tokens.length == 2) { // inner application in both parts
                    e1 = tokens[0];
                    e2 = tokens[1];
                } else {
                    int index;
                    if (expression.startsWith("(")) { // inner application in first part
                        index = expression.lastIndexOf(")");
                        e1 = expression.substring(0, index + 1).trim();
                        e2 = expression.substring(index + 1).trim();
                    } else if (expression.contains("(")) { // inner application in second part
                        index = expression.indexOf("(");
                        e1 = expression.substring(0, index).trim();
                        e2 = expression.substring(index).trim();
                    } else {
                        tokens = expression.split(" ");
                        e1 = tokens[0];
                        e2 = tokens[1];
                    }
                }
                Expression ae1 = annotate(e1, bv1);
                Expression ae2 = annotate(e2, bv2);
                Type type = typeVariableGenerator.generateNext();
                return new Application(ae1, ae2, type);
            }
        }
    }

}
