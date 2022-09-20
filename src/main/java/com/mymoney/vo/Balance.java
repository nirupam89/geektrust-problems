package com.mymoney.vo;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

	private Integer equityAmount;
	private Integer debtAmount;
	private Integer goldAmount;

	public String toString() {
		return equityAmount + StringUtils.SPACE + debtAmount + StringUtils.SPACE + goldAmount;
	}
}
