package com.ruoyi.web.controller.login;


import lombok.Data;

@Data
public class userSyn {

private  Long id;
private  int sex;
private  int birthday;
private  int  last_login_time;
private  int create_time;
private  int user_status;
private  String user_login;
private  String user_pass ;
private  String user_nickname;
private  String user_email;
private  String last_login_ip;
private  String mobile;

}
