package me.tud.critic;

public class Main {

    public static final ClassLoader CLASS_LOADER = Main.class.getClassLoader();

    public static void main(String[] args) throws Exception {
        new Main();
    }

    private Main() throws Exception {
        ScriptLoader.loadScript("test");
    }

}
