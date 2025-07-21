package com.example.it404.LoginAndRegister;

public class StudentModel {

    private String Name;
    private String StudentID;
    private String Email;
    private String Contact;
    private String Type;


    public StudentModel() {
    }


    public StudentModel(String name, String studentID, String email, String contact, String type) {
        Name = name;
        StudentID = studentID;
        Email = email;
        Contact = contact;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
