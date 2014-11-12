import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.*;

public class JRisk {


    private JFrame mainMap;
    private Polygon poly;


    public JRisk() {

        initComponents();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                new JRisk();
            }
        });
    }

    private void initComponents()
    {

        mainMap = new JFrame();
        mainMap.setResizable(false);

        mainMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int xPoly[] = {150, 250, 325, 375, 450, 275, 100};
        int yPoly[] = {150, 100, 125, 225, 250, 375, 300};

        poly = new Polygon(xPoly, yPoly, xPoly.length);
        JPanel p = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.drawPolygon(poly);

            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(820, 580);
            }

        };

        mainMap.add(p);
        mainMap.pack();
        mainMap.setVisible(true);
    }

//    JLabel l = new JLabel()
//    {
//        @Override
//        protected void paintComponent(Graphics g)
//        {
//            super.paintComponent(g);
//
//        }
//    };
}