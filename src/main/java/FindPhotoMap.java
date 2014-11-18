import com.sun.javafx.css.Stylesheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

     class FindPhotoMap extends JLabel
        implements MouseListener, MouseMotionListener
     {
         private boolean polygonIsNowComplete = false;
         /**
          * The 'dummy' point tracking the mouse.
          */
         private final Point trackPoint = new Point();

         /**
          * The list of points making up a polygon.
          */
         private static ArrayList points = new ArrayList();


//         List<Polygon> polyList;
//    ImageIcon imageIcon;
//    List<ArrayList<Integer>> allPhotoGeo;
//    List<ArrayList<Integer>> allPhotographerGeo;



//

         public FindPhotoMap(ImageIcon imageIcon)
    {
        super(imageIcon,SwingConstants.LEFT);
        addMouseListener(this);
        addMouseMotionListener(this);


//        public OriginalMap(java.util.List<Polygon> polyList, java.util.List<ArrayList<Integer>> allPhotoGeo,
//                   List<ArrayList<Integer>> allPhotographerGeo, ImageIcon imageIcon)
//        super(imageIcon,SwingConstants.LEFT);
//        this.polyList = polyList;
//        this.allPhotoGeo = allPhotoGeo;
//        this.allPhotographerGeo = allPhotographerGeo;
//        this.imageIcon = imageIcon;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

//        if(point != null)
//        {
//            Double x = point.getX();
//            Double y = point.getY();
//            g.setColor(Color.RED);
//            g.fillOval(x.intValue(), y.intValue(), 5, 5);
//        }
//
        super.paintComponent(g);

        int numPoints = points.size();
        if (numPoints == 0)
            return; // nothing to draw

        Point prevPoint = (Point) points.get(0);

//        draw polygon
        Iterator it = points.iterator();
        while (it.hasNext()) {
            Point curPoint = (Point) it.next();
            draw(g, prevPoint, curPoint);
            prevPoint = curPoint;
        }

//        now draw tracking line or complete the polygon
        if (polygonIsNowComplete)
            draw(g, prevPoint, (Point) points.get(0));
        else
            draw(g, prevPoint, trackPoint);

    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(820, 580);
    }


         /**
          * This method is required by the MouseListener interface,
          * and is the only one that we really care about.
          */
         @Override
         public void mouseClicked(MouseEvent evt) {
             int x = evt.getX();
             int y = evt.getY();

             switch (evt.getClickCount()) {
                 case 1: // single-click
                     if (polygonIsNowComplete) {
                         points.clear();
                         polygonIsNowComplete = false;
                     }
                     points.add(new Point(x, y));
                     repaint();
                     break;

                 case 2: // double-click
                     polygonIsNowComplete = true;
                     points.add(new Point(x, y));
                     repaint();
                     break;

                 default: // ignore anything else
                     break;
             }
         }



         /**
          * This method is required by the MouseMotionListener interface,
          * and is the only one that we really care about.
          */
         @Override
         public void mouseMoved(MouseEvent evt) {
             trackPoint.x = evt.getX();
             trackPoint.y = evt.getY();
             repaint();
         }

         /**
          * Utility method used to draw points and lines.
          */
         private void draw(Graphics g, Point p1, Point p2) {
             int x1 = p1.x;
             int y1 = p1.y;

             int x2 = p2.x;
             int y2 = p2.y;

             // draw the line first so that the points
             // appear on top of the line ends, not below
             g.setColor(Color.RED);
             g.drawLine(x1 + 3, y1 + 3, x2 + 3, y2 + 3);

             g.setColor(Color.RED);
             g.fillOval(x1, y1, 8, 8);

             g.setColor(Color.RED);
             g.fillOval(x2, y2, 8, 8);
         }

         public static List<Point> getPolygonPoints()
         {
             return points;
         }






         private static Point point;

         public static Point getPoint()
         {
             return point;
         }

         @Override
         public void mousePressed(MouseEvent e) {
             int x = e.getX();
             int y = e.getY();
             point = new Point(x,y);
             repaint();

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
         public void mouseDragged(MouseEvent e) {

         }


     }
