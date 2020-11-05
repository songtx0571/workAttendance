package com.howei.controller;

import com.alibaba.fastjson.JSON;
import com.howei.pojo.Employee;
import com.howei.pojo.Tax;
import com.howei.pojo.Users;
import com.howei.pojo.Wages;
import com.howei.service.EmployeeService;
import com.howei.service.UserService;
import com.howei.service.WagsService;
import com.howei.util.DateFormat;
import com.howei.util.Result;
import com.howei.util.Type;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/wa/wags")
public class WagsComtroller {

    @Autowired
    private WagsService wagsService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    public Users getPrincipal(){
        Subject subject=SecurityUtils.getSubject();
        Users users=(Users) subject.getPrincipal();
        return users;
    }

    @RequestMapping("/toWags")
    public String toWags(){
        return "wags";
    }

    @RequestMapping("/getWagsList")
    @ResponseBody
    public Result getWagsList(HttpServletRequest request){
        String month=request.getParameter("month");
        if(month==null||month.equals("")){
            month=DateFormat.ThisMonth();
        }
        Map map=new HashMap<>();
        map.put("month",month);
        List<Wages> list=wagsService.getWagsList(map);
        Result result=new Result();
        result.setCode(0);
        if(list!=null){
            result.setCount(list.size());
            result.setData(list);
        }
        return result;
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
    public String copyToThisMonthWags(HttpServletRequest request){
        String monthStart=request.getParameter("monthStart");
        String monthEnd=request.getParameter("monthEnd");
        Map map=new HashMap<>();
        map.put("month",monthStart);
        List<Wages> list=wagsService.getWagsList(map);
        if(list!=null){
            List<Wages> wagesList=new ArrayList<>();
            for(Wages wages:list){
                //判断员工是否在职
                Integer empId=wages.getEmployeeId();
                Users user=userService.getUserByEmpId(empId);
                if(user!=null&&user.getState()==1){
                    wages.setDate(monthEnd);
                    wages.setCreated(monthEnd);
                    wagesList.add(wages);
                }
            }
            int result=wagsService.copyToThisMonthWags(wagesList);
            if(result>0){
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
    public String a(HttpServletRequest request){
        String month=request.getParameter("month");
        String id=request.getParameter("id");
        String currentIncomeTax=request.getParameter("currentIncomeTax");//本期收入
        String fiveInsurancesAndOneFund=request.getParameter("fiveInsurancesAndOneFund");//五险一金
        String other=request.getParameter("other");//其他
        String specialAdditionalDeduction=request.getParameter("specialAdditionalDeduction");//专项扣除
        String totalTax=request.getParameter("totalTax");//计税合计
        Wages wages=wagsService.getWagsListById(id);
        //************************************ 个调税计算 *****************************************
        Double tax=this.taxCalculator(month,Double.valueOf(currentIncomeTax),Double.valueOf(fiveInsurancesAndOneFund),Double.valueOf(specialAdditionalDeduction),5000.00,Double.valueOf(other));
        //************************************ 实发工资 *****************************************
        Double netSalary=Double.valueOf(totalTax)-wages.getIndividualTaxAdjustment();//实发工资
        Map map=new HashMap();
        map.put("tax",tax);
        map.put("netSalary",netSalary);
        return JSON.toJSONString(map);
    }

    /**
     * 个税计算
     * @param month 月份
     * @param totalPable 本期收入
     * @param totalDeduction 五险一金
     * @param sixSpecialDeductions 专项附加扣除
     * @param deductionOfExpenses 减除费用
     * @param other 其他
     * @return wages
     */
    public Double taxCalculator(String month,Double totalPable,Double totalDeduction,Double sixSpecialDeductions,Double deductionOfExpenses,Double other){
        Double taxRate=0.00;//预扣税率
        Double quickCalculationDeduction=0.00;//速算扣除数
        //累计值
        Double taxableIncome=0.00;//累计应纳税所得额
        Double currentIncomeTaxTotal=totalPable;//累计收入
        Double deductionOfExpensesTaxTotal=deductionOfExpenses;//累计减除费用
        Double fiveInsurancesAndOneFundTaxTotal=totalDeduction;//累计五险一金
        Double otherTaxTotal=other;//累计其他
        Double specialAdditionalDeductionTaxTotal=sixSpecialDeductions;//累计专项扣除
        Double personalIncomeTotalTax=0.00;//累计个税

        Map map=new HashMap();
        map.put("month",month+"-01");
        List<Tax> listTax=wagsService.getTaxList(map);
        if(listTax!=null){
            for(Tax tax:listTax){
                currentIncomeTaxTotal+=tax.getCurrentIncome();
                deductionOfExpensesTaxTotal+=tax.getDeductionOfExpenses();
                fiveInsurancesAndOneFundTaxTotal+=tax.getFiveInsurancesAndOneFund();
                otherTaxTotal+=tax.getOther();
                specialAdditionalDeductionTaxTotal+=tax.getSpecialAdditionalDeduction();
                personalIncomeTotalTax+=tax.getPersonalIncomeTax();
            }
        }
        //计算累计应纳税所得额
        taxableIncome=currentIncomeTaxTotal-fiveInsurancesAndOneFundTaxTotal-specialAdditionalDeductionTaxTotal-deductionOfExpensesTaxTotal-otherTaxTotal;
        if(taxableIncome<0){
            taxableIncome=0.00;
        }
        //根据`累计应纳税所得额` 判断 `预扣税率`,`速算扣除数`
        if(taxableIncome<=36000.00){
            taxRate=0.03;
            quickCalculationDeduction=0.00;
        }else if(36000.00<taxableIncome&&taxableIncome<=144000.00){
            taxRate=0.1;
            quickCalculationDeduction=2520.00;
        }else if(144000.00<taxableIncome&&taxableIncome<=300000.00){
            taxRate=0.2;
            quickCalculationDeduction=16920.00;
        }else if(300000.00<taxableIncome&&taxableIncome<=420000.00){
            taxRate=0.25;
            quickCalculationDeduction=31920.00;
        }else if(420000.00<taxableIncome&&taxableIncome<=660000.00){
            taxRate=0.3;
            quickCalculationDeduction=52920.00;
        }else if(660000.00<taxableIncome&&taxableIncome<=960000.00){
            taxRate=0.35;
            quickCalculationDeduction=85920.00;
        }else if(960000.00<taxableIncome){
            taxRate=0.45;
            quickCalculationDeduction=181920.00;
        }
        //累计个税 = 累计应纳税所得额*预扣税率 - 速算扣除数
        Double accumulatedPersonalIncomeTax=taxableIncome*taxRate-quickCalculationDeduction;
        //当月个税
        Double tax=accumulatedPersonalIncomeTax-personalIncomeTotalTax;
        return tax;
    }

    /**
     * 修改工资信息
     * @param wages
     * @return
     */
    @RequestMapping(value = "/updWags",method = {RequestMethod.POST})
    @ResponseBody
    public String updWags(@RequestBody Wages wages){
        int result=wagsService.updWags(wages);
        if(result>0){
            return JSON.toJSONString(Type.SUCCESS);
        }else{
            return JSON.toJSONString(Type.CANCEL);
        }
    }

    /*public void a(){
        //************************************ 工资小计 *****************************************
        //工资小计=基本工资+技能工资+职务工资+工龄工资+其他+绩效工资
        String baseicWages=employee.getBasicwages();//基本工资
        Double baseicWagesDouble=0.00;
        String meritpay=employee.getMeritpay();//绩效工资
        Double meritpayDouble=0.00;
        if(baseicWages!=null&&!baseicWages.equals("")){
            baseicWagesDouble=Double.valueOf(baseicWages);
        }
        wages.setBasePay(Double.valueOf(baseicWagesDouble));//绩效工资
        if(meritpay!=null&&!meritpay.equals("")){
            meritpayDouble=Double.valueOf(meritpay);
        }
        wages.setMeritPay(meritpayDouble);
        Double postitionSalary=employee.getPositionSalary()==null ?0.00:employee.getPositionSalary();//职务工资
        wages.setPositionSalary(postitionSalary);
        Double seniorityWage=employee.getSeniorityWage()==null ?0.00:employee.getSeniorityWage();//工龄工资
        wages.setSeniorityWage(seniorityWage);
        Double skillPay=employee.getSkillPay()==null ?0.00:employee.getSkillPay();//技能工资
        wages.setSkillPay(skillPay);
        Double wageSubtotal=baseicWagesDouble+skillPay+postitionSalary+seniorityWage+meritpayDouble;
        wages.setWageSubtotal(wageSubtotal);//工资小计

        //************************************ 扣款合计 *****************************************
        //扣款合计=养老保险+失业金+医疗保险+公积金+其他扣款+工会费
        Double accumulationFund=employee.getAccumulationFund()==null?0.00:employee.getAccumulationFund();//公积金
        wages.setAccumulationFund(accumulationFund);
        Double endowmentInsurance=employee.getEndowmentInsurance()==null?0.00:employee.getEndowmentInsurance();//养老保险
        wages.setEndowmentInsurance(endowmentInsurance);
        Double medicalInsurance=employee.getMedicalInsurance()==null?0.00:employee.getMedicalInsurance();//医疗保险
        wages.setMedicalInsurance(medicalInsurance);
        Double unemploymentBenefits=employee.getMedicalInsurance()==null?0.00:employee.getMedicalInsurance();//失业金
        wages.setUnemploymentBenefits(unemploymentBenefits);
        Double unionFees=employee.getUnionFees()==null?0.00:employee.getUnionFees();//工会费
        wages.setUnionFees(unionFees);
        Double totalDeduction=endowmentInsurance+unemploymentBenefits+medicalInsurance+accumulationFund+unionFees;//扣款合计
        wages.setTotalDeduction(totalDeduction);//扣款合计

        //************************************ 应发工资 *****************************************
        //(基本工资+技能工资+职务工资+工龄工资+其他)+绩效工资*绩效系数
        Double wagesPayable=(baseicWagesDouble+skillPay+postitionSalary+seniorityWage)+meritpayDouble*performanceCoefficient;//应发工资
        wages.setWagesPayable(wagesPayable);

        //************************************ 应发合计 *****************************************
        //应发合计=工资小计+补贴小记
        Double foodSupplement=employee.getFoodSupplement()==null?0.00:employee.getFoodSupplement();//餐补
        wages.setFoodSupplement(foodSupplement);
        Double highTemperatureSubsidy=0.00;//高温补贴
        wages.setHighTemperatureSubsidy(highTemperatureSubsidy);
        Double totalPable=wageSubtotal+foodSupplement+highTemperatureSubsidy;
        wages.setTotalPayable(totalPable);//应发合计

        //************************************ 计税合计 *****************************************
        //计税合计=应发合计-扣款合计
        Double totalTax=totalPable-totalDeduction;
        wages.setTotalTax(totalTax);//计税合计

    }*/

}
