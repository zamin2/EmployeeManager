package com.example.firebaseproject;

// Employee class with its getters, setters and constructors.
public class EmployeeClass {
    String name, role, email, imageURL;

    EmployeeClass(){

    }

    public EmployeeClass(String name, String role, String email, String imageURL) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
