
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ConfigHandler {

    ConfigGUI gui = new ConfigGUI();
    public static int camera1_pause, camera2_pause, reboot, compression_1, compression_2;
    public static String resolution_1, resolution_2;

    public boolean saveConfigFile() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Config File", ".ini");
        fileChooser.setFileFilter(filter);
        fileChooser.showSaveDialog(null);
        File f = new File(fileChooser.getSelectedFile().getPath() + ".ini");
        String output = gui.getC1() + "-" + gui.getC2() + "-" + gui.getR() + "-" + gui.getCom() + "-" + gui.getCom2() + "-" + gui.getRes() + "-" + gui.getRes2();
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter(f));
            write.write(output);
            write.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public String loadConfigFile() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Config File", ".ini");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(null);
        File f = fileChooser.getSelectedFile();
        if (f == null || !f.exists()) {
            return "-1";
        }
        try {
            BufferedReader read = new BufferedReader(new FileReader(f));

            try {
                String out = read.readLine();
                read.close();
                return out;
            } catch (IOException ex) {
                return "-1";
            }
        } catch (FileNotFoundException ex) {
            return "-1";
        }
    }

    public ConfigHandler() {
        String temp = loadConfigFile();
        if (!temp.equals("-1")) {
            camera1_pause = Integer.parseInt(temp.split("-")[0]);
            camera2_pause = Integer.parseInt(temp.split("-")[1]);
            reboot = Integer.parseInt(temp.split("-")[2]);
            compression_1 = Integer.parseInt(temp.split("-")[3]);
            compression_2 = Integer.parseInt(temp.split("-")[4]);
            resolution_1 = temp.split("-")[5];
            resolution_2 = temp.split("-")[6];
            gui.setData(camera1_pause, camera2_pause, reboot, compression_1, compression_2, resolution_1, resolution_2);
        }
    }

    public void updateConfigs() {
        camera1_pause = gui.getC1();
        camera2_pause = gui.getC2();
        reboot = gui.getR();
        compression_1 = gui.getCom();
        resolution_1 = gui.getRes();
        compression_2 = gui.getCom2();
        resolution_2 = gui.getRes2();
    }
}
