package cn.edu.swpu.cins.weike.entity.view;

import java.util.List;

/**
 * Created by muyi on 17-4-9.
 */
public class ProjectRecommend {

    //项目发布时进行的推荐人选

    //推荐人选姓名
    private String username;
    //推荐人选头像
    private String image;
    //推荐人选性别
    private String sex;
    //推荐人选所在大学
    private String university;
    //推荐人选专业和年级
    private String majorAndGrade;
    //推荐人选项目经验
    private String experience;
    //推荐人选拥有专业技能
    private List<String> skills;
    //推荐人选联系QQ
    private long qq;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajorAndGrade() {
        return majorAndGrade;
    }

    public void setMajorAndGrade(String majorAndGrade) {
        this.majorAndGrade = majorAndGrade;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public ProjectRecommend(String username, String image, String sex, String university, String majorAndGrade, String experience, List<String> skills, long qq) {
        this.username = username;
        this.image = image;
        this.sex = sex;
        this.university = university;
        this.majorAndGrade = majorAndGrade;
        this.experience = experience;

        this.skills = skills;
        this.qq = qq;
    }

    public long getQq() {
        return qq;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }

    public ProjectRecommend() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectRecommend)) return false;

        ProjectRecommend that = (ProjectRecommend) o;

        if (getQq() != that.getQq()) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (!getImage().equals(that.getImage())) return false;
        if (!getSex().equals(that.getSex())) return false;
        if (!getUniversity().equals(that.getUniversity())) return false;
        if (!getMajorAndGrade().equals(that.getMajorAndGrade())) return false;
        if (!getExperience().equals(that.getExperience())) return false;
        return getSkills().equals(that.getSkills());
    }

    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + getImage().hashCode();
        result = 31 * result + getSex().hashCode();
        result = 31 * result + getUniversity().hashCode();
        result = 31 * result + getMajorAndGrade().hashCode();
        result = 31 * result + getExperience().hashCode();
        result = 31 * result + getSkills().hashCode();
        result = 31 * result + (int) (getQq() ^ (getQq() >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ProjectRecommend{" +
                "username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", university='" + university + '\'' +
                ", majorAndGrade='" + majorAndGrade + '\'' +
                ", experience='" + experience + '\'' +
                ", skills=" + skills +
                ", qq=" + qq +
                '}';
    }
}
