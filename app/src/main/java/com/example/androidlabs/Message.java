package com.example.androidlabs;

public class Message {

    public String message;
    public boolean isSendOrReceieve;




    public Message(String message,boolean isSendOrReceive){
        this.message = message;
        this.isSendOrReceieve = isSendOrReceive;
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
}

