package com.tank.es.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ES微信内容实体类
 *
 * @author sezina
 * @author JinHuaTao 添加注释描述
 * @since 3/30/17
 */
public class Wechat {

    private String id;//           doc id

    @SerializedName("article_id")
    private String articleId;//    内容唯一id

    @SerializedName("wx_biz")
    private String wxBiz;//       账号id
    private String author;//        作者
    private String title;//        标题
    private String brief;//        概要
    private String content;//       内容

    @SerializedName("read_count")
    private Long readCount;//    阅读数

    @SerializedName("like_count")
    private Long likeCount;//    点赞数
    private String url;//       内容url

    @SerializedName("is_audio")
    private Boolean isAudio;//        是否音频

    @SerializedName("is_video")
    private Boolean isVideo ;//       是否视频

    @SerializedName("is_infraction")
    private Boolean isInfraction;// 是否违规

    @SerializedName("is_headline")
    private Boolean isHeadline;//  是否头条

    @SerializedName("is_original")
    private Boolean isOriginal;//  是否原创
    private List<String> keywords;//     已匹配关键词
    private Integer state;//    内容状态: 0.待处理, 1.取证中, 2.已分派 3.已取证, 4.处理中, 5.文书回退, 6.已处罚, 7.已处理, 8.待验证, 9.封堵失败, 10.封堵成功
    private List<Integer> infractions;//  违规条款

    @SerializedName("task_id")
    private Integer taskId;//    审看任务id

    @SerializedName("create_time")
    private Long createTime;//  创建时间

    @SerializedName("update_time")
    private Long crawlerTime;// 爬虫入库时间

    @SerializedName("update_time")
    private Long updateTime;//  更新时间

    @SerializedName("operator_id")
    private String operatorId;//操作人id

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getWxBiz() {
        return wxBiz;
    }

    public void setWxBiz(String wxBiz) {
        this.wxBiz = wxBiz;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<Integer> getInfractions() {
        return infractions;
    }

    public void setInfractions(List<Integer> infractions) {
        this.infractions = infractions;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCrawlerTime() {
        return crawlerTime;
    }

    public void setCrawlerTime(Long crawlerTime) {
        this.crawlerTime = crawlerTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsAudio() {
        return isAudio;
    }

    public void setIsAudio(Boolean isAudio) {
        this.isAudio = isAudio;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }

    public Boolean getIsInfraction() {
        return isInfraction;
    }

    public void setIsInfraction(Boolean isInfraction) {
        this.isInfraction = isInfraction;
    }

    public Boolean getIsHeadline() {
        return isHeadline;
    }

    public void setIsHeadline(Boolean isHeadline) {
        this.isHeadline = isHeadline;
    }

    public Boolean getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(Boolean isOriginal) {
        this.isOriginal = isOriginal;
    }

    @Override
    public String toString() {
        return "Wechat{" +
                "id='" + id + '\'' +
                ", article_id='" + articleId + '\'' +
                ", wxBiz='" + wxBiz + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", content='" + content + '\'' +
                ", readCount=" + readCount +
                ", likeCount=" + likeCount +
                ", url='" + url + '\'' +
                ", isAudio=" + isAudio +
                ", isVideo=" + isVideo +
                ", isInfraction=" + isInfraction +
                ", isHeadline=" + isHeadline +
                ", isOriginal=" + isOriginal +
                ", keywords=" + keywords +
                ", infractions=" + infractions +
                ", state=" + state +
                ", taskTd=" + taskId +
                ", createTime=" + createTime +
                ", crawlerTime=" + crawlerTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
