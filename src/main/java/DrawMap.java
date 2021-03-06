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
    public static List<Polygon> rangePolygon;
    private static Point pointClicked;
    private static boolean drawPointOn = false;
    public static boolean displayBuildingsOn = false;
    public static boolean displayPhotosOn = false;
    public static boolean displayPhotographersOn = false;
    public static boolean greenFlagOn = false;
    public static boolean drawRedBuildingOn = false; // how will you turn this off after find photographer query?
    private static StringBuilder circleCoordinates;
    private static Polygon buildingNearCentre;
    private static List<Integer> photoNearCentre;
    private static List<Integer> photographerNearCentre;
    public static boolean displayCircleAroundPoint = false;
    public static boolean isFindPhotographerOn = false;      // how will you turn this off   ?
    public static boolean isFindPhotoOn = false;
    public static Polygon redBuilding;
    public static List<ArrayList<Integer>> redPhotos;
    public static List<ArrayList<Integer>> redPhotographers;
    public static Point photographerNearPoint;
    public static List<ArrayList<Integer>> photoByPhotographerInPolygon;
    public static boolean startDrawPolygon = false;
    public static boolean mouseMoveOn = false;
    public static boolean drawPersonNearPointOn = false;
    private static boolean drawRedPhotosOn = false;
    private static boolean drawRedPhotographersOn = false;
    private static boolean buildingNearCentreOn = false;
    private static boolean photoNearCentreOn = false;
    private static boolean photographerNearCentreOn = false;
    private static boolean photosInPolygonByPhotographerOn = false;
    public static boolean isRangeRadioOn = false;
    public static boolean isPointRadioOn = false;


    private boolean polygonIsNowComplete = false;

    /**
     * The 'dummy' point tracking the mouse.
     */
    private final Point trackPoint = new Point();

    /**
     * The list of polygonPointsList making up a polygon.
     */
    private static ArrayList polygonPointsList = new ArrayList();




  /* Constructor */
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
        if (displayBuildingsOn) {
            drawBuilding(g);
            displayBuildingsOn = false;
        }
        if (displayPhotosOn) {
            drawPhoto(g);
            displayPhotosOn = false;
        }
        if (displayPhotographersOn) {
            drawPhotographer(g);
            displayPhotographersOn = false;
        }
        if (drawPointOn) {
            drawPoint(g);

        }
        if (displayCircleAroundPoint) {
            drawCircleAroundPoint(g);
        }
        if(buildingNearCentreOn) {drawBuildingNearCenter(g);}                   //For query#3
        if(photoNearCentreOn) {drawPhotoNearCenter(g);}                         //For query#3
        if(photographerNearCentreOn) {drawPhotographerNearCentre(g);}           //For query#3

        if(drawPersonNearPointOn)
        {
            drawPhotographerNearPoint(g);
        }
        if(drawRedBuildingOn)
        {
            drawRedBuilding(g);
//            drawRedBuildingOn = false;
        }
        if(drawRedPhotosOn)
        {drawRedPhotos(g);}
        if(drawRedPhotographersOn)
        {drawPhotographersNearRedBuilding(g);}

        if(startDrawPolygon)
        {
            int numPoints = polygonPointsList.size();
            if (numPoints == 0)
                return; // nothing to draw

            Point prevPoint = (Point) polygonPointsList.get(0);

            // draw polygon
            Iterator it = polygonPointsList.iterator();
            while (it.hasNext()) {
                Point curPoint = (Point) it.next();
                draw(g, prevPoint, curPoint);
                prevPoint = curPoint;
            }

            // now draw tracking line or complete the polygon
            if (polygonIsNowComplete)
                draw(g, prevPoint, (Point) polygonPointsList.get(0));
            else
                draw(g, prevPoint, trackPoint);
        }

        if(photosInPolygonByPhotographerOn)
        {drawPhotosInPolygonByPerson(g);}

    }



    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(isRangeRadioOn || isFindPhotoOn) {
            startDrawPolygon = true;
            mouseMoveOn = true;
            int x = e.getX();
            int y = e.getY();

            switch (e.getClickCount()) {
                case 1: // single-click
                    if (polygonIsNowComplete) {
                        polygonPointsList.clear();
                        polygonIsNowComplete = false;
                    }
                    polygonPointsList.add(new Point(x, y));
                    repaint();
                    break;

                case 2: // double-click
                    polygonIsNowComplete = true;
                    polygonPointsList.add(new Point(x, y));
                    repaint();
                    break;

                default: // ignore anything else
                    break;
            }
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        trackPoint.x = e.getX();
        trackPoint.y = e.getY();
        if(mouseMoveOn)
        {repaint();}

    }


    private void draw(Graphics g, Point p1, Point p2) {
        int x1 = p1.x;
        int y1 = p1.y;

        int x2 = p2.x;
        int y2 = p2.y;

        // draw the line first so that the polygonPointsList
        // appear on top of the line ends, not below
        g.setColor(Color.RED);
        g.drawLine(x1 + 3, y1 + 3, x2 + 3, y2 + 3);

        g.setColor(Color.RED);
        g.fillOval(x1, y1, 8, 8);

        g.setColor(Color.RED);
        g.fillOval(x2, y2, 8, 8);
    }


    /* Setter Methods

    /* This method sets the List<Polygon> for all the building coordinates*/
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
    Sets the photographerLocation list
     */
    public static   void setAllPhotographerGeo(List<ArrayList<Integer>> allPhotographerGeo){
        DrawMap.allPhotographerGeo = allPhotographerGeo;
    }

    /*
       Sets the boolean flag to display all buildings
     */
    public static void setDisplayBuildingsOn(boolean b)
    {
        displayBuildingsOn = b;
    }

/*
       Sets the boolean flag to display all photos
     */

    public static void setDisplayPhotosOn(boolean b)
    {
        displayPhotosOn = b;
    }

    /*
       Sets the boolean flag to display all photographers
     */

    public static void setDisplayPhotographersOn(boolean b)
    {
        displayPhotographersOn = b;
    }

    /*
       Sets the boolean flag to display point
     */

    public static void setDrawPointOn(boolean b)
    {
        drawPointOn = b;
    }


   /* This method sets the red polygon for the range query#2  */
    public static  void setRangePolygon(List<Polygon> rangePolygon)
    {
        DrawMap.rangePolygon = rangePolygon;
    }


     /*   Sets the Polygon near centre for Point query #3 */
    public static void setBuildingNearCentre(Polygon buildingNearCentre)
    {
        DrawMap.buildingNearCentre = buildingNearCentre;
    }

    /* Set the indicator for building near centre query#3*/
    public static void setBuildingNearCentreOn(Boolean b)
    {
         buildingNearCentreOn = b;
    }

    /*
    Sets the Photo near centre for Point query #3  */
    public static void setPhotoNearCentre(List<Integer> photoNearCentre)
    {
        DrawMap.photoNearCentre = photoNearCentre;
    }

    /*Set the boolean indicator to display for photo near centre Point query #3 */
    public static void setPhotoNearCentreOn(boolean b) { photoNearCentreOn = b;}


    /* Sets the photographer near centre for point query#3    */
    public static void setPhotographerNearCentre(List<Integer> photographerNearCentre)
    {
        DrawMap.photographerNearCentre = photographerNearCentre;
    }

    /* Set the indicator to display photographer near centre for point query#3 */
    public static void setPhotographerNearCentreOn(boolean b){photographerNearCentreOn = b;}


    /* Sets the green flag for query#3 */
    public static void setGreenFlagOn(Boolean b)
    {
        DrawMap.greenFlagOn = b;
    }

    /* Sets the boolean indicator to display photos inside polygon by photographer*/
    public static void setPhotosInPolygonByPhotographerOn(boolean b ){photosInPolygonByPhotographerOn = b;}

    /*  Setter for red photos in Query#5 */
    public static void setDrawRedPhotosOn(Boolean b)
    {
        drawRedPhotosOn = b;
    }

    /*  Setter for red photographers in Query#5 */
    public static void setDrawRedPhotographersOn(Boolean b)
    {
        drawRedPhotographersOn = b;
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

    /*
    Returns the list of polygon points
     */
    public static ArrayList getPolygonPoints()
    {
        return polygonPointsList;
    }



    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        pointClicked = new Point(x, y);
        if(isPointRadioOn)
        {
            setDrawPointOn(true);
            displayCircleAroundPoint = true;
            repaint();
        }
        if(isFindPhotoOn)
        {
            setDrawPointOn(true);
            setDisplayBuildingsOn(true);
            setDisplayPhotosOn(true);
            setDisplayPhotographersOn(true);
            repaint();
        }
        if(isFindPhotographerOn)
        {
            setDrawPointOn(true);
            setDisplayBuildingsOn(true);
            setDisplayPhotosOn(true);
            setDisplayPhotographersOn(true);
            repaint();
        }
    }

    /*
    This method draw the point on the map for Point Query#3,4,5
     */

    private void drawPoint(Graphics g)
    {
        if (pointClicked != null) {
            Double x = pointClicked.getX();
            Double y = pointClicked.getY();
            g.setColor(Color.RED);
            g.fillRect(x.intValue(), y.intValue(), 8, 8);
        }
        if(isFindPhotographerOn)
        {
            displayBuildingsOn = true;
            displayPhotosOn = true;
            displayPhotographersOn = true;
            if(pointClicked != null)
            {
            drawRedBuildingOn = true;
            }
        }
        if(isFindPhotoOn)
        {
            displayBuildingsOn = true;
            displayPhotosOn = true;
            displayPhotographersOn = true;
            if(pointClicked != null)
            {
                drawPersonNearPointOn = true;
                photographerNearPoint = FrontEnd.getPhotographerNearPoint(pointClicked);
            }
            startDrawPolygon = true;
        }
    }




    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

//        startDrawPolygon = true;
//        repaint();

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
                    if(greenFlagOn)
                    {g.setColor(Color.GREEN);}
                    else{g.setColor(Color.YELLOW);}
                    g.drawPolygon(poly);
                }
            }
        }catch (NullPointerException e)
        {
        }
        greenFlagOn = false;

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
                    if(greenFlagOn)
                    {g.setColor(Color.GREEN);}
                    else {g.setColor(Color.BLUE);}
                    g.drawOval(allPhotoGeo.get(i).get(0), allPhotoGeo.get(i).get(1), 6, 6);
                }
            }
        }catch (NullPointerException e)
        {
        }
        greenFlagOn = false;
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
        }
    }


    /*
    This method draw circle around the point chosen by user for Point Query#3
     */
    private void drawCircleAroundPoint(Graphics g)
    {
        if(pointClicked != null)
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
            }

    /*
    Draw the building nearest to center of circle Point query#3
     */
    private void drawBuildingNearCenter(Graphics g)
    {
        if(buildingNearCentre != null)
        {
            g.setColor(Color.YELLOW.darker());
            g.fillPolygon(buildingNearCentre);
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
            g.fillOval(photoNearCentre.get(0), photoNearCentre.get(1), 6, 6);
        }
    }

    /*
    Draw photographer nearest to centre of circle Point query#3
     */
    private void drawPhotographerNearCentre(Graphics g)
    {
        try
        {
            if(photographerNearCentre.size() > 0 )
            {
                g.setColor(Color.YELLOW.darker());
                g.fillRect(photographerNearCentre.get(0), photographerNearCentre.get(1), 5, 5);
            }
        } catch (NullPointerException e)
        {
        }
    }



    /*
 This method draws nearest photographer to the point for Find photos query#4
  */
    private void drawPhotographerNearPoint(Graphics graphics)
    {
        try{
            if(photographerNearPoint != null)
            {
                Double x = photographerNearPoint.getX();
                Double y = photographerNearPoint.getY();
                graphics.drawOval(x.intValue(), y.intValue(),5,5);
                graphics.setColor(Color.RED.darker().brighter().darker());
            }
        } catch (NullPointerException e)
        {
        }
    }


    /*
          Draws the photos inside the polygon taken by selected photographer
     */

    private void drawPhotosInPolygonByPerson(Graphics g)
    {
        try
        {
            if(photoByPhotographerInPolygon.size() > 0 )
            {
                for(int i = 0; i < photoByPhotographerInPolygon.size(); i++)
                {
                    g.setColor(Color.RED.darker());
                    g.drawOval(photoByPhotographerInPolygon.get(i).get(0), photoByPhotographerInPolygon.get(i).get(1), 6, 6);
                }
            }
        }catch (NullPointerException e)
        {
        }
    }

    /*
   Draw the Red building for Find Photographer query#5
    */
    private void drawRedBuilding(Graphics g)
    {

        try{
        redBuilding = FrontEnd.getBuilding(pointClicked);
        g.setColor(Color.RED.darker());
        g.drawPolygon(redBuilding);
        }catch(NullPointerException e)
        {
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
        }
    }

    /*
    This method draws photographer near red building in query#5
    */
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
        }
    }

}
