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
    public static String databaseQuery = null;


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
        String query = "select GEO from building";

                List<ArrayList<Integer>> allBuildingsGeo = queryBuildingTable(query);
                for (int i = 0; i < allBuildingsGeo.size(); i++) {
                    int[] xPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                    int[] yPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 1);
                    polygon = new Polygon(xPoly, yPoly, xPoly.length);
                    polyList.add(polygon);
                }
         databaseQuery = " Query:"+ query;
        return polyList;
    }

    /*
    Returns a list of all photo coordinates for Whole query#1
     */
    protected List<ArrayList<Integer>> getWholePhotoPoints(List<FeatureType> featureTypes)
    {
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();

            String query = "select PHOTOCOORDINATES from photo";
           allPhotoGeo = queryPhotoTable(query);
        databaseQuery = " Query: "+query;
         return allPhotoGeo;
    }

    /*
    Returns the list of all photographer coordinates for Whole query#1
     */
    protected List<ArrayList<Integer>> getWholePhotographerPoints(List<FeatureType> featureTypes)
    {
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();

            String query = "select PHOTOGRAPHERLOC from photographer";
            allPhotographerGeo = queryPhotographerTable(query);
            databaseQuery = " Query: "+ query;
            return allPhotographerGeo;
    }


    /*
    Returns List of all polygons for Range Query#2
     */
    protected List<Polygon> getRangePolygons(List<FeatureType> featureTypes, String polyPoints)
    {
        Polygon polygon;
        List<Polygon> polyList = new ArrayList<Polygon>();
        String query = null;

            if (featureTypes.contains(FeatureType.BUILDING))
            {
                query = "(select B.GEO from building B\n" +
                        "where MDSYS.SDO_RELATE(B.GEO, \n" +
                        "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                        "MDSYS.SDO_ORDINATE_ARRAY("+polyPoints+")),'mask = anyinteract') = 'TRUE')\n";

                        List<ArrayList<Integer>> allBuildingsGeo = queryBuildingTable(query);
                for (int i = 0; i < allBuildingsGeo.size(); i++) {
                    int[] xPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                    int[] yPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 1);
                    polygon = new Polygon(xPoly, yPoly, xPoly.length);
                    polyList.add(polygon);
                }
            }
        databaseQuery = " Query: "+ query;
        return polyList;
    }

    /*
    Returns List of all PhotoCoordinates for Range Query#2
     */
    protected List<ArrayList<Integer>> getRangePhotoPoints(List<FeatureType> featureTypes, String polyPoints)
    {
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();
        String query = null;

        if(featureTypes.contains(FeatureType.PHOTO))
        {
            query = "(select P.PHOTOCOORDINATES from photo P\n" +
                    "where MDSYS.SDO_RELATE(P.PHOTOCOORDINATES,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+polyPoints+")),'mask = anyinteract') = 'TRUE')\n";
            allPhotoGeo = queryPhotoTable(query);
        }
        databaseQuery = " Query: "+query;
        return allPhotoGeo;
    }

    /*
    Returns the list of all Photographer Coordinates for Range query#2
     */
    protected List<ArrayList<Integer>> getRangePhotographerPoints(List<FeatureType> featureTypes, String polyPoints)
    {
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();
        String query = null;

        if(featureTypes.contains(FeatureType.PHOTOGRAPHER))
        {
            query = "(select Ph.PHOTOGRAPHERLOC from photographer Ph\n" +
                    "where MDSYS.SDO_RELATE(Ph.PHOTOGRAPHERLOC,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+polyPoints+")),'mask = anyinteract') = 'TRUE')\n";
            allPhotographerGeo = queryPhotographerTable(query);
        }
        databaseQuery = " Query: "+query;
        return allPhotographerGeo;
    }

    /*
    Returns buildings within circle for query#3
     */
    protected List<Polygon>  getBuildingsWithinCircle(String circlePoints)
    {
        Polygon polygon;
        List<Polygon> polyList = new ArrayList<Polygon>();
        String query = null;

                query = "(select B.GEO from building B\n" +
                    "where MDSYS.SDO_RELATE(B.GEO, \n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")),'mask = anyinteract') = 'TRUE')\n";

            List<ArrayList<Integer>> allBuildingsGeo = queryBuildingTable(query);
            for (int i = 0; i < allBuildingsGeo.size(); i++)
            {
                int[] xPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 0);
                int[] yPoly = separatePolyCoordinates(allBuildingsGeo.get(i), 1);
                polygon = new Polygon(xPoly, yPoly, xPoly.length);
                polyList.add(polygon);
            }
        databaseQuery = " Query: "+query;
        return polyList;
    }

    /*
    Returns PhotoCoordinates  within circle for Point Query#3
     */
    protected List<ArrayList<Integer>> getPhotoWithinCircle(String circlePoints)
    {
            String query = "(select P.PHOTOCOORDINATES from photo P\n" +
                    "where MDSYS.SDO_RELATE(P.PHOTOCOORDINATES,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")),'mask = anyinteract') = 'TRUE')\n";
            databaseQuery = " Query: "+query;
            return queryPhotoTable(query);
    }

    /*
   Returns all Photographer locations within circle for Point query#3
    */
    protected List<ArrayList<Integer>> getPhotographerWithinCircle(String circlePoints)
    {
            String query = "(select Ph.PHOTOGRAPHERLOC from photographer Ph\n" +
                    "where MDSYS.SDO_RELATE(Ph.PHOTOGRAPHERLOC,\n" +
                    "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4)," +
                    "MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")),'mask = anyinteract') = 'TRUE')\n";
            databaseQuery = " Query: "+query;
            return queryPhotographerTable(query);
    }

    /*
        Returns building polygon near to centre query#3
     */
    protected Polygon getBuildingNearCentre(String circlePoints, String centre)
    {
        String query = "SELECT B.GEO FROM building B  WHERE \n" +
                "SDO_NN(B.GEO, mdsys.sdo_geometry(2001, null, " +
                "mdsys.sdo_point_type("+centre+"), NULL, NULL), 'sdo_num_res=1') = 'TRUE' " +
                "AND B.BUILDINGCODE IN (select B.BUILDINGCODE from building B where MDSYS.SDO_RELATE(B.GEO," +
                "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4),MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")), 'mask = INSIDE') = 'TRUE')";
        List<ArrayList<Integer>> geo =   queryBuildingTable(query);
        System.out.println(geo);
        Polygon polygon = null;
        if(geo.size() > 0)
        {
            for (int i = 0; i < geo.size(); i++)
            {
                int[] xPoly = separatePolyCoordinates(geo.get(i), 0);
                int[] yPoly = separatePolyCoordinates(geo.get(i), 1);
                polygon = new Polygon(xPoly, yPoly, xPoly.length);
            }
        }
        databaseQuery = " Query: "+query;
        return polygon;
    }


    /*
    Returns Photo coordinates nearest to centre query#3
     */
    protected List<Integer> getPhotoNearCentre(String circlePoints,String centre)
    {
        String query = "SELECT P.PHOTOCOORDINATES FROM photo P  WHERE \n" +
                "SDO_NN(P.PHOTOCOORDINATES, mdsys.sdo_geometry(2001, null, " +
                "mdsys.sdo_point_type("+centre+"), NULL, NULL), 'sdo_num_res=1') = 'TRUE' " +
                "AND " +
                "P.PHOTOID IN (select P.PHOTOID from photo P where MDSYS.SDO_RELATE(P.PHOTOCOORDINATES," +
                "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4),MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")), 'mask = INSIDE') = 'TRUE')";

        List<Integer> photoNearCentre = new ArrayList<Integer>();
        if(queryPhotoTable(query).size() > 0)
        {
        int x = queryPhotoTable(query).get(0).get(0);
        int y = queryPhotoTable(query).get(0).get(1);
        photoNearCentre.add(x);
        photoNearCentre.add(y);
        }
        databaseQuery = " Query: "+query;
        return photoNearCentre;
    }


    /*
    Returns Photographer location nearest to centre query#3
     */
    protected List<Integer> getPhotographerNearCentre(String circlePoints, String centre)
    {
        String query = "SELECT Ph.PHOTOGRAPHERLOC FROM photographer Ph  WHERE \n" +
                "SDO_NN(Ph.photographerloc, mdsys.sdo_geometry(2001, null, " +
                "mdsys.sdo_point_type("+centre+"), NULL, NULL), 'sdo_num_res=1') = 'TRUE' AND " +
        "Ph.PHOTOGRAPHERID IN " +
                "(select Ph.PHOTOGRAPHERID from photographer Ph where MDSYS.SDO_RELATE(Ph.PHOTOGRAPHERLOC," +
                "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,4),MDSYS.SDO_ORDINATE_ARRAY("+circlePoints+")), 'mask = INSIDE') = 'TRUE')";


        List<Integer> photographerNearCentre = new ArrayList<Integer>();
        if(queryPhotographerTable(query).size() > 0)
        {
        int x = queryPhotographerTable(query).get(0).get(0);
        int y = queryPhotographerTable(query).get(0).get(1);
        photographerNearCentre.add(x);
        photographerNearCentre.add(y);
        }
        databaseQuery = " Query: "+ query;
        return photographerNearCentre;
    }

    /*
    Returns the location point of the nearest photographer query #4
     */
    protected Point getPhotographerNearPoint(String point)
    {
        List<ArrayList<Integer>> photographerLoc ;
        String query = "(SELECT Ph.PHOTOGRAPHERLOC FROM photographer Ph  WHERE \n" +
                "SDO_NN(Ph.photographerloc, mdsys.sdo_geometry(2001, null, " +
                "mdsys.sdo_point_type("+point+"), NULL, NULL), 'sdo_num_res=1') = 'TRUE')";

        photographerLoc = queryPhotographerTable(query);
        int x = photographerLoc.get(0).get(0);
        int y = photographerLoc.get(0).get(1);
        databaseQuery = " Query: "+query;
        return (new Point(x,y));
    }


    /*
    Returns the photos inside polygon taken by the selected photographer query#4
     */
    protected List<ArrayList<Integer>> getPhotosInPolygonForPhotographer(String polygonPoints, String photographerLocation)
    {
        String query = "SELECT P.PHOTOCOORDINATES from photo P WHERE MDSYS.SDO_INSIDE(P.PHOTOCOORDINATES," +
                "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                "MDSYS.SDO_ORDINATE_ARRAY("+polygonPoints+"))) = 'TRUE' AND " +
                "P.PHOTOGRAPHERID = (select Ph.PHOTOGRAPHERID from Photographer Ph where MDSYS.SDO_EQUAL(Ph.PHOTOGRAPHERLOC,\n" +
                "mdsys.sdo_geometry(2001, null,mdsys.sdo_point_type("+photographerLocation+"),NULL, NULL)) = 'TRUE')";
        databaseQuery = " Query: "+ query;
        return (queryPhotoTable(query));

    }


    /*
    Returns the coordinates of the RedBuilding query#5
     */

    protected Polygon getRedBuildingCoordinates(String point)
    {
        String query = "select B.GEO from building B where MDSYS.SDO_CONTAINS(B.GEO," +
                "mdsys.sdo_geometry(2001, null,mdsys.sdo_point_type("+point+"), NULL, NULL)) = 'TRUE'";

        List<ArrayList<Integer>> buildingGeo = queryBuildingTable(query);
        Polygon polygon = null;

        for (int i = 0; i < buildingGeo.size(); i++)
        {
            int[] xPoly = separatePolyCoordinates(buildingGeo.get(i), 0);
            int[] yPoly = separatePolyCoordinates(buildingGeo.get(i), 1);
            polygon = new Polygon(xPoly, yPoly, xPoly.length);
        }
        databaseQuery = " Query: "+ query;
        return polygon;
    }

    /*
    Returns the photos near Red Building query#5
     */
    protected List<ArrayList<Integer>> getRedPhotos(String xyRedBuilding)
    {
        String query = "SELECT P.PHOTOCOORDINATES from photo P WHERE MDSYS.SDO_WITHIN_DISTANCE(P.PHOTOCOORDINATES," +
                "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                "MDSYS.SDO_ORDINATE_ARRAY("+xyRedBuilding+")),'distance = 80') = 'TRUE'";

        databaseQuery = " Query: "+ query;
        return (queryPhotoTable(query));
    }

    /*
    Returns photographers near Red Building query#5
     */
    protected List<ArrayList<Integer>> getRedPhotographers(String xyRedBuilding)
    {
        String query = "SELECT Ph.PHOTOGRAPHERLOC from photographer Ph WHERE MDSYS.SDO_WITHIN_DISTANCE(Ph.PHOTOGRAPHERLOC," +
                "MDSYS.SDO_GEOMETRY(2003,null,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                "MDSYS.SDO_ORDINATE_ARRAY("+xyRedBuilding+")),'distance = 50' ) = 'TRUE'";

        databaseQuery = " Query: "+query;
        return (queryPhotographerTable(query));
    }
}
