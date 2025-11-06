package utn.programacion2.TPintegrador.main;

import java.util.Scanner;

public class AppMenu implements Runnable {

    private boolean running;
    private final Scanner scanner;
    private final ServiceManager manager;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.manager = ServiceManager.getInstance();
        this.running = true;
    }

    @Override
    public void run() {

    }
}
