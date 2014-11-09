import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UserInterface
{
    private Frame mainFrame;
    private Label headerLabel;
//    private Label statusLabel;
    private Panel controlPanel;
    private Panel activeFeaturePanel;


//    public UserInterface()
//    {
//        prepareGUI();
//    }

    public static void main(String[] args)
    {
        UserInterface  awtControlDemo = new UserInterface();
        awtControlDemo.prepareGUI();
        awtControlDemo.addCheckbox();
        awtControlDemo.show();
    }

    private void prepareGUI()
    {
        mainFrame = new Frame("Deeksha Sharma - 1088052");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new Label();
        headerLabel.setAlignment(Label.CENTER);
//        statusLabel = new Label();
//        statusLabel.setAlignment(Label.CENTER);
//        statusLabel.setSize(350,100);


        activeFeaturePanel = new Panel();
        activeFeaturePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));


        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(activeFeaturePanel);
//        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }





    private void show(){
        headerLabel.setText("Spatial Database");
        Button submit = new Button("Submit Query");

        Label label1 = new Label("Active Feature Type");
        activeFeaturePanel.add(label1);
        controlPanel.add(submit);

        mainFrame.setVisible(true);
    }

    private void addCheckbox()
    {
        Checkbox buildingBox = new Checkbox("Building");
        Checkbox photoBox = new Checkbox("Photo");
        Checkbox photographerBox = new Checkbox("Photographer");
        controlPanel.add(buildingBox);
        controlPanel.add(photoBox);
        controlPanel.add(photographerBox);
        mainFrame.setVisible(true);
    }







//    Label label = new Label();
//        label.setText("Welcome to TutorialsPoint AWT Tutorial.");
//        label.setAlignment(Label.CENTER);
//        label.setBackground(Color.GRAY);
//        label.setForeground(Color.WHITE);


//

//        controlPanel.add(new ImageComponent("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));
//        mainFrame.add(new ImageComponent("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));

    class ImageComponent extends Component {

        BufferedImage img;

        public void paint(Graphics g)
        {
            g.drawImage(img, 0,0 , null);
        }

        public ImageComponent(String path) {
            try {
                img = ImageIO.read(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Dimension getPreferredSize() {
            if (img == null) {
                return new Dimension(100,100);
            } else {
                return new Dimension(img.getWidth(), img.getHeight());
            }
        }
    }

}
