package com.atet.tvmarket.entity.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table NOTICE_INFO.
 */
public class NoticeInfo implements java.io.Serializable {

    /** Not-null value. */
    private String noticeId;
    private String title;
    private String icon;
    private String content;
    private String url;
    private Integer noticeType;
    private Long startTime;
    private Long endTime;
    private Long createTime;

    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public NoticeInfo() {
    }

    public NoticeInfo(String noticeId) {
        this.noticeId = noticeId;
    }

    public NoticeInfo(String noticeId, String title, String icon, String content, String url, Integer noticeType, Long startTime, Long endTime, Long createTime) {
        this.noticeId = noticeId;
        this.title = title;
        this.icon = icon;
        this.content = content;
        this.url = url;
        this.noticeType = noticeType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
    }

    /** Not-null value. */
    public String getNoticeId() {
        return noticeId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    // KEEP METHODS - put your custom methods here
	@Override
	public String toString() {
		return "NoticeInfo [noticeId=" + noticeId + ", title=" + title
				+ ", icon=" + icon + ", content=" + content + ", url=" + url
				+ ", noticeType=" + noticeType + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", createTime=" + createTime + "]";
	}
    // KEEP METHODS END

}