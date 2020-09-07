import ExpressionTypes.Application;
import ExpressionTypes.Expression;
import ExpressionTypes.Function;
import ExpressionTypes.Variable;
import Types.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TypeInference {

    public static void main(String[] args) throws IOException {
        ExpressionAnnotator annotator = new ExpressionAnnotator();
//        Expression expression = annotator.annotate("(\\x -> (\\y -> (\\z -> ((y z) x))))");
//        Expression expression = annotator.annotate("(\\x -> (\\y -> (y (y x))))");
//        Expression expression = annotator.annotate("(\\x -> (\\y -> (\\z -> (z (y (y x))))))");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Integer N = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < N; i++) {
            Expression expression = annotator.annotate(bufferedReader.readLine());
            LinkedList<Expression> expressions = new LinkedList<>();
            expressions.add(expression);
            LinkedList<Constraint> constraints = collectConstraints(expressions, new LinkedList<>());
            List<Substitution> substitutions = null;
            try {
                substitutions = unify(constraints);
                System.out.println(replace(apply(substitutions, expression.getType())));
            } catch (Exception e) {
                System.out.println("type error");
            }
        }
        bufferedReader.close();
    }

    public static LinkedList<Constraint> collectConstraints(LinkedList<Expression> expressions, LinkedList<Constraint> constraints) {
        if (expressions.isEmpty()) {
            return constraints;
        }
        Expression expression = expressions.removeFirst();
        if (expression instanceof Variable) {
        } else if (expression instanceof Function) {
            expressions.addFirst(((Function) expression).getE());
        } else if (expression instanceof Application) { // Application
            expressions.addFirst(((Application) expression).getE2());
            expressions.addFirst(((Application) expression).getE1());
            constraints.addFirst(new Constraint(((Application) expression).getE1().getType(), new TypeArrow(((Application) expression).getE2().getType(), expression.getType())));
        }
        return collectConstraints(expressions, constraints);
    }

    public static Type subst(Substitution s, Type t) {
        if (t instanceof TypeVar) {
            if (((TypeVar) t).getId().equals(s.getId())) {
                return s.getT();
            } else {
                return t;
            }
        } else {
            return new TypeArrow(subst(s, ((TypeArrow)t).getU()), subst(s, ((TypeArrow)t).getV()));
        }
    }

    public static Type apply(List<Substitution> s, Type t) {
        Type result = t;
        for (int i = s.size() - 1; i >= 0; i--) {
            result = subst(s.get(i), result);
        }
        return result;
    }

    public static List<Substitution> unify_one(Type s, Type t) throws Exception {
        if (s instanceof TypeVar && t instanceof TypeVar) {
            if (((TypeVar) s).getId().equals(((TypeVar) t).getId())) {
                return new LinkedList<>();
            } else {
               List<Substitution> l = new LinkedList<>();
               l.add(new Substitution(((TypeVar) s).getId(), t));
               return l;
            }
        } else if (s instanceof TypeArrow && t instanceof TypeArrow) {
            LinkedList<Constraint> l = new LinkedList<>();
            l.add(new Constraint(((TypeArrow) s).getU(), ((TypeArrow) t).getU()));
            l.add(new Constraint(((TypeArrow) s).getV(), ((TypeArrow) t).getV()));
            return unify(l);
        } else if (s instanceof TypeArrow && t instanceof TypeVar) {
            if (occurs(((TypeVar) t).getId(), s)) {
                throw new Exception("Unification failed: circularity");
            }
            List<Substitution> l = new LinkedList<>();
            l.add(new Substitution(((TypeVar) t).getId(), s));
            return l;
        } else {
            if (occurs(((TypeVar) s).getId(), t)) {
                throw new Exception("Unification failed: circularity");
            }
            List<Substitution> l = new LinkedList<>();
            l.add(new Substitution(((TypeVar) s).getId(), t));
            return l;
        }
    }

    public static boolean occurs(String id, Type t) {
        if (t instanceof TypeVar) {
            return (id.equals(((TypeVar) t).getId()));
        } else {
            return occurs(id, ((TypeArrow)t).getU()) || occurs(id, ((TypeArrow)t).getV());
        }
    }

    public static List<Substitution> unify(LinkedList<Constraint> constraints) throws Exception {
        if (constraints.isEmpty()) {
            return new LinkedList<>();
        }
        Constraint c = constraints.removeFirst();
        List<Substitution> t2 = unify(constraints);
        List<Substitution> t1 = unify_one(apply(t2, c.getT1()), apply(t2, c.getT2()));
        t1.addAll(t2);
        return t1;
    }

    public static Integer findFirstGreaterThan(Type t, int x, Integer min) {
        if (t instanceof TypeVar) {
            Integer i = Integer.parseInt(((TypeVar) t).getId().substring(1));

            if ((min == null || i < min) && i > x) {
                return i;
            }
            return min;
        } else {
            min = findFirstGreaterThan(((TypeArrow)t).getU(), x, min);
            min = findFirstGreaterThan(((TypeArrow)t).getV(), x, min);
            return min;
        }
    }

    public static String replace(Type t) {
        int currentIndex = 0;
        Integer min = findFirstGreaterThan(t, -1, null);
        String expr = t.toString();
        while (min != null) {
            if (min != currentIndex) {
                expr = expr.replace(min + "", currentIndex + "");
            }
            currentIndex++;
            min = findFirstGreaterThan(t, min, null);
        }
        return expr;
    }

    public static List<Substitution> replace2(Type t, List<Substitution> s) {
        int currentIndex = 0;
        Integer min = findFirstGreaterThan(t, -1, null);
        while (min != null) {
            if (min != currentIndex) {
                s.add(0, new Substitution("@" + min, new TypeVar("@" + currentIndex)));
            }
            currentIndex++;
            min = findFirstGreaterThan(t, min, null);
        }
        return s;
    }

}
