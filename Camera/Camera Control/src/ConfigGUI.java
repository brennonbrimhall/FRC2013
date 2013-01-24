
import java.awt.event.WindowEvent;
import javax.swing.WindowConstants;

public class ConfigGUI extends javax.swing.JFrame {

    public ConfigGUI() {
        this.setDefaultCloseOperation(javax.swing.JFrame.HIDE_ON_CLOSE);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        res = new javax.swing.JComboBox();
        camera_1 = new javax.swing.JTextField();
        compression = new javax.swing.JSlider();
        camera_2 = new javax.swing.JTextField();
        reboot = new javax.swing.JTextField();
        compression2 = new javax.swing.JSlider();
        res2 = new javax.swing.JComboBox();
        compression_t2 = new javax.swing.JTextField();
        compression_t = new javax.swing.JTextField();

        res.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "640x480", "320x240", "160x120" }));
        res.setSelectedIndex(1);

        camera_1.setText("1");

        compression.setMajorTickSpacing(25);
        compression.setMinorTickSpacing(5);
        compression.setPaintLabels(true);
        compression.setPaintTicks(true);
        compression.setValue(90);
        compression.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                c2(evt);
            }
        });

        camera_2.setText("2");

        reboot.setText("r");

        compression2.setMajorTickSpacing(25);
        compression2.setMinorTickSpacing(5);
        compression2.setPaintLabels(true);
        compression2.setPaintTicks(true);
        compression2.setValue(90);
        compression2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                c3(evt);
            }
        });

        res2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "640x480", "320x240", "160x120" }));
        res2.setSelectedIndex(1);

        compression_t2.setText("90");
        compression_t2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                update_cam_2(evt);
            }
        });

        compression_t.setText("90");
        compression_t.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                update_cam_1(evt);
            }
        });
        compression_t.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                update_cam_3(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(compression, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compression2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(res, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(camera_1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compression_t, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(reboot, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(res2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(camera_2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(compression_t2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(res, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(camera_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(res2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(camera_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(compression_t, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(compression_t2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reboot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(compression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(compression2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void update_cam_1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_cam_1
        try {
            int t = Integer.parseInt(compression_t.getText());
            if (t > 0 && t < 101) {
                compression.setValue(t);
            }
        } catch (java.lang.NumberFormatException e) {
        }
    }//GEN-LAST:event_update_cam_1

    private void update_cam_2(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_update_cam_2
        try {
            int t = Integer.parseInt(compression_t2.getText());
            if (t > 0 && t < 101) {
                compression2.setValue(t);
            }
        } catch (java.lang.NumberFormatException e) {
        }
    }//GEN-LAST:event_update_cam_2

    private void c2(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_c2
        compression_t.setText(compression.getValue()+"");
    }//GEN-LAST:event_c2

    private void c3(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_c3
       compression_t2.setText(compression2.getValue()+"");
    }//GEN-LAST:event_c3

    private void update_cam_3(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_update_cam_3
        try {
            int t = Integer.parseInt(compression_t.getText());
            if (t > 0 && t < 101) {
                compression.setValue(t);
            }
        } catch (java.lang.NumberFormatException e) {
        }
    }//GEN-LAST:event_update_cam_3

    public void setData(int c1, int c2, int r, int com,int com2, String res,String res2) {
        camera_1.setText(String.valueOf((char) c1));
        camera_2.setText(String.valueOf((char) c2));
        reboot.setText(String.valueOf((char) r));
        compression.setValue(com);
        compression2.setValue(com2);
        this.res.setSelectedItem(res);
        this.res2.setSelectedItem(res2);
    }

    public int getC1() {
        return (int) camera_1.getText().toUpperCase().charAt(0);
    }

    public int getC2() {
        return (int) camera_2.getText().toUpperCase().charAt(0);
    }

    public int getR() {
        return (int) reboot.getText().toUpperCase().charAt(0);
    }

    
    public String getRes() {
        return (String) res.getSelectedItem();
    }

    public String getRes2() {
        return (String) res2.getSelectedItem();
    }
    
    public int getCom() {
        return compression.getValue();
    }
    public int getCom2() {
        return compression2.getValue();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField camera_1;
    private javax.swing.JTextField camera_2;
    private javax.swing.JSlider compression;
    private javax.swing.JSlider compression2;
    private javax.swing.JTextField compression_t;
    private javax.swing.JTextField compression_t2;
    private javax.swing.JTextField reboot;
    private javax.swing.JComboBox res;
    private javax.swing.JComboBox res2;
    // End of variables declaration//GEN-END:variables
}
