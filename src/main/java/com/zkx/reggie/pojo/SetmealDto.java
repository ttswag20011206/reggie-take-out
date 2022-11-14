package com.zkx.reggie.pojo;

import com.zkx.reggie.pojo.Setmeal;
import com.zkx.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
