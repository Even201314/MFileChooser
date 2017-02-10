package com.even.mfilechooser.model;

import android.graphics.drawable.Drawable;
import java.io.File;

/**
 * Item
 * Created by Even on 17/2/10.
 */

public class Item extends File implements Comparable<File> {

    private Drawable icon;

    public Item(String path, Drawable icon) {
        super(path);
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override public int compareTo(File pathname) {
        if (isDirectory() && pathname.isFile()) {
            return -1;
        }
        if (isFile() && pathname.isDirectory()) {
            return 1;
        }
        return 0;
    }
}
