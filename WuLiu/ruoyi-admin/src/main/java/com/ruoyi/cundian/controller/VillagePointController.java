package com.ruoyi.cundian.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cundian.domain.*;
import com.ruoyi.cundian.service.IVillagePointService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.wuliu.utils.randomLongUtils;
import com.ruoyi.wuliu.domain.WuliuCouriercompany;
import com.ruoyi.wuliu.service.ICourierService;
import com.ruoyi.wuliu.service.IWuliuCouriercompanyService;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * 村点Controller
 *
 * @author zheng
 * @date 2019-12-10
 */
@Controller
@RequestMapping("/cundian/point")
public class VillagePointController extends BaseController {
    private String prefix = "cundian/point";

    @Autowired
    private IVillagePointService villagePointService;

    @Autowired
    private ICourierService courierService;

    @Autowired
    private IWuliuCouriercompanyService couriercompanyService;

    @RequiresPermissions("cundian:point:view")
    @GetMapping()
    public String point() {
        return prefix + "/point";
    }

    /**
     * 查询村点树列表
     */
    @RequiresPermissions("cundian:point:list")
    @PostMapping("/list")
    @ResponseBody
    public List<VillagePoint> list(VillagePoint villagePoint) {
        // 获取当前的用户信息
        SysUser sysUser = ShiroUtils.getSysUser();
        SysDept dept = sysUser.getDept();
        String deptName = dept.getDeptName();
        List<VillagePoint> list1 = new ArrayList<>();
        if ("admin".equals(sysUser.getLoginName())) {
            list1 = villagePointService.selectVillagePointList(villagePoint);
        } else if (!"县级管理".equals(deptName) && deptName != null) {
            //获取到所属服务站名
            String station = sysUser.getStation();
            //查询指定服务站名的服务站以及子服务站
            list1 = villagePointService.selectbyvillageName(station);
        } else {
            list1 = villagePointService.selectVillagePointList(villagePoint);
        }
        return list1;
    }

    /**
     * 导出村点列表
     */
    @RequiresPermissions("cundian:point:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(VillagePoint villagePoint) {
        List<VillagePoint> list = villagePointService.selectVillagePointList(villagePoint);
        ExcelUtil<VillagePoint> util = new ExcelUtil<VillagePoint>(VillagePoint.class);
        return util.exportExcel(list, "point");
    }

    /**
     * 新增村点
     */
    @GetMapping(value = {"/add/{id}", "/add/"})
    public String add(@PathVariable(value = "id", required = false) Long id, ModelMap mmap) {
        if (StringUtils.isNotNull(id)) {
            mmap.put("villagePoint", villagePointService.selectVillagePointById(id));
        }
        return prefix + "/add";
    }

    /**
     * 新增保存村点
     */
    @RequiresPermissions("cundian:point:add")
    @Log(title = "村点", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam("file") MultipartFile picture, VillagePoint villagePoint, @RequestParam("checked") String[] arr) throws IOException {
        // 上传文件路径
        String filePath = Global.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, picture);
        villagePoint.setPicture(fileName);
        villagePoint.setIsEnable("0");
        int i = 0;
        try {
            i = villagePointService.insertVillagePoint(villagePoint);
        } catch (Exception e) {
//            return new AjaxResult("村点名称已经存在");
            return error("村点名称已经存在");
        }
        List<VillagePoint> list = villagePointService.selectVillagePointList(villagePoint);
        Long id = list.get(0).getId();
        for (String s : arr) {
            WuliuCouriercompany couriercompany = new WuliuCouriercompany();
            couriercompany.setCouriercompany(s);
            List<WuliuCouriercompany> couriercompanies = couriercompanyService.selectWuliuCouriercompanyList(couriercompany);
            Long id1 = couriercompanies.get(0).getId();
            villagePointService.setvillageCourierId(id, id1);
        }
        return toAjax(i);
    }

    /**
     * 修改村点
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        VillagePoint villagePoint = villagePointService.selectVillagePointById(id);
        Long[] arr = villagePointService.selectbycourierid(id);
        StringBuilder stringBuilder = new StringBuilder();
        for (Long aLong : arr) {
            WuliuCouriercompany couriercompany = couriercompanyService.selectWuliuCouriercompanyById(aLong);
            stringBuilder.append(couriercompany.getCouriercompany());
            stringBuilder.append(",");
        }
        VillagePointVo villagePointVo = new VillagePointVo();
        villagePointVo.setVillagePoint(villagePoint);
        String s = stringBuilder.toString();
        villagePointVo.setCourierCompanyIdS(s);
        mmap.put("villagePointVo", villagePointVo);
        return prefix + "/edit";
    }

    /**
     * //     * 修改保存村点
     * //
     */
    @RequiresPermissions("cundian:point:edit")
    @Log(title = "村点", businessType = BusinessType.UPDATE)
    @PostMapping("/edit1")
    @ResponseBody
    public AjaxResult editSave(VillagePointVo1 villagePointVo) throws IOException {
        VillagePoint villagePoint = new VillagePoint();
        villagePoint.setId(villagePointVo.getId());
        villagePoint.setVillageName(villagePointVo.getVillageName());
        villagePoint.setProvinces(villagePointVo.getProvinces());
        villagePoint.setCities(villagePointVo.getCities());
        villagePoint.setCounties(villagePointVo.getCounties());
        villagePoint.setAddress(villagePointVo.getAddress());
        villagePoint.setLongitude(villagePointVo.getLongitude());
        villagePoint.setLatitude(villagePointVo.getLatitude());
        villagePoint.setIsEnable("0");
        villagePoint.setHead(villagePointVo.getHead());
        villagePoint.setHeadPhone(villagePointVo.getHeadPhone());
        villagePoint.setExaminationName(villagePointVo.getExaminationName());
        villagePoint.setExaminationPhone(villagePointVo.getExaminationPhone());
        villagePoint.setExaminationTime(villagePointVo.getExaminationTime());
        villagePoint.setProductId(villagePointVo.getProductId());
        String courierCompanyIdS = villagePointVo.getCourierCompanyIdS();
        // 上传文件路径
        // 上传并返回新文件名称
        villagePoint.setPicture(villagePointVo.getPicture());
        String[] split = courierCompanyIdS.split(",");
        Long id = villagePoint.getId();
        villagePointService.deleteCourierByvilid(id);
        for (String s : split) {
            WuliuCouriercompany couriercompany = new WuliuCouriercompany();
            couriercompany.setCouriercompany(s);
            List<WuliuCouriercompany> couriercompanies = couriercompanyService.selectWuliuCouriercompanyList(couriercompany);
            Long id1 = couriercompanies.get(0).getId();
            villagePointService.setvillageCourierId(id, id1);
        }
        return toAjax(villagePointService.updateVillagePoint(villagePoint));
    }

    /**
     * 修改保存村点
     */
    @RequiresPermissions("cundian:point:edit")
    @Log(title = "村点", businessType = BusinessType.UPDATE)
    @PostMapping("/edit2")
    @ResponseBody
    public AjaxResult editSave(VillagePointVo1 villagePointVo, @RequestParam("file") MultipartFile picture) throws IOException {
        VillagePoint villagePoint = new VillagePoint();
        villagePoint.setId(villagePointVo.getId());
        villagePoint.setVillageName(villagePointVo.getVillageName());
        villagePoint.setProvinces(villagePointVo.getProvinces());
        villagePoint.setCities(villagePointVo.getCities());
        villagePoint.setCounties(villagePointVo.getCounties());
        villagePoint.setAddress(villagePointVo.getAddress());
        villagePoint.setLongitude(villagePointVo.getLongitude());
        villagePoint.setLatitude(villagePointVo.getLatitude());
        villagePoint.setIsEnable("0");
        villagePoint.setHead(villagePointVo.getHead());
        villagePoint.setHeadPhone(villagePointVo.getHeadPhone());
        villagePoint.setExaminationName(villagePointVo.getExaminationName());
        villagePoint.setExaminationPhone(villagePointVo.getExaminationPhone());
        villagePoint.setExaminationTime(villagePointVo.getExaminationTime());
        villagePoint.setProductId(villagePointVo.getProductId());
        String courierCompanyIdS = villagePointVo.getCourierCompanyIdS();
        if (picture != null) {
            // 上传文件路径
            String filePath = Global.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, picture);
            villagePoint.setPicture(fileName);
        } else {
            villagePoint.setPicture(villagePointVo.getPicture());
        }
        String[] split = courierCompanyIdS.split(",");
        Long id = villagePoint.getId();
        villagePointService.deleteCourierByvilid(id);
        for (String s : split) {
            WuliuCouriercompany couriercompany = new WuliuCouriercompany();
            couriercompany.setCouriercompany(s);
            List<WuliuCouriercompany> couriercompanies = couriercompanyService.selectWuliuCouriercompanyList(couriercompany);
            Long id1 = couriercompanies.get(0).getId();
            villagePointService.setvillageCourierId(id, id1);
        }
        return toAjax(villagePointService.updateVillagePoint(villagePoint));
    }

    /**
     * 删除
     */
    @RequiresPermissions("cundian:point:remove")
    @Log(title = "村点", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Long id) {
        return toAjax(villagePointService.deleteVillagePointById(id));
    }

    /**
     * 选择村点树
     */
    @GetMapping(value = {"/selectPointTree/{id}", "/selectPointTree/"})
    public String selectPointTree(@PathVariable(value = "id", required = false) Long id, ModelMap mmap) {
        if (StringUtils.isNotNull(id)) {
            mmap.put("villagePoint", villagePointService.selectVillagePointById(id));
        }
        return prefix + "/tree";
    }

    /**
     * 加载村点树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = villagePointService.selectVillagePointTree();
        return ztrees;
    }

    /*
     *通过服务站名查询可支配的快递公司
     * */
    @GetMapping("/slectcourier")
    @ResponseBody
    public List<String> slectcourier(String villageName) {
        VillagePoint villagePoint = new VillagePoint();
        villagePoint.setVillageName(villageName);
        List<VillagePoint> list = villagePointService.selectVillagePointList(villagePoint);
        Long id = list.get(0).getId();
        Long[] selectbycourierid = villagePointService.selectbycourierid(id);
        List<String> strings = new ArrayList<>();
        for (Long aLong : selectbycourierid) {
            WuliuCouriercompany couriercompany = couriercompanyService.selectWuliuCouriercompanyById(aLong);
            strings.add(couriercompany.getCouriercompany());
        }
        return strings;
    }


    //    查询村点总数
    @GetMapping("/cundianNum")
    @ResponseBody
    public String cundianNum() {
        String num = villagePointService.cundianNum();
        return num;
    }

    //    查询服务站名称
    @GetMapping("/saveList")
    @ResponseBody
    public List<VillagePoint> saveList() {
        VillagePoint villagePoint = new VillagePoint();
        List<VillagePoint> list = villagePointService.selectVillagePointList(villagePoint);
        return list;

    }


    //    首页统计村点信息 一级页面
    @GetMapping("/statistcalIndex")
    @ResponseBody
    public List<VillageStatisticalVo> statistcalIndex() {

        List<VillageStatisticalVo> list = villagePointService.cundianSaveNum();
        ArrayList<VillageStatisticalVo> villageStatisticalVos = new ArrayList<>();

        for (VillageStatisticalVo villageStatisticalVo : list) {
//            乡镇名称
            String name = villageStatisticalVo.getName();
//           真实的 分上下行给数据
//            List<VillageStatisticalVo> list1 = courierService.selectSend(name);
//
//            for (VillageStatisticalVo statisticalVo : list1) {
//                VillageStatisticalVo villageStatisticalVo1 = new VillageStatisticalVo();
//                String a = statisticalVo.getName() + statisticalVo.getDrilldown();
//                villageStatisticalVo1.setName(a);
//                villageStatisticalVo1.setDrilldown(a);
//                villageStatisticalVo1.setY(statisticalVo.getY());
//                villageStatisticalVos.add(villageStatisticalVo1);
//            }
            /*
             *模拟数据
             * */
            List<VillageStatisticalVo> judge = judge(name);
            villageStatisticalVos.addAll(judge);
        }
        return villageStatisticalVos;
    }
        /*
    常乐下行47800上行27020
    洪池下行20640上行19750
    张村下行22500上行16000
    张店下行27000上行12500
    杜马下行17500上行13000
    部官下行14245上行9470
    曹川下行23390上行6000
    三门下行9365上行3370
    坡底下行4215上行470
    圣人涧镇下行86954  上行38450
    * */

    public static int[] upArray = {27020, 19750, 16000, 12500, 13000, 9470, 6000, 3370, 3000, 38450};
    public static int[] downArray = {47800, 20640, 22500, 27000, 17500, 14245, 23390, 9365, 4215, 86954};
    public static String[] nameArray = {"常乐镇", "洪池乡", "张村镇", "张店镇", "杜马乡", "部官乡", "曹川镇", "三门镇", "坡底乡", "圣人涧镇"};

    public static List<VillageStatisticalVo> judge( String name) {
         List<VillageStatisticalVo> villageStatisticalVos=new ArrayList<VillageStatisticalVo>();
        if (name.contains("常乐镇")) {
            villageStatisticalVos.addAll(villageZhen(0));
        } else if (name.equals("洪池乡")) {
            villageStatisticalVos.addAll(villageZhen(1));
        } else if (name.equals("张村镇")) {
            villageStatisticalVos.addAll(villageZhen(2));
        } else if (name.equals("张店镇")) {
            villageStatisticalVos.addAll(villageZhen(3));
        } else if (name.equals("杜马乡")) {
            villageStatisticalVos.addAll(villageZhen(4));
        } else if (name.equals("部官乡")) {
            villageStatisticalVos.addAll(villageZhen(5));
        } else if (name.equals("曹川镇")) {
            villageStatisticalVos.addAll(villageZhen(6));
        } else if (name.equals("三门镇")) {
            villageStatisticalVos.addAll(villageZhen(7));
        } else if (name.equals("坡底乡")) {
            villageStatisticalVos.addAll(villageZhen(8));
        } else if (name.equals("圣人涧镇")) {
            villageStatisticalVos.addAll(villageZhen(9));
        }
        return villageStatisticalVos;
    }


    public static List<VillageStatisticalVo> villageZhen(int type) {
        Random random = new Random();
        int i = random.nextInt(1000);
        int arr2 = 0;
        String a;
        List<VillageStatisticalVo> list = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            VillageStatisticalVo villageStatisticalVo1 = new VillageStatisticalVo();
            if (j == 1) {
                a = nameArray[type] + "县到村";
                int arr = upArray[type];
                arr2 = (random.nextBoolean() ? arr - i : arr + i);
            } else {
                int arr = downArray[type];
                a = nameArray[type] + "村到县";
                arr2 = (random.nextBoolean() ? arr - i : arr + i);
            }
            villageStatisticalVo1.setName(a);
            villageStatisticalVo1.setDrilldown(a);
            villageStatisticalVo1.setY(arr2);
            list.add(villageStatisticalVo1);
        }
        return list;
    }

    /*
     *真实数据
     * */
/*    //    首页统计村点信息 二级页面
    @GetMapping("/statistcalIndexTwo")
    @ResponseBody
    public Map<String, List<VillageTwoVo>> statistcalIndexTwo() {
//        拿出所有的父级name
        List<VillageTwoVo> villageTwoVos = new ArrayList<>();

        List<VillageStatisticalVo> list = villagePointService.cundianSaveNum();

        list.forEach(item -> {
            String name = item.getName();
            List<VillageStatisticalVo> list1 = courierService.selectSend(name);
            for (VillageStatisticalVo statisticalVo : list1) {
                VillageTwoVo villageTwoVo = new VillageTwoVo();
                String a = statisticalVo.getName() + statisticalVo.getDrilldown();
                villageTwoVo.setName(a);
                villageTwoVo.setId(a);
                VillagePoint villagePoint = new VillagePoint();
                villagePoint.setVillageName(statisticalVo.getName());
                List<VillagePoint> list3 = villagePointService.selectVillagePointList(villagePoint);
                List<VillageStatisticalVo> list4 = villagePointService.cundianSaveNumAll(list3.get(0).getId());
                List<List> objects = new ArrayList<>();
                for (VillageStatisticalVo villageStatisticalVo : list4) {
                    List<VillageStatisticalVo> list2 = courierService.selectSendTwo(villageStatisticalVo.getName(), statisticalVo.getDrilldown());
//                拼接date
                    list2.forEach(item2 -> {
                        List<Object> objects1 = new ArrayList<>();
                        objects1.add(item2.getName() + item2.getDrilldown());
                        objects1.add(item2.getY());
                        objects.add(objects1);
                    });
                }
                villageTwoVo.setData(objects);
                villageTwoVos.add(villageTwoVo);
            }
        });
        Map<String, List<VillageTwoVo>> stringvillageTwoVosMap = new HashMap<String, List<VillageTwoVo>>();
        stringvillageTwoVosMap.put("series", villageTwoVos);
        return stringvillageTwoVosMap;

    }*/
    //    首页统计村点信息 二级页面

    @GetMapping("/statistcalIndexTwo")
    @ResponseBody
    public Map<String, List<VillageTwoVo>> statistcalIndexTwo() {
//        拿出所有的父级name
        List<VillageTwoVo> villageTwoVos = new ArrayList<>();
        List<VillageStatisticalVo> list = villagePointService.cundianSaveNum();
        list.forEach(item -> {
            String name = item.getName();
            List<VillageStatisticalVo> judge = judge(name);

            for (VillageStatisticalVo statisticalVo : judge) {
                VillageTwoVo villageTwoVo = new VillageTwoVo();
                String a = statisticalVo.getName();
                villageTwoVo.setName(a);
                villageTwoVo.setId(a);

                VillagePoint villagePoint = new VillagePoint();
                String name1 = statisticalVo.getName();
                String[] split = name1.split("");
                split[split.length-1]="";
                split[split.length-2]="";
                split[split.length-3]="";
                StrBuilder strBuilder = new StrBuilder();
                for (String s : split) {
                    strBuilder.append(s);
                }
                villagePoint.setVillageName(strBuilder.toString());
                List<VillagePoint> list3 = villagePointService.selectVillagePointList(villagePoint);
//                子列表集合
                List<VillageStatisticalVo> list4 = villagePointService.cundianSaveNumAll(list3.get(0).getId());
                List<List> objects = new ArrayList<>();
                    List<Integer> random = randomLongUtils.random(statisticalVo.getY(), list4.size());
                for (int i = 0; i < list4.size(); i++) {
                    //                拼接date
                    List<Object> objects1 = new ArrayList<>();
                    String drilldown = statisticalVo.getDrilldown();
                    String[] split1 = drilldown.split("");
                    StrBuilder strBuilder1 = new StrBuilder();
                    strBuilder1.append(split1[split1.length-3]);
                    strBuilder1.append(split1[split1.length-2]);
                    strBuilder1.append(split1[split1.length-1]);

                    objects1.add(list4.get(i).getName() +strBuilder1.toString() );
                    objects1.add(random.get(i));
                    objects.add(objects1);
                }
                villageTwoVo.setData(objects);
                villageTwoVos.add(villageTwoVo);
                }

        });
        Map<String, List<VillageTwoVo>> stringvillageTwoVosMap = new HashMap<String, List<VillageTwoVo>>();
        stringvillageTwoVosMap.put("series", villageTwoVos);
        return stringvillageTwoVosMap;
    }



    //    查询服务站名称
    @GetMapping("/openIndexHome")
    public String openIndexHome() {
        return "cundian_main_index";
    }

    //    查询服务站名称
    @GetMapping("/openIndexHomePage")
    public ModelAndView openIndexHomePage(String pageUrl) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cundian_index_page");
        modelAndView.addObject("pageUrl", pageUrl);
        return modelAndView;
    }

}
