package com.example;

import java.util.Random;

public class Joker {

    private String[] jokes;
    private Random random;

    public Joker() {
        jokes = new String[5];
        jokes[0] = "At what age do you think it's appropriate to tell a highway it's adopted?";
        jokes[1] = "I believe that if life gives you lemons, you should make lemonade... And try to find somebody whose life has given them vodka, and have a party.";
        jokes[2] = "Behind every great man is a woman rolling her eyes.";
        jokes[3] = "I found there was only one way to look thin: hang out with fat people.";
        jokes[4] = "Rodney Dangerfield I have a love interest in every one of my films: a gun.";
        random = new Random();
    }

    public String[] getJokes() {
        return jokes;
    }

    public String getRandomJoke() {
        return jokes[random.nextInt(jokes.length)];
    }

}
