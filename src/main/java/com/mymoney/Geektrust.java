package com.mymoney;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.mymoney.constants.StringConstant;
import com.mymoney.enums.CommandType;
import com.mymoney.enums.FundType;
import com.mymoney.vo.Balance;

public class Geektrust {

	private static final Logger LOGGER = Logger.getLogger(Geektrust.class.getName());

	public static void main(String[] args) {
		String inputFilePath = args[0];
		
		System.out.println(new Geektrust().parseAndProcessFile(inputFilePath));;
	}

	/**
	 * Takes absolute file path as input.
	 * Returns console output as String.
	 * @param inputFilePath
	 * @return
	 */
	public String parseAndProcessFile(String inputFilePath) {
		Stream<String> lines = null;
		StringBuilder response = new StringBuilder();
		try {
			lines = Files.lines(Paths.get(inputFilePath));

			Balance total = new Balance();
			Balance sip = new Balance();
			Map<FundType, Double> allocationMapping = new HashMap<>();
			Map<String, Balance> monthToBalanceMapping = new HashMap<>();

			lines.filter(StringUtils::isNotBlank).forEach(line -> {
				String[] splits = line.split(StringUtils.SPACE);

				if (CommandType.ALLOCATE.toString().equalsIgnoreCase(splits[0])) {
					total.setEquityAmount(Integer.parseInt(splits[1]));
					total.setDebtAmount(Integer.parseInt(splits[2]));
					total.setGoldAmount(Integer.parseInt(splits[3]));

					int totalAllocation = total.getEquityAmount() + total.getDebtAmount() + total.getGoldAmount();
					allocationMapping.put(FundType.EQUITY, (double) total.getEquityAmount() / totalAllocation);
					allocationMapping.put(FundType.DEBT, (double) total.getDebtAmount() / totalAllocation);
					allocationMapping.put(FundType.GOLD, (double) total.getGoldAmount() / totalAllocation);
				} else if (CommandType.SIP.toString().equalsIgnoreCase(splits[0])) {
					sip.setEquityAmount(Integer.parseInt(splits[1]));
					sip.setDebtAmount(Integer.parseInt(splits[2]));
					sip.setGoldAmount(Integer.parseInt(splits[3]));
				} else if (CommandType.CHANGE.toString().equalsIgnoreCase(splits[0])) {
					Double equityPercent = Double
							.parseDouble(splits[1].replace(StringConstant.PERCENTAGE, StringUtils.EMPTY));
					Double debtPercent = Double
							.parseDouble(splits[2].replace(StringConstant.PERCENTAGE, StringUtils.EMPTY));
					Double goldPercent = Double
							.parseDouble(splits[3].replace(StringConstant.PERCENTAGE, StringUtils.EMPTY));

					total.setEquityAmount(
							total.getEquityAmount() + (int) Math.floor(total.getEquityAmount() * equityPercent / 100));
					total.setDebtAmount(
							total.getDebtAmount() + (int) Math.floor(total.getDebtAmount() * debtPercent / 100));
					total.setGoldAmount(
							total.getGoldAmount() + (int) Math.floor(total.getGoldAmount() * goldPercent / 100));

					Balance balanceTillmonth = new Balance(total.getEquityAmount(), total.getDebtAmount(),
							total.getGoldAmount());

					String month = splits[4];
					monthToBalanceMapping.put(month, balanceTillmonth);

					total.setEquityAmount(total.getEquityAmount() + sip.getEquityAmount());
					total.setDebtAmount(total.getDebtAmount() + sip.getDebtAmount());
					total.setGoldAmount(total.getGoldAmount() + sip.getGoldAmount());

				} else if (CommandType.BALANCE.toString().equalsIgnoreCase(splits[0])) {
					String month = splits[1];
					
					// add to response
					response.append(monthToBalanceMapping.get(month)).append(StringUtils.LF);
				} else if (CommandType.REBALANCE.toString().equalsIgnoreCase(splits[0])) {
					if (monthToBalanceMapping.size() < 6) {
						
						// add to response
						response.append(StringConstant.CANNOT_REBALANCE_MSG).append(StringUtils.LF);
					} else {
						int currentTotalAllocation = total.getEquityAmount() - sip.getEquityAmount()
								+ total.getDebtAmount() - sip.getDebtAmount() + total.getGoldAmount()
								- sip.getGoldAmount();
						total.setEquityAmount(
								(int) Math.floor(currentTotalAllocation * allocationMapping.get(FundType.EQUITY)));
						total.setDebtAmount(
								(int) Math.floor(currentTotalAllocation * allocationMapping.get(FundType.DEBT)));
						total.setGoldAmount(
								(int) Math.floor(currentTotalAllocation * allocationMapping.get(FundType.GOLD)));
						
						// add to response
						response.append(total).append(StringUtils.LF);
					}
				}
			});
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
		
		return response.toString().trim();
	}

}
