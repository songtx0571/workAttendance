package com.howei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.howei.mapper.AchievementMapper;
import com.howei.pojo.*;
import com.howei.service.BehaviorService;
import com.howei.service.EmployeeService;
import com.howei.service.WageBaseService;
import com.howei.service.WagsService;
import com.howei.util.DateFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class workAttendanceApplicationTests {

    @Autowired
    private WagsService wagsService;

    @Autowired
    private BehaviorService behaviorService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AchievementMapper achievementMapper;

    @Test
    void contextLoads() {
        List<Achievement> achievementList = achievementMapper.select();
        Map<String, List<Achievement>> collect = achievementList.stream().collect(Collectors.groupingBy(item -> item.getCycle() + "##" + item.getName()));
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (String s : collect.keySet()) {
            JSONObject jsonObject = new JSONObject();
            String[] split = s.split("##");
            jsonObject.put("createMonth", split[0]);
            jsonObject.put("name", split[1]);
            List<Achievement> achievements = collect.get(s);
            for (Achievement achievement : achievements) {
                int result = 0;
                Integer result1 = null;
                try {
                    result1 = Integer.parseInt(achievement.getResult1());
                } catch (Exception e) {

                }
                if (result1 != null && result1 > result) {
                    result = result1;
                }


                Integer result2 = null;
                try {
                    result2 = Integer.parseInt(achievement.getResult2());
                } catch (Exception e) {

                }
                if (result2 != null && result2 > result) {
                    result = result2;
                }


                Integer result3 = null;
                try {
                    result3 = Integer.parseInt(achievement.getResult3());
                } catch (Exception e) {

                }
                if (result3 != null && result3 > result) {
                    result = result3;
                }

                Integer result4 = null;
                try {
                    result4 = Integer.parseInt(achievement.getResult4());
                } catch (Exception e) {

                }
                if (result4 != null && result3 > result) {
                    result = result4;
                }
                jsonObject.put("week" + achievement.getWeek(), result);
            }
            jsonObjectList.add(jsonObject);
        }
        List<JSONObject> collect1 = jsonObjectList.stream()
                .sorted((o1, o2) -> {
                    String c1 = "-" + o1.get("createMonth").toString() + "-" + o1.get("name").toString();
                    String c2 = "-" + o2.get("createMonth").toString() + "-" + o2.get("name").toString();
                    return c1.compareTo(c2);
                }).collect(Collectors.toList());
        for (JSONObject jsonObject : collect1) {
            String row = jsonObject.getString("createMonth") + ",";
            row += jsonObject.getString("name") + ",";
            row += jsonObject.getString("week1") + ",";
            row += jsonObject.getString("week2") + ",";
            row += jsonObject.getString("week3") + ",";
            row += jsonObject.getString("week4") + ",";
            System.out.println(row);
        }

    }


    /**
     * 4月之前工资计算测试
     */
    //@Test
    public void taxation1() {
        String month = "2021-02";
        String employeeId = "318";//员工id
        double specialAdditionalDeduction = 0;//专项扣除
        double totalTaxThisMonth = 5733.62;//计税合计

        //************************************ 个调税计算 *****************************************
        Double tax = this.taxCalculator(employeeId, month, specialAdditionalDeduction, totalTaxThisMonth);
        //************************************ 实发工资 *****************************************
        Double netSalary = Double.valueOf(totalTaxThisMonth) - tax;//实发工资
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("个调税计算:" + df.format(tax));
        System.out.println("实发工资:" + df.format(netSalary));
    }

    /**
     * 4月之前工资计算
     */
    //@Test
    public void taxationBe() {
        String month = "2021-01";
        Map map = new HashMap();
        //获取5月份工资信息
        map.put("month", month + "-01");
        List<Wages> listWages = wagsService.getWagsList(map);
        for (int i = 0; i < listWages.size(); i++) {
            Wages wages = listWages.get(i);
            double sixSpecialDeductions = wages.getSixSpecialDeductions();//六项专项扣除金额
            double totalTax = wages.getTotalTax();//计税合计
            String employeeId = wages.getEmployeeId().toString();//员工id
            System.out.println(i + ";当前操作对象：" + employeeId);
            //************************************ 个调税计算 *****************************************
            Double tax = this.taxCalculator(employeeId, month, sixSpecialDeductions, totalTax);
            //************************************ 实发工资 *****************************************
            Double netSalary = Double.valueOf(totalTax) - tax;//实发工资
            DecimalFormat df = new DecimalFormat("0.00");
            wages.setNetSalary(Double.valueOf(df.format(netSalary)));//实发工资
            wages.setIndividualTaxAdjustment(Double.valueOf(df.format(tax)));//个调税
            //扣款合计=养老保险+医疗保险+公积金+失业金+工会费+其他扣款
            double totalDeduction = wages.getEndowmentInsurance() + wages.getMedicalInsurance() + wages.getAccumulationFund() + wages.getUnemploymentBenefits() + wages.getUnionFees() + wages.getOtherDeductions();
            wages.setTotalDeduction(totalDeduction);
            wagsService.updWags(wages);
        }
    }

    /**
     * 4月包含4月之后工资计算
     *
     * @return
     */
    //@Test
    public void taxation() {
        String month = "2021-04";
        //获取在职人员列表
        Map map = new HashMap();
        map.put("month", month + "-01");
        List<Wages> listWages = wagsService.getWagsList(map);
        for (int i = 0; i < listWages.size(); i++) {
            Wages wages = listWages.get(i);
            double sixSpecialDeductions = wages.getSixSpecialDeductions();//六项专项扣除金额
            double totalTax = wages.getTotalTax();//计税合计
            String employeeId = wages.getEmployeeId().toString();//员工id
            System.out.println(i + ";当前操作对象：" + employeeId);
            //4月后计税合计减300
            double totalTaxSimulation = totalTax - 300;
            //************************************ 个调税计算 *****************************************
            Double tax = this.taxCalculator(employeeId, month, sixSpecialDeductions, totalTaxSimulation);
            //************************************ 实发工资 *****************************************
            Double netSalary = Double.valueOf(totalTax) - tax;//实发工资
            DecimalFormat df = new DecimalFormat("0.00");
            wages.setNetSalary(Double.valueOf(df.format(netSalary)));//实发工资
            wages.setIndividualTaxAdjustment(Double.valueOf(df.format(tax)));//个调税
            //扣款合计=养老保险+医疗保险+公积金+失业金+工会费+其他扣款
            double totalDeduction = wages.getEndowmentInsurance() + wages.getMedicalInsurance() + wages.getAccumulationFund() + wages.getUnemploymentBenefits() + wages.getUnionFees() + wages.getOtherDeductions();
            wages.setTotalDeduction(totalDeduction);
            wagsService.updWags(wages);
        }
    }

    /**
     * 个税计算
     *
     * @param employeeId 员工id
     * @param month      月份
     * @return wages
     */
    public Double taxCalculator(String employeeId, String month, double specialAdditionalDeduction, double totalTaxThisMonth) {
        //累计值
        Double wageSubtotal = 0.00;//工资小计
        Double wagesPayable = 0.00;//应发工资
        Double totalPayable = 0.00;//应发合计
        Double totalDeduction = 0.00;//扣款合计
        Double totalTax = 0.00;//计税合计
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
                double PerformanceCoefficient = getAssessmentByEmployeeId(wages.getDate(), wages.getEmployeeId() + "");
                //工资小计=基本工资+职务工资+绩效工资+其他
                wageSubtotal = wages.getBasePay() + wages.getPositionSalary() + wages.getMeritPay() + wages.getOther();
                //应发工资=（基本工资+职务工资+其他）+绩效工资*绩效系数
                wagesPayable = (wages.getBasePay() + wages.getPositionSalary() + wages.getOther()) + wages.getMeritPay() * PerformanceCoefficient;
                //应发合计=应发工资+补贴小计
                totalPayable = wagesPayable + wages.getFoodSupplement() + wages.getHighTemperatureSubsidy();
                //扣款合计=养老保险+医疗保险+公积金+失业金+工会费+其他扣款
                totalDeduction = wages.getEndowmentInsurance() + wages.getMedicalInsurance() + wages.getAccumulationFund() + wages.getUnemploymentBenefits() + wages.getUnionFees() + wages.getOtherDeductions();
                //计税合计=应发合计-扣款合计
                totalTax = totalTax + (totalPayable - totalDeduction);
                try {
                    if (DateFormat.comparetoTime("2021-04-01 00:00:00", wages.getDate() + " 00:00:00")) {
                        totalTax = totalTax - 300;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //累计减除费用
                deductionOfExpensesTaxTotal += 5000;
                //累计专项扣除
                specialAdditionalDeductionTaxTotal += wages.getSixSpecialDeductions();
                //计算累计应纳税所得额：累计应纳税所得额=累计计税合计-累计专项附加扣除-5000*月份
                taxableIncome = totalTax - specialAdditionalDeductionTaxTotal - deductionOfExpensesTaxTotal;
                //累计个税
                personalIncomeTotalTax += taxableIncome(taxableIncome, personalIncomeTotalTax);
                System.out.println("个税: " + personalIncomeTotalTax);
            }
            //计算截至到指定月份
            specialAdditionalDeductionTaxTotal += specialAdditionalDeduction;
            totalTax += totalTaxThisMonth;
            deductionOfExpensesTaxTotal += 5000;
            //计算累计应纳税所得额：累计应纳税所得额=累计计税合计-累计专项附加扣除-5000*月份
            taxableIncome = totalTax - specialAdditionalDeductionTaxTotal - deductionOfExpensesTaxTotal;
            //指定月份的个税
            return taxableIncome(taxableIncome, personalIncomeTotalTax);
        }
        return 0.00;
    }

    /**
     * 计算综合绩效
     *
     * @param cycle
     * @param employeeId
     * @return
     */
    public double getAssessmentByEmployeeId(String cycle, String employeeId) {
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
            double jianban = assessment.getJiaban();
            //净绩效=(行为* 0.5 + 业绩 * 0.5)/90
            BigDecimal bd = new BigDecimal((score1 * 0.5 + score2 * 0.5) / 90);
            double netPerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            assessment.setNetPerformance(netPerformance);
            //综合绩效=净绩效+加班*0.01
            bd = new BigDecimal(netPerformance + jianban * 0.01);
            double comprehensivePerformance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return comprehensivePerformance;
        }
        return 0.0;
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

    @Test
    public void a() {
        System.out.println(new Date());
        Map<String, Double> mapDayData = new HashMap<>();//初始化日期
        defaultMothData(mapDayData);
        System.out.println(mapDayData.hashCode());
        for (Integer i = 1; i <= 30; i++) {
        }
        System.out.println(new Date());
    }

    //设置当月数据默认为0
    private void defaultMothData(Map map) {
        DecimalFormat df = new DecimalFormat("00");
        for (Integer i = 1; i <= 30; i++) {
            map.put(df.format(i), 0D);
        }
        System.out.println(map.hashCode());
    }

    //设置当月数据默认为0
    private void defaultMothData1(int day) {
        DecimalFormat df = new DecimalFormat("00");
        Map<String, Double> map = new HashMap<>();
        for (Integer i = 1; i <= day; i++) {
            map.put(df.format(i), 0D);
        }
    }

}
