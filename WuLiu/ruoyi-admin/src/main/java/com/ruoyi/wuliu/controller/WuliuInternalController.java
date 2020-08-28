package com.ruoyi.wuliu.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.ruoyi.wuliu.domain.Courier;
import com.ruoyi.wuliu.domain.WuliuTask;
import com.ruoyi.wuliu.service.ICourierService;
import com.ruoyi.wuliu.service.IWuliuTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wuliu.domain.WuliuInternal;
import com.ruoyi.wuliu.service.IWuliuInternalService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 内部订单（上下行）Controller
 * 
 * @author zheng
 * @date 2019-12-05
 */
@Controller
@RequestMapping("/wuliu/internal")
public class WuliuInternalController extends BaseController
{
    private String prefix = "wuliu/internal";

    @Autowired
private IWuliuInternalService wuliuInternalService;


    @Autowired
    private IWuliuTaskService wuliuTaskService;


    @Autowired
    private ICourierService courierService;

    @RequiresPermissions("wuliu:internal:view")
    @GetMapping()
    public String internal()
    {
        return prefix + "/internal";
    }

    /**
     * 查询内部订单（上下行）列表
     */
    @RequiresPermissions("wuliu:internal:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WuliuInternal wuliuInternal)
    {
        startPage();
        List<WuliuInternal> list = wuliuInternalService.selectWuliuInternalList(wuliuInternal);
        return getDataTable(list);
    }

    /**
     * 导出内部订单（上下行）列表
     */
    @RequiresPermissions("wuliu:internal:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WuliuInternal wuliuInternal)
    {
        List<WuliuInternal> list = wuliuInternalService.selectWuliuInternalList(wuliuInternal);
        ExcelUtil<WuliuInternal> util = new ExcelUtil<WuliuInternal>(WuliuInternal.class);
        return util.exportExcel(list, "internal");
    }

    /**
     * 新增内部订单（上下行）
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存内部订单（上下行）
     */
    @RequiresPermissions("wuliu:internal:add")
    @Log(title = "内部订单（上下行）", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WuliuInternal wuliuInternal)
    {
        return toAjax(wuliuInternalService.insertWuliuInternal(wuliuInternal));
    }

    /**
     * 修改内部订单（上下行）
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        WuliuInternal wuliuInternal = wuliuInternalService.selectWuliuInternalById(id);
        mmap.put("wuliuInternal", wuliuInternal);
        return prefix + "/edit";
    }

    /**
     * 修改保存内部订单（上下行）
     */
    @RequiresPermissions("wuliu:internal:edit")
    @Log(title = "内部订单（上下行）", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WuliuInternal wuliuInternal)
    {
        return toAjax(wuliuInternalService.updateWuliuInternal(wuliuInternal));
    }

    /**
     * 删除内部订单（上下行）
     */
    @RequiresPermissions("wuliu:internal:remove")
    @Log(title = "内部订单（上下行）", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(wuliuInternalService.deleteWuliuInternalByIds(ids));
    }

/*
*导入生成100条数据
* */

    @GetMapping("/demo")
    @ResponseBody
    public void randomDemo()  {
        Random random = new Random();
            Courier courier = new Courier();
            List<Courier> courierList = courierService.selectCourierList(courier);
        for (int i = 0; i <100 ; i++) {
            Courier courier1 = courierList.get(random.nextInt(courierList.size() - 1));
            WuliuInternal wuliuInternal = new WuliuInternal();
            wuliuInternal.setCratetime(new Date());
            wuliuInternal.setCouriercompany(courier1.getCouriercompany());
            wuliuInternal.setDowndress(courier1.getDowndress());
            WuliuTask task = new WuliuTask();
            List<WuliuTask> wuliuTasks = wuliuTaskService.selectWuliuTaskList(task);
            WuliuTask task1 = wuliuTasks.get(random.nextInt(wuliuTasks.size() - 1));
            wuliuInternal.setTasknum(task1.getTaskId());
            wuliuInternal.setIsDelete("1");
            wuliuInternal.setSheng(courier1.getSheng());
            wuliuInternal.setShi(courier1.getShi());
            wuliuInternal.setXian(courier.getXian());
            wuliuInternal.setDownName(courier1.getDownName());
            wuliuInternal.setDownPhone(courier1.getDownPhone());
            wuliuInternal.setCreatePhone(courier1.getCreatePhone());
            int randomNum = (int) ((Math.random() * 9 + 1) * 100000); // 随机数 6 位
            String s = ("D" + randomNum + "");
            wuliuInternal.setInternalNum(s);
            wuliuInternal.setDirection(courier1.getType());
            wuliuInternal.setVillagePointName(courier1.getVillageName());
            wuliuInternalService.insertWuliuInternal(wuliuInternal);
        }


    }
}
