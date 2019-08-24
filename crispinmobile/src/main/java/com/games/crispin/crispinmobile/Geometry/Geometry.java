package com.games.crispin.crispinmobile.Geometry;

public class Geometry {
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
        Vector3D p2ToPoint = vectorBetween(translate(ray.point3D, ray.vector), point3D);

        float areaOfTriangleTimesTwo = p1ToPoint.getCrossProduct(p2ToPoint).getLength();
        float lengthOfBase = ray.vector.getLength();

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

    public static Point3D translate(Point3D point3D, Vector3D vector)
    {
        return new Point3D(
                point3D.x + vector.x,
                point3D.y + vector.y,
                point3D.z + vector.z);
    }
    public static Vector3D scaleVector(Vector3D vector, float scale)
    {
        return new Vector3D(
                vector.x * scale,
                vector.y * scale,
                vector.z * scale);
    }

    public static Point3D intersectionPoint(Ray ray, Plane plane)
    {
        Vector3D rayToPlaneVector = vectorBetween(ray.point3D, plane.point3D);

        float scaleFactor = rayToPlaneVector.getDotProduct(plane.normal)
                / ray.vector.getDotProduct(plane.normal);

        Point3D intersectionPoint3D = translate(ray.point3D, scaleVector(ray.vector, scaleFactor));
        return intersectionPoint3D;
    }
}