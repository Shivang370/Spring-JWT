package org.example.response;

import java.io.Serializable;

public class ResponseWO implements Serializable {

    private ResponseCode status;
    private String description;

    public ResponseCode getStatus() {
        return status;
    }

    public void setStatus(ResponseCode status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
