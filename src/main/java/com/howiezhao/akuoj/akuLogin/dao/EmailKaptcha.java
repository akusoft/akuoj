package com.howiezhao.akuoj.akuLogin.dao;

import java.util.Date;

public class EmailKaptcha {

  private int id;
  private String email;
  private String kaptcha;
  private int status;
  private Date expired;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getKaptcha() {
    return kaptcha;
  }

  public void setKaptcha(String kaptcha) {
    this.kaptcha = kaptcha;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getExpired() {
    return expired;
  }

  public void setExpired(Date expired) {
    this.expired = expired;
  }



}
