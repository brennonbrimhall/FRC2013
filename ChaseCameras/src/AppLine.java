
import java.awt.Color;

public class AppLine {

    public static final int HORIZONTAL = 1, VERTICAL = 0, CONSTANT = 5;
    public int h, level;
    public Color c;

    public AppLine(int h, int level, Color c) {
        this.h = h;
        this.level = level;
        this.c = c;
    }

    public String toString() {
        return h + "-" + level + "-" + c.getRed() + "-" + c.getGreen() + "-" + c.getBlue();
    }
}
