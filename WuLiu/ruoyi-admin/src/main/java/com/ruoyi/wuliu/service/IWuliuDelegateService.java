package com.ruoyi.wuliu.service;

import com.ruoyi.wuliu.domain.WuliuDelegate;
import java.util.List;

/**
 * 委派单关联调度单Service接口
 *
 * @author ruoyi
 * @date 2020-04-23
 */
public interface IWuliuDelegateService
{
    /**
     * 查询委派单关联调度单
     *
     * @param id 委派单关联调度单ID
     * @return 委派单关联调度单
     */
    public WuliuDelegate selectWuliuDelegateById(Long id);

    /**
     * 查询委派单关联调度单列表
     *
     * @param wuliuDelegate 委派单关联调度单
     * @return 委派单关联调度单集合
     */
    public List<WuliuDelegate> selectWuliuDelegateList(WuliuDelegate wuliuDelegate);

    /**
     * 新增委派单关联调度单
     *
     * @param wuliuDelegate 委派单关联调度单
     * @return 结果
     */
    public int insertWuliuDelegate(WuliuDelegate wuliuDelegate);

    /**
     * 修改委派单关联调度单
     *
     * @param wuliuDelegate 委派单关联调度单
     * @return 结果
     */
    public int updateWuliuDelegate(WuliuDelegate wuliuDelegate);

    /**
     * 批量删除委派单关联调度单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWuliuDelegateByIds(String ids);

    /**
     * 删除委派单关联调度单信息
     *
     * @param id 委派单关联调度单ID
     * @return 结果
     */
    public int deleteWuliuDelegateById(Long id);
}