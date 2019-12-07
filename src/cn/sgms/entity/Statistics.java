package cn.sgms.entity;

import com.google.gson.JsonObject;

import java.util.List;

public class Statistics {
    private List<JsonObject> studentSum;
    private List<JsonObject> subjectAvg;

    public void setStudentSum(List<JsonObject> studentSum) {
        this.studentSum = studentSum;
    }
    public List<JsonObject> getStudentSum() {
        return studentSum;
    }

    public void setSubjectAvg(List<JsonObject> subjectAvg) {
        this.subjectAvg = subjectAvg;
    }
    public List<JsonObject> getSubjectAvg() {
        return subjectAvg;
    }
}
