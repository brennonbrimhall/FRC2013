
import com.sun.image.codec.jpeg.JPEGCodec;
import java.awt.image.*;
import java.io.*;
import java.net.*;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * An instance of a network camera.
 *
 * @author Kenton McHenry
 */
public class ImageFeed extends Applet implements MouseListener, MouseMotionListener {
    
    boolean edit;
    ArrayList<Line> lines = new ArrayList<Line>();
    Line temp;
    int base_x = 0, base_y = 0, cur_x = 0, cur_y = 0, value = 0, constant = 5;

    @Override
    public void mouseDragged(MouseEvent e) {
        cur_x = e.getX();
        cur_y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    class Line {
//0==vert, 1==herz

        int h, level;
        Color c;

        public Line(int h, int level, Color c) {
            this.h = h;
            this.level = level;
            this.c = c;
        }
    }

    public ImageFeed() {
    }
    boolean clicked = false;
    BufferedImage Irgb;
    ImageFeed camera;
    private String url = "";
    private String username = "";
    private String password = "";
    private HttpURLConnection huc = null;
    private DataInputStream dis;
    private boolean AXIS_CAMERA = true;
    private boolean USE_MJPG = true;
    private boolean CONNECTED = false;

    /**
     * Class constructor.
     *
     * @param url the URL where to access the image data
     * @param username the user name to use
     * @param password the password to use
     * @param AXIS_CAMERA true if this is this an AXIS camera
     * @param USE_MJPG true if the given URL is for motion JPEG
     */
    public ImageFeed(String url, String username, String password, boolean AXIS_CAMERA, boolean USE_MJPG) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.AXIS_CAMERA = AXIS_CAMERA;
        this.USE_MJPG = USE_MJPG;

        connect();
    }

    /**
     * Encode user name and password in base 64 for a web request.
     *
     * @param username the user name
     * @param password the password
     * @return the encoded user name and password
     */
    private String encodeUsernameAndPasswordInBase64(String username, String password) {
        String string = username + ":" + password;
        //string = new sun.misc.BASE64Encoder().encode(string.getBytes());
        string = javax.xml.bind.DatatypeConverter.printBase64Binary(string.getBytes());
        return "Basic " + string;
    }

    /**
     * Read a line from a data input stream.
     *
     * @param dis the data input stream
     */
    private void readLine(DataInputStream dis) {
        try {
            String delimiter = "\n";    //Assumes that the end of the line is marked with this
            int delimiter_bytes = delimiter.getBytes().length;
            byte[] buffer = new byte[delimiter_bytes];
            String line = "";
            boolean FOUND = false;

            while (!FOUND) {
                dis.read(buffer, 0, delimiter_bytes);
                line = new String(buffer);
                //System.out.print(line);

                if (line.equals(delimiter)) {
                    FOUND = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read lines from the given data input stream.
     *
     * @param n the number of lines to read
     * @param dis the data input stream
     */
    private void readLine(DataInputStream dis, int n) {
        //Used to strip out the header lines
        for (int i = 0; i < n; i++) {
            readLine(dis);
        }
    }

    /**
     * Connect to the network camera.
     */
    public void connect() {
        try {
            huc = (HttpURLConnection) (new URL(url)).openConnection();
            huc.setRequestProperty("Authorization", encodeUsernameAndPasswordInBase64(username, password));

            InputStream is = huc.getInputStream();
            CONNECTED = true;
            BufferedInputStream bis = new BufferedInputStream(is);
            dis = new DataInputStream(bis);
        } catch (IOException e) {
            System.out.println(e);//If no connection exists wait and try again
            try {
                huc.disconnect();
                Thread.sleep(60);
            } catch (InterruptedException ie) {
                huc.disconnect();
                connect();
            }

            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from the network camera.
     */
    public void disconnect() {
        try {
            if (CONNECTED) {
                dis.close();
                CONNECTED = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Grab an image from the camera.
     *
     * @return a 2D integer array containing the image in ARGB format
     */
    public BufferedImage grab() {
        try {
            BufferedImage image = null;

            if (USE_MJPG) {					//Remove the MJPG encapsulation
                readLine(dis, 4);		//Discard the first 4 lines

                image = JPEGCodec.createJPEGDecoder(dis).decodeAsBufferedImage();

                if (AXIS_CAMERA) {
                    readLine(dis, 1);	//Discard one line 
                } else {
                    readLine(dis, 2);	//Discard the last two lines
                }
            } else {
                connect();
                String t = dis.readLine();
                while (t != null) {
                    System.out.println(t);
                    t = dis.readLine();
                }
                //
                disconnect();
            }

            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * A simple main for debug purposes.
     *
     * @param args input arguments (not used)
     */
    public void init() {
        
        edit = JOptionPane.showConfirmDialog(null, "Acess edit mode?", "Edit Mode", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==0?true:false;
        addMouseMotionListener(this);
        addMouseListener(this);
        setSize(640, 460);
        lines.add(new Line(1, getHeight() / 2, Color.RED));
        lines.add(new Line(0, getWidth() / 2, Color.RED));
        lines.add(new Line(1, getHeight() / 4, Color.GREEN));
        lines.add(new Line(1, (int) (getHeight() * (3.0 / 4)), Color.GREEN));

        camera = new ImageFeed("http://10.0.20.8/mjpg/video.mjpg", "root", "team20", true, true);
        setVisible(true);

        repaint();
    }

    public void paint(Graphics g) {
        g.drawImage(camera.grab(), 0, 0, null);
        if (clicked && temp != null&&edit) {
            if (temp.h == 0) {

                temp.level = cur_x;
            } else {
                temp.level = cur_y;

            }
            lines.set(value, temp);
        }
        for (int z = 0; z < lines.size(); z++) {
            g.setColor(lines.get(z).c);
            if (lines.get(z).h == 0) {
                g.drawLine(lines.get(z).level + 1, 0, lines.get(z).level + 1, getHeight());
                g.drawLine(lines.get(z).level, 0, lines.get(z).level, getHeight());
                g.drawLine(lines.get(z).level - 1, 0, lines.get(z).level - 1, getHeight());
            } else {
                g.drawLine(0, lines.get(z).level, getWidth(), lines.get(z).level);
                g.drawLine(0, lines.get(z).level + 1, getWidth(), lines.get(z).level + 1);
                g.drawLine(0, lines.get(z).level - 1, getWidth(), lines.get(z).level - 1);
            }

        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        repaint();
    }

    public boolean between(int t, int x, int y) {
        return Math.min(y, x) < t && t < Math.max(x, y);
    }

    public int distance(int x, int y, int x1, int y1) {
        return (int) Math.abs(Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2)));
    }

    public Line getLine(int x, int y) {
        for (int z = 0; z < lines.size(); z++) {
            if (lines.get(z).h == 0) {
                if (between(x, lines.get(z).level - constant, lines.get(z).level + constant)) {
                    value = z;
                    return lines.get(z);
                }
            } else {
                if (between(y, lines.get(z).level - constant, lines.get(z).level + constant)) {
                    value = z;
                    return lines.get(z);
                }
            }
        }

        return null;
    }

    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3&&edit) {
            lines.remove(getLine(e.getX(), e.getY()));
        }
        if (e.getButton() == 1&&edit) {
            String color = JOptionPane.showInputDialog(null, "New line color?", "New Line", JOptionPane.QUESTION_MESSAGE);
            int h = Integer.parseInt(JOptionPane.showInputDialog(null, "Vertical line,0\nHoriztonal line, 1", "New Line", JOptionPane.QUESTION_MESSAGE));
            lines.add(new Line(h,h==0?e.getX():e.getY(),getColor(color)));

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
        temp = getLine(e.getX(), e.getY());
        base_x = e.getX();
        base_y = e.getY();
        clicked = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clicked = false;
        temp = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}