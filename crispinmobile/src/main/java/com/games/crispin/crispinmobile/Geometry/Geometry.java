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
    /**
     * Get the vector between two points.
     *
     * @param from  The first 3D point
     * @param to    The second 3D point
     * @return      A vector with a direction from the first point to the second.
     * @since 1.0
     */
    public static Vector3D getVectorBetween(Point3D from, Point3D to)
    {
        return new Vector3D(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    /**
     * Get the ray between two points
     *
     * @param from  The first 3D point (and also the origin of the ray)
     * @param to    The second 3D point
     * @return      A ray originating from the first point that is facing the second point
     * @since 1.0
     */
    public static Ray getRayBetween(Point3D from, Point3D to)
    {
        return new Ray(from,
                getVectorBetween(from, to));
    }

    /**
     * Check if a ray intersects a sphere. The method checks if the distance between a ray and a
     * spheres center point is less than the spheres radius.
     *
     * @param sphere    The virtual sphere
     * @param ray       The virtual ray
     * @return          True if the ray intersects the sphere, else false
     * @since 1.0
     */
    public static boolean intersects(Sphere sphere, Ray ray)
    {
        return getDistanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * Get the distance between a point and a ray
     *
     * @param point3D   The 3D point
     * @param ray       The virtual ray
     * @return          Distance between the given point and ray as a float
     * @since 1.0
     */
    public static float getDistanceBetween(Point3D point3D, Ray ray)
    {
        Vector3D p1ToPoint = getVectorBetween(ray.position, point3D);
        Vector3D p2ToPoint = getVectorBetween(translate(ray.position, ray.direction), point3D);

        float areaOfTriangleTimesTwo = p1ToPoint.getCrossProduct(p2ToPoint).getLength();
        float lengthOfBase = ray.direction.getLength();

        // area of triangle = base * height so height = triangle / base
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }

    /**
     * Translate a point by a vector
     *
     * @param point3D   The point to translate
     * @param vector    The vector to translate the point by
     * @return          The translated point
     * @since 1.0
     */
    public static Point3D translate(Point3D point3D, Vector3D vector)
    {
        return new Point3D(
                point3D.x + vector.x,
                point3D.y + vector.y,
                point3D.z + vector.z);
    }

    /**
     * Translate a point by floats
     *
     * @param point3D   The point to translate
     * @param x         Amount to translate the x co-ordinate
     * @param y         Amount to translate the y co-ordinate
     * @param z         Amount to translate the z co-ordinate
     * @return          The translated point
     * @since 1.0
     */
    public static Point3D translate(Point3D point3D,
                                    float x,
                                    float y,
                                    float z)
    {
        return new Point3D(
                point3D.x + x,
                point3D.y + y,
                point3D.z + z);
    }

    /**
     * Translate a 2D point by a vector
     *
     * @param point2D   The point to translate
     * @param vector    The vector to translate the point by
     * @return          The translated point
     * @since 1.0
     */
    public static Point2D translate(Point2D point2D, Vector2D vector)
    {
        return new Point2D(
                point2D.x + vector.x,
                point2D.y + vector.y);
    }

    /**
     * Translate a point by floats
     *
     * @param point2D   The point to translate
     * @param x         Amount to translate the x co-ordinate
     * @param y         Amount to translate the y co-ordinate
     * @return          The translated point
     * @since 1.0
     */
    public static Point2D translate(Point2D point2D,
                                    float x,
                                    float y)
    {
        return new Point2D(
                point2D.x + x,
                point2D.y + y);
    }

    /**
     * Scale a given direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x, y and z)
     * @return      The scaled vector
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
     * @return  The scaled vector
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

    /**
     * Get the intersection point between a ray and a plane
     *
     * @param ray   The virtual ray
     * @param ray   The virtual plane
     * @return      The point in which the ray intersects the plane
     * @since 1.0
     */
    public static Point3D getIntersectionPoint(Ray ray, Plane plane)
    {
        Vector3D rayToPlaneVector = getVectorBetween(ray.position, plane.position);

        float scaleFactor = rayToPlaneVector.getDotProduct(plane.direction)
                / ray.direction.getDotProduct(plane.direction);

        Point3D intersectionPoint3D = translate(ray.position,
                scaleVector(ray.direction, scaleFactor));
        return intersectionPoint3D;
    }
}