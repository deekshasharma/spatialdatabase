import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.management.Query;
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
    private  JTextArea viewQuery;
    private Polygon poly;
    private JPanel panel;
    private JPanel panel1;

    private String path = "map.JPG";
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
            DrawMap.setDisplayBuildingsOn(false);
            DrawMap.setDisplayPhotosOn(false);
            DrawMap.setDisplayPhotographersOn(false);
            DrawMap.setDrawPointOn(false);
            map.repaint();
        }
    }
      /*
      Handles the action of Range Query radio button
       */
     class rangeRadioAction implements ActionListener{

        public void actionPerformed (ActionEvent e)
        {
            DrawMap.displayBuildingsOn = false;
            DrawMap.displayPhotosOn = false;
            DrawMap.displayPhotographersOn = false;
            DrawMap.startDrawPolygon = true;
            map.repaint();
        }

    }

   /*
      Handles the action of Find Photos radio button
       */
    class pointRadioAction implements ActionListener{
       public void  actionPerformed(ActionEvent e)
       {
           DrawMap.setDrawPointOn(true);
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
            DrawMap.setDrawPointOn(true);
            DrawMap.isFindPhotoOn = true;
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

            DrawMap.setDrawPointOn(true);
            DrawMap.displayCircleAroundPoint = false;
            DrawMap.isFindPhotographerOn = true; // remember to turn off after submit event

        }
    }

    /*
    Query Text Field functionality
     */

    private void setQueryTextField()
    {
        viewQuery = new JTextArea("Query",15,50);
        viewQuery.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(viewQuery);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);
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
                  if(findPhoto.isSelected())
                  {
                      findPhotoSelected();
                  }
                  if(findPhotographer.isSelected())
                  {
                      findPhotographerSelected();
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
        StringBuilder builder = new StringBuilder();
        if(featureTypes.contains(FeatureType.BUILDING))
        {
            List<Polygon> polyList = queryDatabase.getWholePolygons(featureTypes);
            if(polyList.size() > 0)
            {
                builder.append(QueryDatabase.databaseQuery);
            }
            DrawMap.setPolyList(polyList);
            DrawMap.setDisplayBuildingsOn(true);
        }
        if(featureTypes.contains(FeatureType.PHOTO))
        {
            List<ArrayList<Integer>> allPhotoGeo = queryDatabase.getWholePhotoPoints(featureTypes);
            if(allPhotoGeo.size() > 0)
            {
                builder.append(QueryDatabase.databaseQuery);
            }
            DrawMap.setAllPhotoGeo(allPhotoGeo);
            DrawMap.setDisplayPhotosOn(true);
        }
        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            List<ArrayList<Integer>> allPhotographerGeo = queryDatabase.getWholePhotographerPoints(featureTypes);
            if(allPhotographerGeo.size() > 0)
            {
                builder.append(QueryDatabase.databaseQuery);
            }
            DrawMap.setAllPhotographerGeo(allPhotographerGeo);
            DrawMap.setDisplayPhotographersOn(true);
        }
        viewQuery.setText(builder.toString());
        map.repaint();
    }

     /*
   This method is called when range query#2 is selected and submitted.
    */

    private void rangeSelected(List<FeatureType> featureTypes)
    {
        Helper helper = new Helper();
        String polygonPoints = helper.toStringPolygon(DrawMap.getPolygonPoints());

        if(featureTypes.contains(FeatureType.BUILDING))
        {
            List<Polygon> polyList = queryDatabase.getRangePolygons(featureTypes,polygonPoints);
            DrawMap.setPolyList(polyList);
            DrawMap.setDisplayBuildingsOn(true);
        }
        if(featureTypes.contains(FeatureType.PHOTO))
        {
            List<ArrayList<Integer>> allPhotoGeo = queryDatabase.getRangePhotoPoints(featureTypes,polygonPoints);
            DrawMap.setAllPhotoGeo(allPhotoGeo);
            DrawMap.setDisplayPhotosOn(true);
        }
        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            List<ArrayList<Integer>> allPhotographerGeo = queryDatabase.getRangePhotographerPoints(featureTypes,polygonPoints);
            DrawMap.setAllPhotographerGeo(allPhotographerGeo);
            DrawMap.setDisplayPhotographersOn(true);
        }
        map.repaint();
    }


    /*
    This is called when point query#3 is selected and submitted.
     */
    private void pointSelected(List<FeatureType> featureTypes)
    {
        String circleCoordinates = DrawMap.getCircleCoordinates();
        Helper helper = new Helper();
        String centreCoordinates = helper.toStringPoint(DrawMap.getPointClicked());
        DrawMap.setGreenFlagOn(true);
        if(featureTypes.contains(FeatureType.BUILDING))
        {
             List<Polygon> polygonList = queryDatabase.getBuildingsWithinCircle(circleCoordinates);
             Polygon buildingNearCentre = queryDatabase.getBuildingNearCentre(circleCoordinates,centreCoordinates);
             DrawMap.setBuildingNearCentre(buildingNearCentre);
             DrawMap.setPolyList(polygonList);
             DrawMap.setDisplayBuildingsOn(true);
        }
        if(featureTypes.contains(FeatureType.PHOTO))
        {
            List<ArrayList<Integer>>  photoPoints = queryDatabase.getPhotoWithinCircle(circleCoordinates);
            List<Integer> photoNearCentre = queryDatabase.getPhotoNearCentre(circleCoordinates,centreCoordinates);
            DrawMap.setPhotoNearCentre(photoNearCentre);
            DrawMap.setAllPhotoGeo(photoPoints);
            DrawMap.setDisplayPhotosOn(true);
        }
        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            List<ArrayList<Integer>> photographerPoints = queryDatabase.getPhotographerWithinCircle(circleCoordinates);
            List<Integer> photographerNearCentre = queryDatabase.getPhotographerNearCentre(circleCoordinates,centreCoordinates);

            DrawMap.setPhotographerNearCentre(photographerNearCentre);
            DrawMap.setAllPhotographerGeo(photographerPoints);
            DrawMap.setDisplayPhotographersOn(true);
        }
        map.repaint();
        // turn off displayCircleAroundPoint
    }


    /*
       Returns the location of photographer nearest to the selected point query#4
     */
    public static Point getPhotographerNearPoint(Point p)
    {
        Helper helper = new Helper();
        String pointCoordinates = helper.toStringPoint(p);
        QueryDatabase queryDatabase = new QueryDatabase();
        Point nearestPhotographer = queryDatabase.getPhotographerNearPoint(pointCoordinates);

        return nearestPhotographer;
    }

    /*
        This is called when Find Photos query#4 is selected and submitted
     */
    private void findPhotoSelected()
    {
        Helper helper = new Helper();
        String photographerLocation = helper.toStringPoint(DrawMap.photographerNearPoint);
        String polygonCoordinates = helper.toStringPolygon(DrawMap.getPolygonPoints());
        List<ArrayList<Integer>> photos = queryDatabase.getPhotosInPolygonForPhotographer(polygonCoordinates,photographerLocation);
        System.out.println("Photos found are : "+ photos);
        DrawMap.photoByPhotographerInPolygon = photos;
        map.repaint();
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

    /*
    This is called when Find Photographer query is selected and submitted query#5
     */
    private void findPhotographerSelected()
    {
        int[] x = DrawMap.getXRedBuilding();
        int[] y = DrawMap.getYRedBuilding();
        Helper helper = new Helper();
        String xyRedBuilding = helper.constructPolygon(x,y);
        List<ArrayList<Integer>> redPhotos = queryDatabase.getRedPhotos(xyRedBuilding);
        List<ArrayList<Integer>> redPhotographers = queryDatabase.getRedPhotographers(xyRedBuilding);

        DrawMap.redPhotos = redPhotos;
        DrawMap.redPhotographers = redPhotographers;
        map.repaint();
//        DrawMap.isFindPhotographerOn = false;



    }




}
