import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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


    DatabaseConnection dbConnection = new DatabaseConnection();
    Connection connection = dbConnection.getConnection();


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
//        image.setMapImage();
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


    private void setMapImage(List<FeatureType> featureTypes)
    {
        Queries queries = new Queries(dbConnection, connection);
        final List<Polygon> polyList = new ArrayList<Polygon>();
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();

        for(FeatureType feature : featureTypes)
        {
            if(feature == FeatureType.BUILDING)
            {
                List<ArrayList<Integer>> allBuildingsGeo = queries.getAllBuildingGeo();
                for(int i = 0; i < allBuildingsGeo.size(); i++) {
                    int[] xPoly = queries.separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                    int[] yPoly = queries.separatePolyCoordinates(allBuildingsGeo.get(i), 1);        // see if it can be abstracted
                    poly = new Polygon(xPoly, yPoly, xPoly.length);
                    polyList.add(poly);
                }
            }
            if(feature == FeatureType.PHOTO)
            {
                allPhotoGeo = queries.getAllPhotoGeo();
            }
            if(feature == FeatureType.PHOTOGRAPHER)
            {
                allPhotographerGeo = queries.getAllPhotographerGeo();
            }
        }
//        dbConnection.closeConnection();
        frame.remove(map);
        map = new WholeRegion(polyList,allPhotoGeo,allPhotographerGeo,new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));
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
        query = new JLabel("Query", SwingConstants.RIGHT);
        whole = new JRadioButton("Whole Region");
        range = new JRadioButton("Range Query");
        point = new JRadioButton("Point Query");
        findPhoto = new JRadioButton("Find photos");
        findPhotographer = new JRadioButton("Find photographer");
        panel1.add(query);
        panel1.add(whole);
        panel1.add(range);
        panel1.add(point);
        panel1.add(findPhoto);
        panel1.add(findPhotographer);
        frame.add(panel1);
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


    private void addSubmit()
    {
        submitButton = new JButton("Submit Query");
        frame.add(submitButton);
        submitButton.addActionListener(new ButtonClickListener());
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener
    {
              public void actionPerformed(ActionEvent e)
              {
                  e.getActionCommand();
                  if(whole.isSelected())
                  {
                      List<FeatureType> featureTypes = getActiveFeatureType();
                      setMapImage(featureTypes);
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
