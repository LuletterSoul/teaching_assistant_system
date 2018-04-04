package com.easyclass.claraliu.core.application;

import android.app.Application;

import java.util.List;

import edu.vero.easyclass.domain.Attendance;
import edu.vero.easyclass.domain.ClassSchedule;
import edu.vero.easyclass.domain.TeacherArrangement;

/**
 * Created by Clara liu on 2017/12/14.
 */

public class MyApplication extends Application {
    private int userId;     //用户Id，登录时获取到，用于唯一标识一个用户
    private String baseURL;     //服务器基地址
    private List<ClassSchedule> studentClassSchedules;      //登录后获取到的该学生的全部课程（课表）
    private int studentOnclickCourseId;     //学生在主界面点击的某门课的课程号，唯一标识该门课
    //修改：增加了studentId以及对应的getters和setters
    private String studentId;
    private int arrangementId;

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    private Attendance attendance;
    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    private List<TeacherArrangement> teacherArrangements;
private int attendanceId;
    public void onCreate() {
        super.onCreate();
        setBaseURL("http://47.95.113.210:8080");
//        setBaseURL("http://10.0.2.2:8080");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public List<ClassSchedule> getStudentClassSchedules() {
        return studentClassSchedules;
    }

    public void setStudentClassSchedules(List<ClassSchedule> studentClassSchedules) {
        this.studentClassSchedules = studentClassSchedules;
    }

    public int getStudentOnclickCourseId() {
        return studentOnclickCourseId;
    }

    public void setStudentOnclickCourseId(int studentOnclickCourseId) {
        this.studentOnclickCourseId = studentOnclickCourseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(int arrangementId) {
        this.arrangementId = arrangementId;
    }

    public List<TeacherArrangement> getTeacherArrangements() {
        return teacherArrangements;
    }

    public void setTeacherArrangements(List<TeacherArrangement> teacherArrangements) {
        this.teacherArrangements = teacherArrangements;
    }
}
