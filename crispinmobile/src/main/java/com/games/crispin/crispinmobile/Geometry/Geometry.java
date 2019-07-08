package com.games.crispin.crispinmobile.Geometry;

import static java.lang.Math.sin;

/**
 * Created by Christian Benner on 11/08/2017.
 */

public class Geometry {
    public static class Cuboid {
        public final Point3D center;
        public float height;
        public float width;
        public float depth;

        public Cuboid(Point3D center, float width, float height, float depth)
        {
            this.center = center;
            this.width = width;
            this.height = height;
            this.depth = depth;
        }
    }

    public static class Circle {
        public final Point3D center;
        public final float radius;

        public Circle(Point3D center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point3D center;
        public final float radius;
        public final float height;

        public Cylinder(Point3D center, float radius, float height){
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }


    public static Vector3D vectorBetween(Point3D from, Point3D to) {
        return new Vector3D(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    public static class Ray {
        public final Point3D point3D;
        public final Vector3D vector;

        public Ray(Point3D point3D, Vector3D vector) {
            this.point3D = point3D;
            this.vector = vector;
        }
    }

    public static class Sphere {
        public final Point3D center;
        public final float radius;

        public Sphere(Point3D center, float radius)
        {
            this.center = center;
            this.radius = radius;
        }
    }

    public static boolean intersects(Sphere sphere, Ray ray)
    {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    public static float distanceBetween(Point3D point3D, Ray ray)
    {
        Vector3D p1ToPoint = vectorBetween(ray.point3D, point3D);
        Vector3D p2ToPoint = vectorBetween(ray.point3D.translate(ray.vector), point3D);


        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        // area of triangle = base * height so height = triangle / base
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }

    public static class Plane
    {
        public final Point3D point3D;
        public final Vector3D normal;

        public Plane(Point3D point3D, Vector3D normal)
        {
            this.point3D = point3D;
            this.normal = normal;
        }
    }

    public static Point3D intersectionPoint(Ray ray, Plane plane)
    {
        Vector3D rayToPlaneVector = vectorBetween(ray.point3D, plane.point3D);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
                / ray.vector.dotProduct(plane.normal);

        Point3D intersectionPoint3D = ray.point3D.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint3D;
    }
}