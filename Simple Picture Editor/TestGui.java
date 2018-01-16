//package shoddyphotoshop;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import static java.lang.Math.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

class ImageFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File file) {
        // Allow only directories, or files with extension suitable for images
        return file.isDirectory() || file.getAbsolutePath().endsWith(".jpg") || file.getAbsolutePath().endsWith(".png") || file.getAbsolutePath().endsWith(".gif");
    }

    @Override
    public String getDescription() {
        // This description will be displayed in the dialog,
        // hard-coded = ugly, should be done via I18N
        return "JPG, PNG, and GIF images";
    }
}

public class TestGui extends javax.swing.JFrame {

    public TestGui() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        display = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        openButton = new javax.swing.JMenuItem();
        saveAsButton = new javax.swing.JMenuItem();
        exitButton = new javax.swing.JMenuItem();
        options = new javax.swing.JMenu();
        restoreButton = new javax.swing.JMenuItem();
        hFlipButton = new javax.swing.JMenuItem();
        vFlipButton = new javax.swing.JMenuItem();
        greyScaleButton = new javax.swing.JMenuItem();
        sepiaButton = new javax.swing.JMenuItem();
        invertButton = new javax.swing.JMenuItem();
        bulgeButton = new javax.swing.JMenuItem();

        jFileChooser1.setFileFilter(new ImageFilter());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        display.setForeground(java.awt.Color.white);
        display.setOpaque(true);
        display.setPreferredSize(new java.awt.Dimension(1000, 1000));

        file.setText("File");

        openButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openButton.setText("Open");
        openButton.addActionListener(this::open);
        file.add(openButton);

        saveAsButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveAsButton.setText("Save As");
        saveAsButton.addActionListener(this::saveAs);
        file.add(saveAsButton);

        exitButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exitButton.setText("Exit");
        exitButton.addActionListener(this::exit);

        file.add(exitButton);

        jMenuBar1.add(file);

        options.setText("Options");

        restoreButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        restoreButton.setText("Restore to Original");
        restoreButton.addActionListener(this::restore);
        options.add(restoreButton);

        hFlipButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        hFlipButton.setText("Horizontal Flip");
        hFlipButton.addActionListener(this::hFlip);
        options.add(hFlipButton);

        vFlipButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        vFlipButton.setText("Vertical Flip");
        vFlipButton.addActionListener(this::vFlip);
        options.add(vFlipButton);

        greyScaleButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        greyScaleButton.setText("Gray Scale");
        greyScaleButton.addActionListener(this::greyScale);
        options.add(greyScaleButton);

        sepiaButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        sepiaButton.setText("Sepia Tone");
        sepiaButton.addActionListener(this::sepia);
        options.add(sepiaButton);

        invertButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        invertButton.setText("Invert Colour");
        invertButton.addActionListener(this::invert);
        options.add(invertButton);

        bulgeButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        bulgeButton.setText("Bulge Effect");
        bulgeButton.addActionListener(this::bulge);
        options.add(bulgeButton);

        jMenuBar1.add(options);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("JFrame");

        pack();
    }

    private void open(ActionEvent evt) {//open
        jFileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                im1 = jFileChooser1.getSelectedFile();
                imageDir = im1.toString();
                temp = ImageIO.read(im1);
                display.setIcon(new ImageIcon(temp));
            } catch (IOException ex) {
                System.out.println("problem accessing file" + imageDir);
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }

    private void saveAs(ActionEvent evt) {//save as
        String name = JOptionPane.showInputDialog("Name this file");
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = jFileChooser1.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = new File(jFileChooser1.getSelectedFile(), name + ".png");
                ImageIO.write(temp, "PNG", file);
            } catch (IOException e) {
                System.out.println("Incorrect save file!");
            }
        }
    }

    private void exit(java.awt.event.ActionEvent evt) {//exit
        System.exit(0);
    }

    private void restore(ActionEvent evt) {
        try {
            temp = ImageIO.read(im1);
            display.setIcon(new ImageIcon(temp));
        } catch (IOException ex) {
        }
    }

    private void hFlip(ActionEvent evt) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-temp.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        temp = op.filter(temp, null);
        display.setIcon(new ImageIcon(temp));
    }

    private void vFlip(ActionEvent evt) {//v-flip
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -temp.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        temp = op.filter(temp, null);
        display.setIcon(new ImageIcon(temp));
    }

    private void greyScale(ActionEvent evt) {//greyscale
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(temp, temp);
        display.setIcon(new ImageIcon(temp));
    }

    private void sepia(ActionEvent evt) {//sepia
        raster = temp.getRaster();
        int width = temp.getWidth();
        int height = temp.getHeight();
        System.out.println(width * height * 3);
        int[] pixels = new int[(width * height * 3)];
        raster.getPixels(0, 0, width, height, pixels);

        for (int i = 0; i < pixels.length - 2; i += 3) {// array goes r0,g0,b0,r1,g1,b1, etc
            int r = pixels[i];
            int g = pixels[i + 1];
            int b = pixels[i + 2];

            int ave = (r + g + b) / 3;
            r = ave + 50;
            g = ave + 20;
            b = ave - 30;

            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 1;
            }

            pixels[i] = r;
            pixels[i + 1] = g;
            pixels[i + 2] = b;
        }
        raster.setPixels(0, 0, temp.getWidth(), temp.getHeight(), pixels);
        display.setIcon(new ImageIcon(temp));
    }

    private void invert(ActionEvent evt) {
        for (int x = 0; x < temp.getWidth(); x++) {
            for (int y = 0; y < temp.getHeight(); y++) {
                int rgba = temp.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                temp.setRGB(x, y, col.getRGB());
            }
        }
        display.setIcon(new ImageIcon(temp));
    }

    private void bulge(ActionEvent evt) {
        BufferedImage copy = new BufferedImage(temp.getWidth(), temp.getHeight(), TYPE_INT_RGB);
        for (int x = 0; x < temp.getWidth(); x++) {
            for (int y = 0; y < temp.getHeight(); y++) {
                double r = sqrt(pow(x - temp.getWidth() / 2, 2) + pow(y - temp.getHeight() / 2, 2));
                double theta = atan2((y - temp.getHeight() / 2), (x - temp.getWidth() / 2));
                double rn = pow(r, 1.6) / 30;
                int a = (int) (rn * cos(theta) + temp.getWidth() / 2);
                int b = (int) (rn * sin(theta) + temp.getHeight() / 2);
                if (a < temp.getWidth() && a >= 0 && b >= 0 && b < temp.getHeight()) {
                    copy.setRGB(x, y, temp.getRGB(a, b));
                }
            }
            //I swear to god java hates me ;-;
        }
        display.setIcon(new ImageIcon(copy));
        temp = copy;
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestGui().setVisible(true);
            }
        });
    }

    String imageDir;
    BufferedImage temp;
    File im1;
    WritableRaster raster;

    // Variables declaration - do not modify
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel display;
    private javax.swing.JMenu file;
    private javax.swing.JMenu options;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem openButton;
    private javax.swing.JMenuItem bulgeButton;
    private javax.swing.JMenuItem saveAsButton;
    private javax.swing.JMenuItem exitButton;
    private javax.swing.JMenuItem restoreButton;
    private javax.swing.JMenuItem hFlipButton;
    private javax.swing.JMenuItem vFlipButton;
    private javax.swing.JMenuItem greyScaleButton;
    private javax.swing.JMenuItem sepiaButton;
    private javax.swing.JMenuItem invertButton;

}
