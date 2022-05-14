package org.example.model;

public class OtpRequest {

    private  Integer otp;

    public OtpRequest() {
    }

    public OtpRequest(Integer otp) {
        this.otp = otp;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
}
