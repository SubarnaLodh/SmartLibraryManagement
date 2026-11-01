import java.io.Serializable;

public class Student implements Serializable {

    private String studentId;
    private String name;
    private String course;
    private String email;
    private String phone;

    // Constructor
    public Student(String studentId, String name,
                   String course, String email,
                   String phone) {

        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

