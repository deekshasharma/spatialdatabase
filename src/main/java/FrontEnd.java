import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class FrontEnd {

    private JButton submitButton;
    private JLabel activeFeature;
    private JLabel query;
    private JLabel background;
    private JFrame frame;
    private JCheckBox building;
    private JCheckBox photo;
    private JCheckBox photographer;
    private JPanel panel1;
    private JRadioButton whole;
    private JRadioButton range;
    private JRadioButton point;
    private JRadioButton findPhoto;
    private JRadioButton findPhotographer;
    private  JTextField viewQuery;


    public FrontEnd()
    {

        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setTitle("Deeksha Sharma, SCU ID: 1088052");
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    public static void main(String[] args)
    {
        FrontEnd image = new FrontEnd();
        image.setMapImage();
        image.setActiveFeature();
        image.setQuery();
        image.addSubmit();
        image.setQueryTextField();


    }

    private void setMapImage()
    {
        background=new JLabel(new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"),SwingConstants.LEFT);
        background.setVerticalAlignment(SwingConstants.TOP);
        frame.add(background);
        background.setLayout(new FlowLayout());
        frame.setVisible(true);



    }

    private void setActiveFeature()
    {
        activeFeature =new JLabel("Active Feature Type",SwingConstants.RIGHT);
        activeFeature =new JLabel("Active Feature Type",JLabel.NORTH_EAST);
        activeFeature.setVerticalAlignment(SwingConstants.TOP);
        frame.add(activeFeature);
        frame.setVisible(true);

        building = new JCheckBox("Building");
        frame.add(building);
        frame.setVisible(true);

        photo = new JCheckBox("Photo");
        frame.add(photo);
        frame.setVisible(true);

        photographer = new JCheckBox("Photographer");
        frame.add(photographer);
        frame.setVisible(true);
    }

    private void setQuery()
    {
        query = new JLabel("Query", SwingConstants.RIGHT);
        query.setVerticalAlignment(SwingConstants.CENTER);
        frame.add(query);

        whole = new JRadioButton("Whole Region");
        frame.add(whole);

        range = new JRadioButton("Range Query");
        frame.add(range);

        point = new JRadioButton("Point Query");
        frame.add(point);

        findPhoto = new JRadioButton("Find photos");
        frame.add(findPhoto);

        findPhotographer = new JRadioButton("Find photographer");
        frame.add(findPhotographer);
        frame.setVisible(true);

    }

    private void addSubmit()
    {
        submitButton = new JButton("Submit Query");
        frame.add(submitButton);
        frame.setVisible(true);
    }

    private void setQueryTextField()
    {
        viewQuery = new JTextField("Query",20);
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        BoundedRangeModel brm = viewQuery.getHorizontalVisibility();
        scrollBar.setModel(brm);
        frame.add(scrollBar);
        frame.add(viewQuery);


        viewQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Text: " + viewQuery.getText());
            }
        });
        frame.setVisible(true);
    }





}
