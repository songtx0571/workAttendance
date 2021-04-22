package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.*;
import com.howei.service.*;
import com.howei.util.DateFormat;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.invoke.empty.Empty;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static org.apache.shiro.authz.annotation.Logical.OR;

@Controller
@RequestMapping("/wa/wags")
public class WagsComtroller {

    @Autowired
    private WagsService wagsService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BehaviorService behaviorService;

    @Autowired
    private WageBaseService wageBaseService;


    public Users getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        return users;
    }

    @RequestMapping("/toWags")
    public String toWags() {
        return "wags";
    }

    @RequestMapping("/getWagsList")
    @ResponseBody
    public Result getWagsList(HttpServletRequest request) {
        String month = request.getParameter("month");
        Users users = this.getPrincipal();

        String empIdStr = "";
        Integer employeeId = null;
        if (month == null || month.equals("")) {
            month = DateFormat.ThisMonth();
        }
        if (users != null) {
            employeeId = users.getEmployeeId();
        }
        employeeId=240;
        Map map = new HashMap<>();
        map.put("month", month + "-01");
        //判断是否存在以下角色，
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("财务") || subject.hasRole("总部管理员")) {
            empIdStr = null;
        } else {
            List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
            if (rootList != null) {
                empIdStr += employeeId + ",";
                List<Employee> empList = employeeService.getEmployeeByManager(0);
                for (Employee employee : rootList) {
                    empIdStr += employee.getId() + ",";
                    empIdStr += getUsersId(employee.getId(), empList);
                }
            }
            if (empIdStr != null && !empIdStr.equals("")) {
                empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
            }
        }
        map.put("empId", empIdStr);
        List<Wages> list = wagsService.getWagsList(map);
        Result result = new Result();
        result.setCode(0);
        if (list != null) {
            result.setCount(list.size());
            result.setData(list);
        }
        return result;
    }

    /**
     * 计算综合绩效
     * @param cycle
     * @param employeeId
     * @return
     */
    public double getAssessmentByEmployeeId(String cycle,String employeeId) {
        Map map=new HashMap();
        if(cycle!=null&&!cycle.equals("")){
            //获取指定日期的上一月份
            cycle=DateFormat.getYearMonthByMonth(cycle,-1);
            map.put("cycle",cycle);
        }
        if(employeeId!=null&&!employeeId.equals("")){
            map.put("employeeId",employeeId);
        }
        Assessment assessment=behaviorService.getAssessmentByEmployeeId(map);
        if(assessment!=null){
            double score1=assessment.getScore1();
            double score2=assessment.getScore2();
            double jianban=assessment.getJiaban();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1*0.5+score2*0.5)/90);
            double netPerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setNetPerformance(netPerformance);
            //综合绩效=净绩效+加班*0.01
            bd = new BigDecimal(netPerformance+jianban*0.01);
            double comprehensivePerformance=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return comprehensivePerformance;
        }
        return 0.0;
    }

    public String getUsersId(Integer empId, List<Employee> empList) {
        List<String> result = new ArrayList<>();
        String userId = "";
        String usersId = "";
        for (Employee employee : empList) {
            if (employee.getManager() != null || employee.getManager() != 0) {
                if (employee.getManager().equals(empId)) {
                    usersId += employee.getId() + ",";
                    result.add(employee.getId() + "");
                }
            }
        }
        for (String str : result) {
            String userId1 = getUsersId(Integer.parseInt(str), empList);
            if (userId1 != null && !userId1.equals("")) {
                userId += userId1;
            }
        }
        if (userId != null && !userId.equals("null")) {
            usersId += userId;
        }
        return usersId;
    }

    /**
     * 生成本月工资信息
     * 工资小计=基本工资+技能工资+职务工资+工龄工资+其他+绩效工资
     * 应发工资=(基本工资+技能工资+职务工资+工龄工资+其他)+绩效工资*绩效系数
     * 应发合计=工资小计+补贴小记
     * 扣款合计=养老保险+失业金+医疗保险+公积金+其他扣款+工会费
     * 计税合计=应发合计-扣款合计
     *
     * @return
     */
    @RequestMapping("/copyToThisMonthWags")
    @ResponseBody
    public String copyToThisMonthWags(HttpServletRequest request) {
        String monthStart = request.getParameter("monthStart");
        String monthEnd = request.getParameter("monthEnd");
        Map map = new HashMap<>();
        map.put("month", monthStart + "-01");
        List<Wages> list = wagsService.getWagsList(map);
        if (list != null) {
            List<Wages> wagesList = new ArrayList<>();
            for (Wages wages : list) {
                //判断员工是否在职
                Integer empId = wages.getEmployeeId();
                Users user = userService.getUserByEmpId(empId);
                if (user != null && user.getState() == 1) {
                    wages.setDate(monthEnd + "-01");
                    wages.setCreated(monthEnd + "-01");
                    wages.setPerformanceCoefficient(0.00);//设置绩效系数
                    wages.setFoodSupplement(0.00);//餐补
                    wages.setHighTemperatureSubsidy(0.00);//高温补贴
                    wages.setTotalPayable(0.00);//应发合计
                    wages.setTotalDeduction(0.00);//扣款合计
                    wages.setOtherDeductions(0.00);//其他扣款
                    wages.setIndividualTaxAdjustment(0.00);//个调税
                    wages.setNetSalary(0.00);//实发工资
                    if(wages.getWagesPostId()!=null){
                        WagesPost wagesPost=wageBaseService.getWagesPostById(wages.getWagesPostId());
                        wages.setBasePay(wagesPost.getWagesPostWage());//基本工资
                        PostGrade postGrade=wageBaseService.getPostBaseById(wages.getPostGradeId());
                        wages.setPositionSalary(postGrade.getPostGradeWage());//职级工资
                        //工资小计=岗位工资+职级工资
                        wages.setWageSubtotal(wagesPost.getWagesPostWage()+postGrade.getPostGradeWage());//工资小计
                        wages.setMeritPay(wages.getWageSubtotal()/2);
                    }
                    wagesList.add(wages);
                }
            }
            int result = wagsService.copyToThisMonthWags(wagesList);
            if (result > 0) {
                return JSON.toJSONString(Type.SUCCESS);
            }
            return JSON.toJSONString(Type.CANCEL);
        }
        return JSON.toJSONString(Type.CANCEL);
    }

    /**
     * 生成本月工资信息
     * 工资小计=基本工资+技能工资+职务工资+工龄工资+其他+绩效工资
     * 应发工资=(基本工资+技能工资+职务工资+工龄工资+其他)+绩效工资*绩效系数
     * 应发合计=工资小计+补贴小记
     * 扣款合计=养老保险+失业金+医疗保险+公积金+其他扣款+工会费
     * 计税合计=应发合计-扣款合计
     *
     * @return
     */
    @RequestMapping("/taxation")
    @ResponseBody
    public String taxation(HttpServletRequest request) {
        String month = request.getParameter("month");
        String employeeId = request.getParameter("employeeId");
        double specialAdditionalDeduction = Double.valueOf(request.getParameter("specialAdditionalDeduction"));//专项扣除
        double totalTax = Double.valueOf(request.getParameter("totalTax"));//计税合计
        //************************************ 个调税计算 *****************************************
        Double tax = this.taxCalculator(employeeId, month,specialAdditionalDeduction,totalTax);
        //************************************ 实发工资 *****************************************
        Double netSalary = Double.valueOf(totalTax) - tax;//实发工资
        Map map = new HashMap();
        DecimalFormat df = new DecimalFormat("0.00");
        map.put("tax", df.format(tax));
        map.put("netSalary", df.format(netSalary));
        return JSON.toJSONString(map);
    }

    /**
     * 个税计算
     * @param employeeId
     * @param month
     * @param specialAdditionalDeduction 专项扣除
     * @param totalTaxThisMonth 计税合计
     * @return
     */
    public Double taxCalculator(String employeeId, String month,double specialAdditionalDeduction,double totalTaxThisMonth) {
        //累计值
        Double wageSubtotal=0.00;//工资小计
        Double wagesPayable=0.00;//应发工资
        Double totalPayable=0.00;//应发合计
        Double totalDeduction=0.00;//扣款合计
        Double totalTax=0.00;//计税合计
        Double deductionOfExpensesTaxTotal = 0.00;//累计减除费用
        Double taxableIncome = 0.00;//累计应纳税所得额
        Double personalIncomeTotalTax = 0.00;//累计个税
        Double specialAdditionalDeductionTaxTotal = 0.00;//累计专项扣除

        Map map = new HashMap();
        map.put("month", month + "-01");
        map.put("employeeId", employeeId);
        List<Wages> listWages = wagsService.getWagesToTax(map);
        if (listWages != null) {
            for (Wages wages : listWages) {
                double PerformanceCoefficient=getAssessmentByEmployeeId(wages.getDate(),wages.getEmployeeId()+"");
                //工资小计=基本工资+职务工资+绩效工资+其他
                //工资小计=岗位工资+职级工资
                wageSubtotal=wages.getBasePay()+wages.getPositionSalary()+wages.getMeritPay()+wages.getOther();
                //应发工资=（基本工资+职务工资+其他）+绩效基数*绩效系数
                //应发工资==(岗位工资+职级工资)/2+绩效工资+其他
                wagesPayable=(wages.getBasePay()+wages.getPositionSalary()+wages.getOther())+wages.getMeritPay()*PerformanceCoefficient;
                //应发合计=基本工资+职务工资+其他+补贴小计
                totalPayable =wagesPayable+wages.getHighTemperatureSubsidy()+wages.getFoodSupplement();
                //扣款合计=养老保险+医疗保险+公积金+失业金+工会费+其他扣款
                totalDeduction=wages.getEndowmentInsurance()+wages.getMedicalInsurance()+wages.getAccumulationFund()+wages.getUnemploymentBenefits()+wages.getUnionFees()+wages.getOtherDeductions();
                //计税合计=应发合计-扣款合计
                totalTax =totalTax+(totalPayable-totalDeduction);
                //累计减除费用
                deductionOfExpensesTaxTotal += 5000;
                //累计专项扣除
                specialAdditionalDeductionTaxTotal += wages.getSixSpecialDeductions();
                //计算累计应纳税所得额：累计应纳税所得额=累计计税合计-累计专项附加扣除-5000*月份
                taxableIncome=totalTax-specialAdditionalDeductionTaxTotal-deductionOfExpensesTaxTotal;
                //累计个税
                personalIncomeTotalTax += taxableIncome(taxableIncome, personalIncomeTotalTax);
            }
            //计算截至到指定月份
            specialAdditionalDeductionTaxTotal+=specialAdditionalDeduction;
            totalTax+=totalTaxThisMonth;
            deductionOfExpensesTaxTotal += 5000;
            //计算累计应纳税所得额：累计应纳税所得额=累计计税合计-累计专项附加扣除-5000*月份
            taxableIncome=totalTax-specialAdditionalDeductionTaxTotal-deductionOfExpensesTaxTotal;
            //指定月份的个税
            return taxableIncome(taxableIncome, personalIncomeTotalTax);
        }
        return 0.00;
    }

    /**
     * 根据应纳税所得额计算个税
     *
     * @param taxableIncome
     */
    public Double taxableIncome(Double taxableIncome, Double personalIncomeTotalTax) {
        Double taxRate = 0.00;//预扣税率
        Double quickCalculationDeduction = 0.00;//速算扣除数
        //根据`累计应纳税所得额` 判断 `预扣税率`,`速算扣除数`
        if (taxableIncome <= 36000.00) {
            taxRate = 0.03;
            quickCalculationDeduction = 0.00;
        } else if (36000.00 < taxableIncome && taxableIncome <= 144000.00) {
            taxRate = 0.1;
            quickCalculationDeduction = 2520.00;
        } else if (144000.00 < taxableIncome && taxableIncome <= 300000.00) {
            taxRate = 0.2;
            quickCalculationDeduction = 16920.00;
        } else if (300000.00 < taxableIncome && taxableIncome <= 420000.00) {
            taxRate = 0.25;
            quickCalculationDeduction = 31920.00;
        } else if (420000.00 < taxableIncome && taxableIncome <= 660000.00) {
            taxRate = 0.3;
            quickCalculationDeduction = 52920.00;
        } else if (660000.00 < taxableIncome && taxableIncome <= 960000.00) {
            taxRate = 0.35;
            quickCalculationDeduction = 85920.00;
        } else if (960000.00 < taxableIncome) {
            taxRate = 0.45;
            quickCalculationDeduction = 181920.00;
        }
        //累计个税 = 累计应纳税所得额*预扣税率 - 速算扣除数
        Double accumulatedPersonalIncomeTax = taxableIncome * taxRate - quickCalculationDeduction;
        //当月个税
        Double tax = accumulatedPersonalIncomeTax - personalIncomeTotalTax;
        if (tax < 0) {
            tax = 0.00;
        }
        return tax;
    }


    /**
     * 修改工资信息
     *
     * @param wages
     * @return
     */
    @RequestMapping(value = "/updWages", method = {RequestMethod.POST})
    @ResponseBody
    public String updWags(@RequestBody Wages wages) {
        //获取工资编号
        Integer wagesId = wages.getId();
        int result = 0;

        if (wagesId != null && !"".equals(wagesId)) {
            result = wagsService.updWags(wages);
        } else {
            String dateTime = wages.getDate() + "-01";
            wages.setDate(dateTime);
            wages.setCreated(dateTime);
            wages.setHighTemperatureSubsidy(0.00);
            result = wagsService.addThisMonthWags(wages);
        }
        if (result > 0) {
            return JSON.toJSONString(Type.SUCCESS);
        } else {
            return JSON.toJSONString(Type.CANCEL);
        }
    }


    /**-------------------------------------工资岗位----------------------------------------*/

    /**
     * 获取工资岗位下拉框
     * @return
     */
    @RequestMapping("/getWagesPostMap")
    @ResponseBody
    public String getWagesPostMap(){
        List<Map<String,String>> list=wageBaseService.getWagesPostMap();
        return JSON.toJSONString(list);
    }

    /**
     * 获取工资岗位等级下拉框
     * @return
     */
    @RequestMapping("/getPostGradeMap")
    @ResponseBody
    public String getPostGradeMap(@RequestParam("id") Integer id){
        if(id==null){
            return JSON.toJSONString(null);
        }
        List<Map<String,String>> list=wageBaseService.getPostGradeMap(id);
        return JSON.toJSONString(list);
    }

}
