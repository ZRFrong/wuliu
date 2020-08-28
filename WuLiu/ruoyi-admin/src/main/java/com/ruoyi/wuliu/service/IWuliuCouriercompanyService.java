package com.ruoyi.wuliu.service;

import com.ruoyi.wuliu.domain.WuliuCouriercompany;
import java.util.List;

/**
 * 快递公司Service接口
 *
 * @author ruoyi
 * @date 2020-04-24
 */
public interface IWuliuCouriercompanyService
{
    /**
     * 查询快递公司
     *
     * @param id 快递公司ID
     * @return 快递公司
     */
    public WuliuCouriercompany selectWuliuCouriercompanyById(Long id);

    /**
     * 查询快递公司列表
     *
     * @param wuliuCouriercompany 快递公司
     * @return 快递公司集合
     */
    public List<WuliuCouriercompany> selectWuliuCouriercompanyList(WuliuCouriercompany wuliuCouriercompany);

    /**
     * 新增快递公司
     *
     * @param wuliuCouriercompany 快递公司
     * @return 结果
     */
    public int insertWuliuCouriercompany(WuliuCouriercompany wuliuCouriercompany);

    /**
     * 修改快递公司
     *
     * @param wuliuCouriercompany 快递公司
     * @return 结果
     */
    public int updateWuliuCouriercompany(WuliuCouriercompany wuliuCouriercompany);

    /**
     * 批量删除快递公司
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWuliuCouriercompanyByIds(String ids);

    /**
     * 删除快递公司信息
     *
     * @param id 快递公司ID
     * @return 结果
     */
    public int deleteWuliuCouriercompanyById(Long id);
}