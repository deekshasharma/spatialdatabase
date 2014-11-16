import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class WholeRegionUI extends JLabel{

    List<Polygon> polyList;
    ImageIcon imageIcon;
    List<ArrayList<Integer>> allPhotoGeo;
    List<ArrayList<Integer>> allPhotographerGeo;
    private static List<Polygon> rangePolygon;



    public WholeRegionUI(java.util.List<Polygon> polyList, java.util.List<ArrayList<Integer>> allPhotoGeo,
                         List<ArrayList<Integer>> allPhotographerGeo, ImageIcon imageIcon)
    {
        super(imageIcon,SwingConstants.LEFT);
        this.polyList = polyList;
        this.allPhotoGeo = allPhotoGeo;
        this.allPhotographerGeo = allPhotographerGeo;
        this.imageIcon = imageIcon;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(rangePolygon.size() > 0)
        {
            g.setColor(Color.RED);
            g.drawPolygon(rangePolygon.get(0));
        }
        if(polyList.size() > 0)
        {
            for(Polygon poly:polyList)

            {
                g.setColor(Color.YELLOW);
                g.drawPolygon(poly);
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
        WholeRegionUI.rangePolygon = rangePolygon;
    }

}
