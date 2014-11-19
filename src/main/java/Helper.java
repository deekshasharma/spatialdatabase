import java.awt.*;
import java.util.*;
import java.util.List;

public class Helper {

    public static void main(String[] args) {
//        List<Point> polyPoints = new ArrayList<Point>();
//        System.out.println(helper.toStringPoint(p));
//
    }

    protected String toStringPolygon(java.util.List<Point> polygonPoints)
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

    /*
    This method converts the coordinates of a point to String
     */
    protected String toStringPoint(Point p)
    {
        StringBuilder points = new StringBuilder();
        Double x = p.getX();
        Double y = p.getY();
        int x1 = x.intValue();
        int y1 = y.intValue();

        points.append(x1).append(",").append(y1).append(",").append("null");

        return points.toString();
    }

    protected String constructPolygon(int[] x, int[] y)
    {
        StringBuilder polyCoordinates = new StringBuilder();
        for (int i= 0 ; i < x.length;i++)
        {
            if(i < x.length-1)
            {
               polyCoordinates.append(x[i]).append(",").append(y[i]).append(",");
            }
            else
            {
                polyCoordinates.append(x[i]).append(",").append(y[i]);
            }
        }
        return polyCoordinates.toString();
    }

}
