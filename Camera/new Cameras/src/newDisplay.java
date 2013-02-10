
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class newDisplay extends JFrame implements MouseListener, MouseMotionListener, KeyListener {

    public final int width = 45;
    JPanel camera_1 = new JPanel(null, true), camera_2 = new JPanel(null, true);
    JPanel temp_Line = null;
    boolean mouse_pressed = false;
    int cur_x = 0, cur_y = 0;
    boolean edit = true;
    AppletControl[] apps = new AppletControl[2];
    ArrayList<JPanel> lines = new ArrayList<JPanel>();
    int[] random = new int[3];

    public static void main(String[] args) {
        newDisplay n = new newDisplay();
        n.hope();
    }

    public void hope() {
        getContentPane().setBackground(Color.BLACK);
        edit = "20team".equals(JOptionPane.showInputDialog(rootPane, "Edit Mode Password?", "Edit Mode", JOptionPane.QUESTION_MESSAGE));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        apps[0] = new AppletControl("http://10.0.20.9:800/", 640, 480, "admin", "Team20");
        apps[1] = new AppletControl("http://10.0.20.8:799/", 640, 480, "admin", "Team20");
        setSize(apps[0].app.getWidth() + apps[1].app.getWidth(), Math.max(apps[0].app.getHeight(), apps[1].app.getHeight()));
        apps[0].start();
        apps[1].start();
        setLayout(null);
        addKeyListener(this);
        add(camera_1);
        add(camera_2);
        camera_1.setVisible(true);
        camera_2.setVisible(true);
        apps[0].app.addMouseListener(this);
        apps[0].app.addMouseMotionListener(this);
        apps[1].app.addMouseListener(this);
        apps[1].app.addMouseMotionListener(this);
        camera_1.setSize(640, 480);
        camera_2.setSize(640, 480);
        camera_2.setLocation(apps[0].app.getWidth() + width, 0);
        camera_1.add(apps[0].app);
        camera_2.add(apps[1].app);
        
        setVisible(true);
        addMouseMotionListener(this);
        addMouseListener(this);

    }


    public void addLines() {
        for (int z = 0; z < lines.size(); z++) {
            remove(lines.get(z));
        }
        lines.clear();
        for (int z = 0; z < apps.length; z++) {
            for (int v = 0; v < apps[z].lines.size(); v++) {
                drawLine(apps[z].lines.get(v), String.valueOf(apps[z].app.getWidth()) + "x" + String.valueOf(apps[z].app.getHeight()), z);
            }
        }
        drawAll();
    }

    public JPanel getLine(AppLine line, String appres, int cam) {
        JPanel temp = new JPanel(null, true);
        temp.setSize((line.h == AppLine.HORIZONTAL ? Integer.parseInt(appres.split("x")[0]) : 3), (line.h == AppLine.VERTICAL ? Integer.parseInt(appres.split("x")[1]) : 3));
        temp.setVisible(true);
        temp.setBackground(line.c);
        if (edit) {
            temp.addMouseMotionListener(this);
            temp.addMouseListener(this);
        }
        temp.setLocation(line.h == AppLine.HORIZONTAL ? (apps[0].app.getWidth() + width) * cam : (apps[0].app.getWidth() + width) * cam + line.level, line.h == AppLine.HORIZONTAL ? line.level : 0);
        return temp;
    }

    public void drawLine(AppLine line, String appres, int cam) {
        JPanel temp = new JPanel(null, true);
        temp.setSize((line.h == AppLine.HORIZONTAL ? Integer.parseInt(appres.split("x")[0]) : 3), (line.h == AppLine.VERTICAL ? Integer.parseInt(appres.split("x")[1]) : 3));
        temp.setVisible(true);
        temp.setBackground(line.c);
        if (edit) {
            temp.addMouseMotionListener(this);
            temp.addMouseListener(this);
        }
        temp.setLocation(line.h == AppLine.HORIZONTAL ? (apps[0].app.getWidth() + width) * cam : (apps[0].app.getWidth() + width) * cam + line.level, line.h == AppLine.HORIZONTAL ? line.level : 0);
        lines.add(temp);
    }

    public void drawAll() {
        remove(camera_1);
        remove(camera_2);
        for (int z = 0; z < lines.size(); z++) {
            add(lines.get(z));
        }
        add(camera_1);
        add(camera_2);
    }

    public boolean save() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Config File", ".ini");
        fileChooser.setFileFilter(filter);
        fileChooser.showSaveDialog(null);
        File f = new File(fileChooser.getSelectedFile().getPath().contains(".ini") ? fileChooser.getSelectedFile().getPath() : fileChooser.getSelectedFile().getPath() + ".ini");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter(f));
            for (int z = 0; z < apps.length; z++) {
                for (int v = 0; v < apps[z].lines.size(); v++) {
                    write.write(apps[z].lines.get(v).toString());
                    write.newLine();
                }
                write.write("~");
                write.newLine();
            }
            write.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void load() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Config File", ".ini");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(null);
        File f = fileChooser.getSelectedFile();
        if (f == null || !f.exists()) {
            return;
        }
        try {
            BufferedReader readLine = new BufferedReader(new FileReader(f));

            try {
                String temp;
                int z = 0;
                while ((temp = readLine.readLine()) != null) {
                    if (temp.equals("~")) {
                        z = 1;
                        continue;
                    }
                    apps[z].addLine(Integer.parseInt(temp.split("-")[0]), Integer.parseInt(temp.split("-")[1]), new Color(Integer.parseInt(temp.split("-")[2]), Integer.parseInt(temp.split("-")[3]), Integer.parseInt(temp.split("-")[4])));
                }
                readLine.close();
                addLines();
            } catch (IOException ex) {
                return;

            }
        } catch (FileNotFoundException ex) {
            return;

        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int cap = 0;
        if (temp_Line != null) {
            temp_Line.setLocation(apps[random[0]].lines.get(random[2]).h == AppLine.HORIZONTAL ? lines.get(random[1]).getX() : e.getX() + lines.get(random[1]).getX(), apps[random[0]].lines.get(random[2]).h == AppLine.HORIZONTAL ? e.getY() + lines.get(random[1]).getY() : lines.get(random[1]).getY());
        }
        for (int z = 0; z < apps.length && temp_Line == null; z++) {
            for (int v = 0; v < apps[z].lines.size(); v++) {
                if (z == 0) {
                    cap++;
                }
                if (String.valueOf(e.getSource()).equals(String.valueOf(getLine(apps[z].lines.get(v), String.valueOf(apps[z].app.getWidth()) + "x" + String.valueOf(apps[z].app.getHeight()), z)))) {
                    temp_Line = lines.get(v + cap * z);
                    random[0] = z;
                    random[1] = v + cap * z;
                    random[2] = v;
                }
            }
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3 && edit) {
            for (int z = 0; z < apps.length; z++) {
                for (int v = 0; v < apps[z].lines.size(); v++) {
                    if (String.valueOf(e.getSource()).equals(String.valueOf(getLine(apps[z].lines.get(v), String.valueOf(apps[z].app.getWidth()) + "x" + String.valueOf(apps[z].app.getHeight()), z)))) {
                        apps[z].lines.remove(v);
                    }
                }
            }

            addLines();
        }
        if (e.getButton() == 1 && edit) {
            String color = JOptionPane.showInputDialog(null, "New line color?", "New Line", JOptionPane.QUESTION_MESSAGE);
            apps[String.valueOf(e.getSource()).split("panel")[1].split(",")[0].equals("0") ? 0 : 1].lines.add(new AppLine(AppLine.VERTICAL, e.getX(), getColor(color)));
            apps[String.valueOf(e.getSource()).split("panel")[1].split(",")[0].equals("0") ? 0 : 1].lines.add(new AppLine(AppLine.HORIZONTAL, e.getY(), getColor(color)));
            addLines();
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
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (temp_Line != null) {
            apps[random[0]].lines.get(random[2]).level = apps[random[0]].lines.get(random[2]).h == AppLine.VERTICAL ? temp_Line.getX() - random[0] * apps[0].app.getWidth() : temp_Line.getY();
            temp_Line = null;
            addLines();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && edit) {
            save();
        }
        if (e.getKeyCode() == KeyEvent.VK_L) {
            load();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
