package com.zkx.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import com.zkx.reggie.commons.R;
import com.zkx.reggie.pojo.Employee;
import com.zkx.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        String username = employee.getUsername();
        String password = employee.getPassword();

        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);

        Employee emp = employeeService.getOne(wrapper);

        if(!emp.getUsername().equals(username)){
            return R.error("账号不存在");
        }
         else if (!password.equals(emp.getPassword())) {
            return R.error("密码错误");
        } else if (emp.getStatus()==0) {
            return R.error("您被禁用");
        }else{
            request.getSession().setAttribute("employee",emp.getId());
        }
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        String password = "123456";
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        //设置修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //账户默认状态0
        employee.setStatus(0);
        //获取当前新增操作人员的id
        Long empId= (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        //MP自动CRUD的功能，封装好了save方法
        employeeService.save(employee);

        return R.success("插入成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        //分页构造器,Page(第几页, 查几条)
        Page pageInfo = new Page(page, pageSize);
        //查询构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //过滤条件.like(什么条件下启用模糊查询，模糊查询字段，被模糊插叙的名称)
        lambdaQueryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        //添加排序
        lambdaQueryWrapper.orderByDesc(Employee::getCreateTime);
        //查询分页、自动更新
        employeeService.page(pageInfo, lambdaQueryWrapper);
        //返回查询结果
        return R.success(pageInfo);
    }

    @PutMapping()
    public R<String> update(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        System.out.println("更新"+Thread.currentThread().getName());
        //从Request作用域中拿到员工ID
        Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
        //拿新的状态值
        employee.setStatus(employee.getStatus());
        //更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //更新处理人id
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 拿到员工资料，前端自动填充列表，更新的时候复用上面的update方法
     * @param id ResultFul风格传入参数，用@PathVariable来接收同名参数
     * @return 返回员工对象
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("没有查到员工信息");
    }
}
