package com.example.mainapp.api;

public class UserApiResponse {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private String email;
    private String hashed_password;
    private int id;

    public String toString(){
        return "UserApiResponse [name= "+name+" email= "+email+" password= "+hashed_password+" id= " + Integer.toString(id)+" ]";
    }

}
