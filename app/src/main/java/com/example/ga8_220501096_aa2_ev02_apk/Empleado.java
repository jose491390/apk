package com.example.ga8_220501096_aa2_ev02_apk;

import android.os.Parcel;
import android.os.Parcelable;

public class Empleado implements Parcelable {

    private int documento;
    private String nombre;
    private  String cargo;
    private String contrasenia;

    public Empleado() {
    }

    public Empleado(int documento, String nombre, String cargo, String contrasenia) {
        this.documento = documento;
        this.nombre = nombre;
        this.cargo = cargo;
        this.contrasenia = contrasenia;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public String toString() {
        return "EMPLEADO=" + nombre ;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.documento);
        dest.writeString(this.nombre);
        dest.writeString(this.cargo);
        dest.writeString(this.contrasenia);
    }

    public void readFromParcel(Parcel source) {
        this.documento = source.readInt();
        this.nombre = source.readString();
        this.cargo = source.readString();
        this.contrasenia = source.readString();
    }

    protected Empleado(Parcel in) {
        this.documento = in.readInt();
        this.nombre = in.readString();
        this.cargo = in.readString();
        this.contrasenia = in.readString();
    }

    public static final Parcelable.Creator<Empleado> CREATOR = new Parcelable.Creator<Empleado>() {
        @Override
        public Empleado createFromParcel(Parcel source) {
            return new Empleado(source);
        }

        @Override
        public Empleado[] newArray(int size) {
            return new Empleado[size];
        }
    };
}
