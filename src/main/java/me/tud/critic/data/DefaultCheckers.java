package me.tud.critic.data;

import me.tud.critic.util.Checker;

public class DefaultCheckers {

    public static final Checker<String> operatorChecker = string ->
            string.equals("=") || string.equals("+") || string.equals("-")
                    || string.equals("*") || string.equals("/") || string.equals("%")
                    || string.equals("==") || string.equals(">") || string.equals("<")
                    || string.equals(">=") || string.equals("<=");

}
