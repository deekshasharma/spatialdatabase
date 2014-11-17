import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

public class DrawMap extends JLabel implements MouseListener, MouseMotionListener{

//    List<Polygon> polyList;
//    List<ArrayList<Integer>> allPhotographerGeo;
//    List<ArrayList<Integer>> allPhotoGeo;
    private static List<Polygon> polyList;
    ImageIcon imageIcon;
    private static List<ArrayList<Integer>> allPhotoGeo;
    private static List<ArrayList<Integer>> allPhotographerGeo;
    private static List<Polygon> rangePolygon;
    private static Point pointClicked;
    private static Point nearestPhotographer;


    private boolean polygonIsNowComplete = false;
    /**
     * The 'dummy' point tracking the mouse.
     */
    private final Point trackPoint = new Point();

    /**
     * The list of points making up a polygon.
     */
    private static ArrayList points = new ArrayList();




//    public DrawMap(java.util.List<Polygon> polyList, java.util.List<ArrayList<Integer>> allPhotoGeo,
//                   List<ArrayList<Integer>> allPhotographerGeo, ImageIcon imageIcon)
//    {
//        super(imageIcon,SwingConstants.LEFT);
//        this.polyList = polyList;
//        this.allPhotoGeo = allPhotoGeo;
//        this.allPhotographerGeo = allPhotographerGeo;
//        this.imageIcon = imageIcon;
//        addMouseListener(this);
//        addMouseMotionListener(this);
//    }

    public DrawMap(ImageIcon imageIcon)
    {
        super(imageIcon,SwingConstants.LEFT);
//        this.polyList = polyList;
//        this.allPhotoGeo = allPhotoGeo;
//        this.allPhotographerGeo = allPhotographerGeo;
        this.imageIcon = imageIcon;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try{
        if(rangePolygon.size() > 0)
        {
            g.setColor(Color.RED);
            g.drawBuilding(rangePolygon.get(0));
        }
        }catch(NullPointerException e)
        {
            System.out.println("");
        }
        if(polyList.size() > 0)
        {
            for(Polygon poly:polyList)

            {
                g.setColor(Color.YELLOW);
                g.drawBuilding(poly);
            }
        }
        if(allPhotoGeo.size() > 0)
        {
            for(int i = 0; i < allPhotoGeo.size(); i++)
                {
                    g.drawOval(allPhotoGeo.get(i).get(0),allPhotoGeo.get(i).get(1),3,3);
                    g.setColor(Color.BLUE);
                }
        }
        if(allPhotographerGeo.size() > 0)
        {
            for(int i = 0; i < allPhotographerGeo.size(); i++)
                {
                    g.drawRect(allPhotographerGeo.get(i).get(0),allPhotographerGeo.get(i).get(1),5,5);
                    g.setColor(Color.GREEN);
                }
        }

        if (pointClicked != null) {
            Double x = pointClicked.getX();
            Double y = pointClicked.getY();
            g.setColor(Color.RED);
            g.fillOval(x.intValue(), y.intValue(), 10, 10);
        }
        drawNearestPhotographer(g);


    }
    */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBuilding(g);
        drawPhoto(g);
        drawPhotographer(g);
        drawPoint(g);
//        try{
//            if(rangePolygon.size() > 0)
//            {
//                g.setColor(Color.RED);
//                g.drawBuilding(rangePolygon.get(0));
//            }
//        }catch(NullPointerException e)
//        {
//            System.out.println("");
//        }
//
//        drawNearestPhotographer(g);

    }

    /* This method sets the polList for all the building coordinates*/
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



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(820, 580);
    }


    /*
    This method sets the red polygon for the range query
     */
    public static  void setRangePolygon(List<Polygon> rangePolygon)
    {
        DrawMap.rangePolygon = rangePolygon;
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
        repaint();

//        nearestPhotographer = FrontEnd.getNearestPhotographer(pointClicked);
//        repaint();
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
                    g.setColor(Color.YELLOW);
                    g.drawPolygon(poly);
                }
            }
        }catch (NullPointerException e)
        {
            System.out.println("polyList is empty");
        }

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
                    g.drawOval(allPhotoGeo.get(i).get(0),allPhotoGeo.get(i).get(1),6,6);
                    g.setColor(Color.BLUE);
                }
            }
        }catch (NullPointerException e)
        {
            System.out.println("allPhotoGeoList is empty");
        }
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
                    g.drawRect(allPhotographerGeo.get(i).get(0),allPhotographerGeo.get(i).get(1),5,5);
                    g.setColor(Color.GREEN);
                }
            }
//
        }catch (NullPointerException e){
            System.out.println("allPhotographerGeo list is empty");
        }
    }

    /*
    This method draw the point on the map
     */

    private void drawPoint(Graphics g)
    {
           try{
               if (pointClicked != null) {
                    Double x = pointClicked.getX();
                    Double y = pointClicked.getY();
                    g.setColor(Color.RED);
                    g.fillOval(x.intValue(), y.intValue(), 10, 10);
                }
           }catch (NullPointerException e)
           {
               System.out.println("No point drawn");
           }
    }


    /*
    This method draws the photographer in red that is nearest to teh selected point.
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



}
