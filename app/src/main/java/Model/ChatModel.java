package Model;

public class ChatModel {

    private String senderName;
    private String msg;
    private String time;

    public ChatModel(String senderName, String msg, String time) {
        this.senderName = senderName;
        this.msg = msg;
        this.time = time;
    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
