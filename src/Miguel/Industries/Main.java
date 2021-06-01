package Miguel.Industries;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author Miguel Emmara - 18022146
 */
public class Main {

    public static void main(String[] args) {
        FlatLightLaf.install();
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("Factory.png")));
        JFrame frame = new JFrame("Factory Simulator");
        frame.setPreferredSize(new Dimension(1300,800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new FactorySimulatorGUI());
        frame.pack();

        // Resize frame appropriately for its content
        // Positions frame in center of screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        frame.setLocation(new Point((screenWidth / 2) - (frame.getWidth() / 2),
                (screenHeight / 2) - (frame.getHeight() / 2)));
        frame.setIconImage(logo.getImage());
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
