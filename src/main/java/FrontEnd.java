import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;


public class FrontEnd extends JLabel {

    private JButton submitButton;
    private JLabel activeFeature;
    private JLabel query;
    private JLabel map;
    private JFrame frame;
    private JCheckBox building;
    private JCheckBox photo;
    private JCheckBox photographer;
    private JRadioButton whole;
    private JRadioButton range;
    private JRadioButton point;
    private JRadioButton findPhoto;
    private JRadioButton findPhotographer;
    private  JTextField viewQuery;
    private Polygon poly;
    private JPanel panel;
    private JPanel panel1;


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
        image.setMap();
        image.setActiveFeature();
        image.setQuery();
        image.addSubmit();
        image.setQueryTextField();
    }

    private void setMap()
    {
        map = new JLabel(new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"),SwingConstants.LEFT);
        map.setVerticalAlignment(SwingConstants.TOP);
        frame.add(map);
        map.setLayout(new FlowLayout());
        frame.setVisible(true);
    }

    /*
    This method is called when whole region query is selected and submitted.
     */
    private void wholeSelected(List<FeatureType> featureTypes)
    {
        QueryDatabase queryDatabase = new QueryDatabase();
        List<Polygon> polyList = queryDatabase.getWholePolygons(featureTypes);
        List<ArrayList<Integer>> allPhotoGeo = queryDatabase.getWholePhotoPoints(featureTypes);
        List<ArrayList<Integer>> allPhotographerGeo = queryDatabase.getWholePhotographerPoints(featureTypes);

        frame.remove(map);
        map = new WholeRegionUI(polyList,allPhotoGeo,allPhotographerGeo,
                new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));
        map.setVerticalAlignment(SwingConstants.TOP);
        frame.add(map);
        map.setLayout(new FlowLayout());
        frame.setVisible(true);
    }

    private void setActiveFeature()
    {
        panel = new JPanel();
        activeFeature =new JLabel("Active Feature Type",SwingConstants.RIGHT);
        building = new JCheckBox("Building");
        photo = new JCheckBox("Photo");
        photographer = new JCheckBox("Photographer");

        panel.add(activeFeature);
        panel.add(building);
        panel.add(photo);
        panel.add(photographer);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void setQuery()
    {
        panel1 = new JPanel();
        ButtonGroup group = new ButtonGroup();
        query = new JLabel("Query", SwingConstants.RIGHT);
        whole = new JRadioButton("Whole Region");
        range = new JRadioButton("Range Query");
        point = new JRadioButton("Point Query");
        findPhoto = new JRadioButton("Find photos");
        findPhotographer = new JRadioButton("Find photographer");
        group.add(whole);

        group.add(range);
        range.addActionListener(new rangeRadioAction());

        group.add(point);
        group.add(findPhoto);
        group.add(findPhotographer);

        panel1.add(query);
        panel1.add(whole);
        panel1.add(range);
        panel1.add(point);
        panel1.add(findPhoto);
        panel1.add(findPhotographer);
        frame.add(panel1);
        frame.setVisible(true);

    }
      /*
      Allows the user to draw the polygon once range radio button is checked.
       */
     class rangeRadioAction implements ActionListener{

        public void actionPerformed (ActionEvent e)
        {
            frame.remove(map);
            map = new RangeUI(new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));
            frame.add(map);
            frame.setVisible(true);
            map.setVisible(true);
        }

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


    private void addSubmit()
    {
        submitButton = new JButton("Submit Query");
        frame.add(submitButton);
        submitButton.addActionListener(new submitButtonListener());
        frame.setVisible(true);
    }


    /*
    This methods directs the actions of submit button
     */
    private class submitButtonListener implements ActionListener
    {
              public void actionPerformed(ActionEvent e)
              {
                  e.getActionCommand();
                  if(whole.isSelected())
                  {
                      List<FeatureType> featureTypes = getActiveFeatureType();
                      wholeSelected(featureTypes);
                  }
                  if(range.isSelected())
                  {
                      System.out.println("Range is selected");
                  }
              }
    }

    /*
    This method returns the List<FeatureType> that is active/checked. FeatureType is an ENUM
     */
    private List<FeatureType> getActiveFeatureType()
    {
        List<FeatureType> featureTypes = new ArrayList<FeatureType>();
        if(building.isSelected()) {
            featureTypes.add(FeatureType.BUILDING);
        }
        if(photo.isSelected()) {
            featureTypes.add(FeatureType.PHOTO);
        }
        if (photographer.isSelected()) {
            featureTypes.add(FeatureType.PHOTOGRAPHER);
        }
        return featureTypes;
    }


}
