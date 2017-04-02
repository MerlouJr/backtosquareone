package com.example.dobit.recall;

/**
 * Represents an item in a Patient list
 */
public class Patient {


    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Item emailAdd
     */
    @com.google.gson.annotations.SerializedName("emailAddress")
    private String emailAdd;

    /**
     * Item name
     */
    @com.google.gson.annotations.SerializedName("name")
    private String name;

    /**
     * Item birthday
     */
    @com.google.gson.annotations.SerializedName("birthday")
    private String birthday;

    /**
     * Item mainContact
     */

    @com.google.gson.annotations.SerializedName("mainContact")
    private String mainContact;

    /**
     * Item alternativeContact
     */

    @com.google.gson.annotations.SerializedName("alternativeContact")
    private String altContact;


    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMainContact() {
        return mainContact;
    }

    public void setMainContact(String mainContact) {
        this.mainContact = mainContact;
    }

    public String getAltContact() {
        return altContact;
    }

    public void setAltContact(String altContact) {
        this.altContact = altContact;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }


    /**
     * Patient constructor
     */
    public Patient() {

    }

    /**
     * Initializes a new Patient
     *
     * @param emailAdd
     *            The item emailAddress
     * @param name
     *            The item name
     * @param birthday
     *            The item birthday
     * @param mainContact
     *            The item mainContact
     * @param altContact
     *            The item alternativeContact
     */

    public Patient(String mId, String emailAdd, String name, String birthday, String mainContact, String altContact) {
        this.mId = mId;
        this.emailAdd = emailAdd;
        this.name = name;
        this.birthday = birthday;
        this.mainContact = mainContact;
        this.altContact = altContact;
    }


}