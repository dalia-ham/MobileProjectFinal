package com.example.mobileprojectfinal;

public class Employee {
    private int employeeId;
    private String email;
    private double salary;
    private String position;
    private int managerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String profileImage;

    // Constructor
    public Employee(int employeeId, String email, double salary, String position, int managerId,
                    String firstName, String lastName, String phone, String profileImage) {
        this.employeeId = employeeId;
        this.email = email;
        this.salary = salary;
        this.position = position;
        this.managerId = managerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", position='" + position + '\'' +
                ", managerId=" + managerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
