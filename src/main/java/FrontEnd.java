import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
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

    private String path = "/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG";
    private static QueryDatabase queryDatabase = new QueryDatabase();


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
        map = new DrawMap(new ImageIcon(path));
//        map = new JLabel(new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"),SwingConstants.LEFT);
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
        whole.addActionListener(new wholeRadioAction());

        group.add(range);
        range.addActionListener(new rangeRadioAction());

        group.add(point);
        point.addActionListener(new pointRadioAction());

        group.add(findPhoto);
        findPhoto.addActionListener(new photoRadioAction());


        group.add(findPhotographer);
        findPhotographer.addActionListener(new photographerRadioAction());

        panel1.add(query);
        panel1.add(whole);
        panel1.add(range);
        panel1.add(point);
        panel1.add(findPhoto);
        panel1.add(findPhotographer);
        frame.add(panel1);
        frame.setVisible(true);

    }

    class wholeRadioAction implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            DrawMap.setDisplayBuildings(false);
            DrawMap.setDisplayPhotos(false);
            DrawMap.setDisplayPhotographers(false);
            DrawMap.setDrawPoint(false);
            map.repaint();
        }
    }
      /*
      Handles the action of Range Query radio button
       */
     class rangeRadioAction implements ActionListener{

        public void actionPerformed (ActionEvent e)
        {
            frame.remove(map);
            map = new DrawPolygon(new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));
            frame.add(map);
            frame.setVisible(true);
            map.setVisible(true);
        }
    }

   /*
      Handles the action of Find Photos radio button
       */
    class pointRadioAction implements ActionListener{
       public void  actionPerformed(ActionEvent e)
       {
           DrawMap.setDrawPoint(true);
           DrawMap.displayCircleAroundPoint = true;
//           map.repaint();
       }
   }

    /*
      Handles the action of Find Photos radio button
       */
    class photoRadioAction implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            List<FeatureType> featureTypes = new ArrayList<FeatureType>();
            featureTypes.add(FeatureType.BUILDING);
            featureTypes.add(FeatureType.PHOTO);
            featureTypes.add(FeatureType.PHOTOGRAPHER);
            wholeSelected(featureTypes);
        }
    }

    /*
    Handles the action of Find Photographer radio buttion
     */

    class photographerRadioAction implements ActionListener{

        public void actionPerformed(ActionEvent e)
        {
            List<FeatureType> featureTypes = new ArrayList<FeatureType>();
            featureTypes.add(FeatureType.BUILDING);
            featureTypes.add(FeatureType.PHOTO);
            featureTypes.add(FeatureType.PHOTOGRAPHER);
            wholeSelected(featureTypes);

            DrawMap.setDrawPoint(true);
            DrawMap.displayCircleAroundPoint = false;
            DrawMap.isFindPhotographer = true; // remember to turn off after submit event

        }
    }

    /*
    Query Text Field functionality
     */

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

    /*
    Submit button functionality
     */
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
                  List<FeatureType> featureTypes;
                  if(whole.isSelected())
                  {
                      featureTypes = getActiveFeatureType();
                      wholeSelected(featureTypes);
                  }
                  if(range.isSelected())
                  {
                      featureTypes = getActiveFeatureType();
                      rangeSelected(featureTypes);
                  }
                  if(point.isSelected())
                  {
                      featureTypes = getActiveFeatureType();
                      pointSelected(featureTypes);
                  }
                  if(photographer.isSelected())
                  {
                      photographerSelected();
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

    /*
   This method is called when whole region query is selected and submitted.
    */
    private void wholeSelected(List<FeatureType> featureTypes)
    {
        if(featureTypes.contains(FeatureType.BUILDING))
        {
            List<Polygon> polyList = queryDatabase.getWholePolygons(featureTypes);
            DrawMap.setPolyList(polyList);
            DrawMap.setDisplayBuildings(true);
        }
        if(featureTypes.contains(FeatureType.PHOTO))
        {
            List<ArrayList<Integer>> allPhotoGeo = queryDatabase.getWholePhotoPoints(featureTypes);
            DrawMap.setAllPhotoGeo(allPhotoGeo);
            DrawMap.setDisplayPhotos(true);
        }
        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            List<ArrayList<Integer>> allPhotographerGeo = queryDatabase.getWholePhotographerPoints(featureTypes);
            DrawMap.setAllPhotographerGeo(allPhotographerGeo);
            DrawMap.setDisplayPhotographers(true);
        }
        map.repaint();
    }

     /*
   This method is called when range query is selected and submitted.
    */

    private void rangeSelected(List<FeatureType> featureTypes)
    {
        Helper helper = new Helper();
        String polygonPoints = helper.toStringPolygon(DrawPolygon.getPolygonPoints());
        int[] xRangePoly = helper.getX(DrawPolygon.getPolygonPoints());
        int[] yRangePoly = helper. getY(DrawPolygon.getPolygonPoints());
        Polygon rangePolygon = new Polygon(xRangePoly,yRangePoly,xRangePoly.length);
        List<Polygon> rangePolygonList = new ArrayList<Polygon>();
        rangePolygonList.add(rangePolygon);

        final List<Polygon> polyList = queryDatabase.getRangePolygons(featureTypes,polygonPoints);
        List<ArrayList<Integer>> photo = queryDatabase.getRangePhotoPoints(featureTypes,polygonPoints);
        List<ArrayList<Integer>> photographer = queryDatabase.getRangePhotographerPoints(featureTypes,polygonPoints);

        frame.remove(map);
        DrawMap.setRangePolygon(rangePolygonList);
//        map = new DrawMap(polyList,photo,photographer,
//                                new ImageIcon("/Users/deeksha/IdeaProjects/spatialdatabase/map.JPG"));
                      frame.add(map);
                      map.setVisible(true);
                      frame.setVisible(true);
    }


    /*
    This is called when point query is selected and submitted.
     */
    private void pointSelected(List<FeatureType> featureTypes)
    {
        String circleCoordinates = DrawMap.getCircleCoordinates();
        Helper helper = new Helper();
        String centreCoordinates = helper.toStringPoint(DrawMap.getPointClicked());
        DrawMap.setGreenFlag(true);
        if(featureTypes.contains(FeatureType.BUILDING))
        {
             List<Polygon> polygonList = queryDatabase.getBuildingsWithinCircle(circleCoordinates);
             DrawMap.setPolyList(polygonList);
             DrawMap.setDisplayBuildings(true);
        }
        if(featureTypes.contains(FeatureType.PHOTO))
        {
            List<ArrayList<Integer>>  photoPoints = queryDatabase.getPhotoWithinCircle(circleCoordinates);
            DrawMap.setAllPhotoGeo(photoPoints);
            DrawMap.setDisplayPhotos(true);
        }
        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            List<ArrayList<Integer>> photographerPoints = queryDatabase.getPhotographerWithinCircle(circleCoordinates);
            List<Integer> photographerNearCentre = queryDatabase.getPhotographerNearCentre(circleCoordinates,centreCoordinates);
            System.out.println("Centre coordinates are: "+ centreCoordinates);
            System.out.println("Photographer near centre is "+ photographerNearCentre);
            System.out.println("All photographers within circle "+ photographerPoints);

            DrawMap.setPhotographerNearCentre(photographerNearCentre);
            DrawMap.setAllPhotographerGeo(photographerPoints);
            DrawMap.setDisplayPhotographers(true);
        }
        map.repaint();
        // turn off displayCircleAroundPoint
    }


    /*

     */
    public static Point getNearestPhotographer(Point p)
    {
        Helper helper = new Helper();
        String pointCoordinates = helper.toStringPoint(p);
        QueryDatabase queryDatabase = new QueryDatabase();
        Point nearestPhotographer = queryDatabase.getNearestPhotographer(pointCoordinates);
        return nearestPhotographer;

    }


    /*
    Return the polygon for Red Building in Query#5
     */
    public static Polygon getBuilding(Point p)
    {
        Helper helper = new Helper();
        String point = helper.toStringPoint(p);
        return (queryDatabase.getRedBuildingCoordinates(point));
    }

    private void photographerSelected()
    {
//        List<ArrayList<Integer>> photographers = queryDatabase.getClosePhotographers()
    }




}
