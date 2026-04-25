package org.cyberagent;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Connecting to the server...");

        while (true) {
            printStatus();
            Thread.sleep(2000); //wait up for 2 secs
        }
    }

    public static void printStatus() {
        System.out.println("[Project Edge] Verification completed!");
    }
}