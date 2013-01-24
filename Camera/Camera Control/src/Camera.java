
import com.sun.image.codec.jpeg.JPEGCodec;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Camera {
    
    
    
    public static final int FRONT = 0, BACK = 1;
    private String url = "";
    private String username = "";
    private String password = "";
    private HttpURLConnection huc = null;
    private DataInputStream dis;
    private boolean AXIS_CAMERA = true;
    private boolean USE_MJPG = true;
    private boolean CONNECTED = false;
    
    public Camera(String url, String username, String password, boolean AXIS_CAMERA, boolean USE_MJPG) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.AXIS_CAMERA = AXIS_CAMERA;
        this.USE_MJPG = USE_MJPG;

        connect();
    }
    
    public void setURL(String url){
        this.url = url;
        connect();
    }
    
    private String encodeUsernameAndPasswordInBase64(String username, String password) {
        String string = username + ":" + password;
        //string = new sun.misc.BASE64Encoder().encode(string.getBytes());
        string = javax.xml.bind.DatatypeConverter.printBase64Binary(string.getBytes());
        return "Basic " + string;
    }

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
                if (line.equals(delimiter)) {
                    FOUND = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void readLine(DataInputStream dis, int n) {
        //Used to strip out the header lines
        for (int i = 0; i < n; i++) {
            readLine(dis);
        }
    }

    public void connect() {
        try {
            huc = (HttpURLConnection) (new URL(url)).openConnection();
            huc.setRequestProperty("Authorization", encodeUsernameAndPasswordInBase64(username, password));
            InputStream is = huc.getInputStream();
           
            BufferedInputStream bis = new BufferedInputStream(is);
            dis = new DataInputStream(bis);
            CONNECTED = true;
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

    public BufferedImage grab() {
        try {
            BufferedImage image = null;
            if (USE_MJPG) {			//Remove the MJPG encapsulation
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
                disconnect();
            }

            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
