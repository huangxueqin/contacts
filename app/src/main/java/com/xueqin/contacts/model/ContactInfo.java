package com.xueqin.contacts.model;

public class ContactInfo {

    private String mFirstName;
    private String mLastName;
    private String mTitle;
    private String mIntroduction;
    private String mAvatarFileName;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String introduction) {
        this.mIntroduction = introduction;
    }

    public String getAvatarFileName() {
        return mAvatarFileName;
    }

    public void setAvatarFileName(String avatarFileName) {
        this.mAvatarFileName = avatarFileName;
    }
}
