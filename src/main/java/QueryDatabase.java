import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryDatabase {
    private static DatabaseConnection dbConnection = new DatabaseConnection();
    private static Connection connection = dbConnection.getConnection();


    public static void main(String[] args) {
        QueryDatabase database = new QueryDatabase();
        String point = " 220,110,NULL";
        database.getNearestPhotographer(point);

//        List<FeatureType> featureTypes = new ArrayList<FeatureType>();
//        featureTypes.add(FeatureType.BUILDING);
//        featureTypes.add(FeatureType.PHOTO);
//        featureTypes.add(FeatureType.PHOTOGRAPHER);
//        String s = "323,200,479,417,690,244,585,59,327,200,327,200";
//        database.getRangePolygons(featureTypes,s);
//        database.getRangePhotoPoints(featureTypes,s);
//        database.getRangePhotographerPoints(featureTypes);
//        dbConnection.closeConnection();


    }
    /*
    This method returns the List of ArrayList containing coordinates of each building
     */
    protected List<ArrayList<Integer>> queryBuildingTable(String query) {
        List<ArrayList<Integer>> allBuildingsGeo = new ArrayList<ArrayList<Integer>>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            allBuildingsGeo = processResultSet(rs,"building");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error while retrieving data from Building table");
            e.printStackTrace();
        }
        return allBuildingsGeo;
    }

    /*
     This method returns the List of ArrayList containing coordinates of all photos
     */
    protected List<ArrayList<Integer>> queryPhotoTable(String query)
    {
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            allPhotoGeo = processResultSet(rs,"photo");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error while retrieving data from Photo table");
            e.printStackTrace();
        }
        return allPhotoGeo;
    }

    /*
     This method returns the List of ArrayList containing location of all photographers
     */
    protected List<ArrayList<Integer>> queryPhotographerTable(String query)
    {
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            allPhotographerGeo = processResultSet(rs,"photographer");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error while retrieving data from Photographer table");
            e.printStackTrace();
        }
        return allPhotographerGeo;
    }

    /*
    This method takes the ResultSet as input and returns the combined list of all coordinates for the tables.
     */
    private List<ArrayList<Integer>> processResultSet(ResultSet rs, String tableName)
    {
        List<Integer> coordinateList ;
        List<ArrayList<Integer>> allCoordinates = new ArrayList<ArrayList<Integer>>();

        try{
            while (rs.next()) {
                STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
                JGeometry j_geom = JGeometry.load(st);
                if(tableName.equalsIgnoreCase("building"))
                {
                    double[] coordinates = j_geom.getOrdinatesArray();
                    coordinateList = convertToIntegerList(coordinates);
                    allCoordinates.add((ArrayList<Integer>) coordinateList);
                }else if (tableName.equalsIgnoreCase("photo") || tableName.equalsIgnoreCase("photographer"))
                {
                    double[] coordinates = j_geom.getPoint();
                    coordinateList = convertToIntegerList(coordinates);
                    allCoordinates.add((ArrayList<Integer>) coordinateList);
                }
            }
        } catch(SQLException e)
        {
            System.out.println("Error while processing ResultSet");
            e.printStackTrace();
        }
        return allCoordinates;

    }


    /*
   This method converts the double array of ordinates received from database into int arraylist
    */
    protected List<Integer> convertToIntegerList(double[] coordinates) {
        List<Integer> points = new ArrayList<Integer>();
        for (int i = 0; i < coordinates.length; i++) {
            Double value = coordinates[i];
            points.add(value.intValue());
        }
        return points;
    }

    /*
    This methods returns an array of xCoordinates or yCoordinates depending upon the index provided
     */
    protected int[] separatePolyCoordinates(List<Integer> coordinateList, int index) {
        int[] points = new int[coordinateList.size() / 2];
        int indexOfCoordinates = index;
        for (int i = 0; i < points.length; i++) {
            points[i] = coordinateList.get(indexOfCoordinates);
            indexOfCoordinates += 2;
        }
        return points;

    }

 /*
    Returns a list of all building / polygons for Whole query#1
  */
    protected List<Polygon> getWholePolygons(List<FeatureType> featureTypes)
    {
        Polygon polygon;
        List<Polygon> polyList = new ArrayList<Polygon>();
        String buildingGeo = "select GEO from building";

//        if(featureTypes.contains(FeatureType.BUILDING))
//        {
                List<ArrayList<Integer>> allBuildingsGeo = queryBuildingTable(buildingGeo);
                for (int i = 0; i < allBuildingsGeo.size(); i++) {
                    int[] xPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                    int[] yPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 1);
                    polygon = new Polygon(xPoly, yPoly, xPoly.length);
                    polyList.add(polygon);
                }
//        }
        return polyList;
    }

    /*
    Returns a list of all photo coordinates for Whole query#1
     */
    protected List<ArrayList<Integer>> getWholePhotoPoints(List<FeatureType> featureTypes)
    {
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();

//       if(featureTypes.contains(FeatureType.PHOTO))
//       {
            String photoGeo = "select PHOTOCOORDINATES from photo";
           allPhotoGeo = queryPhotoTable(photoGeo);
//       }
         return allPhotoGeo;
    }

    /*
    Returns the list of all photographer coordinates for Whole query#1
     */
    protected List<ArrayList<Integer>> getWholePhotographerPoints(List<FeatureType> featureTypes)
    {
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();

//        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
//        {
            String photographerGeo = "select PHOTOGRAPHERLOC from photographer";
            allPhotographerGeo = queryPhotographerTable(photographerGeo);
//        }
            return allPhotographerGeo;
    }


    /*
    Returns List of all polygons for Range Query#2
     */
    protected List<Polygon> getRangePolygons(List<FeatureType> featureTypes, String polyPoints)
    {
        Polygon polygon;
        List<Polygon> polyList = new ArrayList<Polygon>();

            if (featureTypes.contains(FeatureType.BUILDING))
            {
                String buildingGeo = "(select B.GEO from building B\n" +
                        "where MDSYS.SDO_RELATE(B.GEO, \n" +
                        "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                        "MDSYS.SDO_ORDINATE_ARRAY("+polyPoints+")),'mask = anyinteract') = 'TRUE')\n";

                        List<ArrayList<Integer>> allBuildingsGeo = queryBuildingTable(buildingGeo);
                for (int i = 0; i < allBuildingsGeo.size(); i++) {
                    int[] xPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                    int[] yPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 1);
                    polygon = new Polygon(xPoly, yPoly, xPoly.length);
                    polyList.add(polygon);
                }
            }
        return polyList;
    }

    /*
    Returns List of all PhotoCoordinates for Range Query#2
     */
    protected List<ArrayList<Integer>> getRangePhotoPoints(List<FeatureType> featureTypes, String polyPoints)
    {
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();

        if(featureTypes.contains(FeatureType.PHOTO))
        {
            String photoGeo = "(select P.PHOTOCOORDINATES from photo P\n" +
                    "where MDSYS.SDO_RELATE(P.PHOTOCOORDINATES,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+polyPoints+")),'mask = anyinteract') = 'TRUE')\n";
            allPhotoGeo = queryPhotoTable(photoGeo);
        }
        return allPhotoGeo;
    }

    /*
    Returns the list of all Photographer Coordinates for Range query#2
     */
    protected List<ArrayList<Integer>> getRangePhotographerPoints(List<FeatureType> featureTypes, String polyPoints)
    {
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();

        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            String photographerGeo = "(select Ph.PHOTOGRAPHERLOC from photographer Ph\n" +
                    "where MDSYS.SDO_RELATE(Ph.PHOTOGRAPHERLOC,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+polyPoints+")),'mask = anyinteract') = 'TRUE')\n";
            allPhotographerGeo = queryPhotographerTable(photographerGeo);
        }
        return allPhotographerGeo;
    }

    /*
    Returns the list of polygons inside/intersecting circle for query#3
     */
    protected List<Polygon>  getBuildingsWithinCircle(String circlePoints)
    {
        Polygon polygon;
        List<Polygon> polyList = new ArrayList<Polygon>();

                String buildingGeo = "(select B.GEO from building B\n" +
                    "where MDSYS.SDO_RELATE(B.GEO, \n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")),'mask = anyinteract') = 'TRUE')\n";

            List<ArrayList<Integer>> allBuildingsGeo = queryBuildingTable(buildingGeo);
            for (int i = 0; i < allBuildingsGeo.size(); i++)
            {
                int[] xPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                int[] yPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 1);
                polygon = new Polygon(xPoly, yPoly, xPoly.length);
                polyList.add(polygon);
            }
        return polyList;

    }

    /*
    Returns List of all PhotoCoordinates for Point Query#3
     */
    protected List<ArrayList<Integer>> getPhotoWithinCircle(String circlePoints)
    {
            String photoGeo = "(select P.PHOTOCOORDINATES from photo P\n" +
                    "where MDSYS.SDO_RELATE(P.PHOTOCOORDINATES,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")),'mask = anyinteract') = 'TRUE')\n";
            return queryPhotoTable(photoGeo);
    }

    /*
   Returns the list of all Photographer Coordinates for Point query#3
    */
    protected List<ArrayList<Integer>> getPhotographerWithinCircle(String circlePoints)
    {
            String photographerGeo = "(select Ph.PHOTOGRAPHERLOC from photographer Ph\n" +
                    "where MDSYS.SDO_RELATE(Ph.PHOTOGRAPHERLOC,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")),'mask = anyinteract') = 'TRUE')\n";
            return queryPhotographerTable(photographerGeo);
    }



    /*
    Returns the location point of the nearest photographer query #4
     */
    protected Point getNearestPhotographer(String point)
    {
        List<ArrayList<Integer>> photographerLoc = new ArrayList<ArrayList<Integer>>();
        String query = "(SELECT Ph.PHOTOGRAPHERLOC FROM photographer Ph  WHERE \n" +
                "SDO_NN(Ph.photographerloc, mdsys.sdo_geometry(2001, null, " +
                "mdsys.sdo_point_type("+point+"), NULL, NULL), 'sdo_num_res=1') = 'TRUE')";
        photographerLoc = queryPhotographerTable(query);
        int x = photographerLoc.get(0).get(0);
        int y = photographerLoc.get(0).get(1);
        return (new Point(x,y));
    }


}
