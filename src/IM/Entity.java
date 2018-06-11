package IM;


public class Entity implements Comparable<Entity> {
    public int x;
    public int y;
    public double value;

    public Entity(int x, int y, double value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }


    @Override
    public int compareTo(Entity entity) {
        if (Math.abs(this.value) < Math.abs(entity.value)) {
            return 1;
        }
        if (Math.abs(this.value) > Math.abs(entity.value)) {
            return -1;
        }
        return 0;
    }
}
