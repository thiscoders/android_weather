package ye.mdroid.imweather.domain;

/**
 * Created by ye on 16-11-29.
 */

public class MPoint {
    private float mx;
    private float my;

    public MPoint() {
    }

    public MPoint(int mx) {
        this.mx = mx;
    }

    public MPoint(float my) {
        this.my = my;
    }

    public MPoint(float mx, float my) {
        this.mx = mx;
        this.my = my;
    }

    public float getMx() {
        return mx;
    }

    public void setMx(float mx) {
        this.mx = mx;
    }

    public float getMy() {
        return my;
    }

    public void setMy(float my) {
        this.my = my;
    }

    @Override
    public String toString() {
        return "MPoint{" +
                "my=" + my +
                ", mx=" + mx +
                '}';
    }
}
