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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
        Users users=this.getPrincipal();
        String empIdStr="";
        Integer employeeId=null;
        if(month==null||month.equals("")){
            month=DateFormat.ThisMonth();
        }
        if(users!=null){
            employeeId=users.getEmployeeId();
        }
        Map map=new HashMap<>();
        map.put("month",month+"-01");
        //判断是否存在以下角色，
        Subject subject=SecurityUtils.getSubject();
        if(subject.hasRole("财务")||subject.hasRole("总部管理员")){
            empIdStr=null;
        }else{
            List<Employee> rootList=employeeService.getEmployeeByManager(employeeId);
            if(rootList!=null){
                empIdStr+=employeeId+",";
                List<Employee> empList=employeeService.getEmployeeByManager(0);
                for(Employee employee:rootList){
                    empIdStr+=employee.getId()+",";
                    empIdStr+=getUsersId(employee.getId(),empList);
                }
            }
            if(empIdStr!=null&&!empIdStr.equals("")){
                empIdStr=empIdStr.substring(0,empIdStr.lastIndexOf(","));
            }
        }
        map.put("empId",empIdStr);
        List<Wages> list=wagsService.getWagsList(map);
        Result result=new Result();
        result.setCode(0);
        if(list!=null){
            result.setCount(list.size());
            result.setData(list);
        }
        return result;
    }

    public String getUsersId(Integer empId,List<Employee> empList){
        List<String> result=new ArrayList<>();
        String userId="";
        String usersId="";
        for(Employee employee:empList){
            if(employee.getManager()!=null||employee.getManager()!=0){
                if(employee.getManager().equals(empId)){
                    usersId+=employee.getId()+",";
                    result.add(employee.getId()+"");
                }
            }
        }
        for(String str:result){
            String userId1=getUsersId(Integer.parseInt(str),empList);
            if(userId1!=null&&!userId1.equals("")){
                userId+=userId1;
            }
        }
        if(userId!=null&&!userId.equals("null")){
            usersId+=userId;
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
     * @return
     */
    @RequestMapping("/copyToThisMonthWags")
    @ResponseBody
    public String copyToThisMonthWags(HttpServletRequest request){
        String monthStart=request.getParameter("monthStart");
        String monthEnd=request.getParameter("monthEnd");
        Map map=new HashMap<>();
        map.put("month",monthStart+"-01");
        List<Wages> list=wagsService.getWagsList(map);
        if(list!=null){
            List<Wages> wagesList=new ArrayList<>();
            for(Wages wages:list){
                //判断员工是否在职
                Integer empId=wages.getEmployeeId();
                Users user=userService.getUserByEmpId(empId);
                if(user!=null&&user.getState()==1){
                    wages.setDate(monthEnd+"-01");
                    wages.setCreated(monthEnd+"-01");
                    wages.setPerformanceCoefficient(1.00);//设置绩效系数
                    wages.setFoodSupplement(0.00);//餐补
                    wages.setHighTemperatureSubsidy(0.00);//高温补贴
                    wages.setTotalPayable(0.00);//应发合计
                    wages.setTotalDeduction(0.00);//扣款合计
                    wages.setOtherDeductions(0.00);//其他扣款
                    wages.setIndividualTaxAdjustment(0.00);//个调税
                    wages.setNetSalary(0.00);
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
    public String taxation(HttpServletRequest request){
        String month=request.getParameter("month");
        String employeeId=request.getParameter("employeeId");
        String currentIncomeTax=request.getParameter("currentIncomeTax");//本期收入
        String fiveInsurancesAndOneFund=request.getParameter("fiveInsurancesAndOneFund");//五险一金
        String other=request.getParameter("other");//其他
        String specialAdditionalDeduction=request.getParameter("specialAdditionalDeduction");//专项扣除
        String totalTax=request.getParameter("totalTax");//计税合计
        //************************************ 个调税计算 *****************************************
        Double tax=this.taxCalculator(employeeId,month,Double.valueOf(currentIncomeTax),Double.valueOf(fiveInsurancesAndOneFund),Double.valueOf(specialAdditionalDeduction),5000.00,Double.valueOf(0.00));
        //************************************ 实发工资 *****************************************
        Double netSalary=Double.valueOf(totalTax)-tax;//实发工资
        Map map=new HashMap();
        DecimalFormat df=new DecimalFormat("0.00");
        map.put("tax",df.format(tax));
        map.put("netSalary",df.format(netSalary));
        return JSON.toJSONString(map);
    }

    /**
     * 个税计算
     * @param employeeId 员工id
     * @param month 月份
     * @param totalPable 本期收入
     * @param totalDeduction 五险一金
     * @param sixSpecialDeductions 专项附加扣除
     * @param deductionOfExpenses 减除费用
     * @param other 其他
     * @return wages
     */
    public Double taxCalculator(String employeeId,String month,Double totalPable,Double totalDeduction,Double sixSpecialDeductions,Double deductionOfExpenses,Double other){
        //累计值
        Double taxableIncome=0.00;//累计应纳税所得额
        Double currentIncomeTaxTotal=0.00;//累计收入
        Double deductionOfExpensesTaxTotal=0.00;//累计减除费用
        Double fiveInsurancesAndOneFundTaxTotal=0.00;//累计五险一金
        Double otherTaxTotal=0.00;//累计其他
        Double specialAdditionalDeductionTaxTotal=0.00;//累计专项扣除
        Double personalIncomeTotalTax=0.00;//累计个税

        Map map=new HashMap();
        map.put("month",month+"-01");
        map.put("employeeId",employeeId);
        List<Tax> listTax=wagsService.getTaxList(map);
        if(listTax!=null){
            for(Tax tax:listTax){
                currentIncomeTaxTotal+=tax.getCurrentIncome();
                deductionOfExpensesTaxTotal+=tax.getDeductionOfExpenses();
                fiveInsurancesAndOneFundTaxTotal+=tax.getFiveInsurancesAndOneFund();
                otherTaxTotal+=tax.getOther();
                specialAdditionalDeductionTaxTotal+=tax.getSpecialAdditionalDeduction();
                //计算累计应纳税所得额
                taxableIncome=currentIncomeTaxTotal-fiveInsurancesAndOneFundTaxTotal-specialAdditionalDeductionTaxTotal-deductionOfExpensesTaxTotal-otherTaxTotal;
                if(taxableIncome<0){
                    taxableIncome=0.00;
                }
                personalIncomeTotalTax+=taxableIncome(taxableIncome,personalIncomeTotalTax);
            }
        }
        currentIncomeTaxTotal+=totalPable;
        deductionOfExpensesTaxTotal+=deductionOfExpenses;
        fiveInsurancesAndOneFundTaxTotal+=totalDeduction;
        otherTaxTotal+=other;
        specialAdditionalDeductionTaxTotal+=sixSpecialDeductions;
        taxableIncome=currentIncomeTaxTotal-fiveInsurancesAndOneFundTaxTotal-specialAdditionalDeductionTaxTotal-deductionOfExpensesTaxTotal-otherTaxTotal;
        if(taxableIncome<0){
            taxableIncome=0.00;
        }
        Double tax=taxableIncome(taxableIncome,personalIncomeTotalTax);
        return tax;
    }

    /**
     * 根据应纳税所得额计算个税
     * @param taxableIncome
     */
    public Double taxableIncome(Double taxableIncome,Double personalIncomeTotalTax){
        Double taxRate=0.00;//预扣税率
        Double quickCalculationDeduction=0.00;//速算扣除数
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
        if(tax<0){
            tax=0.00;
        }
        return tax;
    }


    /**
     * 修改工资信息
     * @param wages
     * @return
     */
    //@RequiresPermissions(value = {"工资修改"},logical = OR)
    @RequestMapping(value = "/updWages",method = {RequestMethod. POST})
    @ResponseBody
    public String updWags(@RequestBody Wages wages){
        System.out.println(wages.toString());
        int result=wagsService.updWags(wages);
        if(result>0){
            return JSON.toJSONString(Type.SUCCESS);
        }else{
            return JSON.toJSONString(Type.CANCEL);
        }
    }

}
