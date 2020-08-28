package com.ruoyi.wuliu.domain;

import com.ruoyi.common.annotation.Excel;

public class Demo {

    @Excel(name = "客户名称")
    private String name;

    @Excel(name = "手机号")
    private String phoneNum;

    @Excel(name = "微信号")
    private String vxNum;

    @Excel(name = "地址")
    private String address;

    @Excel(name = "备注")
    private String note;

    @Excel(name = "性别")
    private String sex;

}

