import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

public class DrawMap extends JLabel implements MouseListener, MouseMotionListener{

    private static List<Polygon> polyList;
    ImageIcon imageIcon;
    private static List<ArrayList<Integer>> allPhotoGeo;
    private static List<ArrayList<Integer>> allPhotographerGeo;
    private static List<Polygon> rangePolygon;
    private static Point pointClicked;
    private static Point nearestPhotographer;
    private static boolean drawPoint = false;
    private static boolean displayBuildings = false;
    private static boolean displayPhotos = false;
    private static boolean displayPhotographers = false;
    private static boolean greenFlag = false;
    private static boolean drawRedBuildingFlag = false; // how will you turn this off after find photographer query?
    private static StringBuilder circleCoordinates;
    private static Polygon polygonNearCentre;
    private static List<Integer> photoNearCentre;
    private static List<Integer> photographerNearCentre;
    public static boolean displayCircleAroundPoint = false;
    public static boolean isFindPhotographer = false;
    public static Polygon redBuilding;
    public static List<ArrayList<Integer>> redPhotos;
    public static List<ArrayList<Integer>> redPhotographers;



    public DrawMap(ImageIcon imageIcon)
    {
        super(imageIcon,SwingConstants.LEFT);
        this.imageIcon = imageIcon;
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (displayBuildings) {
            drawBuilding(g);
            System.out.println("drawing building");
            displayBuildings = false;
        }
        if (displayPhotos) {
            drawPhoto(g);
            System.out.println("drawing photo");

            displayPhotos = false;
        }
        if (displayPhotographers) {
            drawPhotographer(g);
            System.out.println("drawing photographer");

            displayPhotographers = false;
        }
        if (drawPoint) {
            drawPoint(g);
            System.out.println("drawing point");

//            drawPoint = false;
        }
        if (displayCircleAroundPoint) {
            drawCircleAroundPoint(g);
            System.out.println("drawing circle");
//            displayCircleAroundPoint = false;
        }
//        drawPhotographerNearCentre(g);

        if(drawRedBuildingFlag)
        {
            System.out.printf("drawing red building");
            drawRedBuilding(g);
            drawRedBuildingFlag = false;
        }
        drawRedPhotos(g);

    }

   /* Setter Methods

    /* This method sets the polyList for all the building coordinates*/
    public static   void setPolyList(List<Polygon> polyList){

        DrawMap.polyList = polyList;
    }


    /*
    This method sets the photoGeo list
     */
    public static void setAllPhotoGeo(List<ArrayList<Integer>> allPhotoGeo)
    {
        DrawMap.allPhotoGeo = allPhotoGeo;
    }


    /*
    this method sets the photographerLocation list
     */
    public static   void setAllPhotographerGeo(List<ArrayList<Integer>> allPhotographerGeo){
        DrawMap.allPhotographerGeo = allPhotographerGeo;

    }

    /*
       Sets the boolean flag to display buildings
     */
    public static void setDisplayBuildings(boolean b)
    {
        displayBuildings = b;
    }

/*
       Sets the boolean flag to display photos
     */

    public static void setDisplayPhotos(boolean b)
    {
        displayPhotos = b;
    }

    /*
       Sets the boolean flag to display photographers
     */

    public static void setDisplayPhotographers(boolean b)
    {
        displayPhotographers = b;
    }

    /*
       Sets the boolean flag to display point
     */

    public static void setDrawPoint(boolean b)
    {
        drawPoint = b;
    }

    /*
        Sets the Polygon near centre for Point query #3
     */
    public static void setPolygonNearCentre(Polygon polygonNearCentre)
    {
        DrawMap.polygonNearCentre = polygonNearCentre;
    }

    /*
    Sets the Photo near centre for Point query #3
     */
    public static void setPhotoNearCentre(List<Integer> photoNearCentre)
    {
        DrawMap.photoNearCentre = photoNearCentre;
    }

    /*
    Sets the photographer near centre for Point query #4
     */
    public static void setPhotographerNearCentre(List<Integer> photographerNearCentre)
    {
        DrawMap.photographerNearCentre = photographerNearCentre;
    }


    /*
    Sets the green flag
     */
    public static void setGreenFlag(Boolean b)
    {
        DrawMap.greenFlag = b;
    }

    /*
   This method sets the red polygon for the range query
    */
    public static  void setRangePolygon(List<Polygon> rangePolygon)
    {
        DrawMap.rangePolygon = rangePolygon;
    }


    /* Getter Methods

     */
    /*
    Returns the circleCoordinates list
     */

    public static String getCircleCoordinates()
    {
        return circleCoordinates.toString();
    }

    /*
    Returns the pointClicked by the user.
     */
    public static Point getPointClicked()
    {
        return pointClicked;
    }

    /*
    Returns x coordinates for red building query#5
     */
    public static int[] getXRedBuilding()
    {
        return redBuilding.xpoints;
    }

    /*
    Returns y coordinates for red building query#5
     */
    public static int[] getYRedBuilding()
    {
        return redBuilding.ypoints;
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(820, 580);
    }



    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        pointClicked = new Point(x, y);
        if(drawPoint)
        {repaint();}

//        nearestPhotographer = FrontEnd.getNearestPhotographer(pointClicked);
//        repaint();
    }


    /*
    This method draw the point on the map for Point Query#3
     */

    private void drawPoint(Graphics g)
    {
        if (pointClicked != null) {
            Double x = pointClicked.getX();
            Double y = pointClicked.getY();
            System.out.println(pointClicked);
            g.setColor(Color.RED);
            g.fillOval(x.intValue(), y.intValue(), 10, 10);
        }
        if(isFindPhotographer)
        {
            displayBuildings = true;
            displayPhotos = true;
            displayPhotographers = true;
            if(pointClicked != null)
            {
            drawRedBuildingFlag = true;
            }
        }
//                displayCircleAroundPoint = true;
    }

    /*
    Draw the Red building for Find Photographer query#5
     */
    private void drawRedBuilding(Graphics g)
    {
        redBuilding = FrontEnd.getBuilding(pointClicked);
        g.setColor(Color.RED.darker());
        g.drawPolygon(redBuilding);
    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /* Draw Methods

    /*
   This methods draws the buildings
    */
    protected void drawBuilding(Graphics g)
    {
        try{
            if(polyList.size() > 0)
            {
                for(Polygon poly:polyList)

                {
                    if(greenFlag)
                    {g.setColor(Color.GREEN);}
                    else{g.setColor(Color.YELLOW);}
                    g.drawPolygon(poly);
                }
            }
        }catch (NullPointerException e)
        {
            System.out.println("polyList is empty");
        }
        greenFlag = false;

    }


    /*
    This method draws the photo on the map
     */
    private void drawPhoto(Graphics g)
    {
        try
        {
            if(allPhotoGeo.size() > 0)
            {
                for(int i = 0; i < allPhotoGeo.size(); i++)
                {
                    if(greenFlag)
                    {g.setColor(Color.GREEN);}
                    else {g.setColor(Color.BLUE);}
                    g.drawOval(allPhotoGeo.get(i).get(0), allPhotoGeo.get(i).get(1), 6, 6);
                }
            }
        }catch (NullPointerException e)
        {
            System.out.println("allPhotoGeoList is empty");
        }
        greenFlag = false;
    }


    /*
    This method draws photographer on the map
     */
    private void drawPhotographer(Graphics g)
    {
        try{
            if(allPhotographerGeo.size() > 0)
            {
                for(int i = 0; i < allPhotographerGeo.size(); i++)
                {
                    g.setColor(Color.GREEN);
                    g.drawRect(allPhotographerGeo.get(i).get(0),allPhotographerGeo.get(i).get(1),5,5);
                }
            }
        }catch (NullPointerException e){
            System.out.println("allPhotographerGeo list is empty");
        }
    }


    /*
    This method draw circle around the point chosen by user for Point Query#3
     */
    private void drawCircleAroundPoint(Graphics g)
    {
        Double x = pointClicked.getX();
        Double y = pointClicked.getY();
        int radius = 100;
        g.setColor(Color.RED);
        g.drawOval(x.intValue() - radius,y.intValue() - radius,2*radius,2*radius);

        circleCoordinates = new StringBuilder();
        circleCoordinates.append(x.intValue()).append(",")
                .append(y.intValue() - radius).append(",")
                .append((x.intValue()+radius)).append(",")
                .append(y.intValue()).append(",")
                .append(x.intValue()).append(",")
                .append(y.intValue() + radius);
    }

    /*
    Draw the building nearest to center of circle Point query#3
     */
    private void drawBuildingNearCenter(Graphics g)
    {
        if(polygonNearCentre != null)
        {
            g.setColor(Color.YELLOW.darker());
            g.drawPolygon(polygonNearCentre);
        }
    }

    /*
    Draw photo nearest to center of circle Point query#3
     */
    private void drawPhotoNearCenter(Graphics g)
    {
        if(photoNearCentre != null)
        {
            g.setColor(Color.YELLOW.darker());
            g.drawOval(photoNearCentre.get(0), photoNearCentre.get(1), 6, 6);
        }
    }

    /*
    Draw photographer nearest to centre of circle Point query#3
     */
    private void drawPhotographerNearCentre(Graphics g)
    {
        if(photographerNearCentre != null)
        {
            g.setColor(Color.RED.darker());
            g.drawRect(photographerNearCentre.get(0), photographerNearCentre.get(1), 5, 5);
        }
    }

    /*
    This method draws nearest photographer to the point for Find photos query#4
     */
    private void drawNearestPhotographer(Graphics graphics)
    {
        try{
            if(nearestPhotographer != null)
            {
                Double x = nearestPhotographer.getX();
                Double y = nearestPhotographer.getY();
                graphics.drawOval(x.intValue(), y.intValue(),5,5);
                graphics.setColor(Color.RED.darker().brighter().darker());
            }
        } catch (NullPointerException e)
        {
            System.out.println("NearestPhotographer is null");
        }
    }

    /*
    This method draws the photos near red building in query#5
     */
    private void drawRedPhotos(Graphics g)
    {
        try
        {
            if(redPhotos.size() > 0)
            {
                for(int i = 0; i < redPhotos.size(); i++)
                {
                    g.setColor(Color.RED.darker());
                    g.drawOval(redPhotos.get(i).get(0), redPhotos.get(i).get(1), 6, 6);
                }
            }
        }catch (NullPointerException e)
        {
            System.out.println("redPhotos is empty");
        }
    }

    /*
    This method draws photographer near red building in query#5

    private void drawPhotographersNearRedBuilding(Graphics g)
    {
        try{
            if(redPhotographers.size() > 0)
            {
                for(int i = 0; i < redPhotographers.size(); i++)
                {
                    g.setColor(Color.RED.darker());
                    g.drawRect(redPhotographers.get(i).get(0),redPhotographers.get(i).get(1),5,5);
                }
            }
        }catch (NullPointerException e){
            System.out.println("redPhotographers list is empty");
        }
    }
    */



}
