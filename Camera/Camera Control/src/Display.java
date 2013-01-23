
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

public class Display extends Applet implements KeyListener, MouseMotionListener, MouseListener {

    Line temp_Line = null;
    boolean mouse_pressed = false;
    int cur_x = 0, cur_y = 0;
    CameraControl[] camera = new CameraControl[2];
    boolean[] camera_on = new boolean[2];
    boolean edit = true;

    public void init() {
        camera[Camera.FRONT] = new CameraControl("http://10.0.20.8/mjpg/video.mjpg?compression=90&resolution=320x240", "root", "team20", true, true, 0x31, true);
        camera[Camera.BACK] = new CameraControl("http://10.0.20.9/mjpg/video.mjpg?compression=90&resolution=320x240", "root", "team20", true, true, 0x32, true);
        setSize(camera[Camera.FRONT].camera.grab().getWidth() * 2, camera[Camera.FRONT].camera.grab().getHeight());
        addKeyListener(this);
        setLocation(0,300);
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);
        requestFocus();
        repaint();
    }

    public void paint(Graphics graphics) {
        if (mouse_pressed && temp_Line != null && edit) {
            if (temp_Line.h == Line.VERTICAL) {
                if ((temp_Line.level < getWidth() / 2 && cur_x < getWidth() / 2) || temp_Line.level > getWidth() / 2 && cur_x > getWidth() / 2) {
                    temp_Line.level = cur_x;
                }

            } else {
                temp_Line.level = cur_y;
            }

        }
        BufferedImage im = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = im.getGraphics();
        g.drawImage(camera[Camera.FRONT].getImage(), 0, 0, null);
        g.drawImage(camera[Camera.BACK].getImage(), getWidth() / 2, 0, null);
        for (int z = 0; z < camera.length; z++) {
            for (int v = 0; v < camera[z].lines.size(); v++) {
                g.setColor(camera[z].lines.get(v).c);
                if (camera[z].lines.get(v).h == Line.VERTICAL) {
                    g.drawLine(camera[z].lines.get(v).level + 1, 0, camera[z].lines.get(v).level + 1, getHeight());
                    g.drawLine(camera[z].lines.get(v).level, 0, camera[z].lines.get(v).level, getHeight());
                    g.drawLine(camera[z].lines.get(v).level - 1, 0, camera[z].lines.get(v).level - 1, getHeight());
                } else {
                    g.drawLine(z * getWidth() / 2, camera[z].lines.get(v).level, (z + 1) * getWidth() / 2, camera[z].lines.get(v).level);
                    g.drawLine(z * getWidth() / 2, camera[z].lines.get(v).level + 1, (z + 1) * getWidth() / 2, camera[z].lines.get(v).level + 1);
                    g.drawLine(z * getWidth() / 2, camera[z].lines.get(v).level - 1, (z + 1) * getWidth() / 2, camera[z].lines.get(v).level - 1);
                }


            }
        }
        graphics.drawImage(im, 0, 0, null);
        repaint();
    }

    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (int z = 0; z < camera.length; z++) {
            if (e.getKeyCode() == camera[z].control_key) {
                camera[z].switchState();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public boolean between(int t, int x, int y) {
        return Math.min(y, x) < t && t < Math.max(x, y);
    }

    public Line getLine(int x, int y) {
        for (int z = 0; z < camera.length; z++) {
            for (int v = 0; v < camera[z].lines.size(); v++) {
                if (camera[z].lines.get(v).h == Line.VERTICAL) {
                    if (between(x, camera[z].lines.get(v).level - Line.CONSTANT, camera[z].lines.get(v).level + Line.CONSTANT)) {
                        return camera[z].lines.get(v);
                    }
                } else {
                    if (between(y, camera[z].lines.get(v).level - Line.CONSTANT, camera[z].lines.get(v).level + Line.CONSTANT)) {
                        return camera[z].lines.get(v);
                    }
                }
            }
        }


        return null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        cur_x = e.getX();
        cur_y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3 && edit) {
            Line temp = getLine(e.getX(), e.getY());
            for (int z = 0; z < camera.length; z++) {
                for (int v = 0; v < camera[z].lines.size(); v++) {
                    if (camera[z].lines.get(v).equals(temp)) {
                        camera[z].deleteLine(temp);
                    }
                }
            }
        }
        if (e.getButton() == 1 && edit) {
            String color = JOptionPane.showInputDialog(null, "New line color?", "New Line", JOptionPane.QUESTION_MESSAGE);
            //int h = Integer.parseInt(JOptionPane.showInputDialog(null, "Vertical line,0\nHoriztonal line, 1", "New Line", JOptionPane.QUESTION_MESSAGE));
            //camera[e.getX() < getWidth() / 2 ? Camera.FRONT : Camera.BACK].lines.add(new Line(h, h == 0 ? e.getX() : e.getY(), getColor(color)));
            camera[e.getX() < getWidth() / 2 ? Camera.FRONT : Camera.BACK].lines.add(new Line(Line.VERTICAL, e.getX(), getColor(color)));
            camera[e.getX() < getWidth() / 2 ? Camera.FRONT : Camera.BACK].lines.add(new Line(Line.HORIZONTAL, e.getY(), getColor(color)));
        }
    }

    public Color getColor(String s) {

        try {
            return (Color) Class.forName("java.awt.Color").getField(s).get(null);
        } catch (Exception e) {
            return Color.RED;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton()==1){
        mouse_pressed = true;
        temp_Line = getLine(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouse_pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
