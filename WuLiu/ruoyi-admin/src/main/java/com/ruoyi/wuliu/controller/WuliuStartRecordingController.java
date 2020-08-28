package com.ruoyi.wuliu.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.cundian.domain.VillagePoint;
import com.ruoyi.cundian.service.IVillagePointService;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.wuliu.domain.WuliuDriver;
import com.ruoyi.wuliu.domain.WuliuSrartRecordingVo;
import com.ruoyi.wuliu.domain.WuliuVehicleRoute;
import com.ruoyi.wuliu.service.IWuliuDriverService;
import com.ruoyi.wuliu.service.IWuliuVehicleRouteService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wuliu.domain.WuliuStartRecording;
import com.ruoyi.wuliu.service.IWuliuStartRecordingService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;


/**
 * 发车记录Controller
 *
 * @author zheng
 * @date 2019-12-23
 */
@Controller
@RequestMapping("/wuliu/recording")
public class WuliuStartRecordingController extends BaseController {
    private String prefix = "wuliu/recording";

    @Autowired
    private IWuliuStartRecordingService wuliuStartRecordingService;


    @Autowired
    private IWuliuDriverService wuliuDriverService;

    @Autowired
    private IWuliuVehicleRouteService wuliuVehicleRouteService;

    @Autowired
    private IVillagePointService pointService;


    @Autowired
    private ISysDictDataService dictDataService;


    @RequiresPermissions("wuliu:recording:view")
    @GetMapping()
    public String recording() {
        return prefix + "/recording";
    }

    /**
     * 查询发车记录列表
     */
    @RequiresPermissions("wuliu:recording:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WuliuStartRecording wuliuStartRecording) {
        startPage();
        List<WuliuStartRecording> list = wuliuStartRecordingService.selectWuliuStartRecordingList(wuliuStartRecording);
        return getDataTable(list);
    }

    /**
     * 导出发车记录列表
     */
    @RequiresPermissions("wuliu:recording:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WuliuStartRecording wuliuStartRecording) {
        List<WuliuStartRecording> list = wuliuStartRecordingService.selectWuliuStartRecordingList(wuliuStartRecording);
        ExcelUtil<WuliuStartRecording> util = new ExcelUtil<WuliuStartRecording>(WuliuStartRecording.class);
        return util.exportExcel(list, "recording");
    }

    /**
     * 新增发车记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存发车记录
     */
    @RequiresPermissions("wuliu:recording:add")
    @Log(title = "发车记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WuliuStartRecording wuliuStartRecording) {
        return toAjax(wuliuStartRecordingService.insertWuliuStartRecording(wuliuStartRecording));
    }

    /**
     * 修改发车记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        WuliuStartRecording wuliuStartRecording = wuliuStartRecordingService.selectWuliuStartRecordingById(id);
        mmap.put("wuliuStartRecording", wuliuStartRecording);
        return prefix + "/edit";
    }

    /**
     * 修改保存发车记录
     */
    @RequiresPermissions("wuliu:recording:edit")
    @Log(title = "发车记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WuliuStartRecording wuliuStartRecording) {
        return toAjax(wuliuStartRecordingService.updateWuliuStartRecording(wuliuStartRecording));
    }

    /**
     * 删除发车记录
     */
    @RequiresPermissions("wuliu:recording:remove")
    @Log(title = "发车记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(wuliuStartRecordingService.deleteWuliuStartRecordingByIds(ids));
    }

    /*
     *根据id查询查询路径，根据路径获取途径点的经纬度
     * */

    /**
     * 发车路径
     */
    @GetMapping("/path")
    @ResponseBody
    public List<WuliuSrartRecordingVo> path(Long id) {
        WuliuStartRecording wuliuStartRecording = wuliuStartRecordingService.selectWuliuStartRecordingById(id);
        String route = wuliuStartRecording.getRoute();
        List<WuliuSrartRecordingVo> wuliuSrartRecordingVos = new ArrayList<>();
        WuliuVehicleRoute wuliuVehicleRoute = new WuliuVehicleRoute();
        wuliuVehicleRoute.setSerialNumber(route);
        List<WuliuVehicleRoute> wuliuVehicleRoutes = wuliuVehicleRouteService.selectWuliuVehicleRouteList(wuliuVehicleRoute);
        for (String s : wuliuVehicleRoutes.get(0).getPath().split("-")) {
//获取到每个途经点的服务站名
            VillagePoint villagePoint = new VillagePoint();
            villagePoint.setVillageName(s);
            List<VillagePoint> list = pointService.selectVillagePointList(villagePoint);
            VillagePoint villagePoint1 = list.get(0);
            WuliuSrartRecordingVo wuliuSrartRecordingVo = new WuliuSrartRecordingVo();
            wuliuSrartRecordingVo.setLongitude(villagePoint1.getLongitude());
            wuliuSrartRecordingVo.setLatitude(villagePoint1.getLatitude());
            wuliuSrartRecordingVos.add(wuliuSrartRecordingVo);
        }
        return wuliuSrartRecordingVos;
    }


    /**
     * 发车路径
     */
    @GetMapping("/demo")
    @ResponseBody
    public void demo() throws Exception {
        String beginDate = "2019-11-01";//开始时间
        String endDate = "2020-05-06";//结束时间

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(beginDate));

        for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
            String format = sdf.format(d);
            String format1 = format + " 08:00:00";
            String format2 = format + " 18:00:00";
            Date dateStart = DateUtils.parseDate(format1);
            Date dateEnd = DateUtils.parseDate(format2);
            storage1(dateStart, dateEnd);
        }
    }


    /**
     * 定时保存发车记录
     */
    public void storage1(Date dateStart, Date dateEnd) {
        List<WuliuDriver> wuliuDrivers1 = random();
        for (int i = 0; i < 4; i++) {
            WuliuStartRecording wuliuStartRecording = new WuliuStartRecording();

//        添加随机路线
            WuliuVehicleRoute wuliuVehicleRoute = new WuliuVehicleRoute();
            List<WuliuVehicleRoute> wuliuVehicleRoutes = wuliuVehicleRouteService.selectWuliuVehicleRouteList(wuliuVehicleRoute);
            WuliuVehicleRoute wuliuVehicleRoute1 = wuliuVehicleRoutes.get(i);
            wuliuStartRecording.setRoute(wuliuVehicleRoute1.getSerialNumber());
//        添加随机车牌号
            String drivernum = wuliuDrivers1.get(i).getVehicleNum();
            wuliuStartRecording.setDriveNum(drivernum);
//添加司机
            String drrivername = wuliuDrivers1.get(i).getName();
            wuliuStartRecording.setDirectorName(drrivername);
//        添加发起时间
            Date date = randomDate(dateStart, getNewDate(dateStart));

            wuliuStartRecording.setDepartureTime(date);
//        添加结束时间
            Date date1 = randomDate(dateEnd, getNewDate(dateEnd));
            wuliuStartRecording.setReturnCarTime(date1);
//添加状态
            wuliuStartRecording.setDrivingStart("正常");

            toAjax(wuliuStartRecordingService.insertWuliuStartRecording(wuliuStartRecording));
        }

    }


    /**
     * 定时保存发车记录
     */
    public void storage() {
        List<WuliuDriver> wuliuDrivers1 = random();
        for (int i = 0; i < 4; i++) {
            WuliuStartRecording wuliuStartRecording = new WuliuStartRecording();

//        添加路线
            WuliuVehicleRoute wuliuVehicleRoute = new WuliuVehicleRoute();
            List<WuliuVehicleRoute> wuliuVehicleRoutes = wuliuVehicleRouteService.selectWuliuVehicleRouteList(wuliuVehicleRoute);
            WuliuVehicleRoute wuliuVehicleRoute1 = wuliuVehicleRoutes.get(i);
            wuliuStartRecording.setRoute(wuliuVehicleRoute1.getSerialNumber());
//        添加随机车牌号
            String drivernum = wuliuDrivers1.get(i).getVehicleNum();
            wuliuStartRecording.setDriveNum(drivernum);
//添加司机 名称
            String drrivername = wuliuDrivers1.get(i).getName();
            wuliuStartRecording.setDirectorName(drrivername);
//        添加发起时间
            Date date = randomDate(getDate1(8), getDate1(9));

            wuliuStartRecording.setDepartureTime(date);
//        添加结束时间
            Date date1 = randomDate(getDate1(18), getDate1(19));
            wuliuStartRecording.setReturnCarTime(date1);
//添加状态
            wuliuStartRecording.setDrivingStart("正常");

            toAjax(wuliuStartRecordingService.insertWuliuStartRecording(wuliuStartRecording));
        }

    }

    //获取车辆集合随机拿四个
    public List<WuliuDriver> random() {
        List<WuliuDriver> wuliuDrivers = wuliuDriverService.selectWuliuDriverList(new WuliuDriver());
        ArrayList<String> strings = new ArrayList<>();
//        打乱顺序
        Collections.shuffle(wuliuDrivers);

        return wuliuDrivers;
    }

    //获取当天0点时间
    public static Date getDate1(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, i);//控制时
        cal.set(Calendar.MINUTE, 0);//控制分
        cal.set(Calendar.SECOND, 0);//控制秒
        return cal.getTime();
    }

    //生成指定时间内的时间
    private static Date randomDate(Date start, Date end) {

        if (start.getTime() >= end.getTime()) {
            return null;
        }
        long date = random(start.getTime(), end.getTime());
        return new Date(date);
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }


    public static Date rollMinute(Date d, int minute) {
        return new Date(d.getTime() + minute * 60 * 1000);
    }

    public Date getNewDate(Date cur) {
        Calendar c = Calendar.getInstance();
        c.setTime(cur);   //设置时间
        c.add(Calendar.HOUR, 1); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
        Date date = c.getTime(); //结果
        return date;
    }


    public static long get_D_Plaus_1(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }
}
