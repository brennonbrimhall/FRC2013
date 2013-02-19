
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Color;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class AppletControl implements AppletStub {

    public static final int FRONT = 0, BACK = 1;
    String url = "";
    private final HashMap<String, String> PARAMETERS = new HashMap<String, String>();
    public Applet app;
    ArrayList<AppLine> lines = new ArrayList<AppLine>();
    
    @Override
    public void appletResize(int width, int height) {
        //app.setSize(width, height);
    }

    public void addLine(int h, int level, Color c) {
        lines.add(new AppLine(h, level, c));
    }

    public void deleteLine(int l) {
        lines.remove(l);
    }

    public void start() {
        app.init();
        app.start();
    }

    public AppletControl(String u, int w, int h, String user, String p) {
       
        PARAMETERS.put("RemoteHost", u.split("//")[1].split(":")[0]);
        PARAMETERS.put("RemotePort", u.split(":")[2].split("/")[0]);
        System.out.println(PARAMETERS.toString());
        this.url = u;
        URL[] url = new URL[1];
        try {

            url[0] = new URL(u);
            URLClassLoader load = new URLClassLoader(url);
            try {
                try {
                    app = (Applet) load.loadClass("aplug").newInstance();
                    app.setSize(w, h);
                    app.setStub(this);
                    app.setVisible(true);
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

    }

    private void parse(final String url, String u, String p) {
        HttpURLConnection huc;
        try {
            huc = (HttpURLConnection) (new URL(url + "jview.htm")).openConnection();
            huc.setRequestProperty("Authorization", encodeUsernameAndPasswordInBase64(u, p));
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains("param name=\"")) {
                        PARAMETERS.put(line.split("\"")[1], line.split("=")[2].split(">")[0]);
                    }
                }
                System.out.println(PARAMETERS.toString());
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            huc.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private String encodeUsernameAndPasswordInBase64(String username, String password) {
        String string = username + ":" + password;
        //string = new sun.misc.BASE64Encoder().encode(string.getBytes());
        string = javax.xml.bind.DatatypeConverter.printBase64Binary(string.getBytes());
        return "Basic " + string;
    }

    @Override
    public String getParameter(String name) {
        if (PARAMETERS.get(name) != null) {
            return PARAMETERS.get(name);
        }
        switch (name) {


            case "Timeout":
                return "5000";

            case "RotateAngle":
                return "0";

            case "PreviewFrameRate":
                return "2";

            case "DeviceSerialNo":
                return "YWRtaW46VGVhbTIw";



        }

        return null;



        //return PARAMETERS.get(name);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }
}
