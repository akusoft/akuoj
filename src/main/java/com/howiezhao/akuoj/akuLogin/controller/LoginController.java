package com.howiezhao.akuoj.akuLogin.controller;

import com.howiezhao.akuoj.akuCommunity.services.impl.FollowServicesImpl;
import com.howiezhao.akuoj.akuCommunity.services.impl.LikeServicesImpl;
import com.howiezhao.akuoj.akuLogin.dao.User;
import com.howiezhao.akuoj.akuLogin.services.impl.RegisterServicesImpl;
import com.howiezhao.akuoj.akuLogin.services.impl.UserServicesImpl;
import com.howiezhao.akuoj.annoation.LoginRequired;
import com.howiezhao.akuoj.utils.AkuOjConstant;
import com.howiezhao.akuoj.utils.AkuOjUtils;
import com.howiezhao.akuoj.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author LiMing
 * @since 2020/5/11 12:49
 **/
@Controller
public class LoginController implements AkuOjConstant {

    @Autowired
    private RegisterServicesImpl registerServices;

    @Autowired
    private UserServicesImpl userServices;

    /*@Autowired
    private Producer producerKaptcha;*/

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${aku.path.upload}")
    private String uploadLoaclation;

    @Value("${aku.path}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeServicesImpl likeServices;

    @Autowired
    private FollowServicesImpl followServices;

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }


    @RequestMapping(value = "/profile",method = RequestMethod.GET)
    public String profile(){
        return "profile";
    }
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(){
        return "register";
    }



    @RequestMapping(value = "/forget",method = RequestMethod.GET)
    public String forget(){
        return "forget";
    }


    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> register = registerServices.register(user);

        if(register==null || register.isEmpty()){
            model.addAttribute("msg","我们已经向你发送了一封邮件，请前去激活");
            model.addAttribute("target","/index");
            return "operate-result";
        }else{
            model.addAttribute("usernameMsg",register.get("usernameMsg"));
            model.addAttribute("passwordMsg",register.get("passwordMsg"));
            model.addAttribute("emailMsg",register.get("emailMsg"));
            return "register";
        }
    }

    @RequestMapping(value = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int status = registerServices.getActivationStatus(userId, code);
        if(status==ACTIVATION_SUCCESS){
            model.addAttribute("msg","你已经成功激活，请前往登录！");
            model.addAttribute("target","/login");
        }else if(status==ACTIVATION_REPEAT){
            model.addAttribute("msg","激活无效，您已经激活过了");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，激活码是错误的");
            model.addAttribute("target","/index");
        }

        return "operate-result";
    }

    /*@RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){

        //生成验证码
        String text = producerKaptcha.createText();
        BufferedImage image = producerKaptcha.createImage(text);

        //将验证码写入session
        session.setAttribute("kaptchacode",text);

        //将图片响应给浏览器
        response.setContentType("image/png");

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("验证码生成失败！",e.getMessage());
        }

    }*/

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(String username,String password,Model model, String code,boolean remember,HttpSession session,HttpServletResponse response){

       /* String kaptchacode = (String) session.getAttribute("kaptchacode");

        //先判断验证码
        if(StringUtils.isBlank(kaptchacode) || StringUtils.isBlank(code)|| !kaptchacode.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确！！");
            return "login";
        }*/
        int expried=remember?REMEMBER_EXPIRED_TIME:DEFAULT_EXPIRED_TIME;

        Map<String, Object> map = userServices.login(username, password, expried);

        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expried);
/*
            cookie.setSecure(true);
*/
            response.addCookie(cookie);
            return "redirect:index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "login";
        }
    }
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userServices.logout(ticket);
        return "redirect:/index";
    }

    @LoginRequired
    @RequestMapping(value = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "site/setting";
    }

    @RequestMapping(value = "/upload")
    @LoginRequired
    public String upload(Model model, MultipartFile handerImage){

        if(handerImage==null){
            model.addAttribute("error","你还没有选择文件");
            return "site/setting";
        }
        String filename = handerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)|| !suffix.equalsIgnoreCase(".png")){
            model.addAttribute("error","图片格式有问题,系统只支持png格式的图片");
            return "site/setting";
        }

        //生成随机的图片名字
        filename= AkuOjUtils.generateUUID()+suffix;
        //存储文件
        File file = new File(uploadLoaclation + "/" + filename);
        try {
            handerImage.transferTo(file);
        } catch (IOException e) {
            logger.error("文件上传失败！{}",e.getMessage());
            new RuntimeException("上传文件失败，服务器异常",e);
        }

        //改变user的handerUrl
        String headerUrl=domain+contextPath+"/header/"+filename;
        userServices.uploadHeaderUrl(hostHolder.getUser().getId(),headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(value = "/header/{filename}",method = RequestMethod.GET)
    public void getHeaderImage(HttpServletResponse response, @PathVariable("filename") String filename){

        String fileLocation=uploadLoaclation+"/"+filename;

        String suffix = filename.substring(filename.lastIndexOf("."));

        response.setContentType("image/"+suffix);

        try (
                FileInputStream fis = new FileInputStream(fileLocation);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        }
        catch (IOException e) {
            logger.error("获取头像失败！{}",e.getMessage());
            new RuntimeException("头像加载失败",e);
        }


    }


    @LoginRequired
    @RequestMapping(value = "/updatePassword")
    public String updatePassword(String oldpassword,String newpassword,Model model,@CookieValue("ticket") String ticket){

        User user = hostHolder.getUser();
        if(StringUtils.isBlank(oldpassword)){
            model.addAttribute("oldpasswordMsg","原始密码不能为空！");
            return "site/setting";
        }
        String pass = AkuOjUtils.md5(oldpassword + user.getSalt());
        if(!pass.equals(user.getPassword())){
            model.addAttribute("oldpasswordMsg","原始密码不正确！");
            return "site/setting";
        }
        if(StringUtils.isBlank(newpassword)){
            model.addAttribute("newpasswordMsg","原始密码不能为空！");
            return "site/setting";
        }
        userServices.updatepassword(user.getId(),newpassword);
        userServices.logout(ticket);
        return "redirect:/login";

    }

    @RequestMapping(value = "/forget/sendKaptcha",method = RequestMethod.POST)
    public String sendKaptcha(Model model, User user){
        Map<String, Object> forget = registerServices.sendKaptcha(user.getEmail());
        if(forget==null || forget.isEmpty()){
            model.addAttribute("msg","我们已经向你发送了一封邮件，请前去查看验证码");
            model.addAttribute("target","/forget");
            return "operate-result";
        }else{
            model.addAttribute("emailMsg",forget.get("emailMsg"));
            return "forget";
        }
    }

    @RequestMapping(value = "/forget",method = RequestMethod.POST)
    public String forget(Model model, User user){
        Map<String, Object> forget = registerServices.forget(user);
        if(forget==null || forget.isEmpty()){
            model.addAttribute("msg","我们已经向你发送了一封邮件，请前去激活");
            model.addAttribute("target","/index");
            return "operate-result";
        }else{
            model.addAttribute("usernameMsg",forget.get("usernameMsg"));
            model.addAttribute("passwordMsg",forget.get("passwordMsg"));
            model.addAttribute("emailMsg",forget.get("emailMsg"));
            return "register";
        }
    }

    @RequestMapping(value = "/profile/{userId}",method = RequestMethod.GET)
    public String profile(@PathVariable("userId") int userId,Model model){

        User user = userServices.selectUserById(userId);
        if(user==null){
            throw new RuntimeException("用户不存在！！！");
        }
        model.addAttribute("user",user);


        if(user!=null){
            //关注数

            long followeeCount = followServices.getFolloweeCount(userId, REPLY_USER);

            model.addAttribute("followeeCount",followeeCount);

            //粉丝数

            long followerCount = followServices.getFollowerCount(REPLY_USER, userId);
            model.addAttribute("followerCount",followerCount);

            //是否已关注
            boolean hasFollow=false;
            if(hostHolder.getUser()!=null){
                hasFollow=followServices.hasFollowee(hostHolder.getUser().getId(),REPLY_USER,userId);
            }
            model.addAttribute("hasFollow",hasFollow);
        }
        model.addAttribute("userLikeCount",likeServices.getEntityUserCount(userId));

        return "profile";
    }

}
