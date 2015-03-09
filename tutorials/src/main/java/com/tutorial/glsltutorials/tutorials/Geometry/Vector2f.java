package com.tutorial.glsltutorials.tutorials.Geometry;

import android.util.FloatMath;

public class Vector2f {
    public float x, y;
    public Vector2f(){
        x = y = 0.f;
    }
    public Vector2f(float x, float y){
        this.x = x; this.y = y;
    }
    public Vector2f(Vector2f v){
        x = v.x; y = v.y;
    }
    public Vector2f(float v[]){
        x = v[0];
        y = v[1];
    }

    public float dot(Vector2f v){
        return x *v.x + y *v.y;
    }

	/*public Vector2f cross(Vector2f v){
		return new Vector2f(
			y*v._z - _z*v.y,
			x*v._z - _z*v.x,
			x*v.y - y*v._z
		);
	}*/

    /*** Return a float [3]
     *
     * @return an array of float with the components
     */
    public float [] getAsArray(){
        float array[] = new float[2];
        array[0] = x;
        array[1] = y;

        return array;
    }

    /*** Lenght of this vector */
    public float length(){
        return FloatMath.sqrt(x * x + y * y);
    }
    /*** Perform a normalization
     * If sqrt(len) of this vector is greater than an EPSILON value (0,0000001)
     * this methods perform a normalization of this vector.
     * Original vector is untouched, a new one is returned.
     * @return Returns a new normalized vector.
     */
    public Vector2f normalize(){
        float length =  length();
        if(length >= 0.0000001f){
            float inv = 1/length;
            return new Vector2f(x *inv, y *inv);
        }
        return new Vector2f(0.f, 0.f);
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }


    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }

    public void set(Vector2f v){
        x = v.x;
        y = v.y;
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }


    public Vector2f sub(Vector2f b){
        return new Vector2f(
                x - b.x,
                y - b.y
        );
    }
    public Vector2f add(Vector2f b){
        return new Vector2f(
                x + b.x,
                y + b.y
        );
    }

    public Vector2f mul(float f){
        return new Vector2f(
                x *f,
                y *f
        );
    }

    public Vector2f negate(){
        return new Vector2f(
                -x,
                -y
        );
    }

    public static Vector2f Midpoint(Vector2f p1, Vector2f p2)
    {
        return new Vector2f((p1.x + p2.x)/2, (p1.y + p2.y)/2);
    }

}