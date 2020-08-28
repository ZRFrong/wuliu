package com.ruoyi.wuliu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 快递公司对象 wuliu_couriercompany
 *
 * @author ruoyi
 * @date 2020-04-24
 */
public class WuliuCouriercompany extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 最后修改人 */
    @Excel(name = "最后修改人")
    private String updatep;

    /** 快递公司名字 */
    @Excel(name = "快递公司名字")
    private String couriercompany;

    /** 主键 */
    private Long id;

    /** 负责人名字 */
    @Excel(name = "负责人名字")
    private String theName;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 负责人手机号 */
    @Excel(name = "负责人手机号")
    private String theNamePhone;

    public void setUpdatep(String updatep)
    {
        this.updatep = updatep;
    }

    public String getUpdatep()
    {
        return updatep;
    }
    public void setCouriercompany(String couriercompany)
    {
        this.couriercompany = couriercompany;
    }

    public String getCouriercompany()
    {
        return couriercompany;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTheName(String theName)
    {
        this.theName = theName;
    }

    public String getTheName()
    {
        return theName;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setTheNamePhone(String theNamePhone)
    {
        this.theNamePhone = theNamePhone;
    }

    public String getTheNamePhone()
    {
        return theNamePhone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("updateTime", getUpdateTime())
                .append("updatep", getUpdatep())
                .append("couriercompany", getCouriercompany())
                .append("id", getId())
                .append("theName", getTheName())
                .append("address", getAddress())
                .append("theNamePhone", getTheNamePhone())
                .toString();
    }
}