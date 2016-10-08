package com.derek.doraemon.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by derek on 10/6/16.
 */
public class Chat extends BaseModel {
    ChatUser from;
    ChatUser to;
    @SerializedName("chat")
    List<ChatInfo> chatInfoList;

    public ChatUser getFrom() {
        return from;
    }

    public void setFrom(ChatUser from) {
        this.from = from;
    }

    public ChatUser getTo() {
        return to;
    }

    public void setTo(ChatUser to) {
        this.to = to;
    }

    public List<ChatInfo> getChatInfoList() {
        return chatInfoList;
    }

    public void setChatInfoList(List<ChatInfo> chatInfoList) {
        this.chatInfoList = chatInfoList;
    }

    public static class ChatUser {
        @SerializedName("username")
        String userName;
        @SerializedName("avatar")
        String avatarUrl;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }

    public static class ChatInfo extends BaseModel {
        @SerializedName("msg_from")
        private int msgFrom;
        @SerializedName("msg_to")
        private int msgTo;
        private String content;
        @SerializedName("created_at")
        private String createdAt;
        private ChatUser chatUser;

        public int getMsgFrom() {
            return msgFrom;
        }

        public void setMsgFrom(int msgFrom) {
            this.msgFrom = msgFrom;
        }

        public int getMsgTo() {
            return msgTo;
        }

        public void setMsgTo(int msgTo) {
            this.msgTo = msgTo;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public ChatUser getChatUser() {
            return chatUser;
        }

        public void setChatUser(ChatUser chatUser) {
            this.chatUser = chatUser;
        }
    }
}
