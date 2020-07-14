package net.dev.action;

import java.text.*;
import java.util.*;

public class MetricsHandler {
    private static DecimalFormat doubleFormat = new DecimalFormat("#.#");
    private static int[] coutns = new int[]{0, 0, 0};

    private static Map<String, Integer> MENU_SIZE, MENU_ITEMS, INVENTORY_TYPES;


    public static void increase(int index) {
        if (coutns[index] < Integer.MAX_VALUE) {
            coutns[index] += 1;
        }
    }

    private static Map<String, Integer> getMenuSize() {
        return MENU_SIZE;
    }

    private static Map<String, Integer> getMenuItems() {
        return MENU_ITEMS;
    }

    private static Map<String, Integer> getInventoryTypes() {
        return INVENTORY_TYPES;
    }

}
