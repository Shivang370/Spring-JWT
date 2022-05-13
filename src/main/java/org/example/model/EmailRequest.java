package org.example.model;

public class EmailRequest {

    private String subject;
    private String message;
    private String to;

    public EmailRequest(String subject, String message, String to) {
        this.subject = subject;
        this.message = message;
        this.to = to;
    }

    public EmailRequest() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
