package com.cgvsu.render_engine;


public class RenderContext {


    private int width;
    private int height;
    private float[] zBuffer;
    private int[] colorBuffer;


    public RenderContext(int width, int height, float[] zBuffer, int[] colorBuffer) {

        this.width = width;
        this.height = height;
        this.zBuffer = zBuffer;
        this.colorBuffer = colorBuffer;
    }




    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float[] getZBuffer() {
        return zBuffer;
    }

    public int[] getColorBuffer() {
        return colorBuffer;
    }


    public void updateZBuffer(int x, int y, float z) {
        int index = y * width + x;
        if (z < zBuffer[index]) {
            zBuffer[index] = z;
        }
    }


    public void updateColorBuffer(int x, int y, int color) {
        int index = y * width + x;
        colorBuffer[index] = color;
    }

    public void setzBuffer(float[] zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void setColorBuffer(int[] colorBuffer) {
        this.colorBuffer = colorBuffer;
    }
}
