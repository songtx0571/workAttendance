package com.howei.controller;

import com.howei.pojo.*;
import com.howei.service.*;
import com.howei.util.DateFormat;
import com.howei.util.EasyuiResult;
import com.howei.util.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.authz.annotation.Logical.OR;

@Controller
@RequestMapping("/wa/kpi")
@CrossOrigin(origins="http://test.hopeop.com:80",allowCredentials = "true")
public class KPIController {

    @Autowired
    PostPeratorService postPeratorService;

    @Autowired
    PostPeratorDataService postPeratorDataService;

    @Autowired
    private InformService informService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExaminationService examinationServive;
    /**
     *
     * 跳转kpi页面
     * @return
     */
    @RequestMapping("/tokpi")
    public String toKPI(){
        return "kpi";
    }

    @RequestMapping("/toInformKpi")
    public String toInformKpi(){
        return "informKpi";
    }

    @RequestMapping("/toWorkingHours")
    public String toWorkingHours(){
        return "workingHours";
    }

    /**
     * 获取运行KPI数据
     * @param request
     * @return
     */
    @RequiresPermissions(value = {"运行KPI查询"},logical = OR)
    @RequestMapping("/getKPIList")
    @ResponseBody
    public EasyuiResult getKPIList(HttpServletRequest request){
        String depart=request.getParameter("depart");
        String startTime=request.getParameter("startTime");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        Map map=new HashMap();
        if(depart!=null&&!depart.equals("")){
            map.put("depart",depart);
        }
        if(startTime!=null&&!startTime.equals("")){
            map.put("startTime",startTime);
        }else{
            map.put("startTime",DateFormat.ThisMonth());
        }

        List<Map> listTotal=postPeratorService.getKPIList(map);
        map.put("page",offset);
        map.put("pageSize",rows);
        List<Map> list=postPeratorService.getKPIList(map);
        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(list);
        easyuiResult.setTotal(listTotal.size());
        return easyuiResult;
    }

    /**
     * 跳转当月巡检次数页面
     * @param userId
     * @return
     */
    @RequestMapping("/toFrequency")
    public ModelAndView toFrequency(@Param("userId") String userId,@Param("startTime") String startTime){
        ModelAndView model=new ModelAndView();
        model.setViewName("frequency");
        model.addObject("userId",userId);
        model.addObject("startTime",startTime);
        return model;
    }

    /**
     * 查询
     * @param request
     * @return
     */
    @RequestMapping("/getFrequencyList")
    @ResponseBody
    public EasyuiResult getFrequencyList(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String startTime=request.getParameter("startTime");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        List<PostPerator> result=new ArrayList<>();
        int count=0;
        if(userId!=null){
            Map map=new HashMap();
            map.put("postPeratorId",userId);
            if(startTime!=null&&!startTime.equals("")){
                map.put("startTime",startTime);
            }else{
                map.put("startTime",DateFormat.ThisMonth());
            }
            result=postPeratorService.getFrequencyList(map);
            count=result.size();
            result.clear();
            map.put("page",offset);
            map.put("pageSize",rows);
            result=postPeratorService.getFrequencyList(map);
        }
        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(result);
        easyuiResult.setTotal(count);
        return easyuiResult;
    }

    /**
     * 跳转巡检点数页面
     * @param userId
     * @param startTime
     * @return
     */
    @RequestMapping("/toPoint")
    public ModelAndView toPoint(@Param("userId") String userId,@Param("startTime") String startTime){
        ModelAndView model=new ModelAndView();
        model.setViewName("point");
        model.addObject("userId",userId);
        model.addObject("startTime",startTime);
        return model;
    }

    /**
     * 查询巡检点数
     * @param request
     * @return
     */
    @RequestMapping("/getPointList")
    @ResponseBody
    public EasyuiResult getPointList(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String startTime=request.getParameter("startTime");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        List<PostPeratorData> result=new ArrayList<>();
        int count=0;
        if(userId!=null){
            Map map=new HashMap();
            map.put("createdBy",userId);
            if(startTime!=null&&!startTime.equals("")){
                map.put("startTime",startTime);
            }else{
                map.put("startTime",DateFormat.ThisMonth());
            }
            result=postPeratorDataService.getPointList(map);
            count=result.size();
            result.clear();
            map.put("page",offset);
            map.put("pageSize",rows);
            result=postPeratorDataService.getPointList(map);
        }
        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(result);
        easyuiResult.setTotal(count);
        return easyuiResult;
    }

    @RequiresPermissions(value = {"通知KPI查询"},logical = OR)
    @RequestMapping("/getInformKPIList")
    @ResponseBody
    public EasyuiResult getInformKPIList(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        int offset = Page.getOffSet(page, rows);
        Map map = new HashMap();
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        } else {
            map.put("startTime", DateFormat.ThisMonth());
        }

        List<Map> listTotal = informService.getInformKPIList(map);
        map.put("page", offset);
        map.put("pageSize", rows);
        List<Map> list = informService.getInformKPIList(map);
        if(list!=null){
            for (int i=0;i<list.size();i++){
                Map map1=list.get(i);
                String userName=(String)map1.get("userName");
                String createdCount=map1.get("createdCount")+"";
                if(createdCount.isEmpty()||createdCount==null||createdCount.equals("null")){
                    map1.put("createdCount",0);
                    Users user=userService.findByUserName(userName);
                    if(user!=null){
                        map1.put("Id",user.getId());
                        map1.put("Name",user.getUserName());
                    }else{
                        list.remove(map1);
                    }
                }
            }
        }
        EasyuiResult easyuiResult = new EasyuiResult();
        easyuiResult.setRows(list);
        easyuiResult.setTotal(listTotal.size());
        return easyuiResult;
    }

    @RequestMapping("/toCreated")
    public ModelAndView toCreated(@Param("userId") String userId,@Param("startTime") String startTime){
        ModelAndView model=new ModelAndView();
        model.setViewName("created");
        model.addObject("userId",userId);
        model.addObject("startTime",startTime);
        return model;
    }

    @RequestMapping("/toSel")
    public ModelAndView toSel(@Param("userId") String userId){
        ModelAndView model=new ModelAndView();
        model.setViewName("sel");
        model.addObject("userId",userId);
        return model;
    }

    @RequestMapping("/getSelList")
    @ResponseBody
    public EasyuiResult getSelList(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        List<Inform> result=new ArrayList<>();
        int count=0;
        if(userId!=null){
            Map map=new HashMap();
            map.put("createdBy",userId);
            result=informService.getSelList(map);
            count=result.size();
            result.clear();
            map.put("page",offset);
            map.put("pageSize",rows);
            result=informService.getSelList(map);
        }
        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(result);
        easyuiResult.setTotal(count);
        return easyuiResult;
    }


    @RequestMapping("/getCreatedList")
    @ResponseBody
    public EasyuiResult getCreatedList(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String startTime=request.getParameter("startTime");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        List<Inform> result=new ArrayList<>();
        int total=0;
        if(userId!=null){
            Map map=new HashMap();
            if(startTime!=null&&!startTime.equals("")){
                map.put("startTime",startTime);
            }else{
                map.put("startTime",DateFormat.ThisMonth());
            }
            map.put("createdBy",userId);
            total=informService.getCreatedList(map).size();
            map.put("page",offset);
            map.put("pageSize",rows);
            result=informService.getCreatedList(map);
            if(result!=null){
                for (Inform inf:result) {
                    inf.getCreatedBy();
                }
            }
        }
        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(result);
        easyuiResult.setTotal(total);
        return easyuiResult;
    }

    @RequiresPermissions(value = {"检修KPI查询"},logical = OR)
    @RequestMapping("/getWorkHoursList")
    @ResponseBody
    public EasyuiResult getWorkHoursList(HttpServletRequest request){
        String startTime=request.getParameter("startTime");
        String depart=request.getParameter("depart");
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        int offset=Page.getOffSet(page,rows);
        Map map=new HashMap();
        if(startTime!=null&&!startTime.equals("")){
            map.put("startTime",startTime);
        }else{
            map.put("startTime",DateFormat.ThisMonth());
        }
        List<MaintenanceRecord> list = examinationServive.getWorkingHoursByProPeople(map);
        List<Users> users=userService.getNameByProjectId(Integer.parseInt(depart));
        Integer size = users.size();
        //处理分页的users列表
        List<Users> users2 = new ArrayList<>();
        for (int i = offset; i < Integer.parseInt(page) * (Integer.parseInt(rows)); i++) {
            if (i < users.size()) {
                users2.add(users.get(i));
            }
        }
        users = users2;

        List<Map<String,String>> result=resultWorkHoursList(users,list,depart,rows,offset,page);

        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setTotal(size);
        easyuiResult.setRows(result);
        return easyuiResult;
    }

    public List<Map<String,String>> resultWorkHoursList(List<Users> users, List<MaintenanceRecord> list, String depart, String rows, int offset, String page){
        List<Map<String,String>> result=new ArrayList<>();
        double[] count=new double[users.size()];
        for(int i=0;i<users.size();i++){
            Users user=users.get(i);
            count[i]=0;
            for(int k=0;k<list.size();k++){
                MaintenanceRecord record=list.get(k);
                String people=record.getPeople();
                String userName=user.getUserName().toUpperCase();
                if(people.toUpperCase().contains(userName)){//包含
                    count[i]+=record.getWorkingHours();
                }
            }
        }

        for(int i=0;i<users.size();i++){
            double data=count[i];
            if(data<=0){

            }else {
                Map<String, String> map = new HashMap<>();
                map.put("id", users.get(i).getId() + "");
                map.put("name", users.get(i).getUserName());
                map.put("workingHours", count[i] + "");
                result.add(map);
            }
        }
        return result;
    }

    @RequestMapping("/toSelWorkHours")
    public ModelAndView toSel(@Param("userId") String userId,@Param("startTime") String startTime,@Param("depart") String depart){
        ModelAndView model=new ModelAndView();
        model.setViewName("selWorkHours");
        model.addObject("userId",userId);
        model.addObject("startTime",startTime);
        model.addObject("depart",depart);
        return model;
    }

    @RequestMapping("/getSelWorkHoursList")
    @ResponseBody
    public List<Map<String,String>> getSelWorkHoursList(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String startTime=request.getParameter("startTime");
        String depart=request.getParameter("depart");
        List<Map<String,String>> result=new ArrayList<>();
        Map map=new HashMap();
        if(startTime!=null&&!startTime.equals("")&&!startTime.equals(",")){
            map.put("startTime",startTime);
        }else{
            startTime=DateFormat.ThisMonth();
            map.put("startTime",DateFormat.ThisMonth());
        }
        if(depart!=null){
            map.put("depart",depart);
        }
        List<MaintenanceRecord> list = examinationServive.getWorkingHoursByProPeople(map);
        Users user=userService.findById(userId);
        int days=DateFormat.getDaysOfMonth(startTime+"-01");
        double[][] dayData=new double[days][1];
        String userName=user.getUserName().toUpperCase();
        for(int k=0;k<list.size();k++){
            MaintenanceRecord record=list.get(k);
            String people=record.getPeople().toUpperCase();
            if(people.contains(userName)){//包含
                String dateTimeStr=record.getDatetime();
                int dataTime=Integer.parseInt(dateTimeStr.substring(8,10));
                dataTime--;
                dayData[dataTime][0]+=record.getWorkingHours();
            }
        }

        for(int i=0;i<dayData.length;i++){
            for(int k=0;k<dayData[i].length;k++){
                double data=dayData[i][k];
                Map<String, String> mapMap = new HashMap<>();
                mapMap.put("day", "第" + (i + 1) + "天");
                mapMap.put("workHours", data+ "");
                result.add(mapMap);
            }
        }
        EasyuiResult easyuiResult=new EasyuiResult();
        easyuiResult.setRows(result);
        easyuiResult.setTotal(result.size());
        return result;
    }
}
