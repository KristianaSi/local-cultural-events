package com.kristiana.presentation;


public class PrintColor {

    public static void printRed(String message) {
        System.out.println(ColorCodes.RED.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printBlue(String message) {
        System.out.println(ColorCodes.BLUE.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printGreen(String message) {
        System.out.println(ColorCodes.GREEN.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printPurple(String message) {
        System.out.println(ColorCodes.PURPLE.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printYellow(String message) {
        System.out.println(ColorCodes.YELLOW.getCode() + message + ColorCodes.RESET.getCode());
    }

    enum ColorCodes {
        RED("\u001B[31m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        GREEN("\u001B[32m"),
        PURPLE("\u001B[35m"),
        RESET("\u001B[0m");

        private final String code;

        ColorCodes(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
