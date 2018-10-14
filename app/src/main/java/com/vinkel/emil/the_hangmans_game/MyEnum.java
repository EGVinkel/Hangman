package com.vinkel.emil.the_hangmans_game;

public enum MyEnum {
    EASY("Easy",5),NORMAL("Normal", 8),HARD("Hard", 20), DEFAULT("Default", 1), STARWARS("Star Wars", 2), HARRYPOTTER("Harry Potter", 3),FOOD("Food", 4);
        private String stringvalue;
        private int intvalue;
        MyEnum(String s, int i) {
            stringvalue=s;
            intvalue=i;
        }
        @Override
        public String toString(){
            return stringvalue ;
        }

    public int getIntvalue() {
        return intvalue;
    }
}

