import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class WholeRegionUI extends JLabel{

    List<Polygon> polyList;
    ImageIcon imageIcon;
    List<ArrayList<Integer>> allPhotoGeo;
    List<ArrayList<Integer>> allPhotographerGeo;
    List<Polygon> rangepolygon;



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
        g.setColor(Color.YELLOW);
        if(polyList.size() > 0)
        {
            for(Polygon poly:polyList)
            {g.drawPolygon(poly);}
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


    protected void setRangepolygon(List<Polygon> rangepolygon)
    {
        this.rangepolygon = rangepolygon;
    }

}
