package com.mad.snailmailversion1a;

public class SnailMail {

    private String mId;
    // package in User class later (may use the Google Account)
    private String mSenderName;
    private String mRecipientName;
    private String mTitle;
    private String mMessage;
    // package in Location class later
    private float mDestinationXCoordinate;
    private float mDestinationYCoordinate;
    private float mSourceXCoordinate;
    private float mSourceYCoordinate;
    // possibly use dateTime class later
    private String mSentTime;

    public SnailMail() {

    }

    public SnailMail(String senderName, String recipientName, String title, String message,
                     float destinationXCoordinate, float destinationYCoordinate,
                     float sourceXCoordinate, float sourceYCoordinate, String sentTime) {
        mSenderName = senderName;
        mRecipientName = recipientName;
        mTitle = title;
        mMessage= message;
        mDestinationXCoordinate = destinationXCoordinate;
        mDestinationYCoordinate = destinationYCoordinate;
        mSourceXCoordinate = sourceXCoordinate;
        mSourceYCoordinate = sourceYCoordinate;
        mSentTime = sentTime;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String senderName) {
        this.mSenderName = senderName;
    }

    public String getRecipientName() {
        return mRecipientName;
    }

    public void setRecipientName(String recipientName) {
        this.mRecipientName = recipientName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public float getDestinationXCoordinate() {
        return mDestinationXCoordinate;
    }

    public void setDestinationXCoordinate(float destinationXCoordinate) {
        this.mDestinationXCoordinate = destinationXCoordinate;
    }

    public float getDestinationYCoordinate() {
        return mDestinationYCoordinate;
    }

    public void setDestinationYCoordinate(float destinationYCoordinate) {
        this.mDestinationYCoordinate = destinationYCoordinate;
    }

    public float getSourceXCoordinate() {
        return mSourceXCoordinate;
    }

    public void setSourceXCoordinate(float sourceXCoordinate) {
        this.mSourceXCoordinate = sourceXCoordinate;
    }

    public float getSourceYCoordinate() {
        return mSourceYCoordinate;
    }

    public void setSourceYCoordinate(float sourceYCoordinate) {
        this.mSourceYCoordinate = sourceYCoordinate;
    }

    public String getSentTime() {
        return mSentTime;
    }

    public void setSentTime(String sentTime) {
        this.mSentTime = sentTime;
    }
}
