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
//        System.out.println(polyPoints.size());
//        pointToString(polyPoints);
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
}
