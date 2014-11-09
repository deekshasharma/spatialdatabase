import java.lang.reflect.Array;
import java.util.Arrays;

public class test {

    public static void main(String[] args) {

        String s = " p1,ABC,1,2,3,4,5,6";
        String[] temp = s.split(",", 3);
        System.out.println(temp[2]);

//        System.out.println(Arrays.toString(temp));
    }
}
