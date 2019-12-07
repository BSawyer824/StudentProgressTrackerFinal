package com.wgu_android.studentprogresstracker.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

/**********************************************************
Assessment Entity is the object class that will be used to hold all Assessment objects
 ********************************************************/


@Entity(tableName="assessment_table",
        indices = {@Index(value = {"assessment_id"}), @Index(value={"fk_course_id"}), @Index(value="fk_course_name", unique=true)},
        foreignKeys = {@ForeignKey(entity = CourseEntity.class, parentColumns = "course_id", childColumns = "fk_course_id"),
        @ForeignKey(entity = CourseEntity.class, parentColumns = "course_name", childColumns = "fk_course_name")})
public class AssessmentEntity {


    //*********************************************************
    //Variable Declarations

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="assessment_id")
    private int assessmentID;

    @ColumnInfo(name="assessment_name")
    private String assessmentName;

    @ColumnInfo(name="assessment_type")
    private String assessmentType;  //Objective or Performance

    @ColumnInfo(name="assessment_goal_date")
    private Date assessmentGoalDate;

    @ColumnInfo(name="assessment_due_date")
    private Date assessmentDueDate;

    @ColumnInfo(name="fk_course_id")
    private int fkCourseId;

    @ColumnInfo(name="fk_course_name")
    private String fkCourseName;

    //*********************************************************
    //Constructor
    @Ignore
    public AssessmentEntity(int assessmentID, String assessmentName, String assessmentType, Date assessmentGoalDate, Date assessmentDueDate, int fkCourseId, String fkCourseName) {
        this.assessmentID = assessmentID;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentDueDate = assessmentDueDate;
        this.fkCourseId = fkCourseId;
        this.fkCourseName = fkCourseName;
    }


    public AssessmentEntity(String assessmentName, String assessmentType, Date assessmentGoalDate, Date assessmentDueDate, int fkCourseId, String fkCourseName) {
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentDueDate = assessmentDueDate;
        this.fkCourseId = fkCourseId;
        this.fkCourseName = fkCourseName;
    }

    //*********************************************************
    //Accessors/Mutators
    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Date getAssessmentGoalDate() {
        return assessmentGoalDate;
    }

    public void setAssessmentGoalDate(Date assessmentGoalDate) {
        this.assessmentGoalDate = assessmentGoalDate;
    }

    public Date getAssessmentDueDate() {
        return assessmentDueDate;
    }

    public void setAssessmentDueDate(Date assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
    }

    public int getFkCourseId() {
        return fkCourseId;
    }

    public void setFkCourseId(int fkCourseId) {
        this.fkCourseId = fkCourseId;
    }

    public String getFkCourseName() {
        return fkCourseName;
    }

    public void setFkCourseName(String fkCourseName) {
        this.fkCourseName = fkCourseName;
    }

    @Override
    public String toString() {
        return "AssessmentEntity{" +
                "assessmentID=" + assessmentID +
                ", assessmentName='" + assessmentName + '\'' +
                ", assessmentType='" + assessmentType + '\'' +
                ", assessmentGoalDate=" + assessmentGoalDate +
                ", assessmentDueDate=" + assessmentDueDate +
                ", fkCourseId=" + fkCourseId +
                '}';
    }
}

