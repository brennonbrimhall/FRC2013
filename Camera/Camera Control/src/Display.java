
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

public class Display extends Applet implements KeyListener, MouseMotionListener, MouseListener {

    ConfigHandler config = new ConfigHandler();
    public static int REBOOT_KEY = 0x52;
    Line temp_Line = null;
    boolean mouse_pressed = false;
    int cur_x = 0, cur_y = 0;
    CameraControl[] camera = new CameraControl[2];
    boolean[] camera_on = new boolean[2];
    boolean edit = true;

    public void init() {
        updateConfig();
        camera[Camera.FRONT] = new CameraControl("http://10.0.20.8/mjpg/video.mjpg?compression=" + config.compression_1 + "&resolution=" + config.resolution_1, "root", "team20", true, true, config.camera1_pause, true);
        camera[Camera.BACK] = new CameraControl("http://10.0.20.9/mjpg/video.mjpg?compression=" + config.compression_2 + "&resolution=" + config.resolution_2, "root", "team20", true, true, config.camera2_pause, true);
        setSize(camera[Camera.FRONT].camera.grab().getWidth() + camera[Camera.BACK].camera.grab().getWidth(), Math.max(camera[Camera.FRONT].camera.grab().getHeight(), camera[Camera.BACK].camera.grab().getHeight()));
        addKeyListener(this);
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
        BufferedImage im_1 = camera[Camera.FRONT].getImage();
        BufferedImage im_2 = camera[Camera.BACK].getImage();
        g.drawImage(im_1, 0, 0, null);
        g.drawImage(im_2, im_1.getWidth() + 1, 0, null);
        for (int v = 0; v < camera[Camera.FRONT].lines.size(); v++) {
            g.setColor(camera[Camera.FRONT].lines.get(v).c);
            if (camera[Camera.FRONT].lines.get(v).h == Line.VERTICAL) {
                g.drawLine(camera[Camera.FRONT].lines.get(v).level + 1, 0, camera[Camera.FRONT].lines.get(v).level + 1, im_1.getHeight());
                g.drawLine(camera[Camera.FRONT].lines.get(v).level, 0, camera[Camera.FRONT].lines.get(v).level, im_1.getHeight());
                g.drawLine(camera[Camera.FRONT].lines.get(v).level - 1, 0, camera[Camera.FRONT].lines.get(v).level - 1, im_1.getHeight());
            } else {
                g.drawLine(0, camera[Camera.FRONT].lines.get(v).level + 1, im_1.getWidth(), camera[Camera.FRONT].lines.get(v).level + 1);
                g.drawLine(0, camera[Camera.FRONT].lines.get(v).level, im_1.getWidth(), camera[Camera.FRONT].lines.get(v).level);
                g.drawLine(0, camera[Camera.FRONT].lines.get(v).level - 1, im_1.getWidth(), camera[Camera.FRONT].lines.get(v).level - 1);
            }


        }
        for (int v = 0; v < camera[Camera.BACK].lines.size(); v++) {
            g.setColor(camera[Camera.BACK].lines.get(v).c);
            if (camera[Camera.BACK].lines.get(v).h == Line.VERTICAL) {
                g.drawLine(camera[Camera.BACK].lines.get(v).level + im_1.getWidth() + 1, 0, camera[Camera.BACK].lines.get(v).level + im_1.getWidth() + 1, im_2.getHeight());
                g.drawLine(camera[Camera.BACK].lines.get(v).level + im_1.getWidth(), 0, camera[Camera.BACK].lines.get(v).level + im_1.getWidth(), im_2.getHeight());
                g.drawLine(camera[Camera.BACK].lines.get(v).level + im_1.getWidth() - 1, 0, camera[Camera.BACK].lines.get(v).level + im_1.getWidth() - 1, im_2.getHeight());
            } else {
                g.drawLine(im_1.getWidth() + 1, camera[Camera.BACK].lines.get(v).level + 1, im_1.getWidth() + im_2.getWidth(), camera[Camera.BACK].lines.get(v).level + 1);
                g.drawLine(im_1.getWidth() + 1, camera[Camera.BACK].lines.get(v).level, im_1.getWidth() + im_2.getWidth(), camera[Camera.BACK].lines.get(v).level);
                g.drawLine(im_1.getWidth() + 1, camera[Camera.BACK].lines.get(v).level - 1, im_1.getWidth() + im_2.getWidth(), camera[Camera.BACK].lines.get(v).level - 1);
            }


        }
        graphics.drawImage(im, 0, 0, null);
        repaint();
    }

    public void updateConfig() {
        config.updateConfigs();
        REBOOT_KEY = config.reboot;

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
        if (e.getKeyCode() == REBOOT_KEY) {
            String old_res = config.resolution_1;
            String old_res_2 = config.resolution_2;
            updateConfig();
            String new_res = config.resolution_1;
            String new_res_2 = config.resolution_2;
            camera[Camera.FRONT].newCamera("http://10.0.20.8/mjpg/video.mjpg?compression=" + config.compression_1 + "&resolution=" + config.resolution_1, config.camera1_pause);
            camera[Camera.BACK].newCamera("http://10.0.20.9/mjpg/video.mjpg?compression=" + config.compression_2 + "&resolution=" + config.resolution_2, config.camera2_pause);
            setSize(camera[Camera.FRONT].camera.grab().getWidth() + camera[Camera.BACK].camera.grab().getWidth(), Math.max(camera[Camera.FRONT].camera.grab().getHeight(), camera[Camera.BACK].camera.grab().getHeight()));
            for (int v = 0; v < camera[Camera.FRONT].lines.size(); v++) {
                if (camera[Camera.FRONT].lines.get(v).h == Line.VERTICAL) {
                    camera[Camera.FRONT].lines.get(v).level = (int) (((double) camera[Camera.FRONT].lines.get(v).level) * ((double) ((double) Integer.parseInt(new_res.split("x")[0]) / (double) Integer.parseInt(old_res.split("x")[0]))));
                } else {
                    camera[Camera.FRONT].lines.get(v).level = (int) (((double) camera[Camera.FRONT].lines.get(v).level) * ((double) ((double) Integer.parseInt(new_res.split("x")[1]) / (double) Integer.parseInt(old_res.split("x")[1]))));
                }
            }
            for (int v = 0; v < camera[Camera.BACK].lines.size(); v++) {
                if (camera[Camera.BACK].lines.get(v).h == Line.VERTICAL) {
                    camera[Camera.BACK].lines.get(v).level = (int) (((double) camera[Camera.BACK].lines.get(v).level) * ((double) ((double) Integer.parseInt(new_res_2.split("x")[0]) / (double) Integer.parseInt(old_res_2.split("x")[0]))));
                } else {
                    camera[Camera.BACK].lines.get(v).level = (int) (((double) camera[Camera.BACK].lines.get(v).level) * ((double) ((double) Integer.parseInt(new_res_2.split("x")[1]) / (double) Integer.parseInt(old_res_2.split("x")[1]))));
                }
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            config.gui.setVisible(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            config.saveConfigFile();
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
                    if (between(x, camera[z].lines.get(v).level+(z==Camera.BACK?camera[Camera.FRONT].getImage().getWidth():0) - Line.CONSTANT, camera[z].lines.get(v).level+(z==Camera.BACK?camera[Camera.FRONT].getImage().getWidth():0) + Line.CONSTANT)) {
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
            camera[e.getX() < camera[Camera.FRONT].getImage().getWidth() ? Camera.FRONT : Camera.BACK].lines.add(new Line(Line.VERTICAL, (e.getX() < camera[Camera.FRONT].getImage().getWidth() ? e.getX() : e.getX()-camera[Camera.FRONT].getImage().getWidth()),getColor(color)));
            camera[e.getX() < camera[Camera.FRONT].getImage().getWidth() ? Camera.FRONT : Camera.BACK].lines.add(new Line(Line.HORIZONTAL, e.getY(), getColor(color)));
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
        if (e.getButton() == 1) {
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
