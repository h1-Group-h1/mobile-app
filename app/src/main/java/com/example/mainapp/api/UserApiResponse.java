package com.example.mainapp.api;

public class UserApiResponse {

    private addUser data;

    public addUser getData(){return data;}
    public void setData(addUser data){this.data = data;}

    public String toString(){
        return "UserApiResponse [data=" + data + "]";
    }

}
