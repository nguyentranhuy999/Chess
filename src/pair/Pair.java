package pair;

public class Pair<U, V> {
    public final U first;
    public final V second;
    public  boolean special1;
    public  boolean special2;
    public  boolean special3;
    public  boolean special4;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
        this.special1 = false;
        this.special2 = false;
        this.special3 = false;
        this.special4 = false;
    }
}