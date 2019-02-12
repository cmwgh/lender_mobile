package net.christopherwhite.lender.lender;

public class Student {
    // fields
    private int studentID;
    private String studentName;
    // constructors
    public Student() {}
    public Student(int id, String studentName) {
        this.studentID = id;
        this.studentName = studentName;
    }
    // properties
    public void setID(int id) {
        this.studentID = id;
    }
    public int getID() {
        return this.studentID;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentName() {
        return this.studentName;
    }
}
