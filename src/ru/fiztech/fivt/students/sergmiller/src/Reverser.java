import java.util.ArrayList;
/**
 * Created by sergmiller on 12.09.15.
 */
public class Reverser {
    public static void main(String[] args)
    {
        ArrayList<String> parsedNums = new ArrayList<String>();
        /*
        for(int i = 0;i < args.length;++i)
            System.out.println(args);*/

        for(int i = 0;i < args.length;++i) {
            String[] parsedArgs = args[i].split("\\s");

            for (int j = 0; j < parsedArgs.length; ++j)
                parsedNums.add(parsedArgs[j]);
        }


        for(int i = parsedNums.size() - 1; i >= 0;--i) {
            System.out.print(parsedNums.get(i));
            System.out.print(" ");
        }

        System.out.println("");
    }
}
