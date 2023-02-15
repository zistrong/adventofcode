package com.zistrong.adventofcode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyFile implements Serializable {
    private final String name;
    private boolean directory = false;
    private int size;
    private final List<MyFile> children = new ArrayList<>();

    public MyFile(String name, boolean directory) {
        this.name = name;
        this.directory = directory;
    }

    public MyFile(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public void addChild(MyFile child) {
        if (this.children.stream().filter(item -> item.name.equals(child.name)).count() == 0) {
            this.children.add(child);
        }
    }

    public boolean isDirectory() {
        return directory;
    }

    public int getSize() {
        return directory ? this.children.stream().mapToInt(MyFile::getSize).sum() : size;
    }

    public List<MyFile> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }
}

