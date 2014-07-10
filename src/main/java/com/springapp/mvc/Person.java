package com.springapp.mvc;

import org.springframework.stereotype.Component;

@Component
public class Person {

    private int id;
    private String name;
    private int age;
    private Sex sex;
    private String img;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public enum Sex
    {
        Male("Male"),
        Female("Female");

        private String name;

        Sex(String name)
        {
            this.name = name;
        }

        String getValue()
        {
            return name;
        }
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
