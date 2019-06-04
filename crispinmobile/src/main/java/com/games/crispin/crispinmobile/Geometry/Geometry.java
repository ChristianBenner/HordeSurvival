package com.games.crispin.crispinmobile.Geometry;

import static java.lang.Math.sin;

/**
 * Created by Christian Benner on 11/08/2017.
 */

public class Geometry {
   /* public static class Hitbox3D
    {
    }*/

    public static class Quaternion
    {
        private float[] rotationMatrix = new float[16];
        private Point3D rotation;
        public Quaternion(Point3D rotationAxis, Point3D rotationAngles)
        {
            this.rotation = new Point3D(rotationAxis.x * (float)sin(rotationAngles.x / 2.0f),
                    rotationAxis.y * (float)sin(rotationAngles.y / 2.0f),
                    rotationAxis.z * (float)sin(rotationAngles.z / 2.0f));
        }

 /*       // Returns a 16 float array
        float[] createRotation()
        {
            float[] m1 = {
            };
            float[] m2 = {
            };
            Matrix m;
            m = new Matrix();
            Matrix.
        }*/

        @Override
        public String toString()
        {
            return "Quat[x:" + rotation.x + ",y:" + rotation.y + ",z:" + rotation.z + "]";
        }
    }

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

    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length(){
            return (float)Math.sqrt(
                    x * x
                            + y * y
                            + z*z);
        }

        // http://en.wikipedia.org/wiki/Cross_product
        public Vector crossProduct(Vector other) {
            return new Vector(
                    (y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x));
        }

        public float dotProduct(Vector other)
        {
            return (x * other.x) + (y * other.y) + (z * other.z);
        }

        public Vector scale(float f)
        {
            return new Vector(x * f, y * f, z * f);
        }

        public Vector translateY(float y) { return new Vector(x, this.y + y, z); }

        @Override
        public String toString()
        {
            return "x: " + x + ", y: " + y + ", z: " + y;
        }
    }


    public static Vector vectorBetween(Point3D from, Point3D to) {
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    public static class Ray {
        public final Point3D point3D;
        public final Vector vector;

        public Ray(Point3D point3D, Vector vector) {
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
        Vector p1ToPoint = vectorBetween(ray.point3D, point3D);
        Vector p2ToPoint = vectorBetween(ray.point3D.translate(ray.vector), point3D);


        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        // area of triangle = base * height so height = triangle / base
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }

    public static class Plane
    {
        public final Point3D point3D;
        public final Vector normal;

        public Plane(Point3D point3D, Vector normal)
        {
            this.point3D = point3D;
            this.normal = normal;
        }
    }

    public static Point3D intersectionPoint(Ray ray, Plane plane)
    {
        Vector rayToPlaneVector = vectorBetween(ray.point3D, plane.point3D);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
                / ray.vector.dotProduct(plane.normal);

        Point3D intersectionPoint3D = ray.point3D.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint3D;
    }
}