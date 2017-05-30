package it.com.imagewatcherdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2017/5/28.
 */

public class Image  implements Serializable{
    private int state;
    private String message;
    private int loadStart;
    private List<ImageBean> subjectList;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLoadStart() {
        return loadStart;
    }

    public void setLoadStart(int loadStart) {
        this.loadStart = loadStart;
    }

    public List<ImageBean> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<ImageBean> subjectList) {
        this.subjectList = subjectList;
    }

    public static class ImageBean implements Serializable{
        private String subjectCover;
        private int subjectId;
        private String subjectTitle;

        public String getSubjectCover() {
            return subjectCover;
        }

        public void setSubjectCover(String subjectCover) {
            this.subjectCover = subjectCover;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectTitle() {
            return subjectTitle;
        }

        public void setSubjectTitle(String subjectTitle) {
            this.subjectTitle = subjectTitle;
        }
    }
}
