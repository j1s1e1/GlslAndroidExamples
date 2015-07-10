package com.tutorial.glsltutorials.tutorials.Geometry;

import android.util.FloatMath;

/*** Simple 3-component vector class
 * We use this to store vertex info, specify object positions, etc.
 * @author oscarblasco
 */
public class Vector3f {

    // Global ShadingZen's axis vectors
    public static final Vector3f vectorRight = new Vector3f(1.f, 0.f, 0.f);
    public static final Vector3f vectorUp = new Vector3f(0.f, 1.f, 0.f);
    public static final Vector3f vectorFront = new Vector3f(0.f, 0.f, 1.f);
    public static final float[] vectorRightArray = {1.f, 0.f, 0.f, 0.f};
    public static final float[] vectorUpArray = {0.f, 1.f, 0.f, 0.f};
    public static final float[] vectorFrontArray = {0.f, 0.f, 1.f, 0.f};
    public static final Vector3f zero = new Vector3f();
    public static final Vector3f UnitX = new Vector3f(1f, 0f, 0f);
    public static final Vector3f UnitY = new Vector3f(0f, 1f, 0f);
    public static final Vector3f UnitZ = new Vector3f(0f, 0f, 1f);

    public float x, y, z;
    public Vector3f(){
        x = y = z = 0.f;
    }
    public Vector3f(float x, float y, float z){
        this.x = x; this.y = y; this.z = z;
    }
    public Vector3f(Vector3f v){
        x = v.x; y = v.y; z = v.z;
    }

    public Vector3f(Vector4f v){
        x = v.x; y = v.y; z = v.z;
    }

    public Vector3f(float v[]){
        x = v[0];
        y = v[1];
        z = v[2];
    }

    public Vector3f(float f){
        x = f;
        y = f;
        z = f;
    }

    public float dot(Vector3f v){
        return x *v.x + y *v.y + z *v.z;
    }

    public static float dot(Vector3f v1, Vector3f v2){
        return v1.x *v2.x + v1.y *v2.y + v1.z *v2.z;
    }

    public Vector3f cross(Vector3f v){
        return new Vector3f(
                y *v.z - z *v.y,
                z *v.x - x *v.z,
                x *v.y - y *v.x
        );
    }

    public void crossNoCopy(Vector3f v){

        float _x = y *v.z - z *v.y;
        float _y = z *v.x - x *v.z;
        z = x *v.y - y *v.x;
        x = _x;
        y = _y;

    }

    /*** Return a float [3]
     *
     * @return an array of float with the components
     */
    public float [] toArray(){
        float array[] = new float[3];
        array[0] = x;
        array[1] = y;
        array[2] = z;

        return array;
    }

    public float length(){
        return FloatMath.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize(){
        float length =  length();
        if(length >= 0.0000001f){
            float inv = 1/length;
            return new Vector3f(x *inv, y *inv, z *inv);
        }
        return new Vector3f(0.f, 0.f, 0.f);
    }

    public Vector3f divide(float divisor){
        return new Vector3f(x/divisor, y/divisor, z/divisor);
    }

    /***
     * Normalizes this vector without creating a new one
     */
    public float normalizeNoCopy(){
        float length =  length();
        if(length >= 0.0000001f){
            float inv = 1/length;
            x *= inv;
            y *= inv;
            z *= inv;
        } else {
            x = 0.f;
            y = 0.f;
            z = 0.f;
        }

        return length;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getZ(){
        return z;
    }
    public Vector2f getXY(){
        return new Vector2f(x, y);
    }

    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setZ(float z){
        this.z = z;
    }

    public void set(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void set(Vector3f v){
        setX(v.x);
        setY(v.y);
        setZ(v.z);
    }

    public void set(float [] vec){
        x = vec[0];
        y = vec[1];
        z = vec[2];
    }

    public Vector3f sub(Vector3f b){
        return new Vector3f(
                x - b.x,
                y - b.y,
                z - b.z
        );
    }
    public void subNoCopy(Vector3f b){
        x -= b.x;
        y -= b.y;
        z -= b.z;
    }

    public Vector3f add(Vector3f b){
        return new Vector3f(
                x + b.x,
                y + b.y,
                z + b.z
        );
    }

    public void addNoCopy(Vector3f b){
        x += b.x;
        y += b.y;
        z += b.z;
    }

    public Vector3f mul(float f){
        return new Vector3f(
                x *f,
                y *f,
                z *f
        );
    }

    public void mulInplace(float f){
        x *= f;
        y *= f;
        z *= f;
    }

    public Vector3f negate(){
        return new Vector3f(
                -x,
                -y,
                -z
        );
    }

    public void negateNoCopy(){
        x = -x;
        y = -y;
        z = -z;
    }

    public static Vector3f normalize(Vector3f vin)
    {
        float length = (float)Math.sqrt(Math.pow(vin.x, 2) + Math.pow(vin.y, 2) + Math.pow(vin.z, 2));
        vin.x = vin.x / length;
        vin.y = vin.y / length;
        vin.z = vin.z / length;
        return vin;
    }

    public static Vector3f multiply(Vector3f vector, float multiple)
    {
        return vector.mul(multiple);
    }

    public static Vector3f Transform(Vector3f vec, Matrix4f mat)
    {
        Vector3f v = new Vector3f();
        v.x = Vector3f.dot(vec, new Vector3f(mat.GetCol0()));
        v.y = Vector3f.dot(vec, new Vector3f(mat.GetCol1()));
        v.z = Vector3f.dot(vec, new Vector3f(mat.GetCol2()));
        return v;
    }

    public String toString()
    {
        return new String("X= " + String.format("%.3f" , x) + " Y= " + String.format("%.3f" , y) +
                " Z= " + String.format("%.3f" , z));
    }

    public Vector3f Clone()
    {
        return  new Vector3f(x, y, z);
    }

    public static float distance(Vector3f a, Vector3f b)
    {
        return (a.sub(b).length());
    }

    public float [] getAsArray(){
        float array[] = new float[3];
        array[0] = x;
        array[1] = y;
        array[2] = z;

        return array;
    }

    public float[] getFloats()
    {
        return getAsArray();
    }
}