package com.example.androidlabs;

public class Message {

    public String message;
    public boolean isSendOrReceieve;
    public long messageID;




    public Message(String message,boolean isSendOrReceive,long messageID){
        this.message = message;
        this.isSendOrReceieve = isSendOrReceive;
        this.messageID = messageID;

    }
    public Message(){ }

    public String getMessage(){return message;}
    public void setMessage(String message){this.message= message;}

    public boolean isSendOrReceieve(){
        return isSendOrReceieve;
    }
    public void setSendOrReceieve(boolean isSendOrReceieve){
        this.isSendOrReceieve = isSendOrReceieve;
    }

    public long getMessageID(){return messageID;}
    public void setMessageID(long messageID){this.messageID=messageID;}
}

