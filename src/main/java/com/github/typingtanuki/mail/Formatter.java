package com.github.typingtanuki.mail;

public final class Formatter {
    private Formatter() {
        super();
    }

    public static void finished() {
        finished("DONE");
    }

    public static void skipped() {
        finished("SKIPPED");
    }

    public static void finished(String s) {
        System.out.println(" " + s);
    }

    public static void progress(String s) {
        System.out.print(s);
        System.out.flush();
    }

    public static void progress() {
        progress(".");
    }
}
