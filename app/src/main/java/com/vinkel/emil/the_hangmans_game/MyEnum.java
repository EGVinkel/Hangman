package com.vinkel.emil.the_hangmans_game;

public enum MyEnum {
    EASY("Easy"), NORMAL("Normal"), HARD("Hard"), DEFAULT("Default"), STARWARS("Star Wars"), HARRYPOTTER("Harry Potter"), FOOD("Food"), WordsDR("Words from DR"), MyWord("Type your word");
    private String stringvalue;

    MyEnum(String s) {
        stringvalue = s;
    }

    @Override
    public String toString() {
        return stringvalue;
    }
}

