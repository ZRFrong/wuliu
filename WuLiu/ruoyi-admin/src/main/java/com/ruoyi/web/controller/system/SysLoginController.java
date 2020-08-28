package com.ruoyi.web.controller.system;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import com.google.common.collect.Maps;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysDept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.DbAreaMapper;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.login.LoginService;
import com.ruoyi.web.controller.login.loginpojo;
import com.ruoyi.web.controller.login.userSyn;
import jdk.nashorn.internal.parser.Token;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.List;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 如果是Ajax请求，返回Json字符串。

/*        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

       return "login";*/
       return "redirect:https://peasetech.cn/admin/public/regulatory";
    }
    private static Logger logger = LoggerFactory.getLogger(SysLoginController.class);

    @GetMapping("/loginSession")
    public String loginSession(@Param("token") String token) {
        Long i = loginService.selectByAddress(token);
        loginpojo loginpojo = loginService.selectUser(i);
        SysUser sysUser = new SysUser();
        sysUser.setLoginName(loginpojo.getUsername());
        List<SysUser> sysUsers = sysUserService.selectUserList(sysUser);
        if (sysUsers==null||sysUsers.size()==0){
            //        同步user表
            synchronous();
        }
        String password = loginpojo.getPassword();
        String username = loginpojo.getUsername();
        Boolean rememberMe = false;
        UsernamePasswordToken token1 = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        ModelAndView modelAndView = new ModelAndView();
        try {
            subject.login(token1);

            return "redirect:/index";
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return "redirect:/index";
        }

}

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {

//        userName和PassWord封装到U色那么PasswordToken中
//        rememberMe记住我  boolean值
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return success();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }

    @Autowired
    private LoginService loginService;

    @Autowired
    private ISysUserService sysUserService;





    public  void  synchronous (){
//        同步user表
//         userList
         List<userSyn> synList= loginService.selectUserList();
        for (userSyn userSyn : synList) {
            String user_login = userSyn.getUser_login();
           List<SysUser> sysUsers=  loginService.selectByUserLogin(user_login);
           if (sysUsers.size()==0){
               SysUser sysUser = new SysUser();
               sysUser.setLoginName(userSyn.getUser_login());
               sysUser.setUserName(userSyn.getUser_nickname());
               sysUser.setEmail(userSyn.getUser_email());
               sysUser.setPhonenumber(userSyn.getMobile());
               sysUser.setSex(userSyn.getSex()+"");
               sysUser.setPassword(userSyn.getUser_pass());
               sysUser.setStatus(userSyn.getUser_status()+"");
               sysUser.setDelFlag("0");
               sysUser.setRoleId(2l);
               sysUser.setDeptId(1L);
               Long[] longs = {2L};
               sysUser.setPostIds(longs);
               sysUser.setRoleIds(longs);
               sysUser.setLoginIp(userSyn.getLast_login_ip());
               long millions=new Long(userSyn.getLast_login_time()).longValue()*1000;
               sysUser.setLoginDate(new Date(millions));
               long creatTime=new Long(userSyn.getCreate_time()).longValue()*1000;
               sysUser.setCreateTime(new Date(creatTime));
                sysUserService.insertUser(sysUser);
           }
        }

    }
}
