package com.cgvsu.model;

import com.cgvsu.math.Point2f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

public class Triangle {
    public final Point2f p0, p1, p2;
    public final float z0, z1, z2;
    public final Vector3f n0, n1, n2;
    public final Vector2f tex0, tex1, tex2;

    public Triangle(Point2f p0, Point2f p1, Point2f p2,
                    float z0, float z1, float z2,
                    Vector3f n0, Vector3f n1, Vector3f n2,
                    Vector2f tex0, Vector2f tex1, Vector2f tex2) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.z0 = z0;
        this.z1 = z1;
        this.z2 = z2;
        this.n0 = n0;
        this.n1 = n1;
        this.n2 = n2;
        this.tex0 = tex0;
        this.tex1 = tex1;
        this.tex2 = tex2;
    }
}