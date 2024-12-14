import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class DrawingApplication extends JFrame {
    private JPanel canvas;
    private Color currentColor = Color.BLACK;
    private BufferedImage image;
    private Graphics2D g2d;
    private boolean isDarkMode = false;

    public DrawingApplication() {
        setTitle("FLAmInGo");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(currentColor);

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        canvas.setBackground(Color.WHITE);
        //canvas.setOpaque(true);
        canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        //canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        // Mouse listener for drawing
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                g2d.fillOval(x - 2, y - 2, 5, 5);
                canvas.repaint();
            }
        });

        JPanel toolbar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.BLUE, getWidth(), 0, Color.CYAN);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };


        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Toolbar with buttons
        //JPanel toolbar = new JPanel();
        JButton colorButton = new JButton("color");
        JButton clearButton = new JButton("clear");
        JButton saveButton = new JButton("save");
        JButton loadButton = new JButton("load");
        JToggleButton toggleThemeButton = new JToggleButton("Dark Mode");

        toggleThemeButton.setFocusPainted(false);

        toolbar.add(colorButton);
        toolbar.add(clearButton);
        toolbar.add(saveButton);
        toolbar.add(loadButton);
        toolbar.add(toggleThemeButton);


        // Status bar
        JLabel statusBar = new JLabel("Current Color: Black");
        statusBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);
     
        colorButton.setToolTipText("Choose Color");
        clearButton.setToolTipText("Clear Canvas");
        saveButton.setToolTipText("Save Drawing");
        loadButton.setToolTipText("Load Drawing");

      
        // colorButton.addActionListener(e -> {
            // currentColor = JColorChooser.showDialog(this, "Choose a Color", currentColor);
            // g2d.setColor(currentColor);
        // });


        // Button actions
        colorButton.addActionListener(e -> {
            currentColor = JColorChooser.showDialog(this, "Choose a Color", currentColor);
            g2d.setColor(currentColor);
            statusBar.setText("Current Color: " + currentColor.toString());
        });

        clearButton.addActionListener(e -> {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
            g2d.setColor(currentColor);
            canvas.repaint();
        });

        
        saveButton.addActionListener(e -> saveImage());

        
        loadButton.addActionListener(e -> loadImage());

      
        toggleThemeButton.addActionListener(e -> {
           
            isDarkMode = toggleThemeButton.isSelected();
            if (isDarkMode) {
                canvas.setBackground(Color.DARK_GRAY);
                g2d.setBackground(Color.DARK_GRAY);
                g2d.setColor(Color.WHITE); 
                currentColor = Color.WHITE;
            } else {
                canvas.setBackground(Color.WHITE);
                g2d.setBackground(Color.WHITE);
                g2d.setColor(Color.WHITE); 
                
                currentColor = Color.BLACK;
            }
            g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
            canvas.repaint();
        });

        // Adding toolbar and canvas 
        add(toolbar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveImage() {
        try {
            File file = new File("drawing.png");
            ImageIO.write(image, "PNG", file);
            JOptionPane.showMessageDialog(this, "Image saved as 'drawing.png'");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage());
        }
    }

    private void loadImage() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                image = ImageIO.read(file);
                g2d = image.createGraphics();
                canvas.repaint();
                JOptionPane.showMessageDialog(this, "Image loaded successfully!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        //try {
          //  UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        //} catch (Exception ex) {
          //  ex.printStackTrace();
        //}

        SwingUtilities.invokeLater(DrawingApplication::new);
    }
}
