import java.awt.*;
import java.util.*;
import java.util.List;

public class Helper {

//    public static void main(String[] args) {
//        List<Point> polyPoints = new ArrayList<Point>();
//        polyPoints.add(new Point(2,3));
//        polyPoints.add(new Point(4,5));
//        polyPoints.add(new Point(6,7));
//        polyPoints.add(new Point(9,8));
//
//        Helper helper = new Helper();
//        System.out.println(polyPoints.size());
//        System.out.println(Arrays.toString(helper.getX(polyPoints)));
//        System.out.println(Arrays.toString(helper.getY(polyPoints)));
//    }

    protected String pointToString(java.util.List<Point> polygonPoints)
    {
        StringBuilder points = new StringBuilder();
         for(Point p : polygonPoints)
         {
             if(p == polygonPoints.get(polygonPoints.size() - 1))
             {

                points.append(p.x + "," + p.y);
             }else
             {
                 points.append(p.x + "," + p.y + ",");
             }
         }
        System.out.println(points);
        return points.toString();
    }


  /*
  Return an array of xCoordinates for a polygon
   */
    protected int[] getX(List<Point> polygonPoints)
    {
        int[] xPoints = new int[polygonPoints.size()];
        for(int i = 0; i < xPoints.length; i++)
        {
            Double x = polygonPoints.get(i).getX();
            xPoints[i] = x.intValue();
        }
        return xPoints;
    }


     /*
  Return an array of yCoordinates for a polygon
   */
    protected int[] getY(List<Point> polygonPoints)
    {
        int[] yPoints = new int[polygonPoints.size()];
        for(int i = 0; i < yPoints.length; i++)
        {
            Double y = polygonPoints.get(i).getY();
            yPoints[i] = y.intValue();
        }
        return yPoints;
    }

}
