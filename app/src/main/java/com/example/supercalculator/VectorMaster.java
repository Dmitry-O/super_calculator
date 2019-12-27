package com.example.supercalculator;

import static java.lang.Math.*;

//класс для работы с вектоорами
public class VectorMaster {
    double x, y, z;

    //инициализаця переменных в конструкторе
    public VectorMaster(double a, double b, double c) {
        x = a;
        y = b;
        z = c;
    }

    //умножение на скаляр
    public VectorMaster constMultiply(double l) {
        return new VectorMaster(x*l, y*l, z*l);
    }

    //сложение векторов
    public VectorMaster adding(VectorMaster a, VectorMaster b) {
        return new VectorMaster(a.x+b.x, a.y+b.y, a.z+b.z);
    }

    //нахождение скаляра вектора
    public double getScalar() {
        return sqrt(pow(x, 2)+pow(y, 2)+pow(z, 2));
    }

    //скалярное умножение векторов
    public double scalarMultiply(VectorMaster a, VectorMaster b) {
        return (a.x*b.x+a.y*b.y+a.z*b.z);
    }

    //нахождение угла между векторами
    public double getAngle(VectorMaster a, VectorMaster b) {
        return acos(scalarMultiply(a, b)/(a.getScalar()*b.getScalar()));
    }

    //векторное умножение векторов
    public VectorMaster vectorMultiply(VectorMaster a, VectorMaster b) {
        return new VectorMaster(a.y*b.z-a.z*b.y, -(a.x*b.z-a.z*b.x), a.x*b.y-a.y*b.x);
    }

    //перевод вектора в строку
    public String toString() {
        return Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z);
    }

    //получение значений координат по X, y и Z

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}