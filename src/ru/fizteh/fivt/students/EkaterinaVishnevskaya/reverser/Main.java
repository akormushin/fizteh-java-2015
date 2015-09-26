package ru.fizteh.fivt.students.EkaterinaVishnevskaya.reverser;

public class Main {

    public static void main(String[] args)
    {
        for (int i=args.length; i>=0; i--)
        {
            String[] result = args[i].split("\\s+");
            for (int j=result.length; j>=0; j--)
            {
                System.out.print(result[j]+' ');
            }
            System.out.println();
        }

    }
}
