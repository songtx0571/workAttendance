package com.howei.timerUtils;

import com.howei.pojo.Assessment;
import com.howei.pojo.Tax;
import com.howei.pojo.Wages;
import com.howei.service.BehaviorService;
import com.howei.service.WagsService;
import com.howei.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class SchedulerTask {

    @Autowired
    private WagsService wagsService;

    @Autowired
    private BehaviorService behaviorService;

    /**
     * 计算工资
     * 每月5号执行
     */
    //@Scheduled(cron = "0 0 11 2 * ?")
    private void processFixedRate(){
        Map map=new HashMap();
        String month=DateFormat.beforeMonth();
        map.put("month",month);
        List<Wages> list=wagsService.getWagsList(map);
        if(list!=null){
            for (Wages wages:list) {
                Integer employeeId=wages.getEmployeeId();
                //绩效系数
                Assessment assessment=this.getAssessmentByEmployeeId(month,employeeId+"");
                double comprehensivePerformance=assessment.getComprehensivePerformance();
                //考勤
                int kapqin=assessment.getKaoqin();
                /*-----------------获取基本数据-------------------*/
                double basePay=wages.getBasePay();//基本工资
                double skillPay=wages.getSkillPay();//技能工资
                double positionSalary=wages.getPositionSalary();//职务工资
                double seniorityWage=wages.getSeniorityWage();//工龄工资
                double other=wages.getOther();//其他
                double meritPay=wages.getMeritPay();//绩效工资
                double highTemperatureSubsidy=wages.getHighTemperatureSubsidy();//高温补贴
                double endowmentInsurance=wages.getEndowmentInsurance();//养老保险
                double unemploymentBenefits=wages.getUnemploymentBenefits();//失业金
                double medicalInsurance=wages.getMedicalInsurance();//医疗保险
                double accumulationFund=wages.getAccumulationFund();//公积金
                double otherDeductions=wages.getOtherDeductions();//其他扣款
                double unionFees=wages.getUnionFees();//工会费
                double sixSpecialDeductions=wages.getSixSpecialDeductions();//六项专项扣除

                //工资小计=基本工资+技能工资+职务工资+工龄工资+其他+绩效工资
                BigDecimal bd = new BigDecimal(basePay+skillPay+positionSalary+seniorityWage+other+meritPay);
                double wageSubtotal=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //应发工资=(基本工资+技能工资+职务工资+工龄工资+其他)+绩效工资*绩效系数
                bd = new BigDecimal((basePay+skillPay+positionSalary+seniorityWage+other)+meritPay*comprehensivePerformance);
                double wagesPayable=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //餐补=20*考勤
                double foodSupplement=20*kapqin;
                //补贴小记=高温补贴+餐补
                double subtotalSubsidy=highTemperatureSubsidy+foodSupplement;
                //本期收入:应发合计=工资小计+补贴小记
                bd = new BigDecimal(wageSubtotal+subtotalSubsidy);
                double totalPayable=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //五险一金:扣款合计=养老保险+失业金+医疗保险+公积金+其他扣款+工会费
                bd = new BigDecimal(endowmentInsurance+unemploymentBenefits+medicalInsurance+accumulationFund+otherDeductions+unionFees);
                double totalDeduction=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //计税合计=应发合计-扣款合计
                bd = new BigDecimal(totalPayable-totalDeduction);
                double totalTax=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //************************************ 个调税计算 *****************************************
                Double tax = this.taxCalculator(employeeId+"", month, totalPayable, totalDeduction, sixSpecialDeductions, 5000.00, other);
                //************************************ 实发工资 *****************************************
                Double netSalary = Double.valueOf(totalTax) - tax;
                wages.setWageSubtotal(wageSubtotal);
                wages.setWagesPayable(wagesPayable);
                wages.setFoodSupplement(foodSupplement);
                wages.setTotalPayable(totalPayable);
                wages.setTotalDeduction(totalDeduction);
                wages.setTotalTax(totalTax);
                wages.setNetSalary(netSalary);
                wages.setIndividualTaxAdjustment(tax);
                wages.setPerformanceCoefficient(comprehensivePerformance);
                wagsService.updWags(wages);
            }
        }

    }

    /**
     * 计算净绩效与综合绩效
     * @param cycle
     * @param employeeId
     * @return
     */
    public Assessment getAssessmentByEmployeeId(String cycle,String employeeId) {
        Map map=new HashMap();
        if(cycle!=null&&!cycle.equals("")){
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
            assessment.setComprehensivePerformance(comprehensivePerformance);
        }
        return assessment;
    }

    /**
     * 个税计算
     *
     * @param employeeId           员工id
     * @param month                月份
     * @param totalPable           本期收入
     * @param totalDeduction       五险一金
     * @param sixSpecialDeductions 专项附加扣除
     * @param deductionOfExpenses  减除费用
     * @param other                其他
     * @return wages
     */
    public Double taxCalculator(String employeeId, String month, Double totalPable, Double totalDeduction, Double sixSpecialDeductions, Double deductionOfExpenses, Double other) {
        //累计值
        Double taxableIncome = 0.00;//累计应纳税所得额
        Double currentIncomeTaxTotal = 0.00;//累计收入
        Double deductionOfExpensesTaxTotal = 0.00;//累计减除费用
        Double fiveInsurancesAndOneFundTaxTotal = 0.00;//累计五险一金
        Double otherTaxTotal = 0.00;//累计其他
        Double specialAdditionalDeductionTaxTotal = 0.00;//累计专项扣除
        Double personalIncomeTotalTax = 0.00;//累计个税

        Map map = new HashMap();
        map.put("month", month + "-01");
        map.put("employeeId", employeeId);
        List<Tax> listTax = wagsService.getTaxList(map);
        if (listTax != null) {
            for (Tax tax : listTax) {
                currentIncomeTaxTotal += tax.getCurrentIncome();
                deductionOfExpensesTaxTotal += tax.getDeductionOfExpenses();
                fiveInsurancesAndOneFundTaxTotal += tax.getFiveInsurancesAndOneFund();
                otherTaxTotal += tax.getOther();
                specialAdditionalDeductionTaxTotal += tax.getSpecialAdditionalDeduction();
                //计算累计应纳税所得额
                taxableIncome = currentIncomeTaxTotal - fiveInsurancesAndOneFundTaxTotal - specialAdditionalDeductionTaxTotal - deductionOfExpensesTaxTotal - otherTaxTotal;
                if (taxableIncome < 0) {
                    taxableIncome = 0.00;
                }
                personalIncomeTotalTax += taxableIncome(taxableIncome, personalIncomeTotalTax);
            }
        }
        currentIncomeTaxTotal += totalPable;
        deductionOfExpensesTaxTotal += deductionOfExpenses;
        fiveInsurancesAndOneFundTaxTotal += totalDeduction;
        otherTaxTotal += other;
        specialAdditionalDeductionTaxTotal += sixSpecialDeductions;
        taxableIncome = currentIncomeTaxTotal - fiveInsurancesAndOneFundTaxTotal - specialAdditionalDeductionTaxTotal - deductionOfExpensesTaxTotal - otherTaxTotal;
        if (taxableIncome < 0) {
            taxableIncome = 0.00;
        }
        Double tax = taxableIncome(taxableIncome, personalIncomeTotalTax);
        return tax;
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
}
