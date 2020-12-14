package com.phone.plantscollect;

public class listItem {

    String name, path;
    int resource;

    public listItem(String name, String path, int resource) {
        this.name = name;
        this.path = path;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
