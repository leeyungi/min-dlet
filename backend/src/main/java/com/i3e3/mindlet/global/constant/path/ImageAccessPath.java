package com.i3e3.mindlet.global.constant.path;

public enum ImageAccessPath {

    CONTENT_IMAGE_ACCESS_PATH("/static/files/images/content");

    private String data;

    ImageAccessPath(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
