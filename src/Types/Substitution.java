package Types;

public class Substitution {

    private String id;
    private Type t;

    public Substitution(String id, Type t) {
        this.id = id;
        this.t = t;
    }

    public String getId() {
        return id;
    }

    public Type getT() {
        return t;
    }

    @Override
    public String toString() {
        return "Substitution{" +
                "id='" + id + '\'' +
                ", t=" + t +
                '}';
    }

}
