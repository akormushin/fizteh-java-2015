package ru.fizteh.fivt.students.mamaevads.twitterstream;


public class WordForms {
    enum Form{
        firstform ,secondform,thirdform
    }

    static Form getForm(long number){
        if (number==1||(number>20&&number%10==1&&number%100!=11))
            return Form.firstform;
        else if((number%100<10||number%100>20)&&number%10>=2 && number%10<=4)
            return Form.secondform;
        else return Form.thirdform;
    }

    static String hourForm(long number){
        Form thisform;
        thisform = getForm(number);
        switch (thisform){
            case firstform: return "час назад ";
            case secondform: return "часа назад ";
            case thirdform: return "часов назад ";
            default: return "hours ago";
        }
    }

    static String minutesForm(long number){
        Form thisform;
        thisform = getForm(number);
        switch (thisform){
            case firstform: return "минута назад ";
            case secondform: return "минуты назад ";
            case thirdform: return "минут назад ";
            default: return "minutes ago";
        }
    }

    static String daysForm(long number){
        Form thisform;
        thisform = getForm(number);
        switch (thisform){
            case firstform: return "день назад ";
            case secondform: return "дня назад ";
            case thirdform: return "дней назад ";
            default: return "days ago";
        }
    }
    static String retweetForm(long number){
        Form thisform;
        thisform = getForm(number);
        switch (thisform){
            case firstform: return " ретвит ";
            case secondform: return " ретвита ";
            case thirdform: return " ретвитов ";
            default: return "retweets";
        }
    }

}
