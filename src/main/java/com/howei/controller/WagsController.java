package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.*;
import com.howei.service.*;
import com.howei.util.DateFormat;
import com.howei.util.ListUtils;
import com.howei.util.Result;
import com.howei.util.Type;
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
        //user为空
        if (users == null) {
            Result result = new Result(0, null, 0, "noUser");
            return result;
        }
        employeeId = users.getEmployeeId();
        Map map = new HashMap<>();
        map.put("month", month + "-01");
        //判断是否存在以下角色，
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("财务") || subject.hasRole("总部管理员")) {
            empIdStr = null;
        } else {
            empIdStr += users.getEmployeeId() + ",";
            List<String> employeeIdList = new ArrayList<>();

            List<Employee> rootList = employeeService.getEmployeeByManager(employeeId);

            List<Employee> empList = employeeService.getEmployeeByManager(0);
            ListUtils.getChildEmployeeId(rootList, empList, employeeIdList, null);
            for (String employeeIdStr : employeeIdList) {
                empIdStr += employeeIdStr + ",";
            }
            if (empIdStr != null && !empIdStr.equals("")) {
                empIdStr = empIdStr.substring(0, empIdStr.lastIndexOf(","));
            }
        }
        map.put("empId", empIdStr);
        List<Wages> list = wagsService.getWagsList(map);


        Result result = new Result();
        result.setCode(0);
        result.setCount(list.size());
        result.setData(list);
        return result;
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
     */
    @RequestMapping("/taxation")
    @ResponseBody
    public Result taxation(@RequestBody Wages wagesDTO) {

        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        Integer employeeId = wagesDTO.getEmployeeId();
        paramMap.put("employeeId", employeeId);
        String date = wagesDTO.getDate();
        paramMap.put("cycle", date + "-01");
        //获取指定日期的上一月份
        String lastYearMonth = DateFormat.getYearMonthByMonth(date, -1);

//        Wages wages = wagsService.getWagesByMap(paramMap);
//        if (wages == null) {
//            wages = new Wages();
//        }

        BigDecimal bd;
        //岗位工资
        Double basePay = wagesDTO.getBasePay();
        //职级工资
        Double positionSalary = wagesDTO.getPositionSalary();
        //绩效基数
        Double meritBase = 0D;

        //工资小计=岗位工资+职级工资
        Double wageSubtotal = 0D;
        if (basePay.compareTo(0D) != 0 || positionSalary.compareTo(0D) != 0) {
            meritBase = (basePay + positionSalary) / 2.0;
            wageSubtotal = basePay + positionSalary;
        }

        resultMap.put("meritBase", new BigDecimal(meritBase).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        resultMap.put("wageSubtotal", new BigDecimal(wageSubtotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());


        paramMap.put("cycle", lastYearMonth + "-01");
        Assessment assessment = behaviorService.getAssessmentByEmployeeId(paramMap);
        Wages wagesLastMonth = wagsService.getWagesByMap(paramMap);
        if (wagesLastMonth == null || !date.substring(0, 4).equals(lastYearMonth.substring(0, 4))) {
            wagesLastMonth = Wages.initTotalValue(employeeId);
        }
        //综合绩效 (绩效系数)
        double comprehensivePerformance = 0.00;
        if (assessment != null) {
            double score1 = assessment.getScore1();
            double score2 = assessment.getScore2();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
            double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setNetPerformance(netPerformance);
            //综合绩效=净绩效
            bd = new BigDecimal(netPerformance);
            comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //绩效系数


        //绩效工资
        Double meritPay = 0D;
        if (meritBase.compareTo(0D) != 0) {
            meritPay = meritBase * comprehensivePerformance;
        }
        resultMap.put("meritPay", new BigDecimal(meritPay).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //其他
        Double other = wagesDTO.getOther();

        //餐补
        Double foodSupplement = wagesDTO.getFoodSupplement();
        //高温补贴
        Double highTemperatureSubsidy = wagesDTO.getHighTemperatureSubsidy() == null ? 0D : wagesDTO.getHighTemperatureSubsidy();
        //加班补贴
        Double overtimeSubsidy = wagesDTO.getOvertimeSubsidy();
        //补贴小计
        Double subTotalOfSubsidies = other + foodSupplement + highTemperatureSubsidy + overtimeSubsidy;
        resultMap.put("subTotalOfSubsidies", new BigDecimal(subTotalOfSubsidies).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //应发工资=(岗位工资+职级工资)/2+绩效工资
        Double wagesPayable = (basePay + positionSalary) / 2.0 + meritPay;
        resultMap.put("wagesPayable", new BigDecimal(wagesPayable).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //应发合计=应发工资+补贴小计
        Double totalPayable = wagesPayable + subTotalOfSubsidies;
        resultMap.put("totalPayable", new BigDecimal(totalPayable).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //养老保险
        Double endowmentInsurance = wagesDTO.getEndowmentInsurance() == null ? 0D : wagesDTO.getEndowmentInsurance();

        //失业金
        Double unemploymentBenefits = wagesDTO.getUnemploymentBenefits() == null ? 0D : wagesDTO.getUnemploymentBenefits();
        //医疗保险
        Double medicalInsurance = wagesDTO.getMedicalInsurance() == null ? 0D : wagesDTO.getMedicalInsurance();
        //公积金
        Double accumulationFund = wagesDTO.getAccumulationFund() == null ? 0D : wagesDTO.getAccumulationFund();
        //其他扣款
        Double otherDeductions = wagesDTO.getOtherDeductions() == null ? 0D : wagesDTO.getOtherDeductions();
        //工会费
        Double unionFees = wagesDTO.getUnionFees() == null ? 0D : wagesDTO.getUnionFees();

        //扣款合计 =养老保险+失业金+医疗保险+公积金+其他扣款+工会费
        Double totalDeduction = endowmentInsurance + unemploymentBenefits + medicalInsurance + accumulationFund + otherDeductions + unionFees;
        resultMap.put("totalDeduction", new BigDecimal(totalDeduction).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //累计附加扣除
        Double specialAdditionalDeduction = wagesDTO.getSpecialAdditionalDeduction();

        //计税合计=应发合计-扣款合计
        Double totalTax = totalPayable - totalDeduction;
        resultMap.put("totalTax", new BigDecimal(totalTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //累计收入额=上个月的累计收入额+(当月应发合计-其他扣款)
        Double incomeTotal = wagesLastMonth.getIncomeTotal() + totalPayable;
        resultMap.put("incomeTotal", new BigDecimal(incomeTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //累计费用减免
        Double deductionOfExpensesTaxTotal = wagesLastMonth.getDeductionOfExpensesTaxTotal() + 5000;
        resultMap.put("deductionOfExpensesTaxTotal", new BigDecimal(deductionOfExpensesTaxTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //累计专项扣除
        Double specialDeductionTaxTotal = wagesLastMonth.getSpecialDeductionTaxTotal() + totalDeduction - unionFees;
        resultMap.put("specialDeductionTaxTotal", new BigDecimal(specialDeductionTaxTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //累计附加专项扣除
        Double specialAdditionalDeductionTaxTotal = wagesLastMonth.getSpecialAdditionalDeductionTaxTotal() + specialAdditionalDeduction;
        resultMap.put("specialAdditionalDeductionTaxTotal", new BigDecimal(specialAdditionalDeductionTaxTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //累计其他扣除
        double communicationFee = 0;
        String laowupaiqian = wagesDTO.getLaowupaiqian();
        if ("否".equals(laowupaiqian)) {
            communicationFee = 300;
        }
        Double otherDeductionTaxTotal = wagesLastMonth.getOtherDeductionTaxTotal() + unionFees + communicationFee;

        resultMap.put("otherDeductionTaxTotal", new BigDecimal(otherDeductionTaxTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //累计计税合计
        Double taxableIncomeTotal = 0.00;//累计应缴纳税所得额
        Double totalTaxTotal = wagesLastMonth.getTotalTaxTotal() + totalTax;//累计计税合计
        resultMap.put("totalTaxTotal", new BigDecimal(totalTaxTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        paramMap = new HashMap();
        paramMap.put("month", lastYearMonth + "-01");
        paramMap.put("employeeId", employeeId);
        List<Wages> listWages = wagsService.getWagesToTax(paramMap);
        Double CommunicationFeeTotal = 0.00;
        if (listWages != null) {
            for (Wages wagesOtherMonth : listWages) {
                System.out.println(wagesOtherMonth);
                //计税合计
//                            totalTaxTotal += wagesOtherMonth.getTotalTax();
                String dateWagesOtherMonth = wagesOtherMonth.getDate();
                if ("2021-04-01".compareTo(dateWagesOtherMonth) <= 0 && "否".equals(laowupaiqian)) {
                    CommunicationFeeTotal += 300;
                }

            }
            if ("否".equals(laowupaiqian)) {
                CommunicationFeeTotal += 300;
            }
            System.out.println(CommunicationFeeTotal);
//                        System.out.println(totalTaxTotal + totalTax);
            //计算累计应纳税所得额：累计应纳税所得额=累计计税合计 -（当前月份-3）*300-累计专项附加扣除-5000*月份
            taxableIncomeTotal = totalTaxTotal - CommunicationFeeTotal - specialAdditionalDeductionTaxTotal - deductionOfExpensesTaxTotal;
        }
        resultMap.put("taxableIncomeTotal", new BigDecimal(taxableIncomeTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());


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
        Double accumulatedPersonalIncomeTax = taxableIncomeTotal * taxRate - quickCalculationDeduction;
        //当月个税=累计个税-历史月个调税
        Double tax = accumulatedPersonalIncomeTax - wagesLastMonth.getIndividualIncomeTaxTotal();
        tax = (tax < 0) ? 0D : tax;
        //累计已缴纳个税
        Double individualIncomeTaxPaidTotal = wagesLastMonth.getIndividualIncomeTaxTotal();
        resultMap.put("individualIncomeTaxPaidTotal", new BigDecimal(individualIncomeTaxPaidTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //累计个税
        resultMap.put("accumulatedPersonalIncomeTax", new BigDecimal(accumulatedPersonalIncomeTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //个税
        resultMap.put("individualTaxAdjustment", new BigDecimal(tax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //实发工资
        Double netSalary = totalTax - tax;
        resultMap.put("netSalary", new BigDecimal(netSalary).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return Result.ok(1, resultMap);
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
                    if (assessment != null) {
                        double score1 = assessment.getScore1();
                        double score2 = assessment.getScore2();
                        jianban = assessment.getJiaban();//加班
                        int kapqin = assessment.getKaoqin();//考勤
                        //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
                        bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
                        double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        assessment.setNetPerformance(netPerformance);
                        //综合绩效=净绩效+加班*0.01
                        bd = new BigDecimal(netPerformance);
                        comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //餐补=20*考勤
                        foodSupplement = 20 * kapqin;
                    }
                    //绩效系数
                    wages.setPerformanceCoefficient(comprehensivePerformance);

                    //绩效工资=绩效基数*绩效系数
                    bd = new BigDecimal(wages.getMeritBase() * comprehensivePerformance);
                    Double meritPay = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setMeritPay(meritPay);
                    //工资小计=岗位工资+职级工资
                    Double wageSubtotal = wages.getWageSubtotal();//工资小计
                    //应发工资==(岗位工资+职级工资)/2+绩效工资
                    bd = new BigDecimal(wageSubtotal / 2.0 + meritPay);
                    Double wagesPayable = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setWagesPayable(wagesPayable);
                    //餐补
                    bd = new BigDecimal(foodSupplement);
                    foodSupplement = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    wages.setFoodSupplement(foodSupplement);

                    //加班补贴=  加班 * 0.01 * 21.875
                    wages.setOvertimeSubsidy(jianban * 0.01 * 30D);
                    //补贴小计=高温补贴+餐补 +其他+加班补贴
                    bd = new BigDecimal(wages.getHighTemperatureSubsidy() + foodSupplement + wages.getOther());
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

                    System.out.println("wagesLastMonth::" + wagesLastMonth);
                    System.out.println("wagesthisMonth::" + wages);
                    //累计收入额=上个月的累计收入额+(当月应发合计-其他扣款)
                    Double incomeTotal = wagesLastMonth.getIncomeTotal() + totalPayable;
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
                    //累计其他扣除
                    double communicationFee = 0;
                    if ("否".equals(wages.getLaowupaiqian())) {
                        communicationFee = 300;
                    }
                    Double otherDeductionTaxTotal = wagesLastMonth.getOtherDeductionTaxTotal() + wages.getUnionFees() + communicationFee;
                    wages.setOtherDeductionTaxTotal(otherDeductionTaxTotal);

                    //累计计税合计
                    Double taxableIncomeTotal = 0.00;//累计应缴纳税所得额
                    Double totalTaxTotal = wagesLastMonth.getTotalTaxTotal() + totalTax;//累计计税合计
                    wages.setTotalTaxTotal(totalTaxTotal);
                    map = new HashMap();
                    map.put("month", month + "-01");
                    map.put("employeeId", employeeId);
                    List<Wages> listWages = wagsService.getWagesToTax(map);
                    Double CommunicationFeeTotal = 0.00;
                    if (listWages != null) {
                        for (Wages wagesOtherMonth : listWages) {
                            System.out.println(wagesOtherMonth);
                            //计税合计
//                            totalTaxTotal += wagesOtherMonth.getTotalTax();
                            String date = wagesOtherMonth.getDate();
                            if ("2021-04-01".compareTo(date) <= 0 && "否".equals(wages.getLaowupaiqian())) {
                                CommunicationFeeTotal += 300;
                            }

                        }
                        if ("否".equals(wages.getLaowupaiqian())) {
                            CommunicationFeeTotal += 300;
                        }
                        System.out.println(CommunicationFeeTotal);
//                        System.out.println(totalTaxTotal + totalTax);
                        //计算累计应纳税所得额：累计应纳税所得额=累计计税合计 -（当前月份-3）*300-累计专项附加扣除-5000*月份
                        taxableIncomeTotal = wages.getTotalTaxTotal() - CommunicationFeeTotal - wages.getSpecialAdditionalDeductionTaxTotal() - deductionOfExpensesTaxTotal;
                    }

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
                    Double accumulatedPersonalIncomeTax = taxableIncomeTotal * taxRate - quickCalculationDeduction;
                    //当月个税=累计个税-历史月个调税
                    Double tax = accumulatedPersonalIncomeTax - wagesLastMonth.getIndividualIncomeTaxTotal();
                    tax = (tax < 0) ? 0D : tax;
                    //累计已缴纳个税
                    Double individualIncomeTaxPaidTotal = wagesLastMonth.getIndividualIncomeTaxTotal();
                    wages.setIndividualIncomeTaxPaidTotal(individualIncomeTaxPaidTotal);
                    //累计个税
                    //Double individualIncomeTaxTotal = wagesLastMonth.getIndividualIncomeTaxTotal() + tax;
                    wages.setIndividualIncomeTaxTotal(accumulatedPersonalIncomeTax);
                    //实发工资
                    Double netSalary = totalTax - tax;
                    wages.setIndividualTaxAdjustment(tax);
                    wages.setNetSalary(netSalary);


                    wagsService.updWags(wages);
                }
            } catch (Exception e) {
                System.out.println("wages::" + wages.getId() + "::" + wages.getUserNumber() + "::" + wages.getEmployeeName());
                msg += "(" + wages.getUserNumber() + ")" + wages.getEmployeeName();
                e.printStackTrace();
                break;
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

}
