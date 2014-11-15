import javax.swing.*;

public class Map extends JLabel
{
    private static Map ourInstance = new Map();

    public static Map getInstance()
    {
        return ourInstance;
    }

    private Map()
    {

    }
}
