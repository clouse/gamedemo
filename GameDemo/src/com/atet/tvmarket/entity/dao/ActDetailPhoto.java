package com.atet.tvmarket.entity.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ACT_DETAIL_PHOTO.
 */
public class ActDetailPhoto implements java.io.Serializable {

    private Long id;
    private String picture;
    /** Not-null value. */
    private String activityId;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ActDetailPhoto() {
    }

    public ActDetailPhoto(Long id) {
        this.id = id;
    }

    public ActDetailPhoto(Long id, String picture, String activityId) {
        this.id = id;
        this.picture = picture;
        this.activityId = activityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    /** Not-null value. */
    public String getActivityId() {
        return activityId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
