import Types.TypeVar;

public class TypeVariableGenerator {

    private int typeIndex = 0;

    public TypeVar generateNext() {
        String type = "@" + typeIndex;
        typeIndex++;
        return new TypeVar(type);
    }

    public void reset() {
        typeIndex = 0;
    }

}
