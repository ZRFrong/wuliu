package com.ruoyi.wuliu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wuliu.domain.WuliuCouriercompany;
import com.ruoyi.wuliu.service.IWuliuCouriercompanyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 快递公司Controller
 *
 * @author ruoyi
 * @date 2020-04-24
 */
@Controller
@RequestMapping("/wuliu/couriercompany")
public class WuliuCouriercompanyController extends BaseController
{
    private String prefix = "wuliu/couriercompany";

    @Autowired
    private IWuliuCouriercompanyService wuliuCouriercompanyService;

    @RequiresPermissions("wuliu:couriercompany:view")
    @GetMapping()
    public String couriercompany()
    {
        return prefix + "/couriercompany";
    }

    /**
     * 查询快递公司列表
     */
    @RequiresPermissions("wuliu:couriercompany:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WuliuCouriercompany wuliuCouriercompany)
    {
        startPage();
        List<WuliuCouriercompany> list = wuliuCouriercompanyService.selectWuliuCouriercompanyList(wuliuCouriercompany);
        return getDataTable(list);
    }

    /**
     * 查询快递公司列表
     */
    @CrossOrigin
    @GetMapping("/dropdown")
    @ResponseBody
    public HashMap<String, ArrayList<HashMap<String, String>>> dropdown() {
        WuliuCouriercompany couriercompany = new WuliuCouriercompany();
        List<WuliuCouriercompany> list = wuliuCouriercompanyService.selectWuliuCouriercompanyList(couriercompany);
        HashMap<String, ArrayList<HashMap<String, String>>> stringArrayListHashMap = new HashMap<>();
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
        list.forEach(itm -> {
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("couier", itm.getCouriercompany());
            hashMaps.add(stringStringHashMap);
        });
        stringArrayListHashMap.put("name", hashMaps);
        return stringArrayListHashMap;
    }

    /**
     * 导出快递公司列表
     */
    @RequiresPermissions("wuliu:couriercompany:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WuliuCouriercompany wuliuCouriercompany)
    {
        List<WuliuCouriercompany> list = wuliuCouriercompanyService.selectWuliuCouriercompanyList(wuliuCouriercompany);
        ExcelUtil<WuliuCouriercompany> util = new ExcelUtil<WuliuCouriercompany>(WuliuCouriercompany.class);
        return util.exportExcel(list, "couriercompany");
    }

    /**
     * 新增快递公司
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存快递公司
     */
    @RequiresPermissions("wuliu:couriercompany:add")
    @Log(title = "快递公司", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WuliuCouriercompany wuliuCouriercompany)
    {
        return toAjax(wuliuCouriercompanyService.insertWuliuCouriercompany(wuliuCouriercompany));
    }

    /**
     * 修改快递公司
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        WuliuCouriercompany wuliuCouriercompany = wuliuCouriercompanyService.selectWuliuCouriercompanyById(id);
        mmap.put("wuliuCouriercompany", wuliuCouriercompany);
        return prefix + "/edit";
    }

    /**
     * 修改保存快递公司
     */
    @RequiresPermissions("wuliu:couriercompany:edit")
    @Log(title = "快递公司", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WuliuCouriercompany wuliuCouriercompany)
    {
        return toAjax(wuliuCouriercompanyService.updateWuliuCouriercompany(wuliuCouriercompany));
    }

    /**
     * 删除快递公司
     */
    @RequiresPermissions("wuliu:couriercompany:remove")
    @Log(title = "快递公司", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(wuliuCouriercompanyService.deleteWuliuCouriercompanyByIds(ids));
    }
}