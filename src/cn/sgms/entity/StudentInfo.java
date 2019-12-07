package cn.sgms.entity;


/**
 * 学生类(成员变量为表格中的属性)
 * @author lcy
 * @version 12-02
 */
public class StudentInfo {
    private String specialty;
    private String grade;
    private String studentNo;
    private String studentSex;
    private String studentName;
    private String subjectName;
    private float studentScore;

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }

    public void setStudentScore(float studentScore) {
        this.studentScore = studentScore;
    }
    public float getStudentScore() {
        return studentScore;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public String getSpecialty() {
        return specialty;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }
    public String getStudentSex() {
        return studentSex;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String toString() {
        return specialty + " " +  grade + " " + studentNo
                + " " + studentSex + " " + studentName + " " + subjectName + " " + studentScore;
    }
}
