package com.ruoyi.wuliu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.wuliu.mapper.WuliuCouriercompanyMapper;
import com.ruoyi.wuliu.domain.WuliuCouriercompany;
import com.ruoyi.wuliu.service.IWuliuCouriercompanyService;
import com.ruoyi.common.core.text.Convert;

/**
 * 快递公司Service业务层处理
 *
 * @author ruoyi
 * @date 2020-04-24
 */
@Service
public class WuliuCouriercompanyServiceImpl implements IWuliuCouriercompanyService
{
    @Autowired
    private WuliuCouriercompanyMapper wuliuCouriercompanyMapper;

    /**
     * 查询快递公司
     *
     * @param id 快递公司ID
     * @return 快递公司
     */
    @Override
    public WuliuCouriercompany selectWuliuCouriercompanyById(Long id)
    {
        return wuliuCouriercompanyMapper.selectWuliuCouriercompanyById(id);
    }

    /**
     * 查询快递公司列表
     *
     * @param wuliuCouriercompany 快递公司
     * @return 快递公司
     */
    @Override
    public List<WuliuCouriercompany> selectWuliuCouriercompanyList(WuliuCouriercompany wuliuCouriercompany)
    {
        return wuliuCouriercompanyMapper.selectWuliuCouriercompanyList(wuliuCouriercompany);
    }

    /**
     * 新增快递公司
     *
     * @param wuliuCouriercompany 快递公司
     * @return 结果
     */
    @Override
    public int insertWuliuCouriercompany(WuliuCouriercompany wuliuCouriercompany)
    {
        return wuliuCouriercompanyMapper.insertWuliuCouriercompany(wuliuCouriercompany);
    }

    /**
     * 修改快递公司
     *
     * @param wuliuCouriercompany 快递公司
     * @return 结果
     */
    @Override
    public int updateWuliuCouriercompany(WuliuCouriercompany wuliuCouriercompany)
    {
        return wuliuCouriercompanyMapper.updateWuliuCouriercompany(wuliuCouriercompany);
    }

    /**
     * 删除快递公司对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWuliuCouriercompanyByIds(String ids)
    {
        return wuliuCouriercompanyMapper.deleteWuliuCouriercompanyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除快递公司信息
     *
     * @param id 快递公司ID
     * @return 结果
     */
    @Override
    public int deleteWuliuCouriercompanyById(Long id)
    {
        return wuliuCouriercompanyMapper.deleteWuliuCouriercompanyById(id);
    }
}