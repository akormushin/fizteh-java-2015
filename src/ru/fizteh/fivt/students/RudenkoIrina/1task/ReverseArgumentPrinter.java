import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

public class ReverseArgumentPrinter {
    public static void main(String[] args) {
        ArrayList <String> res = new ArrayList<>();
        for (int i = 0; i < args.length; ++i ){
            StringTokenizer splited = new StringTokenizer(args[i], " \\\t\"\n\r\f");
            while(splited.hasMoreTokens()){
                res.add(splited.nextToken());
            }
        }
        for (int i = res.size() - 1; i >= 0; i-- ){

            if (res.get(i).matches("\\d+")) {
                System.out.print(res.get(i) + ' ');
            }
        }
    }
}

