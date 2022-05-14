package org.example.model;

import javax.persistence.*;

@Entity
@Table(name = "otpdetails")
public class OtpDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String email;

    @Column
    private Integer otp;

    public OtpDetails(Integer id, String email, Integer otp) {
        this.id = id;
        this.email = email;
        this.otp = otp;
    }

    public OtpDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
