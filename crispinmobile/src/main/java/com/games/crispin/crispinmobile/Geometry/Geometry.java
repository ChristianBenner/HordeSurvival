package com.games.crispin.crispinmobile.Geometry;

/**
 * Geometry class provides some public static functions for useful calculations and operations based
 * on vectors and points.
 *
 * @author  Christian Benner
 * @version %I%, %G%
 * @see     Plane
 * @see     Ray
 * @see     Sphere
 * @see     Vector2D
 * @see     Vector3D
 * @see     Point2D
 * @see     Point3D
 * @since   1.0
 */
public class Geometry
{
    public static Vector3D getVectorBetween(Point3D from, Point3D to)
    {
        return new Vector3D(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    public static boolean intersects(Sphere sphere, Ray ray)
    {
        return getDistanceBetweens(sphere.center, ray) < sphere.radius;
    }

    public static float getDistanceBetweens(Point3D point3D, Ray ray)
    {
        Vector3D p1ToPoint = getVectorBetween(ray.position, point3D);
        Vector3D p2ToPoint = getVectorBetween(translate(ray.position, ray.direction), point3D);

        float areaOfTriangleTimesTwo = p1ToPoint.getCrossProduct(p2ToPoint).getLength();
        float lengthOfBase = ray.direction.getLength();

        // area of triangle = base * height so height = triangle / base
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }

    public static Point3D translate(Point3D point3D, Vector3D vector)
    {
        return new Point3D(
                point3D.x + vector.x,
                point3D.y + vector.y,
                point3D.z + vector.z);
    }

    /**
     * Scale a given direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x, y and z)
     * @since 1.0
     */
    public static Vector3D scaleVector(Vector3D vector, float scale)
    {
        return new Vector3D(
                vector.x * scale,
                vector.y * scale,
                vector.z * scale);
    }

    /**
     * Scale each dimension of a given direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @param z Scale multiplier for the z dimension
     * @since 1.0
     */
    public static Vector3D scaleVector(Vector3D vector,
                                float x,
                                float y,
                                float z)
    {
        return new Vector3D(
                vector.x * x,
                vector.y * y,
                vector.z * z);
    }

    public static Point3D getIntersectionPoint(Ray ray, Plane plane)
    {
        Vector3D rayToPlaneVector = getVectorBetween(ray.position, plane.position);

        float scaleFactor = rayToPlaneVector.getDotProduct(plane.direction)
                / ray.direction.getDotProduct(plane.direction);

        Point3D intersectionPoint3D = translate(ray.position, scaleVector(ray.direction, scaleFactor));
        return intersectionPoint3D;
    }
}