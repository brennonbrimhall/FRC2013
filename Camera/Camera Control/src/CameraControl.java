
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CameraControl {

    BufferedImage defualt;
    public int control_key;
    boolean on = true;
    Camera camera;
    ArrayList<Line> lines = new ArrayList<Line>();

    public CameraControl(String url, String username, String password, boolean AXIS_CAMERA, boolean USE_MJPG,int control, boolean on) {
        camera = new Camera(url, username, password, AXIS_CAMERA, USE_MJPG);
        this.on = on;
        control_key = control;
    }

    public BufferedImage getImage() {
        BufferedImage i = defualt;
        if (on) {
            defualt = i = camera.grab();
        }

        return i;


    }

    public void switchState() {
        
        on = !on;
    }

    public void turnOn() {
        on = true;
    }

    public void turnOff() {
        on = false;
    }

    public void addLine(int h, int level, Color c) {
        lines.add(new Line(h, level, c));
    }

    public void deleteLine(Line l) {
        lines.remove(l);
    }
}
