package goixeom.com.socket;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/19/2017.
 */

class AckSocket {
    @SerializedName("body")
    public Body body;
    @SerializedName("headers")
    public Headers headers;
    @SerializedName("code")
    public int statusCode;
    @SerializedName("message")
    public String msg;
    public static class Body {
        @SerializedName("message")
        public String message;
        @SerializedName("notificationUnRead")
        public int notificationUnRead;
        public String ready;
        public int status;

        public String getReady() {
            return this.ready;
        }

        public void setReady(String ready) {
            this.ready = ready;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getNotificationUnRead() {
            return this.notificationUnRead;
        }

        public void setNotificationUnRead(int notificationUnRead) {
            this.notificationUnRead = notificationUnRead;
        }
    }

    public static class Headers {
    }

    public Body getBody() {
        return this.body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Headers getHeaders() {
        return this.headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
