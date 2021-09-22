package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.*;
import com.howei.service.*;
import com.howei.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wa/wags")
public class WagsController {

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

    @Autowired
    private WorkingService workingService;


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

        String empIdStr;
        if (month == null || "".equals(month)) {
            month = DateFormat.ThisMonth();
        }
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        Integer employeeId = users.getEmployeeId();
        Map map = new HashMap<>();
        map.put("month", month + "-01");
        //判断是否存在以下角色，
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("财务") || subject.hasRole("总部管理员")) {
            empIdStr = null;
        } else {
            List<String> employeeIdList = new ArrayList<>();
            employeeIdList.add(employeeId.toString());
            List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);
            List<Employee> empList = employeeService.getEmployeeByManager(0);
            ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
            empIdStr = employeeIdList.stream().collect(Collectors.joining(","));
        }
        map.put("empId", empIdStr);
        List<Wages> wages = wagsService.getWagsList(map);

        return Result.ok(wages.size(), wages);
    }

    /**
     * 工资编辑加载数据
     * 获取综合绩效
     * 获取餐补
     *
     * @param cycle
     * @param employeeId
     * @return
     */
    @RequestMapping("/getPerformanceCoefficientByEmployeeId")
    @ResponseBody
    public Map getPerformanceCoefficientByEmployeeId(String cycle, String employeeId) {
        Map map = new HashMap();
        if (cycle != null && !cycle.equals("")) {
            //获取指定日期的上一月份
            cycle = DateFormat.getYearMonthByMonth(cycle, -1);
            map.put("cycle", cycle);
        }
        if (employeeId != null && !employeeId.equals("")) {
            map.put("employeeId", employeeId);
        }
        Assessment assessment = behaviorService.getAssessmentByEmployeeId(map);
        if (assessment != null) {
            double score1 = assessment.getScore1();
            double score2 = assessment.getScore2();
            double jianban = assessment.getJiaban();//加班
            int kapqin = assessment.getKaoqin();//考勤
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
            double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setNetPerformance(netPerformance);
            //综合绩效=净绩效
            bd = new BigDecimal(netPerformance);
            double comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //餐补=20*考勤
            double foodSupplement = 20 * kapqin;
            Map result = new HashMap();
            result.put("performanceCoefficient", comprehensivePerformance);
            result.put("foodSupplement", foodSupplement);
            return result;
        }
        return null;
    }


    /**
     * monthStart 初始月,年月
     * monthEnd 复制月,年月
     * confirmType 确认按钮,1确认
     *
     * @return
     */
    @RequestMapping("/copyToThisMonthWags")
    @ResponseBody
    public Result copyToThisMonthWags(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Users users = (Users) subject.getPrincipal();
        if (users == null) {
            return Result.fail(ResultEnum.NO_USER);
        }
        String monthStart = request.getParameter("monthStart");
        String monthEnd = request.getParameter("monthEnd");
        String confirmType = request.getParameter("confirmType");
        Map map = new HashMap<>();
        map.put("month", monthEnd + "-01");
        List<Wages> list = wagsService.getSimpleWagsListByMap(map);
        if (list != null && list.size() > 0) {
            if ("1".equals(confirmType)) {
                wagsService.deleteByDate(monthEnd + "-01");
            } else {
                return Result.fail(ResultEnum.HAVE_RECORD_TO_OVERWRITE);
            }
        }
        map.put("month", monthStart + "-01");
        list = wagsService.getWagsList(map);
        if (list == null) {
            return Result.fail(ResultEnum.HAVE_NO_RECORD);
        }

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
                wages.setSubTotalOfSubsidies(0.00);//补贴小计
                if (wages.getWagesPostId() != null) {
                    WagesPost wagesPost = wageBaseService.getWagesPostById(wages.getWagesPostId());
                    wages.setBasePay(wagesPost.getWagesPostWage());//基本工资
                    PostGrade postGrade = wageBaseService.getPostBaseById(wages.getPostGradeId());
                    wages.setPositionSalary(postGrade.getPostGradeWage());//职级工资
                    //工资小计=岗位工资+职级工资
                    wages.setWageSubtotal(wagesPost.getWagesPostWage() + postGrade.getPostGradeWage());//工资小计
                    //绩效工资
                    wages.setMeritPay(wages.getWageSubtotal() / 2.0);
                }
                wagesList.add(wages);
            }
        }
        int result = wagsService.copyToThisMonthWags(wagesList);
        if (result > 0) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 生成本月工资信息
     * 工资小计=基本工资+技能工资+职务工资+工龄工资+其他+绩效工资
     * 应发工资=(基本工资+技能工资+职务工资+工龄工资+其他)+绩效工资*绩效系数
     * 应发合计=工资工资+补贴小记
     * 扣款合计=养老保险+失业金+医疗保险+公积金+其他扣款+工会费
     * 计税合计=应发合计-扣款合计
     * <p>
     * 第一步，求“累计应纳税所得”
     * <p>
     * 累计应纳税所得=累计计税合计-（当前月份-3）*300- *当前月份-5000*工作月份
     * <p>
     * *300是通讯费，从4月起每月-300
     * *当前月份 是发放工资的月份，比如现在是7月
     * *工作月份 是员工的工龄（按月统计），工作月份=当前月份-入职月份，比如1月入职，工作月份=7-1=6；如果1月之前就已经在职，工作月份=当前月份
     * <p>
     * 第二步，求“个调税”
     * 分解成两小步 即
     * 1、累计个税=累计应纳税所得*税率-速算扣除数
     * 2、当月个调税=累计个税-历史月个调税【比如  7月个调税=累计个税-1到6月个调税合计】
     * <p>
     * 也就是 当月个调税=累计应纳税所得*税率-速算扣除数-历史月个调税
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

    /**
     * 核算指定月份的工资
     *
     * @param month 月份
     * @param id    工资id
     * @return
     */
    @RequestMapping("thisMonthCalculation")
    @ResponseBody
    public String thisMonthCalculation(@RequestParam("month") String month, @RequestParam(required = false) String id) {
        Users users = this.getPrincipal();
        if (users == null) {
            return JSON.toJSONString(Type.noUser);
        }
        if (StringUtils.isEmpty(month)) {
            return JSON.toJSONString(Type.noParameters);
        }
        Map map = new HashMap<>();
        map.put("month", month + "-01");
        if (!StringUtils.isEmpty(id)) {
            map.put("wagesIds", id);
        }
        //当月需核算的工资数据
        List<Wages> wageslist = wagsService.getWagsList(map);

        //获取指定日期的上一月份
        String lastYearMonth = DateFormat.getYearMonthByMonth(month, -1);
        if (wageslist == null || wageslist.size() == 0) {
            return JSON.toJSONString(Type.success);
        }
        //当月天数
        int daysOfMonth = DateFormat.getDaysOfMonth(lastYearMonth + "-1");
        String msg = "";
        for (int i = 0; i < wageslist.size(); i++) {

            Wages wages = wageslist.get(i);
            try {
                if (wages.getId() != null) {
                    Integer employeeId = wages.getEmployeeId();
                    map.put("cycle", lastYearMonth);

                    if (employeeId != null && !employeeId.equals("")) {
                        map.put("employeeId", employeeId);
                    }

                    //该员工的月绩效
                    Assessment assessment = behaviorService.getAssessmentByEmployeeId(map);
                    map.put("cycle", lastYearMonth + "-01");
                    Wages wagesLastMonth = wagsService.getWagesByMap(map);
                    if (wagesLastMonth == null || !month.substring(0, 4).equals(lastYearMonth.substring(0, 4))) {
                        wagesLastMonth = Wages.initTotalValue(employeeId);
                    }
                    //绩效基数=(岗位工资+职级工资)/2
                    BigDecimal bd = new BigDecimal((wages.getBasePay() + wages.getPositionSalary()) / 2.0);
                    double meritBase = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setMeritBase(meritBase);
                    //综合绩效 (绩效系数)
                    double comprehensivePerformance = 0.00;
                    double foodSupplement = 0.00;//餐补
                    double jianban = 0D;
                    int kaoqin = 0;
                    if (assessment != null) {
                        double score1 = assessment.getScore1();
                        double score2 = assessment.getScore2();
                        jianban = assessment.getJiaban();//加班
                        kaoqin = assessment.getKaoqin();//考勤
                        //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
                        bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
                        double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        assessment.setNetPerformance(netPerformance);
                        //综合绩效=净绩效+加班*0.01
                        bd = new BigDecimal(netPerformance);
                        comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //餐补=20*考勤
                        foodSupplement = 20 * kaoqin;
                    }
                    wages.setJiaban(jianban);
                    wages.setWorkAttendance(kaoqin);
                    //绩效系数
                    if ("当月入职".equals(wages.getIsChanged())) {
                        comprehensivePerformance = 1D;
                    }
                    wages.setPerformanceCoefficient(comprehensivePerformance);

                    //绩效工资=绩效基数*绩效系数
                    bd = new BigDecimal(wages.getMeritBase() * comprehensivePerformance);
                    Double meritPay = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setMeritPay(meritPay);
                    //工资小计=岗位工资+职级工资
                    bd = new BigDecimal(wages.getBasePay() + wages.getPositionSalary());
                    Double wageSubtotal = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setWageSubtotal(wageSubtotal);
                    //应发工资==(岗位工资+职级工资)/2+绩效工资 * (N/D | 0.8)
                    bd = new BigDecimal(wageSubtotal / 2.0 + meritPay);
                    if ("当月入职".equals(wages.getIsChanged())) {
                        map.clear();
                        map.put("employeeId", employeeId);
                        map.put("date", lastYearMonth);
                        map.put("changedType", "0");
                        int maxMinWorkAttendanceDay = workingService.getMaxMinWorkAttendanceDayByMap(map);
                        bd = new BigDecimal((wageSubtotal / 2.0 + meritPay) * (daysOfMonth - maxMinWorkAttendanceDay) / daysOfMonth);
                    } else if ("当月离职(正常)".equals(wages.getIsChanged())) {
                        map.clear();
                        map.put("employeeId", employeeId);
                        map.put("date", lastYearMonth);
                        map.put("changedType", "1");
                        int maxMinWorkAttendanceDay = workingService.getMaxMinWorkAttendanceDayByMap(map);
                        bd = new BigDecimal((wageSubtotal / 2.0 + meritPay) * maxMinWorkAttendanceDay / daysOfMonth);
                    } else if ("当月离职(试用期)".equals(wages.getIsChanged())) {
                        map.clear();
                        map.put("employeeId", employeeId);
                        map.put("date", lastYearMonth);
                        map.put("changedType", "1");
                        int maxMinWorkAttendanceDay = workingService.getMaxMinWorkAttendanceDayByMap(map);
                        bd = new BigDecimal(((wageSubtotal / 2.0 + meritPay) * maxMinWorkAttendanceDay / daysOfMonth) * 0.8);
                    } 
                    Double wagesPayable = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    wages.setWagesPayable(wagesPayable);
                    //餐补
                    bd = new BigDecimal(foodSupplement);
                    foodSupplement = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setFoodSupplement(foodSupplement);
                    //加班补贴=  加班 * 30
                    wages.setOvertimeSubsidy(jianban * 30D);
                    //补贴小计=高温补贴+餐补 +其他+加班补贴
                    bd = new BigDecimal(wages.getHighTemperatureSubsidy() + foodSupplement + wages.getOther() + (jianban * 30D));
                    Double subTotalOfSubsidies = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setSubTotalOfSubsidies(subTotalOfSubsidies);
                    //应发合计=应发工资+补贴小计
                    Double totalPayable = wagesPayable + subTotalOfSubsidies;
                    wages.setTotalPayable(totalPayable);
                    //扣款合计 =养老保险+失业金+医疗保险+公积金+其他扣款+工会费
                    double totalDeduction = wages.getEndowmentInsurance() + wages.getUnemploymentBenefits() + wages.getMedicalInsurance() + wages.getAccumulationFund() + wages.getOtherDeductions() + wages.getUnionFees();
                    bd = new BigDecimal(totalDeduction);
                    totalDeduction = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setTotalDeduction(totalDeduction);
                    //计税合计=应发合计-扣款合计
                    Double totalTax = totalPayable - wages.getTotalDeduction();
                    //判断是否是劳务派遣人员
                    //totalTax = "否".equals(wages.getLaowupaiqian()) ? totalTax - 300 : totalTax;
                    //计税合计
                    wages.setTotalTax(totalTax);

                    //累计收入额=上个月的累计收入额+(当月应发合计-其他扣款)
                    Double incomeTotal = wagesLastMonth.getIncomeTotal() + totalPayable - wages.getOtherDeductions();
                    wages.setIncomeTotal(incomeTotal);
                    //累计费用减免
                    Double deductionOfExpensesTaxTotal = wagesLastMonth.getDeductionOfExpensesTaxTotal() + 5000;
                    wages.setDeductionOfExpensesTaxTotal(deductionOfExpensesTaxTotal);
                    //累计专项扣除
                    Double specialDeductionTaxTotal = wagesLastMonth.getSpecialDeductionTaxTotal() + totalDeduction - wages.getUnionFees();
                    wages.setSpecialDeductionTaxTotal(specialDeductionTaxTotal);
                    //累计附加专项扣除
                    Double specialAdditionalDeductionTaxTotal = wagesLastMonth.getSpecialAdditionalDeductionTaxTotal() + wages.getSpecialAdditionalDeduction();
                    wages.setSpecialAdditionalDeductionTaxTotal(specialAdditionalDeductionTaxTotal);
                    //累计应纳税所得额
                    double communicationFee = 0;
                    if ("否".equals(wages.getLaowupaiqian())) {
                        communicationFee = 300;
                    }
                    //累计其他扣除 =上月累计其他扣除+工会费+通讯费(非派遣)
                    Double otherDeductionTaxTotal = wagesLastMonth.getOtherDeductionTaxTotal() + wages.getUnionFees() + communicationFee;
                    wages.setOtherDeductionTaxTotal(otherDeductionTaxTotal);

                    Double totalTaxTotal = wagesLastMonth.getTotalTaxTotal() + totalTax;//累计计税合计
                    wages.setTotalTaxTotal(totalTaxTotal);
                    map = new HashMap();
                    map.put("month", month + "-01");
                    map.put("employeeId", employeeId);
                    List<Wages> listWages = wagsService.getWagesToTax(map);
                    //累计应缴纳谁所得额=累计收入额-累计费用见面-累计专项扣除-累计专项附加扣除-累计其他扣除
                    //累计应缴纳税所得额
                    Double taxableIncomeTotal = incomeTotal - deductionOfExpensesTaxTotal - specialDeductionTaxTotal - specialAdditionalDeductionTaxTotal - otherDeductionTaxTotal;
                    wages.setTaxableIncomeTotal(taxableIncomeTotal);


                    Double taxRate = 0.00;//预扣税率
                    Double quickCalculationDeduction = 0.00;//速算扣除数
                    //根据`累计应纳税所得额` 判断 `预扣税率`,`速算扣除数`
                    if (taxableIncomeTotal <= 36000.00) {
                        taxRate = 0.03;
                        quickCalculationDeduction = 0.00;
                    } else if (36000.00 < taxableIncomeTotal && taxableIncomeTotal <= 144000.00) {
                        taxRate = 0.1;
                        quickCalculationDeduction = 2520.00;
                    } else if (144000.00 < taxableIncomeTotal && taxableIncomeTotal <= 300000.00) {
                        taxRate = 0.2;
                        quickCalculationDeduction = 16920.00;
                    } else if (300000.00 < taxableIncomeTotal && taxableIncomeTotal <= 420000.00) {
                        taxRate = 0.25;
                        quickCalculationDeduction = 31920.00;
                    } else if (420000.00 < taxableIncomeTotal && taxableIncomeTotal <= 660000.00) {
                        taxRate = 0.3;
                        quickCalculationDeduction = 52920.00;
                    } else if (660000.00 < taxableIncomeTotal && taxableIncomeTotal <= 960000.00) {
                        taxRate = 0.35;
                        quickCalculationDeduction = 85920.00;
                    } else if (960000.00 < taxableIncomeTotal) {
                        taxRate = 0.45;
                        quickCalculationDeduction = 181920.00;
                    }
                    //累计个税 = 累计应纳税所得额*预扣税率 - 速算扣除数
                    Double individualIncomeTaxTotal = taxableIncomeTotal * taxRate - quickCalculationDeduction;
                    wages.setIndividualIncomeTaxTotal(individualIncomeTaxTotal);
                    //当月个税=累计个税-上月累计个税
                    Double tax = (individualIncomeTaxTotal < 0 ? 0D : individualIncomeTaxTotal) - wagesLastMonth.getIndividualIncomeTaxTotal() < 0 ? 0D : wagesLastMonth.getIndividualIncomeTaxTotal();
                    tax = (tax < 0) ? 0D : tax;
                    wages.setIndividualTaxAdjustment(tax);
                    //累计已缴纳个税=上月累计已缴纳个税+上月个调税
                    Double individualIncomeTaxPaidTotal = wagesLastMonth.getIndividualIncomeTaxTotal() + (wagesLastMonth.getIndividualTaxAdjustment());
                    wages.setIndividualIncomeTaxPaidTotal(individualIncomeTaxPaidTotal);
                    //实发工资
                    Double netSalary = totalTax - tax;
                    wages.setNetSalary(netSalary);


                    wagsService.updWags(wages);
                }
            } catch (Exception e) {
                System.out.println("wages::" + wages.getId() + "::" + wages.getUserNumber() + "::" + wages.getEmployeeName());
                msg += "(" + wages.getUserNumber() + ")" + wages.getEmployeeName();
                e.printStackTrace();
                continue;
            }
        }
        if (!"".equals(msg)) {
            return JSON.toJSONString(msg);
        }
        return JSON.toJSONString(Type.success);
    }

    /**-------------------------------------工资岗位----------------------------------------*/

    /**
     * 获取工资岗位下拉框
     *
     * @return
     */
    @RequestMapping("/getWagesPostMap")
    @ResponseBody
    public String getWagesPostMap() {
        List<Map<String, String>> list = wageBaseService.getWagesPostMap();
        return JSON.toJSONString(list);
    }

    /**
     * 获取工资岗位等级下拉框
     *
     * @return
     */
    @RequestMapping("/getPostGradeMap")
    @ResponseBody
    public String getPostGradeMap(@RequestParam("id") Integer id) {
        if (id == null) {
            return JSON.toJSONString(null);
        }
        List<Map<String, String>> list = wageBaseService.getPostGradeMap(id);
        return JSON.toJSONString(list);
    }

    /**
     * @param employeeId
     * @param month
     * @param changedType 0当月入职,1当月离职(正常),2试用期,3当月离职(试用期)
     * @return
     */
    @GetMapping("/getPayableWageFormula")
    @ResponseBody
    public Result getWageLabel(Integer employeeId, String month, String changedType) {
        if ("2".equals(changedType)) {
            return Result.ok(1, "*0.8");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("employeeId", employeeId);
        //获取指定日期的上一月份
        String lastYearMonth = DateFormat.getYearMonthByMonth(month, -1);
        map.put("date", lastYearMonth);
        map.put("changedType", changedType);
        int maxMinWorkAttendanceDay = workingService.getMaxMinWorkAttendanceDayByMap(map);
        int daysOfMonth = DateFormat.getDaysOfMonth(lastYearMonth + "-1");
        String data = "*" + maxMinWorkAttendanceDay + "/" + daysOfMonth;
        if ("3".equals(changedType)) {
            data = data + "*0.8";
        }
        return Result.ok(1, data);
    }

}
