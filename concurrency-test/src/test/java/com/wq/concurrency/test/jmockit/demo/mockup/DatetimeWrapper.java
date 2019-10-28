package com.wq.concurrency.test.jmockit.demo.mockup;

import java.time.LocalDateTime;

/**
 * @Author: wangqiang20995
 * @Date: 2019/10/28 16:04
 * @Description:
 **/
public class DatetimeWrapper {

    public static DatetimeWrapper newInstance(){
        DatetimeWrapper wrapper = new DatetimeWrapper();
        LocalDateTime localDateTime = LocalDateTime.now();
        wrapper.setUpdateDate(DateUtils.getDay());
        wrapper.setUpdateTime(DateUtils.getTime());
        wrapper.setCreateDate(DateUtils.getDay(localDateTime));
        wrapper.setCreateTime(DateUtils.getTime(localDateTime));
        return wrapper;
    }

    /**
     * 记录更新日期
     */
    private Integer updateDate;

    /**
     * 最后冻结更新时间
     */
    private Integer updateTime;

    private Integer createDate;

    private Integer createTime;

    @Override
    public String toString() {
        return "DatetimeWrapper{" +
                "updateDate=" + updateDate +
                ", updateTime=" + updateTime +
                ", createDate=" + createDate +
                ", createTime=" + createTime +
                '}';
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Integer updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}
